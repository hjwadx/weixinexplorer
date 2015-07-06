package fm.jihua.weixinexplorer.rest.entities;

import java.io.Serializable;

public class ScreenShot implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 335161717064002379L;
	
	public String small;
	public String large;
	
	public ScreenShot() {
	}

	public ScreenShot(String small, String large) {
		super();
		this.small = small;
		this.large = large;
	}
}
