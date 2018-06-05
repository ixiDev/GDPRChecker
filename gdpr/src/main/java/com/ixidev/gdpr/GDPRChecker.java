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
import com.google.gson.annotations.SerializedName;

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
    private static Request request = Request.PERSONALIZED;
    private boolean withAdFreeOption = false;

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
        if (publisherIds == null)
            throw new NullPointerException("publisherIds is null, please call withPublisherIds first");
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {

                switch (consentStatus) {
                    case PERSONALIZED:
                        request = Request.PERSONALIZED;
                        Log.i(TAG, "onConsentInfoUpdated: Showing Personalized ads");
                        break;
                    case NON_PERSONALIZED:
                        request = Request.NON_PERSONALIZED;
                        Log.i(TAG, "onConsentInfoUpdated: Showing Non-Personalized ads");
                        break;
                    case UNKNOWN:
                        if (consentInformation.isRequestLocationInEeaOrUnknown()) {
                            setupForm();
                        } else {
                            request = Request.NON_PERSONALIZED;
                            Log.i(TAG, "onConsentInfoUpdated:  case UNKNOWN :: GDPRChecker Request :: " + request.name());
                        }

                        break;
                    default:
                        request = Request.PERSONALIZED;
                        break;
                }

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
        ConsentForm.Builder builder = new ConsentForm.Builder(context, Url)
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
                        consentInformation.setConsentStatus(consentStatus);

                        if (userPrefersAdFree) {
                            Log.i(TAG, "Requesting Consent: User prefers AdFree");
                        } else {
                            Log.i(TAG, "Requesting Consent: Requesting consent again");
                            switch (consentStatus) {
                                case PERSONALIZED:
                                    request = Request.PERSONALIZED;
                                    break;
                                case NON_PERSONALIZED:
                                    request = Request.NON_PERSONALIZED;
                                    break;
                                case UNKNOWN:
                                    request = Request.NON_PERSONALIZED;
                                    break;
                            }
                        }

                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        Log.e(TAG, "onConsentFormError: " + errorDescription);
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption();
        if (withAdFreeOption)
            builder.withAdFreeOption();

        form = builder.build();
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

    public static Request getRequest() {
        return request;
    }

    public enum Request {
        @SerializedName("ADS_PERSONALIZED")
        PERSONALIZED,
        @SerializedName("ADS_NON_PERSONALIZED")
        NON_PERSONALIZED
    }

    /**
     * @param withAdFreeOption ; if true show " Pay for the ad-free version
     *  withAdFreeOption is false by default
     */
    public void setWithAdFreeOption(boolean withAdFreeOption) {
        this.withAdFreeOption = withAdFreeOption;
    }
}
