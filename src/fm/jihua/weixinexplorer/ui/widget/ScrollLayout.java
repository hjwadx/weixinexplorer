package fm.jihua.weixinexplorer.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Scroller;
import fm.jihua.weixinexplorer.R;
import fm.jihua.weixinexplorer.ui.helper.UIUtil;
import fm.jihua.weixinexplorer.utils.Const;
import fm.jihua.weixinexplorer.utils.ImageHlp;

public class ScrollLayout extends ViewGroup{
	
	private static final String TAG = "ScrollLayout";
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;

	private int mCurScreen;
	private int mDefaultScreen = 0;

	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;

	private static final int SNAP_VELOCITY = 600;

	private int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop;
	private int mFullWidth = 0;
	private float mLastMotionX;
	private float mLastMotionY; 
	private long mLastScroll;
	private static final int ANIMATED_SCROLL_GAP = 250;
	private Context mContext;
	private int mScreenCount = 0;
	private boolean mDisableTouch = false;
	private boolean mCycling = false;
	private Drawable mBackground;
	private boolean mStop;
	boolean firstScrooled;
	
	
	//add autoScroll
	private boolean autoScroll = false;
	private boolean autoScrolling = false;
	private int scrollTime = 4 * 1000;
	private int currentWhat = 0;
	
	public void startScroll() {
		autoScroll = true;
		autoScrolling = true;
		handler.sendEmptyMessageDelayed(currentWhat, scrollTime);
	}

	public boolean isScrolling() {
		return autoScroll;
	}

	public void stopScroll() {
		autoScroll = false;
		autoScrolling = false;
		currentWhat++;
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (autoScrolling && currentWhat == msg.what) {
				snapToScreen((mCurScreen + 1) % getChildCount());

				Log.i("TAG", "handleMessage scrollToScreen:"
						+ mCurScreen);

				if (autoScrolling)
					handler.sendEmptyMessageDelayed(currentWhat, scrollTime);
			}
		}
	};

	// Event
	private OnScrollEventListener mScrollListener;

	public void setOnScrollEventListener(OnScrollEventListener listener) {
		this.mScrollListener = listener;
	}
	
	public void setScrollEndStop(boolean stop){
		mStop = stop;
	}

	public void disableTouch() {
		this.mDisableTouch = true;
	}

	@Override
	public void addView(View child) {
		super.addView(child);
		try {
			ViewGroup v = (ViewGroup) ((View) (this.getParent()))
					.findViewWithTag("scroller");
			if (v != null) {
				ImageView v1 = new ImageView(mContext);
				//old 
				v1.setImageDrawable(mContext.getResources().getDrawable(
						mScreenCount == mCurScreen ? R.drawable.page_circle_selected
								: R.drawable.page_circle));
				//new
//				v1.setImageDrawable(mContext.getResources().getDrawable(
//						mScreenCount == mCurScreen ? R.drawable.banner_dot
//								: R.drawable.banner_dot));	
				UIUtil.setAlpha(mScreenCount == mCurScreen ? 204 : 102 , v1);
//				v1.getDrawable().setAlpha(mScreenCount == mCurScreen ? 204 : 102);
//				v1.setLayoutParams(new LayoutParams(ImageHlp
//						.changeToSystemUnitFromDP(mContext, 32), ImageHlp
//						.changeToSystemUnitFromDP(mContext, 32)));
				v1.setPadding(ImageHlp.changeToSystemUnitFromDP(mContext, 4),
						0, ImageHlp.changeToSystemUnitFromDP(mContext, 4), 0);
				v1.setScaleType(ScaleType.CENTER_INSIDE);
				v.addView(v1);
			}
			mScreenCount++;
		} catch (Exception e) {
			Log.e(Const.TAG, "ScrollLayout addView Exception:" + e.getMessage());
		}
	}

	@Override
	public void removeAllViews() {
		super.removeAllViews();
		ViewGroup v = (ViewGroup) ((View) (this.getParent()))
				.findViewWithTag("scroller");
		if (v != null) {
			v.removeAllViews();
			mScreenCount = 0;
		}
	}

	public void setCycling(boolean cycling) {
		mCycling = cycling;
	}

	public ScrollLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);	
	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		mScroller = new Scroller(context);

		mCurScreen = mDefaultScreen;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
//		mBackground  = this.getBackground();
//		this.setBackgroundDrawable(null);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// if (changed) {
		int childLeft = 0;
		final int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				final int childWidth = childView.getMeasuredWidth();
				childView.layout(childLeft, 0, childLeft + childWidth,
						childView.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
		this.mFullWidth = childLeft;
		// }
	}
	
	@Override
	public void setBackgroundDrawable(Drawable d) {
//		super.setBackgroundDrawable(d);
		mBackground  = d;
//		super.setBackgroundDrawable(d);
		super.setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		Log.i(TAG, "onMeasure");
////		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		Log.d(Const.TAG, String.valueOf(widthMeasureSpec));
//		Log.d(Const.TAG, String.valueOf(heightMeasureSpec));
//		
////		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
////		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//		
////		if (widthMode != MeasureSpec.EXACTLY) {
////			throw new IllegalStateException(
////					"ScrollLayout only canmCurScreen run at EXACTLY mode!");
////		}
//
//		
//		// The children are given the same width and height as the scrollLayout
//		final int count = getChildCount();
////		int maxWidth = 0;
////		int maxHeight = 0;
//		for (int i = 0; i < count; i++) {
//			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
////			maxWidth = Math.max(maxWidth, getChildAt(i).getMeasuredWidth());
////			maxHeight = Math.max(maxH, getChildAt(i).getMeasuredWidth());
//		}
////		setMeasuredDimension();
//		// Log.i(TAG, "moving to screen "+mCurScreen);
//		if (!firstScrooled) {
//			scrollTo(mCurScreen * widthSize, 0);
//			firstScrooled = true;
//		}
//		
////		if (widthMode != MeasureSpec.EXACTLY) {
////			setMeasuredDimension(getChildAt(0).getMeasuredWidth(), getChildAt(0).getMeasuredHeight());
//		setMeasuredDimension(widthSize, heightSize);
////		}
//		Log.i(TAG, "onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		Log.d(Const.TAG, String.valueOf(widthMeasureSpec));
//		Log.d(Const.TAG, String.valueOf(heightMeasureSpec));

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ScrollLayout only canmCurScreen run at EXACTLY mode!");
		}


		// The children are given the same width and height as the scrollLayout
		final int count = getChildCount();
//		int maxWidth = 0;
//		int maxHeight = 0;
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
//			maxWidth = Math.max(maxWidth, getChildAt(i).getMeasuredWidth());
//			maxHeight = Math.max(maxH, getChildAt(i).getMeasuredWidth());
		}
//		setMeasuredDimension();
		// Log.i(TAG, "moving to screen "+mCurScreen);
		scrollTo(mCurScreen * width, 0);

		if (heightMode != MeasureSpec.EXACTLY) {
			setMeasuredDimension(widthSize, getChildAt(0).getMeasuredHeight());
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		Log.d(Const.TAG, "onDraw");
		super.onDraw(canvas);
		if (mBackground != null) {
			final int scrollX = getScrollX();
			final int scrollY = getScrollY();
			int width = this.getMeasuredWidth();
			float persent = Math.max(0, Math.min(1f, (getScrollX() / (float)(this.mFullWidth - width)))); 
			int left = (int) (persent * (mBackground.getIntrinsicWidth() - width));
//			Log.d(Const.TAG, String.valueOf(left));
//			Log.d(Const.TAG, String.valueOf(left+width));
//			Log.d(Const.TAG, String.valueOf(this.getMeasuredHeight()));
			if (mBackground.getIntrinsicWidth() < width) {
				mBackground.setBounds(0, 0, width, this.getMeasuredHeight());
			}else{
				mBackground.setBounds(-left, 0, -left+mBackground.getIntrinsicWidth() , this.getMeasuredHeight());
			}
			if ((scrollX | scrollY) == 0) {
				mBackground.draw(canvas);
			}
			else {
				canvas.translate(this.getScrollX(), this.getScrollY());
				mBackground.draw(canvas);
				canvas.translate(-this.getScrollX(), -this.getScrollY());
			}
		}
	}

	/**
	 * According to the position of current layout scroll to the destination
	 * page.
	 */
	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destX = (getScrollX() + screenWidth / 2);
		final int destScreen = destX < 0 ? -1 : destX / screenWidth;
		snapToScreen(destScreen);
	}

	public void snapToScreen(int whichScreen) {

		// get the valid layout page
		if (mCycling) {
			if (whichScreen > getChildCount() - 1)
				whichScreen = 0;
			if (whichScreen < 0)
				whichScreen = getChildCount() - 1;
		} else {
			whichScreen = Math.max(0,
					Math.min(whichScreen, getChildCount() - 1));
		}
		if (getScrollX() != (whichScreen * getWidth())) {

			final int delta = whichScreen * getWidth() - getScrollX();
			final int duration = Math.min(Math.abs(delta) * 2, 1000);
			// smoothScrollBy(delta, 0);
			mScroller.startScroll(getScrollX(), 0, delta, 0, duration);
			try {
				ViewGroup v = (ViewGroup) ((View) (this.getParent()))
						.findViewWithTag("scroller");
				if (v != null) {
					((ImageView) v.getChildAt(mCurScreen))
							.setImageResource(R.drawable.page_circle);
					((ImageView) v.getChildAt(whichScreen))
							.setImageResource(R.drawable.page_circle_selected);
					UIUtil.setAlpha(102 , (ImageView) v.getChildAt(mCurScreen));
					UIUtil.setAlpha(204 , (ImageView) v.getChildAt(whichScreen));
//					((ImageView) v.getChildAt(mCurScreen)).getDrawable().setAlpha(102);
//					((ImageView) v.getChildAt(whichScreen)).getDrawable().setAlpha(204);
				}
				mCurScreen = whichScreen;
				if (mScrollListener != null) {
					mScrollListener.onScroll(whichScreen);
				}
			} catch (Exception e) {
				Log.e(Const.TAG,
						"ScrollLayout snapToScreen Exception:" + e.getMessage());
			}
			invalidate(); // Redraw the layout
		}
	}

	public void setToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		ViewGroup v = (ViewGroup) ((View) (this.getParent()))
				.findViewWithTag("scroller");
		if (v != null) {
			((ImageView) v.getChildAt(mCurScreen))
					.setImageResource(R.drawable.page_circle);
			((ImageView) v.getChildAt(whichScreen))
					.setImageResource(R.drawable.page_circle_selected);

			UIUtil.setAlpha(102 , (ImageView) v.getChildAt(mCurScreen));
			UIUtil.setAlpha(204 , (ImageView) v.getChildAt(whichScreen));
//			((ImageView) v.getChildAt(mCurScreen)).getDrawable().setAlpha(102);
//			((ImageView) v.getChildAt(whichScreen)).getDrawable().setAlpha(204);
		}
		mCurScreen = whichScreen;
		scrollTo(whichScreen * getWidth(), 0);
	}

	public int getCurScreen() {
		return mCurScreen;
	}

	public final void smoothScrollBy(int dx, int dy) {

		long duration = AnimationUtils.currentAnimationTimeMillis()
				- mLastScroll;
		if (duration > ANIMATED_SCROLL_GAP) {
			mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(),
					dx, dy);
			awakenScrollBars(mScroller.getDuration());
			invalidate();
		} else {
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			scrollBy(dx, dy);
		}
		mLastScroll = AnimationUtils.currentAnimationTimeMillis();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mDisableTouch) {
			return super.onTouchEvent(event);
		}
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "event down!");
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			mLastMotionY = y;
			
			// add autoScroll
			autoScrolling = false;
			currentWhat++;
			break;

		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastMotionX - x);
			mLastMotionX = x;
			mLastMotionY = y;
//			if (Math.abs(mLastMotionY - y) < deltaX) {
				scrollBy(deltaX, 0);
//			}
			break;

		case MotionEvent.ACTION_UP:
			Log.i(TAG, "event : up");
			// if (mTouchState == TOUCH_STATE_SCROLLING) {
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();
			Log.i(TAG, "velocityX:" + velocityX);

			if (velocityX > SNAP_VELOCITY) {
				// Fling enough to move left
				Log.i(TAG, "snap left");
				if (mCurScreen > 0) {
					snapToScreen(mCurScreen - 1);
				} else if (mCurScreen == 0) {
					if (mScrollListener != null) {
						mScrollListener.onScrollToEnd(false);
					}
//					if (!mStop) {
						snapToDestination();
//					}
				}

			} else if (velocityX < -SNAP_VELOCITY) {
				// Fling enough to move right
				Log.i(TAG, "snap right");
				if (mCurScreen < getChildCount() - 1) {
					snapToScreen(mCurScreen + 1);
				} else if (mCurScreen == getChildCount() - 1) {
					if (mScrollListener != null) {
						mScrollListener.onScrollToEnd(true);
					}
					if (mStop) {
						snapToDestination();
					}
				}
			} else {
				snapToDestination();
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			// }
			mTouchState = TOUCH_STATE_REST;
			
			// add autoScroll
			if (autoScroll && !autoScrolling) {
				startScroll();
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			
			// add autoScroll
			if (autoScroll && !autoScrolling) {
				startScroll();
			}
			break;
		}

		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.i(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);
		if (mDisableTouch) {
			return super.onInterceptTouchEvent(ev);
		}

		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(mLastMotionX - x);
			final int yDiff = (int) Math.abs(mLastMotionY - y);
			if (xDiff > mTouchSlop && yDiff <= xDiff) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;

		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
//			if (!mScroller.isFinished()) {
//				mScroller.abortAnimation();
//			}
//			mTouchState = TOUCH_STATE_REST;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_REST;
			break;
		}

		return mTouchState != TOUCH_STATE_REST;
	}

	public interface OnScrollEventListener {
		public void onScrollToEnd(boolean isLast);

		public void onScroll(int whichScreen);
	}

}
