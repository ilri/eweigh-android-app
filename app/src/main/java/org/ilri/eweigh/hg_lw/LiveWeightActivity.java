package org.ilri.eweigh.hg_lw;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
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
import org.ilri.eweigh.accounts.AccountUtils;
import org.ilri.eweigh.accounts.models.User;
import org.ilri.eweigh.cattle.models.Cattle;
import org.ilri.eweigh.hg_lw.models.Submission;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.utils.Constants;
import org.ilri.eweigh.utils.URL;
import org.ilri.eweigh.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import locationprovider.davidserrano.com.LocationProvider;

public class LiveWeightActivity extends AppCompatActivity {
    private static final String TAG = LiveWeightActivity.class.getSimpleName();

    EditText inputHG;
    TextView txtLW, txtMeta;

    double latitude = 0;
    double longitude = 0;

    Cattle cattle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_live_weight);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Get Live-Weight");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }

        /*
        *
        * Fetch the cattle's ID passed from the CattleViewActivity and associate with final LW
        *
        * */
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            cattle = (Cattle) bundle.getSerializable(Cattle.CATTLE);
        }

        inputHG = findViewById(R.id.input_hg);
        inputHG.requestFocus();

        txtLW = findViewById(R.id.txt_lw);
        txtMeta = findViewById(R.id.txt_metadata);

        Button btn = findViewById(R.id.btn_submit_hg);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(inputHG.getText().toString())){
                    inputHG.setError("Enter heart girth");
                }
                else{
                    getLiveWeight();
                }
            }
        });

        checkPermission();

        Fonty.setFonts(this);
    }

    private void getLiveWeight(){
        APIService apiService = new APIService(this);

        final ProgressDialog progressDialog = Utils.getProgressDialog(this, "Processing...", false);
        final AlertDialog alertDialog = Utils.getSimpleDialog(LiveWeightActivity.this, "");

        progressDialog.show();

        AccountUtils accountUtils = new AccountUtils(this);
        User user = accountUtils.getUserDetails();

        RequestParams params = new RequestParams();

        /*
        *
        * If cattle object has been passed, then fill these values
        *
        * */
        if(cattle != null){
            params.put(Cattle.CATTLE, String.valueOf(cattle.getId()));
        }

        params.put(User.ID, String.valueOf(user.getUserId()));
        params.put(Submission.HG, inputHG.getText().toString());
        params.put(Submission.LAT, String.valueOf(latitude));
        params.put(Submission.LNG, String.valueOf(longitude));

        apiService.post(URL.GetLiveWeight, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(response);

                    boolean error = obj.optBoolean("error", false);
                    String message = obj.optString("message", "_");

                    if(error){
                        alertDialog.setMessage(message);
                        alertDialog.show();
                    }
                    else{
                        double LW = obj.optDouble(Submission.LW, 0);
                        txtLW.setText(String.valueOf(LW));

                        String meta = "Coordinates: " +
                                obj.optString(Submission.LAT, "0") + ", " +
                                obj.optString(Submission.LNG, "0");
                        // txtMeta.setText(meta);

                        if(cattle != null){

                            /*
                            *
                            * Return value to previous page
                            *
                            * */
                            Intent intent = new Intent();
                            intent.putExtra(Cattle.LIVE_WEIGHT, LW);
                            setResult(Activity.RESULT_OK, intent);

                            finish();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Post: " + error.getLocalizedMessage());
                Toast.makeText(LiveWeightActivity.this, "Could not post data",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                alertDialog.setMessage("Could not post data");
                alertDialog.show();
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

    private void checkGPS(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gpsEnabled && !networkEnabled) {

            new AlertDialog.Builder(this)
                    .setMessage("GPS is not enabled")
                    .setPositiveButton("Open Location Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setCancelable(false)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
        else{
            initLocationUpdates();
        }
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
                                    ActivityCompat.requestPermissions(LiveWeightActivity.this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            Constants.RC_PERMISSION_REQUEST_LOCATION);
                                }
                            })
                            .create()
                            .show();
                }
                else{

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constants.RC_PERMISSION_REQUEST_LOCATION);
                }
            }
            else{
                checkGPS();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case Constants.RC_PERMISSION_REQUEST_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED){

                        checkGPS();
                    }

                }
                else{
                    Toast.makeText(this, "You need to allow location permission",
                            Toast.LENGTH_SHORT).show();
                    checkGPS();
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
