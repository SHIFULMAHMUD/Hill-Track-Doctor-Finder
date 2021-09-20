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
import com.android.hilltrackdoctorfinder.model.Ambulance;
import com.android.hilltrackdoctorfinder.model.Boat;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AmbulanceListAdapter extends RecyclerView.Adapter<AmbulanceListAdapter.MyViewHolder>{
    private List<Ambulance> ambulanceList;
    Activity activity;
    public AmbulanceListAdapter(Activity activity, List<Ambulance> ambulanceList) {
        this.activity = activity;
        this.ambulanceList = ambulanceList;
    }

    @Override
    public AmbulanceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ambulance_list_item, parent, false);
        return new AmbulanceListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AmbulanceListAdapter.MyViewHolder holder, final int position) {
        holder.nameTextView.setText(ambulanceList.get(position).getName());
        holder.phoneTextView.setText(ambulanceList.get(position).getMobile());
        holder.phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+ambulanceList.get(position).getMobile()));
                activity.startActivity(callIntent);
            }
        });
        holder.addressTextView.setText(ambulanceList.get(position).getAddress());
    }

    @Override
    public int getItemCount() { return ambulanceList.size(); }

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
            Intent i = new Intent(activity, AmbulanceDetailsActivity.class);
            i.putExtra("id", ambulanceList.get(getAdapterPosition()).getId());
            i.putExtra("name", ambulanceList.get(getAdapterPosition()).getName());
            i.putExtra("mobile", ambulanceList.get(getAdapterPosition()).getMobile());
            i.putExtra("facility", ambulanceList.get(getAdapterPosition()).getFacility());
            i.putExtra("address", ambulanceList.get(getAdapterPosition()).getAddress());
            i.putExtra("union", ambulanceList.get(getAdapterPosition()).getUnions());
            i.putExtra("image", ambulanceList.get(getAdapterPosition()).getImage());
            activity.startActivity(i);
            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }
}
