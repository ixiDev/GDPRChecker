package com.ixidev.gdpr;

import android.Manifest;
import android.content.Context;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;

import java.net.URL;

/**
 * Created by ixi.Dv on 31/05/2018.
 */
public class GDPRChecker {
    private static final String TAG = "GDPRChecker";
    private ConsentInformation consentInformation;
    private Context context;
    private String privacyUrl;
    private ConsentForm form;
    private String[] publisherIds;
    private static GDPRChecker instance;

    protected GDPRChecker(Context context) {
        this.context = context;
        this.consentInformation = ConsentInformation.getInstance(context);
    }

    public GDPRChecker() {
    }

    public GDPRChecker withContext(Context context) {
        instance = new GDPRChecker(context);
        return instance;
    }

    public GDPRChecker withPrivacyUrl(String privacyUrl) {
        this.privacyUrl = privacyUrl;
        if (instance == null)
            throw new NullPointerException("Please call withContext first");
        return instance;
    }
@RequiresPermission(Manifest.permission.INTERNET)
    private void initGDPR() {
//        consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
        if (publisherIds == null)
            throw new NullPointerException("publisherIds is null, please call withPublisherIds first");
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                if (consentStatus == ConsentStatus.UNKNOWN)
                    if (consentInformation.isRequestLocationInEeaOrUnknown())
                        setupForm();
                    else consentInformation.setConsentStatus(consentStatus);
                else
                    consentInformation.setConsentStatus(consentStatus);
                Log.i(TAG, "onConsentInfoUpdated: " + consentStatus.name());
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                Log.e(TAG, "onFailedToUpdateConsentInfo: " + errorDescription);
            }
        });


    }

    private void setupForm() {
        if (privacyUrl == "")
            throw new NullPointerException("PrivacyUrl is null, Please call withPrivacyUrl first");
        URL Url = null;
        try {
            Url = new URL(privacyUrl);
        } catch (Exception e) {
            Log.e(TAG, "initGDPR: ", e);
        }
        form = new ConsentForm.Builder(context, Url)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        showForm();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        Log.i(TAG, "onConsentFormOpened: ");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        Log.i(TAG, "onConsentFormClosed: " + consentStatus.name());
                        consentInformation.setConsentStatus(consentStatus);

                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        Log.e(TAG, "onConsentFormError: " + errorDescription);
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();

        form.load();
    }

    private void showForm() {
        form.show();
    }

    public void check() {
        initGDPR();
    }

    public GDPRChecker withPublisherIds(String... publisherIds) {
        this.publisherIds = publisherIds;
        if (instance == null)
            throw new NullPointerException("Please call withContext first");
        return instance;
    }

    public GDPRChecker withTestMode(String testDevice) {
        consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
        consentInformation.addTestDevice(testDevice);
        if (instance == null)
            throw new NullPointerException("Please call withContext first");
        return instance;
    }

    public GDPRChecker withTestMode() {
        consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
        if (instance == null)
            throw new NullPointerException("Please call withContext first");
        return instance;
    }
}
