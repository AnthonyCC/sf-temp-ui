package com.freshdirect.mobileapi.controller.data;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressSelection;

public class DeliveryAddressSelectionRequestTest extends MessageTest {

    public static final Logger logger = Logger.getLogger(DeliveryAddressSelectionRequestTest.class);

    public void testParser() throws Exception {
        try {
            //String data = getFileContentAsString("Login.xml");
            ObjectMapper mapper = new ObjectMapper();
            DeliveryAddressSelection request = mapper.readValue(getFileContentAsStream("DeliveryAddressSelection.json"),
                    DeliveryAddressSelection.class);
            assertEquals("2150625068", request.getId());
            assertEquals("RESIDENTIAL", request.getType());
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            e.printStackTrace();
        }
    }

}
