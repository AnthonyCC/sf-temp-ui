package com.freshdirect.webapp.ajax.expresscheckout.content.service;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.MediaUtils;

public class ContentFactoryService {

	private static final ContentFactoryService INSTANCE = new ContentFactoryService();

	private ContentFactory contentFactory;

	private ContentFactoryService() {
		contentFactory = ContentFactory.getInstance();
	}

	public static ContentFactoryService defaultService() {
		return INSTANCE;
	}

	public String getExpressCheckoutReceiptHeader(final FDUserI user) {
		return MediaUtils.renderHtmlToString(contentFactory.getStore().getExpressCheckoutReceiptHeader(), (FDSessionUser) user);
	}

	public String getExpressCheckoutReceiptEditorial(final FDUserI user) {
		return MediaUtils.renderHtmlToString(contentFactory.getStore().getExpressCheckoutReceiptEditorial(), (FDSessionUser) user);
	}

	public String getExpressCheckoutTextMessageAlertHeader(final FDUserI user) {
		return MediaUtils.renderHtmlToString(contentFactory.getStore().getExpressCheckoutTextMessageAlertHeader(), (FDSessionUser) user);
	}
}
