package com.android.hilltrackdoctorfinder.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.ambulance.AmbulanceDetailsActivity;
import com.android.hilltrackdoctorfinder.model.Ambulance;
import com.android.hilltrackdoctorfinder.model.Review;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolder>{
    private List<Review> reviewList;
    Activity activity;
    public ReviewListAdapter(Activity activity, List<Review> reviewList) {
        this.activity = activity;
        this.reviewList = reviewList;
    }

    @Override
    public ReviewListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewListAdapter.MyViewHolder holder, final int position) {
        holder.nameTextView.setText(reviewList.get(position).getReviewer());
        holder.reviewTextView.setText(reviewList.get(position).getReview());
        holder.ratingTextView.setText(reviewList.get(position).getRating());
        holder.ratingBar.setRating(Float.parseFloat(reviewList.get(position).getRating()));
    }

    @Override
    public int getItemCount() { return reviewList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView,reviewTextView,ratingTextView;
        RatingBar ratingBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.nameTextView);
            reviewTextView = itemView.findViewById(R.id.reviewTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
