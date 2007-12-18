package com.freshdirect.fdstore.attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MultiAttributeEditor {
	private MultiAttribute multiAttribute;
	List attribValues=new ArrayList();
	
	public MultiAttributeEditor(MultiAttribute ma) {
		this.multiAttribute = ma;
		if (ma.getValues()!=null) {
			for (int i=0; i<multiAttribute.numberOfValues(); i++) {
			   attribValues.add(multiAttribute.getValue(i));
			}
		}
	}

	public void deleteElements(int[] deleteIndices){
		//replace the element at the specified index with a null
		if (deleteIndices !=null) {
			for(int i=0; i<deleteIndices.length;i++){
				deleteElement(deleteIndices[i]);
			}
		}
	}
	
	public void deleteElement(int index) {
		// replace the element at the specified index with a null
		if (index<attribValues.size()) {
			attribValues.set(index,null);
		}
	}

	public MultiAttribute replaceValueAt(int index, Object newValue) {
		MultiAttribute rtnMultAttrib = new MultiAttribute(multiAttribute.getType(),multiAttribute.getKey(),multiAttribute.isInheritable());
		for (int i=0; i<multiAttribute.numberOfValues(); i++) {
			if (index==i) {
				rtnMultAttrib.addValue(newValue);				
			} else {
				rtnMultAttrib.addValue(multiAttribute.getValue(i));
			}
		}
		return rtnMultAttrib;
	}
	
	public MultiAttribute reOrder(Map reOrderMap) {
		// value is the index of the current position
		Collection orderedIndex = reOrderMap.values();
		MultiAttribute rtnMultAttrib = new MultiAttribute(multiAttribute.getType(),multiAttribute.getKey(),multiAttribute.isInheritable());
		for (Iterator i=orderedIndex.iterator();i.hasNext();) {
			int currentIndex = ((Integer)i.next()).intValue();
			Object attribValue = attribValues.get(currentIndex);
			if (attribValue!=null) {
				rtnMultAttrib.addValue(multiAttribute.getValue(currentIndex));
			}
		}
		return rtnMultAttrib;
	}
}
