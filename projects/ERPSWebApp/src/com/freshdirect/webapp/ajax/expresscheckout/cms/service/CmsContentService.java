package com.freshdirect.webapp.ajax.expresscheckout.cms.service;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.MediaUtils;

public class CmsContentService {

	private static final CmsContentService INSTANCE = new CmsContentService();

	private ContentFactory contentFactory;

	private CmsContentService() {
		contentFactory = ContentFactory.getInstance();
	}

	public static CmsContentService defaultService() {
		return INSTANCE;
	}

	public String getExpressCheckoutReceiptHeader(final FDUserI user) {
		return renderHtmlToString(contentFactory.getStore().getExpressCheckoutReceiptHeader(), user);
	}

	public String getExpressCheckoutReceiptEditorial(final FDUserI user) {
		return renderHtmlToString(contentFactory.getStore().getExpressCheckoutReceiptEditorial(), user);
	}

	public String getExpressCheckoutTextMessageAlertHeader(final FDUserI user) {
		return renderHtmlToString(contentFactory.getStore().getExpressCheckoutTextMessageAlertHeader(), user);
	}

	private String renderHtmlToString(final Html html, final FDUserI user) {
		return MediaUtils.renderHtmlToString(html, (FDSessionUser) user);
	}
}
