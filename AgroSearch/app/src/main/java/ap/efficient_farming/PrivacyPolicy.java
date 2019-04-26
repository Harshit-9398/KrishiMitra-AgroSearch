package ap.efficient_farming;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        WebView myWebView = findViewById(R.id.PrivacyPolicy);
        myWebView.loadUrl("http://adityakrishimitra.blogspot.com/2019/04/privacy-policy.html");
    }
}
