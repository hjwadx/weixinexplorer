package fm.jihua.weixinexplorer.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

import fm.jihua.weixinexplorer.App;

public class BaseActivity extends Activity{
	
public static final int DIALOG_FOR_BLOCK = 0x1111;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		App app = (App)getApplication();
		app.addActivity(this);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		App app = (App)getApplication();
		app.removeActivity(this);
	}
	
	protected void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);
	}
	
	protected void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
	
	//如果已经启动了四个Activity：A，B，C和D，在D Activity里，想再启动一个Actvity B，但不变成A,B,C,D,B，而是希望是A,C,D,B，则可以像下面写代码：
	public void startActivity(Activity activity, Class<?> cls){
		Intent intent = new Intent(activity, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); 
		activity.startActivity(intent);
//		activity.finish();
	}
	
//	@Override
//	protected Dialog onCreateDialog(int id) {
//		if (!Compatibility.isCompatible(8)) {
//			return onCreateDialog(id, new Bundle());
//		}
//		return super.onCreateDialog(id);
//	}
	
//	@Override
//	protected Dialog onCreateDialog(int id, Bundle args) {
//		switch (id) {
//		case DIALOG_FOR_BLOCK:{
//			Dialog dialog = new Dialog(this, R.style.Dialog_Fullscreen);
//			dialog.setContentView(R.layout.dialog);
//			String message = args.getString("message");
//			if (message != null && message.length() != 0) {
//				dialog.findViewById(R.id.tv_message).setVisibility(View.VISIBLE);
//				((TextView)dialog.findViewById(R.id.tv_message)).setText(message);
//			}else {
//				dialog.findViewById(R.id.tv_message).setVisibility(View.GONE);
//			}
//			return dialog;
//		}
//		default:
//			return super.onCreateDialog(id);
//		}
//	}
//	
//	@Override
//	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
//		super.onPrepareDialog(id, dialog, args);
//		switch (id) {
//		case DIALOG_FOR_BLOCK:
//			String message = args.getString("message");
//			if (message != null && message.length() != 0) {
//				dialog.findViewById(R.id.tv_message).setVisibility(View.VISIBLE);
//				((TextView)dialog.findViewById(R.id.tv_message)).setText(message);
//			}else {
//				dialog.findViewById(R.id.tv_message).setVisibility(View.GONE);
//			}
//			break;
//		default:
//			break;
//		}
//	}

}
