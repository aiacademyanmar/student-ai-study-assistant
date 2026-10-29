package asia.aaii.studentstudy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * V2.41 subject specialization readiness dashboard.
 * Important: 91/910 is a development scope, NOT 91 integrated subjects.
 * Only 24 GCE native subjects are linked to StudyPromptComposer (8 modes).
 * All other specialty groups explicitly show "Not integrated".
 */
public final class SpecializationProgressActivity extends Activity {
 private static final String[] LABELS={
   "GCE O Level","GCE AS / A Level","Myanmar University",
   "Engineering","Medicine","Management / Business"
 };
 private static final int[] TARGET={12,12,26,14,15,12};
 private static final int[] CONNECTED={12,12,0,0,0,0};
 private static final String[] DETAIL={
  "12 GCE O Level subjects available. AI Prompt Studio has 8 dynamically generated prompt modes; full official syllabus verification is separate.",
  "12 AS/A Level subjects available. AI Prompt Studio has 8 dynamically generated prompt modes; paper routes and alternative options need careful syllabus checking.",
  "26 University specialization subjects are part of the planned catalog. Specialized lesson-by-lesson prompts are not in this Android repository.",
  "14 Engineering specialization subjects are planned. Native subject-specific prompts and validated lessons are not yet integrated.",
  "15 Medicine specialization subjects are planned. No clinical or patient-care guidance is bundled here; any future study material needs qualified review.",
  "12 Management/Business specialization subjects are planned. Native specialization prompts have not yet been merged into this APK."
 };
 private LinearLayout body;
 private int dp(int n){return PremiumDesign.dp(this,n);}
 private TextView text(String content,int size,int color,boolean bold){
  return PremiumDesign.text(this,content,size,color,bold);
 }
 private void add(View view,int top){PremiumDesign.add(body,view,top);}
 private void heading(String title){
  add(text(title,19,PremiumDesign.INK,true),20);
 }
 private void card(int i){
  LinearLayout outer=PremiumDesign.panel(this);
  int done=CONNECTED[i],total=TARGET[i];
  LinearLayout row=PremiumDesign.row(this);row.setGravity(Gravity.CENTER_VERTICAL);
  TextView icon=text(i<2?"🎓":i==2?"▣":i==3?"⚙":i==4?"✚":"▥",26,
      i<2?PremiumDesign.BLUE:PremiumDesign.PURPLE,true);
  icon.setGravity(Gravity.CENTER);
  icon.setBackground(PremiumDesign.fill(this,0xFFEDF4FF,15));
  row.addView(icon,new LinearLayout.LayoutParams(dp(55),dp(55)));
  LinearLayout labels=PremiumDesign.column(this);
  labels.setPadding(dp(12),0,0,0);
  labels.addView(text(LABELS[i],16,PremiumDesign.INK,true));
  PremiumDesign.add(labels,text(done>0?"Native GCE prompts available":"Not yet merged into APK",
       12,PremiumDesign.MUTED,false),5);
  row.addView(labels,new LinearLayout.LayoutParams(0,-2,1));
  TextView score=text(done+"/"+total,15,done>0?PremiumDesign.BLUE:PremiumDesign.MUTED,true);
  score.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
  row.addView(score,new LinearLayout.LayoutParams(dp(65),dp(55)));
  outer.addView(row);
  PremiumDesign.add(outer,PremiumDesign.progress(this,done,total,PremiumDesign.BLUE,
      PremiumDesign.CYAN),12);
  PremiumDesign.add(outer,PremiumDesign.action(this,done>0?
    "Open GCE Prompt Studio  ›":"View Integration Status  ›",()->{
     if(done>0)startActivity(new Intent(this,PromptStudioActivity.class));
     else new AlertDialog.Builder(this).setTitle(LABELS[i])
      .setMessage(DETAIL[i]+"\n\nTarget: "+TARGET[i]+
       " subject tracks. Current native integration: 0. A prior V2.46 workstream described 10 authored prompts per specialized subject, but its files are not merged into this repository.")
      .setPositiveButton("OK",null).show();
    },false),11);
  add(outer,11);
 }
 @Override protected void onCreate(Bundle savedState){
  super.onCreate(savedState);
  getWindow().setStatusBarColor(PremiumDesign.NAVY);
  getWindow().setNavigationBarColor(0xFFF4F8FF);
  LinearLayout root=PremiumDesign.column(this);
  root.setBackground(PremiumDesign.grad(this,0xFFEBF8FF,0xFFF7F1FF,0));
  setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,insets)->{
    android.graphics.Insets in=insets.getInsets(WindowInsets.Type.systemBars());
    v.setPadding(in.left,in.top,in.right,in.bottom);return insets;
  });
  LinearLayout header=PremiumDesign.row(this);
  header.setGravity(Gravity.CENTER_VERTICAL);
  header.setPadding(dp(13),dp(12),dp(14),dp(12));
  header.setBackground(PremiumDesign.grad(this,PremiumDesign.BLUE,PremiumDesign.PURPLE,0));
  TextView back=text("‹",30,0xFFFFFFFF,true);
  back.setGravity(Gravity.CENTER);back.setContentDescription("Back");
  back.setOnClickListener(v->finish());
  header.addView(back,new LinearLayout.LayoutParams(dp(48),dp(48)));
  TextView title=text("Specialize Prompt Progress",18,0xFFFFFFFF,true);
  title.setMaxLines(1);title.setEllipsize(android.text.TextUtils.TruncateAt.END);
  header.addView(title,new LinearLayout.LayoutParams(0,-2,1));
  root.addView(header);
  ScrollView scroll=new ScrollView(this);
  body=PremiumDesign.column(this);
  body.setPadding(dp(14),dp(12),dp(14),dp(25));
  scroll.addView(body);
  root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));

  LinearLayout overview=PremiumDesign.panel(this);
  overview.addView(text("Specialized Subject Prompt Readiness",20,
      PremiumDesign.NAVY,true));
  PremiumDesign.add(overview,text("91 subject targets · Current Native APK integration",13,
      PremiumDesign.MUTED,false),7);
  LinearLayout counters=PremiumDesign.row(this);
  counters.setGravity(Gravity.CENTER_VERTICAL);
  TextView large=text("24 / 91",31,PremiumDesign.BLUE,true);
  counters.addView(large,new LinearLayout.LayoutParams(0,-2,1));
  counters.addView(text("26.4%",22,PremiumDesign.PURPLE,true));
  PremiumDesign.add(overview,counters,14);
  PremiumDesign.add(overview,PremiumDesign.progress(this,24,91,
      PremiumDesign.BLUE,PremiumDesign.CYAN),10);
  PremiumDesign.add(overview,text(
    "Integrated subjects, not percent of syllabus completeness or quality.",11,
    PremiumDesign.MUTED,false),10);
  add(overview,8);

  heading("Subject Group Status");
  for(int i=0;i<6;i++)card(i);

  heading("AI Prompt Counts — Don't Mix Release Lines");
  LinearLayout notes=PremiumDesign.panel(this);
  notes.addView(text("Native GCE: 24 subjects × 8 generator modes",15,
      PremiumDesign.INK,true));
  PremiumDesign.add(notes,text(
    "192 possible subject/mode selections, dynamically composed — not 192 separately verified syllabus lessons.",12,
    PremiumDesign.MUTED,false),8);
  PremiumDesign.add(notes,text("Original Student AI: 100 general prompts",15,
      PremiumDesign.INK,true),13);
  PremiumDesign.add(notes,text(
    "Separate from GCE and specialization prompts.",12,
    PremiumDesign.MUTED,false),7);
  PremiumDesign.add(notes,text("Prior V2.46: 91 subjects / 910 authored prompts (reported)",15,
      PremiumDesign.INK,true),13);
  PremiumDesign.add(notes,text(
    "Those are from another source package. This GitHub Android project has not imported or independently validated all 910 prompts.",12,
    PremiumDesign.MUTED,false),7);
  add(notes,12);

  heading("Recommended Next Integration");
  add(text("1. Import and verify original V2.46 source packages.\n"+
    "2. Connect all 67 missing specialization subjects.\n"+
    "3. Map 10 prompt types per subject to lessons and exercises.\n"+
    "4. Add real validation/coverage status and per-subject subscriptions.\n"+
    "5. Test learning screens on Android devices.",13,PremiumDesign.INK,false),10);
  add(PremiumDesign.action(this,"Back to Student AI Home",()->finish(),true),14);
 }
}
