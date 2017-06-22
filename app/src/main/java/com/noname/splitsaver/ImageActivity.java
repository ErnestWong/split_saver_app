package com.noname.splitsaver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.noname.splitsaver.ocr.TessOCR;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageActivity extends AppCompatActivity {
    public static final String EXTRA_IMAGE_URI = "extraImageUri";
    public static RectangleView rectView;
    private TessOCR tessOCR;
    private Bitmap imageBitmap;

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

        final SurfaceView surfaceView = (SurfaceView) findViewById(R.id.imagesurfaceview);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        tessOCR = new TessOCR(this);
        rectView = new RectangleView(this);
        relativeLayout.addView(rectView);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_IMAGE_URI)) {
            Uri imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI));
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (Exception e) {
                Log.e("Error", "failed to copy bitmap image");
            }
            setImageView(imageUri);
            Log.d("imageactivity", "in SetIMageView");
        }

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        rectView.setTopLeft(x, y);
                        Log.i("TAG", "touched down: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        rectView.setBottomRight(x, y);
                        break;
                    case MotionEvent.ACTION_UP:
                        rectView.setBottomRight(x, y);
                        Log.i("TAG", "touched up: (" + x + ", " + y + ")");
                        break;
                }
                return true;
            }

        });
    }

    @OnClick(R.id.ocr_button)
    void onCaptureButtonClicked() {
        Rect ocrRect = rectView.getScaledRect(imageBitmap);
        Bitmap cropped = cropBitmap(imageBitmap, ocrRect);
        imageView.setImageBitmap(cropped);
        imageBitmap = cropped;
        getOCRResult(cropped);

    }

    public Bitmap cropBitmap(Bitmap src, Rect dimensions) {
        int width = dimensions.right - dimensions.left;
        int height = dimensions.bottom - dimensions.top;
        String s = String.format("t: %d, b: %d, l: %d, r: %d, w: %d, h: %d", dimensions.top, dimensions.bottom, dimensions.left,
                dimensions.right, width, height);
        Log.d("imageActivity", s);
        return Bitmap.createBitmap(src, dimensions.left, dimensions.top, width, height);
    }

    public void setImageView(Uri imageUri) {
        imageView.setImageURI(imageUri);
    }


    private String getOCRResult(Bitmap bitmap) {
        if (!tessOCR.isInit()) {
            tessOCR.initOCR();
        }
        String result = tessOCR.doOCR(bitmap);
        Log.d("imageActivity", "OCR image result: " + result);
        return result;
    }
}
