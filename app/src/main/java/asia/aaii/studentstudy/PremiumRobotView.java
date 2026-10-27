package asia.aaii.studentstudy;
import android.content.Context;
import android.graphics.*;
import android.view.View;

/** Scalable native AI study mascot. Drawn entirely on Canvas; never needs internet. */
public class PremiumRobotView extends View {
 private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
 public PremiumRobotView(Context c){super(c);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
 private void fill(Canvas c,int color,float l,float t,float r,float b,float radius){
  p.setStyle(Paint.Style.FILL);p.setColor(color);p.clearShadowLayer();c.drawRoundRect(l,t,r,b,radius,radius,p);
 }
 private void oval(Canvas c,int color,float l,float t,float r,float b){p.setColor(color);p.setStyle(Paint.Style.FILL);p.clearShadowLayer();c.drawOval(l,t,r,b,p);}
 private void path(Canvas c,int color,float...xy){Path t=new Path();t.moveTo(xy[0],xy[1]);for(int i=2;i<xy.length;i+=2)t.lineTo(xy[i],xy[i+1]);t.close();p.setColor(color);p.setStyle(Paint.Style.FILL);p.clearShadowLayer();c.drawPath(t,p);}
 private void stroke(Canvas c,int color,float thick,float...xy){Path t=new Path();t.moveTo(xy[0],xy[1]);for(int i=2;i<xy.length;i+=2)t.lineTo(xy[i],xy[i+1]);p.setColor(color);p.setStrokeWidth(thick);p.setStyle(Paint.Style.STROKE);p.setStrokeCap(Paint.Cap.ROUND);c.drawPath(t,p);p.setStyle(Paint.Style.FILL);}
 private void shine(Canvas c,float x,float y,float r,int color){p.setColor(color);p.setShadowLayer(r,0,0,color);c.drawCircle(x,y,Math.max(1,r/6),p);p.clearShadowLayer();}
 @Override protected void onDraw(Canvas c){super.onDraw(c);if(getWidth()==0||getHeight()==0)return;
  c.save();c.scale(getWidth()/600f,getHeight()/400f);
  // cloud and sparkle motifs
  oval(c,0x33FFFFFF,55,345,315,436);oval(c,0x55FFFFFF,320,341,635,434);
  for(float[] a:new float[][]{{68,94,15},{535,61,19},{513,316,12},{88,285,12},{482,158,9}})
   shine(c,a[0],a[1],a[2],0xFFB2FFFF);
  // torso with blue collar and white pearly material
  oval(c,0xFFB5E3FF,178,268,429,424);
  oval(c,0xFFFFFFFF,195,277,413,438);
  path(c,0xFF1E65DF,194,345,243,301,294,338,347,300,409,345,410,430,190,430);
  oval(c,0xFF0B65DB,268,314,337,378);
  oval(c,0xFF8CFFFF,281,323,325,367);oval(c,0xFFFAFFFF,290,330,316,357);
  // arms waving
  stroke(c,0xFF1867CE,38,415,320,480,264,498,213);
  stroke(c,0xFFF0FBFF,27,415,320,480,265,496,222);
  oval(c,0xFF1A82E8,467,187,533,249);
  oval(c,0xFFD2FBFF,476,197,528,245);
  stroke(c,0xFF0D69DB,13,500,214,510,169);
  stroke(c,0xFF0D69DB,13,498,215,540,191);
  // neck
  fill(c,0xFF3287DF,270,248,334,314,24);
  // headphone black blue
  oval(c,0xFF1380EB,137,111,462,289);
  oval(c,0xFFDEF7FF,147,106,455,275);
  oval(c,0xFF1673D9,142,133,187,234);
  oval(c,0xFF2D4FE0,408,129,468,247);
  oval(c,0xFF75E6FF,154,146,181,222);
  oval(c,0xFFAAFBFF,431,150,458,219);
  // head white shell
  fill(c,0xFFF5FCFF,180,102,425,265,67);
  fill(c,0xFFD3EEFF,188,118,412,259,61);
  fill(c,0xFFFAFFFF,190,111,415,248,63);
  // face black glass
  fill(c,0xFF031849,211,137,394,232,36);
  fill(c,0xFF07102D,223,148,388,224,30);
  // eyes cyan smile
  stroke(c,0xFF7BFFFF,13,249,189,254,177,261,171,268,174,271,189);
  stroke(c,0xFF7BFFFF,13,331,189,339,174,345,171,355,178,358,188);
  stroke(c,0xFF73F6FF,9,281,201,300,211,317,202);
  // graduation cap navy/magenta
  path(c,0xFF13289B,154,103,304,34,467,101,303,170);
  path(c,0xFF13256E,157,101,306,42,467,104,304,150);
  path(c,0xFF344EE5,178,99,306,54,434,99,304,143);
  fill(c,0xFF152992,239,108,371,143,10);
  oval(c,0xFF142568,248,124,360,155);
  stroke(c,0xFFFFC855,7,452,110,483,157,483,174);
  oval(c,0xFFFFE081,471,165,495,195);
  // book lower front
  path(c,0xFF008BCF,137,287,280,288,303,306,303,365,256,349,137,355);
  path(c,0xFF303AD8,303,307,327,285,451,283,451,353,330,351,303,366);
  path(c,0xFFF9FFFF,155,286,269,297,299,318,300,346,265,332,155,330);
  path(c,0xFFDCF5FF,306,316,336,295,435,284,435,335,340,333,306,351);
  stroke(c,0xFF69F9FF,6,303,314,303,366);
  c.restore();
 }
}
