package asia.aaii.studentstudy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Student AI Study native registration, porting MOOC LMS V4.1 account rules only.
 * API: Student AI standalone server V1.9.1 — activate/login/cloud-status/logout.
 * Independent of MOOC courses, with the existing 20-screen Student AI app intact.
 */
public final class StudentLicenseActivity extends Activity {
    private static final String STORAGE = "student_study_license_v241";
    private static final String KEY_ALIAS = "student_study_session_aes_v241";
    private static final long MAX_SESSION_MS = 7L * 24 * 60 * 60 * 1000;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private SharedPreferences prefs;
    private LinearLayout column;
    private EditText identifier, password, code, urlInput;
    private TextView status;
    private boolean accountMenu = false;

    private int dp(int n) { return (int) (n * getResources().getDisplayMetrics().density + .5f); }
    private GradientDrawable card(int color, int radius) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color); bg.setCornerRadius(dp(radius)); return bg;
    }
    private TextView text(String s, int size, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(s); view.setTextColor(color); view.setTextSize(size);
        if (bold) view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return view;
    }
    private void add(View view, int margin) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.topMargin = dp(margin); column.addView(view, p);
    }
    private EditText field(String label, int type) {
        EditText input = new EditText(this);
        input.setTextSize(15); input.setHint(label); input.setSingleLine(true);
        input.setInputType(type); input.setPadding(dp(13),dp(11),dp(13),dp(11));
        input.setBackground(card(0xFFEDF4FF, 13)); add(input,9); return input;
    }
    private Button button(String caption, boolean primary, Runnable click) {
        Button b = new Button(this); b.setText(caption); b.setAllCaps(false);
        b.setTextSize(14); b.setTextColor(primary ? Color.WHITE : 0xFF163879);
        b.setBackground(card(primary ? 0xFF2458D8 : 0xFFE7EEFF,14));
        b.setOnClickListener(v -> click.run()); add(b,11); return b;
    }
    private static SharedPreferences pref(Context c) { return c.getSharedPreferences(STORAGE, Context.MODE_PRIVATE); }
    private static SecretKey key() throws Exception {
        KeyStore store = KeyStore.getInstance("AndroidKeyStore"); store.load(null);
        KeyStore.Entry entry = store.getEntry(KEY_ALIAS, null);
        if (entry instanceof KeyStore.SecretKeyEntry) return ((KeyStore.SecretKeyEntry)entry).getSecretKey();
        KeyGenerator generator=KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
        generator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256).build());
        return generator.generateKey();
    }
    private static void saveToken(Context c,String token) throws Exception {
        Cipher cipher=Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE,key());
        byte[] encrypted=cipher.doFinal(token.getBytes(StandardCharsets.UTF_8));
        pref(c).edit().putString("secret", Base64.encodeToString(cipher.getIV(),Base64.NO_WRAP)
            +"."+Base64.encodeToString(encrypted,Base64.NO_WRAP)).apply();
    }
    public static String token(Context c) {
        try {
            String s=pref(c).getString("secret","");
            String[] parts=s.split("\\.");
            if(parts.length!=2)return "";
            Cipher cipher=Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE,key(),new GCMParameterSpec(128,Base64.decode(parts[0],Base64.NO_WRAP)));
            return new String(cipher.doFinal(Base64.decode(parts[1],Base64.NO_WRAP)),StandardCharsets.UTF_8);
        }catch(Exception e){return "";}
    }
    public static boolean hasSession(Context c) {
        SharedPreferences p=pref(c);
        long now=System.currentTimeMillis();
        return !token(c).isEmpty() && now < p.getLong("session_expires_ms",0)
            && now < p.getLong("license_expires_ms",0);
    }
    public static String deviceId(Context c) {
        try {
            String androidId=Settings.Secure.getString(c.getContentResolver(),Settings.Secure.ANDROID_ID);
            String seed=c.getPackageName()+":"+String.valueOf(androidId);
            byte[] digest=MessageDigest.getInstance("SHA-256").digest(seed.getBytes(StandardCharsets.UTF_8));
            StringBuilder out=new StringBuilder();
            for(byte b:digest)out.append(String.format(Locale.US,"%02x",b&0xff));
            return out.toString();
        }catch(Exception e){return "";}
    }
    private static void clear(Context c){ pref(c).edit().remove("secret").remove("name")
        .remove("plan").remove("session_expires_ms").remove("license_expires_ms").apply(); }
    private String apiUrl() {
        String configured=prefs.getString("api_url",BuildConfig.STUDENT_AI_API_URL);
        return configured == null ? "" : configured.trim();
    }
    private JSONObject request(String action, JSONObject payload, String authToken) throws Exception {
        String target=apiUrl();
        if(!target.startsWith("https://"))throw new Exception("HTTPS Cloud API address is required");
        HttpURLConnection c=(HttpURLConnection)new URL(target+(target.contains("?")?"&":"?")+"action="+action).openConnection();
        c.setConnectTimeout(12000);c.setReadTimeout(15000);c.setUseCaches(false);
        c.setRequestProperty("Accept","application/json");
        if(!authToken.isEmpty())c.setRequestProperty("Authorization","Bearer "+authToken);
        c.setRequestProperty("X-Device-Id",deviceId(this));
        try {
            if(payload!=null){
                c.setRequestMethod("POST");c.setDoOutput(true);
                c.setRequestProperty("Content-Type","application/json; charset=utf-8");
                byte[] data=payload.toString().getBytes(StandardCharsets.UTF_8);
                try(OutputStream output=c.getOutputStream()){output.write(data);}
            }
            int status=c.getResponseCode();
            InputStream stream=status>=400?c.getErrorStream():c.getInputStream();
            ByteArrayOutputStream bytes=new ByteArrayOutputStream();
            if(stream!=null)try(InputStream input=stream){
                byte[] b=new byte[4096];int n;
                while((n=input.read(b))!=-1&&bytes.size()<65536)bytes.write(b,0,n);
            }
            JSONObject response=new JSONObject(new String(bytes.toByteArray(),StandardCharsets.UTF_8));
            if(status<200||status>=300)throw new AuthError(status,response.optString("error",response.optString("message","HTTP "+status)));
            return response;
        }finally{c.disconnect();}
    }
    private static final class AuthError extends Exception {
        final int statusCode;
        AuthError(int code,String message){super(message);statusCode=code;}
    }
    private void runRequest(String action, JSONObject body) {
        status.setText("Cloud ကို ချိတ်ဆက်စစ်ဆေးနေပါသည်...");
        io.execute(()->{
            try {
                JSONObject response=request(action,body,"");
                if(response.optString("token").isEmpty())throw new Exception("Cloud did not issue a login token");
                long now=System.currentTimeMillis();
                long licenseMs=response.optLong("expires_at_epoch",0)*1000L;
                if(licenseMs<=now)throw new Exception("Activation is expired");
                saveToken(this,response.getString("token"));
                prefs.edit().putString("name",response.optString("name","Student"))
                    .putString("plan",response.optString("plan","free"))
                    .putLong("license_expires_ms",licenseMs)
                    .putLong("session_expires_ms",now+MAX_SESSION_MS).apply();
                runOnUiThread(this::enterStudy);
            }catch(Exception e){runOnUiThread(()->{
                status.setText("Login မအောင်မြင်ပါ: "+e.getMessage());
            });}
        });
    }
    private void enterStudy() {
        Intent next=new Intent(this,StudentTwentyPagesActivity.class);
        next.putExtra("page",2);startActivity(next);finish();
    }
    private void verifyCurrent() {
        final String existing=token(this);
        if(existing.isEmpty()||!hasSession(this))return;
        status.setText("License ကို စစ်ဆေးနေပါသည်...");
        io.execute(()->{
            try {
                JSONObject r=request("cloud-status",null,existing);
                JSONObject student=r.optJSONObject("student");
                if(student!=null){
                    long exp=student.optLong("expires_at_epoch",0)*1000L;
                    if(exp>0) prefs.edit().putLong("license_expires_ms",exp).apply();
                }
                runOnUiThread(()->{if(!accountMenu)enterStudy();else status.setText("Account active");});
            }catch(AuthError e){
                if(e.statusCode==401||e.statusCode==403)clear(this);
                runOnUiThread(()->status.setText("Session needs login: "+e.getMessage()));
            }catch(Exception e){
                // Allow bundled offline lessons with a previously validated, unexpired license;
                // protected premium API endpoints must still revalidate server-side.
                runOnUiThread(()->{
                    if(hasSession(this)&&!accountMenu)enterStudy();
                    else status.setText("Offline: connect to validate your account");
                });
            }
        });
    }
    private void signOut() {
        String previous=token(this);
        clear(this);
        status.setText("Signed out");
        io.execute(()->{if(!previous.isEmpty())try{request("logout",new JSONObject(),previous);}catch(Exception ignored){}});
        showForm();
    }
    private void showForm() {
        column.removeAllViews();
        add(text("Student AI Study",27,0xFF153C97,true),3);
        add(text(accountMenu?"Student Account & License":"Student Registration / License Login",17,0xFF344E80,true),8);
        add(text("AAII Panyar Home registration rules · No MOOC courses",12,0xFF7283A0,false),7);
        if(hasSession(this)) {
            add(text("Student: "+prefs.getString("name","Student")+
                "    ·    "+prefs.getString("plan","free").toUpperCase(Locale.US),16,0xFF166D67,true),20);
            button("Open Student AI",true,this::enterStudy);
            button("Log Out / Sign out",false,this::signOut);
        }
        add(text("Activation Code Login",17,0xFF14357A,true),20);
        code=field("AAII-XXXX-XXXX-XXXX",android.text.InputType.TYPE_CLASS_TEXT
            |android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        button("Activate Student Account",true,()->{
            try{JSONObject b=new JSONObject();
                b.put("code",code.getText().toString().trim().toUpperCase(Locale.US));
                b.put("device_id",deviceId(this));
                b.put("device_label",Build.MANUFACTURER+" "+Build.MODEL);
                runRequest("activate",b);
            }catch(Exception e){status.setText(e.getMessage());}
        });
        add(text("Username / Email + Password",17,0xFF14357A,true),19);
        identifier=field("Username or Email",android.text.InputType.TYPE_CLASS_TEXT);
        password=field("Password",android.text.InputType.TYPE_CLASS_TEXT|android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        button("Login",true,()->{
            try{JSONObject b=new JSONObject();
                b.put("identifier",identifier.getText().toString().trim());
                b.put("password",password.getText().toString());
                b.put("device_id",deviceId(this));
                b.put("device_label",Build.MANUFACTURER+" "+Build.MODEL);
                runRequest("login",b);
            }catch(Exception e){status.setText(e.getMessage());}
        });
        add(text("Cloud Server (HTTPS)",13,0xFF526783,false),19);
        urlInput=field("https://your-domain/studentai/api.php",android.text.InputType.TYPE_CLASS_TEXT
            |android.text.InputType.TYPE_TEXT_VARIATION_URI);
        urlInput.setText(apiUrl());
        button("Save Server Address",false,()->{
            String value=urlInput.getText().toString().trim();
            if(!value.startsWith("https://")||!value.contains("/")){status.setText("Use a valid HTTPS API URL");return;}
            prefs.edit().putString("api_url",value).apply();
            status.setText("Cloud address saved");
        });
        status=text("Admin creates student registrations and activation codes.",13,0xFF526783,false);
        add(status,18);
    }
    @Override public void onCreate(Bundle saved) {
        super.onCreate(saved);
        getWindow().setStatusBarColor(0xFF153C97);
        prefs=pref(this);
        accountMenu=getIntent().getBooleanExtra("account_menu",false);
        ScrollView scroll=new ScrollView(this);
        scroll.setFillViewport(true);scroll.setBackgroundColor(0xFFF4F7FF);
        column=new LinearLayout(this);column.setOrientation(LinearLayout.VERTICAL);
        column.setPadding(dp(20),dp(30),dp(20),dp(32));
        scroll.addView(column);setContentView(scroll);
        showForm();
        if(hasSession(this))verifyCurrent();
    }
    @Override protected void onDestroy(){io.shutdownNow();super.onDestroy();}
}