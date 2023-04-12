package com.baidu.paddle.lite.demo.ocr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    Button queryButton;
    Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Utils.setWindowWhite(this);
        setContentView(R.layout.activity_second);
        editText = findViewById(R.id.SecondEditText);
        resultDataList = new ArrayList<ResultData>();
        queryButton = findViewById(R.id.btn_query_second);
        backButton = findViewById(R.id.btn_back);
        dbHelper = new DatabaseHelper(SecondActivity.this);
        db = dbHelper.getWritableDatabase();

        //        获取intent传来的值
        Intent lastIntent = getIntent();
        editText.setText(lastIntent.getStringExtra("data"));

//        ImageView searchHintIcon = backButton.findViewById(androidx.appcompat.R.id.icon);
//        searchHintIcon.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary));


        Intent intent = new Intent(this, SearchService.class);
        startService(intent);
        // 绑定 service
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);



        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // Todo: 处理回车键按下事件
                    btn_searchName_click(queryButton);
                    return true;
                }
                return false;
            }
        });

//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
////                    btn_searchName_click(queryButton);
//                    Log.e(TAG, "second listener called");
//                    try {
//                        mService.getByName(editText.getText().toString());
//                    }catch (Exception e){
//                        Log.e(TAG, e.toString() );
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });




    }

    public void btn_insert_click(View view) {
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
    }

    public void btn_back_click(View view) {
        finish();
    }

    public void btn_searchName_click(View view) {
//        Log.e(TAG, "btn_searchName_click: "+editText.getText().toString());
        try {
            mService.getByName(editText.getText().toString());
        }catch (Exception e){
            Log.e(TAG, e.toString() );
        }
//        for(ResultData resultData: resultDataList){
//            Log.e(TAG, "btn_query_click: "+resultData. name);
//            Log.e(TAG, "btn_query_click: "+resultData.description);
//        }
    }


    public void btn_query_click(View view) {
        resultDataList =  mService.getUsers();
//        for(ResultData resultData: resultDataList){
//            Log.e(TAG, "btn_query_click: "+resultData. name);
//            Log.e(TAG, "btn_query_click: "+resultData.description);
//        }
    }

    @Override
    public void onDataReceived(List<ResultData> resultDataList) {
        if(resultDataList.size() == 0){
            Toast.makeText(SecondActivity.this,"无匹配结果！",Toast.LENGTH_SHORT);
        }else{
            MainActivity.list = resultDataList;
            Intent intent = new Intent(SecondActivity.this, ShowResultActivity.class);
            startActivity(intent);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: ok");
            // 获取 MyService 实例
            SearchService.MyBinder binder = (SearchService.MyBinder) service;
            mService = binder.getService();
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