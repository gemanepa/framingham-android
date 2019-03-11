package com.gemanepa.framingham;

import android.os.Bundle;
import android.view.View;

// Menu
import android.view.Menu;
import android.view.MenuItem;

// Buttons
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;

// Spinner
import android.widget.Spinner;
import android.widget.ArrayAdapter;

// Snackbar
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

// Toolbar
import android.support.v7.widget.Toolbar;

// Alert Window
import android.app.AlertDialog;
import android.content.DialogInterface;

// Bottom Sheet
import android.support.design.widget.BottomSheetBehavior;
import android.widget.TextView;

// Logging
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    Spinner ageSpinner;
    Spinner hdlSpinner;
    Spinner ldlSpinner;
    Spinner totaldlSpinner;
    Spinner waistSpinner;
    Spinner taSpinner;
    BottomSheetBehavior mBottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Init float action button
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String scoreDictionary = getResources().getString(R.string.score);
                String cvdDictionary = getResources().getString(R.string.cvd);
                String cvdexplanationDictionary = getResources().getString(R.string.cvdexplanation);
                String heartageDictionary = getResources().getString(R.string.heartage);
                String riskDictionary = getResources().getString(R.string.risk);

                //Init bottom sheet
                View bottomSheet = findViewById(R.id.bottom_sheet);

                mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

                // Start calculation when pressing button only if Bottom Sheet is not expanded...
                if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {

                    // Init Score Calculation
                    Button genderButton = findViewById(R.id.genderinput);
                    String gender = genderButton.getText().toString();

                    int agePoints = calculateAgePoints(gender);
                    int hdlPoints = calculateHDLPoints(gender);
                    int dlPoints = calculateTotalDLPoints(gender);
                    int taPoints = calculateTAPoints(gender);
                    int smokingPoints = calculateSmokingPoints(gender);

                    // Total score
                    int score = agePoints + hdlPoints + dlPoints + taPoints + smokingPoints;

                    // Passing total score to String
                    String scoreString = Integer.toString(score);

                    // Total Score rendering
                    TextView bottomsheetScoreText = findViewById(R.id.bottomsheetScoreText);
                    bottomsheetScoreText.setText(scoreDictionary+": " + scoreString);


                    // CVD calculation
                    String cvd = calculateCVD(score, gender);

                    // CVD rendering
                    TextView bottomsheetCVDText = findViewById(R.id.bottomsheetCVDText);
                    bottomsheetCVDText.setText(cvdDictionary+": " + cvd);

                    TextView bottomsheetCVDExplanationText = findViewById(R.id.bottomsheetCVDExplanationText);
                    bottomsheetCVDExplanationText.setText("*" + cvdexplanationDictionary);


                    // Heart Age calculation
                    String heartage = calculateHeartAge(score, gender);

                    // Heart Age rendering
                    TextView bottomsheetHeartAgeText = findViewById(R.id.bottomsheetHeartAgeText);
                    bottomsheetHeartAgeText.setText(heartageDictionary+": " + heartage);


                    // Risk Level Calculation
                    String risklevel = calculateRiskLevel(score, gender);

                    // Risk Level Rendering
                    TextView bottomsheetRiskLevelText = findViewById(R.id.bottomsheetRiskLevelText);
                    bottomsheetRiskLevelText.setText(riskDictionary+": " + risklevel);


                    // ¿Needs treatment? Calculation
                   String needstreatment = needsTreatment(risklevel, gender, agePoints, hdlPoints, smokingPoints);

                    // ¿Needs treatment? Rendering
                    TextView bottomsheetNeedsTreatmentText = findViewById(R.id.bottomsheetNeedsTreatmentText);
                    bottomsheetNeedsTreatmentText.setText("" + needstreatment);

                    // Changing floating button icon
                    fab.setImageResource(android.R.drawable.ic_menu_revert);

                    // Expanding bottom sheet to show data
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                //if Bottom Sheet is already expanded, it will be collapsed...
                else {
                    // Changing floating button icon
                    fab.setImageResource(android.R.drawable.ic_menu_send);

                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });


        //Init Spinners
        ageSpinner = (Spinner) findViewById(R.id.ageinput);
        hdlSpinner = (Spinner) findViewById(R.id.hdlinput);
        ldlSpinner = (Spinner) findViewById(R.id.ldlinput);
        totaldlSpinner = (Spinner) findViewById(R.id.totaldlinput);
        waistSpinner = (Spinner) findViewById(R.id.waistinput);
        taSpinner = (Spinner) findViewById(R.id.tainput);




        //value to be shown in the spinners
        String [] ageRanges = {"30 - 34", "35 - 39", "40 - 44", "45 - 49", "50 - 54", "55 - 59", "60 - 64", "65 - 69", "70 - 74", "75+"};
        String [] hdlRanges = {"< 35.0", "35.0 - 45.9", "46.0 - 49.9", "50.0 - 61.9", "> 62.0"};
        String [] ldlRanges = {"< 80.0", "80.0 - 135.0", "> 135.0"};
        String [] totaldlRanges = {"< 158", "158 - 200", "201 - 239", "240 - 278", "> 278"};
        String [] waistRanges = {"-", "< 102cm (40inches)", "> 102cm (40inches)"};
        String [] taRanges = {"< 120", "120 - 129", "130 - 139", "140 - 149", "150 - 159", "160+"};

        //array adapters used to bind values in the spinners
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ageRanges);
        ArrayAdapter<String> hdlAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hdlRanges);
        ArrayAdapter<String> ldlAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ldlRanges);
        ArrayAdapter<String> totaldlAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, totaldlRanges);
        ArrayAdapter<String> taAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, taRanges);
        ArrayAdapter<String> waistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, waistRanges);

        ageSpinner.setAdapter(ageAdapter);
        hdlSpinner.setAdapter(hdlAdapter);
        ldlSpinner.setAdapter(ldlAdapter);
        totaldlSpinner.setAdapter(totaldlAdapter);
        taSpinner.setAdapter(taAdapter);
        waistSpinner.setAdapter(waistAdapter);
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

    //Method automatically called when back button is pressed
    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    // Method that shows an alert window asking if really stopping or not an activity
    // Invoked in onBackPressed
    private void showAlertDialog() {
        //init alert dialog
        final AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setCancelable(false);

        String exit = getResources().getString(R.string.exit);
        String exitquestion = getResources().getString(R.string.exitquestion);
        String yes = getResources().getString(R.string.yes);
        String no = getResources().getString(R.string.no);

        builder.setTitle(exit);
        builder.setMessage(exitquestion);
        //set listeners for dialog buttons
        builder.setPositiveButton(yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //finish the activity
                finish();
            }
	        });
        builder.setNegativeButton(no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dialog gone
                dialog.dismiss();
                }
	        });

        //create the alert dialog and show it
        builder.create().show();
    }

    // Button Switches Methods
    public void genderSwitch(View view) {
        Button genderButton = this.<Button>findViewById(R.id.genderinput);
        String genderButtonCurrentState = genderButton.getText().toString();

        String man = getResources().getString(R.string.man);
        String woman = getResources().getString(R.string.woman);

        if(genderButtonCurrentState.equals(man)) {
            genderButton.setText(woman);
        }
        else if(genderButtonCurrentState.equals(woman)) {
            genderButton.setText(man);
        }

        waistSpinner = (Spinner) findViewById(R.id.waistinput);
        String [] waistRanges = getWaist(genderButtonCurrentState);
        ArrayAdapter<String> waistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, waistRanges);
        waistSpinner.setAdapter(waistAdapter);
    }
    // ... Method used by genderSwitch to update waist value component based on gender
    public String[] getWaist(String gender) {
        String [] waist = {"-", "< 102cm (40inches)", "> 102cm (40inches)"};
        String [] menWaist = {"-", "< 102cm (40inches)", "> 102cm (40inches)"};
        String [] womenWaist = {"-", "< 88cm (35inches)", "> 88cm (35inches)"};


        String man = getResources().getString(R.string.man);
        String woman = getResources().getString(R.string.woman);

        if (gender.equals(man)) {
            waist = womenWaist;
        }
        else if (gender.equals(woman)) {
            waist = menWaist;
        }
        return waist;
    }

    public void smokingSwitch(View view) {
        Button smokerButton = this.<Button>findViewById(R.id.smokerinput);
        String smokerButtonCurrentState = smokerButton.getText().toString();

        String yes = getResources().getString(R.string.yes);
        String no = getResources().getString(R.string.no);

        if(smokerButtonCurrentState.equals(no)) {
            smokerButton.setText(yes);
        }
        else if(smokerButtonCurrentState.equals(yes)) {
            smokerButton.setText(no);
        }
    }

    public void diabetesSwitch(View view) {
        Button diabetesButton = this.<Button>findViewById(R.id.diabetesinput);
        String diabetesButtonCurrentState = diabetesButton.getText().toString();

        String yes = getResources().getString(R.string.yes);
        String no = getResources().getString(R.string.no);

        if(diabetesButtonCurrentState.equals(no)) {
            diabetesButton.setText(yes);
        }
        else if(diabetesButtonCurrentState.equals(yes)) {
            diabetesButton.setText(no);
        }
    }

    public void treatmentSwitch(View view) {
        Button treatmentButton = this.<Button>findViewById(R.id.treatmentinput);
        String treatmentButtonCurrentState = treatmentButton.getText().toString();

        String yes = getResources().getString(R.string.yes);
        String no = getResources().getString(R.string.no);

        if(treatmentButtonCurrentState.equals(no)) {
            treatmentButton.setText(yes);
        }
        else if(treatmentButtonCurrentState.equals(yes)) {
            treatmentButton.setText(no);
        }
    }


    // Age Points Calculation Method
    public int calculateAgePoints(String gender) {
        ageSpinner = (Spinner) findViewById(R.id.ageinput);
        String ageRangeSelected = ageSpinner.getSelectedItem().toString();

        String man = getResources().getString(R.string.man);
        String woman = getResources().getString(R.string.woman);

        int agePoints = 0;
        if(gender.equals(man)) {
            agePoints = calculateMenAgePoints(ageRangeSelected);
        }

        else if(gender.equals(woman)) {
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


    // HDL Points Calculation Method
    public int calculateHDLPoints(String gender) {
        hdlSpinner = (Spinner) findViewById(R.id.hdlinput);
        String hdlRangeSelected = hdlSpinner.getSelectedItem().toString();

        String man = getResources().getString(R.string.man);
        String woman = getResources().getString(R.string.woman);

        int HDLPoints = 0;

        if(gender.equals(man)) {
            HDLPoints = calculateMenHDLPoints(hdlRangeSelected);
        }

        else if(gender.equals(woman)) {
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


    // Total DL Points Calculation Method
    public int calculateTotalDLPoints(String gender) {
        totaldlSpinner = (Spinner) findViewById(R.id.totaldlinput);
        String totaldlRangeSelected = totaldlSpinner.getSelectedItem().toString();

        String man = getResources().getString(R.string.man);
        String woman = getResources().getString(R.string.woman);

        int totalDLPoints = 0;
        if(gender.equals(man)) {
            totalDLPoints = calculateMenTotalDLPoints(totaldlRangeSelected);
        }

        else if(gender.equals(woman)) {
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


    // TA Points Calculation Method
    public int calculateTAPoints(String gender) {
        taSpinner = (Spinner) findViewById(R.id.tainput);
        String taRangeSelected = taSpinner.getSelectedItem().toString();

        Button treatmentButton = this.<Button>findViewById(R.id.treatmentinput);
        String isOnTreatment = treatmentButton.getText().toString();

        String man = getResources().getString(R.string.man);
        String woman = getResources().getString(R.string.woman);

        int taPoints = 0;
        if(gender.equals(man)) {
            taPoints = calculateMenTotalTAPoints(taRangeSelected, isOnTreatment);
        }

        else if(gender.equals(woman)) {
            taPoints = calculateWomenTotalTAPoints(taRangeSelected, isOnTreatment);
        }
        return taPoints;
    }

    private int calculateMenTotalTAPoints(String taRangeSelected, String isOnTreatment){
        int taPoints = 0;

        String yes = getResources().getString(R.string.yes);
        String no = getResources().getString(R.string.no);

        switch(taRangeSelected) {
            case "< 120":
                if(isOnTreatment.equals(no)) {
                    taPoints = -2;
                }
                if(isOnTreatment.equals(yes)) {
                    taPoints = 0;
                }
                break;
            case "120 - 129":
                if(isOnTreatment.equals(no)) {
                    taPoints = 0;
                }
                if(isOnTreatment.equals(yes)) {
                    taPoints = 2;
                }
                break;
            case "130 - 139":
                if(isOnTreatment.equals(no)) {
                    taPoints = 1;
                }
                if(isOnTreatment.equals(yes)) {
                    taPoints = 3;
                }
                break;
            case "140 - 149":
                if(isOnTreatment.equals(no)) {
                    taPoints = 2;
                }
                if(isOnTreatment.equals(yes)) {
                    taPoints = 4;
                }
                break;
            case "150 - 159":
                if(isOnTreatment.equals(no)) {
                    taPoints = 2;
                }
                if(isOnTreatment.equals(yes)) {
                    taPoints = 4;
                }
                break;
            case "160+":
                if(isOnTreatment.equals(no)) {
                    taPoints = 3;
                }
                if(isOnTreatment.equals(yes)) {
                    taPoints = 5;
                }
                break;
        }
        return taPoints;
    }

    private int calculateWomenTotalTAPoints(String taRangeSelected, String isOnTreatment){
        int taPoints = 0;

        String yes = getResources().getString(R.string.yes);
        String no = getResources().getString(R.string.no);

        switch(taRangeSelected) {
            case "< 120":
                if(isOnTreatment.equals(no)) {
                    taPoints = -3;
                }
                if(isOnTreatment.equals(yes)) {
                    taPoints = -1;
                }
                break;
            case "120 - 129":
                if(isOnTreatment.equals(no)) {
                    taPoints = 0;
                }
                if(isOnTreatment.equals(yes)) {
                    taPoints = 2;
                }
                break;
            case "130 - 139":
                if(isOnTreatment.equals(no)) {
                    taPoints = 1;
                }
                if(isOnTreatment.equals(yes)) {
                    taPoints = 3;
                }
                break;
            case "140 - 149":
                if(isOnTreatment.equals(no)) {
                    taPoints = 2;
                }
                if(isOnTreatment.equals(yes)) {
                    taPoints = 5;
                }
                break;
            case "150 - 159":
                if(isOnTreatment.equals(no)) {
                    taPoints = 4;
                }
                if(isOnTreatment.equals(yes)) {
                    taPoints = 6;
                }
                break;
            case "160+":
                if(isOnTreatment.equals(no)) {
                    taPoints = 5;
                }
                if(isOnTreatment.equals(yes)) {
                    taPoints = 7;
                }
                break;
        }
        return taPoints;
    }


    // Smoking Points Calculation Method
    public int calculateSmokingPoints(String gender) {
        Button smokerButton = this.<Button>findViewById(R.id.smokerinput);
        String smokerButtonCurrentState = smokerButton.getText().toString();
        int smokingpoints = 0;

        String yes = getResources().getString(R.string.yes);
        String no = getResources().getString(R.string.no);
        String man = getResources().getString(R.string.man);
        String woman = getResources().getString(R.string.woman);

        if(smokerButtonCurrentState.equals(no)) {
            smokingpoints = 0;
        }
        else if(smokerButtonCurrentState.equals(yes)) {
            if(gender.equals(man)) {
                smokingpoints = 4;
            }
            else if(gender.equals(woman)) {
                smokingpoints = 3;
            }
        }
        return smokingpoints;
    }


    // CVD Calculation Method
    public String calculateCVD(int Score, String gender) {
        String cvd = "0";

        String man = getResources().getString(R.string.man);
        String woman = getResources().getString(R.string.woman);

        if(gender.equals(man)) {
            cvd = calculateMenCVD(Score);
        }

        else if(gender.equals(woman)) {
            cvd = calculateWomenCVD(Score);
        }
        return cvd;
    }

    private String calculateMenCVD(int Score){
        String cvd = "0%";

        switch(Score) {
            case -5:
                cvd = "< 1%";
                break;
            case -4:
                cvd = "< 1%";
                break;
            case -3:
                cvd = "< 1%";
                break;
            case -2:
                cvd = "1.1%";
                break;
            case -1:
                cvd = "1.4%";
                break;
            case 0:
                cvd = "1.6%";
                break;
            case 1:
                cvd = "1.9%";
                break;
            case 2:
                cvd = "2.3%";
                break;
            case 3:
                cvd = "2.8%";
                break;
            case 4:
                cvd = "3.3%";
                break;
            case 5:
                cvd = "3.9%";
                break;
            case 6:
                cvd = "4.7%";
                break;
            case 7:
                cvd = "5.6%";
                break;
            case 8:
                cvd = "6.7%";
                break;
            case 9:
                cvd = "7.9%";
                break;
            case 10:
                cvd = "9.4%";
                break;
            case 11:
                cvd = "11.2%";
                break;
            case 12:
                cvd = "13.3%";
                break;
            case 13:
                cvd = "15.6%";
                break;
            case 14:
                cvd = "18.4%";
                break;
            case 15:
                cvd = "21.6%";
                break;
            case 16:
                cvd = "25.3%";
                break;
            case 17:
                cvd = "29.4%";
                break;
            case 18:
                cvd = "> 30%";
                break;
            case 19:
                cvd = "> 30%";
                break;
            case 20:
                cvd = "> 30%";
                break;
            case 21:
                cvd = "> 30%";
                break;
            case 22:
                cvd = "> 30%";
                break;
            case 23:
                cvd = "> 30%";
                break;
            case 24:
                cvd = "> 30%";
                break;
            case 25:
                cvd = "> 30%";
                break;
            case 26:
                cvd = "> 30%";
                break;
        }
        return cvd;
    }

    private String calculateWomenCVD(int Score){
        String cvd = "0%";

        switch(Score) {
            case -5:
                cvd = "< 1%";
                break;
            case -4:
                cvd = "< 1%";
                break;
            case -3:
                cvd = "< 1%";
                break;
            case -2:
                cvd = "< 1%";
                break;
            case -1:
                cvd = "1.0%";
                break;
            case 0:
                cvd = "1.2%";
                break;
            case 1:
                cvd = "1.5%";
                break;
            case 2:
                cvd = "1.7%";
                break;
            case 3:
                cvd = "2.0%";
                break;
            case 4:
                cvd = "2.4%";
                break;
            case 5:
                cvd = "2.8%";
                break;
            case 6:
                cvd = "3.3%";
                break;
            case 7:
                cvd = "3.9%";
                break;
            case 8:
                cvd = "4.5%";
                break;
            case 9:
                cvd = "5.3%";
                break;
            case 10:
                cvd = "6.3%";
                break;
            case 11:
                cvd = "7.3%";
                break;
            case 12:
                cvd = "8.6%";
                break;
            case 13:
                cvd = "10.0%";
                break;
            case 14:
                cvd = "11.7%";
                break;
            case 15:
                cvd = "13.7%";
                break;
            case 16:
                cvd = "15.9%";
                break;
            case 17:
                cvd = "18.51%";
                break;
            case 18:
                cvd = "21.5%";
                break;
            case 19:
                cvd = "24.8%";
                break;
            case 20:
                cvd = "27.5%";
                break;
            case 21:
                cvd = "> 30%";
                break;
            case 22:
                cvd = "> 30%";
                break;
            case 23:
                cvd = "> 30%";
                break;
            case 24:
                cvd = "> 30%";
                break;
            case 25:
                cvd = "> 30%";
                break;
            case 26:
                cvd = "> 30%";
                break;
        }
        return cvd;
    }


    // Heart Age Calculation Method
    public String calculateHeartAge(int Score, String gender) {
        String heartage = "0";

        String man = getResources().getString(R.string.man);
        String woman = getResources().getString(R.string.woman);

        if(gender.equals(man)) {
            heartage = calculateMenHeartAge(Score);
        }

        else if(gender.equals(woman)) {
            heartage = calculateWomenHeartAge(Score);
        }
        return heartage;
    }

    private String calculateMenHeartAge(int Score){
        String heartage = "30";

        switch(Score) {
            case -5:
                heartage = "< 30";
                break;
            case -4:
                heartage = "< 30";
                break;
            case -3:
                heartage = "< 30";
                break;
            case -2:
                heartage = "< 30";
                break;
            case -1:
                heartage = "< 30";
                break;
            case 0:
                heartage = "30";
                break;
            case 1:
                heartage = "31";
                break;
            case 2:
                heartage = "34";
                break;
            case 3:
                heartage = "36";
                break;
            case 4:
                heartage = "38";
                break;
            case 5:
                heartage = "40";
                break;
            case 6:
                heartage = "42";
                break;
            case 7:
                heartage = "45";
                break;
            case 8:
                heartage = "48";
                break;
            case 9:
                heartage = "51";
                break;
            case 10:
                heartage = "54";
                break;
            case 11:
                heartage = "57";
                break;
            case 12:
                heartage = "60";
                break;
            case 13:
                heartage = "64";
                break;
            case 14:
                heartage = "68";
                break;
            case 15:
                heartage = "72";
                break;
            case 16:
                heartage = "76";
                break;
            case 17:
                heartage = "> 80";
                break;
            case 18:
                heartage = "> 80";
                break;
            case 19:
                heartage = "> 80";
                break;
            case 20:
                heartage = "> 80";
                break;
            case 21:
                heartage = "> 80";
                break;
            case 22:
                heartage = "> 80";
                break;
            case 23:
                heartage = "> 80";
                break;
            case 24:
                heartage = "> 80";
                break;
            case 25:
                heartage = "> 80";
                break;
            case 26:
                heartage = "> 80";
                break;
        }
        return heartage;
    }

    private String calculateWomenHeartAge(int Score){
        String heartage = "0%";

        switch(Score) {
            case -5:
                heartage = "< 30";
                break;
            case -4:
                heartage = "< 30";
                break;
            case -3:
                heartage = "< 30";
                break;
            case -2:
                heartage = "< 30";
                break;
            case -1:
                heartage = "< 30";
                break;
            case 0:
                heartage = "< 30";
                break;
            case 1:
                heartage = "31";
                break;
            case 2:
                heartage = "34";
                break;
            case 3:
                heartage = "36";
                break;
            case 4:
                heartage = "39";
                break;
            case 5:
                heartage = "42";
                break;
            case 6:
                heartage = "45";
                break;
            case 7:
                heartage = "48";
                break;
            case 8:
                heartage = "51";
                break;
            case 9:
                heartage = "55";
                break;
            case 10:
                heartage = "59";
                break;
            case 11:
                heartage = "64";
                break;
            case 12:
                heartage = "68";
                break;
            case 13:
                heartage = "73";
                break;
            case 14:
                heartage = "79";
                break;
            case 15:
                heartage = "> 80";
                break;
            case 16:
                heartage = "> 80";
                break;
            case 17:
                heartage = "> 80";
                break;
            case 18:
                heartage = "> 80";
                break;
            case 19:
                heartage = "> 80";
                break;
            case 20:
                heartage = "> 80";
                break;
            case 21:
                heartage = "> 80";
                break;
            case 22:
                heartage = "> 80";
                break;
            case 23:
                heartage = "> 80";
                break;
            case 24:
                heartage = "> 80";
                break;
            case 25:
                heartage = "> 80";
                break;
            case 26:
                heartage = "> 80";
                break;
        }
        return heartage;
    }


    // Risk Level Calculation Method
    public String calculateRiskLevel(int Score, String gender) {

        String man = getResources().getString(R.string.man);
        String woman = getResources().getString(R.string.woman);
        String unknown = getResources().getString(R.string.unknown);

        String risklevel = unknown;
        if(gender.equals(man)) {
            risklevel = calculateMenRiskLevel(Score);
        }

        else if(gender.equals(woman)) {
            risklevel = calculateWomenRiskLevel(Score);
        }
        return risklevel;
    }

    private String calculateMenRiskLevel(int Score){

        String unknown = getResources().getString(R.string.unknown);
        String low = getResources().getString(R.string.low);
        String intermediate = getResources().getString(R.string.intermediate);
        String high = getResources().getString(R.string.high);

        String risklevel = unknown;

        if (Score <= 10) {
            risklevel = low;
        }

        else if (Score >= 11 && Score <= 14) {
            risklevel = intermediate;
        }

        else if (Score >= 15) {
            risklevel = high;
        }
        return risklevel;
    }

    private String calculateWomenRiskLevel(int Score){
        String unknown = getResources().getString(R.string.unknown);
        String low = getResources().getString(R.string.low);
        String intermediate = getResources().getString(R.string.intermediate);
        String high = getResources().getString(R.string.high);

        String risklevel = unknown;

        if (Score <= 12) {
            risklevel = low;
        }

        else if (Score >= 13 && Score <= 17) {
            risklevel = intermediate;
        }

        else if (Score >= 18) {
            risklevel = high;
        }
        return risklevel;
    }


    // Method that calculates if patient needs treatment and of what kind
    public String needsTreatment(String risklevel, String gender, int agePoints, int hdlPoints, int smokingPoints){
        String yes = getResources().getString(R.string.yes);
        String man = getResources().getString(R.string.man);
        String woman = getResources().getString(R.string.woman);
        String unknown = getResources().getString(R.string.unknown);
        String low = getResources().getString(R.string.low);
        String intermediate = getResources().getString(R.string.intermediate);
        String high = getResources().getString(R.string.high);
        String treatmentLowDiabetes = getResources().getString(R.string.treatment_low_diabetes);
        String patientnotrequirestreatment = getResources().getString(R.string.patient_not_requires_treatment);
        String statinsonlyindicated = getResources().getString(R.string.statins_only_indicated);
        String clinicalatherosclerosis = getResources().getString(R.string.aterosclerosis);
        String aorticaneurysm = getResources().getString(R.string.abdominal_aortic_aneurysm);
        String chronickidneydisease = getResources().getString(R.string.chronic_kidney_disease);
        String age = getResources().getString(R.string.age);
        String years = getResources().getString(R.string.years);
        String intermediatewithfactors = getResources().getString(R.string.treatment_intermediate_hasfactors);
        String intermediateldl = getResources().getString(R.string.treatment_intermediate_ldl);
        String intermediatenofactors = getResources().getString(R.string.treatment_intermediate_norisks);
        String patientrequirestreatment = getResources().getString(R.string.patient_highly_requires_treatment);
        String primarytarget = getResources().getString(R.string.primary_target);
        String alternativetarget = getResources().getString(R.string.alternative_target);
        String decreasein = getResources().getString(R.string.decrease_in);

        String needstreatment = unknown;

        Button diabetesButton = this.<Button>findViewById(R.id.diabetesinput);
        String diabetesButtonCurrentState = diabetesButton.getText().toString();

        waistSpinner = (Spinner) findViewById(R.id.waistinput);
        String waistRangeSelected = waistSpinner.getSelectedItem().toString();

        ldlSpinner = (Spinner) findViewById(R.id.ldlinput);
        String ldlRangeSelected = ldlSpinner.getSelectedItem().toString();


            if(risklevel.equals(low)) {
                if (
                        (gender.equals(man) && agePoints >= 5 && diabetesButtonCurrentState.equals(yes)) ||
                                (gender.equals(woman) && agePoints >= 4 && diabetesButtonCurrentState.equals(yes))
                ) {
                    needstreatment = treatmentLowDiabetes;
                } else {
                    needstreatment = patientnotrequirestreatment + "\n" +
                            statinsonlyindicated + " " +
                            clinicalatherosclerosis +", " +
                            aorticaneurysm +", & " +
                            chronickidneydisease +" " +
                            "("+age+" ≥ 50 "+years+" + " +
                            "eGFR <60 mL/min/1.73 m2 or " +
                            "ACR > 3 mg/mmol)";
                }
            }
            else if(risklevel.equals(intermediate)) {
                if (
                        (gender.equals(man) && agePoints >= 8 && (hdlPoints == 2 ||
                                smokingPoints > 1 ||
                                diabetesButtonCurrentState.equals(yes) ||
                                waistRangeSelected.equals("> 102cm (40inches)") ||
                                waistRangeSelected.equals("> 88cm (35inches)")
                        )) ||
                                (gender.equals(woman) && agePoints >= 9 && (hdlPoints == 2 ||
                                        smokingPoints > 1 ||
                                        diabetesButtonCurrentState.equals(yes) ||
                                        waistRangeSelected.equals("> 102cm (40inches)") ||
                                        waistRangeSelected.equals("> 88cm (35inches)")
                                ))
                ) {
                    needstreatment = intermediatewithfactors;

                } else if (ldlRangeSelected.equals("> 135.0")) {
                    needstreatment = intermediateldl;
                } else {
                    needstreatment = intermediatenofactors;
                }
            }

            else if (risklevel.equals(high)) {
                needstreatment = "• "+patientrequirestreatment+"\n" +
                        "• "+primarytarget+": ≤2 mmol/L or ≥50% "+decreasein+" LDL-C\n" +
                        "• "+alternativetarget+": Apo B ≤0.8 g/L\n" +
                        "• "+alternativetarget+": Non-HDL-C ≤2.6 mmol/L\n";
                ;
            }


        return needstreatment;
        }

    // Close Bottom Sheet Method
    public void closeBottomSheet(View view) {
        // Changing floating button icon
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_menu_send);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

}
