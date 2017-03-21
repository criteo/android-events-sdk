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

import java.util.GregorianCalendar;

class ExtraData {

    enum ExtraDataType {
        Float,
        Date,
        Int,
        String
    }

    private ExtraDataType valueType;

    private float floatValue;
    private int intValue;
    private GregorianCalendar dateValue;
    private String stringValue;

    public ExtraData(float value) {
        valueType = ExtraDataType.Float;
        floatValue = value;
    }

    public ExtraData(int value) {
        valueType = ExtraDataType.Int;
        intValue = value;
    }

    public ExtraData(GregorianCalendar value) {
        valueType = ExtraDataType.Date;
        dateValue = value;
    }

    public ExtraData(String value) {
        valueType = ExtraDataType.String;
        stringValue = value;
    }

    ExtraDataType getType() {
        return valueType;
    }

    Object getValue() {
        switch (valueType) {
            case Float:
                return getFloatValue();

            case Int:
                return getIntValue();

            case Date:
                return getDateValue();

            case String:
                return getStringValue();

            default:
                return null;
        }
    }

    public float getFloatValue() {
        if (valueType != ExtraDataType.Float ) {
            String error = String.format("Attempt to retrieve float value from %s ExtraData instance", valueType);
            CRTOLog.e(error);
            return 0;
        }

        return floatValue;
    }

    public int getIntValue() {
        if (valueType != ExtraDataType.Int ) {
            String error = String.format("Attempt to retrieve int value from %s ExtraData instance", valueType);
            CRTOLog.e(error);
            return 0;
        }

        return intValue;
    }

    public GregorianCalendar getDateValue() {
        if (valueType != ExtraDataType.Date ) {
            String error = String.format("Attempt to retrieve Date value from %s ExtraData instance", valueType);
            CRTOLog.e(error);
            return null;
        }

        return dateValue;
    }

    public String getStringValue() {
        if (valueType != ExtraDataType.String) {
            String error = String.format("Attempt to retrieve String value from %s ExtraData instance", valueType);
            CRTOLog.e(error);
            return null;
        }

        return stringValue;
    }
}
