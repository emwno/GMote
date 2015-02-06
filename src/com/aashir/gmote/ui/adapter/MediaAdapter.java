package com.aashir.gmote.ui.adapter;

import com.aashir.gmote.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MediaAdapter extends BaseAdapter {

	private String[] mTitles;
	private LayoutInflater mInflater;
	
	public MediaAdapter(Context c, String[] t) {
		this.mInflater = LayoutInflater.from(c);
		this.mTitles = t;
	}
	
	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public Object getItem(int pos) {
		return mTitles[pos];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	static class ViewHolder {
		@InjectView(R.id.media_title) TextView title;
		
		public ViewHolder(View view) {
		      ButterKnife.inject(this, view);
		    }
		
	}
	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		ViewHolder mHolder = null;
		
		if(view == null) {
			view = mInflater.inflate(R.layout.media_list_item, parent, false);
			mHolder = new ViewHolder(view);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		
		mHolder.title.setText(mTitles[pos]);
		
		return view;
	}

}
