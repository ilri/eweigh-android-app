package org.ilri.eweigh.accounts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.ilri.eweigh.R;
import org.ilri.eweigh.accounts.models.User;
import org.ilri.eweigh.misc.Country;
import org.ilri.eweigh.misc.County;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.utils.URL;
import org.ilri.eweigh.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    Button btnRegister;
    EditText inputName, inputEmail, inputMobile, inputIdNumber, inputCompany, inputPassword;
    Spinner spinnerCountries, spinnerCounties;

    private ProgressDialog progressDialog;

    private AccountUtils accountUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Create Account");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        accountUtils = new AccountUtils(this);

        progressDialog = Utils.getProgressDialog(this, "Registering...", false);

        inputName = findViewById(R.id.input_full_name);
        inputEmail = findViewById(R.id.input_email);
        inputMobile = findViewById(R.id.input_mobile);
        inputIdNumber = findViewById(R.id.input_id_number);
        inputPassword = findViewById(R.id.input_password);

        spinnerCountries = findViewById(R.id.spinner_countries);
        spinnerCounties = findViewById(R.id.spinner_counties);

        // populate countries
        ArrayAdapter<Country> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Utils.getCountriesList());
        spinnerCountries.setAdapter(adapter);

        // populate counties
        ArrayAdapter<County> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Utils.getCountiesList());
        spinnerCounties.setAdapter(adapter2);

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser(){
        APIService apiService = new APIService(this);

        Country ctr = (Country) spinnerCountries.getSelectedItem();
        County ct = (County) spinnerCounties.getSelectedItem();

        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String mobile = inputMobile.getText().toString();
        String idNumber = inputIdNumber.getText().toString();
        String country = String.valueOf(ctr.getId());
        String county = String.valueOf(ct.getId());
        String password = inputPassword.getText().toString();

        if(name.equals("")){
            Toast.makeText(this, "Full Name is required", Toast.LENGTH_SHORT).show();
        }
        else if(email.equals("")){
            Toast.makeText(this, "Email Address is required", Toast.LENGTH_SHORT).show();
        }
        else if(mobile.equals("")){
            Toast.makeText(this, "Mobile No. is required", Toast.LENGTH_SHORT).show();
        }
        else if(idNumber.equals("")){
            Toast.makeText(this, "ID Number is required", Toast.LENGTH_SHORT).show();
        }
        else if(country.equals("")){
            Toast.makeText(this, "Country is required", Toast.LENGTH_SHORT).show();
        }
        else if(county.equals("")){
            Toast.makeText(this, "County is required", Toast.LENGTH_SHORT).show();
        }
        else if(password.equals("")){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
        }
        else{

            progressDialog.show();

            RequestParams params = new RequestParams();
            params.put(User.FULL_NAME, name);
            params.put(User.EMAIL, email);
            params.put(User.MOBILE, mobile);
            params.put(User.ID_NUMBER, idNumber);
            params.put(User.COUNTRY, country);
            params.put(User.COUNTY, county);
            params.put(User.PASSWORD, password);

            apiService.post(URL.Register, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    progressDialog.dismiss();

                    try {
                        JSONObject res = new JSONObject(response);

                        if(res.has("message")){
                            Toast.makeText(RegisterActivity.this,
                                    res.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        if(res.has("user")){
                            JSONObject obj = res.getJSONObject("user");

                            // Persist user
                            accountUtils.persistUser(obj);

                            // On successful registration, SMS is sent to validate phone number
                            Intent intent = new Intent(RegisterActivity.this,
                                    VerifyPhoneActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this,
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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
