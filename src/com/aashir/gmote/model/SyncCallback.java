package com.aashir.gmote.model;

public interface SyncCallback {
	
	public void syncPeer(String action, String path, int val, float dec);
	
	public void onJobComplete();
	
	public void peerConnectionChange();
	
}
