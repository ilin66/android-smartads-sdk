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

package com.deltadna.android.sdk.ads;

import android.support.annotation.Nullable;

import com.deltadna.android.sdk.Engagement;
import com.deltadna.android.sdk.ads.listeners.InterstitialAdsListener;

import org.json.JSONObject;

/**
 * Class for creating and showing an interstitial ad.
 * <p>
 * The ad can be created through one of the static {@code create} helpers,
 * from an {@link Engagement} as well as without one.
 * <p>
 * The ad can be shown through {@link #show()}.
 * <p>
 * {@link DDNASmartAds} must be registered for ads beforehand.
 */
public final class InterstitialAd {
    
    /**
     * Parameters from the Engage response if the ad was created from a
     * successful {@link Engagement}, else {@code null}.
     */
    @Nullable
    public final JSONObject params;
    @Nullable
    private final InterstitialAdsListener listener;
    
    private final DDNASmartAds smartAds = DDNASmartAds.instance();
    
    private InterstitialAd(
            @Nullable JSONObject params,
            @Nullable final InterstitialAdsListener listener) {
        
        this.params = params;
        this.listener = listener;
    }
    
    /**
     * Gets whether an ad is available to be shown.
     *
     * @return {@code true} when an ad is available, else {@code false}
     */
    public boolean isReady() {
        return (smartAds.getAds() != null
                && smartAds.getAds().isInterstitialAdAvailable());
    }
    
    /**
     * Shows an ad, if one is available.
     *
     * @return this instance
     */
    public InterstitialAd show() {
        final Ads ads = smartAds.getAds();
        if (ads != null) {
            ads.setInterstitialAdsListener(listener);
            ads.showInterstitialAd(null);
        }
        
        return this;
    }
    
    /**
     * Creates an interstitial ad.
     * <p>
     * {@code null} may be returned in case the ad is now allowed to show (ie
     * too many ads shown during the session).
     *
     * @return the interstitial ad, or {@code null}
     */
    @Nullable
    public static InterstitialAd create() {
        return create((InterstitialAdsListener) null);
    }
    
    /**
     * Creates an interstitial ad.
     * <p>
     * {@code null} may be returned in case the ad is now allowed to show (ie
     * too many ads shown during the session).
     *
     * @param listener  the listener for events within the ad lifecycle, may be
     *                  {@code null}
     *
     * @return the interstitial ad, or {@code null}
     */
    @Nullable
    public static InterstitialAd create(
            @Nullable InterstitialAdsListener listener) {
        
        return create(null, listener);
    }
    
    /**
     * Creates an interstitial ad from an Engage,ent once it has been populated
     * with response data after a successful request.
     * <p>
     * {@code null} may be returned in case the Engagement was not set-up to
     * show an interstitial ad.
     *
     * @param engagement the Engagement with response data
     *
     * @return  the interstitial ad created from {@code engagement}, else
     *          {@code null}
     */
    @Nullable
    public static InterstitialAd create(Engagement engagement) {
        return create(engagement, null);
    }
    
    /**
     * Creates an interstitial ad from an Engagement once it has been populated
     * with response data after a successful request.
     * <p>
     * {@code null} may be returned in case the Engagement was not set-up to
     * show an ad, or the ad is now allowed to show (ie too many ads shown
     * during the session).
     *
     * @param engagement    the Engagement with response data
     * @param listener      the listener for events within the ad lifecycle,
     *                      may be {@code null}
     *
     * @return  the interstitial ad created from {@code engagement}, else
     *          {@code null}
     */
    @Nullable
    public static InterstitialAd create(
            @Nullable Engagement engagement,
            @Nullable InterstitialAdsListener listener) {
        
        final Ads ads = DDNASmartAds.instance().getAds();
        if (ads == null || !ads.isInterstitialAdAllowed(engagement))  {
            return null;
        } else {
            return new InterstitialAd(
                    (engagement == null || engagement.getJson() == null)
                            ? null
                            : engagement.getJson().optJSONObject("parameters"),
                    listener);
        }
    }
}
