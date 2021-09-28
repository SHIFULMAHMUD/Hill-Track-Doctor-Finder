package com.android.hilltrackdoctorfinder.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.doctor.DoctorDetailsActivity;
import com.android.hilltrackdoctorfinder.activity.notifier.NotificationDetailsActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Notification;
import com.android.hilltrackdoctorfinder.utils.Sharedprefer;
import com.android.hilltrackdoctorfinder.utils.Tools;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder>{
    private List<Notification> notificationList;
    AlertDialog.Builder builder;
    Activity activity;
    Sharedprefer sharedprefer;

    public NotificationListAdapter(Activity activity, List<Notification> notificationList) {
        this.activity = activity;
        this.notificationList = notificationList;
    }

    @Override
    public NotificationListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        builder = new AlertDialog.Builder(activity);
        sharedprefer = new Sharedprefer(activity);
        return new NotificationListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationListAdapter.MyViewHolder holder, final int position) {
        holder.title.setText(notificationList.get(position).getTitle());
//        holder.sub_title.setText(notificationList.get(position).getDetails());
        String message = notificationList.get(position).getDetails();
        if (message.length() < 50) {
            holder.sub_title.setText(message);
        } else {
            holder.sub_title.setText((message.substring(0, 49)+ " ...."));
        }
    }

    @Override
    public int getItemCount() { return notificationList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,sub_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            sub_title = itemView.findViewById(R.id.sub_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(activity, NotificationDetailsActivity.class);
            i.putExtra("id", notificationList.get(getAdapterPosition()).getId());
            activity.startActivity(i);
        }
    }
}
