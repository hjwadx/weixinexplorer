package fm.jihua.weixinexplorer.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import fm.jihua.weixinexplorer.App;
import fm.jihua.weixinexplorer.R;
import fm.jihua.weixinexplorer.cache.ImageFetcher;

public class CachedImageView extends ImageView{
	
	private ImageFetcher mImageFetcher;

	public CachedImageView(Context context) {
		super(context);
		init();
	}

	public CachedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	void init(){
		mImageFetcher = new ImageFetcher(getContext(), 0);
        mImageFetcher.setLoadingImage(R.drawable.default_avatar_weixin);
        App app = (App)getContext().getApplicationContext();
        mImageFetcher.setImageCache(app.getImageCache());
	}
	
	public void setFadeIn(boolean fadeIn){
		mImageFetcher.setImageFadeIn(fadeIn);
	}
	
	public void setCorner(boolean corner){
		mImageFetcher.setImageCorner(corner);
	}
	
	public void setLoadingBitmap(Bitmap bmp){
		mImageFetcher.setLoadingImage(bmp);
	}

	@Override
	public void setImageURI(final Uri uri) {
		if (mImageFetcher != null) {
			mImageFetcher.loadImage(uri, this);
		}
	}

}
