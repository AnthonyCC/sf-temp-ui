package com.freshdirect.cms.ui.service;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;

public abstract class BaseCallback<X> implements AsyncCallback<X> {

    @Override
    public final void onFailure(Throwable error) {
        if (error instanceof InvocationException &&
            error.getMessage().contains("<!--SESSION EXPIRATION-->")) {
            Location.reload();
            return;
        }
        errorOccured(error);
        if (error instanceof GwtSecurityException) {
            MessageBox.alert("Access Denied", error.getMessage(), null);
        } else {
            MessageBox.alert("Error", "Error occured on the server:" + error.getClass() + " message: " + error.getMessage(), null);
        }
    }
    
    public void errorOccured(Throwable error) {
        
    }

}
