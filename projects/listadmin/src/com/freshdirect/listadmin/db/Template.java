package com.freshdirect.listadmin.db;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import com.freshdirect.listadmin.core.ListadminDaoFactory;
import com.freshdirect.listadmin.query.ClauseI;

public class Template implements Serializable {
	private static final long serialVersionUID = 1535053200348114820L;
	
	private String queryId;
	private String name;

	private Set clauses = new HashSet();
	private Set objects = new HashSet();
	private Set fields  = new HashSet();
	private Set groupBy = new HashSet();
	
	private List paramClauses = null;
	
	public Set getClauses() {
		return clauses;
	}
	public void setClauses(Set clauses) {
		this.clauses = clauses;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTemplateId() {
		return queryId;
	}
	public void setTemplateId(String queryId) {
		this.queryId = queryId;
	}
	
	public void addClause(ClauseI clause) {
		getClauses().add(clause);
	}
	
	public static Template getById(String id) {
		ListadminDao		dao  = ListadminDaoFactory.getInstance().getListadminDao();
		Session 			sess = dao.currentSession();

		List l       = sess.createCriteria(Template.class).add(Expression.idEq(id)).list();
		
		if(l != null && l.size() != 0) {
			return (Template) l.get(0);
		}
		
		return null;
	}
	
	// This is a little bogus.  We've added the ability for 
	// a block of SQL to contain named parameters inside
	// braces, everything on the front wants to present fill-in
	// values to the user by getting a list of clauses.  So
	// we extract names from braces here and return them as
	// a list of params.  Arguably it would be better to do this
	// at definition time and store the params in the database...
	// but there's currently no ability to attach params to an 
	// object, only to a template, so...
	public List getAllClauses() throws ParseException {
		if(paramClauses == null) {
			buildParamClauses();
		}
		
		return paramClauses;
	}
	
	public List getParamClauses() {
		List ret = new ArrayList();
		
		for(Iterator it=getClauses().iterator();it.hasNext();) {
			Clause c = (Clause) it.next();
			if(!(c instanceof JoinClause)) {
				ret.add(c);
			}
		}
		
		return ret;
	}
	
	private void buildParamClauses() throws ParseException {
		paramClauses = new ArrayList();
		
		for(Iterator it= getObjects().iterator(); it.hasNext();) {
			VirtualObject obj = (VirtualObject) it.next();	
			String sql        = obj.getSqlText();
			
			int pos = 0;
			while(pos != -1) {
				pos          = sql.indexOf('{',pos);
				
				if(pos != -1) {
					int pos2     = sql.indexOf('}',pos);
					
					if(pos2 == -1) {
						throw new ParseException("Umatched '{'", pos);
					}
					
					String token = sql.substring(pos+1,pos2);
					String name  = token;
					pos          = pos2;
					
					// The token may just be a name, in which case it's
					// a text field.  Or it may be any of the following kinds
					// of dropdowns:
					//   name:options=opt1|opt2|...
					//   name:select=table,name_field,value_field
					//   name:query=another query in the system
					//   name:enum=full.enum.class
					//   name:lookup=lookupkey
					//
					// The last version anticipates the creation of a master lookup
					// table.
					//
					// New, it may also be
					//   date:name
					//
					// for a dateTimeField
					
					Clause c = null;
					
					int colonPos = token.indexOf(':');
					
					if(colonPos == -1) {
						c = new ParamClause(token,0,token);
					} else {
						name        = token.substring(0,colonPos);
						int eqPos   = token.indexOf('=');
						
						if(eqPos == -1)
							eqPos = token.length();
						
						String type = token.substring(colonPos+1,eqPos);
						
						if("date".equals(type)) {
							DateTimeClause cc = new DateTimeClause();
							c = cc;
						} else if("options".equals(type)) {
							StaticDropdownClause cc = new StaticDropdownClause();
							cc.setOptions(token.substring(eqPos+1));
							c = cc;
						} else if("select".equals(type)) {
							SelectDropdownClause qc = new SelectDropdownClause();
							StringTokenizer st2 = new StringTokenizer(token.substring(eqPos+1),",");
							qc.setTableName(st2.nextToken());
							qc.setNameColumn(st2.nextToken());
							qc.setValueColumn(st2.nextToken());
							c = qc;
						} else if("query".equals(type)) {
							QueryDropdownClause qc = new QueryDropdownClause();
							StoredQuery sq         = StoredQuery.getByName(token.substring(eqPos+1));
							if(sq == null) {
								throw new ParseException("Unknown query: " + token.substring(eqPos+1), pos);
							}
							qc.setQuery(sq);
							c = qc;
						} else if("enum".equals(type)) {
							EnumDropdownClause qc = new EnumDropdownClause();
							qc.setClassName(token.substring(eqPos+1));
							c = qc;
						} else {
							throw new ParseException("Unknown tag: " + type,pos);
						}
					}
					
					c.setClauseId(name);
					c.setOperatorId(0);
					c.setColumn(name);
					
					paramClauses.add(c);
				}
			}
		}
		
		for(Iterator it=getClauses().iterator();it.hasNext();) {
			Clause c = (Clause) it.next();
			if(!(c instanceof ConstantClause) && !(c instanceof JoinClause)) {
				paramClauses.add(c);
			}
		}
	}

	
	public static void createNewTemplate(String queryName, String tableName, Map fields) {
		ListadminDao		dao  = ListadminDaoFactory.getInstance().getListadminDao();
		Session 			sess = dao.currentSession();
		
		dao.beginTransaction();
		
		Template q = new Template();
		q.setName(queryName);
		sess.save(q);
		
		for(Iterator it=fields.keySet().iterator();it.hasNext();) {
			String key = (String) it.next();
			Map values = (Map) fields.get(key);
			
			if(values != null && !"0".equals(values.get(key+":type"))) {
				Clause c = Clause.buildClause(key,values);
				c.setTemplateId(q.getTemplateId());
				sess.save(c);
				q.getClauses().add(c);
			}
		}
		
		sess.save(q);
		
		dao.commitTransaction();
	}
	public Set getFields() {
		return fields;
	}
	public void setFields(Set fields) {
		this.fields = fields;
	}
	public Set getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(Set groupBy) {
		this.groupBy = groupBy;
	}
	public void setObjects(Set objects) {
		this.objects = objects;
	}
	public Set getObjects() {return objects;}
}
