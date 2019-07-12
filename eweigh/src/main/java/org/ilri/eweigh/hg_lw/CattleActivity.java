package org.ilri.eweigh.hg_lw;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.R;
import org.ilri.eweigh.accounts.AccountUtils;
import org.ilri.eweigh.accounts.models.User;
import org.ilri.eweigh.hg_lw.adapters.CattleAdapter;
import org.ilri.eweigh.hg_lw.adapters.SubmissionsAdapter;
import org.ilri.eweigh.hg_lw.models.Cattle;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.utils.URL;
import org.ilri.eweigh.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CattleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public static final String TAG = CattleActivity.class.getSimpleName();

    CattleAdapter adapter;
    ListView listView;
    List<Cattle> cattle = new ArrayList<>();

    ProgressBar progressBar;
    TextView blankState;

    APIService apiService;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Cattle");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        apiService = new APIService(this);
        user = new AccountUtils(this).getUserDetails();

        progressBar = findViewById(R.id.progress_bar);
        blankState = findViewById(R.id.txt_blank_state);

        listView = findViewById(R.id.list_view_cattle);

        FloatingActionButton fab = findViewById(R.id.fab_cattle);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterCattleModal();
            }
        });

        getCattle();

        Fonty.setFonts(this);
    }

    private void showRegisterCattleModal(){
        View view = LayoutInflater
                .from(this)
                .inflate(R.layout.fragment_add_cattle, null);

        final EditText inputTag = view.findViewById(R.id.input_tag);
        final EditText inputBreed = view.findViewById(R.id.input_breed);
        final EditText inputHG = view.findViewById(R.id.input_hg);

        final ProgressDialog progressDialog =
                Utils.getProgressDialog(this, "Submitting...", false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Register Cattle")
                .setView(view)
                .setCancelable(true)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        String tag = inputTag.getText().toString();
                        String breed = inputBreed.getText().toString();
                        String hg = inputHG.getText().toString();

                        if(TextUtils.isEmpty(tag)){
                            inputTag.setError("Required");
                        }
                        else if(TextUtils.isEmpty(breed)){
                            inputBreed.setError("Required");
                        }
                        else if(TextUtils.isEmpty(hg)){
                            inputHG.setError("Required");
                        }
                        else{
                            progressDialog.show();

                            RequestParams params = new RequestParams();
                            params.put(User.ID, String.valueOf(user.getUserId()));
                            params.put(Cattle.TAG, tag);
                            params.put(Cattle.BREED, breed);
                            params.put(Cattle.INITIAL_HG, hg);

                            apiService.post(
                                    URL.RegisterCattle,
                                    params,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d(TAG, "Response: " + response);
                                            progressDialog.dismiss();
                                            dialog.dismiss();

                                            try {
                                                JSONObject obj = new JSONObject(response);

                                                if(obj.has(Cattle.CATTLE)){
                                                    inputTag.setText("");
                                                    inputBreed.setText("");
                                                    inputHG.setText("");

                                                    Cattle c = new Cattle(obj.getJSONObject(Cattle.CATTLE));
                                                    cattle.add(0, c);

                                                    adapter.notifyDataSetChanged();
                                                }

                                                if(obj.has("message")){
                                                    Toast.makeText(
                                                            CattleActivity.this,
                                                            obj.getString("message"),
                                                            Toast.LENGTH_SHORT).show();
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d(TAG, "Error: " + error.getMessage());
                                            progressDialog.dismiss();

                                            Toast.makeText(CattleActivity.this,
                                                    "Could not add cattle", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                })
                .setNeutralButton("Cancel", null);

        builder.create().show();
    }

    private void renderList(List<Cattle> cattle){
        adapter = new CattleAdapter(this, cattle);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void getCattle(){
        RequestParams params = new RequestParams();
        params.put(User.ID, String.valueOf(user.getUserId()));

        apiService.post(URL.Cattle, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Response: " + response);
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONArray arr = new JSONArray(response);

                        if(arr.length() > 0){
                            blankState.setVisibility(View.GONE);

                            for(int i=0; i<arr.length(); i++){
                                cattle.add(new Cattle(arr.getJSONObject(i)));
                            }

                            renderList(cattle);
                        }
                        else{
                            blankState.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error: " + error.getMessage());
                    progressBar.setVisibility(View.GONE);
                    blankState.setVisibility(View.VISIBLE);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cattle cattle = (Cattle) parent.getItemAtPosition(position);
    }
}
