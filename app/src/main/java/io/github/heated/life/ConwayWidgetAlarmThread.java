package io.github.heated.life;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.PowerManager;
import android.widget.RemoteViews;

public class ConwayWidgetAlarmThread extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TO BE CONTINUED");
        wl.acquire();

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.conway_widget);

        update(context, intent, remoteViews);

        ComponentName thisWidget = new ComponentName(context, ConwayWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, remoteViews);

        wl.release();
    }

    public void update(Context context, Intent intent, RemoteViews remoteViews) {
        int width = intent.getIntExtra("width", 10);
        int height = intent.getIntExtra("height", 10);
        boolean[] cells = intent.getBooleanArrayExtra("cells");

        Conway conway = new Conway(width, height, cells);
        conway.nextGeneration();
        Bitmap bitmap = ConwayWidgetView.ToBitmap(context, conway);

        remoteViews.setImageViewBitmap(R.id.imageView, bitmap);

        intent.putExtra("cells", conway.cells);
        PendingIntent.getBroadcast(context, 1337, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
