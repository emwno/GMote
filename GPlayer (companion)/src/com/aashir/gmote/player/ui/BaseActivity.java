package com.aashir.gmote.player.ui;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnSeekCompleteListener;
import io.vov.vitamio.MediaPlayer.OnTimedTextListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.MediaController.MediaPlayerControl;

import java.io.File;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.aashir.gmote.player.GMotePlayer;
import com.aashir.gmote.player.R;
import com.aashir.gmote.player.model.Constants;
import com.aashir.gmote.player.service.SyncJob;
import com.aashir.gmote.player.util.Utils;
import com.aashir.gmote.player.widget.CustomVideoView;
import com.aashir.gmote.player.widget.CustomVideoView.PlayPauseListener;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.path.android.jobqueue.JobManager;

public class BaseActivity extends Activity implements MediaPlayerControl, OnTimedTextListener {

	@InjectView(R.id.videoView) CustomVideoView mVideoView;
	@InjectView(R.id.progressBar) ProgressBarCircularIndeterminate mProgressCircle;
	@InjectView(R.id.subtitle) TextView mSubTitles;
	
    protected String VIDEO_PATH = "";
    protected String VIDEO_TITLE = "";
	
    private boolean isComplete = false;
	private boolean isVideoPaused = false;
	protected boolean isSubtitleShown = false;
	protected long mVideoCurrentPosition = -1;
	private int mPlay = -10;
	private int mPause = -11;
	protected MediaController mController;
	protected Utils mUtils;
	protected Configuration mConfiguration;
	private Handler mHandler;
	private JobManager mJobManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    View mDecorView = getWindow().getDecorView();
		mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );
		setContentView(R.layout.video_activity);
		ButterKnife.inject(this);
		
		mConfiguration = getResources().getConfiguration();
		mJobManager = GMotePlayer.getInstance().getJobManager();
		mHandler = new Handler();
		mUtils = new Utils(this);
		mProgressCircle = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBar);

		mVideoView.setMediaBufferingIndicator(mProgressCircle);
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    state.putLong("video_position", mVideoCurrentPosition);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle state) {
	    super.onRestoreInstanceState(state);
	    mVideoCurrentPosition = state.getLong("video_position");
	    mVideoView.seekTo(mVideoCurrentPosition);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (mVideoView != null && mVideoView.isPlaying() && !isVideoPaused) {
			mVideoCurrentPosition = mVideoView.getCurrentPosition();
			mVideoView.suspend();
			isVideoPaused = true;
		}
	}

	@Override
	protected void onDestroy() {
		mJobManager.addJobInBackground(new SyncJob(this, Constants.ACTION_VIDEO_STOP, null,  -1));
		mUtils.deleteQPairProperty(Constants.PROPERTY_VIDEO_TITLE);
		mUtils.deleteQPairProperty(Constants.PROPERTY_VIDEO_PLAYBACK);
		mUtils.deleteQPairProperty(Constants.PROPERTY_VIDEO_DURATION);
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mVideoView != null && isVideoPaused) {
			mVideoView.resume();
			mVideoView.seekTo(mVideoCurrentPosition);
			isVideoPaused = false;
		}
	}

	@Override
	public void onRestart() {
	    super.onRestart();
	    refreshVideoData();
	}
	
	/**
	 * Refresh Video data on screen
	 */
	public void refreshVideoData() {
		if (VIDEO_PATH.length() > 0 && mVideoView != null) {
			mVideoView.stopPlayback();

			mVideoView.setVideoPath(VIDEO_PATH);
			mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
			mVideoView.requestFocus();
			
			mController = new MediaController(this);
			mController.setMediaPlayer(this);
			mVideoView.setMediaController(mController);

			mVideoView.start();
			mProgressCircle.setVisibility(View.VISIBLE);

			mVideoView.setPlayPauseListener(new PlayPauseListener() {

				@Override
				public void onPlay() {
					mJobManager.addJobInBackground(new SyncJob(BaseActivity.this, Constants.ACTION_VIDEO_PLAYBACK, null, mPlay));
					mUtils.setStringProperty(Constants.PROPERTY_VIDEO_PLAYBACK, "true");
					mHandler.postDelayed(mVideoPositionUpdate, 1000);
					
					if(isComplete == true) {
						mVideoView.seekTo(0);
						isComplete = false;
					}
				}

				@Override
				public void onPause() {
					mJobManager.addJobInBackground(new SyncJob(BaseActivity.this, Constants.ACTION_VIDEO_PLAYBACK, null, mPause));
					mUtils.setStringProperty(Constants.PROPERTY_VIDEO_PLAYBACK, "false");
					mHandler.removeCallbacks(mVideoPositionUpdate);
				}
				
			});
			
			mVideoView.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					mProgressCircle.setVisibility(View.INVISIBLE);
					mVideoView.start();
					if(isSubtitleShown) {
						String subpath = VIDEO_PATH.substring(0, VIDEO_PATH.lastIndexOf('/')) + "/" +VIDEO_TITLE +".srt";
						File f = new File(subpath);
			      		if(f.exists()) {
				    		mVideoView.setOnTimedTextListener(BaseActivity.this);
				    		mVideoView.addTimedTextSource(subpath);
				            mVideoView.setTimedTextShown(true);
		            	} else {
					    	Toast.makeText(BaseActivity.this, "No subtitles found for this video", Toast.LENGTH_LONG).show();
					    }
					}
					
					mJobManager.addJobInBackground(new SyncJob(BaseActivity.this, Constants.ACTION_VIDEO_TITLE, VIDEO_TITLE, -1));
					mUtils.setStringProperty(Constants.PROPERTY_VIDEO_TITLE, VIDEO_TITLE);
				
			    	mUtils.setLongProperty(Constants.PROPERTY_VIDEO_DURATION, mVideoView.getDuration());
			    	
			    	mJobManager.addJobInBackground(new SyncJob(BaseActivity.this, Constants.ACTION_VIDEO_DURATION, null, (int)mVideoView.getDuration()));
				}
			});
			
			mVideoView.setOnCompletionListener( new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					mJobManager.addJobInBackground(new SyncJob(BaseActivity.this, Constants.ACTION_VIDEO_PLAYBACK, null, mPause));
					mUtils.setStringProperty(Constants.PROPERTY_VIDEO_PLAYBACK, "false");
					isComplete = true;
				}
				
			});
			
			mVideoView.setOnSeekCompleteListener( new OnSeekCompleteListener() {

				@Override
				public void onSeekComplete(MediaPlayer mp) {
					mJobManager.addJobInBackground(new SyncJob(BaseActivity.this, Constants.ACTION_VIDEO_PLAYBACK, null, mPlay));
					mHandler.postDelayed(mVideoPositionUpdate, 0);
					mUtils.setStringProperty(Constants.PROPERTY_VIDEO_PLAYBACK, "true");
					
					if(isComplete == true) {
						mVideoView.seekTo(0);
						isComplete = false;
					}
				}
				
			});
		}
	}
	
	@Override
	public void start() {
		if (mVideoView != null) mVideoView.start();
	}

	@Override
	public void pause() {
		if (mVideoView != null) mVideoView.pause();
	}

	@Override
	public long getDuration() {
		if (mVideoView != null)
			return mVideoView.getDuration();
		else
			return 0;
	}

	@Override
	public long getCurrentPosition() {
		if (mVideoView != null)
			return mVideoView.getCurrentPosition();
		else
			return 0;
	}

	@Override
	public void seekTo(long pos) {
		if (mVideoView != null)
			mVideoView.seekTo(pos);
	}

	@Override
	public boolean isPlaying() {
		if (mVideoView != null)
			return mVideoView.isPlaying();
		else
			return false;
	}

	@Override
	public int getBufferPercentage() {
		if (mVideoView != null)
			return mVideoView.getBufferPercentage();
		else
			return 0;
	}

	@Override
	public void onTimedText(String text) {
		mSubTitles.setText(text);
	}

	@Override
	public void onTimedTextUpdate(byte[] pixels, int width, int height) {
	}
	
	public Runnable mVideoPositionUpdate = new Runnable() {

	    @Override
	    public void run() {
	    	
	    	if(mVideoView != null && mVideoView.isPlaying() == true) {
	    		mJobManager.addJobInBackground(new SyncJob(BaseActivity.this, Constants.ACTION_VIDEO_POSITION, null, (int) mVideoView.getCurrentPosition()));
	    	}
	    	
	        mHandler.postDelayed(mVideoPositionUpdate, 1000);
	    }
	};
	
}
