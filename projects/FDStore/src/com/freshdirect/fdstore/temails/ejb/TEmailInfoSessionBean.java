package com.freshdirect.fdstore.temails.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.ejb.FDSessionBeanSupport;
import com.freshdirect.fdstore.temails.TEmailConstants;
import com.freshdirect.fdstore.temails.TEmailContentFactory;
import com.freshdirect.fdstore.temails.TEmailTemplateInfo;
import com.freshdirect.fdstore.temails.TransEmailInfoModel;
import com.freshdirect.framework.mail.TEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.EnumEmailType;
import com.freshdirect.mail.EnumTranEmailType;
import com.freshdirect.mail.ejb.TEmailerGatewayHome;
import com.freshdirect.mail.ejb.TMailerGatewaySB;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.temails.TEmailRuntimeException;

public class TEmailInfoSessionBean extends FDSessionBeanSupport{
    
	private static final long serialVersionUID = 1L;
	private static Category LOGGER = LoggerFactory.getInstance( TEmailInfoSessionBean.class );

	public void sendEmail(EnumTranEmailType tranType,Map input) throws FDResourceException, RemoteException{
	    // check template is active 	
		// get the email content from the factory
		// store it in db
		// put it in the jms queue
		// throw exception any error comes in
		Connection con=null;
		boolean isTemplateExist=true;
		try
		{
			con=getConnection();
			EnumEStoreId estoreId = EnumEStoreId.FD;
			EnumServiceType serviceType ;
			/* TODO */
			/*
			 * The following is questionable and I'd like to eliminate it, its a risky way to get the estore id.
			 */
		if ( input.containsKey(TEmailConstants.ESTORE_ID)){
			estoreId =  ((EnumEStoreId)input.get(TEmailConstants.ESTORE_ID));
		 }
		else{
			 estoreId = EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId()));

		}
		
		if ( input.containsKey(TEmailConstants.SERVICE_TYPE)){
			serviceType =  ((EnumServiceType)input.get(TEmailConstants.SERVICE_TYPE));
		 }
		else{
			serviceType =EnumServiceType.NONE ;
		}

		
		

			TEmailTemplateInfo tEmailTemplateInfo=TEmailInfoDAO.getTEmailTemplateInfo(con,tranType,
					input.get(TEmailConstants.EMAIL_TYPE)!=null?EnumEmailType.getEnum((String)input.get(TEmailConstants.EMAIL_TYPE)):EnumEmailType.TEXT,estoreId,serviceType  );	
			
			if(tEmailTemplateInfo==null){
				isTemplateExist=false;	
				throw new TEmailRuntimeException("No active templateId exist for tranType :"+tranType.getName());
			} else {
				LOGGER.debug("------------------------------------info:" + tEmailTemplateInfo.toString());
			//	System.out.println(this.getClass().getSimpleName() + " TEmailInfoSessionBean.sendmail(62) ---------------------------info" + tEmailTemplateInfo.toString());
			}
								
			TEmailI mail=TEmailContentFactory.getInstance().createTransactionEmailModel(tEmailTemplateInfo, input);
									
			
			TEmailInfoDAO.storeTransactionEmailInfo(con, (TransEmailInfoModel)mail);			
			
			TEmailerGatewayHome home=getTMailerGatewayHome();
			TMailerGatewaySB remote= home.create();
			remote.enqueue(mail);			
				
			
		}		
		catch(SQLException re){
			re.printStackTrace();
			getSessionContext().setRollbackOnly();
			throw new TEmailRuntimeException(re);				
		}		
		catch(TEmailRuntimeException re){
			if(isTemplateExist) re.printStackTrace();			   
			getSessionContext().setRollbackOnly();
			throw re;
		}catch (Exception e){
			e.printStackTrace();
			getSessionContext().setRollbackOnly();
			throw new TEmailRuntimeException(e);
		} finally{
			close(con);
		}		
	}
	
	
	
	public int sendFailedTransactions(int timeout){
		Connection con=null;
		int count =0;
		try
		{
			con=getConnection();
			List<TEmailI> failedTranList=TEmailInfoDAO.getFailedTransactions(con);
			System.out.println(this.getClass().getName() + "&&&&&&&&&&&&&&&&&&&&&    retrieved this many failed trans: "+ failedTranList.size());
			long startTime = System.currentTimeMillis();
			count = failedTranList.size();
			TEmailerGatewayHome home=getTMailerGatewayHome();
			TMailerGatewaySB remote= home.create();
			for(TEmailI mail: failedTranList ){			
				if (System.currentTimeMillis() - startTime > timeout) {
					LOGGER.warn(" transaction send mail was running longer than" + timeout / 60 / 1000);
					break;
				}
			
				try{	
					  remote.enqueue(mail);			
				} catch(TEmailRuntimeException re){
						re.printStackTrace();							
				}catch (Exception e){
						e.printStackTrace();							
				}
			}	
		}		
		catch(SQLException re){
			re.printStackTrace();			
			throw new TEmailRuntimeException(re);				
		}
		catch (Exception e){
			e.printStackTrace();							
		}
		finally{
			close(con);
		}	
		return count;
	}
	
	
	public List getFailedTransactionList(int max_count,boolean isEmailContentReqd){
		Connection con=null;
		List failedTranList=null;
		try
		{
			con=getConnection();
			 failedTranList=TEmailInfoDAO.getFailedTransactions(con,max_count,isEmailContentReqd);				
		}		
		catch(SQLException re){
			re.printStackTrace();			
			throw new TEmailRuntimeException(re);				
		}
		catch (Exception e){
			e.printStackTrace();				
			throw new TEmailRuntimeException(e);
		}
		finally{
			close(con);
		}	
		return failedTranList;
	}
	
	
	public Map getFailedTransactionStats(){
		Connection con=null;
		Map failedTranList=null;
		try
		{
			con=getConnection();
			 failedTranList=TEmailInfoDAO.getFailedTransactionsDetails(con);				
		}		
		catch(SQLException re){
			re.printStackTrace();			
			throw new TEmailRuntimeException(re);				
		}
		catch (Exception e){
			e.printStackTrace();			
			throw new TEmailRuntimeException(e);
		}
		finally{
			close(con);
		}	
		return failedTranList;
	}
	

    /**
     * @return
     * @see com.freshdirect.fdstore.customer.ejb.FDServiceLocator#getMailerHome()
     */
    protected TEmailerGatewayHome getTMailerGatewayHome() {
        return LOCATOR.getTMailerGatewayHome();
    }

	

}
