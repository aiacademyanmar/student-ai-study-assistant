package asia.aaii.studentstudy;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/** Cambridge 2026 AS/A level navigator. Study objectives below each module are app-created. */
public class ALevelSyllabusActivity extends Activity {
 private LinearLayout root,body;
 private int subject=-1,module=-1,subtopic=-1,lesson=-1;
 private String filter="ALL";
 private SharedPreferences progress;
 private int dp(int v){return StudyDesign.dp(this,v);}
 private void label(String s,int sz,boolean bold,int margin){
  TextView v=StudyDesign.text(this,s,sz,StudyDesign.INK,bold);
  LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(margin);
  body.addView(v,p);
 }
 private void btn(String s,boolean primary,Runnable click){
  TextView v=StudyDesign.action(this,s,primary,click);
  LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(9);body.addView(v,p);
 }
 private ALevel2026.Subject s(){return ALevel2026.SUBJECTS[subject];}
 private ALevel2026.Module m(){return s().modules[module];}
 @Override public void onCreate(Bundle b){
  super.onCreate(b);progress=getSharedPreferences("gce_a_study_2026_v220",MODE_PRIVATE);
  subject=getIntent().getIntExtra("subject_index",-1);
  if(subject<0||subject>=ALevel2026.SUBJECTS.length)subject=-1;
  draw();
 }
 private String progressKey(){return "done_"+s().code+"_"+m().name+"_"+m().subtopics[subtopic]+"_"+lesson;}
 private void copy(String value){
  ClipboardManager clip=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
  if(clip!=null)clip.setPrimaryClip(ClipData.newPlainText("A Level Prompt",value));
  Toast.makeText(this,"Prompt Copy ပြီးပါပြီ",Toast.LENGTH_SHORT).show();
 }
 private void dispatch(String mode){
  String prompt=ALevelPromptFactory.create(s(),m(),m().subtopics[subtopic],lesson,mode);
  AiProviderRouter.chooseAndRun(this,prompt,"a2026_"+s().code+"_"+module+"_"+subtopic+"_"+mode,"AS/A Level "+mode);
 }
 private void browser(String uri){
  try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(uri)));}
  catch(Exception e){Toast.makeText(this,"Browser not found",Toast.LENGTH_SHORT).show();}
 }
 private void back(){
  if(lesson>=0)lesson=-1;
  else if(subtopic>=0)subtopic=-1;
  else if(module>=0)module=-1;
  else if(subject>=0)subject=-1;
  else {finish();return;}
  draw();
 }
 @Override public void onBackPressed(){back();}
 private void draw(){
  root=StudyDesign.col(this);root.setBackgroundColor(StudyDesign.BG);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,i)->{
   android.graphics.Insets sys=i.getInsets(WindowInsets.Type.systemBars());
   v.setPadding(sys.left,sys.top,sys.right,sys.bottom);return i;});
  LinearLayout head=StudyDesign.row(this);head.setBackgroundColor(StudyDesign.WHITE);
  head.setPadding(dp(13),dp(10),dp(14),dp(10));
  TextView b=StudyDesign.text(this,"‹",29,StudyDesign.BLUE,true);
  b.setGravity(Gravity.CENTER);head.addView(b,new LinearLayout.LayoutParams(dp(40),dp(40)));
  b.setOnClickListener(v->back());
  TextView title=StudyDesign.text(this,"🎓  GCE AS & A Level · 2026",18,StudyDesign.INK,true);
  head.addView(title);root.addView(head);
  ScrollView sc=new ScrollView(this);body=StudyDesign.col(this);
  body.setPadding(dp(15),dp(15),dp(15),dp(25));sc.addView(body);
  root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
  if(subject<0){
   body.addView(StudyDesign.hero(this,"CAMBRIDGE INTERNATIONAL 2026",
     "AS & A Level · 12 Subjects","Learn · Practice · Exam · Revise  |  မြန်မာလို AI ဆရာ"));
   label(ALevel2026.SUBJECTS.length+" subjects · "+ALevel2026.moduleCount()+" study modules · "+
       ALevel2026.subtopicCount()+" app-defined subtopics",13,false,12);
   label("⚠ Topic module study index only. Official outcome-by-outcome PDF verification "
       +"is not complete. Routes and options are not all compulsory.",12,false,8);
   for(int x=0;x<ALevel2026.SUBJECTS.length;x++){
    final int i=x;ALevel2026.Subject item=ALevel2026.SUBJECTS[x];
    btn("📚  "+item.name+" ("+item.code+")\n"+item.modules.length+" Modules · AS / A Level · "+item.years,
     false,()->{subject=i;module=-1;draw();});
   }
  }else if(module<0){
   ALevel2026.Subject item=s();
   body.addView(StudyDesign.hero(this,"SYLLABUS "+item.code,item.name,
     "Cambridge "+item.years+" · AS / A / Options"));
   label("Exam and subject-specific requirements",16,true,15);
   label(item.examNotes,13,false,9);
   btn("📄 Official syllabus PDF / reference",true,()->browser(item.pdfUrl));
   btn("🌐 Official Cambridge subject page",false,()->browser(item.pageUrl));
   label("Select AS/A level or optional route",16,true,17);
   LinearLayout modes=StudyDesign.row(this);
   String[] fs={"ALL","AS","A","OPTIONS","ROUTES"};
   for(String f:fs){
    TextView v=StudyDesign.chip(this,f,f.equals(filter)?StudyDesign.BLUE:StudyDesign.PALE,
     f.equals(filter)?StudyDesign.WHITE:StudyDesign.INK);
    v.setOnClickListener(z->{filter=f;draw();});
    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,-2,1);lp.rightMargin=dp(3);modes.addView(v,lp);
   }body.addView(modes);
   label("OPTION / ROUTE အားလုံး မဖြစ်မနေသင်ရသည် မဟုတ်ပါ။ "
      +"2026 PDF မှ သင်ဖြေမည့် Paper Combination ကို အရင်ရွေးပါ။",12,false,9);
   for(int x=0;x<item.modules.length;x++){
    final int id=x;ALevel2026.Module g=item.modules[x];
    if(!item.show(g,filter))continue;
    btn("["+g.level+" · "+g.paper+"]  "+g.name+"\n"+g.subtopics.length+" Study subtopics",
      false,()->{module=id;subtopic=-1;draw();});
   }
  }else if(subtopic<0){
   ALevel2026.Module item=m();
   label(item.name,22,true,4);
   label(item.level+" · "+item.paper+" | "+s().code,13,false,6);
   label("App-created subtopics · Cambridge PDF reference, NOT verbatim learning outcomes.",12,false,8);
   for(int x=0;x<item.subtopics.length;x++){
    final int id=x;
    btn("📖  "+(x+1)+". "+item.subtopics[x],false,()->{subtopic=id;lesson=-1;draw();});
   }
  }else if(lesson<0){
   label(m().subtopics[subtopic],21,true,2);
   label("Subject "+s().code+"  >  "+m().name+" · "+m().level,13,false,9);
   for(int x=0;x<ALevel2026.LESSON_STAGES.length;x++){
    final int i=x;btn("Lesson "+(x+1)+" · "+ALevel2026.LESSON_STAGES[x],
      false,()->{lesson=i;draw();});
   }
  }else{
   body.addView(StudyDesign.hero(this,"AS & A LEVEL AI TUTOR",
    m().subtopics[subtopic],s().name+" · "+m().level+" / "+m().paper));
   label("Stage "+(lesson+1)+": "+ALevel2026.LESSON_STAGES[lesson],15,true,15);
   label("Original app learning targets: Explain the core theory; apply examples; "
      +"answer exam-relevant ORIGINAL questions and evaluate your work. Verify scope against official PDF.",13,false,8);
   btn("🤖 AI Teaching · မြန်မာလိုတန်းရှင်းပြ",true,()->dispatch("teach"));
   btn("✍ Interactive Exercises & Answers",false,()->dispatch("practice"));
   btn("📝 Paper-based Mock Exam Practice",false,()->dispatch("exam"));
   btn("🔬 Practical / Data / Source Skills",false,()->dispatch("practical"));
   btn("⭐ Revision & Active Recall",false,()->dispatch("revision"));
   btn("📋 Copy Teaching Prompt",false,()->copy(ALevelPromptFactory.create(
     s(),m(),m().subtopics[subtopic],lesson,"teach")));
   btn("📄 Official Cambridge PDF",false,()->browser(s().pdfUrl));
   btn(progress.getBoolean(progressKey(),false)?"✓ Completed · tap to undo":
      "○ Mark lesson completed (self report)",false,()->{
        String key=progressKey();progress.edit().putBoolean(key,!progress.getBoolean(key,false)).apply();draw();
      });
   label("AI backend: App AI requires configured HTTPS service. ChatGPT/Gemini "
    +"opens installed apps and may require paste+send. This is not an official Cambridge exam paper.",12,false,12);
  }
  root.addView(StudyDesign.navigation(this,ALevelSyllabusActivity.class));
 }
}
