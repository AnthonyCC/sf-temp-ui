package com.freshdirect.cms.ui.client.fields;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.GwtNodeContext;

public class PrimaryHomeSelectorField extends ComboBox<ContentNodeModel> {

    public PrimaryHomeSelectorField(ContentNodeModel value, GwtNodeContext ctx) {
        ListStore<ContentNodeModel> store = new ListStore<ContentNodeModel>();
        String key = value != null ? value.getKey() : null;
        ContentNodeModel selected = null;
        for (String path : ctx.getPaths()) {
            String label =  ctx.getLabel(path);
            String[] pathFragments = path.split("/");
            if (pathFragments.length>1) {
                String parentKey = pathFragments[pathFragments.length - 2];
                ContentNodeModel bm = new ContentNodeModel(null, label, parentKey); 
                store.add(bm);
                if (key != null && key.equals(parentKey)) {
                    selected = bm;
                }
            }
        }

        setStore(store);
        setValueField("key");
        setDisplayField("label");
        setValue(selected);
        // setValue(attr.getEnumModel());
        setEditable(false);
    }

    
}
