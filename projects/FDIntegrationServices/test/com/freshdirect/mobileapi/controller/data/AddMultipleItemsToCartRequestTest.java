package com.freshdirect.mobileapi.controller.data;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.mobileapi.controller.data.request.AddMultipleItemsToCart;

public class AddMultipleItemsToCartRequestTest extends MessageTest {

    @Test
    public void testParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AddMultipleItemsToCart request = mapper.readValue(getFileContentAsStream("AddMultipleItemsToCart.json"),
                AddMultipleItemsToCart.class);

        Assert.assertEquals(2, request.getProductsConfiguration().size());
        List<ProductConfiguration> productConfigs = request.getProductsConfiguration();

        ProductConfiguration pc = productConfigs.get(0);
        Assert.assertEquals("bstk_rbeye_bnls", pc.getProduct().getId());
        Assert.assertEquals("bgril", pc.getProduct().getCategoryId());
        Assert.assertEquals("MEA0004676", pc.getSkuCode());
        Assert.assertEquals(2.0f, pc.getQuantity(), 0.01d);
        Assert.assertEquals("E02", pc.getSalesUnit().getName());
        Assert.assertEquals(2, pc.getOptions().size());
        Assert.assertEquals("N", pc.getOptions().get("C_MT_BF_MAR"));
        Assert.assertEquals("ST", pc.getOptions().get("C_MT_BF_PAK"));
        
        ProductConfiguration pc2 = productConfigs.get(1);
        Assert.assertEquals("usq_chi_alfa_merl", pc2.getProduct().getId());
        Assert.assertEquals("usq_red", pc2.getProduct().getCategoryId());
        Assert.assertEquals("WIN0073196", pc2.getSkuCode());
        Assert.assertEquals(1.0f, pc2.getQuantity(), 0.01d);
    }
}
