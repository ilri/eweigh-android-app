package org.ilri.eweigh.accounts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.TextView;

import org.ilri.eweigh.R;
import org.ilri.eweigh.accounts.models.User;

public class MyAccountActivity extends AppCompatActivity {
    private static final String TAG = MyAccountActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Account");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        AccountUtils accountUtils = new AccountUtils(this);
        User accountUser = accountUtils.getUserDetails();

        TextView profileName = findViewById(R.id.txt_profile_name);
        profileName.setText(accountUser.getFullName());

        TextView profileEmail = findViewById(R.id.txt_profile_email);
        profileEmail.setText(accountUser.getEmail());

        TextView profileMobile = findViewById(R.id.txt_profile_mobile);
        profileMobile.setText(accountUser.getMobile());

        TextView profileGroup = findViewById(R.id.txt_profile_group);
        // profileGroup.setText(accountUser.getGroupName());
        profileGroup.setText("Active");
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
}
