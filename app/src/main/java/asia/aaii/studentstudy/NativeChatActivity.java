package asia.aaii.studentstudy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Independent native Android chat client. All AI requests go to a user-configured HTTPS
 * backend; the OpenAI API key must NEVER be shipped in an APK.
 */
public class NativeChatActivity extends Activity {
    private static final int NAVY=0xFF102453, BLUE=0xFF1264EE, MUTED=0xFF6A7895, BG=0xFFF2F7FF;
    private static final int WHITE=Color.WHITE, BORDER=0xFFDFE8F7, GREEN=0xFF149A77;
    private LinearLayout root, thread, footer;
    private ScrollView scroll;
    private EditText composer;
    private TextView sendButton, status;
    private JSONArray messages = new JSONArray();
    private SharedPreferences preferences;
    private boolean sending=false;
    private String conversationKey="conversation";
    private String sessionTitle="Native AI Chat · မြန်မာဘာသာ";

    private int dp(float n){return (int)(getResources().getDisplayMetrics().density*n+.5f);}
    private GradientDrawable shape(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    private TextView text(String s,int size,int color,boolean bold){
        TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);
        if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;
    }
    private LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);return l;}
    private LinearLayout row(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.HORIZONTAL);l.setGravity(Gravity.CENTER_VERTICAL);return l;}
    private void pad(View v,int l,int t,int r,int b){v.setPadding(dp(l),dp(t),dp(r),dp(b));}
    private TextView tap(String value,int color,int background,Runnable action){
        TextView t=text(value,13,color,true);t.setGravity(Gravity.CENTER);pad(t,12,12,12,12);
        t.setBackground(shape(background,14));t.setOnClickListener(v->action.run());return t;
    }
    private void add(LinearLayout parent,View child,int top,int bottom){
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);
        p.topMargin=dp(top);p.bottomMargin=dp(bottom);parent.addView(child,p);
    }

    @Override public void onCreate(Bundle state){
        super.onCreate(state);
        preferences=getSharedPreferences("student_ai_chat_v23",MODE_PRIVATE);
        String session=getIntent().getStringExtra("chat_session");
        if(session!=null&&session.matches("[a-zA-Z0-9_]{1,100}"))conversationKey="conversation_"+session;
        String title=getIntent().getStringExtra("chat_title");
        if(title!=null&&!title.trim().isEmpty())sessionTitle=title;
        try{messages=new JSONArray(preferences.getString(conversationKey,"[]"));}catch(Exception ignored){messages=new JSONArray();}
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(WHITE);
        getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        render();
    }

    private void render(){
        root=column();root.setBackgroundColor(BG);setContentView(root);
        if(Build.VERSION.SDK_INT>=35){
            root.setOnApplyWindowInsetsListener((v,insets)->{
                android.graphics.Insets bars=insets.getInsets(WindowInsets.Type.systemBars()|WindowInsets.Type.ime());
                v.setPadding(bars.left,bars.top,bars.right,bars.bottom);return insets;
            });
        }
        LinearLayout top=row();pad(top,16,12,16,12);top.setBackgroundColor(WHITE);
        TextView back=tap("‹",BLUE,0xFFE8F1FF,()->go("home"));top.addView(back);
        LinearLayout titles=column();pad(titles,12,0,0,0);
        titles.addView(text("✦ AI ဆရာ",20,NAVY,true));
        titles.addView(text(sessionTitle,11,MUTED,false));
        top.addView(titles,new LinearLayout.LayoutParams(0,-2,1));
        top.addView(tap("⚙",BLUE,0xFFE8F1FF,()->settings()));
        top.addView(tap("↺",MUTED,0xFFE8F1FF,()->confirmClear()));
        root.addView(top,new LinearLayout.LayoutParams(-1,-2));

        status=text("",12,MUTED,false);pad(status,17,6,17,7);root.addView(status);
        refreshStatus();

        scroll=new ScrollView(this);scroll.setFillViewport(true);
        thread=column();pad(thread,13,12,13,13);scroll.addView(thread);
        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        drawHistory();

        LinearLayout entry=column();entry.setBackgroundColor(WHITE);pad(entry,11,9,11,8);
        if(messages.length()==0){
            LinearLayout hints=row();
            String[] prompts={"သင်္ချာ ရှင်းပြပါ","Essay စစ်ပေးပါ","Quiz မေးပါ"};
            for(String prompt:prompts){
                TextView chip=tap(prompt,BLUE,0xFFE9F2FF,()->composer.setText(prompt));
                LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(0,-2,1);
                cp.setMargins(dp(2),0,dp(2),dp(6));hints.addView(chip,cp);
            }
            entry.addView(hints);
        }
        LinearLayout input=row();
        composer=new EditText(this);
        composer.setHint("သိချင်တာကို မြန်မာလို ရေးမေးပါ...");
        composer.setTextColor(NAVY);composer.setHintTextColor(MUTED);
        composer.setTextSize(15);composer.setMinLines(1);composer.setMaxLines(4);
        composer.setBackground(shape(0xFFF2F6FF,16));pad(composer,13,9,11,9);
        input.addView(composer,new LinearLayout.LayoutParams(0,-2,1));
        sendButton=tap("ပို့ရန် ➤",WHITE,BLUE,()->send());
        LinearLayout.LayoutParams sendParams=new LinearLayout.LayoutParams(-2,dp(49));
        sendParams.leftMargin=dp(8);input.addView(sendButton,sendParams);
        entry.addView(input);root.addView(entry,new LinearLayout.LayoutParams(-1,-2));
        footer();
        String draft=getIntent().getStringExtra("draft");
        if(draft!=null&&!draft.trim().isEmpty())composer.setText(draft);
    }

    private void refreshStatus(){
        if(status==null)return;
        boolean ready=!preferences.getString("backend_url","").trim().isEmpty();
        status.setText(ready?"● Server ချိတ်ဆက်ရန် အသင့် · AI မေးခွန်းပို့နိုင်သည်":"● AI Server မချိတ်ရသေး · ⚙ တွင် HTTPS Backend ထည့်ပါ");
        status.setTextColor(ready?GREEN:0xFFB2661E);
    }

    private void drawHistory(){
        thread.removeAllViews();
        if(messages.length()==0){
            LinearLayout welcome=column();welcome.setGravity(Gravity.CENTER);
            TextView emoji=text("🤖",54,BLUE,true);emoji.setGravity(Gravity.CENTER);add(welcome,emoji,28,8);
            TextView title=text("AI ဆရာနဲ့ အတူလေ့လာမယ်",21,NAVY,true);title.setGravity(Gravity.CENTER);add(welcome,title,0,9);
            TextView sub=text("မေးခွန်းမေးပါ၊ အဆင့်လိုက်ရှင်းပြချက်ရယူပါ။ AI ဖြေဆိုမှုအတွက် ကိုယ်ပိုင် HTTPS Server ချိတ်ဆက်ရန်လိုအပ်သည်။",14,MUTED,false);
            sub.setGravity(Gravity.CENTER);pad(sub,10,4,10,5);add(welcome,sub,0,12);
            add(welcome,tap("Server ချိတ်ဆက်ရန်",WHITE,BLUE,()->settings()),0,5);
            thread.addView(welcome);
        }else{
            for(int i=0;i<messages.length();i++){
                JSONObject item=messages.optJSONObject(i);if(item==null)continue;
                boolean user="user".equals(item.optString("role"));
                LinearLayout line=row();line.setGravity(user?Gravity.RIGHT:Gravity.LEFT);
                TextView bubble=text(item.optString("content"),15,user?WHITE:NAVY,false);
                bubble.setLineSpacing(dp(3),1.05f);pad(bubble,14,11,14,11);
                bubble.setBackground(shape(user?BLUE:WHITE,16));
                bubble.setMaxWidth((int)(getResources().getDisplayMetrics().widthPixels*.83f));
                LinearLayout.LayoutParams bp=new LinearLayout.LayoutParams(-2,-2);
                bp.topMargin=dp(7);bp.bottomMargin=dp(7);line.addView(bubble,bp);
                thread.addView(line);
            }
        }
        if(scroll!=null)scroll.post(()->scroll.fullScroll(View.FOCUS_DOWN));
    }

    private void record(String role,String content){
        JSONObject item=new JSONObject();
        try{item.put("role",role);item.put("content",content);}catch(Exception ignored){}
        messages.put(item);
        if(messages.length()>40){
            JSONArray trimmed=new JSONArray();
            for(int i=messages.length()-40;i<messages.length();i++)trimmed.put(messages.opt(i));
            messages=trimmed;
        }
        preferences.edit().putString(conversationKey,messages.toString()).apply();
        drawHistory();
    }

    private void send(){
        if(sending)return;
        String msg=composer.getText().toString().trim();
        if(msg.isEmpty())return;
        if(msg.length()>6000){composer.setError("စာလုံး ၆၀၀၀ အောက် ရေးပါ");return;}
        String endpoint=preferences.getString("backend_url","").trim();
        if(!endpoint.startsWith("https://")){
            Toast.makeText(this,"အရင်ဆုံး HTTPS AI Server ချိတ်ဆက်ပါ",Toast.LENGTH_LONG).show();settings();return;
        }
        composer.setText("");record("user",msg);
        sending=true;sendButton.setEnabled(false);sendButton.setAlpha(.5f);
        status.setText("AI ဆရာ ဖြေဆိုနေပါတယ်...");
        String token=preferences.getString("app_token","").trim();
        JSONArray history=new JSONArray();
        for(int i=Math.max(0,messages.length()-14);i<messages.length();i++)history.put(messages.opt(i));
        new Thread(()->{
            String answer=null,error=null;
            HttpURLConnection conn=null;
            try{
                URL url=new URL(endpoint);
                if(!"https".equalsIgnoreCase(url.getProtocol()))throw new Exception("HTTPS လိုအပ်ပါတယ်");
                conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(12000);conn.setReadTimeout(60000);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type","application/json; charset=utf-8");
                conn.setRequestProperty("Accept","application/json");
                if(!token.isEmpty())conn.setRequestProperty("Authorization","Bearer "+token);
                JSONObject payload=new JSONObject();payload.put("message",msg);payload.put("history",history);
                payload.put("language","my");
                try(OutputStream out=conn.getOutputStream()){out.write(payload.toString().getBytes(StandardCharsets.UTF_8));}
                int code=conn.getResponseCode();
                InputStream stream=(code>=200&&code<300)?conn.getInputStream():conn.getErrorStream();
                StringBuilder response=new StringBuilder();
                if(stream!=null){
                    try(BufferedReader reader=new BufferedReader(new InputStreamReader(stream,StandardCharsets.UTF_8))){
                        String line;while((line=reader.readLine())!=null && response.length()<300000)response.append(line);
                    }
                }
                if(code<200||code>=300)throw new Exception("Server HTTP "+code+" · Backend configuration ကို စစ်ပါ");
                JSONObject data=new JSONObject(response.toString());
                answer=data.optString("reply",data.optString("answer","")).trim();
                if(answer.isEmpty())throw new Exception("Server response တွင် reply မပါရှိပါ");
            }catch(Exception ex){error=ex.getMessage();}
            finally{if(conn!=null)conn.disconnect();}
            final String finalAnswer=answer,finalError=error;
            runOnUiThread(()->{
                sending=false;sendButton.setEnabled(true);sendButton.setAlpha(1);
                if(finalAnswer!=null&&!finalAnswer.isEmpty())record("assistant",finalAnswer);
                else{
                    composer.setText(msg);
                    Toast.makeText(this,"AI မဖြေဆိုနိုင်ပါ: "+finalError,Toast.LENGTH_LONG).show();
                }
                refreshStatus();
            });
        }).start();
    }

    private void settings(){
        LinearLayout controls=column();pad(controls,17,5,17,0);
        TextView note=text("ကိုယ်ပိုင် HTTPS AI Backend URL နှင့် Student Access Token ကိုထည့်ပါ။ OpenAI API Key ကို ဒီ App ထဲ မထည့်ပါနှင့်။",13,MUTED,false);
        add(controls,note,0,10);
        EditText address=new EditText(this);address.setSingleLine(true);
        address.setTextSize(14);address.setHint("https://your-domain.com/api/ai-chat.php");
        address.setText(preferences.getString("backend_url",""));
        add(controls,address,0,9);
        EditText token=new EditText(this);token.setSingleLine(true);token.setTextSize(14);
        token.setHint("Student access token (optional)");
        token.setInputType(129);
        token.setText(preferences.getString("app_token",""));
        add(controls,token,0,4);
        new AlertDialog.Builder(this).setTitle("AI Server Settings").setView(controls)
            .setPositiveButton("သိမ်းဆည်းရန်",(dialog,which)->{
                String url=address.getText().toString().trim();
                if(!url.isEmpty() && !url.startsWith("https://")){
                    Toast.makeText(this,"HTTPS URL သာ ခွင့်ပြုပါသည်",Toast.LENGTH_LONG).show();return;
                }
                if(url.contains("api.openai.com")){
                    Toast.makeText(this,"OpenAI API ကို App ကနေ တိုက်ရိုက်မခေါ်ပါနှင့်။ ကိုယ်ပိုင် Backend သုံးပါ",Toast.LENGTH_LONG).show();return;
                }
                preferences.edit().putString("backend_url",url)
                    .putString("app_token",token.getText().toString().trim()).apply();
                refreshStatus();
            }).setNegativeButton("ပယ်ဖျက်",null).show();
    }

    private void confirmClear(){
        new AlertDialog.Builder(this).setTitle("စကားဝိုင်းများ ရှင်းမလား?")
            .setMessage("ဖုန်းထဲမှာ သိမ်းထားသော AI စကားဝိုင်းများကို ဖျက်မည်။")
            .setPositiveButton("ရှင်းမည်",(d,w)->{
                messages=new JSONArray();preferences.edit().remove(conversationKey).apply();render();
            }).setNegativeButton("မလုပ်တော့",null).show();
    }

    private void footer(){
        footer=column();footer.setBackgroundColor(WHITE);
        View line=new View(this);line.setBackgroundColor(BORDER);
        footer.addView(line,new LinearLayout.LayoutParams(-1,dp(1)));
        LinearLayout bar=row();
        String[] labels={"ပင်မ","စကားဝိုင်း","AI ဆရာ","သင်တန်း","ပိုမို"};
        String[] icons={"⌂","▤","✦","▣","☰"};
        String[] routes={"home","list","tutor","courses","profile"};
        for(int i=0;i<routes.length;i++){
            final String dest=routes[i],label=labels[i];
            boolean active=i==2;
            LinearLayout cell=column();cell.setGravity(Gravity.CENTER);
            TextView icon=text(icons[i],22,active?WHITE:MUTED,true);
            icon.setGravity(Gravity.CENTER);
            if(active){icon.setBackground(shape(BLUE,15));pad(icon,10,2,10,2);}
            cell.addView(icon);
            TextView title=text(label,10,active?BLUE:MUTED,active);title.setGravity(Gravity.CENTER);
            cell.addView(title);cell.setContentDescription(label);
            cell.setOnClickListener(v->go(dest));
            bar.addView(cell,new LinearLayout.LayoutParams(0,dp(66),1));
        }
        footer.addView(bar);root.addView(footer,new LinearLayout.LayoutParams(-1,-2));
    }
    private void go(String destination){
        if("tutor".equals(destination))return;
        Intent next;
        if("courses".equals(destination)){AcademyBrowser.openCourses(this);return;}
        else {
            next=new Intent(this,CatalogActivity.class);
            next.putExtra("open_page",destination);
        }
        startActivity(next);finish();
    }
    @Override public void onBackPressed(){go("home");}
}
