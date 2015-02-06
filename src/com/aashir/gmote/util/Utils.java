package com.aashir.gmote.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aashir.gmote.GMote;
import com.aashir.gmote.R;
import com.aashir.gmote.model.Constants;
import com.lge.qpair.api.r2.QPairConstants;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Utils {

	private Context mContext;
	private Dialog mDialog;
	
	public Utils(Context c) {
		this.mContext = c;
	}
	
	public void savePreference(String key, int value) {
		SharedPreferences.Editor edit = GMote.getInstance().getPreferences().edit();
		edit.putInt(key, value);
		edit.apply();
	}
	
    public int getPreference(String key) {
    	SharedPreferences pref = GMote.getInstance().getPreferences();
    	return pref.getInt(key, 10000);
	}
    
    public int convertDpToPixel(int dp){
        Resources resources = mContext.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }
    
    public int adjustVolume(int current, int type) {
    	if(type == 0) {
    	    if(current < Constants.MAX_VOLUME) current += 1;
    	} else {
            if(current > 0) current -= 1;
    	}
    	return current;
    }
    
    public void errorDialog(int type, int visi) {
		int titleID;
		int subID;
		
		if(visi == View.GONE) { 
			if(mDialog != null) mDialog.dismiss();
			return;
		}
		
		if(type == 0) {
			titleID = R.string.not_connected;
		    subID = R.string.not_connected_sub;
		} else {
			titleID = R.string.qpair_not_installed;
			subID = R.string.qpair_not_installed_sub;
		}
		
		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        
        mDialog.getWindow().setAttributes(params);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog);
        
        if(type == 1) mDialog.findViewById(R.id.error_progress).setVisibility(View.GONE);
        TextView title = (TextView) mDialog.findViewById(R.id.dialog_title);
        TextView subtitle = (TextView) mDialog.findViewById(R.id.dialog_sub);
        title.setText(titleID);
        subtitle.setText(subID);
        mDialog.show();
	}
    
    public boolean isURL(String url){	
		final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
		Pattern p = Pattern.compile(URL_REGEX);
		Matcher m = p.matcher(url);
		if(m.find()) return true;
	    
	    return false;
    }

    public boolean isQPairInstalled() {
        try {
            mContext.getPackageManager().getPackageInfo("com.lge.p2p", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
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
        return "";
    }
	
	public long getLongProperty(String uriString) {
        Uri uri = Uri.parse(QPairConstants.PROPERTY_SCHEME_AUTHORITY + uriString);
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
               if (cursor.moveToFirst()) {
                   return cursor.getLong(0);
               }
            }finally {
                cursor.close();
            }
        }
        return 0;
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
