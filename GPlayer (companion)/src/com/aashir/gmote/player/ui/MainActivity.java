package com.aashir.gmote.player.ui;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Toast;

import com.aashir.gmote.player.model.Constants;
import com.aashir.gmote.player.model.PBCallback;
import com.aashir.gmote.player.reciever.ActionBroadcastReciever;
import com.aashir.gmote.player.util.LibAsync;

public class MainActivity extends BaseActivity implements PBCallback {

	private ActionBroadcastReciever mActionReciever;
	private AudioManager mAudioManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new LibAsync(this).execute();
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mActionReciever = new ActionBroadcastReciever(this);
		
		Bundle i = getIntent().getExtras();
		VIDEO_PATH = i.getString("path");
		VIDEO_TITLE = i.getString("title");
		
		String scheck = mUtils.getStringProperty(Constants.PROPERTY_VIDEO_SUBTITLE);
		isSubtitleShown = (scheck.equals("true")) ? true : false;
		
		registerReceiver(mActionReciever, new IntentFilter(Constants.BROADCAST_RECIEVER_ACTION));
		
		refreshVideoData();
	}

	@Override
    protected void onPause() {
        unregisterReceiver(mActionReciever);
        super.onPause();
    }
	
	@Override
    protected void onResume() {
        registerReceiver(mActionReciever, new IntentFilter(Constants.BROADCAST_RECIEVER_ACTION));
        super.onResume();
    }
	
	@Override
	public void remoteVolume(int vol) {
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
	}

	@Override
	public void remotePlayback() {
		if (mVideoView != null) {
			if(!mVideoView.isPlaying()) mVideoView.start();
			else mVideoView.pause();
		}
		
	}
	
	@Override
	public void remoteSeek(int type, int val) {
		if (mVideoView != null) {
			if(type == 0) mVideoView.seekTo(val);
			else mVideoView.seekTo(mVideoView.getCurrentPosition() + val);
		}
		
	}

	@Override
	public void remotePlaybackSpeed(float sp) {
		if (mVideoView != null) mVideoView.setVideoPlaybackSpeed(sp);
	}

	@Override
	public void remoteRotate() {
		if(mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else if (mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
		
	}

	@Override
	public void remoteSubtitle(boolean shown) {
		mVideoView.setTimedTextShown(shown);
		if(!shown) mSubTitles.setText("");
	}
	
}
