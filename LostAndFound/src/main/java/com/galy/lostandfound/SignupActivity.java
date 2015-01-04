package com.galy.lostandfound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.galy.lostandfound.database.DBManager;
import com.galy.lostandfound.database.UserToken;
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

//    private String userName;
//    private String userToken;
    private int errorCode;

    private DBManager tokenDB;

    private static final String SignUp = "newuser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);

        tokenDB = new DBManager(this);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tokenDB.closeDB();
    }

    private void signUp(){
        String username = userNameText.getText().toString();
        String password = passWordText.getText().toString();
        String confirm = passWordConfirmText.getText().toString();

        if (username.length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("请输入用户名");
            builder.setTitle("错误");
            builder.setNeutralButton("确认",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        } else if (password.length() < 8 || password.length() > 32) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("密码格式错误");
            builder.setTitle("错误");
            builder.setNeutralButton("确认",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        } else if (!confirm.equals(password)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("两次密码不一致");
            builder.setTitle("错误");
            builder.setNeutralButton("确认",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        } else {
//        JSONObject signUp = new JSONObject();
            List<NameValuePair> signUp = new ArrayList<NameValuePair>(3);
            signUp.add(new BasicNameValuePair("username", username));
            signUp.add(new BasicNameValuePair("password", password));
            signUp.add(new BasicNameValuePair("confirm", confirm));

            new AsyncTaskHttpClient(this,"post",this,signUp).execute(SignUp);
        }
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
                    String name = result.getString("user");
                    String token = result.getString("token");
                    UserToken t = new UserToken(name, token);
                    if (tokenDB.queryToken() == null){
                        tokenDB.add(t);
                    } else {
                        tokenDB.updateToken(t);
                    }
                    Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    //登陆成功后发送广播切换布局
                    Intent mIntent = new Intent("loginSuccess");
                    //传送数据
                    mIntent.putExtra("username", name);
                    //发送广播
                    sendBroadcast(mIntent);
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
