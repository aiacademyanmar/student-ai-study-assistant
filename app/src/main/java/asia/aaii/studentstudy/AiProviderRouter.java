package asia.aaii.studentstudy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

/**
 * One Android-native AI selector for all student prompts.
 * MAIN AI is persistent across all app screens; users may override per request.
 * ChatGPT/Gemini launches are app-only, with clipboard fallback because these apps
 * do not promise a universal external automatic-prompt submission interface.
 */
public final class AiProviderRouter {
    private AiProviderRouter(){}
    private static final String STORAGE="aaii_ai_provider_v210";
    private static final String KEY="main_ai";
    private static final String APP="app", CHATGPT="chatgpt", GEMINI="gemini";
    private static final String[] LABELS={
        "🤖 APP AI — Native Chat (API Backend လိုအပ်)",
        "✦ ChatGPT — ဖုန်းထဲက App",
        "✧ Gemini — ဖုန်းထဲက App"
    };
    private static final String[] VALUES={APP,CHATGPT,GEMINI};
    private static final String CHATGPT_PACKAGE="com.openai.chatgpt";
    private static final String GEMINI_PACKAGE="com.google.android.apps.bard";

    public static String getMain(Activity a) {
        String p=a.getSharedPreferences(STORAGE,Context.MODE_PRIVATE).getString(KEY,CHATGPT);
        if(!APP.equals(p)&&!CHATGPT.equals(p)&&!GEMINI.equals(p))return CHATGPT;
        return p;
    }
    private static void setMain(Activity a,String p){
        a.getSharedPreferences(STORAGE,Context.MODE_PRIVATE).edit().putString(KEY,p).apply();
    }
    private static int position(String p){
        for(int i=0;i<VALUES.length;i++)if(VALUES[i].equals(p))return i;
        return 1;
    }
    public static String mainLabel(Activity a){
        String p=getMain(a);
        return APP.equals(p)?"APP AI":GEMINI.equals(p)?"Gemini":"ChatGPT";
    }

    /** Show an explicit selection for every prompted task, defaulting to saved MAIN AI. */
    public static void chooseAndRun(Activity activity,String prompt,String session,String title){
        final String content=prompt==null?"":prompt.trim();
        if(content.isEmpty()){
            openMain(activity);return;
        }
        final int[] choice={position(getMain(activity))};
        final AlertDialog dialog=new AlertDialog.Builder(activity)
            .setTitle("AI ရွေးချယ်ပါ · Main AI: "+mainLabel(activity))
            .setSingleChoiceItems(LABELS,choice[0],(d,selected)->choice[0]=selected)
            .setMessage("APP AI က HTTPS Backend နှင့် အလုပ်လုပ်မည်။ ChatGPT/Gemini အတွက် Prompt ကို Clipboard သို့ကူးပြီး ဖုန်းထဲရှိ App ဖွင့်ပေးပါမည်။")
            .setPositiveButton("ယခုတစ်ကြိမ် သုံးမည်",(d,w)->run(activity,VALUES[choice[0]],content,session,title))
            .setNeutralButton("Main AI အဖြစ် သိမ်း၍သုံးမည်",(d,w)->{
                setMain(activity,VALUES[choice[0]]);
                Toast.makeText(activity,"Main AI: "+mainLabel(activity),Toast.LENGTH_SHORT).show();
                run(activity,VALUES[choice[0]],content,session,title);
            })
            .setNegativeButton("မလုပ်တော့",null).create();
        dialog.show();
    }
    public static void selectMain(Activity activity){
        final int[] choice={position(getMain(activity))};
        new AlertDialog.Builder(activity)
            .setTitle("Main AI သတ်မှတ်ရန်")
            .setSingleChoiceItems(LABELS,choice[0],(dialog,index)->choice[0]=index)
            .setMessage("ရွေးထားသော AI ကို နောက်ထပ် Prompt များတွင် ပုံမှန်ရွေးပြီးသားအဖြစ် ပြပါမည်။ Prompt တစ်ခုချင်းစီမှာလည်း ပြန်ပြောင်းနိုင်ပါသည်။")
            .setPositiveButton("သိမ်းဆည်းမည်",(d,w)->{
                setMain(activity,VALUES[choice[0]]);
                Toast.makeText(activity,"Main AI: "+mainLabel(activity)+" · သိမ်းပြီးပါပြီ",Toast.LENGTH_LONG).show();
            }).setNegativeButton("ပယ်ဖျက်",null).show();
    }
    /** Launch currently configured MAIN AI for plain chats and footer navigation. */
    public static void openMain(Activity activity){run(activity,getMain(activity),"",null,null);}
    /** Run current MAIN without prompting; caller explicitly asked for default provider. */
    public static void runMain(Activity activity,String prompt,String session,String title){
        run(activity,getMain(activity),prompt,session,title);
    }
    private static void run(Activity activity,String provider,String prompt,String session,String title){
        if(APP.equals(provider)){
            Intent intent=new Intent(activity,NativeChatActivity.class);
            if(prompt!=null&&!prompt.isEmpty())intent.putExtra("draft",prompt);
            if(session!=null&&!session.trim().isEmpty())intent.putExtra("chat_session",session);
            if(title!=null&&!title.trim().isEmpty())intent.putExtra("chat_title",title);
            intent.putExtra("auto_send",prompt!=null&&!prompt.isEmpty());
            activity.startActivity(intent);
        }else{
            boolean chatgpt=CHATGPT.equals(provider);
            openInstalled(activity,chatgpt?CHATGPT_PACKAGE:GEMINI_PACKAGE,
                chatgpt?"ChatGPT":"Gemini",prompt);
        }
    }
    private static void copyPrompt(Activity activity,String prompt){
        if(prompt==null||prompt.trim().isEmpty())return;
        ClipboardManager clipboard=(ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if(clipboard!=null)clipboard.setPrimaryClip(ClipData.newPlainText("Student AI Prompt",prompt));
    }
    private static void openInstalled(Activity activity,String pkg,String label,String prompt){
        copyPrompt(activity,prompt);
        Intent launch=activity.getPackageManager().getLaunchIntentForPackage(pkg);
        if(launch!=null){
            try{
                launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(launch);
                if(prompt!=null&&!prompt.isEmpty())Toast.makeText(activity,label+" ဖွင့်ပြီးပါပြီ။ Prompt ကို Paste လုပ်ပြီး Send နှိပ်ပါ။",Toast.LENGTH_LONG).show();
                return;
            }catch(Exception ignored){}
        }
        // Do not silently send users to the browser when they explicitly chose a phone app.
        AlertDialog.Builder dialog=new AlertDialog.Builder(activity)
            .setTitle(label+" App မတွေ့ပါ")
            .setMessage("ဤဖုန်းတွင် "+label+" App မရှိသေး၊ မဖွင့်နိုင်၊ သို့မဟုတ် မရရှိနိုင်ပါ။"
                +(prompt!=null&&!prompt.isEmpty()?" Prompt ကို Clipboard ထဲ ကူးထားပါသည်။":"")
                +" Google Play မှာ ရှာဖွေနိုင်ပါသည်။")
            .setPositiveButton("Play Store ဖွင့်ရန်",(d,w)->{
                String market="market://details?id="+pkg;
                try{activity.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(market)));}
                catch(Exception e){
                    try{activity.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+pkg)));}
                    catch(Exception ignored){}
                }
            })
            .setNegativeButton("ပိတ်မည်",null);
        dialog.show();
    }
}
