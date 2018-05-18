package com.aashir.gmote.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aashir.gmote.model.MediaCallback;

public class MediaReciever extends BroadcastReceiver {

private MediaCallback mCallback = null;
	
	public MediaReciever(MediaCallback c) {
    	this.mCallback = c;
    }
	
	@Override
	public void onReceive(Context context, Intent intent) {
        mCallback.onDataRecieve(intent.getStringArrayExtra("video_names_array"), intent.getStringArrayExtra("video_paths_array"));
	}

}
