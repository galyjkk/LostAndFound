package com.galy.lostandfound;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.beardedhen.androidbootstrap.BootstrapButton;


public class SignupActivity extends Activity {

    private ImageButton cancelBtn;
    private BootstrapButton signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);

        cancelBtn = (ImageButton) findViewById(R.id.cancel_signup);
        signUpBtn = (BootstrapButton) findViewById(R.id.sign_up_signup);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupActivity.this.finish();
            }
        });
    }
}
