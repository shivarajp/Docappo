package com.ndtv.ndtvdoc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ndtv.ndtvdoc.R;
import com.ndtv.ndtvdoc.models.Patient;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;

/**
 * Created by Shivam on 4/8/2016.
 */
public class MyAppointmentsAdapter extends RecyclerView.Adapter<MyAppointmentsAdapter.ViewHolder> {
    private ArrayList<Patient> mPatients;
    private Context mContext;
    private DB snappyDb;

    public MyAppointmentsAdapter(Context context, ArrayList<Patient> patients) {
        mPatients = patients;
        mContext = context;
        try {
            snappyDb = DBFactory.open(context);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyAppointmentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_appointment, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tvName.setText(mPatients.get(position).getName());
        holder.tvGender.setText(mPatients.get(position).getGender());
        holder.btMobile.setText(mPatients.get(position).getMobileNo());

        //Picasso.with(mContext).load(mPatients.get(position).getProfile()).placeholder(R.mipmap.ic_launcher).into(holder.ivProfile);

//        Glide.with(mContext).load(mPatients.get(position).getProfile()).into(holder.ivProfile);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPatients.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvName, tvGender;
        Button btMobile, btnTime;
        /*public ImageView ivProfile, ivDecline;
        public CardView cardView;*/

        public ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvName);
            tvGender = (TextView) v.findViewById(R.id.tvGender);
            //ivProfile = (ImageView) v.findViewById(R.id.ivProfile);
            //ivAccept = (Button) v.findViewById(R.id.ivAccept);
            //ivDecline = (ImageView) v.findViewById(R.id.ivDecline);
            btMobile = (Button) v.findViewById(R.id.btMobile);
            //cardView = (CardView) v.findViewById(R.id.cvContainer);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "Item Clicked", Toast.LENGTH_LONG).show();
        }
    }

}
