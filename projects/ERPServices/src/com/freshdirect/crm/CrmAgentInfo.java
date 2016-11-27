package com.freshdirect.crm;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.framework.core.PrimaryKey;

public class CrmAgentInfo implements Serializable {

	private final PrimaryKey agentPK;
	private final int assigned;
	private final int open;
	private final int review;
	private final int closed;
	private final Date oldest;
	private final String firstName;
	private final String lastName;
	private final boolean active;
	private final CrmAgentRole role;
	private final String userId;

	public CrmAgentInfo(PrimaryKey agentPK, int assigned, int open, int review, int closed, Date oldest, String firstName, String lastName, boolean active, CrmAgentRole role, String userId) {
		this.agentPK = agentPK;
		this.assigned = assigned;
		this.open = open;
		this.review = review;
		this.closed = closed;
		this.oldest = oldest;
		this.firstName = firstName;
		this.lastName = lastName;
		this.active = active;
		this.role = role;
		this.userId = userId;
	}

	public PrimaryKey getAgentPK() {
		return agentPK;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public boolean isActive() {
		return this.active;
	}
	

	public int getAssigned() {
		return assigned;
	}

	public int getOpen() {
		return open;
	}

	public int getReview() {
		return review;
	}

	public int getClosed() {
		return closed;
	}

	public Date getOldest() {
		return oldest;
	}
	
	public CrmAgentRole getRole() {
		return this.role;
	}
	
	public String getUserId() {
		return this.userId;
	}

	public String toString() {
		return "CrmAgentInfo[" + agentPK + ", " + assigned + ", " + open + ", " + review + ", " + closed + ", " + oldest + "]";
	}

}
