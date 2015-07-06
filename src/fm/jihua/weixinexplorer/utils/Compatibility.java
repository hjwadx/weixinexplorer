package fm.jihua.weixinexplorer.utils;

import java.lang.reflect.Field;

public class Compatibility {
	
	private static int currentApi = 0;
	
	public static int getApiLevel() {

		if (currentApi > 0) {
			return currentApi;
		}

		if (android.os.Build.VERSION.SDK.equalsIgnoreCase("3")) {
			currentApi = 3;
		} else {
			try {
				Field f = android.os.Build.VERSION.class.getDeclaredField("SDK_INT");
				currentApi = (Integer) f.get(null);
			} catch (Exception e) {
				return 0;
			}
		}
		return currentApi;
	}
	
	public static boolean isCompatible(int apiLevel) {
		return getApiLevel() >= apiLevel;
	}

}
