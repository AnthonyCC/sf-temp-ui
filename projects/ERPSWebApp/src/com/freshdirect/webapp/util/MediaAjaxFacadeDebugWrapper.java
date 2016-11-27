package com.freshdirect.webapp.util;

import com.freshdirect.fdstore.FDStoreProperties;

public class MediaAjaxFacadeDebugWrapper extends
		MediaAjaxFacade {

	private static final long serialVersionUID = 1L;

	MediaAjaxFacadeDebugWrapper() {
		super();
	}

	public String getHelpMessage() throws AjaxFacadeException {
		failIfRequested();
		return super.getHelpMessage();
	}

	public String getNeedLoginMessage() throws AjaxFacadeException {
		failIfRequested();
		return super.getNeedLoginMessage();
	}

	private static void failIfRequested() throws AjaxFacadeException {
		if (FDStoreProperties.isCclAjaxDebugFacade()) {
			String exceptionName = FDStoreProperties.getCclAjaxDebugFacadeException();
			if (!exceptionName.equals("")) {
				try {
					Class exc = Class.forName(exceptionName);
					throw (AjaxFacadeException) exc.newInstance();
				} catch (ClassCastException e) {
					throw new AjaxFacadeDebugException(exceptionName);					
				} catch (InstantiationException e) {
					throw new AjaxFacadeDebugException(exceptionName);					
				} catch (ClassNotFoundException e) {
					throw new AjaxFacadeDebugException(exceptionName);
				} catch (IllegalAccessException e) {
					throw new AjaxFacadeDebugException(exceptionName);
				}
			}
		}		
	}
	
	public static class AjaxFacadeDebugException extends RuntimeException {

		private static final long serialVersionUID = 4611668373926733589L;

		public AjaxFacadeDebugException(String exceptionName) {
			super(exceptionName);
		}
		
	}
}
