package com.android.teaching.miprimeraapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

   protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_web_view);

       Toolbar myToolbar = findViewById(R.id.toolbar);
       setSupportActionBar(myToolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       WebView myWebView = findViewById(R.id.web_view);
       String urlToLoad = getIntent().getStringExtra("url");
       myWebView.loadUrl(urlToLoad);
   }


}
