package com.freshdirect.mktAdmin.service;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.promotion.FDPromotionNewModelFactory;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.mktAdmin.constants.EnumCompetitorStatusType;
import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.constants.EnumFileType;
import com.freshdirect.mktAdmin.constants.EnumListUploadActionType;
import com.freshdirect.mktAdmin.dao.MarketAdminDAOIntf;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;
import com.freshdirect.mktAdmin.model.CustomerAddressModel;
import com.freshdirect.mktAdmin.model.FileDownloadBean;
import com.freshdirect.mktAdmin.model.FileUploadBean;
import com.freshdirect.mktAdmin.model.FileUploadedInfo;
import com.freshdirect.mktAdmin.model.RestrictedPromoCustomerModel;
import com.freshdirect.mktAdmin.model.RestrictionListUploadBean;
import com.freshdirect.mktAdmin.model.RestrictionSearchBean;
import com.freshdirect.mktAdmin.util.FileParser;
import com.freshdirect.mktAdmin.util.FileParserFactory;
import com.freshdirect.mktAdmin.util.MarketAdminUtil;

public class MarketAdminServiceImpl implements MarketAdminServiceIntf {

		private final static Category LOGGER = LoggerFactory.getInstance(MarketAdminServiceImpl.class);
		private MarketAdminDAOIntf mktAdminDAOImpl=null; 
		

		public void addCustomerInformation(Collection collection)
		throws MktAdminApplicationException {
			// TODO Auto-generated method stub
			List exceptionList=new ArrayList();
			List filteredAddrList=new ArrayList();
			try {						
				LOGGER.debug("collection in addCustomerInformation:"+collection.size());
				Iterator iterator=collection.iterator();			
				first:while(iterator.hasNext())
				{   CustomerAddressModel model=null;
					try{
						 model=(CustomerAddressModel)iterator.next();
						if(model.getScrubbedStreet()==null){
							LOGGER.debug("model.getCustomerId() scrubbed street is null "+model.getCustomerId());
						}
						//model.setServiceType(EnumServiceType.CORPORATE);						
						MarketAdminUtil.performAddressCheck(model);	
						if(model.getAddressInfo()==null || model.getAddressInfo().getLongitude()==0){
							LOGGER.debug("customer id :"+model.getId()+": dont have GEOCODE");
							continue first;
						}
						if(mktAdminDAOImpl.isCustomerAllreadyExists(model))
						{
							LOGGER.debug("customer already exist :"); 							
							exceptionList.add(new MktAdminApplicationException("114",new String[]{model.getAddress1(),model.getCustomerId()}));
							continue first;
						}
					}catch(MktAdminApplicationException e){
						//  need to collect all the exception messages and send it in one shot					
						exceptionList.add(e);
						continue first;
					}
					filteredAddrList.add(model);
				}							
				
				mktAdminDAOImpl.insertCustomerAddrModel(filteredAddrList);
				
				if(exceptionList.size()>0){
					throw new MktAdminApplicationException(exceptionList);
				}
				// isert to the db    
				
			} catch (SQLException e) {			
				throw new MktAdminSystemException("1002",e);
			}
			catch(MktAdminSystemException e){			
				throw e;
			}
			catch(MktAdminApplicationException e){			
				throw e;
			}
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			}			
		}

		
		public void addCompetitorInformation(Collection collection)
		throws MktAdminApplicationException {
			// TODO Auto-generated method stub			
			List exceptionList=new ArrayList();
			try {						
				Iterator iterator=collection.iterator();			
				first:while(iterator.hasNext())
				{
					try{
						CompetitorAddressModel model=(CompetitorAddressModel)iterator.next();				
						model.setServiceType(EnumServiceType.CORPORATE);	
						model.setStatus(EnumCompetitorStatusType.ACTIVE);
						MarketAdminUtil.performAddressCheck(model);	
						LOGGER.debug(model+"\n----------- addCompetitorInformation ------------------");
						if(model.getAddressInfo()==null){
							throw new MktAdminApplicationException("110",new String[]{model.getAddress1(),model.getCompanyName()});
						}
						
						if(mktAdminDAOImpl.isAddressAllreadyExists(model))
						{
							LOGGER.debug("address already exist :"); 							
							exceptionList.add(new MktAdminApplicationException("109",new String[]{model.getCompanyName(),model.getAddress1()}));						
						}
					}catch(MktAdminApplicationException e){
						//  need to collect all the exception messages and send it in one shot					
						exceptionList.add(e);
						continue first;
					}
				}							
				
				if(exceptionList.size()>0){
					LOGGER.debug("Exception List :"+exceptionList);
					throw new MktAdminApplicationException(exceptionList);
				}
				// isert to the db    
				mktAdminDAOImpl.insertCompetitorAddrModel(collection);
			} 
//			catch (SQLException e) {			
//				throw new MktAdminSystemException("1002",e);
//			}
			catch(MktAdminSystemException e){			
				throw e;
			}
			catch(MktAdminApplicationException e){			
				throw e;
			}
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			}			
		}
		private static final int FILE_DEVIDER_SIZE=300;
		
		public Collection addPromoRestrictedCustomers(Collection collection)
		throws MktAdminApplicationException {
			// TODO Auto-generated method stub
			long t1 = System.currentTimeMillis();
			List filteredList=new ArrayList();
			List mergedList=new ArrayList();
			Collection invalidMergeList=new ArrayList();
			List exceptionList=new ArrayList();
			try {						
				LOGGER.debug(" List size at the begining:"+collection.size());
				// since oracle does not support in query with more than some number I am deviding this with 100 of collection
				if(collection.size()<FILE_DEVIDER_SIZE){
//					mergedList.addAll(getMktAdminDAOImpl().getRestrictedCustomers(collection));
					invalidMergeList.addAll(getMktAdminDAOImpl().getInvalidCustomerIds(collection));					
				}
				else{
					Iterator iterator1=collection.iterator();
					int counter=0;				
					while(iterator1.hasNext()){										
						if(counter%300!=0){
							RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
							filteredList.add(model);
							if(counter>=collection.size()){
//								mergedList.addAll(getMktAdminDAOImpl().getRestrictedCustomers(filteredList));
								invalidMergeList.addAll(getMktAdminDAOImpl().getInvalidCustomerIds(filteredList));
								filteredList=new ArrayList();
							}
							counter++;
						}
						else{
							RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
							filteredList.add(model);
//							mergedList.addAll(getMktAdminDAOImpl().getRestrictedCustomers(filteredList));
							invalidMergeList.addAll(getMktAdminDAOImpl().getInvalidCustomerIds(filteredList));
							filteredList=new ArrayList();							
						}
						
					}
				}// end of else
//				System.out.println("merged element lenght :"+mergedList.size());
				
				 // check the existing customer information
				
				// remove the existing one from the pro customer model				
				
				collection.removeAll(mergedList);
				
				// remove the invalid userIds
				Iterator iterator1=invalidMergeList.iterator();
				while(iterator1.hasNext()){
					RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
					if(collection.contains(model)){
						LOGGER.debug("invalid add model :"+model);						
						collection.remove(model);
						exceptionList.add(new MktAdminApplicationException("122",new String[]{model.getCustomerId()}));
					}					
				}
				Iterator iterator2=collection.iterator();
				while(iterator2.hasNext()){
					RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator2.next();
					if(null ==model.getPromotionId()){
						LOGGER.debug("invalid add model :"+model);						
						iterator2.remove();
						invalidMergeList.add(model);
						exceptionList.add(new MktAdminApplicationException("123",new String[]{model.getPromotionCode()}));
					}					
				}
				System.out.println("invalidMergeList element lenght :"+invalidMergeList.size());
				// isert to the db    
				LOGGER.debug("filtered List size :"+collection.size());
											
				if(collection.size()>0)
//				     mktAdminDAOImpl.insertRestrictedCustomers(collection);
					mktAdminDAOImpl.newInsertRestrictedCustomers(collection);
				
				// throw invalid userId exception if it exists
			/*	if(exceptionList.size()>0){					
					throw new MktAdminApplicationException(exceptionList);	
				}*/
				
				long t2 = System.currentTimeMillis();
				LOGGER.info("Time taken to complete addPromoRestrictedCustomers:" + (t2 - t1));
				
			} 
			catch (SQLException e) {			
				throw new MktAdminSystemException("1002",e);
			}
			/*catch(MktAdminApplicationException e){			
				throw e;
			}*/
			catch(MktAdminSystemException e){			
				throw e;
			}			
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			}		
			return invalidMergeList;
		}
		
		public void createPromoRestrictedCustomers(Collection collection)
		throws MktAdminApplicationException {
			String promotionId=null;
			List filteredList=new ArrayList();
			List invalidMergeList=new ArrayList();
			List exceptionList=new ArrayList();
			try {						
				LOGGER.debug(" List size at the begining:"+collection.size());
				// delete all the restriction for the promotion
				
				Iterator iterator=collection.iterator();
				if(iterator.hasNext())
				{
					RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator.next();
					promotionId=model.getPromotionId();
				}
				
				mktAdminDAOImpl.deleteRestrictedCustomerForPromotion(promotionId);
				
				
				if(collection.size()<=FILE_DEVIDER_SIZE){					
					invalidMergeList.addAll(getMktAdminDAOImpl().getInvalidCustomerIds(collection));	
				}
				else{
					Iterator iterator1=collection.iterator();
					int counter=0;				
					while(iterator1.hasNext()){										
						if(counter%300!=0){
							RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
							filteredList.add(model);							
							if(counter>=collection.size()){
								invalidMergeList.addAll(getMktAdminDAOImpl().getInvalidCustomerIds(filteredList));
								filteredList=new ArrayList();
							}
							counter++;
						}
						else{						
							RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
							filteredList.add(model);
							invalidMergeList.addAll(getMktAdminDAOImpl().getInvalidCustomerIds(filteredList));
							filteredList=new ArrayList();							
						}	
						
						
					}
				}// end of else
				System.out.println("merged element lenght :"+invalidMergeList.size());
				 // check the existing customer information
				
				// remove the existing one from the pro customer model				
								
				
				// remove the invalid userIds
				Iterator iterator2=invalidMergeList.iterator();
				while(iterator2.hasNext()){
					RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator2.next();
					if(collection.contains(model)){
						LOGGER.debug("invalid add model :"+model);						
						collection.remove(model);
						exceptionList.add(new MktAdminApplicationException("122",new String[]{model.getCustomerId()}));
					}					
				}		
				
				
				
				if(collection.size()>0)
				     mktAdminDAOImpl.insertRestrictedCustomers(collection);
				
//				 throw invalid userId exception if it exists
				if(exceptionList.size()>0){					
					throw new MktAdminApplicationException(exceptionList);	
				}
				
			} 
			catch (SQLException e) {			
				throw new MktAdminSystemException("1002",e);
			}
			catch(MktAdminApplicationException e){			
				throw e;
			}
			catch(MktAdminSystemException e){			
				throw e;
			}
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			}
		}
		
		public void deletePromoRestrictedCustomers(Collection collection)
		throws MktAdminApplicationException {
			// TODO Auto-generated method stub			
			List filteredList=new ArrayList();
			List mergedList=new ArrayList();
			List diffList=new ArrayList();
			List invalidMergeList=new ArrayList();
			List exceptionList=new ArrayList();

			try {						
				LOGGER.debug(" List size at the begining:"+collection.size());
				// since oracle does not support in query with more than some number I am deviding this with 100 of collection
				if(collection.size()<FILE_DEVIDER_SIZE){
					mergedList.addAll(getMktAdminDAOImpl().getRestrictedCustomers(collection));
					invalidMergeList.addAll(getMktAdminDAOImpl().getInvalidCustomerIds(collection));	
				}
				else{
					Iterator iterator1=collection.iterator();
					int counter=0;				
					while(iterator1.hasNext()){										
						if(counter%300!=0){
							RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
							filteredList.add(model);
							if(counter>=collection.size()){
								invalidMergeList.addAll(getMktAdminDAOImpl().getInvalidCustomerIds(filteredList));
								filteredList=new ArrayList();
							}
							counter++;
						}
						else{
							RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
							filteredList.add(model);
							mergedList.addAll(getMktAdminDAOImpl().getRestrictedCustomers(filteredList));
							invalidMergeList.addAll(getMktAdminDAOImpl().getInvalidCustomerIds(filteredList));
							filteredList=new ArrayList();							
						}
						
					}
				}// end of else
				System.out.println("merged element lenght :"+mergedList.size());
				 // check the existing customer information
				
				// remove the existing one from the pro customer model				
				
				collection.removeAll(diffList);
				
				// remove the invalid userIds
				Iterator iterator1=invalidMergeList.iterator();
				while(iterator1.hasNext()){
					RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
					if(collection.contains(model)){
						LOGGER.debug("invalid add model :"+model);						
						collection.remove(model);
						exceptionList.add(new MktAdminApplicationException("122",new String[]{model.getCustomerId()}));
					}					
				}		
				
				// isert to the db				
				
				LOGGER.debug("filtered List size :"+collection.size());
				if(collection.size()>0)
				     mktAdminDAOImpl.deleteRestrictedCustomers(collection);
				
//				 throw invalid userId exception if it exists
				if(exceptionList.size()>0){					
					throw new MktAdminApplicationException(exceptionList);	
				}
			} 
			catch (SQLException e) {			
				throw new MktAdminSystemException("1002",e);
			}
			catch(MktAdminApplicationException e){			
				throw e;
			}
			catch(MktAdminSystemException e){			
				throw e;
			}			
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			}			
		}

		
		public void replacePromoRestrictedCustomers(Collection collection)
		throws MktAdminApplicationException {
			// TODO Auto-generated method stub			
			String promotionId=null;
			List filteredList=new ArrayList();
			List invalidMergeList=new ArrayList();
			List exceptionList=new ArrayList();
			try {						
				LOGGER.debug(" List size at the begining:"+collection.size());
				// delete all the restriction for the promotion
				
				Iterator iterator=collection.iterator();
				if(iterator.hasNext())
				{
					RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator.next();
					promotionId=model.getPromotionId();
					
				}
				mktAdminDAOImpl.deleteRestrictedCustomerForPromotion(promotionId);
				
				
				if(collection.size()<FILE_DEVIDER_SIZE){					
					invalidMergeList.addAll(getMktAdminDAOImpl().getInvalidCustomerIds(collection));	
				}
				else{
					Iterator iterator1=collection.iterator();
					int counter=0;				
					while(iterator1.hasNext()){										
						if(counter%300!=0){
							RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
							filteredList.add(model);
							if(counter>=collection.size()){
								invalidMergeList.addAll(getMktAdminDAOImpl().getInvalidCustomerIds(filteredList));
								filteredList=new ArrayList();
							}
							counter++;
						}
						else{				
							RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
							filteredList.add(model);
							invalidMergeList.addAll(getMktAdminDAOImpl().getInvalidCustomerIds(filteredList));
							filteredList=new ArrayList();							
						}						
						
					}
				}// end of else
				System.out.println("merged element lenght :"+invalidMergeList.size());
				 // check the existing customer information
				
				// remove the existing one from the pro customer model				
								
				
				// remove the invalid userIds
				Iterator iterator1=invalidMergeList.iterator();
				while(iterator1.hasNext()){
					RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator1.next();
					if(collection.contains(model)){
						LOGGER.debug("invalid add model :"+model);						
						collection.remove(model);
						exceptionList.add(new MktAdminApplicationException("122",new String[]{model.getCustomerId()}));
					}					
				}		
				
				
				
				mktAdminDAOImpl.insertRestrictedCustomers(collection);
				
//				 throw invalid userId exception if it exists
				if(exceptionList.size()>0){					
					throw new MktAdminApplicationException(exceptionList);	
				}
				
			} 
			catch (SQLException e) {			
				throw new MktAdminSystemException("1002",e);
			}
			catch(MktAdminApplicationException e){			
				throw e;
			}
			catch(MktAdminSystemException e){			
				throw e;
			}			
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			}			
		}

		
					
		
		
		public Collection parseMktAdminFile(FileUploadBean bean)
		throws MktAdminApplicationException {
			// check if file is not null
			// call the filparser and ask to parse the file
			Collection collection=new ArrayList();			
			try {
				LOGGER.debug("bean.getFileType() :"+bean.getFileType());
				LOGGER.debug("bean.getFileContentType() :"+bean.getFileContentType());
				FileParser parser=FileParserFactory.getFileParser(bean.getFileType());
				LOGGER.debug("parser :"+parser);
				collection = parser.parseFile(bean);
				LOGGER.debug("collection :"+collection);
				if(EnumFileContentType.COMPETITOR_FILE_TYPE==bean.getFileContentType()){
				   addCompetitorInformation(collection);		
				}else if(EnumFileContentType.CUSTOMER_FILE_TYPE==bean.getFileContentType()){
					addCustomerInformation(collection);
				}
				else if(EnumFileContentType.RESTRICTION_LIST_FILE_TYPE==bean.getFileContentType()){
					RestrictionListUploadBean rBean=(RestrictionListUploadBean)bean;
					LOGGER.debug("rBean.getPromotionCode() :"+rBean.getPromotionCode());
					if(EnumListUploadActionType.ADD==rBean.getActionType()){
						LOGGER.debug("ADDING THE RESTRICTIONS" );
						newAddPromoRestrictedCustomers(collection);
					}else if(EnumListUploadActionType.ADD_MULTI_PROMO==rBean.getActionType()){
						LOGGER.debug("ADDING THE RESTRICTIONS FOR MULTIPLE PROMOTIONS" );
						Collection failedList = null;
						if(bean.isAutoUpload()){
						try {
							failedList = addPromoRestrictedCustomers(collection);
							return failedList;
						} catch (MktAdminApplicationException e) {
							LOGGER.debug("catching the MktAdminApplicationException:",e);
						}finally{
							if(true /*&& check property to send email or not*/ ){
								ErpMailSender mailer = new ErpMailSender();
								StringBuffer buff = new StringBuffer();
								mailer.sendMail(FDStoreProperties.getCustomerServiceEmail(),
											"ksriram@freshdirect.com",//FDStoreProperties.getStandingOrderReportToEmail(),
											"ksriram@freshdirect.com",//FDStoreProperties.getStandingOrderReportToEmail(),
											"Auto Upload", buff.toString(), true, "");
							  }
						}}else{
							addPromoRestrictedCustomers(collection);
						}
					}else if(EnumListUploadActionType.DELETE==rBean.getActionType()){
						LOGGER.debug("DELETING THE RESTRICTIONS" );
						newDeletePromoRestrictedCustomers(collection);
					}else if(EnumListUploadActionType.REPLACE==rBean.getActionType()){
						LOGGER.debug("REPLACING THE RESTRICTIONS" );
						newReplacePromoRestrictedCustomers(collection);
					}else if(EnumListUploadActionType.CREATE==rBean.getActionType()){
						LOGGER.debug("CREATINGATING THE RESTRICTIONS" );
						newReplacePromoRestrictedCustomers(collection);
					}
				}
			}
			catch(MktAdminSystemException e){			
				throw e;
			}
			catch(MktAdminApplicationException e){			
				throw e;
			}
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			}		
			return collection;
		}
	
	
		public void storeCompetitorInformation(Collection collection)
		throws MktAdminApplicationException {
			List exceptionList=new ArrayList();
			try{
				Iterator iterator=collection.iterator();			
				first:while(iterator.hasNext())
				{
					try{
						CompetitorAddressModel model=(CompetitorAddressModel)iterator.next();
						model.setServiceType(EnumServiceType.CORPORATE);	
						LOGGER.debug("model.getCompetitorType() "+model.getCompetitorType());
						MarketAdminUtil.performAddressCheck(model);
						if(model.getAddressInfo()==null){
							throw new MktAdminApplicationException("110",new String[]{model.getAddress1(),model.getCompanyName()});
						}
						
					}catch(MktAdminApplicationException e){
						//  need to collect all the exception messages and send it in one shot
						LOGGER.warn("MktAdminApplicationException: "+e);
						exceptionList.add(e);
						continue first;							
					}	
				}	
				
				if(exceptionList.size()>0){
					throw new MktAdminApplicationException(exceptionList);
				}
				
				getMktAdminDAOImpl().storeCompetitorInformation(collection);
				}catch (SQLException e) {	
					LOGGER.warn("catching the database error");
					throw new MktAdminSystemException("1002",e);
				}
				catch(MktAdminSystemException e){			
					throw e;
				}
				catch(MktAdminApplicationException e){			
					throw e;
				}
				catch(Throwable e){			
					throw new MktAdminSystemException("1001",e);
				}		
		}
	
		public void removeCompetitorInformation(Collection collection)
		throws MktAdminApplicationException {
			// TODO Auto-generated method stub
			try{
				 getMktAdminDAOImpl().deleteCompetitorInfo(collection);
				} catch (SQLException e) {	
					LOGGER.warn("catching the database error");
					throw new MktAdminSystemException("1002",e);
				}
				catch(Throwable e){			
					throw new MktAdminSystemException("1001",e);
				} 
			
		}
	
		public Collection getCompetitorInformation() throws MktAdminApplicationException{
			try
			{
				LOGGER.debug("getMktAdminDAOImpl().getCompetitorInfo() :"+getMktAdminDAOImpl().getCompetitorInfo());
				return getMktAdminDAOImpl().getCompetitorInfo();
			} catch (SQLException e) {	
				LOGGER.warn("catching the database error");
				throw new MktAdminSystemException("1002",e);
			}
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			}
		}
		
		
		public CompetitorAddressModel getCompetitorInformation(String competitorId) throws MktAdminApplicationException
		{
			try{
			 return getMktAdminDAOImpl().getCompetitorInfo(competitorId);
			} catch (SQLException e) {	
				LOGGER.warn("catching the database error");
				throw new MktAdminSystemException("1002",e);
			}
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			} 
		}
		
		
	
		public MarketAdminDAOIntf getMktAdminDAOImpl() {
			return mktAdminDAOImpl;
		}
	
		public void setMktAdminDAOImpl(MarketAdminDAOIntf mktAdminDAOImpl) {		
			this.mktAdminDAOImpl = mktAdminDAOImpl;		
		}


		public Collection getAllPromotions() throws MktAdminApplicationException {
			// TODO Auto-generated method stub
			return FDPromotionNewModelFactory.getInstance().getPromotions();
		}
				

		public Collection getRestrictedCustomers(RestrictionSearchBean model) throws MktAdminApplicationException{
			try
			{
				LOGGER.debug("getMktAdminDAOImpl().getRestrictedCustomers() :"+model.getPromotion().getPromotionCode());
				return getMktAdminDAOImpl().getRestrictedCustomers(model.getPromotion().getId(),model.getSerachKey(),model.getStartIndex(),model.getEndIndex());
			} catch (SQLException e) {	
				LOGGER.warn("catching the database error");
				throw new MktAdminSystemException("1002",e);
			}
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			}			
		}
		
		public Collection getPromotionModel(String[] promotionCodes) throws MktAdminApplicationException{
            List promoModelList=new ArrayList();   
			for(int i=0;i<promotionCodes.length;i++){
				FDPromotionNewModel model=FDPromotionNewModelFactory.getInstance().getPromotion(promotionCodes[i]);
				LOGGER.debug("getMktAdminDAOImpl().getPromotionModel() :"+model.getId());	
				promoModelList.add(model);
			}
			return promoModelList;
		}


		public void removeRestrictedCustomers(String promotionCode, String customerId) throws MktAdminApplicationException {
			// TODO Auto-generated method stub               			
			try{				
				LOGGER.debug("getMktAdminDAOImpl().removeRestrictedCustomers() :"+promotionCode+" customerId :"+customerId);
				FDPromotionNewModel model=FDPromotionNewModelFactory.getInstance().getPromotion(promotionCode);
				getMktAdminDAOImpl().deleteRestrictedCustomers(model.getId(),customerId);
			} catch (SQLException e) {	
				LOGGER.warn("catching the database error");
				throw new MktAdminSystemException("1002",e);
			}
		}
		
		public void appendRestrictedCustomersFromEmailAddress(String[] emailAddress,String promotionCode) throws MktAdminApplicationException{
			// validate the email address 			 			
			// throw error if address is wrong
			// get customerIds for the email address
			// for restrictedcustomer model and return 
			Set filteredList=new HashSet();
			List exceptionList=new ArrayList();
			try {						
				LOGGER.debug(" List size at the begining:"+emailAddress.length);
				// since oracle does not support in query with more than some number I am deviding this with 100 of collection
				FDPromotionNewModel fdModel=FDPromotionNewModelFactory.getInstance().getPromotion(promotionCode);
				
				Map customerIdMap=getMktAdminDAOImpl().getCustomerIdsForEmailAddress(emailAddress);
				
				
										
					for(int i=0;i<emailAddress.length;i++){					
						String customerId=(String)customerIdMap.get(emailAddress[i]);
						if(customerId==null){
							LOGGER.debug("customerId not exist :"+customerId);
							exceptionList.add(new MktAdminApplicationException("120",new String[]{emailAddress[i]}));
						}
						else{
							RestrictedPromoCustomerModel model=new RestrictedPromoCustomerModel();
							model.setCustomerId(customerId);
							model.setCustEmailAddress(emailAddress[i]);
							model.setPromotionId(fdModel.getId());
							filteredList.add(model);
						}
					}									
				
				
				// isert to the db    
				LOGGER.debug("filtered List size1 :"+filteredList.size());
				
				if(exceptionList.size()>0){
					throw new MktAdminApplicationException(exceptionList);
				}
				
				//addPromoRestrictedCustomers(filteredList);
				newAddPromoRestrictedCustomers(filteredList);
				//mktAdminDAOImpl.insertRestrictedCustomers(filteredList);
			} 
			catch (SQLException e) {			
				throw new MktAdminSystemException("1002",e);
			}
			catch(MktAdminApplicationException e){			
				throw e;
			}
			catch(MktAdminSystemException e){			
				throw e;
			}			
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			}
			
		}
		
		public boolean isRestrictedCustomersExist(String promotionCode) throws MktAdminApplicationException{
			boolean restrictedCustomerExist=false;
			try
			{
				LOGGER.debug("getMktAdminDAOImpl().isRestrictedCustomersExist() :"+promotionCode);
				FDPromotionNewModel model=FDPromotionNewModelFactory.getInstance().getPromotion(promotionCode);				
				Collection collection=getMktAdminDAOImpl().getRestrictedCustomers(model.getId(),"",0,100);
				if(collection.size()>0)
					restrictedCustomerExist=true;
			} catch (SQLException e) {	
				LOGGER.warn("catching the database error");
				throw new MktAdminSystemException("1002",e);
			}
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			}
			return restrictedCustomerExist;
		}


		public String generateRestrictedCustomerFileContents(String promotionCode,EnumFileType fileType) throws MktAdminApplicationException {
			// TODO Auto-generated method stub		
			String fileContents="";
			try
			{
				LOGGER.debug("getMktAdminDAOImpl().isRestrictedCustomersExist() :"+promotionCode);
				FDPromotionNewModel model=FDPromotionNewModelFactory.getInstance().getPromotion(promotionCode);
				Collection collection=getMktAdminDAOImpl().getRestrictedCustomers(model.getId(),"",0,100000);
				FileParser handler= FileParserFactory.getFileParser(fileType.getName());
				FileDownloadBean bean=new FileDownloadBean();
				bean.setFileContentType(EnumFileContentType.RESTRICTION_LIST_FILE_TYPE);
				bean.setFileContents(collection);
				fileContents=handler.generateFile(bean);
			} catch (SQLException e) {	
				LOGGER.warn("catching the database error");
				throw new MktAdminSystemException("1002",e);
			}
			catch(Throwable e){			
				throw new MktAdminSystemException("1001",e);
			}
			return fileContents;

		}


		public void deleteRestrictionCustomer(String promotionCode) throws MktAdminApplicationException {
			// TODO Auto-generated method stub
			try
			{
				LOGGER.debug("getMktAdminDAOImpl().deleteRestrictionCustomer() :"+promotionCode);
				FDPromotionNewModel model=FDPromotionNewModelFactory.getInstance().getPromotion(promotionCode);
				getMktAdminDAOImpl().deleteRestrictedCustomer(model.getId());
			} catch (SQLException e) {	
				LOGGER.warn("catching the database error");
				throw new MktAdminSystemException("1002",e);
			}			
		}
		
		public void newAddPromoRestrictedCustomers(Collection collection) throws MktAdminApplicationException {
			long t1 = System.currentTimeMillis();
			try {						
				LOGGER.debug(" List size at the begining:"+collection.size());
				
				if(collection.size()>0)
					mktAdminDAOImpl.newInsertRestrictedCustomers(collection);
				
				long t2 = System.currentTimeMillis();
				LOGGER.info("Time taken to complete newaddPromoRestrictedCustomers:" + (t2 - t1));
				
			} 
			catch (Exception e) {			
				LOGGER.error("Error adding new promo restriction list", e);
			}			
		}
		
		public void newDeletePromoRestrictedCustomers(Collection collection) throws MktAdminApplicationException {
			long t1 = System.currentTimeMillis();
			try {						
				LOGGER.debug(" List size at the begining:"+collection.size());
				
				if(collection.size()>0)
				     mktAdminDAOImpl.deleteRestrictedCustomers(collection);
				
				long t2 = System.currentTimeMillis();
				LOGGER.info("Time taken to complete newDeletePromoRestrictedCustomers:" + (t2 - t1));
				
			} 
			catch (Exception e) {			
				LOGGER.error("Error in newAddPromoRestrictedCustomers", e);
			}
		}
		
		public void newReplacePromoRestrictedCustomers(Collection collection) throws MktAdminApplicationException {
			long t1 = System.currentTimeMillis();
			String promotionId=null;
			try {						
				LOGGER.debug(" List size at the begining:"+collection.size());
				// delete all the restriction for the promotion
				
				Iterator iterator=collection.iterator();
				if(iterator.hasNext())
				{
					RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)iterator.next();
					promotionId=model.getPromotionId();
					
				}
				//mktAdminDAOImpl.deleteRestrictedCustomerForPromotion(promotionId);
				
				mktAdminDAOImpl.newInsertRestrictedCustomers(collection);
				
				long t2 = System.currentTimeMillis();
				LOGGER.info("Time taken to complete newReplacePromoRestrictedCustomers:" + (t2 - t1));
				
			} 
			catch (Exception e) {			
				LOGGER.error("Error in newReplacePromoRestrictedCustomers", e);
			}
		}
	
		public FileUploadedInfo parseMktAdminAutoUploadFile(FileUploadBean bean)
		throws MktAdminApplicationException {
			Collection collection=new ArrayList();
			FileUploadedInfo info = new FileUploadedInfo();
			
				try {
					LOGGER.debug("bean.getFileType() :"+bean.getFileType());
					LOGGER.debug("bean.getFileContentType() :"+bean.getFileContentType());
					FileParser parser=FileParserFactory.getFileParser(bean.getFileType());
					LOGGER.debug("parser :"+parser);
					info.setFileName(bean.getAutoUploadFile().getName());
					collection = parser.parseFile(bean);
					info.setTotalCustInfo(collection);
					LOGGER.debug("collection :"+collection);
					if(EnumFileContentType.RESTRICTION_LIST_FILE_TYPE==bean.getFileContentType()){
						RestrictionListUploadBean rBean=(RestrictionListUploadBean)bean;					
						if(EnumListUploadActionType.ADD_MULTI_PROMO==rBean.getActionType()){
							LOGGER.debug("ADDING THE RESTRICTIONS FOR MULTIPLE PROMOTIONS BY AUTO UPLOAD" );
							Collection failedList = null;
							if(bean.isAutoUpload()){	
								info.setFileName(bean.getAutoUploadFile().getName());
								failedList = addPromoRestrictedCustomers(collection);
								info.setFailedCustInfo(failedList);		
								info.setSuccessful(true);
							}
						}
					}
				}catch(MktAdminApplicationException e){
					LOGGER.debug("catching the MktAdminApplicationException:",e);
					info.setSuccessful(false);
				}catch(Exception e){
					LOGGER.debug("catching the Exception:",e);
					info.setSuccessful(false);
				}
				return info;
			
		}
		
		public Collection getUpsOutageList(String fromDate, String endDate) throws MktAdminApplicationException {
			try {
				return getMktAdminDAOImpl().getUpsOutageCustList(fromDate, endDate);
			} catch (SQLException e) {
				LOGGER.error("Error while getting the UPS Outage customer list", e);
			}
			return null;
		}
			
}
