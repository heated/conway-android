package io.github.heated.life;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class ConwayActivity extends AppCompatActivity {
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
                conwayView.waitATick();
                conwayView.conway.toggleTorus();
                conwayView.alrightyWereGood();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conway, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void randomizeGrid(View view) {
        conwayView.waitATick();
        conwayView.conway.randomizeGrid();
        conwayView.alrightyWereGood();
    }

    public void clearGrid(View view) {
        conwayView.waitATick();
        conwayView.conway.clearGrid();
        conwayView.alrightyWereGood();
    }

    public void setGridToGlider(View view) {
        conwayView.waitATick();
        conwayView.conway.setGridToGlider();
        conwayView.alrightyWereGood();
    }

    public void step(View view) {
        conwayView.waitATick();
        conwayView.conway.nextGeneration();
        conwayView.alrightyWereGood();
    }
}
