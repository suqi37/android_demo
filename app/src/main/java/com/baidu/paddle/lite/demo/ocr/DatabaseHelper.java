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
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (2, '乳化剂', '能够使水和油混合的化学物质。常用于奶油、冰淇淋、沙拉酱等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (3, '防腐剂', '能够抑制细菌和真菌的生长。常用于肉类制品、果汁、面包等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (4, '甜味剂', '一种人工合成的化学物质，用于增加食品的甜味。常用于食品和饮料中，例如低热量饮料、口香糖等。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (5, '色素', '用于食品中的人工合成化学物质，用于改变食品的颜色。常用于饼干、糖果、冰淇淋等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (6, '增稠剂', '一种化学物质，用于增加食品的黏稠度。常用于调味品、沙拉酱、饼干等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (7, '抗氧化剂', '能够防止食品被氧化和腐败的化学物质。常用于油脂、肉制品、腌渍食品等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (8, '酸味剂', '用于食品的化学物质，可以增加食品的酸味。常用于饮料、果汁、糖果等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (9, '发酵剂', '用于加速面团或面粉发酵的化学物质。常用于面包、蛋糕、饼干等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (10, '香精', '一种合成或天然的化学物质，用于增加或改变食品的味道。常用于肉制品、调味品、烘焙食品等产品中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (11, '白砂糖', '白色结晶状固体，具有甜味，广泛应用于烘焙食品和饮料中。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (12, '水', '水是一种无色无味的液体，人体必需品，每天饮用2L水是保持身体健康的基本要求。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (13, '山杏仁', '山杏仁是一种营养丰富的坚果，富含蛋白质、脂肪、维生素等，但可能会引起过敏反应，需谨慎食用。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (14, '聚甘油脂肪酸酯', '聚甘油脂肪酸酯是一种食品添加剂，用于改善食品质地和口感，目前未发现对人体有害。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (15, '碳酸氢钠', '碳酸氢钠是一种常用的食品添加剂，可用于调节食品的酸碱度和发酵作用，但过量摄入可能会对人体造成不良影响。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (16, '柠檬酸', '柠檬酸是一种常用的食品添加剂，可用于增酸和保鲜，但过量摄入可能会对人体造成不良影响。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (17, '酱油（含苯甲酸钠）', '酱油是一种常用的调味料，含有苯甲酸钠等添加剂，过量摄入可能会对人体造成不良影响。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (18, '植物油', '植物油是一种脂肪类食品，富含不饱和脂肪酸和维生素E等营养成分，但过量摄入可能会对人体造成不良影响。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (19, '食用盐', '食用盐是一种常用的调味料，过量摄入可能会对人体造成高血压等不良影响，建议每日摄入不超过6g。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (20, '芥末粉', '芥末粉是一种调味料，具有提神醒脑、增进食欲等功效，但过量食用可能会对胃肠道造成刺激。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (22, '天然胡萝卜素', '天然胡萝卜素是一种黄色素，具有丰富的抗氧化成分，但过量摄入可能会对人体造成不良影响。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (23, '亮蓝', '亮蓝是一种人工合成的食品色素，目前已被多个国家禁止使用，长期食用可能会对人体造成伤害')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (24, '酱油', '酱油是一种常用的调味料，含有苯甲酸钠等添加剂，过量摄入可能会对人体造成不良影响。')");
            db.execSQL("INSERT INTO dataTable (id, name, description) VALUES (25, '苯甲酸钠', '苯甲酸钠是一种有机化合物，通常用作食品防腐剂。它可以抑制食品中的细菌、酵母和霉菌的生长，从而延长食品的保质期。然而，苯甲酸钠过多的摄入可能会对人体健康产生不利影响，如引起过敏、皮肤刺激、头痛、呕吐等。因此，建议人们适量食用含有苯甲酸钠的食品，并注意检查食品标签中的成分列表。')");
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
