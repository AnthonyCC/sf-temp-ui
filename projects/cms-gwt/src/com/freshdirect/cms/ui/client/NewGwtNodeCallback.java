package com.freshdirect.cms.ui.client;

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
        NewContentNodePopup window = new NewContentNodePopup (result, field); 
        window.show();
    }

}
