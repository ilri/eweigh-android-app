package org.ilri.eweigh.accounts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.MainActivity;
import org.ilri.eweigh.R;
import org.ilri.eweigh.network.MySingleton;
import org.ilri.eweigh.utils.URL;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    AutoCompleteTextView txtEmail;
    EditText txtPassword;
    ProgressDialog loginProgress;

    AccountUtils accountUtils;

    URL urlStr = new URL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        accountUtils = new AccountUtils(this);

        txtEmail = findViewById(R.id.edit_txt_email);
        txtPassword = findViewById(R.id.edit_txt_password);

        Button mEmailSignInButton = findViewById(R.id.btn_sign_in);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                loginProgress = new ProgressDialog(LoginActivity.this);

                loginProgress.setMessage("Logging in...");
                loginProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loginProgress.show();

                attemptLogin();
            }
        });


        Fonty.setFonts(this);
    }

    private void attemptLogin() {

        // Reset errors.
        txtEmail.setError(null);
        txtPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            txtPassword.setError("Enter a valid password");
            focusView = txtPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Email is required");
            focusView = txtEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            txtEmail.setError("Enter a valid email");
            focusView = txtEmail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            processLogin(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void processLogin(final String email, final String password){

        JSONObject loginObj = new JSONObject();

        try {
            loginObj.put("email", email);
            loginObj.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, urlStr.Login,
                loginObj,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        loginProgress.dismiss();

                        try {
                            if(response.has("message")){
                                Toast.makeText(LoginActivity.this,
                                        response.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                            if(response.has("user")){
                                JSONObject user = (JSONObject) response.get("user");
                                String name = user.getString("name");

                                // Store user locally
                                accountUtils.persistUser(user);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); // Remove login from back stack
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        loginProgress.dismiss();

                        Toast.makeText(LoginActivity.this, "Network Error",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(req);
    }
}

