package com.extjs.gxt.ui.client.widget.treepanel;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.NodeTree;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.AbstractImagePrototype.ImagePrototypeElement;

public class NodeTreePanel extends TreePanel<TreeContentNodeModel> {

	private NodeTree tree;		

	protected class NodeTreeView extends TreePanelView<TreeContentNodeModel> {
		
		@Override			
		public void onIconStyleChange(com.extjs.gxt.ui.client.widget.treepanel.TreePanel.TreeNode node, AbstractImagePrototype icon) {
			Element iconEl = getIconElement(node);
			if (iconEl != null) {
				if (icon != null) {	
					ImagePrototypeElement el = icon.createElement();					
					if (icon.equals(GXT.IMAGES.icon_wait())) {
						el.setClassName("waiting-gif");
					}					
					node.icon = (Element) node.getElement().getFirstChild()
							.insertBefore(el, iconEl);
				} else {
					node.getElement().getFirstChild().insertBefore(
							DOM.createSpan(), iconEl);
					node.icon = null;
				}
				El.fly(iconEl).remove();
			}
		}
		
	}
	
	public NodeTreePanel(NodeTree t, TreeStore<TreeContentNodeModel> store) {
		super(store);
		tree = t;
		setView(new NodeTreeView());
	}

	public TreeNode findNode(TreeContentNodeModel m) {
		return super.findNode(m);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onDoubleClick(TreePanelEvent tpe) {
		tree.selectAction();
	}
	
	@Override
	protected void onCollapse( TreeContentNodeModel model, TreeNode node, boolean deep ) {
		super.onCollapse( model, node, true );
	}

}
