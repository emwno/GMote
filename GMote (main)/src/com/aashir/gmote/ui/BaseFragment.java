package com.aashir.gmote.ui;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView.OnEditorActionListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.aashir.gmote.R;
import com.aashir.gmote.model.Constants;
import com.aashir.gmote.model.SyncCallback;
import com.aashir.gmote.ui.widget.FloatingEditText;
import com.aashir.gmote.ui.widget.Switch;
import com.aashir.gmote.util.Utils;

public class BaseFragment extends Fragment implements OnEditorActionListener, OnItemClickListener, OnSeekBarChangeListener, OnTouchListener, OnCheckedChangeListener {

	@InjectView(R.id.media_list) ListView mList;
	@InjectView(R.id.url_input) FloatingEditText mUrlInput;
	@InjectView(R.id.media_not) TextView mNotYetTV;
	
	@InjectView(R.id.subtitle_switch) Switch mSwitch;
	@InjectView(R.id.setting_skip_value) TextView mSkipTV;
	@InjectView(R.id.setting_skip_seekbar) SeekBar mSkipSeek;
	@InjectView(R.id.setting_speed_value) TextView mSpeedTV;
	@InjectView(R.id.setting_speed_seekbar) SeekBar mSpeedSeek;
	
	SyncCallback mCallback;
	Utils mUtils;
	String[] mPath;
    
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

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		mCallback.syncPeer(Constants.ACTION_VIDEO_SUBTITLE, Boolean.toString(isChecked), -1, -1);
		mUtils.setStringProperty(Constants.PROPERTY_VIDEO_SUBTITLE, Boolean.toString(isChecked));
	}
	
}
