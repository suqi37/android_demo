package com.baidu.paddle.lite.demo.ocr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dataTable.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS dataTable (name TEXT,description TEXT )");
    }

    public List<ResultData> getAllData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + "dataTable", null);

        List<ResultData> resultDataList = new ArrayList<>();

        while (cursor.moveToNext()) {
            ResultData resultData = new ResultData();
            resultData.name = cursor.getString(cursor.getColumnIndex("name"));
            resultData.description = cursor.getString(cursor.getColumnIndex("description"));
            resultDataList.add(resultData);
        }
        cursor.close();
        return resultDataList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS dataTable");
        onCreate(db);
    }
}
