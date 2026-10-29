package asia.aaii.studentstudy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import android.view.Gravity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * V2.43: Original GCE O / AS&A subject-specialized AI prompt library.
 * Authenticated by the Student AI cloud. Premium prompt text requires a subject
 * entitlement verified by the server; listing does NOT disclose paid content.
 * "Used" means copied via this screen, not exam mastery or verified learning.
 */
public final class GceSpecializedPromptsActivity extends Activity {
    private static final int BLUE=0xFF2458DB, INK=0xFF17305B, MUTED=0xFF627495;
    private final ExecutorService executor=Executors.newSingleThreadExecutor();
    private LinearLayout root, cards;
    private Spinner trackSpinner, subjectSpinner;
    private TextView summary, status;
    private ProgressBar progress;
    private boolean selecting=false;
    private int trackIndex=0;
    private int subjectIndex=0;
    private static final String[] TRACKS={"GCE_O","GCE_A"};
    private int dp(int n){return Math.round(n*getResources().getDisplayMetrics().density);}
    private LinearLayout col(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);return l;}
    private GradientDrawable shape(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    private TextView text(String s,int size,int color,boolean bold) {
        TextView v=new TextView(this);v.setText(s);v.setTextColor(color);v.setTextSize(size);
        if(bold)v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return v;
    }
    private void add(LinearLayout p,View child,int top){
        LinearLayout.LayoutParams layout=new LinearLayout.LayoutParams(-1,-2);
        layout.topMargin=dp(top);p.addView(child,layout);
    }
    private Button button(String title,Runnable call) {
        Button b=new Button(this);b.setAllCaps(false);b.setText(title);b.setTextSize(13);
        b.setTextColor(Color.WHITE);b.setBackground(shape(BLUE,12));b.setOnClickListener(v->call.run());
        return b;
    }
    private String code(){
        if(trackIndex==0)return OLevel2026.SUBJECTS[subjectIndex].code;
        return ALevel2026.SUBJECTS[subjectIndex].code;
    }
    private String subjectName(){
        if(trackIndex==0)return OLevel2026.SUBJECTS[subjectIndex].name;
        return ALevel2026.SUBJECTS[subjectIndex].name;
    }
    private String url(){return getSharedPreferences("student_study_license_v241",MODE_PRIVATE)
        .getString("api_url",BuildConfig.STUDENT_AI_API_URL);}
    private JSONObject http(String action,String args,JSONObject data) throws Exception {
        String endpoint=url();
        if(endpoint==null||!endpoint.startsWith("https://"))throw new Exception("HTTPS cloud URL required");
        String path=endpoint+(endpoint.contains("?")?"&":"?")+"action="+action+(args==null?"":args);
        HttpURLConnection conn=(HttpURLConnection)new URL(path).openConnection();
        conn.setConnectTimeout(12000);conn.setReadTimeout(18000);conn.setUseCaches(false);
        conn.setRequestProperty("Accept","application/json");
        conn.setRequestProperty("X-Device-ID",StudentLicenseActivity.deviceId(this));
        String token=StudentLicenseActivity.token(this);
        if(token.isEmpty())throw new Exception("Student login required");
        conn.setRequestProperty("Authorization","Bearer "+token);
        try{
            if(data!=null){
                conn.setRequestMethod("POST");conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type","application/json; charset=utf-8");
                try(OutputStream out=conn.getOutputStream()) {
                    out.write(data.toString().getBytes(StandardCharsets.UTF_8));
                }
            }
            int statusCode=conn.getResponseCode();
            InputStream stream=statusCode>=400?conn.getErrorStream():conn.getInputStream();
            ByteArrayOutputStream bytes=new ByteArrayOutputStream();
            if(stream!=null)try(InputStream input=stream){
                byte[] b=new byte[4096];int n;
                while((n=input.read(b))!=-1&&bytes.size()<300000)bytes.write(b,0,n);
            }
            JSONObject response=new JSONObject(new String(bytes.toByteArray(),StandardCharsets.UTF_8));
            if(statusCode<200||statusCode>=300)
                throw new Exception(response.optString("error","HTTP "+statusCode));
            return response;
        }finally{conn.disconnect();}
    }
    @Override public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getWindow().setStatusBarColor(0xFF142D83);
        ScrollView scrolling=new ScrollView(this);scrolling.setFillViewport(true);
        scrolling.setBackgroundColor(0xFFF4F7FE);
        root=col();root.setPadding(dp(16),dp(20),dp(16),dp(30));
        scrolling.addView(root);setContentView(scrolling);
        add(root,text("✦ Specialized AI Prompt Library",24,INK,true),8);
        add(root,text("GCE O Level • GCE AS & A Level\nမြန်မာလို သင်ကြားရေး Prompt များ",14,MUTED,false),8);
        add(root,text("Step 1 — Choose exam level",14,INK,true),17);
        trackSpinner=new Spinner(this);
        ArrayAdapter<String> tracks=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,
            new String[]{"GCE O Level (12 subjects)","GCE AS & A Level (12 subjects)"});
        trackSpinner.setAdapter(tracks);add(root,trackSpinner,5);
        add(root,text("Step 2 — Select subject",14,INK,true),12);
        subjectSpinner=new Spinner(this);add(root,subjectSpinner,5);
        summary=text("Loading subject prompt list…",14,BLUE,true);add(root,summary,12);
        progress=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        progress.setMax(100);add(root,progress,9);
        status=text("Completed = prompt copied/used; it is not an exam score.",12,MUTED,false);
        add(root,status,8);
        cards=col();add(root,cards,12);
        add(root,button("Refresh Subject Prompts",this::load),15);
        add(root,button("Manage Subject Subscription",()->startActivity(
            new Intent(this,SubjectPackagesActivity.class))),9);
        trackSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){
            @Override public void onNothingSelected(android.widget.AdapterView<?> p){}
            @Override public void onItemSelected(android.widget.AdapterView<?> parent,View view,int pos,long id){
                trackIndex=pos;subjectIndex=0;setupSubjects();
            }
        });
        subjectSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){
            @Override public void onNothingSelected(android.widget.AdapterView<?> p){}
            @Override public void onItemSelected(android.widget.AdapterView<?> parent,View view,int pos,long id){
                subjectIndex=pos;
                if(!selecting)load();
            }
        });
    }
    private void setupSubjects(){
        selecting=true;
        String[] labels;
        if(trackIndex==0){
            labels=new String[OLevel2026.SUBJECTS.length];
            for(int i=0;i<labels.length;i++)labels[i]=OLevel2026.SUBJECTS[i].name+
                    " ("+OLevel2026.SUBJECTS[i].code+")";
        }else{
            labels=new String[ALevel2026.SUBJECTS.length];
            for(int i=0;i<labels.length;i++)labels[i]=ALevel2026.SUBJECTS[i].name+
                    " ("+ALevel2026.SUBJECTS[i].code+")";
        }
        subjectSpinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,labels));
        subjectIndex=0;selecting=false;load();
    }
    private void load(){
        if(cards==null)return;
        final String track=TRACKS[trackIndex],subject=code(),title=subjectName();
        summary.setText(title+" · Loading…");
        executor.execute(()->{
            try{
                JSONObject response=http("subject-prompts","&track="+track+"&subject_code="+subject,null);
                runOnUiThread(()->{
                    if(!TRACKS[trackIndex].equals(track)||!code().equals(subject))return;
                    int total=response.optInt("total");
                    int used=response.optInt("used_count");
                    boolean paid=response.optBoolean("premium_active");
                    summary.setText(title+" · "+used+"/"+total+" Used ("+
                        response.optDouble("completion_percent",0)+"%)"+
                        (paid?" · Subject Premium Active":" · Free Preview"));
                    progress.setProgress(Math.max(0,Math.min(100,
                        (int)Math.round(response.optDouble("completion_percent",0)))));
                    showCards(response.optJSONArray("templates"));
                });
            }catch(Exception e){runOnUiThread(()->{
                summary.setText("Cloud data unavailable");cards.removeAllViews();
                status.setText("API Error: "+e.getMessage()+
                    "\nDeploy the V2.43 server upgrade; check Student Login and Subscription.");
            });}
        });
    }
    private void showCards(JSONArray list){
        cards.removeAllViews();
        if(list==null||list.length()==0){
            add(cards,text("Subject prompt packs are not published on this server yet.",13,MUTED,false),8);
            return;
        }
        for(int i=0;i<list.length();i++){
            JSONObject item=list.optJSONObject(i);
            if(item==null)continue;
            final int id=item.optInt("id");
            boolean available=item.optBoolean("available",false);
            boolean used=item.optBoolean("used",false);
            LinearLayout pane=col();pane.setPadding(dp(15),dp(13),dp(15),dp(13));
            pane.setBackground(shape(Color.WHITE,17));pane.setElevation(dp(2));
            add(pane,text((i+1)+". "+item.optString("title"),16,INK,true),2);
            add(pane,text(item.optString("description"),12,MUTED,false),4);
            String state=used?"✓ USED":"NOT USED";
            add(pane,text((available?"✓ ACCESS":"🔒 PREMIUM")+"    ·    "+state,12,
                available?0xFF0C906E:0xFFB66A19,true),6);
            add(pane,button(available?"View & Copy Prompt":"View Subscription Options",
                available?()->getPrompt(id):()->startActivity(new Intent(this,SubjectPackagesActivity.class))),9);
            add(cards,pane,10);
        }
    }
    private void getPrompt(int id){
        status.setText("Loading prompt…");
        executor.execute(()->{
            try{
                JSONObject result=http("subject-prompt","&id="+id,null);
                JSONObject prompt=result.getJSONObject("prompt");
                final String value=prompt.optString("prompt_text","");
                final String title=prompt.optString("title","Specialized Prompt");
                runOnUiThread(()->{
                    ScrollView scroll=new ScrollView(this);
                    TextView content=text(value,13,INK,false);
                    content.setPadding(dp(16),dp(16),dp(16),dp(16));
                    scroll.addView(content);
                    AlertDialog dialog=new AlertDialog.Builder(this)
                        .setTitle(title).setView(scroll)
                        .setNegativeButton("Close",null)
                        .setPositiveButton("Copy Prompt",null).create();
                    dialog.setOnShowListener(d->dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(v->{
                            ClipboardManager manager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                            manager.setPrimaryClip(ClipData.newPlainText("Student AI GCE prompt",value));
                            status.setText("Prompt copied. Use progress recorded as an action, not mastery.");
                            dialog.dismiss();markUsed(id);
                        }));
                    dialog.show();
                });
            }catch(Exception e){runOnUiThread(()->status.setText("Prompt unavailable: "+e.getMessage()));}
        });
    }
    private void markUsed(int id){
        executor.execute(()->{
            try{
                JSONObject body=new JSONObject();body.put("prompt_id",id);
                http("prompt-progress",null,body);
                runOnUiThread(this::load);
            }catch(Exception e){runOnUiThread(()->status.setText(
                "Copied locally; cloud progress not saved: "+e.getMessage()));}
        });
    }
    @Override protected void onDestroy(){executor.shutdownNow();super.onDestroy();}
}