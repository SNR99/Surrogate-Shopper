package com.example.surrogateshopper;

import android.app.Activity;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class PHPRequests {
  private final String baseUrl = "https://0cd7cabbcbfa.ngrok.io/";
  private final OkHttpClient client = new OkHttpClient();

  public void get_my_comments(Activity activity, String basket_id, RequestHandler r) {
    RequestBody user = new FormBody.Builder().add("basket_id", basket_id).build();
    final String loginUrl = baseUrl + "get_my_comments.php";
    Request loginRequest = new Request.Builder().url(loginUrl).post(user).build();

    client
        .newCall(loginRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {

                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }

  public void get_user(Activity activity, String user_id, RequestHandler r) {
    RequestBody user = new FormBody.Builder().add("user_id", user_id).build();
    final String loginUrl = baseUrl + "get_user.php";
    Request loginRequest = new Request.Builder().url(loginUrl).post(user).build();

    client
        .newCall(loginRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {

                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }

  public void accepted_requests(Activity activity, String user_id, RequestHandler r) {
    RequestBody getMyItems = new FormBody.Builder().add("user_id", user_id).build();
    final String loginUrl = baseUrl + "accepted_requests.php";
    Request loginRequest = new Request.Builder().url(loginUrl).post(getMyItems).build();

    client
        .newCall(loginRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {

                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }

  public void add_comment(Activity activity, String basket_id, String message, RequestHandler r) {
    RequestBody loginFormBody =
        new FormBody.Builder().add("message", message).add("basket", basket_id).build();
    final String loginUrl = baseUrl + "add_comment.php";
    Request loginRequest = new Request.Builder().url(loginUrl).post(loginFormBody).build();

    client
        .newCall(loginRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {

                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }

  public void get_basket_items(Activity activity, String basket_id, RequestHandler r) {
    RequestBody getMyItems = new FormBody.Builder().add("basket_id", basket_id).build();
    final String loginUrl = baseUrl + "get_basket_items.php";
    Request loginRequest = new Request.Builder().url(loginUrl).post(getMyItems).build();

    client
        .newCall(loginRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {

                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }

  void login(Activity activity, String email, String password, RequestHandler r) {
    RequestBody loginFormBody =
        new FormBody.Builder().add("email", email).add("password", password).build();
    final String loginUrl = baseUrl + "login.php";
    Request loginRequest = new Request.Builder().url(loginUrl).post(loginFormBody).build();

    client
        .newCall(loginRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {

                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  public void add_basket(
          Activity activity,
          String basketName,
          String user_id,
          ArrayList<String> items,
          ArrayList<String> quantity,
          RequestHandler r) {

    StringBuilder j = new StringBuilder();
    StringBuilder y = new StringBuilder();

    for (String s : items) j.append(s).append(",");

    for (String p : quantity) y.append(p).append(",");

    RequestBody saveMyBasketFormBody =
        new FormBody.Builder()
            .add("basket_name", basketName)
            .add("user_id", user_id)
            .add("items", j.toString())
            .add("quantity", y.toString())
            .build();
    final String getMyBaskets = baseUrl + "add_basket.php";
    Request saveMyBasketsRequest =
        new Request.Builder().url(getMyBaskets).post(saveMyBasketFormBody).build();
    client
        .newCall(saveMyBasketsRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {
                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }

  public void get_available_baskets(Activity activity, RequestHandler r) {
    RequestBody registerFormBody = new FormBody.Builder().build();
    final String getMyBaskets = baseUrl + "get_available_baskets.php";

    Request getMyBasketsRequest =
        new Request.Builder().url(getMyBaskets).post(registerFormBody).build();
    client
        .newCall(getMyBasketsRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {
                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }

  public void get_my_basket(Activity activity, String user_id, RequestHandler r) {
    RequestBody registerFormBody = new FormBody.Builder().add("user_id", user_id).build();
    final String getMyBaskets = baseUrl + "get_my_basket.php";
    Request getMyBasketsRequest =
        new Request.Builder().url(getMyBaskets).post(registerFormBody).build();
    client
        .newCall(getMyBasketsRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {
                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }

  public void accept_request(
          Activity activity, String user_id, String basket_id, RequestHandler r) {
    RequestBody registerFormBody =
        new FormBody.Builder().add("user_id", user_id).add("basket_id", basket_id).build();
    final String registerUrl = baseUrl + "accept_basket.php";
    Request registerRequest = new Request.Builder().url(registerUrl).post(registerFormBody).build();
    client
        .newCall(registerRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {
                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }

  void register(
          Activity activity,
          String first,
          String last,
          String email,
          String password,
          String userType,
          RequestHandler r) {
    RequestBody registerFormBody =
        new FormBody.Builder()
            .add("first", first)
            .add("last", last)
            .add("email", email)
            .add("password", password)
            .add("type", userType)
            .build();
    final String registerUrl = baseUrl + "register.php";
    Request registerRequest = new Request.Builder().url(registerUrl).post(registerFormBody).build();
    client
        .newCall(registerRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {
                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }

  public void get_vol_comments(Activity activity, String user_id, RequestHandler r) {
    RequestBody user = new FormBody.Builder().add("user_id", user_id).build();
    final String loginUrl = baseUrl + "get_vol_comments.php";
    Request loginRequest = new Request.Builder().url(loginUrl).post(user).build();

    client
        .newCall(loginRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {

                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }

  public void get_vol_basket(FragmentActivity activity, String user_id, RequestHandler r) {
    RequestBody user = new FormBody.Builder().add("user_id", user_id).build();
    final String loginUrl = baseUrl + "get_vol_basket.php";
    Request loginRequest = new Request.Builder().url(loginUrl).post(user).build();

    client
        .newCall(loginRequest)
        .enqueue(
            new Callback() {
              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (response.isSuccessful()) {

                  String myRes = Objects.requireNonNull(response.body()).string();
                  activity.runOnUiThread(
                      () -> {
                        try {
                          r.processResponse(myRes);
                        } catch (JSONException e) {
                          e.printStackTrace();
                        }
                      });
                }
              }

              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {}
            });
  }
}
