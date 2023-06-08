package com.example.animal_shelter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserCtr {
    private DBhelper dbHelper;
    private Context context;
    private customer user;

    public void setContext(Context context) {
        this.context = context;
    }

    public customer getUser() {
        return user;
    }

    public boolean validateLogin(String id, String password) {
        dbHelper = new DBhelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 사용자 테이블에서 아이디와 비밀번호를 조회하여 일치하는 사용자가 있는지 확인합니다.
        String query = "SELECT * FROM customer WHERE ID = ? AND password = ?";
        String[] selectionArgs = {id, password};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        boolean result = cursor.getCount() > 0;

        // 리소스 해제
        cursor.close();
        db.close();

        return result;
    }

    @SuppressLint("Range")
    public void userLogin(String id, String password) {
        dbHelper = new DBhelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 사용자 테이블에서 아이디에 해당하는 사용자의 데이터를 조회합니다.
        String query = "SELECT * FROM customer WHERE ID = ?";
        String[] selectionArgs = {id};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            user = new customer();
            user.setId(cursor.getString(cursor.getColumnIndex("ID")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            user.setAddress(cursor.getString(cursor.getColumnIndex("address")));
        }

        // 리소스 해제
        cursor.close();
        db.close();
    }
}