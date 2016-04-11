package com.ndtv.ndtvdoc.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ndtv.ndtvdoc.R;
import com.ndtv.ndtvdoc.adapters.HomePagerAdapter;
import com.ndtv.ndtvdoc.models.Patient;
import com.ndtv.ndtvdoc.models.PatientDetailObjectModel;
import com.ndtv.ndtvdoc.models.PatientDetails;
import com.ndtv.ndtvdoc.models.Patients;
import com.ndtv.ndtvdoc.utils.AppConstants;
import com.ndtv.ndtvdoc.utils.NDTVSharedPreferencesManager;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    private Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    MaterialNumberPicker numberPicker;
    FloatingActionButton mEmergencyFab;
    private DB snappyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!NDTVSharedPreferencesManager.getBoolean(this, AppConstants.APPOPENED)){

        }

        initInstances();

        if (!NDTVSharedPreferencesManager.getBoolean(this, NDTVSharedPreferencesManager.USER_DATA_STORED)) {
            parseJson();
        }

        assert mEmergencyFab != null;
        mEmergencyFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final ViewGroup viewGroup = (ViewGroup) mInflater.inflate(R.layout.number_picker, null);
                numberPicker = (MaterialNumberPicker) viewGroup.findViewById(R.id.numPicker);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Emergency Appointemt Schedule")
                        .setView(viewGroup)
                        .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ArrayList<String> ids = null;
                                double emergencyTimeMili = numberPicker.getValue() * 60 * 60 * 1000;
                                long timeInMillis = System.currentTimeMillis();
                                Calendar cal1 = Calendar.getInstance();
                                cal1.setTimeInMillis(timeInMillis);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
                                int currentTimeHour = Integer.parseInt(dateFormat.format(cal1.getTime()));

                                try {
                                    ids = snappyDB.get("patientIds", ArrayList.class);
                                    cal1.setTimeInMillis(timeInMillis);
                                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                                    String dat = dateFormat.format(cal1.getTime());
                                    for (int i = 0; i < ids.size(); i++) {
                                        Patient patient = snappyDB.get(ids.get(i), Patient.class);
                                        if ((currentTimeHour + numberPicker.getValue()) < 10 && (currentTimeHour + numberPicker.getValue()) > 18) {
                                            long newTimeMili = (currentTimeHour + numberPicker.getValue() + 16) * 60 * 60 * 1000;
                                            cal1.setTimeInMillis(newTimeMili);
                                            patient.setTime(patient.getTime() + newTimeMili);
                                            patient.setDate(dateFormat2.format(cal1.getTime()));
                                        } else {
                                            cal1.setTimeInMillis((long) (patient.getTime() + emergencyTimeMili));
                                            patient.setTime(patient.getTime() + emergencyTimeMili);
                                            patient.setDate(dateFormat2.format(cal1.getTime()));
                                        }
                                        snappyDB.put(ids.get(i), patient);
                                    }
                                } catch (SnappydbException e) {
                                    e.printStackTrace();
                                }
                                viewPager.getAdapter().notifyDataSetChanged();
                                Snackbar.make(view, "All Appointmets Postponed : " + numberPicker.getValue() + " Hrs", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar.make(view, "Cancelled", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }
        });

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager(), this));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initInstances() {
        setupToolbar();
        setupTablayout();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mEmergencyFab = (FloatingActionButton) findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        try {
            snappyDB = DBFactory.open(this);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void setupTablayout() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
    }


    private void parseJson() {
        storePatients();
        storePatientDetails();
        //Stored flag
        NDTVSharedPreferencesManager.getPreferencesEditor(this).putBoolean(NDTVSharedPreferencesManager.USER_DATA_STORED, true).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_appointments) {

        } else if (id == R.id.nav_chat) {
            startActivity(new Intent(this, ChatActivity.class));


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void storePatients() {
        String jsonString = null;
        //JSONObject jsonObject = null;
        ArrayList<String> patientIds = new ArrayList<>();
        try {
            InputStream is = getResources().openRawResource(R.raw.patients);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }

            jsonString = writer.toString();
            Patients patients = new Gson().fromJson(jsonString, Patients.class);


            //Convert date to Mili's

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            Date date;

            for (int i = 0; i < patients.getPatients().size(); i++) {
                String id = patients.getPatients().get(i).getId();
                String key = String.format(AppConstants.PATIENTS_KEY, id);
                Patient patient = patients.getPatients().get(i);
                String input = (patient.getDate().replace("T", " ")).replace("+01:00", "");
                date = dateFormat.parse(input);
                long milliseconds = date.getTime();
                patient.setTime(milliseconds);
                patient.setDate(dateFormat.format(date));

                snappyDB.put(key, patient);
                patientIds.add(key);
            }

            snappyDB.put("patientIds", patientIds);

            //Stored flag
            NDTVSharedPreferencesManager.getPreferencesEditor(this).putBoolean(NDTVSharedPreferencesManager.PATIENTS, true).commit();

        } catch (IOException ex) {
            ex.printStackTrace();
            //return null;
        } catch (SnappydbException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //return jsonObject;
    }

    public void storePatientDetails() {
        String jsonString = null;
        //JSONObject jsonObject = null;
        ArrayList<String> patientDetailIds = new ArrayList<>();
        try {
            InputStream is = getResources().openRawResource(R.raw.patientdetails);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }

            jsonString = writer.toString();
            // jsonObject = new JSONObject(jsonString);

            PatientDetails patientDetails = new Gson().fromJson(jsonString, PatientDetails.class);

            for (int i = 0; i < patientDetails.getPatientDetails().size(); i++) {
                String id = patientDetails.getPatientDetails().get(i).getId();
                String key = String.format(AppConstants.PATIENTDETAILS_KEY, id);
                String deseases = "";
                PatientDetailObjectModel model = new PatientDetailObjectModel();
                for (int j = 0; j < patientDetails.getPatientDetails().get(i).getKnownDeseases().size(); j++) {
                    deseases += patientDetails.getPatientDetails().get(i).getKnownDeseases().get(j);
                    model.setName(patientDetails.getPatientDetails().get(i).getName());
                    model.setId(patientDetails.getPatientDetails().get(i).getId());
                    model.setComments(patientDetails.getPatientDetails().get(i).getComments());
                    model.setDiagnosis(patientDetails.getPatientDetails().get(i).getDiagnosis());
                    model.setDoctor(patientDetails.getPatientDetails().get(i).getDoctor());
                    model.setMedication(patientDetails.getPatientDetails().get(i).getMedication());
                    model.setSpecialty(patientDetails.getPatientDetails().get(i).getSpecialty());
                    model.setSymptoms(patientDetails.getPatientDetails().get(i).getSymptoms());
                    model.setToBeTaken(patientDetails.getPatientDetails().get(i).getToBeTaken());
                }

                model.setKnownDeseases(deseases);
                //String nxtData = patientDetails.getPatientDetails().get(i).toString();

                snappyDB.put(key, model);
                patientDetailIds.add(id);
            }
            snappyDB.put("patientDetailIds", patientDetailIds);

            //Stored flag
            NDTVSharedPreferencesManager.getPreferencesEditor(this).putBoolean(NDTVSharedPreferencesManager.PATIENT_DETAILS, true).commit();

        } catch (IOException ex) {
            ex.printStackTrace();

        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

}
