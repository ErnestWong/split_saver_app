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
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.noname.splitsaver.ocr.TessOCR;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class ImageActivity extends AppCompatActivity {
    public static final String EXTRA_IMAGE_URI = "extraImageUri";
    public static final String EXTRA_TOTAL_AMOUNT = "extraTotalAmount";
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
        }
    }

    @OnTouch(R.id.imagesurfaceview)
    boolean onTouch(View v, MotionEvent event) {
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

    @OnClick(R.id.split_btn)
    void onSplitButtonClicked() {
        String stringTotal = getOCRResult();
        try {
            float total = Float.parseFloat(stringTotal);
            SplitActivity.startActivity(getApplicationContext(), total);
        } catch (NumberFormatException e) {
            Log.e("ImageActivity", "onSplitButtonClicked: ", e);
        }
    }

    /**
     * Crop bitmap based on Rect dimensions.
     *
     * @param src
     * @param dimensions
     * @return
     */
    public Bitmap cropBitmap(Bitmap src, Rect dimensions) {
        int width = dimensions.right - dimensions.left;
        int height = dimensions.bottom - dimensions.top;
        return Bitmap.createBitmap(src, dimensions.left, dimensions.top, width, height);
    }

    public void setImageView(Uri imageUri) {
        imageView.setImageURI(imageUri);
    }


    private String getOCRResult() {
        Rect ocrRect = rectView.getScaledRect(imageBitmap);
        Bitmap bitmap = cropBitmap(imageBitmap, ocrRect);

        if (!tessOCR.isInit()) {
            tessOCR.initOCR();
        }
        String result = tessOCR.doOCR(bitmap);
        Log.d("imageActivity", "OCR image result: " + result);
        return result;
    }
}
