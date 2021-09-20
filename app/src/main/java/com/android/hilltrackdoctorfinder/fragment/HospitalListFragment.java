package com.android.hilltrackdoctorfinder.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.adapter.HospitalListAdapter;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.BloodBank;
import com.android.hilltrackdoctorfinder.model.Hospital;
import com.android.hilltrackdoctorfinder.utils.Loading;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalListFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    RecyclerView recyclerView;
    Spinner spinnerUnion;
    public Loading loading;
    private List<Hospital> hospitalList;
    private RecyclerView.LayoutManager layoutManager;
    private HospitalListAdapter adapter;
    private ApiInterface apiInterface;
    String[] union = {"Kaptai", "Chandraghona", "Chitmorom", "Raikhali", "Waggya"};

    public HospitalListFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blood_bank_list, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        spinnerUnion = view.findViewById(R.id.spinnerUnion);
        loading=new Loading(getContext(),false);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        spinnerUnion.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, union);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnion.setAdapter(aa);
        return view;
    }

    public void getSearchedUnionHospitalList(String union) {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Hospital>> call = apiInterface.getSearchedHospital(union);
        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                if (response.code()==200) {
                    loading.end();
//                    Log.e("doctor list", new Gson().toJson(hospitalList));
                    hospitalList = response.body();
                    adapter = new HospitalListAdapter(getActivity(), hospitalList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {
//                Log.e("doctor list failure",t.getLocalizedMessage());
                loading.end();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        getSearchedUnionHospitalList(union[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}