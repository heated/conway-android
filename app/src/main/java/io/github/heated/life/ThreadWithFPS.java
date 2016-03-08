package io.github.heated.life;

public class ThreadWithFPS extends Thread {
    public boolean running = true;
    int sleepTicks;
    long startTime, waitUntil;

    public ThreadWithFPS(int fps) {
        super();
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
            if (System.currentTimeMillis() >= waitUntil && extraConditions()) {
                waitUntil += sleepTicks;
                action();

//                try {
//                    wait(System.currentTimeMillis() - waitUntil);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    boolean extraConditions() {
        return true;
    }

    void action() {

    }
}
