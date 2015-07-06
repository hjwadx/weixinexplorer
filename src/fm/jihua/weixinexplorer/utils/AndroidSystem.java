package fm.jihua.weixinexplorer.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class AndroidSystem {
	
	public static void shareText(Context context, String title, String text) {
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("text/plain");
	    intent.putExtra(Intent.EXTRA_SUBJECT, title);
	    intent.putExtra(Intent.EXTRA_TEXT, text);
	    context.startActivity(Intent.createChooser(intent, "选择分享方式"));
	}
	
	public static void openBrowser(Context context, String url) {
		Intent intent = new Intent("android.intent.action.VIEW",Uri.parse(url));
	    context.startActivity(Intent.createChooser(intent, "选择浏览器"));
	}
	
	public static boolean isIntentAvailable(Context context, Intent intent) { 
        final PackageManager packageManager = context.getPackageManager(); 
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 
                PackageManager.MATCH_DEFAULT_ONLY); 
        return list.size() > 0; 
    }

}
