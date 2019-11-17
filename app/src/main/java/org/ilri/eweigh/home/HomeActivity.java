package org.ilri.eweigh.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.AboutActivity;
import org.ilri.eweigh.R;
import org.ilri.eweigh.accounts.AccountUtils;
import org.ilri.eweigh.accounts.MyAccountActivity;
import org.ilri.eweigh.accounts.models.User;
import org.ilri.eweigh.cattle.CattleActivity;
import org.ilri.eweigh.cattle.models.Breed;
import org.ilri.eweigh.cattle.models.Dosage;
import org.ilri.eweigh.database.viewmodel.BreedsViewModel;
import org.ilri.eweigh.database.viewmodel.DosagesViewModel;
import org.ilri.eweigh.database.viewmodel.FeedsViewModel;
import org.ilri.eweigh.database.viewmodel.SubmissionsViewModel;
import org.ilri.eweigh.feeds.Feed;
import org.ilri.eweigh.hg_lw.models.Submission;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.utils.URL;
import org.ilri.eweigh.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        buildHomeMenu();
        fetchBundle();

        Utils.setLocale(this, "sw");

        Fonty.setFonts(this);
    }

    private void buildHomeMenu(){
        final List<HomeMenu> menus = new ArrayList<>();

        menus.add(new HomeMenu("My Cattle", R.drawable.hm_icon_cattle, CattleActivity.class));
        menus.add(new HomeMenu("Info", R.drawable.hm_icon_info, AboutActivity.class));
        menus.add(new HomeMenu("Account", R.drawable.hm_icon_account, MyAccountActivity.class));

        HomeAdapter adapter = new HomeAdapter(this, menus);

        GridView gridView = findViewById(R.id.grid_view_home);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                HomeMenu menuItem = menus.get(position);

                Intent intent = new Intent(HomeActivity.this, menuItem.getActivity());
                startActivity(intent);
            }
        });
    }

    private void fetchBundle(){
        final BreedsViewModel bvm = ViewModelProviders.of(this).get(BreedsViewModel.class);
        final FeedsViewModel fvm = ViewModelProviders.of(this).get(FeedsViewModel.class);
        final DosagesViewModel dvm = ViewModelProviders.of(this).get(DosagesViewModel.class);
        final SubmissionsViewModel svm = ViewModelProviders.of(this).get(SubmissionsViewModel.class);

        AccountUtils  accountUtils = new AccountUtils(this);
        User user = accountUtils.getUserDetails();

        RequestParams params = new RequestParams();
        params.put(User.ID, String.valueOf(user.getUserId()));

        new APIService(this)
                .post(URL.Bundle, params,  new Response.Listener<String>() {

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

                            // Store submissions
                            if(obj.has("submissions")){
                                JSONArray submissions = obj.getJSONArray("submissions");

                                if(Dosage.hasItems(submissions)){
                                    svm.deleteAll();

                                    for (int i=0; i<submissions.length(); i++) {
                                        Submission s = new Submission(submissions.getJSONObject(i));
                                        svm.insert(s);
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
}
