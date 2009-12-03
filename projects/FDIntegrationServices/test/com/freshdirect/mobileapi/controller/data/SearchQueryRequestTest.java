package com.freshdirect.mobileapi.controller.data;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.freshdirect.mobileapi.controller.data.request.SearchQuery;

public class SearchQueryRequestTest extends MessageTest {
    public static final Logger logger = Logger.getLogger(SearchQueryRequestTest.class);

    public void testParser() throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SearchQuery request = mapper.readValue(getFileContentAsStream("SearchQuery.json"), SearchQuery.class);
            assertEquals("apples", request.getQuery());
            assertEquals("brandId", request.getBrand());
            assertEquals(new Integer(1), request.getPage());
            assertEquals(new Integer(25), request.getMax());
            assertEquals("categoryId", request.getCategory());
            assertEquals("departmentId", request.getDepartment());
            assertEquals("relevancy", request.getSortBy());
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            e.printStackTrace();
        }
    }

}
