package com.tspoon.teksyndicate;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.tspoon.teksyndicate.apihelper.ApiFetcher;
import com.tspoon.teksyndicate.db.Category;
import com.tspoon.teksyndicate.db.DBAdapter;
import com.tspoon.teksyndicate.db.VideoItem;
import com.tspoon.teksyndicate.settings.SettingsHelper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FeedPagerAdapter extends FragmentStatePagerAdapter {
	
	private static final String VOLLEY_TAG = "Volley";
	private static FeedPagerAdapter INSTANCE;
	private static boolean IS_LOADING;
	
	private ArrayList<ListAdapter> mAdapters;
	private Context mContext;
	private SettingsHelper mSettings;
	//private ListFragment mCurrentFragment;
	
	private int mCount;
	
	public FeedPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;	
		
		List<Category> categories = DBAdapter.getCategories();
		
		mAdapters = new ArrayList<ListAdapter>(3);
		for(int i = 0; i < categories.size(); i++) {
			mAdapters.add(new ListAdapter(mContext, DBAdapter.getVideoFeed(), categories.get(i).catName));
		}
		mCount = mAdapters.size();
		mSettings = SettingsHelper.getInstance(context);
		INSTANCE = this;
		
		ApiFetcher.getInstance(context).getPlaylistForCategoryAsync(ApiFetcher.PLAYLIST_UPLOADS, 
				FeedPagerAdapter.getInstace().volleySuccessListener, 
				FeedPagerAdapter.getInstace().volleyErrorListener
		);
	}

	@Override
	public Fragment getItem(int i) {
		ListFragment fragment = new FeedFragment();
		Bundle args = new Bundle();
		// Our object is just an integer :-P
		args.putBoolean(FeedFragment.ARG_ITEMS_LOADED, !mAdapters.get(i).isEmpty());
		fragment.setArguments(args);
		fragment.setListAdapter(mAdapters.get(i));
		//mCurrentFragment = fragment;
		return fragment;
	}
	
	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mAdapters.get(position).getTitle();
	}
	
	public void updateView() {
		for(int i = 0; i < mAdapters.size(); i++) {
			mAdapters.get(i).updateItems(DBAdapter.getVideoFeed());
		}
	}
	
	public static FeedPagerAdapter getInstace() {
		return INSTANCE;
	}
	
	public static class FeedFragment extends ListFragment {
	    
		public static final String ARG_ITEMS_LOADED = "items_loaded";
		
		private PullToRefreshLayout mPullToRefreshLayout;
		private ViewRefreshListener mRefreshListener = new ViewRefreshListener();
		
	    
	    @Override
	    public void onViewCreated(View view, Bundle savedInstanceState) {
	    	// This is the View which is created by ListFragment
	        ViewGroup viewGroup = (ViewGroup) view;

	        // We need to create a PullToRefreshLayout manually
	        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());
	        
	        Options options = new Options.Builder()
				    .scrollDistance(0.5f)
				    .build();
	        
	        // We can now setup the PullToRefreshLayout
	        ActionBarPullToRefresh.from(getActivity())
	                // We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
	                .insertLayoutInto(viewGroup)
	                // We need to mark the ListView and it's Empty View as pullable
	                // This is because they are not dirent children of the ViewGroup
	                .theseChildrenArePullable(getListView(), getListView().getEmptyView())
	                // We can now complete the setup as desired
	                .listener(mRefreshListener)
	                .options(options)
	                .setup(mPullToRefreshLayout);
	    }
	    
	    @Override
	    public void onListItemClick(ListView l, View v, int position, long id) {
	        super.onListItemClick(l, v, position, id);
	        VideoItem item = (VideoItem) l.getAdapter().getItem(position);
	        DBAdapter.setItemViewed(item, 1);

	        Intent i = new Intent(v.getContext(), ActivityVideoView.class);
	        i.putExtra(ActivityVideoView.BUNDLE_VIDEO_ID, item.id);
	        startActivity(i);
	    }
	    
	    private class ViewRefreshListener implements OnRefreshListener {
			private static final String TAG = "ViewRefreshListener";
			
			@Override
			public void onRefreshStarted(final View view) {
				new AsyncTask<Void, Void, JSONObject>() {
					
					@Override
		            protected void onPreExecute() {
		                super.onPreExecute();
		            }
					
		            @Override
		            protected JSONObject doInBackground(Void... params) {
		            	return ApiFetcher.getInstance(view.getContext()).getPlaylistForCategory(ApiFetcher.PLAYLIST_UPLOADS, volleySynchronousRequestListener);
		            }

		            @Override
		            protected void onPostExecute(JSONObject result) {
		                super.onPostExecute(result);
		                
		                if(result != null) 
		                	FeedPagerAdapter.getInstace().volleySuccessListener.onResponse(result);
		                else
		                	FeedPagerAdapter.getInstace().volleyErrorListener.onErrorResponse(new VolleyError());
		                // Notify PullToRefreshLayout that the refresh has finished
		                mPullToRefreshLayout.setRefreshComplete();
		            }
		        }.execute();
			}
		}
	}
	
	private static RequestFuture<JSONObject> volleySynchronousRequestListener = RequestFuture.newFuture();
	
	private Response.Listener<JSONObject> volleySuccessListener = new Response.Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject json) {
			Log.d(VOLLEY_TAG, json.toString());
			try {
				DBAdapter.popuplateVideosFromJSON(json.getJSONArray("items"));
				mSettings.setNextPageToken(json.getString("nextPageToken"));
				updateView();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};
	
	private Response.ErrorListener volleyErrorListener = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError e) {
			e.printStackTrace();
		}
	};
	
	private static OnRefreshListener MoreItemLoader = new OnRefreshListener() {
		@Override
		public void onRefreshStarted(View view) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public static void loadNewItems() {
		if(!IS_LOADING) {
			IS_LOADING = true;
			new ItemLoader().execute();
		}
	}
	
	private static class ItemLoader extends AsyncTask<Void, Void, JSONObject> {
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
		
        @Override
        protected JSONObject doInBackground(Void... params) {
        	return ApiFetcher.getInstance(getInstace().mContext).getPlaylistForCategory(ApiFetcher.PLAYLIST_UPLOADS, volleySynchronousRequestListener);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            
            if(result != null) 
            	FeedPagerAdapter.getInstace().volleySuccessListener.onResponse(result);
            else
            	FeedPagerAdapter.getInstace().volleyErrorListener.onErrorResponse(new VolleyError());
            IS_LOADING = false;
        }
	}
}