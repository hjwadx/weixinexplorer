package fm.jihua.weixinexplorer.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.UMFeedbackService;
import com.umeng.newxp.controller.ExchangeDataService;
import com.umeng.newxp.view.ExchangeViewManager;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import fm.jihua.weixinexplorer.App;
import fm.jihua.weixinexplorer.R;
import fm.jihua.weixinexplorer.ui.helper.UIUtil;
import fm.jihua.weixinexplorer.utils.Const;
import fm.jihua.weixinexplorer.utils.ObjectUtils;


public class MoreActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		MobclickAgent.onEvent(this, "enter_more_view");
		init();
	}

	private void init() {
		if (!ObjectUtils.containsElement(Const.NO_CHECK_NEW_VERSION, Const.getChannelName(this))) {
			findViewById(R.id.check_new_version_parent).setVisibility(View.VISIBLE);
			findViewById(R.id.feedback_parent).setBackgroundResource(R.drawable.input_top);
		}
		findViewById(R.id.exit).setOnClickListener(listener);
		findViewById(R.id.feedback_parent).setOnClickListener(listener);
		findViewById(R.id.check_new_version_parent).setOnClickListener(listener);
		ViewGroup fatherLayout = (ViewGroup) findViewById(R.id.root);
		ListView listView = (ListView) this.findViewById(R.id.list);
		String open_recommendation = MobclickAgent.getConfigParams(this, "open_recommendation");
		if (open_recommendation.equals("1")) {
			ExchangeViewManager exchangeViewManager = new ExchangeViewManager(this,new ExchangeDataService());
			exchangeViewManager.addView(fatherLayout, listView);
			listView.setVisibility(View.VISIBLE);
		} else {
			listView.setVisibility(View.GONE);
		}
	}
	
	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.check_new_version_parent:
				checkForNewVersion();
				break;
			case R.id.feedback_parent:
				UMFeedbackService.openUmengFeedbackSDK(MoreActivity.this);
				break;
			case R.id.exit:
				MobclickAgent.onEvent(MoreActivity.this, "action_exit", "from_button_in_more_view");
				App app = (App)getApplication();
				app.exit();
				finish();
				break;
			default:
				break;
			}
		}
	};
	
	void checkForNewVersion(){
		UIUtil.block(MainTabActivity.getInstance());
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		        @Override
		        public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
		        	if (updateStatus == 2 && updateInfo != null) {
		        		if(updateInfo.hasUpdate){
			        		updateStatus = 0;
			        	}else {
							updateStatus = 1;
						}
					}
		            switch (updateStatus) {
		            case 0: // has update
		                UmengUpdateAgent.showUpdateDialog(MoreActivity.this.getParent(), updateInfo);
		                break;
		            case 1: // has no update
		                Toast.makeText(MoreActivity.this, "你的微信精品指南已是最新版本", Toast.LENGTH_SHORT).show();
		                break;
		            case 2: // none wifi
		                Toast.makeText(MoreActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
		                break;
		            case 3: // time out
		                Toast.makeText(MoreActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
		                break;
		            }
		            UIUtil.unblock(MainTabActivity.getInstance());
		        }
		});
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.update(MoreActivity.this.getParent());
	}
}
