package fm.jihua.weixinexplorer.rest.entities;

import java.io.Serializable;

public class Category implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8092257504290618956L;
	
	public int id;
	public String name;
	public int num_accounts;
	public Account[] top_accounts;
	
	public Category() {
	}

	public Category(int id, String name, int num_accounts,
			Account[] top_accounts) {
		super();
		this.id = id;
		this.name = name;
		this.num_accounts = num_accounts;
		this.top_accounts = top_accounts;
	}
}
