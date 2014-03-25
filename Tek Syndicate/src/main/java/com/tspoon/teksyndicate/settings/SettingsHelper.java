package com.tspoon.teksyndicate.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsHelper {

    private static final String APP_SHARED_PREFS 	= SettingsHelper.class.getSimpleName();
	private static final String NEXT_PAGE_UPLOADS 	= "video_extension";
	private static final String DATABASE_CREATED	= "database_created";
    
    private static SettingsHelper mInstance = null;
    
    private SharedPreferences preferences;
    private Editor editor;

    private SettingsHelper(Context context) {
        this.preferences = context.getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.editor = preferences.edit();
    }
    
    /**
     * Don't worry about passing the correct context in - this will only get used to retrieve the Application Context, 
     * to ensure the SharedPreferences object is shared between Activities, no matter what the current Context is.
     * @param 	context - used to retrieve the ApplicationContext
     * @return	The instance of SettingsHelper
     */
    public static SettingsHelper getInstance(Context context) {
    	if (mInstance == null) {
    		mInstance = new SettingsHelper(context);
    	}
    	return mInstance;
    }
    
    public void setNextPageToken(String pageToken) {
    	editor.putString(NEXT_PAGE_UPLOADS, pageToken);
    	editor.commit();
    }
    
    public String getNextPageToken() {
    	return preferences.getString(NEXT_PAGE_UPLOADS, "");
    }
    
    public void setDatabaseCreated() {
    	editor.putBoolean(DATABASE_CREATED, true);
    	editor.commit();
    }
    
    public boolean getDatabaseCreated() {
    	return preferences.getBoolean(DATABASE_CREATED, false); 
    }
}
