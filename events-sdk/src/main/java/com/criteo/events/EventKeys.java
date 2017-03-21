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

import java.util.HashSet;

/**
 * This class contains a Set of all reserved keys that are used by Criteo to store data. These reserved keyts may not be used to store any extra data with any events.
 */
final class EventKeys {
    static final String APP_LAUNCH = "appLaunch";
    static final String BASKET_VIEW = "viewBasket";
    static final String HOME_VIEW = "viewHome";
    static final String PRODUCT_LIST_VIEW = "viewListing";
    static final String PRODUCT_VIEW = "viewProduct";
    static final String TRANSACTION_CONFIRMATION = "trackTransaction";
    static final String DATA_EVENT = "setData"; //todo: change to viewSearch?
    static final String DEEPLINK_EVENT = "appDeeplink";

    static final String START_DATE = "checkin_date";
    static final String END_DATE = "checkout_date";
    static final String EVENTNAME = "event";
    static final String EVENTS = "events";
    static final String DEEPLINK = "deeplink_uri"; // todo ?
    static final String GOOGLE_REFFERER = "referrer"; // todo ?
    static final String FIRST_LAUNCH = "first_launch";
    static final String ACCOUNT_NAME = "an";
    static final String COUNTRY = "country_code";
    static final String LANGUAGE = "language_code";
    static final String SITE_TYPE = "site_type";
    static final String DEVICE_ID = "gaid";
    static final String LAT = "limit_ad_tracking";
    static final String ACCOUNT = "account";
    static final String ID = "id";
    static final String PRICE = "price";
    static final String QUANTITY = "quantity";
    static final String VALUE = "value";
    static final String TYPE = "type";
    static final String DEVICE_INFO = "device_info";
    static final String PLATFORM = "platform";
    static final String OS_NAME = "os_name";
    static final String OS_VERSION = "os_version";
    static final String DEVICE_MODEL = "device_model";
    static final String DEVICE_MANUFACTURER = "device_manufacturer";
    static final String APP_INFO = "app_info";
    static final String APP_ID = "app_id";
    static final String APP_NAME = "app_name";
    static final String APP_VERSION = "app_version";
    static final String SDK_VERSION = "sdk_version";
    static final String APP_LANGUAGE = "app_language";
    static final String APP_COUNTRY = "app_country";
    static final String PRODUCT = "product";
    static final String CURRENCY = "currency";
    static final String JSON_PROTOCOL_VERSION = "version";
    static final String TIMESTAMP = "timestamp";
    static final String CUSTOMER_ID = "customer_id";
    static final String HASH = "hash_method";
    static final String ALTERNATE_IDS = "alternate_ids";
    static final String DEDUPLICATION = "deduplication";
    static final String USER_SEGMENT = "user_segment";
    static final String NEW_CUSTOMER = "new_customer";


    private static final HashSet<String> reservedKeys;
    static
    {
        reservedKeys = new HashSet<>();

        reservedKeys.add("e");
        reservedKeys.add("p");
        reservedKeys.add("at");
        reservedKeys.add(ID);
        reservedKeys.add("din");
        reservedKeys.add("dout");
        reservedKeys.add("kw");
        reservedKeys.add("a");
        reservedKeys.add("b");
        reservedKeys.add("c");
        reservedKeys.add("idfa");
        reservedKeys.add("v");
        reservedKeys.add("ip");
        reservedKeys.add("ai");
        reservedKeys.add(START_DATE);
        reservedKeys.add(END_DATE);
        reservedKeys.add("ebs");
        reservedKeys.add(EVENTNAME);
        reservedKeys.add("item");
        reservedKeys.add("keywords");
        reservedKeys.add(PRODUCT);
        reservedKeys.add("products");
        reservedKeys.add("attribution");
        reservedKeys.add(ACCOUNT);
        reservedKeys.add("publisher");
        reservedKeys.add(CURRENCY);
        reservedKeys.add(JSON_PROTOCOL_VERSION);
        reservedKeys.add("debug");
        reservedKeys.add(SITE_TYPE);
        reservedKeys.add("data");
        reservedKeys.add(EVENTS);
        reservedKeys.add("tv");
        reservedKeys.add("transaction_value");
        reservedKeys.add("dc");
        reservedKeys.add("vb");
        reservedKeys.add("exd");
        reservedKeys.add("dis");
        reservedKeys.add("vh");
        reservedKeys.add("al");
        reservedKeys.add("vl");
        reservedKeys.add("vp");
        reservedKeys.add("vs");
        reservedKeys.add("vc");
        reservedKeys.add("cl");
        reservedKeys.add("ce");
        reservedKeys.add(BASKET_VIEW);
        reservedKeys.add(DATA_EVENT);
        reservedKeys.add("callDising");
        reservedKeys.add(HOME_VIEW);
        reservedKeys.add(APP_LAUNCH);
        reservedKeys.add(PRODUCT_VIEW);
        reservedKeys.add(PRODUCT_LIST_VIEW);
        reservedKeys.add("viewItem");
        reservedKeys.add("viewSearch");
        reservedKeys.add(TRANSACTION_CONFIRMATION);
        reservedKeys.add("setLogin");
        reservedKeys.add("setHashedLogin");
        reservedKeys.add("setEmail");
        reservedKeys.add("setHashedEmail");
        reservedKeys.add("i");
        reservedKeys.add("pr");
        reservedKeys.add("p");
        reservedKeys.add("q");
        reservedKeys.add(PRICE);
        reservedKeys.add(QUANTITY);
        reservedKeys.add("ac");
        reservedKeys.add("channel");
        reservedKeys.add(VALUE);
        reservedKeys.add("i");
        reservedKeys.add("m");
        reservedKeys.add("h");
        reservedKeys.add("login");
        reservedKeys.add(HASH);
        reservedKeys.add("hash_method");
        reservedKeys.add("d");
        reservedKeys.add("aios");
        reservedKeys.add("m");
        reservedKeys.add("t");
        reservedKeys.add("an");
        reservedKeys.add(APP_NAME);
        reservedKeys.add(COUNTRY);
        reservedKeys.add("cn");
        reservedKeys.add("ln");
        reservedKeys.add("ci");
        reservedKeys.add(CUSTOMER_ID);
        reservedKeys.add("dd");
        reservedKeys.add(DEDUPLICATION);
        reservedKeys.add("dr");
        reservedKeys.add("nc");
        reservedKeys.add(NEW_CUSTOMER);
        reservedKeys.add("pt2");
        reservedKeys.add("ref");
        reservedKeys.add("si");
        reservedKeys.add(USER_SEGMENT);
        reservedKeys.add("vurl");
        reservedKeys.add(PLATFORM);
        reservedKeys.add(OS_NAME);
        reservedKeys.add(OS_VERSION);
        reservedKeys.add(DEVICE_MODEL);
        reservedKeys.add(DEVICE_MANUFACTURER);
        reservedKeys.add(APP_INFO);
        reservedKeys.add(APP_ID);
        reservedKeys.add(APP_VERSION);
        reservedKeys.add(SDK_VERSION);
        reservedKeys.add(APP_LANGUAGE);
        reservedKeys.add(APP_COUNTRY);
        reservedKeys.add(TYPE);
        reservedKeys.add(APP_ID);
        reservedKeys.add(TIMESTAMP);
    }

    /**
     * Returns whether a key is reserved or not.
     */
    public static boolean isKeyReserved(String key) {
        return EventKeys.reservedKeys.contains(key);
    }

    private EventKeys() {}
}
