# GDPRChecker
GDPR Checker is a sample way to implement GDPR to you project

<p align="center">
  <img src="Screenshot.png" width="30%"/>
</p>

## Getting Started
### Step 1. Add the JitPack repository to your build file 
Add it in your root build.gradle at the end of repositories:

```
	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```
### Step 2. Add the dependency
 Add this tow lines to your Module dependency
```
dependencies {
		implementation 'com.github.ixiDev:GDPRChecker:v0.1'
		implementation 'com.google.android.ads.consent:consent-library:1.0.3'
	}
```

## How To use

```
        setContentView(R.layout.activity_main);
        ....
        new GDPRChecker()
                .withContext(this)
                .withPrivacyUrl("https://www.example.com/privacy") // your privacy url
                .withPublisherIds("pub-xxxxxxxxxxxxxxxx") // your admob account Publisher id 
                .withTestMode() // remove this on real project
                .check();
    ...
    
```
### How to Load Ads 
#### Banner ads

```
....

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
	
.....
	
```
#### Interstitail ads

```
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

```


