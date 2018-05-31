package com.ixidev.gdprchecker_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ixidev.gdpr.GDPRChecker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GDPRChecker()
                .withContext(this)
                .withPrivacyUrl("https://sites.google.com/view/ixidev/home")
                .withPublisherIds("pub-1278119229762512")
                .withTestMode()
                .check();
    }
}
