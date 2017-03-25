package com.example.jiayi.intercept;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Build;
/**
 * Created by jiayi on 2015/10/25.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    public DatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory, int version){

        super(context,name,factory,version);
    }

    public DatabaseHelper(Context context,String name){

        this(context, name, VERSION);
    }

    public DatabaseHelper(Context context,String name, int version){

        this(context, name, null, version);
    }

    public void onCreate(SQLiteDatabase db){

        System.out.println("create a database");
        db.execSQL("create table wifiname(id int,name varchar(20))");
    }

    public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){

        System.out.print("update a database");
    }

}
