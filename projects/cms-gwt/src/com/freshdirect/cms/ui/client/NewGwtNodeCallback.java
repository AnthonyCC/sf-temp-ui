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
            MessageBox.alert("Creating node failed", "A node with the given ID already exists", null);
            return;
        }
        NewContentNodePopup window = new NewContentNodePopup (result, field); 
        NewKeySet.add(result.getNode().getKey());
        window.show();
    }

}
