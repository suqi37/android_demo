package com.baidu.paddle.lite.demo.ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class ShowResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        Utils.setWindowWhite(this);
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        MyAdapter adapter = new MyAdapter();
        expandableListView.setAdapter(adapter);
    }

    public void btn_back_click(View view) {
        finish();
    }


}




//        listView = findViewById(R.id.listView);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, data);
//        listView.setAdapter(adapter);