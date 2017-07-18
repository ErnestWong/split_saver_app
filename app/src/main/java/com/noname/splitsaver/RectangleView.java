package com.noname.splitsaver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom view to draw rectangle on preview screen
 * 
 */
public class RectangleView extends View {

	private Paint mPaint;
	private int top = 0;
	private int bottom = 0;
	private int left = 0;
	private int right = 0;
	String s;
    private Canvas mCanvas;
    List<Rect> rects = new ArrayList<Rect>();

	public RectangleView(Context context) {
		super(context);
		mPaint = new Paint();
		mCanvas = new Canvas();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(2);
		canvas.drawRect(left, top, right, bottom, mPaint);

        for (Rect r : rects) {
            canvas.drawRect(r.left, r.top, r.right, r.bottom, mPaint);
        }

		s = String.format("t: %d, b: %d, l: %d, r: %d", top, bottom, left, right);
        Log.d("RectangleView", "Ondraw: " + s);

	}

	public void reset() {
        this.left = 0;
        this.right = 0;
        this.bottom = 0;
        this.top = 0;
        this.invalidate();
    }


	public boolean drawn() {
		return !(right == 0 && left == 0 && top == 0 && bottom == 0);
	}

    public void setTopLeft(int x, int y) {
        this.left = x;
        this.top = y;
        invalidate();
	}

    public void setBottomRight(int x, int y) {
        this.right = x;
        this.bottom = y;
        invalidate();
	}

	public Rect getRect() {
        return new Rect(left, top, right, bottom);

	}

	public void drawRect(float left, float top, float right, float bottom) {
        mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(7);
        rects.add(new  Rect((int)left, (int)top, (int)right, (int)bottom));
		invalidate();
	}

	/**
	 *  Returns scaled version of rectangle based on bitmap dimensions.
	 **/
	public Rect getScaledRect(Bitmap bitmap) {
        float scaledLeft = ((float)left / getWidth() * bitmap.getWidth());
        float scaledRight = ((float)right / getWidth() * bitmap.getWidth());
        float scaledTop = ((float)top / getHeight() * bitmap.getHeight());
        float scaledBottom = ((float)bottom / getHeight() * bitmap.getHeight());

        return new Rect((int)scaledLeft, (int)scaledTop, (int)scaledRight, (int)scaledBottom);
    }

	public void setPaintColor(int color) {
		mPaint.setColor(color);
		invalidate();
	}

}
