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
    //获取Myservice实例
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
    }

    public void btn_back_click(View view) {
        finish();
    }

    public void btn_searchName_click(View view) {
        try {
            MainActivity.list.clear();
            mService.getByName(editText.getText().toString());
        }catch (Exception e){
            Toast.makeText(SecondActivity.this,"查询失败！",Toast.LENGTH_SHORT);
            Log.e(TAG, e.toString() );
        }
    }

    //实现SearchService.Callback的回调方法
    @Override
    public void onDataReceived(List<ResultData> resultDataList) {
        if(resultDataList.size() == 0){
            Toast.makeText(SecondActivity.this,"无匹配结果！",Toast.LENGTH_SHORT);
            Log.e(TAG, "size==0无匹配结果!!" );
            Toast.makeText(SecondActivity.this, "查询失败!!!", Toast.LENGTH_SHORT).show();
        }else{
            Log.e(TAG, "查询成功!!" );
            MainActivity.list = resultDataList;
            Intent intent = new Intent(SecondActivity.this, ShowResultActivity.class);
            startActivity(intent);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            mService.unregisterCallback();
            unbindService(mConnection);
        }
    }
}
