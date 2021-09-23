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
import com.android.hilltrackdoctorfinder.activity.doctor.DoctorReviewActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Review;
import com.android.hilltrackdoctorfinder.model.Wishlist;
import com.android.hilltrackdoctorfinder.utils.Sharedprefer;
import com.android.hilltrackdoctorfinder.utils.Tools;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder>{
    private List<Wishlist> wishList;
    AlertDialog.Builder builder;
    Activity activity;
    Sharedprefer sharedprefer;
    private ApiInterface apiInterface;
    public WishListAdapter(Activity activity, List<Wishlist> wishList) {
        this.activity = activity;
        this.wishList = wishList;
    }

    @Override
    public WishListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item, parent, false);
        builder = new AlertDialog.Builder(activity);
        sharedprefer = new Sharedprefer(activity);
        return new WishListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WishListAdapter.MyViewHolder holder, final int position) {
        holder.textViewDoctorName.setText(wishList.get(position).getDoc_name());
        holder.textViewSpeciality.setText(wishList.get(position).getDoc_specialty());
        holder.ratingBar.setRating(Float.parseFloat(wishList.get(position).getRating()));
        holder.favouriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Remove from Wishlist ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                                Call<Wishlist> call = apiInterface.updateWishList(wishList.get(position).getDoc_id(),sharedprefer.getMobile_number(),"0");
                                call.enqueue(new Callback<Wishlist>() {
                                    @Override
                                    public void onResponse(Call<Wishlist> call, Response<Wishlist> response) {
                                        if (response.code()==200) {

                                            Wishlist body=response.body();
                                            if (body.getValue().equals("success"))
                                            {
                                                removeAt(position);
                                                Tools.setSuccessToast(activity,body.getMessage());
                                            }
                                            else if (body.getValue().equals("failure"))
                                            {
                                                Tools.setErrorToast(activity,body.getMessage());
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Wishlist> call, Throwable t) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        holder.buttonVisitDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, DoctorDetailsActivity.class);
                intent.putExtra("doctor_id",wishList.get(position).getDoc_id());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    @Override
    public int getItemCount() { return wishList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewDoctorName,textViewSpeciality;
        RatingBar ratingBar;
        Button buttonVisitDoctor;
        ImageButton favouriteImageButton;
        public MyViewHolder(View itemView) {
            super(itemView);
            textViewDoctorName=itemView.findViewById(R.id.textViewDoctorName);
            textViewSpeciality = itemView.findViewById(R.id.textViewSpeciality);
            buttonVisitDoctor = itemView.findViewById(R.id.buttonVisitDoctor);
            favouriteImageButton = itemView.findViewById(R.id.favouriteImageButton);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
    public void removeAt(int position) {
        wishList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, wishList.size());
    }
}
