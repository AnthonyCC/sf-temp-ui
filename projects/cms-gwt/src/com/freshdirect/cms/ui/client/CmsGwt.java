package com.freshdirect.cms.ui.client;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.NavigableRelationInfo;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.freshdirect.cms.ui.service.BulkLoaderService;
import com.freshdirect.cms.ui.service.BulkLoaderServiceAsync;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.ContentServiceAsync;
import com.freshdirect.cms.ui.service.GwtDraftService;
import com.freshdirect.cms.ui.service.GwtDraftServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class CmsGwt implements EntryPoint {

    private static MainLayout mainLayout;

    private static GwtUser currentUser;
    private static ContentServiceAsync contentService;
    private static BulkLoaderServiceAsync bulkLoaderService;
    private static GwtDraftServiceAsync draftService;
    
    private static Map<String, NavigableRelationInfo> navigableInfo;
    
    private static boolean gmapsApiLoaded = false;

    public CmsGwt() {
    	contentService = (ContentServiceAsync) GWT.create(ContentService.class);
        bulkLoaderService = (BulkLoaderServiceAsync) GWT.create(BulkLoaderService.class);
        draftService = (GwtDraftServiceAsync) GWT.create(GwtDraftService.class);

        navigableInfo = new HashMap<String, NavigableRelationInfo>();        
	}
    
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
    	if (RootPanel.get("viewhistory-indicator") != null) {
    		return;
    	}
    	
    	contentService.getGoogleMapsApiKey(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				Maps.loadMapsApi(result, "2", false, new Runnable() {
		            public void run() {
		            	gmapsApiLoaded = true;
		            }
		        });
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});

    	
    	mainLayout = MainLayout.getInstance();              
		                
    	contentService.getUser(new BaseCallback<GwtUser>() {
            @Override
            public void onSuccess(GwtUser result) {
                CmsGwt.currentUser = result;
                mainLayout.userChanged();
                History.fireCurrentHistoryState();
            }
        });
        RootPanel.get().add(mainLayout);
    	
    }

    public static GwtUser getCurrentUser() {
        return currentUser;
    }

    public static ContentServiceAsync getContentService() {
        return contentService;
    }
    
    public static BulkLoaderServiceAsync getBulkLoaderService() {
		return bulkLoaderService;
	}
    
    public static GwtDraftServiceAsync getDraftService() {
        return draftService;
    }

	public static MainLayout getMainLayout() {
		return mainLayout;
	}
	
	public static boolean isGmapsApiLoaded() {
		return gmapsApiLoaded;
	}
 
	
	
	public static void getNavigableRelations(String contentType, final AsyncCallback<NavigableRelationInfo> callback) {
	    NavigableRelationInfo info = navigableInfo.get(contentType);
	    if (info == null) {
	        contentService.getNavigableRelations(contentType, new BaseCallback<NavigableRelationInfo>() {
	            @Override
	            public void onSuccess(NavigableRelationInfo result) {
	                navigableInfo.put(result.getContentType(), result);
	                callback.onSuccess(result);
	            }
	            
                });
	    } else {
	        callback.onSuccess(info);
	    }
	}
	
	public static void log(String message) {
		log(message, false);
	}
	
	public static void debug(String message) {
		consoleDebug(message);
	}
	
	public static void log(String message, boolean isErr) {
		PrintStream s = isErr ? System.err : System.out;
		s.println(message);
		consoleLog(message);
	}
	
	public static native void consoleLog(String message) /*-{
	    if (!$wnd.console) {
	    	return;
	    }
	    console.log(message);
	}-*/;
	
	public static native void consoleDebug(String message) /*-{
    if (!$wnd.console) {
    	return;
    }
    console.debug(message);
}-*/;
}
