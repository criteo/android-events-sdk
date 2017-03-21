package com.criteo.events;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class EmailHasherTest {

    @Parameterized.Parameters(name = "testComputeMd5Hash({0})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null,                                  null},
                {"",                                    "d41d8cd98f00b204e9800998ecf8427e"},
                {"     ",                               "d41d8cd98f00b204e9800998ecf8427e"},
                {"foobar@criteo.com",                   "2abe7bdc1feba5d99ffc8bde4250e604"},
                {"toto@gmail.com",                      "5a3f2bbc4a48a3b65438890ecb202aba"},
                {"TOTO@GMAIL.COM",                      "5a3f2bbc4a48a3b65438890ecb202aba"},
                {"TOTO@gmail.com",                      "5a3f2bbc4a48a3b65438890ecb202aba"},
                {" \t\t TOTO@gmail.com\n\r \r\n \t",    "5a3f2bbc4a48a3b65438890ecb202aba"},
                {"utfê@gmail.com",                      "d22eb70c7f070f61484e1f0740f37d4e"},
                {"utfÊ@gmail.com",                      "d22eb70c7f070f61484e1f0740f37d4e"},
        });
    }

    private String email;
    private String expectedHash;

    public EmailHasherTest(String email, String expectedHash) {
        this.email = email;
        this.expectedHash = expectedHash;
    }

    @Test
    public void TestComputeMd5Hash() {
        String hash = EmailHasher.computeMd5Hash(email);
        Assert.assertEquals(expectedHash, hash);
    }
}
