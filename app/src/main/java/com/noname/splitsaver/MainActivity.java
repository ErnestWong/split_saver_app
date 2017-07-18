package com.noname.splitsaver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.noname.splitsaver.Login.LoginActivity;
import com.noname.splitsaver.Transaction.TransactionActivity;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 1;
    private Uri imageUri;

//    static {
//        if(!OpenCVLoader.initDebug()){
//            Log.d("OPENCV", "OpenCV not loaded");
//        } else {
//            Log.d("OPENCV", "OpenCV loaded");
//        }
//    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @OnClick(R.id.camera_btn)
    void onCameraButtonClicked() {
        dispatchTakePictureIntent();
        // to debug
//        ArrayList<Float> amounts = new ArrayList<Float>();
//        amounts.add(10f);
//        amounts.add(10f);
//        amounts.add(10f);
//        SplitActivity.startActivity(getApplicationContext(), 100, amounts);
    }

    @OnClick(R.id.history_btn)
    void onTransactionButtonClicked() {
        TransactionActivity.startActivity(getApplicationContext());
    }

    @OnClick(R.id.logout_btn)
    void onLogoutButtonClicked() {
        MainApplication.logout(getApplicationContext());
        LoginActivity.startActivity(getApplicationContext());
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            ImageActivity.startActivity(getApplicationContext(), imageUri);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
            imageUri = Uri.fromFile(photo);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }
}

//package com.noname.splitsaver;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//
//import org.opencv.android.OpenCVLoader;
//import org.opencv.core.Mat;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final String TAG = "MainActivity";
//
//    public static void startActivity(Context context) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
//
////    static {
////        if(!OpenCVLoader.initDebug()){
////            Log.d(TAG, "OpenCV not loaded");
////        } else {
////            Log.d(TAG, "OpenCV loaded");
////        }
////    }
//
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        System.loadLibrary("opencv_java");
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
////    }
//}
