package com.codepolitan.s4cataoguemovie.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.codepolitan.s4cataoguemovie.R;
import com.codepolitan.s4cataoguemovie.ui.splashScreen.SplashScreenActivity;

/**
 * Implementation of App Widget functionality.
 */
public class MovieWidget extends AppWidgetProvider {

    public static final String TOAST_ACTION = "com.codepolitan.s4cataoguemovie.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.codepolitan.s4cataoguemovie.EXTRA_ITEM";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.i(MovieWidget.class.getSimpleName(), "onUpdate()");

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.movie_widget);

            Intent serviceIntent = new Intent(context, MovieWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            remoteViews.setRemoteAdapter(appWidgetId, R.id.stack_view, serviceIntent);
            remoteViews.setEmptyView(R.id.stack_view, R.id.empty_view);

            Intent viewIntent = new Intent(context, SplashScreenActivity.class);
            viewIntent.setAction(MovieWidget.TOAST_ACTION);
            viewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            viewIntent.setData(Uri.parse(viewIntent.toUri(Intent.URI_INTENT_SCHEME)));

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, viewIntent, 0);
            remoteViews.setPendingIntentTemplate(R.id.stack_view, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.i(MovieWidget.class.getSimpleName(), "onEnabled()");
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(MovieWidget.class.getSimpleName(), "onDisabled()");
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(MovieWidget.class.getSimpleName(), "onReceive()");
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + String.valueOf(viewIndex), Toast.LENGTH_SHORT).show();
        }

        super.onReceive(context, intent);

    }
}

