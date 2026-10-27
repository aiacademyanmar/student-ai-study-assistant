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
 private int index=0,correct=0;
 private final ArrayList<String> weak=new ArrayList<>();
 private int dp(int v){return StudyDesign.dp(this,v);}
 private void label(String s,int size){TextView t=StudyDesign.text(this,s,size,StudyDesign.INK,size>=18);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(12);body.addView(t,p);}
 private void button(String s,Runnable a){TextView t=StudyDesign.action(this,s,false,a);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(9);body.addView(t,p);}
 @Override public void onCreate(Bundle b){super.onCreate(b);history[0]=new OfflineQuizHistory(this);draw();}
 @Override public void onBackPressed(){if(!quiz.isEmpty())new AlertDialog.Builder(this).setMessage("Quiz ကို မသိမ်းဘဲ ထွက်မလား?").setPositiveButton("Exit",(d,w)->{quiz.clear();draw();}).setNegativeButton("Cancel",null).show();else finish();}
 private void draw(){
  root=StudyDesign.col(this);root.setBackgroundColor(StudyDesign.BG);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,i)->{android.graphics.Insets x=i.getInsets(WindowInsets.Type.systemBars());v.setPadding(x.left,x.top,x.right,x.bottom);return i;});
  TextView title=StudyDesign.text(this,"‹    Offline Quiz · V2.23",19,StudyDesign.INK,true);title.setPadding(dp(14),dp(15),dp(12),dp(15));title.setBackgroundColor(-1);title.setOnClickListener(v->onBackPressed());root.addView(title);
  ScrollView sc=new ScrollView(this);body=StudyDesign.col(this);body.setPadding(dp(14),dp(15),dp(14),dp(25));sc.addView(body);root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
  if(!quiz.isEmpty())question();else if(code==null)setup();else results();
  root.addView(StudyDesign.navigation(this,OfflineQuizActivity.class));
 }
 private void setup(){
  body.addView(StudyDesign.hero(this,"ORIGINAL QUESTIONS · NO INTERNET","Native Offline MCQ","Instant scoring · explanation · score history"));
  label("⚠ 96 sample original questions, 4 per subject. NOT full Cambridge coverage or official past papers.",13);
  button("Level: "+("O".equals(level)?"O Level":"AS/A Level")+" · Change",()->new AlertDialog.Builder(this).setItems(new String[]{"GCE O Level","GCE AS/A Level"},(d,x)->{level=x==0?"O":"A";draw();}).show());
  String[] names=new String[12];for(int i=0;i<12;i++)names[i]="O".equals(level)?OLevel2026.SUBJECTS[i].name:ALevel2026.SUBJECTS[i].name;
  for(int i=0;i<12;i++){final int id=i;String code="O".equals(level)?OLevel2026.SUBJECTS[i].code:ALevel2026.SUBJECTS[i].code;button("📚 "+names[i]+" ("+code+") · "+OfflineQuestionBank.count(level,code)+" questions",()->{this.code=code;quiz=OfflineQuestionBank.choose(level,code,4);index=0;correct=0;weak.clear();draw();});}
  label("Previous offline quiz results",20);
  JSONArray old=history[0].all();for(int i=0;i<Math.min(8,old.length());i++){JSONObject r=old.optJSONObject(i);if(r!=null)label(r.optString("level")+" "+r.optString("code")+": "+r.optInt("correct")+"/"+r.optInt("total")+" · weak: "+r.optString("weak"),12);}
 }
 private void question(){
  OfflineQuestionBank.Question q=quiz.get(index);
  body.addView(StudyDesign.hero(this,""+level+" LEVEL · "+code,"Question "+(index+1)+" / "+quiz.size(),"Answered correctly: "+correct));
  label(q.topic,17);label(q.prompt,20);
  String[] keys={"A","B","C","D"};
  for(int n=0;n<4;n++){final int selection=n;button(keys[n]+". "+q.options[n],()->answer(selection));}
 }
 private void answer(int choice){
  OfflineQuestionBank.Question q=quiz.get(index);
  boolean success=choice==q.answer;if(success)correct++;else weak.add(q.topic);
  new AlertDialog.Builder(this).setTitle(success?"✓ Correct":"✗ Review this concept")
   .setMessage("Correct answer: "+q.options[q.answer]+"\n\n"+q.explanation)
   .setPositiveButton(index==quiz.size()-1?"Show score":"Next question",(d,w)->{
    index++;if(index>=quiz.size()){history[0].add(level,code,correct,quiz.size(),android.text.TextUtils.join(", ",weak));quiz.clear();}draw();
   }).setCancelable(false).show();
 }
 private void results(){
  body.addView(StudyDesign.hero(this,"OFFLINE QUIZ COMPLETE","Score: "+correct+" / 4","Local auto-marked original practice"));
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
  button("Start new quiz",()->{quiz=OfflineQuestionBank.choose(level,code,4);index=0;correct=0;weak.clear();draw();});
  button("All subjects",()->{code=null;draw();});
 }
}
