package org.ilri.eweigh.misc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.ilri.eweigh.R;

/**
 *
 * InfoActivity displays info pages stored in the `assets/html` folder
 *
 * */
public class InfoActivity extends AppCompatActivity {
    public static final String TAG = InfoActivity.class.getSimpleName();
    public static final String INFO_PAGE = "info_page";
    public static final String INFO_TITLE = "info_title";

    String pageUrl = "home.html";
    String pageTitle = "Info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            pageUrl = bundle.getString(INFO_PAGE);
            pageTitle = bundle.getString(INFO_TITLE);
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(pageTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }

        final WebView webView = findViewById(R.id.wv_info);

        WebSettings webSetting = webView.getSettings();
        webSetting.setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/html/" + pageUrl);
    }

    private class WebViewClient extends android.webkit.WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            return super.shouldOverrideUrlLoading(view, url);
        }
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
