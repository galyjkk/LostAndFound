package com.galy.lostandfound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.galy.lostandfound.database.DBManager;
import com.galy.lostandfound.database.information;

import java.util.ArrayList;

public class PostActivity extends Activity {

    private DBManager mgr;

    private RadioButton radio_button_lost;
    private RadioButton radio_button_found;
    private EditText edit_title_post;
    private EditText edit_content_post;
    private EditText edit_number_post;
    private ImageButton add_post;
    private ImageButton back;

    private int lostorfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_post);

        mgr = new DBManager(this);

        radio_button_lost = (RadioButton)findViewById(R.id.radio_button_lost);
        radio_button_found = (RadioButton)findViewById(R.id.radio_button_found);
        edit_title_post = (EditText)findViewById(R.id.edit_title_post);
        edit_content_post = (EditText)findViewById(R.id.edit_content_post);
        edit_number_post = (EditText)findViewById(R.id.edit_number_post);
        add_post = (ImageButton)findViewById(R.id.image_button_add_post);
        back = (ImageButton)findViewById(R.id.image_button_back_post);

        Intent intent = getIntent();
        String fromLost = intent.getStringExtra("fromLost");
        String fromFound =intent.getStringExtra("fromFound");

        if(fromLost != null){
            radio_button_lost.setChecked(true);
            lostorfound = 0;
        }else if(fromFound != null){
            radio_button_found.setChecked(true);
            lostorfound = 1;
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });

        //添加发布信息
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String headline = edit_title_post.getText().toString();
                String content = edit_content_post.getText().toString();
                String number = edit_number_post.getText().toString();

                if (edit_title_post.getText().length() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                    builder.setMessage("请输入标题");
                    builder.setTitle("错误");
                    builder.setNeutralButton("确认",
                            new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }else if (edit_content_post.getText().length() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                    builder.setMessage("请输入内容");
                    builder.setTitle("错误");
                    builder.setNeutralButton("确认",
                            new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }else if (edit_number_post.getText().length() < 8){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                    builder.setMessage("请至少输入8位的联系方式");
                    builder.setTitle("错误");
                    builder.setNeutralButton("确认",
                            new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }else {
                    ArrayList<information> addNewInfos = new ArrayList<information>();
                    information addNewInfo = new information(headline, content, lostorfound, number);
                    addNewInfos.add(addNewInfo);
                    mgr.add(addNewInfos);
                    Toast.makeText(PostActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    PostActivity.this.finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.i("", "onBackPressed()");
        Back();
    }

    //返回键调用方法
    public void Back(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要放弃编辑吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PostActivity.this.finish();

                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

}
