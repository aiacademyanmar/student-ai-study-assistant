package asia.aaii.studentstudy;

import android.content.Context;
import android.graphics.*;
import android.view.View;

/** Original lightweight Android Canvas schoolgirl/schoolboy illustration.
 * Works without the network or binary image assets, scales to any Samsung screen.
 */
public final class StudentsIllustrationView extends View {
 private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
 public StudentsIllustrationView(Context c){super(c);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
 private void color(int c){p.reset();p.setAntiAlias(true);p.setColor(c);p.setStyle(Paint.Style.FILL);}
 private void rect(Canvas c,float l,float t,float r,float b,int hue,float radius){
  color(hue);c.drawRoundRect(l,t,r,b,radius,radius,p);
 }
 private void ellipse(Canvas c,float l,float t,float r,float b,int hue){
  color(hue);c.drawOval(l,t,r,b,p);
 }
 private void polygon(Canvas c,int hue,float...pts){
  color(hue);Path path=new Path();path.moveTo(pts[0],pts[1]);
  for(int i=2;i<pts.length;i+=2)path.lineTo(pts[i],pts[i+1]);
  path.close();c.drawPath(path,p);
 }
 private void stroke(Canvas c,float x1,float y1,float x2,float y2,int clr,float width){
  color(clr);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(width);p.setStrokeCap(Paint.Cap.ROUND);
  c.drawLine(x1,y1,x2,y2,p);p.setStyle(Paint.Style.FILL);
 }
 private void cloud(Canvas c,float x,float y,float k){
  ellipse(c,x,y+9*k,x+44*k,y+25*k,0xA9FFFFFF);
  ellipse(c,x+10*k,y,x+42*k,y+30*k,0xD9FFFFFF);
  ellipse(c,x+32*k,y+7*k,x+62*k,y+28*k,0xBCFFFFFF);
 }
 private void pagoda(Canvas c,float x,float base,float scale,int hue){
  c.save();c.translate(x,base);c.scale(scale,scale);
  rect(c,-35,-14,35,0,hue,2);
  rect(c,-25,-35,25,-12,hue,4);
  polygon(c,hue,-20,-35,20,-35,9,-58,-9,-58);
  polygon(c,hue,-11,-58,11,-58,7,-84,-7,-84);
  polygon(c,0xFFFFD17B,-8,-84,8,-84,2,-109,-2,-109);
  stroke(c,0,-107,0,-120,0xFFF6BF3A,2);
  ellipse(c,-4,-124,4,-116,0xFFFFDE75);
  c.restore();
 }
 private void boy(Canvas c){
  ellipse(c,350,137,454,280,0xFF47598C); // backpack
  polygon(c,0xFF203D7C,328,189,382,164,446,208,471,320,316,320);
  polygon(c,0xFFF6FBFF,355,186,389,169,431,197,444,320,338,320);
  polygon(c,0xFF32456D,367,183,380,177,374,320,354,320);
  polygon(c,0xFF3656A6,416,184,424,199,446,317,461,317);
  ellipse(c,365,147,396,197,0xFFF1B994); // neck
  ellipse(c,337,55,433,166,0xFFECAD85); // ears, face
  ellipse(c,337,104,356,134,0xFFF4BE97);
  ellipse(c,419,104,437,131,0xFFEFB48D);
  ellipse(c,349,65,422,158,0xFFF9CEA8);
  ellipse(c,350,70,430,145,0xFFFFD6AF);
  // hair fringe
  ellipse(c,335,42,436,105,0xFF1F2439);
  polygon(c,0xFF1C2336,337,88,348,48,394,35,423,55,439,72,427,90,413,69,399,86,374,72,349,92);
  ellipse(c,355,94,366,100,0xFF1F293E);
  ellipse(c,398,95,409,101,0xFF1F293E);
  ellipse(c,359,95,363,99,0xFFFFFFFF);ellipse(c,401,96,405,99,0xFFFFFFFF);
  stroke(c,378,120,386,121,0xFFD88E6F,2);
  stroke(c,374,137,388,138,0xFFB76E5B,3);
  polygon(c,0xFFDAEFF9,370,183,394,205,417,185,411,171,389,183,374,171);
  // dark tablet
  rect(c,352,231,445,310,0xFF1D2D59,9);
  rect(c,356,235,439,299,0xFF334779,6);
  ellipse(c,396,303,402,308,0xFF9CCEFF);
 }
 private void girl(Canvas c){
  ellipse(c,441,89,570,254,0xFF2B2439); // back hair
  polygon(c,0xFFF9FCFF,438,198,482,184,530,202,566,320,427,320);
  polygon(c,0xFF424B76,443,193,427,218,431,320,454,320);
  polygon(c,0xFF263A79,518,202,540,229,560,320,532,320);
  ellipse(c,474,161,501,206,0xFFEFB18D);
  ellipse(c,446,100,533,187,0xFFF7C39D);
  ellipse(c,441,119,452,146,0xFFF2BC95);
  ellipse(c,522,119,535,146,0xFFF2BC95);
  ellipse(c,452,105,523,178,0xFFFFD5AF);
  // hair top
  ellipse(c,441,81,537,128,0xFF292435);
  polygon(c,0xFF27243B,446,119,456,92,488,82,529,104,533,141,520,120,508,98,475,115,454,125);
  ellipse(c,451,150,467,264,0xFF302639);
  ellipse(c,510,149,539,275,0xFF2B2539);
  ellipse(c,463,129,473,135,0xFF273047);ellipse(c,499,129,509,135,0xFF273047);
  ellipse(c,466,131,469,134,0xFFFFFFFF);ellipse(c,502,131,505,134,0xFFFFFFFF);
  stroke(c,479,157,492,157,0xFFBF776F,2.5f);
  polygon(c,0xFFF5FAFD,472,191,484,210,496,191,499,183,481,195);
  // books
  rect(c,471,225,558,309,0xFF4936AB,7);
  rect(c,474,226,554,298,0xFF8462D8,6);
  rect(c,479,228,550,240,0xFFAD98EC,2);
  stroke(c,485,243,545,243,0xFFDED6FA,3);
  stroke(c,484,286,549,286,0xFFDED6FA,3);
  ellipse(c,466,282,485,313,0xFFF7C7A2);
 }
 @Override protected void onDraw(Canvas canvas){
  super.onDraw(canvas);
  if(getWidth()<=0||getHeight()<=0)return;
  canvas.save();canvas.scale(getWidth()/600f,getHeight()/320f);
  Path clip=new Path();clip.addRoundRect(0,0,600,320,27,27,Path.Direction.CW);canvas.clipPath(clip);
  color(0xFFCAEFFF);p.setShader(new LinearGradient(0,0,600,320,new int[]{
   0xFFCCEFFF,0xFFFDF5F0,0xFFB9C9FF},null,Shader.TileMode.CLAMP));
  canvas.drawRect(0,0,600,320,p);p.setShader(null);
  cloud(canvas,15,35,1.2f);cloud(canvas,155,75,.8f);cloud(canvas,490,21,1.2f);
  ellipse(canvas,-90,219,400,425,0xFFBEDCF7);
  ellipse(canvas,330,209,750,470,0xFFA5CEF3);
  pagoda(canvas,252,300,0.85f,0xFFFFC870);
  pagoda(canvas,574,275,1.28f,0xFFF5B75C);
  pagoda(canvas,96,299,.52f,0xFFDEC493);
  ellipse(canvas,280,245,625,430,0xFFA7B9F2);
  boy(canvas);girl(canvas);
  canvas.restore();
 }
}
