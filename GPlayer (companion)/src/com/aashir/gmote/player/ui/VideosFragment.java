package com.aashir.gmote.player.ui;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.aashir.gmote.player.R;
import com.aashir.gmote.player.database.SQLiteHelper;
import com.aashir.gmote.player.model.VideoItem;
import com.aashir.gmote.player.ui.adapter.VideoAdapter;
import com.aashir.gmote.player.util.Utils;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;

public class VideosFragment extends Fragment implements OnItemClickListener, MultiChoiceModeListener {

	@InjectView(R.id.video_list) GridView mGridView;
	
	private ArrayList<VideoItem> mVideoArray;
	private Utils mUtils;
	private VideoAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUtils = new Utils(getActivity());
		mVideoArray = new ArrayList<VideoItem>();
		getVideos();
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.video_list, container, false);
		ButterKnife.inject(this, view);
		if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
	        mGridView.setNumColumns(3);
	    }
		
		mAdapter = new VideoAdapter(getActivity(), mVideoArray);
	    mGridView.setOnItemClickListener(this);
	    mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
		mGridView.setMultiChoiceModeListener(this);
		mGridView.setAdapter(mAdapter);
		return view;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
	        mGridView.setNumColumns(3);
	    } else {
	    	mGridView.setNumColumns(2);
	    }
	}
	
	private void getVideos() {
		ContentResolver contentResolver = getActivity().getContentResolver();
        final Uri mVideoUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] mProj = {android.provider.BaseColumns._ID, MediaStore.Video.VideoColumns.DISPLAY_NAME,
                               MediaStore.Video.VideoColumns.DURATION, MediaStore.Video.VideoColumns.DATA,
                               MediaStore.Video.VideoColumns.RESOLUTION, MediaStore.Video.Thumbnails.DATA};

        Cursor mCursor = contentResolver.query(mVideoUri, mProj, null, null, null);
        if (mCursor.moveToFirst()) {
        	if(mVideoArray != null) mVideoArray.clear();
        	
            do {
            	VideoItem  mItem = new VideoItem();
            	String title = mUtils.removeExtension(mCursor.getString(1));
                mItem.setTitle(title);
                String duration = mUtils.timeFormat(mCursor.getInt(2));
                mItem.setDuration(duration);
                mItem.setPath(mCursor.getString(3));
                mItem.setResolution(mCursor.getString(4));
            
                mVideoArray.add(mItem);
            } while(mCursor.moveToNext());
        }
        
        mCursor.close();
	}
	
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem arg1) {
		final Dialog dialog = new Dialog(getActivity(), "Are you sure you want to delete?", 
				"Deleting the video(s) means that they are also removed from your Tablet, they can not be recovered.");
		dialog.show();
		
		ButtonFlat accept = dialog.getButtonAccept();
		accept.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				SQLiteHelper db = new SQLiteHelper(getActivity());
				SparseBooleanArray selected = mAdapter.getSelections();
			    for (int i = selected.size()-1 ; i >= 0; i--) {
				    if (selected.valueAt(i)) {
					    VideoItem video = mAdapter.getItem(selected.keyAt(i));
					    db.deleteVideoItem(video);
					    File f = new File(video.getPath());
					    f.delete();
					    getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
				    	mAdapter.remove(video);
				    }
			    }
			    
			    mAdapter.select(-1, false);
				mAdapter.notifyDataSetChanged();
			    db.close();
			    dialog.dismiss();
			}
			
		});
		
		mode.finish();
		return true;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		mode.getMenuInflater().inflate(R.menu.menu_long, menu);
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode arg0) {
		mAdapter.select(-1, false);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int pos, long arg2, boolean isChecked) {
		final int checkedCount = mGridView.getCheckedItemCount();
		mode.setTitle(checkedCount + " Selected");
		mAdapter.select(pos, isChecked);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		if(mVideoArray != null) {
			SQLiteHelper db = new SQLiteHelper(getActivity());
            VideoItem item =  mVideoArray.get(position);
            db.addVideoItem(item);
            db.close();
            
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("path", item.getPath());
            intent.putExtra("title", item.getTitle());
            startActivity(intent);
    	}
	}
	
}
