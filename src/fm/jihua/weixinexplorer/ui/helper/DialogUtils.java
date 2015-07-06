package fm.jihua.weixinexplorer.ui.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import fm.jihua.weixinexplorer.utils.AndroidSystem;

public class DialogUtils {
	
	public static void showWeixinDownloadDialog(final Context context) {
		new AlertDialog.Builder(context)
				.setTitle("提示")
				.setMessage("您还没有安装微信，是否马上下载？")
				.setPositiveButton("下载", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						AndroidSystem.openBrowser(context, "http://weixin.qq.com/d");
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						dialoginterface.dismiss();
					}
				}).show();
	}

}
