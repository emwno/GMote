package com.aashir.gmote.reciever;

import android.database.ContentObserver;
import android.os.Handler;

import com.aashir.gmote.model.SyncCallback;

public class ConnectionObserver extends ContentObserver {
	
	private SyncCallback mCallback;
	
	public ConnectionObserver(Handler handler, SyncCallback cx) {
		super(handler);
		this.mCallback = cx;
	}
 
	@Override
	public void onChange(boolean selfChange) {
        mCallback.peerConnectionChange();
	}
   
}