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

import android.support.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class EmailHasher {
    private final static char[] hexArray = "0123456789abcdef".toCharArray();

    @Nullable
    public static String computeMd5Hash(@Nullable String email) {
        if (email == null) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] emailBuffer = getCanonicalEmail(email).getBytes();
            byte[] hash = md5.digest(emailBuffer);
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String getCanonicalEmail(String email) {
        return email.toLowerCase().trim();
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
