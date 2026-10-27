package asia.aaii.studentstudy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;

/**
 * V2.30 unified premium native flow: 1 welcome, 2 home, 3 subjects,
 * 4 detail, 5 lesson, 6 quiz, 7 written, 8 AI prompt, 9 audit, 10 progress.
 * Existing V2.29 activity routes remain operational and persistent.
 */
public final class PremiumStudyActivity extends Activity {
 private LinearLayout base,body;
 private SharedPreferences prefs;
 private int scene=1,subject=1,topic=0,lesson=0,level=0;
 private String search="";
 private int dp(float n){return PremiumDesign.dp(this,n);}
 private boolean a(){return level==1;}
 private String name(){return a()?ALevel2026.SUBJECTS[subject].name:OLevel2026.SUBJECTS[subject].name;}
 private String code(){return a()?ALevel2026.SUBJECTS[subject].code:OLevel2026.SUBJECTS[subject].code;}
 private int topics(){return a()?ALevel2026.SUBJECTS[subject].modules.length:OLevel2026.SUBJECTS[subject].topics.length;}
 private String group(int i){return a()?ALevel2026.SUBJECTS[subject].modules[i].name:OLevel2026.SUBJECTS[subject].topics[i].name;}
 private String[] sections(int i){return a()?ALevel2026.SUBJECTS[subject].modules[i].subtopics:OLevel2026.SUBJECTS[subject].topics[i].subtopics;}
 private void gotoScreen(int dest){scene=dest;draw();}
 private void launch(Class<?> activity){startActivity(new Intent(this,activity));}
 private void add(View v,int top){PremiumDesign.add(body,v,top);}
 private void label(String val,int size,int color){add(PremiumDesign.text(this,val,size,color,size>=16),10);}
 private void button(String text,Runnable action){add(PremiumDesign.action(this,text,action,true),12);}
 private void secondary(String text,Runnable action){add(PremiumDesign.action(this,text,action,false),8);}
 @Override public void onCreate(Bundle state){
  super.onCreate(state);prefs=getSharedPreferences("premium_navigation_v230",MODE_PRIVATE);
  scene=getIntent().getIntExtra("page",prefs.getBoolean("onboarded",false)?2:1);
  draw();
 }
 private void draw(){
  base=PremiumDesign.column(this);base.setBackground(PremiumDesign.grad(this,0xFFE6F8FF,0xFFF2E9FF,0));
  setContentView(base);
  if(Build.VERSION.SDK_INT>=35)base.setOnApplyWindowInsetsListener((v,in)->{
   android.graphics.Insets q=in.getInsets(WindowInsets.Type.systemBars());
   v.setPadding(q.left,q.top,q.right,q.bottom);return in;});
  if(scene==1){welcome();return;}
  header();
  ScrollView sc=new ScrollView(this);sc.setFillViewport(false);
  body=PremiumDesign.column(this);body.setPadding(dp(13),dp(12),dp(13),dp(23));sc.addView(body);
  base.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
  switch(scene){
   case 2:home();break;
   case 3:subjects();break;
   case 4:detail();break;
   case 5:lessons();break;
   case 6:quiz();break;
   case 7:written();break;
   case 8:tutor();break;
   case 9:audit();break;
   default:progress();
  }
  footer();
 }
 private void header(){
  LinearLayout h=PremiumDesign.row(this);h.setPadding(dp(14),dp(9),dp(14),dp(9));
  h.setBackground(PremiumDesign.grad(this,PremiumDesign.BLUE,PremiumDesign.PURPLE,0));
  TextView back=PremiumDesign.text(this,scene==2?"✦":"‹",29,PremiumDesign.WHITE,true);
  back.setGravity(Gravity.CENTER);back.setOnClickListener(v->gotoScreen(scene==2?1:2));
  h.addView(back,new LinearLayout.LayoutParams(dp(44),dp(43)));
  String[] titles={"","","Student AI Study","ဘာသာရပ်များ","Subject Details","Study Lessons","Offline Quiz","Written Practice","AI Prompt Studio","Syllabus Audit","My Progress"};
  TextView t=PremiumDesign.text(this,titles[Math.min(10,scene)],19,PremiumDesign.WHITE,true);
  t.setGravity(Gravity.CENTER_VERTICAL);h.addView(t,new LinearLayout.LayoutParams(0,dp(45),1));
  TextView badge=PremiumDesign.text(this,String.format("%02d / 10",scene),12,PremiumDesign.WHITE,true);
  h.addView(badge);base.addView(h);
 }
 private void welcome(){
  FrameLayout hero=new FrameLayout(this);
  hero.setBackground(PremiumDesign.grad(this,PremiumDesign.BLUE,0xFF3D25E8,0));
  base.addView(hero,new LinearLayout.LayoutParams(-1,0,1));
  LinearLayout content=PremiumDesign.column(this);content.setGravity(Gravity.CENTER);
  content.setPadding(dp(25),dp(8),dp(25),dp(20));hero.addView(content,new FrameLayout.LayoutParams(-1,-1));
  PremiumRobotView robot=new PremiumRobotView(this);
  content.addView(robot,new LinearLayout.LayoutParams(-1,0,1.2f));
  TextView logo=PremiumDesign.text(this,"Student AI",40,PremiumDesign.WHITE,true);
  logo.setGravity(Gravity.CENTER);content.addView(logo);
  TextView title=PremiumDesign.text(this,"Study Assistant",24,PremiumDesign.WHITE,true);
  title.setGravity(Gravity.CENTER);content.addView(title);
  TextView tagline=PremiumDesign.text(this,"Learn Smarter with AI\nGCE O Level  |  AS/A Level\nမြန်မာလို AI သင်ကြားရေး",20,PremiumDesign.WHITE,true);
  tagline.setGravity(Gravity.CENTER);PremiumDesign.add(content,tagline,22);
  StudentsIllustrationView students=new StudentsIllustrationView(this);
  content.addView(students,new LinearLayout.LayoutParams(-1,0,1.2f));
  PremiumDesign.add(content,PremiumDesign.action(this,"Get Started   ›",()->{
   prefs.edit().putBoolean("onboarded",true).apply();gotoScreen(2);},true),8);
  PremiumDesign.add(content,PremiumDesign.action(this,"Continue to Learning",()->{
   prefs.edit().putBoolean("onboarded",true).apply();gotoScreen(2);},false),8);
 }
 private void artwork(String title,String subtitle,int height){
  FrameLayout hero=PremiumDesign.illustratedHero(this,title,subtitle,height);
  add(hero,7);
 }
 private void smallTiles(String[] icons,String[] names,Runnable[] actions,int[] colors){
  LinearLayout[] items=new LinearLayout[4];
  for(int i=0;i<4;i++)items[i]=PremiumDesign.quick(this,icons[i],names[i],"",colors[i],i%2==0?PremiumDesign.BLUE:PremiumDesign.PURPLE,actions[i]);
  PremiumDesign.rowOfFour(body,items,99);
 }
 private void home(){
  artwork("Learn Smarter\nwith AI","O Level  •  AS/A Level  •  Original Practice",202);
  smallTiles(new String[]{"📘","🎓","✓","▥"},new String[]{"O Level","A Level","Quiz","Audit"},new Runnable[]{
   ()->{level=0;gotoScreen(3);},()->{level=1;gotoScreen(3);},()->gotoScreen(6),()->gotoScreen(9)},
   new int[]{0xFF07C1EE,0xFF9B44EF,0xFFFFB022,0xFF00CDAA});
  PremiumDesign.headline(body,"AI Learning Tools","All",()->gotoScreen(8));
  smallTiles(new String[]{"🤖","✎","▥","✦"},new String[]{"AI Tutor","Written","Analytics","Prompts"},new Runnable[]{
   ()->gotoScreen(8),()->gotoScreen(7),()->gotoScreen(10),()->launch(PromptStudioActivity.class)},
   new int[]{0xFF06D5F2,0xFF9B52EF,0xFF0FC5A6,0xFF8754F5});
  PremiumDesign.headline(body,"Your Learning Journey",null,null);
  secondary("📄  Syllabus Audit  ›",()->gotoScreen(9));
  secondary("📈  My Study Progress  ›",()->gotoScreen(10));
  secondary("📅  7-Day Study Planner  ›",()->launch(PriorityStudyActivity.class));
  secondary("🎨  Share / Promotion",()->launch(PromotionActivity.class));
 }
 private void subjects(){
  artwork("Explore Subjects","2026 study index · 12 tracks per level",132);
  LinearLayout filters=PremiumDesign.row(this);
  for(int i=0;i<2;i++){final int choice=i;
   TextView tab=PremiumDesign.pill(this,i==0?"O Level (12)":"AS/A Level (12)",level==i);
   LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(43),1);
   if(i>0)lp.leftMargin=dp(8);filters.addView(tab,lp);
   tab.setGravity(Gravity.CENTER);tab.setOnClickListener(v->{level=choice;search="";draw();});
  }add(filters,12);
  EditText find=new EditText(this);find.setSingleLine(true);find.setTextSize(14);
  find.setHint("Search subjects by name / code");find.setText(search);
  find.setPadding(dp(17),dp(10),dp(14),dp(10));
  find.setBackground(PremiumDesign.outline(this,PremiumDesign.WHITE,PremiumDesign.BORDER,22));
  add(find,12);
  label("Available subjects  •  Current study map",16,PremiumDesign.INK);
  LinearLayout rows=PremiumDesign.column(this);add(rows,9);
  for(int i=0;i<12;i++){
   final int index=i;
   String sName=level==1?ALevel2026.SUBJECTS[i].name:OLevel2026.SUBJECTS[i].name;
   String sCode=level==1?ALevel2026.SUBJECTS[i].code:OLevel2026.SUBJECTS[i].code;
   LinearLayout line=PremiumDesign.row(this);line.setPadding(dp(10),dp(8),dp(9),dp(8));
   line.setBackground(PremiumDesign.outline(this,PremiumDesign.WHITE,PremiumDesign.BORDER,18));
   TextView icon=PremiumDesign.text(this,subjectIcon(sCode),26,PremiumDesign.BLUE,true);
   icon.setGravity(Gravity.CENTER);
   icon.setBackground(PremiumDesign.fill(this,0xFFE9EEFF,16));
   line.addView(icon,new LinearLayout.LayoutParams(dp(54),dp(55)));
   LinearLayout labels=PremiumDesign.column(this);labels.setPadding(dp(10),0,0,0);
   TextView name=PremiumDesign.text(this,sName,14,PremiumDesign.INK,true);name.setSingleLine(true);name.setEllipsize(android.text.TextUtils.TruncateAt.END);
   labels.addView(name);labels.addView(PremiumDesign.text(this,sCode+" · "+(level==1?ALevel2026.SUBJECTS[i].modules.length:OLevel2026.SUBJECTS[i].topics.length)+" modules",11,PremiumDesign.MUTED,false));
   int[] stats=new OfflineQuizHistory(this).totals(level==1?"A":"O",sCode);
   labels.addView(PremiumDesign.progress(this,stats[1],stats[2],PremiumDesign.CYAN,PremiumDesign.PURPLE));
   line.addView(labels,new LinearLayout.LayoutParams(0,-2,1));
   line.addView(PremiumDesign.progressCircle(this,stats[1],stats[2],PremiumDesign.GREEN,55),
       new LinearLayout.LayoutParams(dp(57),dp(57)));
   line.setOnClickListener(v->{subject=index;topic=0;gotoScreen(4);});
   PremiumDesign.add(rows,line,7);
  }
  find.addTextChangedListener(new android.text.TextWatcher(){
    public void beforeTextChanged(CharSequence s,int st,int c,int after){}
    public void onTextChanged(CharSequence s,int st,int before,int count){
      search=s.toString();for(int i=0;i<rows.getChildCount();i++){
       String n=level==1?ALevel2026.SUBJECTS[i].name:OLevel2026.SUBJECTS[i].name;
       String cd=level==1?ALevel2026.SUBJECTS[i].code:OLevel2026.SUBJECTS[i].code;
       rows.getChildAt(i).setVisibility(n.toLowerCase().contains(search.toLowerCase())||cd.contains(search)?View.VISIBLE:View.GONE);
      }}
    public void afterTextChanged(android.text.Editable e){}
  });
 }
 private String subjectIcon(String cd){
  if(cd.equals("4024")||cd.equals("9709")||cd.equals("4037")||cd.equals("9231"))return "∑";
  if(cd.equals("5090")||cd.equals("9700"))return "✿";
  if(cd.equals("5070")||cd.equals("9701"))return "⚗";
  if(cd.equals("5054")||cd.equals("9702"))return "⚛";
  if(cd.equals("1123")||cd.equals("9093"))return "A";
  if(cd.equals("2210")||cd.equals("9618"))return "⌘";
  return "▣";
 }
 private void detail(){
  artwork(name(),code()+"  •  Cambridge 2026 study reference",175);
  OfflineQuizHistory h=new OfflineQuizHistory(this);
  int[] stats=h.totals(a()?"A":"O",code());
  LinearLayout card=PremiumDesign.panel(this);
  card.addView(PremiumDesign.text(this,"Study & Quiz Progress",18,PremiumDesign.INK,true));
  card.addView(PremiumDesign.progressCircle(this,stats[1],stats[2],PremiumDesign.GREEN,110),new LinearLayout.LayoutParams(-1,dp(124)));
  card.addView(PremiumDesign.text(this,stats[0]+" quizzes taken  •  "+stats[1]+"/"+stats[2]+" MCQs correct",12,PremiumDesign.MUTED,false));
  add(card,11);
  smallTiles(new String[]{"PDF","▤","▶","✓"},new String[]{"Syllabus","Topics","Lessons","Quiz"},new Runnable[]{
   ()->openSyllabus(),()->gotoScreen(4),()->gotoScreen(5),()->gotoScreen(6)},
   new int[]{0xFFEE5797,0xFF00A9EA,0xFF8B58E9,0xFFFFAD27});
  PremiumDesign.headline(body,"Study Topics",null,null);
  int max=Math.min(14,topics());
  for(int i=0;i<max;i++){
   final int id=i;secondary((i+1)+".  "+group(i)+"   ›",()->{topic=id;gotoScreen(5);});
  }
  label("Study topics and progress are app learning aids, not verified Cambridge outcomes.",11,PremiumDesign.MUTED);
 }
 private void openSyllabus(){
  String url=a()?ALevel2026.SUBJECTS[subject].pdfUrl:OLevel2026.SUBJECTS[subject].officialUrl;
  try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));}
  catch(Exception e){android.widget.Toast.makeText(this,"Browser unavailable",0).show();}
 }
 private void lessons(){
  artwork(group(topic),"Lesson navigator • "+name(),153);
  LinearLayout tabs=PremiumDesign.row(this);
  String[] tabNames={"Lesson","Notes","Examples","Quiz"};
  for(int i=0;i<tabNames.length;i++){
   final int j=i;TextView t=PremiumDesign.pill(this,tabNames[i],j==lesson);
   t.setGravity(Gravity.CENTER);t.setOnClickListener(v->{if(j==3)gotoScreen(6);else{lesson=j;draw();}});
   LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(39),1);if(i>0)lp.leftMargin=dp(4);tabs.addView(t,lp);
  }add(tabs,13);
  PremiumDesign.headline(body,"Learn "+group(topic),null,null);
  String[] items=sections(topic);
  for(int i=0;i<items.length;i++){
   final int id=i;
   secondary((i+1)+".  "+items[i]+"  ›",()->{
    String prompt=StudyPromptComposer.build(a(),subject,0,1,items[id],"","");
    AiProviderRouter.chooseAndRun(this,prompt,"premium_lesson_"+code()+"_"+id,"2026 Lesson Tutor");
   });
  }
  button("✦ Open AI Teacher",()->gotoScreen(8));
  secondary("✎ Written Practice",()->gotoScreen(7));
 }
 private void quiz(){
  artwork("Quiz & Mock Exam","Offline questions  •  Timed practice",143);
  LinearLayout panel=PremiumDesign.panel(this);
  panel.addView(PremiumDesign.text(this,"192 Original MCQs · 24 Subject Tracks",18,PremiumDesign.INK,true));
  PremiumDesign.add(panel,PremiumDesign.text(this,"Questions and scoring work offline. Exam-paper practice is not official Cambridge past paper material.",12,PremiumDesign.MUTED,false),8);
  add(panel,13);
  button("▶  Start Offline Quiz",()->launch(OfflineQuizActivity.class));
  secondary("◷  Timed Mock Exam",()->launch(OfflineQuizActivity.class));
  secondary("✓  Retry Wrong Questions",()->launch(OfflineQuizActivity.class));
  secondary("📊  Question-level Analytics",()->launch(TopicAnalyticsActivity.class));
 }
 private void written(){
  artwork("Written Practice","Write • Review • Improve",152);
  label("48 Original Written Exercises",19,PremiumDesign.INK);
  LinearLayout c=PremiumDesign.panel(this);
  c.addView(PremiumDesign.text(this,"Sample: Solve and explain",15,PremiumDesign.INK,true));
  PremiumDesign.add(c,PremiumDesign.text(this,"5x − 4 = 21",26,PremiumDesign.BLUE,true),13);
  PremiumDesign.add(c,PremiumDesign.text(this,"Step-by-step writing • self review • AI feedback",12,PremiumDesign.MUTED,false),12);
  add(c,12);
  button("✎  Open Native Written Editor",()->launch(WrittenPracticeActivity.class));
  secondary("✦  Explain My Answer with AI",()->launch(WrittenPracticeActivity.class));
  secondary("📈  Written Response Analytics",()->launch(TopicAnalyticsActivity.class));
 }
 private void tutor(){
  FrameLayout banner=new FrameLayout(this);banner.setBackground(PremiumDesign.grad(this,PremiumDesign.BLUE,PremiumDesign.PURPLE,25));
  PremiumRobotView robot=new PremiumRobotView(this);
  banner.addView(robot,new FrameLayout.LayoutParams(-1,-1));PremiumDesign.addHeight(body,banner,200,6);
  label("AI Prompt Studio",25,PremiumDesign.INK);
  label("2026-aware prompt templates • Myanmar teaching",13,PremiumDesign.MUTED);
  button("✦  Open AI Prompt Studio",()->launch(PromptStudioActivity.class));
  secondary("🤖  Choose ChatGPT / Gemini / App AI",()->AiProviderRouter.openMain(this));
  secondary("📘  Subject Teaching",()->launch(PromptStudioActivity.class));
  secondary("✎  Written Answer Feedback",()->launch(WrittenPracticeActivity.class));
  secondary("◷  7-Day Revision Prompt",()->launch(PromptStudioActivity.class));
  label("AI provider output is not an endorsed Cambridge solution.",11,PremiumDesign.MUTED);
 }
 private void audit(){
  artwork("Syllabus Audit","Official PDFs • User-reviewed outcome notes",143);
  LinearLayout panel=PremiumDesign.panel(this);
  panel.addView(PremiumDesign.text(this,"2026 · 24 Subject References",20,PremiumDesign.INK,true));
  PremiumDesign.add(panel,PremiumDesign.text(this,"Coverage status below is user-entered and not independently verified by Cambridge.",12,PremiumDesign.MUTED,false),8);
  add(panel,12);
  button("📄  Open PDF Mapping & Audit",()->{
   Intent i=new Intent(this,SyllabusAuditActivity.class);i.putExtra("level",a()?"A":"O");startActivity(i);
  });
  secondary("📚  O Level Official Syllabus",()->{level=0;gotoScreen(3);});
  secondary("🎓  A Level Official Syllabus",()->{level=1;gotoScreen(3);});
  secondary("📊  Topic Coverage & Review",()->launch(SyllabusCoverageActivity.class));
 }
 private void progress(){
  artwork("My Learning Journey","Saved results • honest progress",155);
  OfflineQuizHistory q=new OfflineQuizHistory(this);
  int[] qStats=q.totals(null,null);int[] wr=new WrittenPracticeStore(this).subjectStats(null,null);
  LinearLayout[] stats=new LinearLayout[4];
  String[] numbers={"24",String.valueOf(qStats[0]),qStats[2]>0?Math.round(100f*qStats[1]/qStats[2])+"%":"—",String.valueOf(wr[0])};
  String[] labels={"Tracks","Quizzes","MCQ Accuracy","Written"};
  int[] colors={PremiumDesign.BLUE,PremiumDesign.CYAN,PremiumDesign.GREEN,PremiumDesign.PURPLE};
  for(int i=0;i<4;i++){
   final int k=i;stats[i]=PremiumDesign.column(this);stats[i].setGravity(Gravity.CENTER);
   stats[i].setBackground(PremiumDesign.fill(this,0xFFFFFFFF,16));
   stats[i].addView(PremiumDesign.text(this,numbers[k],18,colors[k],true));
   stats[i].addView(PremiumDesign.text(this,labels[k],10,PremiumDesign.MUTED,false));
  }PremiumDesign.rowOfFour(body,stats,72);
  PremiumDesign.headline(body,"Subject Performance",null,null);
  JSONArray all=q.all();
  int shown=0;
  for(int i=0;i<all.length()&&shown<6;i++){
   JSONObject x=all.optJSONObject(i);if(x==null)continue;
   String cd=x.optString("code");boolean seen=false;
   for(int j=0;j<i;j++){JSONObject y=all.optJSONObject(j);if(y!=null&&cd.equals(y.optString("code"))){seen=true;break;}}
   if(seen)continue;shown++;
   int[] totals=q.totals(null,cd);
   LinearLayout v=PremiumDesign.panel(this);
   v.addView(PremiumDesign.text(this,cd+" · "+totals[1]+"/"+totals[2]+" MCQs",13,PremiumDesign.INK,true));
   PremiumDesign.add(v,PremiumDesign.progress(this,totals[1],totals[2],PremiumDesign.BLUE,PremiumDesign.CYAN),8);
   add(v,9);
  }
  if(shown==0)label("No quiz results yet. Start a quiz to see actual progress.",13,PremiumDesign.MUTED);
  button("📈  Detailed Topic Analytics",()->launch(TopicAnalyticsActivity.class));
  secondary("▣  Saved Written Self Reviews",()->launch(WrittenPracticeActivity.class));
 }
 private void footer(){
  LinearLayout nav=PremiumDesign.row(this);nav.setPadding(dp(5),dp(5),dp(5),dp(5));
  nav.setBackground(PremiumDesign.outline(this,PremiumDesign.WHITE,PremiumDesign.BORDER,22));
  String[] titles={"Home","Subjects","AI Tutor","More"};String[] icons={"⌂","▤","🤖","⊞"};
  int[] pages={2,3,8,10};
  for(int i=0;i<4;i++){
   int dest=pages[i];LinearLayout cell=PremiumDesign.column(this);cell.setGravity(Gravity.CENTER);
   cell.setBackground(scene==dest?PremiumDesign.grad(this,PremiumDesign.BLUE,PremiumDesign.PURPLE,17):PremiumDesign.fill(this,PremiumDesign.WHITE,17));
   int col=scene==dest?PremiumDesign.WHITE:PremiumDesign.BLUE;
   TextView ico=PremiumDesign.text(this,icons[i],24,col,true);ico.setGravity(Gravity.CENTER);cell.addView(ico);
   TextView name=PremiumDesign.text(this,titles[i],10,col,scene==dest);name.setGravity(Gravity.CENTER);cell.addView(name);
   cell.setContentDescription(titles[i]);cell.setOnClickListener(v->gotoScreen(dest));
   LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(63),1);if(i>0)lp.leftMargin=dp(3);
   nav.addView(cell,lp);
  }
  base.addView(nav,new LinearLayout.LayoutParams(-1,-2));
 }
 @Override public void onBackPressed(){if(scene==1||scene==2){super.onBackPressed();}else gotoScreen(scene==5?4:2);}
}
