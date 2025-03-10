package com.mamboflix.utils.track_selection_dialog;

import android.content.Context;
import android.util.SparseArray;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.exoplayer2.C;
import com.mamboflix.R;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter {

  private final SparseArray<TrackSelectionViewFragment> tabFragments;
  private final ArrayList<Integer> tabTrackTypes;
  private Context context;

  public FragmentAdapter(FragmentManager fragmentManager, Context context, SparseArray<TrackSelectionViewFragment> tabFragments, ArrayList<Integer> tabTrackTypes) {
    super(fragmentManager);
    this.context = context.getApplicationContext();
    this.tabFragments = tabFragments;
    this.tabTrackTypes = tabTrackTypes;
  }

  @Override
  public Fragment getItem(int position) {
    return tabFragments.valueAt(position);
  }

  @Override
  public int getCount() {
    return 1;
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    Integer trackType = tabTrackTypes.get(position);
      return switch (trackType) {
          case C.TRACK_TYPE_VIDEO ->
                  context.getResources().getString(R.string.exo_track_selection_title_video);
          case C.TRACK_TYPE_AUDIO ->
                  context.getResources().getString(R.string.exo_track_selection_title_audio);
          case C.TRACK_TYPE_TEXT ->
                  context.getResources().getString(R.string.exo_track_selection_title_text);
          default -> throw new IllegalArgumentException();
      };
  }

}
