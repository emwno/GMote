package com.aashir.gmote.player;

import android.app.Application;

import com.path.android.jobqueue.JobManager;

public class GMotePlayer extends Application {

	private static GMotePlayer mInstance;
    private JobManager mJobManager;

    public GMotePlayer() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mJobManager = new JobManager(this);
    }
    
	public JobManager getJobManager() {
        return mJobManager;
    }

    public static GMotePlayer getInstance() {
        return mInstance;
    }
    
}
