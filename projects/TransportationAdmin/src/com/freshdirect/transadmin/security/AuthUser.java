package com.freshdirect.transadmin.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AuthUser implements Serializable {

	public AuthUser() {	}

	private Set<Privilege> privileges = new HashSet<Privilege>();

	public Set<Privilege> getPrivilages() {
		return privileges;
	}

	public void setPrivilages(Set<Privilege> privileges) {
		this.privileges = privileges;
	}

	public boolean hasPrivilege(Privilege p) {
		return privileges.contains(p);
	}

	public enum Privilege {
		EVENTLOG_ADD, MOTEVENTLOG_ADD, MOTEVENTLOG_VERIFY, SHIFTLOG_ADD
	}
}