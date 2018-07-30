package com.jimfo.baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.jimfo.baking_app.model.Ingredient;
import com.jimfo.baking_app.ui.MainActivity;
import com.jimfo.baking_app.util.SharedPreference;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {

        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        String title = pref.getString("title", null);

        SharedPreference sharedPreference = new SharedPreference();
        ArrayList<Ingredient> mIngredients;
        mIngredients = sharedPreference.getIngredients(context);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_ll, pendingIntent);

        String str = "";

        if (mIngredients != null) {
            for (Ingredient ingredient : mIngredients) {
                str += ingredient.getmQuantity() + " " + ingredient.getmMeasure() + " " + ingredient.getmIngredient() + "\n";
            }

            views.setTextViewText(R.id.appwidget_text, str);
            views.setTextViewText(R.id.header_tv, title);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

