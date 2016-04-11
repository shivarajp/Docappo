package com.ndtv.ndtvdoc.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ndtv.ndtvdoc.R;
import com.ndtv.ndtvdoc.models.Patient;
import com.ndtv.ndtvdoc.models.PatientDetail;
import com.ndtv.ndtvdoc.models.PatientDetails;
import com.ndtv.ndtvdoc.utils.AppConstants;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private Patient patient;
    private PatientDetail detail;
    private PatientDetails patientDetails;
    private DB snappyDb;

    private TextView tvPatientAddress, tvPatientDiagnosis,
            tvPatientName, tvPatientGender, tvPatientBloodgroup,
            tvPatientSymptoms, tvPatientMedication,
            tvPatientPhone, tvPatientDeseasesList, tvPatientDocName,
            tvPatientTobetaken, tvPatientDocSpeciality, tvPatientComments;
    private ImageView ivPatientPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intiate();
        parseDetails();

        try {
            snappyDb = DBFactory.open(this);
            patient = (Patient) getIntent().getSerializableExtra("patient");
            if (patient != null) {
                String key = String.format(AppConstants.PATIENTDETAILS_KEY, patient.getId());
                //String data = toString(snappyDb.get(key));
                detail = patientDetails.getPatientDetails().get(Integer.parseInt(patient.getId()));
            }
            setData();
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

                /*if (patient.getStatus() != 1) {
                    patient.setStatus(1);
                    try {
                        snappyDb.put(String.format(AppConstants.PATIENTS_KEY, patient.getId()), patient);
                        Snackbar.make(view, "Appointment Accepted", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        EventBus.getDefault().post(new UpdateEvent("update"));

                    } catch (SnappydbException e) {
                        e.printStackTrace();
                    }
                } else if (patient.getStatus() == 1) {
                    Snackbar.make(view, "Patient is in your appointment list", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
*/

            }
        });
    }

    private void parseDetails() {
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
            patientDetails = new Gson().fromJson(jsonString, PatientDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        if (detail != null && patient != null) {
            String deseases = "";
            tvPatientAddress.setText(patient.getAddress());
            tvPatientDiagnosis.setText(detail.getDiagnosis());
            tvPatientName.setText(patient.getName());
            tvPatientGender.setText(patient.getGender());
            tvPatientBloodgroup.setText(patient.getBloodGroup());
            tvPatientSymptoms.setText(detail.getSymptoms());
            tvPatientMedication.setText(detail.getMedication());
            tvPatientPhone.setText(patient.getMobileNo());
            tvPatientDocName.setText(detail.getDoctor());
            tvPatientTobetaken.setText(detail.getToBeTaken());
            tvPatientDocSpeciality.setText(detail.getSpecialty());
            Picasso.with(this).load(patient.getProfile()).placeholder(R.mipmap.ic_launcher).into(ivPatientPic);
            //Glide.with(this).load(patient.getProfile()).into(ivPatientPic);
            String data = "";
            for (int i = 0; i < detail.getKnownDeseases().size(); i++) {
                data += detail.getKnownDeseases().get(i);
            }

            tvPatientDeseasesList.setText(data);
            tvPatientComments.setText(detail.getComments());
        }
    }

    private void intiate() {
        tvPatientAddress = (TextView) findViewById(R.id.tvPatientAddress);
        tvPatientDiagnosis = (TextView) findViewById(R.id.tvPatientDiagnosis);
        tvPatientName = (TextView) findViewById(R.id.tvPatientName);
        tvPatientGender = (TextView) findViewById(R.id.tvPatientGender);
        tvPatientBloodgroup = (TextView) findViewById(R.id.tvPatientBloodgroup);
        tvPatientSymptoms = (TextView) findViewById(R.id.tvPatientSymptoms);
        tvPatientMedication = (TextView) findViewById(R.id.tvPatientMedication);
        tvPatientPhone = (TextView) findViewById(R.id.tvPatientPhone);
        tvPatientDeseasesList = (TextView) findViewById(R.id.tvPatientDeseasesList);
        tvPatientDocName = (TextView) findViewById(R.id.tvPatientDocName);
        tvPatientTobetaken = (TextView) findViewById(R.id.tvPatientTobetaken);
        tvPatientDocSpeciality = (TextView) findViewById(R.id.tvPatientDocSpeciality);
        tvPatientComments = (TextView) findViewById(R.id.tvPatientComments);
        ivPatientPic = (ImageView) findViewById(R.id.ivPatientPic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_home, menu);
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

    private String toString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.encodeToString(baos.toByteArray(), 0);
    }
}
