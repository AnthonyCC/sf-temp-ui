package com.freshdirect.ocf.ui.page;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;

import com.freshdirect.listadmin.db.Clause;
import com.freshdirect.listadmin.db.DateTimeClause;
import com.freshdirect.listadmin.db.StoredQuery;
import com.freshdirect.listadmin.nvp.NVPI;
import com.freshdirect.listadmin.ui.page.ConfigureQuery;
import com.freshdirect.ocf.core.DbListGenerator;
import com.freshdirect.ocf.core.Flight;
import com.freshdirect.ocf.core.OcfManager;

public abstract class FillInTemplate extends ConfigureQuery {
	
	public void activateExternalPage(Object[] args, IRequestCycle cycle) {
		IPage page = cycle.getPage("FlightEditor");
		setFlight(reload((Flight) page.getProperty("flight")));
		
		setupStoredQuery();
	}
	
	private final static String instead_null = "Intentionally left blank";
	
	public void saveQuery(IRequestCycle cycle) {
		IPage page = cycle.getPage("FlightEditor");
		setFlight((Flight) page.getProperty("flight"));
		getFlight().getListGenerator().setStoredQuery(getStoredQuery());
		getFlight().getListGenerator().setName(getStoredQuery().getName());
		//XXX: could be null in this case, but the mappings do not allow it to be.
		getFlight().getListGenerator().setQuery(instead_null);
		getFlight().getListGenerator().setDescription(instead_null);

		page.setProperty("flight", getFlight());
	}
	
	/**
	 * TODO: dup from EnterSql, refactor!
	 */
	private Flight reload(Flight flight) {
		if(flight.getId() != null) {
			flight = OcfManager.getInstance().getFlight(flight.getId());
		}
		
		return flight;
	}

	public abstract Flight getFlight() ;

	public abstract void setFlight(Flight flight) ;
	
	public String getFlightId() {
		if(getFlight() == null) {
			return null;
		}
		
		return getFlight().getId();
	}
	
	public void setFlightId(String id) {
		if(id != null) {
			setFlight(OcfManager.getInstance().getFlight(id));
		}
		setupStoredQuery();
	}

	private void setupStoredQuery() {
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		
		if(getFlight() != null) {
			DbListGenerator dbl = getFlight().getListGenerator();
			if(dbl != null) {
				StoredQuery sq = dbl.getStoredQuery();
				
				if(sq != null) {
					setStoredQuery(sq);
					setTemplate(sq.getTemplate());
					
					// Copy saved values from the queryValues collection 
					// back to the params
					Map m = new HashMap();
					for(Iterator it=sq.getValues().iterator();it.hasNext();) {
						NVPI nvp = (NVPI) it.next();
						m.put(nvp.getName(),nvp.getValue());
					}
					
					try {
						for(Iterator it=sq.getTemplate().getAllClauses().iterator();it.hasNext();) {
							Clause c = (Clause) it.next();
							
							if(c instanceof DateTimeClause) {
								Date d = null;
								try {
									d = df.parse((String) m.get(c.getClauseId()));
								} catch (Exception e) {
									
								}
								c.setRuntimeValue(d);
							} else {
								c.setRuntimeValue(m.get(c.getClauseId()));
							}
						}
					} catch (ParseException e) {}
				}
			}
		}
	}
}
