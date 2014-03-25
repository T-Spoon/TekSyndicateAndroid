package com.tspoon.teksyndicate;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tspoon.teksyndicate.db.DBAdapter;
import com.tspoon.teksyndicate.db.VideoItem;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.VideoView;

public class ActivityVideoView extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
	
	private static final String TAG = "ActivityVideoView";
	
	public static final String BUNDLE_VIDEO_TOKEN = "Video_Token";
	
	private VideoItem mItem;
	private VideoView mVideo;
	private YouTubePlayerView mPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		
		Bundle bundle = getIntent().getExtras();
		String videoToken = bundle.getString(BUNDLE_VIDEO_TOKEN);
		mItem = DBAdapter.getVideo(videoToken);
		
		//mItem = new VideoItem(videoID, "Tek Syndicate Outtakes & Shenanigans | CES 2014", "Ow1a_NnPvEM", "14 Jan 2014", "");
		mPlayer = (YouTubePlayerView) findViewById(R.id.youtubePlayer);
        mPlayer.initialize(App.YOUTUBE_API_KEY, this);

		Log.d(TAG, mItem.title);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.activity_view, menu);
		return true;
	}

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        Log.d(TAG, "Playing Video: " + mItem.title);
        youTubePlayer.cueVideo(mItem.videoToken);
        youTubePlayer.play();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
