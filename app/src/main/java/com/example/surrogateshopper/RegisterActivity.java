package com.example.surrogateshopper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

  private EditText name, surname, email, passwordOne, passwordTwo;
  private Button registerBtn;
  private RadioGroup userTypeGroup;
  private PHPRequests phpRequests;
  private ProgressBar registerProgress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    TextView go2Login = findViewById(R.id.go2login);
    name = findViewById(R.id.regFirst);
    surname = findViewById(R.id.regLast);
    email = findViewById(R.id.regEmail);
    passwordOne = findViewById(R.id.regPassOne);
    passwordTwo = findViewById(R.id.regPassTwo);
    userTypeGroup = findViewById(R.id.userType);
    RadioButton volunteerRadioBtn = findViewById(R.id.volunteerRadio);
    RadioButton requestRadioBtn = findViewById(R.id.requesterRadio);
    registerBtn = findViewById(R.id.registerButton);
    registerProgress = findViewById(R.id.registerProgressBar);

    phpRequests = new PHPRequests();

    String go2link = "Already have an Account?  Sign in ";
    Spannable spannable = new SpannableString(go2link);
    ClickableSpan clickableSpan =
        new ClickableSpan() {

          @Override
          public void onClick(@NonNull View widget) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
          }

          @Override
          public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.BLUE);
            ds.setUnderlineText(true);
          }
        };

    spannable.setSpan(clickableSpan, 26, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    go2Login.setText(spannable);
    go2Login.setMovementMethod(LinkMovementMethod.getInstance());
    registerBtn.setOnClickListener(
        v -> {
          String Name = name.getText().toString();
          String Surname = surname.getText().toString();
          String Email = email.getText().toString();
          int selected = userTypeGroup.getCheckedRadioButtonId();
          String USERType = "";

          if (selected >= 0) {
            RadioButton t = findViewById(selected);
            String s = (String) t.getText();
            if (s.equals("Volunteer")) {
              USERType = "2";
            } else if (s.equals("Requester")) {
              USERType = "1";
            }
          }

          String password1 = passwordOne.getText().toString();
          String password2 = passwordTwo.getText().toString();

          if (Name.equals("")
              && Surname.equals("")
              && Email.equals("")
              && USERType.equals("")
              && password1.equals("")
              && password2.equals("")) {
            Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
          } else {
            if (!password1.equals(password2)) {
              Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            } else {
              registerBtn.setVisibility(View.GONE);
              registerProgress.setVisibility(View.VISIBLE);

              phpRequests.register(this, Name, Surname, Email, password1, USERType, this::process);
            }
          }
        });
  }

  private void process(String response) throws JSONException {
    JSONObject jObj = new JSONObject(response);
    String r = jObj.getString("response");
    registerBtn.setVisibility(View.VISIBLE);
    registerProgress.setVisibility(View.GONE);
    if (!r.equals("0")) {
      Intent intent = new Intent(this, LoginActivity.class);
      intent.putExtra("email", email.getText().toString());
      startActivity(intent);
    } else {
      Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
    }
  }
}
