package com.freshdirect.mobileapi.controller.data;

import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.freshdirect.mobileapi.controller.data.request.AddMultipleItemsToCart;

public class AddMultipleItemsToCartRequestTest extends MessageTest {
    public static final Logger logger = Logger.getLogger(AddMultipleItemsToCartRequestTest.class);

    public void testParser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AddMultipleItemsToCart request = mapper.readValue(getFileContentAsStream("AddMultipleItemsToCart.json"),
                AddMultipleItemsToCart.class);

        assertEquals(2, request.getProductsConfiguration().size());
        List<ProductConfiguration> productConfigs = request.getProductsConfiguration();

        ProductConfiguration pc = productConfigs.get(0);
        assertEquals("bstk_rbeye_bnls", pc.getProduct().getId());
        assertEquals("bgril", pc.getProduct().getCategoryId());
        assertEquals("MEA0004676", pc.getSkuCode());
        assertEquals(2.0f, pc.getQuantity());
        assertEquals("E02", pc.getSalesUnit());
        assertEquals(2, pc.getOptions().size());
        assertEquals("N", pc.getOptions().get("C_MT_BF_MAR"));
        assertEquals("ST", pc.getOptions().get("C_MT_BF_PAK"));
        
        ProductConfiguration pc2 = productConfigs.get(1);
        assertEquals("usq_chi_alfa_merl", pc2.getProduct().getId());
        assertEquals("usq_red", pc2.getProduct().getCategoryId());
        assertEquals("WIN0073196", pc2.getSkuCode());
        assertEquals(1.0f, pc2.getQuantity());

    }
}
