package com.freshdirect.transadmin.datamanager.util;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.apache.commons.lang.enum.Enum;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public abstract class GenericEnumUserType  implements  UserType {
	
	private static final int[] SQL_TYPES = {Types.VARCHAR};	    
	
    public int[] sqlTypes() { return SQL_TYPES; }
    public Class returnedClass() { return this.getClass(); }
    public boolean equals(Object x, Object y) {
    	System.out.println("OFFEnumVarcharUserType equals is getting called 11");
    	if( x == y) return true;
    	System.out.println("x"+x+"y"+y);
    	if(x instanceof Enum && y instanceof Enum){
    		Enum x1=(Enum)x;
    		Enum y1=(Enum)y;
    		if(x1.getName().equalsIgnoreCase(y1.getName())) 
    		{	
    			System.out.println("returning true");
    			return true;
    		}
    	}
    		return false;
    	}
    public Object deepCopy(Object value) { return value; }
    public boolean isMutable() { return false; }
    
    

    public Object nullSafeGet(ResultSet resultSet,
                              String[] names,
                              Object owner)
            throws HibernateException, SQLException {

      String name = resultSet.getString(names[0]);
      return resultSet.wasNull() ? null : getEnum(name);
    }

    public void nullSafeSet(PreparedStatement statement,
                            Object value,
                            int index)
            throws HibernateException, SQLException {
        System.out.println("setting the null :"+value);
        if (value == null) {
            statement.setNull(index, Types.VARCHAR);
        } else {
            statement.setString(index, ((Enum)value).getName());
        }
    }
	public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
		// TODO Auto-generated method stub
		System.out.println("assemble is getting called");
		return arg0;
	}
	public Serializable disassemble(Object arg0) throws HibernateException {
		// TODO Auto-generated method stub
		System.out.println("dissemble is getting called");
		return (Serializable)arg0; 
	}
	public int hashCode(Object arg0) throws HibernateException {
		// TODO Auto-generated method stub
		System.out.println("hashcode is getting callled in OFFEnumVarcharUserType*****");
		return super.hashCode();
	}
	public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
		// TODO Auto-generated method stub
		System.out.println("replace is getting called"+arg0+":"+arg1+":"+arg2);
		return arg0;
	}
	
	public abstract Object getEnum(String value); 
        		
}


