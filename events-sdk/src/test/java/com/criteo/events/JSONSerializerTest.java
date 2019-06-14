package com.criteo.events;

import com.criteo.events.product.BasketProduct;
import com.criteo.events.product.Product;

import junit.framework.Assert;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Calendar;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.Locale;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class JSONSerializerTest {

    EventService eventService;
    JSONSerializer jsonSerializer;

    @Before
    public void setUp() {
        Locale.setDefault(Locale.US);
        eventService = new EventService(RuntimeEnvironment.application);
        DeviceInfo.setIdentifiers("", false);
        jsonSerializer = new JSONSerializer();
    }

    @Test
    public void testAppLaunchEvent() throws Exception {
        JSONObject testCase = JsonReader.readJson("appLaunchEvent.json");
        AppLaunchEvent appLaunchEvent = new AppLaunchEvent();
        appLaunchEvent.setGoogleReferrer("googleReferrerTest");

        JSONObject output = jsonSerializer.serializeToJSON(appLaunchEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testBasketViewEvent() throws Exception {
        JSONObject testCase = JsonReader.readJson("basketViewEvent.json");
        BasketViewEvent basketViewEvent = new BasketViewEvent(new BasketProduct("ID123", 5, 10));
        basketViewEvent.setCurrency(Currency.getInstance("GBP"));

        JSONObject output = jsonSerializer.serializeToJSON(basketViewEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testHomeViewEvent() throws Exception {
        JSONObject testCase = JsonReader.readJson("homeViewEvent.json");
        HomeViewEvent homeViewEvent = new HomeViewEvent();

        JSONObject output = jsonSerializer.serializeToJSON(homeViewEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testProductListViewEvent() throws Exception {
        JSONObject testCase = JsonReader.readJson("productListViewEvent.json");
        ProductListViewEvent productListViewEvent = new ProductListViewEvent(new Product("ID123", 10), new Product("ID456", 5));

        JSONObject output = jsonSerializer.serializeToJSON(productListViewEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testProductViewEvent() throws Exception {
        JSONObject testCase = JsonReader.readJson("productViewEvent.json");
        ProductViewEvent productViewEvent = new ProductViewEvent(new Product("ID123", 10));

        JSONObject output = jsonSerializer.serializeToJSON(productViewEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testTransactionConfirmationEvent() throws Exception {
        JSONObject testCase = JsonReader.readJson("transactionConfirmationEvent.json");
        TransactionConfirmationEvent transactionConfirmationEvent = new TransactionConfirmationEvent("TransactionID123", new BasketProduct("ID123", 1, 2), new BasketProduct("ID456", 3, 4));

        JSONObject output = jsonSerializer.serializeToJSON(transactionConfirmationEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testTransactionConfirmationEventDeduplication() throws Exception {
        JSONObject testCase = JsonReader.readJson("transactionConfirmationEventDeduplication.json");
        TransactionConfirmationEvent transactionConfirmationEvent = new TransactionConfirmationEvent("TransactionID123", new BasketProduct("ID123", 1, 2), new BasketProduct("ID456", 3, 4));
        transactionConfirmationEvent.setDeduplication(true);

        JSONObject output = jsonSerializer.serializeToJSON(transactionConfirmationEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testTransactionConfirmationEventDeduplicationFalse() throws Exception {
        JSONObject testCase = JsonReader.readJson("transactionConfirmationEventDeduplicationFalse.json");
        TransactionConfirmationEvent transactionConfirmationEvent = new TransactionConfirmationEvent("TransactionID123", new BasketProduct("ID123", 1, 2), new BasketProduct("ID456", 3, 4));
        transactionConfirmationEvent.setDeduplication(false);

        JSONObject output = jsonSerializer.serializeToJSON(transactionConfirmationEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testTransactionConfirmationEventNewCustomer() throws Exception {
        JSONObject testCase = JsonReader.readJson("transactionConfirmationEventNewCustomer.json");
        TransactionConfirmationEvent transactionConfirmationEvent = new TransactionConfirmationEvent("TransactionID123", new BasketProduct("ID123", 1, 2), new BasketProduct("ID456", 3, 4));
        transactionConfirmationEvent.setNewCustomer(true);

        JSONObject output = jsonSerializer.serializeToJSON(transactionConfirmationEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testTransactionConfirmationEventNewCustomerFalse() throws Exception {
        JSONObject testCase = JsonReader.readJson("transactionConfirmationEventNewCustomerFalse.json");
        TransactionConfirmationEvent transactionConfirmationEvent = new TransactionConfirmationEvent("TransactionID123", new BasketProduct("ID123", 1, 2), new BasketProduct("ID456", 3, 4));
        transactionConfirmationEvent.setNewCustomer(false);

        JSONObject output = jsonSerializer.serializeToJSON(transactionConfirmationEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testDataEvent() throws Exception {
        JSONObject testCase = JsonReader.readJson("dataEvent.json");
        DataEvent dataEvent = new DataEvent();

        JSONObject output = jsonSerializer.serializeToJSON(dataEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testDeeplinkEvent() throws Exception {
        JSONObject testCase = JsonReader.readJson("deeplinkEvent.json");
        DeeplinkEvent deeplinkEvent = new DeeplinkEvent("deeplink-me://here-it-is?a=foo&b=bar");

        JSONObject output = jsonSerializer.serializeToJSON(deeplinkEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testDeeplinkEventWithAccessToken() throws Exception {
        JSONObject testCase = JsonReader.readJson("deeplinkEventAccessToken.json");
        DeeplinkEvent deeplinkEvent = new DeeplinkEvent("deeplink-me://here-it-is?a=foo&b=bar&access_token=EAAZDGZIUD153Z");

        JSONObject output = jsonSerializer.serializeToJSON(deeplinkEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testExtraData() throws Exception {
        JSONObject testCase = JsonReader.readJson("extraData.json");
        DataEvent dataEvent = new DataEvent();
        GregorianCalendar calendar = new GregorianCalendar(2015, Calendar.JANUARY, 1);

        dataEvent.addExtraData("testFloat", 0.1f);
        dataEvent.addExtraData("testString", "testing");
        dataEvent.addExtraData("testInt", 100);
        dataEvent.addExtraData("testDate", calendar);

        JSONObject output = jsonSerializer.serializeToJSON(dataEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));

        //Float has to be input manually as JSONObject by default reads in real numbers as doubles
        testCase.put("testFloat", new JSONObject().put("value", 0.1f).put("type", "float"));

        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testDates() throws Exception {
        JSONObject testCase = JsonReader.readJson("dates.json");
        DataEvent dataEvent = new DataEvent();
        GregorianCalendar startDate = new GregorianCalendar(2015, Calendar.JANUARY, 1);
        GregorianCalendar endDate = new GregorianCalendar(2015, Calendar.JANUARY, 8);

        dataEvent.setStartDate(startDate);
        dataEvent.setEndDate(endDate);

        JSONObject output = jsonSerializer.serializeToJSON(dataEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testEmail() throws Exception {
        eventService.setEmail("foobar@criteo.com");

        JSONObject testCase = JsonReader.readJson("email.json");
        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testEmailUnicode() throws Exception {
        eventService.setEmail("fôôbàr@criteo.com");

        JSONObject testCase = JsonReader.readJson("defaultCommonPayload.json");
        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testEmailValidation() throws Exception {
        eventService.setEmail("criteo123@criteo");

        JSONObject testCase = JsonReader.readJson("defaultCommonPayload.json");
        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testEmailDefaultNull() throws Exception {
        eventService.setEmail(null);

        JSONObject testCase = JsonReader.readJson("defaultCommonPayload.json");
        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testEmailCleartext() throws Exception {
        eventService.setEmail("foobar@criteo.com", EventService.EmailType.CLEARTEXT);

        JSONObject testCase = JsonReader.readJson("email.json");
        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testEmailCleartextNull() throws Exception {
        eventService.setEmail(null, EventService.EmailType.CLEARTEXT);

        JSONObject testCase = JsonReader.readJson("defaultCommonPayload.json");
        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testEmailHashed() throws Exception {
        eventService.setEmail("2abe7bdc1feba5d99ffc8bde4250e604", EventService.EmailType.HASHED_MD5);

        JSONObject testCase = JsonReader.readJson("email.json");
        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testEmailHashedNull() throws Exception {
        eventService.setEmail(null, EventService.EmailType.HASHED_MD5);

        JSONObject testCase = JsonReader.readJson("defaultCommonPayload.json");
        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testCustomerID() throws Exception {
        eventService.setCustomerId("customer123");

        JSONObject testCase = JsonReader.readJson("customerID.json");
        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testCountryLanguage() throws Exception {
        eventService.setCountry("FR");
        eventService.setLanguage("fr");

        JSONObject testCase = JsonReader.readJson("countryLanguage.json");
        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testAccountName() throws Exception {
        eventService.setAccountName("org.example.custom_account");

        JSONObject testCase = JsonReader.readJson("accountName.json");
        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testPriceSerialisation() throws Exception {
        double price = 999.85;
        ProductViewEvent productViewEvent = new ProductViewEvent(new Product("ID123", price));

        JSONObject output = jsonSerializer.serializeToJSON(productViewEvent);
        Assert.assertEquals(price, output.getJSONObject("product").getDouble("price"));
    }

    @Test
    public void testFirstLaunch() throws Exception {
        //First AppLaunchEvent should have firstLaunch set as true
        AppLaunchEvent appLaunchEvent = new AppLaunchEvent();
        Assert.assertEquals(true, appLaunchEvent.isFirstLaunch());

        //Any subsequent AppLaunchEvent should have firstLaunch set to false
        AppLaunchEvent appLaunchEvent2 = new AppLaunchEvent();
        Assert.assertEquals(false, appLaunchEvent2.isFirstLaunch());
    }

    @Test
    public void testLATEnabled() throws Exception {
        DeviceInfo.setIdentifiers("", true);

        JSONObject testCase = JsonReader.readJson("latCheck.json");
        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testUserSegmentation() throws Exception {
        JSONObject testCase = JsonReader.readJson("userSegmentation.json");
        ProductViewEvent productViewEvent = new ProductViewEvent(new Product("ID123", 10));
        productViewEvent.setUserSegment(42);

        JSONObject output = jsonSerializer.serializeToJSON(productViewEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testSetPayloadEvent() throws Exception {
        JSONObject testCase = JsonReader.readJson("setPayloadEvent.json");
        HomeViewEvent homeViewEvent = new HomeViewEvent();
        JSONObject serializedEvent = jsonSerializer.serializeToJSON(homeViewEvent);

        JSONObject output = JSONSerializer.commonPayload(eventService);
        JSONSerializer.setPayloadEvent(output, serializedEvent);

        JSONObject testCaseEvent = testCase.getJSONArray(EventKeys.EVENTS).getJSONObject(0);
        testCaseEvent.put(EventKeys.TIMESTAMP, output.getJSONArray(EventKeys.EVENTS).getJSONObject(0).getString(EventKeys.TIMESTAMP));

        JSONAssert.assertEquals(testCase, output, true);
    }

    @Test
    public void testTravelExtraData() throws Exception {
        JSONObject testCase = JsonReader.readJson("travelExtraData.json");
        ProductViewEvent productViewEvent = new ProductViewEvent(new Product("ID123", 10));
        productViewEvent.addExtraData("nbra", 2).addExtraData("nbrc", 1).addExtraData("nbrr", 1);

        JSONObject output = jsonSerializer.serializeToJSON(productViewEvent);
        testCase.put(EventKeys.TIMESTAMP, output.get(EventKeys.TIMESTAMP));
        JSONAssert.assertEquals(testCase, output, true);
    }
}