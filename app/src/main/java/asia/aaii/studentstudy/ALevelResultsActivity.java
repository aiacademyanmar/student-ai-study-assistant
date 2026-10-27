package asia.aaii.studentstudy;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.EditText;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;

public class ALevelResultsActivity extends Activity {
 private ALevelScoreStore scores;
 private LinearLayout root,body;
 private int subject=-1;
 private int dp(int n){return StudyDesign.dp(this,n);}
 private void info(String value,int size){TextView t=StudyDesign.text(this,value,size,StudyDesign.INK,size>=18);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(11);body.addView(t,p);}
 private void button(String value,Runnable onClick){TextView t=StudyDesign.action(this,value,false,onClick);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(9);body.addView(t,p);}
 @Override public void onCreate(Bundle b){super.onCreate(b);scores=new ALevelScoreStore(this);subject=getIntent().getIntExtra("subject_index",-1);if(subject<0||subject>=ALevel2026.SUBJECTS.length)subject=-1;draw();}
 @Override public void onBackPressed(){if(subject>=0){subject=-1;draw();}else finish();}
 private void draw(){
  root=StudyDesign.col(this);root.setBackgroundColor(StudyDesign.BG);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,i)->{android.graphics.Insets a=i.getInsets(WindowInsets.Type.systemBars());v.setPadding(a.left,a.top,a.right,a.bottom);return i;});
  TextView header=StudyDesign.text(this,"‹   A Level · Exam Results",19,StudyDesign.INK,true);header.setPadding(dp(17),dp(16),dp(12),dp(16));header.setBackgroundColor(-1);header.setOnClickListener(v->onBackPressed());root.addView(header);
  ScrollView sc=new ScrollView(this);body=StudyDesign.col(this);body.setPadding(dp(15),dp(15),dp(15),dp(25));sc.addView(body);root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
  body.addView(StudyDesign.hero(this,"AI STUDY ASSISTANT · V2.22","Mock Score History","Student-entered scores · Weak subject analysis · AI revision"));
  info("⚠ Every score here is manually entered. Not an official Cambridge result, automatic AI score, or predicted grade.",12);
  if(subject<0){
   button("📤 Export score history JSON",()->export());
   info("Subject overview",21);
   for(int n=0;n<ALevel2026.SUBJECTS.length;n++){final int x=n;ALevel2026.Subject s=ALevel2026.SUBJECTS[n];ALevelScoreStore.SubjectSummary k=scores.summary(s.code);
    button("📘 "+s.name+" ("+s.code+")\n"+(k.count==0?"No mock entries yet":k.count+" scores · recent average "+k.average+"%"+(k.weak?" · Revision recommended":"")),()->{subject=x;draw();});
   }
  }else{
   ALevel2026.Subject s=ALevel2026.SUBJECTS[subject];ALevelScoreStore.SubjectSummary k=scores.summary(s.code);
   info(s.name+" · "+s.code,21);
   info(k.count==0?"No results saved yet. Add a mock score to start.":k.count+" recorded · latest "+k.latest+"% · best "+k.best+"% · recent 3 mean "+k.average+"%",14);
   if(k.count>0)body.addView(StudyDesign.progress(this,k.average,100));
   if(k.weak)info("Priority review indicated by your recent self-reported average below 60%. This threshold is an app setting, not Cambridge grading.",13);
   if(!k.weakness.isEmpty())info("Recent notes: "+k.weakness,13);
   button("✎ Add mock exam score",()->record(null));
   button("🤖 AI Personalized Revision",()->AiProviderRouter.chooseAndRun(this,scores.revisionPrompt(s),"al_revision_"+s.code,"A Level Personalized Revision"));
   button("📝 Start new Paper Mock Exam",()->{Intent i=new Intent(this,ALevelExamCoverageActivity.class);i.putExtra("subject_index",subject);i.putExtra("mode","exam");startActivity(i);});
   info("Recent entries",19);
   for(ALevelScoreStore.Record rec:scores.records())if(rec.code.equals(s.code)){
    String date=DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT).format(new Date(rec.timestamp));
    button("📝 "+rec.paperName+" · "+rec.scored+"/"+rec.possible+" ("+rec.percent()+"%)\n"+date+"\n"+
     (rec.focus.isEmpty()?"General revision":rec.focus)+"\n"+rec.note,()->new AlertDialog.Builder(this).setTitle("Mock entry").setMessage("Self-reported exam practice result.").setPositiveButton("Close",null).setNegativeButton("Delete",(d,w)->{scores.delete(rec);draw();}).show());
   }
  }
  root.addView(StudyDesign.navigation(this,ALevelSyllabusActivity.class));
 }
 private void record(ALevelPaperCatalog.Paper initial){
  if(subject<0)return;
  ALevel2026.Subject s=ALevel2026.SUBJECTS[subject];ALevelPaperCatalog.Paper[] papers=ALevelPaperCatalog.forSubject(s.code);
  String[] choices=new String[papers.length];for(int n=0;n<papers.length;n++)choices[n]=papers[n].label();
  new AlertDialog.Builder(this).setTitle("Select paper").setItems(choices,(d,i)->form(papers[i])).setNegativeButton("Cancel",null).show();
 }
 private void form(ALevelPaperCatalog.Paper paper){
  LinearLayout fields=StudyDesign.col(this);fields.setPadding(dp(17),dp(5),dp(17),dp(5));
  EditText[] inputs=new EditText[5];
  String[] hints={"Marks obtained","Marks available","Selected option / route","Topic / study focus","Weakness or feedback notes"};
  for(int i=0;i<inputs.length;i++){inputs[i]=new EditText(this);inputs[i].setTextSize(14);inputs[i].setSingleLine(i<4);inputs[i].setHint(hints[i]);if(i<2)inputs[i].setInputType(2);fields.addView(inputs[i]);}
  new AlertDialog.Builder(this).setTitle("Save self-reported score · "+paper.label()).setView(fields).setPositiveButton("Save",(d,w)->{
   try{
    int earned=Integer.parseInt(inputs[0].getText().toString().trim());int total=Integer.parseInt(inputs[1].getText().toString().trim());
    boolean ok=scores.add(ALevel2026.SUBJECTS[subject].code,paper.number,paper.title,
     inputs[2].getText().toString().trim(),inputs[3].getText().toString().trim(),inputs[4].getText().toString().trim(),earned,total);
    if(!ok)android.widget.Toast.makeText(this,"Score must be 0–total marks, and total > 0",android.widget.Toast.LENGTH_LONG).show();
    draw();
   }catch(Exception ex){android.widget.Toast.makeText(this,"Enter valid numeric marks",android.widget.Toast.LENGTH_LONG).show();}
  }).setNegativeButton("Cancel",null).show();
 }
 private void export(){
  Intent intent=new Intent(Intent.ACTION_CREATE_DOCUMENT);intent.addCategory(Intent.CATEGORY_OPENABLE);
  intent.setType("application/json");intent.putExtra(Intent.EXTRA_TITLE,"GCE_A_Level_Self_Reported_Score_History.json");startActivityForResult(intent,889);
 }
 @Override protected void onActivityResult(int req,int res,Intent data){
  super.onActivityResult(req,res,data);
  if(req==889&&res==RESULT_OK&&data!=null&&data.getData()!=null){
   try(OutputStream out=getContentResolver().openOutputStream(data.getData())){if(out!=null)out.write(scores.export().toString(2).getBytes("UTF-8"));}
   catch(Exception ex){android.widget.Toast.makeText(this,"Export failed",android.widget.Toast.LENGTH_SHORT).show();}
  }
 }
}
