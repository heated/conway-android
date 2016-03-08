package io.github.heated.life;

public class ConwayActivityThread extends Thread {
    public boolean running = true;
    ConwayView conwayView;
    int sleepTicks;
    long startTime, waitUntil;

    public ConwayActivityThread(ConwayView conwayView, int fps) {
        super();
        this.conwayView = conwayView;
        startTime = System.currentTimeMillis();
        setFPS(fps);
        waitUntil = startTime + sleepTicks;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setFPS(int fps) {
        running = fps != 0;

        if (running) {
            sleepTicks = 1000 / fps;
        }

        waitUntil = Math.min(waitUntil, System.currentTimeMillis() + sleepTicks);
    }

    @Override
    public void run() {
        while (running) {
            if (System.currentTimeMillis() >= waitUntil) {
                waitUntil += sleepTicks;

                conwayView.conway.nextGeneration();
                conwayView.render();
            }
        }
    }
}
