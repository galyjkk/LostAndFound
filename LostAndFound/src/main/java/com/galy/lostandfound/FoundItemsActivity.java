package com.galy.lostandfound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.galy.lostandfound.database.DBManager;
import com.galy.lostandfound.database.information;
import com.galy.lostandfound.service.AsyncTaskHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoundItemsActivity extends Activity implements AsyncTaskHttpClient.ILoginListener{

    private DBManager mgr;
    private ListView lv_found;
    private ImageButton refresh_found;
    private ImageButton post_found;

    private static final String GetArticles = "getArticles";
    private static final String GETALL = "?tag=1&ts=";
    private final String ACTION_LOGIN = "loginSuccess";
    private final String ACTION_LOGOUT = "logoutSuccess";
    private final String ACTION_POST = "postSuccess";
    private String username = "";

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_found_items);

        registerBroadcastReceiver();

        lv_found = (ListView) findViewById(R.id.lv_found_items);
        refresh_found = (ImageButton)findViewById(R.id.image_button_refresh_found);
        post_found = (ImageButton)findViewById(R.id.image_button_post_found);

        //初始化DBManager
        mgr = new DBManager(this);

        lv_found.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //获取选中信息的_id
                TextView StringId = (TextView) arg1.findViewById(R.id.ItemId);
                String MessageId = StringId.getText().toString();
                Intent toDetails = new Intent(FoundItemsActivity.this, DetailsActivity.class);
                toDetails.putExtra("messageId", MessageId);
                toDetails.putExtra("username", username);
                startActivity(toDetails);
            }
        });

        post_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username != "") {
                    Intent toPost = new Intent(FoundItemsActivity.this, PostActivity.class);
                    toPost.putExtra("fromFound", "found");
                    toPost.putExtra("name", username);
                    startActivity(toPost);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FoundItemsActivity.this);
                    builder.setMessage("登录后才可发布信息");
                    builder.setTitle("请登录");
                    builder.setNeutralButton("确认",
                            new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }
            }
        });

        //查询数据库
        query(refresh_found);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }

    //寻失主查询
    public void query(View view) {
        List<information> informations = mgr.query();

        //sort rules
        Comparator<information> comparator = new Comparator<information>() {
            @Override
            public int compare(information info1, information info2) {
                if (Long.parseLong(info1.pubDate) > Long.parseLong(info2.pubDate)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };
        //sort list
        Collections.sort(informations, comparator);
        String timestamp = "0";
        if (informations.size() != 0) {
            timestamp = informations.get(0).pubDate;
        }
        new AsyncTaskHttpClient(this, "get", this).execute(GetArticles, GETALL+timestamp);
    }


    @Override
    public void loading() {

    }

    @Override
    public void complete(JSONObject result) {
        // read result first and put them to information list

        if (result == null) {
            Toast.makeText(FoundItemsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (result.getBoolean("success")) {
                    ArrayList<information> newInfo = new ArrayList<information>();
                    JSONArray list = result.getJSONArray("list");
                    if (list.length()==0){
                        return;
                    }
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject infoObj = list.getJSONObject(i);
                        information information = new information(infoObj.getString("title"),
                                infoObj.getString("content"),
                                infoObj.getInt("intent"),
                                infoObj.getString("phone"),
                                infoObj.getString("timestamp"));
                        newInfo.add(information);
                    }
                    mgr.add(newInfo);
                } else {
                    Toast.makeText(FoundItemsActivity.this, result.getInt("error"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        showListView();

    }

    private BroadcastReceiver onFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_LOGIN)) {
                username = intent.getStringExtra("username");
            }
            if(action.equals(ACTION_LOGOUT)) {
                username = "";
            }
            if (action.equals(ACTION_POST)) {
                query(refresh_found);
            }
        }
    };

    public void registerBroadcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_LOGIN);
        myIntentFilter.addAction(ACTION_LOGOUT);
        myIntentFilter.addAction(ACTION_POST);
        //注册广播
        registerReceiver(onFoundReceiver, myIntentFilter);
    }

    private void showListView() {
        List<information> informations = mgr.query();

        //sort rules
        Comparator<information> comparator = new Comparator<information>() {
            @Override
            public int compare(information info1, information info2) {
                if (Long.parseLong(info1.pubDate) > Long.parseLong(info2.pubDate)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };
        //sort list
        Collections.sort(informations, comparator);

        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (information information : informations) {
            if(information.lostorfound == 1)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                String _id = Integer.toString(information._id);

                //转换时间格式
                long timeMillis = Long.parseLong(information.pubDate);
                Date pubDate = new Date(timeMillis);
                String pubDateStr = formatter.format(pubDate);

                map.put("_id", _id);
                map.put("headline", information.headline);
                map.put("content", information.content);
                map.put("pubDateStr", pubDateStr);
                list.add(map);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.list_found_items,
                new String[]{"_id", "headline", "content", "pubDateStr"}, new int[]{R.id.ItemId, R.id.ItemTitle,R.id.ItemText,R.id.PubDate});
        lv_found.setAdapter(adapter);
    }
}
