package fm.jihua.weixinexplorer.ui.activity;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import fm.jihua.weixinexplorer.App;
import fm.jihua.weixinexplorer.AttentListener;
import fm.jihua.weixinexplorer.R;
import fm.jihua.weixinexplorer.rest.contract.DataCallback;
import fm.jihua.weixinexplorer.rest.entities.Account;
import fm.jihua.weixinexplorer.rest.entities.AccountsResult;
import fm.jihua.weixinexplorer.rest.entities.BaseResult;
import fm.jihua.weixinexplorer.rest.entities.Choiceness;
import fm.jihua.weixinexplorer.rest.service.DataAdapter;
import fm.jihua.weixinexplorer.ui.adapters.AccountAdapter;
import fm.jihua.weixinexplorer.ui.view.AccountsView;
import fm.jihua.weixinexplorer.ui.widget.CachedImageView;
import fm.jihua.weixinexplorer.utils.Const;

public class AccountsActivity extends BaseActivity{
	
	ListView accountsListView;
	DataAdapter mDataAdapter;
	AccountAdapter accountsAdapter;
	AccountsView accountsView;
	Choiceness choiceness;
	int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);	
		mDataAdapter = new DataAdapter(this, new MyDataCallback());
		MobclickAgent.onEvent(this, "enter_accounts_list_view");
		id = getIntent().getIntExtra("ID", 0);
		if (getIntent().getIntExtra(Const.ACCOUNTS_FROM, 0) == Const.CATEGORY_ACCOUNTS) {
//			mDataAdapter.getCategory(id);		
			accountsView = new AccountsView(this, id);
			accountsView.init(Const.CATEGORY_ACCOUNTS);
			accountsView.setData(null);
			accountsView.setDataAdapter(mDataAdapter);
			setContentView(accountsView);
		} else {
			choiceness = (Choiceness) getIntent().getSerializableExtra("EDITORIAL");
			setContentView(R.layout.account_list);
			findView();
			initView();
			mDataAdapter.getChoiceness(id);
		}
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_back_button);
		initTitlebar();	
	}
	
	private void initView() {
		findViewById(R.id.banner_parent).setVisibility(View.VISIBLE);
		((CachedImageView)findViewById(R.id.banner_single)).setLoadingBitmap(null);
		((CachedImageView)findViewById(R.id.banner_single)).setImageURI(Uri.parse(choiceness.banner_url));
		((TextView)findViewById(R.id.slogan_single)).setText(choiceness.slogan);
	}

	private void initTitlebar() {
		((TextView)findViewById(R.id.textTitle)).setText("账号列表");
		findViewById(R.id.line).setVisibility(View.VISIBLE);
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void findView() {
		accountsListView = (ListView) findViewById(R.id.account_list);
		findViewById(R.id.root).setBackgroundDrawable(App.getBaseBackground(getResources()));
	}
	
	private void refreshListView(ArrayList<Account> accounts) {
		accountsAdapter = new AccountAdapter(this, accounts);
		accountsAdapter.setAttentListener(new MyAttentListener());
		accountsListView.setAdapter(accountsAdapter);
		accountsListView.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> listView, View arg1, int position,
				long arg3) {
//			MobclickAgent.onEvent(getContext(), "action_users_click_item");
			Account account = (Account) listView.getAdapter().getItem(position);
			Intent intent = new Intent(AccountsActivity.this, ProfileActivity.class);
			intent.putExtra("ACCOUNT", account);
			arg1.findViewById(R.id._new).setVisibility(View.GONE);
			startActivity(intent);
//			finish();
		}		
	}
	
	class MyAttentListener implements AttentListener{

		@Override
		public void onAttented(int id) {
			mDataAdapter.attentAccount(id);
		}
	}
	
	class MyDataCallback implements DataCallback{

		@Override
		public void callback(Message msg) {
			switch (msg.what) {
			case DataAdapter.MESSAGE_GET_CHOICENESS:
				AccountsResult result = (AccountsResult)msg.obj;
				if (result != null) {
					if (result.accounts != null) {
						ArrayList<Account> accounts = new ArrayList<Account>(); 
						accounts.addAll(Arrays.asList(result.accounts));
						refreshListView(accounts);
//						callback.onComplete(accounts);
					}		
				}else {
					findViewById(R.id.account_list).setVisibility(View.GONE);
					findViewById(R.id.empty).setVisibility(View.VISIBLE);
					((TextView)findViewById(R.id.empty)).setText("获取数据失败，请确认网络连接");
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
