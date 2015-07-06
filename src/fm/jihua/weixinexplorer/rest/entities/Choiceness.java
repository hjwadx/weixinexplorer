package fm.jihua.weixinexplorer.rest.entities;

import java.io.Serializable;

public class Choiceness implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5930150318339949117L;
	
	public int id;
	public String banner_url;
	public String slogan;
	public int details_type;
	public Account[] accounts;
	
	public Choiceness() {
	}

	public Choiceness(int id, String banner_url, String slogan, int details_type,
			Account[] accounts) {
		super();
		this.id = id;
		this.banner_url = banner_url;
		this.slogan = slogan;
		this.details_type = details_type;
		this.accounts = accounts;
	}
}
