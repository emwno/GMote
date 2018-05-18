package com.aashir.gmote.player.util;

import io.vov.vitamio.Vitamio;
import android.content.Context;
import android.os.AsyncTask;

public class LibAsync extends AsyncTask<Object, Object, Boolean> {

	private Context mContext;
	
	public LibAsync(Context c) {
		this.mContext = c;
	}
	
	@Override
	protected Boolean doInBackground(Object... arg0) {
		return Vitamio.initialize(mContext, mContext.getResources().getIdentifier("libarm", "raw", mContext.getPackageName()));
	}

}