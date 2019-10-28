package org.ilri.eweigh.cattle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.R;
import org.ilri.eweigh.accounts.AccountUtils;
import org.ilri.eweigh.accounts.models.User;
import org.ilri.eweigh.cattle.models.Breed;
import org.ilri.eweigh.cattle.models.Cattle;
import org.ilri.eweigh.database.viewmodel.BreedsViewModel;
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

    public static final int RC_CATTLE_LIST = 50;

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
        BreedsViewModel bvm = ViewModelProviders.of(this).get(BreedsViewModel.class);

        View view = LayoutInflater
                .from(this)
                .inflate(R.layout.fragment_add_cattle, null);

        final EditText inputTag = view.findViewById(R.id.input_tag);
        inputTag.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        final Spinner spinnerBreeds = view.findViewById(R.id.spinner_breeds);

        // Populate breeds
        bvm.getAll().observe(this, new Observer<List<Breed>>() {

            @Override
            public void onChanged(List<Breed> breeds) {

                ArrayList<Breed> breedsList = new ArrayList<>();

                breedsList.add(new Breed(0, "Select Breed"));
                breedsList.addAll(breeds);

                ArrayAdapter<Breed> adapter = new ArrayAdapter<>(CattleActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, breedsList);
                spinnerBreeds.setAdapter(adapter);
            }
        });

        final ProgressDialog progressDialog =
                Utils.getProgressDialog(this, "Submitting...", false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Register Cattle")
                .setView(view)
                .setCancelable(true);

        final AlertDialog dialog = builder.create();
        dialog.show();

        Button btnAddCattle = view.findViewById(R.id.btn_add_cattle);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        btnAddCattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = inputTag.getText().toString();

                Breed b = (Breed) spinnerBreeds.getSelectedItem();
                int breed = b.getId();

                if(TextUtils.isEmpty(tag)){
                    inputTag.setError("Required");
                }
                else if(breed == 0){
                    ((TextView) spinnerBreeds.getSelectedView()).setError("Select breed");
                }
                else{
                    progressDialog.show();

                    RequestParams params = new RequestParams();
                    params.put(User.ID, String.valueOf(user.getUserId()));
                    params.put(Cattle.TAG, tag);
                    params.put(Cattle.BREED, String.valueOf(breed));

                    apiService.post(
                            URL.RegisterCattle,
                            params,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d(TAG, "Response: " + response);
                                    progressDialog.dismiss();

                                    try {
                                        JSONObject obj = new JSONObject(response);

                                        if(obj.has(Cattle.CATTLE)){
                                            dialog.dismiss();

                                            inputTag.setText("");
                                            spinnerBreeds.setSelection(0);

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
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void renderList(List<Cattle> cattle){
        adapter = new CattleAdapter(this, cattle);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        adapter.notifyDataSetChanged();
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

                        cattle = new ArrayList<>();

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

        Intent intent = new Intent(this, CattleViewActivity.class);
        intent.putExtra(Cattle.CATTLE, cattle);

        startActivityForResult(intent, RC_CATTLE_LIST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_CATTLE_LIST) {

            if (resultCode == Activity.RESULT_OK &&
                    data.getBooleanExtra("deleted", false)) {
                getCattle();
            }
        }
    }
}
