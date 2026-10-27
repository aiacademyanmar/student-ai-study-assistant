package asia.aaii.studentstudy;
import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
public final class OfflineQuizHistory{
 private final SharedPreferences sp;
 public OfflineQuizHistory(Context c){sp=c.getSharedPreferences("student_offline_quiz_v223",Context.MODE_PRIVATE);}
 public void add(String level,String code,int earned,int total,String weak){
  try{
   JSONArray old=all();JSONObject row=new JSONObject();
   row.put("level",level);row.put("code",code);row.put("correct",earned);row.put("total",total);
   row.put("weak",weak);row.put("timestamp",System.currentTimeMillis());
   JSONArray next=new JSONArray();next.put(row);
   for(int i=0;i<Math.min(99,old.length());i++)next.put(old.optJSONObject(i));
   sp.edit().putString("history",next.toString()).apply();
  }catch(Exception ignored){}
 }
 public void addDetailed(String level,String code,int earned,int total,String weak,String topic,String difficulty,int seconds,String wrongIds){
  try{
   JSONArray old=all();JSONObject row=new JSONObject();
   row.put("level",level);row.put("code",code);row.put("correct",earned);row.put("total",total);
   row.put("weak",weak);row.put("topic",topic);row.put("difficulty",difficulty);
   row.put("duration_seconds",seconds);row.put("wrong_ids",wrongIds);row.put("timestamp",System.currentTimeMillis());
   JSONArray next=new JSONArray();next.put(row);
   for(int i=0;i<Math.min(99,old.length());i++)next.put(old.optJSONObject(i));
   sp.edit().putString("history",next.toString()).apply();
  }catch(Exception ignored){}
 }
 public JSONArray all(){try{return new JSONArray(sp.getString("history","[]"));}catch(Exception ex){return new JSONArray();}}
 public int[] totals(String level,String code){
  int attempts=0,earned=0,possible=0;
  JSONArray a=all();for(int i=0;i<a.length();i++){
   JSONObject r=a.optJSONObject(i);if(r==null)continue;
   if((level==null||level.equals(r.optString("level")))&&(code==null||code.equals(r.optString("code")))){
    attempts++;earned+=r.optInt("correct");possible+=r.optInt("total");
   }
  }return new int[]{attempts,earned,possible};
 }
}
