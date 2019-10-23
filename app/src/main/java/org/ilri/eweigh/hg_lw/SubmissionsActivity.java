package org.ilri.eweigh.hg_lw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.R;
import org.ilri.eweigh.accounts.AccountUtils;
import org.ilri.eweigh.accounts.models.User;
import org.ilri.eweigh.hg_lw.adapters.SubmissionsAdapter;
import org.ilri.eweigh.hg_lw.models.Submission;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.utils.URL;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SubmissionsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String TAG = SubmissionsActivity.class.getSimpleName();

    ListView listView;
    List<Submission> submissions = new ArrayList<>();

    ProgressBar progressBar;
    TextView blankState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submissions);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Submissions");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.progress_bar);
        blankState = findViewById(R.id.txt_blank_state);

        listView = findViewById(R.id.list_view_submissions);

        getSubmissions();

        Fonty.setFonts(this);
    }

    private void renderList(List<Submission> submissions){
        SubmissionsAdapter adapter = new SubmissionsAdapter(this, submissions);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void getSubmissions() {
        User user = new AccountUtils(this).getUserDetails();

        RequestParams params = new RequestParams();
        params.put(User.ID, String.valueOf(user.getUserId()));

        new APIService(this)
                .post(URL.Submissions, params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONArray arr = new JSONArray(response);

                            if(arr.length() > 0){
                                blankState.setVisibility(View.GONE);

                                for(int i=0; i<arr.length(); i++){
                                    submissions.add(new Submission(arr.getJSONObject(i)));
                                }

                                renderList(submissions);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Submission submission = (Submission) parent.getItemAtPosition(position);

        // TODO: Show some info in a modal
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
