package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

public abstract class AbstractConstraint<T> implements Constraint<T> {

	private boolean optional;
	private boolean forceInValid;

	public AbstractConstraint(boolean optional) {
		this.optional = optional;
	}

	@Override
	public boolean isOptional() {
		return optional;
	}

	@Override
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	@Override
	public boolean isForceInValid() {
		return forceInValid;
	}

	@Override
	public void setForceInValid(boolean forceInValid) {
		this.forceInValid = forceInValid;
	}

}
