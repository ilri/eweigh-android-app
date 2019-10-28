package org.ilri.eweigh.feeds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.R;
import org.ilri.eweigh.database.viewmodel.FeedsViewModel;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.utils.URL;
import org.ilri.eweigh.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CalculateFeedActivity extends AppCompatActivity {
    public static final String TAG = CalculateFeedActivity.class.getSimpleName();

    FeedsViewModel fvm;

    EditText editLiveWeight, editMilkProduction, editTargetWeight, editTargetDate;
    Spinner spinnerForage, spinnerConcentrate;

    String feedFor = Feed.FEED_FOR_MILK;
    String feedStyle = Feed.FEED_STYLE_STALL;

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

                forageFeeds.add(new Feed(0, "Select Forage"));
                concentrateFeeds.add(new Feed(0, "Select Concentrate"));

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

                Feed forage = (Feed) spinnerForage.getSelectedItem();
                Feed concentrate = (Feed) spinnerConcentrate.getSelectedItem();

                String liveWeight = editLiveWeight.getText().toString();
                String milkProduction = editMilkProduction.getText().toString();
                String targetWeight = editTargetWeight.getText().toString();
                String targetDate = editTargetDate.getText().toString();

                if(liveWeight.isEmpty()){
                    editLiveWeight.setError("Enter live weight");
                }
                else if(feedFor.isEmpty()){
                    Toast.makeText(CalculateFeedActivity.this,
                            "Select purpose for feed", Toast.LENGTH_SHORT).show();
                }
                else if(feedFor.equals(Feed.FEED_FOR_MILK) && milkProduction.isEmpty()){
                    editMilkProduction.setError("Enter milk production");
                }
                else if(feedFor.equals(Feed.FEED_FOR_WEIGHT) && targetWeight.isEmpty()){
                    editTargetWeight.setError("Enter target weight");
                }
                else if(feedFor.equals(Feed.FEED_FOR_WEIGHT) && targetDate.isEmpty()){
                    editMilkProduction.setError("Enter target date");
                }
                else if(feedFor.equals(Feed.FEED_FOR_WEIGHT) &&
                        (Double.parseDouble(liveWeight) > Double.parseDouble(targetWeight))){
                    editTargetWeight.setError("Target weight should be greater than live weight");
                }
                else if(forage.getId() == 0){
                    Toast.makeText(CalculateFeedActivity.this,
                            "Select Forage", Toast.LENGTH_SHORT).show();
                }
                else if(concentrate.getId() == 0){
                    Toast.makeText(CalculateFeedActivity.this,
                            "Select Concentrate", Toast.LENGTH_SHORT).show();
                }
                else{
                    getFeedRation();
                }
            }
        });

        Fonty.setFonts(this);
    }

    private void getFeedRation(){
        RequestParams params = new RequestParams();

        params.put(Feed.LIVE_WEIGHT, editLiveWeight.getText().toString());
        params.put(Feed.FEED_FOR, feedFor);
        params.put(Feed.FEED_STYLE, feedStyle);
        params.put(Feed.MILK_PRODUCTION, editMilkProduction.getText().toString());
        params.put(Feed.TARGET_WEIGHT, editTargetWeight.getText().toString());
        params.put(Feed.TARGET_DATE, editTargetDate.getText().toString());

        Feed forage = (Feed) spinnerForage.getSelectedItem();
        Feed concentrate = (Feed) spinnerConcentrate.getSelectedItem();

        params.put(Feed.FORAGE, String.valueOf(forage.getId()));
        params.put(Feed.CONCENTRATE, String.valueOf(concentrate.getId()));

        final ProgressDialog progressDialog =
                Utils.getProgressDialog(this, "Calculating feed...", false);

        progressDialog.show();

        new APIService(this).post(URL.GetFeedRation, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                progressDialog.dismiss();

                TextView txtRation = findViewById(R.id.txt_feed_ration);

                try {
                    JSONObject res = new JSONObject(response);

                    if(res.has("error") && res.getBoolean("error")){
                        txtRation.setText(res.optString("message", "-"));
                        txtRation.setTextColor(Color.RED);
                    }
                    else{

                        String ration = "RATION AS FED: \n\n" +
                                "Forage: " + res.optDouble("forage", 0) + " kg\n" +
                                "Concentrate: " + res.optDouble("concentrate", 0) + " kg";

                        txtRation.setText(ration);
                        txtRation.setTextColor(Color.BLUE);
                    }

                    ScrollView scrollView = findViewById(R.id.scrollView);
                    scrollView.fullScroll(View.FOCUS_DOWN);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
    }

    public void onFeedStyleRadioChanged(View v){
        switch (v.getId()) {

            case R.id.radio_stall_fed:
                feedStyle = Feed.FEED_STYLE_STALL;
                break;

            case R.id.radio_graze_local:
                feedStyle = Feed.FEED_STYLE_GRAZE_LOCAL;
                break;

            case R.id.radio_graze_extensive:
                feedStyle = Feed.FEED_STYLE_GRAZE_EXT;
                break;
        }
    }

    public void onFeedForRadioChanged(View v){
        LinearLayout layoutWeight = findViewById(R.id.layout_weight_options);
        LinearLayout layoutMilk = findViewById(R.id.layout_milk_options);

        switch (v.getId()) {

            case R.id.radio_milk_production:
                feedFor = Feed.FEED_FOR_MILK;

                layoutMilk.setVisibility(View.VISIBLE);
                layoutWeight.setVisibility(View.GONE);

                editTargetWeight.setText("");
                editTargetDate.setText("");

                break;

            case R.id.radio_weight_gain:
                feedFor = Feed.FEED_FOR_WEIGHT;

                layoutWeight.setVisibility(View.VISIBLE);
                layoutMilk.setVisibility(View.GONE);

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
