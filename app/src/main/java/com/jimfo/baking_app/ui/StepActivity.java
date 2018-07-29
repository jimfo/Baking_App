package com.jimfo.baking_app.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

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
import com.jimfo.baking_app.R;
import com.jimfo.baking_app.model.Step;
import com.jimfo.baking_app.util.ExoPlayerUtils;

public class StepActivity extends AppCompatActivity{

    private static final String TAG = StepActivity.class.getSimpleName();
    private static final String STATE = "STATE";
    private static final String VIDEO = "VIDEO";
    private ExoPlayerUtils mPlayerUtils;
    private Uri mVideoPath = null;
    private Step mStep;
    private long mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ActionBar bar = getSupportActionBar();

        String subTitle = "";
        TextView stepDescription = findViewById(R.id.step_description_tv);
        SimpleExoPlayerView mPlayerView = findViewById(R.id.playerView);
        mPlayerUtils = new ExoPlayerUtils(this, mPlayerView);

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getLong(STATE);
            mVideoPath = Uri.parse(savedInstanceState.getString(VIDEO));
        }

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        if (extras != null && extras.containsKey(this.getResources().getString(R.string.stepKey))) {
            mStep = extras.getParcelable(this.getResources().getString(R.string.stepKey));
            if (mStep != null) {
                stepDescription.append(mStep.getmDescription());
                String title = extras.getString(getResources().getString(R.string.titleKey));
                subTitle = mStep.getmShortDescription();
                setTitle(title);

                if (!mStep.getmVideoUrl().equals("")) {
                    mVideoPath = Uri.parse(mStep.getmVideoUrl());
                }
            }
        }

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.activityBackground));

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.activityBackground));

            if (bar != null) {
                bar.setDisplayHomeAsUpEnabled(true);
                bar.setSubtitle(subTitle);
                bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.activityBackground)));
            }
        }

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.no_video));

        mPlayerUtils.initializeMediaSession();

        mPlayerUtils.initializePlayer(mVideoPath, mPosition);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(STATE, mPlayerUtils.mExoPlayer.getCurrentPosition());
        outState.putString(VIDEO, mStep.getmVideoUrl());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPlayerUtils.releasePlayer();
        mPlayerUtils.mMediaSession.setActive(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((Util.SDK_INT <= 23 || mPlayerUtils.mExoPlayer == null)) {
            mPlayerUtils.initializeMediaSession();
            mPlayerUtils.initializePlayer(mVideoPath, mPosition);
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        if ((Util.SDK_INT <= 23) || mPlayerUtils.mExoPlayer != null) {
            mPlayerUtils.mExoPlayer.stop();
            mPlayerUtils.mExoPlayer.release();
            mPlayerUtils.mExoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (Util.SDK_INT > 23) {
            mPlayerUtils.releasePlayer();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
