package fm.jihua.weixinexplorer.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;

public class AnimTabHost extends TabHost{
	
	private static final String TAG = "AnimTabHost";
	  public boolean isAnimPlaying = false;
	  private boolean isToNextTab = false;
	  private int nextTabIndex = -1;
	  private Animation slideLeftIn;
	  private Animation slideLeftOut;
	  private Animation slideRightIn;
	  private Animation slideRightOut;
	  private int tabCount;

	public AnimTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.slideLeftIn = AnimationUtils.loadAnimation(context, 2130968579);
	    this.slideLeftOut = AnimationUtils.loadAnimation(context, 2130968580);
	    this.slideRightIn = AnimationUtils.loadAnimation(context, 2130968581);
	    this.slideRightOut = AnimationUtils.loadAnimation(context, 2130968582);
//	    AnimTabHost.1 local1 = new AnimTabHost.1(this);
//	    this.slideLeftIn.setAnimationListener(local1);
//	    this.slideRightIn.setAnimationListener(local1);
	}
	
	public void addTab(TabHost.TabSpec paramTabSpec) {
		this.tabCount = (1 + this.tabCount);
		super.addTab(paramTabSpec);
	}

	public int getTabCount() {
		return this.tabCount;
	}

//	public void setCurrentTab(int paramInt) {
//		if (this.isAnimPlaying) {
//			this.isToNextTab = true;
//			this.nextTabIndex = paramInt;
//		}
//		Log.d("AnimTabHost", "setCurrentTab");
//		int i = getCurrentTab();
//		if (getCurrentView() != null) {
//			if ((i == -1 + this.tabCount) && (paramInt == 0))
//				getCurrentView().startAnimation(this.slideLeftOut);
//		} else {
//			super.setCurrentTab(paramInt);
//			if ((i != -1 + this.tabCount) || (paramInt != 0))
//				break label162;
//			getCurrentView().startAnimation(this.slideLeftIn);
//		}
//		while (true) {
//			return;
//			if ((i == 0) && (paramInt == -1 + this.tabCount)) {
//				getCurrentView().startAnimation(this.slideRightOut);
//				break;
//			}
//			if (paramInt > i) {
//				getCurrentView().startAnimation(this.slideLeftOut);
//				break;
//			}
//			if (paramInt >= i)
//				break;
//			getCurrentView().startAnimation(this.slideRightOut);
//			break;
//			label162: if ((i == 0) && (paramInt == -1 + this.tabCount))
//				getCurrentView().startAnimation(this.slideRightIn);
//			else if (paramInt > i)
//				getCurrentView().startAnimation(this.slideLeftIn);
//			else if (paramInt < i)
//				getCurrentView().startAnimation(this.slideRightIn);
//		}
//	}
	
//	class MyAnimationListener implements Animation.AnimationListener{
//
//		@Override
//		public void onAnimationEnd(Animation animation) {
//			// TODO Auto-generated method stub
//		    isAnimPlaying = false;
//		    if (AnimTabHost.access$0(this.this$0))
//		    {
//		      setCurrentTab(AnimTabHost.access$1(this.this$0));
//		      AnimTabHost.access$2(this.this$0, false);
//		    }
//		}
//
//		@Override
//		public void onAnimationRepeat(Animation animation) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onAnimationStart(Animation animation) {
//			// TODO Auto-generated method stub
//			isAnimPlaying = true;
//		}		
//	}
}
