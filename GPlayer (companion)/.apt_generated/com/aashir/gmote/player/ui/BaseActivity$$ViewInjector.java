// Generated code from Butter Knife. Do not modify!
package com.aashir.gmote.player.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BaseActivity$$ViewInjector {
  public static void inject(Finder finder, final com.aashir.gmote.player.ui.BaseActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427420, "field 'mVideoView'");
    target.mVideoView = (com.aashir.gmote.player.widget.CustomVideoView) view;
    view = finder.findRequiredView(source, 2131427422, "field 'mProgressCircle'");
    target.mProgressCircle = (com.gc.materialdesign.views.ProgressBarCircularIndeterminate) view;
    view = finder.findRequiredView(source, 2131427421, "field 'mSubTitles'");
    target.mSubTitles = (android.widget.TextView) view;
  }

  public static void reset(com.aashir.gmote.player.ui.BaseActivity target) {
    target.mVideoView = null;
    target.mProgressCircle = null;
    target.mSubTitles = null;
  }
}
