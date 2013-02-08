package com.search;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.session.Sessions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebView;

public class Gsearch extends Activity {

	String search_url = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
	String search_item ;
	String search_query ;

	WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		search_item = ((Sessions)getApplication()).getSearch_item();
		search_query = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q="+search_item;
		webView = new WebView(this);
		setContentView(webView);
		
		
		new JsonSearchTask().execute();
	}

	private class JsonSearchTask extends AsyncTask<Void, Void, Void> {
		String searchResult = "";

		@Override
		protected Void doInBackground(Void... arg0) {

			try {
				searchResult = ParseResult(sendQuery(search_query));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			webView.loadData(searchResult, "text/html", "UTF-8");
			super.onPostExecute(result);
		}

	}

	private String sendQuery(String query) throws IOException {
		String result = "";

		URL searchURL = new URL(query);
		HttpURLConnection httpURLConnection = (HttpURLConnection) searchURL
				.openConnection();

		if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStreamReader inputStreamReader = new InputStreamReader(
					httpURLConnection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader, 8192);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				result += line;
			}
			bufferedReader.close();
		}
		return result;
	}

	private String ParseResult(String json) throws JSONException {
		String parsedResult = "";

		JSONObject jsonObject = new JSONObject(json);
		JSONObject jsonObject_responseData = jsonObject
				.getJSONObject("responseData");
		JSONArray jsonArray_results = jsonObject_responseData
				.getJSONArray("results");

		parsedResult += "Google Search APIs (JSON) for : <b>" + search_item
				+ "</b><br/>";
		parsedResult += "Number of results returned = <b>"
				+ jsonArray_results.length() + "</b><br/><br/>";

		for (int i = 0; i < jsonArray_results.length(); i++) {

			JSONObject jsonObject_i = jsonArray_results.getJSONObject(i);

			String iTitle = jsonObject_i.getString("title");
			String iContent = jsonObject_i.getString("content");
			String iUrl = jsonObject_i.getString("url");

			parsedResult += "<a href='" + iUrl + "'>" + iTitle + "</a><br/>";
			parsedResult += iContent + "<br/><br/>";
		}

		return parsedResult;
	}
}

