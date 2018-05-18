package com.aashir.gmote.player.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.aashir.gmote.player.R;
import com.aashir.gmote.player.ui.adapter.ViewPagerAdapter;
import com.aashir.gmote.player.widget.SlidingTabLayout;

public class ListActivity extends ActionBarActivity {

	@InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.viewpager) ViewPager mPager;
    @InjectView(R.id.sliding_tabs) SlidingTabLayout mSlidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        
        mPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mSlidingTabLayout.setViewPager(mPager);
    }
    
}