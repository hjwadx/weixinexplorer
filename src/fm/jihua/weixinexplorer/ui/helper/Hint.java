package fm.jihua.weixinexplorer.ui.helper;

import android.content.Context;
import android.widget.Toast;

public class Hint {
	
	public static void showTipsShort(Context cx, String msg) {
//		App app = (App) cx.getApplicationContext();
//		if (app.getState() == Const.STATE_PAUSE)
//			return;
		Toast.makeText(cx, msg, Toast.LENGTH_SHORT).show();
	}

	public static void showTipsLong(Context cx, String msg) {
//		App app = (App) cx.getApplicationContext();
//		if (app.getState() == Const.STATE_PAUSE)
//			return;
		Toast.makeText(cx, msg, Toast.LENGTH_LONG).show();
	}

}
