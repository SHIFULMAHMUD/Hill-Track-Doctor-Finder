package com.android.hilltrackdoctorfinder.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.adapter.BloodBankListAdapter;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.BloodBank;
import com.android.hilltrackdoctorfinder.model.Boat;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.utils.Loading;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodBankListFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    RecyclerView recyclerView;
    Spinner spinnerUnion;
    public Loading loading;
    private List<BloodBank> bloodBankList;
    private RecyclerView.LayoutManager layoutManager;
    private BloodBankListAdapter adapter;
    private ApiInterface apiInterface;
    String[] union = {"Kaptai", "Chandraghona", "Chitmorom", "Raikhali", "Waggya"};

    public BloodBankListFragment() {
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

    public void getSearchedUnionbloodBankList(String union) {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<BloodBank>> call = apiInterface.getSearchedBloodBank(union);
        call.enqueue(new Callback<List<BloodBank>>() {
            @Override
            public void onResponse(Call<List<BloodBank>> call, Response<List<BloodBank>> response) {
                if (response.code()==200) {
                    loading.end();
//                    Log.e("doctor list", new Gson().toJson(bloodBankList));
                    bloodBankList = response.body();
                    adapter = new BloodBankListAdapter(getActivity(), bloodBankList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<BloodBank>> call, Throwable t) {
//                Log.e("doctor list failure",t.getLocalizedMessage());
                loading.end();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        getSearchedUnionbloodBankList(union[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}