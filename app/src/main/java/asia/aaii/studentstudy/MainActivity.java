package asia.aaii.studentstudy;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

/** Offline-first Android study companion. Opens official ChatGPT Study Mode externally. */
public class MainActivity extends Activity {
    private static final int NAVY = Color.rgb(12, 20, 44);
    private static final int SKY = Color.rgb(32, 90, 218);
    private static final int PURPLE = Color.rgb(111, 72, 225);
    private static final int INK = Color.rgb(27, 35, 65);
    private static final int MUTED = Color.rgb(104, 115, 140);
    private static final int BG = Color.rgb(246, 248, 254);
    private static final int WHITE = Color.WHITE;
    private static final int GREEN = Color.rgb(11, 144, 115);
    private static final String STUDY_URL = "https://chatgpt.com/studymode";
    private final String[] tabs = {"ပင်မ", "သင်ခန်းစာ", "လေ့ကျင့်", "အစီအစဉ်"};
    private final String[] tabIcons = {"⌂", "▤", "✦", "☷"};
    private LinearLayout app, surface, bottom;
    private String page = "home";
    private SharedPreferences prefs;
    private int quizIndex = 0, quizScore = 0;
    private boolean questionAnswered = false;
    private int cardIndex = 0;
    private boolean cardShowingAnswer = false;
    private Lesson selectedLesson;
    private EditText questionInput;
    private Spinner gradeSelect, subjectSelect;

    private static final String[] TOPICS = {"မြန်မာစာ", "English", "Mathematics", "Science", "History", "ICT"};
    private static final String[] TOPIC_ICONS = {"က", "Aa", "π", "⚛", "⌛", "⌘"};
    private static final int[] TOPIC_COLORS = {0xFF7252D9,0xFF167DBB,0xFFDE9145,0xFF11A184,0xFFDC6B82,0xFF4A65D8};

    private static class Lesson {
        final String subject, title, body, example, prompt;
        Lesson(String subject, String title, String body, String example, String prompt) {
            this.subject=subject; this.title=title; this.body=body; this.example=example; this.prompt=prompt;
        }
    }
    private static final Lesson[] LESSONS = {
        new Lesson("မြန်မာစာ", "နာမ်နှင့် ကြိယာ", "နာမ်သည် လူ၊ တိရစ္ဆာန်၊ အရာဝတ္ထု၊ နေရာ၊ အမည်တို့ကို ဖော်ပြသည်။ ကြိယာသည် လုပ်ဆောင်မှု သို့မဟုတ် ဖြစ်ပျက်မှုကို ဖော်ပြသည်။", "ဥပမာ — မောင်မောင် စာဖတ်သည်။ ‘မောင်မောင်’ သည် နာမ်၊ ‘ဖတ်သည်’ သည် ကြိယာဖြစ်သည်။", "နာမ်နှင့်ကြိယာ ခွဲခြားသိနိုင်ရန် ဥပမာ ၅ ခုနှင့် လေ့ကျင့်ခန်း ၅ ခု သင်ပေးပါ။"),
        new Lesson("မြန်မာစာ", "စာပိုဒ်အကျဉ်းချုပ်", "စာပိုဒ်တစ်ခုကို အကျဉ်းချုပ်ရာတွင် အဓိကအကြောင်းအရာ၊ ထောက်ခံချက်နှင့် အဆုံးသတ်ကို ရှာပါ။ မလိုအပ်သော အသေးစိတ်များကို ဖြုတ်ပါ။", "စာပိုဒ်တစ်ခု ဖတ်ပြီး ‘ဘယ်သူ၊ ဘာလုပ်၊ ဘာကြောင့်’ ဟု မေးခြင်းသည် အကူအညီဖြစ်သည်။", "မြန်မာစာပိုဒ်အကျဉ်းချုပ် ရေးနည်းကို အဆင့်လိုက် သင်ပေးပါ။"),
        new Lesson("English", "Simple Present Tense", "Use the simple present for habits, routines and general facts. Add -s or -es to verbs with he/she/it in affirmative sentences.", "I study every day. She studies every day. Do you study every day?", "Teach the simple present tense with Myanmar explanations and short exercises."),
        new Lesson("English", "English Vocabulary", "Learn new words by using them in meaningful sentences. Revise them regularly and test your recall without looking at answers.", "Discover → write a sentence → review tomorrow → quiz yourself.", "Teach me 10 useful English school vocabulary words with example sentences and Burmese meanings."),
        new Lesson("Mathematics", "Fractions / အပိုင်းကိန်း", "A fraction represents a part of a whole. In a/b, a is the numerator and b is the denominator. The denominator cannot be zero.", "1/2 + 1/4 = 2/4 + 1/4 = 3/4.", "Explain fractions step by step in Burmese, then ask me 5 practice questions."),
        new Lesson("Mathematics", "Linear Equations", "An equation states that two expressions are equal. Keep both sides balanced when adding, subtracting, multiplying or dividing.", "2x + 3 = 11 → 2x = 8 → x = 4.", "Guide me to solve beginner linear equations using hints, not immediate answers."),
        new Lesson("Science", "Photosynthesis", "Green plants use sunlight, water and carbon dioxide to make glucose and release oxygen. Chlorophyll in leaves absorbs light.", "Light + water + carbon dioxide → glucose + oxygen (simplified).", "Explain photosynthesis for a Myanmar middle-school student and test my understanding."),
        new Lesson("Science", "States of Matter", "Matter commonly occurs as solid, liquid and gas. Heating and cooling can change the state without changing the substance's identity.", "Ice → water → water vapour is melting followed by evaporation.", "Teach solids, liquids and gases with familiar home examples and a quiz."),
        new Lesson("History", "သမိုင်းအချိန်လိုင်း", "သမိုင်းဖြစ်ရပ်များကို အချိန်အစဉ်လိုက် စီစဉ်လေ့လာခြင်းဖြင့် အကြောင်းရင်း၊ ဖြစ်ရပ်နှင့် အကျိုးဆက်ကို ပိုမိုနားလည်နိုင်သည်။", "ဖြစ်ရပ်တစ်ခုအတွက် ခုနှစ်၊ နေရာ၊ ပါဝင်သူ၊ အကျိုးဆက်ကို မှတ်စုရေးပါ။", "သမိုင်း timeline ရေးနည်းကို ဥပမာတွေနဲ့ သင်ပေးပါ။"),
        new Lesson("History", "ပုဂံယဉ်ကျေးမှု", "ပုဂံဒေသသည် မြန်မာ့သမိုင်း၊ ဗိသုကာနှင့် ဗုဒ္ဓဘာသာ အနုပညာတို့တွင် အရေးပါသော ရှေးဟောင်းအမွေအနှစ်ဒေသဖြစ်သည်။", "သမိုင်းဝင် နေရာတစ်ခုကို လေ့လာရာတွင် ခေတ်ကာလ၊ အဆောက်အဦ၊ ယဉ်ကျေးမှုနှင့် ထိန်းသိမ်းရေးကို သီးခြားမှတ်သားပါ။", "ပုဂံသမိုင်းနှင့် ယဉ်ကျေးမှုကို ကျောင်းသားအဆင့် လွယ်လွယ်ရှင်းပြပေးပါ။"),
        new Lesson("ICT", "Digital Safety", "Keep passwords private, use strong unique passwords, and think critically before tapping unknown links or sharing personal information.", "A legitimate study app should not ask for your password inside its own form to open ChatGPT.", "Teach online safety and phishing prevention to students in Burmese."),
        new Lesson("ICT", "AI Literacy", "Generative AI predicts and creates responses based on patterns. It can make mistakes, so verify important claims with textbooks, teachers and reliable references.", "Use AI for explanations and hints, then attempt the answer independently.", "Explain what AI can and cannot do for school homework, with a short critical-thinking quiz.")
    };
    private static class Question {
        final String q; final String[] answers; final int correct; final String explanation;
        Question(String q, String[] answers, int correct, String explanation) {
            this.q=q;this.answers=answers;this.correct=correct;this.explanation=explanation;
        }
    }
    private static final Question[] QUIZ = {
        new Question("1/2 + 1/4 = ?", new String[]{"2/6", "3/4", "1/8", "2/4"},1,"1/2 = 2/4, ထို့ကြောင့် 2/4 + 1/4 = 3/4 ဖြစ်သည်။"),
        new Question("She ___ to school every day.", new String[]{"go", "going", "goes", "gone"},2,"Simple present tense မှာ she နှင့် goes ကိုသုံးရသည်။"),
        new Question("အပင်များ အစာချက်လုပ်ရန် အဓိကလိုအပ်သောအရာမှာ?", new String[]{"နေရောင်ခြည်", "ပလတ်စတစ်", "သံချေး", "သဲ"},0,"Photosynthesis ဖြစ်ရန် အလင်းရောင်လိုအပ်သည်။"),
        new Question("2x + 3 = 11 ဆိုလျှင် x = ?", new String[]{"3", "4", "5", "7"},1,"2x = 11 − 3 = 8, x = 4 ဖြစ်သည်။"),
        new Question("Which word is a noun?",new String[]{"quickly", "write", "teacher", "very"},2,"Teacher သည် လူတစ်ဦးကို ရည်ညွှန်းသည့် noun ဖြစ်သည်။"),
        new Question("Which is a safe online habit?",new String[]{"Sharing passwords", "Using unique passwords", "Clicking unknown links", "Sending OTPs"},1,"သီးခြားခိုင်မာသော password များကိုသုံးပြီး မမျှဝေပါနှင့်။"),
        new Question("Water turning into ice is called?",new String[]{"boiling", "melting", "freezing", "evaporation"},2,"Liquid မှ solid သို့ ပြောင်းခြင်းသည် freezing ဖြစ်သည်။"),
        new Question("စာကြောင်း ‘မမ ပြေးသည်’ တွင် ကြိယာက?",new String[]{"မမ", "ပြေးသည်", "စာကြောင်း", "မရှိပါ"},1,"‘ပြေးသည်’ သည် လုပ်ဆောင်မှုကို ဖော်ပြသော ကြိယာဖြစ်သည်။"),
        new Question("AI မှ ပေးသော အချက်အလက်ကို ဘယ်လိုသုံးသင့်သလဲ?",new String[]{"အမြဲတမ်းမှန်တယ်ဟုယူဆ", "မစစ်ဘဲကူးချ", "ယုံကြည်ရသောရင်းမြစ်နှင့်စစ်", "ဘယ်တော့မှမဖတ်"},2,"AI အဖြေတွင် အမှားရှိနိုင်၍ အခြားရင်းမြစ်များနှင့် စစ်ဆေးပါ။"),
        new Question("3 × 7 = ?",new String[]{"20", "21", "24", "27"},1,"3 × 7 = 21"),
        new Question("Which is the plural of 'child'?",new String[]{"childs", "childes", "children", "child's"},2,"Child ၏ irregular plural သည် children ဖြစ်သည်။"),
        new Question("Timeline တစ်ခုတွင် အရေးကြီးဆုံးစီစဉ်ပုံက?",new String[]{"အက္ခရာစဉ်", "အချိန်အစဉ်", "အရောင်အစဉ်", "အရွယ်အစားအစဉ်"},1,"ဖြစ်ရပ်များကို အချိန်အစဉ်အတိုင်း စီစဉ်ရသည်။")
    };
    private static final String[][] CARDS = {
        {"Fraction", "Whole တစ်ခု၏ အစိတ်အပိုင်းကို ဖော်ပြသော အပိုင်းကိန်း"},
        {"Noun", "လူ၊ နေရာ၊ အရာဝတ္ထု သို့မဟုတ် အယူအဆ၏ အမည်"},
        {"Verb", "လုပ်ဆောင်မှု သို့မဟုတ် ဖြစ်ပျက်မှုကို ဖော်ပြသော စကားလုံး"},
        {"Photosynthesis", "အပင်များ အလင်းရောင်သုံးပြီး အစာချက်လုပ်သော လုပ်ငန်းစဉ်"},
        {"Equation", "ညီမျှသည့် expression နှစ်ခုကို '=' ဖြင့်ဖော်ပြထားခြင်း"},
        {"Phishing", "လိမ်လည်၍ လျှို့ဝှက်အချက်အလက် ရယူရန် ကြိုးပမ်းခြင်း"},
        {"Evaporation", "အရည်မှ အငွေ့သို့ ပြောင်းခြင်း"},
        {"Simple Present", "အလေ့အကျင့်နှင့် အထွေထွေအမှန်တရား ဖော်ပြသည့် tense"},
        {"Timeline", "ဖြစ်ရပ်များကို အချိန်အစဉ်လိုက် ပြထားသည့် ပုံစံ"},
        {"Numerator", "အပိုင်းကိန်း၏ အပေါ်က ကိန်း"},
        {"Denominator", "အပိုင်းကိန်း၏ အောက်က ကိန်း"},
        {"Critical Thinking", "အထောက်အထားနှင့် အကြောင်းပြချက်များ စိစစ်စဉ်းစားခြင်း"}
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("student_ai_prefs", MODE_PRIVATE);
        getWindow().setStatusBarColor(NAVY);
        getWindow().setNavigationBarColor(WHITE);
        if (Build.VERSION.SDK_INT >= 26) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        String requested = getIntent() == null ? null : getIntent().getStringExtra("open_page");
        show("chat".equals(requested) ? "chat" : "home");
    }
    private int dp(float v){ return (int)(getResources().getDisplayMetrics().density*v+0.5f); }
    private GradientDrawable bg(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    private GradientDrawable bordered(int color,int stroke,int radius){GradientDrawable d=bg(color,radius);d.setStroke(dp(1),stroke);return d;}
    private GradientDrawable gradient(int a,int b,int radius){GradientDrawable d=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{a,b});d.setCornerRadius(dp(radius));return d;}
    private LinearLayout vertical(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);return l;}
    private LinearLayout horizontal(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.HORIZONTAL);l.setGravity(Gravity.CENTER_VERTICAL);return l;}
    private LinearLayout.LayoutParams lp(int w,int h){return new LinearLayout.LayoutParams(w<0?w:dp(w),h<0?h:dp(h));}
    private void pad(View v,int left,int top,int right,int bottom){v.setPadding(dp(left),dp(top),dp(right),dp(bottom));}
    private TextView text(String value,int sp,int color,boolean strong){TextView v=new TextView(this);v.setText(value);v.setTextSize(sp);v.setTextColor(color);v.setIncludeFontPadding(true);v.setLineSpacing(dp(3),1.0f);if(strong)v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return v;}
    private void margin(LinearLayout parent, View child,int t,int b){LinearLayout.LayoutParams p=lp(-1,-2);p.topMargin=dp(t);p.bottomMargin=dp(b);parent.addView(child,p);}
    private void headline(LinearLayout p,String title,String subtitle){TextView t=text(title,23,INK,true);margin(p,t,8,2);if(subtitle!=null){TextView s=text(subtitle,13,MUTED,false);margin(p,s,0,14);}}
    private LinearLayout panel(LinearLayout p){LinearLayout card=vertical();pad(card,16,17,16,17);card.setBackground(bg(WHITE,20));card.setElevation(dp(2));margin(p,card,4,12);return card;}
    private void show(String destination){
        page=destination;
        app=vertical();app.setBackgroundColor(BG);setContentView(app);
        // Android 15+ enforces edge-to-edge for targetSdk 35. Keep content outside system bars.
        if (Build.VERSION.SDK_INT >= 35) {
            app.setOnApplyWindowInsetsListener((v, insets) -> {
                android.graphics.Insets bars = insets.getInsets(WindowInsets.Type.systemBars());
                v.setPadding(0, bars.top, 0, bars.bottom);
                return insets;
            });
        }
        LinearLayout top=horizontal();pad(top,20,16,20,16);top.setBackgroundColor(NAVY);
        TextView brand=text("✦",25,WHITE,true);brand.setGravity(Gravity.CENTER);
        brand.setBackground(gradient(SKY,PURPLE,14));top.addView(brand,lp(46,46));
        LinearLayout name=vertical();pad(name,12,0,0,0);
        name.addView(text("Student AI Study",19,WHITE,true));name.addView(text("LEARN • PRACTICE • GROW",10,0xFFB3C4E8,false));
        top.addView(name,new LinearLayout.LayoutParams(0,-2,1));
        TextView info=text("ⓘ",25,WHITE,true);pad(info,8,3,8,3);info.setOnClickListener(v->show("about"));top.addView(info);
        app.addView(top,lp(-1,-2));
        ScrollView sc=new ScrollView(this);sc.setFillViewport(true);sc.setClipToPadding(false);
        surface=vertical();pad(surface,18,20,18,24);sc.addView(surface);
        app.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
        switch(destination){
            case "home":home();break;
            case "lessons":lessons(null);break;
            case "lesson":lessonDetail();break;
            case "quiz":quiz();break;
            case "flashcards":flashcards();break;
            case "practice":practice();break;
            case "planner":planner();break;
            case "chat":chat();break;
            case "about":about();break;
            default:home();
        }
        nav();
    }
    // Match the main catalog's always-visible phone footer in study, quiz and planner screens.
    private void nav(){
        bottom=vertical();
        bottom.setBackgroundColor(WHITE);
        View line=new View(this);
        line.setBackgroundColor(0xFFE4ECF6);
        bottom.addView(line,new LinearLayout.LayoutParams(-1,dp(1)));
        LinearLayout tabsRow=horizontal();
        pad(tabsRow,2,3,2,4);
        String[] titles={"ပင်မ","စကားဝိုင်း","AI ဆရာ","သိမ်းထား","ပိုမို"};
        String[] icons={"⌂","▤","✦","♥","☰"};
        String[] routes={"home","list","chat","saved","about"};
        for(int i=0;i<routes.length;i++){
            final String route=routes[i];
            boolean active=page.equals(route)||(page.equals("quiz")&&route.equals("chat"))
                ||(page.equals("flashcards")&&route.equals("chat"));
            LinearLayout tab=vertical();
            tab.setGravity(Gravity.CENTER);
            pad(tab,1,5,1,3);
            TextView glyph=text(icons[i],22,active?SKY:MUTED,true);
            glyph.setGravity(Gravity.CENTER);
            if(i==2){
                glyph.setTextColor(WHITE);
                glyph.setBackground(bg(SKY,14));
                pad(glyph,10,2,10,2);
            }
            tab.addView(glyph);
            TextView label=text(titles[i],10,active?SKY:MUTED,active);
            label.setGravity(Gravity.CENTER);
            tab.addView(label);
            tab.setContentDescription(titles[i]);
            tab.setFocusable(true);
            tab.setClickable(true);
            tab.setOnClickListener(v->{
                if("chat".equals(route)||"about".equals(route)) show(route);
                else {
                    Intent intent=new Intent(this,CatalogActivity.class);
                    intent.putExtra("open_page",route);
                    startActivity(intent);
                    finish();
                }
            });
            tabsRow.addView(tab,new LinearLayout.LayoutParams(0,dp(61),1));
        }
        bottom.addView(tabsRow,new LinearLayout.LayoutParams(-1,dp(68)));
        app.addView(bottom,new LinearLayout.LayoutParams(-1,-2));
    }
    private TextView button(String label, int color, int textColor,Runnable action){
        TextView v=text(label,15,textColor,true);v.setGravity(Gravity.CENTER);pad(v,16,13,16,13);v.setBackground(bg(color,13));
        v.setClickable(true);v.setFocusable(true);v.setOnClickListener(w->action.run());return v;
    }
    private void mainButton(LinearLayout parent,String label,Runnable action){margin(parent,button(label,SKY,WHITE,action),9,3);}
    private void smallCaption(LinearLayout p,String title){margin(p,text(title,16,INK,true),14,6);}
    private void home(){
        margin(surface,text("မင်္ဂလာပါ၊ ကျောင်းသား/ကျောင်းသူ 👋",23,INK,true),2,5);
        margin(surface,text("ယနေ့ သင်ယူမှုတစ်ခု စတင်လိုက်ပါ။",14,MUTED,false),0,17);
        LinearLayout hero=vertical();pad(hero,20,20,20,21);hero.setBackground(gradient(SKY,PURPLE,24));
        margin(hero,text("AI STUDY COMPANION",11,0xFFDDE9FF,true),0,9);
        margin(hero,text("မေးပါ • စဉ်းစားပါ\nနားလည်အောင် လေ့လာပါ",24,WHITE,true),1,11);
        margin(hero,text("တရားဝင် ChatGPT Study Mode ဖြင့် သင်ခန်းစာများကို အဆင့်လိုက် နားလည်အောင် လေ့လာနိုင်သည်။",13,0xFFE6ECFF,false),0,16);
        margin(hero,button("AI ဆရာကို မေးမည်  ↗",WHITE,SKY,()->show("chat")),2,1);
        margin(surface,hero,2,16);
        LinearLayout stats=horizontal();
        stat(stats,""+LESSONS.length,"သင်ခန်းစာ");stat(stats,""+QUIZ.length,"Quiz မေးခွန်း");stat(stats,""+prefs.getInt("best_score",0),"အမြင့်ဆုံးရမှတ်");
        margin(surface,stats,2,8);
        smallCaption(surface,"ဘာသာရပ်များ");
        for(int i=0;i<TOPICS.length;i+=2){LinearLayout row=horizontal();for(int j=i;j<Math.min(i+2,TOPICS.length);j++){
                int index=j;LinearLayout card=vertical();pad(card,14,15,14,15);card.setBackground(bg(WHITE,18));
                TextView icon=text(TOPIC_ICONS[j],23,TOPIC_COLORS[j],true);margin(card,icon,0,11);
                margin(card,text(TOPICS[j],13,INK,true),0,4);
                margin(card,text("သင်ခန်းစာ ၂ ခု  ›",11,MUTED,false),0,0);
                card.setOnClickListener(v->lessonsBySubject(TOPICS[index]));
                LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(0,dp(133),1);cp.setMargins(dp(3),dp(4),dp(3),dp(4));row.addView(card,cp);
            }margin(surface,row,0,2);}
        smallCaption(surface,"အမြန်စတင်ရန်");
        LinearLayout item=panel(surface);item.addView(text("✦  Daily Quick Quiz",17,INK,true));
        item.addView(text("မေးခွန်း ၁၂ ခုဖြင့် အသိပညာကို စစ်ဆေးပါ။",13,MUTED,false));mainButton(item,"Quiz ဖြေမည်  →",()->startQuiz());
        LinearLayout flash=panel(surface);flash.addView(text("▣  Flashcards",17,INK,true));flash.addView(text("စကားလုံး၊ သဘောတရားများကို မှတ်မိအောင် လေ့ကျင့်ပါ။",13,MUTED,false));mainButton(flash,"Flashcards ဖွင့်မည်  →",()->show("flashcards"));
    }
    private void stat(LinearLayout parent,String num,String caption){LinearLayout c=vertical();c.setGravity(Gravity.CENTER);pad(c,4,13,4,13);c.setBackground(bg(WHITE,15));
        TextView big=text(num,22,SKY,true);big.setGravity(Gravity.CENTER);c.addView(big);TextView lbl=text(caption,10,MUTED,false);lbl.setGravity(Gravity.CENTER);c.addView(lbl);
        LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(0,dp(75),1);cp.setMargins(dp(3),0,dp(3),0);parent.addView(c,cp);}
    private void lessonsBySubject(String subject){show("lessons");lessonsFilter(subject);}
    private void lessonsFilter(String subject){showLessons(subject);}
    private void lessons(String subject){showLessons(subject);}
    private void showLessons(String subject){
        surface.removeAllViews();
        headline(surface,subject==null?"သင်ခန်းစာများ":subject+" သင်ခန်းစာ", "Offline ဖတ်ရှုနိုင်ပြီး AI နှင့် ဆက်လက်လေ့လာနိုင်သည်။");
        if(subject!=null)margin(surface,button("‹  ဘာသာရပ်အားလုံး",0xFFE7ECF9,INK,()->show("lessons")),0,7);
        for(Lesson l:LESSONS){if(subject!=null&&!subject.equals(l.subject))continue;
            LinearLayout p=panel(surface);TextView t=text(l.subject.toUpperCase(Locale.ROOT),11,SKY,true);margin(p,t,0,7);
            margin(p,text(l.title,18,INK,true),0,8);
            margin(p,text(l.body,13,MUTED,false),0,9);
            if(prefs.getBoolean("lesson_"+l.title,false))margin(p,text("✓ လေ့လာပြီး",12,GREEN,true),0,2);
            p.setOnClickListener(v->{selectedLesson=l;show("lesson");});
            margin(p,button("ဖတ်ရှုမည်   →",0xFFEDF2FF,SKY,()->{selectedLesson=l;show("lesson");}),4,0);
        }
    }
    private void lessonDetail(){
        Lesson l=selectedLesson;if(l==null){show("lessons");return;}
        margin(surface,button("‹ သင်ခန်းစာများ",0xFFE8EEFA,INK,()->show("lessons")),0,15);
        headline(surface,l.title,l.subject+" • သင်ခန်းစာ");
        LinearLayout article=panel(surface);
        margin(article,text("အဓိကအကြောင်းအရာ",18,INK,true),0,12);
        margin(article,text(l.body,16,INK,false),0,17);
        margin(article,text("ဥပမာ",17,SKY,true),0,8);
        margin(article,text(l.example,15,MUTED,false),0,17);
        article.addView(text("AI ဆရာကို ဒီသင်ခန်းစာအကြောင်း မေးပါ။",14,INK,true));
        margin(article,button("AI ဖြင့် ဆက်လေ့လာမည်  ↗",SKY,WHITE,()->prepareChat(l.prompt)),12,3);
        margin(surface,button(prefs.getBoolean("lesson_"+l.title,false)?"✓ လေ့လာပြီးပါပြီ":"✓ လေ့လာပြီးဟု မှတ်သားမည်",GREEN,WHITE,()->{
            prefs.edit().putBoolean("lesson_"+l.title,true).apply();Toast.makeText(this,"သင်ခန်းစာမှတ်သားပြီးပါပြီ",Toast.LENGTH_SHORT).show();show("lesson");
        }),3,8);
    }
    private void practice(){
        headline(surface,"လေ့ကျင့်ခန်းများ","နေ့စဉ် အနည်းငယ်စီ ပြန်လည်လေ့ကျင့်ပါ။");
        LinearLayout quiz=panel(surface);margin(quiz,text("✦  Knowledge Quiz",21,INK,true),0,8);
        margin(quiz,text("မေးခွန်း ၁၂ ခု • ချက်ချင်းအဖြေရှင်းချက် • ရမှတ်သိမ်းဆည်းခြင်း",14,MUTED,false),0,9);
        margin(quiz,text("အကောင်းဆုံးရမှတ် — "+prefs.getInt("best_score",0)+" / "+QUIZ.length,13,SKY,true),3,7);
        mainButton(quiz,"Quiz စတင်မည်  →",()->startQuiz());
        LinearLayout flash=panel(surface);margin(flash,text("▤  Study Flashcards",21,INK,true),0,8);
        margin(flash,text("အဓိပ္ပာယ်ဖုံးထားပြီး ကိုယ့်ကိုယ်ကို စမ်းသပ်နိုင်သည့် Card "+CARDS.length+" ခု။",14,MUTED,false),0,10);
        mainButton(flash,"Flashcards လေ့လာမည်  →",()->show("flashcards"));
        LinearLayout ai=panel(surface);margin(ai,text("♧  AI Practice Teacher",21,INK,true),0,8);
        margin(ai,text("အဆင့်နှင့် ဘာသာရပ်ရွေးပြီး ChatGPT Study Mode တွင် လေ့ကျင့်ခန်းယူပါ။",14,MUTED,false),0,10);
        mainButton(ai,"AI ဆရာနှင့် လေ့ကျင့်မည်  ↗",()->show("chat"));
    }
    private void startQuiz(){quizIndex=0;quizScore=0;questionAnswered=false;show("quiz");}
    private void quiz(){
        headline(surface,"Knowledge Quiz", "မေးခွန်း "+(quizIndex+1)+" / "+QUIZ.length+"   •   မှန် "+quizScore);
        Question q=QUIZ[quizIndex];
        LinearLayout p=panel(surface);margin(p,text(q.q,20,INK,true),0,15);
        final TextView feedback=text("",14,GREEN,true);
        final TextView next=button(quizIndex==QUIZ.length-1?"ရလဒ်ကြည့်မည်  →":"နောက်မေးခွန်း  →",SKY,WHITE,()->{
            if(!questionAnswered)return;
            if(quizIndex<QUIZ.length-1){quizIndex++;questionAnswered=false;show("quiz");}else finishQuiz();
        });
        for(int j=0;j<q.answers.length;j++){
            final int answerIndex=j;
            TextView choice=button((char)('A'+j)+".  "+q.answers[j],0xFFECF1FA,INK,()->{
                if(questionAnswered)return;
                questionAnswered=true;if(answerIndex==q.correct){quizScore++;feedback.setText("✓ မှန်ကန်ပါသည်။\n"+q.explanation);feedback.setTextColor(GREEN);}
                else {feedback.setText("✗ အမှန်က "+q.answers[q.correct]+" ဖြစ်သည်။\n"+q.explanation);feedback.setTextColor(0xFFCC544F);}
                next.setVisibility(View.VISIBLE);
            });margin(p,choice,3,7);
        }
        margin(p,feedback,12,8);next.setVisibility(View.GONE);margin(p,next,3,0);
        margin(surface,button("✕  Quiz မှ ထွက်မည်",0xFFE8EEFA,INK,()->show("practice")),7,0);
    }
    private void finishQuiz(){
        int high=Math.max(prefs.getInt("best_score",0),quizScore);
        prefs.edit().putInt("best_score",high).apply();
        page="practice";
        surface.removeAllViews();
        headline(surface,"Quiz ပြီးဆုံးပါပြီ!","ကြိုးစားလေ့လာမှုတိုင်း အကျိုးရှိပါတယ်။");
        LinearLayout r=panel(surface);r.setGravity(Gravity.CENTER);
        TextView score=text(quizScore+" / "+QUIZ.length,37,SKY,true);score.setGravity(Gravity.CENTER);margin(r,score,9,8);
        TextView message=text(quizScore>=10?"အလွန်ကောင်းပါတယ်!":quizScore>=7?"ကောင်းပါတယ်၊ ဆက်လေ့ကျင့်ပါ။":"အဖြေရှင်းချက်ကို ပြန်လေ့လာပြီး ထပ်စမ်းကြည့်ပါ။",16,INK,true);message.setGravity(Gravity.CENTER);margin(r,message,0,16);
        mainButton(r,"ထပ်ဖြေမည်",()->startQuiz());margin(r,button("လေ့ကျင့်ခန်းများသို့",0xFFE8EEFA,INK,()->show("practice")),8,3);
    }
    private void flashcards(){
        headline(surface,"Study Flashcards",(cardIndex+1)+" / "+CARDS.length+" • Card ကိုနှိပ်၍ အဖြေဖွင့်ပါ");
        LinearLayout p=panel(surface);p.setGravity(Gravity.CENTER);p.setMinimumHeight(dp(265));
        margin(p,text("FLIP TO LEARN",12,SKY,true),5,26);
        TextView main=text(cardShowingAnswer?CARDS[cardIndex][1]:CARDS[cardIndex][0],cardShowingAnswer?20:27,INK,true);main.setGravity(Gravity.CENTER);margin(p,main,2,28);
        p.setOnClickListener(v->{cardShowingAnswer=!cardShowingAnswer;show("flashcards");});
        margin(surface,button(cardShowingAnswer?"အဖြေဖုံးမည်  ↻":"အဓိပ္ပာယ်ကြည့်မည်  ↻",SKY,WHITE,()->{cardShowingAnswer=!cardShowingAnswer;show("flashcards");}),9,8);
        LinearLayout row=horizontal();TextView prev=button("‹  ရှေ့ Card",0xFFE8EEFA,INK,()->{cardIndex=(cardIndex+CARDS.length-1)%CARDS.length;cardShowingAnswer=false;show("flashcards");});
        TextView next=button("နောက် Card  ›",0xFFE8EEFA,INK,()->{cardIndex=(cardIndex+1)%CARDS.length;cardShowingAnswer=false;show("flashcards");});
        LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(0,dp(49),1);cp.setMargins(dp(3),0,dp(3),0);row.addView(prev,cp);row.addView(next,new LinearLayout.LayoutParams(cp));
        margin(surface,row,4,5);
    }
    private void planner(){
        headline(surface,"Study Planner","ကိုယ်ပိုင် သင်ယူမှုအစီအစဉ်ကို ဖုန်းထဲမှာ သိမ်းဆည်းထားပါ။");
        LinearLayout add=panel(surface);margin(add,text("လုပ်ဆောင်ရန် သင်ခန်းစာ",17,INK,true),0,10);
        EditText field=input("ဥပမာ: သင်္ချာ အခန်း ၂ လေ့လာရန်");margin(add,field,0,10);
        mainButton(add,"+  အစီအစဉ်ထည့်မည်",()->{
            String task=field.getText().toString().trim();if(task.isEmpty()){field.setError("စာရေးထည့်ပါ");return;}
            JSONArray arr=loadTasks();JSONObject obj=new JSONObject();try{obj.put("task",task);obj.put("done",false);arr.put(obj);}catch(Exception ignored){}
            prefs.edit().putString("tasks",arr.toString()).apply();show("planner");
        });
        JSONArray tasks=loadTasks();int done=0;for(int i=0;i<tasks.length();i++)if(tasks.optJSONObject(i)!=null&&tasks.optJSONObject(i).optBoolean("done"))done++;
        margin(surface,text("အစီအစဉ် "+tasks.length()+" ခု  •  ပြီးဆုံး "+done+" ခု",15,INK,true),10,6);
        if(tasks.length()==0){LinearLayout empty=panel(surface);margin(empty,text("☷  အစီအစဉ် မရှိသေးပါ",17,MUTED,true),15,20);}
        for(int i=0;i<tasks.length();i++){
            JSONObject item=tasks.optJSONObject(i);if(item==null)continue;
            final int at=i;boolean completed=item.optBoolean("done");
            LinearLayout row=panel(surface);LinearLayout h=horizontal();
            TextView toggle=button(completed?"✓":"○",completed?GREEN:0xFFE8EEFA,completed?WHITE:SKY,()->{
                JSONArray fresh=loadTasks();JSONObject obj=fresh.optJSONObject(at);if(obj!=null){try{obj.put("done",!obj.optBoolean("done"));prefs.edit().putString("tasks",fresh.toString()).apply();}catch(Exception ignored){}}show("planner");
            });h.addView(toggle,lp(48,46));
            TextView lbl=text(item.optString("task"),14,completed?MUTED:INK,!completed);pad(lbl,12,4,6,4);if(completed)lbl.setPaintFlags(lbl.getPaintFlags()|16);
            h.addView(lbl,new LinearLayout.LayoutParams(0,-2,1));
            TextView remove=text("✕",17,0xFFC45A5A,true);pad(remove,7,8,5,8);remove.setOnClickListener(v->{
                JSONArray fresh=loadTasks(),result=new JSONArray();for(int k=0;k<fresh.length();k++)if(k!=at)result.put(fresh.opt(k));
                prefs.edit().putString("tasks",result.toString()).apply();show("planner");
            });h.addView(remove);row.addView(h);
        }
    }
    private JSONArray loadTasks(){try{return new JSONArray(prefs.getString("tasks","[]"));}catch(Exception e){return new JSONArray();}}
    private EditText input(String hint){EditText e=new EditText(this);e.setTextSize(15);e.setSingleLine(false);e.setMinHeight(dp(54));
        e.setHint(hint);e.setTextColor(INK);e.setHintTextColor(MUTED);e.setBackground(bordered(0xFFF7F9FD,0xFFD9E2F3,12));pad(e,13,11,13,11);return e;}
    private void prepareChat(String prompt){prefs.edit().putString("chat_prompt",prompt).apply();show("chat");}
    private void chat(){
        headline(surface,"AI Study Teacher","ChatGPT Study Mode ကို တရားဝင် Website/App မှာ ဖွင့်မည်။");
        LinearLayout banner=panel(surface);margin(banner,text("အဖြေကို တန်းမကူးဘဲ\nနားလည်အောင် သင်ယူပါ",21,INK,true),0,12);
        margin(banner,text("အဆင့်လိုက် မေးခွန်းများ၊ အရိပ်အမြွက်နှင့် ရှင်းလင်းချက်များကို ChatGPT Study Mode မှာ လေ့လာနိုင်ပါသည်။",14,MUTED,false),0,3);
        margin(surface,text("တန်း / အဆင့်",14,INK,true),8,8);
        gradeSelect=spinner(new String[]{"မူလတန်း", "အလယ်တန်း", "အထက်တန်း", "တက္ကသိုလ်", "Beginner"});margin(surface,gradeSelect,0,10);
        margin(surface,text("ဘာသာရပ်",14,INK,true),8,8);
        subjectSelect=spinner(new String[]{"အထွေထွေ", "မြန်မာစာ", "English", "Mathematics", "Science", "History", "ICT"});margin(surface,subjectSelect,0,10);
        margin(surface,text("သိချင်သော မေးခွန်း",14,INK,true),8,8);
        questionInput=input("ဥပမာ: အပိုင်းကိန်းပေါင်းနည်းကို အဆင့်လိုက် ရှင်းပြပေးပါ။");questionInput.setMinLines(3);
        questionInput.setText(prefs.getString("chat_prompt",""));margin(surface,questionInput,0,10);
        mainButton(surface,"မေးခွန်း Copy + Study Mode ဖွင့်မည်  ↗",()->openStudyWithPrompt());
        margin(surface,button("Study Mode ကို တိုက်ရိုက်ဖွင့်မည်",0xFFE5EDFF,SKY,()->openLink(STUDY_URL)),7,9);
        LinearLayout note=panel(surface);margin(note,text("သိထားရန်",15,INK,true),0,5);
        margin(note,text("ဤ App သည် OpenAI ၏တရားဝင် App မဟုတ်ပါ။ ChatGPT Login ကို ဒီ App ထဲတွင် မတောင်းခံပါ။ Copy လုပ်ထားသော မေးခွန်းကို ChatGPT ထဲမှာ Paste လုပ်ပြီး ပို့ရပါမည်။",13,MUTED,false),0,0);
    }
    private Spinner spinner(String[] values){Spinner s=new Spinner(this);ArrayAdapter<String> a=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,values);a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);s.setAdapter(a);s.setBackground(bordered(WHITE,0xFFD9E2F3,10));pad(s,10,8,10,8);return s;}
    private void openStudyWithPrompt(){
        String raw=questionInput.getText().toString().trim();if(raw.isEmpty()){questionInput.setError("မေးခွန်းထည့်ပါ");return;}
        String prompt="I am a "+gradeSelect.getSelectedItem()+" student. Subject: "+subjectSelect.getSelectedItem()+". Please tutor me in Burmese using Study Mode. Ask guiding questions, give hints before answers, explain step by step, and check my understanding. My question: "+raw;
        ClipboardManager cb=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        if(cb!=null)cb.setPrimaryClip(ClipData.newPlainText("Study prompt",prompt));
        prefs.edit().putString("chat_prompt",raw).apply();
        Toast.makeText(this,"မေးခွန်း Copy ပြီးပါပြီ။ ChatGPT တွင် Paste လုပ်ပါ။",Toast.LENGTH_LONG).show();
        openLink(STUDY_URL);
    }
    private void openLink(String url){try{Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse(url));startActivity(i);}catch(Exception ex){Toast.makeText(this,"Browser မတွေ့ပါ။ Link: "+url,Toast.LENGTH_LONG).show();}}
    private void about(){
        headline(surface,"About Student AI Study","Native Android • Offline First • Version 1.0.0");
        LinearLayout a=panel(surface);margin(a,text("ကျောင်းသားများအတွက် AI သင်ယူမှုအထောက်အကူပြု App",19,INK,true),0,11);
        margin(a,text("သင်ခန်းစာ ၁၂ ခု၊ Quiz မေးခွန်း ၁၂ ခု၊ Flashcards ၁၂ ခု၊ Study Planner နှင့် ChatGPT Study Mode Shortcut တို့ ပါဝင်ပါသည်။",14,MUTED,false),0,10);
        margin(a,text("ဤ App ကို AAII Student Study Assistant အဖြစ် သီးခြားဖန်တီးထားပြီး OpenAI သို့မဟုတ် ChatGPT ၏ တရားဝင်ထုတ်ကုန်ဟု မဆိုလိုပါ။",13,MUTED,false),0,0);
        LinearLayout b=panel(surface);margin(b,text("Privacy & Safety",18,INK,true),0,10);
        margin(b,text("• သင်ခန်းစာ၊ Quiz နှင့် Planner အချက်အလက်များကို ဖုန်းအတွင်းသာ သိမ်းဆည်းသည်။\n• App သည် ChatGPT password သို့မဟုတ် API key မတောင်းခံပါ။\n• AI ကို အသုံးပြုလိုက်သည့်အခါ ပြင်ပ ChatGPT Website/App ကို ဖွင့်သည်။\n• အရေးကြီးသော AI အဖြေများကို စာအုပ်/ဆရာနှင့် ပြန်လည်စစ်ဆေးပါ။",13,MUTED,false),0,11);
        mainButton(b,"OpenAI Study Mode အကြောင်းဖတ်ရန်  ↗",()->openLink("https://help.openai.com/en/articles/11780217-using-study-mode-in-chatgpt"));
        margin(surface,button("ပင်မသို့ ပြန်သွားမည်",0xFFE8EEFA,INK,()->show("home")),7,7);
    }
    @Override public void onBackPressed(){if(page.equals("lesson")){show("lessons");}else if(!page.equals("home")){show("home");}else super.onBackPressed();}
}
