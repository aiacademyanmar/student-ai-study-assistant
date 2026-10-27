package asia.aaii.studentstudy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStream;

/**
 * Review dashboard for app-authored study topics, NOT a Cambridge-verified LO map.
 * All matches/gaps and PDF locations are manually supplied by the user.
 * Reuses V2.16 O Level and V2.21 A Level local stores without data migration.
 */
public class SyllabusAuditActivity extends Activity {
 private LinearLayout root,body;
 private OutcomeCrosswalkStore oStore;
 private ALevelCoverageStore aStore;
 private String level="O";
 private int subject=-1,topic=-1,focus=-1;
 private int dp(int n){return StudyDesign.dp(this,n);}
 private OLevel2026.Subject os(){return OLevel2026.SUBJECTS[subject];}
 private ALevel2026.Subject as(){return ALevel2026.SUBJECTS[subject];}
 private String sub(){
  return "O".equals(level)?os().topics[topic].subtopics[focus]:as().modules[topic].subtopics[focus];
 }
 private String group(){
  return "O".equals(level)?os().topics[topic].name:as().modules[topic].name;
 }
 private void heading(String t,int size){
  TextView v=StudyDesign.text(this,t,size,StudyDesign.INK,size>=18);
  LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(12);body.addView(v,p);
 }
 private void action(String t,boolean primary,Runnable run){
  TextView v=StudyDesign.action(this,t,primary,run);
  LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(9);body.addView(v,p);
 }
 @Override public void onCreate(Bundle b){
  super.onCreate(b);oStore=new OutcomeCrosswalkStore(this);aStore=new ALevelCoverageStore(this);
  level="A".equals(getIntent().getStringExtra("level"))?"A":"O";draw();
 }
 private int[] counts(int si,int gi){
  int[] n=new int[4]; // total app subtopics, user match, potential gap, pending/unmapped
  if("O".equals(level)){
   OLevel2026.Subject x=OLevel2026.SUBJECTS[si];
   for(int i=(gi<0?0:gi);i<(gi<0?x.topics.length:gi+1);i++){
    OLevel2026.Topic t=x.topics[i];
    for(String sub:t.subtopics){
     n[0]++;JSONArray a=oStore.get(x,t,sub);
     boolean matched=false,gap=false;
     for(int j=0;j<a.length();j++){
      JSONObject r=a.optJSONObject(j);if(r==null)continue;
      String status=r.optString("status");
      if(OutcomeCrosswalkStore.GAP.equals(status))gap=true;
      if(OutcomeCrosswalkStore.MATCH.equals(status)
        &&!r.optString("reference").trim().isEmpty()
        &&!r.optString("summary").trim().isEmpty())matched=true;
     }
     if(gap)n[2]++;else if(matched)n[1]++;else n[3]++;
    }
   }
  }else{
   ALevel2026.Subject x=ALevel2026.SUBJECTS[si];
   for(int i=(gi<0?0:gi);i<(gi<0?x.modules.length:gi+1);i++){
    ALevel2026.Module m=x.modules[i];
    for(String sub:m.subtopics){
     n[0]++;String key=aStore.key(x,m,sub);
     String status=aStore.note(key,"status");
     if("gap".equals(status))n[2]++;
     else if("match".equals(status)&&!aStore.note(key,"reference").trim().isEmpty()
       &&!aStore.note(key,"outcome").trim().isEmpty())n[1]++;
     else n[3]++;
    }
   }
  }return n;
 }
 private String summary(int[] n){return "App topics "+n[0]+" · User Match "+n[1]+" · Gap "+n[2]+" · Pending "+n[3];}
 private String groupName(int i){return "O".equals(level)?os().topics[i].name:as().modules[i].name+" ["+as().modules[i].level+"]";}
 private int groupCount(){return "O".equals(level)?os().topics.length:as().modules.length;}
 private String sourceUrl(){return "O".equals(level)?os().officialUrl:as().pdfUrl;}
 private void openSource(){try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(sourceUrl())));}
  catch(Exception ignored){Toast.makeText(this,"Browser unavailable",Toast.LENGTH_SHORT).show();}}
 private void back(){
  if(focus>=0)focus=-1;
  else if(topic>=0)topic=-1;
  else if(subject>=0)subject=-1;
  else{finish();return;}
  draw();
 }
 @Override public void onBackPressed(){back();}
 private void draw(){
  root=StudyDesign.col(this);root.setBackgroundColor(StudyDesign.BG);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,insets)->{
   android.graphics.Insets sys=insets.getInsets(WindowInsets.Type.systemBars());
   v.setPadding(sys.left,sys.top,sys.right,sys.bottom);return insets;
  });
  TextView back=StudyDesign.text(this,"‹   Syllabus Outcome Audit",19,StudyDesign.INK,true);
  back.setPadding(dp(16),dp(16),dp(10),dp(15));back.setBackgroundColor(-1);back.setOnClickListener(v->back());
  root.addView(back);
  ScrollView sc=new ScrollView(this);body=StudyDesign.col(this);
  body.setPadding(dp(15),dp(13),dp(15),dp(38));sc.addView(body);
  root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
  if(subject<0)subjects();else if(topic<0)groups();else if(focus<0)subtopics();else focus();
  root.addView(StudyDesign.navigation(this,SyllabusAuditActivity.class));
 }
 private void subjects(){
  body.addView(StudentPromoBanner.create(this,"2026 SYLLABUS AUDIT","Check Your\nStudy Coverage","24 subjects · User-reviewed PDF mapping","Open Coverage",()->{},false));
  heading("⚠ These are app-defined study items. Match and gap labels come from YOUR manual PDF review; Cambridge has not verified them.",13);
  LinearLayout tabs=StudyDesign.row(this);
  for(String mode:new String[]{"O","A"}){
   TextView tab=StudyDesign.action(this,mode.equals("O")?"📘 O Level":"🎓 AS / A Level",level.equals(mode),()->{
    level=mode;subject=-1;topic=-1;focus=-1;draw();
   });
   LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,-2,1);lp.setMargins(dp(3),dp(11),dp(3),0);tabs.addView(tab,lp);
  }
  body.addView(tabs);
  int[] total=new int[4];
  for(int i=0;i<12;i++){int[] n=counts(i,-1);for(int j=0;j<4;j++)total[j]+=n[j];}
  heading(summary(total),15);
  body.addView(StudyDesign.progress(this,total[1],total[0]));
  action("📤 Export this level's manual audit summary",false,()->export());
  for(int i=0;i<12;i++){
   final int index=i;
   String name="O".equals(level)?OLevel2026.SUBJECTS[i].name:ALevel2026.SUBJECTS[i].name;
   String code="O".equals(level)?OLevel2026.SUBJECTS[i].code:ALevel2026.SUBJECTS[i].code;
   action("📚 "+name+" ("+code+")\n"+summary(counts(i,-1)),false,()->{subject=index;topic=-1;focus=-1;draw();});
  }
 }
 private void groups(){
  String title="O".equals(level)?os().name:as().name;
  String years="O".equals(level)?os().years:as().years;
  body.addView(StudyDesign.hero(this,"SYLLABUS "+years,title,summary(counts(subject,-1))));
  heading("OPTION and ROUTE modules are alternatives when specified; they are not all mandatory.",13);
  action("📄 Open Cambridge subject syllabus/reference",true,()->openSource());
  for(int i=0;i<groupCount();i++){
   final int id=i;
   action("▤ "+groupName(i)+"\n"+summary(counts(subject,i)),false,()->{topic=id;focus=-1;draw();});
  }
 }
 private void subtopics(){
  heading(groupName(topic),21);
  heading(summary(counts(subject,topic)),13);
  for(int i=0;i<("O".equals(level)?os().topics[topic].subtopics.length:as().modules[topic].subtopics.length);i++){
   final int id=i;focus=id;String name=sub(),status=reviewStatus();focus=-1;
   action("◉ "+name+"\n"+status,false,()->{focus=id;draw();});
  }
 }
 private String reviewStatus(){
  if("O".equals(level)){
   JSONArray a=oStore.get(os(),os().topics[topic],sub());
   boolean match=false,gap=false;
   for(int i=0;i<a.length();i++){
    JSONObject j=a.optJSONObject(i);if(j==null)continue;
    if("gap".equals(j.optString("status")))gap=true;
    if("match".equals(j.optString("status"))&&!j.optString("reference").isEmpty()
     &&!j.optString("summary").isEmpty())match=true;
   }
   return gap?"⚠ Potential gap (manual)":match?"✓ User-marked match":"○ Pending / No validated reference";
  }
  String key=aStore.key(as(),as().modules[topic],sub());
  String status=aStore.note(key,"status");
  if("gap".equals(status))return "⚠ Potential gap (manual)";
  if("match".equals(status)&&!aStore.note(key,"reference").trim().isEmpty()
    &&!aStore.note(key,"outcome").trim().isEmpty())return "✓ User-marked match";
  return "○ Pending / No validated reference";
 }
 private void focus(){
  heading(sub(),21);heading(reviewStatus(),15);
  heading("Application-created study subtopic. Compare the 2026 official PDF manually. Do not use future exam-year syllabuses.",13);
  action("📄 Official subject syllabus",false,()->openSource());
  if("O".equals(level)){
   JSONArray arr=oStore.get(os(),os().topics[topic],sub());
   heading("User-defined PDF crosswalk rows: "+arr.length(),16);
   for(int i=0;i<arr.length();i++){JSONObject r=arr.optJSONObject(i);if(r==null)continue;
    heading("Reference: "+r.optString("reference")+"\nOutcome: "+r.optString("summary")+
     "\nStatus: "+r.optString("status"),13);
   }
   action("✎ Add manual O Level reference",true,()->editO());
   action("📋 Open complete O Level Coverage",false,()->startActivity(new Intent(this,SyllabusCoverageActivity.class)));
  }else{
   String k=aStore.key(as(),as().modules[topic],sub());
   heading("PDF: "+aStore.note(k,"reference"),14);
   heading("Outcome: "+aStore.note(k,"outcome"),14);
   action("✎ Edit AS / A Level reference and review",true,()->editA());
   action("✅ Open A Level Coverage",false,()->{
    Intent x=new Intent(this,ALevelExamCoverageActivity.class);
    x.putExtra("subject_index",subject);x.putExtra("module_index",topic);
    x.putExtra("subtopic_index",focus);x.putExtra("mode","coverage");startActivity(x);
   });
  }
 }
 private void editO(){
  LinearLayout form=StudyDesign.col(this);form.setPadding(dp(16),dp(8),dp(16),0);
  EditText page=field(form,"PDF page / syllabus section",""),ref=field(form,"Outcome ID (if official PDF provides one)",""),
   summary=field(form,"Own words: expected learning outcome","");
  Spinner status=status(form,"pending");
  new AlertDialog.Builder(this).setTitle("Add student-entered syllabus reference").setView(form)
   .setPositiveButton("Save",(d,w)->{
    try{
     String pg=page.getText().toString().trim(),sm=summary.getText().toString().trim();
     String st=statusValue(status);
     if("match".equals(st)&&(pg.isEmpty()||sm.isEmpty()))st="pending";
     JSONObject item=new JSONObject();item.put("reference",pg);item.put("outcome_id",ref.getText().toString().trim());
     item.put("summary",sm);item.put("status",st);item.put("notes","User-authored V2.27");
     item.put("updated",System.currentTimeMillis());
     JSONArray list=oStore.get(os(),os().topics[topic],sub());list.put(item);oStore.put(os(),os().topics[topic],sub(),list);
     draw();
    }catch(Exception e){Toast.makeText(this,"Could not save",Toast.LENGTH_SHORT).show();}
   }).setNegativeButton("Cancel",null).show();
 }
 private void editA(){
  String k=aStore.key(as(),as().modules[topic],sub());
  LinearLayout form=StudyDesign.col(this);form.setPadding(dp(16),dp(8),dp(16),0);
  EditText ref=field(form,"PDF page / section",aStore.note(k,"reference")),
   outcome=field(form,"Outcome paraphrase",aStore.note(k,"outcome"));
  Spinner status=status(form,aStore.note(k,"status"));
  new AlertDialog.Builder(this).setTitle("User-entered A Level crosswalk").setView(form)
   .setPositiveButton("Save",(d,w)->{
    String pg=ref.getText().toString().trim(),sm=outcome.getText().toString().trim();
    String st=statusValue(status);
    if("match".equals(st)&&(pg.isEmpty()||sm.isEmpty()))st="pending";
    aStore.note(k,"reference",pg);aStore.note(k,"outcome",sm);aStore.note(k,"status",st);draw();
   }).setNegativeButton("Cancel",null).show();
 }
 private EditText field(LinearLayout container,String hint,String value){
  EditText e=new EditText(this);e.setTextSize(14);e.setSingleLine(false);e.setMinLines(1);e.setMaxLines(3);
  e.setHint(hint);e.setText(value);container.addView(e);return e;
 }
 private Spinner status(LinearLayout container,String initial){
  Spinner v=new Spinner(this);String[] items={"Pending review","User-marked match","Potential gap"};
  ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,items);
  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  v.setAdapter(adapter);v.setSelection("match".equals(initial)?1:"gap".equals(initial)?2:0);
  container.addView(v);return v;
 }
 private String statusValue(Spinner v){return v.getSelectedItemPosition()==1?"match":v.getSelectedItemPosition()==2?"gap":"pending";}
 private void export(){
  Intent x=new Intent(Intent.ACTION_CREATE_DOCUMENT);x.addCategory(Intent.CATEGORY_OPENABLE);
  x.setType("application/json");x.putExtra(Intent.EXTRA_TITLE,"GCE_"+level+"_Manual_Syllabus_Audit_2026.json");
  startActivityForResult(x,927);
 }
 @Override protected void onActivityResult(int req,int res,Intent intent){
  super.onActivityResult(req,res,intent);
  if(req!=927||res!=RESULT_OK||intent==null||intent.getData()==null)return;
  try(OutputStream stream=getContentResolver().openOutputStream(intent.getData())){
   if(stream==null)return;
   JSONObject out=new JSONObject();JSONArray subjects=new JSONArray();
   out.put("exam_year",2026);out.put("level",level);
   out.put("verification","USER-ENTERED / NOT CAMBRIDGE VERIFIED");
   for(int i=0;i<12;i++){
    JSONObject item=new JSONObject();int[] n=counts(i,-1);
    item.put("code","O".equals(level)?OLevel2026.SUBJECTS[i].code:ALevel2026.SUBJECTS[i].code);
    item.put("source","O".equals(level)?OLevel2026.SUBJECTS[i].officialUrl:ALevel2026.SUBJECTS[i].pdfUrl);
    item.put("app_subtopics",n[0]);item.put("user_matches",n[1]);item.put("potential_gaps",n[2]);item.put("pending",n[3]);
    subjects.put(item);
   }
   out.put("subjects",subjects);
   stream.write(out.toString(2).getBytes("UTF-8"));
   Toast.makeText(this,"Saved audit summary",Toast.LENGTH_SHORT).show();
  }catch(Exception ex){Toast.makeText(this,"Export failed",Toast.LENGTH_SHORT).show();}
 }
}
