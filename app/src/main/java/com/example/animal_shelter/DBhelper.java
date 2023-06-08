package com.example.animal_shelter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DBhelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "my.db";

    public DBhelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db가 생성될때 호출

        //데이터베이스 -> 테이블 -> 컬럼 -> 값

        //고객 테이블
        db.execSQL("CREATE TABLE customer (ID TEXT PRIMARY KEY, name TEXT NOT NULL, password TEXT NOT NULL," +
                " email TEXT NOT NULL, address TEXT NOT NULL, phone TEXT NOT NULL)");
        //입소 테이블
        db.execSQL("CREATE TABLE admission (admissionID INTEGER PRIMARY KEY AUTOINCREMENT, customerID TEXT NOT NULL, admissionDate TIMESTAMP NOT NULL," +
                " dogName TEXT NOT NULL, breed TEXT NOT NULL, resqueArea TEXT NOT NULL, age INTEGER NOT NULL, weight INTEGER NOT NULL," +
                "helth TEXT NOT NULL, personality TEXT NOT NULL, habit TEXT NOT NULL, fur TEXT NOT NULL, caution TEXT NOT NULL," +
                "image BLOB NOT NULL, state TEXT NOT NULL, gender TEXT NOT NULL)");
        //입양 테이블
        db.execSQL("CREATE TABLE adoption (adoptionID INTEGER PRIMARY KEY AUTOINCREMENT, admissionID INTEGER NOT NULL , customerID TEXT NOT NULL ," +
                " adoptionDate TIMESTAMP NOT NULL, state TEXT NOT NULL, reason TEXT NOT NULL)");
        //후기 테이블
        db.execSQL("CREATE TABLE review (reviewID INTEGER PRIMARY KEY AUTOINCREMENT, adoptionID INTEGER NOT NULL, customerID TEXT NOT NULL," +
                " reviewDate TIMESTAMP NOT NULL, content TEXT NOT NULL, image BLOB NOT NULL, title TEXT NOT NULL)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        onCreate(db);
    }
}
