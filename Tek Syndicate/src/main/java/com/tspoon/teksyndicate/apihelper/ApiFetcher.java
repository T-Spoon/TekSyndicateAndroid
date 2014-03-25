package com.tspoon.teksyndicate.apihelper;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.tspoon.teksyndicate.App;
import com.tspoon.teksyndicate.settings.SettingsHelper;

public class ApiFetcher {

	private static final String URL_GET_PLAYLIST = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&key=" + App.YOUTUBE_WEB_API_KEY + "&playlistId=";
	private static final String PARAM_PAGE_TOKEN = "&pageToken=";
	public static final String PLAYLIST_UPLOADS = "UUNovoA9w0KnxyDP5bGrOYzg";
	
	private static RequestQueue mRequestQueue;
	private static ApiFetcher mApi;
	private static SettingsHelper mSettings;
	
	private ApiFetcher(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
		mSettings = SettingsHelper.getInstance(context);
	}
	
	public static ApiFetcher getInstance(Context context) {
		if(mApi == null) {
			mApi = new ApiFetcher(context);
		}
		return mApi;
	}
	
	public void getPlaylistForCategoryAsync(String playlist, Response.Listener<JSONObject> success, Response.ErrorListener error) {
		StringBuilder requestURL = new StringBuilder(URL_GET_PLAYLIST).append(playlist);
		String pageToken = mSettings.getNextPageToken();
		if(!pageToken.equals(""))
			requestURL.append(PARAM_PAGE_TOKEN).append(pageToken);
		
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL.toString(), null, success, error);
		mRequestQueue.add(request);
	}
	
	public JSONObject getPlaylistForCategory(String playlist, RequestFuture<JSONObject> future) {
		StringBuilder requestURL = new StringBuilder(URL_GET_PLAYLIST).append(playlist);
		String pageToken = mSettings.getNextPageToken();
		if(!pageToken.equals(""))
			requestURL.append(PARAM_PAGE_TOKEN).append(pageToken);
		
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL.toString(), null, future, future);
		mRequestQueue.add(request);
		try {
			return future.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
