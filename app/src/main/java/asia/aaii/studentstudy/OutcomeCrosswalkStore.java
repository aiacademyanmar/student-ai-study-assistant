package asia.aaii.studentstudy;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Local learner/educator-authored crosswalk between one APP study subtopic and
 * zero/many USER-ENTERED syllabus references. No rows are official Cambridge data
 * or externally verified. Keys use topic/subtopic titles to reduce index-shift issues.
 */
public final class OutcomeCrosswalkStore {
  public static final String PENDING="pending",MATCH="match",GAP="gap";
  private final SharedPreferences prefs;

  public OutcomeCrosswalkStore(Context context){
    prefs=context.getSharedPreferences("olevel_manual_crosswalk_2026_v216",Context.MODE_PRIVATE);
  }
  private static String key(OLevel2026.Subject subject,OLevel2026.Topic topic,String subtopic){
    return "map:2026:"+subject.code+":"+topic.name+":"+subtopic;
  }
  public JSONArray get(OLevel2026.Subject subject,OLevel2026.Topic topic,String subtopic){
    try{return new JSONArray(prefs.getString(key(subject,topic,subtopic),"[]"));}
    catch(Exception e){return new JSONArray();}
  }
  public void put(OLevel2026.Subject subject,OLevel2026.Topic topic,String subtopic,JSONArray rows){
    prefs.edit().putString(key(subject,topic,subtopic),rows.toString()).apply();
  }
  public static String statusLabel(String status){
    if(MATCH.equals(status))return "✓ User-marked Match";
    if(GAP.equals(status))return "! Potential Gap";
    return "◷ Needs Review";
  }
  public static int countMatching(JSONArray arr,String status){
    int n=0;for(int i=0;i<arr.length();i++){
      JSONObject row=arr.optJSONObject(i);
      if(row!=null&&status.equals(row.optString("status")))n++;
    }
    return n;
  }
  /** counts: references, user matches, gaps, unresolved; status is never independently verified */
  public int[] counts(OLevel2026.Subject subject,int topicIndex){
    int[] a=new int[4];
    int start=topicIndex<0?0:topicIndex, end=topicIndex<0?subject.topics.length:topicIndex+1;
    for(int t=start;t<end;t++){
      OLevel2026.Topic topic=subject.topics[t];
      for(String sub:topic.subtopics){
        JSONArray rows=get(subject,topic,sub);
        a[0]+=rows.length();
        a[1]+=countMatching(rows,MATCH);
        a[2]+=countMatching(rows,GAP);
        a[3]+=countMatching(rows,PENDING);
      }
    }
    return a;
  }
  public JSONObject exportAll(){
    JSONObject result=new JSONObject();
    try{
      result.put("app","Student AI Study Assistant");
      result.put("schema","gce-o-level-manual-crosswalk-v2.16");
      result.put("exam_year",2026);
      result.put("disclaimer","USER-ENTERED source notes and assessments. Not verified or endorsed by Cambridge International; not complete official syllabus mapping.");
      JSONArray records=new JSONArray();
      for(OLevel2026.Subject s:OLevel2026.SUBJECTS){
        Cambridge2026Audit.Source audit=Cambridge2026Audit.byCode(s.code);
        for(OLevel2026.Topic topic:s.topics)for(String sub:topic.subtopics){
          JSONArray rows=get(s,topic,sub);
          for(int i=0;i<rows.length();i++){
            JSONObject row=rows.optJSONObject(i);if(row==null)continue;
            JSONObject o=new JSONObject();
            o.put("subject",s.name);o.put("code",s.code);
            o.put("topic",topic.name);o.put("app_subtopic",sub);
            o.put("syllabus_years",s.years);
            o.put("official_pdf",audit==null?s.officialUrl:audit.pdfUrl);
            o.put("manual_outcome_reference",row.optString("reference"));
            o.put("manual_outcome_summary",row.optString("summary"));
            o.put("manual_mapping_status",row.optString("status"));
            o.put("review_notes",row.optString("notes"));
            o.put("reviewed_by",row.optString("reviewer"));
            o.put("updated_epoch_ms",row.optLong("updated"));
            records.put(o);
          }
        }
      }
      result.put("records",records);
      result.put("record_count",records.length());
    }catch(Exception ignored){}
    return result;
  }
}
