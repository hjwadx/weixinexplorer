package fm.jihua.weixinexplorer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.igexin.slavesdk.MessageManager;

import fm.jihua.weixinexplorer.cache.ImageCache;
import fm.jihua.weixinexplorer.cache.ImageCache.ImageCacheParams;
import fm.jihua.weixinexplorer.rest.service.DataService;
import fm.jihua.weixinexplorer.rest.service.RestService;
import fm.jihua.weixinexplorer.utils.Const;
import fm.jihua.weixinexplorer.utils.DatabaseHelper;
import fm.jihua.weixinexplorer.utils.HttpUtil;

public class App extends Application{
	private List<Activity> activityList = new LinkedList<Activity>();
	private List<String> weixinIdOldList = new ArrayList<String>();
	private DatabaseHelper mDBHelper;
	private SQLiteDatabase mAccountDB;

	private ImageCache mImageCache;
	private static final String IMAGE_CACHE_DIR = "thumbs";
	private LruCache<String, Object> mMemoryCache;
	private static final int DEFAULT_MEM_CACHE_SIZE = 100 * 1024 ; // 100K
	
	@Override
	public void onCreate() {
		HttpUtil.setVersion(Const.getAppVersionName(this));
		DataService.setProvider(new RestService());
		MessageManager.getInstance().initialize(this.getApplicationContext());
		readDatabse();
		try {
			//for startup
			ImageCacheParams cacheParams = new ImageCacheParams(this, IMAGE_CACHE_DIR);
	        // Set memory cache to 25% of mem class
	        cacheParams.setMemCacheSizePercent(this, 0.25f);
			mImageCache = new ImageCache(cacheParams);
			mImageCache.initDiskCacheAsync();
			mMemoryCache = new LruCache<String, Object>(DEFAULT_MEM_CACHE_SIZE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		updateWeixinIdOldList();
	}
	
	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	public void removeActivity(Activity activity){
		activityList.remove(activity);
	}
	
	public int getActivityCount(){
		return activityList.size();
	}
	
	public Activity getTopActivity(){
		if (activityList.size() > 0) {
			return activityList.get(activityList.size() - 1);
		}
		return null;
	}
	
	// 遍历所有Activity并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
	
	private void readDatabse() {
		try {
			mDBHelper = new DatabaseHelper(this);
			mAccountDB = mDBHelper.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(Const.TAG, "App readDatabse Exception:" + e.getMessage());
		}
	}
	
	public SQLiteDatabase getAccountDB() {
		return mAccountDB;
	}
	
	public DatabaseHelper getDBHelper() {
		return mDBHelper;
	}
	
	public List<String> getWeixinIdOldList() {
		return weixinIdOldList;
	}
	
	public boolean existWeixinId(String weixin_id) {
		return weixinIdOldList.contains(weixin_id);
	}
	
	public void updateWeixinIdOldList() {
		weixinIdOldList = mDBHelper.getAccountsOld(mAccountDB);
	}
	
//	static SoftReference<Bitmap> baseBgReference;
	public static BitmapDrawable getBaseBackground(Resources resources){
		Bitmap bmpBaseBg = BitmapFactory.decodeResource(resources, R.drawable.bg_repeat);
		BitmapDrawable bd = new BitmapDrawable(resources, bmpBaseBg);
		bd.setTileModeXY(TileMode.REPEAT , TileMode.REPEAT );
		bd.setDither(true);
		return bd;
	}
	
	public ImageCache getImageCache() {
		return mImageCache;
	}
	
	public LruCache<String, Object> getMemCache() {
		return mMemoryCache;
	}

}
