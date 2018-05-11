package com.freshdirect.mobileapi.controller.data;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressSelection;

public class DeliveryAddressSelectionRequestTest extends MessageTest {

    @Test
    public void testParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeliveryAddressSelection request = mapper.readValue(getFileContentAsStream("DeliveryAddressSelection.json"), DeliveryAddressSelection.class);
        Assert.assertEquals("2150625068", request.getId());
        Assert.assertEquals("RESIDENTIAL", request.getType());
    }

}
