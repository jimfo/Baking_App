package com.jimfo.baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jimfo.baking_app.model.Ingredient;
import com.jimfo.baking_app.ui.MainActivity;
import com.jimfo.baking_app.ui.RecipeDetail;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {

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

    private static RemoteViews getBakingAppRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        Intent intent = new Intent(context, RecipeDetail.class);
        views.setRemoteAdapter(R.id.appwidget_text, intent);

        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, RecipeDetail.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.appwidget_text, appPendingIntent);
        // Handle empty gardens
        views.setEmptyView(R.id.appwidget_ll, R.id.appwidget_text);
        return views;
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

