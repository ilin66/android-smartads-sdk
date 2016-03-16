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

package com.deltadna.android.sdk.ads.integrationtester;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.deltadna.android.sdk.ads.bindings.AdClosedResult;
import com.deltadna.android.sdk.ads.bindings.AdRequestResult;
import com.deltadna.android.sdk.ads.bindings.MediationAdapter;
import com.deltadna.android.sdk.ads.bindings.MediationListener;
import com.deltadna.android.sdk.ads.provider.admob.AdMobAdapter;
import com.deltadna.android.sdk.ads.provider.applovin.AppLovinInterstitialAdapter;
import com.deltadna.android.sdk.ads.provider.applovin.AppLovinRewardedAdapter;
import com.deltadna.android.sdk.ads.provider.mobfox.MobFoxAdapter;
import com.deltadna.android.sdk.ads.provider.supersonic.SupersonicInterstitialAdapter;
import com.deltadna.android.sdk.ads.provider.supersonic.SupersonicRewardedAdapter;
import com.deltadna.android.sdk.ads.provider.vungle.VungleAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class IntegrationActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener {
    
    private static final List<? extends MediationAdapter> PROVIDERS =
            Arrays.asList(
                    new AdMobAdapter(
                            0,
                            0,
                            0,
                            "ca-app-pub-3117129396855330/6027943007"),
                    new AppLovinInterstitialAdapter(
                            0,
                            0,
                            0,
                            "awUGLVznHxpXiT6xATnSkZkXwCTapgzOaRexxchDXVlFzsB8Oza6WIeQU60InzhMPLIKz_XVUHmS7xaVuAcOTF",
                            false,
                            -1),
                    new AppLovinRewardedAdapter(
                            0,
                            0,
                            0,
                            "awUGLVznHxpXiT6xATnSkZkXwCTapgzOaRexxchDXVlFzsB8Oza6WIeQU60InzhMPLIKz_XVUHmS7xaVuAcOTF",
                            false,
                            -1),
                    new MobFoxAdapter(
                            0,
                            0,
                            0,
                            "303fc0e182f1e126f276537f2b3d01ee"),
                    new SupersonicInterstitialAdapter(
                            0,
                            0,
                            0,
                            "45d0aacd",
                            false),
                    new SupersonicRewardedAdapter(
                            0,
                            0,
                            0,
                            "45d0aacd",
                            false),
                    new VungleAdapter(
                            0,
                            0,
                            0,
                            "55e5cbcf15d3c2221b000275"));
    private static final List<String> PROVIDER_NAMES;
    static {
        final List<String> names = new ArrayList<>(PROVIDERS.size());
        for (final MediationAdapter provider : PROVIDERS) {
            names.add(provider.getClass().getSimpleName());
        }
        PROVIDER_NAMES = names;
    }
    
    private MediationAdapter provider = PROVIDERS.get(0);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_integration);
        final ListView viewProviders = (ListView) findViewById(R.id.integration_providers);
        viewProviders.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_single_choice,
                PROVIDER_NAMES));
        viewProviders.setOnItemClickListener(this);
        viewProviders.setItemChecked(0, true);
    }
    
    @Override
    public void onItemClick(
            AdapterView<?> parent, View view, int position, long id) {
        
        provider = PROVIDERS.get(position);
        
        debug("Using " + provider.getClass().getSimpleName());
    }
    
    public void onRequestAd(View view) {
        provider.requestAd(this, new Listener(), new JSONObject());
    }
    
    public void onShowAd(View view) {
        provider.showAd();
    }
    
    private static void debug(String msg) {
        Log.d(IntegrationActivity.class.getSimpleName(), msg);
    }
    
    private final class Listener implements MediationListener {
        
        @Override
        public void onAdLoaded(MediationAdapter mediationAdapter) {
            debug("Ad loaded");
        }
        
        @Override
        public void onAdFailedToLoad(
                MediationAdapter mediationAdapter,
                AdRequestResult adLoadResult,
                String reason) {
            
            debug("Ad failed to load; result: " + adLoadResult + ' ' + reason);
        }
        
        @Override
        public void onAdShowing(MediationAdapter mediationAdapter) {
            debug("Ad showing");
        }
        
        @Override
        public void onAdFailedToShow(
                MediationAdapter mediationAdapter,
                AdClosedResult adClosedResult) {
            
            debug("Ad failed to show; result: " + adClosedResult);
        }
        
        @Override
        public void onAdClicked(MediationAdapter mediationAdapter) {
            debug("Ad clicked");
        }
        
        @Override
        public void onAdLeftApplication(MediationAdapter mediationAdapter) {
            debug("Ad left application");
        }
        
        @Override
        public void onAdClosed(
                MediationAdapter mediationAdapter,
                boolean complete) {
            
            debug("Ad closed; complete: " + complete);
        }
    }
}