package com.aashir.gmote;

import com.aashir.gmote.model.Constants;
import com.path.android.jobqueue.JobManager;

import android.app.Application;
import android.content.SharedPreferences;

public class GMote extends Application {

	private static GMote mInstance;
    private SharedPreferences mPref;
    private JobManager mJobManager;
    
    public GMote() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPref = getSharedPreferences(Constants.SHARED_PREF, 0);
        //mJobManager = new JobManager(this);
    }
    
	public JobManager getJobManager() {
        return mJobManager;
    }
    
	public SharedPreferences getPreferences() {
        return mPref;
    }

    public static GMote getInstance() {
        return mInstance;
    }
    
}
