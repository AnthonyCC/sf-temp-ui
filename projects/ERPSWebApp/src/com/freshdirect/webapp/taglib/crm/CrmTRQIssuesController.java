
package com.freshdirect.webapp.taglib.crm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;


public class CrmTRQIssuesController extends AbstractControllerTag {
	private int formsOnPage=1;
	private List caseList=new ArrayList();
	private String id;
	private String action;
	private String saleIdName;
	private boolean allBlank = true;
	
	private String erpCustomerId=null;
	private String saleId = null;
	private String strStopNumber=null;
	private String route=null;

	private List casesForStop = Collections.EMPTY_LIST;
	
	// OUT
	public void setId(String id) {
		this.id = id;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	
	private void setAllBlank(boolean flag){
		this.allBlank=flag;
	}

	private boolean isAllBlank() {
		return allBlank;
	}

	// OUT
	public void setSaleId(String saleIdName) {
		this.saleIdName = saleIdName;
	}

	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		if ("getCases".equalsIgnoreCase(this.action)) {
			validateCommonStuff(request,actionResult);
			casesForStop = getTRQCasesForSale(saleId,actionResult);
			pageContext.setAttribute(this.id, casesForStop);
			if (this.saleIdName != null && saleId != null) {
				pageContext.setAttribute(this.saleIdName, saleId);
			}
		}
		return true;
	}
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
			String sFormsOnPage = request.getParameter("formsOnPage");
			if (sFormsOnPage!=null) {
				try {
					formsOnPage = Integer.parseInt(sFormsOnPage);
				} catch (NumberFormatException nfe){
					//eating this exception....	
				}
			}

			validateCommonStuff(request,actionResult);

			if (actionResult.isSuccess() && "storeCases".equals(this.action)){
				processForms(request, actionResult,casesForStop);
				
				if(actionResult.isSuccess()) {
					if (caseList.size()==0 && isAllBlank()) {
						actionResult.addError(new ActionError("blank", "You must fill out at least one issue."));
					} else if(caseList.size()==0)	{
						actionResult.addError(new ActionError("noChanges", "You made no modifications to any of the rows.<br>If you would like to leave this screen without saving, click the cancel button."));
					} else {
						storeCases(actionResult);
					}
				}
			}
			
			if (this.id!=null) {
				pageContext.setAttribute(this.id, casesForStop);
			}
			if (this.saleIdName != null && saleId != null) {
				pageContext.setAttribute(this.saleIdName, saleId);
			}
			return true;
	}
	
	
	
	private void validateCommonStuff(HttpServletRequest request, ActionResult result) {
		DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy");
		String stopNumber = NVL.apply(request.getParameter("stopNumber"),"");
		String frmDlvDate = NVL.apply(request.getParameter("dlvDate"),"");
		route = NVL.apply(request.getParameter("route"),"");
		
		Date dlvDate=null;

		result.addError("".equals(route),      "route", "Route is missing");
		result.addError("".equals(stopNumber), "stopNumber","Stop number is missing");
		
		try {
			dlvDate=df2.parse(frmDlvDate);
		} catch (ParseException pe) {
			result.addError(new ActionError("dlvDate","missing or invalid delivery date"));
		}
		
		
		if (!"".equals(stopNumber)) {
			try {
				strStopNumber = StringUtil.leftPad(""+Integer.parseInt(stopNumber),5,'0');
			} catch (NumberFormatException nfe) {
				result.addError(new ActionError("stopNumber", "Invalid value for stop number"));
			}
		}
		
		if (dlvDate!=null && !"".equals(route) && strStopNumber!=null) {
			try {
				List saleInfos =  FDCustomerManager.getOrdersByTruck(StringUtil.leftPad(route,6,'0'),dlvDate);
				for (Iterator itr = saleInfos.iterator(); itr.hasNext() && erpCustomerId==null;) {
					DlvSaleInfo dsi = (DlvSaleInfo) itr.next();
					if (strStopNumber.equals(dsi.getStopSequence())) {
						erpCustomerId = dsi.getErpCustomerId();
						saleId = dsi.getSaleId();
					}
				}
				result.addError((saleId==null ||erpCustomerId==null) ,  "route","Unable to find an order assigned to the specifed route/stop.");
			} catch (FDResourceException fdre) {
				fdre.printStackTrace();
				result.addError(new ActionError("technical_error",SystemMessageList.MSG_TECHNICAL_ERROR));
			}
		}
		
		if (result.isFailure()) {
			return;
		}
	}
	
	// @return List<CrmCaseModel>
	private List getTRQCasesForSale(String saleId,ActionResult actionResult) { 
		if (saleId!=null) {
			CrmCaseTemplate template = new CrmCaseTemplate();
			template.setSalePK(new PrimaryKey(saleId));

			try {
				List trqCases = Collections.EMPTY_LIST;
				List cases=CrmSession.findCases(this.pageContext.getSession(), template);
				//remove items that are not in the transportation que, and are closed
				if (cases.size()>0) {
					trqCases = new ArrayList();
					CrmManager cmgr = CrmManager.getInstance();
					for (Iterator itr = cases.iterator(); itr.hasNext();) {
						CrmCaseModel cm = (CrmCaseModel)itr.next();
						if (CrmCaseState.CODE_CLOSED.equals(cm.getState().getCode()) /* || 
							!CrmCaseQueue.CODE_DSQ.equals(cm.getSubject().getQueue().getCode()) */) { //Changed to replace obsolete queue TRQ
							itr.remove();
						} else {
							trqCases.add(cmgr.getCaseByPk(cm.getPK().getId()));
						}
					}
				}
				return trqCases;
			} catch (FDResourceException fdre) {
				fdre.printStackTrace();
				actionResult.addError(new ActionError("technical_error",SystemMessageList.MSG_TECHNICAL_ERROR));
			}
		}
		return Collections.EMPTY_LIST;
	}

	private void processForms(HttpServletRequest request, ActionResult result, List cases) {		
		String frmSubject;		
		String frmNote;
		String frmReported;
		String frmActual;
		String frmPK;
		String frmCtNumbers; // carton numbers assigned to case
		int iReported=0;
		int iActual=0;
		
		CrmCaseSubject caseSubject ;
		CrmManager cmgr = null;
		CrmCaseInfo caseInfo=null ;
		CrmCaseModel crmCase=null ;

		try {
			cmgr = CrmManager.getInstance();
		} catch (FDResourceException fdre) {
			fdre.printStackTrace();
			result.addError(new ActionError("technical_error",SystemMessageList.MSG_TECHNICAL_ERROR+" Getting CrmManager instance."));
			return;
		}
		
		for (int i=0; i<formsOnPage; i++) {
			
			String suffix = formsOnPage>1 ? "_"+i : "";
			
			frmSubject=NVL.apply(request.getParameter("subject"+suffix),"");
			frmNote=NVL.apply(request.getParameter("note"+suffix),"").trim();
			frmReported=NVL.apply(request.getParameter("reported"+suffix),"0");
			frmActual=NVL.apply(request.getParameter("actual"+suffix),"0");
			frmPK = NVL.apply(request.getParameter("PK"+suffix),"");
			frmCtNumbers = NVL.apply(request.getParameter("cartonNumbers"+suffix),"");

			//if notes or reported amount or acutal amount is not empty string then process it
			if ("".equals(frmNote) && "".equals(frmReported)  && "".equals(frmActual)) {
			 	continue; // skip nothing entered
			}
			setAllBlank(false); 
			caseSubject = CrmCaseSubject.getEnum(frmSubject);

			result.addError("".equals(frmNote),    "note"+suffix,"Note for case is missing");
			
			if(caseSubject==null) {
				result.addError(caseSubject==null, "subject"+suffix,"Invalid subject code was specified");
			}
			 
			if (!"".equals(frmReported)) {
				try {
					iReported = Integer.parseInt(frmReported); 
				} catch (NumberFormatException nfe) {
					result.addError(new ActionError("reported"+suffix, "Invalid value for reported amount"));
				}
			} else {
				iReported=0; 
			}
			
			if (!result.hasError("reported"+suffix)) {
				result.addError(iReported<0,  "reported"+suffix,"Reported amount cannot be negative.");
			}
			
			if (!"".equals(frmActual)) {
				try {
					iActual = Integer.parseInt(frmActual); 
				} catch (NumberFormatException nfe) {
					result.addError(new ActionError("actual"+suffix, "Invalid value for actual amount"));
				}
			} else {
				iActual=0;
			}
			
			result.addError(iActual<0,    "actual"+suffix,"Actual amount cannot be negative.");


			/*
			 * [APPREQ-498] get carton numbers
			 */
			List cartonNumbers = new ArrayList();
			if (!"".equals(frmCtNumbers)) {
				// retrieve carton numbers freezed to string
				StringTokenizer st = new StringTokenizer(frmCtNumbers, ";");
				while (st.hasMoreElements()) {
					cartonNumbers.add(st.nextElement());
				}
			}

			result.addError(caseSubject.isCartonsRequired() && cartonNumbers.size() == 0, "carton"+suffix, "Cartons must be assigned." );
			

			if (result.isSuccess()) {
				caseInfo=null ;
				crmCase=null ;
				if (!"".equals(frmPK)) {
					try {
						CrmCaseTemplate template = new CrmCaseTemplate();
						template.setPK(new PrimaryKey(frmPK));
						List foundCases=cmgr.findCases(template);
						if (foundCases.size()==1) {
							caseInfo = ((CrmCaseModel) foundCases.get(0)).getCaseInfo();
						}
						if (caseInfo !=null) {
							if ( (iActual+"").equals(request.getParameter("origAQty_"+frmPK))
								&& (iReported+"").equals(request.getParameter("origRQty_"+frmPK))
								&& frmNote.equals(request.getParameter("origNote_"+frmPK)) ){
								continue;  //nothing changed for this...so skip it.
							}
						}
					} catch (FDResourceException fdre) {
						fdre.printStackTrace();
						result.addError(new ActionError("technical_error",SystemMessageList.MSG_TECHNICAL_ERROR+" getting case for case PK: "+frmPK));
						return;
					}
					
				} 
				
				if (caseInfo!=null) {
					caseInfo.setProjectedQuantity(iReported);
					caseInfo.setActualQuantity(iActual);
					caseInfo.setAssignedAgentPK(this.getCurrentAgent().getPK());
				} else {
					caseInfo = new CrmCaseInfo();
					caseInfo.setProjectedQuantity(iReported);
					caseInfo.setActualQuantity(iActual);
					crmCase = new CrmCaseModel(caseInfo);
				
					caseInfo.setOrigin(CrmCaseOrigin.getEnum(CrmCaseOrigin.CODE_PHONE));
					caseInfo.setState(CrmCaseState.getEnum(CrmCaseState.CODE_OPEN));
					caseInfo.setSubject(caseSubject);
					caseInfo.setPriority(caseSubject.getPriority());
					caseInfo.setSummary("Issue for Route: "+route
						+" and Stop: "+strStopNumber+"; "
						+caseSubject.getName() );
					caseInfo.setAssignedAgentPK(this.getCurrentAgent().getPK());
					caseInfo.setCustomerPK(new PrimaryKey(erpCustomerId));
					caseInfo.setSalePK(new PrimaryKey(saleId));
				}

				
				crmCase = new CrmCaseModel(caseInfo);
				
				if (frmPK!=null ) {
					crmCase.setPK(new PrimaryKey(frmPK));
				}
			
				CrmCaseAction caseAction = new CrmCaseAction();
				caseAction.setType(CrmCaseActionType.getEnum(CrmCaseActionType.CODE_NOTE));
				caseAction.setTimestamp(new Date());
				caseAction.setAgentPK(this.getCurrentAgent().getPK());
				caseAction.setNote(frmNote);
				crmCase.addAction(caseAction);

				/*
				 * [APPREQ-498] assign carton numbers
				 */
				crmCase.setCartonNumbers(cartonNumbers);

				caseList.add(crmCase);
			}
		}
		
	}
	
	private CrmAgentModel getCurrentAgent() {
		return CrmSession.getCurrentAgent(pageContext.getSession());
	}

	private void storeCases(ActionResult result){
		CrmCaseModel crmCase=null;
		try {
			CrmManager crmManager = CrmManager.getInstance();
			for (Iterator itr = caseList.iterator(); itr.hasNext();) {
				crmCase = (CrmCaseModel) itr.next();
				if (crmCase.getPK()==null) {
					crmManager.createCase(crmCase);
				} else {
					crmManager.unlockCase(crmCase.getPK());
					boolean iLocked = crmManager.lockCase(crmCase.getAssignedAgentPK(),crmCase.getPK());
					if (iLocked) {
						crmManager.updateCase(crmCase.getCaseInfo(),(CrmCaseAction)crmCase.getActions().get(0),crmCase.getAssignedAgentPK());
						crmManager.unlockCase(crmCase.getPK());
					}
				}
			}
		} catch (CrmAuthorizationException ae) {
			ae.printStackTrace();
			result.addError(new ActionError("technical_error",SystemMessageList.MSG_TECHNICAL_ERROR+" Autorization exception on case id:"+crmCase.getPK().getId()));
		} catch (FDResourceException fdre) {
			fdre.printStackTrace();
			result.addError(new ActionError("technical_error",SystemMessageList.MSG_TECHNICAL_ERROR));
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
                    VariableInfo.NESTED),
	            new VariableInfo(data.getAttributeString("saleId"),
                    "java.lang.String",
                    true,
                    VariableInfo.NESTED)             
	        };

	    }
	}	
	
}
