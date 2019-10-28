package org.ilri.eweigh.cattle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.R;
import org.ilri.eweigh.cattle.models.Cattle;
import org.ilri.eweigh.cattle.models.ChemicalAgent;
import org.ilri.eweigh.cattle.models.Disease;
import org.ilri.eweigh.cattle.models.Dosage;
import org.ilri.eweigh.database.viewmodel.DosagesViewModel;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.ui.SectionsPagerAdapter;
import org.ilri.eweigh.utils.URL;
import org.ilri.eweigh.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CattleViewActivity extends AppCompatActivity {
    public static final String TAG = CattleViewActivity.class.getSimpleName();

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;

    AlertDialog alertDialog;

    Cattle cattle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cattle);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            cattle = (Cattle) bundle.get(Cattle.CATTLE);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(String.format("View %s", cattle.getTag()));

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        alertDialog = Utils.getSimpleDialog(this, "");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs_cattle);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        DetailsFragment f1 = new DetailsFragment(this);
        mSectionsPagerAdapter.addFragment(f1, "Details");

        FeedsFragment f2 = new FeedsFragment(this);
        mSectionsPagerAdapter.addFragment(f2, "Feeds");

        DosagesFragment f3 = new DosagesFragment(this, cattle);
        mSectionsPagerAdapter.addFragment(f3, "Dosages");

        MatingGuideFragment f4 = new MatingGuideFragment(this);
        mSectionsPagerAdapter.addFragment(f4, "Mating Guide");

        mViewPager.setAdapter(mSectionsPagerAdapter);

        int[] tabIcons = {
                R.drawable.ic_information_outline_white_24dp,
                R.drawable.ic_sprout_white_24dp,
                R.drawable.ic_pill_white_24dp,
                R.drawable.ic_gender_male_female_white_24dp
        };

        for(int i=0; i<tabLayout.getTabCount(); i++){
            if(tabLayout.getTabAt(i) != null){
                tabLayout.getTabAt(i).setIcon(tabIcons[i]);
            }
        }
    }

    /**
     *
     * DETAILS
     *
     * */
    public static class DetailsFragment extends Fragment {

        ProgressBar progressBar;
        TextView blankState;

        private Context context;

        DetailsFragment(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_cattle_details, container, false);

            progressBar = view.findViewById(R.id.progress_bar);

            blankState = view.findViewById(R.id.txt_blank_state);
            blankState.setText("No info available");

            fetchInfo();

            Fonty.setFonts(container);

            return view;
        }

        private void fetchInfo(){

        }
    }

    /**
     *
     * FEEDS
     *
     * */
    public static class FeedsFragment extends Fragment {

        ProgressBar progressBar;
        TextView blankState;

        private Context context;

        FeedsFragment(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_feeds, container, false);

            progressBar = view.findViewById(R.id.progress_bar);

            blankState = view.findViewById(R.id.txt_blank_state);
            blankState.setText("No feed info");
            blankState.setVisibility(View.VISIBLE);

            fetchInfo();

            Fonty.setFonts(container);

            return view;
        }

        private void fetchInfo(){

        }
    }


    /**
     *
     * DOSAGES
     *
     * */
    public static class DosagesFragment extends Fragment {

        private Context context;

        EditText liveWeight;
        Spinner spinnerDiseases, spinnerAgents;
        TextView txtResponse;

        DosagesViewModel dvm;

        Cattle cattle;

        DosagesFragment(Context context, Cattle cattle) {
            this.context = context;
            this.cattle = cattle;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            dvm = ViewModelProviders.of(this).get(DosagesViewModel.class);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_dosages, container, false);

            txtResponse = view.findViewById(R.id.txt_response);

            liveWeight = view.findViewById(R.id.edit_live_weight);
            liveWeight.setText(String.valueOf(cattle.getLiveWeight()));

            // Init disease spinner
            ArrayAdapter<Disease> adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_dropdown_item, dvm.getDiseases());

            spinnerDiseases = view.findViewById(R.id.spinner_diseases);
            spinnerDiseases.setAdapter(adapter);
            spinnerDiseases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                    Disease disease = (Disease) parent.getItemAtPosition(position);

                    List<ChemicalAgent> agents = dvm.getChemicalAgents(disease.getId());

                    ArrayAdapter<ChemicalAgent> adapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, agents);
                    spinnerAgents.setAdapter(adapter);

                    spinnerAgents.setEnabled(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    spinnerAgents.setEnabled(false);
                }
            });

            spinnerAgents = view.findViewById(R.id.spinner_agents);
            spinnerAgents.setEnabled(false);

            view.findViewById(R.id.btn_get_dosage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fetchDosage();
                }
            });

            Fonty.setFonts(container);

            return view;
        }

        private void fetchDosage(){
            Disease disease = (Disease) spinnerDiseases.getSelectedItem();
            ChemicalAgent agent = (ChemicalAgent) spinnerAgents.getSelectedItem();

            if(disease.getId() == 0){
                ((TextView) spinnerDiseases.getSelectedView()).setError("Select disease");
            }
            else if(agent.getId() == 0){
                ((TextView) spinnerAgents.getSelectedView()).setError("Select chemical agent");
            }
            else{
                final ProgressDialog progressDialog =
                        Utils.getProgressDialog(context, "Fetching dosage", true);

                progressDialog.show();
                txtResponse.setVisibility(View.GONE);

                RequestParams params = new RequestParams();

                params.put(Dosage.DISEASE, String.valueOf(disease.getId()));
                params.put(Dosage.CHEMICAL_AGENT, String.valueOf(agent.getId()));
                params.put(Cattle.LIVE_WEIGHT, String.valueOf(cattle.getLiveWeight()));

                new APIService(context).post(URL.GetDosage, params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response);

                            if(obj.has(Dosage.DOSAGE)){
                                String dose = "" +
                                        "Disease: " + obj.getString(Dosage.DISEASE) + "\n" +
                                        "Agent: " + obj.getString(Dosage.CHEMICAL_AGENT) + "\n" +
                                        "Dosage: " + obj.getString(Dosage.DOSAGE) + "ml\n" +
                                        "Application Mode: " + obj.getString(Dosage.APPLICATION_MODE);

                                txtResponse.setText(dose);
                                txtResponse.setTextColor(Color.BLUE);
                                txtResponse.setVisibility(View.VISIBLE);

                                spinnerAgents.setSelection(0);
                                spinnerDiseases.setSelection(0);
                            }
                            else{
                                txtResponse.setVisibility(View.GONE);
                            }

                            if(obj.has("message")){
                                Toast.makeText(context, obj.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        txtResponse.setVisibility(View.GONE);

                        Toast.makeText(context, "Error fetching dosage",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    /**
     *
     * MATING GUIDE
     *
     * */
    public static class MatingGuideFragment extends Fragment {

        ProgressBar progressBar;
        TextView blankState;

        private Context context;

        MatingGuideFragment(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_mating_guide, container, false);

            progressBar = view.findViewById(R.id.progress_bar);

            blankState = view.findViewById(R.id.txt_blank_state);
            blankState.setText("No mating guide available");

            fetchInfo();

            Fonty.setFonts(container);

            return view;
        }

        private void fetchInfo(){

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cattle_view, menu);

        menu.findItem(R.id.action_delete).setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        showDeleteCattleDialog();

                        return true;
                    }
                });

        return true;
    }

    private void showDeleteCattleDialog(){
        AlertDialog alertDialog = Utils.getSimpleDialog(CattleViewActivity.this, "",
                "Delete cattle?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                RequestParams params = new RequestParams();
                params.put(Cattle.ID, String.valueOf(cattle.getId()));

                new APIService(CattleViewActivity.this).post(URL.DeleteCattle,
                        params, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, response);

                                try {
                                    JSONObject obj = new JSONObject(response);

                                    if(obj.has("success")){
                                        Toast.makeText(CattleViewActivity.this,
                                                " " + obj.getString("message"),
                                                Toast.LENGTH_SHORT).show();

                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("deleted", true);
                                        setResult(Activity.RESULT_OK, returnIntent);
                                        finish();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(CattleViewActivity.this,
                                        "Could not process request", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Fail gracefully
                    }
                });
        alertDialog.show();
    }
}
