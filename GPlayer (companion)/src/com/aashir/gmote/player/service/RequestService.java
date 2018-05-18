package com.aashir.gmote.player.service;

import java.util.ArrayList;
import java.util.List;

import com.aashir.gmote.player.HostPairServiceConnection;
import com.aashir.gmote.player.model.Constants;
import com.aashir.gmote.player.model.IACallback;
import com.aashir.gmote.player.ui.MainActivity;
import com.aashir.gmote.player.util.Utils;
import com.lge.qpair.api.r2.QPairConstants;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

public class RequestService extends Service implements IACallback {

	private ArrayList<String> mNameArray;
	private ArrayList<String> mPathArray;
	private HostPairServiceConnection mConnection;
	private Utils mUtils;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		mUtils = new Utils(this);
		String path = intent.getStringExtra(Constants.ACTION_VIDEO_PATH);
		
		if(path != null) {
			List<String> paths = Uri.parse(path).getPathSegments();
	        String name = paths == null || paths.isEmpty() ? "null" : paths.get(paths.size() - 1);
	        String title = mUtils.removeExtension(name);
	        
	    	Intent i = new Intent(this, MainActivity.class);
	    	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	i.putExtra("path", path);
	    	i.putExtra("title", title);
	        startActivity(i);
		} else {
		    mNameArray = new ArrayList<String>();
		    mPathArray = new ArrayList<String>();
		    getVideos();
		
		    String[] nam = new String[mNameArray.size()];
		    nam = mNameArray.toArray(nam);
		
		    String[] pat = new String[mPathArray.size()];
		    pat = mPathArray.toArray(pat);
		
		    mConnection = new HostPairServiceConnection(this);
		    mConnection.setAction(Constants.ACTION_VIDEO_LIST);
		    mConnection.setExtraStringArrays(nam, pat);
		
		    Intent i = new Intent(QPairConstants.ACTION_SERVICE);
    	    i.setPackage(QPairConstants.PACKAGE_NAME);
    	    bindService(i, mConnection, 0);
		}
		
        return START_NOT_STICKY;
	}
	
	public void getVideos() {
		ContentResolver contentResolver = getContentResolver();
        final Uri mVideoUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] mProj = {android.provider.BaseColumns._ID, MediaStore.Video.VideoColumns.DISPLAY_NAME,
                               MediaStore.Video.VideoColumns.DURATION, MediaStore.Video.VideoColumns.DATA,
                               MediaStore.Video.VideoColumns.RESOLUTION, MediaStore.Video.Thumbnails.DATA};

        Cursor mCursor = contentResolver.query(mVideoUri, mProj, null, null, null);
        
        if (mCursor.moveToFirst()) {
        	
            do {
            	String title = mUtils.removeExtension(mCursor.getString(1));
            	mNameArray.add(title);
            	mPathArray.add(mCursor.getString(3));
            } while(mCursor.moveToNext());
        }
        mCursor.close();
	}

	@Override
	public void onJobComplete() {
		unbindService(mConnection);
	}
	
}
