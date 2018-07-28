package com.jimfo.baking_app;

import android.os.AsyncTask;

import com.jimfo.baking_app.model.Recipe;
import com.jimfo.baking_app.ui.MainActivity;
import com.jimfo.baking_app.util.NetworkUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RecipeTask extends AsyncTask<String, Void, ArrayList<Recipe>> {

    private final WeakReference<MainActivity> myRef;

    private PostExecuteListener mPostExecuteListener;

    public interface PostExecuteListener {
        void onPostExecute(ArrayList<Recipe> movies);
    }

    public RecipeTask(MainActivity activity, PostExecuteListener pel) {

        myRef = new WeakReference<>(activity);
        this.mPostExecuteListener = pel;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Recipe> doInBackground(String... args) {

        ArrayList<Recipe> recipes;
        recipes = NetworkUtils.fetchRecipeData(myRef.get());

        return recipes;
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> result) {
        super.onPostExecute(result);

        mPostExecuteListener.onPostExecute(result);
    }
}
