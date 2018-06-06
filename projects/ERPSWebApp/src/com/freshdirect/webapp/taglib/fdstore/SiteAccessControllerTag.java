package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.webapp.action.Action;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.fdstore.RegistrationAction;
import com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag;
import com.freshdirect.webapp.util.AccountUtil;
import com.freshdirect.webapp.util.StoreContextUtil;

public class SiteAccessControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance(SiteAccessControllerTag.class);
	
	private String action = null;
	private String successPage = null;
	private String moreInfoPage = null;
	private String failureHomePage = null;
	//changes for APPDEV-1196
	//private String altDeliveryHomePage = "alt_dlv_home.jsp";
	private String altDeliveryHomePage = "/site_access/site_access.jsp?ol=altHome";
	//private String altDeliveryCorporatePage = "alt_dlv_corporate.jsp";
	private String altDeliveryCorporatePage = "/site_access/site_access.jsp?ol=altCorp";
	//private String failureCorporatePage = "/survey/cos_site_access_survey.jsp";
	private String failureCorporatePage = "/site_access/site_access.jsp?ol=corpSurvey";
	private String deliveryaddrpage = "/social/DeliveryAddress.jsp";
	private String addressnotificationpage = "/social/AddressNotification.jsp";
	private String socialLoginRecognized ="/social/social_login_recognized.jsp";
	private String socialLoginAccountLinked ="/social/social_login_account_linked.jsp";
	
	private String failureCorporatePageCRM = null;
	
	private String resultName = null;

	private EnumServiceType serviceType = null;
	
	//EnumAvailableServiceType

	private AddressModel address = null;
	private EnumDeliveryStatus requestedServiceTypeDlvStatus;
	private Set availableServiceTypes;
	

	public void setAction(String action) {
		this.action = action;
	}

	public void setSuccessPage(String successPage) {		
		if (successPage != null && successPage.indexOf("://") != -1) {
			LOGGER.debug("successPage before throwing IllegalArgument Exception :"+successPage);
			throw new IllegalArgumentException("Invalid successPage specified");
		}
		
		this.successPage = successPage;
	}

	public void setMoreInfoPage(String moreInfoPage) {
		this.moreInfoPage = moreInfoPage;
	}

	public void setAltDeliveryHomePage(String altDeliveryHomePage) {
		this.altDeliveryHomePage = altDeliveryHomePage;
	}

	public void setAltDeliveryCorporatePage(String altDeliveryCommercialPage) {
		this.altDeliveryCorporatePage = altDeliveryCommercialPage;
	}
	
	public void setFailureCorporatePage (String failureCorporatePage){
		this.failureCorporatePage = failureCorporatePage;
	}
	
	public void setFailureHomePage(String failureHomePage) {
		this.failureHomePage = failureHomePage;
	}

	public void setResult(String resultName) {
		this.resultName = resultName;
	}
	
	public void setServiceType(String serviceTypeStr) {
		if(serviceTypeStr!=null){
			this.serviceType=EnumServiceType.getEnum(serviceTypeStr); 
		}
		if(this.serviceType==null){
			throw new IllegalArgumentException("Invalid ServiceType specified");
		}
	}
	

	public EnumDeliveryStatus getRequestedServiceTypeDlvStatus() {
		return requestedServiceTypeDlvStatus;
	}

	public void setRequestedServiceTypeDlvStatus(
			EnumDeliveryStatus requestedServiceTypeDlvStatus) {
		this.requestedServiceTypeDlvStatus = requestedServiceTypeDlvStatus;
	}
	
	public String getFailureCorporatePageCRM() {
		return failureCorporatePageCRM;
	}

	public Set getAvailableServiceTypes() {
		return availableServiceTypes;
	}

	public void setAvailableServiceTypes(Set availableServiceTypes) {
		this.availableServiceTypes = availableServiceTypes;
	}

	public void setFailureCorporatePageCRM(String failureCorporatePageCRM) {
		this.failureCorporatePageCRM = failureCorporatePageCRM;
	}
	
	/** keep in sync with CheckLoginStatusTag.createUser(String zipCode)*/
	@Override
    public int doStartTag() throws JspException {
		ActionResult result = new ActionResult();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String application = (String) request.getSession().getAttribute(SessionName.APPLICATION);
		
		this.pageContext.getSession().removeAttribute("morepage");

		if ("POST".equalsIgnoreCase(request.getMethod()) || "modalboxpost".equals(request.getParameter("actionName"))) {
			LOGGER.debug("[*****Moreinfopage is coming as*****]"+this.moreInfoPage);
			try {
				if ("saveEmail".equalsIgnoreCase(action)) {
					saveEmail(request, result);
					if (result.isSuccess()) {
						if(request.getSession().getAttribute("SocialDlvAddrFail") != null)
						{
							doRedirect(addressnotificationpage);	
						}
						else
						{
							return doRedirect(successPage);
						}
					}					
				} 
				
				else if ("checkByZipCode".equalsIgnoreCase(action)) {
					FDDeliveryServiceSelectionResult serviceResult = checkByZipCode(request, result);
					if(serviceResult!=null)
						setRequestedServiceTypeDlvStatus(serviceResult.getServiceStatus(this.serviceType));
					
					/* APPDEV-1888 - Check to see if email is present and validate	 */
					boolean isReferralRegistration = "true".equals(request.getParameter("referralRegistration"))?true:false;
					if(isReferralRegistration) {
						LOGGER.debug("[*****Processing referral registration, validating email*******]");
						String email = request.getParameter("email");
						if(!validEmail(email, result)) {
							LOGGER.debug("[*****Something is wrong, redirecting to:*****]" + failureHomePage);
							if(this.pageContext.getSession().getAttribute("MSG_FOR_LOGIN_PAGE") != null) {
								return doRedirect("/login/login_main.jsp?successPage=%2Findex.jsp");
							}
							if(failureHomePage != null)
								failureHomePage = failureHomePage + "&email_error=true";
							pageContext.setAttribute(resultName, result);
							return EVAL_BODY_BUFFERED;
						}						
					}
					
					if (result.isSuccess()) {
					    if (EnumTransactionSource.FOODKICK_WEBSITE.getCode().equals(application)){
					        if (EnumDeliveryStatus.DELIVER == serviceResult.getServiceStatus(this.serviceType)){
					            FDSessionUser user = (FDSessionUser) pageContext.getSession().getAttribute(SessionName.USER);
					            user.setAvailableServices(serviceResult.getAvailableServices());
					        } else {
					            result.addError(new ActionError(EnumUserInfoName.DLV_NOT_IN_ZONE.getCode(), SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS));
					        }
					        pageContext.setAttribute(resultName, result);
					        return EVAL_BODY_BUFFERED;
					    }
					    
                        UserUtil.newSession(request.getSession(), (HttpServletResponse) pageContext.getResponse());
						
						if("WEB".equals(this.serviceType.getName())){
							EnumDeliveryStatus homeDlvStatus = serviceResult.getServiceStatus(EnumServiceType.HOME);
							
							if (EnumDeliveryStatus.DELIVER.equals(homeDlvStatus)) {
								this.createUser(EnumServiceType.HOME, serviceResult.getAvailableServices());
							} else {
								EnumDeliveryStatus corpDlvStatus = serviceResult.getServiceStatus(EnumServiceType.CORPORATE);
								if (EnumDeliveryStatus.DELIVER.equals(corpDlvStatus)) {
									this.createUser(EnumServiceType.CORPORATE, serviceResult.getAvailableServices());
								}else { 
									this.createUser(EnumServiceType.PICKUP, serviceResult.getAvailableServices());
								}
							}
							//System.out.println(" WEB this.serviceType :"+this.serviceType);	
							if(isReferralRegistration) {
								doRedirect(this.moreInfoPage);
							} else {
								doRedirect(successPage);
							}
						} else {
							EnumDeliveryStatus dlvStatus = serviceResult.getServiceStatus(this.serviceType);
							//System.out.println(" NOTWEB this.serviceType :"+this.serviceType);
							
							if (EnumDeliveryStatus.DELIVER.equals(dlvStatus)) {
								this.createUser(this.serviceType, serviceResult.getAvailableServices());
							} else { 
								this.createUser(EnumServiceType.PICKUP, serviceResult.getAvailableServices());
							}
							
							LOGGER.debug("[*****Moreinfopage is coming as*****]"+this.moreInfoPage);
							
							if (this.moreInfoPage != null && this.moreInfoPage.indexOf("serviceType") == -1) {
								if (this.moreInfoPage.indexOf('?') < 0) {
									this.moreInfoPage += "?serviceType=" + this.serviceType.getName();
								} else {
									String[] moreInfoPageTemp = this.moreInfoPage.split("[?]", 0);
									this.moreInfoPage = moreInfoPageTemp[0]+"?serviceType=" + this.serviceType.getName() + "&" + moreInfoPageTemp[1];
								}
							}
							
							if (EnumServiceType.CORPORATE.equals(this.serviceType)) {																					
								if(EnumDeliveryStatus.DONOT_DELIVER.equals(dlvStatus)){
									// check home delivry is available
									if(EnumDeliveryStatus.DELIVER.equals(serviceResult.getServiceStatus(EnumServiceType.HOME))){
										// show E No Corporate HOME delivarable Survey presented /site_access/alt_dlv_home.jsp
										doRedirect(altDeliveryHomePage);
									}
									else if(EnumDeliveryStatus.DONOT_DELIVER.equals(serviceResult.getServiceStatus(EnumServiceType.HOME))){
										
										boolean inCallCenter = "callcenter".equalsIgnoreCase(application);
										if(!inCallCenter) {
											doRedirect(failureCorporatePage);
										}
										else {
											doRedirect(failureCorporatePageCRM);
										}
									}
									else if(EnumDeliveryStatus.PARTIALLY_DELIVER.equals(serviceResult.getServiceStatus(EnumServiceType.HOME))){
										// forward to more address page									
										doRedirect(moreInfoPage);
									}
									//System.out.println(" EnumDeliveryStatus.DONOT_DELIVER :"+EnumDeliveryStatus.DONOT_DELIVER);
									//return doRedirect(failureHomePage);
								}
	//							else if(EnumDeliveryStatus.PARTIALLY_DELIVER.equals(dlvStatus)){
	//								// forward to more address page
	//								doRedirect(moreInfoPage);
	//							}
								//return doRedirect(moreInfoPage);
							}
						
							if (EnumServiceType.HOME.equals(this.serviceType)) { 
								if(EnumDeliveryStatus.RARELY_DELIVER.equals(dlvStatus)){								
									// forward to /site_access/delivery.jsp with rarely deliver message( not required now)
									return doRedirect(failureHomePage);
								}
								else if(EnumDeliveryStatus.DONOT_DELIVER.equals(dlvStatus)){								
									// forward to /site_access/delivery.jsp with no deliver message(not required now) 
									return doRedirect(failureHomePage);
								}
							}						
							if (EnumDeliveryStatus.PARTIALLY_DELIVER.equals(dlvStatus)) {
								if(isReferralRegistration) {
									//Change overlay type for referral partial delivery
									String refMoreInfoPage = moreInfoPage.replaceAll("ol=moreInfo", "ol=partialmoreInfo");
									return doRedirect(refMoreInfoPage);
								}
								return doRedirect(moreInfoPage);
							}							
							if (EnumDeliveryStatus.DELIVER.equals(dlvStatus)) {
								if(isReferralRegistration) {
									return doRedirect(this.moreInfoPage);
								} else {
									return doRedirect(successPage);
								}
								//return doRedirect(successPage);
							}
							return doRedirect(failureHomePage);
						}
					}
				} else if ("checkByAddress".equalsIgnoreCase(action)) {
					checkByAddress(request, result);
				}else if ("tcAgreed".equalsIgnoreCase(action)) {
					setFDTcAccept(request, result);
					HttpSession session = pageContext.getSession();
					 String newURL = "";
					 if(FDStoreProperties.isLocalDeployment()){
						 newURL = "http" + "://" + request.getServerName() + ":" + request.getServerPort();
					 }else{
						 newURL = "https" + "://" + request.getServerName();
					 }
					if(null!=request.getParameter("socialNavPage")&&!"".equals(request.getParameter("socialNavPage"))){
						socialLoginRecognized = socialLoginRecognized + "?socialnetwork=" + request.getParameter("socialNavPage");	
						session.setAttribute("fdTcAgree", true);
						doRedirect(newURL+socialLoginRecognized);
					}else if (successPage.equals("nonIndex")){
					session.setAttribute("nextSuccesspage",null);
					session.setAttribute("nextSuccesspage", "nonIndex");
					session.setAttribute("LITESIGNUP_COMPLETE", "true");
					}else if (successPage.indexOf("index.jsp")==-1 || null!=request.getParameter("nextSuccesspage")){
					session.setAttribute("nextSuccesspage", request.getParameter("nextSuccesspage"));
					session.setAttribute("LITESIGNUP_COMPLETE", "true");
					}else if (successPage.indexOf("index.jsp")!=-1 && request.getParameter("nextSuccesspage").indexOf("index.jsp")!=-1){
					session.setAttribute("nextSuccesspage", "/login/index.jsp");
					session.setAttribute("LITESIGNUP_COMPLETE", "true");
					}else{
					session.setAttribute("TCAGREE_COMPLETE", "true");
					}
					session.setAttribute("fdTcAgree", true);
				} else if ("checkByAddressEX".equalsIgnoreCase(action)) {
					checkByAddress(request, result, false);
				} else if ("doPrereg".equalsIgnoreCase(action)) {
					doPrereg(request, result);
				} else if ("signupLite".equalsIgnoreCase(action)) { 
					HttpSession session = this.pageContext.getSession();
					FDDeliveryServiceSelectionResult serviceResult = checkSLiteZipCode(request, result);
					if(serviceResult!=null)
						setRequestedServiceTypeDlvStatus(serviceResult.getServiceStatus(this.serviceType));
					
					if (result.isSuccess()) {
						
						EnumDeliveryStatus dlvStatus = serviceResult.getServiceStatus(this.serviceType);
							
						if (EnumDeliveryStatus.DELIVER.equals(dlvStatus)) {
							this.createUser(this.serviceType, serviceResult.getAvailableServices());
						} else { 
							this.createUser(EnumServiceType.PICKUP, serviceResult.getAvailableServices());
						}
						
						String altDeliveryPage = "/site_access/alt_dlv_home.jsp?referrer_page=slite&serviceType=" + this.serviceType;
						String failedCorpPage = "/survey/cos_site_access_survey.jsp?successPage=index.jsp&referrer_page=slite";
						String failedHomePage = "/site_access/delivery.jsp?successPage=index.jsp&referrer_page=slite&serviceType=" + this.serviceType + "&email="+ NVL.apply(request.getParameter(EnumUserInfoName.EMAIL.getCode()), "").trim();
						String moreIngoPage = "/site_access/site_access_address_lite.jsp?successPage=index.jsp&serviceType=" + this.serviceType;
						
						
						if(EnumServiceType.CORPORATE.equals(this.serviceType)) {
							//Corporate delivery
							if(EnumDeliveryStatus.DONOT_DELIVER.equals(dlvStatus)) {
								//Do not deliver to corporate, check if alternate home delivery is available
								if(EnumDeliveryStatus.DELIVER.equals(serviceResult.getServiceStatus(EnumServiceType.HOME))) {
									// show E No Corporate HOME delivarable Survey presented /site_access/alt_dlv_home.jsp
									doRedirect(altDeliveryPage);
								}
								else if(EnumDeliveryStatus.DONOT_DELIVER.equals(serviceResult.getServiceStatus(EnumServiceType.HOME))) {
									// forward to site_access/cos_site_access_survey.jsp
									doRedirect(failedCorpPage);
								}
								else if(EnumDeliveryStatus.PARTIALLY_DELIVER.equals(serviceResult.getServiceStatus(EnumServiceType.HOME))) {
									// forward to more address page									
									doRedirect(moreIngoPage);
								}							
							} else {
								//Delivery to corporate
								doRedirect(moreIngoPage);
								
							}
						} else {
							//Home delivery
							
							
							
							if(EnumDeliveryStatus.RARELY_DELIVER.equals(dlvStatus) || EnumDeliveryStatus.DONOT_DELIVER.equals(dlvStatus)) {
								// forward to /site_access/delivery.jsp with rarely deliver message( not required now)
								return doRedirect(failedHomePage);
							} else if(EnumDeliveryStatus.PARTIALLY_DELIVER.equals(dlvStatus)) {
								//Partial delivery is available. Prompt for full address
								session.setAttribute("morepage", moreIngoPage);								
								doRedirect(moreIngoPage);
							} else if (EnumDeliveryStatus.DELIVER.equals(dlvStatus)) {
								//All set. The zipcode is good. Proceed to direct registration. No more info needed.
								doRegistration(result);
							}
							
							
							
						}
					}
				} else if ("expressSignup".equalsIgnoreCase(action)) { 
					
					
					/*
					 * 'expressSignup' is to create account using email/password.
					 * 
					 *  It uses the similar logic as 'signupSocialDlvAddr', 
					 *  which takes delivery info as required part of registration 
					 */					
					
					
					HttpSession session = this.pageContext.getSession();
					//HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
					HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
					HttpContext ctx = new HttpContext( session, request, response);
					
					
					/*
					 *  For express registration, 
					 *  	default "user type" to 'Home_USER'
					 *  	default "delivery status" to 'DONOT_DELIVER'
					 *  	default "available services" to 'PICKUP'
					 */
					int regType = AccountUtil.HOME_USER;
					if(EnumServiceType.CORPORATE.getName().equals(this.serviceType)) {
						regType = AccountUtil.CORP_USER;
					}					
					EnumDeliveryStatus dlvStatus = EnumDeliveryStatus.DONOT_DELIVER;   
					//Set<EnumServiceType> availableServices = Collections.<EnumServiceType>emptySet();
					Set<EnumServiceType> availableServices = new HashSet<EnumServiceType>();
					availableServices.add(EnumServiceType.PICKUP);
					
					
					// Set RegistrationAction which will do the major work										
					RegistrationAction ra = new RegistrationAction(regType);
					ra.setHttpContext(ctx);
					ra.setResult(result);
					
					
					// Delegate to RegistrationAction to Validate Email
					ra.validateSocialSignupEmail();

					
                    if (result.isSuccess()) {

                        // set default address for express registration user
                        if (this.address == null) {
                            address = new AddressModel();
                            /*
                             * address.setAddress1("23-30 borden ave"); address.setCity("Long Island City"); address.setState("NY"); address.setCountry("US");
                             */
                            address.setZipCode(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()));
                        }
                        this.serviceType = EnumServiceType.getEnum(request.getParameter("serviceType"));

                        // Create a user for express registration user
                        if (EnumDeliveryStatus.DELIVER.equals(dlvStatus)) {
                            this.createUser(this.serviceType, availableServices);
                        } else {
                            this.createUser(((this.serviceType != null) ? this.serviceType : EnumServiceType.PICKUP), availableServices);
                        }

                        // Delegate to RegistrationAction to register the new user
                        try {
                            String res = ra.executeEx();
                            if ((Action.SUCCESS).equals(res)) {
                                // "EXPRESS_REGISTRATION_COMPLETE" is used in 'signup_lite.jsp' to return control back to original workflow
                                session.setAttribute("EXPRESS_REGISTRATION_COMPLETE", "true");
                                session.setAttribute(SessionName.SIGNUP_SUCCESS, true);
                            } else if (Action.ERROR.equals(res)) {
                                session.setAttribute(SessionName.SIGNUP_SUCCESS, false);
                            }
                        } catch (Exception ex) {
                            LOGGER.error("Error performing action expresssignup", ex);
                            result.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
                            session.setAttribute(SessionName.SIGNUP_SUCCESS, false);
                        }

                    } else {
                        session.setAttribute(SessionName.SIGNUP_SUCCESS, false);
                    }

				}
				else if ("signupSocialDlvAddr".equalsIgnoreCase(action)) { 
					HttpSession session = this.pageContext.getSession();
					
					String companyname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_COMPANY_NAME.getCode()), "").trim();
					System.out.println("companyname:"+companyname);
					String firstname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode()), "").trim();
					System.out.println("firstname:"+firstname);
					String lastname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode()), "").trim();
					System.out.println("lastname:"+lastname);
					String streetaddr = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "").trim();
					System.out.println("streetaddr:"+streetaddr);
					String suite = NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "").trim();
					System.out.println("suite:"+suite);
					String zipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "").trim();
					System.out.println("zipcode:"+zipcode);
					String city = NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "").trim();
					System.out.println("city:"+city);
					String state = NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "").trim();
					System.out.println("state:"+state);
					String busphone = NVL.apply(request.getParameter(EnumUserInfoName.DLV_WORK_PHONE.getCode()), "").trim();
					System.out.println("busphone:"+busphone);
					String mobilephno = NVL.apply(request.getParameter(EnumUserInfoName.DLV_HOME_PHONE.getCode()), "").trim();
					System.out.println("mobilephno:"+mobilephno);
					String email = NVL.apply(request.getParameter(EnumUserInfoName.EMAIL.getCode()), "").trim();
					System.out.println("email:"+email);
					
					FDDeliveryServiceSelectionResult serviceResult = validateSocialDlvAddr(request, result);
										
					if(serviceResult!=null)
						setRequestedServiceTypeDlvStatus(serviceResult.getServiceStatus(this.serviceType));
					
					if (result.isSuccess()) {
						
						String failedAddresspage = "/social/FailedAddrPage.jsp?successPage=index.jsp&referrer_page=slite&" +
								"serviceType=" + this.serviceType +  
								"&companyname="+ companyname +
								"&firstname="+ firstname +
								"&lastname="+ lastname +
								"&streetaddr="+ streetaddr +
								"&suite="+ suite +
								"&zipcode="+ zipcode +
								"&city="+ city +
								"&state="+ state +
								"&busphone="+ busphone +
								"&mobilephno="+ mobilephno +
								"&email="+ email;
						
																		
						EnumDeliveryStatus dlvStatus = serviceResult.getServiceStatus(this.serviceType);

						if (EnumDeliveryStatus.DELIVER.equals(dlvStatus)) {
							this.createUser(this.serviceType, serviceResult.getAvailableServices());
						} else { 
							this.createUser(EnumServiceType.PICKUP, serviceResult.getAvailableServices());
						}						
													
						if (EnumDeliveryStatus.DELIVER.equals(dlvStatus)) {
							//All set. The zipcode is good. Proceed to direct registration. No more info needed.
							doRegistrationSocial(result);
						}
						else{
							// Show do not deliver page
							request.getSession().setAttribute("SocialDlvAddrFail", "true");
							doRedirect(failedAddresspage);
						}
					}
				}		
			} catch (FDResourceException re) {
				LOGGER.warn("FDResourceException occured", re);
				result.addError(true, "technicalDifficulty", SystemMessageList.MSG_TECHNICAL_ERROR);
			}
		}
		pageContext.setAttribute(resultName, result);
		return EVAL_BODY_BUFFERED;
	}
	
	private void setFDTcAccept(HttpServletRequest request, ActionResult result) {
		FDSessionUser user = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
		try {
			
			boolean success= FDCustomerManager.updateAck(user.getIdentity(),true, EnumEStoreId.valueOfContentId(ContentFactory.getInstance().getStoreKey().getId()).getContentId());
		} catch (FDResourceException e) {
			LOGGER.error("Error performing action setFDTcAccept", e);
			result.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
			
		}
		
	}

	private void doRegistration(ActionResult result) {
		int regType = AccountUtil.HOME_USER;
		if(EnumServiceType.CORPORATE.getName().equals(this.serviceType)) {
			//This is a corp user
			regType = AccountUtil.CORP_USER;
		}
		RegistrationAction ra = new RegistrationAction(regType);

		HttpContext ctx =
			new HttpContext(
				this.pageContext.getSession(),
				(HttpServletRequest) this.pageContext.getRequest(),
				(HttpServletResponse) this.pageContext.getResponse());

		ra.setHttpContext(ctx);
		ra.setResult(result);
		try {
			String res = ra.executeEx();
			if((Action.SUCCESS).equals(res)) {
				this.setSuccessPage("/social/signup_lite.jsp");										
				HttpSession session = pageContext.getSession();
				session.setAttribute("LITESIGNUP_COMPLETE", "true");
				session.removeAttribute("LITEACCOUNTINFO");
				session.removeAttribute("LITECONTACTINFO");

				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				if (user != null) {
					user.setJustSignedUp(true);
				}
				CmRegistrationTag.setPendingRegistrationEvent(session);
			}
		} catch (Exception ex) {
			LOGGER.error("Error performing action signupLite", ex);
			result.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
		}
	}
	
	private void doRegistrationSocial(ActionResult result) {
		int regType = AccountUtil.HOME_USER;
		if(EnumServiceType.CORPORATE.getName().equals(this.serviceType)) {
			//This is a corp user
			regType = AccountUtil.CORP_USER;
		}
		RegistrationAction ra = new RegistrationAction(regType);

		HttpContext ctx =
			new HttpContext(
				this.pageContext.getSession(),
				(HttpServletRequest) this.pageContext.getRequest(),
				(HttpServletResponse) this.pageContext.getResponse());

		ra.setHttpContext(ctx);
		ra.setResult(result);
		try {
			String res = ra.executeEx();
			if((Action.SUCCESS).equals(res)) {
				this.setSuccessPage("/social/DeliveryAddress.jsp");										
				HttpSession session = pageContext.getSession();
				System.out.println("Before>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				session.setAttribute("DELIVERYADDRESS_COMPLETE", "true");
				System.out.println("After>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				session.removeAttribute("SOCIALACCOUNTINFO");
				session.removeAttribute("SOCIALCONTACTINFO");
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				if (user != null) {
					user.setJustSignedUp(true);
				}
				CmRegistrationTag.setPendingRegistrationEvent(session);
			}
		} catch (Exception ex) {
			LOGGER.error("Error performing action socialsignup", ex);
			result.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
		}
	}
	
	private boolean validEmail(String email, ActionResult result) {		
		
		String err_msg = "Please make sure your email address is in the format you@isp.com";//SystemMessageList.MSG_EMAIL_FORMAT;
		//err_msg.replaceAll("\"", "\\\"");
		
		if(email == null || email.length() == 0) {
			result.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), err_msg));
			return false;
		}
		
		if(email != null)
			email = email.trim();
		
		if ((email != null) && (!"".equals(email)) && (!com.freshdirect.mail.EmailUtil.isValidEmailAddress(email))) {
			result.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), err_msg));
			return false;
		}
		
		//check if the email is already taken
		try {
			String dupeCustID = FDCustomerManager.dupeEmailAddress(email);
			if(dupeCustID != null) {
				if(FDReferralManager.getReferralDisplayFlag(dupeCustID)) {
					//Customer has atleast one settled order
					this.pageContext.getSession().setAttribute("MSG_FOR_LOGIN_PAGE", "You already have an account and are ineligible for this referral offer. Please log in to start shopping.");
					//store this customer as ineligible trial
					FDReferralManager.storeFailedAttempt(email,dupeCustID, this.address.getZipCode(),"","", (String) this.pageContext.getSession().getAttribute("REFERRALNAME"),"EXISTING CUSTOMER");
					return false;
				} else {
					//Customer is already registered, but no settled order
					if(FDReferralManager.isCustomerReferred(dupeCustID)) {
						//Customer has been referred by some other referral already
						this.pageContext.getSession().setAttribute("MSG_FOR_LOGIN_PAGE", "You already signed up for the new customer referral program. Please log in to start shopping.");
						return false;
					} else {
						//Customer is not referred yet. Just tick and tie and ask them to login.
						this.pageContext.getSession().setAttribute("TICK_TIE_CUSTOMER", dupeCustID + "|" + (String) this.pageContext.getSession().getAttribute("REFERRALNAME"));
						this.pageContext.getSession().setAttribute("MSG_FOR_LOGIN_PAGE", "You already signed up. Please log in to your account to use your Referral offer.");
						return false;
					}
				}
			}
		} catch (FDResourceException e) {
			LOGGER.error("Email check failed for:" + email, e);
		}
		
		pageContext.getSession().setAttribute("REFERRAL_EMAIL", email);

		return true;
	}

	private int doRedirect(String url) throws JspException {
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		try {
			response.sendRedirect(response.encodeRedirectURL(url));
			JspWriter writer = pageContext.getOut();
			writer.close();
			return SKIP_BODY;
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}

	private FDDeliveryServiceSelectionResult checkByZipCode(HttpServletRequest request, ActionResult result) throws FDResourceException {
		this.populate(request);
		this.validate(result, false);

		if (result.isFailure()) {
			//Reset successpage to null
			setSuccessPage(null);
			//Passing an indicator to the site access page that zip code validation failed.
			request.setAttribute("failed", "true");
			return null;
		}

		return FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(this.address.getZipCode(), StoreContextUtil.getStoreContext(request.getSession()).getEStoreId());
	}
	
	private FDDeliveryServiceSelectionResult checkSLiteZipCode(HttpServletRequest request, ActionResult result) throws FDResourceException {
		//populate address
		this.address = new AddressModel();
		String sType = request.getParameter("serviceType");
		this.address.setZipCode(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()),"").trim());
		this.serviceType = EnumServiceType.getEnum(NVL.apply(sType, "").trim());
		this.address.setAddress1(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "").trim());		
		this.address.setApartment(NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "").trim());
		this.address.setCity(NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "").trim());
		this.address.setState(NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "").trim());	
		
		this.validate(result, false);
		
		int regType = AccountUtil.HOME_USER;
		if(EnumServiceType.CORPORATE.getName().equals(NVL.apply(request.getParameter("serviceType"), ""))) {
			//This is a corp user
			regType = AccountUtil.CORP_USER;
		}
		RegistrationAction ra = new RegistrationAction(regType);

		HttpContext ctx =
			new HttpContext(
				this.pageContext.getSession(),
				(HttpServletRequest) this.pageContext.getRequest(),
				(HttpServletResponse) this.pageContext.getResponse());

		ra.setHttpContext(ctx);
		ra.setResult(result);
		ra.validateLiteSignup();

		if (result.isFailure()) {
			//Reset success page to null
			setSuccessPage(null);
			return null;
		}
		
		return FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(this.address.getZipCode(), StoreContextUtil.getStoreContext(pageContext.getSession()).getEStoreId());
	}
	
	private FDDeliveryServiceSelectionResult validateSocialDlvAddr(HttpServletRequest request, ActionResult result) throws FDResourceException {
		//populate address
		this.address = new AddressModel();
		String sType = request.getParameter("serviceType");
		this.address.setZipCode(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()),"").trim());
		this.serviceType = EnumServiceType.getEnum(NVL.apply(sType, "").trim());
		this.address.setAddress1(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "").trim());		
		this.address.setApartment(NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "").trim());
		this.address.setCity(NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "").trim());
		this.address.setState(NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "").trim());	
		
		this.validate(result, true);
		
		int regType = AccountUtil.HOME_USER;
		if(EnumServiceType.CORPORATE.getName().equals(NVL.apply(request.getParameter("serviceType"), ""))) {
			//This is a corp user
			regType = AccountUtil.CORP_USER;
		}
		RegistrationAction ra = new RegistrationAction(regType);

		HttpContext ctx =
			new HttpContext(
				this.pageContext.getSession(),
				(HttpServletRequest) this.pageContext.getRequest(),
				(HttpServletResponse) this.pageContext.getResponse());

		ra.setHttpContext(ctx);
		ra.setResult(result);
		ra.validateSocialSignupFirstLast();

		if (result.isFailure()) {
			//Reset success page to null
			setSuccessPage(null);
			return null;
		}		
		return FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(this.address.getZipCode());
	}

	/** keep in sync with LocationHandlerTag.doSetMoreInfoAction() */
	private void populate(HttpServletRequest request) {
		this.address = new AddressModel();
		String homeZipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()),"").trim();
		String corpZipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_CORP_ZIPCODE.getCode()),"").trim();
		
		String gcLanding = FDStoreProperties.getGiftCardLandingUrl();
		String rhLanding = FDStoreProperties.getRobinHoodLandingUrl();
		boolean isGiftCardEnabled = FDStoreProperties.isGiftCardEnabled();
		boolean isRobinHoodEnabled = FDStoreProperties.isRobinHoodEnabled();
		
		if(successPage != null && ((successPage.indexOf(gcLanding)>-1 && isGiftCardEnabled)||(successPage.indexOf(rhLanding)>-1 && isRobinHoodEnabled))){
			if(!"".equals(homeZipcode) && "".equals(corpZipcode)){
				this.address.setZipCode(homeZipcode);
			}else{
				this.address.setZipCode(corpZipcode);
			}
			this.serviceType = EnumServiceType.getEnum(NVL.apply("WEB", "").trim());
		}else{
			if(!"".equals(homeZipcode) && "".equals(corpZipcode)){
				this.address.setZipCode(homeZipcode);
				this.serviceType = EnumServiceType.getEnum(NVL.apply(request.getParameter("serviceType"), "").trim());
			}else{
				this.address.setZipCode(corpZipcode);
				this.serviceType = EnumServiceType.getEnum(NVL.apply(request.getParameter("corpServiceType"), "").trim());
			}
		}
		//this.serviceType = EnumServiceType.getEnum(NVL.apply(request.getParameter("serviceType"), "").trim());
		this.address.setAddress1(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "").trim());		
		this.address.setApartment(NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "").trim());
		this.address.setCity(NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "").trim());
		this.address.setState(NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "").trim());				
	}

	private void validate(ActionResult result, boolean validateAddress) throws FDResourceException {
		validate(result, validateAddress, true);
	}
	
	/** keep in sync with LocationHandlerTag.doSetMoreInfoAction() */
	private void validate(ActionResult result, boolean validateAddress, boolean useApt) throws FDResourceException {
		String zipCode = this.address.getZipCode();
		if ("".equals(zipCode)) {
			result.addError(true, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_REQUIRED);
		} else {
			boolean isNumber = true;
			try {
				Integer.parseInt(zipCode);
			} catch (NumberFormatException ne) {
				isNumber = false;
			}
			if (zipCode.length() != 5 || !isNumber || zipCode.equals("00000")) {
				result.addError(true, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_ZIP_CODE);
			}
		}
		if (validateAddress) {
			if ("".equals(this.address.getAddress1())) {
				result.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_REQUIRED);
			}
			if (address.getCity()==null || address.getCity().trim().length()==0) {
				result.addError(true, EnumUserInfoName.DLV_CITY.getCode(), SystemMessageList.MSG_REQUIRED);
			}
			if (address.getState()==null || address.getState().trim().length()==0) {
				result.addError(true, EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_REQUIRED);
			}else if (address.getState().length() < 2) {
				result.addError(new ActionError(EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_REQUIRED));
			} else {
				result.addError(!AddressUtil.validateState(address.getState()), EnumUserInfoName.DLV_STATE.getCode(),
					SystemMessageList.MSG_UNRECOGNIZE_STATE);
			}
			
			try{			
				if (result.isSuccess()) {
					this.address = AddressUtil.scrubAddress(this.address, useApt, result);
				}
			} catch(Exception ee){
				//result.addError(new ActionError(EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_UNRECOGNIZE_ADDRESS));
			}
		}
		
	}

	private int checkByAddress(HttpServletRequest request, ActionResult result) throws FDResourceException, JspException {
		return checkByAddress(request, result, true); // originally always used adress, now does not.
	}
	
	/** keep in sync with LocationHandlerTag.doSetMoreInfoAction() */
	private int checkByAddress(HttpServletRequest request, ActionResult result, boolean useApt) throws FDResourceException, JspException {
		this.populate(request);
		this.validate(result, true, useApt);
		
		if("true".equals(request.getParameter("LITESIGNUP")) && this.serviceType.getName().equals(EnumServiceType.CORPORATE.getName())) {
			String company = NVL.apply(request.getParameter(EnumUserInfoName.DLV_COMPANY_NAME.getCode()), "").trim();
			result.addError("".equals(company), EnumUserInfoName.DLV_COMPANY_NAME.getCode(), SystemMessageList.MSG_REQUIRED);
			String workPhone = NVL.apply(request.getParameter("busphone"), "").trim();
			result.addError("".equals(workPhone), EnumUserInfoName.DLV_WORK_PHONE.getCode(), SystemMessageList.MSG_REQUIRED);
			if(workPhone != null && !"".equals(workPhone) && workPhone.length() > 0 && workPhone.length() < 10){
				result.addError(new ActionError(EnumUserInfoName.DLV_WORK_PHONE.getCode(), SystemMessageList.MSG_NUM_REQ ));
			}			
		}
		
		if (result.isFailure()) {			
			return EVAL_BODY_BUFFERED;
		}
		try {

			EnumServiceType altServiceType = EnumServiceType.HOME.equals(this.serviceType) ? EnumServiceType.CORPORATE : EnumServiceType.HOME;
			
			FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByAddress(this.address);
			//request.setAttribute(REQUESTED_SERVICE_TYPE_DLV_STATUS, serviceResult.getServiceStatus(this.serviceType));
			setRequestedServiceTypeDlvStatus(serviceResult.getServiceStatus(this.serviceType));
			this.setAvailableServiceTypes(serviceResult.getAvailableServices());
						
			
			EnumDeliveryStatus reqStatus = serviceResult.getServiceStatus(this.serviceType);
			EnumDeliveryStatus altStatus = serviceResult.getServiceStatus(altServiceType);
			
			boolean reqDeliverable = EnumDeliveryStatus.DELIVER.equals(reqStatus) || EnumDeliveryStatus.COS_ENABLED.equals(reqStatus);
			boolean altDeliverable = EnumDeliveryStatus.DELIVER.equals(altStatus);
			
			boolean validServiceType = !(EnumServiceType.HOME.equals(this.serviceType) && EnumAddressType.FIRM.equals(this.address
				.getAddressType()));

			int serviceAvailability = getServiceAvailability(validServiceType, reqDeliverable, altDeliverable);

			EnumServiceType st = serviceAvailability==AV_REQUESTED ? this.serviceType : (serviceAvailability==AV_ALTERNATE ? altServiceType : EnumServiceType.PICKUP);
			this.createUser(st, serviceResult.getAvailableServices());
    	       		
			String page = getRedirectPage(this.serviceType, serviceAvailability);
			
			boolean isReferralRegistration = "true".equals(request.getParameter("referralRegistration"))?true:false;
			if(isReferralRegistration) {
				if(page.equals(this.successPage)) {
				//	this.moreInfoPage = "/registration/referee_signup2.jsp";
					this.moreInfoPage = "/registration/invite_signup2.jsp";
					//we need to offer user to signup for referral registration
					if (this.moreInfoPage != null && this.moreInfoPage.indexOf('?') < 0) {
						this.moreInfoPage += "?serviceType=" + this.serviceType.getName();
					} else {
						this.moreInfoPage += "&serviceType=" + this.serviceType.getName();
					}
					//Add address into session.
					pageContext.getSession().setAttribute("REFERRAL_ADDRESS", this.address);
					//return this.doRedirect(moreInfoPage);
					pageContext.getSession().setAttribute("DISPLAY", "STEP2");
					return EVAL_BODY_BUFFERED;
				}
			}
			
			if("true".equals(request.getParameter("LITESIGNUP"))) {
				if(page.equals(successPage)) {
					//everything is good, proceed with registration.
					doRegistration(result);	
					return EVAL_BODY_BUFFERED;
				} else {
					String altDeliveryPage = "/site_access/alt_dlv_home.jsp";
					if(page.equals(altDeliveryCorporatePage)) {
						page = "/site_access/alt_dlv_corporate.jsp?referrer_page=slite";
					} else if (page.equals(altDeliveryHomePage)) {
						page = altDeliveryPage + "?referrer_page=slite";
					} else {
						page = page + "?serviceType=" + this.serviceType + "&referrer_page=slite";
					} 
				}
			}
			return this.doRedirect(page);

		} catch (FDInvalidAddressException ae) {
			result.addError(true, EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), 
					MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE_ZIP_CHECK,new Object[] {UserUtil.getCustomerServiceContact(request)}));
		}
		return EVAL_BODY_BUFFERED;
	}

	private final static int AV_REQUESTED = 0;
	private final static int AV_ALTERNATE = 1;
	private final static int AV_UNAVAIL = 2;

	protected int getServiceAvailability(boolean validServiceType, boolean reqDeliverable, boolean altDeliverable) {
		if (validServiceType && reqDeliverable) {
			return AV_REQUESTED;
		} else if (altDeliverable) {
			return AV_ALTERNATE;
		} else {
			return AV_UNAVAIL;
		}
	}

	protected String getRedirectPage(EnumServiceType reqServiceType, int result) {
		switch (result) {
			case AV_REQUESTED:
				return successPage;
			case AV_ALTERNATE:
				if (EnumServiceType.HOME.equals(reqServiceType)) {
					return this.altDeliveryCorporatePage;
				}
				return this.altDeliveryHomePage;
			default:
				if (EnumServiceType.CORPORATE.equals(reqServiceType)) {
					return this.failureCorporatePage;
				}
				return this.failureHomePage;
		}
	}
	
	/** keep in sync with LocationHandlerTag.doFutureZoneNotificationAction() */
	private void saveEmail(HttpServletRequest request, ActionResult result) throws FDResourceException {
		HttpSession session = pageContext.getSession();
		String email = request.getParameter("email");
		String userEmail = request.getParameter("userEmail");
		
		if ((email == null && userEmail != null) || !email.equalsIgnoreCase(userEmail)) {
			/* set email to user-entered email rather than SUL-used email */
			email = userEmail;
		}
		
		if (email != null)
			email = email.trim();
		if ((email != null) && (!"".equals(email)) && (!com.freshdirect.mail.EmailUtil.isValidEmailAddress(email))) {
			result.addError(true, "email", SystemMessageList.MSG_EMAIL_FORMAT);
			return;
		}

		if (!result.isSuccess()) {
			LOGGER.debug("THERE IS AN ERROR!!");
			return;
		}

		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		String zip = user.getZipCode();
		if ((email != null) && (!"".equals(email))) {					
			FDDeliveryManager.getInstance().saveFutureZoneNotification(email, user.getZipCode(),this.serviceType);
			LOGGER.debug("SAVED FUTURE ZONE TO NOTIFY");
		}
	}
	
	private void doPrereg(HttpServletRequest request, ActionResult result) throws FDResourceException {
		HttpSession session = pageContext.getSession();
		String email = request.getParameter("email");
		String userEnteredZipcode = request.getParameter("zipcode");
	    String zipCodePattern = "\\d{5}";
		

		if (email != null)
			email = email.trim();
		if ((email != null) && (!"".equals(email)) && (!com.freshdirect.mail.EmailUtil.isValidEmailAddress(email))) {
			result.addError(true, "email", SystemMessageList.MSG_EMAIL_FORMAT);
			return;
		}

		if (userEnteredZipcode != null)
			userEnteredZipcode = userEnteredZipcode.trim();
		if ((userEnteredZipcode != null) && (!"".equals(userEnteredZipcode)) && (!userEnteredZipcode.matches(zipCodePattern))) {
			result.addError(true, "zipcode", SystemMessageList.MSG_ZIP_CODE);
			return;
		}
		if (!result.isSuccess()) {
			LOGGER.debug("THERE IS AN ERROR!!");
			return;
		}

		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		if ((email != null) && (!"".equals(email))) {
		    setServiceType("PREREG");					
			FDDeliveryManager.getInstance().saveFutureZoneNotification(email, userEnteredZipcode, this.serviceType);
			LOGGER.debug("SAVED PREREG TO NOTIFY");
		}
	}

    private void createUser(EnumServiceType serviceType, Set<EnumServiceType> availableServices) throws FDResourceException {
        HttpSession session = pageContext.getSession();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        FDSessionUser user = UserUtil.createSessionUser(serviceType, availableServices, session, response, address);

        if (this.address != null && user.getAddress() != null && "".equalsIgnoreCase(this.address.getState()) && this.address.getZipCode().equals(user.getAddress().getZipCode())) {
            this.address.setState(user.getAddress().getState());
        }
        user.setAddress(this.address);

        if (user != null) {
            user.setNewUserWelcomePageShown(true); // do not redirect to welcome.jsp
        }
    }

}
