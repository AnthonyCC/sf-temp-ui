package com.freshdirect.listadmin.ui.page;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.RequestContext;
import org.apache.xerces.impl.xs.dom.DOMParser;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.freshdirect.listadmin.core.ListadminDaoFactory;
import com.freshdirect.listadmin.db.Clause;
import com.freshdirect.listadmin.db.ConstantClause;
import com.freshdirect.listadmin.db.EnumDropdownClause;
import com.freshdirect.listadmin.db.JoinClause;
import com.freshdirect.listadmin.db.ListadminDao;
import com.freshdirect.listadmin.db.ParamClause;
import com.freshdirect.listadmin.db.QueryDropdownClause;
import com.freshdirect.listadmin.db.StaticDropdownClause;
import com.freshdirect.listadmin.db.StoredQuery;
import com.freshdirect.listadmin.db.Template;
import com.freshdirect.listadmin.db.TemplateField;
import com.freshdirect.listadmin.db.TemplateGroupBy;
import com.freshdirect.listadmin.db.VirtualObject;
import com.freshdirect.listadmin.metadata.MetaDataUtils;

/**
 * TODO: In the new nomenclature this should really be "newTemplate".
 * 
 * @author lPekowsky
 */
public class NewQuery extends AppPage implements IExternalPage {
	private List allFields = new ArrayList();
	
	private List fields = new ArrayList();
	private List selectedFields = new ArrayList();
	
	private List groupBys = new ArrayList();
	private List selectedGroupBys = new ArrayList();
	
	private List orderBys = new ArrayList();
	private List selectedOrderBys = new ArrayList();
	
	private List constraints = new ArrayList();
	
	private Template template = new Template();
	
	
	public void activateExternalPage(Object args[], IRequestCycle cycle) {
		tables    = new String[1];
		tables[0] = (String) args[0];
		
		setUpLists();
		
		// Second argument would be a templateID - it means we're editing an existing template
		if(args.length > 1) {
			template = Template.getById((String) args[1]);
			
			if(template.getFields().isEmpty()) {
				selectedFields = fields;
				fields         = new ArrayList();
			} else {
				setUpList(template.getFields(),fields,selectedFields);
			}
			
			setUpList(template.getGroupBy(),groupBys,selectedGroupBys);
			// setUpList(template.getOrderBy(),orderBys,selectedOrderBys);
			constraints.addAll(template.getClauses());
		}
	}

	private void setUpLists() {
		fields    = getAllFieldsForAllTables();
		groupBys  = getAllFieldsForAllTables();
		orderBys  = getAllFieldsForAllTables();
		allFields = getAllFieldsForAllTables();
	}
	
	
	private void setUpList(Set f,List all, List selected) {
		for(Iterator it=f.iterator();it.hasNext();) {
			Object o = it.next();
			
			String fullName = "";
			
			try {
				fullName = BeanUtils.getSimpleProperty(o,"fieldName");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			selected.add(fullName);
			all.remove(fullName);
		}

	}
	public List getFields() {
		return fields;
	}


	public void setFields(List fields) {
		this.fields = fields;
	}


	public List getGroupBys() {
		return groupBys;
	}


	public void setGroupBys(List groupBys) {
		this.groupBys = groupBys;
	}


	public List getOrderBys() {
		return orderBys;
	}


	public void setOrderBys(List orderBys) {
		this.orderBys = orderBys;
	}


	public List getSelectedFields() {
		return selectedFields;
	}


	public void setSelectedFields(List selectedFields) {
		this.selectedFields = selectedFields;
	}


	public List getSelectedGroupBys() {
		return selectedGroupBys;
	}


	public void setSelectedGroupBys(List selectedGroupBys) {
		this.selectedGroupBys = selectedGroupBys;
	}


	public List getSelectedOrderBys() {
		return selectedOrderBys;
	}


	public void setSelectedOrderBys(List selectedOrderBys) {
		this.selectedOrderBys = selectedOrderBys;
	}


	public List getAllFieldsForAllTables() {
		List ret = new ArrayList();
		
		for(int i=0;i<tables.length;i++) {
			List tmp = MetaDataUtils.getAllFields(tables[i]);
			for(Iterator it = tmp.iterator();it.hasNext();) {
				ret.add(tables[i] + "." + it.next());
			}
		}
		return ret;
	}
	
	String tables[];
	
	public void setTables(String tables[]) {
		this.tables = tables;
		setUpLists();
	}

	public String getTableString() {
		String ret = "";
		for(int i=0;i<tables.length;i++) {
			ret = ret + tables[i];
			if(i < tables.length-1) {
				ret = ret + ",";
			}
		}
		
		return ret;
	}
	
	public void setTableString(String tableString) {
		StringTokenizer st = new StringTokenizer(tableString,",");
		tables = new String[st.countTokens()];
		
		for(int i=0;i<tables.length;i++) {
			tables[i] = st.nextToken();
		}
	}
	
	public List getAvailableQueries() {
		ListadminDao dao     = ListadminDaoFactory.getInstance().getListadminDao();
		Session      sess    = dao.currentSession();
		List 		 queries = sess.createCriteria(StoredQuery.class).list();
		List 	     ret     = new ArrayList(queries.size());
		
		for(Iterator it=queries.iterator();it.hasNext();) {
			StoredQuery query = (StoredQuery) it.next();
			
			Map m = new HashMap();
			m.put("name",query.getName());
			//m.put("className",query.getTableName());
			//m.put("queryId",query.getQueryId());
			//m.put("fields",MetaDataUtils.getAllFields(query.getTableName()));
			
			ret.add(m);
		}
		
		return ret;
	}
	
	public List getAvailableStoredQueries() {
		List ret = new ArrayList();
		
		try {
			ListadminDao dao  = ListadminDaoFactory.getInstance().getListadminDao();
			Session      sess = dao.currentSession();
			ret = sess.createCriteria(StoredQuery.class).list();
			dao.closeSession();
		} catch (Exception r) {
			r.printStackTrace();
		}
		return ret;
	}
	
	public void saveQuery(IRequestCycle cycle) {
		String templateId = handleRequest(cycle.getRequestContext());
				
		if(templateId != null) {
			IPage page = cycle.getPage("ConfigureQuery");
			page.setProperty("templateId",templateId);
			cycle.activate("ConfigureQuery");
			return;
		}
		
		cycle.activate("Home");
	}

	private String handleRequest(RequestContext requestContext) {	
		String submitData = requestContext.getParameter("submitData");
		InputSource in    = new InputSource(new StringReader(submitData));
		DOMParser parser  = new DOMParser();
		
		try {
			parser.parse(in);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		ListadminDao dao  = ListadminDaoFactory.getInstance().getListadminDao();
		Session      sess = dao.currentSession();
		dao.beginTransaction();
		
		Document doc         = parser.getDocument();
		NodeList nodeList    = doc.getElementsByTagName("query");
		Node qnode           = nodeList.item(0);	
		NamedNodeMap attribs = qnode.getAttributes();
		
		String id = attribs.getNamedItem("id").getNodeValue();
		
		if(id != null && id.length() > 0) {
			template = Template.getById(id);
			cleanTemplate();
		}
		
		template.setName(attribs.getNamedItem("name").getNodeValue());
		
		sess.save(template);
		
		for(int i=0;i<tables.length;i++) {
			template.getObjects().add(VirtualObject.getByName(tables[i]));
		}
		
		nodeList      = doc.getElementsByTagName("fields");
		Node fields   = nodeList.item(0);	
		NodeList kids = fields.getChildNodes();
		
		int count = 0;
		
		for(int i=0;i<kids.getLength();i++) {
			TemplateField field = new TemplateField();
			field.setTemplateId(template.getTemplateId());
			field.setFieldPosition(count++);
			field.setFieldName(kids.item(i).getAttributes().getNamedItem("name").getNodeValue());
			sess.save(field);
			
			template.getFields().add(field);
		}
		
		nodeList = doc.getElementsByTagName("constraints");
		fields   = nodeList.item(0);
		kids     = fields.getChildNodes();
		
		for(int i=0;i<kids.getLength();i++) {
			Node kid    = kids.item(i);
			String name = kid.getNodeName();
			attribs     = kid.getAttributes();
			
			Clause c    = null;
			
			// There's gotta be something out there that takes
			// <foo a="1" b="2"/> and constructs a Foo and calls
			// setA() and setB() on it
			if("otherField".equals(name)) {
				JoinClause cc = new JoinClause();
				cc.setChildColumn(attribs.getNamedItem("field").getNodeValue());
				cc.setParentColumn(attribs.getNamedItem("otherField").getNodeValue());
				c = cc;
			} else if("constant".equals(name)) {
				ConstantClause cc = new ConstantClause();
				cc.setColumn(attribs.getNamedItem("field").getNodeValue());
				cc.setOperatorId(Integer.parseInt(attribs.getNamedItem("operatorId").getNodeValue()));
				cc.setConstant(attribs.getNamedItem("constant").getNodeValue());
				c = cc;
			} else if("param".equals(name)) {
				ParamClause cc = new ParamClause();
				cc.setColumn(attribs.getNamedItem("field").getNodeValue());
				cc.setOperatorId(Integer.parseInt(attribs.getNamedItem("operatorId").getNodeValue()));
				cc.setParam(attribs.getNamedItem("parameter").getNodeValue());
				c = cc;
			} else if("staticDropdown".equals(name)) {
				StaticDropdownClause cc = new StaticDropdownClause();
				cc.setColumn(attribs.getNamedItem("field").getNodeValue());
				cc.setOperatorId(Integer.parseInt(attribs.getNamedItem("operatorId").getNodeValue()));
				cc.setOptions(attribs.getNamedItem("options").getNodeValue());
				c = cc;
			} else if("queryDropdown".equals(name)) {
				QueryDropdownClause cc = new QueryDropdownClause();
				cc.setColumn(attribs.getNamedItem("field").getNodeValue());
				cc.setOperatorId(Integer.parseInt(attribs.getNamedItem("operatorId").getNodeValue()));
				
				List l = sess.createCriteria(StoredQuery.class).add(Expression.eq("name", attribs.getNamedItem("query").getNodeValue())).list();
						
				if(l != null && l.size() != 0) {
					StoredQuery sq = (StoredQuery) l.get(0);
					cc.setQuery(sq);
				}
				
				c = cc;
			} else if("enumDropdown".equals(name)) {
				EnumDropdownClause cc = new EnumDropdownClause();
				cc.setColumn(attribs.getNamedItem("field").getNodeValue());
				cc.setOperatorId(Integer.parseInt(attribs.getNamedItem("operatorId").getNodeValue()));
				cc.setClassName(attribs.getNamedItem("class").getNodeValue());
				c = cc;
			}
			
			c.setTemplateId(template.getTemplateId());
			sess.save(c);
			template.getClauses().add(c);
		}
		
		nodeList = doc.getElementsByTagName("groupBy");
		fields   = nodeList.item(0);
		kids     = fields.getChildNodes();
		
		for(int i=0;i<kids.getLength();i++) {
			Node kid    = kids.item(i);
			attribs     = kid.getAttributes();

			TemplateGroupBy qgb = new TemplateGroupBy();
			qgb.setTemplateId(template.getTemplateId());
			qgb.setFieldName(attribs.getNamedItem("name").getNodeValue());
			
			sess.save(qgb);
			template.getGroupBy().add(qgb);
		}
		
		sess.merge(template);
		dao.commitTransaction();
		
		return template.getTemplateId();
		
		/*
		<query>
		  <fields>
		    <field name='com.freshdirect.listadmin.db.QueryTable.tableName'/>
		    <field name='com.freshdirect.listadmin.db.QueryClause.queryId'/>
		    <field name='minity(fun)'/>
		  </fields>
		  <orderBy>
		    <field name='com.freshdirect.listadmin.db.QueryTable.tableName'/>
		    <field name='com.freshdirect.listadmin.db.QueryClause.queryId'/>
		    <field name='function(param1.param2)'/>
		  </orderBy>
		  <constraints>
		    <constant field='com.freshdirect.listadmin.db.QueryTable.queryTableId' constant='constant value: 89'/>
		    <otherField field='com.freshdirect.listadmin.db.QueryTable.queryTableId' otherField='com.freshdirect.listadmin.db.QueryClause.parentColumn'/>
		  </constraints>
		</query>
		*/
	}

	// When we're editing it's too hard to figure out what was in the old version and what 
	// should be in the new... so we just delete everything and start fresh.
	private void cleanTemplate() {
		if(template.getName() != null) {
			ListadminDao dao  = ListadminDaoFactory.getInstance().getListadminDao();
			Session      sess = dao.currentSession();

			for(Iterator it=template.getFields().iterator();it.hasNext();) {
				sess.delete(it.next());
			}
			template.getFields().clear();
			
			for(Iterator it=template.getGroupBy().iterator();it.hasNext();) {
				sess.delete(it.next());
			}
			template.getGroupBy().clear();
			
			for(Iterator it=template.getClauses().iterator();it.hasNext();) {
				sess.delete(it.next());
			}
			template.getClauses().clear();
		}		
	}

	public List getAllFields() {
		return allFields;
	}


	public void setAllFields(List allFields) {
		this.allFields = allFields;
	}


	public List getConstraints() {
		return constraints;
	}


	public void setConstraints(List constraints) {
		this.constraints = constraints;
	}


	public Template getTemplate() {
		if(template == null) {
			template = new Template();
		}
		return template;
	}


	public void setTemplate(Template template) {
		this.template = template;
	}	
}
