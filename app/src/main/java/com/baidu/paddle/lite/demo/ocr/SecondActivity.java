package com.baidu.paddle.lite.demo.ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SecondActivity extends AppCompatActivity  implements SearchService.Callback {

    EditText editText;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    String TAG = "suqi";
    List<ResultData> resultDataList;


    private SearchService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);
        editText = findViewById(R.id.SecondEditText);
        resultDataList = new ArrayList<ResultData>();

        Intent intent = new Intent(this, SearchService.class);
        startService(intent);
        // 绑定 service
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

////        获取intent传来的值
//        Intent intent = getIntent();
//        String extraData = intent.getStringExtra("data");
//        editText.setText(extraData);

//        dbHelper = new DatabaseHelper(getApplicationContext());
//        db = dbHelper.getWritableDatabase();


    }
    public void btn_insert_click(View view) {
        db.execSQL("INSERT INTO dataTable (name,description) VALUES ('白砂糖', '糖类,为人体提供日常活动所需的营养物质')");

    }
    public void btn_back_click(View view) {
//        Log.e(TAG, "btn_back_click: ok");
        finish();
    }

    public void btn_test_click(View view) {
        Log.e(TAG, MainActivity.inputText.getText().toString() );
    }


    public void btn_query_click(View view) {
        mService.sendDataToActivity(resultDataList);

        //不使用服务查询
//        Cursor cursor = db.query("dataTable", null, null, null, null, null, null);
//        if (cursor.moveToFirst()) {
//            do {
//                String name = cursor.getString(cursor.getColumnIndex("name"));
//                String description = cursor.getString(cursor.getColumnIndex("description"));
//                // 处理查询结果...
//                Log.e(TAG,name);
//                Log.e(TAG,description);
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//
    }



    @Override
    public void onDataReceived(List<ResultData> resultDataList) {
        for(ResultData resultData: resultDataList){
            Log.e(TAG, resultData. name);
            Log.e(TAG, resultData.description);
        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: ok");
            // 获取 MyService 实例
            SearchService.MyBinder binder = (SearchService.MyBinder) service;
            mService = binder.getService();

            resultDataList = binder.getUsers();
            for(ResultData resultData: resultDataList){
                Log.e(TAG, "onServiceConnected: "+ resultData.name);
                Log.e(TAG, "onServiceConnected: "+ resultData.description);
            }
            // 注册回调接口
            mService.registerCallback(SecondActivity.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            mService.unregisterCallback();
            unbindService(mConnection);
        }
    }
}