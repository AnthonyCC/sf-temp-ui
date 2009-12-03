package com.freshdirect.mobileapi.controller.data;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.freshdirect.mobileapi.controller.data.request.Login;

public class LoginRequestTest extends MessageTest {
    public static final Logger logger = Logger.getLogger(LoginRequestTest.class);

    public void testParser() throws Exception {
        try {
            //String data = getFileContentAsString("Login.xml");
            ObjectMapper mapper = new ObjectMapper();
            Login request = mapper.readValue(getFileContentAsStream("Login.json"), Login.class);
            assertEquals("user", request.getUsername());
            assertEquals("password", request.getPassword());
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            e.printStackTrace();
        }
    }

}
