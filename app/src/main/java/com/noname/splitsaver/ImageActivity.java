package com.noname.splitsaver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.ocr.ImageProcess;
import com.noname.splitsaver.ocr.OCRRegion;
import com.noname.splitsaver.ocr.TessOCR;
//import com.noname.splitsaver.ocr.Testing;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;



public class ImageActivity extends AppCompatActivity {
    public static final String EXTRA_IMAGE_URI = "extraImageUri";
    public static final String EXTRA_TOTAL_AMOUNT = "extraTotalAmount";
    public static RectangleView rectView;
    private TessOCR tessOCR;
    private Bitmap receiptBitmap;
    private Bitmap subBitmap;
    private ArrayList<Float> itemAmounts;
    private Float total;
    private List<OCRRegion> ocrRegions;

    private TouchMode mode;

    @BindView(R.id.image_view)
    ImageView imageView;

    @BindView(R.id.select_rect_toggle_btn)
    ToggleButton selectRectToggleButton;

    @BindView(R.id.cancel_btn)
    ImageButton cancelButton;

    @BindView(R.id.save_receipt_btn)
    ImageButton saveReceiptButton;

    @BindView(R.id.add_amount_btn)
    ToggleButton addAmountToggleButton;

    @BindView(R.id.image_activity_progress_bar)
    ProgressBar progressBar;

    public static void startActivity(Context context, Uri imageUri) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_IMAGE_URI, imageUri.toString());
        context.startActivity(intent);
    }

    private void setTouchMode(TouchMode mode) {
        this.mode = mode;
        showCorrespondingButtons();
    }

    private void showCorrespondingButtons() {
        Log.d("ImageActivity", this.mode.toString());
        switch (this.mode) {
            case INITIAL_MODE:
                // show cancel button, show select rect button.
                cancelButton.setVisibility(View.VISIBLE);
                selectRectToggleButton.setVisibility(View.VISIBLE);
                selectRectToggleButton.setChecked(false);
                saveReceiptButton.setVisibility(View.GONE);
                addAmountToggleButton.setVisibility(View.GONE);
                break;
            case SELECT_RECT_MODE:
                // show cancel button, show DONE button
                cancelButton.setVisibility(View.VISIBLE);
                selectRectToggleButton.setVisibility(View.VISIBLE);
                selectRectToggleButton.setChecked(true);
                saveReceiptButton.setVisibility(View.GONE);
                addAmountToggleButton.setVisibility(View.GONE);
                break;
//            case SUB_BITMAP_MODE:
//                // show cancel button, add total button, select item button
//                cancelButton.setVisibility(View.VISIBLE);
//                saveReceiptButton.setVisibility(View.GONE);
//                selectRectToggleButton.setVisibility(View.GONE);
//                addAmountToggleButton.setVisibility(View.VISIBLE);
//                break;
            case SELECT_ITEM_MODE:
                // show cancel button, save button, add total button, select item button
                cancelButton.setVisibility(View.VISIBLE);
                saveReceiptButton.setVisibility(View.VISIBLE);
                selectRectToggleButton.setVisibility(View.GONE);
                addAmountToggleButton.setVisibility(View.VISIBLE);
                break;
            case SELECT_TOTAL_MODE:
                // cancel button, save button, add total button, select item button
                cancelButton.setVisibility(View.VISIBLE);
                saveReceiptButton.setVisibility(View.VISIBLE);
                selectRectToggleButton.setVisibility(View.GONE);
                addAmountToggleButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onResume() {
//        this.setTouchMode(TouchMode.INITIAL_MODE);
        super.onResume();
        this.resetItemTotals();
        progressBar.setVisibility(View.GONE);
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        itemAmounts = new ArrayList<Float>();
        setTouchMode(TouchMode.INITIAL_MODE);
        progressBar.setVisibility(View.GONE);

        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        tessOCR = new TessOCR(this);
        rectView = new RectangleView(this);
        relativeLayout.addView(rectView);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_IMAGE_URI)) {
            Uri imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI));
            try {
                receiptBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (Exception e) {
                Log.e("Error", "failed to copy bitmap image");
            }
            setImageView(imageUri);
        }
    }

    @OnTouch(R.id.imagesurfaceview)
    boolean onTouch(View v, MotionEvent event) {
        switch (mode) {
            case SELECT_ITEM_MODE:
                return handleSelectItemModeTouch(v, event);
            case SELECT_RECT_MODE:
                return handleSelectRectModeTouch(v, event);
            case SELECT_TOTAL_MODE:
                return handleSelectTotalModeTouch(v, event);
            default:
                return false;
        }
    }



    boolean handleSelectItemModeTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                try {
                    String itemAmount = findSelectedRegionAmount(x, y);
                    if (itemAmount != null) {
                        Log.d("ImageActivity", "ocr result: " + itemAmount);
                        itemAmounts.add(Float.parseFloat(itemAmount));
                        showToast("Successfully added item amount.");
                    } else {
                        showToast("No amount found.");
                    }
                } catch (Exception e) {
                    showToast("Error occurred while adding item");
                    Log.e("ImageActivity", "Error when adding item : " + e.getMessage());
                }
                break;
        }
        return true;
    }

    boolean handleSelectTotalModeTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                try {
                    String totalAmount = findSelectedRegionAmount(x, y);
                    if (totalAmount != null) {
                        Log.d("ImageActivity", "ocr result: " + totalAmount);
                        this.total = Float.parseFloat(totalAmount);
                        showToast("Successfully added total.");
                    } else {
                        showToast("No amount found.");
                    }
                } catch (Exception e) {
                    showToast("Error occurred while adding total.");
                    Log.e("ImageActivity", "Error when adding item : " + e.getMessage());
                }
                break;
        }
        return true;
    }

    boolean handleSelectRectModeTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rectView.setTopLeft(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                rectView.setBottomRight(x, y);
                break;
            case MotionEvent.ACTION_UP:
                rectView.setBottomRight(x, y);
                break;
        }
        return true;
    }

    @OnClick(R.id.add_amount_btn)
    void addAmountToggleButtonClicked() {
        // CHECKED -> ADD ITEM
        // UNCHECKED -> ADD TOTAL
        if (addAmountToggleButton.isChecked()) {
            setTouchMode(TouchMode.SELECT_ITEM_MODE);
        } else {
            setTouchMode(TouchMode.SELECT_TOTAL_MODE);
        }

    }

    @OnClick(R.id.select_rect_toggle_btn)
    void onSelectRectToggleButtonClicked()  {
        if (!selectRectToggleButton.isChecked()) { // user made a rect selection and pressed Done
            doneDrawingBoundingRect();
        } else {
            // otherwise user wants to be in select mode
            setTouchMode(TouchMode.SELECT_RECT_MODE);
        }
    }

    void doneDrawingBoundingRect() {
        if (rectView.drawn()) {
            setTouchMode(TouchMode.SELECT_TOTAL_MODE);
            subBitmap = cropBitmap(receiptBitmap, rectView.getScaledRect(receiptBitmap));
            new ImageProcessTask().execute(subBitmap);
        } else {
            showToast("Please draw a rectangle  first.");
        }
    }

    @OnClick(R.id.cancel_btn)
    void onCancelButtonClicked() {
        rectView.reset();
        setImageView(receiptBitmap);
        reset();

        switch (this.mode) {
            case SELECT_ITEM_MODE:
                setTouchMode(TouchMode.INITIAL_MODE);
                break;
            case SELECT_TOTAL_MODE:
                setTouchMode(TouchMode.INITIAL_MODE);
                break;
            case SELECT_RECT_MODE:
                setTouchMode(TouchMode.INITIAL_MODE);
                break;
            case INITIAL_MODE:
                MainActivity.startActivity(getApplicationContext());
                break;
        }
    }

    @OnClick(R.id.save_receipt_btn)
    void onReceiptSaveButtonClicked() {
        if (this.total != null) {
            SplitActivity.startActivity(getApplicationContext(), this.total, this.itemAmounts);
        } else {
            showToast("Please select a total first");
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

    public void setImageView(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Point getScaledCoordinates(int eventX, int eventY) {
        float[] eventXY = new float[] {eventX, eventY};

        Matrix invertMatrix = new Matrix();
        imageView.getImageMatrix().invert(invertMatrix);

        invertMatrix.mapPoints(eventXY);
        int x = Integer.valueOf((int)eventXY[0]);
        int y = Integer.valueOf((int)eventXY[1]);

        return new Point(x, y);

    }

    private String findSelectedRegionAmount(int x, int y) {
        Point point = getScaledCoordinates(x, y);

        for (OCRRegion region : ocrRegions) {
            org.opencv.core.Rect r = region.getRect();
            if (r.contains(point)) {
                return region.getOCR();
            }
        }
        return  null;
    }

    private void reset()  {
       resetItemTotals();
        if (this.ocrRegions != null) this.ocrRegions.clear();
    }
    private void resetItemTotals() {
        this.total = null;
        if (this.itemAmounts != null) this.itemAmounts.clear();
    }

    private class ImageProcessTask extends AsyncTask<Bitmap, Integer, ImageProcessTaskResult> {
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected ImageProcessTaskResult doInBackground(Bitmap... bitmap) {
            TessOCR tessOCR = new TessOCR(getApplicationContext());
            ImageProcess imgProcess = new ImageProcess(subBitmap, tessOCR);
            return new ImageProcessTaskResult(imgProcess.getBitmap(), imgProcess.getOcrRegions());
        }

        protected void onPostExecute(ImageProcessTaskResult result) {
            subBitmap = result.bitmap;
            ocrRegions = result.ocrRegions;
            setImageView(subBitmap);
            rectView.reset();
            progressBar.setVisibility(View.GONE);
        }
    }

    private class ImageProcessTaskResult {
        Bitmap bitmap;
        List<OCRRegion> ocrRegions;

        public ImageProcessTaskResult(Bitmap bitmap, List <OCRRegion> ocrRegions) {
            this.bitmap = bitmap;
            this.ocrRegions = ocrRegions;
        }
    }
}
