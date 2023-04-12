package com.baidu.paddle.lite.demo.ocr;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseExpandableListAdapter {

    // 定义一些数据用于填充ExpandableListView
    private String[] groups;
    private String[][] children = { {""} };

    public MyAdapter() {
        int n = MainActivity.list.size();
        groups = new String[n];
        children = new String[n][1];
        for(int i=0;i<n;i++){
            ResultData resultData = MainActivity.list.get(i);
            groups[i] = resultData.name;
            children[i][0] = resultData.description;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setText(getGroup(groupPosition).toString());
        // 加粗字体类型
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        // 设置字体类型
        textView.setTypeface(boldTypeface);
        textView.setPadding(100, 30, 20, 30); // 设置父项的文字起始位置
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // 设置字体大小
        textView.setTextColor(R.color.black);         //字体颜色
//        textView.setBackgroundColor(R.color.group_background_color);  //背景颜色
        return textView;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        TextView textView = new TextView(parent.getContext());
        textView.setText(getChild(groupPosition, childPosition).toString());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // 设置字体大小
        textView.setLetterSpacing(0.05f);
        textView.setLineSpacing(10f, 1f);
        textView.setPadding(150, 10, 75, 10); // 设置子项的文字起始位置
        textView.setTextColor(R.color.colorAccent);         //字体颜色
//        textView.setBackgroundColor(R.color.child_background_color);    //背景颜色
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
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
