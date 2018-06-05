package com.ixidev.gdprchecker_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.ixidev.gdpr.GDPRChecker;

public class MainActivity extends AppCompatActivity {
    private AdView adView;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GDPRChecker()
                .withContext(this)
<<<<<<< HEAD
                .withPrivacyUrl("https://www.example.com/privacy")
                .withPublisherIds("pub-xxxxxxxxxxxxxxxx")
=======
                .withPrivacyUrl("Your privacy url ")
                .withPublisherIds("Your Admob PublisherId") //
>>>>>>> add type of Request and solve some errors, add
                .withTestMode()
                .check();
        adView = findViewById(R.id.ad_view);
        interstitialAd = new InterstitialAd(this);

        showBannerADS();
    }


    // example of using GDPRChecker
    private void showBannerADS() {
        AdRequest.Builder builder = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        GDPRChecker.Request request = GDPRChecker.getRequest();
        if (request == GDPRChecker.Request.NON_PERSONALIZED) {
            // load non Personalized ads
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
        } // else do nothing , it will load PERSONALIZED ads
        adView.loadAd(builder.build());

    }

    private void showInterstitialAds() {
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest.Builder builder = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        GDPRChecker.Request request = GDPRChecker.getRequest();
        if (request == GDPRChecker.Request.NON_PERSONALIZED) {
            // load non Personalized ads
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
        }
        interstitialAd.loadAd(builder.build());
        interstitialAd.show();
    }
}
