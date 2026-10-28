package asia.aaii.studentstudy;

import android.app.Application;
import android.os.Build;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.ClipData;
import android.content.ClipboardManager;
import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Offline diagnostic capture. Only exception class and limited stack-frame locations are
 * collected, never exception messages, study answers, AI prompts, nicknames or credentials.
 * Stored privately on device, never sent automatically.
 */
public final class CrashDiagnostics extends Application {
 private static final String PREF="student_ai_crash_diagnostics_v238";
 private static final String KEY="last_crash";
 @Override public void onCreate(){
  super.onCreate();
  final Thread.UncaughtExceptionHandler old=Thread.getDefaultUncaughtExceptionHandler();
  Thread.setDefaultUncaughtExceptionHandler((thread,throwable)->{
   try{
    StringBuilder report=new StringBuilder();
    report.append("Student AI Study Assistant V").append(BuildConfig.VERSION_NAME)
       .append("\nDate: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US).format(new Date()))
       .append("\nAndroid SDK: ").append(Build.VERSION.SDK_INT)
       .append("\nThread: ").append(thread.getName())
       .append("\nException class: ").append(throwable.getClass().getName())
       .append("\nTop stack frames:");
    StackTraceElement[] frames=throwable.getStackTrace();
    int n=Math.min(frames.length,12);
    for(int i=0;i<n;i++){
     StackTraceElement frame=frames[i];
     report.append("\n").append(frame.getClassName()).append(".")
       .append(frame.getMethodName()).append(":").append(frame.getLineNumber());
    }
    getSharedPreferences(PREF,MODE_PRIVATE).edit().putString(KEY,report.toString()).commit();
   }catch(Throwable ignored){ /* crash reporting must never block normal system handler */ }
   if(old!=null)old.uncaughtException(thread,throwable);
   else android.os.Process.killProcess(android.os.Process.myPid());
  });
 }
 public static String get(Context context){
  return context.getSharedPreferences(PREF,Context.MODE_PRIVATE).getString(KEY,"");
 }
 public static boolean has(Context context){return !get(context).isEmpty();}
 public static void clear(Context context){
  context.getSharedPreferences(PREF,Context.MODE_PRIVATE).edit().remove(KEY).apply();
 }
 public static void share(Context context){
  String text=get(context);
  if(text.isEmpty())return;
  Intent send=new Intent(Intent.ACTION_SEND);
  send.setType("text/plain");
  send.putExtra(Intent.EXTRA_SUBJECT,"Student AI Study Assistant Crash Diagnostics");
  send.putExtra(Intent.EXTRA_TEXT,text);
  context.startActivity(Intent.createChooser(send,"Share diagnostic report (optional)"));
 }
}
