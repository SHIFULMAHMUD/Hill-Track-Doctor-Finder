package com.android.hilltrackdoctorfinder.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.activity.HomeActivity;
import com.android.hilltrackdoctorfinder.activity.auth.SignInActivity;
import com.android.hilltrackdoctorfinder.activity.doctor.DoctorDetailsActivity;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.model.Login;
import com.android.hilltrackdoctorfinder.model.User;
import com.android.hilltrackdoctorfinder.utils.Sharedprefer;
import com.android.hilltrackdoctorfinder.utils.Tools;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearestDoctorFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    int MAX_SIZE=999;
    public Sharedprefer sharedprefer;
    public double userLat;
    public double userLong;
    public String kilo[] = new String[MAX_SIZE];
    public String doctorId[]=new String[MAX_SIZE];
    public String doctorName[]=new String[MAX_SIZE];
    public double doctorLatitude[]=new double[MAX_SIZE];
    public double doctorLongitude[]=new double[MAX_SIZE];
    private ApiInterface apiInterface;

    public NearestDoctorFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_nearest_doctor, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sharedprefer = new Sharedprefer(getContext());
        getNearestDoctors();
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
        getNearestDoctors();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                String id=marker.getSnippet();
                Intent intent=new Intent(getContext(), DoctorDetailsActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                return true;
            }
        });

    }

    public void getNearestDoctors() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Doctor>> call = apiInterface.getNearestDoctor();
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.code()==200) {
                    List<Doctor> doctorList=response.body();
//                    Log.e("doctor list", new Gson().toJson(doctorList));
                    if (!doctorList.isEmpty()) {
                        for (int i = 0; i < doctorList.size(); i++) {
                            final String doctor_id = doctorList.get(i).getId();
                            final String doctor_name = doctorList.get(i).getName();
                            final String doctor_latitude = doctorList.get(i).getLatitude();
                            final String doctor_longitude = doctorList.get(i).getLongitude();

                            doctorLatitude[i] = Double.parseDouble(doctor_latitude);
                            doctorLongitude[i] = Double.parseDouble(doctor_longitude);

                            //insert data into array for put extra
                            doctorId[i]=doctor_id;
                            doctorName[i]=doctor_name;
                            userLat = Double.parseDouble(sharedprefer.getLatitude());
                            userLong = Double.parseDouble(sharedprefer.getLongitude());
                            double startLatitude=userLat;
                            double startLongitude=userLong;
                            double endLatitude=doctorLatitude[i];
                            double endLongitude=doctorLongitude[i];

                            //calculate distance
                            float[]results=new float[1];
                            Location.distanceBetween(startLatitude,startLongitude,endLatitude,endLongitude,results);
                            float distance=results[0];
                            int kilometer= (int) (distance/1000);
                            kilo[i]=""+kilometer;
                            if(kilometer<=120)
                            {
                                LatLng sydney = new LatLng(endLatitude,endLongitude);
                                mMap.addMarker(new MarkerOptions().position(sydney).title(doctorName[i]).snippet(doctorId[i]+","+kilo[i]));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(endLatitude,endLongitude),15.0f));
                            }

                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
//                Log.e("doctor list failure",t.getLocalizedMessage());
            }
        });
    }

}
