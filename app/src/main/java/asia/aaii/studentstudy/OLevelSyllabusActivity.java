package asia.aaii.studentstudy;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
/** O Level 2026 curriculum prompt navigation; all AI output requires a chosen provider. */
public class OLevelSyllabusActivity extends Activity{
 int subject=-1,topic=-1,subtopic=-1,stage=-1;
 LinearLayout root,body;
 SharedPreferences prefs;
 final int NAVY=0xFF102957,BLUE=0xFF155FE5,GREY=0xFF667590,WHITE=Color.WHITE;
 int dp(int x){return (int)(x*getResources().getDisplayMetrics().density+.5f);}
 GradientDrawable bg(int color){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(14));return d;}
 TextView text(String s,int size,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextColor(NAVY);t.setTextSize(size);if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setLineSpacing(dp(3),1.08f);return t;}
 void add(String s,int size,boolean bold,int margin){TextView t=text(s,size,bold);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.topMargin=dp(margin);body.addView(t,lp);}
 void btn(String s,Runnable action){TextView v=text(s,15,false);v.setPadding(dp(15),dp(15),dp(12),dp(15));v.setBackground(bg(WHITE));v.setOnClickListener(w->action.run());LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.topMargin=dp(9);body.addView(v,lp);}
 @Override public void onCreate(Bundle b){super.onCreate(b);prefs=getSharedPreferences("gce_o_2026_v211",MODE_PRIVATE);draw();}
 String context(){
  OLevel2026.Subject s=OLevel2026.SUBJECTS[subject];OLevel2026.Topic t=s.topics[topic];
  return "Cambridge O Level | "+s.name+" ("+s.code+") | exam-year 2026 | Topic: "+t.name+" | Subtopic: "+t.subtopics[subtopic]+" | Lesson: "+OLevel2026.LESSON_STAGES[stage];
 }
 String instruction(String mode){
  String detail=mode.equals("teach")?
      "မြန်မာလို အခြေခံသဘောတရားကို တန်းရှင်းပြပါ။ Learning objectives, English exam terms, definitions, worked examples ၂ ခု, common errors, ၃ မေးခွန်းနှင့် model answers, exam tips နဲ့ lesson summary ကို တစ်ကြိမ်တည်း ရှင်းပြပါ။":
      mode.equals("practice")?
      "Practice mode ကို ယခုစတင်ပါ။ မူရင်းလေ့ကျင့်ခန်း ၆ ခု (Concept ၂, MCQ ၂, Structured ၂) ကို တစ်ခုချင်းစီမေးပါ။ ကျောင်းသားဖြေမှ မှန်/မှား၊ ၀–၂ မှတ်၊ hint၊ correction၊ worked answer ပြပါ။ ၁၂ မှတ်အထိ Score စု၍ လိုအပ်သော revision နည်းကို အဆုံးတွင်ပြပါ။":
      mode.equals("exam")?
      "Exam practice ၂၀ မိနစ်စာ ကိုယ်ပိုင်မေးခွန်း ၅ ခု ပြုစုပါ။ ပထမ Q1 တစ်ခုတည်းမေးပြီး အဖြေစောင့်ပါ။ တစ်ခုပြီးတစ်ခု အမှတ်ပေး၍ နောက်ဆုံး model solutions, exam command words, mark allocation သရုပ်ပြ, improvement plan ပြပါ။ မူရင်း Cambridge past paper ဟု မဆိုပါနှင့်။":
      "Revision summary ကို မြန်မာလို တန်းရှင်းပြပါ။ ၅ ချက် summary, keyword/definition ၅ ခု, exam tips ၅ ခု, mistake checklist, memory trick ပြီးရင် active recall Q1 တစ်ခုတည်းမေးပါ။ Q1–Q4 ကို အဖြေပြန်လာမှ ဆက်မေးပြီး ၈ မှတ်အထိ အမှတ်ပေးပါ။";
  return "သင်သည် GCE O Level အတွက် မြန်မာဘာသာ AI ဆရာ ဖြစ်သည်။\n"+
      context()+"\n"+
      "ဤ Subtopic သည် လေ့လာရေးအတွက် အဖွဲ့အစည်းက ခွဲထားသောလမ်းကြောင်းဖြစ်သည်။ Official Cambridge topic/subtopic/assessment scope ကို syllabus PDF နှင့်သာ တိုက်ဆိုင်စစ်ဆေးပါ။ Official past papers, mark schemes, exam year-specific claims မဖန်တီးပါနှင့်။\n"+
      detail+"\nမြန်မာစာအဓိကသုံးပြီး technical terms တွေကို English ဖြင့် မှန်ကန်စွာ ရေးပါ။ မမှန်ကန်နိုင်သော အချက်အလက်ကို မသေချာကြောင်း ရှင်းပြပါ။";
 }
 void run(String mode){
  if(subject<0||topic<0||subtopic<0||stage<0)return;
  String session="olevel_"+OLevel2026.SUBJECTS[subject].code+"_"+topic+"_"+subtopic+"_"+stage+"_"+mode;
  AiProviderRouter.chooseAndRun(this,instruction(mode),session,"O Level "+mode);
 }
 void copy(String s){
  ClipboardManager clipboard=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
  if(clipboard!=null)clipboard.setPrimaryClip(ClipData.newPlainText("O Level prompt",s));
  Toast.makeText(this,"Prompt ကို Copy ပြီးပါပြီ",Toast.LENGTH_SHORT).show();
 }
 void back(){if(stage>=0)stage=-1;else if(subtopic>=0)subtopic=-1;else if(topic>=0)topic=-1;else if(subject>=0)subject=-1;else{finish();return;}draw();}
 void draw(){
  root=new LinearLayout(this);root.setOrientation(1);root.setBackgroundColor(0xFFF2F6FD);
  setContentView(root);getWindow().setStatusBarColor(WHITE);getWindow().setNavigationBarColor(WHITE);
  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,insets)->{android.graphics.Insets i=insets.getInsets(WindowInsets.Type.systemBars());v.setPadding(i.left,i.top,i.right,i.bottom);return insets;});
  LinearLayout header=new LinearLayout(this);header.setGravity(Gravity.CENTER_VERTICAL);header.setPadding(dp(12),dp(12),dp(12),dp(12));header.setBackgroundColor(WHITE);
  TextView b=text("‹",30,true);header.addView(b,new LinearLayout.LayoutParams(dp(46),dp(44)));b.setOnClickListener(v->back());
  TextView h=text("📘 GCE O LEVEL · 2026",19,true);header.addView(h);root.addView(header);
  ScrollView scroll=new ScrollView(this);body=new LinearLayout(this);body.setOrientation(1);body.setPadding(dp(15),dp(13),dp(15),dp(30));scroll.addView(body);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
  if(subject<0){
   add("Subject → Topic → Subtopic → Lesson → Exercises → Exam Practice",16,true,4);
   add(OLevel2026.SUBJECTS.length+" Subjects · "+OLevel2026.topicCount()+" Topic groups · "+OLevel2026.subtopicCount()+" Study subtopics · "+OLevel2026.lessonCount()+" Lesson stages",13,false,9);
   add("ဤအကြောင်းအရာများသည် 2026 Cambridge O Level ကို ရည်ညွှန်းသော Study Map များဖြစ်ပြီး Official Syllabus အပြည့်အစုံ ဟု မဆိုပါ။ Topic နှင့် Assessment Scope ကို official PDF ဖြင့် စစ်ပါ။",13,false,12);
   for(int x=0;x<OLevel2026.SUBJECTS.length;x++){final int i=x;OLevel2026.Subject s=OLevel2026.SUBJECTS[i];btn("📚 "+s.name+" · "+s.code+"\n"+s.topics.length+" Topic groups · syllabus "+s.years,()->{subject=i;draw();});}
  }else if(topic<0){
   OLevel2026.Subject s=OLevel2026.SUBJECTS[subject];
   add(s.name+" · "+s.code,22,true,0);add("Reference period: "+s.years,12,false,6);
   btn("🌐 Cambridge Subject Page / Official PDF",()->{try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(s.officialUrl)));}catch(Exception ex){Toast.makeText(this,"Browser မတွေ့ပါ",Toast.LENGTH_SHORT).show();}});
   for(int x=0;x<s.topics.length;x++){final int i=x;OLevel2026.Topic t=s.topics[i];btn("Topic "+(x+1)+" · "+t.name+"\n"+t.subtopics.length+" Study subtopics",()->{topic=i;draw();});}
  }else if(subtopic<0){
   OLevel2026.Topic t=OLevel2026.SUBJECTS[subject].topics[topic];
   add(t.name,22,true,4);add("သင်ခန်းစာအဖြစ် ခွဲထားသော Subtopics",12,false,8);
   for(int x=0;x<t.subtopics.length;x++){final int i=x;btn("Subtopic "+(x+1)+" · "+t.subtopics[i],()->{subtopic=i;draw();});}
  }else if(stage<0){
   OLevel2026.Topic t=OLevel2026.SUBJECTS[subject].topics[topic];
   add(t.subtopics[subtopic],22,true,4);
   for(int x=0;x<OLevel2026.LESSON_STAGES.length;x++){final int i=x;btn("Lesson "+(x+1)+" · "+OLevel2026.LESSON_STAGES[x],()->{stage=i;draw();});}
  }else{
   add(context(),19,true,4);
   add("AI Provider: "+AiProviderRouter.mainLabel(this)+" (App AI / ChatGPT / Gemini)",13,false,12);
   btn("🤖 Lesson ကို မြန်မာလို AI တန်းရှင်းပြရန်",()->run("teach"));
   btn("✍ Practice & Answers — AI ဖြင့် အပြန်အလှန်လေ့ကျင့်ရန်",()->run("practice"));
   btn("📝 Exam Practice — Timed Questions",()->run("exam"));
   btn("⭐ Exam Tips & Summary — Active Recall",()->run("revision"));
   btn("📋 Teaching Prompt Copy",()->copy(instruction("teach")));
   btn("📋 Practice Prompt Copy",()->copy(instruction("practice")));
   btn("📋 Exam Prompt Copy",()->copy(instruction("exam")));
   btn("📋 Revision Prompt Copy",()->copy(instruction("revision")));
   String key="complete_"+subject+"_"+topic+"_"+subtopic+"_"+stage;
   btn(prefs.getBoolean(key,false)?"✓ Lesson ပြီးဆုံးပြီး":"✓ Lesson လေ့လာပြီး မှတ်သားရန်",()->{prefs.edit().putBoolean(key,true).apply();draw();});
   add("AI အဖြေများရရှိရန် App AI အတွက် HTTPS Backend လိုအပ်သည်။ ChatGPT/Gemini ရွေးလျှင် ဖုန်းထဲရှိ App တွင် Prompt ကို Paste ပြီး Send နှိပ်ပါ။",12,false,17);
  }
  TextView footer=text("Main AI: "+AiProviderRouter.mainLabel(this)+"   ·   ပြောင်းရန် ⚙",13,true);footer.setPadding(dp(15),dp(14),dp(15),dp(17));footer.setBackgroundColor(WHITE);footer.setOnClickListener(v->AiProviderRouter.selectMain(this));root.addView(footer);
 }
 @Override public void onBackPressed(){back();}
}
