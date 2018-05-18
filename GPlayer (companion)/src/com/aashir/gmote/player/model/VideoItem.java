package com.aashir.gmote.player.model;

public class VideoItem {
	
	private int mID;
	private String mTitle;
	private String mDuration;
	private String mPath;
	private String mResolution;
	private String mThumbnail;
	
	public int getID() {
		return mID;
	}
	
	public void setID(int i) {
	    this.mID = i;
	}
	public String getTitle() {
		return mTitle;
	}
	
	public void setTitle(String t) {
		this.mTitle = t;
	}
	
	public String getDuration() {
		return mDuration;
	}
	
	public void setDuration(String d) {
		this.mDuration = d;
	}
	
	public String getPath() {
		return mPath;
	}
	
	public void setPath(String p) {
		this.mPath = p;
	}
	
	public String getResolution() {
		return mResolution;
	}
	
	public void setResolution(String r) {
		this.mResolution = r;
	}
	
	public String getThumbnail() {
		return mThumbnail;
	}

	public void setThumbnail(String b) {
		this.mThumbnail = b;
	}
}
