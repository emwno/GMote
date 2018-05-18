package com.aashir.gmote.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.IconButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.aashir.gmote.GMote;
import com.aashir.gmote.PairServiceConnection;
import com.aashir.gmote.R;
import com.aashir.gmote.model.Constants;
import com.aashir.gmote.model.SyncCallback;
import com.aashir.gmote.reciever.ConnectionObserver;
import com.aashir.gmote.util.Utils;
import com.gc.materialdesign.views.ButtonFloat;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify.IconValue;
import com.lge.qpair.api.r2.QPairConstants;

public class BaseActivity extends ActionBarActivity implements OnSharedPreferenceChangeListener, SyncCallback {

	@InjectView(R.id.my_drawer_layout) DrawerLayout mDrawerLayout;
	@InjectView(R.id.drawer) FrameLayout mDrawerFragment;
	@InjectView(R.id.toolbar) Toolbar mToolbar;
	
	@InjectView(R.id.video_title) TextView mVideoTitleTV;
	@InjectView(R.id.play) ButtonFloat mPlayButton;
	@InjectView(R.id.skip_next) ButtonFloat mSkipNextButton;
	@InjectView(R.id.skip_previous) ButtonFloat mSkipPrevButton;
	@InjectView(R.id.seek) SeekBar mSeek;
	@InjectView(R.id.video_duration) TextView mVideoDurationTV;
	@InjectView(R.id.video_position) TextView mVideoPositionTV;
	@InjectView(R.id.mute_icon) IconButton mMuteButton;
	
	private ActionBarDrawerToggle mDrawerToggle;
	private PairServiceConnection mConnection;
	protected Utils mUtils;
	private ConnectionObserver mObserver;
	
	protected int mVideoDuration;
	protected String mVideoTitle;
	private boolean mBound = false;
	private boolean mMuted = false;
	private int mSkipBy;
	private int mCurrentVolume = 7;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		
		mConnection = new PairServiceConnection(this);
	    mUtils = new Utils(this);
	    mObserver = new ConnectionObserver(new Handler(), this);
	    
	    setUI();
	    restoreData();
	    
        getContentResolver().registerContentObserver(Uri.parse(QPairConstants.PROPERTY_SCHEME_AUTHORITY + Constants.PROPERTY_QPAIR_CONNECTED), true, mObserver);
        
        mSkipBy = mUtils.getPreference(Constants.SHARED_PREF_KEY_SKIP);
	    GMote.getInstance().getPreferences().registerOnSharedPreferenceChangeListener(this);
	    
        boolean qpair = mUtils.isQPairInstalled();
        if(!qpair) {
        	mUtils.errorDialog(1, View.VISIBLE);
        } else {
        	boolean conn = mUtils.getStringProperty(Constants.PROPERTY_QPAIR_CONNECTED).equals("true");
        	if(!conn) mUtils.errorDialog(0, View.VISIBLE);	
        }
	}
	
	@Override
	public void onJobComplete() {
		unbindService(mConnection);
		mBound = false;
	}

	@Override
	public void syncPeer(String action, String path, int val, float dec) {
		mConnection.setAction(action);
		if(action == Constants.ACTION_ADJUST_VOLUME) {
			if(val == -2) {
				val = 0;
			} else if(val == -3) {
				val = mCurrentVolume;
			} else {
			    val = mUtils.adjustVolume(mCurrentVolume, val);
			    mCurrentVolume = val;
			}
		}
		
		mConnection.setExtra(path, val, dec);
		Intent intent = new Intent(QPairConstants.ACTION_SERVICE);
    	intent.setPackage(QPairConstants.PACKAGE_NAME);
        bindService(intent, mConnection, 0);
        mBound = true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
		mSkipBy = sp.getInt(key, mSkipBy);
	}
	
	@Override
	public void peerConnectionChange() {
		syncPeer(Constants.ACTION_VIDEO_LIST, null, -1, -1);
		boolean d = mUtils.getStringProperty(Constants.PROPERTY_QPAIR_CONNECTED).equals("true");
        if(d == false) {
        	mUtils.errorDialog(0, View.VISIBLE);	
        } else {
        	mUtils.errorDialog(0, View.GONE);
        }
	}
	
	private void restoreData() {
		// Restore Video Title
		mVideoTitle = mUtils.getStringProperty(Constants.PROPERTY_VIDEO_TITLE);
        if(mVideoTitle == "") mVideoTitle = "Nothing";
        mVideoTitleTV.setText(mVideoTitle);
        
        // Restore Play/Pause
        String s = mUtils.getStringProperty(Constants.PROPERTY_VIDEO_PLAYBACK);
        if(s.equals("true")) {
        	playBackUI(0);
        }
        
        // Video Duration (ALSO SET SEEKBAR)
        long l = mUtils.getLongProperty(Constants.PROPERTY_VIDEO_DURATION);
        mVideoDurationTV.setText(mUtils.timeFormat(l));
        mSeek.setMax((int)l);
        
	}
	
	private void setUI() {
		mToolbar.setTitle(null);
	    setSupportActionBar(mToolbar);
	    mDrawerToggle= new ActionBarDrawerToggle(this, mDrawerLayout,mToolbar, R.string.app_name, R.string.app_name);
	    mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        IconDrawable play = new IconDrawable(this, IconValue.fa_play);
        play.sizeDp(20);
        play.color(Color.WHITE);
        
        IconDrawable next = new IconDrawable(this, IconValue.fa_step_forward);
        next.sizeDp(20);
        next.color(Color.WHITE);
        
        IconDrawable prev = new IconDrawable(this, IconValue.fa_step_backward);
        prev.sizeDp(20);
        prev.color(Color.WHITE);
        
        mPlayButton.setIconDrawable(play);
        mSkipNextButton.setIconDrawable(next);
        mSkipPrevButton.setIconDrawable(prev);
        
        mVideoTitleTV.setSelected(true);
        mSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar arg0, int value, boolean arg2) {
				if(arg2 == true) {
					syncPeer(Constants.ACTION_SEEK, null, value, -1);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}
        });
        
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MediaFragment s = new MediaFragment();
        fragmentTransaction.add(R.id.drawer, s);
        fragmentTransaction.commit();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	    super.onPostCreate(savedInstanceState);
	    mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ButterKnife.reset(this);
        mUtils.errorDialog(0, View.GONE);
		if(mBound) unbindService(mConnection);
	}
	
	@Override
    public void onPause() {
        getContentResolver().unregisterContentObserver(mObserver);
        super.onPause();
    }
    
    @Override
    public void onResume() {
    	getContentResolver().registerContentObserver(Uri.parse(QPairConstants.PROPERTY_SCHEME_AUTHORITY + 
				 Constants.PROPERTY_QPAIR_CONNECTED), true, mObserver);
        super.onResume();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
        switch (keyCode) {
        
            case KeyEvent.KEYCODE_VOLUME_UP:
            	syncPeer(Constants.ACTION_ADJUST_VOLUME, null, 0, -1);
            return true;
            
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            	syncPeer(Constants.ACTION_ADJUST_VOLUME, null, 1, -1);
            return true;
            
            case KeyEvent.KEYCODE_BACK:
            	if(mDrawerLayout.isDrawerOpen(mDrawerFragment)) {
            		mDrawerLayout.closeDrawer(mDrawerFragment);
            	} else {
            	    onBackPressed();
            	}
            return true;
        default:
            return false;
        }
    }
    
    public void playBackUI(int type) {
    	IconDrawable ic;
    	
    	if(type == 0) {
    	    ic = new IconDrawable(this, IconValue.fa_pause);
            ic.sizeDp(20);
            ic.color(Color.WHITE);
    	} else {
    	    ic = new IconDrawable(this, IconValue.fa_play);
            ic.sizeDp(20);
            ic.color(Color.WHITE);
    	}
    	
    	mPlayButton.setIconDrawable(ic);
    }
    
    public void onClick(View v) {
		switch(v.getId()) {
		    case R.id.play:
		    	syncPeer(Constants.ACTION_VIDEO_PLAYBACK, null, -1, -1);
		    break;
		
		    case R.id.volumeup:
		    	if(mMuted) mMuteButton.setText("{fa-volume-up}");
		    	syncPeer(Constants.ACTION_ADJUST_VOLUME, null, 0, -1);
			break;
			
		    case R.id.volumedown:
		    	if(mMuted) mMuteButton.setText("{fa-volume-up}");
		    	syncPeer(Constants.ACTION_ADJUST_VOLUME, null, 1, -1);
			break;
			
		    case R.id.skip_next:
		    	syncPeer(Constants.ACTION_SEEK_SKIP, null, mSkipBy, -1);
		    break;
		    
		    case R.id.skip_previous:
		    	syncPeer(Constants.ACTION_SEEK_SKIP, null, -mSkipBy, -1);
		    break;
		    
		    case R.id.rotate:
		    	syncPeer(Constants.ACTION_ROTATE, null, -1, -1);
		    break;
		    
		    case R.id.mute_icon:
		    	if(mMuted) {
		    		mMuteButton.setText("{fa-volume-up}");
		    		syncPeer(Constants.ACTION_ADJUST_VOLUME, null, -3, -1);
		    		mMuted = false;
		    	} else {
		    		mMuteButton.setText("{fa-volume-off}");
		    	    syncPeer(Constants.ACTION_ADJUST_VOLUME, null, -2, -1);
		    	    mMuted = true;
	        	}
		    break;
		    
		    case R.id.settings:
		    	mDrawerLayout.openDrawer(mDrawerFragment);
		    break;
		    
		}
	}

}
