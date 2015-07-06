package fm.jihua.weixinexplorer.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import fm.jihua.weixinexplorer.App;
import fm.jihua.weixinexplorer.R;
import fm.jihua.weixinexplorer.rest.contract.DataCallback;
import fm.jihua.weixinexplorer.rest.entities.Account;
import fm.jihua.weixinexplorer.rest.entities.AccountsResult;
import fm.jihua.weixinexplorer.rest.entities.BaseResult;
import fm.jihua.weixinexplorer.rest.entities.ScreenShot;
import fm.jihua.weixinexplorer.rest.service.DataAdapter;
import fm.jihua.weixinexplorer.ui.helper.DialogUtils;
import fm.jihua.weixinexplorer.ui.helper.Hint;
import fm.jihua.weixinexplorer.ui.widget.CachedImageView;
import fm.jihua.weixinexplorer.utils.AndroidSystem;
import fm.jihua.weixinexplorer.utils.Const;

public class ProfileActivity extends BaseActivity{
	
	Account account;
	Button attentButton;
	Button shareButton;
	DataAdapter mDataAdapter;
	CachedImageView avatar, screenshot0, screenshot1 ,screenshot2;
	List<CachedImageView> screenshotList = new ArrayList<CachedImageView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		account = (Account) getIntent().getSerializableExtra("ACCOUNT");
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_profile);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_back_button);
		MobclickAgent.onEvent(this, "enter_profile_view");
		mDataAdapter = new DataAdapter(this, new MyDataCallback());
		findView();
		initTitlebar();
		setListeners();
		init();			
	}
	
	private void init() {
		if (account == null) {
			if (getIntent().getIntExtra(Const.ACCOUNTS_FROM, 0) == Const.CHOICENESS_ACCOUNTS) {
				mDataAdapter.getChoiceness(getIntent().getIntExtra("ID", 0));
			}
		} else {
			addToDB();
			initView();
		}	
	}

	private void addToDB() {
		App mApp;
		mApp = (App) getApplicationContext();
		if(!mApp.existWeixinId(account.weixin_id)) {
			mApp.getDBHelper().addAccountOld(mApp.getAccountDB(), account.weixin_id);
			mApp.updateWeixinIdOldList();
		}
	}

	private void initTitlebar() {
		findViewById(R.id.line).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.textTitle)).setText("账号详情");
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void setListeners() {
		avatar.setOnClickListener(clickListener);
		attentButton.setOnClickListener(clickListener);
		shareButton.setOnClickListener(clickListener);
		screenshot0.setOnClickListener(clickListener);
		screenshot1.setOnClickListener(clickListener);
		screenshot2.setOnClickListener(clickListener);
	}
	
	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.avatar:
				//显示大头像
				break;			
			case R.id.attent:
				startWeixin();
				MobclickAgent.onEvent(ProfileActivity.this, "action_attention", "from_profile");
				mDataAdapter.attentAccount(account.id);
				break;
			case R.id.share:
				// 分享
				AndroidSystem.shareText(ProfileActivity.this, "微信账号分享", "微信名：" + account.name + " 微信号：" + account.weixin_id + " —— 来自 精品微信指南");
				MobclickAgent.onEvent(ProfileActivity.this, "action_share", "system");
				break;
			case R.id.screenshot0:
			case R.id.screenshot1:
			case R.id.screenshot2:
				//显示大图片，传url数组和pisition
				break;
			default:
				break;
			}
		}
	};
	
	private void startWeixin() {
		Intent intent = new Intent();         
		intent.setAction("android.intent.action.VIEW"); 
		Uri uri = Uri.parse(account.qrcode_url);
//		Uri uri = Uri.parse("http://weixin.qq.com/r/qHVQX2nEOldFh3g8nyCM");        
		intent.setData(uri); 

		//包名、要打开的activity 
		intent.setClassName("com.tencent.mm",  "com.tencent.mm.ui.qrcode.GetQRCodeInfoUI");  
		if (AndroidSystem.isIntentAvailable(this, intent)) {
			startActivity(intent); 
		} else {
			Toast.makeText(this, "请先下载微信", Toast.LENGTH_LONG).show();
			DialogUtils.showWeixinDownloadDialog(this);
		}
//		try {
//			startActivity(intent);
//		} catch (ActivityNotFoundException e) {
//			e.printStackTrace();
//			Toast.makeText(this, "请先下载微信", Toast.LENGTH_LONG).show();
//		} 		
	}

	private void initView() {
		avatar.setCorner(true);
		avatar.setFadeIn(false);
		avatar.setImageURI(Uri.parse(account.icon_url_large));
		((TextView)findViewById(R.id.name)).setText(account.name);
		((TextView)findViewById(R.id.category)).setText("分类：" + account.category_name);
		((TextView)findViewById(R.id.followers)).setText("关注：" + String.valueOf(account.num_followers));
		((TextView)findViewById(R.id.weixin_id)).setText("微信号：" + account.weixin_id);
		if (account.screenshots != null && account.screenshots.length > 0) {
			findViewById(R.id.screenshot_parent).setVisibility(View.VISIBLE);
			int i = 0;
			for(ScreenShot screenshot : account.screenshots) {
				screenshotList.get(i).setVisibility(View.VISIBLE);
				screenshotList.get(i).setImageURI(Uri.parse(screenshot.small));
				i++;
			}
		}
		((TextView)findViewById(R.id.details)).setText(account.detail);
	}

	private void findView() {
		findViewById(R.id.root).setBackgroundDrawable(App.getBaseBackground(getResources()));
		attentButton = (Button) findViewById(R.id.attent);
		shareButton = (Button) findViewById(R.id.share);
		avatar = (CachedImageView) findViewById(R.id.avatar);
		screenshotList.add((CachedImageView) findViewById(R.id.screenshot0));
		screenshotList.add((CachedImageView) findViewById(R.id.screenshot1));
		screenshotList.add((CachedImageView) findViewById(R.id.screenshot2));
		screenshot0 = (CachedImageView) findViewById(R.id.screenshot0);
		screenshot1 = (CachedImageView) findViewById(R.id.screenshot1);
		screenshot2 = (CachedImageView) findViewById(R.id.screenshot2);
	}
	
	class MyDataCallback implements DataCallback{

		@Override
		public void callback(Message msg) {
			switch (msg.what) {
			case DataAdapter.MESSAGE_GET_CHOICENESS:
				AccountsResult result = (AccountsResult)msg.obj;
				if (result != null) {
					if (result.accounts != null && result.accounts.length > 0) {
						account = result.accounts[0]; 
						initView();
						addToDB();
						Hint.showTipsShort(ProfileActivity.this, "主题正在创建中，马上更新");
					} else {
						findViewById(R.id.empty).setVisibility(View.VISIBLE);
						findViewById(R.id.profile_parent).setVisibility(View.GONE);
					}		
				}else {
//					callback.onError("获取账号信息失败");
				}
				break;
			case DataAdapter.MESSAGE_ADD_ATTENT:
				BaseResult result1 = (BaseResult)msg.obj;
				if (result1 != null) {
					Log.e("hjw","关注度增加成功");		
				}else {
//					callback.onError("获取账号信息失败");
				}
				break;
			default:
				break;
			}
		}
	}
}
