package com.freshdirect.cms.ui.client.fields;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.freshdirect.cms.ui.client.nodetree.StringTokenizer;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.GwtNodeContext;

public class PrimaryHomeSelectorField extends ComboBox<ContentNodeModel> {

    public PrimaryHomeSelectorField(ContentNodeModel value, GwtNodeContext ctx) {
        ListStore<ContentNodeModel> store = new ListStore<ContentNodeModel>();
        String key = value != null ? value.getKey() : null;
        ContentNodeModel selected = null;
        if ( ctx != null ) {
	        for (String path : ctx.getPaths()) {
	            String label =  ctx.getLabel(path);
	            StringTokenizer tokenizer = new StringTokenizer( path, TreeContentNodeModel.pathSeparator );
	            List<String> pathFragments = new ArrayList<String>();
	            while ( tokenizer.hasMoreTokens() ) {
	            	pathFragments.add( tokenizer.nextToken() );
	            }
	            if ( pathFragments.size() > 1 ) {
	                String parentKey = pathFragments.get( pathFragments.size() - 2 );
	                ContentNodeModel bm = new ContentNodeModel(null, label, parentKey); 
	                store.add(bm);
	                if (key != null && key.equals(parentKey)) {
	                    selected = bm;
	                }
	            }
	        }
        }

        setStore(store);
        setValueField("key");
        setDisplayField("label");
        setValue(selected);
        setEditable(false);
		setTriggerAction(TriggerAction.ALL);
    }    
}
