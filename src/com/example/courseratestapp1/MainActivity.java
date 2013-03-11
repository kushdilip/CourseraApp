package com.example.courseratestapp1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RunnableThread rth = new RunnableThread(this);
		rth.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private InputStream retrieveStream(String url) {

		DefaultHttpClient client = new DefaultHttpClient();

		HttpGet getRequest = new HttpGet(url);

		try {

			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(), "Error " + statusCode
						+ " for URL " + url);
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();

		} catch (IOException e) {
			getRequest.abort();
			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}

		return null;

	}

	class RunnableThread extends AsyncTask<Void, Void, Void> {
		DefaultHttpClient client;
		MainActivity ma = null;

		public RunnableThread(MainActivity ma) {
			this.ma = ma;
			client = new DefaultHttpClient();
		}

		@Override
		protected Void doInBackground(Void... params) {
			StringBuilder builder = new StringBuilder();
			
//			String url = "https://www.coursera.org/maestro/api/topic/list?full=1";
			String url = "https://www.coursera.org/maestro/api/university/list";
			//String url = "http://search.twitter.com/search.json?q=javacodegeeks";
			
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
			
			
			try {
				InputStream source = ma.retrieveStream(url);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(source, Charset.forName("UTF-8")));

				int cp, count = 0;
				while ((cp = reader.read()) != -1) {
					builder.append((char) cp);
					count++;
				}
				
				String jsonText = builder.toString();
				
				JSONArray jsonArray = new JSONArray(jsonText);
				
				Log.i(ma.getClass().toString(), "Size of jsonArray :" + jsonArray.length());
				
				

			} catch (Exception e) {
				// TODO: handle exception
			}

			return null;
		}

	}

}
