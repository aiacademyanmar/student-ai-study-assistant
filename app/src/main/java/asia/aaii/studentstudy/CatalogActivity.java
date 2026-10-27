package asia.aaii.studentstudy;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/** Native 100 student chat starter UI, Myanmar first; launches official Study Mode externally. */
public class CatalogActivity extends Activity {
    private static final int NAVY = 0xFF0D245A, DEEP = 0xFF08204A, BLUE = 0xFF1264EE;
    private static final int CYAN = 0xFF06B8D9, PURPLE = 0xFF7650E9, INK = 0xFF12244E;
    private static final int MUTED = 0xFF667693, BG = 0xFFF3F8FF, WHITE = 0xFFFFFFFF, LINE = 0xFFE4ECF6;
    private static final int GREEN = 0xFF0DAA76, CORAL = 0xFFEF4D7D;
    private static final int[] CAT_COLORS = {GREEN, BLUE, CORAL};
    private static final int[] CAT_BG = {0xFFE8FAF1, 0xFFE7F2FF, 0xFFFFEBF2};
    private static final String STUDY_URL = "https://chatgpt.com/studymode";
    private static final String ACADEMY_URL = "https://academy.openai.com/pages/courses?linkMenu=Courses";
    private static final String[] ACADEMY_COURSES = {
        "https://academy.openai.com/public/courses/ai-foundations-juzjs",
        "https://academy.openai.com/public/courses/applied-ai-foundations-hgk7r",
        "https://academy.openai.com/public/courses/agents-and-workflows-bieml"
    };
    private LinearLayout root, body, cardList;
    private SharedPreferences prefs;
    private final Set<Integer> saved = new HashSet<>();
    private String page="home", returnPage="list";
    private PromptLibrary.Item selected=PromptLibrary.ALL[0];
    private int filter=-1;

    @Override public void onCreate(Bundle state){
        super.onCreate(state);
        prefs=getSharedPreferences("student_catalog_v2", MODE_PRIVATE);
        String s=prefs.getString("favorites", "");
        for(String chunk:s.split(",")) {try{ saved.add(Integer.parseInt(chunk)); }catch(Exception ignored) {}}
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        String requested = getIntent() == null ? null : getIntent().getStringExtra("open_page");
        if (!"list".equals(requested) && !"saved".equals(requested) && !"courses".equals(requested) && !"profile".equals(requested)) requested="home";
        show(requested);
    }
    private int d(float n){ return (int)(getResources().getDisplayMetrics().density*n+.5f); }
    private GradientDrawable fill(int color,int radius){GradientDrawable x=new GradientDrawable();x.setColor(color);x.setCornerRadius(d(radius));return x;}
    private GradientDrawable outline(int color,int radius,int line){GradientDrawable x=fill(color,radius);x.setStroke(d(1),line);return x;}
    private GradientDrawable grad(int a,int b,int radius){GradientDrawable x=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{a,b});x.setCornerRadius(d(radius));return x;}
    private LinearLayout col(){LinearLayout v=new LinearLayout(this);v.setOrientation(LinearLayout.VERTICAL);return v;}
    private LinearLayout row(){LinearLayout v=new LinearLayout(this);v.setOrientation(LinearLayout.HORIZONTAL);v.setGravity(Gravity.CENTER_VERTICAL);return v;}
    private LinearLayout.LayoutParams lp(int w,int h){return new LinearLayout.LayoutParams(w<0?w:d(w),h<0?h:d(h));}
    private void margin(View v,LinearLayout host,int l,int t,int r,int b){LinearLayout.LayoutParams p=lp(-1,-2);p.setMargins(d(l),d(t),d(r),d(b));host.addView(v,p);}
    private TextView tv(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setGravity(Gravity.CENTER_VERTICAL);t.setIncludeFontPadding(true);return t;}
    private TextView click(String s,int size,int color,boolean bold,Runnable action){TextView v=tv(s,size,color,bold);v.setOnClickListener(w->action.run());return v;}
    private void padding(View v,int l,int t,int r,int b){v.setPadding(d(l),d(t),d(r),d(b));}
    private TextView pill(String text,int fill,int color,int size){TextView t=tv(text,size,color,true);padding(t,13,9,13,9);t.setGravity(Gravity.CENTER);t.setBackground(fill(fill,20));return t;}
    private View spacer(int h){ View v=new View(this);v.setLayoutParams(lp(1,h));return v; }
    private void section(String title,String action,Runnable go){LinearLayout r=row();TextView a=tv(title,19,INK,true);r.addView(a,new LinearLayout.LayoutParams(0,-2,1));TextView b=click(action,12,BLUE,true,go);r.addView(b);margin(r,body,0,18,0,12);}
    private void show(String screen){
        page=screen;
        root=col();root.setBackgroundColor(BG);setContentView(root);
        // Keep the permanent footer above Android 15 navigation/gesture bars.
        if (Build.VERSION.SDK_INT >= 35) {
            root.setOnApplyWindowInsetsListener((v, insets) -> {
                android.graphics.Insets bars = insets.getInsets(android.view.WindowInsets.Type.systemBars());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            });
        }
        ScrollView scroll=new ScrollView(this);scroll.setFillViewport(false);scroll.setVerticalScrollBarEnabled(false);
        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        body=col();padding(body,17,17,17,22);scroll.addView(body);
        if(screen.equals("home")) home();
        else if(screen.equals("list")) listPage();
        else if(screen.equals("detail")) detail();
        else if(screen.equals("compose")) compose();
        else if(screen.equals("saved")) favorites();
        else if(screen.equals("courses")) {startActivity(new Intent(this,AcademyWebViewActivity.class));show("home");}
        else profile();
        bottomBar();
    }
    private void pageHeader(String title,String subtitle){
        LinearLayout bar=row(); TextView back=click("‹",32,BLUE,true,()->back());back.setGravity(Gravity.CENTER);
        bar.addView(back,lp(32,40)); LinearLayout texts=col();TextView t=tv(title,21,INK,true);texts.addView(t);
        if(subtitle!=null){texts.addView(tv(subtitle,11,MUTED,false));}
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-2,1);p.leftMargin=d(7);bar.addView(texts,p);
        ImageView icon=new ImageView(this);icon.setImageResource(R.mipmap.ic_launcher);icon.setScaleType(ImageView.ScaleType.CENTER_CROP);bar.addView(icon,lp(39,39));
        margin(bar,body,0,1,0,17);
    }
    private void brand(){
        LinearLayout r=row();ImageView logo=new ImageView(this);logo.setImageResource(R.mipmap.ic_launcher);logo.setBackground(fill(BLUE,12));r.addView(logo,lp(43,43));
        LinearLayout name=col();TextView a=tv("Student AI",21,NAVY,true);name.addView(a);name.addView(tv("Study Assistant · မြန်မာ",11,MUTED,false));
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-2,1);p.leftMargin=d(9);r.addView(name,p);
        TextView bell=click("♡",29,CORAL,true,()->show("saved"));bell.setGravity(Gravity.CENTER);r.addView(bell,lp(39,42));
        margin(r,body,0,0,0,17);
    }
    private void hero(){
        LinearLayout hero=row();hero.setBackground(grad(0xFF07347D,0xFF176DE9,23));padding(hero,15,14,5,14);
        LinearLayout copy=col();TextView h=tv("ကျောင်းသားများအတွက်\nAI စကားဝိုင်း ၁၀၀",24,WHITE,true);copy.addView(h);
        TextView sub=tv("လေ့လာပါ · အလုပ်အကိုင် · နေ့စဉ်ဘဝ",12,0xFFD9F6FF,false);LinearLayout.LayoutParams k=lp(-1,-2);k.topMargin=d(8);copy.addView(sub,k);
        TextView c=pill("စကားဝိုင်းများ ကြည့်ရန်  ›",0xFF1CDACB,0xFF07347D,12);LinearLayout.LayoutParams cp=lp(-2,-2);cp.topMargin=d(15);copy.addView(c,cp);
        c.setOnClickListener(w->show("list"));hero.addView(copy,new LinearLayout.LayoutParams(0,-2,1));
        ImageView image=new ImageView(this);image.setImageResource(R.drawable.study_robot);image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        hero.addView(image,lp(128,171));margin(hero,body,0,0,0,10);
    }
    private void home(){
        brand();hero();
        TextView find=click("⌕   စကားဝိုင်း / အကြောင်းအရာ ရှာရန်...",14,MUTED,false,()->show("list"));
        padding(find,16,15,12,15);find.setBackground(outline(WHITE,17,LINE));margin(find,body,0,1,0,3);
        section("စကားဝိုင်း အမျိုးအစားများ","အားလုံး  ›",()->show("list"));
        LinearLayout tiles=row();String[] labels={"ပညာရေး","အလုပ်အကိုင်","နေ့စဉ်ဘဝ"};String[] icons={"📗","💼","♥"};int[] counts={50,25,25};
        for(int i=0;i<3;i++){ final int cat=i;LinearLayout tile=col();tile.setGravity(Gravity.CENTER);tile.setBackground(fill(CAT_BG[i],18));padding(tile,7,17,7,17);
            TextView icon=tv(icons[i],28,CAT_COLORS[i],true);icon.setGravity(Gravity.CENTER);tile.addView(icon);
            TextView title=tv(labels[i],14,INK,true);title.setGravity(Gravity.CENTER);tile.addView(title);
            TextView small=tv(counts[i]+" စကားဝိုင်း",11,MUTED,false);small.setGravity(Gravity.CENTER);tile.addView(small);
            tile.setOnClickListener(w->{filter=cat;show("list");});
            LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-2,1);if(i>0)p.leftMargin=d(8);tiles.addView(tile,p);
        }margin(tiles,body,0,0,0,3);
        section("🔥 လူကြိုက်များသော စကားဝိုင်းများ","၁၀၀ ခုလုံး ကြည့်ရန်  ›",()->{filter=-1;show("list");});
        int[] picks={1,14,22,51,55,76};
        for(int id:picks){PromptLibrary.Item item=PromptLibrary.ALL[id-1]; chatRow(item,body);}
        section("📖  Offline မြန်မာစာဖတ်ခန်း","ဖတ်ရန်  ›",()->startActivity(new Intent(this,OfflineReaderActivity.class)));
        LinearLayout offline=card(0xFFE3F6EC);
        offline.addView(tv("AI at Work · Work Users",16,NAVY,true));
        offline.addView(tv("အင်တာနက်မလိုဘဲ ဖတ်နိုင်သော မြန်မာဘာသာ သင်ခန်းစာ ၁၅ ခု၊ လေ့ကျင့်ခန်းများနှင့် ဖတ်ပြီးမှတ်သားခြင်း",13,MUTED,false));
        margin(button("Offline သင်ခန်းစာများ ဖတ်ရန်",GREEN,WHITE,()->startActivity(new Intent(this,OfflineReaderActivity.class))),offline,0,12,0,0);
        margin(offline,body,0,0,0,6);
        section("🎓  OpenAI Academy သင်တန်းများ","သင်တန်းကြည့်ရန်  ›",()->startActivity(new Intent(this,AcademyWebViewActivity.class)));
        LinearLayout academy=card(0xFFE9F4FF);
        academy.addView(tv("AI Foundations · Applied AI · Agents & Workflows",15,NAVY,true));
        academy.addView(tv("အခြေခံမှ လက်တွေ့အသုံးချသင်တန်းအထိ တရားဝင် OpenAI Academy မှ လေ့လာနိုင်ပါသည်။",12,MUTED,false));
        margin(button("သင်တန်းစာရင်း ဖွင့်ရန်  ›",BLUE,WHITE,()->show("courses")),academy,0,12,0,0);
        margin(academy,body,0,0,0,4);
        section("AI Study Mode","သင်ခန်းစာများ  ›",()->startActivity(new Intent(this,MainActivity.class)));
        LinearLayout study=card(WHITE);study.addView(tv("🎓  ကိုယ်တိုင်စဉ်းစားပြီး သင်ယူပါ",16,INK,true));study.addView(tv("AI ကို အဖြေချက်ချင်းမပေးစေဘဲ အဆင့်လိုက် မေးခွန်းများဖြင့် လေ့လာနိုင်ပါတယ်။",13,MUTED,false));
        TextView go=button("ChatGPT Study Mode ဖွင့်ရန်",BLUE,WHITE,()->launchChatGPT("ကိုယ်တိုင်စဉ်းစားပြီး သင်ယူနိုင်အောင် မေးခွန်းတစ်ခုစီ မေးပေးပါ။"));margin(go,study,0,14,0,0);
        margin(study,body,0,0,0,0);
        margin(tv("OpenAI ၏ တရားဝင် App မဟုတ်ပါ။ AI ဖြေဆိုမှုသည် ChatGPT ဝဘ်ဆိုက်တွင်ဖြစ်သည်။",10,MUTED,false),body,0,16,0,0);
    }
    private LinearLayout card(int bgColor){LinearLayout c=col();c.setBackground(outline(bgColor,17,LINE));padding(c,14,13,14,13);return c;}
    private void chatRow(PromptLibrary.Item item,LinearLayout host){
        LinearLayout row=row();row.setBackground(outline(WHITE,16,LINE));padding(row,11,12,11,12);
        TextView ico=tv(item.category==0?"📘":item.category==1?"💼":"♥",22,CAT_COLORS[item.category],true);ico.setGravity(Gravity.CENTER);ico.setBackground(fill(CAT_BG[item.category],12));row.addView(ico,lp(48,48));
        LinearLayout mid=col();mid.addView(tv(item.id+".  "+item.title,14,INK,true));mid.addView(tv(item.description(),11,MUTED,false));
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-2,1);p.leftMargin=d(10);row.addView(mid,p);
        TextView arrow=tv("›",25,BLUE,true);row.addView(arrow);
        row.setOnClickListener(v->openDetail(item));LinearLayout.LayoutParams layout=lp(-1,-2);layout.bottomMargin=d(9);host.addView(row,layout);
    }
    private void listPage(){
        pageHeader("AI စကားဝိုင်း ၁၀၀","Student prompts · ပညာရေး / အလုပ်အကိုင် / နေ့စဉ်ဘဝ");
        EditText search=new EditText(this);search.setSingleLine(true);search.setTextSize(14);search.setTextColor(INK);
        search.setHintTextColor(MUTED);search.setHint("⌕  စကားဝိုင်း ရှာရန်...");search.setBackground(outline(WHITE,14,LINE));padding(search,15,11,12,11);
        margin(search,body,0,0,0,12);
        LinearLayout chips=row();String[] text={"အားလုံး (100)","ပညာရေး (50)","အလုပ် (25)","ဘဝ (25)"};
        for(int n=-1;n<3;n++){final int which=n;TextView chip=pill(text[n+1],filter==n?BLUE:0xFFEAF0FB,filter==n?WHITE:NAVY,11);
            LinearLayout.LayoutParams p=lp(0,-2);p.width=0;p.weight=n==-1?1.15f:1f;if(n!=-1)p.leftMargin=d(5);chips.addView(chip,p);
            chip.setOnClickListener(v->{filter=which;show("list");});}
        margin(chips,body,0,0,0,15);
        cardList=col();body.addView(cardList);rebuildList("");
        search.addTextChangedListener(new TextWatcher(){public void beforeTextChanged(CharSequence s,int a,int c,int f){}
            public void onTextChanged(CharSequence s,int start,int before,int count){rebuildList(s.toString());}
            public void afterTextChanged(Editable e){} });
    }
    private void rebuildList(String term){
        if(cardList==null)return;cardList.removeAllViews();int num=0;String q=term.toLowerCase(Locale.ROOT).trim();
        for(PromptLibrary.Item i:PromptLibrary.ALL){if(filter!=-1&&i.category!=filter)continue;
            if(!q.isEmpty()&&!i.title.toLowerCase(Locale.ROOT).contains(q)&&!i.description().contains(q)&&!(""+i.id).contains(q))continue;
            chatRow(i,cardList);num++;}
        if(num==0){TextView empty=tv("ရှာဖွေမှုနှင့် ကိုက်ညီသော စကားဝိုင်း မတွေ့ပါ။",14,MUTED,false);padding(empty,16,25,16,25);cardList.addView(empty);}
    }
    private void openDetail(PromptLibrary.Item i){selected=i;returnPage=page.equals("saved")?"saved":"list";show("detail");}
    private void detail(){
        pageHeader("စကားဝိုင်း #"+selected.id,PromptLibrary.CATEGORY[selected.category]);
        LinearLayout illustration=col();illustration.setGravity(Gravity.CENTER);illustration.setBackground(grad(CAT_BG[selected.category],0xFFECF5FF,21));padding(illustration,12,23,12,23);
        TextView emoji=tv(selected.category==0?"📚  ✍️":selected.category==1?"🎓  💼":"🌱  ♥",44,CAT_COLORS[selected.category],true);emoji.setGravity(Gravity.CENTER);illustration.addView(emoji);
        margin(illustration,body,0,0,0,18);
        TextView cat=pill(PromptLibrary.CATEGORY[selected.category],CAT_BG[selected.category],CAT_COLORS[selected.category],12);margin(cat,body,0,0,0,9);
        margin(tv(selected.title,25,INK,true),body,0,0,0,10);
        margin(tv(selected.description(),15,MUTED,false),body,0,0,0,14);
        LinearLayout box=card(WHITE);box.addView(tv("ဒီစကားဝိုင်းကို ဘာကြောင့်သုံးမလဲ?",16,INK,true));
        box.addView(tv("✓ အကြောင်းအရာကို ရှင်းလင်းစွာ နားလည်နိုင်ရန်\n✓ ကိုယ်ပိုင်အခြေအနေအလိုက် အကြံပြုချက်များရရန်\n✓ လက်တွေ့အသုံးချနိုင်ရန်",13,MUTED,false));
        margin(box,body,0,0,0,12);
        LinearLayout prompt=card(0xFFE9F2FF);prompt.addView(tv("🪄 အသင့်သုံး AI Prompt",16,INK,true));TextView content=tv(selected.prompt(),13,INK,false);padding(content,0,10,0,10);prompt.addView(content);
        margin(prompt,body,0,0,0,13);
        margin(button("ChatGPT Study Mode ဖြင့် စတင်ရန်  ↗",BLUE,WHITE,()->launchChatGPT(selected.prompt())),body,0,0,0,9);
        LinearLayout r=row();TextView cp=button("⧉  Prompt ကူးယူရန်",0xFFE2EDFF,NAVY,()->copy(selected.prompt()));r.addView(cp,new LinearLayout.LayoutParams(0,d(48),1));
        TextView save=button(saved.contains(selected.id)?"♥  သိမ်းပြီး":"♡  သိမ်းထားရန်",0xFFFFE6F0,CORAL,()->{toggleFavorite(selected.id);show("detail");});
        LinearLayout.LayoutParams b=new LinearLayout.LayoutParams(0,d(48),1);b.leftMargin=d(8);r.addView(save,b);margin(r,body,0,0,0,5);
        TextView draft=click("✎  Prompt ကို ပြင်ဆင်ပြီး သုံးရန်",13,BLUE,true,()->show("compose"));draft.setGravity(Gravity.CENTER);padding(draft,8,15,8,9);margin(draft,body,0,8,0,0);
        margin(tv("မှတ်ချက် — ChatGPT တွင် အဖြေမှားနိုင်သဖြင့် အရေးကြီးသောအချက်အလက်များကို ပြန်စစ်ဆေးပါ။",11,MUTED,false),body,0,10,0,0);
    }
    private void compose(){
        pageHeader("AI သင်ယူမှု စတင်ရန်","Prompt ပြင်ပြီး ChatGPT Study Mode သို့ ပို့ရန်");
        LinearLayout welcome=card(0xFFE7F2FF);welcome.addView(tv("🤖  AI လေ့လာရေးလက်ထောက်",19,NAVY,true));
        welcome.addView(tv("မေးခွန်းကို ပြင်ဆင်ပါ။ Prompt ကို ကူးယူပြီး ChatGPT Study Mode ဖြင့် ဆက်လက်လေ့လာနိုင်ပါတယ်။",13,INK,false));margin(welcome,body,0,0,0,14);
        margin(tv("သင်၏ Prompt",17,INK,true),body,0,0,0,9);
        EditText edit=new EditText(this);edit.setText(selected.prompt());edit.setTextSize(15);edit.setTextColor(INK);edit.setMinLines(8);edit.setGravity(Gravity.TOP);edit.setBackground(outline(WHITE,15,LINE));padding(edit,13,13,13,13);margin(edit,body,0,0,0,12);
        margin(button("📋  Prompt ကူးယူရန်",0xFFE0F2FF,BLUE,()->copy(edit.getText().toString())),body,0,0,0,10);
        margin(button("ChatGPT Study Mode ကို ဖွင့်ရန်   ↗",BLUE,WHITE,()->launchChatGPT(edit.getText().toString())),body,0,0,0,13);
        LinearLayout how=card(WHITE);how.addView(tv("အသုံးပြုပုံ",16,INK,true));
        how.addView(tv("၁။ စကားဝိုင်း Prompt ကို ပြင်ဆင်ပါ။\n၂။ အပေါ်က ခလုတ်နှိပ်၍ Prompt ကို ဖုန်း Clipboard ထဲ ကူးပါ။\n၃။ ChatGPT Study Mode ဖွင့်ပြီး Paste လုပ်ပါ။\n၄။ AI ၏ မေးခွန်းများကို ဖြေဆိုလေ့ကျင့်ပါ။",14,MUTED,false));margin(how,body,0,0,0,0);
    }
    private void favorites(){
        pageHeader("♡  သိမ်းထားသော စကားဝိုင်းများ",saved.size()+" ခု သိမ်းထားသည်");int count=0;
        for(PromptLibrary.Item item:PromptLibrary.ALL)if(saved.contains(item.id)){chatRow(item,body);count++;}
        if(count==0){LinearLayout t=card(WHITE);t.addView(tv("♡  စကားဝိုင်း မသိမ်းရသေးပါ",18,INK,true));t.addView(tv("AI စကားဝိုင်း ၁၀၀ ထဲက နှစ်သက်ရာ Prompt များကို ♥ နှိပ်ပြီး သိမ်းထားနိုင်ပါတယ်။",14,MUTED,false));margin(t,body,0,20,0,0);
            margin(button("စကားဝိုင်းများ ကြည့်ရန်",BLUE,WHITE,()->show("list")),body,0,15,0,0);}
    }
    private void courses(){
        pageHeader("AI သင်တန်းများ","OpenAI Academy Courses");
        LinearLayout header=card(0xFFE8F2FF);
        header.addView(tv("🎓  OpenAI Academy",23,NAVY,true));
        header.addView(tv("AI ကို အခြေခံမှ စတင်ပြီး လက်တွေ့အသုံးချရန် သင်ယူနိုင်သော တရားဝင်သင်တန်းများ",14,MUTED,false));
        margin(button("Academy သင်တန်းအားလုံး ဖွင့်ရန် ↗",BLUE,WHITE,()->openUrl(ACADEMY_URL)),header,0,12,0,0);
        margin(header,body,0,0,0,16);
        String[] names={"AI Foundations","Applied AI Foundations","Agents and Workflows"};
        String[] labels={"AI အခြေခံ","လက်တွေ့ AI အသုံးချခြင်း","AI Agent နှင့် Workflow များ"};
        String[] icons={"📘","🧩","🤖"};
        String[] notes={
            "AI နှင့် ChatGPT အခြေခံများ၊ Prompt ရေးသားခြင်း၊ AI အဖြေများကို စနစ်တကျ ပြန်စစ်ခြင်း",
            "နေ့စဉ်လေ့လာမှုနှင့် အလုပ်များတွင် AI ကို လက်တွေ့အသုံးချခြင်း၊ Workflow စီစဉ်ခြင်း",
            "AI Agents၊ လုပ်ငန်းစဉ်များ ချိတ်ဆက်ခြင်းနှင့် လုပ်ဆောင်ချက်များကို စစ်ဆေးခြင်း"
        };
        for(int i=0;i<names.length;i++){
            final int at=i;
            LinearLayout card=card(WHITE);
            card.addView(tv(icons[i]+"  "+names[i],19,INK,true));
            card.addView(tv(labels[i],15,BLUE,true));
            margin(tv(notes[i],13,MUTED,false),card,0,10,0,12);
            margin(button("တရားဝင် သင်တန်းဖွင့်ရန် ↗",BLUE,WHITE,()->{Intent courseIntent=new Intent(this,AcademyWebViewActivity.class);courseIntent.putExtra("target_url",ACADEMY_COURSES[at]);startActivity(courseIntent);}),card,0,0,0,0);
            margin(card,body,0,0,0,12);
        }
        LinearLayout warning=card(0xFFF1F4FA);
        warning.addView(tv("သတိပြုရန်",15,INK,true));
        warning.addView(tv("သင်တန်းဖွင့်ရန် အင်တာနက် လိုအပ်ပါသည်။ သင်တန်းအကြောင်းအရာနှင့် လက်မှတ်များကို OpenAI Academy ဝဘ်ဆိုက်ကသာ စီမံပါသည်။ ဤ App သည် OpenAI ၏ တရားဝင် App မဟုတ်ပါ။",12,MUTED,false));
        margin(warning,body,0,0,0,12);
    }
    private void profile(){
        pageHeader("ပိုမို / အကောင့်","Student AI Study Assistant V2.4");
        LinearLayout c=card(WHITE);c.addView(tv("🎓  Student AI",21,INK,true));
        c.addView(tv("မြန်မာကျောင်းသားများအတွက် AI စကားဝိုင်း ၁၀၀ နှင့် မိမိကိုယ်တိုင် သင်ယူနိုင်သည့် Native Android App",14,MUTED,false));margin(c,body,0,0,0,15);
        margin(button("📚  သင်ခန်းစာ၊ Quiz နှင့် Flashcards",BLUE,WHITE,()->startActivity(new Intent(this,MainActivity.class))),body,0,0,0,12);
        margin(button("📖  Offline မြန်မာစာဖတ်ခန်း",GREEN,WHITE,()->startActivity(new Intent(this,OfflineReaderActivity.class))),body,0,0,0,12);
        margin(button("🎓  OpenAI Academy သင်တန်းများ",BLUE,WHITE,()->show("courses")),body,0,0,0,12);
        margin(button("♡  သိမ်းထားသော Prompt များ",0xFFE8F2FF,NAVY,()->show("saved")),body,0,0,0,12);
        margin(button("ChatGPT Study Mode သို့ သွားရန်",0xFFE8F2FF,NAVY,()->openUrl(STUDY_URL)),body,0,0,0,12);
        LinearLayout privacy=card(WHITE);privacy.addView(tv("အသုံးပြုမှုနှင့် ကိုယ်ရေးလုံခြုံမှု",16,INK,true));
        privacy.addView(tv("• Prompt ၁၀၀ ကို အင်တာနက်မရှိလည်း ကြည့်နိုင်သည်။\n• သိမ်းထားသော Prompt ID များကို ဖုန်းထဲတွင်သာ သိမ်းထားသည်။\n• AI အဖြေများကို ဤ App က မထုတ်ပေးပါ။ ChatGPT တွင်သာ AI Chat ပြုလုပ်နိုင်သည်။\n• ဤ App သည် OpenAI ၏ တရားဝင် App မဟုတ်ပါ။",13,MUTED,false));margin(privacy,body,0,3,0,0);
        TextView foot=click("ရည်ညွှန်းချက် — OpenAI 100 Chats for College Students  ↗",12,BLUE,false,()->openUrl("https://chatgpt.com/use-cases/students/"));margin(foot,body,0,18,0,0);
    }
    private TextView button(String s,int color,int text,Runnable action){TextView t=tv(s,14,text,true);t.setGravity(Gravity.CENTER);t.setBackground(fill(color,15));padding(t,13,14,13,14);t.setOnClickListener(w->action.run());return t;}
    // Persistent, thumb-friendly phone footer, always outside the scrolling body.
    private void bottomBar(){
        LinearLayout wrapper=col();
        wrapper.setBackgroundColor(WHITE);
        View hairline=new View(this);
        hairline.setBackgroundColor(LINE);
        wrapper.addView(hairline,new LinearLayout.LayoutParams(-1,d(1)));
        LinearLayout bar=row();
        padding(bar,2,3,2,4);
        String[] titles={"ပင်မ","စကားဝိုင်း","AI ဆရာ","သင်တန်း","ပိုမို"};
        String[] icons={"⌂","▤","✦","▣","☰"};
        String[] routes={"home","list","tutor","courses","profile"};
        for(int i=0;i<routes.length;i++){
            final String route=routes[i];
            boolean active=page.equals(route)
                 ||(page.equals("detail")&&route.equals(returnPage))
                 ||(page.equals("compose")&&route.equals("list"));
            LinearLayout tab=col();
            tab.setGravity(Gravity.CENTER);
            padding(tab,1,5,1,3);
            TextView glyph=tv(icons[i],22,active?BLUE:MUTED,true);
            glyph.setGravity(Gravity.CENTER);
            if(i==2) { glyph.setTextColor(WHITE); glyph.setBackground(fill(BLUE,16)); padding(glyph,10,2,10,2); }
            tab.addView(glyph);
            TextView label=tv(titles[i],10,active?BLUE:MUTED,active);
            label.setGravity(Gravity.CENTER);
            tab.addView(label);
            tab.setContentDescription(titles[i]);
            tab.setClickable(true);
            tab.setFocusable(true);
            tab.setOnClickListener(v->{
                if("tutor".equals(route)){
                    Intent intent=new Intent(this,NativeChatActivity.class);
                    startActivity(intent);
                }else if("courses".equals(route)){
                    startActivity(new Intent(this,AcademyWebViewActivity.class));
                }else {
                    if("list".equals(route)) filter=-1;
                    show(route);
                }
            });
            bar.addView(tab,new LinearLayout.LayoutParams(0,d(61),1));
        }
        wrapper.addView(bar,new LinearLayout.LayoutParams(-1,d(68)));
        root.addView(wrapper,new LinearLayout.LayoutParams(-1,-2));
    }
    private void toggleFavorite(int id){if(saved.contains(id))saved.remove(id);else saved.add(id);StringBuilder b=new StringBuilder();for(Integer n:saved)b.append(n).append(',');prefs.edit().putString("favorites",b.toString()).apply();}
    private void copy(String s){ClipboardManager manager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);manager.setPrimaryClip(ClipData.newPlainText("Student AI Prompt",s));Toast.makeText(this,"Prompt ကို ကူးယူထားပါပြီ",Toast.LENGTH_SHORT).show();}
    private void launchChatGPT(String prompt){copy(prompt);openUrl(STUDY_URL);}
    private void openUrl(String url){try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));}catch(Exception x){Toast.makeText(this,"Browser မတွေ့ပါ။ ChatGPT ကို Browser တွင် ဖွင့်ပါ။",Toast.LENGTH_LONG).show();}}
    private void back(){if(page.equals("compose")){show("detail");return;}if(page.equals("detail")){show(returnPage);return;}show("home");}
    @Override public void onBackPressed(){if(page.equals("home"))super.onBackPressed();else back();}
}
