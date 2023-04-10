package com.baidu.paddle.lite.demo.ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class ShowResultActivity extends AppCompatActivity {

    String[] data = {"1232433","124123","1224423","12223","14223","123423","14223","123423","12323","12653"};
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        Utils.setWindowWhite(this);
//        listView = findViewById(R.id.listView);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, data);
//        listView.setAdapter(adapter);
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        MyAdapter adapter = new MyAdapter("1231");
        expandableListView.setAdapter(adapter);



    }
}