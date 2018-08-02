package com.jimfo.baking_app.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.jimfo.baking_app.BakingAppWidget;
import com.jimfo.baking_app.R;
import com.jimfo.baking_app.RecipeTask;
import com.jimfo.baking_app.adapter.RecipeAdapter;
import com.jimfo.baking_app.model.Ingredient;
import com.jimfo.baking_app.model.Recipe;
import com.jimfo.baking_app.util.NetworkUtils;
import com.jimfo.baking_app.util.SharedPreference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeTask.PostExecuteListener,
        RecipeAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TITLE = "title";
    private RecyclerView mRecyclerView;
    private ArrayList<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.activityBackground));

        mRecyclerView = findViewById(R.id.recipe_list);
        TextView emptyTv = findViewById(R.id.empty_view);

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.activityBackground));

            if (bar != null) {
                bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.activityBackground)));
            }
        }

        if (NetworkUtils.isNetworkAvailable(this.getApplicationContext())) {
            new RecipeTask(this, this).execute();
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyTv.setVisibility(View.GONE);
        } else {
            emptyTv.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemClickListener(int itemId) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.baking_app_widget);
        ComponentName thisWidget = new ComponentName(this, BakingAppWidget.class);
        ArrayList<Ingredient> ingredients = new ArrayList<>(mRecipes.get(itemId).getmIngredients());

        remoteViews.setTextViewText(R.id.header_tv, mRecipes.get(itemId).getmName());

        Log.i(TAG, mRecipes.get(itemId).getmName());

        remoteViews.setTextViewText(R.id.appwidget_text, buildIngredientString(ingredients));

        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

        Recipe recipe = mRecipes.get(itemId);
        Intent i = new Intent(this, RecipeDetail.class);
        i.putExtra(getResources().getString(R.string.selected), recipe);
        startActivity(i);
    }

    private String buildIngredientString(ArrayList<Ingredient> ingredients) {
        StringBuilder strBuilder = new StringBuilder();

        for (Ingredient ingredient : ingredients) {
            strBuilder.append(ingredient.getmQuantity());
            strBuilder.append(" ");
            strBuilder.append(ingredient.getmMeasure());
            strBuilder.append(" ");
            strBuilder.append(ingredient.getmIngredient());
            strBuilder.append("\n");
        }

        return strBuilder.toString();
    }

    private void saveToSharedPreferences(int i) {

        SharedPreferences title = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = title.edit();
        editor.putString(TITLE, mRecipes.get(i).getmName());
        editor.apply();

        SharedPreference sharedPreference = new SharedPreference();
        sharedPreference.saveIngredients(this, new ArrayList<>(mRecipes.get(i).getmIngredients()));
    }

    @Override
    public void onPostExecute(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        RecipeAdapter mAdapter = new RecipeAdapter(this, this, recipes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        saveToSharedPreferences(0);
    }
}
