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
import android.widget.Toast;

import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.ocr.TessOCR;

import java.util.ArrayList;

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
    private ArrayList<Float> itemAmounts;
    private Float total;

    @BindView(R.id.image_view)
    ImageView imageView;

    public static void startActivity(Context context, Uri imageUri) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_IMAGE_URI, imageUri.toString());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        itemAmounts = new ArrayList<Float>();

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

    @OnClick(R.id.add_total_btn)
    void onAddTotalButtonClicked() {
//        String stringTotal = getOCRResult();
        String stringTotal = "123.45";
        try {
            this.total = Float.parseFloat(stringTotal);
            Log.d("ImageActivity", String.format("OCR total amount: %f", this.total));
        } catch (NumberFormatException e) {
            Log.e("ImageActivity", "onSplitButtonClicked: ", e);
        }
    }

    @OnClick(R.id.add_item_btn)
    void onAddItemButtonClicked() {
//        String stringAmount = getOCRResult();
        String stringAmount = "12.00";
        try {
            float amount = Float.parseFloat(stringAmount);
            itemAmounts.add(amount);
            Log.d("ImageActivity", String.format("OCR item amount: %f", amount));
        } catch (NumberFormatException e) {
            Log.e("ImageActivity", "onSplitButtonClicked: ", e);
        }
    }

    @OnClick(R.id.split_btn)
    void onSplitButtonClicked() {
        if (this.total == null) {
            Toast.makeText(this, "Please select a total first", Toast.LENGTH_SHORT).show();
        } else {
            SplitActivity.startActivity(getApplicationContext(), this.total, itemAmounts);
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
        return cleanup(result);
    }

    /**
     * Removes spaces or dollar signs from the OCR string.
     * @param result
     * @return
     */
    private String cleanup(String result) {
        return result.replaceAll("\\s+","").replaceAll("\\$", "");
    }
}
