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

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The DeeplinkEvent is for sending an event if the app was launched via a deeplink url.
 * Only send this event once if a deeplink is detected and the URL can be provided.
 * @since v1.0
 */
public class DeeplinkEvent extends Event {

    private AtomicReference<String> deeplinkUrl = new AtomicReference<>();

    /**
     * Construct a new DeeplinkEvent with url passed as string.
     * @param url String of the deeplink used to launch the app.
     */
    public DeeplinkEvent(String url) {
        if (deeplinkUrl == null) {
            CRTOLog.e("Argument deeplinkUrl must not be null");
            return;
        }
        this.deeplinkUrl.set(url);
    }

    /**
     * Construct a new DeeplinkEvent with url passed as URI.
     * @param url URI of the deeplink used to launch the app.
     */
    public DeeplinkEvent(URI url) {
        if (deeplinkUrl == null) {
            CRTOLog.e("Argument deeplinkUrl must not be null");
            return;
        }
        this.deeplinkUrl.set(url.toString());
    }

    /**
     * Construct a new DeeplinkEvent by copying another DeeplinkEvent
     * @param event Event to copy
     */
    public DeeplinkEvent(DeeplinkEvent event) {
        super(event);
        this.deeplinkUrl.set(event.getDeeplinkUrl());
    }

    /**
     * Gets the previously set deeplink value.
     * @return Deeplink as string.
     */
    public String getDeeplinkUrl() {
        return deeplinkUrl.get();
    }


}
