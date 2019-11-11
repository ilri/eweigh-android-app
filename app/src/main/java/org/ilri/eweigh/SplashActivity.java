package org.ilri.eweigh;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import org.ilri.eweigh.accounts.AccountUtils;
import org.ilri.eweigh.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AccountUtils accountUtils = new AccountUtils(this);
        Intent intent = new Intent(this, MainActivity.class);

        if(accountUtils.isLoggedIn()){
            intent = new Intent(this, HomeActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
