package com.tspoon.teksyndicate;


import com.activeandroid.ActiveAndroid;
import com.tspoon.teksyndicate.db.DBAdapter;
import com.tspoon.teksyndicate.settings.SettingsHelper;

public class App extends com.activeandroid.app.Application {
	
	public static String LANG = "EN";
	public static boolean JUST_LAUNCHED;

    // TODO: Replace this key with your own
    public static final String YOUTUBE_API_KEY = "AIzaSyDQeyA3ine6OJbF02v-XWLCLQRGQwnXlFE";
    public static final String YOUTUBE_WEB_API_KEY = "AIzaSyBI2uiFShkyibHr56Nxn1mfnMW27DD3i-8";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		ActiveAndroid.initialize(this);
		
		SettingsHelper settings = SettingsHelper.getInstance(this);
		
		if(!settings.getDatabaseCreated()) {
			DBAdapter.insertCategories();
			settings.setDatabaseCreated();
		}
		
		LANG = getApplicationContext().getResources().getString(R.string.language);
	}
	
	@Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
