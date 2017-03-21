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
import java.util.concurrent.atomic.AtomicReference;

/**
 * A BasketViewEvent is used to indicate when a user has viewed their shopping basket. It is composed of a list of {@link BasketProduct}s
 * @since v1.0
 */
public class BasketViewEvent extends Event {

    private CopyOnWriteArrayList<BasketProduct> basketProductList = new CopyOnWriteArrayList<>();
    private AtomicReference<Currency> currency = new AtomicReference<>();

    /**
     * Construct a new BasketViewEvent with an iterable of BasketProduct.
     * @param basketProducts Iterable of BasketProduct objects
     */
    public BasketViewEvent(Iterable<BasketProduct> basketProducts) {
        setBasketProductList(basketProducts);
    }

    /**
     * Construct a new BasketViewEvent a variable argument parameter of BasketProduct.
     * @param basketProducts Vararg of BasketProduct objects
     */
    public BasketViewEvent(BasketProduct... basketProducts) {
        setBasketProductList(new ArrayList<>(Arrays.asList(basketProducts)));
    }

    /**
     * Construct a new BasketViewEvent with an overriden currency, an iterable of BasketProduct and start and end dates
     * @param currency Override the default currency
     * @param basketProducts Iterable of BasketProduct objects
     * @param startDate The optional start date associated with the products in the basket
     * @param endDate The optional end date associated with the products in the basket
     */
    public BasketViewEvent(Currency currency, Iterable<BasketProduct> basketProducts, GregorianCalendar startDate, GregorianCalendar endDate) {
        setBasketProductList(basketProducts);
        setCurrency(currency);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    /**
     * Construct a new BasketViewEvent by copying another BasketViewEvent
     * @param event Event to copy
     */
    public BasketViewEvent(BasketViewEvent event) {
        super(event);
        setBasketProductList(event.basketProductList);
        setCurrency(event.currency.get());
    }


    /**
     * Add a new {@link BasketProduct} to the event's baskets product list.
     * @param basketProduct the basket product to add
     */
    public void addBasketProduct(BasketProduct basketProduct) {
        if (basketProduct == null) {
            CRTOLog.e("Argument basketProduct must not be null");
            return;
        }
        basketProductList.add(basketProduct);
    }

    private CopyOnWriteArrayList<BasketProduct> makeBasketProducts(Iterable<BasketProduct> basketProductIterable){
        CopyOnWriteArrayList<BasketProduct> basketProducts = new CopyOnWriteArrayList<>();

        for(BasketProduct product: basketProductIterable) {
            basketProducts.add(new BasketProduct(product.getProductId(), product.getPrice(), product.getQuantity()));
        }

        return basketProducts;
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

    public CopyOnWriteArrayList<BasketProduct> getBasketProductList() {
        return basketProductList;
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


