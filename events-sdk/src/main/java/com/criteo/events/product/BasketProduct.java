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
package com.criteo.events.product;

import android.util.Log;

/**
 * A BasketProduct is used to describe individual products in the {@link com.criteo.events.BasketViewEvent} and the {@link com.criteo.events.TransactionConfirmationEvent}
 * It is composed of a {@link Product} and a quantity.
 * @since v1.0
 */
public class BasketProduct {

    private final int quantity;
    private final Product product;

    /**
     * Constructs new BasketProduct from a {@link Product} and a quantity
     * @param product Product in basket or transaction
     * @param quantity Quanity of the products in basket or transaction
     */
    public BasketProduct(Product product, int quantity) {
        this.product = product;
        this.quantity  = validateQuantity(quantity);
    }

    /**
     * Constructs new BasketProduct from product components and quantity
     * @param productId Product ID for new product
     * @param price Price for new product
     * @param quantity Quanity of the product in basket or transaction
     */
    public BasketProduct(String productId, double price, int quantity) {
        product = new Product(productId, price);
        this.quantity  = validateQuantity(quantity);
    }

    private int validateQuantity(int quantity) {
        if (quantity < 1 ) {
            Log.e("[Criteo]", "Argument quantity must be greater than zero");
        }

        return quantity;
    }

    /**
     * Gets product ID of this BasketProduct
     * @return product ID of this BasketProduct
     */
    public String getProductId() {
        return product.getProductId();
    }

    /**
     * Gets price of this BasketProduct
     * @return price of this BasketProduct
     */
    public double getPrice() {
        return product.getPrice();
    }

    /**
     * Gets quantity of this BasketProduct
     * @return the quantity of this BasketProduct
     */
    public int getQuantity() {
        return quantity;
    }
}
