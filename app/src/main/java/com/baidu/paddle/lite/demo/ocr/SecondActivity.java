package com.baidu.paddle.lite.demo.ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SecondActivity extends AppCompatActivity {

    EditText searchText;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //获取intent传来的值
//        Intent intent = getIntent();
//        String extraData = intent.getStringExtra("data");
//        searchText.setText(extraData);

        dbHelper = new DatabaseHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();


    }
    public void btn_insert_click(View view) {
        db.execSQL("INSERT INTO students (id,name, age) VALUES (3,'jack', 33)");
        Log.e("suqi", "btn_insert_click: OK");
    }

    public void btn_query_click(View view) {
        Log.e("suqi", "btn_query_click: ok");
        Cursor cursor = db.query("students", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int age = cursor.getInt(cursor.getColumnIndex("age"));
                // 处理查询结果...
                Log.e("suqi",String.valueOf(id));
                Log.e("suqi",name);
                Log.e("suqi",String.valueOf(age));

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void btn_back_click(View view) {
        Log.e("suqi", "btn_back_click: ok");
        finish();
    }
}