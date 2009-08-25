package com.freshdirect.cms.ui.client.nodetree;

import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanelSelectionModel;


public class NodeTreeSelectionModel extends TreePanelSelectionModel<ContentNodeModel> {

	private NodeTree tree;
	
	public NodeTreeSelectionModel( NodeTree t ) {
		super();
		this.tree = t;
		keyNav = new KeyNav<TreePanelEvent<ContentNodeModel>>() {
		    @Override
		    public void onDown(TreePanelEvent<ContentNodeModel> e) {
		      onKeyDown(e);
		    }
		    @Override
		    public void onLeft(TreePanelEvent<ContentNodeModel> ce) {
		      onKeyLeft(ce);
		    }
		    @Override
		    public void onRight(TreePanelEvent<ContentNodeModel> ce) {
		      onKeyRight(ce);
		    }
		    @Override
		    public void onUp(TreePanelEvent<ContentNodeModel> e) {
		      onKeyUp(e);
		    }
		    @Override
		    public void onEnter( TreePanelEvent<ContentNodeModel> ce ) {
		    	tree.selectAction();
		    }
		};
	}
}
