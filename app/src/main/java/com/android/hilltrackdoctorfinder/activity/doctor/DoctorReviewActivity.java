package com.android.hilltrackdoctorfinder.activity.doctor;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.adapter.ReviewListAdapter;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Ambulance;
import com.android.hilltrackdoctorfinder.model.Review;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorReviewActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    private List<Review> reviewList;
    private RecyclerView.LayoutManager layoutManager;
    private ReviewListAdapter adapter;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_review);
        ButterKnife.bind(this);
        title.setText(R.string.all_review);
        String id= getIntent().getStringExtra("id");
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        getReviewList(id);
        back.setOnClickListener(this);
    }

    public void getReviewList(String id) {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Review>> call = apiInterface.getDoctorReview(id);
        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.code()==200) {
                    loading.end();
//                    Log.e("doctor list", new Gson().toJson(reviewList));
                    reviewList = response.body();
                    adapter = new ReviewListAdapter(DoctorReviewActivity.this, reviewList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(DoctorReviewActivity.this));
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
//                Log.e("doctor list failure",t.getLocalizedMessage());
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