package com.noname.splitsaver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 1;

    @BindView(R.id.imageView)
    ImageView imageView;

    private Uri imageUri;

    @OnClick(R.id.captureButton)
    void onCaptureButtonClicked() {
        dispatchTakePictureIntent();
    }

    @OnClick(R.id.sendButton)
    void onSendButtonClicked() {
//        if (imageFile != null) {
//            NetworkManager.postImage(imageFile);
//        }
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
            setImageView();
        }
    }

    public void setImageView() {
        imageView.setImageURI(imageUri);
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
