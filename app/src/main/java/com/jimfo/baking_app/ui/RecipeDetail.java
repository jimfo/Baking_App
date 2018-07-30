package com.jimfo.baking_app.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.jimfo.baking_app.R;
import com.jimfo.baking_app.adapter.IngredientAdapter;
import com.jimfo.baking_app.adapter.StepAdapter;
import com.jimfo.baking_app.model.Ingredient;
import com.jimfo.baking_app.model.Recipe;
import com.jimfo.baking_app.model.Step;
import com.jimfo.baking_app.util.ExoPlayerUtils;

import java.util.ArrayList;

public class RecipeDetail extends AppCompatActivity implements StepAdapter.ItemClickListener {

    private static final String STEPS = "steps";
    private static final String INGREDIENTS = " ingredients";
    private static final String RECIPE = "recipe";
    private Recipe mRecipe;
    private String mTitle;
    private RecyclerView mIngredientRV;
    private RecyclerView mStepRV;
    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> mIngredients;
    private TextView stepDescription;
    private ExoPlayerUtils mPlayerUtils;
    private long mPosition;
    private Uri mVideoPath = null;
    private SimpleExoPlayerView mPlayerView;
    private boolean mTwoPane;
    private ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mStepRV = findViewById(R.id.step_rv);
        mIngredientRV = findViewById(R.id.ingredient_rv);
        mPlayerView = findViewById(R.id.playerView);
        mPlayerUtils = new ExoPlayerUtils(this, mPlayerView);

        bar = getSupportActionBar();

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.activityBackground));

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.activityBackground));

            if (bar != null) {
                bar.setDisplayHomeAsUpEnabled(true);
                bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.activityBackground)));
            }
        }

        if(savedInstanceState != null){
            mRecipe = savedInstanceState.getParcelable(RECIPE);
            if (mRecipe != null) {
                mSteps = new ArrayList<>(mRecipe.getmSteps());
                mIngredients = new ArrayList<>(mRecipe.getmIngredients());
                mTitle = mRecipe.getmName();
            }
        }
        else {
            Intent i = getIntent();
            Bundle extras = i.getExtras();

            if (extras != null && extras.containsKey(this.getResources().getString(R.string.selected))) {
                mRecipe = extras.getParcelable(this.getResources().getString(R.string.selected));

                if (mRecipe != null) {
                    mTitle = mRecipe.getmName();
                    mSteps = new ArrayList<>(mRecipe.getmSteps());
                    mIngredients = new ArrayList<>(mRecipe.getmIngredients());
                }
            }
        }

        if(findViewById(R.id.recipe_detail_ll) != null && isLandscape()){
            mTwoPane = true;
            stepDescription = findViewById(R.id.step_description_tv);
            stepDescription.setText(mSteps.get(0).getmDescription());
        }
        else if(findViewById(R.id.recipe_detail_ll) != null && !isLandscape()){
            mTwoPane = false;
            setupPortraitMode();

        }
        setTitle(mTitle);
        setUpAdapters(mSteps, mIngredients);
    }

    public void setupPortraitMode(){
        LinearLayout exoplayerLL = findViewById(R.id.exoplayer_ll);
        ScrollView scrollView = findViewById(R.id.scrollview);

        scrollView.getLayoutParams().width = WindowManager.LayoutParams.MATCH_PARENT;
        View separator = findViewById(R.id.separator);
        exoplayerLL.setVisibility(View.GONE);
        separator.setVisibility(View.GONE);
    }

    public void setUpAdapters(ArrayList<Step> steps, ArrayList<Ingredient> ingredients){

        IngredientAdapter ingredientAdapter = new IngredientAdapter(getApplicationContext(), ingredients);
        mIngredientRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mIngredientRV.setAdapter(ingredientAdapter);

        StepAdapter stepAdapter = new StepAdapter(this, this, steps);
        mStepRV.setLayoutManager(new LinearLayoutManager(this));
        mStepRV.setAdapter(stepAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(RECIPE, mRecipe);
        outState.putParcelableArrayList(STEPS, mSteps);
        outState.putParcelableArrayList(INGREDIENTS, mIngredients);
    }

    private boolean isLandscape(){

        int orientation = getResources().getConfiguration().orientation;

        return (orientation == Configuration.ORIENTATION_LANDSCAPE);
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

    @Override
    public void onItemClickListener(int itemId) {

        Step step = mSteps.get(itemId);
        mVideoPath = Uri.parse(step.getmVideoUrl());

        if (mTwoPane){
            mPlayerUtils = new ExoPlayerUtils(this, mPlayerView);
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.no_video));

            mPlayerUtils.initializeMediaSession();
            mPlayerUtils.initializePlayer(mVideoPath, mPosition);
            bar.setSubtitle(step.getmShortDescription());
            stepDescription.setText(step.getmDescription());
        }
        else {

            Intent i = new Intent(this, StepActivity.class);
            i.putExtra(getResources().getString(R.string.stepKey), step);
            i.putExtra(getResources().getString(R.string.titleKey), mTitle);
            startActivity(i);
        }
    }

}
