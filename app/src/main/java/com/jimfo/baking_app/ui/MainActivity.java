package com.jimfo.baking_app.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jimfo.baking_app.R;
import com.jimfo.baking_app.adapter.RecipeAdapter;
import com.jimfo.baking_app.RecipeTask;
import com.jimfo.baking_app.model.Recipe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeTask.PostExecuteListener,
        RecipeAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;
    private ArrayList<Recipe> mRecipes;
    private RecipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.activityBackground));

        mRecyclerView = findViewById(R.id.recipe_list);

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.activityBackground));

            if (bar != null) {
                bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.activityBackground)));
            }
        }

        new RecipeTask(this, this).execute();
    }

    @Override
    public void onItemClickListener(int itemId) {
        Recipe recipe = mRecipes.get(itemId);
        Intent i = new Intent(this, RecipeDetail.class);
        i.putExtra(getResources().getString(R.string.selected), recipe);
        startActivity(i);
    }

    @Override
    public void onPostExecute(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        mAdapter = new RecipeAdapter(this, this, recipes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }
}
