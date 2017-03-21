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
import android.content.SharedPreferences;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An AppLaunchEvent is used to indicate that the app has been launched. This should be called everytime the user opens the app.
 * If the app was launched from a deeplink url, you should set this url in the event.
 * If a referrer was passed from Google this also should be set in the event.
 * @since v1.0
 */
public class AppLaunchEvent extends Event {
    private AtomicReference<String> googleReferrer = new AtomicReference<>();

    /**
     * Construct a default AppLaunchEvent
     */
    public AppLaunchEvent() {
    }

    /**
     * Construct a new AppLaunchEvent by copying another AppLaunchEvent
     * @param event Event to copy
     */
    public AppLaunchEvent(AppLaunchEvent event) {
        super(event);
    }

    /**
     * Gets the previously set Google Referrer value.
     * @return Google Referrer as string.
     */
    public String getGoogleReferrer() {
        return googleReferrer.get();
    }

    /**
     * Sets the Google Referrer to be passed to Criteo. This should be picked up using by listening for the Google Play intent: com.android.vending.INSTALL_REFERRER
     * This can then be parsed to a string and set in this method. Dont set if no referrer is recieved.
     * @param googleReferrer String of the URI picked up from the Google Play store intent. Must not be null.
     */
    public void setGoogleReferrer(String googleReferrer) {
        if (googleReferrer == null ) {
            CRTOLog.e("Argument googleReferrer must not be null");
            return;
        }

        this.googleReferrer.set(googleReferrer);
    }

    /**
     * Determines whether the application has been launched before.
     * If the application has not been launched before, writes an integer in the shared preferences
     * of the application for future reference.
     * It MUST NOT be executed from the main UI thread, because Android gives a warning if shared
     * preferences are used in a way that can impact the responsiveness of the application.
     * @return true if the app has never been launched before, false otherwise
     */
    boolean isFirstLaunch() {
        SharedPreferences settings = EventService.context.getSharedPreferences(EventService.sharedPrefs, Context.MODE_PRIVATE);

        int firstLaunch = settings.getInt(EventKeys.FIRST_LAUNCH, 0);
        if (firstLaunch == 0) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(EventKeys.FIRST_LAUNCH, 1);
            editor.apply();
            return true;
        } else {
            return false;
        }
    }
}
