package com.freshdirect.crm;

import java.util.Comparator;

import com.freshdirect.fdstore.FDResourceException;

/**@author ekracoff on Apr 22, 2004*/
public abstract class CrmAgentComparator implements Comparator {

	protected abstract int compare(CrmAgentInfo c1, CrmAgentInfo c2);

	public int compare(Object o1, Object o2) {
		return this.compare((CrmAgentInfo) o1, (CrmAgentInfo) o2);
	}

	public final static CrmAgentComparator NAME_COMP = new CrmAgentComparator() {
		protected int compare(CrmAgentInfo a1, CrmAgentInfo a2) {
			CrmManager cm = null;
			String agentName1 = null;
			String agentName2 = null;
			try {
				cm = CrmManager.getInstance();
				agentName1 = cm.getAgentByPk(a1.getAgentPK().getId()).getLastName();
				agentName2 = cm.getAgentByPk(a2.getAgentPK().getId()).getLastName();
			} catch (FDResourceException e) {
				e.printStackTrace();
			}
			if (agentName1 == null && agentName2 == null)
				return 0;
			if (agentName1 == null)
				return -1;
			if (agentName2 == null)
				return 1;

			return agentName1.compareTo(agentName2);
		}
	};

	public final static CrmAgentComparator ROLE_COMP = new CrmAgentComparator() {
		protected int compare(CrmAgentInfo a1, CrmAgentInfo a2) {
			CrmManager cm = null;
			String agentRole1 = null;
			String agentRole2 = null;
			try {
				cm = CrmManager.getInstance();
				agentRole1 = cm.getAgentByPk(a1.getAgentPK().getId()).getRole().getCode();
				agentRole2 = cm.getAgentByPk(a2.getAgentPK().getId()).getRole().getCode();
			} catch (FDResourceException e) {
				e.printStackTrace();
			}
			if (agentRole1 == null && agentRole2 == null)
				return 0;
			if (agentRole1 == null)
				return -1;
			if (agentRole2 == null)
				return 1;

			return agentRole1.compareTo(agentRole2);
		}
	};

	public final static CrmAgentComparator ASSIGNED_COMP = new CrmAgentComparator() {
		protected int compare(CrmAgentInfo a1, CrmAgentInfo a2) {
			return a1.getAssigned() - a2.getAssigned();
		}
	};

	public final static CrmAgentComparator OPEN_COMP = new CrmAgentComparator() {
		protected int compare(CrmAgentInfo a1, CrmAgentInfo a2) {
			return a1.getOpen() - a2.getOpen();
		}
	};

	public final static CrmAgentComparator CLOSED_COMP = new CrmAgentComparator() {
		protected int compare(CrmAgentInfo a1, CrmAgentInfo a2) {
			return a1.getClosed() - a2.getClosed();
		}
	};

	public final static CrmAgentComparator REVIEW_COMP = new CrmAgentComparator() {
		protected int compare(CrmAgentInfo a1, CrmAgentInfo a2) {
			return a1.getReview() - a2.getReview();
		}
	};

	public final static CrmAgentComparator OLDEST_COMP = new CrmAgentComparator() {
		protected int compare(CrmAgentInfo a1, CrmAgentInfo a2) {
			if (a1.getOldest() == null && a2.getOldest() == null)
				return 0;
			if (a1.getOldest() == null)
				return -1;
			if (a2.getOldest() == null)
				return 1;

			return a1.getOldest().compareTo(a2.getOldest());
		}
	};

}
