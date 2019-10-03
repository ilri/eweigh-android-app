package org.ilri.eweigh.feeds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.R;
import org.ilri.eweigh.database.viewmodel.FeedsViewModel;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.utils.URL;

import java.util.ArrayList;
import java.util.List;

public class CalculateFeedActivity extends AppCompatActivity {
    public static final String TAG = CalculateFeedActivity.class.getSimpleName();

    FeedsViewModel fvm;

    EditText editLiveWeight, editMilkProduction, editTargetWeight, editTargetDate;
    Spinner spinnerForage, spinnerConcentrate;

    String feedFor = Feed.FEED_FOR_MILK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_feed);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(getString(R.string.get_feed_ration));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fvm = ViewModelProviders.of(this).get(FeedsViewModel.class);

        editLiveWeight = findViewById(R.id.edit_txt_lw);
        editMilkProduction = findViewById(R.id.edit_txt_milk_production);
        editTargetWeight = findViewById(R.id.edit_txt_target_weight);
        editTargetDate = findViewById(R.id.edit_txt_target_date);

        /*
         *
         * Populate feed spinners
         *
         * */
        spinnerForage = findViewById(R.id.spinner_forage);
        spinnerConcentrate = findViewById(R.id.spinner_concentrate);

        fvm.getAll().observe(this, new Observer<List<Feed>>() {
            @Override
            public void onChanged(List<Feed> feeds) {
                ArrayList<Feed> forageFeeds = new ArrayList<>();
                ArrayList<Feed> concentrateFeeds = new ArrayList<>();

                forageFeeds.add(new Feed(0, "- Select Forage -"));
                concentrateFeeds.add(new Feed(0, "- Select Concentrate -"));

                for(Feed f : feeds){

                    if(f.getFeedType().equals(Feed.FEED_TYPE_FORAGE)){
                        forageFeeds.add(f);
                    }

                    if(f.getFeedType().equals(Feed.FEED_TYPE_CONCENTRATE)){
                        concentrateFeeds.add(f);
                    }
                }

                ArrayAdapter<Feed> adapter = new ArrayAdapter<>(CalculateFeedActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, forageFeeds);
                spinnerForage.setAdapter(adapter);

                ArrayAdapter<Feed> adapter2 = new ArrayAdapter<>(CalculateFeedActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, concentrateFeeds);
                spinnerConcentrate.setAdapter(adapter2);
            }
        });

        Button btnGetFeedRation = findViewById(R.id.btn_get_feed_ration);
        btnGetFeedRation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Validate forms
                getFeedRation();
            }
        });

        Fonty.setFonts(this);
    }

    private void getFeedRation(){
        RequestParams params = new RequestParams();

        params.put(Feed.LIVE_WEIGHT, editLiveWeight.getText().toString());
        params.put(Feed.FEED_FOR, feedFor);
        params.put(Feed.MILK_PRODUCTION, editMilkProduction.getText().toString());
        params.put(Feed.TARGET_WEIGHT, editTargetWeight.getText().toString());
        params.put(Feed.TARGET_DATE, editTargetWeight.getText().toString());

        new APIService(this).post(URL.GetFeedRation, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public void onRadioButtonClicked(View v){

        switch (v.getId()) {

            case R.id.radio_milk_production:
                feedFor = Feed.FEED_FOR_MILK;

                editMilkProduction.setVisibility(View.VISIBLE);
                editTargetWeight.setVisibility(View.GONE);
                editTargetDate.setVisibility(View.GONE);

                editTargetWeight.setText("");
                editTargetDate.setText("");

                break;

            case R.id.radio_weight_gain:
                feedFor = Feed.FEED_FOR_WEIGHT;

                editTargetWeight.setVisibility(View.VISIBLE);
                editTargetDate.setVisibility(View.VISIBLE);
                editMilkProduction.setVisibility(View.GONE);

                editMilkProduction.setText("");

                break;
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
