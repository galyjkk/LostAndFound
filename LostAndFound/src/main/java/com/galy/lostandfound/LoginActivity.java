package com.galy.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.beardedhen.androidbootstrap.BootstrapButton;


public class LoginActivity extends Activity {

    private ImageButton cancelBtn;
    private BootstrapButton signInBtn;
    private BootstrapButton signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        cancelBtn = (ImageButton) findViewById(R.id.cancel_login);
        signInBtn = (BootstrapButton) findViewById(R.id.sign_in_login);
        signUpBtn = (BootstrapButton) findViewById(R.id.sign_up_login);

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
                LoginActivity.this.finish();
            }
        });
    }
}
