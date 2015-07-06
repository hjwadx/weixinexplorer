package fm.jihua.weixinexplorer.ui.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;

import fm.jihua.weixinexplorer.App;
import fm.jihua.weixinexplorer.R;

public class MainTabActivity extends TabActivity{
	
	
	private static final String TAG = "MainTabActivity";
	private static MainTabActivity mainTabActivity;
	private AlphaAnimation alphaAnimation;
	private long exitTime = 0L;
	public TabHost tabHost;
	int old_position;
	int toolbar_icon[] = { R.drawable.toolbar_icon_recommendation, R.drawable.toolbar_icon_topic, R.drawable.toolbar_icon_category, R.drawable.toolbar_icon_more};
	int toolbar_icon_pressed[] = { R.drawable.toolbar_icon_recommendation_pressed, R.drawable.toolbar_icon_topic_pressed, R.drawable.toolbar_icon_category_pressed, R.drawable.toolbar_icon_more_pressed};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mainTabActivity = this;
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_maintab);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_back_button);
		old_position = 0;
		initUM();
		App app = (App)getApplication();
		app.addActivity(this);
		initTitlebar();
		setUpViews();
	    setUpListeners();
	}
	
	private void initUM() {
		UmengUpdateAgent.setUpdateOnlyWifi(true);
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.update(this);
		MobclickAgent.onError(this);
		MobclickAgent.updateOnlineConfig(this);
		UMFeedbackService.enableNewReplyNotification(this, NotificationType.NotificationBar);
	}

	protected void onDestroy() {
		super.onDestroy();
		App app = (App)getApplication();
		app.removeActivity(this);
	}
	
	private void initTitlebar() {
		((TextView)findViewById(R.id.textTitle)).setText("推荐");
		findViewById(R.id.back).setVisibility(View.GONE);
		findViewById(android.R.id.tabhost).setBackgroundDrawable(App.getBaseBackground(getResources()));
	}
	
	public static MainTabActivity getInstance() {
		return mainTabActivity;
	}
	
	private void setUpViews() {
		this.tabHost = getTabHost();
		setIndicat("推荐", R.drawable.toolbar_icon_recommendation_pressed, RankingsActivity.class);
		setIndicat("专题", R.drawable.toolbar_icon_topic, ChoicenessActivity.class);
		setIndicat("分类", R.drawable.toolbar_icon_category, CategoryActivity.class);
		setIndicat("更多", R.drawable.toolbar_icon_more, MoreActivity.class);
	}
	
	private void setIndicat(String paramString,int resourceId, Class<?> paramClass) {
		View localView = View.inflate(this, R.layout.view_tab, null);
		((TextView) localView.findViewById(R.id.tab_text)).setText(paramString);
		((ImageView) localView.findViewById(R.id.tab_image)).setImageDrawable(getResources().getDrawable(resourceId));
		TabHost.TabSpec localTabSpec = this.tabHost.newTabSpec(paramString)
				.setIndicator(localView)
				.setContent(new Intent(this, paramClass));
		this.tabHost.addTab(localTabSpec);
	}
	
	private void setUpListeners() {
		this.alphaAnimation = new AlphaAnimation(0.0F, 1.0F);
		this.alphaAnimation.setDuration(1000L);
		this.tabHost.startAnimation(this.alphaAnimation);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				((TextView)findViewById(R.id.textTitle)).setText(tabId);
				int position = tabHost.getCurrentTab();
				View mView = tabHost.getTabWidget().getChildAt(position);
				((ImageView)mView.findViewById(R.id.tab_image)).setImageDrawable(getResources().getDrawable(toolbar_icon_pressed[position]));			
				mView = tabHost.getTabWidget().getChildAt(old_position);
				((ImageView)mView.findViewById(R.id.tab_image)).setImageDrawable(getResources().getDrawable(toolbar_icon[old_position]));
				old_position = position;
			}
		});
	}

}
