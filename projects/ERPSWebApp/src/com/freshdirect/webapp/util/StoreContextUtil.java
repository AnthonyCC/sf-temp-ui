package com.freshdirect.webapp.util;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import com.freshdirect.common.context.StoreContext;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * 
 * @author ksriram
 *
 */
public class StoreContextUtil implements Serializable {
	
	private static final long serialVersionUID = -4340268835909648550L;

	public static StoreContext getStoreContext(HttpSession session){
		StoreContext storeContext = null;
		if(null !=session){
			try {
				storeContext =(StoreContext)session.getAttribute(SessionName.STORE_CONTEXT);	
			}catch(IllegalStateException e) {
				storeContext = null;
			}
			if(null == storeContext){
        		storeContext = StoreContext.createStoreContext(EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId())));
        		try {
        			session.setAttribute(SessionName.STORE_CONTEXT, storeContext);
        		}catch(IllegalStateException e) {
    			}
			}
        }else{
        	storeContext = StoreContext.createStoreContext(EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId())));
        }
        return storeContext;
	}

	public static StoreContext getStoreContext(){
		return getStoreContext(null);
	}
}
