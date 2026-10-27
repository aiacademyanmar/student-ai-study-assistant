package asia.aaii.studentstudy;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
public class OfflineQuizActivity extends Activity{
 private LinearLayout root,body;
 private final OfflineQuizHistory[] history={null};
 private String level="O",code=null;
 private List<OfflineQuestionBank.Question> quiz=new ArrayList<>();
 private int index=0,correct=0,limit=6,minutes=5;
 private String topic=OfflineQuizCatalog.ALL,difficulty=OfflineQuizCatalog.ANY;
 private long deadline=0;private android.os.Handler clock=new android.os.Handler(android.os.Looper.getMainLooper());
 private TextView timerView;private boolean answered=false;
 private final ArrayList<String> wrongIds=new ArrayList<>();
 private final ArrayList<String> weak=new ArrayList<>();
 private int dp(int v){return StudyDesign.dp(this,v);}
 private void label(String s,int size){TextView t=StudyDesign.text(this,s,size,StudyDesign.INK,size>=18);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(12);body.addView(t,p);}
 private void button(String s,Runnable a){TextView t=StudyDesign.action(this,s,false,a);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(9);body.addView(t,p);}
 @Override public void onCreate(Bundle b){super.onCreate(b);history[0]=new OfflineQuizHistory(this);draw();}
 @Override protected void onPause(){super.onPause();clock.removeCallbacksAndMessages(null);}
 @Override protected void onResume(){super.onResume();if(!quiz.isEmpty())updateClock();}
 private void updateClock(){
  clock.removeCallbacksAndMessages(null);
  if(quiz.isEmpty()||deadline<=0)return;
  long left=deadline-android.os.SystemClock.elapsedRealtime();
  if(left<=0){finishQuiz();return;}
  if(timerView!=null)timerView.setText("⏳ Time left: "+((left+999)/1000)+" seconds");
  clock.postDelayed(()->updateClock(),1000);
 }
 private void finishQuiz(){
  if(quiz.isEmpty())return;
  clock.removeCallbacksAndMessages(null);
  int total=quiz.size();
  history[0].addDetailed(level,code,correct,total,android.text.TextUtils.join(", ",weak),
      topic,difficulty,minutes*60,android.text.TextUtils.join(", ",wrongIds));
  quiz.clear();draw();
 }
 @Override public void onBackPressed(){if(!quiz.isEmpty())new AlertDialog.Builder(this).setMessage("Quiz ကို မသိမ်းဘဲ ထွက်မလား?").setPositiveButton("Exit",(d,w)->{quiz.clear();clock.removeCallbacksAndMessages(null);code=null;draw();}).setNegativeButton("Cancel",null).show();else finish();}
 private void draw(){
  root=StudyDesign.col(this);root.setBackgroundColor(StudyDesign.BG);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,i)->{android.graphics.Insets x=i.getInsets(WindowInsets.Type.systemBars());v.setPadding(x.left,x.top,x.right,x.bottom);return i;});
  TextView title=StudyDesign.text(this,"‹    Offline Quiz · V2.24",19,StudyDesign.INK,true);title.setPadding(dp(14),dp(15),dp(12),dp(15));title.setBackgroundColor(-1);title.setOnClickListener(v->onBackPressed());root.addView(title);
  ScrollView sc=new ScrollView(this);body=StudyDesign.col(this);body.setPadding(dp(14),dp(15),dp(14),dp(25));sc.addView(body);root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
  if(!quiz.isEmpty())question();else if(code==null)setup();else results();
  root.addView(StudyDesign.navigation(this,OfflineQuizActivity.class));
 }
 private void setup(){
  body.addView(StudyDesign.hero(this,"ORIGINAL QUESTIONS · NO INTERNET","Native Offline MCQ","Instant scoring · explanation · score history"));
  label("⚠ 144 original starter MCQs across 24 subjects (6 each). Not official Cambridge papers or comprehensive syllabus coverage.",13);
  button("Level: "+("O".equals(level)?"O Level":"AS/A Level")+" · Change",()->new AlertDialog.Builder(this).setItems(new String[]{"GCE O Level","GCE AS/A Level"},(d,x)->{level=x==0?"O":"A";draw();}).show());
  String[] names=new String[12];for(int i=0;i<12;i++)names[i]="O".equals(level)?OLevel2026.SUBJECTS[i].name:ALevel2026.SUBJECTS[i].name;
  for(int i=0;i<12;i++){String cd="O".equals(level)?OLevel2026.SUBJECTS[i].code:ALevel2026.SUBJECTS[i].code;final String pick=cd;button("📚 "+names[i]+" ("+cd+") · "+OfflineQuizCatalog.count(level,cd,OfflineQuizCatalog.ALL,OfflineQuizCatalog.ANY)+" questions",()->{code=pick;topic=OfflineQuizCatalog.ALL;difficulty=OfflineQuizCatalog.ANY;configure();});}
  label("Previous offline quiz results",20);
  JSONArray old=history[0].all();for(int i=0;i<Math.min(8,old.length());i++){JSONObject r=old.optJSONObject(i);if(r!=null)label(r.optString("level")+" "+r.optString("code")+": "+r.optInt("correct")+"/"+r.optInt("total")+" · weak: "+r.optString("weak"),12);}
 }
 private void question(){
  OfflineQuestionBank.Question q=quiz.get(index);
  body.addView(StudyDesign.hero(this,""+level+" LEVEL · "+code,"Question "+(index+1)+" / "+quiz.size(),"Answered correctly: "+correct));
  timerView=StudyDesign.text(this,"",16,StudyDesign.BLUE,true);body.addView(timerView);updateClock();
  label(q.topic,17);label(q.prompt,20);
  String[] keys={"A","B","C","D"};
  for(int n=0;n<4;n++){final int selection=n;button(keys[n]+". "+q.options[n],()->answer(selection));}
 }
 private void answer(int choice){
  OfflineQuestionBank.Question q=quiz.get(index);
  if(answered)return;answered=true;clock.removeCallbacksAndMessages(null);
  boolean success=choice==q.answer;if(success)correct++;else{weak.add(q.topic);wrongIds.add(q.id);}
  new AlertDialog.Builder(this).setTitle(success?"✓ Correct":"✗ Review this concept")
   .setMessage("Correct answer: "+q.options[q.answer]+"\n\n"+q.explanation)
   .setPositiveButton(index==quiz.size()-1?"Show score":"Next question",(d,w)->{
    index++;answered=false;if(index>=quiz.size())finishQuiz();else draw();
   }).setCancelable(false).show();
 }
 private void configure(){
  android.widget.LinearLayout form=StudyDesign.col(this);form.setPadding(dp(15),dp(6),dp(15),dp(4));
  java.util.List<String> topics=OfflineQuizCatalog.topics(level,code);
  android.widget.Spinner topicSelect=new android.widget.Spinner(this),diffSelect=new android.widget.Spinner(this);
  android.widget.ArrayAdapter<String> topicsAdapter=new android.widget.ArrayAdapter<>(this,android.R.layout.simple_spinner_item,topics);
  topicsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);topicSelect.setAdapter(topicsAdapter);
  android.widget.ArrayAdapter<String> difficultyAdapter=new android.widget.ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
      new String[]{OfflineQuizCatalog.ANY,"Easy","Medium","Hard"});
  difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);diffSelect.setAdapter(difficultyAdapter);
  form.addView(StudyDesign.text(this,"Topic",14,StudyDesign.INK,true));form.addView(topicSelect);
  form.addView(StudyDesign.text(this,"Difficulty",14,StudyDesign.INK,true));form.addView(diffSelect);
  android.widget.EditText num=new android.widget.EditText(this),dur=new android.widget.EditText(this);
  num.setInputType(2);num.setText("6");num.setHint("Number of questions");
  dur.setInputType(2);dur.setText("5");dur.setHint("Minutes");
  form.addView(num);form.addView(dur);
  new AlertDialog.Builder(this).setTitle("Timed Quiz · "+code).setView(form).setPositiveButton("Start",(d,w)->{
   topic=topics.get(topicSelect.getSelectedItemPosition());
   difficulty=(String)diffSelect.getSelectedItem();
   int requested=6,mins=5;try{requested=Integer.parseInt(num.getText().toString());mins=Integer.parseInt(dur.getText().toString());}catch(Exception ignored){}
   limit=Math.max(1,Math.min(25,requested));minutes=Math.max(1,Math.min(60,mins));
   quiz=OfflineQuizCatalog.choose(level,code,topic,difficulty,limit);
   if(quiz.isEmpty()){code=null;android.widget.Toast.makeText(this,"This topic/difficulty has no questions. Choose another filter.",1).show();draw();return;}
   limit=quiz.size();index=0;correct=0;weak.clear();wrongIds.clear();answered=false;
   deadline=android.os.SystemClock.elapsedRealtime()+minutes*60000L;draw();
  }).setNegativeButton("Cancel",(d,w)->{code=null;draw();}).show();
 }
 private void dashboard(){
  JSONArray rows=history[0].all();java.util.LinkedHashMap<String,int[]> byTopic=new java.util.LinkedHashMap<>();
  int attempts=0,marks=0,total=0;
  for(int i=0;i<rows.length();i++){JSONObject x=rows.optJSONObject(i);if(x==null||!level.equals(x.optString("level")))continue;
   attempts++;marks+=x.optInt("correct");total+=x.optInt("total");
   String t=x.optString("topic","All Topics");int[] record=byTopic.get(t);if(record==null){record=new int[3];byTopic.put(t,record);}
   record[0]++;record[1]+=x.optInt("correct");record[2]+=x.optInt("total");
  }
  StringBuilder report=new StringBuilder("Attempts: "+attempts+"\\nTotal: "+marks+"/"+total+"\\n\\n");
  for(java.util.Map.Entry<String,int[]> item:byTopic.entrySet()){
   int[] v=item.getValue();report.append(item.getKey()).append(" · ").append(v[1]).append("/").append(v[2]).append(" correct, ").append(v[0]).append(" attempts\\n");
  }
  new AlertDialog.Builder(this).setTitle("Offline Quiz Dashboard · "+level+" Level")
    .setMessage(report.toString().replace("\\\\n","\\n")).setPositiveButton("Close",null).show();
 }
 private void results(){
  body.addView(StudyDesign.hero(this,"OFFLINE QUIZ COMPLETE","Score: "+correct+" / "+limit,"Local auto-marked original practice"));
  int[] total=history[0].totals(level,code);
  label("This subject: "+total[0]+" attempts · "+total[1]+"/"+total[2]+" cumulative correct",15);
  label("Revise: "+(weak.isEmpty()?"No incorrect answers this attempt":android.text.TextUtils.join(", ",weak)),14);
  button("🤖 AI Revision for weak topics",()->{
   String target=weak.isEmpty()?"general revision and new examples":android.text.TextUtils.join(", ",weak);
   AiProviderRouter.chooseAndRun(this,"Be a Myanmar-language GCE "+level+" Level tutor for "+code+
    ". Explain these weak concepts using original worked examples: "+target+
    ". Then ask five new questions one at a time, give hints and feedback. This is not an official Cambridge paper.",
    "offline_revise_"+code,"Quiz Revision");
  });
  button("Start new quiz",()->configure());
  button("📊 Detailed Score Dashboard",()->dashboard());
  button("All subjects",()->{code=null;draw();});
 }
}
