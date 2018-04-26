package com.freshdirect.mobileapi.controller.data;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.mobileapi.controller.data.request.UpdateItemInCart;

public class UpdateItemInCartRequestTest extends MessageTest {

    @Test
    public void testParser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        UpdateItemInCart request = mapper.readValue(getFileContentAsStream("UpdateItemInCart.json"), UpdateItemInCart.class);
        Assert.assertEquals("bstk_rbeye_bnls", request.getProductConfiguration().getProductId());
        Assert.assertEquals("bgril", request.getProductConfiguration().getCategoryId());
        Assert.assertEquals("MEA0004675", request.getProductConfiguration().getSkuCode());
        Assert.assertEquals(1.0f, request.getProductConfiguration().getQuantity(), 0.01f);
        Assert.assertEquals("E04", request.getProductConfiguration().getSalesUnit().getName());
        Assert.assertEquals(2, request.getProductConfiguration().getOptions().size());
        Assert.assertEquals("N", request.getProductConfiguration().getOptions().get("C_MT_BF_MAR"));
        Assert.assertEquals("VP", request.getProductConfiguration().getOptions().get("C_MT_BF_PAK"));
        Assert.assertEquals("1234567890", request.getCartLineId());
    }

}
