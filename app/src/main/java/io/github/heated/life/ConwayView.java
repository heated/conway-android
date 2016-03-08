package io.github.heated.life;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class ConwayView extends SurfaceView implements SurfaceHolder.Callback {
    int cellSize = (int) (15 * getResources().getDisplayMetrics().density);
    boolean firstDraw = true;
    ConwayActivityUIThread uiThread;
    ConwayActivityWorkerThread workerThread;
    SurfaceHolder holder;
    boolean brb = true;
    Conway conway;
    boolean drawing = false;
    float mPreviousX;
    float mPreviousY;
    boolean drawLiving;
    int frameRate = 1;
    private TextView generationText;

    public ConwayView(Context context) {
        super(context);
        init(null, 0);
    }

    public ConwayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ConwayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ConwayView, defStyle, 0);
        a.recycle();

        holder = getHolder();
        holder.addCallback(this);
    }

    void initGrid() {
        int gridWidth = getWidth() / cellSize;
        int gridHeight = getHeight() / cellSize;
        conway = Conway.Random(gridWidth, gridHeight);
        System.out.println(conway);
    }

    void drawTo(Canvas canvas) {
        if (firstDraw) {
            firstDraw = false;
            initGrid();
            workerThread.setConway(conway);
        }

        drawBackground(canvas);
        drawCells(canvas);
    }

    public void render() {
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            drawTo(canvas);
            holder.unlockCanvasAndPost(canvas);
        }

        generationText.post(new Runnable() {
            public void run() {
                generationText.setText(conway.generations + " Generations");
            }
        });
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
        int x = cellX * cellSize;
        int y = cellY * cellSize;

        if (conway.cell(cellX, cellY)) {
            canvas.drawRect(x, y, x + cellSize, y + cellSize, cell);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (conway == null) {
            initGrid();
        }

        unpause();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pause();
    }

    public void startThread() {
        uiThread = new ConwayActivityUIThread(this, frameRate);
        uiThread.setRunning(true);
        uiThread.start();
        workerThread = new ConwayActivityWorkerThread(conway, frameRate);
        workerThread.setRunning(true);
        workerThread.start();
    }

    public void stopThread() {
        if (uiThread == null) {
            return;
        }

        uiThread.setRunning(false);
        workerThread.setRunning(false);

        boolean retry = true;
        while (retry) {
            try {
                uiThread.join();
                workerThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    public void pause() {
        brb = uiThread != null && uiThread.running;
        if (brb) {
            stopThread();
        }
    }

    public void unpause() {
        if (brb) {
            brb = false;
            startThread();
        }
        render();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                beginDrawing(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                continueDrawing(x, y);
                break;

            case MotionEvent.ACTION_UP:
                drawing = false;
                break;
        }

        return true;
    }

    private void beginDrawing(float x, float y) {
        drawing = true;
        mPreviousX = x;
        mPreviousY = y;
        int cellX = (int) (x / cellSize);
        int cellY = (int) (y / cellSize);

        pause();

        boolean cellAlive = conway.cell(cellX, cellY);
        drawLiving = !cellAlive;
        drawPoint(x, y);

        unpause();

    }

    private void continueDrawing(float x, float y) {
        if (drawing) {
            float dx = x - mPreviousX;
            float dy = y - mPreviousY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance >= 1) {
                pause();

                for (int i = 1; i <= distance; i++) {
                    double newX = mPreviousX + i * dx / distance;
                    double newY = mPreviousY + i * dy / distance;
                    drawPoint(newX, newY);
                }

                mPreviousX = x;
                mPreviousY = y;

                unpause();
            }
        }
    }

    void drawPoint(double x, double y) {
        int cellX = (int) (x / cellSize);
        int cellY = (int) (y / cellSize);
        conway.tryToSet(cellX, cellY, drawLiving);
    }

    public void setFramerate(int frameRate) {
        int prevFrameRate = this.frameRate;
        this.frameRate = frameRate;

        if (prevFrameRate == 0) {
            startThread();
        }

        if (frameRate == 0) {
            stopThread();
        }

        if (uiThread != null) {
            uiThread.setFPS(frameRate);
            workerThread.setFPS(frameRate);
        }
    }

    public void setGenerationText(TextView generationText) {
        this.generationText = generationText;
    }
}
