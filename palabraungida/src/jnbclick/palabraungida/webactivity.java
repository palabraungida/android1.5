package jnbclick.palabraungida;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.content.Intent;

public class webactivity extends Activity {
	WebView mWebView;
	String url = "http://www.ustream.tv/channel/ungidatv";
    /** Called when the activity is first created. */
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        Bundle b = this.getIntent().getExtras();
        String tempUrl = b.getString("key");
        mWebView.loadUrl(tempUrl);
       finish();
    } 
  
    public boolean shouldOverrideUrlLoading(WebView mWebView, String url) {    
        mWebView.loadUrl(url);
        return true;
    }
    
}