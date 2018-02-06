package com.yosri.mustafa.eng.bakingapp.Ui;

import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.yosri.mustafa.eng.bakingapp.Model.Step;
import com.yosri.mustafa.eng.baking.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import static android.view.Gravity.FILL;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepDetailsFragment extends Fragment {

    private ArrayList<Step> steps;
    int position;
    private Step step;
    private SimpleExoPlayer mExoPlayer;

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.short_desc)
    TextView shortDesc;
    @BindView(R.id.thumbnail)
    ImageView thumbnail;
    @BindView(R.id.nextpage)
    Button next;
    @BindView(R.id.lastpage)
    Button last;
    private long vidPosition = C.TIME_UNSET;
    private String video;
    private String img;
    private Bundle getbundle;

    @OnClick(R.id.lastpage)
    public void submit() {
        if (mExoPlayer != null) {
            releasePlayer();
        }
        position = --position;
        checkPosition(position);
        step = steps.get(position);
        viewSteps(step);

        Log.e("Video", " prev link  " + position);
    }

    @OnClick(R.id.nextpage)
    public void submit1() {
        if (mExoPlayer != null) {
            releasePlayer();
        }
        position = ++position;
        checkPosition(position);
        step = steps.get(position);
        viewSteps(step);
        Log.e("next Video", "" + position);

    }

    public StepDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, view);
        viewSteps(step);
        getbundle = new Bundle();
        return view;

    }

    @Override
    public void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("vidPosition","pos is :" +vidPosition);
        outState.putLong("vPosition",vidPosition);
        outState.putInt("position",position);
    }

    private void viewSteps(Step step) {
        checkPosition(position);
        Log.e("next Video", "" + position);
        video = step.getVideoURL();
         img = step.getThumbnailURL();

        if (img == "" && video == "") {
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.no_video_recording));
            mPlayerView.setVisibility(View.GONE);
            thumbnail.setVisibility(View.GONE);

        }
        else {
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.food));

            if (img.equals("")&& !video.equals("")) {
                thumbnail.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);
                initializePlayer(Uri.parse(video));

            }
            else {
                mPlayerView.setVisibility(View.GONE);
                thumbnail.setVisibility(View.VISIBLE);

                if (img.isEmpty()) {
                    thumbnail.setImageResource(R.drawable.food);
                } else {

                    Picasso.with(getActivity())
                            .load(img)
                            .placeholder(R.drawable.food)
                            .into(thumbnail);
                }

            }
        }
        shortDesc.setText(step.getShortDescription());
        desc.setText(step.getDescription());


    }

    private void checkPosition(int position) {

        if (position == 0 || position == -1) {
            next.setVisibility(View.VISIBLE);
            last.setVisibility(View.GONE);

        } else if (position == steps.size() - 1) {
            last.setVisibility(View.VISIBLE);
            next.setVisibility(View.GONE);

        } else {
            next.setVisibility(View.VISIBLE);
            last.setVisibility(View.VISIBLE);
        }
    }


    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {

            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "Baking");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
// new
            if (vidPosition != C.TIME_UNSET) {
                mExoPlayer.seekTo(vidPosition);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(false);
            }
                else {
                mExoPlayer.seekTo(0);

                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
            }
            }


        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            mPlayerView.setResizeMode(FILL);
        }

    }


    private void releasePlayer() {
        if(mExoPlayer!= null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    public static StepDetailsFragment getInstance(ArrayList<Step> steps, int position) {
        Log.e("StepDetailsFragment", "StepDeialsF Postis :  " + position);
        Log.e("StepDetailsFragment", "StepDeialsF :  " + steps.size());

        StepDetailsFragment recipeStepDetailFragment = new StepDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("step", steps);
        bundle.putInt("position", position);
        recipeStepDetailFragment.setArguments(bundle);
        return recipeStepDetailFragment;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        Bundle bundle = getArguments();
        setRetainInstance(true);

        if (saveInstanceState != null) {
            Log.e("saveInstanceState", "saveInstanceState :" + saveInstanceState.getLong("vPosition"));
            position = saveInstanceState.getInt("postion");
            vidPosition = saveInstanceState.getLong("vPosition", C.TIME_UNSET);
            mExoPlayer.seekTo(vidPosition);

        } else {
            Log.e("saveInstanceState", "saveInstanceState :" + vidPosition);


            if (bundle != null) {
                steps = (ArrayList<Step>) bundle.getSerializable("step");
                Log.e("StepDeialsF", "StepDeialsF :  " + steps.size());

                position = bundle.getInt("position");
                Log.e("StepDeialsF", "StepDeialsF Postis :  " + position);

                if (steps != null) {
                    step = steps.get(position);
                } else
                    Log.e("StepDeialsF", "Steps null");
            }
        }
    }
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
      vidPosition =savedInstanceState.getLong("vPosition",C.TIME_UNSET);
      Log.e("onActivityCreated","onActivityCreated   :"+vidPosition);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (video != null){
            if (mExoPlayer != null) {
//                initializeVideoPlayer(videoUri);
                mExoPlayer.seekTo(vidPosition);
            } else {
            initializePlayer(Uri.parse(video));
    }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
            releasePlayer();
        }


    @Override
    public void onPause() {
        super.onPause();
        vidPosition = mExoPlayer.getCurrentPosition();
        position = position;
     releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
            releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
            releasePlayer();
    }


}