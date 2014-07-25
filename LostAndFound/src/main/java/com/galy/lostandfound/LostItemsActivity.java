package com.galy.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.galy.lostandfound.database.DBManager;
import com.galy.lostandfound.database.information;


public class LostItemsActivity extends Activity {

    private DBManager mgr;
    private ListView lv_lost;
    private ImageButton refresh_lost;
    private ImageButton post_lost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lost_items);

        lv_lost = (ListView) findViewById(R.id.lv_lost_items);
        refresh_lost = (ImageButton)findViewById(R.id.image_button_refresh_lost);
        post_lost = (ImageButton)findViewById(R.id.image_button_post_lost);

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
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (information information : informations) {
            if(information.lostorfound == 0)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                String _id = Integer.toString(information._id);
                map.put("_id", _id);
                map.put("headline", information.headline);
                map.put("content", information.content);
                list.add(map);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.list_lost_items,
                new String[]{"_id", "headline", "content"}, new int[]{R.id.ItemId, R.id.ItemTitle,R.id.ItemText});
        lv_lost.setAdapter(adapter);
    }
}
