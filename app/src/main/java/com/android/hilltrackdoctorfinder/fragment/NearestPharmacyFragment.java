package com.android.hilltrackdoctorfinder.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.doctor.DoctorDetailsActivity;
import com.android.hilltrackdoctorfinder.activity.medicine.PharmacyDetailsActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.model.Pharmacy;
import com.android.hilltrackdoctorfinder.utils.Sharedprefer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearestPharmacyFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    int MAX_SIZE=999;
    public Sharedprefer sharedprefer;
    public double userLat;
    public double userLong;
    public String kilo[] = new String[MAX_SIZE];
    public String pharmacyId[]=new String[MAX_SIZE];
    public String pharmacyName[]=new String[MAX_SIZE];
    public double pharmacyLatitude[]=new double[MAX_SIZE];
    public double pharmacyLongitude[]=new double[MAX_SIZE];
    private ApiInterface apiInterface;

    public NearestPharmacyFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_nearest_pharmacy, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sharedprefer = new Sharedprefer(getContext());
        getNearestPharmacy();
        return view;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        getNearestPharmacy();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                String id=marker.getSnippet();
                Intent intent=new Intent(getContext(), PharmacyDetailsActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                return true;
            }
        });

    }

    public void getNearestPharmacy() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Pharmacy>> call = apiInterface.getNearestPharmacy();
        call.enqueue(new Callback<List<Pharmacy>>() {
            @Override
            public void onResponse(Call<List<Pharmacy>> call, Response<List<Pharmacy>> response) {
                if (response.code()==200) {
                    List<Pharmacy> pharmacyList=response.body();
//                    Log.e("doctor list", new Gson().toJson(pharmacyList));
                    if (!pharmacyList.isEmpty()) {
                        for (int i = 0; i < pharmacyList.size(); i++) {
                            final String pharmacy_id = pharmacyList.get(i).getId();
                            final String pharmacy_name = pharmacyList.get(i).getName();
                            final String pharmacy_latitude = pharmacyList.get(i).getLatitude();
                            final String pharmacy_longitude = pharmacyList.get(i).getLongitude();

                            pharmacyLatitude[i] = Double.parseDouble(pharmacy_latitude);
                            pharmacyLongitude[i] = Double.parseDouble(pharmacy_longitude);

                            //insert data into array for put extra
                            pharmacyId[i]=pharmacy_id;
                            pharmacyName[i]=pharmacy_name;
                            userLat = Double.parseDouble(sharedprefer.getLatitude());
                            userLong = Double.parseDouble(sharedprefer.getLongitude());
                            double startLatitude=userLat;
                            double startLongitude=userLong;
                            double endLatitude=pharmacyLatitude[i];
                            double endLongitude=pharmacyLongitude[i];

                            //calculate distance
                            float[]results=new float[1];
                            Location.distanceBetween(startLatitude,startLongitude,endLatitude,endLongitude,results);
                            float distance=results[0];
                            int kilometer= (int) (distance/1000);
                            kilo[i]=""+kilometer;
                            if(kilometer<=200)
                            {
                                LatLng location = new LatLng(endLatitude,endLongitude);
                                mMap.addMarker(new MarkerOptions().position(location).title(pharmacyName[i]).snippet(pharmacyId[i]+","+kilo[i]));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(endLatitude,endLongitude),15.0f));
                            }

                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Pharmacy>> call, Throwable t) {
//                Log.e("doctor list failure",t.getLocalizedMessage());
            }
        });
    }

}
