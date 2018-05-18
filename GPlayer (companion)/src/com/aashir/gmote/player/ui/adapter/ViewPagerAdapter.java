package com.aashir.gmote.player.ui.adapter;
 
import com.aashir.gmote.player.ui.RecentsFragment;
import com.aashir.gmote.player.ui.VideosFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class ViewPagerAdapter extends FragmentPagerAdapter {
	
	private String[] mTitles = new String[]{"VIDEOS", "RECENT PLAYS"};
	
	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
 
	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		
		case 0:
			VideosFragment vid = new VideosFragment();
			return vid;
			
		case 1:
			RecentsFragment rem = new RecentsFragment();
			return rem;
		}
		return null;
	}
 
	public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
	
	@Override
	public int getCount() {
		return 2;
	}
 
}