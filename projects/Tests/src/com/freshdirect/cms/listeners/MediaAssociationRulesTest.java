package com.freshdirect.cms.listeners;

import junit.framework.TestCase;

import com.freshdirect.cms.fdstore.FDContentTypes;

public class MediaAssociationRulesTest extends TestCase {

	public void testAssociationRules() {
		MediaAssociator a = new MediaAssociator();
		a.addRule(null, "c", FDContentTypes.PRODUCT, "PROD_IMAGE");
		a.addRule(null, "cx", FDContentTypes.PRODUCT, "PROD_IMAGE_CONFIRM");
		a.addRule(null, "p", FDContentTypes.PRODUCT, "PROD_IMAGE_DETAIL");
		a.addRule(null, "f", FDContentTypes.PRODUCT, "PROD_IMAGE_FEATURE");
		a.addRule(null, "cr", FDContentTypes.PRODUCT, "PROD_IMAGE_ROLLOVER");
		a.addRule(null, "z", FDContentTypes.PRODUCT, "PROD_IMAGE_ZOOM");
		a.addRule(null, "desc", FDContentTypes.PRODUCT, "PROD_DESCR");
		a.addRule("bd", "l", FDContentTypes.BRAND, "BRAND_LOGO");
		a.addRule("bd", "m", FDContentTypes.BRAND, "BRAND_LOGO_MEDIUM");
		a.addRule("bd", "s", FDContentTypes.BRAND, "BRAND_LOGO_SMALL");

		assertNull(a.getAssociation("http://foo/bar"));
		assertNull(a.getAssociation("http://foo/bar/"));
		assertNull(a.getAssociation("http://foo/bar/foo.jpg"));
		assertNull(a.getAssociation("http://foo/bar/bd_foo_mx.jpg"));
		assertNull(a.getAssociation("http://foo/bar/___.jpg"));
		assertNull(a.getAssociation("http://foo/bar/___.foo.jpg"));
		assertNull(a.getAssociation("http://foo/bar/___.foo.jpg_"));
		assertNull(a.getAssociation("http://foo/bar/_foo_.jpg_"));
		assertNull(a.getAssociation("http://foo/bar/bd_foo__bar_baz.jpg"));
		assertNull(a.getAssociation("http://foo/bar/bd_foo.jpg"));
		assertNull(a.getAssociation("http://foo/bar/bd_foo_p.jpg"));

		assertEquals("Brand:bd_foo.BRAND_LOGO", String.valueOf(a
				.getAssociation("http://foo/bar/bd_foo_l.jpg")));
		
		assertEquals("Brand:bd_foo_bar.BRAND_LOGO", String.valueOf(a
				.getAssociation("http://foo/bar/bd_foo_bar_l.jpg")));

		assertEquals("Product:foo.PROD_IMAGE", String.valueOf(a
				.getAssociation("http://foo/bar/foo_c.jpg")));
		
		assertEquals("Product:foo_bar.PROD_IMAGE", String.valueOf(a
				.getAssociation("http://foo/bar/foo_bar_c.jpg")));
	}

}
