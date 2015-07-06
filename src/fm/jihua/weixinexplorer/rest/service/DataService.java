package fm.jihua.weixinexplorer.rest.service;

import fm.jihua.weixinexplorer.rest.contract.DataProvider;


public class DataService {
	private static DataProvider _provider = null;

	public static synchronized DataProvider getProvider() {
		if (_provider == null) {
			throw new IllegalStateException("provider not set");
		}

		return _provider;
	}

	public static synchronized void setProvider(DataProvider provider) {
		if (_provider != null) {
			//throw new IllegalStateException("provider already set");
		}
		_provider = provider;
	}
}
