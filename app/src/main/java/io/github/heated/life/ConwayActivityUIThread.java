package io.github.heated.life;

public class ConwayActivityUIThread extends ThreadWithFPS {
    ConwayView conwayView;
    int renderings;

    public ConwayActivityUIThread(ConwayView conwayView, int fps) {
        super(fps);
        this.conwayView = conwayView;
    }

    void action() {
        conwayView.render();
        renderings++;
    }

    boolean extraConditions() {
        return renderings < conwayView.conway.generations;
    }
}
