package com.zr.scalevideoviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

public class MyVideoView extends VideoView {
	
	 private int mVideoWidth;
	 private int mVideoHeight;

	public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyVideoView(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		Log.d("zr", "widthMeasureSpe = " + widthMeasureSpec);
//		Log.d("zr", "heightMeasureSpec = " + heightMeasureSpec);
		int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
//        Log.d("zr", "width = " + width);
//		Log.d("zr", "height = " + height);
		setMeasuredDimension(width, height);
	}
	
}
