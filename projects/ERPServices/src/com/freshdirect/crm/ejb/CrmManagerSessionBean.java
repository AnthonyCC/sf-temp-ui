package com.freshdirect.crm.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CrmAgentList;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseChangeAuditor;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOperation;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.crm.CrmCustomerHeaderInfo;
import com.freshdirect.crm.CrmLateIssueModel;
import com.freshdirect.crm.CrmStatus;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.deliverypass.ejb.DlvPassManagerHome;
import com.freshdirect.deliverypass.ejb.DlvPassManagerSB;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;



public class CrmManagerSessionBean extends SessionBeanSupport {
	
	private final static Category LOGGER = LoggerFactory.getInstance( CrmManagerSessionBean.class );
    
    private final static ServiceLocator LOCATOR = new ServiceLocator();
    private CrmAgentModel systemUser;
    
    protected String getResourceCacheKey() {
        return "com.freshdirect.crm.ejb.CrmManagerHome";
    }
    
    public void createAgent(CrmAgentModel agent, PrimaryKey userPk) throws FDResourceException, CrmAuthorizationException, ErpDuplicateUserIdException {
        try{
            CrmAgentHome home = this.getCrmAgentHome();
            CrmAgentEB eb = home.findByPrimaryKey(userPk);
            CrmAgentModel user = (CrmAgentModel)eb.getModel();
            if(!user.isAdmin()){
                throw new CrmAuthorizationException("You are not authorized to perform this action");
            }
            
            home.create(agent);
        }catch(DuplicateKeyException e){
            throw new ErpDuplicateUserIdException(e, "UserId or password already exists");
        } catch(CreateException e){
            throw new FDResourceException(e);
        }catch(RemoteException e){
            throw new FDResourceException(e);
        }catch(FinderException e){
            throw new FDResourceException(e);
        }
    }
    
    public void updateAgent(CrmAgentModel agent, PrimaryKey userPk) throws CrmAuthorizationException, FDResourceException {
        try{
            CrmAgentHome home = this.getCrmAgentHome();
            CrmAgentEB eb = home.findByPrimaryKey(userPk);
            CrmAgentModel user = (CrmAgentModel)eb.getModel();
            if(!user.isAdmin()){
                throw new CrmAuthorizationException("You are not authorized to perform this acton");
            }
            CrmAgentEB agentEB = home.findByPrimaryKey(agent.getPK());
            agentEB.setFromModel(agent);
        }catch(FinderException e){
            throw new FDResourceException(e, "Cannot find agent for id: "+agent.getPK());
        }catch(RemoteException e){
            throw new FDResourceException(e, "Cannot talk to CrmAgentEB");
        }
    }
    
    public CrmAgentModel getAgentByPk(String agentPk) throws FDResourceException, FinderException{
        try{
            CrmAgentEB agentEB = this.getCrmAgentHome().findByPrimaryKey(new PrimaryKey(agentPk));
            return (CrmAgentModel)agentEB.getModel();
        }catch(RemoteException e){
            throw new FDResourceException(e, "Cannot talk to CrmAgentEB");
        }
    }
    
    public CrmAgentList getAllAgents() throws FDResourceException {
        try{
            Collection agentEB = this.getCrmAgentHome().findAll();
            List agents = new ArrayList();
            for (Iterator i = agentEB.iterator(); i.hasNext();) {
                CrmAgentEB eb = (CrmAgentEB) i.next();
                agents.add((CrmAgentModel) eb.getModel());
            }
            return new CrmAgentList(agents);
        }catch(FinderException e){
            throw new FDResourceException(e, "FinderExzception");
        }catch(RemoteException e){
            throw new FDResourceException(e, "Cannot talk to CrmAgentEB");
        }
    }
    
    public List findCases(CrmCaseTemplate template) throws FDResourceException {
        Connection conn = null;
        try{
            conn = this.getConnection();
            return new CrmCaseDAO().loadCaseInfoByTemplate(conn, template);
        }catch(SQLException e){
            throw new FDResourceException(e);
        }finally{
            try{
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException ignored){
            }
        }
    }
    
    public List getQueueOverview() throws FDResourceException {
        Connection conn = null;
        try {
            conn = this.getConnection();
            return new CrmCaseDAO().loadQueueOverview(conn);
        } catch (SQLException e) {
            throw new FDResourceException(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }
    
    public List getCSROverview() throws FDResourceException {
        Connection conn = null;
        try {
            conn = this.getConnection();
            return new CrmCaseDAO().loadCSROverview(conn);
        } catch (SQLException e) {
            throw new FDResourceException(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }
    
    public CrmCaseModel getCaseByPk(String casePk) throws FDResourceException {
        try{
            CrmCaseEB caseEB = this.getCrmCaseHome().findByPrimaryKey(new PrimaryKey(casePk));
            return (CrmCaseModel)caseEB.getModel();
        }catch(FinderException e){
            throw new FDResourceException(e, "FinderException");
        }catch(RemoteException e){
            throw new FDResourceException(e, "Cannot talk to CrmCaseEB");
        }
    }
    
    public CrmAgentModel loginAgent(String userId, String password) throws FDResourceException, CrmAuthenticationException {
        try{
            CrmAgentEB agentEB = this.getCrmAgentHome().findByUserIdAndPassword(userId, password);
            if(!agentEB.isActive()){
                throw new CrmAuthenticationException("Account diabled");
            }
            return (CrmAgentModel)agentEB.getModel();
        }catch(ObjectNotFoundException e){
            throw new CrmAuthenticationException("Invalid username or password");
        }catch(FinderException e){
            throw new FDResourceException(e, "FinderException");
        }catch(RemoteException e){
            throw new FDResourceException(e, "Cannot talk to CrmAgentEB");
        }
    }
    
    public PrimaryKey createCase(CrmCaseModel caseModel) throws FDResourceException {
        try{
            return getCrmCaseHome().create(caseModel).getPK();
        }catch(CreateException e){
            throw new FDResourceException(e, "CreateException");
        }catch(RemoteException e){
            throw new FDResourceException(e, "Cannot talk to CrmCaseEB");
        }
    }
    
    public PrimaryKey createSystemCase(CrmSystemCaseInfo caseInfo) throws FDResourceException {
        try{
        	CrmCaseModel caseModel = this.getSystemCase(caseInfo);
            return getCrmCaseHome().create(caseModel).getPK();
        }catch(CreateException e){
            throw new FDResourceException(e, "CreateException");
        }catch(RemoteException e){
            throw new FDResourceException(e, "RemoteException while trying to talk to CrmCaseEB");
        }
    }
    
    public PrimaryKey createSystemCaseInSingleTx(CrmSystemCaseInfo caseInfo) throws FDResourceException {
        try {
            CrmCaseModel caseModel = this.getSystemCase(caseInfo);
            return getCrmCaseHome().create(caseModel).getPK();
        } catch(CreateException e) {
            throw new FDResourceException(e, "CreateException");
        } catch(RemoteException e) {
            throw new FDResourceException(e, "Cannot talk to CrmCaseEB");
        }
    }
    
	private CrmAgentModel getSystemUser() throws FDResourceException {
		try {
			if (this.systemUser == null) {
				this.systemUser =
					this.loginAgent(ErpServicesProperties.getCrmSystemUserName(), ErpServicesProperties.getCrmSystemUserPassword());
			}
			return this.systemUser;
		} catch (CrmAuthenticationException e) {
			throw new FDResourceException(e, "cannot find system user");
		}
	}
	
	private CrmCaseModel getSystemCase(CrmSystemCaseInfo caseInfo) throws FDResourceException {
		CrmCaseModel cm = new CrmCaseModel();
		
		cm.setCustomerPK(caseInfo.getCustomerPK());
		cm.setSalePK(caseInfo.getSalePK());
		cm.setSubject(caseInfo.getSubject());
		cm.setPriority(caseInfo.getPriority());
		cm.setSummary(caseInfo.getSummary());
		cm.setOrigin(caseInfo.getOrigin());
		cm.setState(caseInfo.getState());
		cm.setAssignedAgentPK(this.getSystemUser().getPK());
		
		List caseActions = new ArrayList();
		
		CrmCaseAction caseAction = new CrmCaseAction();
		caseAction.setAgentPK(this.getSystemUser().getPK());
		caseAction.setType(CrmCaseActionType.getEnum(CrmCaseActionType.CODE_NOTE));
		caseAction.setNote(caseInfo.getNote() != null ? caseInfo.getNote() : caseInfo.getSummary());
		caseAction.setTimestamp(new Date());
     
		caseActions.add(caseAction);
		cm.setActions(caseActions);
		return cm;
	}
    
    public void updateCase(CrmCaseInfo caseInfo, CrmCaseAction caseAction, PrimaryKey agentPk) throws FDResourceException, CrmAuthorizationException {
        try {
            CrmCaseEB caseEB = getCrmCaseHome().findByPrimaryKey(caseInfo.getPK());
            CrmCaseModel cm = (CrmCaseModel) caseEB.getModel();
            if (!agentPk.equals(cm.getLockedAgentPK())) {
                this.getSessionContext().setRollbackOnly();
                throw new CrmAuthorizationException("Agent cannot update case without having the case locked first");
            }
            CrmAgentHome home = this.getCrmAgentHome();
            CrmAgentEB agentEB = home.findByPrimaryKey(agentPk);
            CrmAgentModel agent = (CrmAgentModel) agentEB.getModel();
            
            // audit
            CrmCaseChangeAuditor audit = new CrmCaseChangeAuditor();
            audit.attach(cm);
            
            // bind
            cm.setSubject(caseInfo.getSubject());
            cm.setPriority(caseInfo.getPriority());
            cm.setSummary(caseInfo.getSummary());
            cm.setAssignedAgentPK(caseInfo.getAssignedAgentPK());
            cm.setCustomerPK(caseInfo.getCustomerPK());
            cm.setDepartments(caseInfo.getDepartments());
            cm.setActualQuantity(caseInfo.getActualQuantity());
            cm.setProjectedQuantity(caseInfo.getProjectedQuantity());
                        
            cm.setCrmCaseMedia(caseInfo.getCrmCaseMedia());
            cm.setMoreThenOneIssue(caseInfo.isMoreThenOneIssue());            
			cm.setFirstContactForIssue(caseInfo.isFirstContactForIssue());
			cm.setFirstContactResolved(caseInfo.isFirstContactResolved());
			cm.setResonForNotResolve(caseInfo.getResonForNotResolve());
			cm.setSatisfiedWithResolution(caseInfo.isSatisfiedWithResolution());
			cm.setCustomerTone(caseInfo.getCustomerTone());
            
            // audit
            audit.detach();
            
            System.out.println("audit.isChanged()"+audit.isChanged());
            
            CrmCaseState stateAfterInfo = null;
            
            if (audit.isChanged()) {
                
                CrmCaseAction editAction = new CrmCaseAction();
                editAction.setType(CrmCaseActionType.getEnum(CrmCaseActionType.CODE_EDIT));
                editAction.setTimestamp(new Date());
                editAction.setAgentPK(agent.getPK());
                editAction.setNote(audit.getNote());
                
                CrmCaseOperation op = this.actionAllowed(agent.getRole().getCode(), caseInfo.getSubject().getCode(),
                cm.getState().getCode(), editAction.getType().getCode());
                caseEB.updateCaseInfo(caseInfo);
                caseEB.addCaseAction(editAction);
                caseEB.setState(op.getEndState());
                
                stateAfterInfo = op.getEndState();
            }
            
            if (caseAction != null) {
            	if (audit.isChanged()) { // if audit took place make sure not action is timestamped later.
            		caseAction.setTimestamp(new Date());
            	}
                CrmCaseOperation op = this.actionAllowed(agent.getRole().getCode(), caseInfo.getSubject().getCode(),
                    stateAfterInfo == null ? cm.getState().getCode() : stateAfterInfo.getCode(), caseAction.getType().getCode());
                caseEB.addCaseAction(caseAction);
                caseEB.setState(op.getEndState());
            }
            
        } catch (FinderException e) {
            throw new FDResourceException(e, "FinderException");
        } catch (RemoteException e) {
            throw new FDResourceException(e, "Cannot talk to CrmCaseEB");
        }
    }
    
    private CrmCaseOperation actionAllowed(String agentRole, String caseSubject, String caseState, String actionType) throws CrmAuthorizationException, FDResourceException {
    	Connection conn = null;
        try {
        	conn = getConnection();
            CrmCaseOperationDAO codao = new CrmCaseOperationDAO();
            CrmCaseOperation op = codao.getPermissableOperation(conn, agentRole, caseSubject, caseState, actionType);
            
            if (op == null) {
                this.getSessionContext().setRollbackOnly();
                throw new CrmAuthorizationException("Action not allowed");
            }
            
            return op;
        } catch (SQLException sqle) {
            throw new FDResourceException(sqle);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ignored) {
			}
       }
    }
    
    
    public boolean lockCase(PrimaryKey agentPK, PrimaryKey casePK) throws FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			CrmCaseDAO dao = new CrmCaseDAO();
			dao.unlockAll(conn, agentPK);
			if (casePK != null) {
				return dao.lock(conn, agentPK, casePK);
			}
			return true;

		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ignored) {
			}
		}
    }
    
    public void unlockCase(PrimaryKey casePK) throws FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			CrmCaseDAO dao = new CrmCaseDAO();
			dao.unlock(conn, casePK);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ignored) {
			}
		}
    }
    
    public List getOperations() throws FDResourceException {
        Connection conn = null;
        try {
            conn = this.getConnection();
            return new CrmCaseOperationDAO().loadAll(conn);
        } catch(SQLException e) {
            throw new FDResourceException(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException ignored){
            }
        }
    }
    
	public void downloadCases(PrimaryKey agentPK, String queue, String subject, int numberToDownload) throws FDResourceException {
		Connection conn = null;
		try{
			conn = this.getConnection();
			CrmCaseDAO dao = new CrmCaseDAO();
			dao.downloadCases(conn, agentPK, queue, subject, numberToDownload);
		}catch(SQLException e) {
			LOGGER.debug("SQLException: ", e);
			throw new FDResourceException(e);
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException e){
				LOGGER.warn("SQLException while cleaningup", e);
			}
		}
	}
	
	public CrmStatus getSessionStatus(PrimaryKey agentPK) throws FDResourceException{
		Connection conn = null;
		try{
			conn = this.getConnection();
			CrmStatusDAO dao = new CrmStatusDAO();
			CrmStatus status = dao.retrieve(conn, agentPK);
			return status;
		}catch(SQLException e) {
			LOGGER.debug("SQLException: ", e);
			throw new FDResourceException(e);
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException e){
				LOGGER.warn("SQLException while cleaningup", e);
			}
		}
	}
	
	public void saveSessionStatus(CrmStatus status) throws FDResourceException{
		Connection conn = null;
		try{
			conn = this.getConnection();
			CrmStatusDAO dao = new CrmStatusDAO();
			if(dao.retrieve(conn, status.getAgentPK()) == null){
				dao.create(conn, status);
			} else {
				dao.update(conn, status);
			}
		}catch(SQLException e) {
			LOGGER.debug("SQLException: ", e);
			throw new FDResourceException(e);
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException e){
				LOGGER.warn("SQLException while cleaningup", e);
			}
		}
	}

	public CrmCustomerHeaderInfo getCustomerHeaderInfo(String customerId) throws FDResourceException {
		Connection conn = null;
		try{
			conn = this.getConnection();
			return CrmCustomerInfoDAO.getCrmCustomerHeaderInfo(conn, customerId);
		}catch(SQLException e) {
			throw new FDResourceException(e);
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException e){
				LOGGER.warn("SQLException while cleaningup", e);
			}
		}
	}
    
    private CrmAgentHome getCrmAgentHome() {
        try {
            return (CrmAgentHome) LOCATOR.getRemoteHome("java:comp/env/ejb/CrmAgent", CrmAgentHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }
    
    private CrmCaseHome getCrmCaseHome() {
        try {
            return (CrmCaseHome) LOCATOR.getRemoteHome("java:comp/env/ejb/CrmCase", CrmCaseHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }
    
	private ErpCustomerManagerHome getErpCustomerManagerHome() {
		try {
			return (ErpCustomerManagerHome) LOCATOR.getRemoteHome("freshdirect.erp.CustomerManager", ErpCustomerManagerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
    private DlvPassManagerHome getDlvPassManagerHome() {
        try {
            return (DlvPassManagerHome) LOCATOR.getRemoteHome("java:comp/env/ejb/DlvPassManager", DlvPassManagerHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }
	private ErpCustomerHome getErpCustomerHome() {
		try {
			//return (ErpCustomerHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpCustomer", ErpCustomerHome.class);
			return (ErpCustomerHome) LOCATOR.getRemoteHome("freshdirect.erp.Customer", ErpCustomerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
    
    public PrimaryKey  createLateIssue(CrmLateIssueModel lateIssue) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			//conn.setAutoCommit(false);
			PrimaryKey pk = CrmLateIssueDAO.createLateIssue(conn,lateIssue);
			return pk;
		} catch (SQLException sqle) {
			if (conn != null) {
				try { conn.rollback();} catch (SQLException e) {}
			}
			throw new FDResourceException(sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}		
	}

	
	public void updateLateIssue(CrmLateIssueModel model) throws FinderException, FDResourceException {
		// remove existing 
		Connection conn = null;
		try {
			conn = getConnection();
			//conn.setAutoCommit(false);
			CrmLateIssueDAO.updateLateIssue(conn, model);
		} catch (SQLException sqle) {
			if (conn != null) {
				try { conn.rollback();} catch (SQLException e) {}
			}
			throw new FDResourceException(sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}		
	}
	
	public CrmLateIssueModel getLateIssueById(String id) throws FinderException, FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return  CrmLateIssueDAO.getLateIssueModelById(conn, id);
		}catch(SQLException e){
			throw new FinderException(e.getMessage());
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException ignored){
				LOGGER.warn("Ignoreing SqlException closing connection", ignored);
			}
		}

	}

	public Collection getLateIssuesByRouteAndDate(String route,Date dateOfDlv) throws FDResourceException {
		Connection conn = null;
		try{
			conn = this.getConnection();
			return CrmLateIssueDAO.findByRouteAndDate(conn, route,dateOfDlv);
		}catch(SQLException e){
			throw new FDResourceException(e.getMessage());
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException ignored){
				LOGGER.warn("Ignoreing SqlException closing connection", ignored);
			}
		}
	
	}

	public Collection getLateIssuesByDate(Date dateOfDlv) throws  FDResourceException  {
		Connection conn = null;
		try{
			conn = this.getConnection();
			return CrmLateIssueDAO.findByDate(conn, dateOfDlv);
		}catch(SQLException e){
			throw new FDResourceException(e.getMessage());
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException ignored){
			}
		}
		
	}   
	
	public CrmLateIssueModel getRecentLateIssueForOrder(String orderId) throws  FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return  CrmLateIssueDAO.getRecentLateIssueForOrder(conn,orderId);
		}catch(SQLException e){
			throw new FDResourceException(e);
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException ignored){
			}
		}

	}

	
	public List getTruckNumbersForDate(Date date) throws FDResourceException {
		try {
			ErpCustomerManagerSB erpCustomerManagerSB = this.getErpCustomerManagerHome().create();
			return erpCustomerManagerSB.getTruckNumbersForDate(date);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}
	

	
	public void incrDeliveryCount(DeliveryPassModel model, 
			CrmAgentModel agentmodel, 
			int delta, 
			String note, 
			String reasonCode, 
			String saleId) throws FDResourceException, CrmAuthorizationException {
		try {
			/*
			 *  CSR would only add 1 delivery to BSGS DP/or one week to unlimited DP. Anything 
			 *  higher supervisor would have to do.
			 */
			//Get the No.Of credits given for this order.
			ErpActivityRecord template = new ErpActivityRecord();
			template.setCustomerId(model.getCustomerId());
			template.setDeliveryPassId(model.getPK().getId());
			template.setChangeOrderId(saleId);
			//BSGS Pass.
			template.setActivityType(EnumAccountActivityType.CREDIT_DLV_PASS);
			Collection credits = ActivityLog.getInstance().findActivityByTemplate(template);
			if((credits.size()+delta) >3){
				//He has already got 3 or more weeks extensions on this order. Further extensions
				//need to handled by the supervsior.
	            CrmAgentHome home = this.getCrmAgentHome();
	            CrmAgentEB eb = home.findByPrimaryKey(agentmodel.getPK());
	            CrmAgentModel user = (CrmAgentModel)eb.getModel();
	            if(!user.isSupervisor()){
	                throw new CrmAuthorizationException("You are not authorized to perform this action. Please contact your Supervisor.");
	            }
			}
			
			DlvPassManagerSB dlvPassManagerSB = this.getDlvPassManagerHome().create();
			dlvPassManagerSB.creditDelivery(model, delta);
			//Create a activity log to track the delivery credits.
			for(int i=0;i<delta;i++) {
				ErpActivityRecord activityRecord = createActivity(EnumAccountActivityType.CREDIT_DLV_PASS, 
																	agentmodel.getUserId(), 
																	note, 
																	model,
																	saleId,
																	reasonCode);
				logActivity(activityRecord);
			}
			
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}catch(FinderException e){
            throw new FDResourceException(e);
        }		
	}	
	
	public void incrExpirationPeriod(DeliveryPassModel model, 
									CrmAgentModel agentmodel, 
									int noOfDays, 
									String note, 
									String reasonCode, 
									String saleId) throws FDResourceException, CrmAuthorizationException {
		try {
			/*
			 *  CSR would only add upto 3 delivery to BSGS DP/or one week to unlimited DP. Anything 
			 *  higher supervisor would have to do.
			 */
			//Get the No.Of credits given for this order.
			ErpActivityRecord template = new ErpActivityRecord();
			template.setCustomerId(model.getCustomerId());
			template.setDeliveryPassId(model.getPK().getId());
			template.setChangeOrderId(saleId);
			//Unlimited Pass.
			template.setActivityType(EnumAccountActivityType.EXTEND_DLV_PASS);
			Collection extns = ActivityLog.getInstance().findActivityByTemplate(template);
			if((extns.size()+(int)(noOfDays/7)) >3){//must come from template.
				
	            CrmAgentHome home = this.getCrmAgentHome();
	            CrmAgentEB eb = home.findByPrimaryKey(agentmodel.getPK());
	            CrmAgentModel user = (CrmAgentModel)eb.getModel();
	            if(!user.isSupervisor()){
	                throw new CrmAuthorizationException("You are not authorized to perform this action. Please contact your Supervisor.");
	            }
			}
			DlvPassManagerSB dlvPassManagerSB = this.getDlvPassManagerHome().create();
			dlvPassManagerSB.extendExpirationPeriod(model, noOfDays);
			//Create a activity log to track the delivery credits.
			for(int i=0;i<(int)(noOfDays/7);i++) {
				ErpActivityRecord activityRecord = createActivity(EnumAccountActivityType.EXTEND_DLV_PASS, 
																	agentmodel.getUserId(), 
																	note, 
																	model,
																	saleId,
																	reasonCode);
				logActivity(activityRecord);
			}
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch(FinderException e){
            throw new FDResourceException(e);
        }		
	}	
	
	private boolean isAutoRenewDPCustomer(String hasAutoRenewDP_Val) {
		
		boolean isAutoRenewDPCustomer=false;
		if(hasAutoRenewDP_Val!=null && !hasAutoRenewDP_Val.equals("") && hasAutoRenewDP_Val.equalsIgnoreCase("Y")) {
			isAutoRenewDPCustomer=true;
		}
		return isAutoRenewDPCustomer;
			
	}
	
	   
	public void cancelDeliveryPass(DeliveryPassModel model, 
								CrmAgentModel agentmodel, 
								String note, 
								String reasonCode, 
								String saleId) throws FDResourceException{
		try {
			DlvPassManagerSB dlvPassManagerSB = this.getDlvPassManagerHome().create();
			model.setStatus(EnumDlvPassStatus.CANCELLED);
					
			model.setExpirationDate(new Date());
			dlvPassManagerSB.cancel(model);
			if(model.getType().isAutoRenewDP()) {
				ErpCustomerEB erpCustomer = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(model.getCustomerId()));
				ErpCustomerInfoModel custInfo=erpCustomer.getCustomerInfo();
				if(isAutoRenewDPCustomer(custInfo.getHasAutoRenewDP()) ) {
						List autoRenewPasses=dlvPassManagerSB.getUsableAutoRenewPasses(model.getCustomerId());
						if(autoRenewPasses.size()==0) {
						
							custInfo.setHasAutoRenewDP("N");
							erpCustomer.setCustomerInfo(custInfo);
							ErpActivityRecord rec = new ErpActivityRecord();
							rec.setActivityType(EnumAccountActivityType.AUTORENEW_DP_FLAG_OFF);
							rec.setCustomerId(model.getCustomerId());
							rec.setSource(EnumTransactionSource.SYSTEM);
							rec.setInitiator(agentmodel.getUserId());
							logActivity(rec);
						}
				}
			}

			//Create a activity log to track the delivery credits.
			ErpActivityRecord activityRecord = createActivity(EnumAccountActivityType.CANCEL_DLV_PASS, 
																agentmodel.getUserId(), 
																note, 
																model,
																saleId,
																reasonCode);
			logActivity(activityRecord);
			
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (FinderException fe) {
			throw new FDResourceException(fe);		} 
	}
	
		
	public void reactivateDeliveryPass(DeliveryPassModel model) throws FDResourceException {
		try {
			DlvPassManagerSB dlvPassManagerSB = this.getDlvPassManagerHome().create();
			dlvPassManagerSB.reactivate(model);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}		
	}
	
	private ErpActivityRecord createActivity(EnumAccountActivityType type, 
			String initiator, 
			String note, 
			DeliveryPassModel model, 
			String saleId, 
			String reasonCode) {
			ErpActivityRecord rec = new ErpActivityRecord();
			rec.setActivityType(type);
			
			rec.setSource(EnumTransactionSource.CUSTOMER_REP);
			rec.setInitiator(initiator);
			rec.setCustomerId(model.getCustomerId());
			
			StringBuffer sb = new StringBuffer();
			if (note != null) {
			sb.append(note);
			}
			rec.setNote(sb.toString());
			rec.setDeliveryPassId(model.getPK().getId());
			rec.setChangeOrderId(saleId);
			rec.setReason(reasonCode);
			return rec;
	}
	
	private void logActivity(ErpActivityRecord record) {
		new ErpLogActivityCommand(LOCATOR, record).execute();
	}
	
	/**
	 * 
	 * @param accountNum -- CC/Checking Account Number.
	 * @return Customer's UserID who owns this Account.
	 */
	public String lookupAccount(String accountNum) throws FDResourceException{
        Connection conn = null;
        try{
            conn = this.getConnection();
            return CustomerPaymentDAO.getUserIDByAccountNumber(conn, accountNum);
        }catch(SQLException e){
            throw new FDResourceException(e);
        }finally{
            try{
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException se){
            	throw new FDResourceException(se);
            }
        }
	}
	
	/**
	 * 
	 * @param accountNum -- CC/Checking Account Number.
	 * @return Orders that were placed using this Account Number.
	 */
	public List lookupOrders(String accountNum) throws FDResourceException{
        Connection conn = null;
        try{
            conn = this.getConnection();
            return CustomerPaymentDAO.getOrdersByAccountNumber(conn, accountNum);
        }catch(SQLException e){
            throw new FDResourceException(e);
        }finally{
            try{
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException se){
            	throw new FDResourceException(se);
            }
        }
	}
	
	public void logViewAccount(CrmAgentModel agent, String customerID)throws FDResourceException {
		ErpActivityRecord rec = new ErpActivityRecord();
		rec.setActivityType(EnumAccountActivityType.VIEW_CC_ECHECK);
		rec.setCustomerId(customerID);
		rec.setSource(EnumTransactionSource.CUSTOMER_REP);
		rec.setInitiator(agent.getUserId());
		logActivity(rec);

	}
}
