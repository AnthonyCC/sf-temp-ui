package com.freshdirect.crm;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;


public class CrmLateIssueModel extends ModelSupport {
	
	private String route;
	private Date deliveryDate;
	private String agentUserId;
	private Date reportedAt;
	private String reportedBy;
	private String comments;
	private String delivery_window;
	private int delayMinutes;
	private String reportedStopsText;
	private String actualStopsText;
	private TreeMap stopsAndOrders;
	private int actualStopCount;
	
	public CrmLateIssueModel() {
		super();
	}
	
	public CrmLateIssueModel(PrimaryKey pk) {
		this();
		this.setPK(pk);
	}
	
	public String getReportedStopsText() {
		return this.reportedStopsText;
	}
	
	public void setReportedStopsText(String txtStops){
			this.reportedStopsText = NVL.apply(txtStops,"").trim();
	}
	
	public String getActualStopsText() {
		return this.actualStopsText;
	}
	
	public void setActualStopsText(String txtStops){
		this.actualStopsText = NVL.apply(txtStops,"").trim();
	}
	
	public int getDelayMinutes() {
		return this.delayMinutes;
	}
	public void setDelayMinutes(int delayMinutes) {
		this.delayMinutes = delayMinutes;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public Date getDeliveryDate() {
		return this.deliveryDate;
	}
	
	public String getAgentUserId() {
		return agentUserId;
	}
	
	public void setAgentUserId(String agentUserName) {
		this.agentUserId = agentUserName;
	}

	public Date getReportedAt() {
		return reportedAt;
	}
	
	public void setReportedAt(Date reportedAt) {
		this.reportedAt = reportedAt;
	}
	
	public String getReportedBy() {
		return reportedBy;
	}
	
	public void setReportedBy(String reportedBy) {
		this.reportedBy = reportedBy;
	}
	
	public String getRoute() {
		return route;
	}
	
	public void setRoute(String route) {
		if (route==null || "".equals(route.trim())) {
			throw new IllegalArgumentException("route cannot be null or empty string");
		}
		this.route = route;
	}
	
	public Map getStopsAndOrders() {
		return stopsAndOrders==null 
		  ? Collections.EMPTY_MAP 
		  : Collections.unmodifiableMap(stopsAndOrders);
	}
			
	public void setStopsAndOrders(TreeMap mStopsAndOrders) {
		this.stopsAndOrders = mStopsAndOrders;
		
	}

	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getDelivery_window() {
		return this.delivery_window;
	}
	
	public void setDelivery_window(String delivery_window) {
		this.delivery_window = delivery_window;
	}
	
	public void setStopAndOrder(int stopNumber, String saleId) {
		this.setStopAndOrder( new Integer(stopNumber),saleId);
	}
	
	public void setStopAndOrder(Integer stopNumber, String saleId) {
		stopsAndOrders.put(stopNumber,saleId);
	}
	
	public int getActualStopCount() {
		return this.actualStopCount;
	}
	
	public void setActualStopCount(int stopCount) {
		this.actualStopCount = stopCount;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (getPK()!=null) sb.append(getPK());
		sb.append("LateIssue[\n  Route:").append(route);
		sb.append("\n Stop Text: ").append(reportedStopsText);
		sb.append("\n Dlv Date: ").append(SimpleDateFormat.getDateTimeInstance().format(deliveryDate));
		sb.append("\n Dlv Window: ").append(delivery_window);
		sb.append("\n reported At: ").append(SimpleDateFormat.getDateTimeInstance().format(reportedAt));
		sb.append("\n reported By: ").append(reportedBy);
		sb.append("\n delay Minutes: ").append(delayMinutes);
		sb.append("\n comments: ").append(comments);
		sb.append("\n Sale Id's:");
		if (stopsAndOrders!=null) {
			for (Iterator i = stopsAndOrders.keySet().iterator(); i.hasNext();) {
				Integer stopNumber = (Integer) i.next();
				sb.append("\n    Stop#: ").append(stopNumber).append(" Sale id: ").append(stopsAndOrders.get(stopNumber));
			}
		}
		sb.append("\n]");
		return  sb.toString();
	}
	
	public boolean expandReportedStops(boolean validateOnly){
		boolean expansionResult =true;
		if (this.reportedStopsText!=null && !"".equals(this.reportedStopsText)) {
	       TreeMap stopMap = new TreeMap();
	       expansionResult = StopsTextToStopNumber(this.reportedStopsText,stopMap);
	       if (validateOnly && stopMap.size() > 120) expansionResult=false;
	       if (!validateOnly && stopMap.size()>0) {
	       		this.setStopsAndOrders(stopMap);
	       }
		}
		return expansionResult;
	}
	
	public boolean expandActualStops(boolean validateOnly){
		boolean expansionResult = true;
		if (this.actualStopsText!=null && !"".equals(this.actualStopsText)) {
	       TreeMap stopMap = new TreeMap();
	       expansionResult = StopsTextToStopNumber(this.actualStopsText,stopMap);
	       if (validateOnly && stopMap.size() > 120) expansionResult=false;
	       if (!validateOnly && stopMap.size()>0) {
	       		this.setActualStopCount(stopMap.size());
	       }
		}
		return expansionResult;
	}

	private boolean StopsTextToStopNumber(String stopText, Map stopMap){
		boolean allOk = true;
		if (stopText!=null  && !"".equals(stopText)) {
		   String newStops = stopText.toLowerCase().replaceAll(" thru ","-").replaceAll(" - ","-");
	       String[] splitString  = newStops.split("[,;[ ]]");
	       int x=0;
	       for (int i = 0; i< splitString.length && allOk;i++) {
	       		try {
	       			if("".equals(splitString[i])) continue;
	       			if (isInteger(splitString[i])) {
	       				stopMap.put(new Integer(Math.abs(Integer.parseInt(splitString[i]))),null);
	       			} else {
		       			String splitRange[] = splitString[i].split("[-]");
		       			if (splitRange.length==2  && isInteger(splitRange[0]) && isInteger(splitRange[1])) {
		       				int startStop = Math.min(Math.abs(Integer.parseInt(splitRange[0])),Math.abs(Integer.parseInt(splitRange[1])));
		       				int endStop = Math.max(Math.abs(Integer.parseInt(splitRange[0])),Math.abs(Integer.parseInt(splitRange[1])));
		       				for (int j=startStop; j<= endStop;j++){
		       					stopMap.put(new Integer(j),null);
		       				}
		       			} else {
		       				allOk = false;
		       			}
	       			}
	       		} catch (NumberFormatException nfe) {
	       			allOk=false;
	       		}
	       }
		}
		return allOk;
	}
	
	private boolean isInteger(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (NumberFormatException nfe ) {
			return false;
		}catch (NullPointerException nfe) {
			return false;
		}
	}
}
