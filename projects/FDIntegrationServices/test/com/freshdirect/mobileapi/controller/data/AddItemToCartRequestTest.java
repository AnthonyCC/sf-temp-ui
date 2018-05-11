package com.freshdirect.mobileapi.controller.data;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.mobileapi.controller.data.request.AddItemToCart;
import com.freshdirect.mobileapi.controller.data.request.SmartStoreConfiguration;

public class AddItemToCartRequestTest extends MessageTest {

    @Test
    public void testParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AddItemToCart request = mapper.readValue(getFileContentAsStream("AddItemToCart.json"), AddItemToCart.class);
        Assert.assertEquals("bstk_rbeye_bnls", request.getProductConfiguration().getProductId());
        Assert.assertEquals("bgril", request.getProductConfiguration().getCategoryId());
        Assert.assertEquals("MEA0004676", request.getProductConfiguration().getSkuCode());
        Assert.assertEquals(2.0f, request.getProductConfiguration().getQuantity(), 0.01d);
        Assert.assertEquals("E02", request.getProductConfiguration().getSalesUnit().getName());
        Assert.assertEquals(2, request.getProductConfiguration().getOptions().size());
        Assert.assertEquals("N", request.getProductConfiguration().getOptions().get("C_MT_BF_MAR"));
        Assert.assertEquals("ST", request.getProductConfiguration().getOptions().get("C_MT_BF_PAK"));

        Assert.assertEquals("yes", request.getAgreeToTerms());
        Assert.assertEquals("74832939_p4", request.getImpressionId());
        Assert.assertEquals("rec_rs_vietnams_bf_sld", request.getRecipeId());
        Assert.assertEquals("true", request.getRequestNotification());
        Assert.assertEquals("rec_rs_vietnams_bf_sld", request.getRecipeId());

        SmartStoreConfiguration smartStoreConfig = request.getSmartStoreConfiguration();
        Assert.assertNotNull(smartStoreConfig.getParameterBundle());
    }

}
