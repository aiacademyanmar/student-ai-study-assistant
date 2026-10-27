package asia.aaii.studentstudy;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.Gravity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.net.Uri;

public class AcademyWebViewActivity extends Activity {
 private static final String HOME="https://academy.openai.com/pages/courses?linkMenu=Courses";
 private WebView browser;
 private LinearLayout root;
 private int dp(int x){return (int)(x*getResources().getDisplayMetrics().density+.5f);}
 private TextView label(String s,int size){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(0xFF142B58);t.setGravity(Gravity.CENTER);return t;}
 private void navigate(String route){
   if("courses".equals(route))return;
   Intent intent;
   if("tutor".equals(route))intent=new Intent(this,NativeChatActivity.class);
   else {intent=new Intent(this,CatalogActivity.class);intent.putExtra("open_page",route);}
   startActivity(intent);finish();
 }
 @Override public void onCreate(Bundle state){
  super.onCreate(state);
  getWindow().setStatusBarColor(Color.WHITE);getWindow().setNavigationBarColor(Color.WHITE);
  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
  root=new LinearLayout(this);root.setOrientation(1);root.setBackgroundColor(Color.WHITE);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,insets)->{
   android.graphics.Insets b=insets.getInsets(WindowInsets.Type.systemBars());
   v.setPadding(b.left,b.top,b.right,b.bottom);return insets;
  });
  LinearLayout toolbar=new LinearLayout(this);toolbar.setGravity(Gravity.CENTER_VERTICAL);toolbar.setPadding(dp(8),dp(8),dp(8),dp(8));
  TextView back=label("‹",30);toolbar.addView(back,new LinearLayout.LayoutParams(dp(40),dp(42)));
  back.setOnClickListener(v->{if(browser.canGoBack())browser.goBack();else navigate("home");});
  TextView title=label("🎓 OpenAI Academy",19);title.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
  toolbar.addView(title,new LinearLayout.LayoutParams(0,dp(44),1));
  TextView refresh=label("↻",25);toolbar.addView(refresh,new LinearLayout.LayoutParams(dp(44),dp(44)));refresh.setOnClickListener(v->browser.reload());
  TextView external=label("↗",24);toolbar.addView(external,new LinearLayout.LayoutParams(dp(44),dp(44)));
  external.setOnClickListener(v->startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(browser.getUrl()==null?HOME:browser.getUrl()))));
  root.addView(toolbar);
  ProgressBar progress=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);root.addView(progress,new LinearLayout.LayoutParams(-1,dp(3)));
  browser=new WebView(this);
  browser.getSettings().setJavaScriptEnabled(true);
  browser.getSettings().setDomStorageEnabled(true);
  browser.getSettings().setAllowFileAccess(false);
  browser.getSettings().setAllowContentAccess(false);
  browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
  browser.getSettings().setSupportMultipleWindows(false);
  browser.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW);
  android.webkit.CookieManager.getInstance().setAcceptCookie(true);
  browser.setWebChromeClient(new WebChromeClient(){@Override public void onProgressChanged(WebView view,int n){progress.setProgress(n);progress.setVisibility(n>=100?View.GONE:View.VISIBLE);}});
  browser.setWebViewClient(new WebViewClient(){
   @Override public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest req){
    Uri uri=req.getUrl();String scheme=uri.getScheme()==null?"":uri.getScheme();
    if("https".equalsIgnoreCase(scheme)){
     String host=uri.getHost()==null?"":uri.getHost().toLowerCase(java.util.Locale.ROOT);
     if(host.equals("academy.openai.com")||host.endsWith(".academy.openai.com")||host.equals("auth.openai.com")||host.equals("chatgpt.com")||host.endsWith(".openai.com"))return false;
     try{startActivity(new Intent(Intent.ACTION_VIEW,uri));}catch(Exception ignored){}
     return true;
    }
    if("http".equalsIgnoreCase(scheme))return true;
    if("intent".equalsIgnoreCase(scheme))return true;
    try{startActivity(new Intent(Intent.ACTION_VIEW,uri));}catch(Exception ignored){}
    return true;
   }
  });
  root.addView(browser,new LinearLayout.LayoutParams(-1,0,1));
  LinearLayout footer=new LinearLayout(this);footer.setBackgroundColor(Color.WHITE);footer.setPadding(dp(2),dp(5),dp(2),dp(5));
  String[] items={"ပင်မ","စကားဝိုင်း","AI ဆရာ","သင်တန်း","ပိုမို"};
  String[] icons={"⌂","▤","✦","▣","☰"};
  String[] routes={"home","list","tutor","courses","profile"};
  for(int i=0;i<5;i++){
   final String route=routes[i];LinearLayout cell=new LinearLayout(this);cell.setOrientation(1);cell.setGravity(Gravity.CENTER);
   TextView glyph=label(icons[i],22);glyph.setTextColor(i==3?0xFF1264EE:0xFF708099);cell.addView(glyph);
   TextView caption=label(items[i],10);caption.setTextColor(i==3?0xFF1264EE:0xFF708099);cell.addView(caption);
   cell.setOnClickListener(v->navigate(route));footer.addView(cell,new LinearLayout.LayoutParams(0,dp(64),1));
  }
  root.addView(footer);
  String target=getIntent().getStringExtra("target_url");
  if(target==null||!target.startsWith("https://academy.openai.com/"))target=HOME;
  browser.loadUrl(target);
 }
 @Override public void onBackPressed(){if(browser!=null&&browser.canGoBack())browser.goBack();else navigate("home");}
 @Override protected void onDestroy(){if(browser!=null){browser.stopLoading();browser.destroy();browser=null;}super.onDestroy();}
}
