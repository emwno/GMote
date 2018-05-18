package com.aashir.gmote.player.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aashir.gmote.player.R;
import com.aashir.gmote.player.model.VideoItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class VideoAdapter extends BaseAdapter {	
	
	private Context mContext;
	private LayoutInflater mInflater;
	private List<VideoItem> feedItems = null;
	private SparseBooleanArray mSelections;
	
	public VideoAdapter(Context activity, List<VideoItem> feedItems) {
		this.mContext = activity;
		this.feedItems = feedItems;
		this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mSelections = new SparseBooleanArray();
	}
	
	@Override
	public View getView(int pos, View view, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if(view == null) {
			view = mInflater.inflate(R.layout.k, viewGroup, false);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
        VideoItem item =  feedItems.get(pos);
        
        if(mSelections.get(pos)) holder.thumb.setColorFilter(Color.parseColor("#1E88E5"), PorterDuff.Mode.SRC_IN);
        else holder.thumb.setColorFilter(null);
		holder.title.setText(item.getTitle());
	    holder.duration.setText(item.getDuration());
        holder.res.setText(item.getResolution());
	    
		Glide.with(mContext)
         .load(item.getPath())
         .transform(new FitTransform(mContext, holder))
         .into(holder.thumb);
		
		return view;
	}
    

    public static class ViewHolder {
    	TextView title;
		TextView duration;
		TextView res;
		ImageView thumb;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.video_title);
		    duration = (TextView) view.findViewById(R.id.video_duration);
		    res = (TextView) view.findViewById(R.id.video_resolution);
			thumb = (ImageView) view.findViewById(R.id.video_thumb);
        }

    }

	@Override
	public int getCount() {
		return feedItems.size();
	}

	@Override
	public VideoItem getItem(int pos) {
		return feedItems.get(pos);
	}

	public void remove(VideoItem object) {
		feedItems.remove(object);
		notifyDataSetChanged();
	}
	
	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	private class FitTransform extends BitmapTransformation {

    	private ViewHolder b;
    	
        public FitTransform(Context c, ViewHolder w) {
        	super(c);
			this.b = w;
		}

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap source, 
                int outWidth, int outHeight) {
				int targetWidth = (int) (b.thumb.getWidth() * 0.8);

	            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
	            int targetHeight = (int) ((targetWidth * aspectRatio));
	            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
	            return result;
        }

        @Override
        public String getId() {
            return "com.aashir.gmote.player.Transformation";
        }
    }

	public void select(int pos, boolean isChecked) {
		if(pos == -1) {
			mSelections.clear(); 
			return;
		} else if (isChecked) {
			mSelections.put(pos, isChecked);
		} else { 
			mSelections.delete(pos);
		}
	}
	
	public int getSelectedCount() {
		return mSelections.size();
	}
 
	public SparseBooleanArray getSelections() {
		return mSelections;
	}
	
}
