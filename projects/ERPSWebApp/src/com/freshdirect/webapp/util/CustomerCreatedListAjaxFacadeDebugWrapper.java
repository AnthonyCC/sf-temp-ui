package com.freshdirect.webapp.util;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;

public class CustomerCreatedListAjaxFacadeDebugWrapper extends
		CustomerCreatedListAjaxFacade {

	private static final long serialVersionUID = 1L;

	CustomerCreatedListAjaxFacadeDebugWrapper() {
		super();
	}

	public FDCustomerCreatedList addItemsToList(HttpSession session, String listName, FDCustomerCreatedList items) throws FDResourceException, AjaxFacadeException, FDSkuNotFoundException, IllegalStateException {
		failIfRequested();
		return super.addItemsToList(session, listName, EnumCustomerListType.CC_LIST.getName(), items);
	}


	public String createList(HttpSession session, String name) throws AjaxFacadeException, FDResourceException {
		failIfRequested();
		return super.createList(session, name);
	}

	public void deleteList(HttpSession session, String listName) throws FDResourceException, AjaxFacadeException {
		failIfRequested();
		super.deleteList(session, listName);
	}

	public String[] getListNames(HttpSession session) throws FDResourceException, FDCustomerListExistsException, AjaxFacadeException {
		failIfRequested();
		return super.getListNames(session);
	}

	public CustomerListNames getListNamesWithItemCount(HttpSession session) throws FDResourceException, FDCustomerListExistsException, AjaxFacadeException {
		failIfRequested();
		return super.getListNamesWithItemCount(session);
	}

	public CustomerListNames getListNamesWithItemCount(HttpSession session, String srcListName) throws FDResourceException, FDCustomerListExistsException, AjaxFacadeException {
		failIfRequested();
		return super.getListNamesWithItemCount(session, srcListName);
	}

	public String renameList(HttpSession session, String oldList, String newList) throws FDResourceException, AjaxFacadeException {
		failIfRequested();
		return super.renameList(session, oldList, newList);
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

		
		private static final long serialVersionUID = -6019502158200245577L;

		public AjaxFacadeDebugException(String exceptionName) {
			super(exceptionName);
		}
		
	}
}
