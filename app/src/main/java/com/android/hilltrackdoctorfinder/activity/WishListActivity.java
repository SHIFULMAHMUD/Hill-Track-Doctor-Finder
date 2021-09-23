package com.android.hilltrackdoctorfinder.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.doctor.DoctorReviewActivity;
import com.android.hilltrackdoctorfinder.adapter.ReviewListAdapter;
import com.android.hilltrackdoctorfinder.adapter.WishListAdapter;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Review;
import com.android.hilltrackdoctorfinder.model.Wishlist;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    private List<Wishlist> wishList;
    private RecyclerView.LayoutManager layoutManager;
    private WishListAdapter adapter;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        ButterKnife.bind(this);
        title.setText("Wishlist");
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        getWishList(sharedprefer.getMobile_number());
        back.setOnClickListener(this);
    }

    private void getWishList(String mobile) {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Wishlist>> call = apiInterface.getWishList(mobile);
        call.enqueue(new Callback<List<Wishlist>>() {
            @Override
            public void onResponse(Call<List<Wishlist>> call, Response<List<Wishlist>> response) {
                loading.end();
                if (response.code()==200) {
                    List<Wishlist> body=response.body();
                    wishList = response.body();
                    adapter = new WishListAdapter(WishListActivity.this, wishList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(WishListActivity.this));
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Wishlist>> call, Throwable t) {
                loading.end();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view==back)
        {
            finish();
        }
    }
}