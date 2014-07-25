package com.galy.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.galy.lostandfound.database.DBManager;
import com.galy.lostandfound.database.information;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoundItemsActivity extends Activity {

    private DBManager mgr;
    private ListView lv_found;
    private ImageButton refresh_found;
    private ImageButton post_found;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_found_items);

        //final TextView tv1 = (TextView) findViewById(R.id.label);
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
                startActivity(toDetails);
            }
        });

        //查询数据库
        query(refresh_found);

        post_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPost = new Intent(FoundItemsActivity.this, PostActivity.class);
                toPost.putExtra("fromFound", "found");
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

    //寻失主查询
    public void query(View view) {
        List<information> informations = mgr.query();
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (information information : informations) {
            if(information.lostorfound == 1)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                String _id = Integer.toString(information._id);
                map.put("_id", _id);
                map.put("headline", information.headline);
                map.put("content", information.content);
                list.add(map);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.list_found_items,
                new String[]{"_id", "headline", "content"}, new int[]{R.id.ItemId, R.id.ItemTitle,R.id.ItemText});
        lv_found.setAdapter(adapter);
    }
}
