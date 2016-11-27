package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

public interface Constraint<T> {

	boolean isValid(T value);

	String getErrorMessage();

	boolean isOptional();

	void setOptional(boolean optional);

	boolean isForceInValid();

	void setForceInValid(boolean optional);
}
