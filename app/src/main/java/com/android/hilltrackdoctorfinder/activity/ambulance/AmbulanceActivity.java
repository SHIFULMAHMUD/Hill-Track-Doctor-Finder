package com.android.hilltrackdoctorfinder.activity.ambulance;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.BaseActivity;
import com.android.hilltrackdoctorfinder.adapter.AmbulanceListAdapter;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Ambulance;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AmbulanceActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.spinnerUnion)
    Spinner spinnerUnion;
    private List<Ambulance> ambulanceList;
    private RecyclerView.LayoutManager layoutManager;
    private AmbulanceListAdapter adapter;
    private ApiInterface apiInterface;
    String[] union = {"Kaptai", "Chandraghona", "Chitmorom", "Raikhali", "Waggya"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat);
        ButterKnife.bind(this);
        title.setText("Get Ambulance");
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
//        getAmbulanceList();
        back.setOnClickListener(this);
        spinnerUnion.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, union);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnion.setAdapter(aa);
    }

//    public void getAmbulanceList() {
//        loading.start();
//        apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<List<Boat>> call = apiInterface.getAllBoats();
//        call.enqueue(new Callback<List<Boat>>() {
//            @Override
//            public void onResponse(Call<List<Boat>> call, Response<List<Boat>> response) {
//                if (response.code()==200) {
//                    loading.end();
////                    Log.e("doctor list", new Gson().toJson(ambulanceList));
//                    ambulanceList = response.body();
//                    adapter = new AmbulanceListAdapter(AmbulanceActivity.this, ambulanceList);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(AmbulanceActivity.this));
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Boat>> call, Throwable t) {
////                Log.e("doctor list failure",t.getLocalizedMessage());
//                loading.end();
//            }
//        });
//    }

    public void getSearchedUnionAmbulanceList(String union) {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Ambulance>> call = apiInterface.getSearchedUnionAmbulance(union);
        call.enqueue(new Callback<List<Ambulance>>() {
            @Override
            public void onResponse(Call<List<Ambulance>> call, Response<List<Ambulance>> response) {
                if (response.code()==200) {
                    loading.end();
//                    Log.e("doctor list", new Gson().toJson(ambulanceList));
                    ambulanceList = response.body();
                    adapter = new AmbulanceListAdapter(AmbulanceActivity.this, ambulanceList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AmbulanceActivity.this));
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Ambulance>> call, Throwable t) {
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
        getSearchedUnionAmbulanceList(union[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}