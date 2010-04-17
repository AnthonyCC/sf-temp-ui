package com.freshdirect.giftcard.ejb;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.ERPSessionBeanSupport;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpCancelOrderModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.mail.EmailI;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.giftcard.EnumGiftCardStatus;
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardAuthModel;
import com.freshdirect.giftcard.ErpGiftCardBalanceTransferModel;
import com.freshdirect.giftcard.ErpGiftCardDlvConfirmModel;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.ErpGiftCardTransModel;
import com.freshdirect.giftcard.ErpGiftCardTransactionModel;
import com.freshdirect.giftcard.ErpGiftCardUtil;
import com.freshdirect.giftcard.ErpPostAuthGiftCardModel;
import com.freshdirect.giftcard.ErpPreAuthGiftCardModel;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.giftcard.ErpRegisterGiftCardModel;
import com.freshdirect.giftcard.ErpReverseAuthGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.mail.ErpEmailFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.mail.GiftCardOrderInfo;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.payment.EnumGiftCardTransactionStatus;
import com.freshdirect.payment.EnumGiftCardTransactionType;
import com.freshdirect.payment.GivexException;
import com.freshdirect.payment.GivexResponseModel;
import com.freshdirect.payment.ejb.GivexServerGateway;

public class GiftCardManagerSessionBean extends ERPSessionBeanSupport {
	
	private final static Logger LOGGER = LoggerFactory.getInstance(GiftCardManagerSessionBean.class);
	
	private final static int AUTH_HOURS;
	
	static {
		int hours = 48;
		try {
			hours = Integer.parseInt(ErpServicesProperties.getAuthHours());
		} catch (NumberFormatException ne) {
			LOGGER.warn("authHours is not in correct format", ne);
		}
		AUTH_HOURS = hours;
	}
	
	public void registerGiftCard(String saleId, double amount) throws FDResourceException, RemoteException, ErpTransactionException{
		registerGiftCard(saleId, amount,EnumTransactionSource.SYSTEM);
	}
	
	public void registerGiftCard(String saleId, double amount,EnumTransactionSource source) throws FDResourceException, RemoteException, ErpTransactionException{
		List recipentList;
		Connection conn = null;
		boolean isGCTransactionSuccess=true; 
		try{	
			ErpSaleEB saleEB=null;			
			saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			recipentList=saleEB.getCurrentOrder().getRecipientsList();
			ErpRegisterGiftCardModel regModel = saleEB.getRecentRegistration();
			int count = 0;
			if(regModel != null)
				count = regModel.getRegisterTranactionList().size();
			List assignedGiftcards = new ArrayList();
			conn = this.getConnection();
			List gcList = GiftCardPersistanceDAO.loadGiftCardbySaleId(conn, saleId);
			//regFailureList = getRecentRegistrationFailures(regModel);		
			ErpGiftCardTransModel model=createGiftCardTransModel(amount,saleId,EnumTransactionType.REGISTER_GIFTCARD);
			GivexResponseModel rspModel=null;		
			ErpGiftCardDlvConfirmModel dlvInfoTrans=(ErpGiftCardDlvConfirmModel)createGiftCardTransModel(amount,saleId,EnumTransactionType.GIFTCARD_DLV_CONFIRM);			
				for(int i=0;i<recipentList.size();i++){			
					String referenceId = convertToGivexReference(saleId, count++);				
					try{
						ErpRecipentModel recModel=(ErpRecipentModel)recipentList.get(i); 			
						String availableGCId = getAvailableGCIdFromList(gcList, assignedGiftcards, recModel.getAmount());
						if(availableGCId == null) {
							rspModel=GivexServerGateway.registerCard(recModel.getAmount(), referenceId);
							addGiftCardTransactionModel(rspModel,saleId,EnumGiftCardTransactionType.REGISTER,model, referenceId);
							String gcId=storeGiftCardInfo(convertGiftCardModel(model,rspModel, saleId));
							ErpGCDlvInformationHolder dlvModel=new ErpGCDlvInformationHolder();
							dlvModel.setRecepientModel(recModel);
							dlvModel.setGiftCardId(gcId);
							dlvModel.setGivexNum(rspModel.getGivexNumber());
							dlvInfoTrans.addGiftCardDlvInfo(dlvModel);
						} else {
							ErpGCDlvInformationHolder dlvModel=new ErpGCDlvInformationHolder();
							dlvModel.setRecepientModel(recModel);
							dlvModel.setGiftCardId(availableGCId);
							dlvInfoTrans.addGiftCardDlvInfo(dlvModel);
						}
					
					} catch (GivexException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();	
						ErpGiftCardTransactionModel gcModel=new ErpGiftCardTransactionModel();
						gcModel.setErrorMsg(e.getMessage());
						gcModel.setGiftCardTransactionType(EnumGiftCardTransactionType.REGISTER);
						gcModel.setGcTransactionStatus(EnumGiftCardTransactionStatus.FAILURE);
						//gcModel.setActionTime(new Date());
						gcModel.setTransactionAmount(model.getAmount());
						gcModel.setReferenceId(referenceId);
						ErpRegisterGiftCardModel transactionModel=(ErpRegisterGiftCardModel)model;
						transactionModel.addRegisterTransaction(gcModel);
						isGCTransactionSuccess=false;
						//Alert Appsupport. Send an email.
						String subject =  "[System] Unable to Register Gift Card. "+saleId;
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						String detailMessage = sw.toString();
						sendNotificationEmail(subject, detailMessage);

					}															
				}	
				model.setTransactionSource(source);	
				saleEB.addRegisterGiftCard(model);
								
				if(isGCTransactionSuccess)
				{
				
					//ErpEmailGiftCardModel emailModel=(ErpEmailGiftCardModel)createGiftCardTransModel(amount,saleId,EnumTransactionType.EMAIL_GIFTCARD);
					//emailModel.setRecepientsTranactionList(recipentList);
					saleEB.emailPending();															
					// now send the email
					// send email
					// one more no tweek :))						
					//Sending the GiftCard emails to all the applicable recipients.
					sendGiftCardToRecipients(dlvInfoTrans, false);
					saleEB.addGiftCardDeliveryConfirm(dlvInfoTrans);				
					
					//ErpDeliveryConfirmModel deliveryConfirmModel = new ErpDeliveryConfirmModel();
					//saleEB.addDeliveryConfirm(deliveryConfirmModel);
				}else
				{
					if(saleEB.getCurrentOrder().getSubTotal() <= 0.0){
						//THis is $0 card with no value created for balance transfer. At this point cancel the order.
						//since register transaction did not go through.
						ErpCancelOrderModel cancelOrder = new ErpCancelOrderModel();
						cancelOrder.setTransactionSource(source);
						cancelOrder.setTransactionInitiator("");
						saleEB.cancelGCOrder(cancelOrder);
						saleEB.cancelOrderComplete();
					} 
				}				
		} catch (FinderException ce) {
			throw new EJBException(ce);
		} catch (RemoteException re) {
			throw new EJBException(re);
		} catch (SQLException e) {
			LOGGER.warn("SQLException while loading gift cards by sale id.", e);
			throw new EJBException(e);
		} finally {
                    close(conn);
		}	
	}
	
	private String getAvailableGCIdFromList(List gcList, List assignedGiftcards, double recipientAmount){
		if(gcList == null || gcList.size() == 0)return null;
		for(Iterator it = gcList.iterator(); it.hasNext();){
			ErpGiftCardModel gcModel = (ErpGiftCardModel)it.next();
			String gcId = gcModel.getPK().getId();
			if(gcModel.getOriginalAmount() == recipientAmount && !assignedGiftcards.contains(gcId)){
				assignedGiftcards.add(gcId);
				return gcId;
			}
		}
		return null;
	}
	
	public List getGiftCardRecepientsForCustomer(String customerId) throws RemoteException,FDResourceException{
		Connection conn = null;
        List recepientList =null;
		try {
			conn = this.getConnection();			
			recepientList=GiftCardPersistanceDAO.loadGiftCardRecipents(conn, customerId);
		} catch (Exception e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
		return recepientList;
	}
	
	public Map getGiftCardRecepientsForOrders(List saleIds) throws RemoteException,FDResourceException{
		Connection conn = null;
        Map recipientListAll = new HashMap();
		try {
			if(null != saleIds && !saleIds.isEmpty()){
				for (Iterator iterator = saleIds.iterator(); iterator
						.hasNext();) {
					String saleId = (String) iterator.next();
					conn = this.getConnection();			
					List recepientList=GiftCardPersistanceDAO.loadGiftCardRecipentsBySaleId(conn, saleId);
					if(!recipientListAll.containsKey(saleId)){
						recipientListAll.put(saleId, recepientList);
					}
				}
			
			}
		} catch (Exception e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
		return recipientListAll;
	}
	
	public void resendGiftCard(String saleId,List recepientList,EnumTransactionSource source) throws RemoteException,FDResourceException{
		
		ErpSaleEB saleEB=null;			
		try {
			saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));			
			ErpGiftCardDlvConfirmModel dlvConfirmModel=(ErpGiftCardDlvConfirmModel)createGiftCardTransModel(0,saleId,EnumTransactionType.EMAIL_GIFTCARD);
			dlvConfirmModel.setDlvInfoTranactionList(recepientList);				
			saleEB.addGiftCardEmailInfo(dlvConfirmModel);			
			//storeGCEmailToRecipents()

			// send the email now
			sendGiftCardToRecipients(dlvConfirmModel, true);
			
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();					
		}catch (RemoteException e) {
			throw new EJBException("RemoteException occured processing sale " + saleId, e);
	
		} 
		catch (ErpTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	   
		
		public void addGiftCardTransactionModel(GivexResponseModel model,String saleId,EnumGiftCardTransactionType transType, ErpGiftCardTransModel tranModel, String referenceId)
		{						
			if(transType==EnumGiftCardTransactionType.REGISTER)
			{			
				ErpRegisterGiftCardModel transactionModel=(ErpRegisterGiftCardModel)tranModel;
				ErpGiftCardTransactionModel gcModel=new ErpGiftCardTransactionModel();
				gcModel.setAuthCode(""+model.getAuthCode());
				gcModel.setGivexNum(model.getGivexNumber());
				gcModel.setCertificateNumber(ErpGiftCardUtil.getCertificateNumber(model.getGivexNumber()));
				gcModel.setGcTransactionStatus(EnumGiftCardTransactionStatus.SUCCESS);
				gcModel.setGiftCardTransactionType(EnumGiftCardTransactionType.REGISTER);
				gcModel.setSecurityCode(model.getSecurityCode());
				gcModel.setTransactionAmount(model.getAmount());
				gcModel.setReferenceId(referenceId);
				transactionModel.addRegisterTransaction(gcModel);
			}		
			if(transType==EnumGiftCardTransactionType.BALANCE_TRANSFER)
			{			
				ErpGiftCardBalanceTransferModel transactionModel=(ErpGiftCardBalanceTransferModel)tranModel;
				ErpGiftCardTransactionModel gcModel=new ErpGiftCardTransactionModel();
				gcModel.setAuthCode(""+model.getAuthCode());
				gcModel.setGivexNum(model.getGivexNumber());
				gcModel.setCertificateNumber(ErpGiftCardUtil.getCertificateNumber(model.getGivexNumber()));
				gcModel.setGcTransactionStatus(EnumGiftCardTransactionStatus.SUCCESS);
				gcModel.setGiftCardTransactionType(EnumGiftCardTransactionType.BALANCE_TRANSFER);
				gcModel.setSecurityCode(model.getSecurityCode());
				gcModel.setTransactionAmount(model.getAmount());
				gcModel.setReferenceId(referenceId);
				transactionModel.setBalanceTransfer(gcModel);
			}		
			
		}
		
		public ErpGiftCardTransModel createGiftCardTransModel(double amount,String saleId,EnumTransactionType transType)
		{
			ErpGiftCardTransModel regModel=null;
			
			if(transType==EnumTransactionType.REGISTER_GIFTCARD)
			{			
				regModel=new ErpRegisterGiftCardModel();
	            regModel.setTransactionSource(EnumTransactionSource.WEBSITE);
	            regModel.setTransactionDate(new Date());
	            regModel.setTax(0);
	            regModel.setAmount(amount);	            
			}
			else if(transType==EnumTransactionType.EMAIL_GIFTCARD)
			{			
				regModel=new ErpGiftCardDlvConfirmModel(EnumTransactionType.EMAIL_GIFTCARD);
	            regModel.setTransactionSource(EnumTransactionSource.WEBSITE);
	            regModel.setTransactionDate(new Timestamp(System.currentTimeMillis()));
	            regModel.setTax(0);
	            regModel.setAmount(amount);	            
			}
			if(transType==EnumTransactionType.GIFTCARD_DLV_CONFIRM)
			{			
				regModel=new ErpGiftCardDlvConfirmModel(EnumTransactionType.GIFTCARD_DLV_CONFIRM);
	            regModel.setTransactionSource(EnumTransactionSource.WEBSITE);
	            regModel.setTransactionDate(new Date());
	            regModel.setTax(0);
	            regModel.setAmount(amount);	            
			}
			if(transType==EnumTransactionType.BALANCETRANSFER_GIFTCARD)
			{			
				regModel=new ErpGiftCardBalanceTransferModel();
	            regModel.setTransactionSource(EnumTransactionSource.WEBSITE);
	            regModel.setTransactionDate(new Date());
	            regModel.setTax(0);
	            regModel.setAmount(amount);	            
			}
				
			return regModel;
		}
		
		
	private List getRecentRegistrationFailures(ErpRegisterGiftCardModel regModel) {
		List regFailureList = new ArrayList();
		List regTranList = regModel.getRegisterTranactionList();
		for (Iterator i = regTranList.iterator(); i.hasNext();) {
			ErpGiftCardTransactionModel tranModel = (ErpGiftCardTransactionModel) i.next();
			if(tranModel.getGcTransactionStatus().equals(EnumGiftCardTransactionStatus.FAILURE)){
				regFailureList.add(tranModel);
			}
		}
		return regFailureList;
	}
	
	private void storeGCEmailToRecipents(String gcId,List recipentsList,String salesactionId) throws FDResourceException{
		// contact email service and send email		
		Connection conn = null;
        String id=null;
		try {
			conn = this.getConnection();			
			GiftCardPersistanceDAO.storeGCRecipentsList(conn, gcId, recipentsList,salesactionId);
		} catch (Exception e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}				
	}
	
	
	private ErpGiftCardModel convertGiftCardModel(ErpGiftCardTransModel model,GivexResponseModel resModel,String saleId){
		ErpGiftCardModel gcModel=null;
		if(model instanceof ErpRegisterGiftCardModel){
			model=(ErpRegisterGiftCardModel)model;
			gcModel=new ErpGiftCardModel();
			gcModel.setAccountNumber(resModel.getGivexNumber());
			gcModel.setBalance(resModel.getAmount());
			gcModel.setCardType(EnumCardType.GCP);
			gcModel.setPurchaseSaleId(saleId);
			gcModel.setAccountNumber(resModel.getGivexNumber());
		}	
		return gcModel;
	}
	
	
	public String storeGiftCardInfo(ErpGiftCardModel gcModel) throws SQLException
	{
		Connection conn = null;
        String id=null;
		try {
			conn = this.getConnection();			
			id=GiftCardPersistanceDAO.storeGiftCardModel(conn,gcModel);
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
                    close(conn);
		}
		return id;		
	}
	

		public List loadRecipentsForOrder(String saleId){
			List recList=null;
			try{
				recList=GiftCardPersistanceDAO.loadGiftCardRecipentsBySaleId(getConnection(), saleId);
			}catch (Exception e) {
				LOGGER.warn("Unexpected Exception while trying to process invoice for order#: "+saleId, e);
				throw new EJBException("Unexpected Exception while trying to process invoice for order#: "+saleId, e);
			}
			return recList;
		}
	
		
		
	   public ErpGiftCardModel validateAndGetGiftCardBalance(String givexNum) throws InvalidCardException{
		   
		   Connection conn = null;
			ErpGiftCardModel giftCard =  null;
			//First validate against givex.
			try {
				conn = getConnection();				
				giftCard = GiftCardPersistanceDAO.loadGiftCardModel(conn, givexNum);
				if(giftCard == null){
					//GC doesn't exist on FD side.
					throw new InvalidCardException("Invalid Gift Certificate Number.");
				}
				ErpGiftCardModel gcModel = new ErpGiftCardModel();
				gcModel.setAccountNumber(givexNum);
				GivexResponseModel response = GivexServerGateway.getBalance(gcModel);
				//Validation is sucessful. Load the information from the database.
				//Set the card status as active. 
				giftCard.setStatus(EnumGiftCardStatus.ACTIVE);
				//Set the actual balance from Givex
				giftCard.setBalance(response.getCertBalance());
				/*
				 * There can be pending auths on a  gift card that was partially used by a customer
				 * and later gave remaining balance to his friend.
				 */
				applyPendingAuthsToBalance(conn, giftCard);

			}catch(GivexException ge) {
				if(ge.getErrorCode() == GivexException.ERROR_CERT_NOT_EXIST) {
					throw new InvalidCardException("Invalid Gift Certificate Number.");
				} else {					
					if(ge.getErrorCode() < 0) {
						//Probably a system exception. Connectivity to Givex failed or transaction timed out. Log the error and proceed.
						LOGGER.error("System error occurred while verifying status of Gift certificate. Certificate Number : "
																+ ((null != giftCard)?giftCard.getCertificateNumber():""), ge);
					} else {
						//Some issue with Gift certificate either card on hold or cancelled or ?
						if(ge.getErrorCode() == GivexException.ERROR_CERT_ON_HOLD) {
							LOGGER.error("Gift Certificate on Hold. Certificate Number : "+((null != giftCard)?giftCard.getCertificateNumber():""));
						} else {
							LOGGER.error("This Gift Certificate has an issue. "+ge.getMessage()+" Certificate Number : "
																		+((null != giftCard)?giftCard.getCertificateNumber():""));
						}
						giftCard.setStatus(EnumGiftCardStatus.INACTIVE);
					}										
				}
			}catch(IOException ie) {
				throw new EJBException(ie);
			}catch (SQLException e) {
				LOGGER.warn("SQLException while cancelling the delivery pass.", e);
				throw new EJBException(e);
			} finally {
	                    close(conn);
			}
			return giftCard;
		   
		   
	   }
	   
	   
	   public void transferGiftCardBalance(String fromGivexNum,String toGivexNum,double amount) throws InvalidCardException{
		   Connection conn = null;
		   try {
			   conn = getConnection();		
			   ErpGiftCardModel model=GiftCardPersistanceDAO.loadGiftCardModel(conn, toGivexNum);
			   String saleId=model.getPurchaseSaleId();
			   ErpSaleEB saleEB=null;			
			   saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));			   
			   ErpGiftCardModel fromModel=new ErpGiftCardModel();
			   ErpGiftCardModel toModel=new ErpGiftCardModel();
			   fromModel.setAccountNumber(fromGivexNum);
			   toModel.setAccountNumber(toGivexNum);	
			   ErpSaleModel saleModel = (ErpSaleModel)saleEB.getModel();				
			   int count = saleModel.getGCTransactions().size();
			   String referenceId=convertToGivexReference(saleId, count++);					   
			   GivexResponseModel rspModel= GivexServerGateway.transferBalance(fromModel, toModel, amount,referenceId);
			   rspModel.setGivexNumber(toGivexNum);
			   ErpGiftCardTransModel btTransModel=createGiftCardTransModel(amount,saleId,EnumTransactionType.BALANCETRANSFER_GIFTCARD);
			 
			   addGiftCardTransactionModel( rspModel, saleId, EnumGiftCardTransactionType.BALANCE_TRANSFER, btTransModel, referenceId);
			   saleEB.addGiftCardBalanceTransfer(btTransModel); 

//			   sendGiftCardBalanceTransferEmail(fromGivexNum, conn);
			   
		   }catch(GivexException ge) {
				if(ge.getErrorCode() == GivexException.ERROR_CERT_NOT_EXIST) {
					throw new InvalidCardException("Invalid Gift Certificate Number.");
				} else {
					throw new EJBException(ge);
				}
			}catch(FinderException e){
				e.printStackTrace();
				throw new EJBException(e);
			}
		     catch(IOException ie) {
		    	 ie.printStackTrace();
				throw new EJBException(ie);
			}
			catch(ErpTransactionException e){
				throw new EJBException(e);
			}catch (SQLException e) {
				LOGGER.warn("SQLException while cancelling the delivery pass.", e);
				throw new EJBException(e);
			}/*catch(FDResourceException fdre){
				throw new EJBException(fdre);
			}*/ finally {
	                    close(conn);
			}		   		   
	   }

	/**
	 * Method to send the email with successful gift card balance transfer message to the gift card user.
	 * 
	 * @param fromGivexNum
	 * @param conn
	 * @throws SQLException
	 * @throws FinderException
	 * @throws RemoteException
	 * @throws FDResourceException
	 */
	/*private void sendGiftCardBalanceTransferEmail(String fromGivexNum,
			Connection conn) throws SQLException,
			FinderException, RemoteException, FDResourceException {
		ErpGCDlvInformationHolder erpGCDlvInformationHolder = GiftCardPersistanceDAO.loadGiftCardRecipentByGivexNum(conn, fromGivexNum);
		   if(null != erpGCDlvInformationHolder){
			   ErpRecipentModel recipientModel = erpGCDlvInformationHolder.getRecepientModel();
			   String recipientName = recipientModel.getRecipientName();			   
			   
			   ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(recipientModel.getCustomerId()));
			   ErpCustomerInfoModel custInfoModel = customerEB.getCustomerInfo();
			   
			   this.doEmail(ErpEmailFactory.getInstance().createGCBalanceTransferEmail(recipientName, custInfoModel));
		   }
	}*/
		   			
	
		public ErpGiftCardModel validate(String givexNum) throws InvalidCardException, CardInUseException, CardOnHoldException{
		Connection conn = null;
		//First validate against givex.
		try {
			ErpGiftCardModel gcModel = new ErpGiftCardModel();
			gcModel.setAccountNumber(givexNum);
			GivexResponseModel response = GivexServerGateway.getBalance(gcModel);
			//Validation is sucessful. Load the information from the database.
			conn = getConnection();
			String owner = GiftCardPersistanceDAO.lookupOwner(conn, givexNum);
			if(owner != null){
				CardInUseException ce = new CardInUseException("Card Already in Use.");
				ce.setCardOwner(owner);
				//The gift card is already in use.		
				throw ce;
			}
			ErpGiftCardModel giftCard = GiftCardPersistanceDAO.loadGiftCardModel(conn, givexNum);
			if(giftCard == null){
				//Certificate doesn't exist in FD side. Throw Invalid cert exception.
				throw new InvalidCardException("Invalid Gift Certificate Number.");
			}
			//Set the card status as active. 
			giftCard.setStatus(EnumGiftCardStatus.ACTIVE);
			//Set the actual balance from Givex
			giftCard.setBalance(response.getCertBalance());
			/*
			 * There can be pending auths on a  gift card that was partially used by a customer
			 * and later gave remaining balance to his friend.
			 */
			applyPendingAuthsToBalance(conn, giftCard);
			return giftCard;
		}catch(GivexException ge) {
			if(ge.getErrorCode() == GivexException.ERROR_CERT_NOT_EXIST || ge.getErrorCode()== GivexException.ERROR_CERT_CANCELLED) {
				throw new InvalidCardException("Invalid Gift Certificate Number. Error Code:"+ge.getErrorCode());
			} else if(ge.getErrorCode() == GivexException.ERROR_CERT_ON_HOLD) {
				throw new CardOnHoldException("Certificate on Hold");
			} else {
				throw new EJBException(ge);
			}
		}catch(IOException ie) {
			throw new EJBException(ie);
		}catch (SQLException e) {
			LOGGER.warn("SQLException while cancelling the delivery pass.", e);
			throw new EJBException(e);
		} finally {
                    close(conn);
		}
		
	}
	
	public List verifyStatusAndBalance(List giftcards, boolean reloadBalance ) {
		 List verifiedList = new ArrayList();
		 for(Iterator iter = giftcards.iterator(); iter.hasNext();) {
			 ErpGiftCardModel giftcard = (ErpGiftCardModel) iter.next();
			 giftcard = verifyStatusAndBalance(giftcard, reloadBalance);
			 //if(giftcard.getBalance() > 0 )
			 verifiedList.add(giftcard);
		 }
		 return verifiedList;
	}
	
	public ErpGiftCardModel verifyStatusAndBalance(ErpGiftCardModel giftcard, boolean reloadBalance) {
		Connection conn = null;
		GivexResponseModel response = null;
		try {
			//Default Behaviour - Get balance from database.
			conn = getConnection();
			ErpGiftCardModel gc = GiftCardPersistanceDAO.loadGiftCardModel(conn, giftcard.getAccountNumber());
			giftcard.setBalance(gc.getBalance());
			giftcard.setOriginalAmount(gc.getOriginalAmount());
			giftcard.setPurchaseDate(gc.getPurchaseDate());
			List validPreAuths = getValidPreAuths(conn, giftcard);
			if(giftcard.getBalance() <= 0 && validPreAuths.size() == 0) {
				//Ignore zero balance gift card that has no any pending auths.				
				giftcard.setStatus(EnumGiftCardStatus.ZERO_BALANCE);
				return giftcard;
			}
	
			//First validate against givex.
			try {
				ErpGiftCardModel gcModel = new ErpGiftCardModel();
				gcModel.setAccountNumber(giftcard.getAccountNumber());
				response = GivexServerGateway.getBalance(gcModel);
				//Validation is sucessful. Set the card status as active.
				giftcard.setStatus(EnumGiftCardStatus.ACTIVE);
			}catch(GivexException ge) {
				if(ge.getErrorCode() < 0) {
					//Probably a system exception. Connectivity to Givex failed or transaction timed out. Log the error and proceed.
					LOGGER.error("System error occurred while verifying status of Gift certificate. Certificate Number : "
															+giftcard.getCertificateNumber(), ge);
				} else {
					//Some issue with Gift certificate either card on hold or cancelled or ?
					if(ge.getErrorCode() == GivexException.ERROR_CERT_ON_HOLD) {
						LOGGER.error("Gift Certificate on Hold. Certificate Number : "+giftcard.getCertificateNumber());
					} else {
						LOGGER.error("This Gift Certificate has an issue. "+ge.getMessage()+" Certificate Number : "
																	+giftcard.getCertificateNumber());
					}
					giftcard.setStatus(EnumGiftCardStatus.INACTIVE);
				}
			}catch(IOException ie) {
				//Log the error and proceed.
				LOGGER.error("IO error occurred while verifying status of Gift certificate. Certificate Number : "
														+giftcard.getCertificateNumber(), ie);
			}
			if(giftcard.isRedeemable() && reloadBalance) {
				//Set the actual balance from Givex
				if(giftcard.getStatus().equals(EnumGiftCardStatus.ACTIVE)) {
					giftcard.setBalance(response.getCertBalance());	
				} 
				applyPendingAuthsToBalance(conn, giftcard);
				if(giftcard.getBalance() <= 0.0) {
					giftcard.setStatus(EnumGiftCardStatus.ZERO_BALANCE);
				}
			}
		}catch (SQLException e) {
			LOGGER.warn("SQLException while applying pending auths to balance.", e);
			throw new EJBException(e);
		} finally {
                    close(conn);
		}
		return giftcard;
	}
	
	private void applyPendingAuthsToBalance(Connection conn, ErpGiftCardModel giftcard) throws SQLException {
		double balance = giftcard.getBalance(); 
		//Apply any pending pre-auth transactions to the balance.
		List pendPreAuths = GiftCardPersistanceDAO.loadPendingPreAuths(conn, giftcard.getAccountNumber());
		for(Iterator iter = pendPreAuths.iterator(); iter.hasNext();) {
			ErpGiftCardAuthModel transModel = (ErpGiftCardAuthModel) iter.next();
			if(transModel.getGCTransactionType().equals(EnumGiftCardTransactionType.PRE_AUTH)){
				balance = balance - transModel.getAmount();
			}
			else if(transModel.getGCTransactionType().equals(EnumGiftCardTransactionType.REVERSE_PRE_AUTH)){
				balance = balance + transModel.getAmount();
			}
		}
		giftcard.setBalance(MathUtil.roundDecimal(balance));
	}
	
	private List getValidPreAuths(Connection conn, ErpGiftCardModel giftcard) throws SQLException {
		//Apply any pending pre-auth transactions to the balance.
		List validPreAuths = GiftCardPersistanceDAO.loadValidPreAuths(conn, giftcard.getAccountNumber());
		return validPreAuths;
	}
	
	public void initiatePreAuthorization(String saleId) throws ErpTransactionException{
		try{
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpAbstractOrderModel order = saleEB.getCurrentOrder();
			//ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(order.getCustomerId()));
			
			List giftCardList = ErpGiftCardUtil.getGiftcardPaymentMethods(order.getAppliedGiftcards());
			ErpSaleModel model = (ErpSaleModel)saleEB.getModel();				
			int count = model.getGCTransactions().size();
			List appliedCerts  = new ArrayList(); 
			for(Iterator it = giftCardList.iterator(); it.hasNext();){
				ErpPaymentMethodI pm = (ErpPaymentMethodI) it.next();
				double appliedAmount = ErpGiftCardUtil.getAppliedAmount(pm.getCertificateNumber(), order.getAppliedGiftcards());
				if(appliedAmount == 0.0){
					//nothing to pre-auth 
					continue;
				}
				String referenceId = convertToGivexReference(saleId, ++count);
				//Valid GC auth = approved auth + pending auth.
				List validAuths = saleEB.getValidGCAuthorizations(pm);
				if(validAuths.size() == 0){
					//No Pre Auth exists. create a new one pre auth for applied amount.
					ErpPreAuthGiftCardModel auth = createPreAuthTransaction(referenceId, pm.getCertificateNumber(), appliedAmount);
					saleEB.addGCPreAuthorization(auth);
				}else {
					ErpPreAuthGiftCardModel existingAuth = (ErpPreAuthGiftCardModel) validAuths.get(0);
					double authAmount = existingAuth.getAmount();
					if(Math.round(authAmount - appliedAmount) != 0.0){
						//Cancel/Reverse the existing pre-auth if applied amount has changed. Create a new pre-auth.

						if(existingAuth.isPending()){
							//cancel the auth.
							saleEB.cancelGCPreAuthorization(existingAuth);
						}else {
							//Auth has been approved. Reverse it.
							ErpReverseAuthGiftCardModel rauth = createReverseAuthTransaction(referenceId, pm.getCertificateNumber(), 
																existingAuth.getAuthCode(), existingAuth.getAmount());
							saleEB.addReverseGCPreAuthorization(rauth);
						}
						//Add a n.ew pre-auth for new amount
						ErpPreAuthGiftCardModel auth = createPreAuthTransaction(referenceId, pm.getCertificateNumber(), appliedAmount);
						saleEB.addGCPreAuthorization(auth);
					}else {
						//Retain the existing pre-auth. Do nothing.
					}
				}
				appliedCerts.add(pm.getCertificateNumber());
			}
			//Reverse or cancel pre-auths on gift cards that was used during last modification and removed in the current session.
			List validAuths = saleEB.getValidGCAuthorizations();
			for(Iterator it = validAuths.iterator(); it.hasNext();){
				ErpPreAuthGiftCardModel auth = (ErpPreAuthGiftCardModel) it.next();
				if(!appliedCerts.contains(auth.getCertificateNum())){
					//This gift card is not applied currently so reverse/cancel the auth associated with that.
					String referenceId = convertToGivexReference(saleId, ++count);
					if(auth.isPending()){
						//cancel the auth.
						saleEB.cancelGCPreAuthorization(auth);
					}else {
						//Auth has been approved. Reverse it.
						ErpReverseAuthGiftCardModel rauth = createReverseAuthTransaction(referenceId, auth.getCertificateNum(), 
								auth.getAuthCode(), auth.getAmount());
						saleEB.addReverseGCPreAuthorization(rauth);
					}
				}
			}
			
		} catch (FinderException fe) {
			LOGGER.warn("FinderExceptin while trying to locate Sale Entity Bean", fe);
			throw new EJBException(fe);
		} catch (RemoteException re) {
			LOGGER.warn("RemoteException while trying to talk to Sale Entity Bean", re);
			throw new EJBException(re);
		}
		
	}

	public void initiateCancelAuthorizations(String saleId) throws ErpTransactionException {
		try{
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpAbstractOrderModel order = saleEB.getCurrentOrder();
			//ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(order.getCustomerId()));
			List giftCardList = ErpGiftCardUtil.getGiftcardPaymentMethods(order.getAppliedGiftcards());
			ErpSaleModel model = (ErpSaleModel)saleEB.getModel();				
			int count = model.getGCTransactions().size();
			for(Iterator it = giftCardList.iterator(); it.hasNext();){
				ErpPaymentMethodI pm = (ErpPaymentMethodI) it.next();
				//Valid GC auth = approved auth + pending auth.
				List validAuths = saleEB.getValidGCAuthorizations(pm);
				if(validAuths.size() > 0){
					//There are valid auths to cancel/reverse.
					String referenceId = convertToGivexReference(saleId, ++count);
					//Cancel/Reverse the existing pre-auth. Create a new pre-auth.
					ErpPreAuthGiftCardModel existingAuth = (ErpPreAuthGiftCardModel) validAuths.get(0);
					if(existingAuth.isPending()){
						//cancel the auth.
						saleEB.cancelGCPreAuthorization(existingAuth);
					}else {
						//Auth has been approved. Reverse it.
						ErpReverseAuthGiftCardModel rauth = createReverseAuthTransaction(referenceId, pm.getCertificateNumber(), 
																existingAuth.getAuthCode(), existingAuth.getAmount());
						saleEB.addReverseGCPreAuthorization(rauth);
					}
				}
			}
			
		} catch (FinderException fe) {
			LOGGER.warn("FinderExceptin while trying to locate Sale Entity Bean", fe);
			throw new EJBException(fe);
		} catch (RemoteException re) {
			LOGGER.warn("RemoteException while trying to talk to Sale Entity Bean", re);
			throw new EJBException(re);
		}
		
	}
	
	
	public List getGiftCardModel(GenericSearchCriteria resvCriteria){
		Connection conn = null;
        String id=null;
        List model;
		try {
			conn = this.getConnection();			
			model=GiftCardPersistanceDAO.loadGiftCardRecipents(conn,resvCriteria);
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
                    close(conn);
		}
		return model;		
	}
	
	public List preAuthorizeSales(String saleId) {
		List errorList = new ArrayList();
		try{
			
			if (!"true".equalsIgnoreCase(ErpServicesProperties.getPreAuthorize())) {
				return errorList;
			}			
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpAbstractOrderModel order = saleEB.getCurrentOrder();
			long currentTime = System.currentTimeMillis();
			long difference = order.getDeliveryInfo().getDeliveryStartTime().getTime() - currentTime;
			difference = difference / (1000 * 60 * 60);

			if (difference > AUTH_HOURS) {
				return errorList;
			}			
			//ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(saleEB.getCustomerPk()));
			List giftCardList = ErpGiftCardUtil.getGiftcardPaymentMethods(order.getAppliedGiftcards());
			for(Iterator it = giftCardList.iterator(); it.hasNext();){
				ErpPaymentMethodI pm = (ErpPaymentMethodI) it.next();
				List rAuths = saleEB.getPendingGCReverseAuths(pm);
				if(rAuths != null && rAuths.size() > 0) {
					//If reverse auth is found for this gift card then post cancel pre auth to Givex.
					ErpReverseAuthGiftCardModel rauth = (ErpReverseAuthGiftCardModel) rAuths.get(0);
					GivexResponseModel rspModel = null;
					try{
						rspModel = GivexServerGateway.cancelPreAuthorization(pm, new Long(rauth.getPreAuthCode()).longValue(),
																	rauth.getReferenceId());
						rauth.setGcTransactionStatus(EnumGiftCardTransactionStatus.SUCCESS);
						rauth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
						//Reset the error message.
						rauth.setErrorMsg("");
					}catch(GivexException ge){
						if(ge.getErrorCode() > 0){
							rauth.setGcTransactionStatus(EnumGiftCardTransactionStatus.FAILURE);
							//rauth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
							errorList.add(rauth);
						} else {
							//System failed to connect to Givex. Leave the status in pending.
						}
						rauth.setErrorMsg(ge.getMessage());
					} 	
					saleEB.updateGCAuthorization(rauth);
				} 
				//If pre auth is found for this gift card then post pre auth to Givex.
				List pAuths = saleEB.getPendingGCAuthorizations(pm);
				if(pAuths == null || pAuths.size() == 0) continue;
				//Check if there are any reverse auth pending for this certificate foe this order. If yes then do not proceed
				//with pre-auth unless reverse-auth is cleared.
				rAuths = saleEB.getPendingGCReverseAuths(pm);
				if(rAuths != null && rAuths.size() > 0) continue;
				ErpPreAuthGiftCardModel auth = (ErpPreAuthGiftCardModel) pAuths.get(0);
				GivexResponseModel rspModel = null;
				try{
					rspModel = GivexServerGateway.preAuthGiftCard(pm, auth.getAmount(), auth.getReferenceId());
					auth.setGcTransactionStatus(EnumGiftCardTransactionStatus.SUCCESS);
					auth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
					//Reset the error message.
					auth.setErrorMsg("");
				}catch(GivexException ge){
					if(ge.getErrorCode() > 0){
						auth.setGcTransactionStatus(EnumGiftCardTransactionStatus.FAILURE);
						//auth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
						errorList.add(auth);
					}else{
						//System failed to connect to Givex. Leave the status in pending.
					}
					auth.setErrorMsg(ge.getMessage());
				} 	
				saleEB.updateGCAuthorization(auth);
				//Update the final GC balance from givex to DB
				updateBalance(saleId, pm);
			}
			//Check if there are any other pending reverse pre-auths other than current order.
			//If so process them
			List pendingRevAuths =  saleEB.getPendingReverseGCAuthorizations();
			for(Iterator it = pendingRevAuths.iterator(); it.hasNext();){
				ErpReverseAuthGiftCardModel rauth = (ErpReverseAuthGiftCardModel) it.next();
				ErpGiftCardModel pm = new ErpGiftCardModel();
				pm.setAccountNumber(getAccountNumber(rauth.getCertificateNum()));
				GivexResponseModel rspModel = null;
				try{
					rspModel = GivexServerGateway.cancelPreAuthorization(pm, new Long(rauth.getPreAuthCode()).longValue(),
																rauth.getReferenceId());
					rauth.setGcTransactionStatus(EnumGiftCardTransactionStatus.SUCCESS);
					rauth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
					//Reset the error message.
					rauth.setErrorMsg("");
				}catch(GivexException ge){
					if(ge.getErrorCode() > 0){
						rauth.setGcTransactionStatus(EnumGiftCardTransactionStatus.FAILURE);
						//rauth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
						errorList.add(rauth);
					} else {
						//System failed to connect to Givex. Leave the status in pending.
					}
					rauth.setErrorMsg(ge.getMessage());
				} 	
				saleEB.updateGCAuthorization(rauth);
				//Update the final GC balance from givex to DB
				updateBalance(saleId, pm);
				
			}
			
		}catch (FinderException fe) {
			LOGGER.warn("FinderExceptin while trying to locate Sale Entity Bean", fe);
			throw new EJBException(fe);
		} catch (RemoteException re) {
			LOGGER.warn("RemoteException while trying to talk to Sale Entity Bean", re);
			throw new EJBException(re);
		}catch (SQLException se) {
			LOGGER.warn("FinderExceptin while trying to pre authorize sales", se);
			throw new EJBException(se);
		} 

		return errorList;
	}

	public List reversePreAuthForCancelOrders(String saleId) {
		List errorList = new ArrayList();
		try{
			
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpAbstractOrderModel order = saleEB.getCurrentOrder();
			//ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(saleEB.getCustomerPk()));
			List giftCardList = ErpGiftCardUtil.getGiftcardPaymentMethods(order.getAppliedGiftcards());
			for(Iterator it = giftCardList.iterator(); it.hasNext();){
				ErpPaymentMethodI pm = (ErpPaymentMethodI) it.next();
				List rAuths = saleEB.getPendingGCReverseAuths(pm);
				if(rAuths != null && rAuths.size() > 0) {
					//If reverse auth is found for this gift card then post cancel pre auth to Givex.
					ErpReverseAuthGiftCardModel rauth = (ErpReverseAuthGiftCardModel) rAuths.get(0);
					GivexResponseModel rspModel = null;
					try{
						rspModel = GivexServerGateway.cancelPreAuthorization(pm, new Long(rauth.getPreAuthCode()).longValue(),
																	rauth.getReferenceId());
						rauth.setGcTransactionStatus(EnumGiftCardTransactionStatus.SUCCESS);
						//Reset the error message.
						rauth.setErrorMsg("");
						rauth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
					}catch(GivexException ge){
						if(ge.getErrorCode() > 0){
							rauth.setGcTransactionStatus(EnumGiftCardTransactionStatus.FAILURE);
							//rauth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
							errorList.add(rauth);
						} else {
							//System failed to connect to Givex. Leave the status in pending.
						}
						rauth.setErrorMsg(ge.getMessage());
					} 	
					saleEB.updateGCAuthorization(rauth);
					//Update the final GC balance from givex to DB
					updateBalance(saleId, pm);
				} 
			}
			//Check if there are any other pending reverse pre-auths other than current order.
			//If so process them
			List pendingRevAuths =  saleEB.getPendingReverseGCAuthorizations();
			for(Iterator it = pendingRevAuths.iterator(); it.hasNext();){
				ErpReverseAuthGiftCardModel rauth = (ErpReverseAuthGiftCardModel) it.next();
				ErpGiftCardModel pm = new ErpGiftCardModel();
				pm.setAccountNumber(getAccountNumber(rauth.getCertificateNum()));
				GivexResponseModel rspModel = null;
				try{
					rspModel = GivexServerGateway.cancelPreAuthorization(pm, new Long(rauth.getPreAuthCode()).longValue(),
																rauth.getReferenceId());
					rauth.setGcTransactionStatus(EnumGiftCardTransactionStatus.SUCCESS);
					rauth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
					//Reset the error message.
					rauth.setErrorMsg("");
				}catch(GivexException ge){
					if(ge.getErrorCode() > 0){
						rauth.setGcTransactionStatus(EnumGiftCardTransactionStatus.FAILURE);
						//rauth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
						errorList.add(rauth);
					} else {
						//System failed to connect to Givex. Leave the status in pending.
					}
					rauth.setErrorMsg(ge.getMessage());
				} 	
				saleEB.updateGCAuthorization(rauth);
				//Update the final GC balance from givex to DB
				updateBalance(saleId, pm);
				
			}			
		}catch (FinderException fe) {
			LOGGER.warn("FinderExceptin while trying to locate Sale Entity Bean", fe);
			throw new EJBException(fe);
		} catch (RemoteException re) {
			LOGGER.warn("RemoteException while trying to talk to Sale Entity Bean", re);
			throw new EJBException(re);
		}catch (SQLException se) {
			LOGGER.warn("FinderExceptin while trying to pre authorize sales", se);
			throw new EJBException(se);
		}
		return errorList;
	}
	
	private void updateBalance(String saleId, ErpPaymentMethodI pm) {
		Connection conn = null;
		try{
			//Update new GC balance to DB from Givex.
			GivexResponseModel balResponse = GivexServerGateway.getBalance(pm);
			conn = getConnection();
			GiftCardPersistanceDAO.updateBalance(conn, (ErpGiftCardModel)pm, balResponse.getCertBalance());
		}catch(GivexException ge){
			LOGGER.error("Givex Exception occurred while updating the balance after PRE-Auth. "+saleId+" "+ge.getMessage());
		} catch(IOException ie){
			LOGGER.error("IO Exception occurred while updating the balance after PRE-Auth. "+saleId+" "+ie.getMessage());
		}catch(SQLException se){
			LOGGER.error("SQL Exception occurred while updating the balance after PRE-Auth. "+saleId+" "+se.getMessage());
		}finally{
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
	}
	
	public ErpPreAuthGiftCardModel createPreAuthTransaction(String referenceId, String certificationNum, double amount)
	{
		ErpPreAuthGiftCardModel tranModel = new ErpPreAuthGiftCardModel();
		tranModel.setAmount(amount);
		tranModel.setGcTransactionStatus(EnumGiftCardTransactionStatus.PENDING);
		tranModel.setGCTransactionType(EnumGiftCardTransactionType.PRE_AUTH);
		tranModel.setCertificateNum(certificationNum);	
		tranModel.setReferenceId(referenceId);
		tranModel.setTransactionSource(EnumTransactionSource.SYSTEM);
		return tranModel;
	}

	public ErpReverseAuthGiftCardModel createReverseAuthTransaction(String referenceId, String certificationNum, String preAuthCode, double amount)
	{
		ErpReverseAuthGiftCardModel tranModel = new ErpReverseAuthGiftCardModel();
		tranModel.setAmount(amount);
		tranModel.setGcTransactionStatus(EnumGiftCardTransactionStatus.PENDING);
		tranModel.setGCTransactionType(EnumGiftCardTransactionType.REVERSE_PRE_AUTH);
		tranModel.setCertificateNum(certificationNum);	
		tranModel.setReferenceId(referenceId);
		tranModel.setPreAuthCode(preAuthCode);
		tranModel.setTransactionSource(EnumTransactionSource.SYSTEM);
		return tranModel;
	}
	
	public List getGiftCardOrdersForCustomer(String erpCustomerPK) throws RemoteException, FDResourceException{
		Connection conn = null;
        List recepientList =null;
		try {
			conn = this.getConnection();			
			recepientList=GiftCardPersistanceDAO.loadGiftCardOrders(conn, erpCustomerPK);
		} catch (Exception e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("error while saving customer request", e);
			}
		}
		return recepientList;
	}
	
	public String getAccountNumber(String certificateNum) throws SQLException{
		Connection conn = null;
        String accountNumber =null;
		try {
			conn = this.getConnection();			
			accountNumber=GiftCardPersistanceDAO.getAccountNumber(conn, certificateNum);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("error while getting account number", e);
			}
		}
		return accountNumber;
	}
	
	private String convertToGivexReference(String saleId, int count){
		return saleId+"X"+count;
	}

	public Object getGiftCardRedeemedOrders(String erpCustomerPK, String certNum)  throws RemoteException, FDResourceException{
		Connection conn = null;
        List recepientList =null;
		try {
			conn = this.getConnection();			
			recepientList=GiftCardPersistanceDAO.loadGiftCardRedemedOrders(conn, erpCustomerPK,certNum);
		} catch (Exception e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("error while saving customer request", e);
			}
		}
		return recepientList;
	}
	
	public Object getGiftCardRedeemedOrders(String certNum)  throws RemoteException, FDResourceException{
		Connection conn = null;
        List recepientList =null;
		try {
			conn = this.getConnection();			
			recepientList=GiftCardPersistanceDAO.loadGiftCardRedemedOrders(conn, certNum);
		} catch (Exception e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("error while saving customer request", e);
			}
		}
		return recepientList;
	}
	
	
	
	public List getAllDeletedGiftCard(String erpCustomerPK)  throws RemoteException, FDResourceException{
		Connection conn = null;
        List allGCList =null;
        List deletedGCList=new ArrayList();
		try {
			conn = this.getConnection();			
			allGCList=GiftCardPersistanceDAO.loadAllGiftCardsUsed(conn, erpCustomerPK);
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(erpCustomerPK));
			List giftCards = customerEB.getGiftCards();			
			for(int i=0;i<allGCList.size();i++){
				ErpGiftCardModel model=(ErpGiftCardModel)allGCList.get(i);
				if(!giftCards.contains(model)){
					deletedGCList.add(model);	
				}
			}
			
		} catch (Exception e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("error while saving customer request", e);
			}
		}
		return deletedGCList;
	}
	

	private String convertToGivexReference(String saleId){
		return saleId+"X1";
	}
	

	
	
	/**
	 * Method for sending the 'Gift Card' to the chosen recipients by email.
	 */
	private void sendGiftCardToRecipients(ErpGiftCardDlvConfirmModel dlvConfirmModel, boolean resend)throws FDResourceException{
		List dlvInfoTranactionList = dlvConfirmModel.getDlvInfoTranactionList();
		if(null != dlvInfoTranactionList && !dlvInfoTranactionList.isEmpty()){
			Iterator itr =dlvInfoTranactionList.iterator();
			while (itr.hasNext()) {
				ErpGCDlvInformationHolder erpGCDlvInformationHolder = (ErpGCDlvInformationHolder) itr.next();
				
				//If the chosen delivery mode for the recipient is by Email, then send the Gift Card by Email.
				if(resend || erpGCDlvInformationHolder.getRecepientModel().getDeliveryMode().equals(EnumGCDeliveryMode.EMAIL)){
					GiftCardOrderInfo giftCardOrderInfo = new GiftCardOrderInfo();
					giftCardOrderInfo.setGcId(erpGCDlvInformationHolder.getGiftCardId());
					giftCardOrderInfo.setGcAmount(erpGCDlvInformationHolder.getRecepientModel().getAmount());
					giftCardOrderInfo.setGcFor(erpGCDlvInformationHolder.getRecepientModel().getRecipientName());
					giftCardOrderInfo.setGcRecipientEmail(erpGCDlvInformationHolder.getRecepientModel().getRecipientEmail());
					giftCardOrderInfo.setGcFrom(erpGCDlvInformationHolder.getRecepientModel().getSenderName());
					giftCardOrderInfo.setGcSenderEmail(erpGCDlvInformationHolder.getRecepientModel().getSenderEmail());
					giftCardOrderInfo.setGcMessage(erpGCDlvInformationHolder.getRecepientModel().getPersonalMessage());
					giftCardOrderInfo.setGcRedempcode((null == erpGCDlvInformationHolder.getGivexNum()? " " :erpGCDlvInformationHolder.getGivexNum()));//FTL won't accept 'null' values.
					giftCardOrderInfo.setGcType(erpGCDlvInformationHolder.getRecepientModel().getTemplateId());
					
					//Sending 'Gift Card' to the recipient by email.
					this.doEmail(ErpEmailFactory.getInstance().createGCEmail(giftCardOrderInfo));	
				}
			}
		}
	}
	
	
	public List getGiftCardForOrder(String saleId) throws RemoteException, FDResourceException{
		Connection conn = null;
        List recepientList =null;
		try {
			conn = this.getConnection();			
			recepientList=GiftCardPersistanceDAO.loadGiftCardbySaleId(conn, saleId);
		} catch (Exception e) {
			throw new FDResourceException(e);
		} finally {
		    close(conn);
		}
		return recepientList;
	}
	
	
	private void doEmail(EmailI email) throws FDResourceException {
		try {
			MailerGatewaySB mailer = getMailerHome().create();
			mailer.enqueueEmail(email);
		} catch (CreateException ce) {
			throw new FDResourceException(ce, "Cannot create MailerGatewayBean");
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Cannot talk to MailerGatewayBean");
		}
	}
	
	
	public List getGiftCardRecepientsForOrder(String saleId) throws RemoteException,FDResourceException{
		Connection conn = null;
        List recepientList =null;
		try {
			conn = this.getConnection();			
			recepientList=GiftCardPersistanceDAO.loadGiftCardRecipentsBySaleId(conn, saleId);
		} catch (Exception e) {
			throw new FDResourceException(e);
		} finally {
                    close(conn);
		}
		return recepientList;
	}
	

	public void postAuthorizeSales(String saleId) {
		try{
			
			/*if (!"true".equalsIgnoreCase(ErpServicesProperties.getPr eAuthorize())) {
				return errorList;
			}	*/		
			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));			
			/* 
			 * If there are pending auths then retry posting pending pre auths.
			 * If successful proceed else return and retry later.
			 * 
             */	
            List pendingAuths = saleEB.getPendingGCAuthorizations(); 
            if (null !=pendingAuths && !pendingAuths.isEmpty()) {
            	preAuthorizeSales(saleId);
            	pendingAuths = saleEB.getPendingGCAuthorizations();
            	//If there are pending auths still 
            	if(null !=pendingAuths && !pendingAuths.isEmpty())
            		return;
            }            
            
			ErpAbstractOrderModel order = saleEB.getCurrentOrder();
			ErpSaleModel model = (ErpSaleModel)saleEB.getModel();
			int refCount = model.getGCTransactions().size();
			
			//ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(order.getCustomerId()));
			List giftCardList = ErpGiftCardUtil.getGiftcardPaymentMethods(order.getAppliedGiftcards());
			ErpInvoiceModel erpInvoiceModel = saleEB.getInvoice();
			//If the invoice amt is '0', cancel all the pre-auth'ed gift card transactions.
			if(null != erpInvoiceModel && erpInvoiceModel.getAmount()== 0){			
		        if(reverseAuthAllGiftCards(saleEB, giftCardList, refCount)) {
		            saleEB.markAsCapturePending() ;
		        }
                return;
			}
            boolean transComplete = true;        
            for(Iterator it = giftCardList.iterator(); it.hasNext();){
                boolean isInvoicedGiftCard = false;
                ErpPaymentMethodI pm = (ErpPaymentMethodI) it.next();
                List invoiceGiftCards = erpInvoiceModel.getAppliedGiftCards();
                    for (Iterator iterator = invoiceGiftCards.iterator(); iterator
                            .hasNext();) {
                        ErpAppliedGiftCardModel invoiceAppliedGiftCardModel = (ErpAppliedGiftCardModel) iterator.next();
                        if(pm.getCertificateNumber().equalsIgnoreCase(invoiceAppliedGiftCardModel.getCertificateNum())){
                            isInvoicedGiftCard = true;
                            //TODO:iterator.remove();//No need to check with this gift card again.
                            break;
                        }							
                    }
                    String referenceId = convertToGivexReference(saleEB.getPK().getId(), ++refCount);
                    List vAuths = saleEB.getValidGCAuthorizations(pm);
                    if(vAuths == null || vAuths.size() == 0) continue;			
                    ErpPreAuthGiftCardModel auth = (ErpPreAuthGiftCardModel) vAuths.get(0);
                    if(isInvoicedGiftCard){
                        // Post auth with Givex.
                        /* one more thing we need to handle here is if already post-auth exists for this pre-Auth. 
                        This can happen when partial post auth happened due to givex connectivity issue.
                        */
                        
                        //If post auth already exists skip this auth.
                        if(saleEB.hasValidPostAuth(pm, auth.getAuthCode())) continue;
                        //Get the invoiced gift card amount to be applied for this gift card.
                        
                        double appliedAmt = ErpGiftCardUtil.getAppliedAmount(pm.getCertificateNumber(), invoiceGiftCards);
                        //Need to return ErpPostAuthModel    
                        ErpPostAuthGiftCardModel postAuth = postAuthGiftCard(pm, auth, referenceId, appliedAmt);							
                        if(postAuth.isApproved() || postAuth.isDeclined())
                            saleEB.addPostAuthorization(postAuth);
                        else
                            transComplete = false;
                    }else{
                        // Reverse auth with Givex.
                        ErpReverseAuthGiftCardModel rauth = reverseAuthGiftCard(pm, auth, referenceId); 	
                        if(rauth.isApproved() || rauth.isDeclined())
                            saleEB.addReverseGCPreAuthorization(rauth);
                        else
                            transComplete = false;
                    }
    				//Update the final GC balance from givex to DB
    				updateBalance(saleId, pm);
                }
					
			//Change the sale status to 'CAPTURE_PENDING'.
            if(transComplete) {                
                saleEB.markAsCapturePending() ;
            }

		}catch (FinderException fe) {
			LOGGER.warn("FinderExceptin while trying to locate Sale Entity Bean", fe);
			throw new EJBException(fe);
		}catch(ErpTransactionException ete){
			LOGGER.warn("ErpTransactionException while trying to talk to Sale Entity Bean", ete);
			throw new EJBException(ete);
		}catch (RemoteException re) {
			LOGGER.warn("RemoteException while trying to talk to Sale Entity Bean", re);
			throw new EJBException(re);
		}
		return;
	}

	/**
	 * Method to reverse auth a list of giftcards.
	 * @param errorList
	 * @param saleEB
	 * @param giftCardList
	 * @throws RemoteException
	 */
	private boolean reverseAuthAllGiftCards(ErpSaleEB saleEB,List giftCardList, int refCount) throws ErpTransactionException, RemoteException {
        boolean transComplete = true;        
        String saleId  = saleEB.getPK().getId();
		for(Iterator it = giftCardList.iterator(); it.hasNext();){
			ErpPaymentMethodI pm = (ErpPaymentMethodI) it.next();
			List vAuths = saleEB.getValidGCAuthorizations(pm);
			if(vAuths == null || vAuths.size() == 0) continue;
			String referenceId = convertToGivexReference(saleId, ++refCount);
			ErpPreAuthGiftCardModel auth = (ErpPreAuthGiftCardModel) vAuths.get(0);
			//If valid auth(already pre-auth'ed) is found for this gift card then post cancel pre auth to Givex.
			ErpReverseAuthGiftCardModel rauth = reverseAuthGiftCard(pm, auth, referenceId); 	
            if(rauth.isDeclined() || rauth.isApproved()){
                saleEB.addReverseGCPreAuthorization(rauth);
				//Update the final GC balance from givex to DB
				updateBalance(saleId, pm);
			}else
                //If status is pending only means the givex connectivity was lost.
               transComplete = false; 

		}
        return transComplete;
	}

	/**
	 * Method to post auth a gift card.
	 * @param errorList
	 * @param pm
	 * @param vAuths
	 * @return
	 */
	private ErpPostAuthGiftCardModel postAuthGiftCard(ErpPaymentMethodI pm, ErpPreAuthGiftCardModel auth, String referenceId, double postAuthAmt) {
		ErpPostAuthGiftCardModel postAuth = createPostAuthTransaction(referenceId, pm.getCertificateNumber(), auth.getAuthCode(), postAuthAmt);
		GivexResponseModel rspModel = null;
		try{
			rspModel = GivexServerGateway.postAuthGiftCard(pm, postAuthAmt, new Long(auth.getAuthCode()).longValue(), referenceId);
			postAuth.setGcTransactionStatus(EnumGiftCardTransactionStatus.SUCCESS);
			postAuth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
			//Reset the error message.
			postAuth.setErrorMsg("");			
		}catch(GivexException ge){
			if(ge.getErrorCode() > 0){
				postAuth.setGcTransactionStatus(EnumGiftCardTransactionStatus.FAILURE);
				//postAuth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
			}else{
				//System failed to connect to Givex. Leave the status in pending.
			}
			postAuth.setErrorMsg(ge.getMessage());
           
		}
		return postAuth;
	}

	/**
	 * Method to reverse auth a gift card.
	 * @param errorList
	 * @param pm
	 * @param vAuths
	 * @return
	 */
	private ErpReverseAuthGiftCardModel reverseAuthGiftCard(ErpPaymentMethodI pm, ErpPreAuthGiftCardModel auth, String referenceId) {
		ErpReverseAuthGiftCardModel rauth = createReverseAuthTransaction(referenceId, pm.getCertificateNumber(), auth.getAuthCode(), auth.getAmount());		
		GivexResponseModel rspModel =  null;
		try{
			rspModel = GivexServerGateway.cancelPreAuthorization(pm, new Long(auth.getAuthCode()).longValue(),
														auth.getReferenceId());

			
			rauth.setGcTransactionStatus(EnumGiftCardTransactionStatus.SUCCESS);
			rauth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
			//Reset the error message.
			rauth.setErrorMsg("");			
		}catch(GivexException ge){
			if(ge.getErrorCode() > 0){
				rauth.setGcTransactionStatus(EnumGiftCardTransactionStatus.FAILURE);
				//rauth.setAuthCode(String.valueOf(rspModel.getAuthCode()));
			} else {
				//System failed to connect to Givex. Leave the status in pending.
			}
			rauth.setErrorMsg(ge.getMessage());
		}
		return rauth;
	}
    
    public ErpPostAuthGiftCardModel createPostAuthTransaction(String referenceId, String certificationNum, String preAuthCode, double amount)
	{
		ErpPostAuthGiftCardModel tranModel = new ErpPostAuthGiftCardModel();
		tranModel.setAmount(amount);
		tranModel.setGcTransactionStatus(EnumGiftCardTransactionStatus.PENDING);
		tranModel.setGCTransactionType(EnumGiftCardTransactionType.POST_AUTH);
		tranModel.setCertificateNum(certificationNum);	
		tranModel.setReferenceId(referenceId);
		tranModel.setPreAuthCode(preAuthCode);
		tranModel.setTransactionSource(EnumTransactionSource.SYSTEM);
		return tranModel;
	}
	
	public ErpGCDlvInformationHolder loadGiftCardRecipentByGivexNum(String fromGivexNum)throws EJBException{
		try {
			Connection conn = getConnection();
			return GiftCardPersistanceDAO.loadGiftCardRecipentByGivexNum(conn, fromGivexNum);
		} catch (SQLException e) {
			LOGGER.warn("SQLException in the method:loadGiftCardRecipentByGivexNum() ", e);
			throw new EJBException(e);
		}	
	}
	
	
	public ErpGCDlvInformationHolder loadGiftCardRecipentByCertNum(String certNum)throws EJBException{
		try {
			Connection conn = getConnection();
			return GiftCardPersistanceDAO.loadGiftCardRecipentByCertNum(conn, certNum);
		} catch (SQLException e) {
			LOGGER.warn("SQLException in the method:loadGiftCardRecipentByGivexNum() ", e);
			throw new EJBException(e);
		}	
	}

	private void sendNotificationEmail(String subject, String body) {
		try {
			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getGCMailFrom(), 
							ErpServicesProperties.getGCMailTo(), 
							ErpServicesProperties.getGCMailCC(), 
							subject, body);
		} catch (MessagingException e) {
			throw new EJBException(e);
		}		
	}


}
