package com.freshdirect.mobileapi.controller.data;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.freshdirect.mobileapi.controller.data.request.UpdateItemInCart;

public class UpdateItemInCartRequestTest extends MessageTest {

    public static final Logger logger = Logger.getLogger(UpdateItemInCartRequestTest.class);

    public void testParser() throws Exception {
        try {
            //String data = getFileContentAsString("Login.xml");
            ObjectMapper mapper = new ObjectMapper();
            UpdateItemInCart request = mapper.readValue(getFileContentAsStream("UpdateItemInCart.json"), UpdateItemInCart.class);
            assertEquals("bstk_rbeye_bnls", request.getProductConfiguration().getProductId());
            assertEquals("bgril", request.getProductConfiguration().getCategoryId());
            assertEquals("MEA0004675", request.getProductConfiguration().getSkuCode());
            assertEquals(1.0f, request.getProductConfiguration().getQuantity());
            assertEquals("E04", request.getProductConfiguration().getSalesUnit());
            assertEquals(2, request.getProductConfiguration().getOptions().size());
            assertEquals("N", request.getProductConfiguration().getOptions().get("C_MT_BF_MAR"));
            assertEquals("VP", request.getProductConfiguration().getOptions().get("C_MT_BF_PAK"));

            assertEquals("1234567890", request.getCartLineId());

        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            e.printStackTrace();
        }
    }

}
