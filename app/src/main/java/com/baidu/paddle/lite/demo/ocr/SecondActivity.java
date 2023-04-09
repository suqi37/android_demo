package com.baidu.paddle.lite.demo.ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    EditText editText;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    String TAG = "suqi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        editText = findViewById(R.id.SecondEditText);
//        获取intent传来的值
        Intent intent = getIntent();
        String extraData = intent.getStringExtra("data");
        editText.setText(extraData);

        dbHelper = new DatabaseHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();


    }
    public void btn_insert_click(View view) {
        db.execSQL("INSERT INTO dataTable (name,description) VALUES ('白砂糖', '糖类,为人体提供日常活动所需的营养物质')");

    }

    public void btn_query_click(View view) {
        Cursor cursor = db.query("dataTable", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                // 处理查询结果...
                Log.e(TAG,name);
                Log.e(TAG,description);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void btn_back_click(View view) {
//        Log.e(TAG, "btn_back_click: ok");
        finish();
    }
}