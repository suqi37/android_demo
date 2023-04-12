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
        try {
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (1, '白砂糖', '糖类,人体活动主要的供能物质')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (2, '乳化剂', '能够使水和油混合的化学物质。常用于奶油、冰淇淋、沙拉酱等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (3, '防腐剂', '能够抑制细菌和真菌的生长。常用于肉类制品、果汁、面包等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (4, '甜味剂', '一种人工合成的化学物质，用于增加食品的甜味。常用于食品和饮料中，例如低热量饮料、口香糖等。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (5, '色素', '用于食品中的人工合成化学物质，用于改变食品的颜色。常用于饼干、糖果、冰淇淋等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (6, '增稠剂', '一种化学物质，用于增加食品的黏稠度。常用于调味品、沙拉酱、饼干等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (7, '抗氧化剂', '能够防止食品被氧化和腐败的化学物质。常用于油脂、肉制品、腌渍食品等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (8, '酸味剂', '用于食品的化学物质，可以增加食品的酸味。常用于饮料、果汁、糖果等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (9, '发酵剂', '用于加速面团或面粉发酵的化学物质。常用于面包、蛋糕、饼干等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (10, '香精', '一种合成或天然的化学物质，用于增加或改变食品的味道。常用于肉制品、调味品、烘焙食品等产品中。')");

        }catch (Exception e){

        }
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
            resultDataList.add(resultData);
        }
        cursor.close();

        return resultDataList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS dataTable");
        Log.e(TAG, "onUpgrade: ok" );
        onCreate(db);
    }
}
