package com.yosri.mustafa.eng.bakingapp.Widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.yosri.mustafa.eng.baking.R;
import com.yosri.mustafa.eng.bakingapp.Service.StackWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static final String TOAST_ACTION = "com.yosri.mustafa.eng.bakingapp.Widget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.yosri.mustafa.eng.bakingapp.Widget.EXTRA_ITEM";
    private SharedPreferences pref;

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        Log.e("test","updateAppWidget ");



       for (int i = 0; i < appWidgetIds.length; i++) {
            Intent svcIntent = new Intent(context, StackWidgetService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
//            svcIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            views.setRemoteAdapter(appWidgetIds[i], R.id.stack_view,
                    svcIntent);
//            views.setTextViewText(R.id.name,"Recipe");
//            views.setEmptyView(R.id.stack_view, R.id.empty_view);

            Intent toastIntent = new Intent(context, RecipeWidgetProvider.class);
            // Set the action for the intent.
            // When the user touches a particular view, it will have the effect of
            // broadcasting TOAST_ACTION.
            toastIntent.setAction(RecipeWidgetProvider.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.empty_view, toastPendingIntent);

//            Intent intent = new Intent(context,MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
//
//            views.setPendingIntentTemplate(R.id.empty_view, pendingIntent);


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetIds, views);
        }
       super.onUpdate(context, appWidgetManager, appWidgetIds);

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetIds);
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

