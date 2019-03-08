package com.gemanepa.framingham;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    Spinner ageSpinner;
    Spinner taSpinner;
    Spinner hdlSpinner;
    Spinner totaldlSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Init float action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //Init Spinners
        ageSpinner = (Spinner) findViewById(R.id.ageinput);
        hdlSpinner = (Spinner) findViewById(R.id.hdlinput);
        totaldlSpinner = (Spinner) findViewById(R.id.totaldlinput);
        taSpinner = (Spinner) findViewById(R.id.tainput);

        //value to be shown in the spinners
        String [] ageRanges = {"30 - 34", "35 - 39", "40 - 44", "45 - 49", "50 - 54", "55 - 59", "60 - 64", "65 - 69", "70 - 74", "75+"};
        String [] hdlRanges = {"< 35.0", "35.0 - 45.9", "46.0 - 49.9", "50.0 - 61.9", "> 62.0"};
        String [] totaldlRanges = {"< 158", "158 - 200", "201 - 239", "240 - 278", "> 278"};
        String [] taRanges = {"< 120", "120 - 129", "130 - 139", "140 - 149", "150 - 159", "160+"};

        //array adapters used to bind values in the spinners
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ageRanges);
        ArrayAdapter<String> hdlAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hdlRanges);
        ArrayAdapter<String> totaldlAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, totaldlRanges);
        ArrayAdapter<String> taAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, taRanges);

        ageSpinner.setAdapter(ageAdapter);
        hdlSpinner.setAdapter(hdlAdapter);
        totaldlSpinner.setAdapter(totaldlAdapter);
        taSpinner.setAdapter(taAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void genderSwitch(View view) {
        Button genderButton = this.<Button>findViewById(R.id.genderinput);
        String genderButtonCurrentState = genderButton.getText().toString();
        //Log.d("Genre Before Conditional", genderButtonCurrentState);
        if(genderButtonCurrentState.equals("Hombre")) {
            genderButton.setText("Mujer");
            // Log.d("Genre First Conditional", genderButtonCurrentState);
        }
        else if(genderButtonCurrentState.equals("Mujer")) {
            genderButton.setText("Hombre");
            // Log.d("Genre Second Conditional", genderButtonCurrentState);
        }
    }

    public void smokingSwitch(View view) {
        Button smokerButton = this.<Button>findViewById(R.id.smokerinput);
        String smokerButtonCurrentState = smokerButton.getText().toString();
        //Log.d("Smoking PreConditional", genderButtonCurrentState);
        if(smokerButtonCurrentState.equals("No")) {
            smokerButton.setText("Si");
            //Log.d("Smoking 1st Conditional", genderButtonCurrentState);
        }
        else if(smokerButtonCurrentState.equals("Si")) {
            smokerButton.setText("No");
            //Log.d("Smoking 2nd Conditional", genderButtonCurrentState);
        }
    }

    public void treatmentSwitch(View view) {
        Button treatmentButton = this.<Button>findViewById(R.id.treatmentinput);
        String treatmentButtonCurrentState = treatmentButton.getText().toString();
        //Log.d("Treatment PreCond", treatmentButtonCurrentState);
        if(treatmentButtonCurrentState.equals("No")) {
            treatmentButton.setText("Si");
            //Log.d("Treatment 1st Cond", treatmentButtonCurrentState);
        }
        else if(treatmentButtonCurrentState.equals("Si")) {
            treatmentButton.setText("No");
            //Log.d("Treatment 2nd Cond", treatmentButtonCurrentState);
        }
    }

}
