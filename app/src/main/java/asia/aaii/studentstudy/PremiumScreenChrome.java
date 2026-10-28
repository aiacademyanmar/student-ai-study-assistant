package asia.aaii.studentstudy;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Original native Android screen chrome inspired by the 20 user-approved mockups.
 * Raster artwork is limited to illustrative banners; labels, progress and actions
 * remain accessible, responsive, clickable Android View elements.
 */
public final class PremiumScreenChrome {
 private PremiumScreenChrome(){}
 private static int px(Context c,int x){return StudyDesign.dp(c,x);}
 private static final int[] ACCENTS={
  0xFF1677F8,0xFF8F48EF,0xFF03BFAE,0xFFFFAF27,0xFFF14B88,0xFF2563DE
 };
 public static FrameLayout hero(Activity activity,int page,String title,String subtitle,int height){
  FrameLayout shell=new FrameLayout(activity);
  shell.setBackground(PremiumDesign.fill(activity,PremiumDesign.WHITE,22));
  shell.setElevation(px(activity,3));
  shell.setClipToOutline(true);
  int id=activity.getResources().getIdentifier(String.format(java.util.Locale.US,
      "ui_hero_%02d",Math.max(1,Math.min(20,page))),"drawable",activity.getPackageName());
  if(id!=0){
   ImageView art=new ImageView(activity);
   art.setImageResource(id);art.setScaleType(ImageView.ScaleType.CENTER_CROP);
   shell.addView(art,new FrameLayout.LayoutParams(-1,-1));
  }else{
   View fallback=new View(activity);
   fallback.setBackground(PremiumDesign.grad(activity,PremiumDesign.BLUE,PremiumDesign.PURPLE,22));
   shell.addView(fallback,new FrameLayout.LayoutParams(-1,-1));
  }
  View shade=new View(activity);
  GradientDrawable overlay=new GradientDrawable(
     GradientDrawable.Orientation.LEFT_RIGHT,
     new int[]{0xED061C5F,0xB607248A,0x080D6EAA});
  shade.setBackground(overlay);shell.addView(shade,new FrameLayout.LayoutParams(-1,-1));
  LinearLayout copy=PremiumDesign.column(activity);
  copy.setGravity(Gravity.CENTER_VERTICAL);
  copy.setPadding(px(activity,15),px(activity,13),px(activity,4),px(activity,13));
  FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(px(activity,215),-1,Gravity.LEFT);
  shell.addView(copy,lp);
  TextView kicker=PremiumDesign.text(activity,"STUDENT AI • GCE 2026",10,0xFFB5EEFF,true);
  copy.addView(kicker);
  TextView main=PremiumDesign.text(activity,title,height>190?22:19,Color.WHITE,true);
  main.setMaxLines(3);main.setEllipsize(android.text.TextUtils.TruncateAt.END);
  LinearLayout.LayoutParams m=new LinearLayout.LayoutParams(-1,-2);m.topMargin=px(activity,6);
  copy.addView(main,m);
  TextView detail=PremiumDesign.text(activity,subtitle,11,0xFFE5F6FF,false);
  detail.setMaxLines(height>160?3:2);
  LinearLayout.LayoutParams d=new LinearLayout.LayoutParams(-1,-2);d.topMargin=px(activity,8);
  copy.addView(detail,d);
  shell.setContentDescription(title+". "+subtitle);
  shell.setMinimumHeight(px(activity,height));
  return shell;
 }
 public static LinearLayout entry(Activity a,String label,Runnable click,int position){
  LinearLayout outer=PremiumDesign.row(a);
  outer.setGravity(Gravity.CENTER_VERTICAL);
  outer.setBackground(PremiumDesign.outline(a,Color.WHITE,PremiumDesign.BORDER,18));
  outer.setPadding(px(a,9),px(a,10),px(a,9),px(a,10));
  outer.setElevation(px(a,2));
  int accent=ACCENTS[Math.abs(position)%ACCENTS.length];
  String symbol=iconFor(label);
  TextView icon=PremiumDesign.text(a,symbol,24,Color.WHITE,true);
  icon.setGravity(Gravity.CENTER);
  icon.setBackground(PremiumDesign.grad(a,accent,ACCENTS[(position+1)%ACCENTS.length],14));
  outer.addView(icon,new LinearLayout.LayoutParams(px(a,52),px(a,52)));
  LinearLayout words=PremiumDesign.column(a);words.setPadding(px(a,11),0,0,0);
  TextView title=PremiumDesign.text(a,strip(label),14,PremiumDesign.INK,true);
  title.setMaxLines(2);title.setEllipsize(android.text.TextUtils.TruncateAt.END);
  words.addView(title);
  String hint=hintFor(label);
  TextView description=PremiumDesign.text(a,hint,11,PremiumDesign.MUTED,false);
  LinearLayout.LayoutParams hintParams=new LinearLayout.LayoutParams(-1,-2);
  hintParams.topMargin=px(a,5);words.addView(description,hintParams);
  outer.addView(words,new LinearLayout.LayoutParams(0,-2,1));
  TextView arrow=PremiumDesign.text(a,"›",24,accent,true);
  arrow.setGravity(Gravity.CENTER);outer.addView(arrow,
     new LinearLayout.LayoutParams(px(a,28),px(a,50)));
  outer.setMinimumHeight(px(a,75));outer.setClickable(true);outer.setFocusable(true);
  outer.setContentDescription(label);
  outer.setOnClickListener(v->{v.animate().scaleX(.985f).scaleY(.985f).setDuration(65)
   .withEndAction(()->v.animate().scaleX(1).scaleY(1).setDuration(105).start()).start();
   click.run();
  });
  return outer;
 }
 private static String strip(String s){
  return s.replace("→","").replace("›","").trim();
 }
 private static String iconFor(String s){
  String l=s.toLowerCase(java.util.Locale.ROOT);
  if(l.contains("pdf")||l.contains("syllabus")||l.contains("audit"))return "▣";
  if(l.contains("quiz")||l.contains("mcq")||l.contains("exam"))return "✓";
  if(l.contains("ai")||l.contains("prompt")||l.contains("tutor"))return "✦";
  if(l.contains("case")||l.contains("study"))return "▤";
  if(l.contains("course")||l.contains("lesson"))return "▶";
  if(l.contains("result")||l.contains("analytic")||l.contains("progress"))return "▥";
  if(l.contains("practice")||l.contains("written")||l.contains("notes"))return "✎";
  if(l.contains("setting")||l.contains("profile"))return "⚙";
  return "✧";
 }
 private static String hintFor(String s){
  String l=s.toLowerCase(java.util.Locale.ROOT);
  if(l.contains("pdf")||l.contains("syllabus"))return "Official PDF and your review notes";
  if(l.contains("quiz")||l.contains("mcq"))return "Original questions with saved local scores";
  if(l.contains("ai")||l.contains("prompt")||l.contains("tutor"))return "Open a learning assistant or prompt";
  if(l.contains("case"))return "Original offline learning scenario";
  if(l.contains("course"))return "Explore available learning resources";
  if(l.contains("practice")||l.contains("written"))return "Try one exercise and check your work";
  if(l.contains("progress")||l.contains("result"))return "Review your saved learning history";
  return "Tap to explore";
 }
}
