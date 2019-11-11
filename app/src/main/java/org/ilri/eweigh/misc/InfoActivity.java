package org.ilri.eweigh.misc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    String pageUrl = "home.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            pageUrl = bundle.getString(INFO_PAGE);
        }

        WebView webView = findViewById(R.id.wv_info);

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
}
