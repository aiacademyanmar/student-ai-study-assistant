package asia.aaii.studentstudy;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Per-question analytics for V2.26+ records. Earlier stored quiz aggregates are
 * retained but DO NOT permit question-level accuracy reconstruction.
 */
public final class QuestionInsightEngine {
 private QuestionInsightEngine(){}
 public static final class Stats{
  public int attempted,correct;
  public int mistakes(){return attempted-correct;}
  public int percent(){return attempted==0?0:Math.round(correct*100f/attempted);}
 }
 public static final class QuestionStats{
  public final String level,code,topic,id;
  public final Stats result;
  public QuestionStats(String l,String c,String t,String q,Stats a){
    level=l;code=c;topic=t;id=q;result=a;
  }
 }
 public static Map<String,Stats> subjectScores(JSONArray history){
  LinkedHashMap<String,Stats> result=new LinkedHashMap<>();
  for(int i=0;i<history.length();i++){
   JSONObject x=history.optJSONObject(i);if(x==null)continue;
   String k=x.optString("level")+"|"+x.optString("code");
   Stats score=result.get(k);if(score==null){score=new Stats();result.put(k,score);}
   score.attempted+=Math.max(0,x.optInt("total"));score.correct+=Math.max(0,x.optInt("correct"));
  }return result;
 }
 public static Map<String,Stats> topics(JSONArray history){
  LinkedHashMap<String,Stats> output=new LinkedHashMap<>();
  for(int i=0;i<history.length();i++){
   JSONObject record=history.optJSONObject(i);if(record==null)continue;
   JSONArray answers=record.optJSONArray("answers");
   // Older history only has weak-topic lists / aggregate totals; do not infer
   // an all-topic success denominator from those legacy records.
   if(answers==null)continue;
   for(int j=0;j<answers.length();j++){
    JSONObject a=answers.optJSONObject(j);if(a==null)continue;
    String k=record.optString("level")+"|"+record.optString("code")+
      "|"+a.optString("topic","Unknown");
    Stats s=output.get(k);if(s==null){s=new Stats();output.put(k,s);}
    s.attempted++;
    if(a.optBoolean("correct",false))s.correct++;
   }
  }
  return output;
 }
 public static List<QuestionStats> questions(JSONArray history){
  LinkedHashMap<String,Stats> scores=new LinkedHashMap<>();
  LinkedHashMap<String,String[]> fields=new LinkedHashMap<>();
  for(int i=0;i<history.length();i++){
   JSONObject record=history.optJSONObject(i);if(record==null)continue;
   JSONArray answers=record.optJSONArray("answers");if(answers==null)continue;
   for(int j=0;j<answers.length();j++){
    JSONObject q=answers.optJSONObject(j);if(q==null)continue;
    String id=q.optString("id");if(id.length()==0)continue;
    String level=record.optString("level"),code=record.optString("code"),topic=q.optString("topic");
    String key=level+"|"+code+"|"+id;Stats s=scores.get(key);
    if(s==null){s=new Stats();scores.put(key,s);fields.put(key,new String[]{level,code,topic,id});}
    s.attempted++;if(q.optBoolean("correct"))s.correct++;
   }
  }
  ArrayList<QuestionStats> result=new ArrayList<>();
  for(Map.Entry<String,Stats> row:scores.entrySet()){
   String[] f=fields.get(row.getKey());result.add(new QuestionStats(f[0],f[1],f[2],f[3],row.getValue()));
  }
  return result;
 }
 public static List<String> wrongQuestionIds(JSONArray history,String level,String code){
  ArrayList<String> list=new ArrayList<>();
  for(int i=0;i<history.length();i++){
   JSONObject r=history.optJSONObject(i);if(r==null)continue;
   if(!level.equals(r.optString("level"))||!code.equals(r.optString("code")))continue;
   JSONArray answers=r.optJSONArray("answers");
   if(answers!=null){
    for(int j=0;j<answers.length();j++){
     JSONObject a=answers.optJSONObject(j);if(a!=null&&!a.optBoolean("correct",false)){
      String id=a.optString("id");if(id.length()>0&&!list.contains(id))list.add(id);
     }
    }
   }else{
    String legacy=r.optString("wrong_ids","");
    for(String id:legacy.split(",\\s*"))if(!id.isEmpty()&&!list.contains(id))list.add(id);
   }
  }return list;
 }
}
