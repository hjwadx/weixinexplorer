package fm.jihua.weixinexplorer.ui.activity;


import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import fm.jihua.weixinexplorer.R;
import fm.jihua.weixinexplorer.rest.contract.DataCallback;
import fm.jihua.weixinexplorer.rest.entities.BaseResult;
import fm.jihua.weixinexplorer.rest.entities.Choiceness;
import fm.jihua.weixinexplorer.rest.entities.ChoicenessesResult;
import fm.jihua.weixinexplorer.rest.service.DataAdapter;
import fm.jihua.weixinexplorer.ui.view.AccountsView;
import fm.jihua.weixinexplorer.ui.widget.CachedImageView;
import fm.jihua.weixinexplorer.ui.widget.ScrollLayout;
import fm.jihua.weixinexplorer.utils.Const;

public class RankingsActivity extends BaseActivity{
	
	ListView accountListView;
	AccountsView accountsView;
	DataAdapter mDataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDataAdapter = new DataAdapter(this, new MyDataCallback());
		accountsView = new AccountsView(this);
		accountsView.init(Const.All_ACCOUNTS);
		accountsView.setData(null);
		accountsView.setDataAdapter(mDataAdapter);
		setContentView(accountsView);	
		mDataAdapter.getNewChoicenesses();
	}
	
	private void refreshBannerView(ArrayList<Choiceness> choicenesses) {
		if (choicenesses.size() > 0) {
			LayoutInflater inflater = LayoutInflater.from(this);
			findViewById(R.id.introcontainer).setVisibility(View.VISIBLE);
			ScrollLayout introLayout = (ScrollLayout) findViewById(R.id.tutorial);
//			introLayout.setOnScrollEventListener(new MyOnScrollEventListener());
			introLayout.setVisibility(View.VISIBLE);
			introLayout.setScrollEndStop(true);
//			introLayout.setCycling(true);
			for(final Choiceness choiceness : choicenesses){
				final View introPage = inflater.inflate(R.layout.banner_1, null);
				((TextView)introPage.findViewById(R.id.slogan)).setText(choiceness.slogan);
				((CachedImageView)introPage.findViewById(R.id.image_banner)).setLoadingBitmap(null);
				((CachedImageView)introPage.findViewById(R.id.image_banner)).setImageURI(Uri.parse(choiceness.banner_url));
				((CachedImageView)introPage.findViewById(R.id.image_banner)).setFadeIn(false);
				introPage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (choiceness.details_type == 1) {
							//进入个人界面
							MobclickAgent.onEvent(RankingsActivity.this, "action_click_editorial_item", "from_new_account");
							Intent intent = new Intent(RankingsActivity.this, ProfileActivity.class);
							intent.putExtra(Const.ACCOUNTS_FROM, Const.CHOICENESS_ACCOUNTS);
							intent.putExtra("ID", choiceness.id);
							startActivity(intent);
						} else {
							MobclickAgent.onEvent(RankingsActivity.this, "action_click_editorial_item", "from_new_accounts");
							Intent intent = new Intent(RankingsActivity.this, AccountsActivity.class);
							intent.putExtra(Const.ACCOUNTS_FROM, Const.CHOICENESS_ACCOUNTS);
							intent.putExtra("ID", choiceness.id);
							intent.putExtra("EDITORIAL", choiceness);
							startActivity(intent);
						}						
					}
				});
				introLayout.addView(introPage);
			}
//			introLayout.startScroll();
		}
	}
	
	class MyDataCallback implements DataCallback{

		@Override
		public void callback(Message msg) {
			switch (msg.what) {
			case DataAdapter.MESSAGE_GET_NEW_CHOICENESSES:
				ChoicenessesResult result = (ChoicenessesResult)msg.obj;
				if (result != null) {
					if (result.editorials != null) {
						ArrayList<Choiceness> choicenesses = new ArrayList<Choiceness>(); 
						choicenesses.addAll(Arrays.asList(result.editorials));
						refreshBannerView(choicenesses);
					}		
				}else {
					//获取new主题失败
				}
				break;
			case DataAdapter.MESSAGE_ADD_ATTENT:
				BaseResult result1 = (BaseResult)msg.obj;
				if (result1 != null) {
					Log.e("hjw","关注度增加成功");		
				}else {
//					callback.onError("获取账号信息失败");
				}
				break;
			default:
				break;
			}
		}
	}
}
