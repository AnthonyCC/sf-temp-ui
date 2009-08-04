package com.freshdirect.framework.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.apache.commons.lang.enums.Enum;
import org.apache.commons.lang.enums.EnumUtils;
import org.hibernate.HibernateException;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.ParameterizedType;

/**
 * Maps a <code>org.apache.commons.lang.enums.Enum</code> to a Hibernate type.
 * 
 * @author Gavin King
 * @see http://www.hibernate.org/272.html
 */
public class EnumUserType implements EnhancedUserType, ParameterizedType {

	private Class enumClass;

	public void setParameterValues(Properties parameters) {
		String enumClassName = parameters.getProperty("enumClassName");
		try {
			enumClass = (Class) Class.forName(enumClassName);
		} catch (ClassNotFoundException cnfe) {
			throw new HibernateException("Enum class not found", cnfe);
		}
	}

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Enum) value;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return x == y;
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	public boolean isMutable() {
		return false;
	}

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		String name = rs.getString(names[0]);
		return rs.wasNull() ? null : EnumUtils.getEnum(enumClass, name);
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, Types.VARCHAR);
		} else {
			st.setString(index, ((Enum) value).getName());
		}
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	public Class returnedClass() {
		return enumClass;
	}

	public int[] sqlTypes() {
		return new int[] {Types.VARCHAR};
	}

	public Object fromXMLString(String xmlValue) {
		return EnumUtils.getEnum(enumClass, xmlValue);
	}

	public String objectToSQLString(Object value) {
		return '\'' + ((Enum) value).getName() + '\'';
	}

	public String toXMLString(Object value) {
		return ((Enum) value).getName();
	}

}
