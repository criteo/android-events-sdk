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
 * The product class is an object used to describe individual products in your store.
 * Each product consists of a product id and a price.
 *
 * Note: The loss of precision associated with representing price as a floating-point value does not impact Criteo's ability to process your event data.
 * @since v1.0
 */
public class Product {

    private final String productId;
    private final double price;

    /**
     * Contructs new Product item
     * @param productId Unique id of product
     * @param price Price of one unit of product
     */
    public Product(String productId, double price) {
        this.productId = validateProductId(productId);
        this.price     = validatePrice(price);
    }

    private String validateProductId(String productId) {
        if (productId == null ) {
            Log.e("[Criteo]", "Argument productId must not be null");
            return "";
        }

        if (productId.length() == 0 ) {
            Log.e("[Criteo]", "Argument productId must be string of length greater than 0");
            return "";
        }

        return productId;
    }

    private double validatePrice(double price) {
        if (price < 0 ) {
            Log.e("[Criteo]", "Argument price must be greater than or equal to zero");
        }

        return price;
    }

    /**
     * Gets product ID of this Product
     * @return the product ID as a String
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Gets price of this Product
     * @return the price of this product
     */
    public double getPrice() {
        return price;
    }
}
