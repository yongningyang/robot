package com.innsmap.versionchecker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


public class CustomProgressBar extends View {
	private int width;
	private int height;

	private Paint framePaint;
	private Paint solidPaint;

	private Rect frameRect;
	private Rect solidRect;

	private float persent;

	private Context context;

	public float getPersent() {
		return persent;
	}

	public void setPersent(float persent) {
		this.persent = persent;
		invalidate();
	}

	@SuppressWarnings("deprecation")
	public CustomProgressBar(Context context, AttributeSet attrs,
                             int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		framePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		framePaint.setColor(Color.parseColor("#FFDDDDDD"));
		framePaint.setStyle(Style.STROKE);
		framePaint.setStrokeWidth(1);

		solidPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		solidPaint.setColor(Color.parseColor("#FF50D2CD"));
	}

	public CustomProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomProgressBar(Context context) {
		this(context, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (frameRect == null)
			return;
		canvas.drawRect(frameRect, framePaint);
		canvas.drawRect(solidRect.left, solidRect.top, solidRect.left
				+ solidRect.width() * persent * 1.0f / 100, solidRect.bottom,
				solidPaint);
	}

	@Override
	@SuppressLint("DrawAllocation")
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = measureWidth(widthMeasureSpec);
		height = measureHeight(heightMeasureSpec);
		setMeasuredDimension(width, height);

		frameRect = new Rect(0, 0, width, height);
		solidRect = new Rect(1, 1, width - 1, height - 1);
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = UpdateVersionUtils.getWindowWith(context);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return specSize;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = getResources().getDimensionPixelOffset(
					R.dimen.custom_progressbar_height);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return specSize;
	}

}
