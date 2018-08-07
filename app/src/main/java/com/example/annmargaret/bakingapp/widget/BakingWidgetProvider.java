package com.example.annmargaret.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.annmargaret.bakingapp.R;
import com.example.annmargaret.bakingapp.ui.RecipeDetailActivity;
import static com.example.annmargaret.bakingapp.widget.UpdateBakingService.FROM_ACTIVITY_INGREDIENTS_LIST;

import java.util.ArrayList;

public class BakingWidgetProvider extends AppWidgetProvider {

    static ArrayList<String> ingredientsList = new ArrayList<>();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        BakingWidgetProvider.updateBakingWidgets(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingWidgetProvider.class));
        final String action = intent.getAction();

        if (action != null && intent.getExtras() != null) {
            ArrayList<String> extras = intent.getExtras().getStringArrayList(FROM_ACTIVITY_INGREDIENTS_LIST);
            Log.v("ACTION: ", action);
            Log.v("EXTRAS", String.valueOf(extras));
            if(extras != null) {
                Log.v("ACTION:", "Inside Conditional");
                ingredientsList = intent.getExtras().getStringArrayList(FROM_ACTIVITY_INGREDIENTS_LIST);
                Log.v("Broadcast Recieved: ", String.valueOf(ingredientsList));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
                //update all widgets
                BakingWidgetProvider.updateBakingWidgets(context, appWidgetManager, appWidgetIds);
            }
        }

        super.onReceive(context, intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);

        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        appIntent.addCategory(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent appPendingIntent = PendingIntent.getBroadcast(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //views.setTextViewText(R.id.widget_grid_view_item, String.valueOf(ingredientsList));
        views.setOnClickPendingIntent(R.id.widget_grid_view_item, appPendingIntent);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);


        Intent intent = new Intent(context, GridWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(appWidgetId, R.id.widget_grid_view, intent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
       for(int appWidgetId : appWidgetIds) {
           updateAppWidget(context, appWidgetManager, appWidgetId);
       }
    }

}
