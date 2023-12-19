package com.demo.livwllpaper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.livwllpaper.R;


public class PrivacyPolicyActivity extends AppCompatActivity {
    LinearLayout ll_back_btn;

    
    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_privacy_policy);
        WebView webView = (WebView) findViewById(R.id.privacypolicyweb);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.google.com");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_back_btn);
        this.ll_back_btn = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                PrivacyPolicyActivity.this.onBackPressed();
            }
        });
    }

    @Override 
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
