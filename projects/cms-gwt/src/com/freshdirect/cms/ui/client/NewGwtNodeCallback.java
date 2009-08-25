package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.freshdirect.cms.ui.client.fields.OneToManyRelationField;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class NewGwtNodeCallback implements AsyncCallback<GwtNodeData> {
    
    OneToManyRelationField field;
    
    public NewGwtNodeCallback(OneToManyRelationField field) {
        this.field = field;
    }

    @Override
    public void onSuccess(final GwtNodeData result) {
        NewContentNodePopup window = new NewContentNodePopup (result, field); 
        window.show();
    }
    
    @Override
    public void onFailure(Throwable caught) {
        MessageBox.alert("Error creating node", caught.getMessage(), null);
    }

}
