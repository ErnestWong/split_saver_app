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
 * @author E Wong
 * 
 */
public class RectangleView extends View {

	private Context mContext;
	private Paint mPaint;
	private float mRectDimens = 0;
	private int top = 0;
	private int bottom = 0;
	private int left = 0;
	private int right = 0;
	private Rect mRect;
	String s;

	public RectangleView(Context context) {
		super(context);
		mContext = context;
		mPaint = new Paint();
		Log.d("rectangleview", "init");

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

        /*
		mRectDimens = (float) (this.getWidth() * 0.9);
		mTop = this.getHeight() / 2 - mRectDimens / 2;
		mBottom = mTop + mRectDimens;
		mLeft = (float) (this.getWidth() * 0.05);
		mRight = mLeft + mRectDimens;
		*/
		if (mRect == null) {
			mRect = new Rect((int) left, (int) top, (int) right,
					(int) bottom);
		}

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(2);
		canvas.drawRect(left, top, right, bottom, mPaint);

		s = String.format("t: %d, b: %d, l: %d, r: %d", top, bottom, left,
				right);
		String t = String.format("w: %d, h: %d", this.getWidth(),
				this.getHeight());
		Log.d("ondraw", s);
		Log.d("view dimen", t);

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

	public Rect getScaledRect(Bitmap bitmap) {
        int scaledLeft = (int)((float)left / getWidth() * bitmap.getWidth());
        int scaledRight = (int)((float)right / getWidth() * bitmap.getWidth());
        int scaledTop = (int)((float)top / getHeight() * bitmap.getHeight());
        int scaledBottom = (int)((float)bottom / getHeight() * bitmap.getHeight());
        String s = String.format("original: t: %d, b: %d, l: %d, r: %d", top, bottom, left, right);
        Log.d("scaleRect", s);
        String t = String.format("t: %d, b: %d, l: %d, r: %d", scaledTop, scaledBottom, scaledLeft,
				scaledRight);
        Log.d("scaleRect", t);

        return new Rect(scaledLeft, scaledTop, scaledRight, scaledBottom);
    }

	public void setPaintColor(int color) {
		mPaint.setColor(color);
		invalidate();
	}

}
