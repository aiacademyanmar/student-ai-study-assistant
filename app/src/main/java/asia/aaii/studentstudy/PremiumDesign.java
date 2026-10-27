package asia.aaii.studentstudy;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Native Android premium design components; real clickable controls, no WebView. */
public final class PremiumDesign {
 private PremiumDesign(){}
 public static final int NAVY=0xFF071D61,BLUE=0xFF1166F8,CYAN=0xFF03D3F6,
  PURPLE=0xFF9628EF,INK=0xFF0E2255,MUTED=0xFF6178A7,WHITE=Color.WHITE,
  SURFACE=0xFFF0F7FF,BORDER=0xFFDCE6FA,GREEN=0xFF00BC86,AMBER=0xFFFFB021,PINK=0xFFF5429D;
 public static int dp(Context c,float n){return StudyDesign.dp(c,n);}
 public static GradientDrawable fill(Context c,int color,int radius){
  GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(c,radius));return d;
 }
 public static GradientDrawable grad(Context c,int a,int b,int radius){
  GradientDrawable d=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{a,b});
  d.setCornerRadius(dp(c,radius));return d;
 }
 public static GradientDrawable outline(Context c,int color,int border,int radius){
  GradientDrawable d=fill(c,color,radius);d.setStroke(dp(c,1),border);return d;
 }
 public static TextView text(Context c,String value,int sp,int color,boolean bold){
  TextView t=StudyDesign.text(c,value,sp,color,bold);t.setIncludeFontPadding(true);return t;
 }
 public static LinearLayout column(Context c){return StudyDesign.col(c);}
 public static LinearLayout row(Context c){return StudyDesign.row(c);}
 public static LinearLayout panel(Context c){
  LinearLayout p=column(c);p.setBackground(outline(c,WHITE,BORDER,22));
  p.setPadding(dp(c,14),dp(c,14),dp(c,14),dp(c,14));p.setElevation(dp(c,2));return p;
 }
 public static void add(LinearLayout host,View view,int top){
  LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1,-2);
  params.topMargin=dp(host.getContext(),top);host.addView(view,params);
 }
 public static void addHeight(LinearLayout host,View view,int height,int top){
  LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1,dp(host.getContext(),height));
  params.topMargin=dp(host.getContext(),top);host.addView(view,params);
 }
 public static TextView pill(Context c,String text,boolean selected){
  TextView v=text(c,text,11,selected?WHITE:INK,true);
  v.setGravity(Gravity.CENTER);v.setPadding(dp(c,10),dp(c,9),dp(c,10),dp(c,9));
  v.setBackground(selected?grad(c,BLUE,PURPLE,24):fill(c,0xFFE8F1FF,24));
  return v;
 }
 public static TextView action(Context c,String value,Runnable run,boolean primary){
  TextView v=text(c,value,14,primary?WHITE:BLUE,true);
  v.setGravity(Gravity.CENTER);v.setMinHeight(dp(c,49));
  GradientDrawable bg=primary?grad(c,CYAN,PURPLE,22):outline(c,WHITE,0xFFA6C7FF,22);
  v.setBackground(new RippleDrawable(ColorStateList.valueOf(0x44FFFFFF),bg,null));
  v.setPadding(dp(c,14),dp(c,10),dp(c,14),dp(c,10));v.setClickable(true);v.setFocusable(true);
  v.setContentDescription(value);
  if(run!=null)v.setOnClickListener(w->{w.animate().scaleX(.98f).scaleY(.98f).setDuration(70).withEndAction(
   ()->w.animate().scaleX(1f).scaleY(1f).setDuration(110).start()).start();run.run();});
  return v;
 }
 public static LinearLayout quick(Context c,String symbol,String title,String extra,int a,int b,Runnable run){
  LinearLayout tile=column(c);tile.setGravity(Gravity.CENTER);
  tile.setPadding(dp(c,3),dp(c,11),dp(c,3),dp(c,9));
  tile.setBackground(grad(c,a,b,19));tile.setElevation(dp(c,2));
  TextView icon=text(c,symbol,29,WHITE,true);icon.setGravity(Gravity.CENTER);
  tile.addView(icon,new LinearLayout.LayoutParams(-1,dp(c,43)));
  TextView name=text(c,title,13,WHITE,true);name.setGravity(Gravity.CENTER);tile.addView(name);
  if(extra!=null&&!extra.isEmpty()){
   TextView sub=text(c,extra,9,WHITE,false);sub.setMaxLines(2);sub.setGravity(Gravity.CENTER);
   tile.addView(sub);
  }
  tile.setContentDescription(title);tile.setOnClickListener(v->run.run());return tile;
 }
 public static void rowOfFour(LinearLayout host,LinearLayout[] items,int h){
  Context c=host.getContext();LinearLayout row=row(c);
  for(int i=0;i<items.length;i++){
   LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,dp(c,h),1);
   if(i>0)lp.leftMargin=dp(c,5);row.addView(items[i],lp);
  }
  add(host,row,10);
 }
 public static LinearLayout progress(Context c,int numerator,int denominator,int a,int b){
  LinearLayout track=row(c);track.setBackground(fill(c,0xFFDDE8FA,15));track.setClipToOutline(true);
  int pct=denominator>0?Math.round(Math.max(0,Math.min(1,numerator/(float)denominator))*1000):0;
  View done=new View(c);done.setBackground(grad(c,a,b,15));
  track.addView(done,new LinearLayout.LayoutParams(0,dp(c,9),pct));
  track.addView(new View(c),new LinearLayout.LayoutParams(0,dp(c,9),1000-pct));
  return track;
 }
 public static View progressCircle(Context c,int done,int all,int color,int diameter){
  return new View(c){
   private final Paint p=new Paint(3);
   @Override protected void onDraw(Canvas canv){
    super.onDraw(canv);float w=getWidth(),h=getHeight();
    float x=w/2f,y=h/2f,r=Math.min(w,h)*.39f;
    p.setColor(0xFFDDE7FB);p.setStrokeWidth(dp(c,8));p.setStyle(Paint.Style.STROKE);
    canv.drawCircle(x,y,r,p);
    if(all>0){
     p.setColor(color);p.setStrokeCap(Paint.Cap.ROUND);
     canv.drawArc(x-r,y-r,x+r,y+r,-90,360*Math.min(1f,done/(float)all),false,p);
    }
    p.setStyle(Paint.Style.FILL);p.setTextAlign(Paint.Align.CENTER);
    p.setTypeface(Typeface.create("sans-serif",Typeface.BOLD));
    p.setColor(INK);p.setTextSize(dp(c,all>0?21:14));
    String main=all>0?Math.round(100*done/(float)all)+"%":"—";
    canv.drawText(main,x,y+dp(c,7),p);
   }
  };
 }
 public static FrameLayout illustratedHero(Context c,String title,String subtitle,int height){
  FrameLayout root=new FrameLayout(c);
  root.setBackground(grad(c,BLUE,PURPLE,24));root.setClipToOutline(true);
  StudentsIllustrationView art=new StudentsIllustrationView(c);
  FrameLayout.LayoutParams ap=new FrameLayout.LayoutParams(-1,-1);root.addView(art,ap);
  LinearLayout words=column(c);words.setPadding(dp(c,17),dp(c,14),dp(c,0),0);
  FrameLayout.LayoutParams wp=new FrameLayout.LayoutParams(dp(c,215),-2,Gravity.TOP|Gravity.LEFT);
  root.addView(words,wp);
  TextView t=text(c,title,22,NAVY,true);t.setMaxLines(2);words.addView(t);
  add(words,text(c,subtitle,12,0xFF2756AA,true),8);
  root.setMinimumHeight(dp(c,height));root.setContentDescription(title+" "+subtitle);
  return root;
 }
 public static void headline(LinearLayout host,String title,String action,Runnable run){
  Context c=host.getContext();LinearLayout bar=row(c);
  TextView label=text(c,title,18,INK,true);bar.addView(label,new LinearLayout.LayoutParams(0,-2,1));
  if(action!=null){TextView right=text(c,action+" ›",12,BLUE,true);
    right.setPadding(dp(c,6),dp(c,8),0,dp(c,8));right.setOnClickListener(v->run.run());bar.addView(right);}
  add(host,bar,16);
 }
}
