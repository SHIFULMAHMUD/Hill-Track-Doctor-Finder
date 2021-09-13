package com.android.hilltrackdoctorfinder.fragment;


import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.adapter.DoctorListAdapter;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.utils.Loading;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorListFragment extends Fragment {
    private List<Doctor> doctorList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DoctorListAdapter adapter;
    private ApiInterface apiInterface;
    Button btnSearch;
    EditText txtSearch;
    public Loading loading;
    public DoctorListFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_list, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        btnSearch=view.findViewById(R.id.btn_search);
        txtSearch=view.findViewById(R.id.txt_search);
        loading=new Loading(getContext(),false);
        getDoctorsList();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String search=txtSearch.getText().toString().trim();

                if (search.isEmpty())
                {
                    Toasty.info(getActivity(), "Write Doctor Name!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getSearchData(search);

                }

            }
        });

        return view;
    }
    public void getDoctorsList() {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Doctor>> call = apiInterface.getNearestDoctor();
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.code()==200) {
                    loading.end();
//                    Log.e("doctor list", new Gson().toJson(doctorList));
                    doctorList = response.body();
                    adapter = new DoctorListAdapter(getActivity(), doctorList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
//                Log.e("doctor list failure",t.getLocalizedMessage());
                loading.end();
            }
        });
    }
    public void getSearchData(String text) {
        loading.start();
        Call<List<Doctor>> call = apiInterface.getSearchedDoctor(text);
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.code() == 200) {
                    loading.end();
                    doctorList = response.body();
                    adapter = new DoctorListAdapter(getActivity(), doctorList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();//for search
                }
            }
            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                loading.end();
            }
        });
    }
}
