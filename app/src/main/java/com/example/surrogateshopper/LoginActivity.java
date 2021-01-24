package com.example.surrogateshopper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.surrogateshopper.Requester.RequesterActivity;
import com.example.surrogateshopper.Volunteer.VolunteerActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

  private RelativeLayout LoginLayout, LoaderLayout;
  private EditText email, password;
  private Button loginBtn;
  private PHPRequests phpRequests;
  private ProgressBar loginProgress;
  private DatabaseHelper db;

  Runnable welcomeLoader =
      new Runnable() {
        @Override
        public void run() {
          LoaderLayout.setVisibility(View.GONE);
          LoginLayout.setVisibility(View.VISIBLE);
        }
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    setContentView(R.layout.activity_login);
    LoginLayout = findViewById(R.id.loginLayout);
    LoaderLayout = findViewById(R.id.loaderLayout);
    TextView go2Register = findViewById(R.id.go2register);
    email = findViewById(R.id.loginEmail);

    if (intent != null) {
      String str = intent.getStringExtra("email");
      email.setText(str);
    }

    password = findViewById(R.id.loginPassword);
    loginBtn = findViewById(R.id.loginButton);
    new Handler(Looper.getMainLooper()).postDelayed(welcomeLoader, 2000);
    String go2link = "New to this app?  Sign up ";
    Spannable spannable = new SpannableString(go2link);
    loginProgress = findViewById(R.id.loginProgressBar);
    phpRequests = new PHPRequests();

    ClickableSpan clickableSpan =
        new ClickableSpan() {

          @Override
          public void onClick(@NonNull View widget) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
          }

          @Override
          public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.BLUE);
            ds.setUnderlineText(true);
          }
        };

    spannable.setSpan(clickableSpan, 18, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    go2Register.setText(spannable);
    go2Register.setMovementMethod(LinkMovementMethod.getInstance());

    loginBtn.setOnClickListener(
        v -> {
          db = new DatabaseHelper(this);
          db.deleteAll();

          String Email = email.getText().toString();
          String Password = password.getText().toString();

          if (!Email.equals("") && !Password.equals("")) {
            loginBtn.setVisibility(View.GONE);
            loginProgress.setVisibility(View.VISIBLE);
            phpRequests.login(this, Email, Password, this::process);
          } else {
            Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
          }
        });
  }

  private void process(String response) throws JSONException {

    JSONObject jObj = new JSONObject(response);
    String res = jObj.getString("response");
    db.deleteAll();
    if (!res.equals("0")) {
      JSONObject jRes = new JSONObject(res);
      String user_id = jRes.getString("user_id");
      String name = jRes.getString("first_name");
      String surname = jRes.getString("last_name");
      String user_type = jRes.getString("user_type");
      String user_email = email.getText().toString();

      if (db.insertData(user_id, name, surname, user_email, user_type)) {
        password.setText("");
        if (user_type.equals("1")) {
          startActivity(new Intent(this, RequesterActivity.class));
        } else if (user_type.equals("2")) {
          startActivity(new Intent(this, VolunteerActivity.class));
        } else {
          Toast.makeText(this, "Login  here", Toast.LENGTH_SHORT).show();
        }
      } else {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
      }

    } else {
      Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
    }

    loginBtn.setVisibility(View.VISIBLE);
    loginProgress.setVisibility(View.GONE);
  }
}
