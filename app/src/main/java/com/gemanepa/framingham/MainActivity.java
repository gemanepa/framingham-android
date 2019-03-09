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
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                    // Total score
                    int score = agePoints + hdlPoints + dlPoints + taPoints;

                    // Passing total score to String
                    String scoreString = Integer.toString(score);

                    // Total Score rendering
                    TextView bottomsheetScoreText = findViewById(R.id.bottomsheetScoreText);
                    bottomsheetScoreText.setText("Score: " + scoreString);


                    // CVD calculation
                    String cvd = calculateCVD(score, gender);

                    // CVD rendering
                    TextView bottomsheetCVDText = findViewById(R.id.bottomsheetCVDText);
                    bottomsheetCVDText.setText("CVD: " + cvd);

                    TextView bottomsheetCVDExplanationText = findViewById(R.id.bottomsheetCVDExplanationText);
                    bottomsheetCVDExplanationText.setText("*" + "CVD: CardioVascular Disease Risk in 10 years");


                    // Heart Age calculation
                    String heartage = calculateHeartAge(score, gender);

                    // Heart Age rendering
                    TextView bottomsheetHeartAgeText = findViewById(R.id.bottomsheetHeartAgeText);
                    bottomsheetHeartAgeText.setText("Heart Age: " + heartage);


                    // Risk Level Calculation
                    String risklevel = calculateRiskLevel(score, gender);

                    // Risk Level Rendering
                    TextView bottomsheetRiskLevelText = findViewById(R.id.bottomsheetRiskLevelText);
                    bottomsheetRiskLevelText.setText("Risk Level: " + risklevel);

                    // ¿Needs treatment? Calculation
                   String needstreatment = needsTreatment(risklevel);

                    // ¿Needs treatment? Rendering
                    TextView bottomsheetNeedsTreatmentText = findViewById(R.id.bottomsheetNeedsTreatmentText);
                    bottomsheetNeedsTreatmentText.setText("" + needstreatment);

                    // Expanding bottom sheet to show data
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                //if Bottom Sheet is already expanded, it will be collapsed...
                else {
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
        String [] ldlRanges = {"< 80.0", "> 80.0"};
        String [] totaldlRanges = {"< 158", "158 - 200", "201 - 239", "240 - 278", "> 278"};

        Button genderButton = findViewById(R.id.genderinput);
        String gender = genderButton.getText().toString();
        String [] waistRanges = getWaist(gender);

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
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to leave?");
        //set listeners for dialog buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //finish the activity
                finish();
            }
	        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dialog gone
                dialog.dismiss();
                }
	        });

        //create the alert dialog and show it
        builder.create().show();
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

    public void diabetesSwitch(View view) {
        Button diabetesButton = this.<Button>findViewById(R.id.diabetesinput);
        String diabetesButtonCurrentState = diabetesButton.getText().toString();
        //Log.d("Smoking PreConditional", genderButtonCurrentState);
        if(diabetesButtonCurrentState.equals("No")) {
            diabetesButton.setText("Si");
            //Log.d("Smoking 1st Conditional", genderButtonCurrentState);
        }
        else if(diabetesButtonCurrentState.equals("Si")) {
            diabetesButton.setText("No");
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

    public void closeBottomSheet(View view) {
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    // CVD Calculation
    public String calculateCVD(int Score, String gender) {
        String cvd = "0";
        if(gender.equals("Hombre")) {
            cvd = calculateMenCVD(Score);
        }

        else if(gender.equals("Mujer")) {
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

    // Heart Age Calculation
    public String calculateHeartAge(int Score, String gender) {
        String heartage = "0";
        if(gender.equals("Hombre")) {
            heartage = calculateMenHeartAge(Score);
        }

        else if(gender.equals("Mujer")) {
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

    // Risk Level Calculation
    public String calculateRiskLevel(int Score, String gender) {
        String risklevel = "Unknown";
        if(gender.equals("Hombre")) {
            risklevel = calculateMenRiskLevel(Score);
        }

        else if(gender.equals("Mujer")) {
            risklevel = calculateWomenRiskLevel(Score);
        }
        return risklevel;
    }

    private String calculateMenRiskLevel(int Score){
        String risklevel = "Unknown";

        if (Score <= 10) {
            risklevel = "Low";
        }

        else if (Score >= 11 && Score <= 14) {
            risklevel = "Intermediate";
        }

        else if (Score >= 15) {
            risklevel = "High";
        }
        return risklevel;
    }

    private String calculateWomenRiskLevel(int Score){
        String risklevel = "Unknown";

        if (Score <= 12) {
            risklevel = "Low";
        }

        else if (Score >= 13 && Score <= 17) {
            risklevel = "Intermediate";
        }

        else if (Score >= 18) {
            risklevel = "High";
        }
        return risklevel;
    }

    public String needsTreatment(String risklevel){
        String needstreatment = "Unknown";

        switch(risklevel) {
            case "Low":
                needstreatment = "Patient not requires treatment.\n" +
                        "Statins only indicated if:\n"  +
                        "• Diabetes mellitus + Age ≥ 40 years \n" +
                        "• Clinical atherosclerosis\n" +
                        "• Abdominal aortic aneurysm\n" +
                        "• Chronic kidney disease\n" +
                        "(age ≥ 50 years)\n" +
                        "eGFR <60 mL/min/1.73 m2 or\n" +
                        "ACR > 3 mg/mmol\n";
                break;
            case "Intermediate":
                needstreatment = "Only if yadah yadah hipertension diabetes";
                break;
            case "High":
                needstreatment = "• Patient highly requires treatment\n" +
                "• Primary Target: ≤2 mmol/L or ≥50% decrease in LDL-C\n" +
                "• Alternative Target: Apo B ≤0.8 g/L\n" +
                "• Alternative Target: Non-HDL-C ≤2.6 mmol/L\n";
                ;
                break;
        }
        return needstreatment;
    }

    public String[] getWaist(String gender) {
        String [] waist = {"-", "< 102cm (40inches)", "> 102cm (40inches)"};
        String [] menWaist = {"-", "< 102cm (40inches)", "> 102cm (40inches)"};
        String [] womenWaist = {"-", "< 88cm (35inches)", "> 88cm (35inches)"};
        switch(gender) {
            case "Hombre":
                waist = menWaist;
                break;
            case "Mujer":
                waist = womenWaist;
                break;
            default:
                waist = menWaist;
                break;
        }
        return waist;
    }
}
