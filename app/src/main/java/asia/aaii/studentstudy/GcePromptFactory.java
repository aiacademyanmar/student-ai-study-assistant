package asia.aaii.studentstudy;

/**
 * Distinct ready-to-use teaching prompts for each GCE level/subject/chapter/lesson.
 * Curriculum groupings are app-created study paths, not official board chapter order.
 */
public final class GcePromptFactory {
    private GcePromptFactory(){}

    /** Take the next topic from each chapter's existing focus words for lesson-specific prompts. */
    public static String topic(GceCurriculum.Chapter chapter,int lessonIndex){
        String[] parts=chapter.focus.split(",\\s*|\\s+and\\s+");
        if(parts.length>=3){
            if(lessonIndex<2)return parts[lessonIndex].trim();
            StringBuilder remaining=new StringBuilder();
            for(int i=2;i<parts.length;i++){
                if(remaining.length()>0)remaining.append(" and ");
                remaining.append(parts[i].trim());
            }
            return remaining.toString();
        }
        if(parts.length==2){
            return lessonIndex==0?parts[0].trim():lessonIndex==1?parts[1].trim():"exam applications of "+chapter.focus;
        }
        return lessonIndex==0?chapter.focus:lessonIndex==1?"worked examples of "+chapter.focus:"exam applications of "+chapter.focus;
    }
    public static String lessonTitle(GceCurriculum.Chapter chapter,int index){
        return "Lesson "+(index+1)+" · "+topic(chapter,index);
    }
    private static String context(GceCurriculum.Subject subject,GceCurriculum.Chapter chapter,int index){
        return "အတန်းအဆင့်: GCE "+subject.level+" Level\\n"
            +"ဘာသာရပ်: "+subject.name+" (Syllabus code "+subject.code+")\\n"
            +"Chapter: "+chapter.title+"\\n"
            +"Chapter scope: "+chapter.focus+"\\n"
            +"သင်ခန်းစာ: "+lessonTitle(chapter,index)+"\\n"
            +"အဓိကလေ့လာရန်အကြောင်းအရာ: "+topic(chapter,index)+"\\n";
    }
    public static String explain(GceCurriculum.Subject subject,GceCurriculum.Chapter chapter,int index){
        return "သင်သည် Cambridge-oriented GCE သင်ကြားရေးအတွေ့အကြုံရှိသော ဆရာတစ်ယောက်အဖြစ် ဆောင်ရွက်ပါ။ "
            +"အောက်ပါ သင်ခန်းစာကို မြန်မာဘာသာဖြင့် ယခုပင် စတင်ပြီး အပြည့်အဝ ရှင်းပြပေးပါ။ "
            +"ကျွန်ုပ်ကို မေးခွန်းပြန်မမေးပါနှင့်၊ အဖြေကိုစောင့်မနေပါနှင့်။ "
            +"သင်ခန်းစာတစ်ခုလုံးကို အဖြေတစ်ကြိမ်တည်းတွင် ရေးပေးပါ။\\n\\n"
            +context(subject,chapter,index)+"\\n"
            +"ရှင်းပြပုံစံ (အောက်ပါခေါင်းစဉ်များအားလုံး ပါရမည်)\\n"
            +"၁။ 🎯 Learning Objectives — ဒီ Lesson ပြီးလျှင် သိသင့်သောအချက် ၃ ချက်။\\n"
            +"၂။ 📘 အခြေခံသဘောတရား — ရှင်းလင်းလွယ်ကူသော မြန်မာစာဖြင့် အဆင့်လိုက်ရှင်းပြပြီး သက်ဆိုင်ရာ English Technical Terms များ၏အဓိပ္ပာယ်ကိုပါ ထည့်ပါ။\\n"
            +"၃။ 🔍 Why & How — အကြောင်းရင်း၊ အလုပ်လုပ်ပုံနှင့် သဘောတရားချင်းဆက်စပ်မှုကို နားလည်လွယ်အောင်ပြပါ။\\n"
            +"၄။ 🧮 Worked Examples (၂ ခု) — ဘာသာရပ်နှင့်ကိုက်ညီသော မူရင်းနမူနာ ၂ ခုကို အဆင့်တိုင်းရှင်းပြီး၊ တွက်ချက်မှုရှိလျှင် Formula, Unit, Substitution, Final Answer နှင့် စစ်ဆေးနည်းကို ဖော်ပြပါ။ ဘာသာစကားနှင့် လူမှုရေးဘာသာရပ်များတွင် အကြောင်းပြချက်၊ သက်သေ၊ အရေးအသားနမူနာဖြင့်ပြပါ။\\n"
            +"၅။ ⚠️ Common Mistakes — ကျောင်းသားများ မကြာခဏမှားတတ်သော အချက် ၃ ချက်၊ အမှန်ပြင်ပုံ။\\n"
            +"၆။ 📝 Quick Revision — အရေးကြီးသော Definition, Formula သို့မဟုတ် Keyword ၅ ခု။\\n"
            +"၇။ ✍️ Practice Questions — စတင်သူ ၁ ခု၊ အလယ်အလတ် ၁ ခု၊ Exam-style ၁ ခု၊ စုစုပေါင်း ၃ ခု။\\n"
            +"၈။ ✅ Worked Answers — အထက်ပါ မေးခွန်း ၃ ခု၏ အဖြေနှင့် အဆင့်လိုက်ရှင်းချက်ကို သီးခြားအပိုင်းမှာ တစ်ခါတည်းပြပါ။\\n"
            +"၉။ ⭐ Exam Tips — Mark မဆုံးရှုံးစေရန် သတိထားရမည့် အချက် ၃ ချက်။\\n\\n"
            +"ရေးသားရာတွင် Chapter အပြင်ဘက်သို့ အလွန်မဖြန့်ပါနှင့်။ နားလည်ရလွယ်သော ဥပမာသုံးပါ။ "
            +"မူရင်း Exam Past Paper, Official Mark Scheme သို့မဟုတ် တိကျသော Syllabus အခန်းအမှတ်များကို မရှိဘဲ မဖန်တီးပါနှင့်။ "
            +"ဤ Chapter များသည် လေ့လာရေးအစီအစဉ် နမူနာဖြစ်ပြီး သက်ဆိုင်ရာ Exam Board နှင့် Syllabus Year အလိုက် သီးခြားစစ်ဆေးရမည်။ "
            +"အဆုံးမှာ ၂ မိနစ်အတွင်း ပြန်မှတ်နိုင်မည့် Mini Summary ထည့်ပါ။";
    }
    public static String practice(GceCurriculum.Subject subject,GceCurriculum.Chapter chapter,int index,int type){
        String[] descriptions={
            "Lesson topic ကို အဓိကထားပြီး Concept Check မေးခွန်း ၅ ခုနှင့် ဖြေရှင်းချက်များကို တစ်ခါတည်း ထုတ်ပေးပါ။",
            "မူရင်း Multiple Choice Question ၅ ခု (A/B/C/D) ဖန်တီးပြီး အောက်ဆုံးတွင် Answer Key နှင့် တစ်ခုချင်း explanation ကို ထည့်ပေးပါ။",
            "အဆင့်လိုက် Structured Questions ၄ ခု ဖန်တီးပါ။ ဖြေဆိုရန် အချိန်၊ လိုအပ်သော Method နှင့် Model Solutions ကို သီးခြားအပိုင်းတွင် ပေးပါ။",
            "ကျောင်းသားက မှားလေ့ရှိသည့် Worked Answers ၃ ခုနှင့် အမှားနေရာ၊ မှန်ကန်သောဖြေရှင်းနည်းကို တစ်ခါတည်းရှင်းပါ။",
            "၁၅ မိနစ်အတွက် မူရင်း Exam-style Mini Test ၅ ခု၊ အမှတ်ခွဲဝေပုံ နမူနာ၊ Model Solutions နှင့် Self-assessment checklist ထုတ်ပေးပါ။"
        };
        if(type<0||type>=descriptions.length)type=0;
        return "GCE AI ဆရာအဖြစ် အောက်ပါ Lesson အတွက် မြန်မာဘာသာ လေ့ကျင့်ခန်းများကို ယခုပင် ထုတ်ပေးပါ။\\n"
            +context(subject,chapter,index)+"\\n"
            +descriptions[type]+"\\n"
            +"ကျောင်းသားကို အရင်ပြန်မမေးပါနှင့်။ သက်ဆိုင်ရာ English Exam Terms ကို မှန်ကန်စွာရေးပါ။ "
            +"မူရင်း Past Paper ကို တိုက်ရိုက်ကူးယူခြင်းမပြုဘဲ ကိုယ်ပိုင်နမူနာများ ဖန်တီးပါ။";
    }
}
