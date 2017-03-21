/**
 * Copyright (C)2016 - Criteo
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
package com.criteo.events;

class AdvertisingInfo {
    private final String advertisingId;
    private final boolean latEnabled;

    public AdvertisingInfo(String advertisingId, boolean latEnabled) {
        this.advertisingId = advertisingId;
        this.latEnabled = latEnabled;
    }

    public String getAdvertisingId() {
        return advertisingId;
    }

    public boolean isLatEnabled() {
        return latEnabled;
    }
}
