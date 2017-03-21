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

import com.criteo.events.product.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A ProductListViewEvent is to be used when a user views a listing view of items. It is composed of a list of {@link Product}s
 * The currency is automatically set to the default currency associated with your Criteo account, but it can be overriden.
 * @since v1.0
 */
public class ProductListViewEvent extends Event{
    private CopyOnWriteArrayList<Product> productList = new CopyOnWriteArrayList<>();
    private AtomicReference<Currency> currency = new AtomicReference<>();


    /**
     * Construct a new ProductListViewEvent with an iterable of your products.
     * @param productList Iterable of {@link Product}
     */
    public ProductListViewEvent(Iterable<Product> productList) {
        setProductList(productList);
    }

    /**
     * Construct a new ProductListViewEvent with a variable argument parameter of your products.
     * @param products Vararg of {@link Product}
     */
    public ProductListViewEvent(Product... products) {
        setProductList(new ArrayList<>(Arrays.asList(products)));
    }

    /**
     * Contruct a new ProductListViewEvent with an iterable of your products, an overidden currency and a start date and end date.
     * @param productList Iterable of {@link Product}
     * @param currency Override the default currency
     * @param startDate The optional start date associated with the viewing
     * @param endDate The optional end date associated with the viewing
     */
    public ProductListViewEvent(Iterable<Product> productList, Currency currency, GregorianCalendar startDate, GregorianCalendar endDate) {
        setProductList(productList);
        setStartDate(startDate);
        setEndDate(endDate);
        setCurrency(currency);
    }

    /**
     * Construct a new ProductListViewEvent by copying another ProductListViewEvent
     * @param event Event to copy
     */
    public ProductListViewEvent(ProductListViewEvent event) {
        super(event);
        setProductList(event.productList);
        setCurrency(event.currency.get());
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

    public CopyOnWriteArrayList<Product> getProductList() {
        return productList;
    }

    private CopyOnWriteArrayList<Product> makeProducts(Iterable<Product> catalogProducts){
        CopyOnWriteArrayList<Product> catalogProductList = new CopyOnWriteArrayList<>();

        for(Product product: catalogProducts) {
            catalogProductList.add(new Product(product.getProductId(),product.getPrice()));
        }

        return catalogProductList;
    }

    /**
     * Sets a new list of {@link Product}. Only one list is associated per event so this overwrites the current list.
     * @param productList Iterable of {@link Product}. Must not be null.
     */
    public void setProductList(Iterable<Product> productList) {
        if (productList == null) {
            CRTOLog.e("Argument productList must not be null");
            return;
        }

        this.productList = makeProducts(productList);
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
