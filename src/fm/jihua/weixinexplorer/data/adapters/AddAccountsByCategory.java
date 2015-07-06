package fm.jihua.weixinexplorer.data.adapters;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Message;
import fm.jihua.weixinexplorer.data.providers.AddAccountsProvider;
import fm.jihua.weixinexplorer.rest.contract.DataCallback;
import fm.jihua.weixinexplorer.rest.entities.Account;
import fm.jihua.weixinexplorer.rest.entities.AccountsResult;
import fm.jihua.weixinexplorer.rest.service.DataAdapter;

public class AddAccountsByCategory extends AddAccountsProvider{
	
	DataAdapter mDataAdapter;

	public AddAccountsByCategory(Activity parent, int category) {
		super(parent, category);
		init();
	}

	@Override
	public void getAccounts(Object param, int offset, int limit) {
		// TODO Auto-generated method stub
	    mDataAdapter.getCategory(param.toString(), offset, limit);
	}
	
	void init(){
		mDataAdapter = new DataAdapter(mActivity, new MyDataCallback());
	}
	
	class MyDataCallback implements DataCallback{

		@Override
		public void callback(Message msg) {
			switch (msg.what) {
			case DataAdapter.MESSAGE_GET_CATEGORY:
				AccountsResult result = (AccountsResult)msg.obj;
				if (result != null) {
					if (result.accounts != null) {
						ArrayList<Account> accounts = new ArrayList<Account>(); 
						accounts.addAll(Arrays.asList(result.accounts));
						callback.onComplete(accounts);
					}		
				}else {
					callback.onError("获取账号信息失败");
				}
				break;
			default:
				break;
			}
		}
	}

}
