package fm.jihua.weixinexplorer.rest.contract;

import fm.jihua.weixinexplorer.rest.entities.AccountsResult;
import fm.jihua.weixinexplorer.rest.entities.BaseResult;
import fm.jihua.weixinexplorer.rest.entities.CategorysResult;
import fm.jihua.weixinexplorer.rest.entities.ChoicenessesResult;

public interface DataProvider {
	
	public AccountsResult getAccounts(int page, int limit);
	public ChoicenessesResult getNewChoicenesses();
	public ChoicenessesResult getChoicenesses();
	public AccountsResult getChoiceness(int id);
	public CategorysResult getCategorys();
	public AccountsResult getCategory(int id);
	public BaseResult attentAccount(int id);
	
	public AccountsResult getCategory(String id, int page, int limit);
}
