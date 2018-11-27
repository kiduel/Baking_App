package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.model.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoFragment extends Fragment {
    private static final String STATE = "STATE";
    private static final String VIDEO = "VIDEO";
    private static final String POSITION = "POS";
    private static final String STEP = "STEP";
    private static final String STEP_LIST = "STEP_LIST";
    private static final String STEP_NUMBER = "STEP_NUMBER";

    public long currentPosition = C.TIME_UNSET;
    @BindView(R.id.description)
    TextView description_text_view;
    @BindView(R.id.next_button)
    TextView next_button;
    @BindView(R.id.prev_button)
    TextView prev_button;
    @BindView(R.id.playerView)
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer simpleExoPlayer;
    Context context;
    Step step_org;
    ArrayList<Step> stepArrayList;
    int pos;
    String video_to_be_saved;
    private String step_description;
    private boolean state = true;
    private String videoUrl;

    Step s;

    public VideoFragment() {
    }


    @Override
    public void onAttach(Context context) {
        Log.i("TAG", "onAttach: is running");
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        ButterKnife.bind(this, view);

        context = getContext();


        Log.i("TAG", "onCreate is running");


        if(savedInstanceState == null){
            Bundle bundle = getArguments();
            stepArrayList = bundle.getParcelableArrayList("stepsArrayList");
            pos = bundle.getInt("stepNumber");
            step_org = stepArrayList.get(pos);
        }else {
            stepArrayList = savedInstanceState.getParcelableArrayList(STEP_LIST);
            step_org = savedInstanceState.getParcelable(STEP);
            currentPosition = savedInstanceState.getLong(POSITION, 0L);
            state = savedInstanceState.getBoolean(STATE);
            pos = savedInstanceState.getInt(STEP_NUMBER);
        }

        Log.i("VideoFragment", String.valueOf(currentPosition));
        populateUI();


        if ( next_button != null ) {
            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearPlayerDetails();
                   // simpleExoPlayer = null;
                   releasePlayer();
                    if ( pos < stepArrayList.size() - 1 ) {
                        pos++;
                    } else {
                        Toast.makeText(getContext(), "You have reached the final step", Toast.LENGTH_SHORT).show();
                    }
                    populateUI();

                }
            });
        }

        if ( prev_button != null ) {
            prev_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearPlayerDetails();
                    releasePlayer();
                    if ( pos < stepArrayList.size() - 1 ) {
                        pos--;
                    }
                    if ( pos < 0 ) {
                        pos = 0;
                        Toast.makeText(getContext(), "You have reached the first step", Toast.LENGTH_SHORT).show();
                    }
                    populateUI();

                }
            });
        }

        return view;
    }


    private void populateUI(){
        step_org = stepArrayList.get(pos);
        videoUrl = step_org.getVideoURL();
        description_text_view.setText(step_org.getDescription());
        Log.i("PopulateUI", String.valueOf(currentPosition));
        if ( videoUrl.isEmpty() ) {
            simpleExoPlayerView.setVisibility(View.GONE);
            releasePlayer();
        } else {
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(videoUrl));
        }


    }

    public void setDescription(String description) {
        step_description = description;

    }


    private void clearPlayerDetails(){

        video_to_be_saved = null;
        videoUrl = null;
        currentPosition = 0L;
        state = false;

    }


    /**
     * Initialize ExoPlayer.
     *
     * @param videoUri The URI of the sample to play.
     */
    private void initializePlayer(Uri videoUri) {
        releasePlayer();
        if ( simpleExoPlayer == null ) {
            String uriString = videoUri.toString();
            if ( uriString.isEmpty() ) {
                Toast.makeText(context, "No Video", Toast.LENGTH_SHORT).show();
            } else {
                // Create an instance of the ExoPlayer.
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                if ( getContext() != null ) {
                    simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
                    simpleExoPlayerView.setPlayer(simpleExoPlayer);
                    simpleExoPlayer.addListener(new ExoPlayer.EventListener() {

                        @Override
                        public void onTimelineChanged(Timeline timeline, Object manifest) {

                        }

                        @Override
                        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                        }

                        @Override
                        public void onLoadingChanged(boolean isLoading) {

                        }

                        @Override
                        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                            if(playWhenReady && playbackState == ExoPlayer.STATE_READY) {
                                state = true;
                            }else if (!playWhenReady) {
                                state = false;
                            }
                        }



                        @Override
                        public void onPlayerError(ExoPlaybackException error) {

                        }

                        @Override
                        public void onPositionDiscontinuity() {

                        }
                    });


                    Log.i("InitPlayer", String.valueOf(currentPosition));
                    // Prepare the MediaSource.
                    String userAgent = Util.getUserAgent(getContext(), "BakingApp");
                    MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                            getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                    if ( currentPosition != 0L && state == true) {
                    simpleExoPlayer.seekTo(currentPosition);
                    simpleExoPlayer.prepare(mediaSource);
                    //simpleExoPlayer.setPlayWhenReady(true);
                    simpleExoPlayer.setPlayWhenReady(state);
                } else if (currentPosition != 0L){
                        simpleExoPlayer.seekTo(currentPosition);
                        simpleExoPlayer.prepare(mediaSource);
                    }
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if ( simpleExoPlayer != null ) {
            currentPosition = simpleExoPlayer.getCurrentPosition();
            Log.i("TAG", "onPause: " + currentPosition);
            state = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }


    @Override
    public void onDestroyView() {
        Log.i("TAG", "onDestroyView is running ");
        if (simpleExoPlayer != null) {
            releasePlayer();
        }
        super.onDestroyView();
    }

    /*
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if ( simpleExoPlayer != null ) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
    }

    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(videoUrl)){
            initializePlayer(Uri.parse(videoUrl));
        }else {
            state = false;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putParcelableArrayList(STEP_LIST, stepArrayList);
        currentState.putString(VIDEO, videoUrl);
        currentState.putParcelable(STEP, step_org);
        currentState.putInt(STEP_NUMBER, pos);
        currentState.putLong(POSITION, currentPosition);
        currentState.putBoolean(STATE, state);

        super.onSaveInstanceState(currentState);
    }

}

