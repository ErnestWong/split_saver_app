package com.noname.splitsaver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

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

	public RectangleView(Context context) {
		super(context);
		mPaint = new Paint();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(2);
		canvas.drawRect(left, top, right, bottom, mPaint);

		s = String.format("t: %d, b: %d, l: %d, r: %d", top, bottom, left, right);
        Log.d("RectangleView", "Ondraw: " + s);

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
