package asia.aaii.studentstudy;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
/** Open the official Academy Courses destination using Android's external browser handler. */
public final class AcademyBrowser {
  private AcademyBrowser(){}
  public static final String URL="https://academy.openai.com/pages/courses?linkMenu=Courses";
  public static void openCourses(Activity activity){
    Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(URL));
    intent.addCategory(Intent.CATEGORY_BROWSABLE);
    try {activity.startActivity(intent);}
    catch(android.content.ActivityNotFoundException ex){
      Toast.makeText(activity,"Web Browser မတွေ့ပါ။ Chrome သို့မဟုတ် Samsung Internet ထည့်သွင်းပါ။",Toast.LENGTH_LONG).show();
    }
  }
}
