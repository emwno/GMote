package com.aashir.gmote.player.service;

import android.content.Context;
import android.content.Intent;

import com.aashir.gmote.player.HostPairServiceConnection;
import com.aashir.gmote.player.model.IACallback;
import com.lge.qpair.api.r2.QPairConstants;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

public class SyncJob extends Job implements IACallback {

	private static final long serialVersionUID = 1L;
	
	private Context mContext;
	private HostPairServiceConnection mConnection;
	private String mAction;
	private String mTitle;
	private int mValue;

	public SyncJob(Context c, String a, String e, int v) {
		super(new Params(1));
		mConnection = new HostPairServiceConnection(this);
		mContext = c;
		mAction = a;
		mTitle = e;
		mValue = v;
	}

	@Override
	public void onAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRun() throws Throwable {
		mConnection.setAction(mAction);
		mConnection.setExtra(mTitle, mValue);
    	Intent intent = new Intent(QPairConstants.ACTION_SERVICE);
    	intent.setPackage(QPairConstants.PACKAGE_NAME);
    	mContext.bindService(intent, mConnection, 0);
	}

	@Override
	protected boolean shouldReRunOnThrowable(Throwable arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onJobComplete() {
		mContext.unbindService(mConnection);
	}

}
