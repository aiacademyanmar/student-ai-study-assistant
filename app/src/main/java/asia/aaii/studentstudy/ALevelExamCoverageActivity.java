package asia.aaii.studentstudy;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.io.OutputStream;

public class ALevelExamCoverageActivity extends Activity{
 private ALevelCoverageStore store;
 private LinearLayout root,body;
 private int subject=-1,module=-1,focus=-1;
 private String screen="coverage";
 private int dp(int n){return StudyDesign.dp(this,n);}
 private ALevel2026.Subject s(){return ALevel2026.SUBJECTS[subject];}
 private ALevel2026.Module m(){return s().modules[module];}
 private String key(){return store.key(s(),m(),m().subtopics[focus]);}
 private void text(String t,int size){TextView v=StudyDesign.text(this,t,size,StudyDesign.INK,size>=18);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(10);body.addView(v,p);}
 private void button(String t,Runnable r){TextView v=StudyDesign.action(this,t,false,r);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(8);body.addView(v,p);}
 private void open(String url){try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));}catch(Exception ignored){}}
 @Override public void onCreate(Bundle b){super.onCreate(b);store=new ALevelCoverageStore(this);subject=getIntent().getIntExtra("subject_index",-1);module=getIntent().getIntExtra("module_index",-1);focus=getIntent().getIntExtra("subtopic_index",-1);screen=getIntent().getStringExtra("mode");if(screen==null)screen="coverage";if(subject<0||subject>=ALevel2026.SUBJECTS.length){subject=-1;module=-1;focus=-1;}else if(module<0||module>=s().modules.length){module=-1;focus=-1;}else if(focus<0||focus>=m().subtopics.length)focus=-1;draw();}
 private void back(){if(focus>=0)focus=-1;else if(module>=0)module=-1;else if(subject>=0)subject=-1;else{finish();return;}draw();}
 @Override public void onBackPressed(){back();}
 private void draw(){
  root=StudyDesign.col(this);root.setBackgroundColor(StudyDesign.BG);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,i)->{android.graphics.Insets q=i.getInsets(WindowInsets.Type.systemBars());v.setPadding(q.left,q.top,q.right,q.bottom);return i;});
  TextView h=StudyDesign.text(this,"‹    A Level · "+("exam".equals(screen)?"Mock Exam":"Coverage Checklist"),18,StudyDesign.INK,true);
  h.setPadding(dp(14),dp(15),dp(10),dp(15));h.setBackgroundColor(-1);h.setOnClickListener(v->back());root.addView(h);
  ScrollView scroll=new ScrollView(this);body=StudyDesign.col(this);body.setPadding(dp(14),dp(12),dp(14),dp(25));scroll.addView(body);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
  if("exam".equals(screen))exam();else coverage();
  root.addView(StudyDesign.navigation(this,ALevelSyllabusActivity.class));
 }
 private void coverage(){
  if(subject<0){
   body.addView(StudyDesign.hero(this,"GCE A LEVEL · 2026","Coverage Checklist","Study · Practice · Exam · Manual PDF References"));
   text("Manual Mapping ≠ Cambridge verified. User notes only.",13);
   for(int i=0;i<ALevel2026.SUBJECTS.length;i++){final int id=i;ALevel2026.Subject s=ALevel2026.SUBJECTS[i];int[] x=store.summary(s);
    button("📚 "+s.name+" ("+s.code+")\nStudy "+x[1]+"/"+x[0]+"  Practice "+x[2]+"  Exam "+x[3]+"  PDF notes "+x[4],()->{subject=id;draw();});
   }
   button("📤 Export all coverage JSON",()->export());
  }else if(module<0){
   body.addView(StudyDesign.hero(this,"SUBJECT "+s().code,s().name,"User-tracked syllabus-oriented study map"));
   int[] x=store.summary(s());text("Study "+x[1]+"/"+x[0]+" · Practice "+x[2]+" · Exam "+x[3]+" · PDF notes "+x[4],13);
   button("📄 Official syllabus reference",()->open(s().pdfUrl));
   for(int i=0;i<s().modules.length;i++){final int id=i;ALevel2026.Module m=s().modules[i];button("["+m.level+"] "+m.name+" · "+m.subtopics.length+" subtopics",()->{module=id;draw();});}
  }else if(focus<0){
   text(m().name+" · "+m().paper,21);
   for(int i=0;i<m().subtopics.length;i++){final int id=i;String topic=m().subtopics[i];String k=store.key(s(),m(),topic);
    button("○ "+topic+"\n"+(store.flag(k,"study")?"✓ Study ":"")+" "+(store.flag(k,"practice")?"✓ Practice ":"")+" "+(store.flag(k,"exam")?"✓ Exam ":"")+
     (store.note(k,"reference").isEmpty()?" · PDF pending":" · PDF note recorded"),()->{focus=id;draw();});
   }
  }else{
   text(m().subtopics[focus],21);
   text("Original app topic, NOT the wording of an official Cambridge learning outcome.",13);
   String k=key();
   for(String flag:new String[]{"study","practice","exam"}){final String f=flag;button((store.flag(k,f)?"✓ ":"○ ")+f.toUpperCase()+" · self report",()->{store.setFlag(k,f,!store.flag(k,f));draw();});}
   text("Manual PDF outcome crosswalk",18);
   text("Reference: "+(store.note(k,"reference").isEmpty()?"Not entered":store.note(k,"reference")),13);
   text("Outcome paraphrase: "+(store.note(k,"outcome").isEmpty()?"Not entered":store.note(k,"outcome")),13);
   text("Review: "+(store.note(k,"status").isEmpty()?"Pending":store.note(k,"status")),13);
   button("✎ Edit PDF page / outcome / status",()->edit());
   button("📄 Open official PDF",()->open(s().pdfUrl));
   button("🤖 AI Teaching / Exercises",()->AiProviderRouter.chooseAndRun(this,ALevelPromptFactory.create(s(),m(),m().subtopics[focus],0,"practice"),"al_coverage_"+s().code+"_"+module+"_"+focus,"A Level Exercise"));
   button("📝 Mock Exam for this focus",()->{screen="exam";draw();});
  }
 }
 private void edit(){
  String k=key();LinearLayout form=StudyDesign.col(this);form.setPadding(dp(15),dp(5),dp(15),dp(2));
  EditText ref=new EditText(this);ref.setSingleLine(false);ref.setHint("Official PDF page / section");ref.setText(store.note(k,"reference"));form.addView(ref);
  EditText outcome=new EditText(this);outcome.setSingleLine(false);outcome.setHint("Own paraphrase of outcome");outcome.setText(store.note(k,"outcome"));form.addView(outcome);
  Spinner status=new Spinner(this);String[] statuses={"Pending manual review","User-marked match","Potential gap"};
  ArrayAdapter<String> a=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,statuses);a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);status.setAdapter(a);form.addView(status);
  String current=store.note(k,"status");status.setSelection("match".equals(current)?1:"gap".equals(current)?2:0);
  new AlertDialog.Builder(this).setTitle("User-defined PDF crosswalk").setView(form).setPositiveButton("Save",(dialog,v)->{
   store.note(k,"reference",ref.getText().toString().trim());store.note(k,"outcome",outcome.getText().toString().trim());
   store.note(k,"status",status.getSelectedItemPosition()==1?"match":status.getSelectedItemPosition()==2?"gap":"pending");draw();
  }).setNegativeButton("Cancel",null).show();
 }
 private void exam(){
  if(subject<0){
   body.addView(StudyDesign.hero(this,"2026 · AS/A LEVEL","Paper-specific Mock Exam","Original practice questions · AI tutor"));
   for(int i=0;i<ALevel2026.SUBJECTS.length;i++){final int id=i;ALevel2026.Subject s=ALevel2026.SUBJECTS[i];button(s.name+" · "+s.code,()->{subject=id;draw();});}
  }else{
   ALevelPaperCatalog.Paper[] papers=ALevelPaperCatalog.forSubject(s().code);
   text(s().name+" ("+s().code+")",22);
   text("Only original mock questions. Paper rules, exam options and timing must be verified in the 2026 syllabus PDF.",13);
   button("📄 Official syllabus PDF",()->open(s().pdfUrl));
   for(ALevelPaperCatalog.Paper paper:papers){
    button("📝 "+paper.label()+"\n"+paper.stage+" · "+paper.format,()->configurePaper(paper));
   }
   if(module>=0)button("◀ Coverage Checklist",()->{screen="coverage";draw();});
  }
 }
 private void configurePaper(ALevelPaperCatalog.Paper paper){
  LinearLayout form=StudyDesign.col(this);form.setPadding(dp(15),dp(6),dp(15),dp(4));
  TextView caution=StudyDesign.text(this,"Practice timing is user-set, NOT official exam duration. Choose option/route before generating.",13,StudyDesign.MUTED,false);form.addView(caution);
  EditText option=new EditText(this);option.setHint("Option / route (e.g. selected history option)");form.addView(option);
  EditText time=new EditText(this);time.setInputType(2);time.setText("30");time.setHint("Practice minutes");form.addView(time);
  EditText count=new EditText(this);count.setInputType(2);count.setText("5");count.setHint("Question count");form.addView(count);
  new AlertDialog.Builder(this).setTitle(paper.label()).setView(form).setPositiveButton("Start AI Mock",(d,w)->{
    int mins=30,questions=5;try{mins=Integer.parseInt(time.getText().toString());questions=Integer.parseInt(count.getText().toString());}catch(Exception ignored){}
    mins=Math.min(180,Math.max(5,mins));questions=Math.min(20,Math.max(1,questions));
    String focusText=module>=0&&focus>=0?m().subtopics[focus]:"";
    AiProviderRouter.chooseAndRun(this,ALevelPaperCatalog.examPrompt(s(),paper,focusText,mins,questions,option.getText().toString()),
      "a_mock_"+s().code+"_paper_"+paper.number,"2026 AS/A Paper "+paper.number);
  }).setNegativeButton("Cancel",null).show();
 }
 private void export(){Intent i=new Intent(Intent.ACTION_CREATE_DOCUMENT);i.addCategory(Intent.CATEGORY_OPENABLE);i.setType("application/json");i.putExtra(Intent.EXTRA_TITLE,"GCE_A_Level_2026_Coverage.json");startActivityForResult(i,801);}
 @Override protected void onActivityResult(int req,int res,Intent data){super.onActivityResult(req,res,data);
  if(req==801&&res==RESULT_OK&&data!=null&&data.getData()!=null)try(OutputStream o=getContentResolver().openOutputStream(data.getData())){if(o!=null)o.write(store.export().toString(2).getBytes("UTF-8"));}catch(Exception ignored){}
 }
}
