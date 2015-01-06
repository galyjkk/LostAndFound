package com.galy.lostandfound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.galy.lostandfound.database.DBManager;
import com.galy.lostandfound.database.information;
import com.galy.lostandfound.service.AsyncTaskHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoreActivity extends Activity {

    private DBManager mgr;

    private final String ACTION_LOGIN = "loginSuccess";
    private final String ACTION_LOGOUT = "logoutSuccess";
    private String username = null;

    private BootstrapButton exit;
    private BootstrapButton about;
//    private BootstrapButton add;
    private BootstrapButton qr_code;
    private BootstrapButton logout;
    private TextView signInText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mgr = new DBManager(this);

        //注册广播
        registerBroadcastReceiver();
        //默认使用未登录界面
        visitorLayout();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }

//    public void addInformations() {
//        ArrayList<information> informations = new ArrayList<information>();
//
//        information information1 = new information("好心人请归还我的优盘", "6G优盘黑色，无绳子细带。内有本人自考司考和户口户籍电子版身份证电子版等重要内容。于2013年12月4日18点在武汉市黄陂区海事学院对面网吧上网走时忘记抽出，必有重谢", 0, "13001112222", "0");
//        information information2 = new information("痛失钱包 麻烦不断", "11月中旬在中关村或出租车上丢失了钱包，内有身份证、银行卡等重要证件。补办挂失麻烦，没有证件举步维艰，亏啊过年了，没有身份证回不了家。如有拾到者请与我本人联系，必当酬谢。", 0, "13001113333", "0");
//        information information3 = new information("拾到钱包一个棕色二折", "今天在方庄物美卖场楼梯拾到钱包一个，棕色二折的，内有身份证一张，证明是香港人丢失的钱包。失主姓孙。包内有各类卡片，还有门禁卡。请失主尽快联系，否则抛弃处理。", 1, "13001112222", "0");
//        information information4 = new information("女士围巾寻失主", "11月4日晚八时在地铁13号线开往东直门方向的座椅上，捡到一条女士围巾，粉红色。失主看到此贴后请与我联系。", 1, "13001114444", "0");
//        information information5 = new information("丢了手机", "三星galaxy，比较旧，有捡到的联系我，必有重谢", 0, "15001235531", "0");
//
//        informations.add(information1);
//        informations.add(information2);
//        informations.add(information3);
//        informations.add(information4);
//        informations.add(information5);
//
//        mgr.add(informations);
//    }

    //receiver
    private BroadcastReceiver onMoreReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            username = intent.getStringExtra("username");
            if(action.equals(ACTION_LOGIN)) {
                //Toast.makeText(MoreActivity.this, "MoreActivity接收广播", Toast.LENGTH_SHORT).show();
                //unregisterReceiver(onPostReceiver);
                //使用已登录界面
                userLayout();
            }
            if (action.equals(ACTION_LOGOUT)) {
                visitorLayout();
            }
        }
    };

    public void registerBroadcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_LOGIN);
        myIntentFilter.addAction(ACTION_LOGOUT);
        //注册广播
        registerReceiver(onMoreReceiver, myIntentFilter);
    }

    private void visitorLayout() {
        setContentView(R.layout.activity_more);

        exit = (BootstrapButton)findViewById(R.id.button_exit);
        about = (BootstrapButton)findViewById(R.id.button_about);
//        add = (BootstrapButton)findViewById(R.id.button_add);
        qr_code = (BootstrapButton)findViewById(R.id.button_qr_code);
        signInText = (TextView)findViewById(R.id.username_more);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoreActivity.this);
                builder.setMessage("确定要退出吗?");
                builder.setTitle("提示");
                builder.setPositiveButton("确认",
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                android.os.Process.killProcess(android.os.Process.myPid());
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
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoreActivity.this);
                builder.setMessage("Version 1.0.0 Made by L&F Group");
                builder.setTitle("关于Lost&Found");
                builder.setNeutralButton("确认",
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        });

        qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent QRWebLink = new Intent(Intent.ACTION_VIEW);
                QRWebLink.setData(Uri.parse("http://182.92.170.217:8000/getMatrixCode"));
                startActivity(QRWebLink);
            }
        });

//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addInformations();
//            }
//        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MoreActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLayout() {
        setContentView(R.layout.activity_more_login);

        signInText = (TextView)findViewById(R.id.username_more);
        exit = (BootstrapButton)findViewById(R.id.button_exit);
        about = (BootstrapButton)findViewById(R.id.button_about);
        logout = (BootstrapButton)findViewById(R.id.button_logout);

        signInText.setText(username);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoreActivity.this);
                builder.setMessage("确定要注销吗?");
                builder.setTitle("提示");
                builder.setPositiveButton("确认",
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //注销service
                                mgr.deleteToken();
                                Intent mIntent = new Intent("logoutSuccess");
                                sendBroadcast(mIntent);
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
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoreActivity.this);
                builder.setMessage("确定要退出吗?");
                builder.setTitle("提示");
                builder.setPositiveButton("确认",
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                android.os.Process.killProcess(android.os.Process.myPid());
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
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoreActivity.this);
                builder.setMessage("Version 1.0.0 Made by L&F Group");
                builder.setTitle("关于Lost&Found");
                builder.setNeutralButton("确认",
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        });
    }
}
