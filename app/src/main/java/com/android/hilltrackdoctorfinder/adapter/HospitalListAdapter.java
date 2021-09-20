package com.android.hilltrackdoctorfinder.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.bloodbank.BloodBankDetailsActivity;
import com.android.hilltrackdoctorfinder.activity.hospital.HospitalDetailsActivity;
import com.android.hilltrackdoctorfinder.model.BloodBank;
import com.android.hilltrackdoctorfinder.model.Hospital;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HospitalListAdapter extends RecyclerView.Adapter<HospitalListAdapter.MyViewHolder>{
    private List<Hospital> hospitalList;
    Activity activity;
    public HospitalListAdapter(Activity activity, List<Hospital> hospitalList) {
        this.activity = activity;
        this.hospitalList = hospitalList;
    }

    @Override
    public HospitalListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_list_item, parent, false);
        return new HospitalListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HospitalListAdapter.MyViewHolder holder, final int position) {
        holder.nameTextView.setText(hospitalList.get(position).getName());
        holder.phoneTextView.setText(hospitalList.get(position).getCell());
        holder.phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+hospitalList.get(position).getCell()));
                activity.startActivity(callIntent);
            }
        });
        holder.addressTextView.setText(hospitalList.get(position).getAddress());
    }

    @Override
    public int getItemCount() { return hospitalList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTextView,addressTextView,phoneTextView;
        public MyViewHolder(View itemView) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.nameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(activity, HospitalDetailsActivity.class);
            i.putExtra("hospital_id", hospitalList.get(getAdapterPosition()).getId());
            activity.startActivity(i);
            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }
}
