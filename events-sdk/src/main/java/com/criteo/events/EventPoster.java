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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class EventPoster {
    private URL url;
    private final SendPolicy sendPolicy;

    enum PostResult {
        DONE,
        RETRY_LATER,
        REDIRECT
    }

    public EventPoster(URL url, SendPolicy sendPolicy) {
        this.url = url;
        this.sendPolicy = sendPolicy;
    }

    public EventPoster() {
        this.url = buildEventEndpoint();
        this.sendPolicy = new SendPolicy();
    }

    public URL getUrl() {
        return url;
    }

    public SendPolicy getSendPolicy() {
        return sendPolicy;
    }

    public PostResult post(String payload) {
        boolean retry = false;
        StringBuilder response = new StringBuilder();
        int responseCode = -1;
        try {
            // Open connection
            HttpURLConnection urlConnection = createHTTPConnection(url, payload.length());

            // Send event
            sendPayload(urlConnection, payload);

            // Read initial response
            responseCode = urlConnection.getResponseCode();

            // Check if its a redirect
            if (responseCode != HttpURLConnection.HTTP_OK) {
                if (responseCode == 307) {
                    url = new URL(urlConnection.getHeaderField("Location"));
                    return PostResult.REDIRECT;
                }
            }

            InputStream is;
            try {
                // If response = 200
                is = urlConnection.getInputStream();
            } catch (IOException e) {
                // If response is an error code
                is = urlConnection.getErrorStream();
            }

            // Read body of response
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
            } catch (IOException e) {
                CRTOLog.e("Error reading server response in background thread", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (IOException ignored) {

                    }
                }
            }
        } catch (IOException e) {
            CRTOLog.e("Network error while posting event in background thread", e);
            retry = true;
        } catch (SecurityException e) {
            // "Permission denied (missing INTERNET permission?)" sometimes raised by sendPayload
            CRTOLog.e("Security error while sending payload in background thread", e);
        } catch (Exception e) {
            CRTOLog.e("Exception while sending payload in background thread", e);
        }

        // Log if there was an error
        String responseBody = response.toString();
        if (!responseBody.isEmpty() && responseBody.contains("errors")) {
            CRTOLog.w("Response code : " + responseCode + " Body: " + responseBody);
        }

        return retry ? PostResult.RETRY_LATER : PostResult.DONE;
    }

    private static URL buildEventEndpoint() {
        try {
            return new URL(String.format("%s/m/event", resolveHost()));
        }
        catch (Exception e) {
            CRTOLog.e("Error creating Criteo url", e);
            return null;
        }
    }

    private static String resolveHost() {
        String result = "https://widget.criteo.com";
        String environmentOverride = System.getenv("CRITEO_WIDGET_BASEURL");
        if (environmentOverride != null) {
            result = environmentOverride + ":8050";
        }
        return result;
    }

    private HttpURLConnection createHTTPConnection(URL url, int length) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(60000);
        urlConnection.setConnectTimeout(60000);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setInstanceFollowRedirects(true);
        urlConnection.setFixedLengthStreamingMode(length);
        return urlConnection;
    }

    private void sendPayload(HttpURLConnection urlConnection, String payload) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(payload);
            writer.flush();
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
