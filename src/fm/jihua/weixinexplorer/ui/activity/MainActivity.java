package fm.jihua.weixinexplorer.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import fm.jihua.weixinexplorer.R;

public class MainActivity extends Activity {
	
	Button attentButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		attentButton = (Button) findViewById(R.id.attent);
		attentButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				String str = "http://weixin.qq.com/r/qHVQX2nEOldFh3g8nyCM";
//				startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
				
				Intent intent = new Intent();         
				intent.setAction("android.intent.action.VIEW");         
				Uri uri = Uri.parse("http://weixin.qq.com/r/qHVQX2nEOldFh3g8nyCM");        
				intent.setData(uri); 

				//包名、要打开的activity 
				intent.setClassName("com.tencent.mm",  "com.tencent.mm.ui.qrcode.GetQRCodeInfoUI");         
				startActivity(intent); 
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
