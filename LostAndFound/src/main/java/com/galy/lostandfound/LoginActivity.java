package com.galy.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.galy.lostandfound.service.AsyncTaskHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LoginActivity extends Activity implements AsyncTaskHttpClient.ILoginListener{

    private ImageButton cancelBtn;
    private BootstrapButton signInBtn;
    private BootstrapButton signUpBtn;
    private BootstrapEditText username;
    private BootstrapEditText password;

    private String userName;
    private int errorCode;

    private static final String SignIn = "signin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        cancelBtn = (ImageButton) findViewById(R.id.cancel_login);
        signInBtn = (BootstrapButton) findViewById(R.id.sign_in_login);
        signUpBtn = (BootstrapButton) findViewById(R.id.sign_up_login);
        username = (BootstrapEditText) findViewById(R.id.username_login);
        password = (BootstrapEditText) findViewById(R.id.password_login);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u = username.getText().toString();
                String p = password.getText().toString();

                if(u != "" && p != "") {
                    List<NameValuePair> signInInfo = new ArrayList<NameValuePair>(2);
                    signInInfo.add(new BasicNameValuePair("username", u));
                    signInInfo.add(new BasicNameValuePair("password", p));

                    signIn(signInInfo);
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignUpIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(toSignUpIntent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //登陆成功后发送广播切换布局
                Intent mIntent = new Intent("sendBroadcast");
                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                //发送广播
                sendBroadcast(mIntent);

                LoginActivity.this.finish();

            }
        });
    }

    public void signIn(List<NameValuePair> signInInfo) {
        new AsyncTaskHttpClient(this, this, signInInfo).execute(SignIn);
        Toast.makeText(LoginActivity.this, "send", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loading() {

    }

    // success == true 返回用户名(String)
    // success == false 返回错误码(int)，102:用户名或密码错误,103:没有输入用户名或者密码,104:登录时发生其他错误
    @Override
    public void complete(JSONObject result) {
        if (result == null){
            Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if(result.getBoolean("success")){
                    userName = result.getString("user");
                } else {
                    errorCode = result.getInt("code");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    public void loading() {
//
//    }
//
//    @Override
//    public void complete(JSONArray j) {
//        Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
//    }
}
