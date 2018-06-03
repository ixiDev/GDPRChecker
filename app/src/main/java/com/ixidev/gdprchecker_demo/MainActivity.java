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
                .withPrivacyUrl("https://www.example.com/privacy")
                .withPublisherIds("pub-xxxxxxxxxxxxxxxx")
                .withTestMode()
                .check();
    }
}
