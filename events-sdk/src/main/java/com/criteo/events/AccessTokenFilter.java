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

import java.util.regex.Pattern;

class AccessTokenFilter {
    private static final String ACCESS_TOKEN_PATTERN = "((?<![A-Za-z])access_token=)[^&]*";
    private static final String REDACTED_ACCESS_TOKEN = "__REDACTED_ACCESS_TOKEN__";
    private static final String REPLACEMENT_STR = "$1" + REDACTED_ACCESS_TOKEN;

    private final Pattern filterPattern = Pattern.compile(ACCESS_TOKEN_PATTERN);

    String filter(String uri) {
        if (uri == null) {
            return null;
        }

        return filterPattern.matcher(uri).replaceAll(REPLACEMENT_STR);
    }
}
