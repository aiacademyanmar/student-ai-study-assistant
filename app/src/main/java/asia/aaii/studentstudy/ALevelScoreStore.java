package asia.aaii.studentstudy;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * V2.22 self-reported AI MOCK results. Not official Cambridge marks, grade,
 * externally validated scores, or automatic scores from other AI apps.
 * Persists independently from the V2.21 manual syllabus coverage store.
 */
public final class ALevelScoreStore {
  private static final int MAX_RECORDS=300;
  private static final int WEAK_THRESHOLD=60;
  private final SharedPreferences prefs;
  public static final class Record {
    public final String code,paper,paperName,route,focus,note;
    public final int scored,possible;
    public final long timestamp;
    public Record(String code,String paper,String paperName,String route,String focus,
                  String note,int scored,int possible,long timestamp){
      this.code=code;this.paper=paper;this.paperName=paperName;this.route=route;
      this.focus=focus;this.note=note;this.scored=scored;this.possible=possible;
      this.timestamp=timestamp;
    }
    public int percent(){return possible<=0?0:(int)Math.round(scored*100d/possible);}
    public JSONObject toJson(){
      JSONObject j=new JSONObject();
      try{
        j.put("code",code);j.put("paper",paper);j.put("paperName",paperName);
        j.put("route",route);j.put("focus",focus);j.put("note",note);
        j.put("scored",scored);j.put("possible",possible);j.put("timestamp",timestamp);
        j.put("assessmentType","SELF_REPORTED_AI_MOCK");
      }catch(Exception ignored){}
      return j;
    }
    static Record fromJson(JSONObject j){
      if(j==null)return null;
      int max=j.optInt("possible",0),score=j.optInt("scored",-1);
      if(max<=0||score<0||score>max)return null;
      return new Record(j.optString("code"),j.optString("paper"),j.optString("paperName"),
        j.optString("route"),j.optString("focus"),j.optString("note"),score,max,j.optLong("timestamp",0));
    }
  }
  public static final class SubjectSummary {
    public final String code;
    public int count,average,best,latest;
    public boolean weak;
    public String weakness;
    SubjectSummary(String s){code=s;}
  }
  public ALevelScoreStore(Context c){
    prefs=c.getSharedPreferences("gce_a_self_reported_scores_v222",Context.MODE_PRIVATE);
  }
  public List<Record> records(){
    ArrayList<Record> list=new ArrayList<>();
    try{
      JSONArray arr=new JSONArray(prefs.getString("history","[]"));
      for(int i=0;i<arr.length();i++){
        Record r=Record.fromJson(arr.optJSONObject(i));
        if(r!=null)list.add(r);
      }
    }catch(Exception ignored){}
    Collections.sort(list,(a,b)->Long.compare(b.timestamp,a.timestamp));
    return list;
  }
  private void persist(List<Record> records){
    JSONArray arr=new JSONArray();
    for(int i=0;i<Math.min(MAX_RECORDS,records.size());i++)arr.put(records.get(i).toJson());
    prefs.edit().putString("history",arr.toString()).apply();
  }
  public boolean add(String code,String paper,String paperName,String route,String focus,
                     String note,int scored,int possible){
    if(ALevel2026.byCode(code)==null||possible<=0||scored<0||scored>possible)return false;
    if(possible>10000||note.length()>400||focus.length()>180||route.length()>150)return false;
    List<Record> old=records();
    old.add(0,new Record(code,paper,paperName,route,focus,note,scored,possible,
      System.currentTimeMillis()));
    persist(old);
    return true;
  }
  public void delete(Record target){
    List<Record> list=records();
    for(int i=0;i<list.size();i++){
      Record r=list.get(i);
      if(r.timestamp==target.timestamp&&r.code.equals(target.code)&&
         r.paper.equals(target.paper)&&r.scored==target.scored&&r.possible==target.possible){
        list.remove(i);break;
      }
    }
    persist(list);
  }
  public SubjectSummary summary(String code){
    SubjectSummary o=new SubjectSummary(code);
    ArrayList<Record> recent=new ArrayList<>();
    for(Record r:records())if(r.code.equals(code))recent.add(r);
    o.count=recent.size();
    if(recent.isEmpty())return o;
    int count=0,sum=0;
    o.best=0;o.latest=recent.get(0).percent();
    for(Record r:recent){
      o.best=Math.max(o.best,r.percent());
      if(count<3){sum+=r.percent();count++;}
    }
    o.average=Math.round(sum/(float)count);
    o.weak=o.average<WEAK_THRESHOLD;
    StringBuilder notes=new StringBuilder();
    for(int i=0;i<Math.min(3,recent.size());i++){
      Record r=recent.get(i);
      if(r.percent()<WEAK_THRESHOLD||!r.note.trim().isEmpty()){
        if(notes.length()>0)notes.append(" | ");
        notes.append("Paper ").append(r.paper).append(": ");
        notes.append(r.focus.isEmpty()?"general":r.focus);
        if(!r.note.trim().isEmpty())notes.append(" — ").append(r.note);
      }
    }
    o.weakness=notes.toString();
    return o;
  }
  public String revisionPrompt(ALevel2026.Subject subject){
    SubjectSummary a=summary(subject.code);
    StringBuilder detail=new StringBuilder();
    int count=0;
    for(Record r:records())if(r.code.equals(subject.code)&&count++<5){
      detail.append("\n- Paper ").append(r.paper)
        .append(" [").append(r.paperName).append("] score ")
        .append(r.scored).append("/").append(r.possible)
        .append(" (").append(r.percent()).append("%)")
        .append("; route: ").append(r.route.isEmpty()?"unspecified":r.route)
        .append("; study focus: ").append(r.focus.isEmpty()?"mixed":r.focus)
        .append("; student weakness note: ").append(r.note.isEmpty()?"none":r.note);
    }
    if(count==0)detail.append("\nNo recorded mock scores. Start with a diagnostic quiz; do not invent a student performance history.");
    return "You are an AS/A Level Cambridge 2026-oriented personal revision tutor. "+
      "Communicate mainly in Myanmar Unicode using correct English exam terms.\n"+
      "Subject: "+subject.name+" ("+subject.code+"); syllabus period "+subject.years+
      ". Official reference page: "+subject.pageUrl+"\n"+
      "Results below are SELF-REPORTED by a student, possibly marked by an external AI; "+
      "none is an official Cambridge assessment or verified score.\n"+
      "Recent self-reported results:"+detail+"\n"+
      "Indicative mean of up to 3 newest mocks: "+(a.count==0?"not available":a.average+"%")+
      ". Do not infer an official grade, predict exam outcomes, or diagnose mastery from sparse entries. "+
      "Respect AS/A paper selections, optional routes, and actual 2026 exam-year PDF. "+
      "Syllabus Learning Outcome references recorded by the student are not Cambridge-certified.\n"+
      "NOW: 1) Identify 3 likely revision priorities based ONLY on supplied weakness notes and scores; "+
      "if there are no scores, ask for one diagnostic question. "+
      "2) Explain the weakest area clearly in Burmese with 2 original worked examples. "+
      "3) Create a practical seven-day revision proposal adjusted to the user's paper and level "+
      "(not an automatic calendar booking). 4) Ask 5 original exam-style questions ONE AT A TIME; "+
      "wait for each student answer and give corrections, hints and a self-created mark guide. "+
      "5) Finish with a follow-up assessment plan. Do not quote official past papers.";
  }
  public JSONObject export(){
    JSONObject output=new JSONObject();JSONArray rows=new JSONArray();
    try{
      output.put("schema","al-mock-self-reported-history-v2.22");
      output.put("disclaimer","Scores manually entered, not official Cambridge results.");
      for(Record r:records())rows.put(r.toJson());
      output.put("records",rows);output.put("count",rows.length());
    }catch(Exception ignored){}
    return output;
  }
}
