package org.ilri.eweigh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.hg_lw.ConvertActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        Button btnLW = findViewById(R.id.btn_get_lw);
        btnLW.setOnClickListener(this);

        Button btnAbout = findViewById(R.id.btn_about);
        btnAbout.setOnClickListener(this);

        TextView txtAppNameVersion = findViewById(R.id.txt_app_name_version);
        txtAppNameVersion.setText(String.format("v%s", BuildConfig.VERSION_NAME));

        Fonty.setFonts(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.btn_get_lw:
                startActivity(new Intent(this, ConvertActivity.class));
                break;

            case R.id.btn_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }
}
