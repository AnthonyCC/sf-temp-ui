<%@page import="com.freshdirect.fdstore.EnumEStoreId"%>
<%@ page import='com.freshdirect.framework.util.NVL'
%><%@ page import='org.apache.commons.lang.StringUtils'
%><%@ page import='java.util.*'
%><%@ page import='java.net.*'
%><%@ page import='java.io.UnsupportedEncodingException'
%><%@ page import='com.freshdirect.cms.CmsServiceLocator'
%><%@ page import='com.freshdirect.cms.contentio.xml.XmlContentMetadataService'
%><%@ page import='com.freshdirect.customer.ErpSaleInfo'
%><%@ page import='com.freshdirect.customer.EnumDeliveryType'
%><%@ page import='com.freshdirect.fdstore.FDStoreProperties'
%><%@ page import='com.freshdirect.fdstore.customer.FDUserI'
%><%@ page import='com.freshdirect.fdstore.customer.FDOrderI'
%><%@ page import='com.freshdirect.fdstore.customer.FDCartLineI'
%><%@ page import='com.freshdirect.fdstore.customer.ProfileModel'
%><%@ page import='com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor'
%><%@ page import='com.freshdirect.fdstore.customer.FDCustomerManager'
%><%@ page import='com.freshdirect.fdstore.customer.FDProductSelectionI'
%><%@ page import='com.freshdirect.fdstore.customer.FDProductCollectionI'
%><%@ page import='com.freshdirect.storeapi.content.ContentFactory'
%><%@ page import='com.freshdirect.common.customer.EnumServiceType'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'
%><%@ page import='com.freshdirect.deliverypass.EnumDlvPassStatus'
%><%@ page import='com.freshdirect.deliverypass.EnumDlvPassProfileType'
%><%@ page import='com.freshdirect.fdstore.deliverypass.DeliveryPassUtil'
%><%@ page import='com.freshdirect.framework.util.DateUtil'
%><%@ page import='com.freshdirect.framework.util.QueryStringBuilder'
%><%@ page import='com.freshdirect.deliverypass.EnumDPAutoRenewalType'
%><%@ page import='com.freshdirect.smartstore.fdstore.SmartStoreUtil'
%><%@ page import='com.freshdirect.fdstore.zone.FDZoneInfoManager'
%><%@ page import='com.freshdirect.fdstore.ZonePriceListing'
%><%@ page import='com.freshdirect.webapp.util.JspMethods'
%><%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"
%><%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"
%><%@ page import='com.freshdirect.fdstore.orderhistory.OrderHistoryService'
%><%@ taglib prefix="fd" uri="freshdirect"
%><%!

	private final static String CCL_NONELIGIBLE = "0";
	private final static String CCL_INEXPERIENCED = "1";
	private final static String CCL_EXPERIENCED = "2";

	private String cclExperienceLevel(FDUserI user) {
		if (!user.isCCLEnabled())
			return CCL_NONELIGIBLE;
		if (user.isCCLInExperienced())
			return CCL_INEXPERIENCED;
		return CCL_EXPERIENCED;
	}

%><%

	if (FDStoreProperties.isAdServerEnabled() || FDStoreProperties.isDfpEnabled()) {

%><fd:SmartSavingsUpdate justCheckSavingVariantId="true"></fd:SmartSavingsUpdate><%

		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

		QueryStringBuilder queryString = new QueryStringBuilder();
		if (user != null) {

		    XmlContentMetadataService metadataService = CmsServiceLocator.xmlContentMetadataService();
            String storeVersion = metadataService != null ? metadataService.calculatePublishId().toString() : null;

			String metalRating = "";
			int vip = 0;
			int chefsTable = 0;
			String test = "";
			ProfileModel profile = null;
			if (user.getIdentity() != null) {
				profile = user.getFDCustomer().getProfile();
				metalRating = profile.getCustomerMetalType();
				vip = profile.isVIPCustomer() ? 1 : 0;
				chefsTable = profile.isChefsTable() ? 1 : 0;
				test = profile.isOASTest() ? "true" : "false";
			}

			String type = "";
			String depotAffil = "";
			EnumServiceType service = user.getSelectedServiceType();
			if (EnumServiceType.HOME.equals(service)) {
				type = "home";
			} else if (EnumServiceType.DEPOT.equals(service)) {
				type = "depot";
				depotAffil = user.getDepotCode();
			} else if (EnumServiceType.PICKUP.equals(service)) {
				type = "pickup";
			} else if (EnumServiceType.CORPORATE.equals(service)) {
				type = "cos";
			} else if (EnumServiceType.WEB.equals(service)) {
				type = "web";
			}

			Date dlvDate = user.getOrderHistory().getLastOrderDlvDate();
			String lastOrderDate = (dlvDate != null) ? dlvDate
					.toString() : "";
			EnumDeliveryType orderType = user.getOrderHistory()
					.getLastOrderType();
			String lastOrderType = orderType != null ? orderType
					.getCode().toLowerCase() : "";
			String orderZone = user.getOrderHistory()
					.getLastOrderZone();
			String lastOrderZone = orderZone != null ? orderZone : "";
			
			Date fdDlvDate = OrderHistoryService.defaultService().getLastOrderDateByDeliveryTypes(user.getOrderHistory(), EnumDeliveryType.HOME, EnumDeliveryType.PICKUP, EnumDeliveryType.DEPOT);
			String fdLastOrderDate = (fdDlvDate != null) ? fdDlvDate.toString() : "";
			String fdOrderZone = OrderHistoryService.defaultService().getLastOrderDeliveryZoneByDeliveryTypes(user.getOrderHistory(), EnumDeliveryType.HOME, EnumDeliveryType.PICKUP, EnumDeliveryType.DEPOT);
			String fdLastOrderZone = fdOrderZone != null ? fdOrderZone : "";

			Date fkDlvDate = OrderHistoryService.defaultService().getLastOrderDateByDeliveryTypes(user.getOrderHistory(), EnumDeliveryType.FDX);
			String fkLastOrderDate = (fkDlvDate != null) ? fkDlvDate.toString() : "";
			String fkOrderZone = OrderHistoryService.defaultService().getLastOrderDeliveryZoneByDeliveryTypes(user.getOrderHistory(),EnumDeliveryType.FDX);
			String fkLastOrderZone = fkOrderZone != null ? fkOrderZone : "";
			
			Date cosDlvDate = OrderHistoryService.defaultService().getLastOrderDateByDeliveryTypes(user.getOrderHistory(), EnumDeliveryType.CORPORATE);
			String cosLastOrderDate = (cosDlvDate != null) ? cosDlvDate.toString() : "";
			String cosOrderZone = OrderHistoryService.defaultService().getLastOrderDeliveryZoneByDeliveryTypes(user.getOrderHistory(),EnumDeliveryType.CORPORATE);
			String cosLastOrderZone = cosOrderZone != null ? cosOrderZone : "";
			

			// Set of String (product department Ids, "rec" for recipe items)
			Set cartDeptIds = new HashSet();
			for (Iterator i = user.getShoppingCart().getOrderLines()
					.iterator(); i.hasNext();) {
				FDCartLineI cartLine = (FDCartLineI) i.next();
				if (cartLine.getRecipeSourceId() != null) {
					cartDeptIds.add("rec");
				} else {
					cartDeptIds.add(cartLine.lookupProduct()
							.getDepartment().getContentName());
				}
			}

			// Set of product deparment ids made from CCL list contents
			// The "loadedCclList" request attribute is set by the QuickShop controller tag (when
			// list items are loaded)
			Set ccListDeptIds = new HashSet();
			FDProductCollectionI ccList = (FDProductCollectionI) request
					.getAttribute("loadedCclList");
			if (ccList != null) {
				for (Iterator i = ccList.getProducts().iterator(); i
						.hasNext();) {
					FDProductSelectionI productSelection = (FDProductSelectionI) i
							.next();
					if (productSelection.getRecipeSourceId() != null) {
						ccListDeptIds.add("rec");
					} else {
						ccListDeptIds.add(productSelection
								.lookupProduct().getDepartment()
								.getContentName());
					}
				}
			}

			String pageId = "";
			if (request.getParameter("deptId") != null)
				pageId = request.getParameter("deptId");
			if (request.getParameter("catId") != null)
				pageId = request.getParameter("catId");
			if (request.getParameter("subCatId") != null)
				pageId = request.getParameter("subCatId");
			if (request.getParameter("productId") != null)
				pageId = request.getParameter("productId");
			if (request.getParameter("recipeId") != null)
				pageId = request.getParameter("recipeId");
			if (request.getParameter("id") != null)
				pageId = request.getParameter("id");

			String brand = "";
			if (request.getParameter("brandValue") != null)
				brand = request.getParameter("brandValue");

			Map pages = new HashMap();
			pages.put("/cart_confirm.jsp", "confirm");
			pages.put("/quickshop/", "quickshop");
			pages.put("/checkout/signup_ckt.jsp", "signup_ckt");
			pages.put("/registration/signup.jsp", "signup");
			pages.put("/checkout/step_1_choose.jsp", "step_1_choose");
			pages.put("/checkout/step_2_select.jsp", "step_2_select");
			pages.put("/checkout/step_3_choose.jsp", "step_3_choose");
			pages.put("/checkout/step_4_submit.jsp", "step_4_submit");
			pages.put("/checkout/step_4_receipt.jsp", "step_4_receipt");
			pages.put("/delivery_info_avail", "delivery_info");
			pages.put("/your_account/manage_account.jsp",
					"manage_account");
			pages.put("/login/login.jsp", "login");
			pages.put("/search.jsp", "search");
			pages.put("/help/index.jsp", "help");
			pages.put("grocery_cart_confirm.jsp", "gconfirm");
			pages.put("/pay_by_check.jsp", "pay_by_check_popup");
			pages.put("/your_account/payment_information.jsp",
					"payment_info");
			pages.put("/recipe_search.jsp", "recipe_search");
			pages.put("cart_confirm_pdp.jsp", "pdpconfirm");

  			String pageType = NVL.apply(request.getParameter("pageType"), "");

  			if (request.getParameter("searchParams") != null) {
  				pageType = "search";
  			}
			
  			/* this loop does not set the pageType from query params like it looks like */
			String uri = request.getRequestURI().toLowerCase();
			for (Iterator ptIter = pages.entrySet().iterator(); ptIter
					.hasNext();) {
				Map.Entry e = (Map.Entry) ptIter.next();
				String pattern = (String) e.getKey();
				String value = (String) e.getValue();

				if (uri.indexOf(pattern) > -1) {
					pageType = value;
					break;
				}
			}
			
			if (smartSavingVariantId != null && smartSavingVariantId.length() > 0) {
			    queryString.addParam("ssp", smartSavingVariantId);
			}
			queryString
					.addParam("v", metalRating)
					.addParam("hv", vip)
					.addParam("ct", chefsTable)
					.addParam("test", test)
					.addParam("sv", storeVersion)
					.addParam("zip", user.getZipCode())
					.addParam("type", type)
					.addParam("servicetype", user.isCorporateUser() ? "cos" : "home")
					.addParam("depot", depotAffil)
					.addParam("nod", lastOrderDate)
					.addParam("fdnod",fdLastOrderDate)
					.addParam("cosnod", cosLastOrderDate)
					.addParam("fknod",fkLastOrderDate)
					.addParam("do", user.getAdjustedValidOrderCount())
					.addParam("fddo",user.getAdjustedValidOrderCount(EnumEStoreId.FD) - user.getAdjustedValidOrderCount(EnumDeliveryType.CORPORATE))
					.addParam("cosdo", user.getAdjustedValidOrderCount(EnumDeliveryType.CORPORATE))
					.addParam("fkdo",user.getAdjustedValidOrderCount(EnumEStoreId.FDX))
					.addParam("win", 2)
					.addParam("COSSTO", ((user.getActiveSO3s().size()>0)?'T':'F'));

			if (cartDeptIds.size() > 0) {
				StringBuffer cartString = new StringBuffer();
				for (Iterator i = cartDeptIds.iterator(); i.hasNext();) {
					cartString.append(i.next());
					if (i.hasNext())
						cartString.append(':');
				}
				queryString.addParam("cart", cartString);
			}

			if (ccListDeptIds.size() > 0) {
				StringBuffer listString = new StringBuffer();
				for (Iterator i = ccListDeptIds.iterator(); i.hasNext();) {
					listString.append(i.next());
					if (i.hasNext())
						listString.append(':');
				}
				queryString.addParam("list", listString);
			}

			queryString.addParam("lu", cclExperienceLevel(user))
				.addParam("pt", pageType).addParam("id", pageId)
				.addParam("brand", brand).addParam("lotype",lastOrderType)
					.addParam("lozn",lastOrderZone)
					.addParam("fdlozn",fdLastOrderZone)
					.addParam("coslozn", cosLastOrderZone)
					.addParam("fklozn",fkLastOrderZone)
					.addParam("ecp",user.isCheckEligible() ? 1 : 0)
					.addParam("ecpoc",user.getOrderHistory().getValidECheckOrderCount())
				.addParam("county",
					NVL.apply(user.getDefaultCounty(), ""))
				.addParam("ref_prog_id",
					NVL.apply(user.getLastRefProgId(), ""))
				.addParam("oim", user.isReceiveFDEmails() ? 1 : 0)
				.addParam("recipe", true);

			if (profile != null) {
				queryString.addParam("ecppromo", NVL.apply(profile
						.getEcpPromo(), ""));
			} else {
				queryString.addParam("ecppromo", "");
			}

			String extraProps = FDStoreProperties
					.getExtraAdServerProfileAttributes();
			StringTokenizer tokenizer = new StringTokenizer(extraProps,
					",");

			while (tokenizer.hasMoreTokens()) {
				String tok = tokenizer.nextToken();
				String[] pairs = tok.split("=");

				if (pairs.length != 2) {
					continue;
				}

				queryString.addParam(pairs[1], profile != null ? NVL
						.apply(profile.getAttribute(pairs[0]), "")
						: null);

			}

			//Building up the values required for Delivery Pass Ads.
			if (user.isEligibleForDeliveryPass()) {
				String profileVal = user.getDlvPassProfileValue();

				queryString.addParam("dpas", profileVal);

				String dprem = null;
				String dpused = null;
				String dpar = "n";
				boolean expired = false;
				EnumDlvPassStatus status = user.getDeliveryPassStatus();
				EnumDPAutoRenewalType dparType = user.hasAutoRenewDP();
				if ((EnumDPAutoRenewalType.YES.equals(dparType))
						&& (user.getDlvPassInfo()
								.getAutoRenewUsablePassCount() > 0)) {
					dpar = "y";
				}
				queryString.addParam("dpar", dpar);
				if (DeliveryPassUtil.isEligibleStatus(status)
						&& (user.isDlvPassExpired() == false)) {
					//Eligible to Buy. But not purchased.
					dprem = "n";
					dpused = "n";
				} else {
					//Delivery pass purchased. Account Exists.
					dprem = String.valueOf(user.getDlvPassInfo()
							.getRemainingCount());
					dpused = String.valueOf(user.getDlvPassInfo()
							.getUsedCount());
				}

				if (user.getEligibleDeliveryPass() == EnumDlvPassProfileType.BSGS) {
					//If BSGS pass then pass the remaining count.
					queryString.addParam("dpr", dprem);
				} else {
					//If UNLIMITED pass then pass the used count if not expired.
					if (user.isDlvPassExpired()) {
						int days = user.getDlvPassInfo()
								.getDaysSinceDPExpiry()
								* -1;

						queryString.addParam("expd", days);
					} else if ((user.getDlvPassInfo() != null)
							&& user.getDlvPassInfo().isUnlimited() == false) {
						//If BSGS pass then pass the remaining count.
						dprem = String.valueOf(user.getDlvPassInfo()
								.getRemainingCount());
						queryString.addParam("dpr", dprem);

					} else if (user.getUsableDeliveryPassCount() > 0) {
						//Not Purchased yet or Purchased not expired.
						//int days=DateUtil.getDiffInDays(user.getDlvPassInfo().getExpDate(), new Date());
						int days = user.getDlvPassInfo()
								.getDaysToDPExpiry();
						queryString.addParam("dpu", dpused).addParam(
								"expd", days);
					} else {
						queryString.addParam("dpu", dpused);
					}
				}
				if( user.getDlvPassInfo() != null && 
					user.getDlvPassInfo().getTypePurchased() != null ) {	 
					queryString.addParam("dp", user.getDlvPassInfo().getTypePurchased().getCode());	 
				}
			}

			if(user.getDefaultState()!=null){
				queryString.addParam("state", user.getDefaultState());
			}
			if(user.isEbtAccepted()){
				queryString.addParam("ebt_accepted", "true");
			}

			// record cohort ID
			queryString.addParam("cohort", user.getCohortName());
		} else { //! user == null
			if (request.getAttribute("RefProgId") != null) {
				queryString.addParam("ref_prog_id", NVL.apply(
					(String) request.getAttribute("RefProgId"), ""));
			}
		}

		// record search terms
		if (request.getParameter("searchParams") != null) {
			queryString.addParam("searchParams", URLEncoder.encode(request.getParameter("searchParams"), "UTF-8"));
		}
		if(FDStoreProperties.isZonePricingAdEnabled()){
			queryString.addParam("zonelevel","true");
			if(null !=user){
			String zoneId = FDZoneInfoManager.findZoneId((null!=user.getSelectedServiceType()?user.getSelectedServiceType().getName():null), user.getZipCode());
			if(zoneId.equalsIgnoreCase(ZonePriceListing.MASTER_DEFAULT_ZONE)){
				queryString.addParam("mzid",zoneId);

			}else if(zoneId.equalsIgnoreCase(ZonePriceListing.RESIDENTIAL_DEFAULT_ZONE)||zoneId.equalsIgnoreCase(ZonePriceListing.CORPORATE_DEFAULT_ZONE)){
				queryString.addParam("szid",zoneId);
				queryString.addParam("mzid",ZonePriceListing.MASTER_DEFAULT_ZONE);
			}else{
				queryString.addParam("zid",zoneId);
				zoneId = FDZoneInfoManager.findZoneId((null!=user.getSelectedServiceType()?user.getSelectedServiceType().getName():null),null);
				queryString.addParam("szid",zoneId);
				queryString.addParam("mzid",ZonePriceListing.MASTER_DEFAULT_ZONE);
			}
			}
		}
		if (request.getParameter("TSAPROMO") != null) {
			queryString.addParam("TSAPROMO",request.getParameter("TSAPROMO"));
		}else if (
				("/about/index.jsp".equalsIgnoreCase(request.getRequestURI()) || "/site_access/site_access.jsp".equalsIgnoreCase(request.getRequestURI())) &&
				request.getParameter("successPage") != null
			) {
			//check if TSAPROMO is coming from a targeted page that is NOT siteaccess, and we're on siteaccess
			String sp = URLDecoder.decode(request.getParameter("successPage").toString(), "UTF-8");
			if (sp.indexOf("TSAPROMO") != -1) {
				String pairs[] = sp.replace("?", "&").split("&");

			    for (String pair : pairs) {
					 String name = null;
					 String value = null;
					 int pos = pair.indexOf("=");
					 if (pos == -1) {
						continue; //not a valid key=val pair, ignore
					 } else {
						try {
							name = URLDecoder.decode(pair.substring(0, pos), "UTF-8");
							value = URLDecoder.decode(pair.substring(pos+1, pair.length()), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// Not really possible, throw unchecked
						    throw new IllegalStateException("ad_server.jsp: No UTF-8");
						}
					}
					if ("TSAPROMO".equalsIgnoreCase(name) && value != null) {
						queryString.addParam("TSAPROMO", value);
						break; //found, we're done
					}
			    }
			}
		}
		if(request.getParameter("apc") != null) {
			queryString.addParam("apc", request.getParameter("apc"));
		}
		//APPDEV-2500 - add subtotal to oas query string
		if(user != null) {
			queryString.addParam("sub", user.getShoppingCart().getSubTotal() + "");
		}

		if (user != null) {
			queryString.addParam("mobWeb", (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"))) + "");
		}
		
		//Sending RAF promo code to OAS, to target different ads based on the promo code.
		if(request.getParameter("raf_promo_code")!= null){
			queryString.addParam("raf_promo_code", request.getParameter("raf_promo_code"));
		}

		String sitePage = request.getAttribute("sitePage") == null ? "www.freshdirect.com"
				: (String) request.getAttribute("sitePage");
		String listPos = request.getAttribute("listPos") == null ? "SystemMessage"
				: (String) request.getAttribute("listPos");

		String[] listPosArray = listPos.split(",");%>


<%
	if (!FDStoreProperties.isDfpEnabled()) {
%>
  <!-- OAS SETUP begin -->
  <script type="text/javascript">
  protocol = 'http://';
  if(document.location.href.substring(0,5) == 'https'){
  	protocol = 'https://';
  }

  //configuration
  OAS_url =  protocol + '<%=FDStoreProperties.getAdServerUrl()%>';
  OAS_sitepage = '<%=sitePage%>';
  OAS_listpos = '<%=listPos%>';
  OAS_query = '<%=queryString.toString()%>';
  OAS_target = '';
  //end of configuration

  OAS_version = 10;
  OAS_rn = '001234567890'; OAS_rns = '1234567890';
  OAS_rn = new String (Math.random()); OAS_rns = OAS_rn.substring (2, 11);
  function OAS_NORMAL(pos) {
    document.write('<A HREF="' + OAS_url + 'click_nx.ads/' + OAS_sitepage + '/1' + OAS_rns + '@'
  + OAS_listpos + '!' + pos + '?' + OAS_query + '" TARGET=' + OAS_target + '>');
    document.write('<IMG SRC="' + OAS_url + 'adstream_nx.ads/' + OAS_sitepage + '/1' + OAS_rns +
  '@' + OAS_listpos + '!' + pos + '?' + OAS_query + '" BORDER=0><\/A>');
  }
  function OAS_RICH(pos) {
  }
  </script>
  <%
  	if (!FDStoreProperties.getAdServerUsesDeferredImageLoading()) {
  %>
  <script type="text/JavaScript">
  OAS_version = 11;
  if ((navigator.userAgent.indexOf('Mozilla/3') != -1) ||
    (navigator.userAgent.indexOf('Mozilla/4.0 WebTV') != -1))
  	OAS_version = 10;

  if (OAS_version >= 11)
    document.write('<scr' + 'ipt type="text/javascript" src="' + OAS_url + 'adstream_mjx.ads/' +
  OAS_sitepage + '/1' + OAS_rns + '@' +
  OAS_listpos + '?' + OAS_query + '"><\/script>');
  </script>
  <script type="text/javascript">
  document.write('');
  function OAS_AD(pos) {
  	if (OAS_version >= 11)
  		OAS_RICH(pos);
  	else
  		OAS_NORMAL(pos);
  		if(typeof window.parent['OAS_DONE'] =='function') { OAS_DONE(pos); }
  }
  </script><%
  	}

  		if (FDStoreProperties.getAdServerUsesDeferredImageLoading()) {
  %><iframe name="oasif" id="oas_IF" width="1" height="1" src="about:blank" style="visibility: hidden; border: 0; position: absolute; top: 1px; left: 1px;"></iframe>
  <script type="text/javascript">
  OAD_POS = OAS_listpos.split(/,/);


  function createOASFrame() {
  	// write static content to iframe
  	var ifr = document.getElementById('oas_IF');
  	var idoc;
  	if (ifr.contentWindow) {
  	  // IE way
  	  idoc = ifr.contentWindow.document;
  	} else if (ifr.contentDocument) {
  	  // Mozilla way
  	  idoc = ifr.contentDocument.document ? ifr.contentDocument.document : ifr.contentDocument;
  	}

  	// Inject IFRAME content
  	idoc.open();
  	idoc.writeln('<html lang="en-US" xml:lang="en-US"><body>');

  	// modified document.write function
  	idoc.writeln("<scr" + "ipt type='text/javascript'>");
  	idoc.writeln("document._fragment='';");
  	idoc.writeln("document.fwrite=function(str){document._fragment+=str};");
  	idoc.writeln("document._write=document.write;");

      idoc.writeln("var ads_done = [];");
      idoc.writeln("var tries = 2;");
  <%for (int k = 0; k < listPosArray.length; k++) {%>
      idoc.writeln("ads_done['<%=listPosArray[k]%>'] = false;");
  <%}%>

      idoc.writeln("function copy_ad(oas_id) {");
      idoc.writeln("  var done=false; document._fragment='';");
      idoc.writeln("  var OAS_RICH=OAS_RICH || function() {};");
      idoc.writeln("  document.write=document.fwrite;OAS_RICH(oas_id);document.write=document._write;");
      idoc.writeln("  document.getElementById(oas_id).innerHTML = document._fragment;");
      idoc.writeln("  var pdiv = window.parent.document.getElementById('OAS_' + oas_id);");
      idoc.writeln("  if (pdiv) {");
      idoc.writeln("    pdiv.innerHTML = document.getElementById(oas_id).innerHTML;done=true;");
      idoc.writeln("    if(typeof window.parent['OAS_DONE'] =='function') { window.parent.OAS_DONE(oas_id); }");
      idoc.writeln("  }");
      idoc.writeln("  return done;");
      idoc.writeln("}");

      idoc.writeln("function copy_ads() {");
      idoc.writeln("  var k = 0;");
  <%for (int k = 0; k < listPosArray.length; k++) {%>
      idoc.writeln("  if (ads_done['<%=listPosArray[k]%>'] == true) {");
      idoc.writeln("    ++k;");
      idoc.writeln("  } else if (copy_ad('<%=listPosArray[k]%>')) {");
      idoc.writeln("    ads_done['<%=listPosArray[k]%>'] = true;");
      idoc.writeln("    ++k;");
      idoc.writeln("  }");
  <%}%>
      idoc.writeln("  return (k == <%=listPosArray.length%>);");
      idoc.writeln("}");

      idoc.writeln("function do_copy() {");
      idoc.writeln("  if (tries > 0 && copy_ads() == false) {");
      idoc.writeln("    --tries;");
      idoc.writeln("    window.setTimeout(do_copy, 200);");
      idoc.writeln("  }");
      idoc.writeln("}");


  	idoc.writeln("<\/script>");

  	// Put placeholder DIVs
  <%for (int k = 0; k < listPosArray.length; k++) {%>
  	idoc.writeln("<div id='<%=listPosArray[k]%>'><\/div>");
  <%}%>

  	// bootstrap loader
  	idoc.writeln('<scr' + 'ipt type="text/javascript" src="' + OAS_url + 'adstream_mjx.ads/' +
  	  OAS_sitepage + '/1' + OAS_rns + '@' +
  	  OAS_listpos + '?' + OAS_query + '" defer="defer"><\/script>');

      // handlers
  	idoc.writeln("<scr" + "ipt type='text/javascript' defer='defer'>");
  	idoc.writeln("do_copy();");
  	idoc.writeln("<\/script>");
  	idoc.writeln("<\/body><\/html>");
  	idoc.close();
  }

  // detect Safar 2 or older
  var m=navigator.appVersion.match(/Safari\/(\d+)/);
  var isOldSafari = (m && Number(m[1]) < 500);

  if (isOldSafari) {
      // delayed iframe creation for Safari 2
      document.onDocumentLoaded = createOASFrame;
  } else {
      createOASFrame();
  }


  function OAS_AD(pos) {
  	document.writeln('<div id="oas_' + pos + '"><\/div>');
  }

  // document.onDocumentLoaded = createOASIFrame;
  </script>
  <%
  	} //
  %>
  <!-- OAS SETUP end -->

  <%
  } else {
  %>
    <script>
    window.FreshDirect = window.FreshDirect || {};
    var fd = window.FreshDirect;

    fd.properties = fd.properties || {};
    fd.properties.isDFPEnabled = <%= FDStoreProperties.isDfpEnabled() ? true : false %>;
    fd.properties.dfpId = '<%= FDStoreProperties.getDfpId() %>';

    DFP_query = '<%=queryString.toString()%>';

    function OAS_AD(pos) {
    }

    </script>
  <%
    }
  %>


  <%
  	} else {
  %>
  <script type="text/javascript">
  function OAS_AD(pos) {
  }
  </script>
  <%
  	}
  %>
  <script>
  function OAS_DONE(oas_id) {
  	var e = document.getElementById('oas_'+oas_id);
  	if(window.jQuery && e) {
  		jQuery(document).ready(function() {
  			if (FreshDirect && FreshDirect.updateOAS && FreshDirect.updateOAS.done) {
  	  			FreshDirect.updateOAS.done([oas_id]);
  	  		}
  		});
  		jQuery(e).trigger('OAS_DONE',[oas_id]);
  		
  	}
  }

  </script>
