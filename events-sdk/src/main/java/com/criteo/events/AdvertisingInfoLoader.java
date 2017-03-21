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

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.lang.reflect.Method;

/**
 * This class retrieves the Advertising Id information using the Google Play Services API.
 * The code uses reflection because it relies on the fact that the hosting application links
 * with it, instead of the SDK itself.
 * It MUST NOT be used from the main UI thread because the Google API does not allow it and
 * will throw an IllegalStateException.
 */
class AdvertisingInfoLoader {
    private final static int GOOGLE_PLAY_SUCCESS_CODE = 0;
    private final static String PlayServicesUtilClassName = "com.google.android.gms.common.GooglePlayServicesUtil";
    private final static String AdvertisingIdClientClassName = "com.google.android.gms.ads.identifier.AdvertisingIdClient";

    public static AdvertisingInfo getAdvertisingInfo() {
        try {
            Class<?> googleService = Class.forName(PlayServicesUtilClassName);
            Method gpsAvail = googleService.getMethod("isGooglePlayServicesAvailable", Context.class);

            int resultCode = (Integer) gpsAvail.invoke(googleService, EventService.context);
            if (resultCode != GOOGLE_PLAY_SUCCESS_CODE) {
                CRTOLog.e("Unable to retrieve Google Ad ID");
                return null;
            }

            ApplicationInfo ai = EventService.context.getPackageManager().getApplicationInfo(EventService.context.getPackageName(), android.content.pm.PackageManager.GET_META_DATA);
            if (ai.metaData.getInt("com.google.android.gms.version") == 0) {
                CRTOLog.e("You must include and then set the com.google.android.gms.version value in the AndroidManifest.xml file in order to use the GAID");
                return null;
            }

            Class<?> AdvertisingIdClient = Class.forName(AdvertisingIdClientClassName);
            Method getAdvertisingIdInfo = AdvertisingIdClient.getDeclaredMethod("getAdvertisingIdInfo", Context.class);
            Object adInfo = getAdvertisingIdInfo.invoke(null, EventService.context);

            return new AdvertisingInfo(
                    reflectedGetAdvertisingId(adInfo, null),
                    reflectedIsLimitAdTrackingEnabled(adInfo, false)
            );
        } catch (Exception e) {
            CRTOLog.e("Unable to retrieve Google Ad ID", e);
            return null;
        }
    }

    private static String reflectedGetAdvertisingId(final Object adInfo, final String defaultValue) {
        try {
            Method getAdvertisingIdInfoMethodID = adInfo.getClass().getDeclaredMethod("getId");
            return (String) getAdvertisingIdInfoMethodID.invoke(adInfo);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static boolean reflectedIsLimitAdTrackingEnabled(final Object adInfo, final boolean defaultValue) {
        try {
            Method getAdvertisingIdInfoMethodLAT = adInfo.getClass().getDeclaredMethod("isLimitAdTrackingEnabled");
            return (boolean) getAdvertisingIdInfoMethodLAT.invoke(adInfo);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
