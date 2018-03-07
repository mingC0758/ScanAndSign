package com.scanandsign.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 绘画板（绘制签名）
 * @author mingC
 * @date 2018/1/31
 */
public class DrawView extends View {
	private int mWidth;
	private int mHeight;
	private Paint mPaint;

	public DrawView(Context context) {
		super(context);
		initPaint();
	}

	public DrawView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	/**
	 * 画笔初始化
	 */
	void initPaint() {
		mPaint = new Paint();
		//抗锯齿
		mPaint.setAntiAlias(true);
		//颜色
		mPaint.setARGB(255,0,0,0);
		//粗细度
		mPaint.setStrokeWidth(5);
		//边界风格
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		//风格
		mPaint.setStyle(Paint.Style.STROKE);
	}


	private float downX;
	private float downY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				downX = event.getX();
				downY = event.getY();
				mPath.moveTo(downX, downY);
				break;
			case MotionEvent.ACTION_MOVE:
				mPath.lineTo(event.getX(), event.getY());
				break;
			case MotionEvent.ACTION_UP:
				break;
			default:
		}
		//刷新View，重新绘制
		invalidate();
		return true;
	}

	Path mPath = new Path();

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawPath(mPath, mPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;
		Log.d("DrawView", "DrawView onSizeChange：w：" + w + " h:" + h);
	}

	/**
	 * 生成签名图片
	 * @return
	 */
	public Bitmap createBitmap() {
		Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
		canvas.drawPath(mPath, mPaint);
		return bitmap;
	}
}
