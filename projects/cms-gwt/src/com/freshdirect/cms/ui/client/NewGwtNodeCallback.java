package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.freshdirect.cms.ui.client.fields.OneToManyRelationField;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.service.BaseCallback;

public class NewGwtNodeCallback extends BaseCallback<GwtNodeData> {
    
    OneToManyRelationField field;
    
    public NewGwtNodeCallback(OneToManyRelationField field) {
        this.field = field;
    }

    @Override
    public void onSuccess(final GwtNodeData result) {
        if (result == null) {
            MessageBox.alert("Creating node failed", "There is already exist a node with the given ID", null);
            return;
        }
        NewContentNodePopup window = new NewContentNodePopup (result, field); 
        window.show();
    }

}
