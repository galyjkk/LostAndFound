package com.galy.lostandfound.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add informations
     * @param informations
     */
    public void add(List<information> informations) {
        db.beginTransaction();	//开始事务
        try {
            for (information information : informations) {
                db.execSQL("INSERT INTO information VALUES(null, ?, ?, ?, ?)", new Object[]{information.headline, information.content, information.lostorfound, information.number});
            }
            db.setTransactionSuccessful();	//设置事务成功完成
        } finally {
            db.endTransaction();	//结束事务
        }
    }

//    /**
//     * update information's headline
//     * @param information
//     */
//    public void updateAge(information information) {
//        ContentValues cv = new ContentValues();
//        cv.put("age", information.age);
//        db.update("information", cv, "name = ?", new String[]{information.name});
//    }
//
//    /**
//     * delete old information
//     * @param information
//     */
//    public void deleteOldPerson(information information) {
//        db.delete("information", "age >= ?", new String[]{String.valueOf(information.age)});
//    }


    /**
     * query all persons, return list
     * @return List<information>
     */
    public List<information> query() {
        ArrayList<information> informations = new ArrayList<information>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            information information = new information();
            information._id = c.getInt(c.getColumnIndex("_id"));
            information.headline = c.getString(c.getColumnIndex("headline"));
            information.content = c.getString(c.getColumnIndex("content"));
            information.lostorfound = c.getInt(c.getColumnIndex("lostorfound"));
            information.number = c.getString(c.getColumnIndex("number"));
            informations.add(information);
        }
        c.close();
        return informations;
    }

    /**
     * query all persons, return cursor
     * @return	Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM information", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
