package fm.jihua.weixinexplorer.ui.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import fm.jihua.weixinexplorer.App;
import fm.jihua.weixinexplorer.AttentListener;
import fm.jihua.weixinexplorer.R;
import fm.jihua.weixinexplorer.rest.entities.Account;
import fm.jihua.weixinexplorer.ui.helper.DialogUtils;
import fm.jihua.weixinexplorer.ui.widget.CachedImageView;
import fm.jihua.weixinexplorer.utils.AndroidSystem;


public class AccountAdapter extends BaseAdapter{
	
	List<Account> mAccounts;
	Context context;
	App mApp;
	AttentListener attentListener;
	
	public AccountAdapter(Context context, List<Account> accounts) {
		this.context = context;
		mApp = (App) context.getApplicationContext();
		setData(accounts);
	}
	
	public void setAttentListener(AttentListener attentListener) {
		this.attentListener = attentListener;
	}
	
	public void setData(List<Account> accounts) {
		this.mAccounts = accounts;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mAccounts.size();
	}

	@Override
	public Object getItem(int position) {
		return mAccounts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.account_item, parent, false);
			holder = new ViewHolder();
			holder.avatar = (CachedImageView) convertView.findViewById(R.id.avatar);
			holder.name = (TextView) convertView.findViewById(R.id.name);
//			holder.weixin_id = (TextView)convertView.findViewById(R.id.weixin_id);
			holder.category = (TextView) convertView.findViewById(R.id.category);
			holder.follower = (TextView) convertView.findViewById(R.id.followers);
			holder._new = (TextView) convertView.findViewById(R.id._new);
			holder.attent = (Button) convertView.findViewById(R.id.attention);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			final Account account = mAccounts.get(position);
			holder.avatar.setFadeIn(false);
			holder.avatar.setCorner(true);
			holder.avatar.setImageURI(Uri.parse(account.icon_url));
			holder.name.setText(account.name);
//			holder.weixin_id.setText(account.weixin_id);
			holder.category.setText(account.category_name);
			holder.follower.setText("关注:" + String.valueOf(account.num_followers) + "人");
			if (mApp.existWeixinId(account.weixin_id)) {
				holder._new.setVisibility(View.GONE);
			} else {
				holder._new.setVisibility(View.VISIBLE);
			}
			holder.attent.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();         
					intent.setAction("android.intent.action.VIEW"); 
					Uri uri = Uri.parse(account.qrcode_url); 
//					Uri uri = Uri.parse("http://weixin.qq.com/r/ccKhruTEVNorh-XNn5dV");        
					intent.setData(uri); 

					//包名、要打开的activity 
					intent.setClassName("com.tencent.mm",  "com.tencent.mm.ui.qrcode.GetQRCodeInfoUI");
					MobclickAgent.onEvent(context, "action_attention", "from_listview");
					if (AndroidSystem.isIntentAvailable(context, intent)) {
						context.startActivity(intent); 
					} else {
						Toast.makeText(context, "请先下载微信", Toast.LENGTH_LONG).show();
						DialogUtils.showWeixinDownloadDialog(context);
					}
					if (attentListener != null) {
						attentListener.onAttented(account.id);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}
	
	static class ViewHolder{
		CachedImageView avatar;
		TextView name;
//		TextView weixin_id;
		TextView category;
		TextView follower;
		TextView _new;
		Button attent;
	}
}
