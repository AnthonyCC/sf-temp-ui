package com.freshdirect.mobileapi.util;

import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * @author Rob
 *
 */
public class GeneralCacheAdministratorFactory {

    private static GeneralCacheAdministrator cacheAdmin = null;

    public static synchronized GeneralCacheAdministrator getCacheAdminInstance() {
        if (null == cacheAdmin) {
            cacheAdmin = new GeneralCacheAdministrator();
        }
        return cacheAdmin;
    }

}
