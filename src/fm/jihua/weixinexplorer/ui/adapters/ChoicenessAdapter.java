package fm.jihua.weixinexplorer.ui.adapters;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fm.jihua.weixinexplorer.R;
import fm.jihua.weixinexplorer.rest.entities.Choiceness;
import fm.jihua.weixinexplorer.ui.widget.CachedImageView;

public class ChoicenessAdapter extends BaseAdapter{
	
	List<Choiceness> mChoicenesses;
	Context context;
	
	public ChoicenessAdapter(Context context, List<Choiceness> choicenesses) {
		this.context = context;
		setData(choicenesses);
	}
	
	public void setData(List<Choiceness> choicenesses) {
		this.mChoicenesses = choicenesses;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mChoicenesses.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mChoicenesses.get(position);
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
					R.layout.choiceness_item, parent, false);
			holder = new ViewHolder();
			holder.banner = (CachedImageView) convertView.findViewById(R.id.banner);
			holder.slogan = (TextView) convertView.findViewById(R.id.slogan);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			final Choiceness choiceness = mChoicenesses.get(position);
			holder.banner.setLoadingBitmap(null);
			holder.banner.setImageURI(Uri.parse(choiceness.banner_url));
			holder.banner.setFadeIn(false);
			holder.slogan.setText(choiceness.slogan);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}
	
	static class ViewHolder{
		CachedImageView banner;
		TextView slogan;
	}

}
