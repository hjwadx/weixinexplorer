package fm.jihua.weixinexplorer.data.providers;

import java.util.List;

import android.app.Activity;
import fm.jihua.weixinexplorer.data.adapters.AddAccountsByAll;
import fm.jihua.weixinexplorer.data.adapters.AddAccountsByCategory;
import fm.jihua.weixinexplorer.rest.entities.Account;
import fm.jihua.weixinexplorer.utils.Const;



public abstract class AddAccountsProvider {
	
	protected Activity mActivity;
	protected int category;
	protected GetAccountsCallback callback;

	public AddAccountsProvider(Activity parent, int category){
		mActivity = parent;
		this.category = category;
	}
	
	public interface GetAccountsCallback{
		public void onComplete(List<Account> accounts);
		public void onError(String notice);
	}
	
	public void setGetAccountsCallback(GetAccountsCallback callback){
		this.callback = callback;
	}
	
	public int getDefaultDataLimit(){
		return Const.DATA_COUNT_PER_REQUEST;
	}
	
	public boolean isAnyMore(int count){
		return count >= getDefaultDataLimit();
	}
	
	public void getAccounts(Object param){
		getAccounts(param, 1, getDefaultDataLimit());
	}
	public abstract void getAccounts(Object param, int offset, int limit);
	
	public static AddAccountsProvider createByCategory(Activity parent, int category,int courseId){
		switch (category) {
		case Const.All_ACCOUNTS:
			return new AddAccountsByAll(parent, category);
		case Const.CATEGORY_ACCOUNTS:
			return new AddAccountsByCategory(parent, category);
		default:
			break;
		}
		return null;
	}

}
