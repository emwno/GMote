package com.aashir.gmote.player.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.aashir.gmote.player.model.Constants;
import com.aashir.gmote.player.ui.MainActivity;

public class PathBroadcastReciever extends BroadcastReceiver {
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	Toast.makeText(context, "WOW WOW WOW WOW WOW", 1000).show();
    	String url = intent.getStringExtra(Constants.ACTION_VIDEO_PATH);
    	Intent i = new Intent(context, MainActivity.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	i.putExtra("path", url);
        context.startActivity(i);
    }

}