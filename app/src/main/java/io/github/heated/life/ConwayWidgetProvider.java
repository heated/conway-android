package io.github.heated.life;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

public class ConwayWidgetProvider extends AppWidgetProvider {
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ConwayWidgetAlarmThread.class);
        PendingIntent sender = getBroadcast(context, intent, true);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.conway_widget);
        newGame(context, remoteViews, 10, 10);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, sender);
    }

    public void newGame(Context context, RemoteViews remoteViews, int width, int height) {
        Conway conway = Conway.Random(width, height);
        Bitmap bitmap = ConwayWidgetView.ToBitmap(context, conway);
        remoteViews.setImageViewBitmap(R.id.imageView, bitmap);

        Intent intent = new Intent(context, ConwayWidgetAlarmThread.class);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.putExtra("cells", conway.cells);

        getBroadcast(context, intent, true);
    }

    @Override
    public void onDisabled(Context context) {
        Intent intent = new Intent(context, ConwayWidgetAlarmThread.class);
        PendingIntent sender = getBroadcast(context, intent, false);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

        super.onDisabled(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
            newGame(context, appWidgetManager, appWidgetId, options);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.conway_widget);

            // Register an onClickListener
            Intent intent = new Intent(context, ConwayWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = getBroadcast(context, intent, true);
            remoteViews.setOnClickPendingIntent(R.id.imageView, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        newGame(context, appWidgetManager, appWidgetId, newOptions);
    }

    public void newGame(Context context, AppWidgetManager appWidgetManager,
                        int appWidgetId, Bundle options) {
        if (options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) > 0) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.conway_widget);
            int minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            int maxHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
            int cellSize = ConwayWidgetView.CellSize;
            newGame(context, remoteViews,
                    minWidth  / cellSize,
                    maxHeight / cellSize);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    PendingIntent getBroadcast(Context context, Intent intent, boolean updateCurrent) {
        return PendingIntent.getBroadcast(context, 1337, intent, updateCurrent ? PendingIntent.FLAG_UPDATE_CURRENT : 0);
    }
}
