package com.video.playerdemo;

import android.app.AlertDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {
    DefaultTrackSelector trackSelector;
    int HI_BITRATE = 2097152;
    int MI_BITRATE = 1048576;
    int LO_BITRATE = 524288;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlayerView playerView = findViewById(R.id.video_player);

        trackSelector = new DefaultTrackSelector();
        DefaultTrackSelector.Parameters defaultTrackParam = trackSelector.buildUponParameters().build();
        trackSelector.setParameters(defaultTrackParam);

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        playerView.setPlayer(player);

        DefaultDataSourceFactory fac = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)));

        HlsMediaSource videoSource = new HlsMediaSource.Factory(fac).createMediaSource(
                Uri.parse("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
        );

        player.prepare(videoSource);


        findViewById(R.id.quality_lo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultTrackSelector.Parameters parameters = trackSelector.buildUponParameters()
                        .setMaxVideoBitrate(LO_BITRATE)
                        .setForceHighestSupportedBitrate(true)
                        .build();
                trackSelector.setParameters(parameters);
            }
        });

        findViewById(R.id.quality_mi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultTrackSelector.Parameters parameters = trackSelector.buildUponParameters()
                        .setMaxVideoBitrate(MI_BITRATE)
                        .setForceHighestSupportedBitrate(true)
                        .build();
                trackSelector.setParameters(parameters);
            }
        });

        findViewById(R.id.quality_hi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultTrackSelector.Parameters parameters = trackSelector.buildUponParameters()
                        .setMaxVideoBitrate(HI_BITRATE)
                        .setForceHighestSupportedBitrate(true)
                        .build();
                trackSelector.setParameters(parameters);
            }
        });
        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    CharSequence title = "Setting";
                    int rendererIndex = 0; // renderer for video
                    int rendererType = mappedTrackInfo.getRendererType(rendererIndex);
                    boolean allowAdaptiveSelections =
                            rendererType == C.TRACK_TYPE_VIDEO
                                    || (rendererType == C.TRACK_TYPE_AUDIO
                                    && mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_NO_TRACKS);


                    Pair<AlertDialog, TrackSelectionView> dialogPair =
                            TrackSelectionView.getDialog(MainActivity.this, title, trackSelector, rendererIndex);
                    dialogPair.second.setShowDisableOption(true);
                    dialogPair.second.setAllowAdaptiveSelections(allowAdaptiveSelections);
                    dialogPair.first.show();
                }
            }
        });
    }

}
