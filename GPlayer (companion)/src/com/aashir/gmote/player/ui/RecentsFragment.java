package com.aashir.gmote.player.ui;

import java.util.ArrayList;

import com.aashir.gmote.player.R;
import com.aashir.gmote.player.database.SQLiteHelper;
import com.aashir.gmote.player.model.VideoItem;
import com.aashir.gmote.player.ui.adapter.VideoAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class RecentsFragment extends Fragment implements OnItemClickListener {

    @InjectView(R.id.video_list) GridView mGridView;
	
	private ArrayList<VideoItem> mVideoArray;
	private VideoAdapter mAdapter;
	private SQLiteHelper mDatabase;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDatabase = new SQLiteHelper(getActivity());
		mVideoArray = new ArrayList<VideoItem>();
	}
	
	private void getStoredVideos() {
		mVideoArray = mDatabase.getAllVideoItems();
		mAdapter = new VideoAdapter(getActivity(), mVideoArray);
		mGridView.setAdapter(mAdapter);
		mDatabase.close();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.video_list, container, false);
		ButterKnife.inject(this, view);
		if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
	        mGridView.setNumColumns(3);
	    }
	    mGridView.setOnItemClickListener(this);
	    getStoredVideos();
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getStoredVideos();
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
