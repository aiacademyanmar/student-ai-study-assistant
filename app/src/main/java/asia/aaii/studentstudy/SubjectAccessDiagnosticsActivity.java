package asia.aaii.studentstudy;

import android.app.Activity;
import android.content.Context;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Read-only V2.47 subject permission diagnostics. Never records purchases or payments. */
public final class SubjectAccessDiagnosticsActivity extends Activity {
  private final ExecutorService executor=Executors.newSingleThreadExecutor();
  private final ArrayList<JSONObject> subjects=new ArrayList<>();
  private Spinner subjectSpinner;
  private TextView result;
  private static final int BLUE=0xFF245BDA,INK=0xFF182E55,MUTED=0xFF637591;
  private int dp(int n){return Math.round(n*getResources().getDisplayMetrics().density);}
  private LinearLayout col(){LinearLayout l=new LinearLayout(this);l.setOrientation(1);return l;}
  private TextView label(String s,int sp,boolean bold,int color){
    TextView t=new TextView(this);t.setText(s);t.setTextSize(sp);t.setTextColor(color);
    if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;
  }
  private GradientDrawable bg(int color,int r){
    GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(r));return d;
  }
  private void add(LinearLayout p,View v,int top){
    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.topMargin=dp(top);p.addView(v,lp);
  }
  private Button button(String title,Runnable run){
    Button b=new Button(this);b.setText(title);b.setAllCaps(false);b.setTextColor(Color.WHITE);
    b.setBackground(bg(BLUE,15));b.setOnClickListener(v->run.run());return b;
  }
  private String endpoint(){
    return getSharedPreferences("student_study_license_v241",MODE_PRIVATE)
      .getString("api_url",BuildConfig.STUDENT_AI_API_URL);
  }
  private JSONObject get(String action,String extra,boolean auth) throws Exception {
    String target=endpoint();if(target==null||!target.startsWith("https://"))
      throw new Exception("Valid HTTPS API URL required");
    String path=target+(target.contains("?")?"&":"?")+"action="+action+(extra==null?"":extra);
    HttpURLConnection c=(HttpURLConnection)new URL(path).openConnection();
    c.setConnectTimeout(12000);c.setReadTimeout(18000);c.setUseCaches(false);
    c.setRequestProperty("Accept","application/json");
    if(auth){
      String token=StudentLicenseActivity.token(this);
      if(token.isEmpty())throw new Exception("Student login required");
      c.setRequestProperty("Authorization","Bearer "+token);
      c.setRequestProperty("X-Device-ID",StudentLicenseActivity.deviceId(this));
    }
    try{
      int code=c.getResponseCode();
      InputStream stream=code>=400?c.getErrorStream():c.getInputStream();
      ByteArrayOutputStream output=new ByteArrayOutputStream();
      if(stream!=null)try(InputStream in=stream){
        byte[] buf=new byte[4096];int n;
        while((n=in.read(buf))!=-1 && output.size()<250000)output.write(buf,0,n);
      }
      JSONObject obj=new JSONObject(new String(output.toByteArray(),StandardCharsets.UTF_8));
      if(code<200||code>=300)throw new Exception(obj.optString("error","HTTP "+code));
      return obj;
    }finally{c.disconnect();}
  }
  @Override public void onCreate(Bundle state){
    super.onCreate(state);
    getWindow().setStatusBarColor(0xFF17368A);
    ScrollView scroll=new ScrollView(this);scroll.setBackgroundColor(0xFFF4F7FF);
    LinearLayout panel=col();panel.setPadding(dp(18),dp(25),dp(18),dp(35));
    scroll.addView(panel);setContentView(scroll);
    add(panel,label("Subject Access Check",24,true,INK),3);
    add(panel,label("GCE O / AS & A • University • Engineering • Medicine • Management",13,false,MUTED),7);
    add(panel,label("တကယ် Premium ဝင်ခွင့်ရှိ/မရှိကို Cloud Server မှ စစ်ဆေးပါ။",14,false,INK),12);
    subjectSpinner=new Spinner(this);add(panel,subjectSpinner,14);
    add(panel,button("Check Selected Subject Access",this::checkSelected),12);
    result=label("Loading the 91-subject catalog from your Cloud Server...",14,false,MUTED);
    add(panel,result,18);
    add(panel,label("Access check does not charge payments or modify subscriptions.",12,false,MUTED),13);
    loadCatalog();
  }
  private void loadCatalog(){
    result.setText("Loading server catalog…");
    executor.execute(()->{
      try{
        JSONObject data=get("subject-packages",null,false);
        JSONArray items=data.optJSONArray("subjects");
        if(items==null||items.length()==0)throw new Exception("No published subjects");
        ArrayList<JSONObject> found=new ArrayList<>();
        ArrayList<String> names=new ArrayList<>();
        for(int i=0;i<items.length();i++){
          JSONObject item=items.optJSONObject(i);if(item==null)continue;
          found.add(item);
          names.add(item.optString("track").replace('_',' ')+" · "+
            item.optString("title")+" ("+item.optString("subject_code")+")");
        }
        runOnUiThread(()->{
          subjects.clear();subjects.addAll(found);
          subjectSpinner.setAdapter(new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_dropdown_item,names));
          result.setText("Select a subject. Access is verified against your own active Student AI license.");
        });
      }catch(Exception e){runOnUiThread(()->result.setText(
        "Cannot load subject catalog: "+e.getMessage()+
        "\nThe V2.47 server must be deployed and connected."));}
    });
  }
  private void checkSelected(){
    int idx=subjectSpinner.getSelectedItemPosition();
    if(idx<0||idx>=subjects.size()){result.setText("Choose a subject.");return;}
    JSONObject subject=subjects.get(idx);
    String track=subject.optString("track"),code=subject.optString("subject_code");
    result.setText("Checking account and license with Cloud Server…");
    executor.execute(()->{
      try{
        JSONObject info=get("subject-access-check","&track="+
          java.net.URLEncoder.encode(track,"UTF-8")+"&subject_code="+
          java.net.URLEncoder.encode(code,"UTF-8"),true);
        boolean permitted=info.optBoolean("access",false);
        String reason=info.optString("reason");
        String explanation=reason.equals("legacy_full_premium")?"Full Premium membership":
          reason.equals("active_subject_subscription")?"Active subscription for this subject":
          reason.equals("subject_unavailable")?"Subject not enabled on server":
          "Subject subscription required or expired";
        runOnUiThread(()->{
          result.setText((permitted?"✓ ACCESS GRANTED":"🔒 ACCESS NOT GRANTED")+
            "\n"+subject.optString("title")+" ("+code+")\n"+
            explanation+"\n\nNo payment was charged or subscription changed.");
          result.setTextColor(permitted?0xFF07875E:0xFFAD541A);
        });
      }catch(Exception e){runOnUiThread(()->{
        result.setText("Cloud permission check failed: "+e.getMessage()+
          "\nConfirm Student login, Device ID and V2.47 server deployment.");
        result.setTextColor(0xFFAD541A);
      });}
    });
  }
  @Override protected void onDestroy(){executor.shutdownNow();super.onDestroy();}
}