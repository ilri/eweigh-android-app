package org.ilri.eweigh.accounts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.ilri.eweigh.MainActivity;
import org.ilri.eweigh.R;
import org.ilri.eweigh.accounts.models.User;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.utils.URL;
import org.ilri.eweigh.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

public class VerifyPhoneActivity extends AppCompatActivity {
    public static final String TAG = VerifyPhoneActivity.class.getSimpleName();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Verify Mobile No.");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressDialog = Utils.getProgressDialog(this, "Verifying...", false);

        Button btnVerify = findViewById(R.id.btn_verify);
        final EditText inputCode = findViewById(R.id.input_code);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = inputCode.getText().toString();

                if(code.isEmpty() || code.length() != 4){
                    Toast.makeText(VerifyPhoneActivity.this,
                            "Enter a valid verification code", Toast.LENGTH_SHORT).show();
                }
                else{
                    checkVerification(code);
                }
            }
        });
    }

    public void checkVerification(String code){
        APIService apiService = new APIService(this);
        AccountUtils accountUtils = new AccountUtils(this);

        User user = accountUtils.getUserDetails();

        RequestParams params = new RequestParams();
        params.put(User.ID, String.valueOf(user.getUserId()));
        params.put(User.VERIFICATION_CODE, code);

        progressDialog.show();

        apiService.post(URL.Verify, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(response);

                    if(obj.has("message")){
                        Toast.makeText(VerifyPhoneActivity.this,
                                obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    if(obj.has("success")){
                        startActivity(new Intent(VerifyPhoneActivity.this, MainActivity.class));
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
                Toast.makeText(VerifyPhoneActivity.this,
                        "Could not verify account", Toast.LENGTH_SHORT).show();
            }
        });
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
