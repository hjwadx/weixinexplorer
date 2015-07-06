package fm.jihua.weixinexplorer.rest.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import fm.jihua.weixinexplorer.App;
import fm.jihua.weixinexplorer.rest.contract.DataCallback;
import fm.jihua.weixinexplorer.rest.entities.AccountsResult;
import fm.jihua.weixinexplorer.rest.entities.BaseResult;
import fm.jihua.weixinexplorer.rest.entities.CategorysResult;
import fm.jihua.weixinexplorer.rest.entities.ChoicenessesResult;

public class DataAdapter {
	
	public static final int MESSAGE_GET_ACCOUNTS = 1;
	public static final int MESSAGE_GET_NEW_CHOICENESSES = 2;
	public static final int MESSAGE_GET_CHOICENESSES = 3;
	public static final int MESSAGE_GET_CHOICENESS = 4;
	public static final int MESSAGE_GET_CATEGORYS = 5;
	public static final int MESSAGE_GET_CATEGORY = 6;
	

	public static final int MESSAGE_ADD_ATTENT = 7;
	
	private DataCallback mCallback;
	private android.app.Activity mContext;
	private UIHandler mHandler;
	private App mApp;
	
	public DataAdapter(android.app.Activity context, DataCallback callback) {
		this.mCallback = callback;
		this.mContext = context;
		mApp = (App) context.getApplication();
		Looper looper = Looper.myLooper();
		if (looper != null && mHandler == null) {
			mHandler = new UIHandler(looper);
		}
	}
	
	public void getAccounts(final int page, final int limit) {
		new Thread() {
			@Override
			public void run() {
				AccountsResult result = DataService.getProvider().getAccounts(page, limit);
				mHandler.sendMessage(createMessage(MESSAGE_GET_ACCOUNTS, result));
			}
		}.start();
	}
	
	public void getCategorys() {
		new Thread() {
			@Override
			public void run() {
				CategorysResult result = DataService.getProvider().getCategorys();
				mHandler.sendMessage(createMessage(MESSAGE_GET_CATEGORYS, result));
			}
		}.start();
	}
	
	public void getCategory(final int id) {
		new Thread() {
			@Override
			public void run() {
				AccountsResult result = DataService.getProvider().getCategory(id);
				mHandler.sendMessage(createMessage(MESSAGE_GET_CATEGORY, result));
			}
		}.start();
	}
	
	public void getCategory(final String id, final int offset, final int limit) {
		new Thread() {
			@Override
			public void run() {
				AccountsResult result = DataService.getProvider().getCategory(id, offset, limit);
				mHandler.sendMessage(createMessage(MESSAGE_GET_CATEGORY, result));
			}
		}.start();
	}
	
	public void getChoicenesses() {
		new Thread() {
			@Override
			public void run() {
				ChoicenessesResult result = DataService.getProvider().getChoicenesses();
				mHandler.sendMessage(createMessage(MESSAGE_GET_CHOICENESSES, result));
			}
		}.start();
	}
		
	public void getChoiceness(final int id) {
		new Thread() {
			@Override
			public void run() {
				AccountsResult result = DataService.getProvider().getChoiceness(id);
				mHandler.sendMessage(createMessage(MESSAGE_GET_CHOICENESS, result));
			}
		}.start();
	}
	
	public void getNewChoicenesses() {
		new Thread() {
			@Override
			public void run() {
				ChoicenessesResult result = DataService.getProvider().getNewChoicenesses();
				mHandler.sendMessage(createMessage(MESSAGE_GET_NEW_CHOICENESSES, result));
			}
		}.start();
	}
	
	public void attentAccount(final int id) {
		new Thread() {
			@Override
			public void run() {
				BaseResult result = DataService.getProvider().attentAccount(id);
				mHandler.sendMessage(createMessage(MESSAGE_ADD_ATTENT, result));
			}
		}.start();
	}	
	
	private Message createMessage(int what, Object obj) {
		return Message.obtain(null, what, obj);
	}
	
	private class UIHandler extends Handler {
		public UIHandler(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
			if (msg != null) {
				if (mCallback != null && !mContext.isFinishing()) {
					mCallback.callback(msg);
				}
				super.handleMessage(msg);
			}
		}
	}

}
