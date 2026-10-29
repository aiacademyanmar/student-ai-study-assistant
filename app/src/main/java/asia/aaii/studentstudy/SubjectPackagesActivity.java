package asia.aaii.studentstudy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Student AI Study V2.42 native subject subscription catalog.
 * All prices, entitlements, request approvals and expiry are authoritative on the server.
 * No automatic purchase/payment verification is claimed or performed by this screen.
 */
public final class SubjectPackagesActivity extends Activity {
    private static final int BLUE=0xFF235CE9, INK=0xFF192951, MUTED=0xFF667A9F, BG=0xFFF4F7FE;
    private static final String[] TRACK_CODES={"ALL","GCE_O","GCE_A","UNIVERSITY","ENGINEERING","MEDICINE","MANAGEMENT"};
    private static final String[] TRACK_LABELS={"All Subjects","GCE O Level","GCE AS & A Level","Myanmar University","Engineering","Medicine","Management"};
    private final ExecutorService executor=Executors.newSingleThreadExecutor();
    private final ArrayList<JSONObject> subjects=new ArrayList<>();
    private final Map<String,ArrayList<JSONObject>> packages=new HashMap<>();
    private final HashSet<String> entitled=new HashSet<>();
    private String selectedTrack="ALL";
    private LinearLayout root, cards;
    private TextView message, summary;
    private int dp(float n){return Math.round(n*getResources().getDisplayMetrics().density);}
    private GradientDrawable bg(int col,int radius){GradientDrawable d=new GradientDrawable();d.setColor(col);d.setCornerRadius(dp(radius));return d;}
    private TextView label(String content,int sp,boolean bold,int color){
        TextView v=new TextView(this);v.setText(content);v.setTextSize(sp);v.setTextColor(color);
        if(bold)v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return v;
    }
    private LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);return l;}
    private void add(LinearLayout parent,View view,int top){
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);
        p.topMargin=dp(top);parent.addView(view,p);
    }
    private Button button(String title,boolean enabled,Runnable click){
        Button b=new Button(this);b.setText(title);b.setAllCaps(false);
        b.setTextSize(13);b.setTextColor(enabled?Color.WHITE:MUTED);
        b.setBackground(bg(enabled?BLUE:0xFFE8EFFB,12));b.setEnabled(enabled);
        b.setOnClickListener(v->click.run());return b;
    }
    private LinearLayout panel(){
        LinearLayout p=column();p.setPadding(dp(14),dp(14),dp(14),dp(14));
        p.setBackground(bg(Color.WHITE,17));p.setElevation(dp(2));return p;
    }
    private String subjectKey(JSONObject x){return x.optString("track")+"|"+x.optString("subject_code");}
    private String apiUrl(){
        SharedPreferences p=getSharedPreferences("student_study_license_v241",MODE_PRIVATE);
        return p.getString("api_url",BuildConfig.STUDENT_AI_API_URL);
    }
    private JSONObject api(String action, JSONObject request) throws Exception{
        String base=apiUrl();
        if(base==null||!base.startsWith("https://"))throw new Exception("HTTPS Server URL required");
        HttpURLConnection conn=(HttpURLConnection)new URL(base+(base.contains("?")?"&":"?")+"action="+action).openConnection();
        conn.setConnectTimeout(12000);conn.setReadTimeout(18000);
        conn.setRequestProperty("Accept","application/json");
        String token=StudentLicenseActivity.token(this);
        if(!token.isEmpty())conn.setRequestProperty("Authorization","Bearer "+token);
        conn.setRequestProperty("X-Device-ID",StudentLicenseActivity.deviceId(this));
        try{
            if(request!=null){
                conn.setRequestMethod("POST");conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type","application/json; charset=utf-8");
                try(OutputStream out=conn.getOutputStream()){out.write(request.toString().getBytes(StandardCharsets.UTF_8));}
            }
            int status=conn.getResponseCode();
            InputStream body=status>=400?conn.getErrorStream():conn.getInputStream();
            ByteArrayOutputStream bytes=new ByteArrayOutputStream();
            if(body!=null)try(InputStream in=body){
                byte[] buffer=new byte[4096];int n;
                while((n=in.read(buffer))!=-1 && bytes.size()<1000000)bytes.write(buffer,0,n);
            }
            JSONObject response=new JSONObject(new String(bytes.toByteArray(),StandardCharsets.UTF_8));
            if(status<200||status>=300)throw new Exception(response.optString("error","Server HTTP "+status));
            return response;
        }finally{conn.disconnect();}
    }
    @Override public void onCreate(Bundle state){
        super.onCreate(state);
        getWindow().setStatusBarColor(0xFF153C97);
        getWindow().setNavigationBarColor(Color.WHITE);
        ScrollView scroll=new ScrollView(this);
        scroll.setBackgroundColor(BG);scroll.setFillViewport(true);
        root=column();root.setPadding(dp(16),dp(25),dp(16),dp(30));
        scroll.addView(root);setContentView(scroll);
        TextView title=label("Subject Subscriptions",24,true,INK);add(root,title,3);
        add(root,label("GCE O Level • AS & A Level • University • Engineering • Medicine • Management",13,false,MUTED),6);
        add(root,label("ဘာသာရပ်အလိုက် 30 / 90 / 365 days package ကို ရွေးချယ်နိုင်ပါတယ်။",14,false,INK),10);
        summary=label("Loading available subjects…",14,true,BLUE);add(root,summary,14);
        Spinner track=new Spinner(this);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,TRACK_LABELS);
        track.setAdapter(adapter);
        track.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){
            @Override public void onNothingSelected(android.widget.AdapterView<?> p){}
            @Override public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){
                selectedTrack=TRACK_CODES[pos];renderCards();
            }
        });
        add(root,track,14);
        cards=column();add(root,cards,12);
        message=label("Prices are set by Admin. Sending a request does not complete payment.",12,false,MUTED);
        add(root,message,10);
        add(root,button("Refresh Packages & My Subscription",true,this::load),15);
        load();
    }
    private void load(){
        summary.setText("Connecting to Student AI Cloud…");
        executor.execute(()->{
            try{
                JSONObject catalog=api("subject-packages",null);
                JSONObject mine=api("my-subscriptions",null);
                final ArrayList<JSONObject> s=new ArrayList<>();
                final Map<String,ArrayList<JSONObject>> p=new HashMap<>();
                final HashSet<String> access=new HashSet<>();
                JSONArray items=catalog.optJSONArray("subjects");
                if(items!=null)for(int i=0;i<items.length();i++)s.add(items.getJSONObject(i));
                JSONArray list=catalog.optJSONArray("packages");
                if(list!=null)for(int i=0;i<list.length();i++){
                    JSONObject item=list.getJSONObject(i);
                    String k=subjectKey(item);
                    if(!p.containsKey(k))p.put(k,new ArrayList<>());
                    p.get(k).add(item);
                }
                JSONArray entitlements=mine.optJSONArray("subscriptions");
                if(entitlements!=null)for(int i=0;i<entitlements.length();i++){
                    JSONObject e=entitlements.getJSONObject(i);
                    if(e.isNull("revoked_at")&&e.optString("expires_at","").compareTo(
                      new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US).format(new java.util.Date()))>0){
                        access.add(subjectKey(e));
                    }
                }
                boolean legacy=mine.optBoolean("legacy_full_premium",false);
                runOnUiThread(()->{
                    subjects.clear();subjects.addAll(s);
                    packages.clear();packages.putAll(p);
                    entitled.clear();entitled.addAll(access);
                    if(legacy)summary.setText("Full Premium (legacy) · "+subjects.size()+" subjects");
                    else summary.setText(subjects.size()+" subjects · "+entitled.size()+" active subject subscriptions");
                    renderCards();
                });
            }catch(Exception e){runOnUiThread(()->{
                summary.setText("Could not load packages");
                message.setText("Cloud error: "+e.getMessage()+"\nCheck Server URL, Login, and Subject Package migration.");
            });}
        });
    }
    private void renderCards(){
        if(cards==null)return;cards.removeAllViews();int shown=0;
        for(JSONObject s:subjects){
            if(!"ALL".equals(selectedTrack)&&!selectedTrack.equals(s.optString("track")))continue;
            shown++;
            final String k=subjectKey(s);
            LinearLayout box=panel();
            add(box,label(s.optString("title"),17,true,INK),0);
            add(box,label(s.optString("track").replace('_',' ')+" · "+s.optString("subject_code"),12,false,MUTED),4);
            if(entitled.contains(k))add(box,label("✓ Active Subscription",13,true,0xFF118C66),6);
            ArrayList<JSONObject> options=packages.get(k);
            if(options==null||options.isEmpty()){
                add(box,label("Coming soon · Admin has not enabled pricing",12,false,MUTED),10);
            }else{
                for(JSONObject opt:options){
                    final int pid=opt.optInt("package_id");
                    int days=opt.optInt("duration_days");
                    long price=opt.optLong("price_mmk");
                    Button b=button(days+" Days  •  "+String.format(Locale.US,"%,d",price)+" MMK  •  Request",true,()->ask(pid,s,days,price));
                    add(box,b,9);
                }
            }
            add(cards,box,11);
        }
        if(shown==0)add(cards,label(subjects.isEmpty()?"Loading…":"No subjects for this selection.",14,false,MUTED),14);
    }
    private void ask(int packageId,JSONObject subject,int days,long amount){
        new AlertDialog.Builder(this)
            .setTitle("Request Subject Subscription")
            .setMessage(subject.optString("title")+"\n"+days+" Days · "+String.format(Locale.US,"%,d",amount)+" MMK\n\nAdmin will review this request. No money is charged by this app.")
            .setNegativeButton("Cancel",null)
            .setPositiveButton("Send Request",(dialog,which)->{
                message.setText("Sending request…");
                executor.execute(()->{
                    try{
                        JSONObject data=new JSONObject();data.put("package_id",packageId);
                        JSONObject result=api("subject-package-request",data);
                        runOnUiThread(()->{
                            message.setText("Request #"+result.optInt("request_id")+" submitted for Admin review. Payment is NOT confirmed.");
                            Toast.makeText(this,"Subscription request sent",Toast.LENGTH_LONG).show();
                        });
                    }catch(Exception e){runOnUiThread(()->message.setText("Request failed: "+e.getMessage()));}
                });
            }).show();
    }
    @Override protected void onDestroy(){executor.shutdownNow();super.onDestroy();}
}