package asia.aaii.studentstudy;
import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;

/** Persistent local, manually self-assessed written work. Not machine-marked. */
public final class WrittenPracticeStore {
 private final SharedPreferences prefs;
 public WrittenPracticeStore(Context ctx){prefs=ctx.getSharedPreferences("student_written_v225",Context.MODE_PRIVATE);}
 public static String id(WrittenPracticeBank.Task t){return t.level+"_"+t.code+"_"+Integer.toHexString(t.question.hashCode());}
 public void saveDraft(WrittenPracticeBank.Task t,String response){prefs.edit().putString("draft_"+id(t),response).apply();}
 public String draft(WrittenPracticeBank.Task t){return prefs.getString("draft_"+id(t),"");}
 public JSONArray history(){
  try{return new JSONArray(prefs.getString("attempts","[]"));}catch(Exception ignored){return new JSONArray();}
 }
 public int[] subjectStats(String level,String code){
  int attempts=0,score=0,max=0;
  JSONArray rows=history();
  for(int i=0;i<rows.length();i++){JSONObject j=rows.optJSONObject(i);if(j==null)continue;
   if((level==null||level.equals(j.optString("level")))&&(code==null||code.equals(j.optString("code")))){
    attempts++;score+=j.optInt("score");max+=j.optInt("max");
   }
  }
  return new int[]{attempts,score,max};
 }
 public boolean save(WrittenPracticeBank.Task t,String response,boolean[] points){
  if(response==null||response.trim().isEmpty()||points==null||points.length!=4)return false;
  try{
   int score=0;JSONArray flags=new JSONArray();
   for(boolean f:points){flags.put(f);if(f)score++;}
   JSONObject row=new JSONObject();
   row.put("level",t.level);row.put("code",t.code);row.put("topic",t.topic);
   row.put("question",t.question);row.put("response",response.trim());
   row.put("score",score);row.put("max",4);row.put("rubric",flags);
   row.put("type","student_self_assessment");row.put("timestamp",System.currentTimeMillis());
   JSONArray old=history(),out=new JSONArray();out.put(row);
   for(int i=0;i<Math.min(149,old.length());i++)out.put(old.optJSONObject(i));
   prefs.edit().putString("draft_"+id(t),response).putString("attempts",out.toString()).apply();
   return true;
  }catch(Exception e){return false;}
 }
}
