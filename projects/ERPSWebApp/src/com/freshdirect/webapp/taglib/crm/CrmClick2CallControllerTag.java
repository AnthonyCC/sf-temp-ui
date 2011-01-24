package com.freshdirect.webapp.taglib.crm;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmClick2CallModel;
import com.freshdirect.crm.CrmClick2CallTimeModel;
import com.freshdirect.enums.WeekDay;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class CrmClick2CallControllerTag extends AbstractControllerTag {

	@Override
	protected boolean performAction(HttpServletRequest request,
			ActionResult actionResult) throws JspException {
		// TODO Auto-generated method stub
		CrmClick2CallModel click2CallModel = new CrmClick2CallModel();
//		CrmAgentModel agent =CrmSession.getCurrentAgent(request.getSession());
		String agentId =CrmSession.getCurrentAgentStr(request.getSession());
		try {
			if(null != request.getParameter("cancel") || null !=request.getParameter("edit")){
				click2CallModel = getClick2CallInfo(actionResult);
				if(null !=request.getParameter("edit")){
					if(!click2CallModel.isStatus()){
						pageContext.setAttribute("mode", "EDIT");
					}
				}
			}else if(null !=request.getParameter("saveStatus")){
				click2CallModel = getClick2CallInfo(actionResult);
				if(null != click2CallModel){
					String id =click2CallModel.getId();
					boolean status = click2CallModel.isStatus();
					CallCenterServices.saveClick2CallStatus(id, agentId, !status);
					click2CallModel.setUserId(agentId);
					click2CallModel.setStatus(!status);
				}				
			}else{		
				click2CallModel.setUserId(agentId);
				String eligibleCustType = request.getParameter("eligibleCustType");
				StringBuffer eligibleCustomers = new StringBuffer();
				
				if("selected".equalsIgnoreCase(eligibleCustType)){
					boolean isEligibleCustSelected = false;
					String chefstable = request.getParameter("chefsTable");
					if(null != chefstable){		
						
						String ctDlvPass = request.getParameter("ct_dp");
						if(null !=ctDlvPass){
							isEligibleCustSelected = true;
							eligibleCustomers.append("ct_dp").append(",");
						}
						String ctNonDlvPass = request.getParameter("ct_ndp");
						if(null !=ctNonDlvPass){
							isEligibleCustSelected = true;
							eligibleCustomers.append("ct_ndp").append(",");
						}
					}
					
					String nonChefsTable = request.getParameter("nonChefsTable");
					if(null !=nonChefsTable){
						
						String nCtDlvPass = request.getParameter("nct_dp");
						if(null !=nCtDlvPass){
							isEligibleCustSelected = true;
							eligibleCustomers.append("nct_dp").append(",");
						}
						String nCtNonDlvPass = request.getParameter("nct_ndp");
						if(null !=nCtNonDlvPass){
							isEligibleCustSelected = true;
							eligibleCustomers.append("nct_ndp").append(",");
						}
					}
					if(!isEligibleCustSelected){
						actionResult.addError(true, "noSelectedCustomers", "Please select atleast one segment for Eligible Customers.");
					}
				}else{
					eligibleCustomers.append(eligibleCustType);
				}
				click2CallModel.setEligibleCustomers(eligibleCustomers.toString());
				WeekDay weekNames[] = WeekDay.values();
				CrmClick2CallTimeModel[] timeModel = new CrmClick2CallTimeModel[weekNames.length];
				for (WeekDay weekName2 : weekNames) {
					String wkName = weekName2.name();
					timeModel[weekName2.ordinal()] = new CrmClick2CallTimeModel();
					timeModel[weekName2.ordinal()].setDayName(wkName);
					timeModel[weekName2.ordinal()].setShow((null!=request.getParameter(wkName.toLowerCase()+"Show")?true:false));
					timeModel[weekName2.ordinal()].setStartTime(request.getParameter(wkName.toLowerCase()+"Start"));
					timeModel[weekName2.ordinal()].setEndTime(request.getParameter(wkName.toLowerCase()+"End"));
				}
				click2CallModel.setDays(timeModel);
					
				String nextDayTimeSlot = request.getParameter("nextDayTimeSlot");
				if(null !=nextDayTimeSlot){
				click2CallModel.setNextDayTimeSlot(("Yes".equalsIgnoreCase(nextDayTimeSlot)?true:false));
				}
				
				String[] selectedZones = request.getParameterValues("selectedZones");
				if(null == selectedZones || selectedZones.length==0){
					actionResult.addError(true, "noSelectedZones", "Please select atleast one Delivery Zone.");
				}
				/*if(null != selectedZones && selectedZones.length>0){
					StringBuffer zoneIdBuffer = new StringBuffer();
					for(int i=0;i<selectedZones.length-1;i++){
						zoneIdBuffer.append(selectedZones[i]);
						zoneIdBuffer.append(",");
					}
					zoneIdBuffer.append(selectedZones[selectedZones.length-1]);
					click2CallModel.setDeliveryZones(zoneIdBuffer.toString());			
				}*/
				click2CallModel.setDeliveryZones(selectedZones);
		//		selectedZones.toString();
				
		//		request.getParameter("selectedZones");
				if(actionResult.isSuccess()){
				click2CallModel.setStatus(true);
				CallCenterServices.saveClick2CallInfo(click2CallModel);
				}else{
					pageContext.setAttribute("mode", "EDIT");
				}
		 
				
			}
		}catch (FDResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageContext.setAttribute("click2CallInfo",click2CallModel);
		pageContext.setAttribute("availableDeliveryZones", getAvailableDeliveryZones(actionResult));
		return true;
	}
	
	private List getAvailableDeliveryZones(ActionResult actionResult){
	
		try {
			return FDDeliveryManager.getInstance().getActiveZoneCodes();
		} catch (FDResourceException e) {
			actionResult.addError(true, "getAvailableDlvZonesError", " Failed to get the available delivery zones.");
			return Collections.EMPTY_LIST;
		}
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		   public VariableInfo[] getVariableInfo(TagData data) {
		        return new VariableInfo[] {
		            new VariableInfo(data.getAttributeString("id"),
		                            "java.util.List",
		                            true,
		                            VariableInfo.NESTED),
						            new VariableInfo(data.getAttributeString("result"),
			                            "com.freshdirect.framework.webapp.ActionResult",
			                            true,
			                            VariableInfo.NESTED)             
		        };

		    }
	}
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		CrmClick2CallModel click2CallModel = getClick2CallInfo(actionResult);
		pageContext.setAttribute("click2CallInfo",click2CallModel);
		pageContext.setAttribute("availableDeliveryZones", getAvailableDeliveryZones(actionResult));
		return true;
	}

	private CrmClick2CallModel getClick2CallInfo(ActionResult actionResult) {
		try {
			CrmClick2CallModel click2CallModel =CallCenterServices.getClick2CallInfo();
			return click2CallModel;
		} catch (FDResourceException e) {
			actionResult.addError(true, "getClick2CallInfo", " Failed to get the saved Click 2 Call Campaign information.");
			return null;
		}
	}

}
