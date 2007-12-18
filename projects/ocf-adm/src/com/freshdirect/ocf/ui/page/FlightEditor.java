/**
 * @author ekracoff
 * Created on Jun 8, 2005*/

package com.freshdirect.ocf.ui.page;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ExternalServiceParameter;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.hibernate.Session;
import org.quartz.SchedulerException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.hibernate.HibernateDaoSupport;
import com.freshdirect.framework.hibernate.UnitOfWork;
import com.freshdirect.framework.util.JndiWrapper;
import com.freshdirect.listadmin.db.StoredQuery;
import com.freshdirect.ocf.core.ActionI;
import com.freshdirect.ocf.core.CaseAction;
import com.freshdirect.ocf.core.DbListGenerator;
import com.freshdirect.ocf.core.Emailer;
import com.freshdirect.ocf.core.Flight;
import com.freshdirect.ocf.core.OcfDaoFactory;
import com.freshdirect.ocf.core.OcfManager;
import com.freshdirect.ocf.core.OcfTableI;
import com.freshdirect.ocf.core.ProfileAction;
import com.freshdirect.ocf.core.PromotionPopulator;
import com.freshdirect.ocf.impl.hibernate.ActionDao;
import com.freshdirect.ocf.impl.hibernate.FlightDao;

public abstract class FlightEditor extends AppPage implements IExternalPage {

	public void activateExternalPage(Object[] parameters, IRequestCycle cycle) {
		Flight flight;

		String campId = (String) parameters[0];
		String flightId = parameters.length == 2 ? (String) parameters[1] : null;

		if (flightId == null) {
			flight = new Flight();
			flight.setListGenerator(new DbListGenerator());
			flight.setCampaign(OcfManager.getInstance().getCampaign(campId));
		} else {
			flight = OcfManager.getInstance().getFlight(flightId);
		}
		
		setFlight(flight);
	}

	public void beginPageRender() {
		super.beginPageRender();
		if (getFlight() != null && getFlight().getId() != null) {
			System.err.println("FlightEditor.attach() " + getFlight());
			//HibernateSessionFactory.reattach(getFlight());
		}
	}

	public void saveFlight(IRequestCycle cycle) {
		System.err.println("FlightEditor.saveFlight()");
		final FlightDao dao = OcfDaoFactory.getInstance().getFlightDao();
		new UnitOfWork() {
			protected void perform() {
				dao.saveOrUpdateFlight(getFlight());
			}

			protected HibernateDaoSupport getDaoSupport() {
				return dao;
			}
		}.execute();
		
		throw new TapestryRedirectException(cycle, "CampaignEditor", new ExternalServiceParameter("CampaignEditor",new Object[]{getFlight().getCampaign().getId()}) );

	}

	public void cancelAction(IRequestCycle cycle) {

	}

	public void addAction(IRequestCycle cycle) {
		Class c = this.getActionType();
		if (c == null) {
			return;
		}
		ActionI action = (ActionI) newInstance(c);

		this.getFlight().addAction(action);
	}

	public void deleteAction(Integer ix) {
		Flight  flight  = getFlight();
		List    actions = flight.getActions();
		ActionI	action  = (ActionI) actions.get(ix.intValue()); 
		
		flight.removeAction(action);
		
		setFlight(flight);
	}
	
	public void previewList(IRequestCycle cycle) throws FDResourceException {
		OcfTableI table = getFlight().getListGenerator().getList();
		ListResults page = (ListResults) cycle.getPage("ListResults");
		page.setTable(table);
		cycle.activate((IPage) page);
	}

	private Object newInstance(Class klazz) {
		try {
			return klazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the columns in the query, which may be used in case or profile action patterns
	 * 
	 * @return
	 */
	public List getAvailableTokens() {
		List ret              = new ArrayList();
		
		// If this flight is running off a query then the only datasource it could
		// be using is the fdddatasource.
		String dataSourceName = "fddatasource"; 
		
		Flight flight  = getFlight();
		
		if(flight == null) {
			return ret;
		}
		
		DbListGenerator dbl = flight.getListGenerator();
		
		if(dbl == null) {
			return ret;
		}
		
		String sql     = "";
		StoredQuery sq = dbl.getStoredQuery();
		
		if(sq != null) {
			sql = sq.getSql();
			dataSourceName = sq.getDataSourceName();
		}
		
		if(sql == null) {
			sql = dbl.getQuery();
			
			if(sql != null) {
				sql = "select * from (" + sql + ")";
			}
		}
		
		if(sql == null || sql.trim().length() == 0) {
			return ret;
		}
		
		if(sql.endsWith(")")) {
			sql = sql + " WHERE 1=0";
		} else {
			sql = sql + " AND 1=0";
		}
		
		// TODO: This should really be elsewhere
		try {
			Connection conn = JndiWrapper.getConnection(dataSourceName);
			Statement st    = conn.createStatement();
			
			st.execute(sql);
			ResultSet rs         = st.getResultSet();
			ResultSetMetaData md = rs.getMetaData();
			int cols             = md.getColumnCount();
			
			for(int i=1;i<cols;i++) {
				ret.add(md.getColumnLabel(i));
			}
			
			rs.close();
			st.close();
			conn.close();
		} catch (Exception e) {
			System.err.println("Unable to get columns/tokens");
			e.printStackTrace(System.err);
		}
		return ret;
	}
	
	public IPropertySelectionModel getActionSelectionModel() {
		Map actions = new HashMap();
		actions.put("Send Email", Emailer.class);
		actions.put("Populate Promotion", PromotionPopulator.class);
		actions.put("Open Case", CaseAction.class);
		actions.put("Populate Profile", ProfileAction.class);
		return new LabelPropertySelectionModel(actions, true);
	}

	public IPropertySelectionModel getStoredQueriesSelectionModel() {
		Map actions = new HashMap();
		ActionDao dao = OcfDaoFactory.getInstance().getActionDao();
		Session sess = dao.currentSession();
		List list    = sess.createCriteria(StoredQuery.class).list();
		
		for(Iterator it=list.iterator();it.hasNext();) {
			StoredQuery sq = (StoredQuery) it.next();
			actions.put(sq.getName(),sq);
		}
		
		return new LabelPropertySelectionModel(actions, true);
	}
	
	public IPropertySelectionModel getScheduleSelectionModel() throws SchedulerException {
		String[] schedules = OcfManager.getInstance().getSchedules();
		return new StringPropertySelectionModel(schedules);
	}

	public abstract Class getActionType();

	public abstract void setActionType(Class klazz);

	public abstract void setFlight(Flight flight);

	public abstract Flight getFlight();

}
