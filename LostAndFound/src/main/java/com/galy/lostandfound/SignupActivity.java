package com.galy.lostandfound;

import android.app.Activity;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SignupActivity extends Activity implements AsyncTaskHttpClient.ILoginListener {

    private ImageButton cancelBtn;
    private BootstrapButton signUpBtn;

    private BootstrapEditText userNameText;
    private BootstrapEditText passWordText;
    private BootstrapEditText passWordConfirmText;

    private String userName;
    private int errorCode;

    private static final String SignUp = "newuser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);

        cancelBtn = (ImageButton) findViewById(R.id.cancel_signup);
        signUpBtn = (BootstrapButton) findViewById(R.id.sign_up_signup);

        userNameText = (BootstrapEditText) findViewById(R.id.username_signup);
        passWordText = (BootstrapEditText) findViewById(R.id.password_signup);
        passWordConfirmText = (BootstrapEditText) findViewById(R.id.password_confirm);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupActivity.this.finish();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void signUp(){
//        JSONObject signUp = new JSONObject();
        List<NameValuePair> signUp = new ArrayList<NameValuePair>(3);
        signUp.add(new BasicNameValuePair("username", userNameText.getText().toString()));
        signUp.add(new BasicNameValuePair("password", passWordText.getText().toString()));
        signUp.add(new BasicNameValuePair("confirm", passWordConfirmText.getText().toString()));

        new AsyncTaskHttpClient(this,this,signUp).execute(SignUp);
    }

    @Override
    public void loading() {

    }

    // success == true 返回用户名(String)
    // success == false 返回错误码(int)，100:用户名已经被注册过,101:创建新用户时发生其他错误,105:两次输入的密码不同
    @Override
    public void complete(JSONObject result) {
        if (result == null){
            Toast.makeText(SignupActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if(result.getBoolean("success")){
                    userName = result.getString("user");
                    Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    SignupActivity.this.finish();
                } else {
                    errorCode = result.getInt("code");
                    Toast.makeText(SignupActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
