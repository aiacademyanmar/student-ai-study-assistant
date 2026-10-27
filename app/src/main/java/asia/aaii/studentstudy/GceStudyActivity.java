package asia.aaii.studentstudy;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Gravity;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
public class GceStudyActivity extends Activity {
 private int level=1,subject=-1,chapter=-1,lesson=-1;
 private LinearLayout root,body;
 private SharedPreferences prefs;
 private final int BLUE=0xFF125AE3,NAVY=0xFF132B58,MUTED=0xFF667893,BG=0xFFF3F7FF,WHITE=Color.WHITE;
 private int d(float n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
 private GradientDrawable bg(int color){GradientDrawable x=new GradientDrawable();x.setColor(color);x.setCornerRadius(d(15));return x;}
 private TextView t(String value,int size,int color,boolean bold){TextView v=new TextView(this);v.setText(value);v.setTextSize(size);v.setTextColor(color);if(bold)v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);v.setLineSpacing(d(4),1.08f);return v;}
 private void add(LinearLayout host,View v,int top){LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.topMargin=d(top);host.addView(v,lp);}
 private TextView button(String label,Runnable run){TextView v=t(label,15,NAVY,true);v.setPadding(d(14),d(14),d(14),d(14));v.setBackground(bg(WHITE));v.setOnClickListener(w->run.run());return v;}
 private String lv(){return level==0?"O":"A";}
 private GceCurriculum.Subject currentSubject(){return GceCurriculum.at(lv(),subject);}
 @Override public void onCreate(Bundle state){super.onCreate(state);prefs=getSharedPreferences("gce_v26",MODE_PRIVATE);getWindow().setStatusBarColor(BG);getWindow().setNavigationBarColor(WHITE);getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);draw();}
 private void draw(){
  root=new LinearLayout(this);root.setOrientation(1);root.setBackgroundColor(BG);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,insets)->{android.graphics.Insets b=insets.getInsets(WindowInsets.Type.systemBars());v.setPadding(b.left,b.top,b.right,b.bottom);return insets;});
  LinearLayout header=new LinearLayout(this);header.setGravity(Gravity.CENTER_VERTICAL);header.setPadding(d(14),d(15),d(14),d(15));header.setBackgroundColor(WHITE);
  TextView back=t("‹",32,BLUE,true);header.addView(back,new LinearLayout.LayoutParams(d(36),-2));back.setOnClickListener(v->back());
  TextView title=t("🎓 GCE "+lv()+" LEVEL",20,NAVY,true);header.addView(title,new LinearLayout.LayoutParams(0,-2,1));root.addView(header);
  ScrollView scroll=new ScrollView(this);body=new LinearLayout(this);body.setOrientation(1);body.setPadding(d(16),d(16),d(16),d(26));scroll.addView(body);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
  if(subject<0)levels();else if(chapter<0)chapters();else if(lesson<0)lessons();else lessonView();
  footer();
 }
 private void levels(){
  add(body,t("GCE O Level နှင့် A Level",24,NAVY,true),0);
  add(body,t("ဘာသာရပ် → Chapter → Lesson → Exercises → AI ဆရာ",14,MUTED,false),8);
  LinearLayout choose=new LinearLayout(this);
  for(int i=0;i<2;i++){final int at=i;TextView b=button(i==0?"O LEVEL":"A LEVEL",()->{level=at;subject=-1;draw();});b.setGravity(Gravity.CENTER);b.setTextColor(level==i?WHITE:BLUE);b.setBackground(bg(level==i?BLUE:WHITE));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,d(55),1);p.setMargins(d(3),0,d(3),0);choose.addView(b,p);}add(body,choose,16);
  add(body,t("ဘာသာရပ် "+GceCurriculum.subjectCount(lv())+" ခု",19,NAVY,true),20);
  for(int i=0;i<GceCurriculum.subjectCount(lv());i++){final int n=i;GceCurriculum.Subject s=GceCurriculum.at(lv(),i);add(body,button("📘  "+s.name+"  ·  "+s.code+"\n     Chapter "+s.chapters.length+" ခု",()->{subject=n;chapter=-1;draw();}),10);}
  add(body,t("Cambridge-oriented နမူနာလေ့လာရေးလမ်းကြောင်းဖြစ်ပြီး Official Syllabus အပြည့်အစုံ မဟုတ်ပါ။ Exam Board နှင့် Syllabus Year ကို စစ်ဆေးပါ။",12,MUTED,false),20);
 }
 private void chapters(){
  GceCurriculum.Subject s=currentSubject();add(body,t(s.name+" ("+s.code+")",22,NAVY,true),0);
  add(body,t("CHAPTERS · "+s.chapters.length+" ခု",13,BLUE,true),10);
  for(int i=0;i<s.chapters.length;i++){final int n=i;GceCurriculum.Chapter c=s.chapters[i];add(body,button("Chapter "+(i+1)+"  "+c.title+"\n"+c.focus,()->{chapter=n;lesson=-1;draw();}),13);}
 }
 private void lessons(){
  GceCurriculum.Chapter c=currentSubject().chapters[chapter];add(body,t(c.title,22,NAVY,true),0);add(body,t(c.focus,14,MUTED,false),8);
  for(int i=0;i<GceCurriculum.LESSONS.length;i++){final int n=i;add(body,button("📖  "+GcePromptFactory.lessonTitle(c,i),()->{lesson=n;draw();}),13);}
  add(body,t("သင်ကြားမှု: မြန်မာဘာသာရှင်းပြချက် + English Exam Keywords + Worked Example + Guided Questions",13,MUTED,false),18);
 }
 private String prompt(int kind){
  GceCurriculum.Subject s=currentSubject();GceCurriculum.Chapter c=s.chapters[chapter];
  return kind<0?GcePromptFactory.explain(s,c,lesson):GcePromptFactory.practice(s,c,lesson,kind);
 }
 private void copy(String prompt){ClipboardManager c=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);c.setPrimaryClip(ClipData.newPlainText("GCE AI Tutor Prompt",prompt));Toast.makeText(this,"AI Prompt Copy ပြီးပါပြီ",Toast.LENGTH_SHORT).show();}
 private void chat(String prompt){AiProviderRouter.chooseAndRun(this,prompt,"gce_"+lv()+"_"+currentSubject().code+"_"+chapter+"_"+lesson+"_lesson","GCE Lesson");}
 private void startInteractive(String name,String title,String prompt){
  GceCurriculum.Subject s=currentSubject();
  AiProviderRouter.chooseAndRun(this,prompt,"gce_"+lv()+"_"+s.code+"_"+chapter+"_"+lesson+"_"+name,title);
 }
 private void browserPractice(String text){
  copy(text);
  try{
   Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse("https://chatgpt.com/studymode"));
   i.addCategory(Intent.CATEGORY_BROWSABLE);
   startActivity(i);
   Toast.makeText(this,"ChatGPT တွင် Prompt Paste လုပ်ပြီး စလေ့ကျင့်ပါ",Toast.LENGTH_LONG).show();
  }catch(Exception ex){
   Toast.makeText(this,"Browser မတွေ့ပါ။ Copy ပြီးသော Prompt ကို AI Chat မှာ Paste လုပ်နိုင်သည်။",Toast.LENGTH_LONG).show();
  }
 }
 private void modeCard(String name,String title,String detail,String prompt,int tint){
  LinearLayout card=new LinearLayout(this);
  card.setOrientation(LinearLayout.VERTICAL);
  card.setBackground(bg(tint));
  card.setPadding(d(14),d(17),d(14),d(15));
  add(card,t(title,19,NAVY,true),0);
  add(card,t(detail,13,NAVY,false),9);
  add(card,button("🤖  AI နှင့် အပြန်အလှန် စမ်းမည်",()->startInteractive(name,title,prompt)),13);
  add(card,button("📋  Prompt Copy",()->copy(prompt)),9);
  add(card,button("⭐ AI Provider ရွေးချယ်၍ သုံးမည်",()->AiProviderRouter.chooseAndRun(this,prompt,"gce_"+lv()+"_"+currentSubject().code+"_"+chapter+"_"+lesson+"_"+name,title)),9);
  add(card,button("👁  Prompt အပြည့်ကြည့်ရန်",()->new AlertDialog.Builder(this)
      .setTitle(title).setMessage(prompt)
      .setPositiveButton("Copy",(dialog,which)->copy(prompt))
      .setNegativeButton("ပိတ်မည်",null).show()),9);
  add(body,card,14);
 }
 private void lessonView(){
  GceCurriculum.Chapter c=currentSubject().chapters[chapter];add(body,t(GcePromptFactory.lessonTitle(c,lesson),21,NAVY,true),0);add(body,t(c.title+" · "+c.focus,14,MUTED,false),12);
  add(body,t("AI က တန်းရှင်းပြမည့် Lesson Prompt",19,NAVY,true),20);
  add(body,t("၁။ Learning Objectives\n၂။ မြန်မာဘာသာ အခြေခံရှင်းပြချက်\n၃။ Worked Examples ၂ ခု\n၄။ အမှားများနှင့် Exam Tips\n၅။ လေ့ကျင့်ခန်း ၃ ခု၊ Model Answers နှင့် Mini Summary",15,NAVY,false),11);
  add(body,button("🤖  Lesson တစ်ခုလုံး AI ရှင်းပြရန်",()->chat(prompt(-1))),18);
  add(body,button("📋  Lesson Prompt Copy",()->copy(prompt(-1))),9);
  add(body,t("PROMPT PREVIEW · AI က အောက်ပါညွှန်ကြားချက်အတိုင်း မြန်မာလို တန်းရှင်းပြပါမည်။",13,BLUE,true),16);
  TextView preview=t(prompt(-1),13,NAVY,false);preview.setPadding(d(13),d(13),d(13),d(13));preview.setBackground(bg(WHITE));add(body,preview,10);
  add(body,t("📝 PRACTICE & ANSWERS",20,NAVY,true),25);
  GceCurriculum.Subject subjectInfo=currentSubject();
  String activePractice=GcePracticeModes.practiceAndAnswers(subjectInfo,c,lesson);
  modeCard("practice","Practice & Answers · ၆ မေးခွန်း",
    "မေးခွန်းတစ်ခုချင်း ဖြေ၊ Hint ယူ၊ အမှားရှင်း၊ ၁၂ မှတ်အထိ Score စစ်နိုင်မည့် AI Practice Prompt ဖြစ်သည်။",
    activePractice,0xFFEAF5FF);
  add(body,t("⭐ EXAM TIPS & SUMMARY",20,NAVY,true),25);
  String activeRevision=GcePracticeModes.examTipsAndSummary(subjectInfo,c,lesson);
  modeCard("revision","Exam Tips & Summary · Revision",
    "မြန်မာလို Summary၊ Exam Tips ၅ ခု၊ Active Recall ၄ ခု၊ အဖြေစစ်ပြီး ၅ မိနစ် Revision Plan ရမည်။",
    activeRevision,0xFFEBF8F2);
  add(body,t("EXERCISES · စမ်းသပ်လေ့ကျင့်ရေး",19,NAVY,true),24);
  for(int i=0;i<GceCurriculum.EXERCISES.length;i++){final int x=i;add(body,button((i+1)+". "+GceCurriculum.EXERCISES[i],()->chat(prompt(x))),9);}
  String key=lv()+":"+subject+":"+chapter+":"+lesson;
  add(body,button(prefs.getBoolean(key,false)?"✓ လေ့လာပြီးပါပြီ":"✓ လေ့လာပြီးဟု မှတ်သားမည်",()->{prefs.edit().putBoolean(key,true).apply();Toast.makeText(this,"Progress သိမ်းဆည်းပြီးပါပြီ",Toast.LENGTH_SHORT).show();draw();}),20);
  add(body,t("AI ဖြေဆိုမှုရရှိရန် Native Chat တွင် HTTPS Backend ချိတ်ဆက်ရန်လိုအပ်သည်။ Prompt ကို Copy ပြုလုပ်၍ အခြား AI Study Mode တွင်လည်း အသုံးပြုနိုင်သည်။",12,MUTED,false),16);
 }
 private void footer(){
  LinearLayout bar=new LinearLayout(this);bar.setBackgroundColor(WHITE);bar.setPadding(d(3),d(4),d(3),d(4));
  String[] names={"ပင်မ","GCE","AI ဆရာ","သင်တန်း","ပိုမို"};String[] icons={"⌂","▤","✦","▣","☰"};
  for(int i=0;i<5;i++){final int n=i;LinearLayout tile=new LinearLayout(this);tile.setOrientation(1);tile.setGravity(Gravity.CENTER);TextView icon=t(icons[i],22,i==1?BLUE:MUTED,true);icon.setGravity(Gravity.CENTER);tile.addView(icon);TextView label=t(names[i],10,i==1?BLUE:MUTED,i==1);label.setGravity(Gravity.CENTER);tile.addView(label);tile.setOnClickListener(v->nav(n));bar.addView(tile,new LinearLayout.LayoutParams(0,d(62),1));}root.addView(bar);
 }
 private void nav(int n){if(n==1){level=1;subject=-1;chapter=-1;lesson=-1;draw();return;}if(n==3){AcademyBrowser.openCourses(this);return;}if(n==2){AiProviderRouter.openMain(this);return;}Intent i=new Intent(this,CatalogActivity.class);if(n==4)i.putExtra("open_page","profile");startActivity(i);finish();}
 private void back(){if(lesson>=0)lesson=-1;else if(chapter>=0)chapter=-1;else if(subject>=0)subject=-1;else {finish();return;}draw();}
 @Override public void onBackPressed(){back();}
}
