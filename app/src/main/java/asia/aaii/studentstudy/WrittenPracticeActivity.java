package asia.aaii.studentstudy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Offline original written-answer study; marking uses MANUAL learner checkboxes.
 * Neither the rubrics nor self-reported scores are Cambridge mark schemes.
 */
public final class WrittenPracticeActivity extends Activity {
 private final boolean[] checked=new boolean[4];
 private final List<WrittenPracticeBank.Task> current=new ArrayList<>();
 private LinearLayout root,body;
 private WrittenPracticeStore store;
 private String level="O",code=null;
 private int selected=-1;
 private boolean reveal=false;
 private EditText input;
 private int dp(float n){return StudyDesign.dp(this,n);}
 private void heading(String s,int size){
  TextView t=StudyDesign.text(this,s,size,StudyDesign.INK,size>=18);
  LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(12);body.addView(t,p);
 }
 private void action(String label,boolean primary,Runnable click){
  TextView v=StudyDesign.action(this,label,primary,click);
  LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(9);body.addView(v,p);
 }
 private WrittenPracticeBank.Task task(){return current.get(selected);}
 @Override public void onCreate(Bundle b){
  super.onCreate(b);store=new WrittenPracticeStore(this);
  String lvl=getIntent().getStringExtra("level");
  if("A".equals(lvl))level="A";
  code=getIntent().getStringExtra("code");
  if(code!=null&&WrittenPracticeBank.forSubject(level,code).isEmpty())code=null;
  if(code!=null)current.addAll(WrittenPracticeBank.forSubject(level,code));
  draw();
 }
 @Override public void onBackPressed(){
  if(selected>=0){selected=-1;reveal=false;}
  else if(code!=null){code=null;current.clear();}
  else{finish();return;}
  draw();
 }
 private void pickSubject(String id){code=id;selected=-1;current.clear();current.addAll(WrittenPracticeBank.forSubject(level,code));draw();}
 private void draw(){
  root=StudyDesign.col(this);root.setBackgroundColor(StudyDesign.BG);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,i)->{
    android.graphics.Insets bar=i.getInsets(WindowInsets.Type.systemBars());
    v.setPadding(bar.left,bar.top,bar.right,bar.bottom);return i;
  });
  TextView h=StudyDesign.text(this,"‹   Written Answer Practice",18,StudyDesign.INK,true);
  h.setBackgroundColor(-1);h.setPadding(dp(16),dp(15),dp(14),dp(15));
  h.setOnClickListener(v->onBackPressed());root.addView(h);
  ScrollView s=new ScrollView(this);body=StudyDesign.col(this);
  body.setPadding(dp(15),dp(14),dp(15),dp(40));s.addView(body);
  root.addView(s,new LinearLayout.LayoutParams(-1,0,1));
  if(code==null)subjects();else if(selected<0)tasks();else exercise();
  root.addView(StudyDesign.navigation(this,WrittenPracticeActivity.class));
 }
 private void subjects(){
  body.addView(StudentPromoBanner.create(this,"STUDENT AI · WRITING","Practice Writing","48 original tasks · model answers","Choose Subject",()->{},false));
  heading("⚠ Self-assessment only. Cambridge official mark schemes are not included.",13);
  action("Level: "+("O".equals(level)?"O Level":"AS/A Level")+" · Change",false,()->
    new AlertDialog.Builder(this).setItems(new String[]{"O Level","AS/A Level"},(d,index)->{
      level=index==0?"O":"A";code=null;selected=-1;current.clear();draw();
    }).show());
  for(int i=0;i<12;i++){
    String sc="O".equals(level)?OLevel2026.SUBJECTS[i].code:ALevel2026.SUBJECTS[i].code;
    String sn="O".equals(level)?OLevel2026.SUBJECTS[i].name:ALevel2026.SUBJECTS[i].name;
    int[] stats=store.subjectStats(level,sc);
    action("📝 "+sn+" · "+sc+"\n2 original tasks · "+stats[0]+" self-reviews",false,()->pickSubject(sc));
  }
 }
 private void tasks(){
  body.addView(StudyDesign.hero(this,"SHORT ANSWER & STRUCTURED",code,
    "Write your own answer, then compare with the original worked example"));
  int[] x=store.subjectStats(level,code);
  heading("Saved self-checks: "+x[0]+" · Points "+x[1]+"/"+x[2],14);
  for(int i=0;i<current.size();i++){
    final int id=i;WrittenPracticeBank.Task t=current.get(i);
    action("🖊 "+(i+1)+". "+t.topic+"\n"+t.question,false,()->{
      selected=id;reveal=false;java.util.Arrays.fill(checked,false);draw();
    });
  }
  action("📊 Topic Learning Analytics",true,()->startActivity(new Intent(this,TopicAnalyticsActivity.class)));
 }
 private void exercise(){
  WrittenPracticeBank.Task t=task();
  body.addView(StudyDesign.hero(this,"WRITE · SELF-REVIEW",t.topic,
    ("O".equals(level)?"O Level":"AS/A Level")+" · "+code));
  heading(t.question,19);
  heading("Your response (stored locally on this device)",13);
  input=new EditText(this);
  input.setHint("အဖြေကို မြန်မာလို / English Technical Terms ဖြင့် ရေးပါ…");
  input.setTextSize(15);input.setMinLines(5);input.setMaxLines(12);
  input.setGravity(android.view.Gravity.TOP);
  input.setPadding(dp(14),dp(15),dp(14),dp(15));
  input.setBackground(StudyDesign.stroked(this,-1,StudyDesign.BORDER,15));
  input.setText(store.draft(t));
  input.addTextChangedListener(new TextWatcher(){
    public void beforeTextChanged(CharSequence s,int start,int count,int after){}
    public void onTextChanged(CharSequence s,int start,int before,int count){store.saveDraft(t,s.toString());}
    public void afterTextChanged(Editable e){}
  });
  LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.topMargin=dp(12);body.addView(input,lp);
  if(!reveal){
    action("👁 Compare with original model answer",true,()->{
      store.saveDraft(t,input.getText().toString());reveal=true;draw();
    });
    heading("First draft your own answer, then reveal the worked example. You may revise your answer afterwards.",12);
  }else{
    heading("Sample explanation (not a Cambridge official answer)",17);
    heading(t.model,15);
    heading("Self-review rubric · tick the points you demonstrated",16);
    for(int j=0;j<t.points.length;j++){
      final int idx=j;
      CheckBox item=new CheckBox(this);item.setText(t.points[j]);item.setTextColor(StudyDesign.INK);
      item.setTextSize(14);item.setChecked(checked[j]);item.setPadding(dp(2),dp(7),0,dp(7));
      item.setOnCheckedChangeListener((c,v)->checked[idx]=v);
      body.addView(item);
    }
    action("✓ Save my SELF-CHECK score (0–4)",true,()->{
      String own=input.getText().toString().trim();
      if(own.isEmpty()){Toast.makeText(this,"Write your answer before recording a score",Toast.LENGTH_LONG).show();return;}
      if(store.save(t,own,checked)){
        int n=0;for(boolean v:checked)if(v)n++;
        new AlertDialog.Builder(this).setTitle("Self-check saved: "+n+"/4")
          .setMessage("This is your own checklist score, not AI marking or an official Cambridge grade.")
          .setPositiveButton("Next exercise",(d,w)->{selected=-1;reveal=false;draw();})
          .setNegativeButton("Continue editing",null).show();
      }
    });
    action("🤖 AI Written Feedback · Explainable Review",false,()->{
      String own=input.getText().toString().trim();
      if(own.isEmpty()){Toast.makeText(this,"Write an answer before AI feedback",Toast.LENGTH_LONG).show();return;}
      new AlertDialog.Builder(this).setTitle("Choose feedback focus")
        .setItems(new String[]{"Complete detailed assessment","Find missing concepts","Improve answer structure","Create two revision exercises"},(d,n)->{
          String[] req={"full learning feedback","identify missing concepts and why",
            "improve response structure and clarity","generate two targeted revision exercises"};
          AiProviderRouter.chooseAndRun(this,WrittenFeedbackPrompts.review(t,own,req[n]),
             "written_feedback_"+code+"_"+selected,"Written AI Feedback");
        }).show();
    });
  }
  heading("Answer and self-checks stay on the phone. Sending to an AI provider shares the selected answer via that provider's app.",12);
 }
}
