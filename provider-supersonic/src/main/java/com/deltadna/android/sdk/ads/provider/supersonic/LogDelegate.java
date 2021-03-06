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

package com.deltadna.android.sdk.ads.provider.supersonic;

import android.util.Log;

import com.supersonic.mediationsdk.logger.LogListener;
import com.supersonic.mediationsdk.logger.SupersonicLogger;

import java.util.Locale;

final class LogDelegate implements LogListener {
    
    @Override
    public void onLog(
            SupersonicLogger.SupersonicTag tag,
            String message,
            int level) {
        
        Log.d(BuildConfig.LOG_TAG, String.format(
                Locale.US,
                "Level/tag/message %d/%s/%s",
                level,
                tag,
                message));
    }
}
