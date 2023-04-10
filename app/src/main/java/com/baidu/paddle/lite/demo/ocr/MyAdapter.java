package com.baidu.paddle.lite.demo.ocr;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseExpandableListAdapter {

    // 定义一些数据用于填充ExpandableListView
    private String[] groups = {""};
    private String[][] children = { {""} };

    public MyAdapter() {
        ResultData resultData = MainActivity.list.get(0);
        groups[0] = resultData.name;
        children[0][0] = resultData.description;

    }

    public MyAdapter(String s) {
        groups[2] = s;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setText(getChild(groupPosition, childPosition).toString());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // 设置字体大小
        textView.setPadding(200, 0, 0, 0); // 设置子项的文字起始位置
        return textView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return groups.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setText(getGroup(groupPosition).toString());
        textView.setPadding(100, 0, 0, 0); // 设置父项的文字起始位置
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // 设置字体大小
        return textView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
