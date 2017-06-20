package com.noname.splitsaver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageActivity extends AppCompatActivity {
    private static final String EXTRA_IMAGE_URI = "extraImageUri";

    @BindView(R.id.image_view)
    ImageView imageView;

    public static void startActivity(Context context, Uri imageUri) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(EXTRA_IMAGE_URI, imageUri.toString());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_IMAGE_URI)) {
            Uri imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI));
            setImageView(imageUri);
        }
    }

    public void setImageView(Uri imageUri) {
        imageView.setImageURI(imageUri);
    }
}