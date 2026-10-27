package asia.aaii.studentstudy;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Locally generated suggested tasks; not an exam board course completion
 * certificate nor a prediction of student performance.
 *
 * Key compatibility: reads and writes V2.15 progress flags, so existing
 * lesson/practice/exam self-reports are preserved.
 */
public final class PriorityStudyPlan {
    public static final int DAYS=7, DEFAULT_PER_DAY=3;
    public static final String[] STEPS={"study","practice","exam"};
    private final SharedPreferences progress,options;
    private final SyllabusGapEngine gapEngine;
    public PriorityStudyPlan(Context context){
        gapEngine=new SyllabusGapEngine(context);
        progress=context.getSharedPreferences("o_level_coverage_v215",Context.MODE_PRIVATE);
        options=context.getSharedPreferences("o_level_priority_plan_v218",Context.MODE_PRIVATE);
    }
    public int subject(){
        int n=options.getInt("subject",-1);
        return n>=0&&n<OLevel2026.SUBJECTS.length?n:-1;
    }
    public int perDay(){
        int n=options.getInt("per_day",DEFAULT_PER_DAY);
        return Math.max(1,Math.min(4,n));
    }
    public void configure(int selected,int tasksPerDay){
        int safe=selected>=0&&selected<OLevel2026.SUBJECTS.length?selected:-1;
        options.edit().putInt("subject",safe).putInt("per_day",
            Math.max(1,Math.min(4,tasksPerDay))).apply();
    }
    private static String base(SyllabusGapEngine.Entry e){
        return "c_"+e.subjectData.code+"_"+e.topic+"_"+e.subtopic+"_";
    }
    public boolean complete(SyllabusGapEngine.Entry e,String step){
        return progress.getBoolean(base(e)+step,false);
    }
    public void setComplete(SyllabusGapEngine.Entry e,String step,boolean done){
        for(String allowed:STEPS){
            if(allowed.equals(step)){
                progress.edit().putBoolean(base(e)+step,done).apply();return;
            }
        }
    }
    public String nextStep(SyllabusGapEngine.Entry e){
        for(String item:STEPS)if(!complete(e,item))return item;
        return null;
    }
    public int completedSteps(SyllabusGapEngine.Entry e){
        int k=0;for(String step:STEPS)if(complete(e,step))k++;return k;
    }
    public int priority(SyllabusGapEngine.Entry e){
        int base;
        if(SyllabusGapEngine.GAP.equals(e.status))base=100;
        else if(SyllabusGapEngine.REVIEW.equals(e.status))base=70;
        else if(SyllabusGapEngine.UNMAPPED.equals(e.status))base=55;
        else base=25;
        int incomplete=3-completedSteps(e);
        return base+incomplete*5;
    }
    public List<SyllabusGapEngine.Entry> queue(){
        List<SyllabusGapEngine.Entry> all=gapEngine.entries(subject());
        List<SyllabusGapEngine.Entry> available=new ArrayList<>();
        for(SyllabusGapEngine.Entry entry:all)if(nextStep(entry)!=null)available.add(entry);
        Collections.sort(available,new Comparator<SyllabusGapEngine.Entry>(){
            @Override public int compare(SyllabusGapEngine.Entry a,SyllabusGapEngine.Entry b){
                int p=Integer.compare(priority(b),priority(a));if(p!=0)return p;
                p=Integer.compare(a.subject,b.subject);if(p!=0)return p;
                p=Integer.compare(a.topic,b.topic);if(p!=0)return p;
                return Integer.compare(a.subtopic,b.subtopic);
            }
        });
        return available;
    }
    public static String stepTitle(String step){
        if("study".equals(step))return "📖 Explain & Learn";
        if("practice".equals(step))return "✍ Practice & Answers";
        if("exam".equals(step))return "📝 Exam Practice";
        return "Completed";
    }
    public String remediationPrompt(SyllabusGapEngine.Entry e,String step){
        String mode="study".equals(step)?"teach":"practice".equals(step)?"practice":"exam";
        return OLevelLearningOutcomes.lessonPrompt(e.subjectData,e.topicData,e.subtopic,0,mode)
            +"\n\nPERSONAL STUDY PLAN CONTEXT (non-official):\n"
            +"Priority: "+e.label()+"\n"
            +"Self-reported completion: lesson="+complete(e,"study")+
            ", practice="+complete(e,"practice")+
            ", exam="+complete(e,"exam")+
            ".\nPlan requested activity: "+stepTitle(step)+".\n"
            +"Start now; use original exam-style questions; show model reasoning. "+
            "Be clear that this is not an official Cambridge exam prediction.";
    }
    public static final class Summary{
        public int subtopics,completedSubtopics,studyDone,practiceDone,examDone,totalSteps;
        public String percent(){
            return totalSteps==0?"0%":
                ((int)Math.round((studyDone+practiceDone+examDone)*100.0/totalSteps))+"%";
        }
    }
    public Summary summary(){
        Summary s=new Summary();
        for(SyllabusGapEngine.Entry item:gapEngine.entries(subject())){
            s.subtopics++;s.totalSteps+=3;
            if(complete(item,"study"))s.studyDone++;
            if(complete(item,"practice"))s.practiceDone++;
            if(complete(item,"exam"))s.examDone++;
            if(completedSteps(item)==3)s.completedSubtopics++;
        }
        return s;
    }
}
