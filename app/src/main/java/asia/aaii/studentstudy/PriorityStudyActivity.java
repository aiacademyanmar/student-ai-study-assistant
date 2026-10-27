package asia.aaii.studentstudy;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.List;

/** Native seven-day task plan, based entirely on local self-report data. */
public class PriorityStudyActivity extends Activity{
 private PriorityStudyPlan planner;
 private LinearLayout root,content;
 private int day=0;
 private int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
 private GradientDrawable shape(int color){GradientDrawable g=new GradientDrawable();g.setColor(color);g.setCornerRadius(dp(14));return g;}
 private TextView text(String label,int size,boolean bold){
  TextView t=new TextView(this);t.setText(label);t.setTextColor(0xFF132B58);t.setTextSize(size);
  if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setLineSpacing(dp(3),1.08f);return t;
 }
 private void add(String label,int size,int margin){
  TextView t=text(label,size,false);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(margin);content.addView(t,p);
 }
 private void btn(String label,Runnable click){
  TextView t=StudyDesign.action(this,label,label.startsWith("🤖"),click);
  LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);
  p.topMargin=dp(9);content.addView(t,p);
 }
 @Override public void onCreate(Bundle b){super.onCreate(b);planner=new PriorityStudyPlan(this);draw();}
 private void draw(){
  root=new LinearLayout(this);root.setOrientation(1);root.setBackgroundColor(StudyDesign.BG);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,i)->{
   android.graphics.Insets bars=i.getInsets(WindowInsets.Type.systemBars());
   v.setPadding(bars.left,bars.top,bars.right,bars.bottom);return i;});
  TextView header=text("‹    📅 GCE O Level · Priority Plan",19,true);
  header.setPadding(dp(18),dp(15),dp(12),dp(15));header.setBackgroundColor(-1);
  header.setOnClickListener(v->finish());root.addView(header);
  ScrollView sc=new ScrollView(this);content=new LinearLayout(this);content.setOrientation(1);
  content.setPadding(dp(15),dp(13),dp(15),dp(35));sc.addView(content);
  root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
  root.addView(StudyDesign.navigation(this,PriorityStudyActivity.class));
  PriorityStudyPlan.Summary stat=planner.summary();
  LinearLayout hero=StudyDesign.hero(this,"PERSONAL LEARNING DASHBOARD","7-Day Priority Plan","မပြီးသေးသော Study • Practice • Exam tasks");
  LinearLayout.LayoutParams hp=new LinearLayout.LayoutParams(-1,-2);hp.topMargin=dp(5);content.addView(hero,hp);
  add("Study / Practice / Exam self-reported progress: "+stat.percent()+
      "\nFinished subtopics: "+stat.completedSubtopics+"/"+stat.subtopics+
      "\nStudy "+stat.studyDone+" · Practice "+stat.practiceDone+" · Exam "+stat.examDone,13,9);
  content.addView(StudyDesign.progress(this,stat.studyDone+stat.practiceDone+stat.examDone,stat.totalSteps));
  add("⚠ Days are task batches, not scheduled calendar dates. Queue is locally generated. "+
      "Unmapped means no manual PDF reference, NOT missing official Cambridge syllabus coverage.",12,12);
  btn("⚙ Filter: "+(planner.subject()<0?"All O Level subjects":
      OLevel2026.SUBJECTS[planner.subject()].name)+" · Tasks/day: "+planner.perDay(),()->settings());
  List<SyllabusGapEngine.Entry> list=planner.queue();
  int per=planner.perDay(),totalDays=PriorityStudyPlan.DAYS,available=Math.min(list.size(),per*totalDays);
  add("Queued subtopics: "+list.size()+" · Displaying "+available+" highest-priority tasks",13,13);
  btn("◀ Day "+(day+1)+" / "+totalDays+" ▶  · Change day",()->new AlertDialog.Builder(this)
    .setTitle("Day / Task Batch")
    .setSingleChoiceItems(new String[]{"Day 1","Day 2","Day 3","Day 4","Day 5","Day 6","Day 7"},day,
      (dialog,selected)->{day=selected;dialog.dismiss();draw();})
    .setNegativeButton("Cancel",null).show());
  int start=day*per,stop=Math.min(list.size(),start+per);
  if(start>=stop){add("ဒီနေ့အတွက် Queue မရှိပါ။ ပြီးစီးမှုများကို ပြန်စစ်ပါ၊ သို့မဟုတ် Filter ပြောင်းပါ။",15,15);return;}
  for(int n=start;n<stop;n++){
   SyllabusGapEngine.Entry entry=list.get(n);
   String next=planner.nextStep(entry);
   if(next==null)continue;
   add("Task "+(n+1)+" · Priority "+planner.priority(entry)+
    "\n"+entry.subjectData.name+" ("+entry.subjectData.code+")"+
    "\n"+entry.topicData.name+" → "+entry.subtopicName+
    "\n"+entry.label()+"\n"+
    PriorityStudyPlan.stepTitle(next)+
    "\n✅ Progress "+planner.completedSteps(entry)+"/3",14,17);
   btn("🤖 AI နဲ့ "+PriorityStudyPlan.stepTitle(next)+" စတင်ရန်",()->
    AiProviderRouter.chooseAndRun(this,planner.remediationPrompt(entry,next),
     "priority_"+entry.subjectData.code+"_"+entry.topic+"_"+entry.subtopic+"_"+next,
     "O Level Priority Study"));
   btn("✓ "+PriorityStudyPlan.stepTitle(next)+" ပြီးစီးကြောင်း မှတ်ရန်",()->{
    planner.setComplete(entry,next,true);draw();
   });
   btn("📘 Open Coverage / Official PDF",()->{
    Intent i=new Intent(this,SyllabusCoverageActivity.class);
    i.putExtra("subject_index",entry.subject);i.putExtra("topic_index",entry.topic);
    i.putExtra("subtopic_index",entry.subtopic);startActivity(i);
   });
  }
  add("မှတ်ချက် — Study/Practice/Exam ကို ပြီးစီးကြောင်း နှိပ်မှသာ လက်ရှိဖုန်းထဲမှာ မှတ်သားပါတယ်။ "+
   "AI အဖြေမှန်ကန်မှု၊ တကယ့်စာမေးပွဲအောင်မြင်မှုကို စစ်ဆေးထားခြင်းမဟုတ်ပါ။",12,18);
 }
 private void settings(){
  String[] subjects=new String[OLevel2026.SUBJECTS.length+1];subjects[0]="All Subjects";
  for(int n=0;n<OLevel2026.SUBJECTS.length;n++)subjects[n+1]=
    OLevel2026.SUBJECTS[n].name+" · "+OLevel2026.SUBJECTS[n].code;
  new AlertDialog.Builder(this).setTitle("ဘာသာရပ်ရွေးရန်")
    .setSingleChoiceItems(subjects,planner.subject()+1,(d,index)->{
      d.dismiss();
      String[] amounts={"1 task/day","2 tasks/day","3 tasks/day","4 tasks/day"};
      new AlertDialog.Builder(this).setTitle("Tasks per day")
       .setSingleChoiceItems(amounts,planner.perDay()-1,(dialog,count)->{
        planner.configure(index-1,count+1);day=0;dialog.dismiss();draw();
       }).setNegativeButton("Cancel",null).show();
    }).setNegativeButton("Cancel",null).show();
 }
}
