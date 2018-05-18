package com.aashir.gmote.player;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.aashir.gmote.player.model.Constants;
import com.aashir.gmote.player.model.IACallback;
import com.lge.qpair.api.r2.IPeerContext;
import com.lge.qpair.api.r2.IPeerIntent;

public class HostPairServiceConnection implements ServiceConnection {

	private IACallback mCallback = null;
	private String mAction;
	private String mExtraString;
	private int mExtraInt;
	private String[] mExtraStringArray1;
	private String[] mExtraStringArray2;
	
    public HostPairServiceConnection(IACallback c) {
    	this.mCallback = c;
    }

    public void setAction(String s) {
    	this.mAction = s;
    }
    
    public void setExtra(String s, int i) {
    	this.mExtraInt = i;
    	this.mExtraString = s;
    }
    
    public void setExtraStringArrays(String[] a, String[] b) {
    	this.mExtraStringArray1 = a;
    	this.mExtraStringArray2 = b;
    }
    
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
    	
        IPeerContext peerContext = IPeerContext.Stub.asInterface(service);

        try {
        	
            IPeerIntent peerIntent = peerContext.newPeerIntent();
            if(mAction == Constants.ACTION_VIDEO_LIST) {
            	peerIntent.setAction(Constants.BROADCAST_RECIEVER_MEDIA);
            	peerIntent.putStringArrayExtra("video_names_array", mExtraStringArray1);
            	peerIntent.putStringArrayExtra("video_paths_array", mExtraStringArray2);
            } else {
            	peerIntent.setAction(Constants.BROADCAST_RECIEVER_PEER_DATA);
            }
            
            peerIntent.putStringExtra(Constants.ACTION, mAction);
            
            if(mAction == Constants.ACTION_VIDEO_PLAYBACK) {
            	boolean play = (mExtraInt == -10) ? true : false;
            	peerIntent.putBooleanExtra(mAction, play);
            } else {
            	if(mExtraInt != -1) peerIntent.putIntExtra(mAction, mExtraInt);
            	if(mExtraString != null) peerIntent.putStringExtra(mAction, mExtraString);
            }

            IPeerIntent callback = peerContext.newPeerIntent();
            callback.setAction(Constants.ACTION_CALLBACK);

            peerContext.sendBroadcastOnPeer(peerIntent, callback, callback);
            mCallback.onJobComplete();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }
    
}
