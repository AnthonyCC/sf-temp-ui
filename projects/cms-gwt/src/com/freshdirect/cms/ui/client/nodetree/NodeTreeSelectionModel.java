package com.freshdirect.cms.ui.client.nodetree;

import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanelSelectionModel;


public class NodeTreeSelectionModel extends TreePanelSelectionModel<TreeContentNodeModel> {

	private NodeTree tree;
	
	public NodeTreeSelectionModel( NodeTree t ) {
		super();
		this.tree = t;
		keyNav = new KeyNav<TreePanelEvent<TreeContentNodeModel>>() {
		    @Override
		    public void onDown(TreePanelEvent<TreeContentNodeModel> e) {
		      onKeyDown(e);
		    }
		    @Override
		    public void onLeft(TreePanelEvent<TreeContentNodeModel> ce) {
		      onKeyLeft(ce);
		    }
		    @Override
		    public void onRight(TreePanelEvent<TreeContentNodeModel> ce) {
		      onKeyRight(ce);
		    }
		    @Override
		    public void onUp(TreePanelEvent<TreeContentNodeModel> e) {
		      onKeyUp(e);
		    }
		    @Override
		    public void onEnter( TreePanelEvent<TreeContentNodeModel> ce ) {
		    	tree.selectAction();
		    }
		};
	}
}
