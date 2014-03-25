package com.tspoon.teksyndicate;

import java.util.List;

import com.squareup.picasso.Picasso;
import com.tspoon.teksyndicate.db.VideoItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
		
		private Integer mFirstItemTag = 1;
		
		private List<VideoItem> mItems;
		private Context mContext;
		private String mTitle;
        // We can use this to support Alpha on API 8+
        private AlphaAnimation mFader;
		private int mCount;
		private static Picasso mPicasso;
		
		public ListAdapter(Context context, List<VideoItem> items, String title) {
			mContext = context;
			mItems = items;
			mCount = items.size();
			mTitle = title;

			if(mPicasso == null)
				mPicasso = new Picasso.Builder(mContext).build();
			//mPicasso.setDebugging(true);

            mFader = new AlphaAnimation(0.6f, 0.6f);
            mFader.setDuration(0);
            mFader.setFillAfter(true);

			System.out.println(items.size());
		}
		
		public String getTitle() {
			return mTitle;
		}
		
		public void updateItems(List<VideoItem> items) {
			mItems.clear();
			mItems.addAll(items);
			mCount = mItems.size();
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return mCount;
		}
		
		@Override
		public VideoItem getItem(int position) {
			return mItems.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = null;
		    TextView titleView = null;
			
			if(position == 0) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item_large, parent, false);
				convertView.setTag(mFirstItemTag);
				titleView = (TextView) convertView.findViewById(R.id.itemTitle);
				imageView = (ImageView) convertView.findViewById(R.id.itemImage);
			}
			else if (convertView == null || convertView.getTag() == mFirstItemTag) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item, parent, false);
				imageView = (ImageView) convertView.findViewById(R.id.itemImage);
				titleView = (TextView) convertView.findViewById(R.id.itemTitle);
		        convertView.setTag(new ViewHolder(imageView, titleView));
			} else {
				ViewHolder viewHolder = (ViewHolder) convertView.getTag();
				imageView = viewHolder.imageView;
		        titleView = viewHolder.titleView;
			}
			
			VideoItem item = getItem(position);
			
			titleView.setText(item.title.toUpperCase());
			mPicasso.load(item.imageURL).error(R.drawable.ic_logo_grey).into(imageView);
			if(item.itemViewed > 0)
                convertView.startAnimation(mFader);

			if(position == mCount - 1)
				FeedPagerAdapter.loadNewItems();
				
			return convertView;
		}
		
		private static class ViewHolder {
		    public final ImageView imageView;
		    public final TextView titleView;

		    public ViewHolder(ImageView imageView, TextView titleView) {
		        this.imageView = imageView;
		        this.titleView = titleView;
		    }
		}
	}