package org.ilri.eweigh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import org.ilri.eweigh.liveweight.LiveWeight;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
