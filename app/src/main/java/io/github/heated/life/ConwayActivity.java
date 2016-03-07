package io.github.heated.life;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class ConwayActivity extends Activity {
    ConwayView conwayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conway_activity);

        conwayView = (ConwayView) findViewById(R.id.grid);

        addPlayStopListener();
        addTorusListener();
    }

    void addPlayStopListener() {
        ToggleButton toggle = (ToggleButton) findViewById(R.id.playStop);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    conwayView.startThread();
                } else {
                    conwayView.stopThread();
                }
            }
        });
    }

    void addTorusListener() {
        ToggleButton toggle = (ToggleButton) findViewById(R.id.torus);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                conwayView.pause();
                conwayView.conway.toggleTorus();
                conwayView.unpause();
            }
        });
    }

    public void randomizeCells(View view) {
        conwayView.pause();
        conwayView.conway.randomizeCells();
        conwayView.unpause();
    }

    public void clearGrid(View view) {
        conwayView.pause();
        conwayView.conway.clearGrid();
        conwayView.unpause();
    }

    public void setGridToGlider(View view) {
        conwayView.pause();
        conwayView.conway.setGridToGlider();
        conwayView.unpause();
    }

    public void step(View view) {
        conwayView.pause();
        conwayView.conway.nextGeneration();
        conwayView.unpause();
    }
}
