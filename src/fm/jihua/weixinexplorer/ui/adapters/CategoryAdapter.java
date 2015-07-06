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
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import fm.jihua.weixinexplorer.R;
import fm.jihua.weixinexplorer.rest.entities.Category;
import fm.jihua.weixinexplorer.ui.activity.AccountsActivity;
import fm.jihua.weixinexplorer.ui.activity.ProfileActivity;
import fm.jihua.weixinexplorer.ui.widget.CachedImageView;
import fm.jihua.weixinexplorer.utils.Const;

public class CategoryAdapter extends BaseAdapter{
	
	List<Category> mCategorys;
	Context context;
	
	public CategoryAdapter(Context context, List<Category> categorys) {
		this.context = context;
		setData(categorys);
	}
	
	public void setData(List<Category> categorys) {
		this.mCategorys = categorys;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCategorys.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mCategorys.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.category_item, parent, false);
			holder = new ViewHolder();
			holder.avatar0 = (CachedImageView) convertView.findViewById(R.id.avatar0);
			holder.avatar1 = (CachedImageView) convertView.findViewById(R.id.avatar1);
			holder.avatar2 = (CachedImageView) convertView.findViewById(R.id.avatar2);
			holder.more = (CachedImageView) convertView.findViewById(R.id.more);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.amount = (TextView)convertView.findViewById(R.id.amount);
			holder.name0 = (TextView) convertView.findViewById(R.id.name0);
			holder.name1 = (TextView) convertView.findViewById(R.id.name1);
			holder.name2 = (TextView) convertView.findViewById(R.id.name2);
			holder.more_text = (TextView) convertView.findViewById(R.id.more_text);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			final Category category = mCategorys.get(position);
			holder.avatar0.setImageURI(Uri.parse(category.top_accounts[0].icon_url));
			holder.avatar1.setImageURI(Uri.parse(category.top_accounts[1].icon_url));
			holder.avatar2.setImageURI(Uri.parse(category.top_accounts[2].icon_url));
			holder.name0.setText(category.top_accounts[0].name);
			holder.name1.setText(category.top_accounts[1].name);
			holder.name2.setText(category.top_accounts[2].name);
			holder.name.setText(category.name);
			holder.amount.setText(String.valueOf(category.num_accounts) + "个");
			holder.avatar0.setCorner(true);
			holder.avatar1.setCorner(true);
			holder.avatar2.setCorner(true);
			holder.avatar0.setFadeIn(false);
			holder.avatar1.setFadeIn(false);
			holder.avatar2.setFadeIn(false);
			holder.avatar0.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(context, "action_click_avatar", "form_categories_top3");
					Intent intent = new Intent(context, ProfileActivity.class);
					intent.putExtra("ACCOUNT", category.top_accounts[0]);
					context.startActivity(intent);				
				}
			});
			holder.avatar1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(context, "action_click_avatar", "form_categories_top3");
					Intent intent = new Intent(context, ProfileActivity.class);
					intent.putExtra("ACCOUNT", category.top_accounts[1]);
					context.startActivity(intent);	
				}
			});
			holder.avatar2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(context, "action_click_avatar", "form_categories_top3");
					Intent intent = new Intent(context, ProfileActivity.class);
					intent.putExtra("ACCOUNT", category.top_accounts[2]);
					context.startActivity(intent);	
				}
			});
			holder.more.setFadeIn(false);
			holder.more.setCorner(true);
			holder.more.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MobclickAgent.onEvent(context, "action_click_more_in_categories", category.name);
					Intent intent = new Intent(context, AccountsActivity.class);
					intent.putExtra(Const.ACCOUNTS_FROM, Const.CATEGORY_ACCOUNTS);
					intent.putExtra("ID", category.id);
					context.startActivity(intent);	
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}
	
	static class ViewHolder{
		CachedImageView avatar0;
		CachedImageView avatar1;
		CachedImageView avatar2;
		CachedImageView more;
		TextView name0;
		TextView name1;
		TextView name2;
		TextView more_text;
		TextView name;
		TextView amount;
	}

}
