package org.ilri.eweigh;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.ilri.eweigh.accounts.AccountUtils;
import org.ilri.eweigh.accounts.LoginActivity;
import org.ilri.eweigh.accounts.MyAccountActivity;
import org.ilri.eweigh.hg_lw.CattleActivity;
import org.ilri.eweigh.hg_lw.ConvertActivity;
import org.ilri.eweigh.hg_lw.SubmissionsActivity;
import org.ilri.eweigh.utils.Utils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnLW = findViewById(R.id.btn_get_lw);
        btnLW.setOnClickListener(this);

        Button btnSubmissions = findViewById(R.id.btn_view_submissions);
        btnSubmissions.setOnClickListener(this);

        Button btnCattle = findViewById(R.id.btn_view_cattle);
        btnCattle.setOnClickListener(this);

        Button btnAbout = findViewById(R.id.btn_about);
        btnAbout.setOnClickListener(this);

        String[] locales = Resources.getSystem().getAssets().getLocales();

        for (String l : locales){
            Log.d(TAG, "Locale: " + l + "\n");
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.btn_get_lw:
                startActivity(new Intent(this, ConvertActivity.class));
                break;

            case R.id.btn_view_submissions:
                startActivity(new Intent(this, SubmissionsActivity.class));
                break;

            case R.id.btn_view_cattle:
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
