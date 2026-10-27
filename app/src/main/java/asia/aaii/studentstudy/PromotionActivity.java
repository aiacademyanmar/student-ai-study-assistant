package asia.aaii.studentstudy;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
/** In-app campaign page. No fake store availability or unsupported app-download claims. */
public final class PromotionActivity extends Activity {
 private int dp(int n){return StudyDesign.dp(this,n);}
 @Override public void onCreate(Bundle state){
  super.onCreate(state);
  LinearLayout root=StudyDesign.col(this);root.setBackgroundColor(StudyDesign.BG);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,i)->{
   android.graphics.Insets b=i.getInsets(WindowInsets.Type.systemBars());
   v.setPadding(b.left,b.top,b.right,b.bottom);return i;
  });
  TextView back=StudyDesign.text(this,"‹     Student AI · Learning Together",19,StudyDesign.INK,true);
  back.setPadding(dp(16),dp(16),dp(10),dp(16));back.setBackgroundColor(-1);
  back.setOnClickListener(v->finish());root.addView(back);
  ScrollView sc=new ScrollView(this);LinearLayout body=StudyDesign.col(this);
  body.setPadding(dp(16),dp(16),dp(16),dp(24));sc.addView(body);
  root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
  body.addView(StudentPromoBanner.create(this,"MYANMAR STUDENTS","Study Today.\nShine Tomorrow.","GCE O Level + AS / A Level","Learn Now",()->startActivity(new Intent(this,OfflineQuizActivity.class)),true));
  TextView slogan=StudyDesign.text(this,"ကျောင်းသားများအတွက် AI ပညာသင်ဖော်",22,StudyDesign.BLUE,true);
  LinearLayout.LayoutParams m=new LinearLayout.LayoutParams(-1,-2);m.topMargin=dp(22);
  body.addView(slogan,m);
  for(String line:new String[]{"📘  GCE O Level · 12 Subjects","🎓  AS & A Level · 12 Subjects","✅  Cambridge 2026 PDF Reference Audit","🧠  Offline Quiz · 192 Original MCQs","✍  Written Practice · 48 Sample Tasks"}){
   TextView t=StudyDesign.text(this,line,15,StudyDesign.INK,true);
   LinearLayout.LayoutParams a=new LinearLayout.LayoutParams(-1,-2);a.topMargin=dp(13);body.addView(t,a);
  }
  TextView note=StudyDesign.text(this,"Study exercises are original, not official Cambridge past papers. AI feedback requires a configured or external AI provider.",12,StudyDesign.MUTED,false);
  LinearLayout.LayoutParams n=new LinearLayout.LayoutParams(-1,-2);n.topMargin=dp(17);body.addView(note,n);
  TextView share=StudyDesign.action(this,"Share Student AI Study Assistant",true,()->{
   Intent i=new Intent(Intent.ACTION_SEND);i.setType("text/plain");
   i.putExtra(Intent.EXTRA_TEXT,"Student AI Study Assistant — GCE O Level + AS/A Level. Learn, practise and review. https://github.com/aiacademyanmar/student-ai-study-assistant");
   startActivity(Intent.createChooser(i,"Share App"));
  });
  LinearLayout.LayoutParams a=new LinearLayout.LayoutParams(-1,-2);a.topMargin=dp(19);body.addView(share,a);
  TextView home=StudyDesign.action(this,"Go to Home",false,()->finish());
  LinearLayout.LayoutParams b=new LinearLayout.LayoutParams(-1,-2);b.topMargin=dp(12);body.addView(home,b);
  root.addView(StudyDesign.navigation(this,PromotionActivity.class));
 }
}