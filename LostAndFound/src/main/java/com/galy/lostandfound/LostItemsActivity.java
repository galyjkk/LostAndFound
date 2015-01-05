package com.galy.lostandfound;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.galy.lostandfound.database.DBManager;
import com.galy.lostandfound.database.UserToken;
import com.galy.lostandfound.database.information;

import com.galy.lostandfound.service.AsyncTaskHttpClient;
import com.joanzapata.android.iconify.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LostItemsActivity extends Activity implements AsyncTaskHttpClient.ILoginListener {

    private DBManager mgr;
    private ListView lv_lost;
    private ImageButton refresh_lost;
    private ImageButton post_lost;

    private static final String GetArticles = "getArticles";
    private static final String GETALL = "?tag=0&ts=";
    private final String ACTION_NAME = "loginSuccess";
    private String username = "";

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lost_items);

        registerBroadcastReceiver();

        lv_lost = (ListView) findViewById(R.id.lv_lost_items);
        refresh_lost = (ImageButton) findViewById(R.id.image_button_refresh_lost);
        post_lost = (ImageButton) findViewById(R.id.image_button_post_lost);

        //初始化DBManager
        mgr = new DBManager(this);

        lv_lost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //获取选中信息的_id
                TextView StringId = (TextView) arg1.findViewById(R.id.ItemId);
                String MessageId = StringId.getText().toString();
                Intent toDetails = new Intent(LostItemsActivity.this, DetailsActivity.class);
                toDetails.putExtra("messageId", MessageId);
                toDetails.putExtra("username", username);
                startActivity(toDetails);
            }
        });

        //查询数据库
        query(refresh_lost);

        post_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPost = new Intent(LostItemsActivity.this, PostActivity.class);
                toPost.putExtra("fromLost", "lost");
                toPost.putExtra("name", username);
                startActivity(toPost);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }

    //找东西查询
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
            Toast.makeText(LostItemsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LostItemsActivity.this, result.getInt("error"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        showListView();

    }

    private BroadcastReceiver onPostReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            username = intent.getStringExtra("username");
            if (action.equals(ACTION_NAME)) {

            }
        }
    };

    public void registerBroadcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        //注册广播
        registerReceiver(onPostReceiver, myIntentFilter);
    }

    public void showListView() {
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
            if (information.lostorfound == 0) {
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
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.list_lost_items,
                new String[]{"_id", "headline", "content", "pubDateStr"}, new int[]{R.id.ItemId, R.id.ItemTitle, R.id.ItemText, R.id.PubDate});
        lv_lost.setAdapter(adapter);
    }
}
