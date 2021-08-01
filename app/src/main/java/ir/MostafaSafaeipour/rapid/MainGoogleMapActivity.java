package ir.MostafaSafaeipour.rapid;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import ir.MostafaSafaeipour.rapid.RapidAPI;

public class MainGoogleMapActivity extends FragmentActivity implements OnMapReadyCallback,RapidAPIEvents {

    private GoogleMap mMap;
    private ScooterDetailBlankFragment ScooterDetailFrag;
    private TripDetailBlankFragment TripDetailFrag;

    private  Button scanbarcode;
    private  Button currentloc;

    private boolean isStartbtn;

    private AlertDialog.Builder builder;

    private RapidAPI rapidapi = new RapidAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main_google_map);

        rapidapi.addListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ScooterDetailFrag = (ScooterDetailBlankFragment) getSupportFragmentManager().findFragmentById(R.id.ScooterDetailFragment);
        TripDetailFrag = (TripDetailBlankFragment) getSupportFragmentManager().findFragmentById(R.id.TripDetailFragment);

        /////ScooterDetailButtons
        isStartbtn=true;
        ScooterDetailFrag.Startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStartbtn) {
                    builder = new AlertDialog.Builder(MainGoogleMapActivity.this);
                    builder.setMessage("آیا می خواهید سفر را شروع کنید؟(توجه کنید در صورتی که سفر شروع شود از حساب شما هزینه سفر کم می شود و همچنین قفل چرخ ها باز می شود)");
                    builder.setCancelable(false);
                    builder.setTitle("توجه!");
                    builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                rapidapi.getStartTrip(ScooterDetailFrag.getScooterIDOfScooterDetailFragment(),"09121111111");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();

                    isStartbtn = false;
                }else{
                    builder = new AlertDialog.Builder(MainGoogleMapActivity.this);
                    builder.setMessage("آیا می خواهید سفر را تمام کنید؟(توجه کنید در صورتی که سفر شما به اتمام برسد چرخ ها قفل می شوند و همچنین از جای پارک اسکوتر اطمینان حاصل کنید)");
                    builder.setCancelable(false);
                    builder.setTitle("توجه!");
                    builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //showControlMapButtons();
                            try {
                                rapidapi.getEndTrip(ScooterDetailFrag.getScooterIDOfScooterDetailFragment(),"09121111111");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();

                    isStartbtn=true;
                }
            }
        });

        ScooterDetailFrag.GrayExitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showControlMapButtons();
            }
        });
        ///////////////////////

        ///TripDetailButtons
        TripDetailFrag.ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showControlMapButtons();
            }
        });
        ///////////////////

        scanbarcode = (Button) findViewById(R.id.ScanBarcodeButton);
        scanbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToQrCodeScannerIntent = new Intent(getApplicationContext(),QrCodeScannerActivity.class);
                startActivityForResult(goToQrCodeScannerIntent,1);
            }
        });

        currentloc = (Button) findViewById(R.id.CurrentLocationButton);
        currentloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(MainGoogleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainGoogleMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermission
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null){
                    System.out.println("is Null");
                }else{
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    mMap.animateCamera(cameraUpdate);
                }
            }
        });
        showControlMapButtons();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            String QrCodeValue = data.getStringExtra("QrCode");
            try {
                rapidapi.getOneScooterDetail(QrCodeValue);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null){
            System.out.println("is Null");
        }else{
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            LatLng latLng = new LatLng(latitude, longitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(cameraUpdate);
        }

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    rapidapi.getScooterDetail();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000);


    }

    private void showControlMapButtons(){
        scanbarcode.setVisibility(View.VISIBLE);
        currentloc.setVisibility(View.VISIBLE);
        ScooterDetailFrag.getView().setVisibility(View.GONE);
        TripDetailFrag.getView().setVisibility(View.GONE);
    }

    private void showScooterDetailFrag(){
        scanbarcode.setVisibility(View.GONE);
        currentloc.setVisibility(View.GONE);
        ScooterDetailFrag.getView().setVisibility(View.VISIBLE);
        TripDetailFrag.getView().setVisibility(View.GONE);
    }

    private void showTripDetailFrag(){
        scanbarcode.setVisibility(View.GONE);
        currentloc.setVisibility(View.GONE);
        ScooterDetailFrag.getView().setVisibility(View.GONE);
        TripDetailFrag.getView().setVisibility(View.VISIBLE);
    }

    private void addMarkersToMap(ScooterDetailObject scooters){
        for (int i=0;i<scooters.List.size();i++){
            int pinaddress;
            if (scooters.List.get(i).Battery >= 0 && scooters.List.get(i).Battery <= 34)
                pinaddress = R.drawable.low_pin;
            else if (scooters.List.get(i).Battery >= 35 && scooters.List.get(i).Battery <= 69)
                pinaddress = R.drawable.medium_pin;
            else
                pinaddress = R.drawable.high_pin;

            mMap.addMarker(new MarkerOptions()
            .position(new LatLng(scooters.List.get(i).Latitude,scooters.List.get(i).Longitude))
            .icon(BitmapDescriptorFactory.fromResource(pinaddress)));

        }
    }

    @Override
    public void ScooterDetailReady(final ScooterDetailObject object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.clear();
                addMarkersToMap(object);
            }
        });
    }

    @Override
    public void OneScooterDetailReady(final ScooterDetailObject object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (object.Status.equals("NotAvailable"))
                    showControlMapButtons();
                else if (object.Status.equals("OK")) {
                    ScooterDetailFrag.setScooterIDOfScooterDetailFragment(object.List.get(0).ScooterID);
                    ScooterDetailFrag.setBatteryTextViewText(String.valueOf(object.List.get(0).Battery));
                    showScooterDetailFrag();
                }
            }
        });
    }

    @Override
    public void CheckScooterAvailableReady(final StatusJson object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (object.Status.equals("Available"))
                    showScooterDetailFrag();
                else if (object.Status.equals("NotAvailable"))
                    showControlMapButtons();
            }
        });
    }

    @Override
    public void StartTripReady(final StatusJson object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (object.Status.equals("Sucess")) {
                    ScooterDetailFrag.Startbtn.setBackgroundResource(R.drawable.endtripbutton);
                    ScooterDetailFrag.GrayExitbtn.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void EndTripReady(final StatusJson object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (object.Status.equals("Sucess")) {
                    ScooterDetailFrag.Startbtn.setBackgroundResource(R.drawable.startbutton);
                    ScooterDetailFrag.GrayExitbtn.setVisibility(View.VISIBLE);
                    showTripDetailFrag();
                }
            }
        });
    }



}