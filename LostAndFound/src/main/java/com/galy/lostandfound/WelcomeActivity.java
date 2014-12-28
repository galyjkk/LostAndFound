package com.galy.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import com.galy.lostandfound.database.DBManager;
import com.galy.lostandfound.database.UserToken;
import com.galy.lostandfound.service.AsyncTaskHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends Activity implements AsyncTaskHttpClient.ILoginListener {

    private DBManager mgr;
    private UserToken token;

    private static final String Validate = "validate";
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        mgr = new DBManager(this);
        token = mgr.queryToken();

        if (token == null){
            pageJump();
        } else {
            List<NameValuePair> t = new ArrayList<NameValuePair>(2);
            t.add(new BasicNameValuePair("username", token.username));
            t.add(new BasicNameValuePair("token", token.token));

            new AsyncTaskHttpClient(this,this,t).execute(Validate);
        }
//        //设置跳转页面和延迟
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                startActivity(intent);
//                WelcomeActivity.this.finish();
//            }
//        }, 3000);
    }

    @Override
    public void loading() {

    }

    @Override
    public void complete(JSONObject result) {
        if (result == null){
            Toast.makeText(WelcomeActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if(result.getBoolean("success")){
                    userName = result.getString("user");
                    //登陆成功后发送广播切换布局
                    Intent mIntent = new Intent("loginSuccess");
                    //传送数据
                    mIntent.putExtra("username", userName);
                    //发送广播
                    sendBroadcast(mIntent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        pageJump();
    }

    void pageJump(){
        //设置跳转页面和延迟
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, 3000);
    }
}
