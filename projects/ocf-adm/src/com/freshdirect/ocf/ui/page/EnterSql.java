package com.freshdirect.ocf.ui.page;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.hibernate.LockMode;
import org.hibernate.Session;

import com.freshdirect.ocf.core.Flight;
import com.freshdirect.ocf.core.OcfDaoFactory;
import com.freshdirect.ocf.impl.hibernate.FlightDao;


public abstract class EnterSql extends AppPage implements IExternalPage {
	
	private String name;
	private String description;
	private String query;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	
	public void activateExternalPage(Object[] parameters, IRequestCycle cycle) {
		IPage page = cycle.getPage("FlightEditor");
		Flight flight = (Flight)page.getProperty("flight");
		setFlight(flight);
		//flight = reload((Flight) page.getProperty("flight"));
	}
	
	public void saveQuery(IRequestCycle cycle) {
		FlightDao dao = OcfDaoFactory.getInstance().getFlightDao();
		Session sess = dao.currentSession();
		
		dao.beginTransaction();
		Flight flight = getFlight();
		if(flight.getListGenerator().getId() != null) {
			sess.merge(flight);
		}
		sess.save(flight.getListGenerator());
		dao.commitTransaction();
		
		IPage page = cycle.getPage("FlightEditor");
		page.setProperty("flight",flight);
	}
	
	public abstract Flight getFlight();
	public abstract void setFlight(Flight flight);
}
