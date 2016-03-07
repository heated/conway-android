package io.github.heated.life;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ConwayWidgetView extends View {
    public static int CellSize = 5;
    Conway conway;

    public ConwayWidgetView(Context context, Conway conway) {
        super(context);
        this.conway = conway;

        int w = conway.width * CellSize;
        int h = conway.height * CellSize;
        measure(w, h);
        layout(0, 0, w, h);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);
        drawCells(canvas);
    }

    void drawBackground(Canvas canvas) {
        Paint background = new Paint();
        background.setColor(Color.BLACK);

        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
    }

    void drawCells(Canvas canvas) {
        Paint cell = new Paint();
        cell.setColor(Color.WHITE);

        for (int x = 0; x < conway.width; x++) {
            for (int y = 0; y < conway.height; y++) {
                drawCell(canvas, x, y, cell);
            }
        }
    }

    void drawCell(Canvas canvas, int cellX, int cellY, Paint cell) {
        if (conway.cell(cellX, cellY)) {
            int x = cellX * CellSize;
            int y = cellY * CellSize;

            canvas.drawRect(x, y, x + CellSize, y + CellSize, cell);
        }
    }

    Bitmap blankBitmap() {
        return Bitmap.createBitmap(
                conway.width  * CellSize,
                conway.height * CellSize,
                Bitmap.Config.ARGB_8888);
    }

    public Bitmap render() {
        Bitmap bitmap = blankBitmap();

        Canvas canvas = new Canvas(bitmap);
        draw(canvas);

        return bitmap;
    }

    public static Bitmap ToBitmap(Context context, Conway conway) {
        ConwayWidgetView view = new ConwayWidgetView(context, conway);
        return view.render();
    }

//    public static int CellSize(Context context) {
//        Conway conway = new Conway(0, 0);
//        ConwayWidgetView view = new ConwayWidgetView(context, conway);
//        return view.CellSize;
//    }
}
