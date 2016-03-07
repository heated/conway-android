package io.github.heated.life;

public class ConwayActivityThread extends Thread {
    public boolean running = true;
    ConwayView conwayView;

    public ConwayActivityThread(ConwayView conwayView) {
        super();
        this.conwayView = conwayView;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {
            conwayView.conway.nextGeneration();
            conwayView.render();
        }
    }
}
