package com.criteo.events;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class EventPosterTest {
    private URL url;
    private HttpURLConnection httpURLConnection;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;


    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[0]);
        httpURLConnection = Mockito.mock(HttpURLConnection.class);

        URLStreamHandler streamHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL url) throws IOException {
                return httpURLConnection;
            }
        };

        url = new URL("http", "example.org", 80, "/m/event", streamHandler);
    }

    @Test
    public void testPostOk() throws Exception {
        Mockito.when(httpURLConnection.getOutputStream()).thenReturn(outputStream);
        Mockito.when(httpURLConnection.getInputStream()).thenReturn(inputStream);
        Mockito.when(httpURLConnection.getResponseCode()).thenReturn(200);

        EventPoster poster = new EventPoster(url, new SendPolicy());
        String payload = "{\"event\":\"testEvent\"}";

        EventPoster.PostResult result = poster.post(payload);

        Mockito.verify(httpURLConnection, Mockito.times(1)).getResponseCode();
        Assert.assertEquals(EventPoster.PostResult.DONE, result);
        Assert.assertEquals(payload, outputStream.toString());
    }

    @Test
    public void testPostRedirect() throws Exception {
        String expectedRedirectUrl = "http://example.org/m/event";

        Mockito.when(httpURLConnection.getOutputStream()).thenReturn(outputStream);
        Mockito.when(httpURLConnection.getResponseCode()).thenReturn(307);
        Mockito.when(httpURLConnection.getHeaderField("Location")).thenReturn(expectedRedirectUrl);

        EventPoster poster = new EventPoster(url, new SendPolicy());
        String payload = "{\"event\":\"testEvent\"}";

        EventPoster.PostResult result = poster.post(payload);

        Mockito.verify(httpURLConnection, Mockito.times(1)).getResponseCode();
        Mockito.verify(httpURLConnection, Mockito.times(1)).getHeaderField("Location");
        Assert.assertEquals(EventPoster.PostResult.REDIRECT, result);
        Assert.assertEquals(payload, outputStream.toString());
        Assert.assertEquals(expectedRedirectUrl, poster.getUrl().toString());
    }

    @Test
    public void testPostIOException() throws Exception {
        Mockito.when(httpURLConnection.getOutputStream()).thenThrow(new IOException());

        EventPoster poster = new EventPoster(url, new SendPolicy());
        String payload = "{\"event\":\"testEvent\"}";

        EventPoster.PostResult result = poster.post(payload);

        Mockito.verify(httpURLConnection, Mockito.never()).getResponseCode();
        Assert.assertEquals(EventPoster.PostResult.RETRY_LATER, result);
        Assert.assertEquals("", outputStream.toString());
    }
}
