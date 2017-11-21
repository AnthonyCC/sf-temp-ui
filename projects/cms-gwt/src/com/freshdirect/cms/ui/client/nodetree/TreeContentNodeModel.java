package com.freshdirect.cms.ui.client.nodetree;

import com.freshdirect.cms.ui.model.ContentNodeModel;

public class TreeContentNodeModel extends ContentNodeModel {
	
	private static final long serialVersionUID = 6355813552570393744L;
	
	public static final String PATH_SEPARATOR = "|";

    protected boolean hasChildren = true;

    /**
     * Invalid constructor to be Serializable, don't use this.
     */
    public TreeContentNodeModel() {
		super();
	}

    /**
     * Creates a TreeContentNodeModel with no path.
     * @param type
     * @param label
     * @param key
     */
    public TreeContentNodeModel(String type, String label, String key) {
    	this(type, label, key, null);
    }

    /**
     * Creates a TreeContentNodeModel for root nodes (no parent)
     * @param node
     */
	public TreeContentNodeModel(ContentNodeModel node) {
		this(node, null);
    }

	/**
	 * Creates a TreeContentNodeModel for any node, you have to specify the parent node.
	 * @param node
	 * @param parent
	 */
	public TreeContentNodeModel(ContentNodeModel node, TreeContentNodeModel parent) {
		super(node);
		setPath(decoratePath(parent));
    }

	/**
     * Creates a TreeContentNodeModel for any node, you have to specify the parent node.
     * @param type
     * @param label
     * @param key
     * @param parent
     */
    public TreeContentNodeModel(String type, String label, String key, TreeContentNodeModel parent) {
    	super(type, label, key);
		setPath(decoratePath(parent));
    }

    private String decoratePath(TreeContentNodeModel parent) {
        StringBuilder path = new StringBuilder();
        if (parent != null){
            path.append(parent.getPath());
        }
        path.append(PATH_SEPARATOR).append(getKey());
        return path.toString();
    }

	public void setPath(String path) {
		set("path", path);
	}	

	public String getPath() {
		return get("path");
	}
	
	public String getId() { 
		return getPath();
	}

	public boolean hasChildren() {
		return hasChildren;
	}
	
	public void setHasChildren( boolean hasChildren ) {
		this.hasChildren = hasChildren;
	}	
	
	@Override
	public boolean equals( Object obj ) {
		if ( obj instanceof TreeContentNodeModel && getPath() != null  )
			return getPath().equals( ((TreeContentNodeModel)obj).getPath() );
		else 
			return false;
	}
}
