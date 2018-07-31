package com.jimfo.baking_app.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jimfo.baking_app.model.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {

    // http://androidopentutorials.com/android-how-to-store-list-of-values-in-sharedpreferences/

    public static final String PREFS_NAME = "Ingredient_List";
    public static final String INGREDIENTS = "Ingredients";

    public SharedPreference() {
        super();
    }

    public void saveIngredients(Context context, List<Ingredient> ingredients) {

        SharedPreferences ingredientList;
        SharedPreferences.Editor editor;

        ingredientList = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        editor = ingredientList.edit();

        Gson gson = new Gson();
        String jsonIngredients = gson.toJson(ingredients);

        editor.putString(INGREDIENTS, jsonIngredients);

        editor.apply();
    }

    public ArrayList<Ingredient> getIngredients(Context context) {

        SharedPreferences settings;
        List<Ingredient> ingredients;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE
        );

        if (settings.contains(INGREDIENTS)) {

            String jsonFavorites = settings.getString(INGREDIENTS, null);
            Gson gson = new Gson();

            Ingredient[] ingredientItems = gson.fromJson(jsonFavorites,
                    Ingredient[].class
            );

            ingredients = Arrays.asList(ingredientItems);
            ingredients = new ArrayList<>(ingredients);
        } else {
            return null;
        }

        return (ArrayList<Ingredient>) ingredients;
    }
}
