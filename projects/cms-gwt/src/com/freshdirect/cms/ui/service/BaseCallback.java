package com.freshdirect.cms.ui.service;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;

public abstract class BaseCallback<X> implements AsyncCallback<X> {

    @Override
    public final void onFailure(Throwable error) {
        if (error instanceof InvocationException && error.getMessage().contains("<!--SESSION EXPIRATION-->")) {
            Location.reload();
            return;
        }
        errorOccured(error);
        if (error instanceof GwtSecurityException) {
            alert("Access Denied", error.getMessage());
        } else if (error instanceof ServerException) {
            alert("Server Error", error.getMessage());
        } else {
            alert("Error", "Error occured on the server:" + error.getClass() + " message: " + error.getMessage());
        }
    }

    public void errorOccured(Throwable error) {

    }

    void alert(String title, String msg) {
        MessageBox box = new MessageBox();
        box.setTitle(title);
        box.setMessage(msg);
        box.setButtons(MessageBox.OK);
        box.setIcon(MessageBox.WARNING);
        box.setMaxWidth(800);
        box.setMinWidth(700);
        box.show();
    }
}
