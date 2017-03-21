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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A TransactionConfirmationEvent is to be used when a user has confirmed and completed an in app transaction.
 * It is composed of a list of {@link BasketProduct}s and a unique transaction ID.
 * The currency is automatically set to the default currency associated with your Criteo account, but it can be overriden.
 * An optional boolean for indicating if the customer is a new customer can be set.
 * @since v1.0
 */
public class TransactionConfirmationEvent extends Event {

    private CopyOnWriteArrayList<BasketProduct> basketProductList = new CopyOnWriteArrayList<>();
    private AtomicReference<String> transactionId = new AtomicReference<>();
    private AtomicReference<Currency> currency = new AtomicReference<>();
    private AtomicBoolean newCustomer = new AtomicBoolean();

    /**
     * Contruct a TransactionConfirmationEvent with a transaction Id and an iterable of BasketProduct
     * @param transactionId String of the transaction Id
     * @param basketProducts Iterable of {@link BasketProduct} objects
     */
    public TransactionConfirmationEvent(String transactionId, Iterable<BasketProduct> basketProducts) {
        setTransactionId(transactionId);
        setBasketProductList(basketProducts);
    }

    /**
     * Contruct a TransactionConfirmationEvent with a transaction Id and a variable argument parameter of BasketProduct
     * @param transactionId String of the transaction Id
     * @param basketProducts Vararg of {@link BasketProduct} objects
     */
    public TransactionConfirmationEvent(String transactionId, BasketProduct... basketProducts) {
        setTransactionId(transactionId);
        setBasketProductList(new ArrayList<>(Arrays.asList(basketProducts)));
    }

    /**
     * Contruct a TransactionConfirmationEvent with a transaction Id and an iterable of BasketProduct, a start date and end date
     * @param transactionId String of the transaction Id
     * @param basketProducts Iterable of {@link BasketProduct} objects
     * @param startDate The optional start date associated with the transaction
     * @param endDate The optional end date associated with the transaction
     */
    public TransactionConfirmationEvent(String transactionId, Iterable<BasketProduct> basketProducts, GregorianCalendar startDate, GregorianCalendar endDate) {
        setTransactionId(transactionId);
        setBasketProductList(basketProducts);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    /**
     * Construct a new TransactionConfirmationEvent by copying another TransactionConfirmationEvent
     * @param event Event to copy
     */
    public TransactionConfirmationEvent(TransactionConfirmationEvent event) {
        super(event);
        setTransactionId(event.transactionId.get());
        setBasketProductList(event.basketProductList);
        setCurrency(event.currency.get());
    }

    /**
     * Add a new {@link BasketProduct} to the event's basket product list.
     * @param basketProduct the basket product to add
     */
    public void addBasketProduct(BasketProduct basketProduct) {
        if (basketProduct == null) {
            CRTOLog.e("Argument basketProduct must not be null");
            return;
        }
        basketProductList.add(basketProduct);
    }

    public CopyOnWriteArrayList<BasketProduct> getBasketProductList() {
        return basketProductList;
    }

    /**
     * This overrides the default currency for this event.
     * The default is automatically calculated based on the device locale and language.
     * Only use if it's necessary to override the default.
     * @param currency Currency of products
     */
    public void setCurrency(Currency currency) {
        if (currency == null) {
            CRTOLog.e("Argument currency must not be null");
            return;
        }
        this.currency.set(currency);
    }

    public Currency getCurrency() {
        return this.currency.get();
    }

    /**
     * Sets an optional boolean indicating whether the transaction is attributed to Criteo. The default value is false.
     * This property will not be sent to Criteo if you do not explicitly set it.
     * @param deduplication the deduplication flag
     * @since v1.1
     */
    public void setDeduplication(boolean deduplication) {
        extraDataMap.put(EventKeys.DEDUPLICATION, new ExtraData(deduplication ? 1 : 0));
    }

    /**
     * Returns the current value for the deduplication flag.
     * If the flag hasn't been set, it returns false.
     * @return the value of the deduplication flag, false if it hasn't been set
     * @since v1.1
     */
    public boolean getDeduplication() {
        ExtraData extraData = extraDataMap.get(EventKeys.DEDUPLICATION);
        return extraData != null ? extraData.getIntValue() != 0 : false;
    }

    /**
     * Sets an optional boolean indicating whether or not this is the first sale recorded for
     * the user associated with this transaction event. The default value is false.
     * This property will not be sent to Criteo if you do not explicitly set it.
     * @param newCustomer the newCustomer flag
     * @since v1.1
     */
    public void setNewCustomer(boolean newCustomer) {
        extraDataMap.put(EventKeys.NEW_CUSTOMER, new ExtraData(newCustomer ? 1 : 0));
    }

    /**
     * Returns the current value for the newCustomer flag.
     * If the flag hasn't been set, it returns false.
     * @return the value of the newCustomer flag, false if it hasn't been set
     * @since v1.1
     */
    public boolean getNewCustomer() {
        ExtraData extraData = extraDataMap.get(EventKeys.NEW_CUSTOMER);
        return extraData != null ? extraData.getIntValue() != 0 : false;
    }

    /**
     * Sets a new transaction id for this event. Only one id is associated per event so this overwrites the current id.
     * @param transactionId New transaction ID string. Must not be null.
     */
    public void setTransactionId(String transactionId) {
        if (transactionId == null) {
            CRTOLog.e("Argument transactionId must not be null");
            return;
        }
        this.transactionId.set(transactionId);
    }

    public String getTransactionId() {
        return transactionId.get();
    }

    private CopyOnWriteArrayList<BasketProduct> makeBasketProducts(Iterable<BasketProduct> basketProductIterable){
        CopyOnWriteArrayList<BasketProduct> basketProducts = new CopyOnWriteArrayList<>();

        for(BasketProduct product: basketProductIterable) {
            basketProducts.add(new BasketProduct(product.getProductId(), product.getPrice(), product.getQuantity()));
        }

        return basketProducts;
    }

    /**
     * Sets a new list of {@link BasketProduct}. Only one list is associated per event so this overwrites the current list.
     * @param basketProductList Iterable of {@link BasketProduct}. Must not be null.
     */
    public void setBasketProductList(Iterable<BasketProduct> basketProductList) {
        if (basketProductList == null) {
            CRTOLog.e("Argument basketProductList must not be null");
            return;
        }
        this.basketProductList = makeBasketProducts(basketProductList);
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
