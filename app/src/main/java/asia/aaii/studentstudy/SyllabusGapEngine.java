package asia.aaii.studentstudy;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Gap classifications concern LOCAL APP STUDY MAP / USER-ENTERED PDF references.
 * An unmapped app subtopic does not establish that the Cambridge syllabus has a gap.
 */
public final class SyllabusGapEngine {
    public static final String UNMAPPED="unmapped",REVIEW="review",
        GAP="gap",MATCH="match";
    public static final class Entry {
        public final int subject,topic,subtopic;
        public final OLevel2026.Subject subjectData;
        public final OLevel2026.Topic topicData;
        public final String subtopicName,status;
        public final JSONArray references;
        Entry(int s,int t,int x,OLevel2026.Subject sd,OLevel2026.Topic td,String sub,
              String st,JSONArray refs){
            subject=s;topic=t;subtopic=x;subjectData=sd;topicData=td;
            subtopicName=sub;status=st;references=refs;
        }
        public String label(){
            if(GAP.equals(status))return "⚠ Potential gap (self-reported)";
            if(MATCH.equals(status))return "✓ User-marked match";
            if(REVIEW.equals(status))return "◷ Outcome review pending";
            return "○ No outcome reference entered";
        }
    }
    public static final class Stats {
        public int total,unmapped,pending,gaps,matches,references,studyIncomplete;
        public void add(Entry entry,boolean incomplete){
            total++;references+=entry.references.length();
            if(UNMAPPED.equals(entry.status))unmapped++;
            else if(REVIEW.equals(entry.status))pending++;
            else if(GAP.equals(entry.status))gaps++;
            else matches++;
            if(incomplete)studyIncomplete++;
        }
        public String summary(){
            return "Map "+total+" · Unmapped "+unmapped+" · Review "+pending+
                " · User gaps "+gaps+" · User matches "+matches;
        }
    }
    private final OutcomeCrosswalkStore store;
    private final android.content.SharedPreferences progress;
    public SyllabusGapEngine(Context ctx){
        store=new OutcomeCrosswalkStore(ctx);
        progress=ctx.getSharedPreferences("o_level_coverage_v215",Context.MODE_PRIVATE);
    }
    public List<Entry> entries(int subjectIndex){
        List<Entry> result=new ArrayList<>();
        for(int s=0;s<OLevel2026.SUBJECTS.length;s++){
            if(subjectIndex>=0&&s!=subjectIndex)continue;
            OLevel2026.Subject subject=OLevel2026.SUBJECTS[s];
            for(int t=0;t<subject.topics.length;t++){
                OLevel2026.Topic topic=subject.topics[t];
                for(int x=0;x<topic.subtopics.length;x++){
                    String sub=topic.subtopics[x];JSONArray refs=store.get(subject,topic,sub);
                    int gap=OutcomeCrosswalkStore.countMatching(refs,OutcomeCrosswalkStore.GAP);
                    int pending=OutcomeCrosswalkStore.countMatching(refs,OutcomeCrosswalkStore.PENDING);
                    String status=refs.length()==0?UNMAPPED:gap>0?GAP:
                        pending>0?REVIEW:MATCH;
                    result.add(new Entry(s,t,x,subject,topic,sub,status,refs));
                }
            }
        }
        return result;
    }
    public boolean learningIncomplete(Entry item){
        String key="c_"+item.subjectData.code+"_"+item.topic+"_"+item.subtopic+"_";
        return !progress.getBoolean(key+"study",false)||
               !progress.getBoolean(key+"practice",false)||
               !progress.getBoolean(key+"exam",false);
    }
    public Stats stats(int subjectIndex){
        Stats a=new Stats();
        for(Entry e:entries(subjectIndex))a.add(e,learningIncomplete(e));
        return a;
    }
    public String aiGapPrompt(Entry e){
        StringBuilder refs=new StringBuilder();
        for(int i=0;i<e.references.length();i++){
            JSONObject r=e.references.optJSONObject(i);if(r==null)continue;
            refs.append("\n- USER ENTERED PDF reference: ")
                .append(r.optString("reference","not supplied"))
                .append("; candidate outcome ID: ")
                .append(r.optString("outcome_id","not supplied"))
                .append("; paraphrase: ")
                .append(r.optString("summary","not supplied"))
                .append("; status: ")
                .append(r.optString("status","pending"))
                .append("; note: ")
                .append(r.optString("notes",""));
        }
        if(refs.length()==0)refs.append("\n- No manual PDF outcome references entered yet.");
        return "သင်သည် Myanmar-language GCE O Level curriculum tutor ဖြစ်သည်။"
            +"\nSubject: "+e.subjectData.name+" ("+e.subjectData.code+")"
            +"\nSyllabus validity: "+e.subjectData.years
            +"\nApp topic: "+e.topicData.name
            +"\nApp study subtopic: "+e.subtopicName
            +"\nLocal crosswalk status: "+e.label()
            +"\nManual crosswalk notes:"+refs
            +"\nIMPORTANT: These are USER-ENTERED notes, NOT Cambridge-verified outcome statements. "
            +"Do not claim the source ID or gap is official or verified. "
            +"If exact syllabus information is unavailable, explicitly ask the learner to check "
            +"the provided official Cambridge PDF rather than inventing clause IDs. "
            +"Use only original practice questions; never imitate an official past paper."
            +"\nTASK in Burmese Unicode:"
            +"\n1. Explain which learning targets from this APP topic seem not yet covered, "
            +"distinguishing 'not mapped' from 'genuine syllabus omission'."
            +"\n2. Propose a missing-lesson candidate: title, learning goals, outline and "
            +"key English exam terms, using only supplied verified information."
            +"\n3. Teach the candidate lesson with clear Burmese explanations and 2 fully worked examples."
            +"\n4. Set 5 ORIGINAL exercises (concept, application, exam-style) and ask them "
            +"one at a time, wait for each answer, then mark with reasoning and hints."
            +"\n5. Close with a revision plan and a list of PDF clauses that a human "
            +"still needs to verify. Do not create bogus official clause numbers.";
    }
    private static String csvCell(String s){
        return "\""+(s==null?"":s).replace("\"","\"\"").replace("\r"," ").replace("\n"," ")+"\"";
    }
    /** CSV rows are internal app-subtopic statuses, NOT an official syllabus gap audit. */
    public String reportCsv(int subjectIndex){
        StringBuilder out=new StringBuilder();
        out.append("subject_code,subject,topic,app_subtopic,local_status,reference_count,outcome_ids,reference_pages,study_incomplete,disclaimer\n");
        for(Entry e:entries(subjectIndex)){
            StringBuilder ids=new StringBuilder(),refs=new StringBuilder();
            for(int i=0;i<e.references.length();i++){
                JSONObject row=e.references.optJSONObject(i);if(row==null)continue;
                if(ids.length()>0)ids.append("; ");
                if(refs.length()>0)refs.append("; ");
                ids.append(row.optString("outcome_id",""));
                refs.append(row.optString("reference",""));
            }
            String[] cells={e.subjectData.code,e.subjectData.name,e.topicData.name,
                e.subtopicName,e.status,String.valueOf(e.references.length()),ids.toString(),
                refs.toString(),String.valueOf(learningIncomplete(e)),
                "User-authored app crosswalk; not an official Cambridge coverage audit"};
            for(int i=0;i<cells.length;i++){
                if(i>0)out.append(',');
                out.append(csvCell(cells[i]));
            }
            out.append('\n');
        }
        return out.toString();
    }
}
