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

import android.content.pm.PackageManager;
import android.os.Build;

import java.util.Locale;

final class DeviceInfo
{
    private static AdvertisingInfo advertisingInfo = null;

    private DeviceInfo() {}

    static String getAppVersion() {
        try {
            return EventService.context.getPackageManager()
                    .getPackageInfo(EventService.context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e){
            CRTOLog.e("Error getting AppVersion", e);
            return "";
        }

    }
    static String getSDKVersion() {
        return BuildConfig.VERSION_NAME;
    }

    static String getModel()
    {
        return Build.MODEL;
    }

    static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    static String getBundleName() {
        return EventService.context.getPackageName();
    }

    static String getCountry() {
        return Locale.getDefault().getCountry();
    }

    static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    static String[] getCountriesList() {
        return Locale.getISOCountries();
    }

    static String[] getLanguageList() {
        return Locale.getISOLanguages();
    }

    static String getAdvertisingId() {
        if (advertisingInfo == null) {
            advertisingInfo = AdvertisingInfoLoader.getAdvertisingInfo();
        }

        return advertisingInfo != null ? advertisingInfo.getAdvertisingId() : null;
    }

    static boolean getLimitAdTrackingEnabled() {
        if (advertisingInfo == null) {
            advertisingInfo = AdvertisingInfoLoader.getAdvertisingInfo();
        }

        return advertisingInfo != null ? advertisingInfo.isLatEnabled() : false;
    }

    static void setIdentifiers(String GAID, boolean LAT) {
        advertisingInfo = new AdvertisingInfo(GAID, LAT);
    }
}
