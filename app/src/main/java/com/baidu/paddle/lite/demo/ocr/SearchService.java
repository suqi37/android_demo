package com.baidu.paddle.lite.demo.ocr;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class SearchService extends Service {

    String TAG = "suqi";
    private DatabaseHelper mHelper;
    private final IBinder mBinder = new MyBinder();
    private Callback mCallback;

    // 声明 MyBinder 内部类
    public class MyBinder extends Binder {
        public SearchService getService(){
            return SearchService.this;
        }
    }

    public List<ResultData> getUsers() {
        sendDataToActivity("call back");
        return mHelper.getAllData();

    }

    public SearchService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHelper = new DatabaseHelper(this);
        Log.e(TAG, "onCreate: SearchService");
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    // 定义回调接口
    public interface Callback {
        void onDataReceived(String s);
    }

    // 在需要传递数据的时候调用此方法
    public void sendDataToActivity(String s) {
        if (mCallback != null) {
            mCallback.onDataReceived(s);
        }
    }

    // 注册回调接口
    public void registerCallback(Callback callback) {
        mCallback = callback;
    }

    // 取消回调接口的注册
    public void unregisterCallback() {
        mCallback = null;
    }

}