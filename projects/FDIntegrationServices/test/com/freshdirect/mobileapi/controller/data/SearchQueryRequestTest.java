package com.freshdirect.mobileapi.controller.data;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.mobileapi.controller.data.request.SearchQuery;

public class SearchQueryRequestTest extends MessageTest {

    @Test
    public void testParser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SearchQuery request = mapper.readValue(getFileContentAsStream("SearchQuery.json"), SearchQuery.class);
        Assert.assertEquals("apples", request.getQuery());
        Assert.assertEquals("brandId", request.getBrand());
        Assert.assertEquals(Integer.valueOf(1), request.getPage());
        Assert.assertEquals(Integer.valueOf(25), request.getMax());
        Assert.assertEquals("categoryId", request.getCategory());
        Assert.assertEquals("departmentId", request.getDepartment());
        Assert.assertEquals("relevancy", request.getSortBy());
    }

}
