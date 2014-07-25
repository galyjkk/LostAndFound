package com.galy.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.galy.lostandfound.database.DBManager;
import com.galy.lostandfound.database.information;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsActivity extends Activity {

    private DBManager mgr;

    private TextView title_details;
    private TextView content_details;
    private TextView num_details;
    private ImageButton back;
    private ImageButton call;
    private ImageButton message;

    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details);

        title_details = (TextView)findViewById(R.id.text_title_details);
        content_details = (TextView)findViewById(R.id.text_content_details);
        num_details = (TextView)findViewById(R.id.text_number_details);
        back = (ImageButton)findViewById(R.id.image_button_back_details);
        call = (ImageButton)findViewById(R.id.image_button_call_details);
        message = (ImageButton)findViewById(R.id.image_button_message_details);

        //初始化DBManager
        mgr = new DBManager(this);

        //接受传值id
        Intent intent = getIntent();
        String messageId = intent.getStringExtra("messageId");

        //按id查找信息
        List<information> informations = mgr.query();
        for (information information : informations) {
            String _id = Integer.toString(information._id);
            //如果id与messageid相等，则输出相应消息
            if(_id.equals(messageId))
            {
                title_details.setText(information.headline);
                content_details.setText(information.content);
                num = information.number;
                String numF = information.number.substring(0,4);
                String numB = information.number.substring(7);
                num_details.setText(numF+"****"+numB);
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsActivity.this.finish();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri telUri = Uri.parse("tel:"+num);
                Intent intent= new Intent(Intent.ACTION_DIAL, telUri);
                startActivity(intent);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri msgUri = Uri.parse("smsto:"+num);
                Intent intent= new Intent(Intent.ACTION_SENDTO, msgUri);
                startActivity(intent);
            }
        });

    }

}
