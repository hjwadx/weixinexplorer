package fm.jihua.weixinexplorer.rest.entities;

import java.io.Serializable;


public class Account implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5792014400304577953L;
	
	public int id;
	public String name;
	public String weixin_id;
	public String qrcode_url;	
	public int num_followers;
	public String detail;
	public String icon_url;
	public String icon_url_large;
	public ScreenShot[] screenshots;
	
	public String category_name;
	
	public Account() {
	}
	
	public Account(int id, String name, String weixin_id, String weixin_url, int num_followers,
			String detail, String icon_url, String icon_url_large, ScreenShot[] screenshots,
			String category_name) {
		super();
		this.id = id;
		this.name = name;
		this.weixin_id = weixin_id;
		this.qrcode_url = weixin_url;
		this.num_followers = num_followers;
		this.detail = detail;
		this.icon_url = icon_url;
		this.icon_url_large = icon_url_large;
		this.screenshots = screenshots;
		this.category_name = category_name;
	}
}
