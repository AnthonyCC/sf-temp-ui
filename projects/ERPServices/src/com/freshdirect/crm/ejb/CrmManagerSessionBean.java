package com.freshdirect.crm.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.crm.CrmAgentInfo;
import com.freshdirect.crm.CrmAgentList;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmAuthInfo;
import com.freshdirect.crm.CrmAuthSearchCriteria;
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
import com.freshdirect.crm.CrmQueueInfo;
import com.freshdirect.crm.CrmStatus;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumCannedTextCategory;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpCannedText;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpTruckInfo;
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
import com.freshdirect.customer.CustomerCreditModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;



public class CrmManagerSessionBean extends SessionBeanSupport {
	private static final long serialVersionUID = 6027777576711960489L;

	private final static Category LOGGER = LoggerFactory.getInstance( CrmManagerSessionBean.class );
    
    private final static ServiceLocator LOCATOR = new ServiceLocator();
    private CrmAgentModel systemUser;
    
    protected String getResourceCacheKey() {
        return "com.freshdirect.crm.ejb.CrmManagerHome";
    }
    
    public PrimaryKey createAgent(CrmAgentModel agent, PrimaryKey userPk) throws FDResourceException, CrmAuthorizationException, ErpDuplicateUserIdException {
        try{
            CrmAgentHome home = this.getCrmAgentHome();
            /*CrmAgentEB eb = home.findByPrimaryKey(userPk);
            CrmAgentModel user = (CrmAgentModel)eb.getModel();
            if(!user.isAdmin()){
                throw new CrmAuthorizationException("You are not authorized to perform this action");
            }*/
            
            CrmAgentEB agentEB =home.create(agent);
            return agentEB.getPK();
        }catch(DuplicateKeyException e){
            throw new ErpDuplicateUserIdException(e, "UserId or password already exists");
        } catch(CreateException e){
            throw new FDResourceException(e);
        }catch(RemoteException e){
            throw new FDResourceException(e);
        }/*catch(FinderException e){
            throw new FDResourceException(e);
        }*/
    }
    
    public void updateAgent(CrmAgentModel agent, PrimaryKey userPk) throws CrmAuthorizationException, FDResourceException {
        try{
            CrmAgentHome home = this.getCrmAgentHome();
            /*CrmAgentEB eb = home.findByPrimaryKey(userPk);
            CrmAgentModel user = (CrmAgentModel)eb.getModel();
            if(!user.isAdmin()){
                throw new CrmAuthorizationException("You are not authorized to perform this acton");
            }*/
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
            Collection<CrmAgentEB> agentEB = this.getCrmAgentHome().findAll();
            List<CrmAgentModel> agents = new ArrayList<CrmAgentModel>();
            for ( CrmAgentEB eb : agentEB ) {
                agents.add((CrmAgentModel) eb.getModel());
            }
            return new CrmAgentList(agents);
        }catch(FinderException e){
            throw new FDResourceException(e, "FinderException");
        }catch(RemoteException e){
            throw new FDResourceException(e, "Cannot talk to CrmAgentEB");
        }
    }
    
    public List<CrmCaseModel> findCases(CrmCaseTemplate template) throws FDResourceException {
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
    
    public List<CrmQueueInfo> getQueueOverview() throws FDResourceException {
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
    
    public List<CrmAgentInfo> getCSROverview() throws FDResourceException {
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
                throw new CrmAuthenticationException("Account disabled");
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
    
    public PrimaryKey createCase(CrmCaseModel caseModel) throws FDResourceException,CrmAuthorizationException {
        try{
        	 CrmAgentHome home = this.getCrmAgentHome();
             CrmAgentEB agentEB = home.findByPrimaryKey(caseModel.getAssignedAgentPK());
             CrmAgentModel agent = (CrmAgentModel) agentEB.getModel();
             String agentRoleCode = (null!=agent && null!=agent.getRole() && null!=agent.getRole().getCode())?agent.getRole().getCode():null;
             String subjectCode = (null!=caseModel.getSubject() && null!=caseModel.getSubject().getCode())?caseModel.getSubject().getCode():null;
        	 this.actionAllowed(agentRoleCode, subjectCode,
        			 CrmCaseState.CODE_NEW, CrmCaseActionType.CODE_NOTE);
            return getCrmCaseHome().create(caseModel).getPK();
        }catch(CreateException e){
            throw new FDResourceException(e, "CreateException");
        }catch (FinderException e) {
            throw new FDResourceException(e, "FinderException");
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
		cm.setAssignedAgentPK(caseInfo.getLoginAgent() != null ? caseInfo.getLoginAgent().getPK() : this.getSystemUser().getPK());
		cm.setCrmCaseMedia(caseInfo.getCrmCaseMedia());
		
		List<CrmCaseAction> caseActions = new ArrayList<CrmCaseAction>();
		
		CrmCaseAction caseAction = new CrmCaseAction();
		caseAction.setAgentPK(caseInfo.getLoginAgent() != null ? caseInfo.getLoginAgent().getPK() : this.getSystemUser().getPK());
		caseAction.setType(CrmCaseActionType.getEnum(CrmCaseActionType.CODE_NOTE));
		caseAction.setNote(caseInfo.getNote() != null ? caseInfo.getNote() : caseInfo.getSummary());
		caseAction.setTimestamp(new Date());
     
		caseActions.add(caseAction);
		cm.setActions(caseActions);
		cm.setCartonNumbers(caseInfo.getCartonNumbers());
		return cm;
	}
    
    public void updateCase(CrmCaseInfo caseInfo, CrmCaseAction caseAction, PrimaryKey agentPk) throws FDResourceException, CrmAuthorizationException {
        try {
            CrmCaseEB caseEB = getCrmCaseHome().findByPrimaryKey(caseInfo.getPK());
            CrmCaseModel cm = (CrmCaseModel) caseEB.getModel();
            
            System.out.println("cm.getLockedAgentPK() :"+cm.getLockedAgentPK());
            System.out.println("agentPk :"+agentPk);
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
            cm.setMoreThenOneIssue(caseInfo.getMoreThenOneIssue());            
			cm.setFirstContactForIssue(caseInfo.getFirstContactForIssue());
			cm.setFirstContactResolved(caseInfo.getFirstContactResolved());
			cm.setResonForNotResolve(caseInfo.getResonForNotResolve());
			cm.setSatisfiedWithResolution(caseInfo.getSatisfiedWithResolution());
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
                caseEB.updateCaseInfo(caseInfo);
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
    
    public boolean closeAutoCase(PrimaryKey casePK) throws FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			CrmCaseDAO dao = new CrmCaseDAO();
			return dao.closeAutoCase(conn, casePK);
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

    public List<CrmCaseOperation> getOperations() throws FDResourceException {
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
            return (CrmAgentHome) LOCATOR.getRemoteHome("java:comp/env/ejb/CrmAgent");
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }
    
    private CrmCaseHome getCrmCaseHome() {
        try {
            return (CrmCaseHome) LOCATOR.getRemoteHome("java:comp/env/ejb/CrmCase");
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }
    
	private ErpCustomerManagerHome getErpCustomerManagerHome() {
		try {
			return (ErpCustomerManagerHome) LOCATOR.getRemoteHome("freshdirect.erp.CustomerManager");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
    private DlvPassManagerHome getDlvPassManagerHome() {
        try {
            return (DlvPassManagerHome) LOCATOR.getRemoteHome("java:comp/env/ejb/DlvPassManager");
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }
	private ErpCustomerHome getErpCustomerHome() {
		try {
			return (ErpCustomerHome) LOCATOR.getRemoteHome("freshdirect.erp.Customer");
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

	public Collection<CrmLateIssueModel> getLateIssuesByRouteAndDate(String route,Date dateOfDlv) throws FDResourceException {
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

	public Collection<CrmLateIssueModel> getLateIssuesByDate(Date dateOfDlv) throws  FDResourceException  {
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

	
	public List<ErpTruckInfo> getTruckNumbersForDate(Date date) throws FDResourceException {
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
			Collection<ErpActivityRecord> credits = ActivityLog.getInstance().findActivityByTemplate(template);
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
			Collection<ErpActivityRecord> extns = ActivityLog.getInstance().findActivityByTemplate(template);
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
						List<DeliveryPassModel> autoRenewPasses=dlvPassManagerSB.getUsableAutoRenewPasses(model.getCustomerId());
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
	public List<String> lookupOrders(String accountNum) throws FDResourceException{
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
	
	public void logViewAccount(CrmAgentModel agent, String customerID,EnumAccountActivityType activityType,String maskedAcctNumber)throws FDResourceException {
		ErpActivityRecord rec = new ErpActivityRecord();
		rec.setActivityType(activityType);
		rec.setNote(maskedAcctNumber);
		rec.setCustomerId(customerID);
		rec.setSource(EnumTransactionSource.CUSTOMER_REP);
		rec.setInitiator(agent.getLdapId());
		logActivity(rec);

	}

	public ErpCannedText createCannedText(ErpCannedText cannedText) throws FDResourceException {
        Connection conn = null;
		try {
			conn = this.getConnection();
			CrmCannedTextDAO dao = new CrmCannedTextDAO();
			return dao.create(conn, cannedText);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new FDResourceException(se);
			}
		}
	}

	public void updateCannedText(ErpCannedText cannedText, String id) throws FDResourceException {
        Connection conn = null;
		try {
			conn = this.getConnection();
			CrmCannedTextDAO dao = new CrmCannedTextDAO();
			dao.update(conn, cannedText, id);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new FDResourceException(se);
			}
		}
	}
	
	public void deleteCannedText(String id) throws FDResourceException {
        Connection conn = null;
		try {
			conn = this.getConnection();
			CrmCannedTextDAO dao = new CrmCannedTextDAO();
			dao.delete(conn, id);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new FDResourceException(se);
			}
		}
	}

	public ErpCannedText getCannedTextById(String id) throws FDResourceException {
        Connection conn = null;
		try {
			conn = this.getConnection();
			CrmCannedTextDAO dao = new CrmCannedTextDAO();
			return dao.load(conn, id);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new FDResourceException(se);
			}
		}
	}

	public Collection<ErpCannedText> getAllCannedTextInCategory(EnumCannedTextCategory category) throws FDResourceException {
        Connection conn = null;
		try {
			conn = this.getConnection();
			CrmCannedTextDAO dao = new CrmCannedTextDAO();
			return dao.loadAllInCategory(conn, category);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new FDResourceException(se);
			}
		}
	}
	
	public Collection<ErpCannedText> getAllCannedText() throws FDResourceException {
        Connection conn = null;
		try {
			conn = this.getConnection();
			CrmCannedTextDAO dao = new CrmCannedTextDAO();
			return dao.loadAll(conn);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new FDResourceException(se);
			}
		}
	}





	// selects complaint delivery issue types for orders in history
	public static final String DLV_ISSUE_TYPES_SQL = 
		"select x.sale_id, x.itype " +
		"from cust.sale s " +
		"join ( " +
		"  select distinct c.sale_id as sale_id, cc.dlv_issue_code as itype " +
		"  from cust.complaint c " +
		"  join cust.complaintline cl " +
		"    on(cl.complaint_id=c.id) " +
		"  join cust.complaint_dept_code cdc " +
		"    on(cdc.id=cl.complaint_dept_code_id) " +
		"  join cust.complaint_code cc " +
		"    on(cc.code=cdc.comp_code) " +
		"  where c.status = 'APP' " +
		"  order by c.sale_id, cc.dlv_issue_code " +
		") x on(s.id=x.sale_id) " +
		"where s.customer_id=? " +
		"and x.itype is not null " +
		"order by sale_id";
	
	
	/* @return Map<String,Set<String>> */
	public Map<String,Set<String>> getComplaintDeliveryIssueTypes(String erpCustomerId) throws FDResourceException {
        Connection conn = null;
		try {
			conn = this.getConnection();

			PreparedStatement ps = conn.prepareStatement(DLV_ISSUE_TYPES_SQL);
			ps.setString(1, erpCustomerId);
			ResultSet rs = ps.executeQuery();

			Map<String,Set<String>> complDlvIssueTypes = new HashMap<String,Set<String>>();
			while(rs.next()) {
				String key=rs.getString("sale_id");
				if (complDlvIssueTypes.get(key) == null) {
					Set<String> s = new HashSet<String>();
					s.add(rs.getString("itype"));
					complDlvIssueTypes.put(key, s);
				} else {
					complDlvIssueTypes.get(key).add(rs.getString("itype"));
				}
			}


			rs.close();
			ps.close();

			return complDlvIssueTypes;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new FDResourceException(se);
			}
		}
	}
	


	public static final String QUERY_LAST_DELIVERED_ORDER = 
		"select id from ( " +
		"  select s.id, row_number() over (order by to_number(s.id) desc) r " +
		"  from cust.sale s, cust.salesaction sa " +
		"  where s.id = sa.sale_id " +
		"  and sa.action_type in ('CRO', 'MOD') " + 
		"  and sa.action_date = ( " +
		"    select max(action_date) " +
		"    from cust.salesaction sa1 " +
		"    where sa1.action_type in ('CRO', 'MOD') " +
		"    and sa1.sale_id = s.id " +
		"  ) " +
		"  and s.customer_id= ? " +
		"  and s.truck_number is not null " +
		"  and s.type='REG' " +
		") where r=1";
	
	public String getLastDeliveredOrder(String erpCustomerId) throws FDResourceException {
        Connection conn = null;
		try {
			conn = this.getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_LAST_DELIVERED_ORDER);
			ps.setString(1, erpCustomerId);
			ResultSet rs = ps.executeQuery();

			String saleId = null;
			if (rs.next()) {
				saleId = rs.getString(1);
			}


			rs.close();
			ps.close();

			return saleId;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new FDResourceException(se);
			}
		}
	}
	
	public CrmAgentModel getAgentByLdapId(String agentLdapId) throws CrmAuthenticationException,FDResourceException{
		 try{
	            CrmAgentEB agentEB = this.getCrmAgentHome().findAgentByLdapId(agentLdapId);
	            
	            return (CrmAgentModel)agentEB.getModel();
	        }catch(ObjectNotFoundException e){
	            throw new CrmAuthenticationException("Unable to find agent by LDAP Id:"+agentLdapId);
	        }catch(FinderException e){
	            throw new FDResourceException(e, "FinderException");
	        }catch(RemoteException e){
	            throw new FDResourceException(e, "Cannot talk to CrmAgentEB");
	        }
	}
	
	private static final String AUTH_SEARCH_QUERY=
	"select CC.TRANS_DATE_TIME ,CC.CUSTOMER_NAME ,CC.MERCHANT_ID,CC.AMOUNT,CC.APPROVAL_CODE,CVV_RESPONSE_CODE, AUTH_RESPONSE_MSG,"+
    " CC.ZIP_MATCH, CC.USER_DEFINED_1 as CustomerID, "+
    "( CASE cc.CARD_TYPE WHEN '001' THEN 'VISA' WHEN '002' THEN 'MASTERCARD' WHEN '003' THEN 'AMEX' WHEN '004' THEN 'DISCOVER' WHEN '005' THEN 'ECheck' END) AS CardType "+
    ",CC.ORDER_NUMBER,NVL(s.id,'N/A') as ORDERNUMBER, "+
    " CC.CUSTOMER_STREET||' '|| CC.CUSTOMER_CITY||' '|| CC.CUSTOMER_STATE||' '|| CC.CUSTOMER_ZIP as Address "+
    " from PAYLINX.CC_TRANSACTION_NEW cc, cust.sale s where CC.TRANS_DATE_TIME between to_date(?,'MM-DD-YYYY HH24:MI') and   to_date(?,'MM-DD-YYYY HH24:MI') and "+
    " CC.TRANSACTION_CODE='100' AND SUBSTR(CC.ORDER_NUMBER,1,INSTRB(CC.ORDER_NUMBER, 'X', 1, 1)-1)=s.id(+) ";
	//order by cc.TRANS_DATE_TIME asc
	
	private static final String AUTH_SEARCH_QUERY_MIS="select GAL.TRANSACTION_TIME TRANS_DATE_TIME,GAL.CUSTOMER_NAME ,GAL.MERCHANT MERCHANT_ID, GAL.AMOUNT, GAL.AUTH_CODE APPROVAL_CODE, GAL.CVV_RESPONSE_CODE, "+
	" GAL.STATUS_MSG AUTH_RESPONSE_MSG, GAL.IS_AVS_MATCH ZIP_MATCH, GAL.CUSTOMER_ID CustomerID, GAL.CARD_TYPE CardType, GAL.ORDER_ID ORDER_NUMBER, "+
	" NVL(s.id,'N/A') as ORDERNUMBER,GAL.CUSTOMER_ADDRESS1||' '|| GAL.CUSTOMER_CITY||' '|| GAL.CUSTOMER_STATE||' '|| GAL.CUSTOMER_ZIP as Address "+ 
	" from MIS.GATEWAY_ACTIVITY_LOG gal, cust.sale s where  GAL.TRANSACTION_TIME between to_date(?,'MM-DD-YYYY HH24:MI') and   to_date(?,'MM-DD-YYYY HH24:MI') and "+
	"   GAL.TRANSACTION_TYPE IN ('AUTHORIZE','CC_VERIFY') AND SUBSTR(GAL.ORDER_ID,1,INSTRB(GAL.ORDER_ID, 'X', 1, 1)-1)=s.id(+)  ";  
	
	public List<CrmAuthInfo> getAuthorizations(CrmAgentRole role,CrmAuthSearchCriteria filter)throws FDResourceException {
		Connection conn = null;
		try {
			StringBuilder query=new StringBuilder(AUTH_SEARCH_QUERY);
			StringBuilder query1=new StringBuilder(AUTH_SEARCH_QUERY_MIS);
			
			if(!StringUtil.isEmpty(filter.getCustomerName())) {
				query.append(" AND UPPER(trim(cc.customer_name)) LIKE UPPER('%").append(filter.getCustomerName()).append("%') ");
				query1.append(" AND UPPER(trim(GAL.customer_name)) LIKE UPPER('%").append(filter.getCustomerName()).append("%') ");
			}
			if(filter.getAmount()!="0") {
				query.append(" AND cc.amount ='").append(filter.getAmount()).append("' ");
				query1.append(" AND GAL.amount ='").append(filter.getAmount()).append("' ");
			}
			
			
			conn = this.getConnection();
			List<CrmAuthInfo> crmAuthInfoList=new ArrayList<CrmAuthInfo>(50);
			crmAuthInfoList.addAll(getAuthInfo(query.toString(),conn,filter));
			crmAuthInfoList.addAll(getAuthInfo(query1.toString(),conn,filter));
			
			return crmAuthInfoList;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new FDResourceException(se);
			}
		}
	}
	
	private List<CrmAuthInfo> getAuthInfo(String query, Connection conn,CrmAuthSearchCriteria filter) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, filter.getFromDateStr());
		ps.setString(2, filter.getToDateStr());
		ResultSet rs = ps.executeQuery();
		List<CrmAuthInfo> crmAuthInfoList=new ArrayList<CrmAuthInfo>(50);
		CrmAuthInfo crmAuthInfo=null;
		while (rs.next()) {
			crmAuthInfo=new CrmAuthInfo();
			crmAuthInfo.setTransactionTime(rs.getTimestamp("TRANS_DATE_TIME"));
			crmAuthInfo.setCustomerName(rs.getString("CUSTOMER_NAME"));
			crmAuthInfo.setMerchantId(rs.getString("MERCHANT_ID"));
			crmAuthInfo.setAmount(rs.getBigDecimal("AMOUNT"));
			crmAuthInfo.setApprovalCode(rs.getString("APPROVAL_CODE"));
			crmAuthInfo.setCvvResponseCode(rs.getString("CVV_RESPONSE_CODE"));
			crmAuthInfo.setAuthResponse(rs.getString("AUTH_RESPONSE_MSG"));
			crmAuthInfo.setZipCheckReponse(rs.getString("ZIP_MATCH"));
			crmAuthInfo.setCardType(EnumCardType.getCardType(rs.getString("CardType")));
			crmAuthInfo.setAddress(rs.getString("Address"));
			crmAuthInfo.setOrder(rs.getString("ORDER_NUMBER"));
			crmAuthInfo.setValidOrder(!"N/A".equals(rs.getString("ORDERNUMBER")));
			crmAuthInfo.setCustomerId(rs.getString("CustomerID"));
			crmAuthInfoList.add(crmAuthInfo);
			
		}
		rs.close();
		ps.close();
		return crmAuthInfoList;
	}
	
	public CustomerCreditModel getOrderForLateCredit(String saleId, String autoId) throws FDResourceException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		CustomerCreditModel ccm = new CustomerCreditModel();
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(GET_SALE_DETS);
			pstmt.setString(1, saleId);
			pstmt.setString(2, autoId);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				ccm.setSaleId(rset.getString("SALE_ID"));
				ccm.setRemType(rset.getString("REM_TYPE"));
				ccm.setCustomerId(rset.getString("CUSTOMER_ID"));
				ccm.setRemainingAmout(rset.getDouble("REM_AMOUNT"));
				ccm.setOriginalAmount(rset.getDouble("ORIGINAL_AMOUNT"));
				ccm.setTaxRate(rset.getDouble("TAX_RATE"));
				ccm.setDlvPassId(rset.getString("DLV_PASS_ID"));
				ccm.setNewCode(rset.getString("complaint_code"));
				ccm.setFdCustomerId(rset.getString("ID"));
			}
		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
			close(conn);
		}
		return ccm;
	}
	
	public boolean isCaseCreatedForOrderLateDelivery(String saleId) throws FDResourceException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(GET_CASE_CREATED);
			pstmt.setString(1, saleId);
			rset = pstmt.executeQuery();
			if(rset.next()) {
				return true;
			}
		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
			close(conn);
		}
		return false;
	}
	
	public boolean isOrderCreditedForLateDelivery(String saleId) throws FDResourceException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(GET_COMPLAINT_CODE);
			pstmt.setString(1, saleId);
			rset = pstmt.executeQuery();
			if(rset.next()) {
				return true;
			}
		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
			close(conn);
		}
		return false;
	}
	
	public DeliveryPassModel getDeliveryPassInfoById(String dlvPassId) throws RemoteException, FDResourceException {
		try{
		DlvPassManagerSB sb = this.getDlvPassManagerHome().create();		
		return sb.getDeliveryPassInfo(dlvPassId);
		}
		catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}
	
	public void updateAutoLateCredit(String autoId, String orderId) throws FDResourceException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(UPDATE_STATUS_FLAG);
			pstmt.setString(1, autoId);
			pstmt.setString(2, orderId);
			pstmt.execute();
		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
			close(conn);
		}
	}
	
	public DeliveryPassModel getActiveDP(String custId) throws FDResourceException,RemoteException {
		try{
		DlvPassManagerSB sb = this.getDlvPassManagerHome().create();		
		List dps = sb.getDeliveryPasses(custId);
		Iterator iter = dps.iterator();
		while(iter.hasNext()) {
			DeliveryPassModel dp = (DeliveryPassModel) iter.next();
			if(dp.getStatus() == EnumDlvPassStatus.ACTIVE || dp.getStatus() == EnumDlvPassStatus.READY_TO_USE) {
				return dp;
			}
		}
		return null;
		}
		catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}
	
	public void updateLateCreditsRejected(String autoId, String agent) throws FDResourceException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rset = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(UPDATE_REJECT_FLAG);
			pstmt.setString(1, autoId);
			pstmt.execute();
			
			pstmt2 = conn.prepareStatement(UPDATE_STATUS);
			pstmt2.setString(1, agent);
			pstmt2.setString(2, autoId);
			pstmt2.execute();
		} catch (SQLException sqle) {
			//sqle.printStackTrace();
			throw new FDResourceException(sqle);
		} finally {
			close(conn);
		}
	}
	
	public boolean isDlvPassAlreadyExtended(String orderId, String customerId) throws FDResourceException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(DLV_PASS_CHECK);
			pstmt.setString(1, customerId);
			pstmt.setString(2, orderId);
			rset = pstmt.executeQuery();
			if(rset.next()) {
				return true;
			}			
		} catch (SQLException sqle) {
			//sqle.printStackTrace();
			throw new FDResourceException(sqle);
		} finally {
			close(conn);
		}
		return false;
	}
	
	
	public boolean isCRMRestrictionEnabled() throws FDResourceException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(CRM_RESTRICTION_ENABLED_CHECK);
			String value="";
			rset = pstmt.executeQuery();
			if(rset.next()) {
				value=rset.getString("NAME");
				if(StringUtil.isEmpty(value)) return false;
				if(value.toLowerCase().indexOf("true")!=-1) {	
					return true;
				}
			}
		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
			close(rset);
			close(pstmt);
			close(conn);
		}
		return false;
	}
	
	public  String getAllowedUsers() throws FDResourceException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(CRM_ALLOWED_USERS);
			String value="";
			rset = pstmt.executeQuery();
			if(rset.next()) {
				
				value= rset.getString("DESCRIPTION");
				if(StringUtil.isEmpty(value)) return "ALL";
				return value;
			}
		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
			close(rset);
			close(pstmt);
			close(conn);
		}
		return "ALL";
	}
	
	
	public static String DLV_PASS_CHECK = "SELECT reason FROM CUST.ACTIVITY_LOG where customer_id=? and sale_id=? and reason in (select comp_code from CUST.LATE_DLV_COMPLAINT_CODES)";
	
	public static String UPDATE_REJECT_FLAG = "update CUST.AUTO_LATE_DELIVERY_ORDERS set status = 'R' where AUTO_LATE_DELIVERY_ID=? and (status is null or status != 'A')";
	
	public static String UPDATE_STATUS_FLAG = "update CUST.AUTO_LATE_DELIVERY_ORDERS set status = 'A' where AUTO_LATE_DELIVERY_ID=? and SALE_ID=?";
	
	public static String UPDATE_STATUS = "update cust.AUTO_LATE_DELIVERY set STATUS='A', APPROVED_BY=? where ID = ?";
	
	public static String GET_CASE_CREATED =  "select * from cust.CASE where case_subject in ('DSQ-011', 'OAQ-009') and sale_id = ? " ;
	
	public static String GET_COMPLAINT_CODE =  
		"select comp_code from cust.complaint_dept_code where id in " + 
        "(select complaint_dept_code_id from cust.complaintline where complaint_id in " + 
            "(select id from cust.complaint where sale_id=? " +
                "and STATUS in ('PEN','APP')) " +
        ") and comp_code in (select comp_code from CUST.LATE_DLV_COMPLAINT_CODES)";
	
	public static String GET_SALE_DETS = "select AUTO_LATE_DELIVERY_ID,SALE_ID,CUSTOMER_ID,REM_AMOUNT,REM_TYPE,ORIGINAL_AMOUNT, " + 
									"TAX_AMOUNT,TAX_RATE,DLV_PASS_ID,complaint_code, f.id from  " +
									"CUST.AUTO_LATE_DELIVERY_ORDERS a, " +
									"cust.fdcustomer f " +
									"where sale_id = ? and AUTO_LATE_DELIVERY_ID=? " +
									"and a.customer_id = f.erp_customer_id";
	
	public static String CRM_RESTRICTION_ENABLED_CHECK = "select NAME from cust.activity_type where code='CRM_OFF'";
	
	public static String CRM_ALLOWED_USERS = "select DESCRIPTION from cust.activity_type where code='CRM_AU'";
}
