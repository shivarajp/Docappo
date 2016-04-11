package com.ndtv.ndtvdoc.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ndtv.ndtvdoc.activities.MainActivity;
import com.ndtv.ndtvdoc.fragments.AppointmentRequestFragment;
import com.ndtv.ndtvdoc.fragments.MyAppointmentsFragment;

/**
 * Created by Shivam on 4/8/2016.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public HomePagerAdapter(FragmentManager supportFragmentManager, MainActivity mainActivity) {
        super(supportFragmentManager);
        context = mainActivity;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AppointmentRequestFragment();
            case 1:
                return new MyAppointmentsFragment();
            default:
                return new MyAppointmentsFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Requests";
            case 1:
                return "Appointments";
            default:
                return "Requests";
        }
    }
}
