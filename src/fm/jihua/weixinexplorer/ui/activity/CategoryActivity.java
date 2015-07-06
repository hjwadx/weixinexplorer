package fm.jihua.weixinexplorer.ui.activity;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import fm.jihua.weixinexplorer.App;
import fm.jihua.weixinexplorer.R;
import fm.jihua.weixinexplorer.rest.contract.DataCallback;
import fm.jihua.weixinexplorer.rest.entities.Category;
import fm.jihua.weixinexplorer.rest.entities.CategorysResult;
import fm.jihua.weixinexplorer.rest.service.DataAdapter;
import fm.jihua.weixinexplorer.ui.adapters.CategoryAdapter;

public class CategoryActivity extends BaseActivity{
	
	ListView categoryListView;
	DataAdapter mDataAdapter;
	CategoryAdapter categoryAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		MobclickAgent.onEvent(this, "enter_categories_view");
		mDataAdapter = new DataAdapter(this, new MyDataCallback());
		mDataAdapter.getCategorys();
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		categoryListView = (ListView) findViewById(R.id.category_list);
		categoryListView.setBackgroundDrawable(App.getBaseBackground(getResources()));
	}
	
	private void refreshListView(ArrayList<Category> categorys) {
		categoryAdapter = new CategoryAdapter(this, categorys);
		categoryListView.setAdapter(categoryAdapter);
	}
	
	class MyDataCallback implements DataCallback{

		@Override
		public void callback(Message msg) {
			switch (msg.what) {
			case DataAdapter.MESSAGE_GET_CATEGORYS:
				CategorysResult result = (CategorysResult)msg.obj;
				if (result != null) {
					if (result.categories != null) {
						ArrayList<Category> categorys = new ArrayList<Category>(); 
						categorys.addAll(Arrays.asList(result.categories));
						refreshListView(categorys);
					}		
				}else {
					//获取分类失败
					findViewById(R.id.category_list).setVisibility(View.GONE);
					findViewById(R.id.empty).setVisibility(View.VISIBLE);
					((TextView)findViewById(R.id.empty)).setText("获取数据失败，请确认网络连接");
				}
				break;
			default:
				break;
			}
		}
	}
	

}
