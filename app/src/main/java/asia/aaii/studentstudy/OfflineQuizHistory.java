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
 public void addAnswers(String level,String code,int correct,int total,String topic,String difficulty,int seconds,JSONArray answers){
  try{
   JSONArray old=all(),next=new JSONArray();JSONObject row=new JSONObject();
   row.put("level",level);row.put("code",code);row.put("correct",correct);row.put("total",total);
   row.put("topic",topic);row.put("difficulty",difficulty);row.put("duration_seconds",seconds);
   row.put("answers",answers);row.put("timestamp",System.currentTimeMillis());
   StringBuilder wrong=new StringBuilder(),weak=new StringBuilder();
   for(int j=0;j<answers.length();j++){JSONObject a=answers.optJSONObject(j);
    if(a!=null&&!a.optBoolean("correct")){if(wrong.length()>0)wrong.append(", ");
      if(weak.length()>0)weak.append(", ");wrong.append(a.optString("id"));weak.append(a.optString("topic"));}}
   row.put("wrong_ids",wrong.toString());row.put("weak",weak.toString());
   next.put(row);for(int i=0;i<Math.min(99,old.length());i++)next.put(old.optJSONObject(i));
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
