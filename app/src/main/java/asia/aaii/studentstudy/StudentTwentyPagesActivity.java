package asia.aaii.studentstudy;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.text.TextWatcher;
import android.text.Editable;
import org.json.*;
import java.util.*;

/** Twenty-page Native Android UI. Visuals use bundled illustrations, native controls and honest data. */
public final class StudentTwentyPagesActivity extends Activity {
 private int page=1,level=0,subject=1,topic=0,question=0,selected=-1,caseIndex=0,quizCorrect=0;
 private String search="",language="MM";
 private LinearLayout root,body;private SharedPreferences prefs;
 private final ArrayList<Boolean> quizAnswers=new ArrayList<>();
 private int dp(int n){return StudyDesign.dp(this,n);}
 private TextView text(String s,int size,int color,boolean bold){return PremiumDesign.text(this,s,size,color,bold);}
 private void add(String s,int size){PremiumDesign.add(body,text(s,size,PremiumDesign.INK,size>=18),11);}
 private void action(String s,Runnable run){PremiumDesign.add(body,PremiumDesign.action(this,s,run,true),11);}
 private void link(String s,Runnable run){PremiumDesign.add(body,PremiumDesign.action(this,s,run,false),8);}
 private void launch(Class<?> c){startActivity(new Intent(this,c));}
 private void go(int n){page=n;render();}
 private String code(){return level==0?OLevel2026.SUBJECTS[subject].code:ALevel2026.SUBJECTS[subject].code;}
 private String subjectName(){return level==0?OLevel2026.SUBJECTS[subject].name:ALevel2026.SUBJECTS[subject].name;}
 private void hero(String title,String caption,int height){
  FrameLayout art=PremiumDesign.illustratedHero(this,title,caption,height);
  PremiumDesign.addHeight(body,art,height,8);
 }
 private void header(){
  LinearLayout h=PremiumDesign.row(this);h.setPadding(dp(10),dp(10),dp(10),dp(10));
  h.setBackground(PremiumDesign.grad(this,PremiumDesign.NAVY,PremiumDesign.BLUE,0));
  TextView back=text(page==1?"✦":"‹",27,-1,true);back.setGravity(Gravity.CENTER);
  h.addView(back,new LinearLayout.LayoutParams(dp(42),dp(46)));back.setOnClickListener(v->onBackPressed());
  TextView brand=text("Student AI\nStudy Assistant",17,-1,true);brand.setGravity(Gravity.CENTER_VERTICAL);
  h.addView(brand,new LinearLayout.LayoutParams(0,dp(49),1));
  TextView indicator=text(String.format(java.util.Locale.US,"%02d / 20",page),11,-1,true);
  indicator.setGravity(Gravity.CENTER);h.addView(indicator);
  root.addView(h);
 }
 @Override public void onCreate(Bundle b){
  super.onCreate(b);prefs=getSharedPreferences("student_twenty_pages_v231",MODE_PRIVATE);
  page=getIntent().getIntExtra("page",1);render();
 }
 private void render(){
  root=PremiumDesign.column(this);root.setBackground(PremiumDesign.grad(this,0xFFECF7FF,0xFFF7F0FF,0));setContentView(root);
  header();ScrollView sc=new ScrollView(this);sc.setFillViewport(false);
  body=PremiumDesign.column(this);body.setPadding(dp(13),dp(11),dp(13),dp(30));sc.addView(body);
  root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
  switch(page){
   case 1:splash();break;case 2:home();break;case 3:subjects();break;case 4:detail();break;
   case 5:lesson();break;case 6:practice();break;case 7:result();break;case 8:tutor();break;
   case 9:prompts();break;case 10:builder();break;case 11:cases();break;case 12:courses();break;
   case 13:audit();break;case 14:written();break;case 15:mock();break;case 16:progress();break;
   case 17:planner();break;case 18:settings();break;case 19:help();break;default:about();
  }
  footer();
 }
 private void footer(){
  LinearLayout nav=PremiumDesign.row(this);
  nav.setPadding(dp(5),dp(4),dp(5),dp(4));nav.setBackground(PremiumDesign.fill(this,-1,18));
  String[] labels={"Home","Original AI","GCE Study","Progress","More"};
  int[] destinations={2,9,3,16,18};
  String[] icons={"⌂","🤖","🎓","▥","⊞"};
  for(int i=0;i<labels.length;i++){
   int dest=destinations[i];LinearLayout cell=PremiumDesign.column(this);cell.setGravity(Gravity.CENTER);
   TextView icon=text(icons[i],23,page==dest?PremiumDesign.BLUE:PremiumDesign.MUTED,true);
   TextView label=text(labels[i],10,page==dest?PremiumDesign.BLUE:PremiumDesign.MUTED,page==dest);
   icon.setGravity(Gravity.CENTER);label.setGravity(Gravity.CENTER);
   cell.addView(icon);cell.addView(label);cell.setOnClickListener(v->go(dest));
   nav.addView(cell,new LinearLayout.LayoutParams(0,dp(54),1));
  }root.addView(nav);
 }
 private void tiles(String[] labels,String[] icons,int[] dest){
  int i=0;
  while(i<labels.length){
   LinearLayout row=PremiumDesign.row(this);
   int n=Math.min(4,labels.length-i);
   for(int j=0;j<n;j++){
    final int k=i+j;LinearLayout cell=PremiumDesign.column(this);cell.setGravity(Gravity.CENTER);
    cell.setBackground(PremiumDesign.grad(this,k%2==0?0xFF078DEF:0xFF9E46F4,k%3==0?0xFF0CE9CC:0xFF3551E9,17));
    TextView icon=text(icons[k],27,-1,true);icon.setGravity(Gravity.CENTER);
    TextView lab=text(labels[k],11,-1,true);lab.setGravity(Gravity.CENTER);
    cell.addView(icon);cell.addView(lab);cell.setOnClickListener(v->go(dest[k]));
    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(86),1);if(j>0)lp.leftMargin=dp(5);row.addView(cell,lp);
   }PremiumDesign.add(body,row,11);i+=n;
  }
 }
 private void splash(){
  FrameLayout banner=new FrameLayout(this);banner.setBackground(PremiumDesign.grad(this,PremiumDesign.BLUE,PremiumDesign.PURPLE,25));
  PremiumRobotView bot=new PremiumRobotView(this);banner.addView(bot);
  PremiumDesign.addHeight(body,banner,280,7);
  add("Learn Smarter with AI",27);add("Student AI Study Assistant\nO Level · AS/A Level · Myanmar",16);
  action("Get Started →",()->go(2));link("Explore the 20-page Learning App",()->go(2));
 }
 private void home(){
  hero("Learn • Practice\nAchieve Together","Original Student AI + GCE O/A Level",176);
  add("Choose Your Learning Space",19);
  action("🤖 Original Student AI · 100 Prompts / Case Studies / Courses",()->go(9));
  action("🎓 GCE O Level & A Level · 24 Subject Tracks",()->go(3));
  tiles(new String[]{"Prompt 100","Case Studies","Courses","AI Tutor"},new String[]{"✦","▤","▶","🤖"},new int[]{9,11,12,8});
  tiles(new String[]{"Practice","Written","Audit","Progress"},new String[]{"✓","✎","PDF","▥"},new int[]{6,14,13,16});
 }
 private void subjects(){
  hero("Explore Subjects","Cambridge 2026 learning guides",130);
  link(level==0?"Switch to AS/A Level":"Switch to O Level",()->{level=1-level;render();});
  for(int i=0;i<12;i++){
   int chosen=i;String name=level==0?OLevel2026.SUBJECTS[i].name:ALevel2026.SUBJECTS[i].name;
   String cd=level==0?OLevel2026.SUBJECTS[i].code:ALevel2026.SUBJECTS[i].code;
   link("📘 "+name+" ("+cd+") →",()->{subject=chosen;topic=0;go(4);});
  }
 }
 private void detail(){
  hero(subjectName(),code()+" • 2026 Syllabus",155);
  int[] totals=new OfflineQuizHistory(this).totals(level==0?"O":"A",code());
  add("MCQ History: "+totals[1]+"/"+totals[2]+" correct in "+totals[0]+" attempts",14);
  tiles(new String[]{"Syllabus PDF","Lessons","Quiz","AI Tutor"},new String[]{"PDF","▤","✓","🤖"},new int[]{13,5,6,8});
  int n=level==0?OLevel2026.SUBJECTS[subject].topics.length:ALevel2026.SUBJECTS[subject].modules.length;
  add("Topics",20);
  for(int i=0;i<n;i++){int x=i;String name=level==0?OLevel2026.SUBJECTS[subject].topics[i].name:ALevel2026.SUBJECTS[subject].modules[i].name;
   link((i+1)+". "+name,()->{topic=x;go(5);});}
 }
 private void lesson(){
  String title=level==0?OLevel2026.SUBJECTS[subject].topics[topic].name:ALevel2026.SUBJECTS[subject].modules[topic].name;
  hero(title,"Original AI explanations, examples, practice",150);
  String[] parts=level==0?OLevel2026.SUBJECTS[subject].topics[topic].subtopics:ALevel2026.SUBJECTS[subject].modules[topic].subtopics;
  for(String part:parts){
   link("📖 "+part,()->AiProviderRouter.chooseAndRun(this,StudyPromptComposer.build(level==1,subject,0,1,part,"",""),
      "lesson_"+code(),"2026 Study Tutor"));}
  action("Practice This Subject",()->go(6));
  link("Written Answer Exercises",()->go(14));
 }
 private void practice(){
  hero("Practice & Quiz","Original MCQs · Timer · Review",140);
  action("▶ Start Native Offline Quiz",()->launch(OfflineQuizActivity.class));
  link("Timed Mock Exam",()->go(15));
  link("Review Saved Results",()->go(7));
  link("Retry Weak Questions",()->launch(OfflineQuizActivity.class));
 }
 private void result(){
  hero("Your Quiz Results","Saved local attempts — no fabricated scores",138);
  int[] r=new OfflineQuizHistory(this).totals(null,null);
  add(r[2]>0?("Overall correct: "+r[1]+" / "+r[2]+" · "+Math.round(100*r[1]/(float)r[2])+"%"):"No completed quizzes recorded yet",19);
  link("Detailed Topic Learning Analytics",()->launch(TopicAnalyticsActivity.class));
  action("Practice Again",()->launch(OfflineQuizActivity.class));
 }
 private void tutor(){
  hero("AI Tutor","Myanmar explanations · step-by-step teaching",148);
  action("Open ChatGPT / Gemini / APP AI",()->AiProviderRouter.openMain(this));
  link("Build Your AI Study Prompt",()->go(10));
  link("Study a Cambridge Subject",()->go(3));
 }
 private void prompts(){
  hero("AI Prompt 100","100 original prompts for student learning",144);
  action("Build My Prompt",()->go(10));
  link("Offline Case Studies",()->go(11));
  link("OpenAI Academy Online Courses",()->go(12));
  for(PromptLibrary.Item t:PromptLibrary.ALL){
   link("#"+t.id+" "+t.title,()->AiProviderRouter.chooseAndRun(this,t.prompt(),"student_prompt_"+t.id,t.title));
  }
 }
 private void builder(){
  hero("Build My Prompt","Ready-to-use student AI prompt composer",132);
  action("Open Native AI Prompt Studio",()->launch(PromptStudioActivity.class));
  link("Prompt 100 Library",()->go(9));
 }
 private void cases(){
  hero("Offline Case Studies","8 complete original fictional learning scenarios",150);
  for(OfflineCaseStudies.Study study:OfflineCaseStudies.ALL){
   link("▤ "+study.title+" · "+study.group,()->showCase(study));
  }
 }
 private void showCase(OfflineCaseStudies.Study study){
  StringBuilder text=new StringBuilder(study.scenario+"\n\nPractice Tasks:\n");
  for(int i=0;i<study.tasks.length;i++)text.append(i+1).append(". ").append(study.tasks[i]).append("\n");
  text.append("\nSample considerations:\n").append(study.guide);
  new AlertDialog.Builder(this).setTitle(study.title).setMessage(text.toString())
  .setPositiveButton("Study with AI",(d,w)->AiProviderRouter.chooseAndRun(this,
    "Explain in Myanmar and ask questions ONE AT A TIME about this original fictional case:\n"+text,
    "case_"+study.title,study.title))
  .setNegativeButton("Close",null).show();
 }
 private void courses(){
  hero("OpenAI Online Courses","Official OpenAI Academy website",150);
  add("Course availability and access are managed by OpenAI Academy.",14);
  action("Open Academy Courses in Browser",()->AcademyBrowser.openCourses(this));
  link("AI Foundations",()->open("https://academy.openai.com"));
  link("Learning with AI Prompts",()->go(9));
 }
 private void audit(){
  hero("2026 Syllabus Audit","User-entered PDF Outcome Mapping",140);
  action("Open O / A Level Audit",()->{
   Intent i=new Intent(this,SyllabusAuditActivity.class);i.putExtra("level",level==0?"O":"A");startActivity(i);
  });
  link("Browse Subject References",()->go(3));
  add("Coverage matches and gaps are manually recorded; not Cambridge verified.",12);
 }
 private void written(){
  hero("Written Practice","48 original tasks · self-review",140);
  action("Native Written Answer Editor",()->launch(WrittenPracticeActivity.class));
  link("Written Answer AI Feedback",()->launch(WrittenPracticeActivity.class));
  link("View Self-Reviews",()->go(16));
 }
 private void mock(){
  hero("Timed Mock Exam","Original practice only — not official past papers",145);
  action("Start Timed Native Offline Test",()->launch(OfflineQuizActivity.class));
  link("Review Mock Scores",()->go(7));
 }
 private void progress(){
  hero("Learning Analytics","Actual saved results from your device",150);
  int[] q=new OfflineQuizHistory(this).totals(null,null);
  int[] wr=new WrittenPracticeStore(this).subjectStats(null,null);
  add("Quiz attempts: "+q[0],17);
  add("MCQs correct: "+q[1]+" / "+q[2],17);
  add("Written self-checks: "+wr[0],17);
  action("Full Topic Analytics",()->launch(TopicAnalyticsActivity.class));
  link("Weekly Study Plan",()->go(17));
 }
 private void planner(){
  hero("My Study Plan","Revision priority and next steps",145);
  action("Open Native Study Planner",()->launch(PriorityStudyActivity.class));
  link("7-Day Personalized Revision Prompt",()->launch(PromptStudioActivity.class));
 }
 private void settings(){
  hero("Settings & Profile","Language • AI • Privacy • Downloads",155);
  link("Main AI Provider · "+AiProviderRouter.mainLabel(this),()->AiProviderRouter.selectMain(this));
  link("Language: "+language,()->new AlertDialog.Builder(this).setItems(new String[]{"မြန်မာ","English"},(d,n)->{
    language=n==0?"MM":"EN";prefs.edit().putString("lang",language).apply();render();}).show());
  link("Offline Library",()->launch(OfflineReaderActivity.class));
  link("Student AI Original Interface",()->launch(CatalogActivity.class));
  link("Help & Community",()->go(19));
  link("About App",()->go(20));
 }
 private void help(){
  hero("Help & Community","Guides • Questions • Responsible AI",152);
  String[][] faqs={{"How to use AI prompts?","Choose an original prompt, select AI provider and paste/send the copied prompt."},
    {"How to practice offline?","Select Practice > Native Offline Quiz. Original questions and scores work without internet."},
    {"Where is my progress?","Scores are stored locally on this device. No server synchronization is enabled."},
    {"How do I find 2026 syllabuses?","Use GCE Study > Subject > Syllabus Audit, then check the official PDF."}};
  for(String[] f:faqs)link(f[0],()->new AlertDialog.Builder(this).setTitle(f[0]).setMessage(f[1]).setPositiveButton("OK",null).show());
  link("Official OpenAI Academy",()->AcademyBrowser.openCourses(this));
  link("About App",()->go(20));
 }
 private void about(){
  hero("Student AI Study Assistant","Independent Myanmar learning companion",160);
  add("Version 2.31.0 · 20-screen native UI",19);
  add("100 original AI Prompts · 8 offline fictional case studies · 24 GCE tracks · 192 MCQs · 48 written tasks",14);
  add("Not affiliated with or endorsed by Cambridge International or OpenAI.",12);
  link("Official Academy Courses",()->AcademyBrowser.openCourses(this));
  link("GitHub Project",()->open("https://github.com/aiacademyanmar/student-ai-study-assistant"));
  action("Return to Home",()->go(2));
 }
 private void open(String url){try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));}catch(Exception e){Toast.makeText(this,"Browser unavailable",Toast.LENGTH_SHORT).show();}}
 @Override public void onBackPressed(){if(page==1||page==2)super.onBackPressed();else go(2);}
}
