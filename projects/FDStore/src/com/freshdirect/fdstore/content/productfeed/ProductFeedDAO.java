package com.freshdirect.fdstore.content.productfeed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.util.DaoUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ProductFeedDAO {

    private static Category LOGGER = LoggerFactory.getInstance(ProductFeedDAO.class);

    private static final String GET_ALL_PRODUCTFEEDSUBSCRIBERS = "select CODE, DESCRIPTION, TYPE, URL, USERID, PASSWORD, DEFAULT_UPLOADPATH, STORE  from MIS.PRODUCTFEED_SUBSCRIBER";

    public static List<ProductFeedSubscriber> getAllProductFeedSubscribers(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ProductFeedSubscriber> productFeedSubscribers = new ArrayList<ProductFeedSubscriber>();
        try {
            LOGGER.info("getAllProductFeedSubscribers : Start Execute");
            ps = conn.prepareStatement(GET_ALL_PRODUCTFEEDSUBSCRIBERS);

            rs = ps.executeQuery();
            while (rs.next()) {
                productFeedSubscribers.add(new ProductFeedSubscriber(rs.getString("CODE"), rs.getString("DESCRIPTION"), ProductFeedSubscriberType.valueOf(rs.getString("TYPE")),
                        rs.getString("URL"), rs.getString("USERID"), rs.getString("PASSWORD"), rs.getString("DEFAULT_UPLOADPATH"), extractStores(rs.getString("STORE"))));
            }

        } catch (SQLException exc) {
            throw exc;
        } finally {
           DaoUtil.close(rs, ps);
        }
        return productFeedSubscribers;
    }

    private static List<EnumEStoreId> extractStores(String storeList) {
        List<EnumEStoreId> stores = new ArrayList<EnumEStoreId>();
        if (storeList != null) {
            EnumEStoreId storeId = null;
            for (String store : storeList.split(",")) {
                storeId = EnumEStoreId.valueOfContentId(store);
                if (storeId == null) {
                    LOGGER.warn("Store not exists with key: " + store);
                    continue;
                }
                stores.add(storeId);
            }
        }
        return stores;
    }

}
