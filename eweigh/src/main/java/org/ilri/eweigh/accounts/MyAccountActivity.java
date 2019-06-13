package org.ilri.eweigh.accounts;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.ilri.eweigh.R;
import org.ilri.eweigh.utils.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class MyAccountActivity extends AppCompatActivity {
    private static final String TAG = MyAccountActivity.class.getSimpleName();

    private static final int RC_READ_PHONE_STATE = 100;

    URL urlStr = new URL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Account");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        AccountUtils accountUtils = new AccountUtils(this);
        User accountUser = accountUtils.getUserDetails();

        TextView profileName = findViewById(R.id.txt_profile_name);
        profileName.setText(accountUser.getFullName());

        TextView profileEmail = findViewById(R.id.txt_profile_email);
        profileEmail.setText(accountUser.getEmail());

        TextView profileMobile = findViewById(R.id.txt_profile_mobile);
        profileMobile.setText(accountUser.getMobile());

        TextView profileGroup = findViewById(R.id.txt_profile_group);
        profileGroup.setText(accountUser.getGroupName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
