package com.baidu.paddle.lite.demo.ocr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dataTable.db";
    public static final int DATABASE_VERSION = 1;
    String TAG = "suqi";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "database onCreate: ok");
        db.execSQL("DROP TABLE IF EXISTS dataTable");
        Log.e(TAG, "onUpgrade: ok" );
        db.execSQL("CREATE TABLE IF NOT EXISTS dataTable (id INTEGER,name TEXT,description TEXT )");
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
//        try {
//            Thread.sleep(20000);
//        }catch (Exception e){
//
//        }
        return resultDataList;
    }


    public List<ResultData> getByName(String[] selectionArgs) {
        SQLiteDatabase db = getReadableDatabase();
        List<ResultData> resultDataList = new ArrayList<>();
        int n = selectionArgs.length;
        String join = "?";
        for(int i=1;i<n;i++){
            join = join + ",?";
        }
        String query = "SELECT * FROM dataTable WHERE name IN (" + join + ")";
        Cursor cursor = db.rawQuery(query, selectionArgs);
        while (cursor.moveToNext()) {
            ResultData resultData = new ResultData();
            resultData.name = cursor.getString(cursor.getColumnIndex("name"));
            resultData.description = cursor.getString(cursor.getColumnIndex("description"));
//            Log.e(TAG,  cursor.getInt(cursor.getColumnIndex("id"))+resultData.name+resultData.description);
            resultDataList.add(resultData);
        }
        cursor.close();
//        try {
//            Thread.sleep(20000);
//        }catch (Exception e){
//        }
        return resultDataList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS dataTable");
        Log.e(TAG, "onUpgrade: ok" );
        onCreate(db);
    }
}
