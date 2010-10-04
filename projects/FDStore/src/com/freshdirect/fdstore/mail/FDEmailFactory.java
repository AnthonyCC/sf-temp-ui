package com.freshdirect.fdstore.mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;
import org.dom4j.Document;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCustomerEmailModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.BookRetailer;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.MediaI;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeSource;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.mail.EmailAddress;
import com.freshdirect.framework.mail.EmailSupport;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDEmailFactory {
	
	private static FDEmailFactory _sharedInstance; // singleton holder
	
	private static final Category LOGGER = LoggerFactory.getInstance(FDEmailFactory.class);
	
	public static final SimpleDateFormat df = new SimpleDateFormat("EEEE, MMM d yyyy");
	public static final SimpleDateFormat DT_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	public static final String GENERAL_CS_EMAIL = FDStoreProperties.getCustomerServiceEmail();
	public static final String PRODUCT_EMAIL = FDStoreProperties.getProductEmail();
	public static final String FEEDBACK_EMAIL = FDStoreProperties.getFeedbackEmail();
	public static final String CHEFSTABLE_EMAIL = FDStoreProperties.getChefsTableEmail();
	public static final String GENERAL_LABEL = "FreshDirect";
	public static final String VENDING_EMAIL = FDStoreProperties.getVendingEmail();


	// default instance getter
	public static FDEmailFactory getInstance() {
		if (_sharedInstance == null) {
			_sharedInstance = new FDEmailFactory();
		}
		return _sharedInstance;
	}


	public XMLEmailI createFinalAmountEmail(FDCustomerInfo customer, FDOrderI order) {
		FDTransactionalEmail email = new FDTransactionalEmail(customer, order);
		email.setXslPath("h_final_amount_confirm_v2.xsl", "x_final_amount_confirm_v2.xsl");

		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));

		if (EnumDeliveryType.PICKUP.equals(order.getDeliveryType())) {
			email.setSubject("Your order for " + df.format(order.getRequestedDate()) + " is ready to be picked up.");
		} else {
			email.setSubject("Your order for " + df.format(order.getRequestedDate()) + " is on its way");
		}

		return email;
	}

	public XMLEmailI createConfirmOrderEmail(FDCustomerInfo customer, FDOrderI order) {
		
		FDTransactionalEmail email = new FDTransactionalEmail(customer, order);

		email.setXslPath("h_order_confirm_v1.xsl", "x_order_confirm_v1.xsl");

		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));

		email.setSubject("Your order for " + df.format(order.getRequestedDate()));

		return email;
	}

	public XMLEmailI createModifyOrderEmail(FDCustomerInfo customer, FDOrderI order) {
		FDTransactionalEmail email = new FDTransactionalEmail(customer, order);

		email.setXslPath("h_order_change_v1.xsl", "x_order_change_v1.xsl");
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		email.setSubject("Your order information has been updated");

		return email;
	}

	public XMLEmailI createChargeOrderEmail(FDCustomerInfo customer, FDOrderI order, double additionalCharge) {
		FDTransactionalEmail email = new FDTransactionalEmail(customer, order);

		email.setXslPath("h_order_charge_v1.xsl", "x_order_charge_v1.xsl");
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		String subject = "Your order has been charged";
		if (order.getPaymentMethod() != null) {
			subject += " to your " + order.getPaymentMethod().getPaymentMethodType().getDescription().toLowerCase(); 
		}		
		email.setSubject(subject);

		return email;
	}

	public XMLEmailI createCancelOrderEmail(FDCustomerInfo customer, String orderNumber, Date startTime, Date endTime) {
		FDCancelOrderConfirmEmail email = new FDCancelOrderConfirmEmail(customer, orderNumber, startTime, endTime);

		email.setXslPath("h_order_cancel_v1.xsl", "x_order_cancel_v1.xsl");
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		email.setSubject("Cancellation receipt");
		return email;
	}

	public XMLEmailI createConfirmCreditEmail(FDCustomerInfo customer, String saleId, ErpComplaintModel complaint) {
		FDConfirmCreditEmail email = new FDConfirmCreditEmail(customer, saleId, complaint);
		//email.setXslPath("h_credit_confirm_v1.xsl", "x_credit_confirm_v1.xsl");
		
		// get the xslpath from the email object in the complaint.
		ErpCustomerEmailModel custEmailObj = complaint.getCustomerEmail();
		email.setXslPath(custEmailObj.getHtmlXslPath(),custEmailObj.getPlainTextXslPath());
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		email.setSubject("We've issued your credits");

		return email;
	}

	public XMLEmailI createForgotPasswordEmail(FDCustomerInfo customer, String requestId, Date expiration, List ccList) {
		String passwordLink =
			ErpServicesProperties.getForgotPasswordPage() + "?email=" + customer.getEmailAddress() + "&link=" + requestId;

		FDForgotPasswordEmail email = new FDForgotPasswordEmail(customer, passwordLink, expiration);

		email.setXslPath("h_password_link_v1.xsl", "x_password_link_v1.xsl");
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		email.setCCList(ccList);
		email.setSubject("Important message from FreshDirect");

		return email;
	}

	public XMLEmailI createConfirmSignupEmail(FDCustomerInfo customer) {
		FDInfoEmail email = new FDInfoEmail(customer);
		if (customer.isPickupOnly()) {
			email.setXslPath("h_pickup_signup_confirm.xsl", "x_pickup_signup_confirm.xsl");
		} else if (customer.isCorporateUser()) {
			email.setXslPath("h_signup_confirm_corp.xsl", "x_signup_confirm_corp.xsl");
		} else {
			email.setXslPath("h_signup_confirm_v2.xsl", "x_signup_confirm_v2.xsl");
		}
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		email.setSubject("Welcome to FreshDirect!");

		return email;
	}
	
	public XMLEmailI createAuthorizationFailedEmail(FDCustomerInfo customer, String orderNumber, Date startTime, Date endTime, Date cutoffTime){
		FDAuthorizationFailedEmail email = new FDAuthorizationFailedEmail(customer, orderNumber, startTime, endTime, cutoffTime);
		email.setXslPath("h_authorization_failure.xsl", "x_authorization_failure.xsl");
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		email.setSubject("Credit Card Authorization Failure");
		

		return email; 
	}
	/*
	 * Added for APPDEV-89 . Sending a seperate Auth failed email to auto renew DP customers.
	 * AR - Stands for Auto Renew DP
	 */
	public XMLEmailI createARAuthorizationFailedEmail(FDCustomerInfo customer, String orderNumber, Date startTime, Date endTime, Date cutoffTime){
		FDAuthorizationFailedEmail email = new FDAuthorizationFailedEmail(customer, orderNumber, startTime, endTime, cutoffTime);
		email.setXslPath("h_ar_authorization_failure.xsl", "x_ar_authorization_failure.xsl");
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		email.setSubject("Credit Card Authorization Failure");
		

		return email; 
	}
	public XMLEmailI createReminderEmail(FDCustomerInfo customer, boolean sendToAltEmail) {
		FDInfoEmail email = new FDInfoEmail(customer);
		email.setXslPath("h_reminder_service.xsl", "x_reminder_service.xsl");
		if(sendToAltEmail){
			List<String> cc = new ArrayList<String>();
			cc.add(customer.getAltEmailAddress());
			email.setCCList(cc);
		}
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		email.setSubject("A friendly reminder from FreshDirect.");
		return email;
	}

	public XMLEmailI createGenericEmail(FDCustomerInfo customer, String subject, Document body) {
		FDGenericEmail email = new FDGenericEmail(customer, body);

		email.setXslPath("h_generic.xsl", "x_generic.xsl");
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		email.setSubject(subject);

		return email;
	}

	public XMLEmailI createRecipeEmail(FDCustomerInfo customer, Recipe recipe) {
		FDRecipeEmail email = new FDRecipeEmail(customer, recipe);

		email.setXslPath("h_recipe.xsl", "x_recipe.xsl");
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		email.setSubject("FreshDirect - Recipe for " + recipe.getName());

		return email;
	}

	public XMLEmailI createTellAFriendEmail(TellAFriend mailInfo, boolean preview) {
		FDTellAFriendEmail email = new FDTellAFriendEmail(mailInfo, preview);
		email.setXslPath(mailInfo.getXsltPath());

		return email;
	}

	public XMLEmailI createProductRequestEmail(FDCustomerInfo customerInfo, String subject, String body) {
		FDProductRequestEmail email = new FDProductRequestEmail(body);

		if (customerInfo == null) {
			email.setFromAddress(new EmailAddress("Unidentified Customer", GENERAL_CS_EMAIL));
		} else {
			email.setFromAddress(
				new EmailAddress(customerInfo.getFirstName() + " " + customerInfo.getLastName(), customerInfo.getEmailAddress()));
		}

		email.setSubject(subject);

		return email;
	}

	public XMLEmailI createChefsTableEmail(FDCustomerInfo customerInfo, String subject, String body) {
		ChefsTableEmail email = new ChefsTableEmail(body);

		if (customerInfo == null) {
			email.setFromAddress(new EmailAddress("Unidentified Customer", GENERAL_CS_EMAIL));
		} else {
			email.setFromAddress(
				new EmailAddress(customerInfo.getFirstName() + " " + customerInfo.getLastName(), customerInfo.getEmailAddress()));
		}

		email.setSubject(subject);

		return email;
	}

	public XMLEmailI createContactServiceEmail(FDCustomerInfo customerInfo, String subject, String body) {
		FDContactServiceEmail email = new FDContactServiceEmail(body);

		if (customerInfo == null) {
			email.setFromAddress(new EmailAddress("Unidentified Customer", GENERAL_CS_EMAIL));
		} else {
			email.setFromAddress(
				new EmailAddress(customerInfo.getFirstName() + " " + customerInfo.getLastName(), customerInfo.getEmailAddress()));
		}

		email.setSubject(subject);

		return email;
	}
	
	public XMLEmailI createVendingEmail(FDCustomerInfo customerInfo, String subject, String body) {
		VendingEmail email = new VendingEmail(body);

		if (customerInfo == null) {
			email.setFromAddress(new EmailAddress("Unidentified Customer", GENERAL_CS_EMAIL));
		} else {
			email.setFromAddress(
				new EmailAddress(customerInfo.getFirstName() + " " + customerInfo.getLastName(), customerInfo.getEmailAddress()));
		}

		email.setSubject(subject);

		return email;
	}
	
	public XMLEmailI createCorporateServiceInterestEmail(ErpCustomerInfoModel erpCustomerInfo, String subject, String body) {
		FDContactServiceEmail email = new CorporateServiceEmail(body);

		if (erpCustomerInfo == null) {
			email.setFromAddress(new EmailAddress("Unidentified Customer", GENERAL_CS_EMAIL));
		} else {
			email.setFromAddress(
				new EmailAddress(erpCustomerInfo.getFirstName() + " " + erpCustomerInfo.getLastName(), erpCustomerInfo.getEmail()));
		}

		email.setSubject(subject);

		return email;
	}
	
	public XMLEmailI createCateringEmail(ErpCustomerInfoModel erpCustomerInfo, String subject, String body) {
		FDContactServiceEmail email = new CateringEmail(body);

		if (erpCustomerInfo == null) {
			email.setFromAddress(new EmailAddress("Unidentified Customer", GENERAL_CS_EMAIL));
		} else {
			email.setFromAddress(
				new EmailAddress(erpCustomerInfo.getFirstName() + " " + erpCustomerInfo.getLastName(), erpCustomerInfo.getEmail()));
		}

		email.setSubject(subject);

		return email;
	}
	
	public XMLEmailI createFeedbackEmail(FDCustomerInfo customerInfo, String subject, String body) {
		FeedbackEmail email = new FeedbackEmail(body);
			
		if (customerInfo == null) {
			email.setFromAddress(new EmailAddress("Unidentified Customer", GENERAL_CS_EMAIL));
		} else {
			email.setFromAddress(
				new EmailAddress(customerInfo.getFirstName() + " " + customerInfo.getLastName(), customerInfo.getEmailAddress()));
		}
	
		email.setSubject(subject);

			return email;
		}
	
	////////

	protected String getFromAddress(String depotCode) {
		if (depotCode == null || "".equals(depotCode)) {
			return GENERAL_CS_EMAIL;
		}
		try {
			return FDDepotManager.getInstance().getCustomerServiceEmail(depotCode);
		} catch (FDResourceException re) {
			LOGGER.warn("Could not get the correct email", re);
			return GENERAL_CS_EMAIL;
		}
	}

	private static class FDGenericEmail extends FDInfoEmail {
		private final Document body;

		public FDGenericEmail(FDCustomerInfo customer, Document body) {
			super(customer);
			this.body = body;
		}

		/**
		 * @see com.freshdirect.fdstore.mail.FDInfoEmail#decorateMap(java.util.Map)
		 */
		protected void decorateMap(Map map) {
			super.decorateMap(map);
			map.put("body", this.body);
		}

	}

	private static class FDRecipeEmail extends FDInfoEmail {
		private final Recipe   recipe;

		public FDRecipeEmail(FDCustomerInfo customer, Recipe recipe) {
			super(customer);
			this.recipe = recipe;
		}

		/**
		 *  Load all details of the recipe, and put it into the supplied map.
		 *  
		 *  @param map the map to fill with the details of the recipe.
		 *  @see com.freshdirect.fdstore.mail.FDInfoEmail#decorateMap(java.util.Map)
		 */
		protected void decorateMap(Map map) {
			super.decorateMap(map);
			
			MediaI			media;
			
			map.put("recipeId", recipe.getContentName());
			map.put("recipeName", recipe.getName());
			map.put("trackingCode", "eau");
			
			// TODO pass "efr" for send-recipe-to-friend
			map.put("trackingCode", "eau");
			
			if (recipe.getSource() != null) {
				RecipeSource		source      = recipe.getSource();
				List 			retailers   = source.getBookRetailers();
				Set				retailerSet = new HashSet();
				
				map.put("sourceId", source.getContentKey().getId());
				map.put("sourceName", source.getName());
				map.put("sourceIsbn", source.getIsbn());
				
				Image photo = source.getZoomImage();
				if (photo != null) {
					map.put("photoUrl", photo.getPath());
					map.put("photoWidth", Integer.toString(photo.getWidth()));
					map.put("photoHeight", Integer.toString(photo.getHeight()));
				}
				
				for (Iterator it = retailers.iterator(); it.hasNext();) {
					BookRetailer retailer    = (BookRetailer) it.next();
					Map			 retailerMap = new HashMap();
					
					retailerMap.put("id", retailer.getContentKey().getId());
					retailerMap.put("name", retailer.getName());
					Image logo = retailer.getLogo();
					if (logo != null) {
						retailerMap.put("logo", logo.getPath());
						retailerMap.put("logoWidth", Integer.toString(logo.getWidth()));
						retailerMap.put("logoHeight", Integer.toString(logo.getHeight()));
					}
					retailerSet.add(retailerMap);
				}
				
				if (!retailerSet.isEmpty()) {
					map.put("retailer", retailerSet);
				}
			}
			
			map.put("authorNames", recipe.getAuthorNames());
			map.put("recipeDescription", loadMedia(recipe.getDescription()));
			
			
			media = recipe.getIngredientsMedia();
			if (media != null) {
				map.put("ingredientsMedia", loadMedia(media));
			}
			
			media = recipe.getPreparationMedia();
			if (media != null) {
				map.put("preparationMedia", loadMedia(media));
			}
			
			media = recipe.getCopyrightMedia();
			if (media != null) {
				map.put("copyrightMedia", loadMedia(media));
			}
			
		}

		/**
		 *  Resolve a path.
		 *  
		 *  @param rootPath the base path
		 *  @param childPath the path to resolve, relative to rootPath
		 *  @return a full URL to childPath, in relation to rootPath
		 */
		public static URL resolve(String rootPath, String childPath) throws IOException {
			URL url = new URL(rootPath);
			if (childPath.startsWith("/")) {
				childPath = childPath.substring(1, childPath.length());
			}
			url = new URL(url, childPath);

			if (!url.toString().startsWith(rootPath)) {
				throw new IOException("Child path not under root");
			}

			return url;
		}
		
		public boolean isHtmlEmail() {
			return true;
		}
		
		/**
		 *  Load media contents.
		 *  
		 *  @param media the media to load
		 *  @return the contents of the media, as a string,
		 *          or an empty string on errors
		 */
		private String loadMedia(MediaI media) {
			if (media == null) {
				return "";
			}
			
			InputStream     in  = null;
			StringBuffer		out = new StringBuffer(); 
			try {
	
				URL url = resolve(FDStoreProperties.getMediaPath(), media.getPath());
				in = url.openStream();
				if (in == null) {
					return "";
				}
	
				byte[] buf = new byte[4096];
				int i;
				while ((i = in.read(buf)) != -1) {
					out.append(new String(buf, 0, i));
				}
	
				return out.toString();
	
			} catch (FileNotFoundException e) {
				LOGGER.warn("Media file not found " + media.getPath());
				return "";
	
			} catch (IOException e) {
				LOGGER.warn("Failed to load resource", e);
	
				return "";
			} finally {
				try {
					if (in != null)
						in.close();
				} catch (IOException ex) {
				}
			}
		}
		
	}

	private static class FDForgotPasswordEmail extends FDInfoEmail {

		private String passwordLink;
		private Date expirationTime;

		public FDForgotPasswordEmail(FDCustomerInfo customer, String passwordLink, Date expirationTime) {
			super(customer);
			this.passwordLink = passwordLink;
			this.expirationTime = expirationTime;
		}

		/**
		 * @see com.freshdirect.fdstore.mail.FDInfoEmail#decorateMap(java.util.Map)
		 */
		protected void decorateMap(Map map) {
			super.decorateMap(map);
			map.put("passwordLink", this.passwordLink);
			map.put("expirationTime", DT_FORMATTER.format(this.expirationTime));
		}

	}

	private static class FDCancelOrderConfirmEmail extends FDInfoEmail {

		private String orderNumber;
		private Date deliveryStartTime;
		private Date deliveryEndTime;

		public FDCancelOrderConfirmEmail(FDCustomerInfo customer, String orderNumber, Date startTime, Date endTime) {
			super(customer);
			this.orderNumber = orderNumber;
			this.deliveryStartTime = startTime;
			this.deliveryEndTime = endTime;
		}

		/**
		 * @see com.freshdirect.fdstore.mail.FDInfoEmail#decorateMap(java.util.Map)
		 */
		protected void decorateMap(Map map) {
			super.decorateMap(map);
			map.put("orderNumber", this.orderNumber);
			map.put("deliveryStartTime", DT_FORMATTER.format(this.deliveryStartTime));
			map.put("deliveryEndTime", DT_FORMATTER.format(this.deliveryEndTime));
		}

	}
	
	static class FDAuthorizationFailedEmail extends FDInfoEmail {

		private String orderNumber;
		private Date deliveryStartTime;
		private Date deliveryEndTime;
		private Date cutoffTime;

		public FDAuthorizationFailedEmail(FDCustomerInfo customer, String orderNumber, Date startTime, Date endTime, Date cutoffTime) {
			super(customer);
			this.orderNumber = orderNumber;
			this.deliveryStartTime = startTime;
			this.deliveryEndTime = endTime;
			this.cutoffTime = cutoffTime;
		}

		/**
		 * @see com.freshdirect.fdstore.mail.FDInfoEmail#decorateMap(java.util.Map)
		 */
		protected void decorateMap(Map map) {
			super.decorateMap(map);
			map.put("orderNumber", this.orderNumber);
			map.put("deliveryStartTime", DT_FORMATTER.format(this.deliveryStartTime));
			map.put("deliveryEndTime", DT_FORMATTER.format(this.deliveryEndTime));
			map.put("cutoffTime", DT_FORMATTER.format(this.cutoffTime));
		}

	}

	protected static class FDConfirmCreditEmail extends FDInfoEmail {
		private ErpComplaintModel complaint;
		private String saleId;

		public FDConfirmCreditEmail(FDCustomerInfo customer, String saleId, ErpComplaintModel complaint) {
			super(customer);
			this.saleId = saleId;
			this.complaint = complaint;
			this.getBCCList().addAll(FDStoreProperties.getIssueCreditBccAddresses());
		}

		protected void decorateMap(Map map) {
			super.decorateMap(map);
			map.put("complaint", this.complaint);
			map.put("saleId", this.saleId);
		}
	}
	
	private static class FDTellAFriendEmail extends EmailSupport implements XMLEmailI {

		private String xslPath;
		private TellAFriend mailInfo;
		private boolean preview;

		public FDTellAFriendEmail(TellAFriend mailInfo, boolean preview) {
			this.setFromAddress(
				new EmailAddress(
					mailInfo.getCustomerFirstName() + " " + mailInfo.getCustomerLastName(),
					mailInfo.getCustomerEmail()));

			this.setRecipient(mailInfo.getFriendEmail());
			this.setSubject(getSubject(mailInfo));

			this.mailInfo = mailInfo;
			this.preview = preview;
		}

		protected void decorateMap(Map map) {

			map.put("preview", new Boolean(this.preview));
			map.put("mailInfo", this.mailInfo);
			map.put("productEmail", new Boolean(mailInfo instanceof TellAFriendProduct));
			map.put("tellAFriendEmail", new Boolean(true));
			
			if (mailInfo instanceof TellAFriendRecipe) {
				TellAFriendRecipe	tafr   = (TellAFriendRecipe) mailInfo;
				Recipe              recipe = tafr.getRecipe();
				
				// this is the same code as in FDRecipeEmail
				// TODO: somehow refactor to avoid code duplication
				MediaI			media;
				
				map.put("recipeId", recipe.getContentName());
				map.put("recipeName", recipe.getName());
				map.put("trackingCode", "efr");
				
				if (recipe.getSource() != null) {
					RecipeSource		source      = recipe.getSource();
					List 			retailers   = source.getBookRetailers();
					Set				retailerSet = new HashSet();
					
					map.put("sourceId", source.getContentKey().getId());
					map.put("sourceName", source.getName());
					map.put("sourceIsbn", source.getIsbn());
					
					Image photo = source.getZoomImage();
					if (photo != null) {
						map.put("photoUrl", photo.getPath());
						map.put("photoWidth", Integer.toString(photo.getWidth()));
						map.put("photoHeight", Integer.toString(photo.getHeight()));
					}
					
					for (Iterator it = retailers.iterator(); it.hasNext();) {
						BookRetailer retailer    = (BookRetailer) it.next();
						Map			 retailerMap = new HashMap();
						
						retailerMap.put("id", retailer.getContentKey().getId());
						retailerMap.put("name", retailer.getName());
						Image logo = retailer.getLogo();
						if (logo != null) {
							retailerMap.put("logo", logo.getPath());
							retailerMap.put("logoWidth", Integer.toString(logo.getWidth()));
							retailerMap.put("logoHeight", Integer.toString(logo.getHeight()));
						}
						retailerSet.add(retailerMap);
					}
					
					if (!retailerSet.isEmpty()) {
						map.put("retailer", retailerSet);
					}
				}
				
				map.put("authorNames", recipe.getAuthorNames());
				map.put("recipeDescription", loadMedia(recipe.getDescription()));
				
				
				media = recipe.getIngredientsMedia();
				if (media != null) {
					map.put("ingredientsMedia", loadMedia(media));
				}
				
				media = recipe.getPreparationMedia();
				if (media != null) {
					map.put("preparationMedia", loadMedia(media));
				}
				
				media = recipe.getCopyrightMedia();
				if (media != null) {
					map.put("copyrightMedia", loadMedia(media));
				}				
			}
		}
		
		/**
		 *  Resolve a path.
		 *  
		 *  @param rootPath the base path
		 *  @param childPath the path to resolve, relative to rootPath
		 *  @return a full URL to childPath, in relation to rootPath
		 */
		public static URL resolve(String rootPath, String childPath) throws IOException {
			// this is the same code as in FDRecipeEmail
			// TODO: somehow refactor to avoid code duplication
			
			URL url = new URL(rootPath);
			if (childPath.startsWith("/")) {
				childPath = childPath.substring(1, childPath.length());
			}
			url = new URL(url, childPath);

			if (!url.toString().startsWith(rootPath)) {
				throw new IOException("Child path not under root");
			}

			return url;
		}
				
		/**
		 *  Load media contents.
		 *  
		 *  @param media the media to load
		 *  @return the contents of the media, as a string,
		 *          or an empty string on errors
		 */
		private String loadMedia(MediaI media) {
			// this is the same code as in FDRecipeEmail
			// TODO: somehow refactor to avoid code duplication
			
			if (media == null) {
				return "";
			}
			
			InputStream     in  = null;
			StringBuffer		out = new StringBuffer(); 
			try {
	
				URL url = resolve(FDStoreProperties.getMediaPath(), media.getPath());
				in = url.openStream();
				if (in == null) {
					return "";
				}
	
				byte[] buf = new byte[4096];
				int i;
				while ((i = in.read(buf)) != -1) {
					out.append(new String(buf, 0, i));
				}
	
				return out.toString();
	
			} catch (FileNotFoundException e) {
				LOGGER.warn("Media file not found " + media.getPath());
				return "";
	
			} catch (IOException e) {
				LOGGER.warn("Failed to load resource", e);
	
				return "";
			} finally {
				try {
					if (in != null)
						in.close();
				} catch (IOException ex) {
				}
			}
		}

		public final String getXML() {
			FDXMLSerializer s = new FDXMLSerializer();
			Map map = new HashMap();
			this.decorateMap(map);
			return s.serialize("fdemail", map);
		}

		public String getXslPath() {
			return this.xslPath;
		}

		public void setXslPath(String xslPath) {
			this.xslPath = ErpServicesProperties.getMailerXslHome() + xslPath;
		}

		public boolean isHtmlEmail() {
			return true;
		}

		private static String getSubject(TellAFriend mailInfo) {
			String subject = "";
			if (mailInfo instanceof TellAFriendProduct) {
				TellAFriendProduct tafp = (TellAFriendProduct) mailInfo;
				subject = "Your friend " + tafp.getCustomerFirstName() + " has sent you " + tafp.getProductTitle() + "!";
			} else if (mailInfo instanceof TellAFriendRecipe) {
				TellAFriendRecipe	tafr   = (TellAFriendRecipe) mailInfo;
				Recipe              recipe = tafr.getRecipe();
				subject = "Your friend " + mailInfo.getCustomerFirstName() + " has sent you " + recipe.getName();
			} else {
				subject = "Your friend " + mailInfo.getCustomerFirstName() + " would like you to try FreshDirect";
			}
			return subject;
		}

	}

	private static class FDContactServiceEmail extends EmailSupport implements XMLEmailI {

		private final String body;

		public FDContactServiceEmail(String body) {
			this.setRecipient(GENERAL_CS_EMAIL);
			this.body = body;
		}

		public String getXslPath() {
			return ErpServicesProperties.getMailerXslHome() + "x_contact_service.xsl";
		}

		public boolean isHtmlEmail() {
			return false;
		}

		public String getXML() {
			FDXMLSerializer s = new FDXMLSerializer();
			Map map = new HashMap();
			map.put("body", this.body);
			return s.serialize("fdemail", map);
		}

	}

	private static class FDProductRequestEmail extends FDContactServiceEmail implements XMLEmailI {

		public FDProductRequestEmail(String body) {
			super(body);
			this.setRecipient(PRODUCT_EMAIL);
		}

	}
	
	private static class CorporateServiceEmail extends FDContactServiceEmail implements XMLEmailI {

		public CorporateServiceEmail(String body) {
			super(body);
			this.setRecipient("corporateservices@freshdirect.com");
		}

	}
	
	private static class CateringEmail extends FDContactServiceEmail implements XMLEmailI {

		public CateringEmail(String body) {
			super(body);
			this.setRecipient("catering@freshdirect.com");
		}

	}
	
	private static class FeedbackEmail extends FDContactServiceEmail implements XMLEmailI {

		public FeedbackEmail(String body) {
			super(body);
			this.setRecipient(FEEDBACK_EMAIL);
		}

	}
	private static class ChefsTableEmail extends FDContactServiceEmail implements XMLEmailI {

		public ChefsTableEmail(String body) {
			super(body);
			this.setRecipient(CHEFSTABLE_EMAIL);
		}

	}	
	
	private static class VendingEmail extends FDContactServiceEmail implements XMLEmailI {

		public VendingEmail(String body) {
			super(body);
			this.setRecipient(VENDING_EMAIL);
		}

	}
	
	private static class FDDPCreditEmail extends FDInfoEmail {
		
		private final String saleId;
		private final int creditCount;
		private final String dpName;

		public FDDPCreditEmail(FDCustomerInfo customer,String saleId,int creditCount, String dpName) {
			
			super(customer);
			this.saleId=saleId;
			this.creditCount=creditCount;
			this.dpName=dpName;
		}

		/**
		 * @see com.freshdirect.fdstore.mail.FDInfoEmail#decorateMap(java.util.Map)
		 */
		protected void decorateMap(Map map) {
			super.decorateMap(map);
			map.put("saleId", this.saleId);
			map.put("creditCount", String.valueOf(this.creditCount));
			map.put("dpName", this.dpName);
		}

	}
	
	
	public XMLEmailI createDPCreditEmail(FDCustomerInfo customer, String saleId, int creditCount, String dpName) {
		FDDPCreditEmail email = new FDDPCreditEmail(customer, saleId, creditCount,dpName);

		email.setXslPath("h_dp_credits_v1.xsl", "x_dp_credits_v1.xsl");
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		email.setSubject("We've credited your DeliveryPass.");
		return email;
	}

	public XMLEmailI createStandingOrderErrorEmail(FDCustomerInfo customer, FDStandingOrder standingOrder) {
		FDStandingOrderErrorEmail email = new FDStandingOrderErrorEmail(customer, standingOrder);

		email.setXslPath("h_standing_order_error_v1.xsl", "x_standing_order_error_v1.xsl");

		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));

		email.setSubject("A problem with your standing order for " + df.format(standingOrder.getNextDeliveryDate()));

		return email;
	}

	public XMLEmailI createConfirmStandingOrderEmail(FDCustomerInfo customer, FDOrderI order, FDStandingOrder standingOrder, boolean hasUnavailableItems) {
		FDStandingOrderEmail email = new FDStandingOrderEmail(customer, order, standingOrder, hasUnavailableItems);

		email.setXslPath("h_standing_order_confirm_v1.xsl", "x_standing_order_confirm_v1.xsl");

		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));

		StringBuilder subject = new StringBuilder("Your standing order for ");
		subject.append(df.format(order.getRequestedDate()));
		if (hasUnavailableItems)
			subject.append(" (some items unavailable)");
		email.setSubject(subject.toString());

		return email;
	}

	public XMLEmailI createConfirmDeliveryStandingOrderEmail(FDCustomerInfo customer, FDOrderI order, FDStandingOrder standingOrder) {
		FDStandingOrderEmail email = new FDStandingOrderEmail(customer, order, standingOrder, false);

		email.setXslPath("h_standing_order_delivery_v1.xsl", "x_standing_order_delivery_v1.xsl");

		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));

		email.setSubject("Reminder, your standing order for " + df.format(order.getRequestedDate()));

		return email;
	}
	public XMLEmailI createSettlementFailedEmail(FDCustomerInfo customer, String orderNumber, Date startTime, Date endTime, Date cutoffTime){
		FDSettlementFailedEmail email = new FDSettlementFailedEmail(customer, orderNumber, startTime, endTime, cutoffTime);
		email.setXslPath("h_settlement_failure.xsl", "x_settlement_failure.xsl");
		email.setFromAddress(new EmailAddress(GENERAL_LABEL, getFromAddress(customer.getDepotCode())));
		email.setSubject("e-Check Settlement Failure");
		

		return email; 
	}
	static class FDSettlementFailedEmail extends FDInfoEmail {

		private String orderNumber;
		private Date deliveryStartTime;
		private Date deliveryEndTime;
		private Date cutoffTime;

		public FDSettlementFailedEmail(FDCustomerInfo customer, String orderNumber, Date startTime, Date endTime, Date cutoffTime) {
			super(customer);
			this.orderNumber = orderNumber;
			this.deliveryStartTime = startTime;
			this.deliveryEndTime = endTime;
			this.cutoffTime = cutoffTime;
		}

		/**
		 * @see com.freshdirect.fdstore.mail.FDInfoEmail#decorateMap(java.util.Map)
		 */
		protected void decorateMap(Map map) {
			super.decorateMap(map);
			map.put("orderNumber", this.orderNumber);
			map.put("deliveryStartTime", DT_FORMATTER.format(this.deliveryStartTime));
			map.put("deliveryEndTime", DT_FORMATTER.format(this.deliveryEndTime));
			map.put("cutoffTime", DT_FORMATTER.format(this.cutoffTime));
		}

	}
	
}
