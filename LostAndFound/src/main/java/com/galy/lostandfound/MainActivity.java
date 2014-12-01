package com.galy.lostandfound;

import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.app.TabActivity;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.joanzapata.android.iconify.Iconify;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends TabActivity implements OnCheckedChangeListener{
    private RadioGroup mainTab;
    private ViewPager mPager;
    private List<View> listViews;
    private LocalActivityManager manager = null;
    private MyPagerAdapter mpAdapter = null;
    private int index;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onBackPressed() {
        Log.i("", "onBackPressed()");
        exit();
        //super.onBackPressed();
    }

    @Override
    protected void onPause() {
        Log.i("","onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("","onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("","onDestroy()");
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent() != null){
            index = getIntent().getIntExtra("index", 0);
            mPager.setCurrentItem(index);
            setIntent(null);
        }else{
            if(index < 4){
                index = index+1;
                mPager.setCurrentItem(index);
                index = index -1;
                mPager.setCurrentItem(index);

            }else if(index == 4){
                index= index-1;
                mPager.setCurrentItem(index);
                index = index +1;
                mPager.setCurrentItem(index);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //viewpage
        mPager = (ViewPager) findViewById(R.id.vPager);
        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);
        InitViewPager();

        //tabhost
        mainTab = (RadioGroup)findViewById(R.id.main_tab);
        mainTab.setOnCheckedChangeListener(MainActivity.this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.radio_button0:
                index = 0;
                listViews.set(0, getView("LostItems", new Intent(MainActivity.this, LostItemsActivity.class)));
                mpAdapter.notifyDataSetChanged();
                mPager.setCurrentItem(0);
                break;
            case R.id.radio_button1:
                index = 1;
                listViews.set(1, getView("FoundItems", new Intent(MainActivity.this, FoundItemsActivity.class)));
                mpAdapter.notifyDataSetChanged();
                mPager.setCurrentItem(1);
                break;
            case R.id.radio_button2:
                index = 2;
                listViews.set(2, getView("MyPost", new Intent(MainActivity.this, MyPostActivity.class)));
                mpAdapter.notifyDataSetChanged();
                mPager.setCurrentItem(2);
                break;
            case R.id.radio_button3:
                index = 3;
                listViews.set(3, getView("MyMessages", new Intent(MainActivity.this, MyMessagesActivity.class)));
                mpAdapter.notifyDataSetChanged();
                mPager.setCurrentItem(3);
                break;
            case R.id.radio_button4:
                //Toast.makeText(MainActivity.this,"button4",Toast.LENGTH_SHORT).show();
                index = 4;
                listViews.set(4, getView("More", new Intent(MainActivity.this, MoreActivity.class)));
                mpAdapter.notifyDataSetChanged();
                mPager.setCurrentItem(4);
                break;
        }
    }

    private void InitViewPager() {
        Intent intent = null;
        listViews = new ArrayList<View>();
        mpAdapter = new MyPagerAdapter(listViews);
        intent = new Intent(MainActivity.this, LostItemsActivity.class);
        listViews.add(getView("LostItems", intent));
        intent = new Intent(MainActivity.this, FoundItemsActivity.class);
        listViews.add(getView("FoundItems", intent));
        intent = new Intent(MainActivity.this, MyPostActivity.class);
        listViews.add(getView("MyPost", intent));
        intent = new Intent(MainActivity.this, MyMessagesActivity.class);
        listViews.add(getView("MyMessages", intent));
        intent = new Intent(MainActivity.this, MoreActivity.class);
        listViews.add(getView("More", intent));
        mPager.setOffscreenPageLimit(0);
        mPager.setAdapter(mpAdapter);
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        public void onPageSelected(int arg0) {
            manager.dispatchResume();
            switch (arg0) {
                case 0:
                    index = 0;
                    mainTab.check(R.id.radio_button0);
                    listViews.set(0, getView("LostItems", new Intent(MainActivity.this, LostItemsActivity.class)));
                    mpAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    index = 1;
                    mainTab.check(R.id.radio_button1);
                    listViews.set(1, getView("FoundItems", new Intent(MainActivity.this, FoundItemsActivity.class)));
                    mpAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    index = 2;
                    mainTab.check(R.id.radio_button2);
                    listViews.set(2, getView("MyPost", new Intent(MainActivity.this, MyPostActivity.class)));
                    mpAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    index = 3;
                    mainTab.check(R.id.radio_button3);
                    listViews.set(3, getView("MyMessages", new Intent(MainActivity.this, MyMessagesActivity.class)));
                    mpAdapter.notifyDataSetChanged();
                    break;
                case 4:
                    index = 4;
                    mainTab.check(R.id.radio_button4);
                    listViews.set(4, getView("More", new Intent(MainActivity.this, MoreActivity.class)));
                    mpAdapter.notifyDataSetChanged();
                    break;
            }
        }
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }

//    //option菜单
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //退出对话框
    public void exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
}
