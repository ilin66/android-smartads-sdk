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

package com.deltadna.android.sdk.ads.core;

@UnityInterOp
public interface AdServiceListener {

    void onRegisteredForInterstitialAds();

    void onFailedToRegisterForInterstitialAds(String reason);

    void onRegisteredForRewardedAds();

    void onFailedToRegisterForRewardedAds(String reason);

    void onInterstitialAdOpened();

    void onInterstitialAdFailedToOpen(String reason);

    void onInterstitialAdClosed();

    void onRewardedAdOpened();

    void onRewardedAdFailedToOpen(String reason);

    void onRewardedAdClosed(boolean completed);

    void onRecordEvent(String name, String jsonParams);

    void onRequestEngagement(
            String decisionPoint,
            String flavour,
            EngagementListener listener);
}
