/*
 * Tspoon | 2014
 */

package com.tspoon.teksyndicate.db;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

/**
 * Created by Tspoon on 22/03/2014.
 */
public class DBAdapter {
	protected static final String TAG = "DBAdapter";

	public static void removeVideoData() {
		new Delete().from(VideoItem.class).execute();
	}

	public static List<VideoItem> getVideoFeed() {
		Log.d(TAG, "getVideoFeed");
		return new Select()
				.from(VideoItem.class)
				.execute();
	}
	
	public static VideoItem getVideo(int id) {
		return new Select()
				.from(VideoItem.class)
				.where("VideoID = ?", id)
				.executeSingle();
	}

	public static void popuplateVideosFromJSON(JSONArray items) {
		ActiveAndroid.beginTransaction();
		try {
			for (int i = 0; i < items.length(); i++) {
				JSONObject object = items.getJSONObject(i).getJSONObject("snippet");
				VideoItem item = new VideoItem();
				item.title = object.getString("title");
				item.videoToken = object.getJSONObject("resourceId").getString("videoId");
				item.datePublished = object.getString("publishedAt");
				item.imageURL = object.getJSONObject("thumbnails").getJSONObject("high").getString("url");
                item.itemViewed = 0;
				item.save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} catch (JSONException e) {
			ActiveAndroid.setTransactionSuccessful();
			e.printStackTrace();
		}
		finally {
			ActiveAndroid.endTransaction();
		}
	}
	
	public static List<Category> getCategories() {
		return new Select().all().from(Category.class).execute();
	}
	
	public static void insertCategories() {
		if(getCategories().size() == 0) {
			ActiveAndroid.beginTransaction();
			String[] categoryNames = new String[] { "Tek Syndicate", "Uploads", "The Tek" }; 
			try {
				for(int i = 0; i < categoryNames.length; i++) {
					Category c = new Category(i + 1, categoryNames[i]);
					c.save();
				}
				ActiveAndroid.setTransactionSuccessful();
			} 
			finally {
				ActiveAndroid.endTransaction();
			}
		} else {
			Log.w(TAG, "Inconsistent State - Attempting to insert categories when already present");
		}
	}

    public static void setItemViewed(VideoItem item, int viewed) {
        item.setItemViewed(viewed);
    }

	/*
    public Video getVideo(int videoID) {
    	Cursor c = db.query("Babe_Clips", null, "clip_id = ?", new String[]{ Integer.toString(videoID) }, null, null, null);
        c.moveToFirst();
        Video v = new Video(
    			c.getInt(c.getColumnIndex(CLIP_COLUMN_ID)), 
    			c.getString(c.getColumnIndex(CLIP_COLUMN_SRC)),
    			c.getInt(c.getColumnIndex(CLIP_COLUMN_LEVEL)), 
    			c.getInt(c.getColumnIndex(CLIP_COLUMN_SKIP)), 
    			c.getString(c.getColumnIndex(CLIP_COLUMN_SCENE)), 
    			c.getInt(c.getColumnIndex(CLIP_COLUMN_PRELOAD)),
    			c.getString(c.getColumnIndex(CLIP_COLUMN_NAME)),
    			c.getInt(c.getColumnIndex(CLIP_COLUMN_COST))
    			);
        c.close();

        // Translate if necessary
        if(mShouldTranslate)
        	updateLanguageForVideo(v);

        return v;
    }
	 */
}