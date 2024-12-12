package com.mamboflix.utils.track_selection_dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.Tracks;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionOverride;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.mamboflix.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TrackSelectionViewFragment extends Fragment implements TrackSelectionView.TrackSelectionListener {
  private MappingTrackSelector.MappedTrackInfo mappedTrackInfo;
  private int rendererIndex;
  private boolean allowAdaptiveSelections;
  private boolean allowMultipleOverrides;
  /* package */ boolean isDisabled;
  /* package */ List<DefaultTrackSelector.SelectionOverride> overrides;

  private List<Tracks.Group> trackGroups; // Corrected type to TrackGroup

  public TrackSelectionViewFragment() {
    // Retain instance across activity re-creation to prevent losing access to init data.
    setRetainInstance(true);
  }

  public void init(
          MappingTrackSelector.MappedTrackInfo mappedTrackInfo,
          int rendererIndex,
          boolean initialIsDisabled,
          @Nullable DefaultTrackSelector.SelectionOverride initialOverride,
          boolean allowAdaptiveSelections,
          boolean allowMultipleOverrides
          // Accept trackGroups here
  ) {
    this.mappedTrackInfo = mappedTrackInfo;
    this.rendererIndex = rendererIndex;
    this.isDisabled = initialIsDisabled;
    this.overrides = initialOverride == null ? Collections.emptyList() : Collections.singletonList(initialOverride);
    this.allowAdaptiveSelections = allowAdaptiveSelections;
    this.allowMultipleOverrides = allowMultipleOverrides;
    this.trackGroups = trackGroups; // Set the track groups
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.exo_track_selection_dialog, container, false);
    TrackSelectionView trackSelectionView = rootView.findViewById(R.id.exo_track_selection_view);

    trackSelectionView.setShowDisableOption(false);
    trackSelectionView.setAllowMultipleOverrides(allowMultipleOverrides);
    trackSelectionView.setAllowAdaptiveSelections(allowAdaptiveSelections);

    trackSelectionView.init(
            trackGroups, // Pass the list of track groups
            isDisabled, // Your boolean for disable status
            createOverridesMap(overrides), // Pass the map of overrides
            null, // Optional comparator, can be null
            this // Pass the listener (this)
    );

    return rootView;
  }

  private Map<TrackGroup, TrackSelectionOverride> createOverridesMap(List<DefaultTrackSelector.SelectionOverride> overrides) {
    Map<TrackGroup, TrackSelectionOverride> overridesMap = new HashMap<>();

    // Get the track groups for the specified renderer index
    TrackGroupArray trackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex);

    if (overrides == null || trackGroupArray == null) {
      return overridesMap; // or throw an exception depending on your use case
    }

    for (DefaultTrackSelector.SelectionOverride override : overrides) {
      // Ensure the group index is valid
      if (override.groupIndex < 0 || override.groupIndex >= trackGroupArray.length) {
        continue; // or handle this case as needed
      }

      // Retrieve the corresponding TrackGroup using the group index from the override
      TrackGroup trackGroup = trackGroupArray.get(override.groupIndex);

      // Create TrackSelectionOverride using the track indices from the override
//      TrackSelectionOverride trackSelectionOverride = new TrackSelectionOverride(override.tracks);
//
//      // Put the TrackGroup and TrackSelectionOverride into the map
//      overridesMap.put(trackGroup, trackSelectionOverride);
    }

    return overridesMap;
  }


  @Override
  public void onTrackSelectionChanged(boolean isDisabled, Map<TrackGroup, TrackSelectionOverride> overrides) {
    this.isDisabled = isDisabled;
    this.overrides = new ArrayList<>();
    for (TrackSelectionOverride override : overrides.values()) {
      // Convert to DefaultTrackSelector.SelectionOverride if necessary
      this.overrides.add(new DefaultTrackSelector.SelectionOverride(override.getType()));
    }
  }
}
