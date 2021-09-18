package com.android.hilltrackdoctorfinder.activity.covid;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.hilltrackdoctorfinder.R;
import com.android.hilltrackdoctorfinder.api.ApiClient;
import com.android.hilltrackdoctorfinder.api.ApiInterface;
import com.android.hilltrackdoctorfinder.model.Center;
import com.android.hilltrackdoctorfinder.model.Doctor;
import com.android.hilltrackdoctorfinder.utils.Loading;
import com.android.hilltrackdoctorfinder.utils.Sharedprefer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class CovidCenterActivity extends FragmentActivity implements OnMapReadyCallback , View.OnClickListener{
    public Sharedprefer sharedprefer;
    public Loading loading;
    private GoogleMap mMap;
    int MAX_SIZE=999;
    public double userLat;
    public double userLong;
    public String kilo[] = new String[MAX_SIZE];
    public String centerId[]=new String[MAX_SIZE];
    public String centerName[]=new String[MAX_SIZE];
    public String centerCell[]=new String[MAX_SIZE];
    public String centerAddress[]=new String[MAX_SIZE];
    public String centerFacility[]=new String[MAX_SIZE];
    public double centerLatitude[]=new double[MAX_SIZE];
    public double centerLongitude[]=new double[MAX_SIZE];
    private ApiInterface apiInterface;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_center);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sharedprefer = new Sharedprefer(this);
        loading=new Loading(this,false);
        ButterKnife.bind(this);
        title.setText("Nearest Covid Test Center");
        getNearestCenterData();
        back.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(CovidCenterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CovidCenterActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        getNearestCenterData();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                marker.showInfoWindow();
                String id=marker.getSnippet();
                Intent intent=new Intent(CovidCenterActivity.this,CovidCenterDetailsActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                return true;
            }
        });

    }

    public void getNearestCenterData() {
        loading.start();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Center>> call = apiInterface.getNearestCovidCenter();
        call.enqueue(new Callback<List<Center>>() {
            @Override
            public void onResponse(Call<List<Center>> call, Response<List<Center>> response) {
                if (response.code()==200) {
                    loading.end();
                    List<Center> centerData;
                    centerData = response.body();

                    if (!centerData.isEmpty()) {
                        for (int i = 0; i < centerData.size(); i++) {
                            final String center_id = centerData.get(i).getId();
                            final String center_name = centerData.get(i).getName();
                            final String center_cell = centerData.get(i).getCell();
                            final String center_address = centerData.get(i).getAddress();
                            final String center_facility = centerData.get(i).getFacility();
                            final String center_latitude = centerData.get(i).getLatitude();
                            final String center_longitude = centerData.get(i).getLongitude();

                            centerLatitude[i] = Double.parseDouble(center_latitude);
                            centerLongitude[i] = Double.parseDouble(center_longitude);

                            //insert data into array for put extra
                            centerId[i]=center_id;
                            centerName[i]=center_name;
                            centerCell[i] = center_cell;
                            centerAddress[i] = center_address;
                            centerFacility[i] = center_facility;
                            userLat = Double.parseDouble(sharedprefer.getLatitude());
                            userLong = Double.parseDouble(sharedprefer.getLongitude());
                            double startLatitude=userLat;
                            double startLongitude=userLong;
                            double endLatitude=centerLatitude[i];
                            double endLongitude=centerLongitude[i];

                            //calculate distance //
                            float[]results=new float[1];
                            Location.distanceBetween(startLatitude,startLongitude,endLatitude,endLongitude,results);
                            float distance=results[0];
                            int kilometer= (int) (distance/1000);
                            kilo[i]=""+kilometer;
                            if(kilometer<=200)

                            {
                                LatLng location = new LatLng(endLatitude,endLongitude);
                                mMap.addMarker(new MarkerOptions().position(location).title(centerName[i]).snippet(centerId[i]+","+kilo[i]));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(endLatitude,endLongitude),15.0f));
                            }

                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Center>> call, Throwable t) {
//                Log.e("doctor list failure",t.getLocalizedMessage());
                loading.end();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view==back){
            finish();
        }
    }
}