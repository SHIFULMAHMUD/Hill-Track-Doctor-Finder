package com.android.hilltrackdoctorfinder.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.ambulance.AmbulanceDetailsActivity;
import com.android.hilltrackdoctorfinder.activity.bloodbank.BloodBankDetailsActivity;
import com.android.hilltrackdoctorfinder.model.Ambulance;
import com.android.hilltrackdoctorfinder.model.BloodBank;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BloodBankListAdapter extends RecyclerView.Adapter<BloodBankListAdapter.MyViewHolder>{
    private List<BloodBank> bloodBankList;
    Activity activity;
    public BloodBankListAdapter(Activity activity, List<BloodBank> bloodBankList) {
        this.activity = activity;
        this.bloodBankList = bloodBankList;
    }

    @Override
    public BloodBankListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_bank_list_item, parent, false);
        return new BloodBankListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BloodBankListAdapter.MyViewHolder holder, final int position) {
        holder.nameTextView.setText(bloodBankList.get(position).getName());
        holder.phoneTextView.setText(bloodBankList.get(position).getCell());
        holder.phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+bloodBankList.get(position).getCell()));
                activity.startActivity(callIntent);
            }
        });
        holder.addressTextView.setText(bloodBankList.get(position).getAddress());
    }

    @Override
    public int getItemCount() { return bloodBankList.size(); }

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
            Intent i = new Intent(activity, BloodBankDetailsActivity.class);
            i.putExtra("blood_bank_id", bloodBankList.get(getAdapterPosition()).getId());
            activity.startActivity(i);
            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }
}
