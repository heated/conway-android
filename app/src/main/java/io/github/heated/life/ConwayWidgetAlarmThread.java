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

        ComponentName thiswidget = new ComponentName(context, ConwayWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thiswidget, remoteViews);

        wl.release();
    }

    public void update(Context context, Intent intent, RemoteViews remoteViews) {
        int width = intent.getIntExtra("width", 10);
        int height = intent.getIntExtra("height", 10);
        boolean[] grid = intent.getBooleanArrayExtra("grid");

        Conway conway = new Conway(width, height, grid);
        conway.nextGeneration();

        ConwayWidgetView view = new ConwayWidgetView(context, conway);
        int size = view.cellSize;

        Bitmap bitmap = Bitmap.createBitmap(width * size, height * size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        remoteViews.setImageViewBitmap(R.id.imageView, bitmap);

        intent.putExtra("grid", conway.serializeGrid());
        PendingIntent.getBroadcast(context, 1337, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
