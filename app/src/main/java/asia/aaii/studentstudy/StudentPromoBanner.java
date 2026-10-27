package asia.aaii.studentstudy;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Native promo/banner element shared by Home, Audit, Offline Quiz, and Written Practice. */
public final class StudentPromoBanner {
 private StudentPromoBanner(){}
 public static View create(Context context,String eyebrow,String headline,String description,String cta,Runnable onClick,boolean tall){
  final int h=tall?238:180;
  FrameLayout frame=new FrameLayout(context);
  frame.setBackground(StudyDesign.stroked(context,0xFFE1F3FF,0xFFCFDDF9,22));
  frame.setClipToOutline(true);
  frame.setElevation(StudyDesign.dp(context,2));
  StudentsIllustrationView art=new StudentsIllustrationView(context);
  frame.addView(art,new FrameLayout.LayoutParams(-1,-1));
  LinearLayout words=StudyDesign.col(context);
  words.setGravity(Gravity.CENTER_VERTICAL);
  words.setPadding(StudyDesign.dp(context,15),StudyDesign.dp(context,11),0,StudyDesign.dp(context,12));
  FrameLayout.LayoutParams textBox=new FrameLayout.LayoutParams(-1,-1);
  frame.addView(words,textBox);
  TextView top=StudyDesign.text(context,eyebrow,11,0xFF2144A4,true);
  top.setMaxLines(2);words.addView(top,new LinearLayout.LayoutParams(StudyDesign.dp(context,170),-2));
  TextView main=StudyDesign.text(context,headline,tall?23:20,0xFF1434B3,true);
  main.setMaxLines(tall?4:3);
  LinearLayout.LayoutParams mp=new LinearLayout.LayoutParams(StudyDesign.dp(context,194),-2);
  mp.topMargin=StudyDesign.dp(context,7);words.addView(main,mp);
  TextView details=StudyDesign.text(context,description,12,0xFF2D539B,false);
  details.setMaxLines(3);
  LinearLayout.LayoutParams dp=new LinearLayout.LayoutParams(StudyDesign.dp(context,180),-2);
  dp.topMargin=StudyDesign.dp(context,6);words.addView(details,dp);
  if(cta!=null&&!cta.isEmpty()&&onClick!=null){
   TextView action=StudyDesign.text(context,cta+"  ›",12,Color.WHITE,true);
   action.setGravity(Gravity.CENTER);
   action.setMinHeight(StudyDesign.dp(context,36));
   action.setPadding(StudyDesign.dp(context,12),StudyDesign.dp(context,5),
      StudyDesign.dp(context,12),StudyDesign.dp(context,5));
   action.setBackground(StudyDesign.gradient(context,0xFF2365FA,0xFF7D39E5,20));
   action.setOnClickListener(v->onClick.run());action.setContentDescription(cta);
   LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(StudyDesign.dp(context,165),-2);
   lp.topMargin=StudyDesign.dp(context,10);words.addView(action,lp);
  }
  if(onClick!=null)frame.setOnClickListener(v->onClick.run());
  frame.setContentDescription(headline+" "+description);
  frame.setMinimumHeight(StudyDesign.dp(context,h));
  frame.setLayoutParams(new LinearLayout.LayoutParams(-1,StudyDesign.dp(context,h)));
  return frame;
 }
}
