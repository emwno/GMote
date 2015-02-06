package com.aashir.gmote.ui;
 
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.IconButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView.OnEditorActionListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.aashir.gmote.R;
import com.aashir.gmote.model.Constants;
import com.aashir.gmote.model.MediaCallback;
import com.aashir.gmote.model.SyncCallback;
import com.aashir.gmote.reciever.MediaReciever;
import com.aashir.gmote.ui.adapter.MediaAdapter;
import com.aashir.gmote.ui.widget.FloatingEditText;
import com.aashir.gmote.ui.widget.Switch;
import com.aashir.gmote.util.Utils;
import com.gc.materialdesign.widgets.Dialog;
 
public class MediaFragment extends Fragment implements MediaCallback, OnEditorActionListener, OnItemClickListener, OnSeekBarChangeListener, OnTouchListener {
	
	@InjectView(R.id.subtitle_help) IconButton mSubHelp;
	@InjectView(R.id.media_list) ListView mList;
	@InjectView(R.id.url_input) FloatingEditText mUrlInput;
	@InjectView(R.id.media_not) TextView mNotYetTV;
	
	@InjectView(R.id.subtitle_switch) Switch mSwitch;
	@InjectView(R.id.setting_skip_value) TextView mSkipTV;
	@InjectView(R.id.setting_skip_seekbar) SeekBar mSkipSeek;
	@InjectView(R.id.setting_speed_value) TextView mSpeedTV;
	@InjectView(R.id.setting_speed_seekbar) SeekBar mSpeedSeek;
	
	private SyncCallback mCallback;
	private Utils mUtils;
	private String[] mPath;
	private MediaReciever mMediaReciever;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCallback = (SyncCallback) getActivity();
		mUtils = new Utils(getActivity());
        mMediaReciever = new MediaReciever(this);
		getActivity().registerReceiver(mMediaReciever, new IntentFilter(Constants.BROADCAST_RECIEVER_MEDIA));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.media, container, false);
		ButterKnife.inject(this, view);
		
		mCallback.syncPeer(Constants.ACTION_VIDEO_LIST, null, -1, -1);
		
		String scheck = mUtils.getStringProperty(Constants.PROPERTY_VIDEO_SUBTITLE);
		boolean check = (scheck == "true") ? true : false;
		int skip = mUtils.getPreference(Constants.SHARED_PREF_KEY_SKIP) / 1000;
		mSwitch.setChecked(check);
		mSkipTV.setText(skip + "S");
		mSkipSeek.setProgress(skip);
		
		mUrlInput.setOnEditorActionListener(this);
		mSubHelp.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog ad = new AlertDialog.Builder(getActivity())
	            .create();
	    ad.setTitle("Subtitle Help");
	    ad.setMessage(getResources().getString(R.string.settings_subtitle_desc));
	    ad.setButton("OK", new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int which) {
	            dialog.dismiss();
	        }
	    });
	ad.show();
			}
			
		});
		
		mSwitch.setOnCheckedChangeListener( new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCallback.syncPeer(Constants.ACTION_VIDEO_SUBTITLE, Boolean.toString(isChecked), -1, -1);
				mUtils.setStringProperty(Constants.PROPERTY_VIDEO_SUBTITLE, Boolean.toString(isChecked));
			}
			
		});
		mSkipSeek.setOnSeekBarChangeListener(this);
		mSkipSeek.setOnTouchListener(this);
		mSpeedSeek.setOnTouchListener(this);
		mSpeedSeek.setOnSeekBarChangeListener(this);
		return view;
	}
	
	@Override
	public void onDataRecieve(String[] names, String[] paths) {
		mPath = paths;
		MediaAdapter mAdapter = new MediaAdapter(getActivity(), names);
		mNotYetTV.setVisibility(View.GONE);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(this);
	}
 
	@Override
    public void onPause() {
		getActivity().unregisterReceiver(mMediaReciever);
        super.onPause();
    }
    
    @Override
    public void onResume() {
    	mCallback.syncPeer(Constants.ACTION_VIDEO_LIST, null, -1, -1);
    	getActivity().registerReceiver(mMediaReciever, new IntentFilter(Constants.BROADCAST_RECIEVER_MEDIA));
        super.onResume();
    }
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		mCallback.syncPeer(Constants.ACTION_VIDEO_PATH, mPath[pos], -1, -1);
	}

	@Override
	public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event) {
		if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
			String url = mUrlInput.getText().toString().trim();
			if(url.length() == 0) Toast.makeText(getActivity(), "Empty URL", Toast.LENGTH_LONG).show();
			else if (!mUtils.isURL(url)) Toast.makeText(getActivity(), "Invalid URL", Toast.LENGTH_LONG).show();
			else mCallback.syncPeer(Constants.ACTION_VIDEO_PATH, url, -1, -1);
			return true;
        }
		return false;
	}

	@Override
	public void onProgressChanged(SeekBar bar, int pos, boolean fromUser) {
		if(bar == mSkipSeek && fromUser) {
			mSkipTV.setText(pos + "S");
			mUtils.savePreference(Constants.SHARED_PREF_KEY_SKIP, pos*1000);
		} else if (bar == mSpeedSeek && fromUser) {
			float d = ((float)pos + 5)/10;
			mSpeedTV.setText(d + "X");
			mCallback.syncPeer(Constants.ACTION_VIDEO_PLAYBACK_SPEED, null, -1, d);
		}
	}


	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}


	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
        switch (action) 
        {
        case MotionEvent.ACTION_DOWN:
            v.getParent().requestDisallowInterceptTouchEvent(true);
            break;

        case MotionEvent.ACTION_UP:
            v.getParent().requestDisallowInterceptTouchEvent(false);
            break;
        }
        v.onTouchEvent(event);
        return true;
	}
	
	@Override 
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}