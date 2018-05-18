package com.aashir.gmote.reciever;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CallbackReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//Toast.makeText(context, intent.getStringExtra(QPairConstants.EXTRA_CAUSE) + "  ", 15000).show();
	}

}
