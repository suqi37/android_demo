package com.baidu.paddle.lite.demo.ocr;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.content.res.AssetManager;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "suqi";
    public static final int OPEN_GALLERY_REQUEST_CODE = 0;
    public static final int TAKE_PHOTO_REQUEST_CODE = 1;

    public static final int REQUEST_LOAD_MODEL = 0;
    public static final int REQUEST_RUN_MODEL = 1;
    public static final int REQUEST_SEARCH = 2;

    public static final int RESPONSE_LOAD_MODEL_SUCCESSED = 0;
    public static final int RESPONSE_LOAD_MODEL_FAILED = 1;
    public static final int RESPONSE_RUN_MODEL_SUCCESSED = 2;
    public static final int RESPONSE_RUN_MODEL_FAILED = 3;
    public static final int RESPONSE_SEARCH_SUCCESSED = 4;
    public static final int RESPONSE_SEARCH_FAILED = 5;

    protected ProgressDialog pbLoadModel = null;
    protected ProgressDialog pbRunModel = null;
    protected ProgressDialog pbSearchData = null;


    protected Handler receiver = null; // Receive messages from worker thread
    protected Handler sender = null; // Send command to worker thread
    protected HandlerThread worker = null; // Worker thread to load&run model&search

    protected Button searchButton;
    protected ImageView ivInputImage;
    public static SearchView inputText;

    // Model settings of ocr
    protected String modelPath = "";
    protected String labelPath = "";
    protected String imagePath = "";
    protected int cpuThreadNum = 1;
    protected String cpuPowerMode = "";
    protected String inputStringText = "";
    protected int detLongSize = 960;
    protected float scoreThreshold = 0.1f;
    private String currentPhotoPath;
    private AssetManager assetManager = null;


    protected Predictor predictor = new Predictor();
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    public static List<ResultData> list;

    private Bitmap cur_predict_image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Clear all setting items to avoid app crashing due to the incorrect settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
//        Log.e(TAG, "onCreate: ");
        Utils.setWindowWhite(this);

        dbHelper = new DatabaseHelper(MainActivity.this);
        db = dbHelper.getWritableDatabase();
        list = new ArrayList<ResultData>();
//        searchButton = findViewById(R.id.btn_search_first);
        ivInputImage = findViewById(R.id.iv_input_image);
        inputText = findViewById(R.id.search_input_text);



// 设置SearchView控件的属性
        inputText.setFocusable(false); // 不获取焦点
        inputText.setIconified(true); // 默认为true时即不展开

// 获取SearchView所在的布局实例
        View container = findViewById(R.id.main_layout);

// 为布局添加OnTouchListener监听器
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 当用户点击布局以外的区域时执行
                // 使SearchView失去焦点
                inputText.clearFocus();
                // 显示搜索框中的叉号
//                inputText.findViewById(R.id.search_close_btn).setVisibility(View.VISIBLE);
                return false;
            }
        });



//        inputText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    // Todo: 处理回车键按下事件
//                    btn_first_search_click(searchButton);
//                    return true;
//                }
//                return false;
//            }
//        });

            inputText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 当用户提交搜索查询时执行，query参数为搜索框中的文本内容
//                Log.e(TAG, "onQueryTextSubmit: " + query);
                if(query.isEmpty()){

                Log.e(TAG, "input empty");
                    Toast.makeText(MainActivity.this, "输入内容不能为空!!!", Toast.LENGTH_SHORT).show();
                }else{
                    inputStringText = query;
                    btn_first_search_click();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 当搜索框文本更改时执行，newText参数为搜索框中的最新文本内容
//                Log.e(TAG, "onQueryTextChange: " + newText);

                return false;
            }
        });

//        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
////                    btn_first_search_click(searchButton);
//                    pbSearchData = ProgressDialog.show(MainActivity.this, "", "正在查询......", false, false);
//                    sender.sendEmptyMessage(REQUEST_SEARCH);
//                    Log.e(TAG, "first listener called");
//                    return true;
//                }
//                return false;
//            }
//        });



        receiver = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case RESPONSE_LOAD_MODEL_SUCCESSED:
                        if (pbLoadModel != null && pbLoadModel.isShowing()) {
                            pbLoadModel.dismiss();
                        }
                        onLoadModelSuccessed();
                        break;
                    case RESPONSE_LOAD_MODEL_FAILED:
                        if (pbLoadModel != null && pbLoadModel.isShowing()) {
                            pbLoadModel.dismiss();
                        }
                        Toast.makeText(MainActivity.this, "Load model failed!", Toast.LENGTH_SHORT).show();
                        onLoadModelFailed();
                        break;
                    case RESPONSE_RUN_MODEL_SUCCESSED:
                        if (pbRunModel != null && pbRunModel.isShowing()) {
                            pbRunModel.dismiss();
                        }
                        onRunModelSuccessed();
                        break;
                    case RESPONSE_RUN_MODEL_FAILED:
                        if (pbRunModel != null && pbRunModel.isShowing()) {
                            pbRunModel.dismiss();
                        }
                        Toast.makeText(MainActivity.this, "Run model failed!", Toast.LENGTH_SHORT).show();
                        onRunModelFailed();
                        break;
                    case RESPONSE_SEARCH_SUCCESSED:
                        if (pbSearchData != null && pbSearchData.isShowing()) {
                            pbSearchData.dismiss();
                        }
                        Toast.makeText(MainActivity.this, "查询成功!!!", Toast.LENGTH_SHORT).show();
                        onSearchSuccessed();
                        break;
                    case RESPONSE_SEARCH_FAILED:
                        if (pbSearchData != null && pbSearchData.isShowing()) {
                            pbSearchData.dismiss();
                        }
                        Toast.makeText(MainActivity.this, "查询失败!!!", Toast.LENGTH_SHORT).show();
                        onSearchFailed();
                        break;
                    default:
                        break;
                }
            }
        };

        worker = new HandlerThread("Predictor Worker");
        worker.start();

        sender = new Handler(worker.getLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case REQUEST_LOAD_MODEL:
                        // Load model and reload test image
                        if (onLoadModel()) {
                            receiver.sendEmptyMessage(RESPONSE_LOAD_MODEL_SUCCESSED);
                        } else {
                            receiver.sendEmptyMessage(RESPONSE_LOAD_MODEL_FAILED);
                        }
                        break;
                    case REQUEST_RUN_MODEL:
                        // Run model if model is loaded
                        if (onRunModel()) {
                            receiver.sendEmptyMessage(RESPONSE_RUN_MODEL_SUCCESSED);
                        } else {
                            receiver.sendEmptyMessage(RESPONSE_RUN_MODEL_FAILED);
                        }
                        break;
                    case REQUEST_SEARCH:
                        // Run model if model is loaded
                        if (onSearchModel()) {
                            receiver.sendEmptyMessage(RESPONSE_SEARCH_SUCCESSED);
                        } else {
                            receiver.sendEmptyMessage(RESPONSE_SEARCH_FAILED);
                        }
                        break;
                    default:
                        break;
                }
            }
        };





    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean settingsChanged = false;
        boolean model_settingsChanged = false;
        String model_path = sharedPreferences.getString(getString(R.string.MODEL_PATH_KEY),
                getString(R.string.MODEL_PATH_DEFAULT));
        String label_path = sharedPreferences.getString(getString(R.string.LABEL_PATH_KEY),
                getString(R.string.LABEL_PATH_DEFAULT));
        String image_path = sharedPreferences.getString(getString(R.string.IMAGE_PATH_KEY),
                getString(R.string.IMAGE_PATH_DEFAULT));
        model_settingsChanged |= !model_path.equalsIgnoreCase(modelPath);
        settingsChanged |= !label_path.equalsIgnoreCase(labelPath);
        settingsChanged |= !image_path.equalsIgnoreCase(imagePath);
        int cpu_thread_num = Integer.parseInt(sharedPreferences.getString(getString(R.string.CPU_THREAD_NUM_KEY),
                getString(R.string.CPU_THREAD_NUM_DEFAULT)));
        model_settingsChanged |= cpu_thread_num != cpuThreadNum;
        String cpu_power_mode =
                sharedPreferences.getString(getString(R.string.CPU_POWER_MODE_KEY),
                        getString(R.string.CPU_POWER_MODE_DEFAULT));
        model_settingsChanged |= !cpu_power_mode.equalsIgnoreCase(cpuPowerMode);

        int det_long_size = Integer.parseInt(sharedPreferences.getString(getString(R.string.DET_LONG_SIZE_KEY),
                getString(R.string.DET_LONG_SIZE_DEFAULT)));
        settingsChanged |= det_long_size != detLongSize;
        float score_threshold =
                Float.parseFloat(sharedPreferences.getString(getString(R.string.SCORE_THRESHOLD_KEY),
                        getString(R.string.SCORE_THRESHOLD_DEFAULT)));
        settingsChanged |= scoreThreshold != score_threshold;
        if (settingsChanged) {
            labelPath = label_path;
            imagePath = image_path;
            detLongSize = det_long_size;
            scoreThreshold = score_threshold;
            set_img();
        }
        if (model_settingsChanged) {
            modelPath = model_path;
            cpuThreadNum = cpu_thread_num;
            cpuPowerMode = cpu_power_mode;
            // Update UI
//            tvInputSetting.setText("Model: " + modelPath.substring(modelPath.lastIndexOf("/") + 1) + "\nOPENCL: " + cbOpencl.isChecked() + "\nCPU Thread Num: " + cpuThreadNum + "\nCPU Power Mode: " + cpuPowerMode);
//            tvInputSetting.scrollTo(0, 0);
            // Reload model if configure has been changed
            loadModel();
        }
    }

    public void loadModel() {
        pbLoadModel = ProgressDialog.show(this, "", "loading model...", false, false);
        sender.sendEmptyMessage(REQUEST_LOAD_MODEL);
    }

    public boolean onLoadModel() {
        if (predictor.isLoaded()) {
            predictor.releaseModel();
        }
        return predictor.init(MainActivity.this, modelPath, labelPath,0, cpuThreadNum,
                cpuPowerMode,
                detLongSize, scoreThreshold);
    }

    public boolean onRunModel() {
        int run_det = 1;    //检测
        int run_cls = 0;    //分类
        int run_rec = 1;    //识别
        return predictor.isLoaded() && predictor.runModel(run_det, run_cls, run_rec);
    }

    public void onLoadModelSuccessed() {
        // Load test image from path and run model
//        tvInputSetting.setText("Model: " + modelPath.substring(modelPath.lastIndexOf("/") + 1) + "\nOPENCL: " + cbOpencl.isChecked() + "\nCPU Thread Num: " + cpuThreadNum + "\nCPU Power Mode: " + cpuPowerMode);
//        tvInputSetting.scrollTo(0, 0);
//        tvStatus.setText("STATUS: load model successed");

    }

    public void onLoadModelFailed() {
//        tvStatus.setText("STATUS: load model failed");
    }

    public void onRunModelSuccessed() {
//        tvStatus.setText("STATUS: run model successed");
        // Obtain results and update UI
//        tvInferenceTime.setText("Inference time: " + predictor.inferenceTime() + " ms");
        Bitmap outputImage = predictor.outputImage();
        if (outputImage != null) {
            ivInputImage.setImageBitmap(outputImage);
        }
        //        tvOutputResult.setText(predictor.outputResult());
//        tvOutputResult.scrollTo(0, 0);
        ivInputImage.setImageBitmap(cur_predict_image);
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("data", predictor.outputResult());
        startActivity(intent);
    }

    public void onRunModelFailed() {
//        tvStatus.setText("STATUS: run model failed");
        Toast.makeText(getApplicationContext(),"识别失败!",Toast.LENGTH_SHORT);
    }

    public void set_img() {
        // Load test image from path and run model
        try {
            assetManager = getAssets();
            InputStream in = assetManager.open(imagePath);
            Bitmap bmp = BitmapFactory.decodeStream(in);
            cur_predict_image = bmp;
            ivInputImage.setImageBitmap(bmp);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Load image failed!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onSettingsClicked() {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_options, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isLoaded = predictor.isLoaded();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.settings:
                if (requestAllPermissions()) {
                    // Make sure we have SDCard r&w permissions to load model from SDCard
                    onSettingsClicked();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (predictor != null) {
            predictor.releaseModel();
        }
        worker.quit();
        super.onDestroy();
    }

    private boolean requestAllPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    0);
            return false;
        }
        return true;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE);
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("MainActitity", ex.getMessage(), ex);
                Toast.makeText(MainActivity.this,
                        "Create Camera temp file failed: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.e(TAG, "FILEPATH " + getExternalFilesDir("Pictures").getAbsolutePath());
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.baidu.paddle.lite.demo.ocr.fileprovider",
                        photoFile);
                currentPhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE);
                Log.i(TAG, "startActivityForResult finished");
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".bmp",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public void btn_choice_img_click(View view) {
        if (requestAllPermissions()) {
            openGallery();
        }
    }

    public void btn_take_photo_click(View view) {
        if (requestAllPermissions()) {
            takePhoto();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case OPEN_GALLERY_REQUEST_CODE:
                    if (data == null) {
                        break;
                    }
                    try {
                        ContentResolver resolver = getContentResolver();
                        Uri uri = data.getData();
                        Bitmap image = MediaStore.Images.Media.getBitmap(resolver, uri);
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = managedQuery(uri, proj, null, null, null);
                        cursor.moveToFirst();
                        if (image != null) {
                            cur_predict_image = image;
                            ivInputImage.setImageBitmap(image);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                    break;
                case TAKE_PHOTO_REQUEST_CODE:
                    if (currentPhotoPath != null) {
                        ExifInterface exif = null;
                        try {
                            exif = new ExifInterface(currentPhotoPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);
                        Log.i(TAG, "rotation " + orientation);
                        Bitmap image = BitmapFactory.decodeFile(currentPhotoPath);
                        image = Utils.rotateBitmap(image, orientation);
                        if (image != null) {
                            cur_predict_image = image;
                            ivInputImage.setImageBitmap(image);
                        }
                    } else {
                        Log.e(TAG, "currentPhotoPath is null");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void btn_run_model_click(View view) {
        Bitmap image = ((BitmapDrawable) ivInputImage.getDrawable()).getBitmap();
        if (image == null) {
        } else if (!predictor.isLoaded()) {
        } else {
//            tvStatus.setText("STATUS: run model ...... ");
            predictor.setInputImage(image);
            runModel();
        }
    }

    public void runModel() {
        pbRunModel = ProgressDialog.show(this, "", "running model...", false, false);
        sender.sendEmptyMessage(REQUEST_RUN_MODEL);
    }


    public void btn_first_search_click(){
        pbSearchData = ProgressDialog.show(this, "", "正在查询......", false, false);
        sender.sendEmptyMessage(REQUEST_SEARCH);
    }

    //查询操作
    public boolean onSearchModel() {
        try {
            Thread.sleep(1000);
        }catch (Exception e){

        }
        list.clear();
        try {
            list = dbHelper.getByName(inputStringText.split("、"));
        }catch (Exception e){
            return false;
        }
        if(list.size() == 0){
            return  false;
        }else{
            return true;
        }
    }

    public void onSearchSuccessed() {
        if(list.size() != 0){
            Intent intent = new Intent(MainActivity.this, ShowResultActivity.class);
            startActivity(intent);
        }
    }

    public void onSearchFailed() {

    }


}
