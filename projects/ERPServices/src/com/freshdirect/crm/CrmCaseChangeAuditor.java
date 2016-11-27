package com.freshdirect.crm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.PrimaryKey;

public class CrmCaseChangeAuditor {

	private boolean done = false;
	private CrmMessageBuffer note = new CrmMessageBuffer();

	private CrmCaseModel cm;
	private CaseAuditListener listener;

	public void attach(CrmCaseModel cm) {
		if (this.cm != null) {
			throw new IllegalStateException();
		}
		this.cm = cm;
		listener = new CaseAuditListener();
		cm.addPropertyChangeListener(listener);
	}

	public void detach() {
		if (done || cm == null) {
			throw new IllegalStateException();
		}
		if (listener != null) {
			cm.removePropertyChangeListener(listener);
		}
		done = true;
	}

	public boolean isChanged() {
		if (!done) {
			throw new IllegalStateException();
		}
		return note.length() != 0;
	}

	public String getNote() {
		if (!done) {
			throw new IllegalStateException();
		}
		return note.toString();
	}

	private class CaseAuditListener implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			if (note.length() != 0) {
				note.append("\n");
			}
			note.append(evt.getPropertyName()).append(" changed");
			note.append(" from '");
			note.append(displayProperty(evt.getPropertyName(), evt.getOldValue()));
			note.append("' to '");
			note.append(displayProperty(evt.getPropertyName(), evt.getNewValue()));
			note.append("'.");
		}

		private Object displayProperty(String propertyName, Object value) {
			if ("assignedAgentPK".equals(propertyName)) {
				try {
					String agentId = ((PrimaryKey) value).getId();
					return CrmManager.getInstance().getAgentUserId(agentId) + " (" + agentId  + ")";
				} catch (FDResourceException e) {
					throw new FDRuntimeException(e);
				}
			}
			if ("subject".equals(propertyName)) {
				String queueCode = ((CrmCaseSubject) value).getQueue().getCode();
				return queueCode + " - " + ((CrmCaseSubject)value).getName();
			}
			return value;
		}

	}

}
