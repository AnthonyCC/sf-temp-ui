package com.freshdirect;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.easymock.ParameterComparator;

public class ReflectionParamComparator implements ParameterComparator {

	public boolean equals(Object[] expected, Object[] actual) {
		return EqualsBuilder.reflectionEquals(expected, actual);
	}

	public String toString(Object[] parameters) {
		StringBuffer sb = new StringBuffer("[");
		for (int i = 0; i < parameters.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(ToStringBuilder.reflectionToString(parameters[i]));
		}
		sb.append("]");
		return sb.toString();

	}

}