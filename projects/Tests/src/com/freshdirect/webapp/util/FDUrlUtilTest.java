package com.freshdirect.webapp.util;

import org.junit.Test;

import com.freshdirect.common.customer.EnumServiceType;

import junit.framework.Assert;

public class FDUrlUtilTest {

    @Test
    public void extendsUrlWithServiceTypeCorporate() {
        Assert.assertEquals("/index.jsp?serviceType=CORPORATE", FDURLUtil.extendsUrlWithServiceType("/index.jsp", EnumServiceType.CORPORATE));
    }

    @Test
    public void extendsUrlWithServiceTypeHome() {
        Assert.assertEquals("/index.jsp?serviceType=HOME", FDURLUtil.extendsUrlWithServiceType("/index.jsp", EnumServiceType.HOME));
    }

    @Test
    public void extendsComplextUrlWithServiceTypeHome() {
        Assert.assertEquals("/index.jsp?something=yes&serviceType=HOME", FDURLUtil.extendsUrlWithServiceType("/index.jsp?something=yes", EnumServiceType.HOME));
    }

    @Test
    public void extendsAlreadyContainsServiceTypeUrlWithServiceTypeHome() {
        Assert.assertEquals("/index.jsp?serviceType=HOME", FDURLUtil.extendsUrlWithServiceType("/index.jsp?serviceType=CORPORATE", EnumServiceType.HOME));
    }

    @Test
    public void extendsAlreadyContainsServiceTypeComplexUrlWithServiceTypeHome() {
        Assert.assertEquals("/index.jsp?param=value&serviceType=HOME&param2=value2", FDURLUtil.extendsUrlWithServiceType("/index.jsp?param=value&serviceType=CORPORATE&param2=value2", EnumServiceType.HOME));
    }

}
