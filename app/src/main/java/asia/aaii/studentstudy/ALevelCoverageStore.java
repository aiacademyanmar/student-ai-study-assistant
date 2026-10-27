package asia.aaii.studentstudy;
import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONObject;
import org.json.JSONArray;
public final class ALevelCoverageStore{
 private final SharedPreferences prefs;
 public ALevelCoverageStore(Context c){prefs=c.getSharedPreferences("alevel_2026_coverage_v221",Context.MODE_PRIVATE);}
 public String key(ALevel2026.Subject s,ALevel2026.Module m,String topic){return s.code+"|"+m.level+"|"+m.name+"|"+topic;}
 public boolean flag(String key,String f){return prefs.getBoolean(key+"|"+f,false);}
 public void setFlag(String key,String f,boolean value){prefs.edit().putBoolean(key+"|"+f,value).apply();}
 public String note(String key,String field){return prefs.getString(key+"|"+field,"");}
 public void note(String key,String field,String value){prefs.edit().putString(key+"|"+field,value).apply();}
 public int[] summary(ALevel2026.Subject subject){
  int[] a=new int[5];
  for(ALevel2026.Module m:subject.modules)for(String t:m.subtopics){
   String k=key(subject,m,t);a[0]++;
   if(flag(k,"study"))a[1]++;if(flag(k,"practice"))a[2]++;if(flag(k,"exam"))a[3]++;
   if(!note(k,"reference").isEmpty())a[4]++;
  }
  return a;
 }
 public JSONObject export(){
  JSONObject out=new JSONObject();JSONArray items=new JSONArray();
  try{
   out.put("schema","a-level-2026-user-coverage-v221");
   out.put("disclaimer","User-entered PDF notes and self-assessment only. NOT Cambridge verified outcomes.");
   for(ALevel2026.Subject s:ALevel2026.SUBJECTS)for(ALevel2026.Module m:s.modules)for(String t:m.subtopics){
    String k=key(s,m,t);JSONObject record=new JSONObject();
    record.put("subject",s.name);record.put("code",s.code);record.put("level",m.level);
    record.put("module",m.name);record.put("study_focus",t);
    record.put("source_url",s.pdfUrl);record.put("reference",note(k,"reference"));
    record.put("outcome",note(k,"outcome"));record.put("status",note(k,"status"));
    record.put("study",flag(k,"study"));record.put("practice",flag(k,"practice"));
    record.put("exam",flag(k,"exam"));items.put(record);
   }
   out.put("items",items);out.put("count",items.length());
  }catch(Exception ignored){}
  return out;
 }
}
