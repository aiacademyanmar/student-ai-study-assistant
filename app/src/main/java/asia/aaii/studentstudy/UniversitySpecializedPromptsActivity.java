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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
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
 * Student AI Study V2.44 -- subject-specialized prompts for university / professional tracks.
 * Displays server-provided subjects, not hard-coded syllabus claims.
 * Paid prompt bodies are only retrieved from the authenticated, entitlement-enforcing server.
 * "Used" progress counts prompt copies; it does NOT mean exam mastery.
 */
public final class UniversitySpecializedPromptsActivity extends Activity {
  private static final int BLUE=0xFF2458DB, INK=0xFF19345E, MUTED=0xFF617697, BG=0xFFF2F7FF;
  private static final String[] TRACKS={"UNIVERSITY","ENGINEERING","MEDICINE","MANAGEMENT"};
  private static final String[] TRACK_LABELS={"Myanmar University (26)","Engineering (14)","Medicine (15)","Management (12)"};
  private final ExecutorService io=Executors.newSingleThreadExecutor();
  private final ArrayList<JSONObject> allSubjects=new ArrayList<>();
  private final ArrayList<JSONObject> visibleSubjects=new ArrayList<>();
  private int trackIndex=0, subjectIndex=0;
  private boolean selecting=false;
  private LinearLayout root,cards;
  private Spinner trackSelector, subjectSelector;
  private TextView status, summary;
  private android.widget.ProgressBar progress;

  private int dp(int value){return Math.round(getResources().getDisplayMetrics().density*value);}
  private GradientDrawable surface(int color,int radius){
    GradientDrawable b=new GradientDrawable();b.setColor(color);b.setCornerRadius(dp(radius));return b;
  }
  private LinearLayout col(){LinearLayout l=new LinearLayout(this);l.setOrientation(1);return l;}
  private TextView text(String value,int size,boolean bold,int color){
    TextView t=new TextView(this);t.setText(value);t.setTextSize(size);t.setTextColor(color);
    if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;
  }
  private void add(LinearLayout where,View child,int gap){
    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1,-2);
    params.topMargin=dp(gap);where.addView(child,params);
  }
  private Button button(String title,Runnable fn){
    Button b=new Button(this);b.setAllCaps(false);b.setText(title);b.setTextSize(13);
    b.setTextColor(Color.WHITE);b.setBackground(surface(BLUE,13));b.setOnClickListener(v->fn.run());
    return b;
  }
  private String apiUrl(){
    return getSharedPreferences("student_study_license_v241",MODE_PRIVATE)
      .getString("api_url",BuildConfig.STUDENT_AI_API_URL);
  }
  private JSONObject api(String action,String query,JSONObject payload) throws Exception {
    String endpoint=apiUrl();
    if(endpoint==null||!endpoint.startsWith("https://"))throw new Exception("Cloud server must use HTTPS");
    String token=StudentLicenseActivity.token(this);
    if(token.isEmpty())throw new Exception("Student login is required");
    String url=endpoint+(endpoint.contains("?")?"&":"?")+"action="+action+(query==null?"":query);
    HttpURLConnection c=(HttpURLConnection)new URL(url).openConnection();
    c.setConnectTimeout(12000);c.setReadTimeout(18000);
    c.setRequestProperty("Accept","application/json");
    c.setRequestProperty("Authorization","Bearer "+token);
    c.setRequestProperty("X-Device-ID",StudentLicenseActivity.deviceId(this));
    try{
      if(payload!=null) {
        c.setRequestMethod("POST");c.setDoOutput(true);
        c.setRequestProperty("Content-Type","application/json; charset=utf-8");
        try(OutputStream output=c.getOutputStream()){
          output.write(payload.toString().getBytes(StandardCharsets.UTF_8));
        }
      }
      int statusCode=c.getResponseCode();
      InputStream stream=statusCode>=400?c.getErrorStream():c.getInputStream();
      ByteArrayOutputStream bytes=new ByteArrayOutputStream();
      if(stream!=null)try(InputStream input=stream) {
        byte[] buffer=new byte[4096];int read;
        while((read=input.read(buffer))!=-1&&bytes.size()<800000)bytes.write(buffer,0,read);
      }
      JSONObject result=new JSONObject(new String(bytes.toByteArray(),StandardCharsets.UTF_8));
      if(statusCode<200||statusCode>=300)throw new Exception(result.optString("error","HTTP "+statusCode));
      return result;
    }finally{c.disconnect();}
  }
  @Override public void onCreate(Bundle saved){
    super.onCreate(saved);
    getWindow().setStatusBarColor(0xFF16358C);
    getWindow().setNavigationBarColor(Color.WHITE);
    ScrollView scroll=new ScrollView(this);scroll.setBackgroundColor(BG);
    root=col();root.setPadding(dp(15),dp(20),dp(15),dp(28));
    scroll.addView(root);setContentView(scroll);
    add(root,text("University Specialized AI Prompts",23,true,INK),5);
    add(root,text("Myanmar University • Engineering • Medicine • Management",13,false,MUTED),7);
    add(root,text("Select your major to learn with subject-specific AI Tutor prompts.",13,false,INK),9);
    trackSelector=new Spinner(this);
    trackSelector.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,TRACK_LABELS));
    add(root,trackSelector,12);
    subjectSelector=new Spinner(this);add(root,subjectSelector,10);
    summary=text("Loading major list…",14,true,BLUE);add(root,summary,13);
    progress=new android.widget.ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
    progress.setMax(100);add(root,progress,10);
    status=text("Progress records prompt copies, not grades or course completion.",12,false,MUTED);
    add(root,status,8);
    cards=col();add(root,cards,13);
    add(root,button("Refresh Prompt Library",this::loadPrompts),13);
    add(root,button("Subject Subscription Packages",()->startActivity(
      new Intent(this,SubjectPackagesActivity.class))),8);
    trackSelector.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){
      @Override public void onNothingSelected(android.widget.AdapterView<?> p){}
      @Override public void onItemSelected(android.widget.AdapterView<?> p,View v,int position,long id){
        trackIndex=position;fillSubjects();
      }
    });
    subjectSelector.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){
      @Override public void onNothingSelected(android.widget.AdapterView<?> p){}
      @Override public void onItemSelected(android.widget.AdapterView<?> p,View v,int position,long id){
        subjectIndex=position;if(!selecting)loadPrompts();
      }
    });
    loadSubjects();
  }
  private void loadSubjects(){
    status.setText("Loading 67 university/professional subjects from server...");
    io.execute(()->{
      try{
        JSONObject result=api("subject-packages",null,null);
        ArrayList<JSONObject> found=new ArrayList<>();
        JSONArray subs=result.optJSONArray("subjects");
        if(subs!=null)for(int i=0;i<subs.length();i++){
          JSONObject s=subs.optJSONObject(i);
          if(s!=null&&isTrack(s.optString("track")))found.add(s);
        }
        runOnUiThread(()->{
          allSubjects.clear();allSubjects.addAll(found);
          status.setText(found.size()+" specialized subjects available; prices controlled by Admin.");
          fillSubjects();
        });
      }catch(Exception e){runOnUiThread(()->{
        summary.setText("Cloud catalog unavailable");
        status.setText("Server error: "+e.getMessage()+"\nDeploy V2.44 and check Student Login.");
      });}
    });
  }
  private boolean isTrack(String value){
    for(String t:TRACKS)if(t.equals(value))return true;
    return false;
  }
  private void fillSubjects(){
    if(subjectSelector==null)return;
    selecting=true;subjectIndex=0;
    visibleSubjects.clear();
    String track=TRACKS[trackIndex];
    for(JSONObject x:allSubjects)if(track.equals(x.optString("track")))visibleSubjects.add(x);
    ArrayList<String> names=new ArrayList<>();
    for(JSONObject x:visibleSubjects)names.add(x.optString("title")+" ("+x.optString("subject_code")+")");
    if(names.isEmpty())names.add("No server subjects for this track");
    subjectSelector.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,names));
    selecting=false;
    cards.removeAllViews();
    if(!visibleSubjects.isEmpty())loadPrompts();
  }
  private void loadPrompts(){
    if(cards==null||subjectIndex<0||subjectIndex>=visibleSubjects.size())return;
    JSONObject selected=visibleSubjects.get(subjectIndex);
    String track=selected.optString("track"),code=selected.optString("subject_code"),title=selected.optString("title");
    summary.setText(title+" · Loading...");
    io.execute(()->{
      try{
        JSONObject response=api("subject-prompts","&track="+track+"&subject_code="+code,null);
        runOnUiThread(()->{
          if(subjectIndex>=visibleSubjects.size())return;
          JSONObject current=visibleSubjects.get(subjectIndex);
          if(!track.equals(current.optString("track"))||!code.equals(current.optString("subject_code")))return;
          int total=response.optInt("total"),used=response.optInt("used_count");
          boolean premium=response.optBoolean("premium_active");
          double pc=response.optDouble("completion_percent",0);
          summary.setText(title+" · "+used+"/"+total+" prompts copied · "+
            String.format(Locale.US,"%.1f",pc)+"%"+(premium?" · Premium Active":" · Free Preview"));
          progress.setProgress(Math.max(0,Math.min(100,(int)Math.round(pc))));
          renderPrompts(response.optJSONArray("templates"));
        });
      }catch(Exception e){runOnUiThread(()->{
        summary.setText("Prompt list unavailable");cards.removeAllViews();
        status.setText("Cloud error: "+e.getMessage());
      });}
    });
  }
  private void renderPrompts(JSONArray templates){
    cards.removeAllViews();
    if(templates==null||templates.length()==0){
      add(cards,text("No published prompts. Server V2.44 migration may be pending.",13,false,MUTED),8);
      return;
    }
    for(int i=0;i<templates.length();i++){
      JSONObject item=templates.optJSONObject(i);if(item==null)continue;
      int promptId=item.optInt("id");
      boolean canOpen=item.optBoolean("available",false);
      LinearLayout panel=col();panel.setPadding(dp(14),dp(13),dp(14),dp(13));
      panel.setElevation(dp(2));panel.setBackground(surface(Color.WHITE,16));
      add(panel,text((i+1)+". "+item.optString("title"),16,true,INK),2);
      add(panel,text(item.optString("description"),12,false,MUTED),6);
      add(panel,text((canOpen?"✓ UNLOCKED":"🔒 SUBJECT SUBSCRIPTION REQUIRED")+"  ·  "+
        (item.optBoolean("used",false)?"COPIED":"NEW"),12,true,canOpen?0xFF078A6C:0xFFAA7318),8);
      add(panel,button(canOpen?"View & Copy Prompt":"View Package Options",canOpen
        ?()->viewPrompt(promptId)
        :()->startActivity(new Intent(this,SubjectPackagesActivity.class))),9);
      add(cards,panel,10);
    }
  }
  private void viewPrompt(int id){
    status.setText("Loading secured prompt...");
    io.execute(()->{
      try{
        JSONObject response=api("subject-prompt","&id="+id,null);
        JSONObject prompt=response.getJSONObject("prompt");
        String title=prompt.optString("title","Subject Prompt");
        String value=prompt.optString("prompt_text");
        runOnUiThread(()->{
          ScrollView area=new ScrollView(this);
          TextView body=text(value,13,false,INK);body.setPadding(dp(16),dp(16),dp(16),dp(16));
          area.addView(body);
          AlertDialog dialog=new AlertDialog.Builder(this).setTitle(title).setView(area)
            .setNegativeButton("Close",null).setPositiveButton("Copy Prompt",null).create();
          dialog.setOnShowListener(x->dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v->{
            ClipboardManager clip=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            clip.setPrimaryClip(ClipData.newPlainText("Student AI Specialized Prompt",value));
            dialog.dismiss();status.setText("Copied prompt. Syncing usage progress...");
            markUsed(id);
          }));
          dialog.show();
        });
      }catch(Exception e){runOnUiThread(()->status.setText("Prompt access failed: "+e.getMessage()));}
    });
  }
  private void markUsed(int id){
    io.execute(()->{
      try{
        JSONObject payload=new JSONObject();payload.put("prompt_id",id);
        api("prompt-progress",null,payload);
        runOnUiThread(this::loadPrompts);
      }catch(Exception e){runOnUiThread(()->status.setText("Copied, but usage sync failed: "+e.getMessage()));}
    });
  }
  @Override protected void onDestroy(){io.shutdownNow();super.onDestroy();}
}
