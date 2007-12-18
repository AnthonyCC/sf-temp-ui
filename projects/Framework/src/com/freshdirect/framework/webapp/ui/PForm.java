package com.freshdirect.framework.webapp.ui;

import java.util.*;
import javax.servlet.http.*;

import com.freshdirect.framework.webapp.*;

public class PForm {

	private Map elements = new HashMap();
	
	public void addElement(PElement element) {
		this.elements.put(element.getName(), element);
	}
	
	public PElement getElement(String name) {
		return (PElement) this.elements.get(name);
	}

	public void initialize(HttpServletRequest request) {
		for (Iterator i=this.elements.values().iterator(); i.hasNext(); ) {
			((PElement)i.next()).initialize(request);
		}
	}
	
	public void validate(ActionResult result) {
		for (Iterator i=this.elements.values().iterator(); i.hasNext(); ) {
			((PElement)i.next()).validate(result);
		}
	}
	
	public Collection getValues(){
		return this.elements.values();
	}

}

