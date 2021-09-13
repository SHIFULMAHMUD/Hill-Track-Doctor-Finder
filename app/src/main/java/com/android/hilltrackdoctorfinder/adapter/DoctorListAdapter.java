package com.android.hilltrackdoctorfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.doctor.DoctorDetailsActivity;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.utils.Urls;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.MyViewHolder>{
    private List<Doctor> doctorList;
    Context context;
    public DoctorListAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    @Override
    public DoctorListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);
        return new DoctorListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorListAdapter.MyViewHolder holder, final int position) {
        holder.nameTextView.setText(doctorList.get(position).getName());
        holder.phoneTextView.setText(doctorList.get(position).getMobile());
        holder.phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+doctorList.get(position).getMobile()));
                context.startActivity(callIntent);
            }
        });
        holder.specialistTextView.setText(doctorList.get(position).getSpecialist());
        Glide.with(context).load(Urls.ImageUrl+doctorList.get(position).getImage()).placeholder(R.drawable.user).into(holder.userImageView);
    }

    @Override
    public int getItemCount() { return doctorList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView,specialistTextView,phoneTextView;
        CircularImageView userImageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.nameTextView);
            userImageView = itemView.findViewById(R.id.userImageView);
            specialistTextView = itemView.findViewById(R.id.specialistTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, DoctorDetailsActivity.class);
            i.putExtra("doctor_id", doctorList.get(getAdapterPosition()).getId());
            context.startActivity(i);
        }
    }
}
