package com.freshdirect.cms.ui.service;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class BaseCallback<X> implements AsyncCallback<X> {

    @Override
    public void onFailure(Throwable error) {
        if (error instanceof GwtSecurityException) {
            MessageBox.alert("Access Denied", error.getMessage(), null);
        } else {
            MessageBox.alert("Error", "Error occured on the server:" + error.getMessage(), null);
        }
    }

}
