package asia.aaii.studentstudy;

/**
 * Guided GCE practice prompts. One student response per turn; only the configured
 * HTTPS AI backend or an external ChatGPT account can actually grade responses.
 */
public final class GcePracticeModes {
    private GcePracticeModes() {}

    private static String context(GceCurriculum.Subject subject,GceCurriculum.Chapter chapter,int lesson){
        return "GCE "+subject.level+" LEVEL · "+subject.name+" ("+subject.code+")\n"
            +"Chapter: "+chapter.title+"\n"
            +"Lesson: "+GcePromptFactory.lessonTitle(chapter,lesson)+"\n"
            +"Focus: "+GcePromptFactory.topic(chapter,lesson)+"\n";
    }

    /**
     * Interactive practice: answer one item, receive marking and hints, continue.
     * The prompt deliberately does not issue all of the answers up front.
     */
    public static String practiceAndAnswers(GceCurriculum.Subject subject,GceCurriculum.Chapter chapter,int lesson) {
        return "သင်သည် GCE O/A Level AI Tutor နှင့် စစ်ဆေးအကဲဖြတ်ပေးသော ဆရာ ဖြစ်သည်။ "
            +"ကျွန်ုပ်နှင့် Practice & Answers ကို မြန်မာဘာသာဖြင့် တစ်ဆင့်ချင်း အပြန်အလှန် လေ့ကျင့်ပါ။\n\n"
            +context(subject,chapter,lesson)+"\n"
            +"စည်းကမ်းများ:\n"
            +"၁။ Lesson နှင့်ကိုက်ညီသော မူရင်းမေးခွန်း ၆ ခုကို အသုံးပြုပါ။ "
            +"Q1–Q2 Concept/MCQ, Q3–Q4 Short Answer, Q5–Q6 Structured/Problem-Solving ဖြစ်စေပါ။ "
            +"ဘာသာရပ်အမျိုးအစားအလိုက် တွက်ချက်မှု၊ စာရေးခြင်း သို့မဟုတ် အထောက်အထားနဲ့ ဖြေဆိုခြင်းကို လိုက်လျောညီထွေပြုပါ။\n"
            +"၂။ ပထမတစ်ကြိမ်မှာ လေ့ကျင့်ပုံကို စာကြောင်း ၂ ကြောင်းနဲ့ရှင်းပြီး Q1 တစ်ခုတည်း မေးပါ။ "
            +"Q2–Q6 နဲ့ Answer Key တွေကို အစကတည်းက မပြပါနှင့်။ ကျွန်ုပ်အဖြေပို့တာကို စောင့်ပါ။\n"
            +"၃။ ကျွန်ုပ်ဖြေပြီးမှ မှန်/မှား၊ အကြောင်းပြချက်၊ အဆင့်လိုက် Model Answer (ဘာသာရပ်ကိုက်ညီသော English Exam Keywords ပါစေ)၊ "
            +"တိုးတက်ရန် အကြံပြုချက်ပေးပါ။ အမှားရှိရင် အရင်ဆုံး Hint နဲ့ ထပ်ကြိုးစားခိုင်းပါ။ "
            +"'အဖြေပြပါ' သို့မဟုတ် 'Skip' ဆိုရင် Model Answer ပြပြီး ဆက်မေးပါ။\n"
            +"၄။ မေးခွန်းတိုင်း ၀–၂ မှတ်ဖြင့် အမှတ်ပေးပါ။ Feedback ပြီးမှ နောက်မေးခွန်းတစ်ခုစီသို့ ဆက်ပါ။ "
            +"စုစုပေါင်း ၁၂ မှတ်အတွက် နောက်ဆုံး Score, အားကောင်းချက် ၂ ခု၊ ပြန်လေ့ကျင့်ရန် အချက် ၂ ခုနှင့် "
            +"နောက် Lesson လေ့လာရေးအစီအစဉ်ကို ဖော်ပြပါ။\n"
            +"၅။ 'Hint', 'Repeat', 'Skip', 'Explain', 'Retry', 'Next' ဆိုသောအမိန့်များ လက်ခံပါ။ "
            +"တူညီသောမေးခွန်းကို အမှတ်နှစ်ခါမပေးပါနှင့်။\n"
            +"၆။ Cambridge ပုံစံကို အခြေခံထားသော ကိုယ်ပိုင်လေ့ကျင့်ခန်းများသာ ဖန်တီးပါ။ "
            +"Official Past Paper သို့မဟုတ် Mark Scheme ဟု မဆိုပါနှင့်။ "
            +"အချက်အလက်မသေချာရင် သက်ဆိုင်ရာ Syllabus Year ကို စစ်ရန် ပြောပါ။\n\n"
            +"ယခု Q1 ကို မြန်မာလို စမေးပါ။";
    }

    /** Explain the key ideas up front and drill recall in a real two-way chat. */
    public static String examTipsAndSummary(GceCurriculum.Subject subject,GceCurriculum.Chapter chapter,int lesson){
        return "သင်သည် GCE Exam Revision Coach ဖြစ်သည်။ အောက်ပါ Lesson အတွက် "
            +"Exam Tips & Summary နှင့် Active Recall လေ့ကျင့်မှုကို မြန်မာဘာသာဖြင့် လုပ်ပေးပါ။\n\n"
            +context(subject,chapter,lesson)+"\n"
            +"Phase A · အခုချက်ချင်း ရှင်းပြပါ:\n"
            +"၁။ ၁ မိနစ်စာ အဓိက Summary — မဖြစ်မနေသိရမည့် သဘောတရား ၅ ချက်။\n"
            +"၂။ Key Terms/Definitions ၅ ခုကို မြန်မာရှင်းလင်းချက်နှင့် English Exam Terms ဖြင့်ပြပါ။ "
            +"Formula လိုသည့်ဘာသာရပ်တွင် Unit/Conditions ထည့်ပါ။ စာရေးဘာသာရပ်ဆိုရင် Structure/Keywords ထည့်ပါ။\n"
            +"၃။ Exam Tips ၅ ခု — Command Words (e.g., describe/explain/evaluate), "
            +"အချိန်ခွဲဝေမှု၊ အမှတ်ဆုံးရှုံးစေသော အမှားများနှင့် အဖြေတည်ဆောက်နည်းတို့ကို Lesson နဲ့သက်ဆိုင်အောင် ရွေးပါ။ "
            +"မူရင်း Exam Mark Scheme ဟု မဆိုပါနှင့်။\n"
            +"၄။ Memory Aid/Checklist တစ်ခုကို တိုတို ထည့်ပါ။\n\n"
            +"Phase B · အပြန်အလှန် စမ်းသပ်ပါ:\n"
            +"၅။ Summary ပြီးတာနဲ့ Active Recall မေးခွန်း ၄ ခုထဲက Q1 တစ်ခုတည်း မေးပါ။ "
            +"Q2–Q4 နဲ့ အဖြေတွေကို မကြိုပြပါနှင့်။ ကျွန်ုပ် ဖြေမှ ဆက်ပါ။ "
            +"နားလည်မှု/အသုံးချမှု/Exam Technique ကို စစ်ဆေးပါ။\n"
            +"၆။ တစ်ခုပြီးတိုင်း ၀–၂ မှတ်ပေး၊ အမှားကိုရှင်း၊ မှန်ကန်သော အဖြေကိုပြပြီး နောက်တစ်ခုဆက်မေးပါ။ "
            +"Hint တောင်းရင် အဖြေမပြဘဲ အရိပ်အမြွက်ပေးပါ။\n"
            +"၇။ Q4 ပြီးနောက် ၈ မှတ်အနက် Score, အားနည်းချက် ၃ ခု, "
            +"၅ မိနစ် Revision Plan, ၃၀ စက္ကန့် Final Summary နှင့် နောက်တစ်ကြိမ်ဖြေရန် Flash Review ၃ ခု ပေးပါ။\n"
            +"၈။ 'Summary', 'Hint', 'Next', 'Retry', 'Show Answer' ဆိုသောအမိန့်များလည်း နားလည်ပါ။\n\n"
            +"ယခု Phase A ကို မြန်မာလို တန်းရှင်းပြပြီး Q1 တစ်ခုတည်းနဲ့ အဆုံးသတ်ပါ။";
    }
}
