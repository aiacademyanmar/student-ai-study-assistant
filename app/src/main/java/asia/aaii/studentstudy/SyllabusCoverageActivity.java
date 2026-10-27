package asia.aaii.studentstudy;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.WindowInsets;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStream;

/**
 * V2.15: offline coverage crosswalk for the app-created O Level subtopic paths.
 *
 * Crucial distinction:
 * - App targets are NOT exact Cambridge official learning outcomes.
 * - A learner may record where they checked a clause/page in the official PDF.
 * - Checking "PDF reviewed" is a user self-report, not content-provider verification.
 *
 * No server or teacher verification is implied. All records stay on-device.
 */
public final class SyllabusCoverageActivity extends Activity {
    private final int NAVY=0xFF16315A, BLUE=0xFF1763E8, MUTED=0xFF677995,
        BG=0xFFF2F6FD, WHITE=Color.WHITE, GREEN=0xFF0E8365, ORANGE=0xFFA76713;
    private int subject=-1,topic=-1,subtopic=-1,lesson=0;
    private SharedPreferences prefs;
    private OutcomeCrosswalkStore crosswalk;
    private LinearLayout root,body;
    private boolean onlyPending=false;
    private int dp(float x){return (int)(getResources().getDisplayMetrics().density*x+.5f);}
    private GradientDrawable shape(int color,int radius){
        GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;
    }
    private LinearLayout column(){LinearLayout v=new LinearLayout(this);v.setOrientation(1);return v;}
    private LinearLayout row(){LinearLayout v=new LinearLayout(this);v.setOrientation(0);v.setGravity(Gravity.CENTER_VERTICAL);return v;}
    private TextView text(String s,int size,int color,boolean bold){
        TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);
        t.setLineSpacing(dp(3),1.08f);if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;
    }
    private void add(LinearLayout host,View item,int margin){
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.topMargin=dp(margin);host.addView(item,p);
    }
    private TextView button(String value,Runnable action,boolean primary){
        TextView v=text(value,14,primary?WHITE:NAVY,true);
        v.setBackground(shape(primary?BLUE:WHITE,14));v.setPadding(dp(13),dp(14),dp(13),dp(14));
        v.setOnClickListener(view->action.run());return v;
    }
    private void toast(String message){Toast.makeText(this,message,Toast.LENGTH_SHORT).show();}
    private OLevel2026.Subject sub(){return OLevel2026.SUBJECTS[subject];}
    private OLevel2026.Topic top(){return sub().topics[topic];}
    private String key(int si,int ti,int xi,String attribute){
        return "c_"+OLevel2026.SUBJECTS[si].code+"_"+ti+"_"+xi+"_"+attribute;
    }
    private boolean marked(int si,int ti,int xi,String attr){return prefs.getBoolean(key(si,ti,xi,attr),false);}
    private void mark(String attr,boolean flag){
        prefs.edit().putBoolean(key(subject,topic,subtopic,attr),flag).apply();draw();
    }
    private String evidence(int si,int ti,int xi){return prefs.getString(key(si,ti,xi,"reference"),"");}
    private int[] stats(int si,int ti) {
        int total=0,studied=0,practiced=0,exam=0,source=0;
        OLevel2026.Subject item=OLevel2026.SUBJECTS[si];
        int start=ti<0?0:ti, end=ti<0?item.topics.length:ti+1;
        for(int t=start;t<end;t++)for(int x=0;x<item.topics[t].subtopics.length;x++){
            total++;
            if(marked(si,t,x,"study"))studied++;
            if(marked(si,t,x,"practice"))practiced++;
            if(marked(si,t,x,"exam"))exam++;
            if(!evidence(si,t,x).trim().isEmpty())source++;
        }
        return new int[]{total,studied,practiced,exam,source};
    }
    private String percent(int k,int total){return total==0?"0%":((int)Math.round(k*100.0/total))+"%";}
    @Override public void onCreate(Bundle b){
        super.onCreate(b);prefs=getSharedPreferences("o_level_coverage_v215",MODE_PRIVATE);crosswalk=new OutcomeCrosswalkStore(this);
        subject=getIntent().getIntExtra("subject_index",-1);
        topic=getIntent().getIntExtra("topic_index",-1);
        subtopic=getIntent().getIntExtra("subtopic_index",-1);
        if(subject<0||subject>=OLevel2026.SUBJECTS.length){subject=-1;topic=-1;subtopic=-1;}
        else if(topic<0||topic>=sub().topics.length){topic=-1;subtopic=-1;}
        else if(subtopic<0||subtopic>=top().subtopics.length)subtopic=-1;
        draw();
    }
    private void draw(){
        root=column();root.setBackgroundColor(BG);setContentView(root);
        getWindow().setStatusBarColor(WHITE);getWindow().setNavigationBarColor(WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,insets)->{
            android.graphics.Insets i=insets.getInsets(WindowInsets.Type.systemBars());
            v.setPadding(i.left,i.top,i.right,i.bottom);return insets;
        });
        LinearLayout header=row();header.setBackgroundColor(WHITE);header.setPadding(dp(12),dp(12),dp(12),dp(12));
        TextView back=text("‹",30,BLUE,true);back.setGravity(Gravity.CENTER);
        header.addView(back,new LinearLayout.LayoutParams(dp(48),dp(42)));
        back.setOnClickListener(v->back());
        LinearLayout headerText=column();
        headerText.addView(text("✅ SYLLABUS COVERAGE",19,NAVY,true));
        headerText.addView(text("Official PDF cross-check · Student progress",11,MUTED,false));
        header.addView(headerText,new LinearLayout.LayoutParams(0,-2,1));
        root.addView(header);

        ScrollView scroll=new ScrollView(this);scroll.setFillViewport(false);
        body=column();body.setPadding(dp(15),dp(15),dp(15),dp(30));
        scroll.addView(body);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        if(subject<0)subjectIndex();
        else if(topic<0)topicIndex();
        else if(subtopic<0)subtopicIndex();
        else detail();

        TextView foot=text("📘 Original app targets · Official PDF clauses must be checked",11,MUTED,false);
        foot.setGravity(Gravity.CENTER);foot.setPadding(dp(9),dp(10),dp(9),dp(14));foot.setBackgroundColor(WHITE);
        root.addView(foot);
    }
    private void warning(){
        LinearLayout box=column();box.setPadding(dp(14),dp(13),dp(14),dp(13));box.setBackground(shape(0xFFFFF3DF,14));
        add(box,text("⚠  မဖြစ်မနေသိရန်",14,ORANGE,true),0);
        add(box,text("ဒီ Checklist က App ဖန်တီးထားသော Learning Subtopic များကို Track လုပ်ပါတယ်။ "+
            "Cambridge PDF ထဲက Official Learning Outcome စာကြောင်းတိုင်းနှင့် တစ်ချက်ချင်း အတည်ပြုထားတဲ့ Crosswalk မဟုတ်သေးပါ။ "+
            "PDF Page/Clause ရေးသိမ်းခြင်းသည် User ကိုယ်တိုင်မှတ်တမ်းဖြစ်ပြီး Cambridge အတည်ပြုချက်မဟုတ်ပါ။",12,NAVY,false),7);
        add(body,box,0);
    }
    private void subjectIndex(){
        add(body,text("O LEVEL · 2026 Coverage",22,NAVY,true),3);
        warning();
        int total=0,learned=0,practice=0,exam=0,mapped=0;
        for(int i=0;i<OLevel2026.SUBJECTS.length;i++){
            int[] a=stats(i,-1);
            total+=a[0];learned+=a[1];practice+=a[2];exam+=a[3];mapped+=a[4];
        }
        add(body,text("App Study Subtopics: "+total+"\n"+
            "သင်ပြီး "+learned+" ("+percent(learned,total)+") · Practice "+practice+
            " · Exam "+exam+"\nPDF Page/Clause မှတ်တမ်း "+mapped+" ခု",14,NAVY,true),15);
        int maps=0,matches=0,gaps=0;
        for(OLevel2026.Subject item:OLevel2026.SUBJECTS){int[] x=crosswalk.counts(item,-1);maps+=x[0];matches+=x[1];gaps+=x[2];}
        add(body,text("Crosswalk references: "+maps+" · user-match "+matches+" · gaps "+gaps,13,BLUE,true),12);
        add(body,button("📤 Manual Crosswalk JSON Export",()->exportCrosswalk(),false),9);
        add(body,button(onlyPending?"✓ Filter ပိတ်ပြီး အားလုံးပြမည်":"◷ မပြီးသေးသည့်ဘာသာများသာ ပြမည်",()->{
            onlyPending=!onlyPending;draw();
        },false),13);
        for(int i=0;i<OLevel2026.SUBJECTS.length;i++){
            int[] a=stats(i,-1);
            if(onlyPending&&a[0]==a[1]&&a[0]>0)continue;
            final int which=i;OLevel2026.Subject item=OLevel2026.SUBJECTS[i];
            add(body,button("📚 "+item.name+" · "+item.code+"\n"+
                "Study "+a[1]+"/"+a[0]+" ("+percent(a[1],a[0])+
                ") · PDF evidence "+a[4]+"\n"+
                (item.officialTopicHeadings?"✓ Top-level headings reviewed":"◷ Topic audit pending"),
                ()->{subject=which;draw();},false),10);
        }
    }
    private void topicIndex(){
        OLevel2026.Subject s=sub();
        add(body,text(s.name+" ("+s.code+")",22,NAVY,true),2);
        add(body,text("2026 Exam · Syllabus reference period "+s.years,12,MUTED,false),6);
        add(body,text("Topic headings: "+(s.officialTopicHeadings?"cross-check recorded":"verification pending")+
            "\nLearning outcome clauses: mapping still requires manual PDF checking",13,ORANGE,true),11);
        add(body,button("🌐 Cambridge subject page ဖွင့်ရန်",()->open(s.officialUrl),false),12);
        Cambridge2026Audit.Source doc=Cambridge2026Audit.byCode(s.code);
        if(doc!=null)add(body,button("📄 PDF Syllabus ဖွင့်ရန်",()->open(doc.pdfUrl),false),8);
        for(int i=0;i<s.topics.length;i++){
            final int j=i;OLevel2026.Topic t=s.topics[i];int[] k=stats(subject,i);
            add(body,button("Topic "+(i+1)+": "+t.name+"\n"+
                "Study "+k[1]+"/"+k[0]+" · Practice "+k[2]+" · Exam "+k[3]+" · PDF notes "+k[4],
                ()->{topic=j;draw();},false),9);
        }
    }
    private void subtopicIndex(){
        add(body,text(top().name,21,NAVY,true),3);
        int[] k=stats(subject,topic);
        add(body,text("Studied "+k[1]+"/"+k[0]+" · Practiced "+k[2]+" · Exam "+k[3]+
            " · PDF-linked notes "+k[4],13,MUTED,false),8);
        add(body,button(onlyPending?"All Subtopics":"Show Incomplete Study",()->{onlyPending=!onlyPending;draw();},false),13);
        for(int i=0;i<top().subtopics.length;i++){
            final int j=i;
            boolean done=marked(subject,topic,i,"study");
            if(onlyPending&&done)continue;
            String detail=evidence(subject,topic,i);
            add(body,button((done?"✓ ":"○ ")+top().subtopics[i]+"\n"+
                "Study "+(done?"✓":"–")+"  Practice "+(marked(subject,topic,i,"practice")?"✓":"–")+
                "  Exam "+(marked(subject,topic,i,"exam")?"✓":"–")+
                "  PDF "+(detail.isEmpty()?"unmapped":"note recorded"),
                ()->{subtopic=j;draw();},false),9);
        }
    }
    private void detail(){
        OLevel2026.Subject s=sub();OLevel2026.Topic t=top();
        add(body,text(t.subtopics[subtopic],21,NAVY,true),1);
        add(body,text(s.name+" · "+s.code+" > "+t.name,13,MUTED,false),7);
        warning();
        add(body,text(OLevelLearningOutcomes.contentOutcome(s,t,subtopic),14,NAVY,false),13);
        add(body,text("LEARNING STATUS",16,BLUE,true),16);
        String[] keys={"study","practice","exam"};
        String[] names={"📖 Lesson Study","✍ Practice & Answers","📝 Exam Practice"};
        for(int i=0;i<keys.length;i++){
            String key=keys[i],name=names[i];
            add(body,button((marked(subject,topic,subtopic,key)?"✓ ":"○ ")+name+
                " — "+(marked(subject,topic,subtopic,key)?"ပြီး":"မပြီး"),
                ()->mark(key,!marked(subject,topic,subtopic,key)),false),8);
        }
        add(body,text("OFFICIAL PDF CROSSWALK · MULTIPLE REFERENCES",16,BLUE,true),22);
        drawCrosswalkRows();
        String existing=evidence(subject,topic,subtopic);
        add(body,text(existing.isEmpty()?
            "မသတ်မှတ်ရသေး — Official PDF မှ Page/Clause ကို ကိုယ်တိုင်ရှာပြီး ထည့်ပါ။":
            "Recorded PDF page/clause: "+existing,13,existing.isEmpty()?ORANGE:GREEN,false),8);
        add(body,button("✎ PDF Page / Clause မှတ်သားရန်",()->editReference(),false),9);
        add(body,button("🌐 Cambridge Syllabus ကို စစ်ရန်",()->{
            Cambridge2026Audit.Source audit=Cambridge2026Audit.byCode(s.code);
            open(audit!=null?audit.pdfUrl:s.officialUrl);
        },false),9);
        add(body,text("AI LEARNING & ASSESSMENT",16,BLUE,true),20);
        for(int i=0;i<OLevel2026.LESSON_STAGES.length;i++){
            final int index=i;
            add(body,button("Lesson "+(i+1)+": "+OLevel2026.LESSON_STAGES[i],
                ()->openLesson(index),false),9);
        }
        add(body,button("🤖 AI သင်ခန်းစာရှင်းပြရန်",()->route("teach"),true),13);
        add(body,button("✍ AI Practice & Answers",()->route("practice"),false),9);
        add(body,button("📝 AI Exam Practice",()->route("exam"),false),9);
        add(body,button("⭐ Exam Tips / Summary",()->route("revision"),false),9);
        add(body,button("📋 Teaching Prompt Copy",()->copy(
            OLevelLearningOutcomes.lessonPrompt(s,t,subtopic,lesson,"teach")),false),9);
        add(body,text("အမှန်တကယ် စာမေးပွဲအတွက် Cambridge က ထုတ်ထားသော Official Syllabus PDF နှင့် "+
            "Learning Outcome/Assessment Objective များကို သီးခြားအတည်ပြုပါ။",12,MUTED,false),15);
    }
    private void drawCrosswalkRows(){
        OLevel2026.Subject subjectObj=sub();OLevel2026.Topic topicObj=top();
        JSONArray rows=crosswalk.get(subjectObj,topicObj,topicObj.subtopics[subtopic]);
        add(body,text("Manual entries: "+rows.length()+" · "+
            OutcomeCrosswalkStore.statusLabel(OutcomeCrosswalkStore.PENDING)+
            ". No content provider verification.",12,ORANGE,false),7);
        add(body,button("＋ Official Outcome / PDF Clause Reference ထည့်ရန်",
            ()->editCrosswalk(-1),true),9);
        for(int i=0;i<rows.length();i++){
            JSONObject record=rows.optJSONObject(i);if(record==null)continue;
            final int position=i;
            String status=record.optString("status",OutcomeCrosswalkStore.PENDING);
            add(body,button("["+(i+1)+"] "+OutcomeCrosswalkStore.statusLabel(status)+
                "\nPDF: "+record.optString("reference","not specified")+
                "\nOutcome: "+record.optString("summary","not specified")+
                "\nReviewer: "+record.optString("reviewer","unknown")+
                "\n✎ Edit / Delete",()->editCrosswalk(position),false),9);
        }
    }
    private void editCrosswalk(int position){
        OLevel2026.Subject selectedSubject=sub();OLevel2026.Topic selectedTopic=top();
        String subname=selectedTopic.subtopics[subtopic];
        JSONArray rows=crosswalk.get(selectedSubject,selectedTopic,subname);
        JSONObject existing=position>=0?rows.optJSONObject(position):null;
        LinearLayout form=column();form.setPadding(dp(17),dp(5),dp(17),dp(5));
        EditText reference=edit("PDF page, section, learning outcome ID",
            existing==null?"":existing.optString("reference"));
        EditText summary=edit("Short paraphrase of the syllabus learning outcome",
            existing==null?"":existing.optString("summary"));
        EditText reviewer=edit("Reviewer name / initials",
            existing==null?"":existing.optString("reviewer"));
        EditText notes=edit("Evidence / difference / missing coverage",
            existing==null?"":existing.optString("notes"));
        android.widget.Spinner choice=new android.widget.Spinner(this);
        String[] labels={"Pending manual review","User-marked match","Potential gap"};
        android.widget.ArrayAdapter<String> adapter=new android.widget.ArrayAdapter<>(
            this,android.R.layout.simple_spinner_item,labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choice.setAdapter(adapter);
        String status=existing==null?OutcomeCrosswalkStore.PENDING:existing.optString("status");
        choice.setSelection(OutcomeCrosswalkStore.MATCH.equals(status)?1:
            OutcomeCrosswalkStore.GAP.equals(status)?2:0);
        add(form,reference,5);add(form,summary,9);add(form,reviewer,9);
        add(form,notes,9);add(form,choice,11);
        AlertDialog.Builder dialog=new AlertDialog.Builder(this)
            .setTitle(position<0?"Add Manual Syllabus Outcome":"Edit Manual Syllabus Outcome")
            .setView(form).setPositiveButton("Save",(d,which)->{
                if(reference.getText().toString().trim().isEmpty()||
                    summary.getText().toString().trim().isEmpty()){
                    toast("PDF reference နှင့် outcome summary လိုအပ်ပါသည်");return;
                }
                JSONObject entry=new JSONObject();
                try{
                    entry.put("reference",reference.getText().toString().trim());
                    entry.put("summary",summary.getText().toString().trim());
                    entry.put("notes",notes.getText().toString().trim());
                    entry.put("reviewer",reviewer.getText().toString().trim());
                    entry.put("status",choice.getSelectedItemPosition()==1?
                        OutcomeCrosswalkStore.MATCH:choice.getSelectedItemPosition()==2?
                        OutcomeCrosswalkStore.GAP:OutcomeCrosswalkStore.PENDING);
                    entry.put("updated",System.currentTimeMillis());
                }catch(Exception ignored){}
                JSONArray updated=new JSONArray();
                for(int i=0;i<rows.length();i++)updated.put(i==position?entry:rows.opt(i));
                if(position<0)updated.put(entry);
                crosswalk.put(selectedSubject,selectedTopic,subname,updated);draw();
            }).setNegativeButton("Cancel",null);
        if(position>=0)dialog.setNeutralButton("Delete",(d,w)->{
            JSONArray updated=new JSONArray();for(int i=0;i<rows.length();i++)
                if(i!=position)updated.put(rows.opt(i));
            crosswalk.put(selectedSubject,selectedTopic,subname,updated);draw();
        });
        dialog.show();
    }
    private EditText edit(String hint,String value){
        EditText input=new EditText(this);input.setHint(hint);
        input.setText(value);input.setTextSize(13);input.setMinLines(1);
        input.setMaxLines(3);return input;
    }
    private void exportCrosswalk(){
        Intent intent=new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE,"GCE_O_Level_2026_Manual_Crosswalk.json");
        try{startActivityForResult(intent,901);}
        catch(Exception ex){toast("File save application not available");}
    }
    @Override protected void onActivityResult(int request,int result,Intent data){
        super.onActivityResult(request,result,data);
        if(request==901&&result==RESULT_OK&&data!=null&&data.getData()!=null){
            try(OutputStream stream=getContentResolver().openOutputStream(data.getData())){
                if(stream==null)throw new Exception("Cannot open document");
                stream.write(crosswalk.exportAll().toString(2).getBytes("UTF-8"));
                toast("Crosswalk JSON file saved");
            }catch(Exception ex){toast("JSON export failed");}
        }
    }
    private void editReference(){
        EditText input=new EditText(this);input.setSingleLine(false);input.setMinLines(2);
        input.setHint("ဥပမာ: Syllabus PDF p. 12, section 1.4 / checked 2026");
        input.setText(evidence(subject,topic,subtopic));
        LinearLayout panel=column();panel.setPadding(dp(14),dp(5),dp(14),dp(1));panel.addView(input);
        new AlertDialog.Builder(this).setTitle("Official PDF Reference · ကိုယ်တိုင်မှတ်တမ်း")
            .setView(panel).setPositiveButton("သိမ်းဆည်းရန်",(dialog,w)->{
                prefs.edit().putString(key(subject,topic,subtopic,"reference"),
                    input.getText().toString().trim()).apply();draw();
            }).setNegativeButton("ပယ်ဖျက်",null).show();
    }
    private void route(String mode){
        AiProviderRouter.chooseAndRun(this,
            OLevelLearningOutcomes.lessonPrompt(sub(),top(),subtopic,lesson,mode),
            "olevel_"+sub().code+"_"+topic+"_"+subtopic+"_"+lesson+"_"+mode,
            "GCE O Level "+mode);
    }
    private void copy(String value){
        ClipboardManager clip=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        if(clip!=null)clip.setPrimaryClip(ClipData.newPlainText("GCE study prompt",value));
        toast("Prompt Copy ပြီးပါပြီ");
    }
    private void openLesson(int index){
        Intent intent=new Intent(this,OLevelSyllabusActivity.class);
        intent.putExtra("subject_index",subject);intent.putExtra("topic_index",topic);
        intent.putExtra("subtopic_index",subtopic);intent.putExtra("lesson_index",index);
        startActivity(intent);
    }
    private void open(String uri){
        try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(uri)));}
        catch(Exception e){toast("Web Browser မတွေ့ပါ");}
    }
    private void back(){
        if(subtopic>=0)subtopic=-1;
        else if(topic>=0)topic=-1;
        else if(subject>=0)subject=-1;
        else{finish();return;}
        draw();
    }
    @Override public void onBackPressed(){back();}
}
