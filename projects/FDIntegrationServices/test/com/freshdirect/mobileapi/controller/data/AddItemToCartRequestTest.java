package com.freshdirect.mobileapi.controller.data;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.freshdirect.mobileapi.controller.data.request.AddItemToCart;
import com.freshdirect.mobileapi.controller.data.request.SmartStoreConfiguration;

public class AddItemToCartRequestTest extends MessageTest {

    public static final Logger logger = Logger.getLogger(AddItemToCartRequestTest.class);

    public void testParser() throws Exception {
        try {
            //String data = getFileContentAsString("Login.xml");
            ObjectMapper mapper = new ObjectMapper();
            AddItemToCart request = mapper.readValue(getFileContentAsStream("AddItemToCart.json"), AddItemToCart.class);
            assertEquals("bstk_rbeye_bnls", request.getProductConfiguration().getProductId());
            assertEquals("bgril", request.getProductConfiguration().getCategoryId());
            assertEquals("MEA0004676", request.getProductConfiguration().getSkuCode());
            assertEquals(2.0f, request.getProductConfiguration().getQuantity());
            assertEquals("E02", request.getProductConfiguration().getSalesUnit());
            assertEquals(2, request.getProductConfiguration().getOptions().size());
            assertEquals("N", request.getProductConfiguration().getOptions().get("C_MT_BF_MAR"));
            assertEquals("ST", request.getProductConfiguration().getOptions().get("C_MT_BF_PAK"));

            assertEquals("yes", request.getAgreeToTerms());
            assertEquals("74832939_p4", request.getImpressionId());
            assertEquals("rec_rs_vietnams_bf_sld", request.getRecipeId());
            assertEquals("true", request.getRequestNotification());
            assertEquals("rec_rs_vietnams_bf_sld", request.getRecipeId());

            SmartStoreConfiguration smartStoreConfig = request.getSmartStoreConfiguration();
            assertNotNull(smartStoreConfig.getParameterBundle());
            //            assertEquals("c_ymal_1", smartStoreConfig.getVariant());
            //            assertEquals("C_YMAL", smartStoreConfig.getSiteFeature());
            //            assertEquals("fru_apl_fji_4pk,trp_kiwi,fru_apl_gala_4pk,pr_asian,mln_hdw", smartStoreConfig.getRecProductIds());
            //            assertEquals("ban_yllw", smartStoreConfig.getRecCurrentNode());
            //            assertEquals("ban_yllw", smartStoreConfig.getRecYmalSource());
            //            assertEquals("true", smartStoreConfig.getRecRefreshable());
            //            assertEquals("false", smartStoreConfig.getRecSmartSavings());

        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            e.printStackTrace();
        }
    }

}
