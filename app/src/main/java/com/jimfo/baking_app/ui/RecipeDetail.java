package com.jimfo.baking_app.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jimfo.baking_app.R;
import com.jimfo.baking_app.adapter.DetailAdapter;
import com.jimfo.baking_app.adapter.IngredientAdapter;
import com.jimfo.baking_app.adapter.RecipeAdapter;
import com.jimfo.baking_app.adapter.StepAdapter;
import com.jimfo.baking_app.model.Ingredient;
import com.jimfo.baking_app.model.Recipe;
import com.jimfo.baking_app.model.Step;

import java.util.ArrayList;

public class RecipeDetail extends AppCompatActivity implements StepAdapter.ItemClickListener {

    private static final String STEPS = "steps";
    private static final String INGREDIENTS = " ingredients";
    private static final String RECIPE = "recipe";
    private Recipe mRecipe;
    private String mTitle;
    private StepAdapter mStepAdapter;
    private IngredientAdapter mIngredientAdapter;
    private RecyclerView mIngredientRV;
    private RecyclerView mStepRV;
    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> mIngredients;
    private LinearLayout detailLL;
    private TextView ingredientTv;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mStepRV = findViewById(R.id.step_rv);
        mIngredientRV = findViewById(R.id.ingredient_rv);
        detailLL = findViewById(R.id.detail_ll);

        ActionBar bar = getSupportActionBar();

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

            }
        }
        else {
            Intent i = getIntent();
            Bundle extras = i.getExtras();

            if (extras != null && extras.containsKey(this.getResources().getString(R.string.selected))) {
                mRecipe = extras.getParcelable(this.getResources().getString(R.string.selected));

                if (mRecipe != null) {
                    mTitle = mRecipe.getmName();
                    setTitle(mTitle);

                    mSteps = new ArrayList<>(mRecipe.getmSteps());
                    mIngredients = new ArrayList<>(mRecipe.getmIngredients());


                    //displayIngredients(new ArrayList<>(mRecipe.getmIngredients()));
                }
            }
        }



//        if(findViewById(R.id.recipe_detail_ll) != null && checkOrientation()){
//            mTwoPane = true;
//            mStepAdapter = new StepAdapter(this, this, new ArrayList<>(mRecipe.getmSteps()));
//            mStepRV.setLayoutManager(new LinearLayoutManager(this));
//            mStepRV.setAdapter(mStepAdapter);
//            displayIngredients(new ArrayList<>(mRecipe.getmIngredients()));
//            mSteps = new ArrayList<>(mRecipe.getmSteps());
//        }
        setUpAdapters(mSteps, mIngredients);
    }

    public void setUpAdapters(ArrayList<Step> steps, ArrayList<Ingredient> ingredients){
        mIngredientAdapter = new IngredientAdapter(getApplicationContext(), ingredients);
        mIngredientRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mIngredientRV.setAdapter(mIngredientAdapter);

        mStepAdapter = new StepAdapter(this, this, steps);
        mStepRV.setLayoutManager(new LinearLayoutManager(this));
        mStepRV.setAdapter(mStepAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(RECIPE, mRecipe);
        outState.putParcelableArrayList(STEPS, mSteps);
        outState.putParcelableArrayList(INGREDIENTS, mIngredients);

    }

    private boolean checkOrientation(){

        int orientation = getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            return true;
        }
        else{
            return false;
        }
    }

//    public void displayIngredients(ArrayList<Ingredient> ingredients){
//
//        ingredientTv.setText("");
//
//        for(Ingredient ingredient : ingredients){
//            ingredientTv.append(ingredient.getmQuantity() + " ");
//            ingredientTv.append(ingredient.getmMeasure() + " ");
//            ingredientTv.append(ingredient.getmIngredient() + "\n");
//        }
//    }

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
        Intent i = new Intent(this, StepActivity.class);
        i.putExtra(getResources().getString(R.string.stepKey), step);
        i.putExtra(getResources().getString(R.string.titleKey), mTitle);
        startActivity(i);
    }
}
