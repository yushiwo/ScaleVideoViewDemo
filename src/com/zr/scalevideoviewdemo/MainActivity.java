package com.zr.scalevideoviewdemo;



import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private MyVideoView mVideoView;
	private Uri mVideoUri;

	private FrameLayout mVideoViewLayout;
	private Button zoomIn;
	private Button zoomOut;

	private int screenWidth;
	private int screenHeight;
	
	private int videoWidth;
	private int videoHeight;

	private int layoutHeight = 400;

	private static int DRAG = 1;
	private static int ZOOM = 2;
	private static int NONE = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();
		setListeners();
		playVideo();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		WindowManager wm = this.getWindowManager();
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		videoWidth = mVideoView.getWidth();
		videoHeight = mVideoView.getHeight();
		
		Log.d("zr", "videoWidth = " + videoWidth);
		Log.d("zr", "videoHeight = " + videoHeight);
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		mVideoView = (MyVideoView) findViewById(R.id.video);
		zoomIn = (Button) findViewById(R.id.btn_zoom_in);
		zoomOut = (Button) findViewById(R.id.btn_zoom_out);
		mVideoViewLayout = (FrameLayout) findViewById(R.id.layout_videoview);
	}

	/**
	 * 监听设置
	 */
	private void setListeners() {
		zoomIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ValueAnimator animation = ValueAnimator.ofFloat(2, 1);
				animation.setDuration(1000);
				animation.start();
				animation.addUpdateListener(new AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						Float delta = (Float) animation.getAnimatedValue();
						Log.d("zr", "delta = " + delta);
						int width = (int) (screenWidth * delta);
						int height = (int) (layoutHeight * delta);
						FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
						mVideoView.getHolder().setFixedSize(width, height);
						mVideoView.setLayoutParams(params);
					}
				});
			}
		});

		zoomOut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ValueAnimator animation = ValueAnimator.ofFloat(1, 2);
				animation.setDuration(1000);
				animation.start();
				animation.addUpdateListener(new AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						Float delta = (Float) animation.getAnimatedValue();
						Log.d("zr", "delta = " + delta);
						int width = (int) (screenWidth * delta);
						int height = (int) (layoutHeight * delta);
						Log.d("zr", "width = " + width);
						Log.d("zr", "height = " + height);
						FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
						mVideoView.getHolder().setFixedSize(width, height);
						mVideoView.setLayoutParams(params);
					}
				});
			}
		});

		mVideoView.setOnTouchListener(movingEventListener);
	}

	/**
	 * 数据初始化
	 */
	private void initData() {
		mVideoUri = Uri
				.parse("http://v.cctv.com/flash/mp4video28/TMS/2013/05/06/265114d5f2e641278098503f1676d017_h264418000nero_aac32-1.mp4");
	}

	/**
	 * 播放视频
	 */
	private void playVideo() {
		mVideoView.setVideoURI(mVideoUri);
		mVideoView.requestFocus();
		mVideoView.start();
	}

	public OnTouchListener movingEventListener = new OnTouchListener() {
		int lastX, lastY;
		int mode;
		float oldDist;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				if (oldDist > 10f) {
					// midPoint(mid, event);
					mode = ZOOM;
					Log.d("zr", "mode = " + mode);
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if (mode == ZOOM) {
					float newDist = spacing(event);

					if (newDist > 10f) {
						int scale = (int) (newDist / oldDist);
						ValueAnimator animation = ValueAnimator.ofFloat(1, scale);
						animation.setDuration(1000);
						animation.start();
						animation
								.addUpdateListener(new AnimatorUpdateListener() {
									@Override
									public void onAnimationUpdate(
											ValueAnimator animation) {
										Float delta = (Float) animation
												.getAnimatedValue();
										int width = (int) (videoWidth * delta);
										int height = (int) (videoHeight * delta);
										if (width < screenWidth) {
											width = screenWidth;
											height = layoutHeight;
										}
										if (width > 2 * screenWidth) {
											width = 2 * screenWidth;
											height = 2 * layoutHeight;
										}
										FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
										mVideoView.getHolder().setFixedSize(width, height);
										mVideoView.setLayoutParams(params);
									}
								});
						
						animation.addListener(new AnimatorListener() {
						
						@Override
						public void onAnimationStart(Animator animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationRepeat(Animator animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animator animation) {
							videoWidth = mVideoView.getWidth();
							videoHeight = mVideoView.getHeight();
						}
						
						@Override
						public void onAnimationCancel(Animator animation) {
							// TODO Auto-generated method stub
							
						}
					});


					}

				}
				mode = NONE;

				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;

					int left = v.getLeft() + dx;
					int top = v.getTop() + dy;
					int right = v.getRight() + dx;
					int bottom = v.getBottom() + dy;
					// 设置不能小于容器
					if (left > 0) {
						left = 0;
						right = left + v.getWidth();
					}

					if (right < screenWidth) {
						right = screenWidth;
						left = right - v.getWidth();
					}

					if (top > 0) {
						top = 0;
						bottom = top + v.getHeight();
					}

					if (bottom < layoutHeight) {
						bottom = layoutHeight;
						top = bottom - v.getHeight();
					}

					v.layout(left, top, right, bottom);

					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();

				}
				break;
			}

			return true;
		}

		/**
		 * 计算touch亮点之间的距离
		 * @param event
		 * @return
		 */
		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}

	};

}
