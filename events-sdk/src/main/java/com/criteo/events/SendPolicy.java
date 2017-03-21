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

import java.util.Date;

class SendPolicy {
    static final long DEFAULT_MAX_QUEUE_SIZE = 15;
    static final long DEFAULT_VALIDITY_DURATION = 3600000;
    static final long DEFAULT_MAX_ATTEMPTS = 3;
    static final long DEFAULT_MAX_REDIRECT_ATTEMPTS = 3;

    private final long maxQueueSize;
    private final long validityDuration;
    private final long maxAttempts;
    private final long maxRedirectAttempts;

    public SendPolicy() {
        this(DEFAULT_MAX_QUEUE_SIZE, DEFAULT_VALIDITY_DURATION, DEFAULT_MAX_ATTEMPTS, DEFAULT_MAX_REDIRECT_ATTEMPTS);
    }

    public SendPolicy(long maxQueueSize, long validityDuration, long maxAttempts, long maxRedirectAttempts) {
        this.maxQueueSize = maxQueueSize;
        this.validityDuration = validityDuration;
        this.maxAttempts = maxAttempts;
        this.maxRedirectAttempts = maxRedirectAttempts;
    }

    public boolean isQueueFull(int queueSize) {
        return queueSize >= maxQueueSize;
    }

    public boolean isEventExpired(long eventTimestamp) {
        boolean expired = false;
        if(new Date().getTime() - eventTimestamp > validityDuration) {
            expired = true;
        }
        return expired;
    }

    public boolean canRetry(int attempts) {
        return attempts < maxAttempts;
    }

    public boolean canRetryRedirect(int attempts) {
        return attempts < maxRedirectAttempts;
    }
}
