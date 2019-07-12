package org.ilri.eweigh.hg_lw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.R;
import org.ilri.eweigh.accounts.AccountUtils;
import org.ilri.eweigh.accounts.models.User;
import org.ilri.eweigh.hg_lw.models.Cattle;
import org.ilri.eweigh.network.APIService;
import org.ilri.eweigh.network.RequestParams;
import org.ilri.eweigh.utils.URL;

public class CattleActivity extends AppCompatActivity {
    public static final String TAG = CattleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Cattle");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getCattle();

        Fonty.setFonts(this);
    }

    private void getCattle(){
        User user = new AccountUtils(this).getUserDetails();

        RequestParams params = new RequestParams();
        params.put(User.ID, String.valueOf(user.getUserId()));

        new APIService(this)
                .post(URL.Cattle, params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.getMessage());
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
}
