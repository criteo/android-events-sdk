package com.criteo.events;

import android.content.Context;
import android.content.Intent;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Date;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "/src/main/AndroidManifest.xml", emulateSdk = 21)
public class EventSenderServiceTest {
    /**
     * This class is a wrapper around the EventSenderService class that
     * we are testing here. It makes the onHandleIntent method public
     * so that we can call it directly. This is made necessary due to
     * limitations in the Robolectric framework that is unable to properly
     * replicate Android's IntentService class lifecycle.
     */
    private class TestableEventSenderService extends EventSenderService {

        @Override
        public void onHandleIntent(Intent eventIntent) {
            super.onHandleIntent(eventIntent);
        }

        public int getQueueSize() {
            return eventQueue.size();
        }
    }

    private EventPoster eventPoster;
    private TestableEventSenderService eventSenderService;
    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;

        eventPoster = Mockito.mock(EventPoster.class);

        EventSenderService.eventPosterFactory = new EventPosterFactory() {
            @Override
            public EventPoster createEventPoster() {
                return eventPoster;
            }
        };

        // reset queue before each test
        EventQueue.INSTANCE.get().clear();

        eventSenderService = new TestableEventSenderService();
        eventSenderService.onCreate();
    }

    @After
    public void tearDown() {
        eventSenderService.onDestroy();
    }

    @Test
    public void testPayloadPosted() throws Exception {
        Mockito.when(eventPoster.post(Mockito.anyString())).thenReturn(EventPoster.PostResult.DONE);
        SendPolicy sendPolicy = new SendPolicy();
        Mockito.when(eventPoster.getSendPolicy()).thenReturn(sendPolicy);

        String testPayload = JsonReader.readRawContents("defaultCommonPayload.json");

        sendEvent(testPayload, new Date().getTime());

        Mockito.verify(eventPoster, Mockito.times(1)).post(testPayload);
    }

    @Test
    public void testSendOrder() throws Exception {
        Mockito.when(eventPoster.post(Mockito.anyString())).thenReturn(EventPoster.PostResult.DONE);
        SendPolicy sendPolicy = new SendPolicy();
        Mockito.when(eventPoster.getSendPolicy()).thenReturn(sendPolicy);

        // enqueue all events
        Intent[] intents = new Intent[7];
        intents[0] = enqueueEvent("payload_1", new Date().getTime());
        intents[1] = enqueueEvent("payload_2", new Date().getTime());
        intents[2] = enqueueEvent("payload_3", new Date().getTime());
        intents[3] = enqueueEvent("payload_4", new Date().getTime());
        intents[4] = enqueueEvent("payload_5", new Date().getTime());
        intents[5] = enqueueEvent("payload_6", new Date().getTime());
        intents[6] = enqueueEvent("payload_7", new Date().getTime());

        // consume events from oldest to most recent
        for (Intent intent: intents) {
            eventSenderService.onHandleIntent(intent);
        }

        // post should have been called 7 times
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(eventPoster, Mockito.times(7)).post(captor.capture());
        List<String> capturedPayloads = captor.getAllValues();

        // make sure that the events are sent sequentially, in FIFO order
        Assert.assertEquals("payload_1", capturedPayloads.get(0));
        Assert.assertEquals("payload_2", capturedPayloads.get(1));
        Assert.assertEquals("payload_3", capturedPayloads.get(2));
        Assert.assertEquals("payload_4", capturedPayloads.get(3));
        Assert.assertEquals("payload_5", capturedPayloads.get(4));
        Assert.assertEquals("payload_6", capturedPayloads.get(5));
        Assert.assertEquals("payload_7", capturedPayloads.get(6));

        // check that the queue is empty
        Assert.assertEquals(0, eventSenderService.getQueueSize());
    }

    @Test
    public void testNullPayload() {
        Mockito.when(eventPoster.post(Mockito.anyString())).thenReturn(EventPoster.PostResult.DONE);
        SendPolicy sendPolicy = new SendPolicy();
        Mockito.when(eventPoster.getSendPolicy()).thenReturn(sendPolicy);

        sendEvent(null, new Date().getTime());

        Mockito.verify(eventPoster, Mockito.never()).post(Mockito.anyString());
    }

    @Test
    public void testEmptyPayload() {
        Mockito.when(eventPoster.post(Mockito.anyString())).thenReturn(EventPoster.PostResult.DONE);
        SendPolicy sendPolicy = new SendPolicy();
        Mockito.when(eventPoster.getSendPolicy()).thenReturn(sendPolicy);

        sendEvent("", new Date().getTime());

        Mockito.verify(eventPoster, Mockito.never()).post(Mockito.anyString());
    }

    @Test
    public void testInvalidIntentAction() {
        Mockito.when(eventPoster.post(Mockito.anyString())).thenReturn(EventPoster.PostResult.DONE);
        SendPolicy sendPolicy = new SendPolicy();
        Mockito.when(eventPoster.getSendPolicy()).thenReturn(sendPolicy);

        Intent intent = new Intent(context, TestableEventSenderService.class);
        intent.setAction("random action");

        eventSenderService.onStartCommand(intent, 0, 42);
        eventSenderService.onHandleIntent(intent);

        Mockito.verify(eventPoster, Mockito.never()).post(Mockito.anyString());
        Assert.assertEquals(0, eventSenderService.getQueueSize());
    }

    @Test
    public void testNullIntentAction() {
        Mockito.when(eventPoster.post(Mockito.anyString())).thenReturn(EventPoster.PostResult.DONE);
        SendPolicy sendPolicy = new SendPolicy();
        Mockito.when(eventPoster.getSendPolicy()).thenReturn(sendPolicy);

        Intent intent = new Intent(context, TestableEventSenderService.class);
        intent.setAction(null);

        eventSenderService.onStartCommand(intent, 0, 42);
        eventSenderService.onHandleIntent(intent);

        Mockito.verify(eventPoster, Mockito.never()).post(Mockito.anyString());
        Assert.assertEquals(0, eventSenderService.getQueueSize());
    }

    @Test
    public void testNullIntent() {
        Mockito.when(eventPoster.post(Mockito.anyString())).thenReturn(EventPoster.PostResult.DONE);
        SendPolicy sendPolicy = new SendPolicy();
        Mockito.when(eventPoster.getSendPolicy()).thenReturn(sendPolicy);

        Intent intent = null;

        eventSenderService.onStartCommand(intent, 0, 42);
        eventSenderService.onHandleIntent(intent);

        Mockito.verify(eventPoster, Mockito.never()).post(Mockito.anyString());
        Assert.assertEquals(0, eventSenderService.getQueueSize());
    }

    @Test
    public void testExpiredEvent() throws Exception {
        Mockito.when(eventPoster.post(Mockito.anyString())).thenReturn(EventPoster.PostResult.DONE);
        SendPolicy sendPolicy = new SendPolicy(15, 1000, 3, 3); // event expires after 1 second
        Mockito.when(eventPoster.getSendPolicy()).thenReturn(sendPolicy);

        String testPayload = JsonReader.readRawContents("defaultCommonPayload.json");
        long eventTimestamp = new Date().getTime() - 3000; // create event in the past

        sendEvent(testPayload, eventTimestamp);

        Mockito.verify(eventPoster, Mockito.never()).post(testPayload);
        Assert.assertEquals(0, eventSenderService.getQueueSize());
    }

    @Test
    public void testRetry() throws Exception {
        Mockito.when(eventPoster.post(Mockito.anyString())).thenReturn(EventPoster.PostResult.RETRY_LATER);
        SendPolicy sendPolicy = new SendPolicy(15, 30000, 3, 3); // 3 attempts
        Mockito.when(eventPoster.getSendPolicy()).thenReturn(sendPolicy);

        // enqueue one event
        Intent intent = enqueueEvent("payload_1", new Date().getTime());
        eventSenderService.onHandleIntent(intent);

        // enqueue four other random intents
        for (int i = 0; i < 4; i++) {
            Intent randomIntent = enqueueEvent("random_payload", new Date().getTime());
            eventSenderService.onHandleIntent(randomIntent);
        }

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(eventPoster, Mockito.times(5)).post(captor.capture());
        List<String> capturedPayloads = captor.getAllValues();

        // we should have tried to post the first event 3 times
        Assert.assertEquals("payload_1", capturedPayloads.get(0));
        Assert.assertEquals("payload_1", capturedPayloads.get(1));
        Assert.assertEquals("payload_1", capturedPayloads.get(2));

        // then a random event twice
        Assert.assertEquals("random_payload", capturedPayloads.get(3));
        Assert.assertEquals("random_payload", capturedPayloads.get(4));

        // 4 events in the queue
        Assert.assertEquals(4, eventSenderService.getQueueSize());
    }

    @Test
    public void testFullQueue() throws Exception {
        Mockito.when(eventPoster.post(Mockito.anyString())).thenReturn(EventPoster.PostResult.RETRY_LATER);
        SendPolicy sendPolicy = new SendPolicy(5, 30000, 5000, 3); // 5 items max in the queue, many attempts
        Mockito.when(eventPoster.getSendPolicy()).thenReturn(sendPolicy);

        // enqueue all events
        Intent[] intents = new Intent[7];
        intents[0] = enqueueEvent("payload_1", new Date().getTime());
        intents[1] = enqueueEvent("payload_2", new Date().getTime());
        intents[2] = enqueueEvent("payload_3", new Date().getTime());
        intents[3] = enqueueEvent("payload_4", new Date().getTime());
        intents[4] = enqueueEvent("payload_5", new Date().getTime());
        intents[5] = enqueueEvent("payload_6", new Date().getTime());
        intents[6] = enqueueEvent("payload_7", new Date().getTime());

        // consume events from oldest to most recent
        for (Intent intent: intents) {
            eventSenderService.onHandleIntent(intent);
        }

        // post should have been called 7 times
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(eventPoster, Mockito.times(7)).post(captor.capture());
        List<String> capturedPayloads = captor.getAllValues();

        // payload_1 is retried until the queue is full
        Assert.assertEquals("payload_1", capturedPayloads.get(0));
        Assert.assertEquals("payload_1", capturedPayloads.get(1));
        Assert.assertEquals("payload_1", capturedPayloads.get(2));
        Assert.assertEquals("payload_1", capturedPayloads.get(3));
        Assert.assertEquals("payload_1", capturedPayloads.get(4));

        // queue is full so payload_1 is evicted, then payload_2 is tried
        Assert.assertEquals("payload_2", capturedPayloads.get(5));

        // queue is full again, so payload_2 is evicted, then payload_3 is tried
        Assert.assertEquals("payload_3", capturedPayloads.get(6));

        // check that the queue is full
        Assert.assertEquals(5, eventSenderService.getQueueSize());
    }

    @Test
    public void testRedirect() throws Exception {
        Mockito.when(eventPoster.post(Mockito.anyString())).thenReturn(EventPoster.PostResult.REDIRECT);
        SendPolicy sendPolicy = new SendPolicy(15, 30000, 5000, 3); // 15 items max in the queue, 3 redirect attempts
        Mockito.when(eventPoster.getSendPolicy()).thenReturn(sendPolicy);

        // enqueue one event
        Intent intent = enqueueEvent("payload_1", new Date().getTime());
        eventSenderService.onHandleIntent(intent);

        // we must have tried to follow the redirect 3 times
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(eventPoster, Mockito.times(3)).post(captor.capture());
        List<String> capturedPayloads = captor.getAllValues();

        // payload_1 is retried until it exceeds the number of allowed redirects
        Assert.assertEquals("payload_1", capturedPayloads.get(0));
        Assert.assertEquals("payload_1", capturedPayloads.get(1));
        Assert.assertEquals("payload_1", capturedPayloads.get(2));

        // 0 item in the queue
        Assert.assertEquals(0, eventSenderService.getQueueSize());
    }

    private void sendEvent(String eventPayload, long eventTimestamp) {
        Intent intent = new Intent(context, TestableEventSenderService.class);
        intent.setAction(EventSenderService.ACTION_SEND_EVENT);
        intent.putExtra("payload", eventPayload);
        intent.putExtra("timestamp", eventTimestamp);

        // IntentService.onStartCommand actually does nothing due to Robolectric limitations,
        // but we call it in order to properly increment the queue size.
        eventSenderService.onStartCommand(intent, 0, 42);

        // Manually invoke intent handling
        eventSenderService.onHandleIntent(intent);
    }

    private Intent enqueueEvent(String eventPayload, long eventTimestamp) {
        Intent intent = new Intent(context, TestableEventSenderService.class);
        intent.setAction(EventSenderService.ACTION_SEND_EVENT);
        intent.putExtra("payload", eventPayload);
        intent.putExtra("timestamp", eventTimestamp);

        // see above
        eventSenderService.onStartCommand(intent, 0, 42);

        return intent;
    }
}
