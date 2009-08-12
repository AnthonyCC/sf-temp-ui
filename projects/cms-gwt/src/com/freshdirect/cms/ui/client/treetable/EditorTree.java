package com.freshdirect.cms.ui.client.treetable;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treetable.TreeTableColumn;
import com.extjs.gxt.ui.client.widget.treetable.TreeTableColumnModel;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.BulkEditModel;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.ContentServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EditorTree extends EditableTreeTable {

	private ContentServiceAsync cs;
	private BaseTreeLoader<BulkEditModel> loader;
	private TreeStore<BulkEditModel> store;
	private EditableTreeTableBinder<BulkEditModel> binder;

	private static TreeTableColumnModel initCM() {
		List<TreeTableColumn> columns = new ArrayList<TreeTableColumn>();		
		EditableTreeTableColumn column = new EditableTreeTableColumn("label", "Label", 300); 		
		column.setEditable(false);		
		columns.add(column); 	
		
		column = new EditableTreeTableColumn("fullname", "Full Name", 240);
		column.setEditable(true);		
		columns.add(column); 
		
		column = new EditableTreeTableColumn("navname", "Nav Name", 240);
		column.setEditable(true);		
		columns.add(column);
		
		column = new EditableTreeTableColumn("glancename", "Glance Name", 240);
		column.setEditable(true);		
		columns.add(column);			
		
		return new TreeTableColumnModel(columns);
	}
	
	public EditorTree(ContentNodeModel selected) {		
		super(initCM());		
		
		cs = (ContentServiceAsync) GWT.create(ContentService.class);

	    // data proxy
	    RpcProxy<List<BulkEditModel>> proxy = new RpcProxy<List<BulkEditModel>>() {

			@Override
			protected void load(Object loadConfig, AsyncCallback<List<BulkEditModel>> callback) {
				cs.getEditChildren((BulkEditModel) loadConfig, callback);
			}
	    };
	    
	    loader = new BaseTreeLoader<BulkEditModel>(proxy) {	    		    	
	    	
	    	@Override
	        public boolean hasChildren(BulkEditModel parent) {
	    		return true;
	        }
	    };	    	   
	   	   
	    store = new TreeStore<BulkEditModel>(loader);
	    
	    binder = new EditableTreeTableBinder<BulkEditModel>(this, store);
	    
	    binder.setIconProvider(new ModelStringProvider<BulkEditModel>() {  
   
	    	public String getStringValue(BulkEditModel model, String property) {  	    	
	    		return "img/icons/" + model.getType() + ".gif";
	    	} 
	    		    	
	    });	    
	    
	    binder.setDisplayProperty("label");	
	    setAutoWidth(true);
	    setHeight(600);	    
	    
	    if (selected == null) {
	    	loader.load(null);
	    } else {
	    	loader.load(new BulkEditModel(selected.getType(), selected.getLabel(), selected.getKey()));
	    }
	}
	
}
