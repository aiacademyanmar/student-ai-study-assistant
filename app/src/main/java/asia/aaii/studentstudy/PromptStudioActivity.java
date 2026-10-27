package asia.aaii.studentstudy;
import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.view.WindowInsets;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.widget.Toast;

/** Native composer for 24 GCE 2026 subject tracks and eight original AI prompt modes. */
public final class PromptStudioActivity extends Activity {
 private LinearLayout root,body;
 private Spinner level,subject,mode,difficulty;
 private EditText topic,paper,route;
 private int dp(int n){return StudyDesign.dp(this,n);}
 private void label(String s,int size){TextView t=StudyDesign.text(this,s,size,StudyDesign.INK,size>=18);
  LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(13);body.addView(t,p);}
 private Spinner spinner(String[] values){
  Spinner v=new Spinner(this);ArrayAdapter<String> a=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,values);
  a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);v.setAdapter(a);
  LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(51));p.topMargin=dp(6);
  v.setBackground(StudyDesign.stroked(this,StudyDesign.WHITE,StudyDesign.BORDER,12));body.addView(v,p);return v;
 }
 private EditText field(String hint){
  EditText v=new EditText(this);v.setHint(hint);v.setTextSize(15);v.setSingleLine(true);
  v.setPadding(dp(13),dp(11),dp(13),dp(11));
  v.setBackground(StudyDesign.stroked(this,StudyDesign.WHITE,StudyDesign.BORDER,12));
  LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(52));p.topMargin=dp(7);body.addView(v,p);return v;
 }
 private void btn(String label,boolean primary,Runnable run){
  TextView t=StudyDesign.action(this,label,primary,run);
  LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.topMargin=dp(12);body.addView(t,lp);
 }
 private String[] subjects(boolean a){
  String[] names=new String[12];for(int i=0;i<12;i++)names[i]=a?
    ALevel2026.SUBJECTS[i].name+" ("+ALevel2026.SUBJECTS[i].code+")":
    OLevel2026.SUBJECTS[i].name+" ("+OLevel2026.SUBJECTS[i].code+")";
  return names;
 }
 private String prompt(){return StudyPromptComposer.build(level.getSelectedItemPosition()==1,
   subject.getSelectedItemPosition(),mode.getSelectedItemPosition(),difficulty.getSelectedItemPosition(),
   topic.getText().toString(),paper.getText().toString(),route.getText().toString());}
 @Override public void onCreate(Bundle b){
  super.onCreate(b);draw();
 }
 private void draw(){
  root=StudyDesign.col(this);root.setBackgroundColor(StudyDesign.BG);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,i)->{
   android.graphics.Insets bars=i.getInsets(WindowInsets.Type.systemBars());
   v.setPadding(bars.left,bars.top,bars.right,bars.bottom);return i;
  });
  TextView head=StudyDesign.text(this,"‹    AI Prompt Studio",20,StudyDesign.INK,true);
  head.setPadding(dp(16),dp(16),dp(12),dp(16));head.setBackgroundColor(-1);
  head.setOnClickListener(v->finish());root.addView(head);
  ScrollView scroll=new ScrollView(this);body=StudyDesign.col(this);
  body.setPadding(dp(16),dp(14),dp(16),dp(36));scroll.addView(body);
  root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
  body.addView(StudyDesign.hero(this,"GCE 2026 · AI PROMPT STUDIO","Build My Study Prompt","24 subject tracks · Myanmar AI teaching · Exam practice"));
  label("Exam level",15);
  level=spinner(new String[]{"GCE O Level","GCE AS / A Level"});
  label("Subject",15);subject=spinner(subjects(false));
  level.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){
   public void onNothingSelected(android.widget.AdapterView<?> v){}
   public void onItemSelected(android.widget.AdapterView<?> v,android.view.View item,int pos,long id){
    ArrayAdapter<String> a=new ArrayAdapter<>(PromptStudioActivity.this,android.R.layout.simple_spinner_item,subjects(pos==1));
    a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);subject.setAdapter(a);
   }
  });
  label("Prompt type",15);mode=spinner(StudyPromptComposer.MODES);
  label("Difficulty",15);difficulty=spinner(StudyPromptComposer.DIFFICULTY);
  label("Study topic (optional)",15);topic=field("e.g. Differentiation / Inference");
  label("Paper / component (optional)",15);paper=field("e.g. Paper 1 / Paper 2");
  label("Specialist option / route (optional)",15);route=field("e.g. Pure & Statistics / History Option");
  btn("✦ Open AI Teacher with Prompt",true,()->AiProviderRouter.chooseAndRun(this,prompt(),
    "promptstudio_"+(level.getSelectedItemPosition()==1?"A":"O")+"_"+subject.getSelectedItemPosition(),
    StudyPromptComposer.MODES[mode.getSelectedItemPosition()]));
  btn("📋 Copy Prompt",false,()->{
   ClipboardManager c=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
   if(c!=null)c.setPrimaryClip(ClipData.newPlainText("GCE 2026 prompt",prompt()));
   Toast.makeText(this,"Prompt copied",Toast.LENGTH_SHORT).show();
  });
  btn("📖 Open Subject Syllabus",false,()->{
   boolean a=level.getSelectedItemPosition()==1;
   Intent i=new Intent(this,a?ALevelSyllabusActivity.class:OLevelSyllabusActivity.class);
   i.putExtra("subject_index",subject.getSelectedItemPosition());startActivity(i);
  });
  label("AI output is not an official Cambridge paper or mark scheme. ChatGPT/Gemini may require Paste and Send. Verify exact 2026 scope from the linked PDF.",12);
  root.addView(StudyDesign.navigation(this,PromptStudioActivity.class));
 }
}