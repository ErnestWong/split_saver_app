package com.noname.splitsaver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import com.noname.splitsaver.Login.LoginActivity;
import com.noname.splitsaver.Transaction.TransactionActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 1;
    private Uri imageUri;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @OnClick(R.id.create)
    void onCameraButtonClicked() {
        dispatchTakePictureIntent();
//        SplitActivity.startActivity(getApplicationContext(), 100f, new ArrayList<Float>());
    }

    @OnClick(R.id.history)
    void onTransactionButtonClicked() {
        TransactionActivity.startActivity(getApplicationContext());
    }

    @OnClick(R.id.logout)
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