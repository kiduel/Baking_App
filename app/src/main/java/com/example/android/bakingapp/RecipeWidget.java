package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.android.bakingapp.model.Cake;
import com.example.android.bakingapp.model.Ingredient;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Cake cake) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.removeAllViews(R.id.single_ingredients_layout);
        views.setTextViewText(R.id.cake_title, cake.getName());
        for(Ingredient ingredient : cake.getIngredients()){
            RemoteViews view = new RemoteViews(context.getPackageName(),
                    R.layout.single_widget_list_item);
            view.setTextViewText(R.id.single_ingredient_item,
                    String.valueOf(ingredient.getQuantity()) +
                            String.valueOf(ingredient.getMeasure()) + " " + ingredient.getIngredient());
            views.addView(R.id.single_ingredients_layout, view);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, Cake cake){
        for(int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId, cake);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

}

