package com.aashir.gmote.player.util;

import java.io.File;

import com.lge.qpair.api.r2.QPairConstants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class Utils {

	private Context mContext;
	
	public Utils(Context c) {
		this.mContext = c;
	}

	// Apache Commons
	public String removeExtension(String s) {
	    String filename;
	    
	    int lastSeparatorIndex = s.lastIndexOf(File.separator);
	    if (lastSeparatorIndex == -1) {
	        filename = s;
	    } else {
	        filename = s.substring(lastSeparatorIndex + 1);
	    }
	    
	    int extensionIndex = filename.lastIndexOf(".");
	    if (extensionIndex == -1)
	        return filename;

	    return filename.substring(0, extensionIndex);
	}
	
	public String timeFormat(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

		int hours = (int) (milliseconds / (1000 * 60 * 60));
	    int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
	    int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
	    
	    if (hours > 0) finalTimerString = hours + ":";
	    
	    if (seconds < 10) {
	        secondsString = "0" + seconds;
	    } else {
	        secondsString = "" + seconds;
	    }

	    finalTimerString = finalTimerString + minutes + ":" + secondsString;

	    return finalTimerString;
	}
	
	public String getStringProperty(String uriString) {
        Uri uri = Uri.parse(QPairConstants.PROPERTY_SCHEME_AUTHORITY + uriString);
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
               if (cursor.moveToFirst()) {
                   return cursor.getString(0);
               }
            }finally {
                cursor.close();
            }
        }
        return "false";
    }
	
	public void setStringProperty(String uriString, String value) {
		try {
            Uri uri = Uri.parse(QPairConstants.PROPERTY_SCHEME_AUTHORITY + uriString);
            ContentValues cv = new ContentValues();
            cv.put("", value);
            mContext.getContentResolver().insert(uri, cv);
		} catch(Exception e) {
			e.printStackTrace();
			updateStringProperty(uriString, value);
		}
    }
	
	public void setLongProperty(String uriString, long value) {
		try {
            Uri uri = Uri.parse(QPairConstants.PROPERTY_SCHEME_AUTHORITY + uriString);
            ContentValues cv = new ContentValues();
            cv.put("", value);
            mContext.getContentResolver().insert(uri, cv);
		} catch(Exception e) {
			e.printStackTrace();
			updateLongProperty(uriString, value);
		}
    }
	
    public void updateLongProperty(String uriString, long value) {
        Uri uri = Uri.parse(QPairConstants.PROPERTY_SCHEME_AUTHORITY + uriString);
        ContentValues cv = new ContentValues();
        cv.put("", value);
        mContext.getContentResolver().update(uri, cv, null, null);
    }
    
    public void updateStringProperty(String uriString, String value) {
        Uri uri = Uri.parse(QPairConstants.PROPERTY_SCHEME_AUTHORITY + uriString);
        ContentValues cv = new ContentValues();
        cv.put("", value);
        mContext.getContentResolver().update(uri, cv, null, null);
    }
    
    public void deleteQPairProperty(String uriString) {
        Uri uri = Uri.parse(QPairConstants.PROPERTY_SCHEME_AUTHORITY + uriString);
        mContext.getContentResolver().delete(uri, null, null);
    }

}
