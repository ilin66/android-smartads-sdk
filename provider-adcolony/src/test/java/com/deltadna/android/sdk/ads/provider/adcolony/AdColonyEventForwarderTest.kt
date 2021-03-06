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

package com.deltadna.android.sdk.ads.provider.adcolony

import com.adcolony.sdk.AdColonyInterstitial
import com.deltadna.android.sdk.ads.bindings.AdRequestResult
import com.deltadna.android.sdk.ads.bindings.MediationAdapter
import com.deltadna.android.sdk.ads.bindings.MediationListener
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockito_kotlin.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AdColonyEventForwarderTest {
    
    private val listener = mock<MediationListener>()
    private val adapter = mock<MediationAdapter>()
    private val ad = mock<AdColonyInterstitial>()
    
    private var uut = AdColonyEventForwarder(listener, adapter)
    
    @Before
    fun before() {
        uut = AdColonyEventForwarder(listener, adapter)
    }
    
    @After
    fun after() {
        reset(listener, adapter, ad)
    }
    
    @Test
    fun onRequestNotFilled() {
        uut.onRequestNotFilled(mock())
        
        verify(listener).onAdFailedToLoad(
                same(adapter),
                eq(AdRequestResult.NoFill),
                any())
        assertThat(uut.ad).isNull()
    }
    
    @Test
    fun onRequestFilled() {
        uut.onRequestFilled(ad)
        
        verify(listener).onAdLoaded(same(adapter))
        assertThat(uut.ad).isSameAs(ad)
    }
    
    @Test
    fun onOpened() {
        uut.onRequestFilled(ad)
        uut.onOpened(ad)
        
        inOrder(listener) {
            verify(listener).onAdLoaded(same(adapter))
            verify(listener).onAdShowing(same(adapter))
        }
        assertThat(uut.ad).isSameAs(ad)
    }
    
    @Test
    fun onClicked() {
        uut.onRequestFilled(ad)
        uut.onOpened(ad)
        uut.onClicked(ad)
        
        inOrder(listener) {
            verify(listener).onAdLoaded(same(adapter))
            verify(listener).onAdShowing(same(adapter))
            verify(listener).onAdClicked(same(adapter))
        }
        assertThat(uut.ad).isSameAs(ad)
    }
    
    @Test
    fun onClosed() {
        uut.onRequestFilled(ad)
        uut.onOpened(ad)
        uut.onClosed(ad)
        
        inOrder(listener) {
            verify(listener).onAdLoaded(same(adapter))
            verify(listener).onAdShowing(same(adapter))
            verify(listener).onAdClosed(same(adapter), eq(true))
        }
        assertThat(uut.ad).isNull()
    }
    
    @Test
    fun onLeftApplication() {
        uut.onRequestFilled(ad)
        uut.onOpened(ad)
        uut.onLeftApplication(ad)
        
        inOrder(listener) {
            verify(listener).onAdLoaded(same(adapter))
            verify(listener).onAdShowing(same(adapter))
            verify(listener).onAdLeftApplication(same(adapter))
        }
        assertThat(uut.ad).isSameAs(ad)
    }
    
    private fun inOrder(vararg mocks: Any, block: org.mockito.InOrder.() -> Unit) {
        block.invoke(org.mockito.Mockito.inOrder(*mocks))
    }
}
