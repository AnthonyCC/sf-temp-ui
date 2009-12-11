package com.freshdirect.mail;

import java.text.SimpleDateFormat;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.mail.EmailAddress;
import com.freshdirect.framework.mail.FDFtlEmail;
import com.freshdirect.framework.mail.FTLEmailI;

/**
 * EmailFactory for the 'ERPServices' project and its dependent projects.
 * 
 * @author ksriram
 *
 */
public class ErpEmailFactory  {

	public static final String GENERAL_LABEL = "FreshDirect";
	public static final SimpleDateFormat df = new SimpleDateFormat("EEEE, MMM d yyyy");
	public static final String GC_FTL_TEMPLATE_NAME = "email_template.ftl";
	public static final String IPHONE_FTL_TEMPLATE_NAME = "email_promo_template.ftl";
	
	private static ErpEmailFactory _sharedInstance = new ErpEmailFactory(); 
	
	public static ErpEmailFactory getInstance() {		
		return _sharedInstance;
	}
	
	//Private Constructor for the singleton class.
	private ErpEmailFactory(){
		
	}
	
	private static class FDGiftCardFtlEmail extends FDFtlEmail{
		private GiftCardOrderInfo giftCardInfo;
		public FDGiftCardFtlEmail(GiftCardOrderInfo giftCardInfo){
			super();
			this.giftCardInfo = giftCardInfo;
		}
		
		public void decorateMap(){
			if(null!=giftCardInfo){
				super.getParameters().put("gcId", giftCardInfo.getGcId());
				super.getParameters().put("gcAmount",new Double(giftCardInfo.getGcAmount()));
				super.getParameters().put("gcRedempCode", giftCardInfo.getGcRedempcode());
				super.getParameters().put("gcFor", giftCardInfo.getGcFor());
				super.getParameters().put("gcFrom", giftCardInfo.getGcFrom());
				super.getParameters().put("gcMessage", giftCardInfo.getGcMessage());
				super.getParameters().put("gcRecipientEmail", giftCardInfo.getGcRecipientEmail());
			}
		}
	}
	
	
	/**
	 * Creating Email using FreeMarker Templates(FTL), for iPhone module.
	 * @param String emailId
	 * @return
	 */
	public FTLEmailI createIPhoneEmail(String emailId) {
		
		String iPhoneMediaPath = FDStoreProperties.getMediaIPhoneTemplatePath();
		String ftlPath =  IPHONE_FTL_TEMPLATE_NAME;		
		FDFtlEmail email = new FDFtlEmail();
		email.setFromAddress(new EmailAddress("FreshDirect", "service@freshdirect.com"));
		email.getParameters().put(MailName.IPHONE_FTL_PATH, ftlPath);
		email.getParameters().put("iPhoneMediaRoot", iPhoneMediaPath);
		email.setSubject(FDStoreProperties.getIPhoneEmailSubject());
		email.getParameters().put(MailName.TO_ADDRESS, emailId);
		email.setRecipient(emailId);
		return email;
	}
	
	/**
	 * Creating Email using FreeMarker Templates(FTL), for GiftCards module.
	 * @param giftCardInfo
	 * @return
	 */
	public FTLEmailI createGCEmail(GiftCardOrderInfo giftCardInfo) {
		
		String gcMediaPath = FDStoreProperties.getMediaGiftCardTemplatePath();//&& !"".equalsIgnoreCase(FDStoreProperties.getMediaGiftCardTemplatePath()))?FDStoreProperties.getMediaGiftCardTemplatePath():"media/editorial/giftcards/");
		String ftlPath = FDStoreProperties.getMediaGiftCardTemplatePath()+giftCardInfo.getGcType()+"/"+GC_FTL_TEMPLATE_NAME;		
		FDGiftCardFtlEmail email = new FDGiftCardFtlEmail(giftCardInfo);
		email.decorateMap();
		email.getParameters().put(MailName.GC_FTL_PATH, ftlPath);
		email.getParameters().put("gcMediaRoot", FDStoreProperties.getGCTemplateBaseUrl()+gcMediaPath+giftCardInfo.getGcType()+"/");
		//email.getParameters().put("gcMediaRootWithTemplate", FDStoreProperties.getMediaPath()+gcMediaPath+giftCardInfo.getGcType()+"/");
		email.setFromAddress(new EmailAddress(giftCardInfo.getGcFrom(), giftCardInfo.getGcSenderEmail()));
		email.setSubject("A Gift for You");
		return email;
	}
	
	/*public XMLEmailI createGiftCardPurchaseConfirmationEmail(ErpCustomerInfoModel custInfoModel, ErpAbstractOrderModel orderModel){
		FDTransactionalEmail email = new FDTransactionalEmail(custInfoModel, orderModel);
//		email.setXslPath("h_order_confirm_v1.xsl", "x_order_confirm_v1.xsl");
//		email.setFromAddress(new EmailAddress(GENERAL_LABEL, "From"));//getFromAddress(custInfoModel.getDepotCode())));
//		email.setSubject("Your order for " + df.format(orderModel.getRequestedDate()));
		return email;
	}*/
	
	/*public XMLEmailI createGCBalanceTransferEmail(final String recipientName, ErpCustomerInfoModel custInfo){
		
		ErpInfoEmail email = new ErpInfoEmail(custInfo){
			protected void decorateMap(Map map) {
				map.put("recipientName", recipientName);
			}			
		};
		email.setXslPath("h_gc_balance_transfer.xsl", "x_gc_balance_transfer.xsl");		
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, FDStoreProperties.getCustomerServiceEmail()));
		email.setSubject("Your Gift Card order Balance Transfer");
		return email;
	}*/
	
	
}
