package io.github.heated.life;

public class ConwayActivityWorkerThread extends ThreadWithFPS {
    Conway conway;

    public ConwayActivityWorkerThread(Conway conway, int fps) {
        super(fps);
        this.conway = conway;
    }

    public void action() {
        conway.nextGeneration();
    }

    public void setConway(Conway conway) {
        this.conway = conway;
    }
}
