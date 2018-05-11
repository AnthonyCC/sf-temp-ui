package com.freshdirect.mobileapi.controller.data;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.mobileapi.controller.data.request.Login;

public class LoginRequestTest extends MessageTest {

    @Test
    public void testParser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Login request = mapper.readValue(getFileContentAsStream("Login.json"), Login.class);
        Assert.assertEquals("user", request.getUsername());
        Assert.assertEquals("password", request.getPassword());
    }

}
