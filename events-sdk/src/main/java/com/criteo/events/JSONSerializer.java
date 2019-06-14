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

import com.criteo.events.product.BasketProduct;
import com.criteo.events.product.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

class JSONSerializer {
    private AccessTokenFilter accessTokenFilter = new AccessTokenFilter();

    JSONObject serializeToJSON(AppLaunchEvent event) {
        try {
            JSONObject serializedEvent = new JSONObject();

            serializedEvent.put(EventKeys.EVENTNAME, EventKeys.APP_LAUNCH);

            if (event.getGoogleReferrer() != null) {
                serializedEvent.put(EventKeys.GOOGLE_REFFERER, event.getGoogleReferrer());
            }
            serializedEvent.put(EventKeys.FIRST_LAUNCH, event.isFirstLaunch());
            serializedEvent.put(EventKeys.TIMESTAMP, getFormattedDate(event.getTimestamp()));
            serializedEvent = addExtraData(event, serializedEvent);

            return serializedEvent;

        } catch (JSONException e) {
            CRTOLog.e("Error in JSON serialisation", e);
            return null;
        }
    }

    JSONObject serializeToJSON(BasketViewEvent event) {
        try {
            JSONObject serializedEvent = new JSONObject();

            serializedEvent.put(EventKeys.EVENTNAME, EventKeys.BASKET_VIEW);

            if (event.getBasketProductList() != null && !event.getBasketProductList().isEmpty()) {
                JSONArray basketProduct = new JSONArray();
                for(BasketProduct item : event.getBasketProductList()) {
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put(EventKeys.ID, item.getProductId());
                    jsonItem.put(EventKeys.PRICE, item.getPrice());
                    jsonItem.put(EventKeys.QUANTITY, item.getQuantity());
                    basketProduct.put(jsonItem);
                }
                serializedEvent.put(EventKeys.PRODUCT, basketProduct);
            }
            if (event.getCurrency() != null) {
                serializedEvent.put(EventKeys.CURRENCY, event.getCurrency().getCurrencyCode());
            }

            serializedEvent.put(EventKeys.TIMESTAMP, getFormattedDate(event.getTimestamp()));
            serializedEvent = addExtraData(event, serializedEvent);

            return serializedEvent;

        } catch (JSONException e) {
            CRTOLog.e("Error in JSON serialisation", e);
            return null;
        }
    }


    JSONObject serializeToJSON(HomeViewEvent event) {
        try {
            JSONObject serializedEvent = new JSONObject();

            serializedEvent.put(EventKeys.EVENTNAME, EventKeys.HOME_VIEW);
            serializedEvent.put(EventKeys.TIMESTAMP, getFormattedDate(event.getTimestamp()));
            serializedEvent = addExtraData(event, serializedEvent);

            return serializedEvent;

        } catch (JSONException e) {
            CRTOLog.e("Error in JSON serialisation", e);
            return null;
        }
    }

    JSONObject serializeToJSON(ProductListViewEvent event) {
        try {
            JSONObject serializedEvent = new JSONObject();

            serializedEvent.put(EventKeys.EVENTNAME, EventKeys.PRODUCT_LIST_VIEW);

            if (event.getCurrency() != null) {
                serializedEvent.put(EventKeys.CURRENCY, event.getCurrency().getCurrencyCode());
            }
            if (event.getProductList() != null && !event.getProductList().isEmpty()){
                JSONArray productList = new JSONArray();

                for(Product product : event.getProductList()) {
                    JSONObject jsonProduct = new JSONObject();
                    jsonProduct.put(EventKeys.ID, product.getProductId());
                    jsonProduct.put(EventKeys.PRICE, product.getPrice());
                    productList.put(jsonProduct);
                }
                serializedEvent.put(EventKeys.PRODUCT, productList);
            }

            serializedEvent.put(EventKeys.TIMESTAMP, getFormattedDate(event.getTimestamp()));
            serializedEvent = addExtraData(event, serializedEvent);

            return serializedEvent;

        } catch (JSONException e) {
            CRTOLog.e("Error in JSON serialisation", e);
            return null;
        }
    }

    JSONObject serializeToJSON(ProductViewEvent event) {
        try {
            JSONObject serializedEvent = new JSONObject();

            serializedEvent.put(EventKeys.EVENTNAME, EventKeys.PRODUCT_VIEW);

            if (event.getCurrency() != null) {
                serializedEvent.put(EventKeys.CURRENCY, event.getCurrency().getCurrencyCode());
            }
            if (event.getProduct() != null) {
                JSONObject product  = new JSONObject();
                product.put(EventKeys.ID, event.getProduct().getProductId());
                product.put(EventKeys.PRICE, event.getProduct().getPrice());
                serializedEvent.put(EventKeys.PRODUCT,product);
            }

            serializedEvent.put(EventKeys.TIMESTAMP, getFormattedDate(event.getTimestamp()));
            serializedEvent = addExtraData(event, serializedEvent);

            return serializedEvent;

        } catch (JSONException e) {
            CRTOLog.e("Error in JSON serialisation", e);
            return null;
        }
    }

    JSONObject serializeToJSON(TransactionConfirmationEvent event) {
        try {
            JSONObject serializedEvent = new JSONObject();

            serializedEvent.put(EventKeys.EVENTNAME, EventKeys.TRANSACTION_CONFIRMATION);

            if (event.getCurrency() != null) {
                serializedEvent.put(EventKeys.CURRENCY, event.getCurrency().getCurrencyCode());
            }
            if (event.getTransactionId() != null) {
                serializedEvent.put(EventKeys.ID, event.getTransactionId());
            }
            if (event.getBasketProductList() != null && !event.getBasketProductList().isEmpty()) {
                JSONArray basketProducts = new JSONArray();
                for(BasketProduct item : event.getBasketProductList()) {
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put(EventKeys.ID, item.getProductId());
                    jsonItem.put(EventKeys.PRICE, item.getPrice());
                    jsonItem.put(EventKeys.QUANTITY, item.getQuantity());
                    basketProducts.put(jsonItem);
                }
                serializedEvent.put(EventKeys.PRODUCT, basketProducts);
            }

            serializedEvent.put(EventKeys.TIMESTAMP, getFormattedDate(event.getTimestamp()));
            serializedEvent = addExtraData(event, serializedEvent);

            return serializedEvent;

        } catch (JSONException e) {
            CRTOLog.e("Error in JSON serialisation", e);
            return null;
        }
    }

    JSONObject serializeToJSON(DataEvent event) {
        try {
            JSONObject serializedEvent = new JSONObject();

            serializedEvent.put(EventKeys.EVENTNAME, EventKeys.DATA_EVENT);
            serializedEvent.put(EventKeys.TIMESTAMP, getFormattedDate(event.getTimestamp()));
            serializedEvent = addExtraData(event, serializedEvent);

            return serializedEvent;

        } catch (JSONException e) {
            CRTOLog.e("Error in JSON serialisation", e);
            return null;
        }
    }

    JSONObject serializeToJSON(DeeplinkEvent event) {
        try {
            JSONObject serializedEvent = new JSONObject();

            serializedEvent.put(EventKeys.EVENTNAME, EventKeys.DEEPLINK_EVENT);
            serializedEvent.put(EventKeys.TIMESTAMP, getFormattedDate(event.getTimestamp()));

            String filteredDeeplinkUrl = accessTokenFilter.filter(event.getDeeplinkUrl());

            serializedEvent.put(EventKeys.DEEPLINK, filteredDeeplinkUrl);
            serializedEvent = addExtraData(event, serializedEvent);

            return serializedEvent;

        } catch (JSONException e) {
            CRTOLog.e("Error in JSON serialisation", e);
            return null;
        }
    }

    private static String getFormattedDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        df.setTimeZone(tz);
        return df.format(date);
    }

    private static String getFormattedDate(GregorianCalendar date) {
        String year = String.format("%04d", date.get(Calendar.YEAR));
        String month = String.format("%02d", date.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", date.get(Calendar.DAY_OF_MONTH));

        return String.format("%s-%s-%sT00:00:00Z", year, month, day);
    }

    private static JSONObject addExtraData(Event event, JSONObject serializedEvent) throws JSONException{
        for(Map.Entry<String, ExtraData> dataEntry : event.getExtraDataMap().entrySet()) {
            JSONObject extraData = new JSONObject();
            if(dataEntry.getValue().getType() == ExtraData.ExtraDataType.Date ){
                extraData.put(EventKeys.VALUE, getFormattedDate((GregorianCalendar) dataEntry.getValue().getValue()));
            } else {
                extraData.put(EventKeys.VALUE, dataEntry.getValue().getValue());
            }
            String type = null;
            switch (dataEntry.getValue().getType()){
                case Float:
                    type = "float";
                    break;
                case Int:
                    type = "integer";
                    break;
                case String:
                    type = "string";
                    break;
                case Date:
                    type = "date";
                    break;
            }
            extraData.put(EventKeys.TYPE, type);
            serializedEvent.put(dataEntry.getKey(), extraData);
        }
        return serializedEvent;
    }

    static JSONObject commonPayload(EventService eventService) throws JSONException {
        JSONObject payload = new JSONObject();

        JSONObject account = new JSONObject();
        account.put(EventKeys.APP_NAME, eventService.getAccountName());
        account.put(EventKeys.COUNTRY, eventService.getCountry());
        account.put(EventKeys.LANGUAGE, eventService.getLanguage());

        JSONObject id = new JSONObject();
        id.put(EventKeys.DEVICE_ID, DeviceInfo.getAdvertisingId());
        id.put(EventKeys.LAT, DeviceInfo.getLimitAdTrackingEnabled());

        JSONObject appInfo = new JSONObject();
        appInfo.put(EventKeys.APP_ID, DeviceInfo.getBundleName());
        appInfo.put(EventKeys.APP_NAME, DeviceInfo.getBundleName());
        appInfo.put(EventKeys.APP_VERSION, DeviceInfo.getAppVersion());
        appInfo.put(EventKeys.SDK_VERSION, DeviceInfo.getSDKVersion());
        appInfo.put(EventKeys.APP_LANGUAGE, DeviceInfo.getLanguage());
        appInfo.put(EventKeys.APP_COUNTRY, DeviceInfo.getCountry());

        JSONObject deviceInfo = new JSONObject();
        deviceInfo.put(EventKeys.PLATFORM, "android");
        deviceInfo.put(EventKeys.OS_NAME, "android");
        deviceInfo.put(EventKeys.OS_VERSION, DeviceInfo.getAndroidVersion());
        deviceInfo.put(EventKeys.DEVICE_MODEL, DeviceInfo.getModel());
        deviceInfo.put(EventKeys.DEVICE_MANUFACTURER, DeviceInfo.getManufacturer());

        JSONArray alternateIds = new JSONArray();
        if (eventService.getEmail() != null) {
            JSONObject email = new JSONObject();
            email.put(EventKeys.VALUE, eventService.getEmail());
            email.put(EventKeys.TYPE, "email");
            email.put(EventKeys.HASH, "md5");
            alternateIds.put(email);
        }

        payload.put(EventKeys.ACCOUNT, account);
        payload.put(EventKeys.ID, id);
        payload.put(EventKeys.DEVICE_INFO, deviceInfo);
        payload.put(EventKeys.APP_INFO, appInfo);
        payload.put(EventKeys.ALTERNATE_IDS,alternateIds);
        payload.put(EventKeys.JSON_PROTOCOL_VERSION, "sdk_1.0.0");

        if (eventService.getCustomerId() != null) {
            payload.put(EventKeys.CUSTOMER_ID, eventService.getCustomerId());
        }

        return payload;
    }

    static void setPayloadEvent(JSONObject payload, JSONObject serializedEvent) throws JSONException {
        JSONArray events = new JSONArray();
        events.put(serializedEvent);
        payload.put(EventKeys.EVENTS, events);
    }
}
