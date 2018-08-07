package com.example.annmargaret.bakingapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class UpdateBakingService extends IntentService {

    public static String FROM_ACTIVITY_INGREDIENTS_LIST = "FROM_ACTIVITY_INGREDIENTS_LIST";

    public UpdateBakingService() {
        super("UpdateBakingService");
    }

    public static void startBakingService(Context context, ArrayList<String> fromActivityIngredientsList) {
        Intent intent = new Intent(context.getApplicationContext(), UpdateBakingService.class);
        intent.putExtra(FROM_ACTIVITY_INGREDIENTS_LIST, fromActivityIngredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent.getExtras() != null) {
            ArrayList<String> fromActivityIngredientList = intent.getExtras().getStringArrayList(FROM_ACTIVITY_INGREDIENTS_LIST);
            Log.v("Handle Intent: ", String.valueOf(fromActivityIngredientList));
            handleActionUpdateBakingWigdets(fromActivityIngredientList);
        }
    }

    private void handleActionUpdateBakingWigdets(ArrayList<String> fromActivityIngredientsList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra(FROM_ACTIVITY_INGREDIENTS_LIST, fromActivityIngredientsList);
        if(fromActivityIngredientsList != null) {
            sendBroadcast(intent);
        }
    }
}
