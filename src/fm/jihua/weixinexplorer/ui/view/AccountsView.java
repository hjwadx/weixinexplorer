package fm.jihua.weixinexplorer.ui.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.commonsware.cwac.endless.LoadMoreAdapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import fm.jihua.weixinexplorer.App;
import fm.jihua.weixinexplorer.AttentListener;
import fm.jihua.weixinexplorer.R;
import fm.jihua.weixinexplorer.data.providers.AddAccountsProvider;
import fm.jihua.weixinexplorer.data.providers.AddAccountsProvider.GetAccountsCallback;
import fm.jihua.weixinexplorer.rest.entities.Account;
import fm.jihua.weixinexplorer.rest.service.DataAdapter;
import fm.jihua.weixinexplorer.ui.activity.ProfileActivity;
import fm.jihua.weixinexplorer.ui.adapters.AccountAdapter;
import fm.jihua.weixinexplorer.ui.helper.Hint;
import fm.jihua.weixinexplorer.ui.helper.UIUtil;
import fm.jihua.weixinexplorer.utils.Compatibility;

public class AccountsView extends LinearLayout{
	
	ListView accountListView;
	final List<Account> accounts = new ArrayList<Account>();
	LoadMoreAccountsAdapter adapter;
	AddAccountsProvider dataProvider;
	Activity parent;
	int page = 1;
	Object param;
	String emptyDataMessage = "获取数据失败，请确认网络连接";
	int courseId;
	DataAdapter mDataAdapter;

	public AccountsView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initViews();
	}
	
	public AccountsView(Context context, Object param) {
		super(context);
		this.param = param;
		initViews();
	}
	
	public void init(int category){
		parent = (Activity)getContext();
		dataProvider = AddAccountsProvider.createByCategory(parent, category, courseId);
		dataProvider.setGetAccountsCallback(new MyGetAccountsCallback());
	}
	
	public void setDataAdapter(DataAdapter dataAdapter) {
		this.mDataAdapter = dataAdapter;
	}
	
	@SuppressWarnings("deprecation")
	@TargetApi(16)
	void initViews(){
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		inflate(getContext(), R.layout.account_list, this);
		accountListView = (ListView) findViewById(R.id.account_list);
		findViewById(R.id.root).setBackgroundDrawable(App.getBaseBackground(getResources()));
		BitmapDrawable bd = App.getBaseBackground(getResources());
		if(Compatibility.isCompatible(16)){
			accountListView.setBackground(bd);
		}else {
			accountListView.setBackgroundDrawable(bd);
		}
		adapter = new LoadMoreAccountsAdapter(getContext(), accounts);
		accountListView.setAdapter(adapter);
		accountListView.setOnItemClickListener(new MyOnItemClickListener());
//		accountListView.setDivider(null);
	}
	
	public void setData(List<Account> accounts){
		setData(accounts, 0);
	}

	public void setData(List<Account> accounts, int position){
		if (accounts == null) {
			dataProvider.getAccounts(param, 1, dataProvider.getDefaultDataLimit());
			return;
		}
		this.accounts.clear();
		this.accounts.addAll(accounts);
		accountListView.setSelection(position);
		adapter.notifyDataSetChanged();
		if (accounts.size() < 30) {
			adapter.stopAppending();	
		}
	}
	
	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> listView, View arg1, int position,
				long arg3) {
//			MobclickAgent.onEvent(getContext(), "action_users_click_item");
			Account account = (Account) listView.getAdapter().getItem(position);
			Intent intent = new Intent(getContext(), ProfileActivity.class);
			intent.putExtra("ACCOUNT", account);
			getContext().startActivity(intent);
			arg1.findViewById(R.id._new).setVisibility(View.GONE);
//			finish();
		}		
	}
		
	class MyGetAccountsCallback implements GetAccountsCallback {

		@Override
		public void onError(final String notice) {
			((Activity) getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					UIUtil.unblock(parent);
                    Hint.showTipsShort(parent, notice);
					adapter.onDataReady();
					findViewById(R.id.account_list).setVisibility(View.GONE);
					findViewById(R.id.empty).setVisibility(View.VISIBLE);
					((TextView)findViewById(R.id.empty)).setText(emptyDataMessage);
					// parent.finish();
				}
			});
		}

		@Override
		public void onComplete(final List<Account> accounts1) {
			// TODO Auto-generated method stub
			((Activity) getContext()).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					UIUtil.unblock((Activity) getContext());
					if (accounts1 != null) {
						boolean anyMore = dataProvider
								.isAnyMore(accounts1.size());
						Collection<? extends Account> friends;
						if (page == 1) {
							adapter.restartAppending();
							if (accounts1.size() == 0 && emptyDataMessage != null) {
								Hint.showTipsShort(getContext(),
										emptyDataMessage);
							}
						}
						if (!anyMore) {
							adapter.stopAppending();
						}
						// users.clear();
						accounts.addAll(accounts1);
						adapter.onDataReady();
					}
				}
			});
		}
	}
	
	class MyAttentListener implements AttentListener{

		@Override
		public void onAttented(int id) {
			mDataAdapter.attentAccount(id);
		}
	}
	
	class LoadMoreAccountsAdapter extends LoadMoreAdapter {
		AccountAdapter adapter;
		LoadMoreAccountsAdapter(Context context, List<Account> accounts) {
	    	super(context, new AccountAdapter(context, accounts), R.layout.pending_large, 
	    			R.layout.load_more_large);
	      adapter = (AccountAdapter) getWrappedAdapter();
	      adapter.setAttentListener(new MyAttentListener());
	      setRunInBackground(false);
	    }
	    
	    @Override
	    protected boolean cacheInBackground() throws Exception {
    		dataProvider.getAccounts(param, ++page, dataProvider.getDefaultDataLimit());
    		return true;
	    }
	    
	    @Override
	    protected void appendCachedData() {
	    }
	  }

}
