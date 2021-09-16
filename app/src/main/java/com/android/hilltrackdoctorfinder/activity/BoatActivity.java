package com.android.hilltrackdoctorfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.adapter.BoatListAdapter;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Boat;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.utils.Loading;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class BoatActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.spinnerUnion)
    Spinner spinnerUnion;
    private List<Boat> boatList;
    private RecyclerView.LayoutManager layoutManager;
    private BoatListAdapter adapter;
    private ApiInterface apiInterface;
    String[] union = { "Select Union","Kaptai", "Chandraghona", "Chitmorom", "Raikhali", "Waggya"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat);
        ButterKnife.bind(this);
        title.setText("Hire Boat");
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
//        getBoatList();
        back.setOnClickListener(this);
        spinnerUnion.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, union);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnion.setAdapter(aa);
    }

    public void getBoatList() {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Boat>> call = apiInterface.getAllBoats();
        call.enqueue(new Callback<List<Boat>>() {
            @Override
            public void onResponse(Call<List<Boat>> call, Response<List<Boat>> response) {
                if (response.code()==200) {
                    loading.end();
//                    Log.e("doctor list", new Gson().toJson(boatList));
                    boatList = response.body();
                    adapter = new BoatListAdapter(BoatActivity.this, boatList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(BoatActivity.this));
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Boat>> call, Throwable t) {
//                Log.e("doctor list failure",t.getLocalizedMessage());
                loading.end();
            }
        });
    }

    public void getSearchedUnionBoatList(String union) {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Boat>> call = apiInterface.getSearchedUnionBoat(union);
        call.enqueue(new Callback<List<Boat>>() {
            @Override
            public void onResponse(Call<List<Boat>> call, Response<List<Boat>> response) {
                if (response.code()==200) {
                    loading.end();
//                    Log.e("doctor list", new Gson().toJson(boatList));
                    boatList = response.body();
                    adapter = new BoatListAdapter(BoatActivity.this, boatList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(BoatActivity.this));
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Boat>> call, Throwable t) {
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        getSearchedUnionBoatList(union[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}