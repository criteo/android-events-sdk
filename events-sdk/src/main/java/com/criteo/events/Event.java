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

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Event class and related classes provide an API for submitting events from your native
 * Android application to Criteo.
 * <p>
 * <b>Warning:</b> This class provides base functionality for other event subclasses, and is not intended
 * to be used directly from your code.
 * @since v1.0
 */
public abstract class Event
{
    // This is the default value for the user segment, which means `no segment or segments not activated`
    static final int NO_USER_SEGMENT = 0;

    ConcurrentHashMap<String,ExtraData> extraDataMap = new ConcurrentHashMap<>();

    private Date timestamp = new Date();

    Event() {
    }

    Event(Event event) {
        this.extraDataMap = new ConcurrentHashMap<>(event.extraDataMap);
        this.setTimestamp(event.getTimestamp());
    }

    protected GregorianCalendar getStartDate() {
        ExtraData value = extraDataMap.get(EventKeys.START_DATE);
        return value.getDateValue();
    }

    protected GregorianCalendar getEndDate() {
        ExtraData value = extraDataMap.get(EventKeys.END_DATE);
        return value.getDateValue();
    }

    /**
     * The start or travel checkin date associated with this event.
     * @param startDate Date of start or checkin
     */
    protected void setStartDate(GregorianCalendar startDate) {
        if (startDate == null) {
            CRTOLog.e("The startDate and endDate arguments must not be null");
            return;
        }
        extraDataMap.put(EventKeys.START_DATE, new ExtraData(startDate));
    }

    /**
     * The end or travel checkout date associated with this event.
     * @param endDate Date of end or checkout
     */
    protected void setEndDate(GregorianCalendar endDate) {
        if (endDate == null) {
            CRTOLog.e("The startDate and endDate arguments must not be null");
            return;
        }
        extraDataMap.put(EventKeys.END_DATE, new ExtraData(endDate));
    }

    /**
     * Set the user segment for this event. The default value for the user segment is 0.
     * This value is optional. If you do not call this method, it will not be sent to Criteo.
     * @param segment user segment for this event as an integer
     * @since v1.1
     */
    public void setUserSegment(int segment) {
        extraDataMap.put(EventKeys.USER_SEGMENT, new ExtraData(segment));
    }

    /**
     * Get the user segment for this event.
     * @return the user segment associated with this event, or 0 if it has not been set
     * @since v1.1
     */
    public int getUserSegment() {
        ExtraData value = extraDataMap.get(EventKeys.USER_SEGMENT);
        return value != null ? value.getIntValue() : NO_USER_SEGMENT;
    }

    /**
     * Add custom {@link Float} data to the event using a key/value pair
     * @param key the name of the extra data
     * @param value the float value for the extra data
     * @return this event
     */
    public Event addExtraData(String key, float value) {
        putExtraData(key, new ExtraData(value));
        return this;
    }

    /**
     * Add custom {@link Date} data to the event using a key/value pair
     * @param key the name of the extra data
     * @param value the date value for the extra data
     * @return this event
     */
    public Event addExtraData(String key, GregorianCalendar value) {
        putExtraData(key, new ExtraData(value));
        return this;
    }

    /**
     * Add custom {@link Integer} data to the event using a key/value pair
     * @param key the name of the extra data
     * @param value the integer value for the extra data
     * @return this event
     */
    public Event addExtraData(String key, int value) {
        putExtraData(key, new ExtraData(value));
        return this;
    }

    /**
     * Add custom {@link String} data to the event using a key/value pair
     * @param key the name of the extra data
     * @param value the string value for the extra data
     * @return this event
     */
    public Event addExtraData(String key, String value) {
        putExtraData(key, new ExtraData(value));
        return this;
    }

    private void putExtraData(String key, ExtraData value) {
        if(EventKeys.isKeyReserved(key)) {
            String error = String.format("The key argument %s must not be a reserved EventKey value.", key);
            CRTOLog.e(error);
            return;
        }

        extraDataMap.put(key, value);
    }

    protected void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    protected Date getTimestamp() {
        return timestamp;
    }

    ExtraData getExtraData(String key) {
        return extraDataMap.get(key);
    }

    /**
     * Returns extra data Float for specified key
     * @param key the name of the extra data
     * @return the float value of the extra data or 0 if it does not exist
     */
    public float getFloatExtraData(String key) {
        ExtraData value = extraDataMap.get(key);
        return value.getFloatValue();
    }

    /**
     * Returns extra data Int for specified key
     * @param key the name of the extra data
     * @return the integer value of the extra data or 0 if it does not exist
     */
    public int getIntExtraData(String key) {
        ExtraData value = extraDataMap.get(key);
        return value.getIntValue();
    }

    /**
     * Returns extra data GregorianCalendar for specified key
     * @param key the name of the extra data
     * @return the date value of the extra data or null if it does not exist
     */
    public GregorianCalendar getDateExtraData(String key) {
        ExtraData value = extraDataMap.get(key);
        return value.getDateValue();
    }

    /**
     * Returns extra data String for specified key
     * @param key the name of the extra data
     * @return the string value of the extra data or null if it does not exist
     */
    public String getStringExtraData(String key) {
        ExtraData value = extraDataMap.get(key);
        return value.getStringValue();
    }

    Map<String,ExtraData> getExtraDataMap() {
        return new ConcurrentHashMap<String, ExtraData>(extraDataMap);
    }

    /**
     * Returns all extra data stored in the event as a string.
     * @return a string representing all extra data
     */
    public String extraDatatoString() {
        return extraDataMap.toString();
    }
}
