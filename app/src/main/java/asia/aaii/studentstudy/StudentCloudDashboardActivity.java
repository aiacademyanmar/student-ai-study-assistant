package asia.aaii.studentstudy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * V2.45 authenticated read-only Cloud Dashboard.
 * Prompt progress reflects COPIED prompts, never academic completion or grades.
 */
public final class StudentCloudDashboardActivity extends Activity {
  private final ExecutorService executor=Executors.newSingleThreadExecutor();
  private LinearLayout root, trackCards;
  private TextView headline, serverStatus;
  private ProgressBar totalProgress;
  private static final int INK=0xFF182F5A,BLUE=0xFF2559D7,MUTED=0xFF687A9B;
  private int dp(int n){return Math.round(getResources().getDisplayMetrics().density*n);}
  private LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(1);return l;}
  private TextView label(String value,int size,boolean bold,int color){
    TextView v=new TextView(this);v.setText(value);v.setTextColor(color);v.setTextSize(size);
    if(bold)v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return v;
  }
  private GradientDrawable background(int color,int radius){
    GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;
  }
  private void add(LinearLayout container,View view,int top){
    LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(top);container.addView(view,p);
  }
  private Button button(String text,Runnable handler){
    Button b=new Button(this);b.setText(text);b.setTextSize(14);b.setAllCaps(false);
    b.setTextColor(Color.WHITE);b.setBackground(background(BLUE,15));b.setOnClickListener(v->handler.run());return b;
  }
  private String url(){
    return getSharedPreferences("student_study_license_v241",MODE_PRIVATE)
      .getString("api_url",BuildConfig.STUDENT_AI_API_URL);
  }
  private JSONObject request(String action) throws Exception {
    String endpoint=url(),token=StudentLicenseActivity.token(this);
    if(endpoint==null||!endpoint.startsWith("https://"))throw new Exception("HTTPS API address required");
    if(token.isEmpty())throw new Exception("Activation/Login required");
    HttpURLConnection connection=(HttpURLConnection)new URL(endpoint+
      (endpoint.contains("?")?"&":"?")+"action="+action).openConnection();
    connection.setRequestProperty("Authorization","Bearer "+token);
    connection.setRequestProperty("X-Device-ID",StudentLicenseActivity.deviceId(this));
    connection.setRequestProperty("Accept","application/json");
    connection.setConnectTimeout(12000);connection.setReadTimeout(15000);
    try{
      int status=connection.getResponseCode();
      InputStream source=status>=400?connection.getErrorStream():connection.getInputStream();
      ByteArrayOutputStream bytes=new ByteArrayOutputStream();
      if(source!=null)try(InputStream in=source){
        byte[] chunk=new byte[4096];int n;
        while((n=in.read(chunk))!=-1&&bytes.size()<200000)bytes.write(chunk,0,n);
      }
      JSONObject data=new JSONObject(new String(bytes.toByteArray(),StandardCharsets.UTF_8));
      if(status<200||status>=300)throw new Exception(data.optString("error","HTTP "+status));
      return data;
    }finally{connection.disconnect();}
  }
  @Override public void onCreate(Bundle saved){
    super.onCreate(saved);
    getWindow().setStatusBarColor(0xFF17368A);
    getWindow().setNavigationBarColor(Color.WHITE);
    ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);
    scroll.setBackgroundColor(0xFFF4F7FF);
    root=column();root.setPadding(dp(16),dp(25),dp(16),dp(28));
    scroll.addView(root);setContentView(scroll);
    add(root,label("My Study Cloud Dashboard",23,true,INK),3);
    add(root,label("Student AI V2.45 • Cloud & Subscription Diagnostics",13,false,MUTED),8);
    headline=label("Connecting to server...",17,true,BLUE);add(root,headline,16);
    totalProgress=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
    totalProgress.setMax(100);add(root,totalProgress,12);
    add(root,label("Progress = prompt copy/usage count, not knowledge mastery or exam completion.",12,false,MUTED),8);
    serverStatus=label("Authenticating Student AI Cloud...",12,false,INK);add(root,serverStatus,12);
    add(root,label("Progress by Subject Category",18,true,INK),20);
    trackCards=column();add(root,trackCards,7);
    add(root,button("Refresh My Study Progress",this::load),15);
    add(root,button("My Subject Subscription Packages",()->
      startActivity(new Intent(this,SubjectPackagesActivity.class))),8);
    add(root,button("GCE O / AS & A Prompt Library",()->
      startActivity(new Intent(this,GceSpecializedPromptsActivity.class))),8);
    add(root,button("University & Professional Prompt Library",()->
      startActivity(new Intent(this,UniversitySpecializedPromptsActivity.class))),8);
    load();
  }
  private void load(){
    headline.setText("Checking API and private study progress...");
    executor.execute(()->{
      try{
        JSONObject dashboard=request("subject-dashboard");
        JSONObject status=request("cloud-status");
        String version=dashboard.optString("server_version","unknown");
        JSONObject totals=dashboard.getJSONObject("totals");
        JSONArray tracks=dashboard.optJSONArray("tracks");
        JSONObject quota=dashboard.optJSONObject("ai_quota");
        JSONObject features=status.optJSONObject("features");
        runOnUiThread(()->{
          int count=totals.optInt("prompts");
          int copies=totals.optInt("copied_prompts");
          double percent=totals.optDouble("copied_percent",0.0);
          headline.setText(String.format(Locale.US,"%d / %d prompts copied · %.1f%%",copies,count,percent));
          totalProgress.setProgress(Math.max(0,Math.min(100,(int)Math.round(percent))));
          String ai=features!=null&&features.optBoolean("ai_tutor")?"Configured":"Not configured";
          serverStatus.setText("API V"+version+" · "+
            dashboard.optString("membership_plan","free").toUpperCase(Locale.US)+
            " License\nSpecialized AI Provider: "+ai+
            "\nAI quota today: "+(quota==null?"unknown":quota.optInt("used")+" / "+quota.optInt("limit"))+
            "\nPublished catalog: "+count+" prompts"+
            (count==910?" (expected 910)":" (server catalog incomplete; check migration)"));
          drawTracks(tracks);
        });
      }catch(Exception e){runOnUiThread(()->{
        headline.setText("Cloud connection not verified");totalProgress.setProgress(0);
        serverStatus.setText("Error: "+e.getMessage()+
          "\nCheck account, HTTPS API URL, deployment and V2.45 migration preflight.");
        trackCards.removeAllViews();
      });}
    });
  }
  private void drawTracks(JSONArray tracks){
    trackCards.removeAllViews();
    if(tracks==null)return;
    for(int i=0;i<tracks.length();i++){
      JSONObject t=tracks.optJSONObject(i);
      if(t==null)continue;
      LinearLayout card=column();card.setPadding(dp(14),dp(12),dp(14),dp(13));
      card.setBackground(background(Color.WHITE,16));card.setElevation(dp(2));
      String name=t.optString("track").replace('_',' ');
      int used=t.optInt("copied_prompts"),max=t.optInt("prompts");
      double pct=t.optDouble("copied_percent");
      add(card,label(name+" · "+t.optInt("subjects")+" Subjects",15,true,INK),2);
      add(card,label(used+" / "+max+" prompts copied · "+
        t.optInt("active_subjects")+" active subject subscriptions",12,false,MUTED),6);
      ProgressBar bar=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
      bar.setMax(100);bar.setProgress(Math.max(0,Math.min(100,(int)Math.round(pct))));
      add(card,bar,10);
      add(trackCards,card,10);
    }
  }
  @Override protected void onDestroy(){executor.shutdownNow();super.onDestroy();}
}
