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

import java.util.GregorianCalendar;

/**
 * The DataEvent is for sending non event specific data to Criteo.
 * This class should be used to send extra data parameters to Criteo when they do not relate to a more specific event subclass.
 * Use the addExtraData methods to add data to this event.
 * @since v1.0
 */
public class DataEvent extends Event{

    /**
     * Construct a new DataEvent
     */
    public DataEvent() {
    }

    /**
     * Construct a new DataEvent by copying another DataEvent
     * @param event Event to copy
     */
    public DataEvent(DataEvent event) {
        super(event);
    }

    @Override
    public GregorianCalendar getStartDate() {
        return super.getStartDate();
    }

    @Override
    public GregorianCalendar getEndDate() {
        return super.getEndDate();
    }

    @Override
    public void setStartDate(GregorianCalendar startDate) {
        super.setStartDate(startDate);
    }

    @Override
    public void setEndDate(GregorianCalendar endDate) {
        super.setEndDate(endDate);
    }
}
