package com.ndtv.ndtvdoc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.ndtvdoc.R;
import com.ndtv.ndtvdoc.adapters.AppointmentRequestAdapter;
import com.ndtv.ndtvdoc.events.UpdateEvent;
import com.ndtv.ndtvdoc.models.Patient;
import com.ndtv.ndtvdoc.utils.AppConstants;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Shivam on 4/8/2016.
 */
public class AppointmentRequestFragment extends Fragment {

    private RecyclerView rvAppointentRequests;
    private DB snaapyDb;
    private ArrayList<Patient> patients = new ArrayList<Patient>();
    AppointmentRequestAdapter adapter;

    public AppointmentRequestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_appointment_request, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvAppointentRequests = (RecyclerView) view.findViewById(R.id.rvAppointentRequests);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvAppointentRequests.setLayoutManager(mLayoutManager);
        rvAppointentRequests.setItemAnimator(new DefaultItemAnimator());
        loadData();
    }

    private void loadData() {
        try {

            snaapyDb = DBFactory.open(getActivity());
            ArrayList<String> ids = snaapyDb.get("patientIds", ArrayList.class);
            for (int i = 0; i < ids.size(); i++) {
                Patient patient = snaapyDb.get(ids.get(i), Patient.class);
                if (patient.getStatus() != 1) {
                    patients.add(patient);
                }
            }

            Collections.sort(patients, new Comparator<Patient>() {
                public int compare(Patient patient1, Patient patient2) {
                    if (patient1.getTime() > patient2.getTime())
                        return 1;
                    else
                        return -1;
                }
            });

            adapter = new AppointmentRequestAdapter(getActivity(), patients, AppConstants.APPOINTMENTS_REQ);
            rvAppointentRequests.setAdapter(adapter);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

    }


    @Subscribe
    public void notifyAdapterNow(UpdateEvent event) {
        patients.clear();
        loadData();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
