package com.freshdirect.crm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import junit.framework.TestCase;

import org.easymock.MockControl;

import com.freshdirect.ReflectionParamComparator;
import com.freshdirect.framework.core.PrimaryKey;

public class CrmCaseModelTestCase extends TestCase {

	public CrmCaseModelTestCase(String name) {
		super(name);
	}

	private MockControl ctrl;
	private PropertyChangeListener mockListener;

	protected void setUp() throws Exception {
		ctrl = MockControl.createStrictControl(PropertyChangeListener.class);
		mockListener = (PropertyChangeListener) ctrl.getMock();
	}

	private void expectPropertyChange(Object source, String propertyName, Object oldValue, Object newValue) {
		mockListener.propertyChange(new PropertyChangeEvent(source, propertyName, oldValue, newValue));
		ctrl.setVoidCallable();
	}

	public void testPropertyChange() {
		// needs PK: anonymous cases don't fire!
		CrmCaseModel cm = new CrmCaseModel(new PrimaryKey("c1"));

		this.expectPropertyChange(cm, "assignedAgentPK", null, new PrimaryKey("foo"));
		ctrl.setComparator(new ReflectionParamComparator());
		this.expectPropertyChange(cm, "customerPK", null, new PrimaryKey("bar"));
		this.expectPropertyChange(cm, "customerPK", new PrimaryKey("bar"), null);
		ctrl.replay();

		cm.addPropertyChangeListener(mockListener);

		cm.setAssignedAgentPK(new PrimaryKey("foo"));
		cm.setAssignedAgentPK(new PrimaryKey("foo")); // don't fire

		cm.setCustomerPK(null); // don't fire
		cm.setCustomerPK(new PrimaryKey("bar"));
		cm.setCustomerPK(null);

		ctrl.verify();

	}

}
