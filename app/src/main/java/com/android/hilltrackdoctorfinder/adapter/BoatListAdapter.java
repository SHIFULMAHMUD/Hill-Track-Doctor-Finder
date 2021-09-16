package com.android.hilltrackdoctorfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.doctor.DoctorDetailsActivity;
import com.android.hilltrackdoctorfinder.model.Boat;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.utils.Urls;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BoatListAdapter extends RecyclerView.Adapter<BoatListAdapter.MyViewHolder>{
    private List<Boat> boatList;
    Context context;
    public BoatListAdapter(Context context, List<Boat> boatList) {
        this.context = context;
        this.boatList = boatList;
    }

    @Override
    public BoatListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boat_list_item, parent, false);
        return new BoatListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BoatListAdapter.MyViewHolder holder, final int position) {
        holder.nameTextView.setText(boatList.get(position).getName());
        holder.phoneTextView.setText(boatList.get(position).getMobile());
        holder.phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+boatList.get(position).getMobile()));
                context.startActivity(callIntent);
            }
        });
        holder.addressTextView.setText(boatList.get(position).getAddress());
    }

    @Override
    public int getItemCount() { return boatList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView,addressTextView,phoneTextView;
        public MyViewHolder(View itemView) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.nameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
        }

    }
}
