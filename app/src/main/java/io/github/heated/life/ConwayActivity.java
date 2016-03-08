package io.github.heated.life;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ConwayActivity extends Activity {
    ConwayView conwayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conway_activity);

        conwayView = (ConwayView) findViewById(R.id.grid);

//        addPlayStopListener();
        addFramerateListener();
        addTorusListener();
    }

    void addFramerateListener() {
        SeekBar bar = (SeekBar) findViewById(R.id.frameRate);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                handleFramerate(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void handleFramerate(int option) {
        int[] options = { 0, 1, 2, 5, 10, 20, 1001 };
        int frameRate = options[option];
        setFramerate(frameRate);
    }

    public void setFramerate(int frameRate) {
        TextView fps = (TextView) findViewById(R.id.frameRateText);
        if (frameRate == 1001) {
            fps.setText("ÜBER");
        } else {
            fps.setText(frameRate + " FPS");
        }
        conwayView.setFramerate(frameRate);
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
