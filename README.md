# Android Demo

### 1. 简介
基于PaddleOCR的Android Demo开发, 为本学期安卓开发作业.

## 2.项目技术

使用git进行版本控制
使用material风格组件

## 3.代码追求

高内聚低耦合
代码逻辑清晰

## 4.首页搜索框:

UI：使用material风格SearchView，通过searchview_border为搜索添加圆角外框，通过ImageView的setColorFilter方法设置图标颜色
搜索功能：通过setOnQueryTextListener监听用户点击键盘搜索按钮，当用户点击后，启动pbSearchData动画遮罩层，向Handler对象sender发送消息启动搜索线程，结束pbSearchData动画，将线程执行结果发送至Handler对象receiver，receiver根据执行结果调用相应的回调方法。
跳转：如果调用搜索成功的回调方法，跳转结果展示界面。
其他特色功能：程序启动时，不自动获取焦点。同时添加OnTouchListener监听器：当用户点击搜索框外的区域时，搜索框自动失去焦点，当用户输入内容后，右侧显示快捷清空按钮

## 5.相机/相册button：

使用material风格的MaterialButton，添加对应的图标
在xml文件中绑定点击事件，解耦合，优化代码结构

## 6.Ocr识别按钮：

使用material风格的MaterialButton，添加对应的图标
在xml文件中绑定点击事件，解耦合，优化代码结构
Ocr识别功能与搜索功能共用一套Handler线程，当Ocr识别成功时调用onRunModelSuccessed回调方法
在onRunModelSuccessed中启动Ocr结果展示Activity并通过intent.putExtra()将识别结果传递给该Activity

## 7.OCR识别结果展示Actuvity：

使用material.textfield.TextInputEditText文本编辑框，使用rounded_edit_text_bg.xml实现圆角效果。
在onCreate方法中接受MainActivity传来的值并将其放置到文本框中供用户编辑
绑定setOnKeyListener监听用户按下回车键或者点击“分析”按钮进行分析

## 8.查询功能：

通过Service功能实现，Service中getByName(String s)接收带查询的字符串，进行处理
字符串处理：使用正则表达式“\\s+”删除回车、换行、空格、tab等特殊字符，使用"[,，。、]"将字符串拆分为字符串数组，放入数据库进行模糊查询
查询结果返回：在Service中定义回调接口Callback，并在SecondActivity中注册回调接口，查询结果通过回调接口传回Activity	

## 9.结果展示Activity：

通过expandableListView（最难用的组件）展示查询结果：定义MyAdapter类继承自BaseExpandableListAdapter实现适配器中需要重写的方法，将识别结果传递给expandableListView
在getGroupView和getChildView方法中设计expandableListView的基本样式。
expandableListView设置点击焦点事件，获得焦点的list item将高亮展示。

## 10.DatabaseHelper：

实现数据库的初始化工作
封装数据库查询操作
