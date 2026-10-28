package asia.aaii.studentstudy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Safe, simple launcher with a single visible OPEN APP action.
 * Avoid custom Canvas drawing on the first frame to reduce device-specific startup risk.
 */
public final class StartActivity extends Activity {
 private int dp(int value){return StudyDesign.dp(this,value);}
 private TextView label(String value,int size,boolean bold){
  TextView t=PremiumDesign.text(this,value,size,PremiumDesign.WHITE,bold);
  t.setGravity(Gravity.CENTER);return t;
 }
 private void gap(LinearLayout root,int height){
  android.view.View v=new android.view.View(this);
  root.addView(v,new LinearLayout.LayoutParams(1,dp(height)));
 }
 private void open(Class<?> target,boolean premium){
  try{
   Intent intent=new Intent(this,target);
   if(premium)intent.putExtra("page",2);
   startActivity(intent);
  }catch(Exception error){
   Toast.makeText(this,"Cannot launch interface: "+error.getClass().getSimpleName(),
    Toast.LENGTH_LONG).show();
  }
 }
 @Override public void onCreate(Bundle state){
  super.onCreate(state);
  getWindow().setStatusBarColor(0xFF124BDC);
  getWindow().setNavigationBarColor(0xFF2620A9);
  LinearLayout root=StudyDesign.col(this);
  root.setBackground(PremiumDesign.grad(this,0xFF0F8CF3,0xFF5120D8,0));
  setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,i)->{
   android.graphics.Insets in=i.getInsets(WindowInsets.Type.systemBars());
   v.setPadding(in.left,in.top,in.right,in.bottom);return i;
  });
  ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);
  root.addView(scroll,new LinearLayout.LayoutParams(-1,-1));
  LinearLayout body=StudyDesign.col(this);
  body.setGravity(Gravity.CENTER);
  body.setPadding(dp(24),dp(25),dp(24),dp(30));scroll.addView(body);
  ImageView icon=new ImageView(this);icon.setImageResource(R.mipmap.ic_launcher);
  icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
  LinearLayout.LayoutParams pic=new LinearLayout.LayoutParams(dp(120),dp(120));
  pic.gravity=Gravity.CENTER;body.addView(icon,pic);
  gap(body,16);
  body.addView(label("Student AI",36,true));
  body.addView(label("Study Assistant",23,true));
  gap(body,14);
  body.addView(label("GCE O Level  •  AS & A Level",16,false));
  body.addView(label("AI နဲ့အတူ ပိုမိုထိရောက်စွာ သင်ယူပါ",16,true));
  gap(body,32);
  TextView open=PremiumDesign.action(this,"OPEN APP    ▶",()->open(StudentTwentyPagesActivity.class,true),true);
  open.setTextSize(20);open.setMinHeight(dp(62));open.setContentDescription("Open Student AI Study Assistant");
  body.addView(open,new LinearLayout.LayoutParams(-1,-2));
  gap(body,13);
  gap(body,25);
  body.addView(label("V2.39.0 · Native Android UI",13,false));
  gap(body,9);
  body.addView(label("OPEN APP ကိုနှိပ်ပြီး Student AI Home သို့ ဝင်ရောက်ပါ။",12,false));
 }
}
