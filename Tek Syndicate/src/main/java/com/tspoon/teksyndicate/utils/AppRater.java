package com.tspoon.teksyndicate.utils;

import com.tspoon.teksyndicate.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class AppRater {
    private static String APP_PACKAGE;
    
    private final static int DAYS_UNTIL_PROMPT 		= 1;
    private final static int LAUNCHES_UNTIL_PROMPT 	= 3;
    
    public static void appLaunched(Context mContext) {
        
    	APP_PACKAGE = mContext.getPackageName();
    	SharedPreferences prefs = mContext.getSharedPreferences("app_rater", 0);
        SharedPreferences.Editor editor = prefs.edit();
        //editor.clear();
        //editor.commit();
        if (prefs.getBoolean("dontshowagain", false)) { return; }
        
        
        // Increment launch counter
        long launchCount = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launchCount);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        
        // Wait at least n days before opening
        if (launchCount >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }
        
        editor.commit();
    }   
    
    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
    	AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(R.string.rate_title);
        dialog.setMessage(R.string.rate_text);
        
        dialog.setPositiveButton(R.string.rate_positive,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PACKAGE)));
                        if (editor != null) {
                            editor.putBoolean("dontshowagain", true);
                            editor.commit();
                        }
                        dialog.dismiss();
                    }
                });

        dialog.setNeutralButton(R.string.rate_neutral, 
        		new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialog.setNegativeButton(R.string.rate_negative,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	if (editor != null) {
                            editor.putBoolean("dontshowagain", true);
                            editor.commit();
                        }
                        dialog.dismiss();
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{ "gtspoon@hotmail.com" });
                        i.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.feedback_title));
                        i.putExtra(Intent.EXTRA_TEXT   , mContext.getResources().getString(R.string.feedback_message));
                        try {
                            mContext.startActivity(Intent.createChooser(i, mContext.getResources().getString(R.string.feedback_title)));
                        } catch (android.content.ActivityNotFoundException ex) {
                            //Toast.makeText(MyActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        dialog.show();        
    }
}