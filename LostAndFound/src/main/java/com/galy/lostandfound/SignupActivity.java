package com.galy.lostandfound;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.galy.lostandfound.service.AsynchronousHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;


public class SignupActivity extends Activity implements AsynchronousHttpClient.ILoadListener {

    private ImageButton cancelBtn;
    private BootstrapButton signUpBtn;

    private BootstrapEditText userNameText;
    private BootstrapEditText passWordText;
    private BootstrapEditText passWordConfirmText;

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
        JSONObject signUp = new JSONObject();

        try {
            signUp.put("username", userNameText.getText().toString());
            signUp.put("password", passWordText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new AsynchronousHttpClient("http://182.92.170.217:8000/newuser/",this,this).post(signUp);
    }

    @Override
    public void loading() {

    }

    @Override
    public void complete(JSONArray j) {

    }
}
