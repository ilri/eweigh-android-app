package org.ilri.eweigh.hg_lw;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.R;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.utils.URL;

import locationprovider.davidserrano.com.LocationProvider;

public class ConvertActivity extends AppCompatActivity {
    private static final String TAG = ConvertActivity.class.getSimpleName();

    private static final int RC_PERMISSION_REQUEST_LOCATION = 100;

    EditText inputHG;
    TextView txtLW;

    double latitude = 0;
    double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Get Live-Weight");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        inputHG = findViewById(R.id.input_hg);
        inputHG.requestFocus();

        txtLW = findViewById(R.id.txt_lw);

        Button btn = findViewById(R.id.btn_submit_hg);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLiveWeight();
            }
        });

        checkPermission();

        Fonty.setFonts(this);
    }

    private void getLiveWeight(){
        APIService apiService = new APIService(this);

        RequestParams params = new RequestParams();
        params.put("hg", inputHG.getText().toString());
        params.put("lat", String.valueOf(latitude));
        params.put("lng", String.valueOf(longitude));

        apiService.post(URL.GetLiveWeight, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                Toast.makeText(ConvertActivity.this, response, Toast.LENGTH_LONG).show();

                txtLW.setText(String.format("%s%s", getString(R.string.live_weight_label), response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ConvertActivity.this, "An error occurred",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initLocationUpdates(){

        LocationProvider.LocationCallback callback = new LocationProvider.LocationCallback() {
            @Override
            public void onNewLocationAvailable(float lat, float lng) {
                latitude = lat;
                longitude = lng;
            }

            @Override
            public void locationServicesNotEnabled() {}

            @Override
            public void updateLocationInBackground(float lat, float lng) {
                latitude = lat;
                longitude = lng;
            }

            @Override
            public void networkListenerInitialised() {}

            @Override
            public void locationRequestStopped() {}
        };

        LocationProvider locationProvider = new LocationProvider.Builder()
                .setContext(this)
                .setListener(callback)
                .create();

        locationProvider.requestLocation();
    }

    private void checkPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Provide an explanation
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    new AlertDialog.Builder(this)
                            .setTitle("Location permission")
                            .setMessage(R.string.location_permission)
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    // Prompt the user once explanation has been shown
                                    ActivityCompat.requestPermissions(ConvertActivity.this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            RC_PERMISSION_REQUEST_LOCATION);
                                }
                            })
                            .create()
                            .show();
                }
                else{

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            RC_PERMISSION_REQUEST_LOCATION);
                }
            }
            else{
                initLocationUpdates();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RC_PERMISSION_REQUEST_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED){

                        //Request location updates:
                        initLocationUpdates();
                    }

                }
                else{
                    Toast.makeText(this, "You need to allow location permission",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
