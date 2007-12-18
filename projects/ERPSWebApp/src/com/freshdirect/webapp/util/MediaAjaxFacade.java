package com.freshdirect.webapp.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URL;

import org.apache.log4j.Category;

import com.freshdirect.cms.template.TemplateException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;


/**
 * Facade class to be invoked by Ajax clients to manage CCLs.
 */
public class MediaAjaxFacade implements Serializable {

	private static final long serialVersionUID = 1L;

	private final static Category LOGGER = LoggerFactory.getInstance(MediaAjaxFacade.class);
	
	
	private static final String HELP_MESSAGE_URL = "/media/editorial/site_pages/lists/help_message.html";
	private static final String NEEDLOGIN_MESSAGE_URL = "/media/editorial/site_pages/lists/login_required.html";
	private static final String DEFAULT_HELP_MESSAGE = "<div align=\"left\"><b>Shopping Lists: THIS IS FPO COPY!</b> <p> <p> Paper grocery lists: love'em and lose'em. But our customer-created <b>Shopping Lists</b> are here to stay. When you want to save a product to a List, click <b>Save to Shopping List</b>. You can continue saving items to your default list with one click, or create multiple lists, to cover different needs. <p> To manage and shop from Your Shopping Lists, visit <a href=\"/quickshop/index.jsp\">Quickshop</a>. Modify, add or delete items, then buy some or all of the items in a list with just a few clicks!  Return to your lists at any time to easily shop for the foods you buy most often.<p> Pretty soon, you'll love lists like we love lists.</div>";
	private static final String DEFAULT_NEEDLOGIN_MESSAGE = "<p>You must have a FreshDirect account in order to use shopping lists.</p>";
	
	protected MediaAjaxFacade() {
	}
			
	public String getHelpMessage() throws AjaxFacadeException {
		LOGGER.debug("getHelpMessage() called");
		try {
			StringWriter out = new StringWriter();
			URL url = MediaUtils.resolve(FDStoreProperties.getMediaPath(),
					HELP_MESSAGE_URL);

			MediaUtils.renderMedia(url, out, null, false);

			return out.getBuffer().toString();
		} catch (FileNotFoundException e) {
			return DEFAULT_HELP_MESSAGE;
		}
		catch (TemplateException e) {
			throw new FDRuntimeException(e);
		}
		catch (IOException e) {
			throw new FDRuntimeException(e);
		}
	}
	
	public String getNeedLoginMessage() throws AjaxFacadeException {
		LOGGER.debug("getNeedLoginMessage() called");
		try {
			StringWriter out = new StringWriter();
			URL url = MediaUtils.resolve(FDStoreProperties.getMediaPath(),
					NEEDLOGIN_MESSAGE_URL);

			MediaUtils.renderMedia(url, out, null, false);

			return out.getBuffer().toString();
		} catch (FileNotFoundException e) {
			return DEFAULT_NEEDLOGIN_MESSAGE;
		}
		catch (TemplateException e) {
			throw new FDRuntimeException(e);
		}
		catch (IOException e) {
			throw new FDRuntimeException(e);
		}
	}
	
	public static MediaAjaxFacade create() {
		if (FDStoreProperties.isCclAjaxDebugFacade()) {
			return new MediaAjaxFacadeDebugWrapper();
		}
		return new MediaAjaxFacade();
	}	
}
