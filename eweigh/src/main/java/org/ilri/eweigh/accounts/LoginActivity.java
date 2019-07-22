package org.ilri.eweigh.accounts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.HomeActivity;
import org.ilri.eweigh.MainActivity;
import org.ilri.eweigh.R;
import org.ilri.eweigh.accounts.models.User;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.utils.URL;
import org.ilri.eweigh.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    AutoCompleteTextView txtEmail;
    EditText txtPassword;
    ProgressDialog progressDialog;

    AccountUtils accountUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Login");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        accountUtils = new AccountUtils(this);

        txtEmail = findViewById(R.id.edit_txt_email);
        txtPassword = findViewById(R.id.edit_txt_password);

        Button btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        Fonty.setFonts(this);
    }

    private void attemptLogin() {
        txtEmail.setError(null);
        txtPassword.setError(null);

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Email is required");
        }
        else if (!isEmailValid(email)) {
            txtEmail.setError("Enter a valid email");
        }
        else if (TextUtils.isEmpty(password)) {
            txtEmail.setError("Enter your password");
        }
        else{
            processLogin(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private void processLogin(final String email, final String password){
        JSONObject loginObj = new JSONObject();

        progressDialog = new ProgressDialog(LoginActivity.this);

        progressDialog.setMessage("Logging in...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        try {
            loginObj.put("email", email);
            loginObj.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final AlertDialog alertDialog = Utils.getSimpleDialog(this, "");

        APIService apiService = new APIService(this);

        RequestParams params = new RequestParams();
        params.put(User.EMAIL, email);
        params.put(User.PASSWORD, password);

        apiService.post(URL.Login, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.has("message")) {
                        alertDialog.setMessage(obj.getString("message"));
                        alertDialog.show();
                    }

                    if (obj.has("user")) {
                        JSONObject user = obj.getJSONObject("user");

                        accountUtils.persistUser(user);

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                Log.d("Error.Response", error.toString());
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Network Error",
                        Toast.LENGTH_SHORT).show();
            }
        });
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

