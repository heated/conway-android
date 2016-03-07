package io.github.heated.life;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ConwayWidgetView extends View {
    int cellSize = 5;
    Conway conway;

    public ConwayWidgetView(Context context, Conway conway) {
        super(context);
        this.conway = conway;

        int w = conway.gridWidth * cellSize;
        int h = conway.gridHeight * cellSize;
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

        for (int x = 0; x < conway.gridWidth; x++) {
            for (int y = 0; y < conway.gridHeight; y++) {
                drawCell(canvas, x, y, cell);
            }
        }
    }

    void drawCell(Canvas canvas, int cellX, int cellY, Paint cell) {
        int x = cellX * cellSize;
        int y = cellY * cellSize;

        if (conway.grid[cellX][cellY]) {
            canvas.drawRect(x, y, x + cellSize, y + cellSize, cell);
        }
    }
}
