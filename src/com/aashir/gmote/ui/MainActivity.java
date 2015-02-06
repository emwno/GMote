package com.aashir.gmote.ui;

import android.content.IntentFilter;
import android.os.Bundle;

import com.aashir.gmote.model.Constants;
import com.aashir.gmote.model.RemoteCallback;
import com.aashir.gmote.reciever.VideoDataReciever;

public class MainActivity extends BaseActivity implements RemoteCallback {

	private VideoDataReciever mVideoDataReciever;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mVideoDataReciever = new VideoDataReciever(this);
		registerReceiver(mVideoDataReciever, new IntentFilter(Constants.BROADCAST_RECIEVER_PEER_DATA));
	}
		
	@Override
    public void onPause() {
		unregisterReceiver(mVideoDataReciever);
        super.onPause();
    }
    
    @Override
    public void onResume() {
    	registerReceiver(mVideoDataReciever, new IntentFilter(Constants.BROADCAST_RECIEVER_PEER_DATA));
        super.onResume();
    }
    
	@Override
	public void setVideoDuration(int dur) {
		mVideoDuration = dur;
		mSeek.setMax(mVideoDuration);
		mVideoDurationTV.setText(mUtils.timeFormat(mVideoDuration));
		mVideoPositionTV.setText(mUtils.timeFormat(0));
	}

	@Override
	public void setVideoPosition(int pos) {
		mSeek.setProgress(pos);
		mVideoPositionTV.setText(mUtils.timeFormat(pos));
		this.peerPlayback(true);
	}

	@Override
	public void setVideoTitle(String title) {
		mVideoTitleTV.setText(title);
	}
	
	@Override
	public void peerStopped() {
		mSeek.setProgress(0);
		mSeek.setMax(0);
		playBackUI(1);
		mVideoDurationTV.setText("00:00");
		mVideoPositionTV.setText("00:00");
		mVideoTitleTV.setText("Nothing");
	}

	@Override
	public void peerPlayback(boolean isPlaying) {
		if(isPlaying) {
			playBackUI(0);
		} else {
			playBackUI(1);
		}
		
	}
	
	
}
