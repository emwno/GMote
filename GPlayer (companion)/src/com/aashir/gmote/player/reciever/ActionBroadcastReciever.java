package com.aashir.gmote.player.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

import com.aashir.gmote.player.model.Constants;
import com.aashir.gmote.player.model.PBCallback;

public class ActionBroadcastReciever extends BroadcastReceiver {

	private PBCallback mCallback = null;
	private Toast mToast;
	
	public ActionBroadcastReciever(PBCallback c) {
    	this.mCallback = c;
        mToast = Toast.makeText((Context)c  , "" , Toast.LENGTH_SHORT );
    }
	
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra(Constants.ACTION);
        switch(action) {
            
            case Constants.ACTION_VIDEO_PLAYBACK:
            	mCallback.remotePlayback();
            break;
            
            case Constants.ACTION_ADJUST_VOLUME:
            	int k = intent.getIntExtra(Constants.ACTION_ADJUST_VOLUME, 7);
            	mToast.setText("Volume: " + k);
            	mToast.setGravity(Gravity.TOP|Gravity.RIGHT , 10, 10);
            	mToast.show();
            	
            	mCallback.remoteVolume(k);
            break;
            
            case Constants.ACTION_SEEK:
            	int pos = intent.getIntExtra(Constants.ACTION_SEEK, -1);
            	mCallback.remoteSeek(0, pos);
            break;
            
            case Constants.ACTION_SEEK_SKIP:
            	int val = intent.getIntExtra(Constants.ACTION_SEEK_SKIP, -1);
            	mCallback.remoteSeek(1, val);
            break;
            
            case Constants.ACTION_ROTATE:
            	mCallback.remoteRotate();
            break;
            
            case Constants.ACTION_VIDEO_PLAYBACK_SPEED:
            	float s = intent.getFloatExtra(Constants.ACTION_VIDEO_PLAYBACK_SPEED, 1.0f);
            	mToast.setText("Playback Speed: " + s + "X");
            	mToast.setGravity(Gravity.TOP|Gravity.RIGHT , 10, 10);
            	mToast.show();
            	mCallback.remotePlaybackSpeed(s);
            break;
            
            case Constants.ACTION_VIDEO_SUBTITLE:
            	String sub = intent.getStringExtra(Constants.ACTION_VIDEO_SUBTITLE);
            	boolean shown = sub.equals("true") ? true : false;
            	String o = shown == true ? "ON" : "OFF";
            	mToast.setText("Subtitles: " + o);
            	mToast.setGravity(Gravity.TOP|Gravity.RIGHT , 10, 10);
            	mToast.show();
            	mCallback.remoteSubtitle(shown);
            break;
        }
        
    }

}