// Generated code from Butter Knife. Do not modify!
package com.aashir.gmote.player.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ListActivity$$ViewInjector {
  public static void inject(Finder finder, final com.aashir.gmote.player.ui.ListActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427393, "field 'mPager'");
    target.mPager = (android.support.v4.view.ViewPager) view;
    view = finder.findRequiredView(source, 2131427391, "field 'mToolbar'");
    target.mToolbar = (android.support.v7.widget.Toolbar) view;
    view = finder.findRequiredView(source, 2131427392, "field 'mSlidingTabLayout'");
    target.mSlidingTabLayout = (com.aashir.gmote.player.widget.SlidingTabLayout) view;
  }

  public static void reset(com.aashir.gmote.player.ui.ListActivity target) {
    target.mPager = null;
    target.mToolbar = null;
    target.mSlidingTabLayout = null;
  }
}
