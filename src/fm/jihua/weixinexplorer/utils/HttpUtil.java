package fm.jihua.weixinexplorer.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import android.util.Log;

public class HttpUtil {
	
	private static String _version;
	
	public static String getVersion() {
		return _version;
	}
	
	public static void setVersion(String version) {
		_version = version;
	}
	
	public static String getStringFromUrl(String url)
			throws ClientProtocolException, IOException, JSONException {
		return getStringFromUrl(url, true, null);
	}

	private static String CLIENT_USER_AGENT = "android api(version:" + getVersion() + ")";
	
	public static String getStringFromUrl(String url, boolean get,
			List<NameValuePair> params) throws ClientProtocolException,
			IOException, JSONException {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				Const.TIMEOUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(httpParameters, Const.TIMEOUT_SOCKET);

		HttpClient httpclient = new DefaultHttpClient(httpParameters);

		// Prepare a request object
		HttpRequestBase httpRequest;
//		Locale myPhoneLocale = Locale.getDefault();
//		if (Locale.CHINA.equals(myPhoneLocale)) {
//			url += "&locale=ch";
//		}
//		url += "&from_app=true";
//		if (getVersion() != null) {
//			url += "&version="+getVersion();
//		}
		if (get) {
			if (params != null) {
				for (NameValuePair p : params) {
					url += "&" + p.getName() + "=" + p.getValue();
				}
			}
			httpRequest = new HttpGet(url);
		} else {
			httpRequest = new HttpPost(url);
		}	
//		if (get) {
//			Locale myPhoneLocale = Locale.getDefault();
//			if (Locale.CHINA.equals(myPhoneLocale)) {
//				url += "&locale=ch";
//			}
//			url += "&from_app=true";
//			if (getVersion() != null) {
//				url += "&version="+getVersion();
//			}
//			if (params != null) {
//				for (NameValuePair p : params) {
//					url += "&" + p.getName() + "=" + p.getValue();
//				}
//			}
//			httpRequest = new HttpGet(url);
//
//		} else {
//			httpRequest = new HttpPost(url);}
//			Locale myPhoneLocale = Locale.getDefault();
//			if (Locale.CHINA.equals(myPhoneLocale)) {
//				params.add(new BasicNameValuePair("locale", "ch"));
//			}
//			JSONObject jsonObject = new JSONObject();
//			for (NameValuePair p : params) {
//				jsonObject.put(p.getName(), p.getValue());
//			}
//			StringEntity se = new StringEntity( jsonObject.toString(), HTTP.UTF_8 );
//            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//            se.setContentEncoding("UTF-8");
//            
//			((HttpPost) httpRequest).setEntity(se);
//		}

		httpRequest.addHeader("User-Agent", CLIENT_USER_AGENT);
		httpRequest.addHeader("accept", "application/json");
		httpRequest.addHeader("Content-Type", "application/json");

		HttpResponse response = httpclient.execute(httpRequest);
		// Get hold of the response entity
		HttpEntity entity = response.getEntity();

		// If the response does not enclose an entity, there is no need
		// to worry about connection release
		if (entity == null) {
			Log.w(Const.TAG, "entity null? " + url);
			return null; // when is this ever reached?
		}

		InputStream instream = null;
		try {
			instream = entity.getContent();
			return convertStreamToString(instream);
		} finally {
			closeQuietly(instream);
		}
	}
	
	public static String convertStreamToString(InputStream is)
			throws IOException {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is), 1024);
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
			return sb.toString();
		} finally {
			closeQuietly(is);
		}
	}
	
	/**
	 * @param stream
	 *            object to close, which can safely be <code>null</code>
	 */
	public static void closeQuietly(Closeable stream) {
		try {
			if (stream != null) {
				stream.close();
			}
		} catch (IOException e) {
			Log.e(Const.TAG, "could not close stream", e);
		}
	}

}
