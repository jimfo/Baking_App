package com.jimfo.baking_app.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.jimfo.baking_app.R;
import com.jimfo.baking_app.model.Ingredient;
import com.jimfo.baking_app.model.Recipe;
import com.jimfo.baking_app.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    private JsonUtils() {
    }

    public static ArrayList<Recipe> extractRecipeData(Context context, String response) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(response)) {
            return null;
        }

        ArrayList<Recipe> recipes = new ArrayList<>();
        Recipe recipe;

        String id;
        String name;
        String ingredientResponse;
        String stepResponse;
        ArrayList<Ingredient> ingredients;
        ArrayList<Step> steps;
        String servings;

        // Try to parse the JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Parse the response given by the JSON_RESPONSE string and
            // build up a list of Film objects with the corresponding data.
            JSONArray recipeArray = new JSONArray(response);

            for (int i = 0; i < recipeArray.length(); i++) {

                JSONObject jsonObject = recipeArray.getJSONObject(i);

                id = jsonObject.optString(context.getString(R.string.id), context.getString(R.string.dna));
                name = jsonObject.optString(context.getString(R.string.name), context.getString(R.string.dna));

                ingredientResponse = jsonObject.optString(context.getString(R.string.ingredients),context.getString(R.string.dna));
                ingredients = extractIngredientData(context, ingredientResponse);

                stepResponse = jsonObject.optString(context.getString(R.string.steps),context.getString(R.string.dna));
                steps = extractStepData(context, stepResponse);

                servings = jsonObject.optString(context.getString(R.string.servings), context.getString(R.string.dna));

                recipe = new Recipe(id, name, ingredients, steps, servings);
                recipes.add(recipe);
            }
        }
        catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(TAG, context.getString(R.string.parseError), e);
        }

        return recipes;
    }

    private static ArrayList<Ingredient> extractIngredientData(Context context, String response) {

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient;

        String qty;
        String measure;
        String item;

        JSONArray jsonArray;

        try {
            jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                qty = jsonObject.optString(context.getString(R.string.quantity), context.getString(R.string.dna));
                measure = jsonObject.optString(context.getString(R.string.measure), context.getString(R.string.dna));
                item = capitaizeEachWord(jsonObject.optString(context.getString(R.string.ingredient), context.getString(R.string.dna)));

                ingredient = new Ingredient(qty, measure, item);
                ingredients.add(ingredient);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    private static String capitaizeEachWord(String str) {

        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();

        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }

        return builder.toString();
    }

    private static ArrayList<Step> extractStepData(Context context, String response) {


        ArrayList<Step> steps = new ArrayList<>();
        Step step;
        String id;
        String shortDescription;
        String description;
        String videoUrl;
        String thumbnailUrl;

        JSONArray jsonArray;
        try {

            jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                id = jsonObject.optString(context.getString(R.string.id), context.getString(R.string.dna));
                shortDescription = jsonObject.optString(context.getString(R.string.shortDescription), context.getString(R.string.dna));
                description = jsonObject.optString(context.getString(R.string.description), context.getString(R.string.dna));
                videoUrl = jsonObject.optString(context.getString(R.string.videoURL), context.getString(R.string.dna));
                thumbnailUrl = jsonObject.optString(context.getString(R.string.thumbnailURL), context.getString(R.string.dna));

                step = new Step(id, shortDescription, description, videoUrl, thumbnailUrl);
                steps.add(step);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return steps;
    }
}
