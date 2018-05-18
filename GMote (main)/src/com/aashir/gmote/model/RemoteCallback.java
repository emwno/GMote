package com.aashir.gmote.model;

public interface RemoteCallback {
	  
	  public void peerPlayback(boolean isPlaying);
	  
	  public void setVideoDuration(int dur);
	  
	  public void setVideoPosition(int pos);
	  
	  public void setVideoTitle(String title);
	  
	  public void peerStopped();
	  
}
