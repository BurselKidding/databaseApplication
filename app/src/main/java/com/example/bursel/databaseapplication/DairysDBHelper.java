package com.example.bursel.databaseapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.UserDictionary;

/**
 * Created by hbs on 2015-10-5.
 */
public class DairysDBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "dairysdb";//数据库名字
    private final static int DATABASE_VERSION = 1;//数据库版本

    //建表SQL
    private final static String SQL_CREATE_DATABASE = "CREATE TABLE " + Dairys.Dairy.TABLE_NAME + " (" +
            Dairys.Dairy._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Dairys.Dairy.COLUMN_NAME_TITLE + " TEXT" + "," +
            Dairys.Dairy.COLUMN_NAME_BODY + " TEXT" +  " )";

    //删表SQL
    private final static String SQL_DELETE_DATABASE = "DROP TABLE IF EXISTS " + Dairys.Dairy.TABLE_NAME;

    public DairysDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //当数据库升级时被调用，首先删除旧表，然后调用OnCreate()创建新表
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        onCreate(sqLiteDatabase);
    }
}