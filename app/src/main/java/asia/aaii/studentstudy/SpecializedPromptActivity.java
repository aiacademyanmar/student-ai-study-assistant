package asia.aaii.studentstudy;
import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowInsets;
import android.widget.*;
import android.content.*;
/** Native specialization prompt studio for 67 new subject starter tracks. */
public final class SpecializedPromptActivity extends Activity {
 private LinearLayout root,body;
 private Spinner group,subject,mode,language;
 private EditText topic;
 private int dp(int x){return PremiumDesign.dp(this,x);}
 private TextView txt(String s,int size,int color,boolean bold){return PremiumDesign.text(this,s,size,color,bold);}
 private void add(android.view.View v,int m){PremiumDesign.add(body,v,m);}
 private Spinner choose(String label,String[] values){
  add(txt(label,15,PremiumDesign.INK,true),14);
  Spinner w=new Spinner(this);ArrayAdapter<String> a=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,values);
  a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);w.setAdapter(a);
  w.setBackground(PremiumDesign.outline(this,0xFFFFFFFF,PremiumDesign.BORDER,15));w.setMinimumHeight(dp(53));add(w,7);return w;
 }
 private String prompt(){
  return SpecializedSubjectCatalog.build(group.getSelectedItemPosition(),subject.getSelectedItemPosition(),
    mode.getSelectedItemPosition(),topic.getText().toString(),language.getSelectedItem().toString());
 }
 @Override public void onCreate(Bundle b){
  super.onCreate(b);getWindow().setStatusBarColor(PremiumDesign.NAVY);
  root=PremiumDesign.column(this);root.setBackground(PremiumDesign.grad(this,0xFFEAF7FF,0xFFF7F1FF,0));setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,in)->{
   android.graphics.Insets x=in.getInsets(WindowInsets.Type.systemBars());v.setPadding(x.left,x.top,x.right,x.bottom);return in;});
  LinearLayout header=PremiumDesign.row(this);header.setGravity(Gravity.CENTER_VERTICAL);header.setPadding(dp(12),dp(8),dp(12),dp(8));
  header.setBackground(PremiumDesign.grad(this,PremiumDesign.BLUE,PremiumDesign.PURPLE,0));
  TextView back=txt("‹",28,0xFFFFFFFF,true);back.setOnClickListener(v->finish());back.setGravity(Gravity.CENTER);
  header.addView(back,new LinearLayout.LayoutParams(dp(49),dp(49)));
  header.addView(txt("Specialize AI Prompt Studio",18,0xFFFFFFFF,true));
  root.addView(header);
  ScrollView sc=new ScrollView(this);body=PremiumDesign.column(this);
  body.setPadding(dp(15),dp(10),dp(15),dp(32));sc.addView(body);root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
  add(PremiumDesign.illustratedHero(this,"67 Subject Tracks","10 original dynamic prompt modes per subject · App-generated; not official syllabus",145),9);
  LinearLayout note=PremiumDesign.panel(this);
  note.addView(txt("Starter content · 0 verified institutional syllabus mappings",14,PremiumDesign.BLUE,true));
  PremiumDesign.add(note,txt("Prompts are generated locally. AI responses need the selected provider and may require internet.",12,PremiumDesign.MUTED,false),8);
  add(note,13);
  group=choose("Specialization Group",SpecializedSubjectCatalog.GROUPS);
  subject=choose("Select Subject",SpecializedSubjectCatalog.SUBJECTS[0]);
  group.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){
   public void onNothingSelected(android.widget.AdapterView<?> a){}
   public void onItemSelected(android.widget.AdapterView<?> parent,android.view.View view,int pos,long id){
    ArrayAdapter<String> choices=new ArrayAdapter<>(SpecializedPromptActivity.this,android.R.layout.simple_spinner_item,SpecializedSubjectCatalog.SUBJECTS[pos]);
    choices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);subject.setAdapter(choices);
   }
  });
  mode=choose("AI Prompt Mode",SpecializedSubjectCatalog.MODES);
  add(txt("Lesson Topic (student-defined)",15,PremiumDesign.INK,true),14);
  topic=new EditText(this);topic.setHint("e.g. Newton's Laws / Market Structures");
  topic.setTextSize(15);topic.setSingleLine(false);topic.setMinLines(2);
  topic.setPadding(dp(12),dp(10),dp(12),dp(10));
  topic.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(240)});
  topic.setBackground(PremiumDesign.outline(this,0xFFFFFFFF,PremiumDesign.BORDER,15));add(topic,8);
  language=choose("Teaching Language",new String[]{"Myanmar","English"});
  add(PremiumDesign.action(this,"✦ Generate and Choose AI",()->{
   AiProviderRouter.chooseAndRun(this,prompt(),"specialize_"+group.getSelectedItemPosition()+"_"+subject.getSelectedItemPosition(), 
    SpecializedSubjectCatalog.SUBJECTS[group.getSelectedItemPosition()][subject.getSelectedItemPosition()]);
  },true),18);
  add(PremiumDesign.action(this,"Copy Prompt",()->{
   ClipboardManager cb=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
   if(cb!=null)cb.setPrimaryClip(ClipData.newPlainText("Specialized Study Prompt",prompt()));
   Toast.makeText(this,"Prompt copied",Toast.LENGTH_SHORT).show();
  },false),10);
 }
}
