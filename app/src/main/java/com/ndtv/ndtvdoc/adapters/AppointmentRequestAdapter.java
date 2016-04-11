package com.ndtv.ndtvdoc.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ndtv.ndtvdoc.R;
import com.ndtv.ndtvdoc.activities.DetailsActivity;
import com.ndtv.ndtvdoc.events.UpdateEvent;
import com.ndtv.ndtvdoc.models.Patient;
import com.ndtv.ndtvdoc.utils.AppConstants;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Shivam on 4/8/2016.
 */
public class AppointmentRequestAdapter extends RecyclerView.Adapter<AppointmentRequestAdapter.ViewHolder> {
    private ArrayList<Patient> mPatients;
    private Context mContext;
    private DB snappyDb;
    private int from;

    public AppointmentRequestAdapter(Context context, ArrayList<Patient> patients, int appointments) {
        mPatients = patients;
        mContext = context;
        from = appointments;
        try {
            snappyDb = DBFactory.open(context);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AppointmentRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment_request, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tvName.setText(mPatients.get(position).getName());
        holder.tvGender.setText(mPatients.get(position).getGender());
        holder.btMobile.setText(mPatients.get(position).getMobileNo());
        holder.tvDate.setText(mPatients.get(position).getDate());

        if (from == AppConstants.MY_APPOINTMENTS) {
            holder.btnAccept.setVisibility(View.INVISIBLE);
            holder.btnDecline.setVisibility(View.INVISIBLE);
        }else {
            holder.btnDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mPatients.get(position).setStatus(0);
                        snappyDb.put(String.format(AppConstants.PATIENTS_KEY, mPatients.get(position).getId()), mPatients.get(position));
                        mPatients.remove(position);
                        notifyDataSetChanged();
                        Snackbar.make(v, "Appointment Declined", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mPatients.get(position).setStatus(1);
                        snappyDb.put(String.format(AppConstants.PATIENTS_KEY, mPatients.get(position).getId()), mPatients.get(position));
                        mPatients.remove(position);
                        Snackbar.make(v, "Appointment Accepted", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        EventBus.getDefault().post(new UpdateEvent("notifyAdapter"));

                    } catch (SnappydbException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("patient", mPatients.get(position));
                mContext.startActivity(intent);
            }
        });





        Picasso.with(mContext).load(mPatients.get(position).getProfile()).placeholder(R.drawable.bookig).into(holder.ivProfile);

//        Glide.with(mContext).load(mPatients.get(position).getProfile()).into(holder.ivProfile);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPatients.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvName, tvGender, tvDate;
        Button btMobile, btnAccept, btnDecline;
        public ImageView ivProfile;
        public CardView cardView;

        public ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvName);
            tvGender = (TextView) v.findViewById(R.id.tvGender);
            tvDate = (TextView) v.findViewById(R.id.tvDate);
            ivProfile = (ImageView) v.findViewById(R.id.ivProfile);
            btnAccept = (Button) v.findViewById(R.id.btnAccept);
            btnDecline = (Button) v.findViewById(R.id.btnDecline);
            btMobile = (Button) v.findViewById(R.id.btMobile);
            cardView = (CardView) v.findViewById(R.id.cvContainer);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "Item Clicked", Toast.LENGTH_LONG).show();
        }
    }
}
