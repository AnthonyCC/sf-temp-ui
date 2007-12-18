package com.freshdirect.listadmin.db;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import com.freshdirect.listadmin.core.ListadminDaoFactory;

public class VirtualObject implements Serializable {
    private String virtualObjectId;
    private String name;
    private String sqlText;
    private String simpleSql;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSimpleSql() {
		return simpleSql;
	}
	public void setSimpleSql(String simpleSql) {
		this.simpleSql = simpleSql;
	}
	public String getSqlText() {
		return sqlText;
	}
	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}
	public String getVirtualObjectId() {
		return virtualObjectId;
	}
	public void setVirtualObjectId(String virtualObjectId) {
		this.virtualObjectId = virtualObjectId;
	}
	
	public static VirtualObject getById(String id) {
		ListadminDao		dao  = ListadminDaoFactory.getInstance().getListadminDao();
		Session 			sess = dao.currentSession();

		List l       = sess.createCriteria(VirtualObject.class).add(Expression.idEq(id)).list();
	
		if(l != null && l.size() > 0) {
			return (VirtualObject) l.get(0);
		}
		
		return null;
	}
	
	public static VirtualObject getByName(String name) {
		ListadminDao		dao  = ListadminDaoFactory.getInstance().getListadminDao();
		Session 			sess = dao.currentSession();

		List l       = sess.createCriteria(VirtualObject.class).add(Expression.eq("name",name)).list();
	
		if(l != null && l.size() > 0) {
			return (VirtualObject) l.get(0);
		}
		
		return null;
	}
}
