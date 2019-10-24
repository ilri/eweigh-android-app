package org.ilri.eweigh;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.ilri.eweigh.accounts.AccountUtils;
import org.ilri.eweigh.accounts.MyAccountActivity;
import org.ilri.eweigh.cattle.models.Breed;
import org.ilri.eweigh.cattle.models.Dosage;
import org.ilri.eweigh.database.viewmodel.BreedsViewModel;
import org.ilri.eweigh.database.viewmodel.DosagesViewModel;
import org.ilri.eweigh.feeds.CalculateFeedActivity;
import org.ilri.eweigh.feeds.Feed;
import org.ilri.eweigh.database.viewmodel.FeedsViewModel;
import org.ilri.eweigh.cattle.CattleActivity;
import org.ilri.eweigh.hg_lw.ConvertActivity;
import org.ilri.eweigh.hg_lw.SubmissionsActivity;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.utils.URL;
import org.ilri.eweigh.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnLW = findViewById(R.id.btn_get_lw);
        btnLW.setOnClickListener(this);

        Button btnGetFeedRation = findViewById(R.id.btn_get_feed_ration);
        btnGetFeedRation.setOnClickListener(this);

        Button btnSubmissions = findViewById(R.id.btn_view_submissions);
        btnSubmissions.setOnClickListener(this);

        Button btnCattle = findViewById(R.id.btn_my_cattle);
        btnCattle.setOnClickListener(this);

        Button btnAbout = findViewById(R.id.btn_about);
        btnAbout.setOnClickListener(this);

        fetchBundle();
    }

    private void fetchBundle(){
        final BreedsViewModel bvm = ViewModelProviders.of(this).get(BreedsViewModel.class);
        final FeedsViewModel fvm = ViewModelProviders.of(this).get(FeedsViewModel.class);
        final DosagesViewModel dvm = ViewModelProviders.of(this).get(DosagesViewModel.class);

        new APIService(this)
                .get(URL.Bundle, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            // Store breeds
                            if(obj.has("breeds")){
                                JSONArray breeds = obj.getJSONArray("breeds");

                                if(Feed.hasItems(breeds)){
                                    bvm.deleteAll();

                                    for (int i=0; i<breeds.length(); i++) {
                                        Breed b = new Breed(breeds.getJSONObject(i));
                                        bvm.insert(b);
                                    }
                                }
                            }

                            // Store feeds
                            if(obj.has("feeds")){
                                JSONArray feeds = obj.getJSONArray("feeds");

                                if(Breed.hasItems(feeds)){
                                    fvm.deleteAll();

                                    for (int i=0; i<feeds.length(); i++) {
                                        Feed f = new Feed(feeds.getJSONObject(i));
                                        fvm.insert(f);
                                    }
                                }
                            }

                            // Store dosages
                            if(obj.has("dosages")){
                                JSONArray dosages = obj.getJSONArray("dosages");

                                if(Dosage.hasItems(dosages)){
                                    dvm.deleteAll();

                                    for (int i=0; i<dosages.length(); i++) {
                                        Dosage d = new Dosage(dosages.getJSONObject(i));
                                        dvm.insert(d);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this,
                                "Could not fetch feeds list", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.btn_get_lw:
                startActivity(new Intent(this, ConvertActivity.class));
                break;

            case R.id.btn_get_feed_ration:
                startActivity(new Intent(this, CalculateFeedActivity.class));
                break;

            case R.id.btn_view_submissions:
                startActivity(new Intent(this, SubmissionsActivity.class));
                break;

            case R.id.btn_my_cattle:
                startActivity(new Intent(this, CattleActivity.class));
                break;

            case R.id.btn_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        menu.findItem(R.id.action_logout).setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        AlertDialog alertDialog = Utils.getSimpleDialog(HomeActivity.this, "",
                                "Log out of account?");

                        final AccountUtils accountUtils = new AccountUtils(HomeActivity.this);

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                accountUtils.logout();

                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); // Remove login from back stack
                            }
                        });

                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Fail gracefully
                            }
                        });
                        alertDialog.show();

                        return true;
                    }
                });

        menu.findItem(R.id.action_profile).setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        startActivity(new Intent(HomeActivity.this, MyAccountActivity.class));
                        return true;
                    }
                });

        return true;
    }
}
