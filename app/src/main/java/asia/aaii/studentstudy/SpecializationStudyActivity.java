package asia.aaii.studentstudy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowInsets;
import android.widget.*;
import android.view.View;
import android.text.TextWatcher;
import android.text.Editable;
import java.util.ArrayList;
import java.util.Locale;

/** Searchable 67-subject Native specialization browser and learning prompt composer.
 * Study completion is manually self-reported; AI prompt launches are never "mastery".
 */
public final class SpecializationStudyActivity extends Activity {
 private final String STORAGE="specialization_study_v242";
 private SharedPreferences prefs;
 private LinearLayout root,body,subjectList,topicList;
 private Spinner groups,topics,modes,difficulty;
 private EditText search,learnerNotes;
 private String group="University",selectedName=null;
 private int selectedTopic=0,selectedMode=0;
 private int dp(int n){return PremiumDesign.dp(this,n);}
 private TextView t(String s,int size,int color,boolean bold){return PremiumDesign.text(this,s,size,color,bold);}
 private void add(LinearLayout host,View v,int top){PremiumDesign.add(host,v,top);}
 private void head(String text){add(body,t(text,19,PremiumDesign.NAVY,true),16);}
 private Spinner pick(LinearLayout host,String[] choices,int selected){
  Spinner input=new Spinner(this);
  ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,choices);
  input.setAdapter(adapter);
  input.setSelection(Math.max(0,Math.min(choices.length-1,selected)));
  input.setPadding(dp(9),0,dp(9),0);
  input.setBackground(PremiumDesign.outline(this,0xFFFFFFFF,PremiumDesign.BORDER,13));
  add(host,input,8);
  input.setMinimumHeight(dp(48));
  return input;
 }
 private void action(LinearLayout host,String name,boolean important,Runnable run){
  add(host,PremiumDesign.action(this,name,run,important),10);
 }
 private void filter(){
  if(subjectList==null)return;
  String q=search.getText().toString().toLowerCase(Locale.ROOT).trim();
  for(int i=0;i<subjectList.getChildCount();i++){
   View card=subjectList.getChildAt(i);
   String title=card.getTag() instanceof String?(String)card.getTag():"";
   card.setVisibility(title.toLowerCase(Locale.ROOT).contains(q)?View.VISIBLE:View.GONE);
  }
 }
 private String preferenceKey(SpecializationCatalog.Subject s,int topic){
  return "read_"+s.group.replace(' ','_')+"_"+s.name.replace(' ','_')+"_"+topic;
 }
 private int reviewed(SpecializationCatalog.Subject subject){
  int n=0;for(int i=0;i<subject.topics.length;i++)
   if(prefs.getBoolean(preferenceKey(subject,i),false))n++;
  return n;
 }
 private int totalReview(){
  int n=0;for(SpecializationCatalog.Subject s:SpecializationCatalog.ALL)n+=reviewed(s);return n;
 }
 private int totalLaunches(){
  int n=0;for(SpecializationCatalog.Subject s:SpecializationCatalog.ALL)
   n+=prefs.getInt("uses_"+s.group+"_"+s.name,0);
  return n;
 }
 private void openSubject(SpecializationCatalog.Subject chosen){
  selectedName=chosen.name;group=chosen.group;selectedTopic=0;
  render();
 }
 private void groupCatalog(){
  selectedName=null;
  String filterGroup=group;
  head("Browse "+filterGroup+" Subjects");
  TextView note=t("App-created learning taxonomy · University syllabus validation pending",12,PremiumDesign.MUTED,false);
  add(body,note,7);
  search=new EditText(this);search.setSingleLine(true);
  search.setTextSize(15);search.setHint("Search by subject name…");
  search.setPadding(dp(14),dp(10),dp(12),dp(10));
  search.setBackground(PremiumDesign.outline(this,0xFFFFFFFF,PremiumDesign.BORDER,15));
  add(body,search,11);
  subjectList=PremiumDesign.column(this);add(body,subjectList,8);
  for(SpecializationCatalog.Subject s:SpecializationCatalog.ALL){
   if(!s.group.equals(filterGroup))continue;
   LinearLayout card=PremiumDesign.panel(this);card.setTag(s.name);
   LinearLayout first=PremiumDesign.row(this);first.setGravity(Gravity.CENTER_VERTICAL);
   TextView icon=t("▣",24,PremiumDesign.BLUE,true);
   icon.setGravity(Gravity.CENTER);icon.setBackground(PremiumDesign.fill(this,0xFFEAF2FF,15));
   first.addView(icon,new LinearLayout.LayoutParams(dp(54),dp(55)));
   LinearLayout labels=PremiumDesign.column(this);
   labels.setPadding(dp(12),0,0,0);
   TextView name=t(s.name,16,PremiumDesign.INK,true);name.setMaxLines(2);labels.addView(name);
   add(labels,t("5 suggested topics · "+reviewed(s)+"/5 self-reviewed",12,PremiumDesign.MUTED,false),5);
   first.addView(labels,new LinearLayout.LayoutParams(0,-2,1));
   card.addView(first);
   add(card,PremiumDesign.progress(this,reviewed(s),5,PremiumDesign.GREEN,PremiumDesign.CYAN),10);
   action(card,"Open Topic & Prompt Modes  ›",false,()->openSubject(s));
   add(subjectList,card,10);
  }
  search.addTextChangedListener(new TextWatcher(){
   public void beforeTextChanged(CharSequence s,int st,int c,int a){}
   public void onTextChanged(CharSequence s,int st,int before,int count){filter();}
   public void afterTextChanged(Editable e){}
  });
 }
 private void compose(SpecializationCatalog.Subject s,int topic,int mode,boolean openAI){
  String prompt=SpecializationPrompts.build(s,s.topics[topic],mode,
     (String)difficulty.getSelectedItem(),learnerNotes.getText().toString());
  prefs.edit().putInt("uses_"+s.group+"_"+s.name,
      prefs.getInt("uses_"+s.group+"_"+s.name,0)+1).apply();
  if(openAI){
   AiProviderRouter.chooseAndRun(this,prompt,
      "specialize_"+s.group+"_"+s.name+"_"+topic+"_"+mode,
      s.name+" · "+SpecializationPrompts.MODES[mode]);
  }else{
   ClipboardManager clipboard=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
   if(clipboard!=null)clipboard.setPrimaryClip(ClipData.newPlainText(
      "Specialization study prompt",prompt));
   Toast.makeText(this,"Study prompt copied",Toast.LENGTH_SHORT).show();
  }
 }
 private void topicDetail(){
  final SpecializationCatalog.Subject subject=SpecializationCatalog.find(group,selectedName);
  if(subject==null){selectedName=null;render();return;}
  head(subject.name);
  LinearLayout summary=PremiumDesign.panel(this);
  summary.addView(t("5 Suggested Topics · "+reviewed(subject)+"/5 Reviewed",16,PremiumDesign.BLUE,true));
  add(summary,PremiumDesign.progress(this,reviewed(subject),5,PremiumDesign.BLUE,PremiumDesign.CYAN),9);
  add(summary,t("Self-reported activity only; review does not mean a syllabus outcome is mastered.",12,
    PremiumDesign.MUTED,false),8);
  add(body,summary,10);
  head("Topic → Lesson Prompt");
  topics=pick(body,subject.topics,selectedTopic);
  head("Choose One of 10 AI Prompt Functions");
  modes=pick(body,SpecializationPrompts.MODES,selectedMode);
  head("Difficulty");
  difficulty=pick(body,new String[]{"Foundation","Intermediate","Advanced"},0);
  head("Optional Learning Notes");
  learnerNotes=new EditText(this);
  learnerNotes.setHint("What do you want to learn or practise?");
  learnerNotes.setTextSize(15);learnerNotes.setMinLines(2);learnerNotes.setMaxLines(4);
  learnerNotes.setGravity(Gravity.TOP);learnerNotes.setPadding(dp(12),dp(10),dp(12),dp(10));
  learnerNotes.setBackground(PremiumDesign.outline(this,0xFFFFFFFF,PremiumDesign.BORDER,16));
  add(body,learnerNotes,9);
  action(body,"✦  Choose AI & Start Teaching",true,()->{
   selectedTopic=topics.getSelectedItemPosition();selectedMode=modes.getSelectedItemPosition();
   compose(subject,selectedTopic,selectedMode,true);
  });
  action(body,"Copy This Study Prompt",false,()->{
   selectedTopic=topics.getSelectedItemPosition();selectedMode=modes.getSelectedItemPosition();
   compose(subject,selectedTopic,selectedMode,false);
  });
  action(body,"Preview Generated Prompt",false,()->{
   selectedTopic=topics.getSelectedItemPosition();selectedMode=modes.getSelectedItemPosition();
   String content=SpecializationPrompts.build(subject,subject.topics[selectedTopic],selectedMode,
     (String)difficulty.getSelectedItem(),learnerNotes.getText().toString());
   new AlertDialog.Builder(this).setTitle("AI Prompt Preview").setMessage(content)
     .setPositiveButton("OK",null).show();
  });
  head("Self-Reported Topic Review");
  for(int i=0;i<subject.topics.length;i++){
   final int pos=i;
   CheckBox check=new CheckBox(this);
   check.setText((i+1)+". "+subject.topics[i]);
   check.setTextSize(15);check.setChecked(prefs.getBoolean(preferenceKey(subject,i),false));
   check.setPadding(dp(5),dp(8),dp(5),dp(8));
   check.setBackground(PremiumDesign.outline(this,0xFFFFFFFF,PremiumDesign.BORDER,13));
   check.setOnCheckedChangeListener((box,checked)->{
    prefs.edit().putBoolean(preferenceKey(subject,pos),checked).apply();
    int offset=topics.getSelectedItemPosition();
    int selectedPrompt=modes.getSelectedItemPosition();
    selectedTopic=offset;selectedMode=selectedPrompt;render();
   });
   add(body,check,8);
  }
  action(body,"‹ Back to Subject List",false,()->{selectedName=null;render();});
  add(body,t("Medical content is educational and not clinical guidance. Engineering prompts "+
    "do not substitute for certified designs. Subject taxonomy is not official university curriculum.",
    12,PremiumDesign.MUTED,false),14);
 }
 private void render(){
  root=PremiumDesign.column(this);
  root.setBackground(PremiumDesign.grad(this,0xFFEEF8FF,0xFFF7F1FF,0));
  setContentView(root);
  getWindow().setStatusBarColor(PremiumDesign.NAVY);
  getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,insets)->{
   android.graphics.Insets z=insets.getInsets(WindowInsets.Type.systemBars());
   v.setPadding(z.left,z.top,z.right,z.bottom);return insets;
  });
  LinearLayout header=PremiumDesign.row(this);header.setGravity(Gravity.CENTER_VERTICAL);
  header.setPadding(dp(12),dp(8),dp(12),dp(8));
  header.setBackground(PremiumDesign.grad(this,PremiumDesign.BLUE,PremiumDesign.PURPLE,0));
  TextView back=t("‹",29,0xFFFFFFFF,true);back.setGravity(Gravity.CENTER);
  back.setOnClickListener(v->onBackPressed());
  header.addView(back,new LinearLayout.LayoutParams(dp(51),dp(51)));
  TextView title=t("Specialize Subject AI",19,0xFFFFFFFF,true);
  header.addView(title,new LinearLayout.LayoutParams(0,-2,1));root.addView(header);
  ScrollView scroll=new ScrollView(this);body=PremiumDesign.column(this);
  body.setPadding(dp(14),dp(13),dp(14),dp(28));scroll.addView(body);
  root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
  LinearLayout top=PremiumDesign.panel(this);
  top.addView(t("67 Specialized Subjects · 4 Groups",20,PremiumDesign.INK,true));
  add(top,t("335 suggested study topics · 10 dynamic prompt modes per subject.",13,
    PremiumDesign.MUTED,false),8);
  int done=totalReview();
  add(top,t(done+" / 335 topics self-reviewed on this device",15,PremiumDesign.BLUE,true),12);
  add(top,PremiumDesign.progress(this,done,335,PremiumDesign.BLUE,PremiumDesign.GREEN),9);
  add(top,t(totalLaunches()+" prompts copied/launched (activity, not mastery).",12,
    PremiumDesign.MUTED,false),10);
  add(body,top,8);
  LinearLayout tabs=PremiumDesign.row(this);
  for(String g:SpecializationCatalog.GROUPS){
   TextView tab=PremiumDesign.pill(this,
     g.equals("University")?"University":g.equals("Engineering")?"Engineering":
       g.equals("Medicine")?"Medicine":"Business",g.equals(group));
   tab.setTextSize(10);tab.setGravity(Gravity.CENTER);
   tab.setOnClickListener(v->{group=g;selectedName=null;render();});
   LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,dp(53),1);
   if(tabs.getChildCount()>0)p.leftMargin=dp(4);tabs.addView(tab,p);
  }
  add(body,tabs,12);
  if(selectedName==null)groupCatalog();else topicDetail();
 }
 @Override public void onCreate(Bundle b){
  super.onCreate(b);SpecializationCatalog.validate();
  prefs=getSharedPreferences(STORAGE,MODE_PRIVATE);
  group=getIntent().getStringExtra("group");
  if(group==null||SpecializationCatalog.countGroup(group)==0)group="University";
  render();
 }
 @Override public void onBackPressed(){
  if(selectedName!=null){selectedName=null;render();}else super.onBackPressed();
 }
}
