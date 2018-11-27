package com.example.android.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.android.bakingapp.model.Cake;

import androidx.annotation.Nullable;

public class RecipeWidgetService extends IntentService {

    public RecipeWidgetService() {
        super("RecipeWidgetService");
    }


    public static void startActionRecipeUpdate(Context context, Cake cake){
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction("com.example.android.bakingapp.action.update");
        intent.putExtra("com.example.android.bakingapp.action.data", cake);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent passed_intent) {
        if(passed_intent != null){
            final String action = passed_intent.getAction();
            if("com.example.android.bakingapp.action.update".equals(action) &&
                    passed_intent.getParcelableExtra("com.example.android.bakingapp.action.data") != null){
                handleActionRecipeUpdate(passed_intent.<Cake>getParcelableExtra("com.example.android.bakingapp.action.data"));
            }
        }
    }

    private void handleActionRecipeUpdate(Cake passed_cake){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        RecipeWidget.updateBakingWidgets(this, appWidgetManager, appWidgetIds, passed_cake);
    }


}

