package com.noname.splitsaver.ocr;

import android.graphics.Bitmap;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class OCRRegion {
    private Rect rect;
    private Bitmap bitmap;
    private Mat mat;
    private String ocrString;

    public OCRRegion(Rect rect, Bitmap bitmap, Mat mat, TessOCR tessOCR) {
        this.rect = rect;
        this.bitmap = bitmap;
        this.mat = mat;
        if (!tessOCR.isInit()) {
            tessOCR.initOCR();
        }
        this.ocrString = tessOCR.doOCR(this.bitmap);
    }

    public Rect getRect() {
        return this.rect;
    }

    public String getOCR() {
        return this.ocrString;
    }
}
