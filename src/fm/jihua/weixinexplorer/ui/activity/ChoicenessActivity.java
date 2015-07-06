package fm.jihua.weixinexplorer.ui.activity;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import fm.jihua.weixinexplorer.R;
import fm.jihua.weixinexplorer.rest.contract.DataCallback;
import fm.jihua.weixinexplorer.rest.entities.Choiceness;
import fm.jihua.weixinexplorer.rest.entities.ChoicenessesResult;
import fm.jihua.weixinexplorer.rest.service.DataAdapter;
import fm.jihua.weixinexplorer.ui.adapters.ChoicenessAdapter;
import fm.jihua.weixinexplorer.utils.Const;

public class ChoicenessActivity extends BaseActivity{
	
	ListView choicenessListView;
	DataAdapter mDataAdapter;
	ChoicenessAdapter choicenessAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choiceness);
		MobclickAgent.onEvent(this, "enter_editorials_view");
		mDataAdapter = new DataAdapter(this, new MyDataCallback());
		mDataAdapter.getChoicenesses();
		findView();
	}

	private void findView() {
		choicenessListView = (ListView) findViewById(R.id.choiceness_list);
	}
	
	private void refreshListView(ArrayList<Choiceness> choicenesses) {
		choicenessAdapter = new ChoicenessAdapter(this, choicenesses);
		choicenessListView.setAdapter(choicenessAdapter);
		choicenessListView.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> listView, View arg1, int position,
				long arg3) {
			Choiceness choiceness = (Choiceness) listView.getAdapter().getItem(position);
			if (choiceness.details_type == 1) {
				//进入个人界面
				MobclickAgent.onEvent(ChoicenessActivity.this, "action_click_editorial_item", "from_history_account");
				Intent intent = new Intent(ChoicenessActivity.this, ProfileActivity.class);
				intent.putExtra(Const.ACCOUNTS_FROM, Const.CHOICENESS_ACCOUNTS);
				intent.putExtra("ID", choiceness.id);
				startActivity(intent);
			} else {
				MobclickAgent.onEvent(ChoicenessActivity.this, "action_click_editorial_item", "from_history_accounts");
				Choiceness editorial = (Choiceness) listView.getAdapter().getItem(position);
				Intent intent = new Intent(ChoicenessActivity.this, AccountsActivity.class);
				intent.putExtra(Const.ACCOUNTS_FROM, Const.CHOICENESS_ACCOUNTS);
				intent.putExtra("ID", choiceness.id);
				intent.putExtra("EDITORIAL", editorial);
				startActivity(intent);
			}
//			finish();
		}		
	}
	
	class MyDataCallback implements DataCallback{

		@Override
		public void callback(Message msg) {
			switch (msg.what) {
			case DataAdapter.MESSAGE_GET_CHOICENESSES:
				ChoicenessesResult result = (ChoicenessesResult)msg.obj;
				if (result != null) {
					if (result.editorials != null) {
						ArrayList<Choiceness> choicenesses = new ArrayList<Choiceness>(); 
						choicenesses.addAll(Arrays.asList(result.editorials));
						refreshListView(choicenesses);
					}		
				}else {
					//获取分类失败
					findViewById(R.id.choiceness_list).setVisibility(View.GONE);
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
