package asia.aaii.studentstudy;
import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Map;
import java.util.LinkedHashMap;
/** Local analytics: MCQ by topic where detailed answers are available, plus written self reviews. */
public class TopicAnalyticsActivity extends Activity {
 private LinearLayout root,body;
 private int dp(int a){return StudyDesign.dp(this,a);}
 private void line(String v,int size){TextView t=StudyDesign.text(this,v,size,StudyDesign.INK,size>=18);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.topMargin=dp(12);body.addView(t,lp);}
 @Override public void onCreate(Bundle b){super.onCreate(b);draw();}
 private void draw(){
  root=StudyDesign.col(this);root.setBackgroundColor(StudyDesign.BG);setContentView(root);
  if(Build.VERSION.SDK_INT>=35)root.setOnApplyWindowInsetsListener((v,insets)->{android.graphics.Insets x=insets.getInsets(WindowInsets.Type.systemBars());v.setPadding(x.left,x.top,x.right,x.bottom);return insets;});
  TextView h=StudyDesign.text(this,"‹   Topic Learning Analytics",18,StudyDesign.INK,true);
  h.setBackgroundColor(-1);h.setPadding(dp(16),dp(15),dp(13),dp(15));h.setOnClickListener(v->finish());root.addView(h);
  ScrollView scroll=new ScrollView(this);body=StudyDesign.col(this);body.setPadding(dp(14),dp(14),dp(14),dp(36));scroll.addView(body);
  root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
  body.addView(StudyDesign.hero(this,"LEARNING REPORT · V2.26","Weak Topics & Progress","MCQ History · Written Self Review · Local Only"));
  OfflineQuizHistory mcq=new OfflineQuizHistory(this);
  WrittenPracticeStore written=new WrittenPracticeStore(this);
  JSONArray rows=mcq.all(),wr=written.history();
  Map<String,int[]> scores=new LinkedHashMap<>();
  int mcqAttempts=0,mcqCorrect=0,mcqTotal=0;
  for(int i=0;i<rows.length();i++){
   JSONObject a=rows.optJSONObject(i);if(a==null)continue;
   mcqAttempts++;mcqCorrect+=a.optInt("correct");mcqTotal+=a.optInt("total");
   String level=a.optString("level"),code=a.optString("code");
   String k=level+" "+code;int[] s=scores.get(k);if(s==null){s=new int[4];scores.put(k,s);}
   s[0]++;s[1]+=a.optInt("correct");s[2]+=a.optInt("total");
  }
  line("MCQ attempts: "+mcqAttempts+" · Correct "+mcqCorrect+"/"+mcqTotal,16);
  body.addView(StudyDesign.progress(this,mcqCorrect,mcqTotal));
  line("Written self-reviews: "+wr.length(),16);
  line("Subject Performance (MCQ)",19);
  for(Map.Entry<String,int[]> e:scores.entrySet()){
   int[] n=e.getValue();int pct=n[2]==0?0:Math.round(100f*n[1]/n[2]);
   line(e.getKey()+" · "+pct+"% · "+n[0]+" attempts",14);
   body.addView(StudyDesign.progress(this,n[1],n[2]));
  }
  line("Topic Learning Review",19);
  Map<String,int[]> topics=new LinkedHashMap<>();
  for(int i=0;i<rows.length();i++){
   JSONObject a=rows.optJSONObject(i);if(a==null)continue;
   String weak=a.optString("weak","");if(weak.length()==0)continue;
   String[] list=weak.split(", ");
   for(String topic:list){String k=a.optString("level")+" "+a.optString("code")+" · "+topic;
    if(topic.trim().length()==0)continue;int[] count=topics.get(k);
    if(count==null){count=new int[2];topics.put(k,count);}count[0]++;
   }
  }
  for(Map.Entry<String,int[]> e:topics.entrySet())
   line("⚠ "+e.getKey()+" · "+e.getValue()[0]+" error(s) recorded",14);
  if(topics.isEmpty())line("No incorrect MCQ topics recorded yet.",13);
  line("Question-level Accuracy · V2.26 records",19);
  for(QuestionInsightEngine.QuestionStats q:QuestionInsightEngine.questions(rows)){
   line(q.level+" "+q.code+" · "+q.id+" · "+q.topic+" · "+q.result.correct+
       "/"+q.result.attempted+" correct",13);
  }
  line("Answered Topic Accuracy · new records only",19);
  for(Map.Entry<String,QuestionInsightEngine.Stats> entry:QuestionInsightEngine.topics(rows).entrySet()){
    QuestionInsightEngine.Stats x=entry.getValue();
    line(entry.getKey().replace("|"," · ")+" · "+x.percent()+"% ("+x.correct+"/"+x.attempted+")",13);
    body.addView(StudyDesign.progress(this,x.correct,x.attempted));
  }
  line("Written Score History (self-review)",19);
  for(int i=0;i<Math.min(wr.length(),30);i++){
   JSONObject a=wr.optJSONObject(i);if(a!=null)
    line(a.optString("level")+" "+a.optString("code")+" · "+a.optString("topic")+
     " · "+a.optInt("score")+"/4 (self-reported)",13);
  }
  line("⚠ A missed topic is based on saved local errors. Older records may lack individual question IDs; accuracy per topic cannot be inferred from missed-only records. "+
       "Written checkbox scores are learner self-assessments, not official Cambridge marks.",12);
  root.addView(StudyDesign.navigation(this,TopicAnalyticsActivity.class));
 }
}