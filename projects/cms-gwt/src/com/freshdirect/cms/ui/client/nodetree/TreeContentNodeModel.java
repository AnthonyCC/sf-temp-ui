package com.freshdirect.cms.ui.client.nodetree;

public class TreeContentNodeModel extends ContentNodeModel {

	private String path;
	
	public TreeContentNodeModel() {
		super();
		path = "/" + getKey();
	}
	
	public TreeContentNodeModel(ContentNodeModel node) {		
		setLabel(node.getLabel());
		setKey(node.getKey());
		setType(node.getType());
		this.hasChildren = node.hasChildren;
		path = "/" + getKey();
    }
	
	public TreeContentNodeModel(ContentNodeModel node, TreeContentNodeModel parent ) {		
		setLabel(node.getLabel());
		setKey(node.getKey());
		setType(node.getType());
		this.hasChildren = node.hasChildren;
		path = parent.path + "/" + getKey();
    }
	
    public String getPath() {
    	return path;
    }
}
