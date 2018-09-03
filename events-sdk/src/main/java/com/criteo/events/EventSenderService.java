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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;

import java.util.Deque;

/**
 * Service dedicated to sending events one at a time to the server.
 */
public class EventSenderService extends IntentService {
    static final String ACTION_SEND_EVENT = "com.criteo.event.intent.action.SEND_EVENT";

    @VisibleForTesting
    static EventPosterFactory eventPosterFactory = new EventPosterFactory();

    protected final Deque<Intent> eventQueue = EventQueue.INSTANCE.get();

    public EventSenderService() {
        super("EventSenderService");
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent eventIntent) {
        if (eventIntent != null) {
            eventQueue.add(eventIntent);
        }
        postWaitingIntents();
    }

    private void postWaitingIntents() {
        boolean stop = false;
        while (!stop && !eventQueue.isEmpty()) {
            Intent eventIntent = eventQueue.remove();
            stop = !postIntent(eventIntent);
        }
    }

    /**
     * Posts a single event, passed as an intent.
     * @param eventIntent the intent containing the event to send
     * @return true if we can continue sending, false if we must stop
     */
    private boolean postIntent(Intent eventIntent) {
        // make sure we were called with the correct intent action
        String action = eventIntent.getAction();
        if (action == null || !action.equals(ACTION_SEND_EVENT)) {
            return true;
        }

        EventPoster eventPoster = eventPosterFactory.createEventPoster();
        final SendPolicy sendPolicy = eventPoster.getSendPolicy();

        // do not send events that have been retried too much
        int eventTries = eventIntent.getIntExtra("retries", 0);
        if (!sendPolicy.canRetry(eventTries)) {
            return true;
        }

        // do not send events until queue size reaches allowed size
        // If the queue is bigger than the maximum allowed value, this method
        // will be run with the oldest event, which will then be dropped.
        int size = eventQueue.size();
        if (sendPolicy.isQueueFull(size)) {
            return true;
        }

        String eventPayload = eventIntent.getStringExtra("payload");

        // discard invalid payloads
        if (eventPayload == null || eventPayload.isEmpty()) {
            return true;
        }

        long eventTimestamp = eventIntent.getLongExtra("timestamp", 0);

        for (int tries = 0; sendPolicy.canRetryRedirect(tries); tries++) {
            // do not try to send expired events
            if (sendPolicy.isEventExpired(eventTimestamp)) {
                return true;
            }

            EventPoster.PostResult result = eventPoster.post(eventPayload);
            switch (result) {
                // DONE means that the operation either succeeded, or failed in a non-recoverable
                // way, so we drop the event
                case DONE:
                    return true;
                // RETRY_LATER means that the operation failed but probably due to conditions that can
                // change at a later point, so we add the event back at the head of the queue
                case RETRY_LATER:
                    eventIntent.putExtra("retries", eventIntent.getIntExtra("retries", 0) + 1);
                    eventQueue.offerFirst(eventIntent);
                    return false;
                // REDIRECT means that the request got a 3xx result not handled by the normal
                // redirect handler in HttpUrlConnection, so we retry immediately, but only
                // a limited amount of times
                case REDIRECT:
                    break;
            }
        }
        return true;
    }

    static void sendEvent(String eventPayload, long eventTimestamp, Context context) {
        Intent intent = new Intent(context, EventSenderService.class);
        intent.setAction(ACTION_SEND_EVENT);
        intent.putExtra("payload", eventPayload);
        intent.putExtra("timestamp", eventTimestamp);
        intent.putExtra("retries", 0);

        try {
            context.startService(intent);
        }
        catch (IllegalStateException e) {
            CRTOLog.e("Unable to start service", e);
        }
        catch (SecurityException e) {
            CRTOLog.e("Permission refused or service not found", e);
        }
    }
}
