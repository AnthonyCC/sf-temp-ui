package com.freshdirect.dlvadmin.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

public abstract class EditShift extends BaseComponent {

	public abstract IActionListener getListener();

	public void submitForm(IRequestCycle cycle) {
		IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
		if (!delegate.getHasErrors()) {
			this.getListener().actionTriggered(this, cycle);	
		}
	}


}
