/*
 * Copyright (c) 2016 deltaDNA Ltd. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.deltadna.android.sdk.ads.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.deltadna.android.sdk.DDNA;
import com.deltadna.android.sdk.Engagement;
import com.deltadna.android.sdk.ImageMessage;
import com.deltadna.android.sdk.ads.DDNASmartAds;
import com.deltadna.android.sdk.ads.InterstitialAd;
import com.deltadna.android.sdk.ads.RewardedAd;
import com.deltadna.android.sdk.ads.listeners.AdRegistrationListener;
import com.deltadna.android.sdk.ads.listeners.RewardedAdsListener;
import com.deltadna.android.sdk.listeners.EngageListener;

public class ExampleActivity extends Activity implements AdRegistrationListener {
    
    // lifecycle methods
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_example);
        
        DDNA.instance().startSdk();
        
        DDNASmartAds.instance().setAdRegistrationListener(this);
        DDNASmartAds.instance().registerForAds(this);
        
        ((TextView) findViewById(R.id.user_id)).setText(getString(
                R.string.user_id, DDNA.instance().getUserId()));
    }
    
    @Override
    protected void onPause() {
        DDNASmartAds.instance().onPause();
        
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        DDNASmartAds.instance().onResume();
    }
    
    @Override
    protected void onDestroy() {
        DDNASmartAds.instance().onDestroy();
        DDNA.instance().stopSdk();
        
        super.onDestroy();
    }
    
    // ad registration callbacks
    
    @Override
    public void onRegisteredForInterstitial() {
        Log.d(BuildConfig.LOG_TAG, "Registered for interstitial ads");
        
        findViewById(R.id.show_interstitial_ad).setEnabled(true);
        findViewById(R.id.show_engage_interstitial_ad).setEnabled(true);
    }
    
    @Override
    public void onFailedToRegisterForInterstitial(String reason) {
        Log.d(BuildConfig.LOG_TAG, "Failed to register for interstitial ads");
        
        findViewById(R.id.show_interstitial_ad).setEnabled(false);
        findViewById(R.id.show_engage_interstitial_ad).setEnabled(false);
    }
    
    @Override
    public void onRegisteredForRewarded() {
        Log.d(BuildConfig.LOG_TAG, "Registered for rewarded ads");
        
        findViewById(R.id.show_rewarded_ad).setEnabled(true);
        findViewById(R.id.show_engage_rewarded_ad).setEnabled(true);
    }
    
    @Override
    public void onFailedToRegisterForRewarded(String reason) {
        Log.d(BuildConfig.LOG_TAG, "Failed to register for rewarded ads");
        
        findViewById(R.id.show_rewarded_ad).setEnabled(false);
        findViewById(R.id.show_engage_rewarded_ad).setEnabled(false);
    }
    
    // view callbacks
    
    public void onRegisterForAds(View view) {
        DDNASmartAds.instance().registerForAds(this);
    }
    
    public void onShowInterstitialAd(View view) {
        InterstitialAd ad = InterstitialAd.create();
        if (ad != null) {
            ad.show();
        } else {
            Log.w(BuildConfig.LOG_TAG, "Interstitial ad not created");
        }
    }
    
    public void onShowEngageInterstitialAd(View view) {
        DDNA.instance().requestEngagement(
                new Engagement("testAdPoint"),
                new EngageListener<Engagement>() {
                    @Override
                    public void onCompleted(Engagement engagement) {
                        InterstitialAd ad = InterstitialAd.create(engagement);
                        if (ad != null) {
                            ad.show();
                        } else {
                            Log.d(  BuildConfig.LOG_TAG,
                                    "Engage not setup to show ad");
                        }
                    }
                    
                    @Override
                    public void onError(Throwable t) {
                        Log.d(BuildConfig.LOG_TAG, "Failed to engage", t);
                    }
                });
    }
    
    public void onShowRewardedAd(View view) {
        RewardedAd ad = RewardedAd.create(new RewardedAdsListener() {
            @Override
            public void onOpened() {
                Log.d(BuildConfig.LOG_TAG, "Rewarded ad opened");
            }
            
            @Override
            public void onFailedToOpen(String reason) {
                Log.d(  BuildConfig.LOG_TAG,
                        "Rewarded ad failed to open: " + reason);
            }
            
            @Override
            public void onClosed(boolean completed) {
                Log.d(BuildConfig.LOG_TAG, "Rewarded ad closed");
            }
        });
        if (ad != null) {
            ad.show();
        } else {
            Log.w(BuildConfig.LOG_TAG, "Rewarded ad not created");
        }
    }
    
    public void onShowEngageRewardedAd(View view) {
        DDNA.instance().requestEngagement(
                new Engagement("testAdPoint"),
                new EngageListener<Engagement>() {
                    @Override
                    public void onCompleted(Engagement engagement) {
                        RewardedAd ad = RewardedAd.create(engagement);
                        if (ad != null) {
                            ad.show();
                        } else {
                            Log.d(  BuildConfig.LOG_TAG,
                                    "Engage not setup to show ad");
                        }
                    }
                    
                    @Override
                    public void onError(Throwable t) {
                        Log.d(BuildConfig.LOG_TAG, "Failed to engage", t);
                    }
                });
    }
    
    public void onEngageRewardOrImage(View view) {
        DDNA.instance().requestEngagement(
                new Engagement("rewardOrImage"),
                new EngageListener<Engagement>() {
                    @Override
                    public void onCompleted(Engagement engagement) {
                        RewardedAd reward = RewardedAd.create(engagement);
                        ImageMessage image = ImageMessage.create(engagement);
                        
                        if (image != null) {
                            image.prepare(new ImageMessage.PrepareListener() {
                                @Override
                                public void onPrepared(ImageMessage src) {
                                    src.show(ExampleActivity.this, 1);
                                }
                                
                                @Override
                                public void onError(Throwable cause) {
                                    Log.d(  BuildConfig.LOG_TAG,
                                            "Failed to prepare image",
                                            cause);
                                }
                            });
                        } else if (reward != null) {
                            reward.show();
                        }
                    }
                    
                    @Override
                    public void onError(Throwable t) {
                        Log.d(BuildConfig.LOG_TAG, "Failed to engage", t);
                    }
                });
    }
}
