package fm.jihua.weixinexplorer.rest.service;

import java.io.IOException;
import java.text.DateFormat;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fm.jihua.weixinexplorer.rest.contract.DataProvider;
import fm.jihua.weixinexplorer.rest.entities.AccountsResult;
import fm.jihua.weixinexplorer.rest.entities.BaseResult;
import fm.jihua.weixinexplorer.rest.entities.CategorysResult;
import fm.jihua.weixinexplorer.rest.entities.ChoicenessesResult;
import fm.jihua.weixinexplorer.utils.Const;
import fm.jihua.weixinexplorer.utils.HttpUtil;


public class RestService implements DataProvider {
	
	private final Gson gson;
	private final Gson time_gson;
	private final String TAG = getClass().getSimpleName();
	private final boolean DEBUG = false;
	
	public RestService() {
		GsonBuilder gsonBuilder = new GsonBuilder();  
		gsonBuilder.setDateFormat("yyyy-MM-dd");
		gson = gsonBuilder.create();
		
		time_gson = new GsonBuilder().setDateFormat(DateFormat.LONG).create();
	}

	@Override
	public AccountsResult getAccounts(int page, int limit) {
		String url = Const.REST_HOST + "/accounts.json?page="+page+"&per_page="+limit;
		return getObject(url, AccountsResult.class);
		// TODO Auto-generated method stub		
	}

	@Override
	public ChoicenessesResult getNewChoicenesses() {
		String url = Const.REST_HOST + "/editorials/top.json";
		return getObject(url, ChoicenessesResult.class);	
	}

	@Override
	public ChoicenessesResult getChoicenesses() {
		String url = Const.REST_HOST + "/editorials.json";
		return getObject(url, ChoicenessesResult.class);		
	}
	
	@Override
	public AccountsResult getChoiceness(int id) {
		String url = Const.REST_HOST + "/editorials/"+ id +".json";
		return getObject(url, AccountsResult.class);	
	}

	@Override
	public CategorysResult getCategorys() {
		String url = Const.REST_HOST + "/categories.json";
		return getObject(url, CategorysResult.class);
	}
	
	@Override
	public AccountsResult getCategory(int id) {
		String url = Const.REST_HOST + "/categories/"+ id +".json";
		return getObject(url, AccountsResult.class);
		
	}
	
	@Override
	public AccountsResult getCategory(String id, int page, int limit) {
		String url = Const.REST_HOST + "/categories/"+ id +".json?page="+page+"&per_page="+limit;
		return getObject(url, AccountsResult.class);
	}
	

	@Override
	public BaseResult attentAccount(int id) {
		String url = Const.REST_HOST + "/accounts/"+ id +"/follow.json";
		BaseResult result = null;
		try {
			String response = HttpUtil.getStringFromUrl(url, false, null);
			result = gson.fromJson(response, 
					BaseResult.class);
		} catch (IOException e) {
			Log.e(RestService.class.toString(),
					"connection failed " + e.getMessage());
		} catch (Exception e) {
			Log.w(RestService.class.toString(),
					e.getMessage(), e);
		}
		return result;
	}
	
	public <T> T getObject(String url, Class<T> classOfT){
		T result = null;
		try {
			String response = HttpUtil.getStringFromUrl(url);
			result = gson.fromJson(response, 
					classOfT);
		} catch (IOException e) {
			Log.e(RestService.class.toString(),
					"connection failed " + e.getMessage());
		} catch (Exception e) {
			Log.w(RestService.class.toString(),
					e.getMessage(), e);
		}
		return result;
	}

}
