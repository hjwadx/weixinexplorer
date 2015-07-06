package fm.jihua.weixinexplorer.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fm.jihua.weixinexplorer.App;

public class DatabaseHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = Const.DATABASE_ACCOUNT_FILE;
	private final static int DATABASE_VERSION = 1;
	private final String TAG = getClass().getSimpleName();
	
	private Object accountLock = new Object();

	App mApp;

	Context mContext;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
		mApp = (App) mContext.getApplicationContext();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String old_accounts_create = "CREATE TABLE IF NOT EXISTS "
				+ Const.DATABASE_TABLE_ACCOUNTS_OLD
				+ "(ID INTEGER PRIMARY KEY, WEIXIN_ID TEXT);";
		
		db.beginTransaction();

		try {
			// Create tables & test data
			db.execSQL(old_accounts_create);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e(TAG, "Error creating tables and debug data" + e.getMessage());
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion) {
			return;
		}
	}
	
	public void cleanData(SQLiteDatabase db) {
		execSQL(db, "delete from " + Const.DATABASE_TABLE_ACCOUNTS_OLD + ";");
	}

	public void execSQL(SQLiteDatabase db, String sql) {
		db.beginTransaction();
		try {
			// Create tables & test data
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error execSQL " + sql + ";ErrorMessage:" + e.getMessage());
		} finally {
			db.endTransaction();
		}
	}

	public void execSQL(SQLiteDatabase db, String sql, Object[] params) {
		db.beginTransaction();
		try {
			// Create tables & test data
			db.execSQL(sql, params);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e(TAG,
					"Error execSQL " + sql + ";ErrorMessage:" + e.getMessage());
		} finally {
			db.endTransaction();
		}
	}
	
	public boolean existWeixinId(SQLiteDatabase db, String id) {
		boolean result = false;
		try {
			Cursor cursor = db.rawQuery("SELECT 1 FROM " + Const.DATABASE_TABLE_ACCOUNTS_OLD
					+ " where WEIXIN_ID = ?;", new String[] { id });
			result = cursor.getCount() > 0;
			cursor.close();
		} catch (SQLException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return result;
	}
	
	public void addAccountOld(SQLiteDatabase db, String weixin_id) {
		synchronized (accountLock) {
			try {
				db.execSQL("INSERT INTO " + Const.DATABASE_TABLE_ACCOUNTS_OLD
						+ "(WEIXIN_ID) VALUES(?)", new Object[] { weixin_id });
			} catch (SQLException e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
	}
	
	public List<String> getAccountsOld(SQLiteDatabase db) {
		synchronized (accountLock) {
			List<String> list = new ArrayList<String>();
			Cursor cursor = db.rawQuery("SELECT WEIXIN_ID FROM "
					+ Const.DATABASE_TABLE_ACCOUNTS_OLD, null);
			try {
				int count = cursor.getCount();
				if (count > 0) {
					cursor.moveToFirst();
					while (!cursor.isAfterLast()) {
						list.add(cursor.getString(0));
						cursor.moveToNext();
					}
				}
			} catch (SQLException e) {
				Log.e(TAG, "Error getRecords" + e.getMessage());
			} finally {
				cursor.close();
			}
			return list;
		}
	}

}
