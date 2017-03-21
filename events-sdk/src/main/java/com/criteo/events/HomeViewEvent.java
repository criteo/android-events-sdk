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

/**
 * The HomeViewEvent is to be used when a user views/visits the home page of the app.
 * @since v1.0
 */
public class HomeViewEvent extends Event{

    /**
     * Construct a new HomeViewEvent
     */
    public HomeViewEvent() {
    }

    /**
     * Construct a new HomeViewEvent by copying another HomeViewEvent
     * @param event Event to copy
     */
    public HomeViewEvent(HomeViewEvent event) {
        super(event);
    }
}
