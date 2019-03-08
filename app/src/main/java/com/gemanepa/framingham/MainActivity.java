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
import java.util.function.Consumer;

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
                Button genderButton = findViewById(R.id.genderinput);
                String gender = genderButton.getText().toString();

                int agePoints = calculateAgePoints(gender);
                int hdlPoints = calculateHDLPoints(gender);
                int dlPoints = calculateTotalDLPoints(gender);
                int taPoints = calculateTAPoints(gender);

                int Score = agePoints + hdlPoints + dlPoints + taPoints;
                String ScoreString = Integer.toString(Score);
                Log.d("Score:", ScoreString);

                Snackbar.make(view,"Replace with your own action", Snackbar.LENGTH_LONG)
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

    // AGE POINTS CALCULATION
    public int calculateAgePoints(String gender) {
        ageSpinner = (Spinner) findViewById(R.id.ageinput);
        String ageRangeSelected = ageSpinner.getSelectedItem().toString();

        int agePoints = 0;
        if(gender.equals("Hombre")) {
            agePoints = calculateMenAgePoints(ageRangeSelected);
        }

        else if(gender.equals("Mujer")) {
            agePoints = calculateWomenAgePoints(ageRangeSelected);
        }
        return agePoints;
    }

    private int calculateMenAgePoints(String ageRangeSelected){
        int agePoints = 0;

        switch(ageRangeSelected) {
            case "30 - 34":
                agePoints = 0;
                break;
            case "35 - 39":
                agePoints = 2;
                break;
            case "40 - 44":
                agePoints = 5;
                break;
            case "45 - 49":
                agePoints = 7;
                break;
            case "50 - 54":
                agePoints = 8;
                break;
            case "55 - 59":
                agePoints = 10;
                break;
            case "60 - 64":
                agePoints = 11;
                break;
            case "65 - 69":
                agePoints = 12;
                break;
            case "70 - 74":
                agePoints = 14;
                break;
            case "75+":
                agePoints = 15;
                break;
        }
        return agePoints;
    }

    private int calculateWomenAgePoints(String ageRangeSelected){
        int agePoints = 0;

        switch(ageRangeSelected) {
            case "30 - 34":
                agePoints = 0;
                break;
            case "35 - 39":
                agePoints = 2;
                break;
            case "40 - 44":
                agePoints = 4;
                break;
            case "45 - 49":
                agePoints = 5;
                break;
            case "50 - 54":
                agePoints = 7;
                break;
            case "55 - 59":
                agePoints = 8;
                break;
            case "60 - 64":
                agePoints = 9;
                break;
            case "65 - 69":
                agePoints = 10;
                break;
            case "70 - 74":
                agePoints = 11;
                break;
            case "75+":
                agePoints = 12;
                break;
        }
        return agePoints;
    }

    // HDL POINTS CALCULATION
    public int calculateHDLPoints(String gender) {
        hdlSpinner = (Spinner) findViewById(R.id.hdlinput);
        String hdlRangeSelected = hdlSpinner.getSelectedItem().toString();

        int HDLPoints = 0;

        if(gender.equals("Hombre")) {
            HDLPoints = calculateMenHDLPoints(hdlRangeSelected);
        }

        else if(gender.equals("Mujer")) {
            HDLPoints = calculateWomenHDLPoints(hdlRangeSelected);
        }
        return HDLPoints;
    }

    private int calculateMenHDLPoints(String hdlRangeSelected){
        int hdlPoints = 0;

        switch(hdlRangeSelected) {
            case "< 35.0":
                hdlPoints = 2;
                break;
            case "35.0 - 45.9":
                hdlPoints = 1;
                break;
            case "46.0 - 49.9":
                hdlPoints = 0;
                break;
            case "50.0 - 61.9":
                hdlPoints = -1;
                break;
            case "> 62.0":
                hdlPoints = -2;
                break;
        }
        return hdlPoints;
    }

    private int calculateWomenHDLPoints(String hdlRangeSelected){
        int hdlPoints = 0;

        switch(hdlRangeSelected) {
            case "< 35.0":
                hdlPoints = 2;
                break;
            case "35.0 - 45.9":
                hdlPoints = 1;
                break;
            case "46.0 - 49.9":
                hdlPoints = 0;
                break;
            case "50.0 - 61.9":
                hdlPoints = -1;
                break;
            case "> 62.0":
                hdlPoints = -2;
                break;
        }
        return hdlPoints;
    }

    // TOTAL DL POINTS CALCULATION
    public int calculateTotalDLPoints(String gender) {
        totaldlSpinner = (Spinner) findViewById(R.id.totaldlinput);
        String totaldlRangeSelected = totaldlSpinner.getSelectedItem().toString();

        int totalDLPoints = 0;
        if(gender.equals("Hombre")) {
            totalDLPoints = calculateMenTotalDLPoints(totaldlRangeSelected);
        }

        else if(gender.equals("Mujer")) {
            totalDLPoints = calculateWomenTotalDLPoints(totaldlRangeSelected);
        }
        return totalDLPoints;
    }

    private int calculateMenTotalDLPoints(String totaldlRangeSelected){
        int totaldlPoints = 0;

        switch(totaldlRangeSelected) {
            case "< 158":
                totaldlPoints = 0;
                break;
            case "158 - 200":
                totaldlPoints = 1;
                break;
            case "201 - 239":
                totaldlPoints = 2;
                break;
            case "240 - 278":
                totaldlPoints = 3;
                break;
            case "> 278":
                totaldlPoints = 4;
                break;
        }
        return totaldlPoints;
    }

    private int calculateWomenTotalDLPoints(String totaldlRangeSelected){
        int totaldlPoints = 0;

        switch(totaldlRangeSelected) {
            case "< 158":
                totaldlPoints = 0;
                break;
            case "158 - 200":
                totaldlPoints = 1;
                break;
            case "201 - 239":
                totaldlPoints = 3;
                break;
            case "240 - 278":
                totaldlPoints = 4;
                break;
            case "> 278":
                totaldlPoints = 5;
                break;
        }
        return totaldlPoints;
    }

    // TA POINTS CALCULATION
    public int calculateTAPoints(String gender) {
        taSpinner = (Spinner) findViewById(R.id.tainput);
        String taRangeSelected = taSpinner.getSelectedItem().toString();

        Button treatmentButton = this.<Button>findViewById(R.id.treatmentinput);
        String isOnTreatment = treatmentButton.getText().toString();

        int taPoints = 0;
        if(gender.equals("Hombre")) {
            taPoints = calculateMenTotalTAPoints(taRangeSelected, isOnTreatment);
        }

        else if(gender.equals("Mujer")) {
            taPoints = calculateWomenTotalTAPoints(taRangeSelected, isOnTreatment);
        }
        return taPoints;
    }

    private int calculateMenTotalTAPoints(String taRangeSelected, String isOnTreatment){
        int taPoints = 0;

        switch(taRangeSelected) {
            case "< 120":
                if(isOnTreatment.equals("No")) {
                    taPoints = -2;
                }
                if(isOnTreatment.equals("Si")) {
                    taPoints = 0;
                }
                break;
            case "120 - 129":
                if(isOnTreatment.equals("No")) {
                    taPoints = 0;
                }
                if(isOnTreatment.equals("Si")) {
                    taPoints = 2;
                }
                break;
            case "130 - 139":
                if(isOnTreatment.equals("No")) {
                    taPoints = 1;
                }
                if(isOnTreatment.equals("Si")) {
                    taPoints = 3;
                }
                break;
            case "140 - 149":
                if(isOnTreatment.equals("No")) {
                    taPoints = 2;
                }
                if(isOnTreatment.equals("Si")) {
                    taPoints = 4;
                }
                break;
            case "150 - 159":
                if(isOnTreatment.equals("No")) {
                    taPoints = 2;
                }
                if(isOnTreatment.equals("Si")) {
                    taPoints = 4;
                }
                break;
            case "160+":
                if(isOnTreatment.equals("No")) {
                    taPoints = 3;
                }
                if(isOnTreatment.equals("Si")) {
                    taPoints = 5;
                }
                break;
        }
        return taPoints;
    }

    private int calculateWomenTotalTAPoints(String taRangeSelected, String isOnTreatment){
        int taPoints = 0;

        switch(taRangeSelected) {
            case "< 120":
                if(isOnTreatment.equals("No")) {
                    taPoints = -3;
                }
                if(isOnTreatment.equals("Si")) {
                    taPoints = -1;
                }
                break;
            case "120 - 129":
                if(isOnTreatment.equals("No")) {
                    taPoints = 0;
                }
                if(isOnTreatment.equals("Si")) {
                    taPoints = 2;
                }
                break;
            case "130 - 139":
                if(isOnTreatment.equals("No")) {
                    taPoints = 1;
                }
                if(isOnTreatment.equals("Si")) {
                    taPoints = 3;
                }
                break;
            case "140 - 149":
                if(isOnTreatment.equals("No")) {
                    taPoints = 2;
                }
                if(isOnTreatment.equals("Si")) {
                    taPoints = 5;
                }
                break;
            case "150 - 159":
                if(isOnTreatment.equals("No")) {
                    taPoints = 4;
                }
                if(isOnTreatment.equals("Si")) {
                    taPoints = 6;
                }
                break;
            case "160+":
                if(isOnTreatment.equals("No")) {
                    taPoints = 5;
                }
                if(isOnTreatment.equals("Si")) {
                    taPoints = 7;
                }
                break;
        }
        return taPoints;
    }

    public void calculateButtonPressed(View view) {
        Button genderButton = this.<Button>findViewById(R.id.genderinput);
        String gender = genderButton.getText().toString();

        int agePoints = calculateAgePoints(gender);
        int hdlPoints = calculateHDLPoints(gender);
        int dlPoints = calculateTotalDLPoints(gender);
        int taPoints = calculateTAPoints(gender);

        int Score = agePoints + hdlPoints + dlPoints + taPoints;
        String ScoreString = Integer.toString(Score);
        Log.d("Score:", ScoreString);

        Snackbar.make(view, ScoreString, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
