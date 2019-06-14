package com.criteo.events;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AccessTokenFilterTest {

    @Parameterized.Parameters(name = "testAccessTokenFilter({0})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {null,                                                                  null},
                {"",                                                                    ""},
                {"bestapp://deeplinks-are-cool",                                        "bestapp://deeplinks-are-cool"},
                {"bestapp://deeplinks-are-cool&access_token=EAAZDGZIUD153Z",            "bestapp://deeplinks-are-cool&access_token=__REDACTED_ACCESS_TOKEN__"},
                {"bestapp://access_token=EAAZDGZIUD153Z&deeplinks-are-cool",            "bestapp://access_token=__REDACTED_ACCESS_TOKEN__&deeplinks-are-cool"},
                {"bestapp://blabla=42&access_token=EAAZDGZIUD153Z&deeplinks-are-cool",  "bestapp://blabla=42&access_token=__REDACTED_ACCESS_TOKEN__&deeplinks-are-cool"},
                {"bestapp://deeplinks-are-cool&paccess_token=EAAZDGZIUD153Z",           "bestapp://deeplinks-are-cool&paccess_token=EAAZDGZIUD153Z"},
                {"bestapp://paccess_token=EAAZDGZIUD153Z&deeplinks-are-cool",           "bestapp://paccess_token=EAAZDGZIUD153Z&deeplinks-are-cool"},
                {"bestapp://blabla=42&paccess_token=EAAZDGZIUD153Z&deeplinks-are-cool", "bestapp://blabla=42&paccess_token=EAAZDGZIUD153Z&deeplinks-are-cool"},
                {"bestapp://access_token=EAAZDGZIUD153Z",                               "bestapp://access_token=__REDACTED_ACCESS_TOKEN__"},
                {"access_token=EAAZDGZIUD153Z",                                         "access_token=__REDACTED_ACCESS_TOKEN__"},
                {"access_token=EAAZDGZIUD153Z&access_token=EAAZDGZIUD153Z",             "access_token=__REDACTED_ACCESS_TOKEN__&access_token=__REDACTED_ACCESS_TOKEN__"},
        });
    }

    private final String uri;
    private final String expectedOutput;

    public AccessTokenFilterTest(String uri, String expectedOutput) {
        this.uri = uri;
        this.expectedOutput = expectedOutput;
    }

    @Test
    public void testAccessTokenFilter() {
        AccessTokenFilter filter = new AccessTokenFilter();
        String output = filter.filter(uri);

        Assert.assertEquals(expectedOutput, output);
    }
}
