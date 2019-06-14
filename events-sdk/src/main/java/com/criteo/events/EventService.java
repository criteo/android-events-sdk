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

import android.app.Activity;
import android.content.Context;
import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * The Event Service class is the main class that send will pack and send events to Criteo.
 * By default your Criteo account is automatically setup using the default constructor. This however
 * may be overridden by manually setting the country and language.
 * Having made an event, it then needs to be passed into the send function for it to be sent to
 * Criteo.
 * @since v1.0
 */
public class EventService {
    private String country;
    private String language;
    private String customerId;
    private String email;
    private String accountName;

    private Executor executor;
    private JSONSerializer jsonSerializer = new JSONSerializer();

    static String sharedPrefs = "CriteoTracker";
    static Context context;

    /**
     * The AsyncEventHandler class is a simple wrapper that handles sending events asynchronously.
     * The event service class uses this class through an executor that starts a single thread which
     * is guaranteed to not be the same as the main UI thread.
     * It allows the retrieval of the GAID and handling of the shared preferences without risking locking
     * the client's application, while keeping a strict control over the number of spawned threads.
     */
    private class AsyncEventHandler implements Runnable {
        private JSONObject serializedEvent;
        private long eventTimestamp;

        private AsyncEventHandler(JSONObject serializedEvent, long eventTimestamp) {
            this.serializedEvent = serializedEvent;
            this.eventTimestamp = eventTimestamp;
        }

        @Override
        public void run() {
            try {
                JSONObject payload = JSONSerializer.commonPayload(EventService.this);
                JSONSerializer.setPayloadEvent(payload, serializedEvent);
                EventSenderService.sendEvent(payload.toString(), eventTimestamp, context);
            } catch (JSONException e) {
                CRTOLog.e("Error in JSON serialisation", e);
            }
        }
    }

    /**
     * Accepted address email formats for the setEmail method.
     * @since v1.1
     */
    public enum EmailType {
        /**
         * The email address is given in plain text and hashed by the platform.
         */
        CLEARTEXT,
        /**
         * The email address is given as an already computed MD5 hexadecimal string hash.
         */
        HASHED_MD5,
    }

    /**
     * Construct a default EventService
     * @param context the Android application context (usually obtained
     *                from {@link Activity#getApplicationContext() getApplicationContext()})
     */
    public EventService(Context context) {
        EventService.context = context;
        country  = getDefaultCountry();
        language = getDefaultLanguage();
        accountName = getDefaultAccountName();
        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Construct a new EventService with overidden country and language. This is passed with every event to determine the account and currency.
     * @param context the Android application context (usually obtained
     *                from {@link Activity#getApplicationContext() getApplicationContext()})
     * @param country ISO 3166-1 two letter country code
     * @param language ISO 639-1 two-letter language code
     */
    public EventService(Context context, String country, String language) {
        EventService.context = context;
        this.country = country;
        this.language = language;
    }

    /**
     * Construct a new EventService with overidden country, language and Customer ID
     * @param context the Android application context (usually obtained
     *                from {@link Activity#getApplicationContext() getApplicationContext()})
     * @param country ISO 3166-1 two letter country code
     * @param language ISO 639-1 two-letter language code
     * @param customerId Customer ID of current user
     */
    public EventService(Context context, String country, String language, String customerId) {
        EventService.context = context;
        this.country = country;
        this.language = language;
        this.customerId = customerId;
    }

    /**
     * Gets previously set email.
     * @return Hashed email as a string
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email address of the user that can be then used for cross device retargeting.
     * Note: The email address is never used or stored in its raw form. It is first hashed to anonymize the user.
     * @param email Email address of user as clear text
     */
    public void setEmail(String email) {
        setEmail(email, EmailType.CLEARTEXT);
    }

    /**
     * Set the email address of the user that can be then used for cross device retargeting.
     * This method accepts clear text emails, as well as already MD5 hashed strings.
     * Note: To be properly passed as a MD5 hash, the address email must be converted to lower case
     * and trimmed of all whitespaces before being hashed.
     * @param email Email address of user
     * @param type Type of the email address, can either be {@link EventService.EmailType#CLEARTEXT}
     *             or {@link EventService.EmailType#HASHED_MD5}
     * @since v1.1
     */
    public void setEmail(String email, EmailType type) {
        switch (type) {
            case CLEARTEXT:
                Pattern emailPattern = Patterns.EMAIL_ADDRESS;
                if (email == null || !emailPattern.matcher(email).matches()) {
                    CRTOLog.e("Argument email is not a valid email address");
                    return;
                }
                this.email = EmailHasher.computeMd5Hash(email);
                break;
            case HASHED_MD5:
                this.email = email;
                break;
            default:
                CRTOLog.e("Argument type is not a valid email type");
        }
    }

    /**
     * Gets currently set country.
     * @return ISO 3166-1 two letter country code
     */
    public String getCountry() {
        return country;
    }

    /**
     * Override the country that is sent with every event.
     * @param country ISO 3166-1 two letter country code
     */
    public void setCountry(String country) {
        if(isCountryValid(country)){
            this.country = country;
        } else {
            CRTOLog.e("Argument country must be valid ISO 3166-1 two-letter code");
        }
    }

    /**
     * Gets currently set language.
     * @return ISO 639-1 two-letter language code
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Override the language that is sent with every event.
     * @param language ISO 639-1 two-letter language code
     */
    public void setLanguage(String language) {
        if(isLanguageValid(language)){
            this.language = language;
        } else {
            CRTOLog.e("Argument language must be valid ISO 639-1 two-letter code");
        }
    }

    /**
     * Gets previously set customer ID.
     * @return Customer ID string
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Set your internal customer ID used to uniquely distinguish this user.
     * (This should not be personally identifable data)
     * @param customerId Unique ID of user
     */
    public void setCustomerId(String customerId) {
        if (customerId == null ) {
            CRTOLog.e("Argument customerId must not be null");
            return;
        }
        this.customerId = customerId;
    }

    /**
     * Set the account name that is sent with every event.
     * By default, it is set to the package name of the application.
     * @param accountName Account name
     * @since v1.1
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * Gets previously set account name.
     * @return account name string
     * @since v1.1
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * Send AppLaunchEvent to Criteo
     * @param event Event to send
     */
    public void send(AppLaunchEvent event) {
        event.setTimestamp(new Date());
        sendSerializedEvent(jsonSerializer.serializeToJSON(event), event.getTimestamp().getTime());
    }

    /**
     * Send BasketViewEvent to Criteo
     * @param event Event to send
     */
    public void send(BasketViewEvent event) {
        event.setTimestamp(new Date());
        sendSerializedEvent(jsonSerializer.serializeToJSON(event), event.getTimestamp().getTime());
    }

    /**
     * Send HomeViewEvent to Criteo
     * @param event Event to send
     */
    public void send(HomeViewEvent event) {
        event.setTimestamp(new Date());
        sendSerializedEvent(jsonSerializer.serializeToJSON(event), event.getTimestamp().getTime());
    }

    /**
     * Send ProductListViewEvent to Criteo
     * @param event Event to send
     */
    public void send(ProductListViewEvent event) {
        event.setTimestamp(new Date());
        sendSerializedEvent(jsonSerializer.serializeToJSON(event), event.getTimestamp().getTime());
    }

    /**
     * Send ProductViewEvent to Criteo
     * @param event Event to send
     */
    public void send(ProductViewEvent event) {
        event.setTimestamp(new Date());
        sendSerializedEvent(jsonSerializer.serializeToJSON(event), event.getTimestamp().getTime());
    }

    /**
     * Send TransactionConfirmationEvent to Criteo
     * @param event Event to send
     */
    public void send(TransactionConfirmationEvent event) {
        event.setTimestamp(new Date());
        sendSerializedEvent(jsonSerializer.serializeToJSON(event), event.getTimestamp().getTime());
    }

    /**
     * Send DataEvent to Criteo
     * @param event Event to send
     */
    public void send(DataEvent event) {
        event.setTimestamp(new Date());
        sendSerializedEvent(jsonSerializer.serializeToJSON(event), event.getTimestamp().getTime());
    }

    /**
     * Send DeeplinkEvent to Criteo
     * @param event Event to send
     */
    public void send(DeeplinkEvent event) {
        event.setTimestamp(new Date());
        sendSerializedEvent(jsonSerializer.serializeToJSON(event), event.getTimestamp().getTime());
    }

    private static String getDefaultCountry() {
        return DeviceInfo.getCountry();
    }

    private static String getDefaultLanguage() {
        return DeviceInfo.getLanguage();
    }

    private static String getDefaultAccountName() {
        return DeviceInfo.getBundleName();
    }

    private static boolean isCountryValid(String country) {
        boolean valid = false;
        if(Arrays.asList(DeviceInfo.getCountriesList()).contains(country)){
            valid = true;
        }
        return valid;
    }

    private static boolean isLanguageValid(String language) {
        boolean valid = false;
        if(Arrays.asList(DeviceInfo.getLanguageList()).contains(language)){
            valid = true;
        }
        return valid;
    }

    private void sendSerializedEvent(JSONObject serializedEvent, long eventTimestamp) {
        if (serializedEvent == null) {
            return;
        }

        executor.execute(new AsyncEventHandler(serializedEvent, eventTimestamp));
    }
}
