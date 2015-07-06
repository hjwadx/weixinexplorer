package fm.jihua.weixinexplorer.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class Const {
	public static final String TAG = "WEIXINEXPLORER";
	
	public static final String REST_HOST = "http://we.jihua.fm";
//	public static final String REST_HOST = "http://" + Const.LOCAL_SERVER_HOST + ":3000";
	public static final String LOCAL_SERVER_HOST = "10.0.0.4";	// 本机,10.0.2.2	
	
	public static final int DATA_COUNT_PER_REQUEST = 30;
	
	public static final int All_ACCOUNTS = 100;
	public static final int CATEGORY_ACCOUNTS = 101;
	public static final int CHOICENESS_ACCOUNTS = 102;
	

	public static final int TIMEOUT_CONNECTION = 60000;
	public static final int TIMEOUT_SOCKET = 60000;
	

	public static final String ACCOUNTS_FROM = "accounts_from";
	
	//数据库
	public static final String DATABASE_ACCOUNT_FILE = "weixinexplorer";
	public static final String DATABASE_TABLE_ACCOUNTS_OLD= "accounts_old";
	
	//没有版本更新
	public static final String[] NO_CHECK_NEW_VERSION = new String[]{"wo"};
	
	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}
	
	public static String getChannelName(Context context) {
		String name = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			
			name = appInfo.metaData.get("UMENG_CHANNEL").toString();
		} catch (Exception e) {
			Log.e(Const.TAG, "Exception", e);
		}
		return name;
	}

}
