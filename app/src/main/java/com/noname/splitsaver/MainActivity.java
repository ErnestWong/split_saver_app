package com.noname.splitsaver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.noname.splitsaver.Transaction.TransactionActivity;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 1;
    private Uri imageUri;
    private Button signupBtn;

    public static final String INTENT_USER_LOGGED_IN = "INTENT_USER_LOGGED_IN";

    @OnClick(R.id.cameraButton)
    void onCameraButtonClicked() {
        dispatchTakePictureIntent();
    }

    @OnClick(R.id.sendButton)
    void onTransactionButtonClicked() {
        TransactionActivity.startActivity(getApplicationContext());
    }
    @OnClick(R.id.signupButton)
    void onSignUpButtonClicked(){
        SignupActivity.startActivity(getApplicationContext());
    }
    public static void startActivity(Context context, boolean loggedIn) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_USER_LOGGED_IN, loggedIn);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        signupBtn = (Button) findViewById(R.id.signupButton);

        Intent intent = getIntent();
        boolean loggedIn = intent.getBooleanExtra(INTENT_USER_LOGGED_IN, false);
        if (loggedIn) {
           signupBtn.setVisibility(View.INVISIBLE);
        } else {
            signupBtn.setVisibility(View.VISIBLE);
        }
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
