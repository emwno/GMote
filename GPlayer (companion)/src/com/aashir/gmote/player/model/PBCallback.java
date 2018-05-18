package com.aashir.gmote.player.model;

public interface PBCallback {
	
	  public void remoteVolume(int vol);
	  
	  public void remotePlayback();
	  
	  public void remoteSeek(int type, int val);
	  
	  public void remotePlaybackSpeed(float sp);
	  
	  public void remoteRotate();
	  
	  public void remoteSubtitle(boolean shown);
	  
}
