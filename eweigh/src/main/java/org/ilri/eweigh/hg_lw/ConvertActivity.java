package org.ilri.eweigh.hg_lw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.marcinorlowski.fonty.Fonty;

import org.ilri.eweigh.R;

public class ConvertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Get Live-Weight");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        EditText inputHG = findViewById(R.id.input_hg);
        final TextView txtLW = findViewById(R.id.txt_lw);

        inputHG.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double liveWeight = 0;

                if(count > 0){
                    double heartGirth = Double.valueOf(s.toString());
                    liveWeight = LiveWeight.calculateLW(heartGirth);
                }

                txtLW.setText(
                        String.format("%s%s", getString(R.string.live_weight_label), liveWeight));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Fonty.setFonts(this);
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
