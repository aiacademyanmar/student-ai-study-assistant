package asia.aaii.studentstudy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Original, bundled Myanmar-language offline reading guide inspired by public Work Users topics.
 * Does NOT cache, copy, or redistribute the underlying Academy course or member videos.
 */
public class OfflineReaderActivity extends Activity {
    private static final int NAVY=0xFF122958, BLUE=0xFF145EE5, INK=0xFF1C3052;
    private static final int WHITE=Color.WHITE, MUTED=0xFF657694, BG=0xFFF3F7FF, LINE=0xFFE2EAF7, GREEN=0xFF13886B;
    private static final String REFERENCE="https://academy.openai.com/home/clubs/work-users-ynjqu";
    private static final String[] TITLES={
        "အလုပ်ခွင်အတွက် AI မိတ်ဆက်","ChatGPT ကို ဘယ်အချိန်သုံးမလဲ","မေးခွန်းကောင်း ရေးသားနည်း",
        "Chat နှင့် Work အကြား ကွာခြားချက်","အစီရင်ခံစာ ရေးသားခြင်း","အစည်းအဝေး မှတ်တမ်းနှင့် အလုပ်တာဝန်",
        "AI ဖြင့် သုတေသနစတင်ခြင်း","Data နှင့် ဇယားများကို နားလည်ခြင်း","Marketing စီမံကိန်းတွင် AI သုံးခြင်း",
        "Sales နှင့် Customer Service","လုပ်ငန်းဆောင်ရွက်မှု တိုးတက်စေခြင်း","AI Workflow တည်ဆောက်ခြင်း",
        "လုံခြုံရေးနှင့် ကိုယ်ရေးအချက်အလက်","AI အဖြေကို စစ်ဆေးအတည်ပြုခြင်း","၇ ရက်တာ လက်တွေ့လေ့ကျင့်ရေး"
    };
    private static final String[] GROUPS={
        "အခြေခံ","အခြေခံ","အခြေခံ","အလုပ်လုပ်နည်း","ရေးသားမှု","အလုပ်လုပ်နည်း",
        "သုတေသန","ဒေတာ","Marketing","Sales","Operations","Workflow","လုံခြုံရေး","အရည်အသွေး","လေ့ကျင့်ရေး"
    };
    private static final String[] READING={
        "AI ဆိုတာ စာရေး၊ အချက်အလက်စီစဉ်၊ အကြံဉာဏ်ရှာဖွေရာမှာ ကူညီပေးနိုင်တဲ့ နည်းပညာပါ။ အလုပ်တစ်ခုလုံးကို မျက်စိမှိတ်အပ်နှံခြင်းထက် မိမိလုပ်ချင်တဲ့ ရလဒ်ကို ရှင်းရှင်းလင်းလင်းပြောပြီး ရလာဒ်ကို ပြန်စစ်တာက ပိုမိုကောင်းမွန်ပါတယ်။\n\nအလုပ်တစ်ခုကို AI နဲ့ စမ်းမယ်ဆိုရင် လုပ်ငန်းရည်ရွယ်ချက်၊ လက်ခံရရှိချင်တဲ့ပုံစံ၊ ကန့်သတ်ချက် သုံးမျိုးကို အရင်စာရင်းရေးပါ။ ဥပမာ အစည်းအဝေး မှတ်တမ်းကို တာဝန်များ၊ တာဝန်ခံများ၊ သတ်မှတ်ရက်များအဖြစ် အကျဉ်းချုပ်စေခြင်း ဖြစ်ပါတယ်။",
        "ChatGPT ကို စာပိုဒ်ပြင်ဆင်ခြင်း၊ အကြံစဉ်းစားခြင်း၊ အစီအစဉ်မူကြမ်းရေးခြင်းလို ချက်ချင်းပြန်တုံ့ပြန်နိုင်တဲ့အလုပ်တွေမှာ စတင်သုံးပါ။ အချက်အလက်အများကြီး စုဆောင်းပြီး ဖိုင်အများအပြားနဲ့ ဆက်စပ်လုပ်ရတဲ့အလုပ်တွေမှာ ပိုမိုစနစ်ကျတဲ့ လုပ်ငန်းစဉ် လိုအပ်နိုင်ပါတယ်။\n\nAI ကူညီတာနဲ့ လူက ဆုံးဖြတ်တာကို ခွဲခြားပါ။ ဥပမာ Customer ကို ပို့မယ့် Email မူကြမ်းကို AI ရေးနိုင်ပေမယ့် စျေးနှုန်း၊ အချက်အလက်နှင့် ကတိကဝတ်တွေကို တာဝန်ရှိသူက အတည်ပြုရပါမယ်။",
        "Prompt ကောင်းတစ်ခုမှာ (၁) AI ရဲ့တာဝန် (၂) လုပ်ဆောင်ရမယ့် အလုပ် (၃) လိုအပ်သော နောက်ခံအချက်အလက် (၄) Output ပုံစံ (၅) အရည်အသွေးကန့်သတ်ချက် ပါသင့်ပါတယ်။ မရှင်းလင်းတာရှိရင် အရင်မေးရန် ညွှန်ကြားနိုင်ပါတယ်။\n\nဥပမာ - မြန်မာဘာသာနဲ့ အစီရင်ခံစာအကျဉ်းတစ်ခုရေးပါ။ ဖတ်ရှုသူက အုပ်ချုပ်ရေးအဖွဲ့ဖြစ်ပြီး ခေါင်းစဉ် ၄ ခု၊ အဓိကရလဒ်၊ နောက်ဆက်တွဲလုပ်ရမည့်အလုပ်နှင့် အချိန်ဇယား ပါစေ။ မရှိတဲ့ကိန်းဂဏန်းကို မခန့်မှန်းပါနှင့်။",
        "Chat ဟာ မေးခွန်းဖြေခြင်း၊ စာပြင်ခြင်းနဲ့ အတွေးစတင်ခြင်းမျိုးမှာ သင့်တော်ပါတယ်။ Work လို လုပ်ငန်းစဉ်များမှာ ပေးထားတဲ့ဖိုင်တွေ၊ လိုအပ်ချက်တွေကို စုစည်းပြီး အဆင့်များစွာလုပ်ဆောင်နိုင်ပါတယ်။ လုပ်နိုင်စွမ်းဟာ အကောင့်နှင့် အဖွဲ့အစည်း သတ်မှတ်ချက်အလိုက် ကွာနိုင်ပါတယ်။\n\nမည်သည့်နည်းလမ်းမဆို မိမိက မူရင်းဖိုင်နဲ့ ရလာဒ်ကို ပြန်ကြည့်ပြီး သေချာမှ အသုံးပြုပါ။ လျှို့ဝှက်လုပ်ငန်းအချက်အလက် ထည့်သွင်းမီ အဖွဲ့အစည်းခွင့်ပြုချက်နှင့် သက်ဆိုင်ရာလုံခြုံရေးမူဝါဒကို စစ်ပါ။",
        "အစီရင်ခံစာကောင်းတစ်ခုမှာ ခေါင်းစဉ်၊ ရည်ရွယ်ချက်၊ တွေ့ရှိချက်၊ ထောက်ခံအချက်အလက်၊ အကြံပြုချက်နဲ့ နောက်တစ်ဆင့် ပါသင့်ပါတယ်။ AI ကို စာရေးခိုင်းရင် ပေးထားတဲ့ အချက်အလက်ကနေသာ ရေးဖို့ ပြောပါ။\n\nစာရွက်စာတမ်းအဆုံးမှာ အတည်ပြုရန် လိုတဲ့ကိန်းဂဏန်းတွေကို သီးခြားဖော်ပြခိုင်းပါ။ မိမိရဲ့ အဖွဲ့အစည်းရေးဟန်အတိုင်း စာပိုဒ်အတို၊ ခေါင်းစဉ်ပြတ်သားတဲ့ပုံစံ ရွေးနိုင်ပါတယ်။",
        "အစည်းအဝေးမှတ်တမ်းမှာ ဆွေးနွေးချက်တိုင်းကို စကားလုံးလိုက် မလိုပါ။ ဆုံးဖြတ်ချက်၊ လုပ်ရမည့်အလုပ်၊ တာဝန်ခံ၊ နောက်ဆုံးရက်ကို ခွဲထုတ်ဖော်ပြခြင်းက ပိုအသုံးဝင်ပါတယ်။\n\nAI ကို မှတ်တမ်းပေးမီ ပုဂ္ဂိုလ်ရေးအချက်အလက်နဲ့ လျှို့ဝှက်ဆွေးနွေးချက်တွေကို ဖယ်ရှားပါ။ ပြီးတော့ တာဝန်ခံတွေနဲ့ Deadline တွေဟာ မူရင်းမှတ်တမ်းနဲ့ ကိုက်ညီမှုရှိမရှိ စစ်ဆေးပါ။",
        "သုတေသနစတင်ရာမှာ သုတေသနမေးခွန်းကို အတိအကျသတ်မှတ်ပါ။ ပြီးနောက် ရှာဖွေမယ့် ခေါင်းစဉ်၊ အဓိကစကားလုံး၊ အထောက်အထားလိုအပ်ချက်တွေကို ခွဲပါ။ AI ဖြင့် အကြမ်းဖျဉ်း Outline ထုတ်နိုင်ပါတယ်။\n\nAI ပေးတဲ့ ရင်းမြစ်စာရင်းကို မျက်စိမှိတ်မယုံပါနှင့်။ စာတမ်းအမည်၊ ရေးသားသူ၊ ခုနှစ်နဲ့ URL ကို တကယ်ရှိမရှိ စစ်ပါ။ သုတေသနအချက်အလက်က နောက်ဆုံးပြောင်းလဲနိုင်တဲ့အတွက် နေ့စွဲကိုလည်း စစ်ဆေးပါ။",
        "ဇယားကို AI ဖြင့် လေ့လာရင် Column အဓိပ္ပာယ်၊ တိုင်းတာယူနစ်၊ မပါရှိသောတန်ဖိုးတွေကို အရင်ရှင်းပြပါ။ အရင်ဆုံး Summary၊ ထို့နောက် Trend နှင့် ထူးခြားချက်တွေကို လေ့လာပါ။\n\nအမြင့်ဆုံး၊ အနိမ့်ဆုံး၊ ပျမ်းမျှလိုတွက်ချက်မှုတွေကို ကိုယ်တိုင် ပြန်စစ်ပါ။ Correlation တွေ့တာနဲ့ အကြောင်းရင်းကို အတည်ပြုလို့မရပါ။ ဂရပ်တစ်ခုစီမှာ Data ရင်းမြစ်နဲ့ ကာလကို ဖော်ပြပါ။",
        "Marketing အတွက် AI ကို Customer အမျိုးအစားခွဲခြင်း၊ ကြော်ငြာစာသားမူကြမ်း၊ Campaign Calendar နဲ့ A/B Test အကြံများ ရေးရာမှာ ကူညီအောင်သုံးနိုင်ပါတယ်။ Brand Tone၊ ရည်ရွယ်ချက်နှင့် ဘတ်ဂျက်ကို သတ်မှတ်ပါ။\n\nစျေးနှုန်း၊ Discount၊ ထိရောက်မှု ရလဒ်တွေကို မူရင်းဒေတာမရှိဘဲ AI မခန့်မှန်းစေပါနှင့်။ ဖောက်သည်များကို လွဲမှားစေမယ့် ကတိကဝတ် မထည့်ပါနှင့်။",
        "Sales နှင့် Customer Service မှာ အီးမေးလ်မူကြမ်း၊ FAQ၊ Customer မေးခွန်းအမျိုးအစားခွဲခြင်းနဲ့ Role-play လေ့ကျင့်ခြင်းကို AI ကူညီနိုင်ပါတယ်။ စကားပြောပုံကို ယဉ်ကျေး၊ ရှင်းလင်း၊ အပြန်အလှန် နားလည်လွယ်စေပါ။\n\nဖောက်သည်အမည်၊ ဖုန်း၊ ငွေပေးချေမှု အချက်အလက်တွေကို ခွင့်ပြုချက်မရှိဘဲ မတင်ပါနှင့်။ ပြန်လည်ပေးပို့မည့် စျေးနှုန်း၊ သဘောတူညီချက်နှင့် ဝန်ဆောင်မှုစည်းကမ်းတွေကို အတည်ပြုပါ။",
        "Operations ပိုကောင်းစေဖို့ နေ့စဉ်လုပ်နေကျလုပ်ငန်းတစ်ခုကို အဆင့်လိုက်ရေးပါ။ ဘယ်အဆင့်မှာ အချိန်ကြာ၊ ဘယ်အဆင့်မှာ ထပ်လုပ်၊ ဘယ်အဆင့်မှာ လူက အတည်ပြုရမလဲဆိုတာ ခွဲထုတ်ပါ။\n\nAI ကို Checklist၊ SOP မူကြမ်းနဲ့ လုပ်ငန်းသတိပေးချက် အကြံများ ရေးခိုင်းပါ။ အလိုအလျောက်လုပ်ဆောင်မှု မတင်မီ တိုင်းတာနိုင်တဲ့ ရလဒ်နဲ့ အန္တရာယ်ရှိတဲ့ အဆင့်တွေကို စမ်းသပ်စစ်ဆေးပါ။",
        "AI Workflow တစ်ခုကို Input → လုပ်ဆောင်ချက် → Review → Output အဖြစ် ရှင်းလင်းစွာ သတ်မှတ်ပါ။ ထပ်ခါတလဲလဲ အသုံးပြုမယ်ဆိုရင် Template နဲ့ စစ်ဆေးရန်စည်းမျဉ်းထားပါ။\n\nဥပမာ မေးလ်မှ အချက်အလက်ထုတ် → အကြောင်းအရာခွဲ → အကျဉ်းချုပ်ရေး → တာဝန်ခံက စစ် → မူကြမ်းပို့ ဆိုတဲ့ လမ်းကြောင်းဖြစ်ပါတယ်။ အပြင်ဘက်ကို ပေးပို့ခြင်းလို အရေးကြီးတဲ့ လုပ်ဆောင်ချက်တွေကို လူက ခွင့်ပြုရပါမယ်။",
        "လျှို့ဝှက်စာရွက်စာတမ်း၊ Password၊ Access Token၊ ကိုယ်ရေးအချက်အလက်တွေကို အတည်မပြုရသေးတဲ့ AI Tool သို့ မတင်ပါနှင့်။ မိမိအဖွဲ့အစည်းရဲ့ အချက်အလက်သုံးစွဲရေးမူဝါဒကို ဦးစားပေးပါ။\n\nလေ့ကျင့်ခန်းအတွက် နမူနာဒေတာ၊ အမည်မပါသော Dataset ကို သုံးနိုင်ပါတယ်။ AI ထုတ်တဲ့အဖြေကို လူကိုထိခိုက်စေမယ့် ဆုံးဖြတ်ချက်တွေမှာ မျက်စိမှိတ် မသုံးပါနှင့်။",
        "AI က စာသားပုံစံကောင်းကောင်း ရေးနိုင်ပေမယ့် ကိန်းဂဏန်းမှားနိုင်၊ မရှိတဲ့ရင်းမြစ်ကို ညွှန်းနိုင်ပါတယ်။ အရေးကြီးဆုံးအချက် ၃ ခုကို မူရင်းဖိုင် သို့မဟုတ် ယုံကြည်ရတဲ့ရင်းမြစ်နဲ့ ထပ်စစ်ပါ။\n\nအရည်အသွေးစစ်ဆေးချက်တွင် မှန်ကန်မှု၊ ပြည့်စုံမှု၊ ဘက်လိုက်မှု၊ လုံခြုံမှု၊ လုပ်ဆောင်နိုင်မှု တို့ပါဝင်သင့်ပါတယ်။ AI အဖြေရဲ့ အားနည်းချက်တွေကိုလည်း ရှင်းပြခိုင်းပြီး လူက နောက်ဆုံး ဆုံးဖြတ်ပါ။",
        "နေ့ ၁ - လုပ်ငန်းတစ်ခု ရွေးပါ။ နေ့ ၂ - Prompt ရေးပါ။ နေ့ ၃ - Draft ထုတ်ပါ။ နေ့ ၄ - အချက်အလက်ပြန်စစ်ပါ။ နေ့ ၅ - Checklist ပြုလုပ်ပါ။ နေ့ ၆ - အခြားလူတစ်ယောက်နဲ့ ပြန်လည်စမ်းသပ်ပါ။ နေ့ ၇ - ရလဒ်နှင့် အချိန်ကုန်သက်သာမှုကို မှတ်တမ်းတင်ပါ။\n\nနေ့တိုင်း မိနစ် ၁၅ ခန့်နဲ့ စတင်နိုင်ပါတယ်။ လိုအပ်ရင် မိမိကိုယ်ပိုင် လေ့လာရေးမှတ်စုရေးပြီး နောက်တစ်ပတ်မှာ အခက်အခဲတွေကို ပြန်ပြင်ပါ။"
    };
    private static final String[] PRACTICE={
        "AI ကိုသုံးမည့် ကိုယ်ပိုင်အလုပ် ၃ ခုစာရင်းရေးပါ။","Chat နဲ့ သင့်တော်မည့် အလုပ်တစ်ခု၊ လူက အတည်ပြုရမည့်အချက်တစ်ခု ရေးပါ။",
        "သင်လုပ်ချင်သောအလုပ်အတွက် Role/Task/Context/Format ပါတဲ့ Prompt တစ်ခုရေးပါ။","တစ်ဆင့်ပြီးတစ်ဆင့် လုပ်ရမည့်အလုပ်တစ်ခုကို Checklist ရေးပါ။",
        "စာပိုဒ်တိုတစ်ခုကို Executive Summary အဖြစ် မူကြမ်းရေးပါ။","နမူနာအစည်းအဝေးတစ်ခုအတွက် Action Items ၃ ခုရေးပါ။",
        "သုတေသနမေးခွန်းတစ်ခုနှင့် စစ်ဆေးနိုင်သောရင်းမြစ် ၂ ခုကို ရှာရန် စာရင်းလုပ်ပါ။","ကိန်းဂဏန်း ၅ ခုအတွက် ပျမ်းမျှတွက်ပြီး ကိုယ်တိုင်ပြန်စစ်ပါ။",
        "ကုန်ပစ္စည်းတစ်ခုအတွက် ကြော်ငြာမူကြမ်း ၂ မျိုး ရေးပါ။","Customer ပြောဆိုမှုအခြေအနေ ၂ ခုအတွက် Reply Draft ရေးပါ။",
        "လုပ်ငန်းအဆင့် ၅ ဆင့်ရေးပြီး စစ်ဆေးရန်အချက် ၂ ချက်ထည့်ပါ။","Input/Review/Output ပါတဲ့ Workflow Diagram ကို စာဖြင့်ရေးပါ။",
        "AI ထဲ မထည့်သင့်သော အချက်အလက် ၅ မျိုးကို စာရင်းလုပ်ပါ။","AI ရေးထားတဲ့ စာပိုဒ်တစ်ခုမှာ ပြန်စစ်ရမည့်အချက် ၃ ခု အမှတ်အသားပြပါ။",
        "၇ ရက်စာ လေ့ကျင့်မှုမှတ်တမ်းဇယား တစ်ခုဆွဲပါ။"
    };

    private SharedPreferences prefs;
    private LinearLayout root, body, cards;
    private ScrollView scroll;
    private int current=-1, font=17;
    private boolean showSaved=false;
    private EditText search;

    private int dp(float x){return (int)(x*getResources().getDisplayMetrics().density+.5f);}
    private GradientDrawable bg(int color,int radius){GradientDrawable x=new GradientDrawable();x.setColor(color);x.setCornerRadius(dp(radius));return x;}
    private GradientDrawable border(int color,int radius){GradientDrawable x=bg(color,radius);x.setStroke(dp(1),LINE);return x;}
    private LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);return l;}
    private LinearLayout row(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.HORIZONTAL);l.setGravity(Gravity.CENTER_VERTICAL);return l;}
    private void padding(View v,int l,int t,int r,int b){v.setPadding(dp(l),dp(t),dp(r),dp(b));}
    private TextView text(String s,int size,int color,boolean bold){TextView v=new TextView(this);v.setText(s);v.setTextSize(size);v.setTextColor(color);if(bold)v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);v.setLineSpacing(dp(3),1.06f);return v;}
    private void insert(View item,int above,int below){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,dp(above),0,dp(below));body.addView(item,p);}
    private TextView button(String s,int foreground,int background,Runnable go){
        TextView b=text(s,14,foreground,true);b.setGravity(Gravity.CENTER);padding(b,14,13,14,13);
        b.setBackground(bg(background,15));b.setClickable(true);b.setFocusable(true);b.setOnClickListener(v->go.run());return b;
    }
    private int count(String key){int c=0;for(int i=0;i<TITLES.length;i++)if(prefs.getBoolean(key+i,false))c++;return c;}
    @Override public void onCreate(Bundle b){
        super.onCreate(b);prefs=getSharedPreferences("student_offline_work_v24",MODE_PRIVATE);
        font=prefs.getInt("font",17);
        getWindow().setStatusBarColor(WHITE);getWindow().setNavigationBarColor(WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        int id=getIntent().getIntExtra("chapter_id",-1);
        if(id>=0&&id<TITLES.length)current=id;
        show();
    }

    private void show(){
        root=column();root.setBackgroundColor(BG);setContentView(root);
        if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,insets)->{
            android.graphics.Insets bars=insets.getInsets(WindowInsets.Type.systemBars());
            v.setPadding(bars.left,bars.top,bars.right,bars.bottom);return insets;
        });
        LinearLayout head=row();head.setBackgroundColor(WHITE);padding(head,12,13,12,13);
        TextView back=button("‹",BLUE,0xFFF0F5FE,()->{if(current>=0){current=-1;show();}else go("home");});
        head.addView(back,new LinearLayout.LayoutParams(dp(43),dp(46)));
        LinearLayout names=column();padding(names,11,0,2,0);
        names.addView(text("📖 မြန်မာ Offline စာဖတ်ခန်း",19,NAVY,true));
        names.addView(text("AI at Work · ကိုယ်ပိုင်မြန်မာဘာသာ လေ့လာရေး",10,MUTED,false));
        head.addView(names,new LinearLayout.LayoutParams(0,-2,1));
        root.addView(head);
        scroll=new ScrollView(this);scroll.setFillViewport(false);scroll.setVerticalScrollBarEnabled(false);
        body=column();padding(body,16,17,16,24);
        scroll.addView(body);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        if(current>=0)readPage();else index();
        footer();
    }
    private void index(){
        LinearLayout intro=column();padding(intro,16,17,16,17);intro.setBackground(bg(0xFFE7F0FF,20));
        intro.addView(text("အင်တာနက်မလိုဘဲ သင်ယူပါ",21,NAVY,true));
        intro.addView(text("OpenAI Academy Work Users ကို အကြောင်းအရာရည်ညွှန်းပြီး ကိုယ်ပိုင်ရေးသားထားသော မြန်မာဘာသာ အကျဉ်းချုပ်နှင့် လေ့ကျင့်ခန်း "+TITLES.length+" ခု",13,MUTED,false));
        TextView stat=text("ဖတ်ပြီး "+count("read_")+" / "+TITLES.length+"     ·     သိမ်းထား "+count("save_")+" ခု",12,GREEN,true);
        padding(stat,0,13,0,1);intro.addView(stat);insert(intro,0,15);
        search=new EditText(this);search.setSingleLine(true);search.setTextSize(14);search.setHint("အကြောင်းအရာ ရှာရန်...");
        search.setTextColor(INK);search.setHintTextColor(MUTED);search.setBackground(border(WHITE,14));padding(search,14,9,14,9);
        insert(search,0,12);
        LinearLayout filters=row();
        TextView all=button("အားလုံး",showSaved?INK:WHITE,showSaved?0xFFE5EEFC:BLUE,()->{showSaved=false;show();});
        TextView bookmarks=button("★ သိမ်းထားသည်",showSaved?WHITE:INK,showSaved?BLUE:0xFFE5EEFC,()->{showSaved=true;show();});
        LinearLayout.LayoutParams left=new LinearLayout.LayoutParams(0,-2,1);
        left.rightMargin=dp(7);filters.addView(all,left);filters.addView(bookmarks,new LinearLayout.LayoutParams(0,-2,1));insert(filters,0,14);
        cards=column();body.addView(cards);list("");
        search.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s,int start,int count,int after){}
            public void onTextChanged(CharSequence s,int start,int before,int count){list(s.toString());}
            public void afterTextChanged(Editable e){}
        });
        TextView note=text("မှတ်ချက် — ဤအရာများသည် OpenAI Academy ၏ မူရင်းဗီဒီယို၊ အဖွဲ့ဝင်စာမျက်နှာ သို့မဟုတ် သင်တန်းစာသားအပြည့်အစုံကို Offline ကူးထားခြင်း မဟုတ်ပါ။",12,MUTED,false);
        insert(note,16,4);
    }
    private void list(String term){
        cards.removeAllViews();String query=term.trim().toLowerCase(java.util.Locale.ROOT);
        int shown=0;
        for(int i=0;i<TITLES.length;i++){
            final int id=i;
            if(showSaved&&!prefs.getBoolean("save_"+i,false))continue;
            if(!query.isEmpty()&&!TITLES[i].toLowerCase(java.util.Locale.ROOT).contains(query)&&!GROUPS[i].toLowerCase(java.util.Locale.ROOT).contains(query))continue;
            LinearLayout item=row();padding(item,14,14,12,14);item.setBackground(border(WHITE,17));
            LinearLayout copy=column();
            copy.addView(text((i+1)+".  "+TITLES[i],16,INK,true));
            TextView sub=text(GROUPS[i]+(prefs.getBoolean("read_"+i,false)?"  ·  ✓ ဖတ်ပြီး":""),12,prefs.getBoolean("read_"+i,false)?GREEN:MUTED,false);
            padding(sub,0,5,0,0);copy.addView(sub);
            item.addView(copy,new LinearLayout.LayoutParams(0,-2,1));
            TextView mark=text(prefs.getBoolean("save_"+i,false)?"★":"›",22,BLUE,true);item.addView(mark);
            item.setOnClickListener(v->{current=id;show();});
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.bottomMargin=dp(10);
            cards.addView(item,lp);shown++;
        }
        if(shown==0)cards.addView(text("ကိုက်ညီသော စာဖတ်ခန်း မတွေ့သေးပါ။",14,MUTED,false));
    }
    private void readPage(){
        int id=current;
        TextView section=text("သင်ခန်းစာ "+(id+1)+" / "+TITLES.length+" · "+GROUPS[id],12,BLUE,true);insert(section,0,9);
        insert(text(TITLES[id],25,NAVY,true),0,16);
        LinearLayout panel=column();padding(panel,17,17,17,18);panel.setBackground(border(WHITE,20));
        TextView bodyText=text(READING[id],font,INK,false);panel.addView(bodyText);
        insert(panel,0,13);
        LinearLayout practice=column();practice.setBackground(bg(0xFFE7F1FE,16));padding(practice,16,15,16,15);
        practice.addView(text("✍  ကိုယ်တိုင်လေ့ကျင့်ပါ",16,NAVY,true));
        TextView challenge=text(PRACTICE[id],14,INK,false);padding(challenge,0,8,0,0);practice.addView(challenge);
        insert(practice,0,15);
        LinearLayout fontControls=row();
        TextView small=button("A−",BLUE,0xFFEAF0FC,()->{font=Math.max(14,font-1);prefs.edit().putInt("font",font).apply();show();});
        TextView big=button("A+",BLUE,0xFFEAF0FC,()->{font=Math.min(24,font+1);prefs.edit().putInt("font",font).apply();show();});
        fontControls.addView(small,new LinearLayout.LayoutParams(0,-2,1));
        LinearLayout.LayoutParams next=new LinearLayout.LayoutParams(0,-2,1);next.leftMargin=dp(8);fontControls.addView(big,next);
        insert(fontControls,0,13);
        String star=prefs.getBoolean("save_"+id,false)?"★ သိမ်းထားပြီး":"☆ နောက်မှဖတ်ရန် သိမ်းမည်";
        insert(button(star,BLUE,0xFFE6EFFD,()->{prefs.edit().putBoolean("save_"+id,!prefs.getBoolean("save_"+id,false)).apply();show();}),0,10);
        insert(button(prefs.getBoolean("read_"+id,false)?"✓ ဖတ်ပြီးပါပြီ":"✓ ဖတ်ပြီးဟု မှတ်မည်",WHITE,GREEN,()->{
            prefs.edit().putBoolean("read_"+id,true).apply();Toast.makeText(this,"ဖတ်ပြီးအဖြစ် မှတ်သားပြီးပါပြီ",Toast.LENGTH_SHORT).show();show();
        }),0,14);
        LinearLayout nav=row();
        TextView previous=button("‹ ရှေ့သင်ခန်းစာ",INK,0xFFE5EEFD,()->{current=Math.max(0,id-1);show();});
        previous.setEnabled(id>0);previous.setAlpha(id>0?1f:.4f);
        TextView after=button("နောက်သင်ခန်းစာ ›",WHITE,BLUE,()->{current=Math.min(TITLES.length-1,id+1);show();});
        after.setEnabled(id<TITLES.length-1);after.setAlpha(id<TITLES.length-1?1f:.4f);
        nav.addView(previous,new LinearLayout.LayoutParams(0,-2,1));
        LinearLayout.LayoutParams np=new LinearLayout.LayoutParams(0,-2,1);np.leftMargin=dp(8);nav.addView(after,np);insert(nav,0,16);
        insert(text("ရည်ညွှန်းချက် — OpenAI Academy Work Users: "+REFERENCE,11,MUTED,false),0,6);
    }
    private void footer(){
        LinearLayout bar=row();bar.setBackgroundColor(WHITE);padding(bar,3,7,3,8);
        String[] names={"ပင်မ","စကားဝိုင်း","AI ဆရာ","သင်တန်း","ပိုမို"};
        String[] icons={"⌂","▤","✦","▣","☰"};
        String[] route={"home","list","tutor","courses","profile"};
        for(int i=0;i<5;i++){
            final String dest=route[i];
            LinearLayout tab=column();tab.setGravity(Gravity.CENTER);
            TextView icon=text(icons[i],23, i==3?BLUE:MUTED,true);icon.setGravity(Gravity.CENTER);tab.addView(icon);
            TextView lab=text(names[i],10,i==3?BLUE:MUTED,i==3);lab.setGravity(Gravity.CENTER);tab.addView(lab);
            tab.setContentDescription(names[i]);tab.setOnClickListener(v->go(dest));
            bar.addView(tab,new LinearLayout.LayoutParams(0,dp(57),1));
        }
        root.addView(bar,new LinearLayout.LayoutParams(-1,-2));
    }
    private void go(String route){
        Intent i;
        if("tutor".equals(route))i=new Intent(this,NativeChatActivity.class);
        else if("courses".equals(route)){AcademyBrowser.openCourses(this);return;}
        else {i=new Intent(this,CatalogActivity.class);i.putExtra("open_page",route);}
        startActivity(i);finish();
    }
    @Override public void onBackPressed(){if(current>=0){current=-1;show();}else go("home");}
}
