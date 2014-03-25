package com.tspoon.teksyndicate;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Tspoon on 24/03/2014.
 */
public class FragmentMenu extends ListFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_sliding_menu, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SlidingMenuAdapter adapter = new SlidingMenuAdapter(getActivity());
        Resources res = getActivity().getApplicationContext().getResources();

        adapter.add(new SlidingMenuItem(res.getString(R.string.videos), R.drawable.ic_logo_grey));
        adapter.add(new SlidingMenuItem(res.getString(R.string.articles), R.drawable.ic_logo_grey));
        adapter.add(new SlidingMenuItem(res.getString(R.string.forums), R.drawable.ic_logo_grey));

        setListAdapter(adapter);
    }

    private static class SlidingMenuItem {
        public String tag;
        public int iconRes;
        public SlidingMenuItem(String tag, int iconRes) {
            this.tag = tag;
            this.iconRes = iconRes;
        }
    }

    public static class SlidingMenuAdapter extends ArrayAdapter<SlidingMenuItem> {

        public SlidingMenuAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_sliding_menu_list_row, null);
            }
            ImageView icon = (ImageView) convertView.findViewById(R.id.rowIcon);
            icon.setImageResource(getItem(position).iconRes);
            TextView title = (TextView) convertView.findViewById(R.id.rowTitle);
            title.setText(getItem(position).tag);

            return convertView;
        }

    }
}
