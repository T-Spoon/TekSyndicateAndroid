package com.tspoon.teksyndicate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tspoon.teksyndicate.R;

public class ActivityFeed extends ActionBarActivity {

	private static final String TAG = "ActivityFeed";
	
	private ViewPager mViewPager;
	private FeedPagerAdapter mPagerAdapter;
    private SlidingMenu mMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);
		
		final ActionBar actionBar = getSupportActionBar();

		mPagerAdapter = new FeedPagerAdapter(this, getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setTitle(mPagerAdapter.getPageTitle(position));
			}
		});

		actionBar.setTitle(mPagerAdapter.getPageTitle(0));
        actionBar.setDisplayHomeAsUpEnabled(true);

        mMenu = new SlidingMenu(this);
        mMenu.setMode(SlidingMenu.LEFT);
        mMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mMenu.setShadowWidthRes(R.dimen.shadow_width);
        mMenu.setShadowDrawable(R.drawable.menu_shadow);
        mMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mMenu.setFadeDegree(0.35f);
        mMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        mMenu.setMenu(R.layout.fragment_slider);
        mMenu.setSelectorDrawable(R.drawable.ic_drawer);
        mMenu.setSelectorEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_feed, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	        	Intent i = new Intent(ActivityFeed.this, ActivitySettings.class);
	        	startActivity(i);
	            return true;
            case android.R.id.home:
                mMenu.toggle();
                return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
