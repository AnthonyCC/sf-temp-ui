package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;
import org.json.JSONObject;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumComplaintLineMethod;
import com.freshdirect.customer.EnumComplaintLineType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumComplaintType;
import com.freshdirect.customer.EnumSendCreditEmail;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpClientCode;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeVariant;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDTransientCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.QuickCart;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.CclUtils;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.FDStandingOrder.ErrorCode;
import com.freshdirect.fdstore.util.IgnoreCaseString;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.ActionWarning;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.callcenter.ComplaintUtil;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.util.FDEventUtil;
import com.freshdirect.webapp.util.ItemSelectionCheckResult;
import com.freshdirect.webapp.util.QuickCartCache;
import com.freshdirect.webapp.util.RequestUtil;


public class FDShoppingCartControllerTag extends BodyTagSupport implements SessionName {


	private static final long serialVersionUID = -7350790143456750035L;

	private static Category LOGGER = LoggerFactory.getInstance(FDShoppingCartControllerTag.class);

	private String id;

	private String action;

	private String successPage;

	private String multiSuccessPage;

	private String resultName;

	private String source;

	private boolean cleanupCart = false;

	private List<Integer> removeIds;

	private HttpServletRequest request;

	private ActionResult result;

	private FDCartModel cart;

	/**
	 * Cartlines already processed, BUT not yet added to the cart.
	 */
	private List<FDCartLineI> cartLinesToAdd = Collections.emptyList();

	private List<String> frmSkuIds = new ArrayList<String>();

	private final String GENERAL_ERR_MSG = "The marked lines have invalid or missing data.";

	// OrderLine credit inputs
	private String[] orderLineId = null;
	private String[] orderLineReason = null;

	// Credit notes
	String description = "Make good 0 credit complaint";

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public void setAction(String a) {
		this.action = a;
	}

	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Return the source of the event, that is, the part of the site the
	 * shopping cart action was made on. If not known, default to "Browse".
	 * 
	 * @return the source of the event.
	 * @see EnumEventSource#BROWSE
	 */
	public EnumEventSource getEventSource() {
		if (source == null) {
			return EnumEventSource.BROWSE;
		}

		EnumEventSource enumSource = EnumEventSource.getEnum(source);
		return enumSource == null ? EnumEventSource.BROWSE : enumSource;
	}

	public void setSuccessPage(String sp) {
		this.successPage = sp;
	}

	public void setMultiSuccessPage(String msp) {
		this.multiSuccessPage = msp;
	}

	public void setResult(String resultName) {
		this.resultName = resultName;
	}

	public void setCleanupCart(boolean cleanup) {
		this.cleanupCart = cleanup;
	}

	// all CCL requests shall start with the "CCL:" prefix
	private boolean isCCLRequest() {
		// see if source was set as CCL
		if (source != null && source.equalsIgnoreCase("CCL"))
			return true;
		// else see if the action had a CCL tag
		return action != null && action.startsWith("CCL:");
	}

	private boolean isAddToCartRequest() {
		return "addMultipleToCart".equals(action) || "addToCart".equals(action);
	}


	@Override
	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();

		FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
		//user.updateUserState();
		this.cart = user.getShoppingCart();
		if (cart == null) {
			// user doesn't have a cart, this is a bug, as login or site_access
			// should put it there
			throw new JspException("No shopping cart found");
		}

		//
		// perform any actions requested by the user if the request was a POST
		//
		this.request = (HttpServletRequest) pageContext.getRequest();
		this.result = new ActionResult();
		int affectedLines = 0;
		String application = (String) session.getAttribute(SessionName.APPLICATION);
		boolean inCallCenter = "callcenter".equalsIgnoreCase(application);

		ErpComplaintModel complaintModel = new ErpComplaintModel();

		boolean hasPending = false;
		boolean useMergePendingOverlay = false;

		//don't do this in CRM
		if (!inCallCenter && action != null && ("POST".equalsIgnoreCase(request.getMethod()))) {

			//check if user should see overlay (for only specific actions), not in CRM
			if (("addToCart".equalsIgnoreCase(action) || "addMultipleToCart".equalsIgnoreCase(action)) 
					&& (!inCallCenter && this.getEventSource() != EnumEventSource.getEnum("SS"))) {
				if (user.isShowPendingOrderOverlay()) {
					//check if user has a pending order
					try {
						hasPending = user.hasPendingOrder();
					} catch (FDResourceException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (hasPending) {
						useMergePendingOverlay = true;
						
						//create a new temp cart for use in the overlay
						FDCartModel tempMergePendCart = new FDCartModel();
						//set pricing context in that cart
						tempMergePendCart.setPricingContextToOrderLines(user.getPricingContext());
						
						//now, set the cart for this to the temp cart so items get added there
						this.cart = tempMergePendCart;
						//put temp cart in session (over writing the cart in use can cause problems)
						session.setAttribute("tempMergePendCart", tempMergePendCart);
						
						//set the other attributes from the original post so we can use them in the overlay
						session.setAttribute("tempMergeSource", this.source);
						session.setAttribute("tempMergeAction", this.action);
						session.setAttribute("tempMergeMultiSuccessPage", this.multiSuccessPage);
						session.setAttribute("tempMergeSuccessPage", this.successPage);
						
					}
				}
			}
			
			//
			// an action was request, decide which one
			//
			if ("pendOrderMerge".equalsIgnoreCase(action)) {
				//parse out user changes
				LOGGER.debug("submit for pending order merging");

				//set redir to modify
				String pendOrdId = request.getParameter("pendOrderId");
				String successPage = request.getParameter("successPage");
				if (successPage == null)
					successPage = this.successPage; //get from tag if not sent in request
				
				successPage += "?orderId="+pendOrdId+"&action=modify";
				LOGGER.debug("submit for pending order merging - redir set to "+successPage);

				/*
				 * "merge" carts (by adding new lines to a new temp cart)
				 * we can't depend on the ids to remove items
				 */
					FDCartModel cartUser = user.getShoppingCart();
					FDCartModel tempMergePendCart = (FDCartModel) session.getAttribute("tempMergePendCart");

					/* check for null here */
					if (tempMergePendCart == null) {
						tempMergePendCart = new FDCartModel();
					}
					
					//remove unchecked items before merging
						//create two lists to hold cartLine ids we're KEEPING
						List<String> tempItemsToKeep = new ArrayList<String>();
						List<String> userItemsToKeep = new ArrayList<String>();
						
						for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
							String n = e.nextElement();
							if (n.startsWith("addCLID_")) {
								tempItemsToKeep.add( n.substring("addCLID_".length()) );
							}
							if (n.startsWith("userCLID_")) {
								userItemsToKeep.add( n.substring("userCLID_".length()) );
							}
						}
						
						//now loop over carts and save cartlines to keep
							List<FDCartLineI> cartlinesToKeep = new ArrayList<FDCartLineI>();
								
							for (int i = 0; i < tempMergePendCart.numberOfOrderLines(); i++) {
								FDCartLineI cartLine = tempMergePendCart.getOrderLine(i);
								if ( tempItemsToKeep.contains(cartLine.getCartlineId()) ) {
									cartlinesToKeep.add(cartLine);
								}
							}
							//clear temp cart and add back in cartlines
							tempMergePendCart.clearOrderLines();
							tempMergePendCart.addOrderLines(cartlinesToKeep);
							
							//clear temp list
							cartlinesToKeep.clear();
							
							//now do user cart
							for (int i = 0; i < cartUser.numberOfOrderLines(); i++) {
								FDCartLineI cartLine = cartUser.getOrderLine(i);
								if ( userItemsToKeep.contains(cartLine.getCartlineId()) ) {
									cartlinesToKeep.add(cartLine);
								}
							}
							//clear temp cart and add back in cartlines
							cartUser.clearOrderLines();
							cartUser.addOrderLines(cartlinesToKeep);
							
							//merge temp and user cart into one (so recentOrderLines get set properly next)
							tempMergePendCart.mergeCart(cartUser);

					FDCartModel cartMerged = new FDCartModel(); //start a new empty cart

					//be sure to merge tempCart TO new merged cart, otherwise recentOrderLines won't be updated properly
					cartMerged.mergeCart( tempMergePendCart );
					cartMerged.sortOrderLines();

					//set pricing context (does this need to be done for both carts BEFORE merge?)
					cartMerged.setPricingContextToOrderLines(user.getPricingContext());

				
				//save cart in to session (as temp)
				session.setAttribute("tempMergePendCart", cartMerged);
				LOGGER.debug("submit for pending order merging - merged carts");

				//remove previous session attributes that we don't need anymore
				session.removeAttribute("tempMergeSource");
				session.removeAttribute("tempMergeAction");
				session.removeAttribute("tempMergeMultiSuccessPage");
				session.removeAttribute("tempMergeSuccessPage");
				
				//edit modify controller? to check for session temp cart and add items to cart

				//do redirecting
				HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
				String redirectURL = response.encodeRedirectURL(successPage);
								
				try {
					response.sendRedirect(redirectURL);
				} catch (IOException ioe) {
					throw new JspException("Error redirecting: "+ ioe.getMessage());
				}

				return SKIP_BODY;
			} else if ("pendOrderMergeChoice".equalsIgnoreCase(action)) {
				//parse out user changes
				LOGGER.debug("submit for pending order merging CHOICE");
				
				//get current cart
				FDCartModel cartSaved = this.cart;
				//get temp cart from overlay (session)
				FDCartModel tempMergePendCart = (FDCartModel) session.getAttribute("tempMergePendCart");
				
				/* check for null here */
				if (tempMergePendCart == null) {
					tempMergePendCart = new FDCartModel();
				}
				//set pricing context
				tempMergePendCart.setPricingContextToOrderLines(user.getPricingContext());
				//merge in cart(s)
				FDCartModel cartMerged = new FDCartModel( cartSaved );
				//be sure to merge tempCart TO saved cart, otherwise recentOrderLines won't be updated properly
				cartMerged.mergeCart( tempMergePendCart );
				cartMerged.sortOrderLines();
				
				user.setShoppingCart( cartMerged );
				
				//set current cart to merged version
				this.cart = cartMerged;
				
				//set affected lines to reflect merge
				affectedLines = tempMergePendCart.numberOfOrderLines();
				
				//reset added skus
				if (session.getAttribute("SkusAdded") != null)
					frmSkuIds.addAll((List<String>)session.getAttribute("SkusAdded"));
				
				//remove previous session attributes
				session.removeAttribute("tempMergePendCart");
				session.removeAttribute("tempMergeSource");
				session.removeAttribute("tempMergeAction");
				session.removeAttribute("tempMergeMultiSuccessPage");
				session.removeAttribute("tempMergeSuccessPage");
				
				//turn off overlay for user
				user.setShowPendingOrderOverlay(false);

				LOGGER.debug("submit for pending order merging CHOICE - merged carts");
				
			} else if ("CCL:AddToList".equalsIgnoreCase(action)) {
				// find suffix from request, strict check, do not use product
				// minimum, do not skip zeros
				ItemSelectionCheckResult checkResult = new ItemSelectionCheckResult();
				checkResult.setResponseType(ItemSelectionCheckResult.SAVE_SELECTION);
				checkResult.setSelection(getProductSelection(null, true, false,false));
				checkResult.setErrors(result.getErrors());
				checkResult.setWarnings(result.getWarnings());
				request.setAttribute("check_result", checkResult);
				pageContext.setAttribute(resultName, result);
				return EVAL_BODY_BUFFERED;
			} else if ("CCL:ItemManipulate".equalsIgnoreCase(action)) {
				String ccListId = request.getParameter(CclUtils.CC_LIST_ID);
				String lineId = request.getParameter("lineId");
				String listAction = request.getParameter("list_action");

				String qcType = request.getParameter("qcType");
				if (qcType == null || "".equals(qcType))
					qcType = QuickCart.PRODUCT_TYPE_CCL;

				if (!(qcType.equals(QuickCart.PRODUCT_TYPE_CCL) || qcType.equals(QuickCart.PRODUCT_TYPE_SO)))
					throw new JspException("Invalid list type " + qcType);
				
				boolean worked = false;
				try {
					if ("modify".equalsIgnoreCase(listAction)) {

						// This will work for both "multi" and single items
						// In the case of multi items, skuCode_x is expected for
						// each item
						// otherwise a single skuCode
						// and of course corresponding quantities

						// the list is only stored if actual modifications take
						// place
						FDCustomerList cclist = FDListManager.getCustomerListById(
								user.getIdentity(),
								QuickCart.PRODUCT_TYPE_SO.equals(qcType) ? EnumCustomerListType.SO : EnumCustomerListType.CC_LIST,
								ccListId);
						List<FDCustomerListItem> items = cclist.getLineItems();
						worked = true;

						for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
							String paramName = e.nextElement();
							if (paramName.startsWith("skuCode")) {
								String suffix = paramName.substring("skuCode".length());

								FDCartLineI cartI = this.processCartLine(suffix, null, true, false);

								if (cartI != null) {
									boolean found = false;
									for (Iterator<FDCustomerListItem> I = items.iterator(); I.hasNext();) {
										FDCustomerProductListLineItem item = (FDCustomerProductListLineItem) I.next();
										if (item.getId().equals(lineId)) {
											found = true;
											I.remove();
											FDCustomerProductListLineItem li = new FDCustomerProductListLineItem(cartI.getSkuCode(),
													new FDConfiguration(cartI.getConfiguration()), cartI.getRecipeSourceId());
											li.setFrequency(1);

											cclist.addLineItem(li);
											FDListManager.storeCustomerList(cclist);

											// invalidate caches
											QuickCartCache.invalidateOnChange(session, qcType, ccListId, null);
											break;
										}
									}

									if (!found)
										LOGGER.warn("Item with id " + lineId + " is not on list");
								} else
									worked = false;
							} // item
						} // items
					} else if ("remove".equalsIgnoreCase(listAction)) {
						FDListManager.removeCustomerListItem(user, new PrimaryKey(lineId));
						QuickCartCache.invalidateOnChange(session, qcType, ccListId, null);
						user.invalidateCache();

						worked = true;
					} else {
						LOGGER.warn("list_action = " + listAction + " (remove or modify accepted)");
						throw new JspException( "Incorrect request parameter: list_action= " + listAction);
					}

					if (worked) {
						
						if ( QuickCart.PRODUCT_TYPE_SO.equals( qcType ) ) {
							FDStandingOrder so = (FDStandingOrder)session.getAttribute( "__actual_so" );
							
							if ( so != null && so.getLastError() == ErrorCode.MINORDER ) {
								
								FDCustomerList customerList = FDListManager.getCustomerListById(user.getIdentity(),EnumCustomerListType.SO,ccListId);							
								FDCartModel temporaryCart = new FDTransientCartModel();							
								List<FDProductSelectionI> productSelectionList = OrderLineUtil.getValidProductSelectionsFromCCLItems( customerList.getLineItems() );
								try {
									ErpAddressModel address = so.getDeliveryAddress();
									temporaryCart.setDeliveryAddress( address );
									temporaryCart.setZoneInfo( FDDeliveryManager.getInstance().getZoneInfo( address, so.getNextDeliveryDate() ) );
								
									for ( FDProductSelectionI ps : productSelectionList ) {
										FDCartLineI cartLine = new FDCartLineModel( ps );
										if ( !cartLine.isInvalidConfig() ) {
											temporaryCart.addOrderLine( cartLine );
										}
									}
									temporaryCart.refreshAll(true);			
								} catch ( FDInvalidConfigurationException e ) {
								} catch ( FDInvalidAddressException e ){									
								}
								
								if ( temporaryCart.getSubTotal() >= user.getMinimumOrderAmount() ) {
									try {
										so.clearLastError();
										FDActionInfo info = AccountActivityUtil.getActionInfo(pageContext.getSession());
										FDStandingOrdersManager.getInstance().save( info, so );
									} catch ( FDResourceException e ) {
										LOGGER.warn( "Could not save standing order.", e );
									}
								}
							}
						}

						HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
						String redirectURL = response.encodeRedirectURL(successPage);
						if (redirectURL == null)
							redirectURL = request.getParameter("successPage");
						if (redirectURL == null)
							return SKIP_BODY;
						LOGGER.debug("List modified, redirecting to " + redirectURL);
						response.sendRedirect(redirectURL);

						return SKIP_BODY;
					}
				} catch (IOException ioe) {
					throw new JspException("Error redirecting: "+ ioe.getMessage());
				} catch (FDResourceException re) {
					throw new JspException("Error in accessing resource: "+ re.getMessage());
				}
			} else if ("CCL:AddMultipleToList".equalsIgnoreCase(action)) {

				FDCustomerCreatedList selection = null;

				String source = request.getParameter("source");
				if (source == null)
					source = "";

				if ("ccl_actual_selection".equalsIgnoreCase(source)) {
					// deduce suffices from request, non-strict check, do not
					// use product min, skip zeros
					selection = getProductSelection(null, false, false, true);
				} else if (source.startsWith("ccl_sidebar")) {
					// use the selected ccl_sidebar's suffix, non-strict check,
					// use product minimum, do not skip zeros
					selection = getProductSelection(source.substring("ccl_sidebar".length()), false, true, false);
				} else {
					LOGGER.warn("Invalid source: " + source);
					throw new JspException("Invalid source: " + source);
				}
				ItemSelectionCheckResult checkResult = new ItemSelectionCheckResult();
				checkResult.setResponseType(ItemSelectionCheckResult.SAVE_SELECTION);
				checkResult.setSelection(selection);
				checkResult.setErrors(result.getErrors());
				checkResult.setWarnings(result.getWarnings());
				request.setAttribute("check_result", checkResult);
				pageContext.setAttribute(resultName, result);

				return EVAL_BODY_BUFFERED;

			} else if ("CCL:copyToList".equalsIgnoreCase(action)) {
				session.removeAttribute("SkusAdded");
				String ccListId = request.getParameter(CclUtils.CC_LIST_ID);
				String listName = null;
				try {
					listName = FDListManager.getListName(user.getIdentity(), ccListId);
					if (listName == null)
						throw new JspException("List with id " + ccListId + " not found");
				} catch (FDResourceException e) {
					e.printStackTrace();
					throw new JspException(e);
				}

				// deduce suffices from request, strict only if not multiple, do
				// not use minimum, skip zeros if multiple
				boolean multiple = "multiple".equalsIgnoreCase(request.getParameter("ccl_copy_type"));

				ItemSelectionCheckResult checkResult = new ItemSelectionCheckResult();
				FDCustomerCreatedList selection = getProductSelection(null,!multiple, false, multiple);
				checkResult.setSelection(selection);
				checkResult.setResponseType(ItemSelectionCheckResult.COPY_SELECTION);
				checkResult.setListName(listName);
				checkResult.setErrors(result.getErrors());
				checkResult.setWarnings(result.getWarnings());
				request.setAttribute("check_result", checkResult);

				pageContext.setAttribute(resultName, result);
				pageContext.setAttribute(resultName, result);
				return EVAL_BODY_BUFFERED;
			} else if ("addToCart".equalsIgnoreCase(action)) {
				affectedLines = addToCart() ? 1 : 0;
			} else if ("addMultipleToCart".equalsIgnoreCase(action)) {
				session.removeAttribute("SkusAdded");
				affectedLines = this.addMultipleToCart();
			} else if ("changeOrderLine".equalsIgnoreCase(action)) {
				affectedLines = changeOrderLine() ? 1 : 0;
			} else if ("updateQuantities".equalsIgnoreCase(action)) {
				affectedLines = updateQuantities() ? 1 : 0;
				if (!inCallCenter && successPage != null && successPage.indexOf("checkout/step") > -1)
					try {
						LOGGER.debug("  about to call calidate Order min");
						cart.refreshAll(true);
						UserValidationUtil.validateCartNotEmpty(request, result);
					} catch (FDResourceException ex) {
						throw new JspException(ex);
					} catch (FDException e) {
						LOGGER.warn("Error refreshing cart", e);
						throw new JspException(e.getMessage());
					}

			} else if ("removeAllCartLines".equalsIgnoreCase(action)) {
				affectedLines = this.removeAllCartLines();
			} else if ("nextPage".equalsIgnoreCase(action)) {
				successPage = "checkout_select_address.jsp";
				// clean data in session
				session.removeAttribute("makeGoodOrder");
				session.removeAttribute("referencedOrder");
				session.removeAttribute(SessionName.MAKEGOOD_COMPLAINT);

				if ("true".equals(request.getParameter("makegood"))) {
					this.getFormData(request, result);
					for (int i = 0; i < orderLineId.length; i++) {
						if (orderLineId[i] == null || "".equals(orderLineId[i].trim())) {
							result.addError(new ActionError(
								"system",
								"Order line ID from original order is missing, please go back clean all items in cart and select items from original order again."));
						}
					}
					for (int i = 0; i < orderLineReason.length; i++) {
						if (orderLineReason[i] == null || "".equals(orderLineReason[i].trim())) {
							result.addError(new ActionError("system", "Make good reason is missing"));
						}
					}

					try {
						buildComplaint(result, complaintModel);
					} catch (FDResourceException ex) {
						LOGGER.warn("FDResourceException while building ErpComplaintModel", ex);
						throw new JspException(ex.getMessage());
					}
					session.setAttribute(SessionName.MAKEGOOD_COMPLAINT, complaintModel);
					session.setAttribute("makeGoodOrder", request.getParameter("makeGoodOrder"));
					session.setAttribute("referencedOrder", request.getParameter("referencedOrder"));
				}
			} else {
				// unrecognized action, it's probably not for this tag then
				// return EVAL_BODY_BUFFERED;
				LOGGER.warn("Unrecognized action:" + action);
			}
		} else if ((request.getParameter("remove") != null) && "GET".equalsIgnoreCase(request.getMethod())) {
			if (request.getParameter("cartLine") == null) {
				// no specific orderline -> remove all
				affectedLines = this.removeAllCartLines();
			} else {
				// remove single line
				affectedLines = removeOrderLine() ? 1 : 0;
			}
			
			if (result.isSuccess() && successPage == null) {
				HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
				
				/*
				 * Construct the success page's URL, removing the delete action's query strings,
				 * and redirect to that URL.
				 */
				String succPage = request.getRequestURI() +"?"+ 
								  RequestUtil.getFilteredQueryString(request, new String[] {"remove", "cartLine"});
				try {
					response.sendRedirect(response.encodeRedirectURL(succPage));
				} catch (IOException e) {
					return SKIP_BODY;
				}
			}
		
		} else if ((request.getParameter("removeRecipe") != null) && "GET".equalsIgnoreCase(request.getMethod())) {
			affectedLines = this.removeRecipe();
		}
		
		
		// Handle delivery pass (if any) in the cart.
		cart.handleDeliveryPass();

		if (user.getSelectedServiceType() == EnumServiceType.HOME) {
			/*
			 * If home address perform delivery pass status check on the user
			 * object. Otherwise delivery pass doesn't apply.
			 */
			try {
				user.performDlvPassStatusCheck();
			} catch (FDResourceException ex) {
				LOGGER.warn("FDResourceException during user.performDlvPassStatusCheck()",ex);
				throw new JspException(ex);
			}
		} else {
			// If corporate or pickup do not apply the pass.
			// If corporate or pickup do not apply the pass.
			if (cart.isDlvPassApplied()) { // This if condition was added for
				// Bug fix MNT-12
				// If corporate or pickup do not apply the pass.
				cart.setDlvPassApplied(false);
				cart.setChargeWaived(EnumChargeType.DELIVERY, false, DlvPassConstants.PROMO_CODE);
			}
		}

		if (affectedLines > 0) {
			try {
				cart.refreshAll(true);
			} catch (FDException e) {
				LOGGER.warn("Error refreshing cart", e);
				throw new JspException(e.getMessage());
			}
			// This method retains all product keys that are in the cart in the
			// dcpd promo product info.
			user.getDCPDPromoProductCache().retainAll(cart.getProductKeysForLineItems());

			user.updateUserState();

			cart.sortOrderLines();
		}

		// Check for expired or cancelled passes if already used.
		checkForExpOrCanPasses(user);

		//
		// sort and save the cart in session
		// if anything in the cart was changed
		// don't save if useMergePendingOverlay
		//
		if (result.isSuccess() && affectedLines > 0) {
			//
			// save cart if it hasn't been saved in a while
			//
			if (!useMergePendingOverlay)
				user.saveCart();
			session.setAttribute(USER, user);
			session.setAttribute("SkusAdded", frmSkuIds);
		}

		//
		// redirect to success page if an action was successfully performed
		// and a success page was defined
		// don't redirect if useMergePendingOverlay
		//
		if ("POST".equalsIgnoreCase(request.getMethod()) && (action != null) && (successPage != null) && result.isSuccess() && !useMergePendingOverlay) {
			String redir = (affectedLines > 1 && this.multiSuccessPage != null) ? this.multiSuccessPage : successPage;
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
			try {
				response.sendRedirect(response.encodeRedirectURL(redir));
				return SKIP_BODY;
			} catch (IOException ioe) {
				// if there was a problem redirecting, well.. fuck it.. :)
				throw new JspException("Error redirecting " + ioe.getMessage());
			}
		}

		if (this.cleanupCart) {
			try {
				this.doCartCleanup();
			} catch (FDResourceException ex) {
				LOGGER.warn("FDResourceException during cleanup", ex);
				throw new JspException(ex);
			}
			// !!! refactor to doEndTag
			this.finishCartCleanup();
		}
		pageContext.setAttribute("cartCleanupRemovedSomeStuff", Boolean.valueOf(!(this.removeIds == null || this.removeIds.size() == 0)));

		//
		// place the cart as a scripting variable in the page
		//
		pageContext.setAttribute(id, cart);
		pageContext.setAttribute(resultName, result);
		
		// if showing overlay, add js to page to make it display, not in CRM
		if (!inCallCenter && useMergePendingOverlay) {
			JspWriter out = pageContext.getOut();
			try {
				//print out method for overlay display
				out.print("<script type=\"text/javascript\">globalDoRemoteOverlay('/ajax/merge_cart_penOrder_choice.jsp');</script>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return EVAL_BODY_BUFFERED;
	}

	private void checkForExpOrCanPasses(FDSessionUser user) {
		/*
		 * if(user.getDlvPassInfo() != null &&
		 * user.getDlvPassInfo().isUnlimited() && user.isDlvPassExpired() &&
		 * user.getShoppingCart().isDlvPassAlreadyApplied()){
		 * 
		 */// -Commented DP1.1
		/*
		 * This Condition happens only for unlimited pass. Let say user places
		 * order A using a unlimited pass that expires the same day. The next
		 * day the user modifies the order A resubmits the order. Since the
		 * applied pass got already expired, we remove the pass applied to the
		 * order afer notifying the user.
		 */
		/*
		 * StringBuffer buffer = new
		 * StringBuffer(SystemMessageList.MSG_1_UNLIMITED_PASS_EXPIRED);
		 * buffer.append(CCFormatter.defaultFormatDate(user.getDlvPassInfo().getExpDate()));
		 * buffer.append(SystemMessageList.MSG_2_UNLIMITED_PASS_EXPIRED);
		 * result.addWarning(new ActionWarning("pass_expired",
		 * buffer.toString())); }
		 */// -Commented DP1.1
		if (user.getDlvPassInfo() != null
				&& user.getDlvPassInfo().isUnlimited()
				&& user.isDlvPassCancelled()
				&& user.getShoppingCart().isDlvPassAlreadyApplied()) {
			/*
			 * This Condition happens only for unlimited pass. Let say user
			 * places order A using a unlimited pass that was cancelled the same
			 * day. The next day the user modifies the order A resubmits the
			 * order. Since the applied pass got already cancelled, we remove
			 * the pass applied to the order afer notifying the user.
			 */
			StringBuffer buffer = new StringBuffer(SystemMessageList.MSG_UNLIMITED_PASS_CANCELLED);
			result.addWarning(new ActionWarning("pass_cancelled", buffer.toString()));
		}
	}

	protected void doCartCleanup() throws FDResourceException {
		HttpSession session = this.pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(USER);
		FDCartModel cart = user.getShoppingCart();

		for (int i = 0; i < cart.numberOfOrderLines(); i++) {
			FDCartLineI cartLine = cart.getOrderLine(i);
			if (cartLine instanceof FDModifyCartLineI) {
				// skip it
				continue;
			}

			boolean isAvail = false;
			try {
				FDProductInfo pi = FDCachedFactory.getProductInfo(cartLine.getSkuCode());
				isAvail = pi.isAvailable();
			} catch (FDSkuNotFoundException ex) {
				// isAvail is false, that's fine
			}

			if (!isAvail) {
				if (this.removeIds == null) {
					this.removeIds = new ArrayList<Integer>();
				}
				this.removeIds.add(new Integer(cartLine.getRandomId()));
			}

		}
	}

	/**
	 * Check and collect products from request.
	 * 
	 * @param suffix
	 *            appended to request parameters to identify a particular item
	 *            (e.g. _1); if not null, only check the item with the suffix,
	 *            otherwise check all items
	 * @param strict
	 *            perform strict checking
	 * @param useProductMinimum
	 *            use the product minimum if less
	 * @param skipZeros
	 *            skip items with zero quantity
	 * @return items selected
	 * @throws JspException
	 */
	protected FDCustomerCreatedList getProductSelection(String suffix, boolean strict, boolean useProductMinimum, boolean skipZeros) throws JspException {
		FDCustomerCreatedList selection = new FDCustomerCreatedList();

		// known suffix
		if (suffix != null) {
			FDCartLineI cartI = this.processCartLine(suffix, null, strict, useProductMinimum);
			if (cartI != null) {
				FDCustomerProductListLineItem lineItem = new FDCustomerProductListLineItem(
						cartI.getSkuCode(), new FDConfiguration(cartI.getConfiguration()), cartI.getRecipeSourceId());
				selection.addLineItem(lineItem);
			}
			return selection;
		}

		// find selected suffices
		String prefix = "skuCode";
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String paramName = e.nextElement();
			if (paramName.startsWith(prefix)) {
				suffix = paramName.substring(prefix.length());

				if (skipZeros) {
					String quant = request.getParameter("quantity" + suffix);
					if (quant == null || "".equals(quant) || "0".equals(quant))
						continue;
					String salesUnit = request.getParameter("salesUnit" + suffix);
					if (salesUnit == null || "".equals(salesUnit))
						continue;
				}

				if ("ccl_actual_selection".equals(request.getParameter("source")) && "_big".equals(suffix))
					continue;
				FDCartLineI cartI = this.processCartLine(suffix, null, strict, useProductMinimum);
				if (cartI != null) {
					FDCustomerProductListLineItem lineItem = new FDCustomerProductListLineItem(
							cartI.getSkuCode(), new FDConfiguration(cartI.getConfiguration()), cartI.getRecipeSourceId());
					selection.addLineItem(lineItem);
				}
			}
		}

		if (result.isSuccess()) {
			if (selection.getLineItems().size() == 0) {
				String errorMsg = "ccl_actual_selection"
						.equalsIgnoreCase(request.getParameter("source")) ? SystemMessageList.MSG_CCL_QUANTITY_REQUIRED
						: SystemMessageList.MSG_QUANTITY_REQUIRED;
				result.addError(new ActionError("quantity", errorMsg));				
			}
		}

		return selection;
	}

	protected void finishCartCleanup() {
		if (this.removeIds == null || this.removeIds.size() == 0) {
			// nothing to be removed
			return;
		}

		HttpSession session = this.pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(USER);
		FDCartModel cart = user.getShoppingCart();

		for (Iterator<Integer> i = this.removeIds.iterator(); i.hasNext();) {
			int rid = i.next().intValue();
			cart.removeOrderLineById(rid);
		}

		session.setAttribute(USER, user);
	}

	/**
	 * processes requests to modify or remove an orderline
	 * 
	 * @return true if the cart was changed
	 */
	protected boolean changeOrderLine() throws JspException {
		String change = request.getParameter("save_changes.x");
		String remove = request.getParameter("remove_from_cart.x");
		String cartLine = request.getParameter("cartLine");
		//
		// first figure out which cartLine we're talking about
		//
		if (cartLine == null) {
			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_CARTLINE));
			return false;
		}
		int cartIndex = -1;
		try {
			cartIndex = cart.getOrderLineIndex(Integer.parseInt(cartLine));
		} catch (NumberFormatException nfe) {
			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_CARTLINE));
			return false;
		}
		if (cartIndex == -1) {
			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_CARTLINE));
			return false;
		}
		//
		// then do the right thing
		//
		if ((change != null) && !"".equals(change.trim())) {
			//
			// do a change
			//
			FDCartLineI originalLine = cart.getOrderLine(cartIndex);
			FDCartLineI newCartLine = this.processCartLine(originalLine);
			if (newCartLine != null) {
				if (originalLine.getClientCodes().size() > 0) {
					newCartLine.getClientCodes().clear();
					newCartLine.getClientCodes().addAll(originalLine.getClientCodes());
					int quantity = (int) newCartLine.getQuantity();
					int sum = 0;
					for (ErpClientCode item : newCartLine.getClientCodes())
						sum += item.getQuantity();
					if (sum > quantity) {
						ListIterator<ErpClientCode> it = newCartLine.getClientCodes().listIterator(newCartLine.getClientCodes().size());
						while (it.hasPrevious()) {
							ErpClientCode item = it.previous();
							if (item.getQuantity() < sum - quantity) {
								sum -= item.getQuantity();
								it.remove();
							} else if (item.getQuantity() == sum - quantity) {
								it.remove();
								break;
							} else /* larger quantity */ {
								item.setQuantity(item.getQuantity() - (sum - quantity));
								break;
							}
						}
					}
				}
				cart.setOrderLine(cartIndex, newCartLine);
				newCartLine.setSource(getEventSource());
				FDEventUtil.logEditCartEvent(newCartLine, request);
				return true;
			}
			return false;
		} else if ((remove != null) && !"".equals(remove.trim())) {
			//
			// do a remove
			//
			FDCartLineI originalLine = cart.getOrderLine(cartIndex);
			cart.removeOrderLine(cartIndex);
			originalLine.setSource(getEventSource());
			FDEventUtil.logRemoveCartEvent(originalLine, request);
			return true;
		}
		return false;
	}

	protected FDIdentity getIdentity() {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(USER);
		return user.getIdentity();
	}

	protected int removeRecipe() {
		String recipeId = request.getParameter("removeRecipe");
		List<FDCartLineI> cartLinesRemoved = cart.removeOrderLinesByRecipe(recipeId);
		for (FDCartLineI removedLine : cartLinesRemoved) {
			removedLine.setSource(getEventSource());
			FDEventUtil.logRemoveCartEvent(removedLine, request);
		}
		return cartLinesRemoved.size();
	}

	protected boolean removeOrderLine() {
		String cartLine = request.getParameter("cartLine");

		if (cartLine == null) {
			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_CARTLINE));
			return false;
		}

		int cartIndex = -1;
		try {
			cartIndex = cart.getOrderLineIndex(Integer.parseInt(cartLine));
		} catch (NumberFormatException nfe) {
			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_CARTLINE));
			return false;
		}
		if (cartIndex == -1) {
			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_CARTLINE));
			return false;
		}
		FDCartLineI originalLine = cart.getOrderLine(cartIndex);
		cart.removeOrderLine(cartIndex);
		originalLine.setSource(getEventSource());
		FDEventUtil.logRemoveCartEvent(originalLine, request);
		return true;

	}

	/**
	 * @return true if the cart was changed
	 */
	protected boolean addToCart() throws JspException {
		FDCartLineI cartLine = this.processCartLine(null);
		if (cartLine != null) {
			cart.addOrderLine(cartLine);
			// Log that an item has been added.
			cartLine.setSource(getEventSource());
			FDEventUtil.logAddToCartEvent(cartLine, request);
			return true;
		}
		return false;
	}
	
	/**
	 * Deduce a suffix pattern for multiple add to carts.
	 * 
	 * This method recognizes a pattern <tt>productId</tt>&lt;SUFFIX&gt;<tt>_</tt>&lt;NUMBER&gt;</tt>
	 * and assumes that &lt;SUFFIX&gt; will be used consistently for related parameters, such
	 * as <tt>quantity</tt>, <tt>catId</tt>, etc.
	 * 
	 * There is already a (hard-coded) pattern of <tt>productId</tt><i>_big</i>, which will not
	 * match this (since big is not a number). &lt;SUFFIX&gt; being the empty string is OK.
	 * 
	 * @return "&lt;SUFFIX&gt;_" (the underscore is part of the return) for matches, "_" otherwise
	 */
	private String deduceMultipleSuffix() {
		for(Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String name = e.nextElement().toString();
			if (name.startsWith("productId")) {
				int end = name.lastIndexOf('_');
				if (end == -1) continue;
				try {
					Integer.parseInt(name.substring(end+1));
					return name.substring("productId".length(), end+1);
				} catch (NumberFormatException ex) {
					continue;
				}
			}
		}
		return "_";
	}

	/**
	 * @return number of orderlines that were added, zero if none/error
	 */
	protected int addMultipleToCart() throws JspException {

		int l = "addSingleToCart_".length();
		String suffix = null;
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String n = e.nextElement();
			if (n.startsWith("addSingleToCart_")) {
				suffix = n.substring(l, n.indexOf('.'));
				break;
			}
		}
		
		if (suffix != null) {
			// add single item from multiple to cart
			LOGGER.debug("addSingleToCart " + suffix);

			FDCartLineI cartLine = this.processCartLine("_" + suffix, null, false, true);
			if (cartLine != null) {
				cart.addOrderLine(cartLine);
				cartLine.setSource(getEventSource());
				/*
				 * Logs a AddToCartEvent whenever the user adds a single item
				 * from Product detail page.
				 */
				FDEventUtil.logAddToCartEvent(cartLine, request);
				return 1;
			}
			return 0;
		}

		try {
			String itemCountParam = request.getParameter("itemCount");
			if (itemCountParam == null) {
				throw new JspException("No itemCount supplied");
			}
			int itemCount = Integer.parseInt(itemCountParam);

			cartLinesToAdd = new ArrayList<FDCartLineI>(itemCount);
			int addedLines = 0;

			//
			// allow adding qty 0 if add multiple
			boolean strict = false;
			

			for (int i = 0; i < itemCount; i++) {
				FDCartLineI cartLine = this.processCartLine(deduceMultipleSuffix() + i, null, strict, false);
				if (cartLine == null) {
					// skip
					continue;
				}
				cartLinesToAdd.add(cartLine);
				// Log that an item has been added.
				// Make sure to describe it first
				try {
					OrderLineUtil.describe(cartLine);
				} catch (FDInvalidConfigurationException e) {
					// don't care
				}
				cartLine.setSource(getEventSource());
				FDEventUtil.logAddToCartEvent(cartLine, request);
				addedLines++;
				frmSkuIds.add("skuCode_" + i);
			}

			if (result.isSuccess()) {
				if (addedLines > 0) {
					// all is well, if ends well
					cart.addOrderLines(cartLinesToAdd);
					cartLinesToAdd.clear();
				} else {
					String errorMsg = "ccl_actual_selection" .equalsIgnoreCase(request.getParameter("source")) 
						? SystemMessageList.MSG_CCL_QUANTITY_REQUIRED
						: SystemMessageList.MSG_QUANTITY_REQUIRED;
					result.addError(new ActionError("quantity", errorMsg));
				}
			}
			return addedLines;

		} catch (NumberFormatException ex) {
			throw new JspException("Invalid itemCount supplied " + ex.getMessage());
		}

	}

	/**
	 * @return the FDCartLine built from the request, or null if there were any
	 *         problems
	 */
	private FDCartLineI processCartLine(FDCartLineI originalLine) throws JspException {		
		return this.processCartLine("", originalLine, true, false);
	}

	private final static String MESSAGE_INVALID_QUANTITY = "Please select a valid quantity before adding items to your cart.";

	/**
	 * @param strictCheck
	 *            pass true to force strict error checking. If false, an
	 *            orderline with empty quantity will be skipped.
	 * @param suffix
	 *            string appended to parameter names
	 * 
	 * @return the FDCartLine built from the request, or null if there were any
	 *         problems
	 */
	private FDCartLineI processCartLine(String suffix, FDCartLineI originalLine, boolean strictCheck, boolean useProductMinimum) throws JspException {

		final String paramSkuCode = "skuCode" + suffix;
		final String paramCatId = "catId" + suffix;
		final String paramProductId = "productId" + suffix;
		// for wine usq changes
		final String paramWineCatId = "wineCatId" + suffix;

		String skuCode = request.getParameter(paramSkuCode);

		if (!strictCheck && "".equals(skuCode)) {
			return null;
		}
		String catName = "";

		if (request.getParameter(paramWineCatId) != null)
			catName = request.getParameter(paramWineCatId);
		else
			catName = request.getParameter(paramCatId);

		String prodName = request.getParameter(paramProductId);
		// variant tracking
		String variantId = request.getParameter(suffix != null ? "variant"+suffix : "variant"); // SmartStore
		
		boolean contentSpecified = !(prodName == null || prodName.length() == 0);

		if (strictCheck && !contentSpecified) {
			result.addError(new ActionError(paramProductId, "Please select a product."));
			return null;
		}

		ProductModel prodNode = null;
		try {
			if (contentSpecified) {
				prodNode = ContentFactory.getInstance().getProductByName( catName, prodName);
				if (prodNode == null) {
					throw new JspException( "Selected product not found in specified category: category: " + catName + " Product: " + prodName);
				}
			} else {
				prodNode = ContentFactory.getInstance().getProduct(skuCode);
			}
		} catch (FDSkuNotFoundException ex) {
			throw new JspException("Error accessing resources: " + ex.getMessage());
		}

		if ("".equals(skuCode)) {
			throw new JspException("No SKU code supplied");
		}

		final String paramQuantity = "quantity" + suffix;

		//
		// get quantity
		//
		double quantity = 0;
		try {

			String quan = request.getParameter(paramQuantity);
			if (quan == null) {
				result.addError(new ActionError(paramQuantity, MESSAGE_INVALID_QUANTITY));
			} else if ("".equals(quan) && useProductMinimum) {
				quan = prodNode.getQuantityMinimum() + "";
			}

			if (!strictCheck && ("".equals(quan) || "0".equals(quan))) {
				// skip this item
				return null;
			}
			quantity = new Double(quan).doubleValue();
		} catch (NumberFormatException nfe) {
			result.addError(new ActionError(paramQuantity, MESSAGE_INVALID_QUANTITY));
		}

		double origQuantity = originalLine == null ? 0 : originalLine.getQuantity();

		FDUserI user = (FDUserI) pageContext.getSession().getAttribute(USER);
		String errorMessage = this.validateQuantity(user, prodNode, quantity, origQuantity);
		if (errorMessage != null) {
			result.addError(new ActionError(paramQuantity, errorMessage));
		}

		FDProduct product;
		try {
			product = FDCachedFactory.getProduct(FDCachedFactory .getProductInfo(skuCode));
		} catch (FDResourceException fdre) {
			LOGGER.warn("Error accessing resource", fdre);
			throw new JspException("Error accessing resource " + fdre.getMessage());
		} catch (FDSkuNotFoundException fdsnfe) {
			LOGGER.warn("SKU not found", fdsnfe);
			throw new JspException("SKU not found", fdsnfe);
		}

		final String paramSalesUnit = "salesUnit" + suffix;

		//
		// pick a sales unit
		//
		FDSalesUnit salesUnit = null;

		String requestedUnit = request.getParameter(paramSalesUnit);
		if ((("".equals(requestedUnit) || requestedUnit == null) && useProductMinimum) || product.getSalesUnits().length == 1) {
			// get the default sales unit
			salesUnit = product.getSalesUnits()[0];
		} else {
			salesUnit = product.getSalesUnit(requestedUnit);
		}

		if (!strictCheck && salesUnit == null) {
			// skip this item
			return null;
		}

		//
		// no sales unit, alert user
		//
		result.addError(salesUnit == null, paramSalesUnit, "Please select "
		                + ContentNodeModelUtil.nullValue(prodNode.getSalesUnitLabel(), "Sales Unit")); 

		LOGGER.debug("Consented " + request.getParameter("consented" + suffix));
		if (prodNode.hasTerms() && !"true".equals(request.getParameter("consented" + suffix))) {
			LOGGER.debug("ADDING ERROR, since consented" + suffix + "=" + request.getParameter("consented" + suffix));
			if (!"yes".equalsIgnoreCase(request.getParameter("agreeToTerms"))) {
				result .addError(new ActionError("agreeToTerms", "Product terms"));
			}
		}
		/*
		 * Get the original cartlineId if one is present else set it blank.
		 */
		String origCartLineId = originalLine == null ? "" : originalLine.getCartlineId();
		/*
		 * The following fix is for a zone pricing bug seems to be there for a
		 * while now which was identified when fixing IPHONE-57 bug.
		 */
		PricingContext origPricingCtx = null; 
		if (originalLine != null)
			origPricingCtx = originalLine.getPricingContext();
		String pricingZoneId;
		if(origPricingCtx != null) {
			pricingZoneId = origPricingCtx.getZoneId();
		} else {
			pricingZoneId = user.getPricingZoneId();
		}
		
		FDGroup originalGrp = null;
		if (originalLine != null) {
			originalGrp = originalLine.getOriginalGroup();
			//reset skipProductPriceValidation.
			//This is temporary fix for Appbug-130 until we fix the behaviour of Modify Product Page.
			if(originalGrp != null)
				originalGrp.setSkipProductPriceValidation(false);
		}
				
		FDCartLineI theCartLine = processSimple(suffix, prodNode, product, quantity, salesUnit, origCartLineId, variantId, pricingZoneId ,originalGrp);

		// recipe source tracking
		String recipeId;
		if (originalLine != null) {
			recipeId = originalLine.getRecipeSourceId();
		} else {
			final String paramRecipeId = "recipeId" + suffix;
			recipeId = request.getParameter(paramRecipeId);
		}
		if (recipeId != null) {
			Recipe recipe = (Recipe) ContentFactory.getInstance().getContentNode(recipeId);
			if (recipe != null && theCartLine != null) {
				theCartLine.setRecipeSourceId(recipeId);
				boolean requestNotification = request .getParameter("requestNotification") != null;
				theCartLine.setRequestNotification(requestNotification);
			}
		}
		//Get the original discount applied flag if available
		boolean discountApplied = false;
		if (originalLine != null) {
			discountApplied = originalLine.isDiscountFlag();
		}
		
		if (theCartLine != null) {
			String catId = request.getParameter("catId");
			String sfx = request.getParameter("ymal_box") != null ? "" : suffix != null ? suffix : ""; 
			String ymalSetId = request.getParameter("ymalSetId"+sfx);
			String originatingProductId = request.getParameter("originatingProductId"+sfx);
			String originalOrderLineId = request.getParameter("originalOrderLineId" + suffix);

			theCartLine.setYmalCategoryId(catId);
			theCartLine.setYmalSetId(ymalSetId);
			theCartLine.setOriginatingProductId(originatingProductId);
			theCartLine.setOrderLineId(originalOrderLineId);
			
			// record 'deals' status
			if (suffix != null) {
				request.setAttribute("atc_suffix", suffix);
			}
			
			if (originalLine != null) {
				//First get the savingsId(already determined for promotion eligibility) if available else get variant id.
				String savingsId = originalLine.getSavingsId();
				if(savingsId == null) savingsId = originalLine.getVariantId();
				theCartLine.setSavingsId(savingsId);
			} else {
				//for any new recommended line
				theCartLine.setSavingsId(variantId);
			}

			theCartLine.setDiscountFlag(discountApplied);
			
			String cartonNumber = request.getParameter("cartonNumber");
			if(cartonNumber!=null)	{ 
				theCartLine.setCartonNumber(cartonNumber);
			}
		}

		return theCartLine;
	}

	private FDCartLineI processSimple(String suffix, ProductModel prodNode,
			FDProduct product, double quantity, FDSalesUnit salesUnit,
			String origCartLineId, String variantId, String pZoneId, FDGroup group) {

		//
		// walk through the variations to see what's been set and try to build a
		// variation map
		//
		HashMap<String,String> varMap = new HashMap<String,String>();
		FDVariation[] variations = product.getVariations();
		for (int i = 0; i < variations.length; i++) {
			FDVariation variation = variations[i];
			FDVariationOption[] options = variation.getVariationOptions();

			String optionName = request.getParameter(variation.getName() + suffix);

			if (options.length == 1) {
				//
				// there's only a single option, pick that
				//
				varMap.put(variation.getName(), options[0].getName());

			} else if (((optionName == null) || "".equals(optionName)) && variation.isOptional()) {
				//
				// user didn't select anything for an optional variation, pick
				// the SELECTED option for them
				//
				String selected = null;
				for (int j = 0; j < options.length; j++) {
					if (options[j].isSelected())
						selected = options[j].getName();
				}
				varMap.put(variation.getName(), selected);
			} else if (optionName != null && !"".equals(optionName)) {
				//
				// validate & add the option the user selected
				//
				boolean validOption = false;
				for (int j = 0; j < options.length; j++) {
					if (optionName.equals(options[j].getName())) {
						validOption = true;
						break;
					}
				}
				if (validOption) {
					varMap.put(variation.getName(), optionName);
				} else {
					result.addError(new ActionError(variation.getName()
							+ suffix, "Please select "
							+ variation.getDescription()));
				}
			} else {
				//
				// user didn't select anything for a required variation, alert
				// them
				//
				result.addError(new ActionError(variation.getName() + suffix,
						"Please select " + variation.getDescription()));
			}
		}

		if (!result.isSuccess()) {
			return null;
		}
		//
		// make the order line and add it to the cart
		//
		FDCartLineModel cartLine = null;
		if (origCartLineId == null || origCartLineId.length() == 0) {
			/*
			 * This condition is true whenever there is a new item added to the
			 * cart.
			 */
			cartLine = new FDCartLineModel(new FDSku(product), prodNode,
					new FDConfiguration(quantity, salesUnit .getName(), varMap), 
					variantId, pZoneId);
		} else {
			/*
			 * When an existing item in the cart is modified, reuse the same
			 * cartlineId instead of generating a new one.
			 */
			List<ErpClientCode> clientCodes = Collections.emptyList();
			cartLine = new FDCartLineModel(new FDSku(product), prodNode,
					new FDConfiguration(quantity, salesUnit .getName(), varMap), 
					origCartLineId, null, false, variantId, pZoneId, clientCodes);
			//Any group info from original cartline is moved to new cartline on modify.
			cartLine.setFDGroup(group);
		}

		return cartLine;
	}

	/**
	 * Deduce the quantity that has already been processed but not yet added to
	 * the cart.
	 * 
	 * @param product
	 * @return total quantity of product already proccessed
	 */
	private double getCartlinesQuantity(ProductModel product) {
		String productName = product.getContentName();
		double sum = 0;
		for (Iterator<FDCartLineI> i = this.cartLinesToAdd.iterator(); i.hasNext();) {
			FDCartLineI line = i.next();
			if (productName.equals(line.getProductName())) {
				sum += line.getQuantity();
			}
		}
		return sum;
	}

	private String validateQuantity(FDUserI user, ProductModel prodNode, double quantity, double adjustmentQuantity) {
		
		DecimalFormat formatter = new DecimalFormat("0.##");
		if (quantity < prodNode.getQuantityMinimum()) {
			return "FreshDirect cannot deliver less than "
					+ formatter.format( prodNode.getQuantityMinimum() ) + " "
					+ prodNode.getFullName();
		}

		if ((quantity - prodNode.getQuantityMinimum()) % prodNode.getQuantityIncrement() != 0) {
			return "Quantity must be an increment of " + formatter.format( prodNode.getQuantityIncrement() );
		}

		// For CCL Requests (other than cart events)
		// quantity limits do not apply
		if (!isCCLRequest() || isAddToCartRequest()) {
			if (getCartlinesQuantity(prodNode)
					+ cart.getTotalQuantity(prodNode) + quantity
					- adjustmentQuantity > user.getQuantityMaximum(prodNode)) {
				return "Please note: there is a limit of " + formatter.format( prodNode.getQuantityMaximum() ) + 
					   " per order of " + prodNode.getFullName();				
			}
		}

		return null;
	}

	/**
	 * Update the quantity/sales unit fields.
	 * 
	 * @return true if the cart was changed
	 */
	protected boolean updateQuantities() throws JspException {
		FDUserI user = (FDUserI) pageContext.getSession().getAttribute(USER);

		boolean cartChanged = false;
		ArrayList<FDCartLineI> orderLines = new ArrayList<FDCartLineI>(cart.getOrderLines());
		try {
			int idx = -1;
			for (ListIterator<FDCartLineI> i = orderLines.listIterator(); i.hasNext();) {
				idx++;
				FDCartLineI orderLine = i.next();

				String randomId = request.getParameter("rnd_" + idx);
				if (!(String.valueOf(orderLine.getRandomId()).equals(randomId))) {
					// request operates on stale data, skip
					continue;
				}

				orderLine.setSource(getEventSource());

				ProductModel prodNode = ContentFactory.getInstance()
						.getProductByName(orderLine.getCategoryName(), orderLine.getProductName());

				boolean modifyOrderMode = orderLine instanceof FDModifyCartLineI;

				// get the quantity from the form
				String reqQty = request.getParameter("quantity_" + idx);

				if (reqQty != null) {
					if ("".equals(reqQty)) {
						// mark orderline for removal
						i.set(null);
						cartChanged = true;
						// Log that the item has been removed.
						FDEventUtil.logRemoveCartEvent(orderLine, request);
						continue;
					}
					try {
						double quantity = new Double(reqQty).doubleValue();
						if (quantity <= 0) {
							// mark orderline for removal
							i.set(null);
							cartChanged = true;
							// Log that the item has been removed.
							FDEventUtil.logRemoveCartEvent(orderLine, request);
							continue;
						}

						
						//make sure prodNode isn't null before checking mins
						if (prodNode != null) {
							if (quantity < prodNode.getQuantityMinimum()) {
								quantity = prodNode.getQuantityMinimum();
							} else {
								double totalQty = cart.getTotalQuantity(prodNode);
								if (quantity + totalQty - orderLine.getQuantity() > user.getQuantityMaximum(prodNode)) {
									quantity = user.getQuantityMaximum(prodNode) - totalQty + orderLine.getQuantity();
								}
							}
							quantity = Math.floor((quantity - prodNode.getQuantityMinimum())
									/ prodNode.getQuantityIncrement())
									* prodNode.getQuantityIncrement()
									+ prodNode.getQuantityMinimum();
						}

						if (quantity <= 0) {
							// set quantity to compensate for subsequent
							// calculations
							orderLine.setQuantity(quantity);
							// mark orderline for removal
							i.set(null);
							cartChanged = true;
							// Log that the item has been removed.
							FDEventUtil.logRemoveCartEvent(orderLine, request);
							continue;
						}

						if ("1".equals(request.getParameter("clicode_dirty"))) {
							List<ErpClientCode> clientCodes = orderLine.getClientCodes();
							if (quantity == 1) {
								// clicode_single
								String reqCCode = request.getParameter("clicode_clientcode_" + idx);
								if (reqCCode != null)
									reqCCode = reqCCode.trim();
								else
									reqCCode = "";

								if (reqCCode.length() == 0) {
									// removal
									if (clientCodes.size() > 0) {
										clientCodes.clear();
										cartChanged = true;
									}
								} else {
									if (clientCodes.size() == 0) {
										// add
										clientCodes.add(new ErpClientCode(reqCCode, 1));
										user.getClientCodesHistory().add(new IgnoreCaseString(reqCCode));
										cartChanged = true;
									} else {
										ErpClientCode firstCC = clientCodes.get(0);
										if (clientCodes.size() != 1 || firstCC.getQuantity() != 1
												|| !firstCC.getClientCode().equals(reqCCode)) {
											// update
											clientCodes.clear();
											clientCodes.add(new ErpClientCode(reqCCode, 1));
											user.getClientCodesHistory().add(new IgnoreCaseString(reqCCode));
											cartChanged = true;
										}
									}
								}
							} else if (quantity > 1) {
								// clicode_multi
								try {
									String multiValString = request.getParameter("clicode_multi_val_" + idx);
									if (multiValString == null)
										multiValString = "";
									else
										multiValString = multiValString.trim();

									if (multiValString.length() != 0) {
										JSONObject multiVal = new JSONObject(multiValString);
										@SuppressWarnings("unchecked")
										Iterator<String> it = multiVal.keys();
										SortedSet<Integer> keys = new TreeSet<Integer>();
										while (it.hasNext())
											keys.add(Integer.parseInt(it.next()));

										List<ErpClientCode> ccs = new ArrayList<ErpClientCode>();
										for (int key : keys) {
											JSONObject item = multiVal.getJSONObject(Integer.toString(key));
											int ccQuantity = item.getInt("quantity");
											String clientCode = item.getString("clientCode");
											ccs.add(new ErpClientCode(clientCode, ccQuantity));
											user.getClientCodesHistory().add(new IgnoreCaseString(clientCode));
										}

										if (!ErpClientCode.equalsList(orderLine.getClientCodes(), ccs)) {
											orderLine.getClientCodes().clear();
											orderLine.getClientCodes().addAll(ccs);
											cartChanged = true;
										}
									}
								} catch (RuntimeException e) {
									LOGGER.warn("error when processing multi-value client codes for cart line #" + idx, e);
									LOGGER.warn("client code values for cart line #" + idx
											+ " are not changed due to previous error");
								} catch (ParseException e) {
									LOGGER.warn("error when parsing multi-value client codes for cart line #" + idx, e);
									LOGGER.warn("client code values for cart line #" + idx
											+ " are not changed due to previous error");
								}
							}
						}

						if (quantity != orderLine.getQuantity()) {

							if (!modifyOrderMode) {
								// simple modify qty, or qty decrease in modify
								// order mode
								orderLine.setQuantity(quantity);
								// Log that the quantity has been updated.
								FDEventUtil .logEditCartEvent(orderLine, request);

							} else {
								// modify order mode

								// how much we're adding/removing
								double deltaQty = quantity - orderLine.getQuantity();

								if (deltaQty < 0) {
									// need to remove some, that's easy, i can
									// do that...
									orderLine.setQuantity(quantity);
									// Log that the quantity has been updated.
									FDEventUtil.logEditCartEvent(orderLine, request);

								} else {
									// deltaQty>0, see how much can we add to
									// this orderline
									double origQuantity = ((FDModifyCartLineI) orderLine)
											.getOriginalOrderLine().getQuantity();
									double origDiff = origQuantity - orderLine.getQuantity();

									if (origDiff > 0) {
										double addToLine = Math.min(origDiff, deltaQty);
										orderLine.setQuantity(orderLine .getQuantity() + addToLine);
										// Log that the quantity has been updated.
										FDEventUtil.logEditCartEvent(orderLine, request);
										deltaQty -= addToLine;
									}

									// add a new orderline for rest of the difference, if any
									if (deltaQty > 0) {

										FDCartLineI newLine = orderLine .createCopy();
										newLine.setPricingContext(new PricingContext(user.getPricingZoneId()));
										try {
											OrderLineUtil.cleanup(newLine);
										} catch (FDInvalidConfigurationException e) {
											throw new JspException( "Orderline configuration no longer valid", e);
										}
										newLine.setQuantity(deltaQty);
										i.add(newLine);
										// Log a addToCart event.
										FDEventUtil.logAddToCartEvent(newLine, request);
									}
								}
							}
							cartChanged = true;
						}
					} catch (NumberFormatException nfe) {
						result .addError(new ActionError("quantity_" + idx, "Invalid quantity " + reqQty + ", not a number"));
					}
				} else {
					// it's a sales unit chg
					String reqSalesUnit = request.getParameter("salesUnit_" + idx);

					if ("".equals(reqSalesUnit)) {
						// mark orderline for removal
						i.set(null);
						cartChanged = true;
						// Log that the item has been removed.
						orderLine.setSource(getEventSource());
						FDEventUtil.logRemoveCartEvent(orderLine, request);
						continue;
					}

					if (!reqSalesUnit.equals(orderLine.getSalesUnit())) {
						FDProduct product = FDCachedFactory.getProduct(
								orderLine.getSkuCode(), orderLine.getVersion());

						boolean unitValid = product.getSalesUnit(reqSalesUnit) != null;
						if (unitValid) {
							if (!modifyOrderMode) {
								orderLine.setSalesUnit(reqSalesUnit);
								// Log that the quantity has been updated.
								FDEventUtil.logEditCartEvent(orderLine, request);
							} else {
								// modify order mode - replace orderline
								// FDCartLineI newLine =
								// orderLine.createCleanLine();
								// if (newLine==null) {
								// !!!
								// throw new JspException("Orderline
								// configuration no longer valid");
								// }
								orderLine.setSalesUnit(reqSalesUnit);
								i.set(orderLine);
								// Log that the salesunit has been updated.
								FDEventUtil.logEditCartEvent(orderLine, request);

							}
							cartChanged = true;
						} else {
							result.addError(new ActionError("salesUnit_" + idx,
									"Sales unit " + reqSalesUnit + " is not valid"));
						}
					}

					if ("1".equals(request.getParameter("clicode_dirty"))) {
						// clicode_sunit (Sales Unit based Client Codes)
						List<ErpClientCode> clientCodes = orderLine.getClientCodes();

						String reqCCode = request.getParameter("clicode_clientcode_" + idx);
						if (reqCCode != null)
							reqCCode = reqCCode.trim();
						else
							reqCCode = "";

						if (reqCCode.length() == 0) {
							// removal
							if (clientCodes.size() > 0) {
								clientCodes.clear();
								cartChanged = true;
							}
						} else {
							if (clientCodes.size() == 0) {
								// add
								clientCodes.add(new ErpClientCode(reqCCode, 1));
								user.getClientCodesHistory().add(new IgnoreCaseString(reqCCode));
								cartChanged = true;
							} else {
								ErpClientCode firstCC = clientCodes.get(0);
								if (clientCodes.size() != 1 || firstCC.getQuantity() != 1
										|| !firstCC.getClientCode().equals(reqCCode)) {
									// update
									clientCodes.clear();
									clientCodes.add(new ErpClientCode(reqCCode, 1));
									user.getClientCodesHistory().add(new IgnoreCaseString(reqCCode));
									cartChanged = true;
								}
							}
						}
					} // if clicode_dirty
				}
			}

		} catch (FDSkuNotFoundException ex) {
			throw new JspException(ex.getMessage());
		} catch (FDResourceException ex) {
			throw new JspException(ex.getMessage());
		}
		if (cartChanged) {
			// remove marked orderlines
			for (ListIterator<FDCartLineI> i = orderLines.listIterator(); i.hasNext();) {
				if (i.next() == null) {
					i.remove();
				}
			}
			cart.setOrderLines(orderLines);
			return true;
		}
		return cartChanged;
	}

	protected int removeAllCartLines() {
		int lines = cart.numberOfOrderLines();
		ArrayList<FDCartLineI> orderLines = new ArrayList<FDCartLineI>(cart.getOrderLines());
		cart.clearOrderLines();
		// Log a removeCart Event for each of those orderLine.
		for (Iterator<FDCartLineI> i = orderLines.listIterator(); i.hasNext();) {
			FDCartLineI orderLine = i.next();
			orderLine.setSource(getEventSource());
			FDEventUtil.logRemoveCartEvent(orderLine, request);
		}
		return lines;
	}

	/**
	 * Builds a valid, well-formed ErpComplaintModel
	 * 
	 */
	private void buildComplaint(ActionResult result, ErpComplaintModel complaintModel) throws FDResourceException {

		this.parseOrderLines(result, complaintModel);
		this.setComplaintDetails(result, complaintModel);

	}

	/**
	 * Build complaint lines for each order line and validate data.
	 * 
	 */
	private void parseOrderLines(ActionResult result, ErpComplaintModel complaintModel) throws FDResourceException {

		ArrayList<ErpComplaintLineModel> lines = new ArrayList<ErpComplaintLineModel>();
		//FDOrderAdapter order = (FDOrderAdapter) FDCustomerManager.getOrder(request.getParameter("orig_sale_id"));
		List<FDCartLineI> lineItems=new ArrayList<FDCartLineI>(this.cart.getOrderLines());
		
		for (int i = 0; i < orderLineReason.length; i++) {

			ErpComplaintLineModel line = new ErpComplaintLineModel();
			//
			// allow complaints with a zero quantity...
			//
			// if ( orderLineQty[i] != null && !"".equals(orderLineQty[i]) &&
			// Double.parseDouble(orderLineQty[i]) <= 0 )
			// continue;
			//
			//
			// ...but make sure they at least have a reason code
			//
			if (orderLineReason[i] == null || "".equals(orderLineReason[i]))
				continue;

			// Set up the Complaint Line Model with proper info
			//
			line.setType(EnumComplaintLineType.ORDER_LINE);
			line.setOrderLineId(this.orderLineId[i]);
			line.setComplaintLineNumber("" + i);

			double quantity = 0.0;
			line.setQuantity(quantity);

			double amount = 0.0;
			line.setAmount(amount);

			if (orderLineReason[i] != null && !"".equals(orderLineReason[i]))
				line.setReason(ComplaintUtil.getReasonById(orderLineReason[i]));

			line.setMethod(EnumComplaintLineMethod.STORE_CREDIT);

			lines.add(line);
			//
			// Investigate for errors
			//
			if (!line.isValidComplaintLine()) {
				result.addError(new ActionError("ol_error_" + i, "Missing or invalid data in this line."));
				addGeneralError(result);
			}
			//add carton id for makegood orders
			try
			{				
				if(lineItems!=null)
				{
					for(FDCartLineI lineItem :lineItems)
					{
						if(this.orderLineId[i].equals(lineItem.getOrderLineId()))
						{
							lineItems.remove(lineItem);
							line.setCartonNumber(lineItem.getCartonNumber());
						}
					}
				}				
			}catch(Exception e){}

		}

		if (lines.size() > 0)
			complaintModel.addComplaintLines(lines);
		complaintModel.setType(EnumComplaintType.STORE_CREDIT);
	}

	private void setComplaintDetails(ActionResult result, ErpComplaintModel complaintModel) {
		CrmAgentModel agent = CrmSession.getCurrentAgent(pageContext.getSession());
		if (agent != null) {
			complaintModel.setCreatedBy(agent.getUserId());
		} else {
			CallcenterUser ccUser = (CallcenterUser) pageContext.getSession().getAttribute(SessionName.CUSTOMER_SERVICE_REP);
			complaintModel.setCreatedBy(ccUser.getId());
		}

		complaintModel.setDescription(this.description);
		complaintModel.setCreateDate(new java.util.Date());
		complaintModel.setStatus(EnumComplaintStatus.PENDING);
		complaintModel.setEmailOption(EnumSendCreditEmail.DONT_SEND);

	} // method setComplaintDetails

	/**
	 * Checks for the presence of a general error message in the ActionResult
	 * parameter. If none is present, one is added.
	 * 
	 * @param ActionResult
	 */
	private void addGeneralError(ActionResult result) {
		if (!result.hasError("general_error_msg"))
			result.addError(new ActionError("general_error_msg", GENERAL_ERR_MSG));
	}

	/**
	 * Gathers registration-specific data (i.e., customer data)
	 * 
	 */
	private void getFormData(HttpServletRequest request, ActionResult result) {

		String orderId = request.getParameter("orig_sale_id");
		this.orderLineId = request.getParameterValues("orderlineId");
		orderLineReason = request.getParameterValues("ol_credit_reason");

	} // method getFormData

}
