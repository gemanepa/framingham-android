package com.gemanepa.framingham;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static androidx.core.content.FileProvider.getUriForFile;

public class ResultsActivity extends AppCompatActivity {
    boolean isFABMenuOpen = false;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        String score = extras.getString("score");
        String cvd = extras.getString("cvd");
        String heartage = extras.getString("heartage");
        String risklevel = extras.getString("risklevel");
        String needstreatment = extras.getString("needstreatment");

        String scoreDictionary = getResources().getString(R.string.score);
        String cvdDictionary = getResources().getString(R.string.cvd);
        String cvdexplanationDictionary = getResources().getString(R.string.cvdexplanation);
        String heartageDictionary = getResources().getString(R.string.heartage);
        String riskDictionary = getResources().getString(R.string.risk);

        // Total Score rendering
        TextView bottomsheetScoreText = findViewById(R.id.bottomsheetScoreText);
        bottomsheetScoreText.setText(scoreDictionary+": " + score);

        // CVD rendering
        TextView bottomsheetCVDText = findViewById(R.id.bottomsheetCVDText);
        bottomsheetCVDText.setText(cvdDictionary+": " + cvd);

        TextView bottomsheetCVDExplanationText = findViewById(R.id.bottomsheetCVDExplanationText);
        bottomsheetCVDExplanationText.setText("*" + cvdexplanationDictionary);

        // Heart Age rendering
        TextView bottomsheetHeartAgeText = findViewById(R.id.bottomsheetHeartAgeText);
        bottomsheetHeartAgeText.setText(heartageDictionary+": " + heartage);

        // Risk Level Rendering
        TextView bottomsheetRiskLevelText = findViewById(R.id.bottomsheetRiskLevelText);
        bottomsheetRiskLevelText.setText(riskDictionary+": " + risklevel);

        // ¿Needs treatment? Rendering
        TextView bottomsheetNeedsTreatmentText = findViewById(R.id.bottomsheetNeedsTreatmentText);
        bottomsheetNeedsTreatmentText.setText("" + needstreatment);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABMenuOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        // Method to return to previous activity
        final FloatingActionButton returnfab = (FloatingActionButton) findViewById(R.id.fab1);
        returnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final FloatingActionButton sharefab = (FloatingActionButton) findViewById(R.id.fab2);
        sharefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertStringWindow(2);
            }
        });

        final FloatingActionButton schedulefab = (FloatingActionButton) findViewById(R.id.fab3);
        schedulefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertStringWindow(3);
            }
        });

        final FloatingActionButton downloadAsPdffab = (FloatingActionButton) findViewById(R.id.fab4);
        downloadAsPdffab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int permission = ActivityCompat.checkSelfPermission(ResultsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            ResultsActivity.this,
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                    insertStringWindow(4);
                }
                else {
                    insertStringWindow(4);
                }

            }
        });
    }

    private void insertStringWindow(final int proceedWithFeature){
        String cancelDictionary = getResources().getString(R.string.cancel);
        String patientnameDictionary = getResources().getString(R.string.patient_name);
        String optionalDictionary = getResources().getString(R.string.optional);
        String continueDictionary = getResources().getString(R.string.continuar);

        Bundle extras = getIntent().getExtras();
        final String gender = extras.getString("gender");
        final String age = extras.getString("age");
        final String hdl = extras.getString("hdl");
        final String ldl = extras.getString("ldl");
        final String totaldl = extras.getString("totaldl");
        final String ta = extras.getString("ta");
        final String waist = extras.getString("waist");
        final String smoker = extras.getString("smoker");
        final String diabetes = extras.getString("diabetes");
        final String onTreatment = extras.getString("onTreatment");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(patientnameDictionary+" ("+optionalDictionary+")");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton(continueDictionary, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String patientName = input.getText().toString();

                if (proceedWithFeature == 2) {
                    shareResults(patientName, gender, age, hdl, ldl, totaldl, ta, waist, smoker, diabetes, onTreatment);
                }
                else if (proceedWithFeature == 3) {
                    Schedule(patientName, gender, age, hdl, ldl, totaldl, ta, waist, smoker, diabetes, onTreatment);
                }
                else if (proceedWithFeature == 4) {
                    createPdf(patientName, gender, age, hdl, ldl, totaldl, ta, waist, smoker, diabetes, onTreatment);
                }
            }
        });
        builder.setNegativeButton(cancelDictionary, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showNoAppForThatIntentPopup() {
        //init alert dialog
        final AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setCancelable(false);

        String yes = getResources().getString(R.string.yes);
        String no = getResources().getString(R.string.no);

        builder.setTitle(getResources().getString(R.string.nopdfreader));
        builder.setMessage(getResources().getString(R.string.gotoplaystore));
        //set listeners for dialog buttons
        builder.setPositiveButton(yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //finish the activity
                Intent pdfviewerApp = new Intent(Intent.ACTION_VIEW);
                pdfviewerApp.setData(Uri.parse(
                        "https://play.google.com/store/apps/details?id=com.google.android.apps.pdfviewer"));
                pdfviewerApp.setPackage("com.android.vending");
                startActivity(pdfviewerApp);
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

    private void pdfCreatedPopup(final String targetPdf, final String patientName) {
        final AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setCancelable(false);

        String close = getResources().getString(R.string.close);
        String view = getResources().getString(R.string.view);
        String send = getResources().getString(R.string.send);

        builder.setTitle(getResources().getString(R.string.pdfsuccess));
        //set listeners for dialog buttons
        builder.setPositiveButton(send, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //finish the activity

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});

                if (patientName.length() > 1) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.navbar_title) + " PDF | "+patientName);

                    String patientDictionary = getResources().getString(R.string.patient);
                    intent.putExtra(Intent.EXTRA_TEXT, patientDictionary + ": "+patientName);
                }
                else {intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.navbar_title)+" PDF");}
                //intent.putExtra(Intent.EXTRA_TEXT, "body text");
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),targetPdf);
                if (!file.exists() || !file.canRead()) {
                    Toast.makeText(ResultsActivity.this, "Error :(", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                Uri contentUri = getUriForFile(ResultsActivity.this, "com.gemanepa.fileprovider", file);
                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                startActivity(Intent.createChooser(intent, "Send email..."));
                /*
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                startActivity(emailIntent);
                */
            }
        });

        builder.setNegativeButton(view, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                readPdf(targetPdf);
            }
        });

        builder.setNeutralButton(close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dialog gone
                dialog.dismiss();
            }
        });

        //create the alert dialog and show it
        builder.create().show();
    }


    private void showFABMenu(){
        findViewById(R.id.fab1).animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        findViewById(R.id.fab2).animate().translationY(-getResources().getDimension(R.dimen.standard_120));
        findViewById(R.id.fab3).animate().translationY(-getResources().getDimension(R.dimen.standard_180));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.fab4).animate().translationY(-getResources().getDimension(R.dimen.standard_240));
        } else {
            findViewById(R.id.fab4).setVisibility(View.GONE);
        }
        isFABMenuOpen = true;
    }

    private void closeFABMenu(){
        findViewById(R.id.fab1).animate().translationY(0);
        findViewById(R.id.fab2).animate().translationY(0);
        findViewById(R.id.fab3).animate().translationY(0);
        findViewById(R.id.fab4).animate().translationY(0);
        isFABMenuOpen = false;
    }

    // Close Bottom Sheet Method
    public void finishActivity(View view) {
        finish();
    }


    private void shareResults(String patientName, String gender, String age, String hdl, String ldl, String totaldl, String ta, String waist, String smoker, String diabetes, String onTreatment) {
        String name = "";
        if (patientName.length() > 1) {
            String patientnameDictionary = getResources().getString(R.string.patient_name);
            name = patientnameDictionary+": "+ patientName + "\n";
        }

        String framingham = getResources().getString(R.string.navbar_title);
        String patientData = getResources().getString(R.string.patientdata);
        String results = getResources().getString(R.string.results);

        TextView bottomsheetScoreText = findViewById(R.id.bottomsheetScoreText);
        String scoreString = bottomsheetScoreText.getText().toString();

        TextView bottomsheetCVDText = findViewById(R.id.bottomsheetCVDText);
        String cvdString = bottomsheetCVDText.getText().toString();

        TextView bottomsheetHeartAgeText = findViewById(R.id.bottomsheetHeartAgeText);
        String heartageString = bottomsheetHeartAgeText.getText().toString();

        TextView bottomsheetRiskLevelText = findViewById(R.id.bottomsheetRiskLevelText );
        String risklevelString = bottomsheetRiskLevelText.getText().toString();

        TextView bottomsheetTreatmentTitle  = findViewById(R.id.bottomsheetTreatmentTitle);
        String treatmentTitleString = bottomsheetTreatmentTitle .getText().toString();

        TextView bottomsheetNeedsTreatmentText  = findViewById(R.id.bottomsheetNeedsTreatmentText);
        String treatmentString = bottomsheetNeedsTreatmentText .getText().toString();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        String allStrings = "#"+framingham.toUpperCase()+"\n\n"+
                patientData.toUpperCase()+"\n"+
                name+gender+"\n"+age+"\n"+hdl+"\n"+ldl+"\n"+totaldl+"\n"+ta+"\n"+onTreatment+"\n"+smoker+"\n"+diabetes+"\n"+waist+"\n\n"+
                results.toUpperCase()+"\n"+
                scoreString + "\n"+
                cvdString + "\n"+
                heartageString + "\n"+
                risklevelString + "\n"+
                treatmentTitleString + " " + treatmentString;

        sendIntent.putExtra(Intent.EXTRA_TEXT, allStrings);

        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void Schedule(String patientName, String gender, String age, String hdl, String ldl, String totaldl, String ta, String waist, String smoker, String diabetes, String onTreatment){
        String name = "";
        String eventTitle = "Framingham";
        if (patientName.length() > 1) {
            String patientnameDictionary = getResources().getString(R.string.patient_name);
            name = patientnameDictionary+": "+ patientName + "\n";
            eventTitle = "Framingham "+patientName;
        }

        String framingham = getResources().getString(R.string.navbar_title);
        String patientData = getResources().getString(R.string.patientdata);
        String results = getResources().getString(R.string.results);

        TextView bottomsheetScoreText = findViewById(R.id.bottomsheetScoreText);
        String scoreString = bottomsheetScoreText.getText().toString();

        TextView bottomsheetCVDText = findViewById(R.id.bottomsheetCVDText);
        String cvdString = bottomsheetCVDText.getText().toString();

        TextView bottomsheetHeartAgeText = findViewById(R.id.bottomsheetHeartAgeText);
        String heartageString = bottomsheetHeartAgeText.getText().toString();

        TextView bottomsheetRiskLevelText = findViewById(R.id.bottomsheetRiskLevelText );
        String risklevelString = bottomsheetRiskLevelText.getText().toString();

        TextView bottomsheetTreatmentTitle  = findViewById(R.id.bottomsheetTreatmentTitle);
        String treatmentTitleString = bottomsheetTreatmentTitle .getText().toString();

        TextView bottomsheetNeedsTreatmentText  = findViewById(R.id.bottomsheetNeedsTreatmentText);
        String treatmentString = bottomsheetNeedsTreatmentText .getText().toString();

        Calendar c = Calendar.getInstance();

        SimpleDateFormat yearr = new SimpleDateFormat("yyyy");
        String yearString = yearr.format(c.getTime());

        SimpleDateFormat monthh = new SimpleDateFormat("M");
        String monthString = monthh.format(c.getTime());

        SimpleDateFormat dayy = new SimpleDateFormat("d");
        String dayString = dayy.format(c.getTime());
        /*
        Log.d("year", yearString);
        Log.d("month", monthString);
        Log.d("day", dayString);
        */
        int year = Integer.parseInt(yearString);
        int month = Integer.parseInt(monthString) - 1;
        int day = Integer.parseInt(dayString);

        SimpleDateFormat hourformat = new SimpleDateFormat("H");
        int hour = Integer.parseInt(hourformat.format(c.getTime()));

        SimpleDateFormat minformat = new SimpleDateFormat("m");
        int minutes = Integer.parseInt(minformat.format(c.getTime()));
        int minutesplus15 = Integer.parseInt(minformat.format(c.getTime())) + 15;


        String allStrings = "#"+framingham.toUpperCase()+"\n\n"+
                patientData.toUpperCase()+"\n"+
                name+gender+"\n"+age+"\n"+hdl+"\n"+ldl+"\n"+totaldl+"\n"+ta+"\n"+onTreatment+"\n"+smoker+"\n"+diabetes+"\n"+waist+"\n\n"+
                results.toUpperCase()+"\n"+
                scoreString + "\n"+
                cvdString + "\n"+
                heartageString + "\n"+
                risklevelString + "\n"+
                treatmentTitleString + " " + treatmentString;

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, day, hour, minutes);
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, day, hour, minutesplus15);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, eventTitle)
                .putExtra(CalendarContract.Events.DESCRIPTION, allStrings)
                //.putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        //.putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        startActivity(intent);
    }

    //PDF CREATION LOGIC
    private void createPdf(String patientName, String gender, String age, String hdl, String ldl, String totaldl, String ta, String waist, String smoker, String diabetes, String onTreatment){
        String patientData = getResources().getString(R.string.patientdata);
        String results = getResources().getString(R.string.results);

        TextView bottomsheetScoreText = findViewById(R.id.bottomsheetScoreText);
        String scoreString = bottomsheetScoreText.getText().toString();

        TextView bottomsheetCVDText = findViewById(R.id.bottomsheetCVDText);
        String cvdString = bottomsheetCVDText.getText().toString();

        TextView bottomsheetHeartAgeText = findViewById(R.id.bottomsheetHeartAgeText);
        String heartageString = bottomsheetHeartAgeText.getText().toString();

        TextView bottomsheetRiskLevelText = findViewById(R.id.bottomsheetRiskLevelText );
        String risklevelString = bottomsheetRiskLevelText.getText().toString();

        TextView bottomsheetTreatmentTitle  = findViewById(R.id.bottomsheetTreatmentTitle);
        String treatmentTitleString = bottomsheetTreatmentTitle .getText().toString();

        TextView bottomsheetNeedsTreatmentText  = findViewById(R.id.bottomsheetNeedsTreatmentText);
        String treatmentString = bottomsheetNeedsTreatmentText .getText().toString();

        // create a new document
        PdfDocument document = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document = new PdfDocument();
        }

        // crate a page description
        PdfDocument.PageInfo pageInfo =
                null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pageInfo = new PdfDocument.PageInfo.Builder(794, 1123, 1).create();
        }

        // start a page
        PdfDocument.Page page = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            page = document.startPage(pageInfo);
        }

        Canvas canvas = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            canvas = page.getCanvas();
        }

        Calendar c = Calendar.getInstance();

        SimpleDateFormat yearr = new SimpleDateFormat("yyyy");
        String yearString = getResources().getString(R.string.year) +": "+yearr.format(c.getTime());

        SimpleDateFormat monthh = new SimpleDateFormat("MM");
        String monthString = getResources().getString(R.string.month) +": "+monthh.format(c.getTime());
        Log.d("monthString", monthString);
        SimpleDateFormat dayy = new SimpleDateFormat("dd");
        String dayString = getResources().getString(R.string.day) +": "+dayy.format(c.getTime());



        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(27);

        if(patientName.length() > 1) {
            String patientDictionary = getResources().getString(R.string.patient);
            canvas.drawText(patientDictionary + ": "+patientName, 10, 35, paint);
        }
        canvas.drawText(yearString, 650, 35, paint);
        canvas.drawText(monthString, 650, 65, paint);
        canvas.drawText(dayString, 650, 95, paint);

        canvas.drawText(getResources().getString(R.string.navbar_title).toUpperCase(), 200, 100, paint);

        canvas.drawText(patientData.toUpperCase(), 20, 175, paint);
        canvas.drawText(gender, 20, 225, paint);
        canvas.drawText(age, 520, 225, paint);

        canvas.drawText(totaldl, 20, 275, paint);
        canvas.drawText(hdl, 20, 325, paint);
        canvas.drawText(ldl, 20, 375, paint);

        canvas.drawText(ta, 20, 425, paint);
        canvas.drawText(onTreatment, 20, 475, paint);

        canvas.drawText(smoker, 20, 525, paint);
        canvas.drawText(diabetes, 20, 575, paint);
        canvas.drawText(waist, 20, 625, paint);

        canvas.drawText(results.toUpperCase(), 20, 700, paint);
        canvas.drawText(scoreString, 20, 750, paint);
        canvas.drawText(cvdString, 420, 750, paint);

        canvas.drawText(heartageString, 20, 800, paint);
        canvas.drawText(risklevelString, 420, 800, paint);

        canvas.drawText(treatmentTitleString, 20, 850, paint);

        String[] SplittedtreatmentString = splitStringEvery(treatmentString, 60);

        int verticalSpace = 90;
        for (int i = 0; i < SplittedtreatmentString.length; i++){
            String y = String.valueOf(verticalSpace)+"0";
            canvas.drawText(SplittedtreatmentString[i], 20, Float.parseFloat(y), paint);
            verticalSpace = verticalSpace + 5;
        }

        // finish the page
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document.finishPage(page);
        }

        // write the document content
        String targetPdf = "Framingham-"+getResources().getString(R.string.score)+".pdf";
        if(patientName.length() > 1) {
            targetPdf = "Framingham-"+getResources().getString(R.string.score)+"-"+patientName+".pdf";
        }


        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),targetPdf);
                document.writeTo(new FileOutputStream(filePath));
            }
            //Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();

            // close the document
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                document.close();
            }

            pdfCreatedPopup(targetPdf, patientName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.toString(),
                    Toast.LENGTH_LONG).show();
        }


    }

    public String[] splitStringEvery(String s, int interval) {
        int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + interval);
            j += interval;
        } //Add the last bit
        result[lastIndex] = s.substring(j);

        return result;
    }

    private void readPdf(String filePath){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),filePath);

        /*
        Log.d("filePath", filePath);
        Log.d("absolutePath", Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.d("file", file.toString());
        */
        Uri contentUri = getUriForFile(ResultsActivity.this, "com.gemanepa.fileprovider", file);


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.setDataAndType(contentUri, "application/pdf");
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        PackageManager manager = this.getPackageManager();
        List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);

        if (infos.size() > 0) {
            //Then there is an Application(s) can handle your intent
            startActivity(intent);
        } else {
            //No Application can handle your intent
            showNoAppForThatIntentPopup();
        }
    }
}
