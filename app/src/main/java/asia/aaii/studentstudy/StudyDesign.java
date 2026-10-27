package asia.aaii.studentstudy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/** A single reusable, native, responsive, no-WebView design system for V2.19. */
public final class StudyDesign {
    private StudyDesign(){}
    public static final int BG=0xFFF4F6FC, INK=0xFF17264B, MUTED=0xFF697891,
        BLUE=0xFF345CFA, PURPLE=0xFF7652E8, TEAL=0xFF00A9A3,
        WHITE=Color.WHITE, BORDER=0xFFE1E8F5, PALE=0xFFECF1FF;
    public static int dp(Context c,float n){return (int)(n*c.getResources().getDisplayMetrics().density+.5f);}
    public static GradientDrawable shape(Context c,int color,int radius){
        GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(c,radius));return d;
    }
    public static GradientDrawable stroked(Context c,int color,int stroke,int radius){
        GradientDrawable d=shape(c,color,radius);d.setStroke(dp(c,1),stroke);return d;
    }
    public static GradientDrawable gradient(Context c,int left,int right,int radius){
        GradientDrawable d=new GradientDrawable(GradientDrawable.Orientation.TL_BR,new int[]{left,right});
        d.setCornerRadius(dp(c,radius));return d;
    }
    public static TextView text(Context c,String s,int size,int color,boolean bold){
        TextView t=new TextView(c);t.setText(s);t.setTextSize(size);t.setTextColor(color);
        if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        t.setLineSpacing(dp(c,3),1.06f);t.setIncludeFontPadding(true);
        return t;
    }
    public static LinearLayout col(Context c){LinearLayout l=new LinearLayout(c);l.setOrientation(LinearLayout.VERTICAL);return l;}
    public static LinearLayout row(Context c){LinearLayout l=new LinearLayout(c);l.setGravity(Gravity.CENTER_VERTICAL);return l;}
    public static LinearLayout card(Context c){
        LinearLayout l=col(c);l.setBackground(stroked(c,WHITE,BORDER,20));
        l.setPadding(dp(c,16),dp(c,16),dp(c,16),dp(c,16));l.setElevation(dp(c,2));return l;
    }
    public static LinearLayout hero(Context c,String eyebrow,String title,String description){
        LinearLayout box=col(c);
        box.setPadding(dp(c,19),dp(c,19),dp(c,17),dp(c,20));
        box.setBackground(gradient(c,0xFF1E4CC1,PURPLE,23));
        TextView eyebrowView=text(c,eyebrow,11,0xFFDBEAFE,true);
        box.addView(eyebrowView);
        TextView titleView=text(c,title,24,WHITE,true);
        LinearLayout.LayoutParams a=new LinearLayout.LayoutParams(-1,-2);a.topMargin=dp(c,8);box.addView(titleView,a);
        if(description!=null){
            TextView desc=text(c,description,13,0xFFF1F4FF,false);
            LinearLayout.LayoutParams b=new LinearLayout.LayoutParams(-1,-2);b.topMargin=dp(c,8);box.addView(desc,b);
        }
        return box;
    }
    public static TextView chip(Context c,String label,int color,int text){
        TextView t=text(c,label,11,text,true);
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setPadding(dp(c,12),dp(c,8),dp(c,12),dp(c,8));
        t.setBackground(shape(c,color,100));return t;
    }
    public static TextView action(Context c,String label,boolean primary,Runnable run){
        TextView t=text(c,label,14,primary?WHITE:INK,true);
        t.setGravity(Gravity.CENTER_VERTICAL);
        int base=primary?BLUE:WHITE;
        GradientDrawable bg=stroked(c,base,primary?BLUE:BORDER,14);
        t.setBackground(new RippleDrawable(ColorStateList.valueOf(primary?0x44FFFFFF:0x1F345CFA),bg,null));
        t.setPadding(dp(c,16),dp(c,13),dp(c,14),dp(c,13));t.setMinHeight(dp(c,52));
        t.setClickable(true);t.setFocusable(true);t.setContentDescription(label);
        t.setOnClickListener(v->{v.animate().scaleX(0.985f).scaleY(0.985f).setDuration(70).withEndAction(
            ()->v.animate().scaleX(1f).scaleY(1f).setDuration(110).start()).start();run.run();});
        return t;
    }
    public static LinearLayout progress(Context c,int done,int max){
        float frac=max<=0?0:Math.max(0,Math.min(1,done/(float)max));
        LinearLayout track=new LinearLayout(c);track.setOrientation(LinearLayout.HORIZONTAL);
        track.setBackground(shape(c,0xFFE5EAF5,100));track.setClipToOutline(true);
        int filled=Math.round(frac*1000);
        View value=new View(c);value.setBackground(gradient(c,BLUE,TEAL,100));
        track.addView(value,new LinearLayout.LayoutParams(0,dp(c,10),Math.max(0,filled)));
        View rest=new View(c);track.addView(rest,new LinearLayout.LayoutParams(0,dp(c,10),1000-Math.max(0,filled)));
        return track;
    }
    public static LinearLayout stat(Context c,String amount,String label){
        LinearLayout box=col(c);box.setPadding(dp(c,10),dp(c,11),dp(c,10),dp(c,11));
        box.setBackground(shape(c,0xFFF1F4FF,15));
        box.addView(text(c,amount,19,BLUE,true));box.addView(text(c,label,11,MUTED,false));
        return box;
    }
    public static LinearLayout navigation(Activity a,Class<?> selected){
        LinearLayout nav=row(a);nav.setPadding(dp(a,4),dp(a,6),dp(a,4),dp(a,6));
        nav.setBackground(stroked(a,WHITE,BORDER,20));nav.setElevation(dp(a,9));
        String[] labels={"ပင်မ","O Level","A Level","Audit"};
        String[] emojis={"⌂","📘","🎓","✓"};
        Class<?>[] destinations={CatalogActivity.class,OLevelSyllabusActivity.class,
            ALevelSyllabusActivity.class,SyllabusAuditActivity.class};
        for(int i=0;i<destinations.length;i++){
            final Class<?> destination=destinations[i], current=selected;
            boolean active=selected==destination;
            LinearLayout item=col(a);item.setGravity(Gravity.CENTER);
            item.setPadding(dp(a,2),dp(a,7),dp(a,2),dp(a,7));
            if(active)item.setBackground(shape(a,PALE,12));
            TextView ico=text(a,emojis[i],19,active?BLUE:MUTED,active);
            ico.setGravity(Gravity.CENTER);item.addView(ico);
            TextView label=text(a,labels[i],10,active?BLUE:MUTED,active);
            label.setGravity(Gravity.CENTER);item.addView(label);
            item.setContentDescription(labels[i]);
            item.setOnClickListener(v->{
                if(current==destination)return;
                Intent intent=new Intent(a,destination);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                a.startActivity(intent);
            });
            nav.addView(item,new LinearLayout.LayoutParams(0,dp(a,58),1));
        }
        return nav;
    }
}
