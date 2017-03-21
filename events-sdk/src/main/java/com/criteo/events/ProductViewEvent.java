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

import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A ProductViewEvent is to be used when a user views a single product. It is composed of a product Id and price, or a {@link Product}
 * The currency is automatically set to the default currency associated with your Criteo account, but it can be overriden.
 * @since v1.0
 */
public class ProductViewEvent extends Event {
    private AtomicReference<Product> product = new AtomicReference<>();
    private AtomicReference<Currency> currency = new AtomicReference<>();

    /**
     * Construct a new ProductViewEvent with a product Id string and price
     * @param productId String of product Id.
     * @param price Float of price
     */
    public ProductViewEvent(String productId, double price) {
        product.set(new Product(productId, price));
    }

    /**
     * Construct a new ProductViewEvent with a product Id string, a start date and end date.
     * @param product CatalogProduct
     */
    public ProductViewEvent(Product product) {
        this.product.set(product);
    }

    /**
     * Construct a new ProductViewEvent with a product Id string, a start date and end date.
     * @param product CatalogProduct
     * @param currency Override the default currency
     * @param startDate The optional start date associated with the viewing
     * @param endDate The optional end date associated with the viewing
     */
    public ProductViewEvent(Product product, Currency currency, GregorianCalendar startDate, GregorianCalendar endDate) {
        this.product.set(product);
        setStartDate(startDate);
        setEndDate(endDate);
        setCurrency(currency);
    }

    /**
     * Construct a new ProductViewEvent by copying another ProductViewEvent
     * @param event Event to copy
     */
    public ProductViewEvent(ProductViewEvent event) {
        super(event);
        setProduct(event.product.get());
        setCurrency(currency.get());
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

    public Product getProduct() {
        return product.get();
    }

    /**
     * Sets a new {@link Product} for this event. Only one product is associated per event so this overwrites the current product.
     * @param product New {@link Product} object. Must not be null.
     */
    public void setProduct(Product product) {
        if (product == null) {
            CRTOLog.e("Argument product must not be null");
            return;
        }
        this.product.set(product);
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
