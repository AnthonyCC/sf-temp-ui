package com.freshdirect.content.attributes;

import java.util.Stack;

import com.freshdirect.erp.DurableModelI;
import com.freshdirect.erp.EntityModelI;
import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.ErpVisitorI;

/**
 * ErpVisitorI that tracks ID paths in a model tree, and calls visit() template methods.
 */
public class ErpIdPathVisitor implements ErpVisitorI {

	private final Stack<String[]> idPathStack = new Stack<String[]>();

	public final void pushModel(ErpModelSupport model) {
		if (!(model instanceof DurableModelI)) {
			// we won't actually visit this
			this.idPathStack.push( this.idPathStack.peek() );
			return;
		}
		
		String id = ((DurableModelI)model).getDurableId();

		String[] idPath;
		if (model instanceof EntityModelI) {
			// root level node, start new idPath
			idPath = new String[] { id };
		} else {
			// child node, append current id to parent path
			String[] prevPath = idPathStack.peek();			

			idPath = new String[ prevPath.length+1 ];
			System.arraycopy(prevPath, 0, idPath, 0, prevPath.length);
			idPath[prevPath.length] = id;
		}
		idPathStack.push(idPath);
		
		this.visit(model, idPath);
	}

	protected void visit(ErpModelSupport model, String[] idPath) {
		String rootId = idPath.length > 0 ? idPath[0] : null;
		String childId = idPath.length > 1 ? idPath[1] : null;
		String grandChildId = idPath.length > 2 ? idPath[2] : null;
		model.setAttributesKey(new ErpsAttributesKey(rootId, childId, grandChildId));
	}

	public final void popModel() {
		if (!this.idPathStack.isEmpty()) {
			this.idPathStack.pop();
		} 
	}

}
