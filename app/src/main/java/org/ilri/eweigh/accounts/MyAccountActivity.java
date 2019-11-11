package org.ilri.eweigh.accounts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.ilri.eweigh.MainActivity;
import org.ilri.eweigh.R;
import org.ilri.eweigh.accounts.models.User;
import org.ilri.eweigh.utils.Utils;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account, menu);

        menu.findItem(R.id.action_logout).setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        logOut();
                        return true;
                    }
                });

        return true;
    }

    private void logOut(){
        AlertDialog alertDialog = Utils.getSimpleDialog(MyAccountActivity.this, "",
                "Log out of account?");

        final AccountUtils accountUtils = new AccountUtils(MyAccountActivity.this);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                accountUtils.logout();

                Intent intent = new Intent(MyAccountActivity.this, MainActivity.class);
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
    }
}
