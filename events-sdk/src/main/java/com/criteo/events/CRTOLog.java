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

import android.util.Log;

class CRTOLog {
    private static final String PREFIX = "[Criteo]";

    static void d(String message) {
        Log.d(PREFIX, message);
    }

    static void d(String message, Throwable e) {
        Log.d(PREFIX, message, e);
    }

    static void i(String message) {
        Log.i(PREFIX, message);
    }

    static void i(String message, Throwable e) {
        Log.i(PREFIX, message, e);
    }

    static void w(String message) {
        Log.w(PREFIX, message);
    }

    static void w(String message, Throwable e) {
        Log.w(PREFIX, message, e);
    }

    static void e(String message) {
        Log.e(PREFIX, message);
    }

    static void e(String message, Throwable e) {
        Log.e(PREFIX, message, e);
    }
}
