package com.aashir.gmote;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.aashir.gmote.model.Constants;
import com.aashir.gmote.model.SyncCallback;
import com.lge.qpair.api.r2.IPeerContext;
import com.lge.qpair.api.r2.IPeerIntent;

public class PairServiceConnection implements ServiceConnection {

	private SyncCallback mCallback = null;
	private String mAction;
	private int mExtraInt;
	private float mExtraFloat;
	private String mExtraString;
	
    public PairServiceConnection(SyncCallback c) {
    	this.mCallback = c;
    }

    public void setAction(String s) {
    	this.mAction = s;
    }
    
    public void setExtra(String s, int i, float f) {
    	this.mExtraInt = i;
    	this.mExtraFloat = f;
    	this.mExtraString = s;
    }
    
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
    	
        IPeerContext peerContext = IPeerContext.Stub.asInterface(service);

        try {
        	IPeerIntent callback = peerContext.newPeerIntent();
            callback.setAction(Constants.ACTION_CALLBACK);
            
        	IPeerIntent peerIntent = peerContext.newPeerIntent();
    		
        	if(mAction == Constants.ACTION_VIDEO_LIST || mAction == Constants.ACTION_VIDEO_PATH) {
        		peerIntent.setClassName("com.aashir.gmote.player", "com.aashir.gmote.player.service.RequestService");
        		if(mExtraString != null) peerIntent.putStringExtra(mAction, mExtraString);
        		peerContext.startServiceOnPeer(peerIntent, callback, callback);
                
        	} else {
                peerIntent.setAction(Constants.BROADCAST_RECIEVER_ACTION);
                peerIntent.putStringExtra(Constants.ACTION, mAction);
                if(mExtraString != null) peerIntent.putStringExtra(mAction, mExtraString);
                if(mExtraInt != -1) peerIntent.putIntExtra(mAction, mExtraInt);
                if(mExtraFloat != -1) peerIntent.putFloatExtra(Constants.ACTION_VIDEO_PLAYBACK_SPEED, mExtraFloat);
                peerContext.sendBroadcastOnPeer(peerIntent, callback, callback);
        	}
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        mCallback.onJobComplete();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }
    
}
