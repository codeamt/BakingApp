package com.example.annmargaret.bakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.annmargaret.bakingapp.R;
import com.example.annmargaret.bakingapp.models.Recipe;
import com.example.annmargaret.bakingapp.models.Step;

import static com.example.annmargaret.bakingapp.ui.RecipeActivity.SELECTED_INDEX;
import static com.example.annmargaret.bakingapp.ui.RecipeActivity.SELECTED_RECIPES;
import static com.example.annmargaret.bakingapp.ui.RecipeActivity.SELECTED_STEPS;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailStepFragment extends Fragment {

    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private ArrayList<Step> steps = new ArrayList<>();
    private int selectedIndex;
    private Handler handler;
    ArrayList<Recipe> recipe;
    String recipeName;
    public long currentPosition = C.TIME_UNSET;
    Boolean playWhenReady = true;
    String videoURL;
    Uri videoURI;


    @BindView(R.id.recipe_step_detail_text) TextView textView;
    @BindView(R.id.playerView) SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.thumbImage) ImageView thumbImage;
    @BindView(R.id.previousStep) Button mPrev;
    @BindView(R.id.nextStep) Button mNext;

    public RecipeDetailStepFragment() {

    }

    private ListItemClickListener itemClickListener;

    public interface ListItemClickListener {
       void onListItemClick(List<Step> allSteps, int index, String recipeName);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail_fragment_body_part, container, false);
        ButterKnife.bind(this, rootView);

        handler = new Handler(Looper.getMainLooper());
        bandwidthMeter = new DefaultBandwidthMeter();

        itemClickListener = (RecipeDetailActivity)getActivity();

        recipe = new ArrayList<>();


        if(savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(SELECTED_STEPS);
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX);
            recipeName = savedInstanceState.getString("Title");
            currentPosition = savedInstanceState.getLong("Player_Position");
        }
        else {

            if (getArguments() != null) {
                steps = getArguments().getParcelableArrayList(SELECTED_STEPS);
                selectedIndex = getArguments().getInt(SELECTED_INDEX);
                recipeName = getArguments().getString("Title");
            } else {
                if(getArguments() != null) {
                    recipe = getArguments().getParcelableArrayList(SELECTED_RECIPES);
                    if(recipe != null)
                        steps = (ArrayList<Step>) recipe.get(0).getSteps();
                }
                selectedIndex = 0;
            }
        }


        textView.setVisibility(View.VISIBLE);
        if(steps != null) {
            textView.setText(steps.get(selectedIndex).getDescription());

            simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

            videoURL = steps.get(selectedIndex).getVideoURL();
            videoURI = Uri.parse(steps.get(selectedIndex).getVideoURL());
            if (rootView.findViewWithTag("sw600dp-port-recipe_step_detail") != null && getActivity() != null) {
                recipeName = ((RecipeDetailActivity) getActivity()).recipeName;
                if (((RecipeDetailActivity) getActivity()).getSupportActionBar() != null) {
                    ((RecipeDetailActivity) getActivity()).getSupportActionBar().setTitle(recipeName);
                }
            }

            String imageUrl = steps.get(selectedIndex).getThumbnailURL();
            if (!imageUrl.isEmpty()) {
                Uri uri = Uri.parse(imageUrl).buildUpon().build();
                Picasso.with(getContext()).load(uri).into(thumbImage);
            }

            if (!videoURL.isEmpty()) {
                initializePlayer(videoURI);

                if (rootView.findViewWithTag("sw600dp-port-recipe_step_detail") != null && getActivity() != null) {
                    getActivity().findViewById(R.id.fragment_container).setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                    simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
                } else if (isInLandscapeMode(getContext())) {
                    textView.setVisibility(View.GONE);
                }
            } else {

                player = null;
                if (getContext() != null) {
                    simpleExoPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.no_media_words));
                    if (!isInLandscapeMode(getContext())) {
                        if(rootView.findViewWithTag("sw600dp-port-recipe_step_detail") != null || rootView.findViewWithTag("sw600dp-land-recipe_step_detail") != null) {
                            simpleExoPlayerView.setLayoutParams(new FrameLayout.LayoutParams(1400, 700));
                        } else {
                            simpleExoPlayerView.setLayoutParams(new FrameLayout.LayoutParams(900, 500));
                        }
                    }
                    else {
                        simpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(1200, 600));
                    }
                }
            }


            mPrev.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (steps.get(selectedIndex).getId() > 0) {
                        if (player != null) {
                            player.stop();
                        }
                        itemClickListener.onListItemClick(steps, steps.get(selectedIndex).getId() - 1, recipeName);
                    } else {
                        Toast.makeText(getActivity(), "You alreay are in the First step of the recipe", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int lastIndex = steps.size() - 1;
                    if (steps.get(selectedIndex).getId() < steps.get(lastIndex).getId()) {
                        if (player != null) {
                            player.stop();
                        }
                        itemClickListener.onListItemClick(steps, steps.get(selectedIndex).getId() + 1, recipeName);
                    } else {
                        Toast.makeText(getContext(), "You already are in the Last step of the recipe", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_STEPS, steps);
        currentState.putInt(SELECTED_INDEX, selectedIndex);
        currentState.putString("Title", recipeName);
        if(player != null) {
            currentPosition = player.getCurrentPosition();
            currentState.putLong("Player_Position", currentPosition);
        }
    }





    @Override
    public void onStart() {
        super.onStart();
        initializePlayer(videoURI);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(player != null && videoURI != null) {
            initializePlayer(videoURI);
            player.seekTo(currentPosition);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    private void initializePlayer(Uri mediaUri) {
        if (player == null) {


            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(handler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            if (getContext() != null) {
                player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
                simpleExoPlayerView.setPlayer(player);
                String userAgent = Util.getUserAgent(getContext(), "Baking App");
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                if (currentPosition != C.TIME_UNSET) player.seekTo(currentPosition);
                player.prepare(mediaSource);
                player.setPlayWhenReady(true);
            }

        }
    }

    private void releasePlayer() {
        if (player != null) {
            currentPosition = player.getCurrentPosition();
            player.stop();
            player.release();
            player = null;
        }
    }


    public boolean isInLandscapeMode( Context context ) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }


}
