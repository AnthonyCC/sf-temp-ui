package com.freshdirect.cms.ui.client.nodetree;

import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;


public class NodeTreePanel extends TreePanel<ContentNodeModel> {

	private NodeTree tree;
	
	public NodeTreePanel( NodeTree t, TreeStore<ContentNodeModel> store ) {
		super( store );
		this.tree = t;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onDoubleClick( TreePanelEvent tpe ) {
		if ( tree.getNodeSelectListener() != null ) {
			tree.selectAction();
		} else {
			super.onDoubleClick( tpe );
		}
	}
		
}
