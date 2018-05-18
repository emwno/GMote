package com.aashir.gmote.reciever;

import com.aashir.gmote.model.Constants;
import com.aashir.gmote.model.RemoteCallback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class VideoDataReciever extends BroadcastReceiver {

private RemoteCallback mCallback = null;
	
	public VideoDataReciever(RemoteCallback c) {
    	this.mCallback = c;
    }
	
	@Override
	public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra(Constants.ACTION);
        
        switch(action) {
            
            case Constants.ACTION_VIDEO_DURATION:
            	int k = intent.getIntExtra(Constants.ACTION_VIDEO_DURATION, 0);
            	mCallback.setVideoDuration(k);
            break;
            
            case Constants.ACTION_VIDEO_POSITION:
            	int d = intent.getIntExtra(Constants.ACTION_VIDEO_POSITION, 0);
            	mCallback.setVideoPosition(d);
            break;
            
            case Constants.ACTION_VIDEO_STOP:
            	mCallback.peerStopped();
            break;
            
            case Constants.ACTION_VIDEO_TITLE:
            	String s = intent.getStringExtra(Constants.ACTION_VIDEO_TITLE);
    	        mCallback.setVideoTitle(s);
            break;
        
            case Constants.ACTION_VIDEO_PLAYBACK:
                boolean isPlaying = intent.getBooleanExtra(Constants.ACTION_VIDEO_PLAYBACK, false);
        	    mCallback.peerPlayback(isPlaying);
            break;
        }
	}

}
