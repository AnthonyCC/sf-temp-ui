package com.freshdirect.content.attributes;

import java.util.Stack;

import com.freshdirect.erp.DurableModelI;
import com.freshdirect.erp.EntityModelI;
import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.ErpVisitorI;

/**
 * ErpVisitorI that tracks ID paths in a model tree, and calls visit() template methods.
 */
abstract class ErpIdPathVisitor implements ErpVisitorI {

	private final Stack idPathStack = new Stack();

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
			String[] prevPath = (String[]) idPathStack.peek();			

			idPath = new String[ prevPath.length+1 ];
			System.arraycopy(prevPath, 0, idPath, 0, prevPath.length);
			idPath[prevPath.length] = id;
		}
		idPathStack.push(idPath);
		
		this.visit(model, idPath);
	}

	protected abstract void visit(ErpModelSupport model, String[] idPath);

	public final void popModel() {
		if (!this.idPathStack.isEmpty()) {
			this.idPathStack.pop();
		} 
	}

}
