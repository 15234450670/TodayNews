package com.bawei.todayheadline.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 类的用途：
 * Created by ：杨珺达
 * date：2017/3/20
 */

public class Sqlite extends SQLiteOpenHelper {
    public Sqlite(Context context) {
        super(context, "Look.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table hide(_id Integer primary key autoincrement,title varchar(100),image varchar(100),image1 varchar(100),image2 varchar(100))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
