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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Locale;

/**
 * Teacher Rent V2.9 — LOCAL prototype only.
 * Sample tutor profiles are clearly marked DEMO; requests and registrations are
 * stored only on this device and are never transmitted, paid for or confirmed.
 */
public final class TeacherRentActivity extends Activity {
    private static final int NAVY=0xFF102858,BLUE=0xFF175FE5,INK=0xFF1B3157,
        MUTED=0xFF647493,BG=0xFFF3F7FD,WHITE=Color.WHITE,LINE=0xFFDDE6F4,
        GREEN=0xFF08775F,ORANGE=0xFFAE6417;
    private SharedPreferences prefs;
    private LinearLayout root,body,teacherList;
    private EditText searchBox;
    private Spinner levelFilter,subjectFilter,formatFilter;
    private int screen=0;  // 0 find, 1 requests, 2 register
    private String searchTerm="";
    private final ArrayList<JSONObject> items=new ArrayList<>();
    private final ArrayList<String> subjects=new ArrayList<>();

    private int dp(float v){return (int)(getResources().getDisplayMetrics().density*v+.5f);}
    private GradientDrawable shape(int color,int radius,boolean outline){
        GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));
        if(outline)d.setStroke(dp(1),LINE);
        return d;
    }
    private LinearLayout col(){LinearLayout x=new LinearLayout(this);x.setOrientation(1);return x;}
    private LinearLayout row(){LinearLayout x=new LinearLayout(this);x.setGravity(Gravity.CENTER_VERTICAL);return x;}
    private TextView tv(String s,int size,int color,boolean strong){
        TextView t=new TextView(this);t.setText(s);t.setTextColor(color);t.setTextSize(size);
        t.setLineSpacing(dp(2),1.04f);if(strong)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        return t;
    }
    private void pad(View x,int l,int t,int r,int b){x.setPadding(dp(l),dp(t),dp(r),dp(b));}
    private void margin(LinearLayout host,View child,int top,int bottom){
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);
        p.topMargin=dp(top);p.bottomMargin=dp(bottom);host.addView(child,p);
    }
    private TextView action(String name,boolean primary,Runnable run){
        TextView v=tv(name,13,primary?WHITE:BLUE,true);v.setGravity(Gravity.CENTER);
        pad(v,10,12,10,12);v.setBackground(shape(primary?BLUE:0xFFE8F1FF,12,false));
        v.setOnClickListener(view->run.run());return v;
    }
    private LinearLayout card(){LinearLayout c=col();pad(c,14,14,14,14);
        c.setBackground(shape(WHITE,17,true));return c;}
    private Spinner spinner(String[] labels){
        Spinner sp=new Spinner(this);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);sp.setBackground(shape(WHITE,12,true));pad(sp,7,7,7,7);return sp;
    }
    private EditText field(String hint,boolean multi){
        EditText e=new EditText(this);e.setTextSize(15);e.setTextColor(INK);
        e.setHintTextColor(MUTED);e.setHint(hint);
        e.setSingleLine(!multi);if(multi)e.setMinLines(2);
        e.setBackground(shape(WHITE,13,true));pad(e,13,10,13,10);return e;
    }
    private JSONObject teacher(String name,String level,String subject,String mode,String bio,boolean sample){
        JSONObject t=new JSONObject();
        try{t.put("name",name);t.put("level",level);t.put("subject",subject);
            t.put("mode",mode);t.put("bio",bio);t.put("demo",sample);
        }catch(Exception ignored){}return t;
    }
    private JSONArray saved(String key){
        try{return new JSONArray(prefs.getString(key,"[]"));}catch(Exception e){return new JSONArray();}
    }
    private void store(String key,JSONArray array){prefs.edit().putString(key,array.toString()).apply();}
    private void loadTeachers(){
        items.clear();
        items.add(teacher("Mathematics Tutor · DEMO","O Level","Mathematics","Online",
            "Algebra, Geometry နှင့် Exam Practice · နမူနာ Profile သာဖြစ်သည်",true));
        items.add(teacher("Physics Tutor · DEMO","A Level","Physics","Online",
            "Mechanics, Waves နှင့် Problems · နမူနာ Profile သာဖြစ်သည်",true));
        items.add(teacher("English Tutor · DEMO","Both","English Language","Online",
            "Reading, Writing, Exam Techniques · နမူနာ Profile သာဖြစ်သည်",true));
        items.add(teacher("Chemistry Tutor · DEMO","Both","Chemistry","In-person",
            "Chemistry Calculations နှင့် Concepts · နမူနာ Profile သာဖြစ်သည်",true));
        JSONArray own=saved("teachers");
        for(int i=0;i<own.length();i++){
            JSONObject x=own.optJSONObject(i);if(x!=null)items.add(x);
        }
    }
    private String[] subjectNames(){
        LinkedHashSet<String> unique=new LinkedHashSet<>();unique.add("အားလုံး");
        for(GceCurriculum.Subject s:GceCurriculum.SUBJECTS)unique.add(s.name);
        return unique.toArray(new String[0]);
    }

    @Override public void onCreate(Bundle state){
        super.onCreate(state);prefs=getSharedPreferences("teacher_rent_local_v29",MODE_PRIVATE);
        getWindow().setStatusBarColor(BG);getWindow().setNavigationBarColor(WHITE);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        loadTeachers();render();
    }
    private void render(){
        root=col();root.setBackgroundColor(BG);setContentView(root);
        if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,insets)->{
            android.graphics.Insets bars=insets.getInsets(WindowInsets.Type.systemBars());
            v.setPadding(bars.left,bars.top,bars.right,bars.bottom);return insets;});
        LinearLayout toolbar=row();pad(toolbar,12,12,12,12);toolbar.setBackgroundColor(WHITE);
        TextView back=action("‹",false,()->go("home"));toolbar.addView(back,new LinearLayout.LayoutParams(dp(43),dp(44)));
        LinearLayout name=col();pad(name,10,0,0,0);name.addView(tv("🎓 TEACHER RENT",19,NAVY,true));
        name.addView(tv("GCE O/A Level · ဆရာ/ဆရာမ ရှာရန်",11,MUTED,false));
        toolbar.addView(name,new LinearLayout.LayoutParams(0,-2,1));root.addView(toolbar);
        ScrollView scroll=new ScrollView(this);scroll.setFillViewport(false);
        body=col();pad(body,15,15,15,20);scroll.addView(body);
        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        tabs();
        if(screen==0)findTeachers();else if(screen==1)requests();else registration();
        footer();
    }
    private void tabs(){
        LinearLayout tabs=row();
        String[] titles={"🔎 ဆရာရှာ","📋 Request","＋ ဆရာစာရင်း"};
        for(int i=0;i<3;i++){
            final int index=i;
            TextView t=tv(titles[i],12,i==screen?WHITE:BLUE,true);t.setGravity(Gravity.CENTER);
            pad(t,3,13,3,13);t.setBackground(shape(i==screen?BLUE:WHITE,13,true));
            t.setOnClickListener(v->{screen=index;render();});
            LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-2,1);
            if(i>0)p.leftMargin=dp(5);tabs.addView(t,p);
        }
        margin(body,tabs,0,13);
        LinearLayout warning=card();
        warning.addView(tv("DEMO / LOCAL ONLY",13,ORANGE,true));
        margin(warning,tv("အမှန်တကယ် အတည်ပြုထားသော ဆရာစာရင်း မဟုတ်ပါ။ Request နှင့် Teacher Registration တို့ကို ဤဖုန်းတွင်သာ သိမ်းပြီး Server သို့ မပို့သေးပါ။ Payment / Confirmed Booking မပါဝင်သေးပါ။",12,MUTED,false),6,0);
        margin(body,warning,0,16);
    }
    private void findTeachers(){
        margin(body,tv("ဆရာ/ဆရာမ ရွေးချယ်ရန်",21,NAVY,true),0,8);
        searchBox=field("ဆရာအမည် / Subject ရှာပါ",false);searchBox.setText(searchTerm);margin(body,searchBox,0,8);
        levelFilter=spinner(new String[]{"အားလုံး","O Level","A Level"});
        subjectFilter=spinner(subjectNames());
        formatFilter=spinner(new String[]{"အားလုံး","Online","In-person"});
        margin(body,tv("Level",12,MUTED,true),7,2);margin(body,levelFilter,0,5);
        margin(body,tv("Subject",12,MUTED,true),5,2);margin(body,subjectFilter,0,5);
        margin(body,tv("သင်ကြားရေးပုံစံ",12,MUTED,true),5,2);margin(body,formatFilter,0,8);
        margin(body,action("🔎  Filter အသုံးပြုရန်",true,()->filter()),2,15);
        teacherList=col();body.addView(teacherList);filter();
        searchBox.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s,int start,int count,int after){}
            public void onTextChanged(CharSequence s,int start,int before,int count){
                searchTerm=s.toString();filter();
            }
            public void afterTextChanged(Editable e){}
        });
    }
    private void filter(){
        if(teacherList==null||levelFilter==null)return;
        teacherList.removeAllViews();
        String level=levelFilter.getSelectedItem().toString();
        String subject=subjectFilter.getSelectedItem().toString();
        String mode=formatFilter.getSelectedItem().toString();
        String search=searchTerm.trim().toLowerCase(Locale.ROOT);
        int shown=0;
        for(JSONObject t:items){
            String l=t.optString("level"),s=t.optString("subject"),m=t.optString("mode"),n=t.optString("name");
            if(!"အားလုံး".equals(level)&&!level.equals(l)&&!"Both".equals(l))continue;
            if(!"အားလုံး".equals(subject)&&!subject.equalsIgnoreCase(s))continue;
            if(!"အားလုံး".equals(mode)&&!mode.equals(m))continue;
            if(!search.isEmpty()&&!((n+" "+s+" "+t.optString("bio")).toLowerCase(Locale.ROOT).contains(search)))continue;
            LinearLayout c=card();
            c.addView(tv("👩‍🏫  "+n,17,NAVY,true));
            margin(c,tv(l+" · "+s+" · "+m,12,BLUE,true),6,6);
            margin(c,tv(t.optString("bio"),13,MUTED,false),0,6);
            margin(c,tv(t.optBoolean("demo")?"နမူနာ Profile · ဆရာအစစ် မဟုတ်ပါ":"ဤဖုန်းမှ ထည့်ထားသော Profile · အတည်မပြုရသေးပါ",11,ORANGE,false),0,8);
            margin(c,action("📝  Booking Request ပြုလုပ်ရန်",true,()->requestForm(t)),5,0);
            margin(teacherList,c,0,10);shown++;
        }
        if(shown==0)margin(teacherList,tv("Filter နှင့် ကိုက်ညီသော Profile မတွေ့ပါ။ ကိုယ်တိုင် Teacher Profile စာရင်းသွင်းနိုင်ပါသည်။",14,MUTED,false),10,8);
    }
    private void requestForm(JSONObject teacher){
        LinearLayout form=col();pad(form,16,6,16,4);
        String chosen=teacher.optString("name");
        margin(form,tv("ရွေးထားသော Profile: "+chosen,13,BLUE,true),0,8);
        EditText student=field("ကျောင်းသားအမည် / အမည်ပြောင်",false);
        EditText when=field("သင်ယူလိုသည့်နေ့နှင့် အချိန် (ဥပမာ Sat 10:00)",false);
        EditText notes=field("သင်ခန်းစာ / Learning Goal (လိုအပ်မှ)",true);
        margin(form,student,0,8);margin(form,when,0,8);margin(form,notes,0,5);
        new AlertDialog.Builder(this).setTitle("Teacher Booking Request · Demo")
            .setView(form).setPositiveButton("ဖုန်းထဲ သိမ်းမည်",(dialog,which)->{
                String studentName=student.getText().toString().trim(),schedule=when.getText().toString().trim();
                if(studentName.isEmpty()||schedule.isEmpty()){
                    Toast.makeText(this,"အမည်နှင့် အချိန် ထည့်ရပါမည်။",Toast.LENGTH_LONG).show();requestForm(teacher);return;
                }
                JSONObject req=new JSONObject();
                try{
                    req.put("teacher",chosen);req.put("learner",studentName);
                    req.put("schedule",schedule);req.put("notes",notes.getText().toString().trim());
                    req.put("subject",teacher.optString("subject"));req.put("level",teacher.optString("level"));
                    req.put("mode",teacher.optString("mode"));req.put("created",System.currentTimeMillis());
                    req.put("status","LOCAL DRAFT · NOT SENT");
                }catch(Exception ignored){}
                JSONArray array=saved("requests");array.put(req);store("requests",array);
                Toast.makeText(this,"Request ကို ဖုန်းထဲ သိမ်းပြီးပါပြီ။ ဆရာထံ မပို့ရသေးပါ။",Toast.LENGTH_LONG).show();
                screen=1;render();
            }).setNegativeButton("ပယ်ဖျက်",null).show();
    }
    private void requests(){
        JSONArray requests=saved("requests");
        margin(body,tv("သိမ်းထားသော Requests · "+requests.length(),19,NAVY,true),0,8);
        margin(body,tv("Request တစ်ခုမှ Teacher ထံ မပို့ထားသေးပါ။ Server နှင့် Verification စနစ်ချိတ်မှ အမှန်တကယ် Booking ပြုလုပ်နိုင်ပါမည်။",12,MUTED,false),0,10);
        if(requests.length()==0)margin(body,tv("Booking Request မရှိသေးပါ။ 'ဆရာရှာ' Menu မှ စတင်နိုင်ပါတယ်။",14,MUTED,false),10,10);
        for(int i=0;i<requests.length();i++){
            JSONObject x=requests.optJSONObject(i);if(x==null)continue;
            final int position=i;
            LinearLayout c=card();
            c.addView(tv("🗓  "+x.optString("teacher"),16,NAVY,true));
            margin(c,tv("Learner: "+x.optString("learner")+"\n"+x.optString("level")+" · "+x.optString("subject")+"\nအချိန်: "+x.optString("schedule")+"\n"+x.optString("notes"),13,MUTED,false),8,6);
            margin(c,tv("● LOCAL DRAFT — NOT SENT",12,ORANGE,true),5,9);
            margin(c,action("Request ဖျက်ရန်",false,()->deleteRequest(position)),0,0);
            margin(body,c,0,11);
        }
        margin(body,action("＋  Request အသစ်ပြုလုပ်ရန်",true,()->{screen=0;render();}),12,0);
    }
    private void deleteRequest(int position){
        new AlertDialog.Builder(this).setTitle("Request ဖျက်မလား?")
          .setPositiveButton("ဖျက်မည်",(dialog,which)->{
              JSONArray old=saved("requests"),list=new JSONArray();
              for(int i=0;i<old.length();i++)if(i!=position)list.put(old.opt(i));
              store("requests",list);render();
          }).setNegativeButton("မဖျက်ပါ",null).show();
    }
    private void registration(){
        margin(body,tv("Teacher Registration · Local Demo",19,NAVY,true),0,6);
        margin(body,tv("ဤဖုန်းမှာသာ Profile သိမ်းမည်။ Public Teacher Marketplace သို့ မတင်ပါ။ Qualification နှင့် Identity ကို မစစ်ဆေးရသေးပါ။",13,MUTED,false),0,13);
        EditText name=field("Teacher အမည် / Display Name",false);
        Spinner level=spinner(new String[]{"O Level","A Level","Both"});
        Spinner subject=spinner(subjectNames());
        Spinner mode=spinner(new String[]{"Online","In-person"});
        EditText bio=field("သင်ကြားမည့်အကြောင်း၊ အတွေ့အကြုံ (မဖြစ်မနေမဟုတ်)",true);
        margin(body,name,0,9);margin(body,level,0,9);margin(body,subject,0,9);margin(body,mode,0,9);margin(body,bio,0,10);
        margin(body,action("✓ Teacher Profile ကို ဖုန်းထဲသိမ်းမည်",true,()->{
            if(name.getText().toString().trim().isEmpty()||subject.getSelectedItemPosition()==0){
                Toast.makeText(this,"Teacher အမည်နှင့် Subject ရွေးပါ",Toast.LENGTH_LONG).show();return;
            }
            JSONObject t=teacher(name.getText().toString().trim(),
                level.getSelectedItem().toString(),subject.getSelectedItem().toString(),
                mode.getSelectedItem().toString(),bio.getText().toString().trim(),false);
            JSONArray a=saved("teachers");a.put(t);store("teachers",a);loadTeachers();
            Toast.makeText(this,"Profile ကို ဤဖုန်းတွင် သိမ်းထားပါပြီ။ Online မတင်ရသေးပါ။",Toast.LENGTH_LONG).show();
            screen=0;render();
        }),0,10);
        margin(body,tv("Teacher တွေ အမှန်တကယ် ငှားရမ်းလို့ရဖို့ နောက်ဆက်တွဲ Server၊ Teacher Verification၊ Secure Messaging၊ Booking Approval နဲ့ Payment System လိုပါမည်။",12,MUTED,false),5,15);
    }
    private void footer(){
        LinearLayout bar=row();bar.setBackgroundColor(WHITE);pad(bar,2,5,2,6);
        String[] labels={"ပင်မ","စကားဝိုင်း","AI ဆရာ","သင်တန်း","ဆရာငှား","ပိုမို"};
        String[] icons={"⌂","▤","✦","▣","♙","☰"};
        for(int i=0;i<labels.length;i++){
            final int index=i;LinearLayout cell=col();cell.setGravity(Gravity.CENTER);
            TextView icon=tv(icons[i],21,i==4?BLUE:MUTED,true);icon.setGravity(Gravity.CENTER);cell.addView(icon);
            TextView label=tv(labels[i],10,i==4?BLUE:MUTED,i==4);label.setGravity(Gravity.CENTER);cell.addView(label);
            cell.setContentDescription(labels[i]);cell.setOnClickListener(v->navigate(index));
            bar.addView(cell,new LinearLayout.LayoutParams(0,dp(58),1));
        }
        root.addView(bar,new LinearLayout.LayoutParams(-1,-2));
    }
    private void navigate(int index){
        if(index==4){screen=0;render();return;}
        if(index==3){AcademyBrowser.openCourses(this);return;}
        if(index==2){startActivity(new Intent(this,NativeChatActivity.class));return;}
        if(index==0){go("home");return;}
        if(index==1){go("list");return;}
        go("profile");
    }
    private void go(String page){Intent i=new Intent(this,CatalogActivity.class);i.putExtra("open_page",page);startActivity(i);finish();}
    @Override public void onBackPressed(){go("home");}
}
