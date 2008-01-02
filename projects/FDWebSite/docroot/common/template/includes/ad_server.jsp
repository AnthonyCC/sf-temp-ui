<%@ page import='com.freshdirect.framework.util.NVL' %>
<%@ page import='org.apache.commons.lang.StringUtils' %>
<%@ page import='java.util.Map'%>
<%@ page import='com.freshdirect.customer.ErpSaleInfo' %>
<%@ page import='com.freshdirect.customer.EnumDeliveryType' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI' %>
<%@ page import='com.freshdirect.fdstore.customer.FDOrderI' %>
<%@ page import='com.freshdirect.fdstore.customer.FDCartLineI' %>
<%@ page import='com.freshdirect.fdstore.customer.ProfileModel' %>
<%@ page import='com.freshdirect.customer.EnumDeliveryType' %>
<%@ page import='com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor' %>
<%@ page import='com.freshdirect.fdstore.customer.FDCustomerManager' %>
<%@ page import='com.freshdirect.fdstore.customer.FDProductSelectionI' %>
<%@ page import='com.freshdirect.fdstore.customer.FDProductCollectionI' %>
<%@ page import='com.freshdirect.fdstore.content.ContentFactory' %>
<%@ page import='com.freshdirect.common.customer.EnumServiceType' %>
<%@ page import='com.freshdirect.framework.conf.FDRegistry' %>
<%@ page import='com.freshdirect.cms.application.service.*' %>
<%@ page import='com.freshdirect.framework.util.NVL' %>
<%@ page import='java.net.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName' %>
<%@ page import='com.freshdirect.deliverypass.EnumDlvPassStatus' %>
<%@ page import='com.freshdirect.deliverypass.EnumDlvPassProfileType' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.DeliveryPassUtil' %>
<%@ page import='com.freshdirect.framework.util.DateUtil' %>
<%@ page import='com.freshdirect.framework.util.StringUtil' %>
<%@ page import='com.freshdirect.framework.util.QueryStringBuilder' %>
<%!

private final static String CCL_NONELIGIBLE  = "0";
private final static String CCL_INEXPERIENCED = "1";
private final static String CCL_EXPERIENCED   = "2";

private String cclExperienceLevel(FDUserI user) {
   if (!user.isCCLEnabled()) return CCL_NONELIGIBLE;
   if (user.isCCLInExperienced()) return CCL_INEXPERIENCED;
   return CCL_EXPERIENCED;
}

%>
<%
if(FDStoreProperties.isAdServerEnabled()){

	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	
     QueryStringBuilder queryString = new QueryStringBuilder();
     if(user!=null)
     {
     
    
            ResourceInfoServiceI resourceService = (ResourceInfoServiceI) FDRegistry.getInstance().getService(ResourceInfoServiceI.class);
            String storeVersion = resourceService.getPublishId();
        
            String metalRating = "";
            int vip = 0;
            int chefsTable = 0;
            ProfileModel profile = null;
            if(user.getIdentity() != null){
                profile = user.getFDCustomer().getProfile();
                metalRating = profile.getCustomerMetalType();
                vip = profile.isVIPCustomer()? 1 : 0 ;
                chefsTable = profile.isChefsTable()? 1 : 0 ;
            }
        
            String type = "";
            String depotAffil = "";
            EnumServiceType service = user.getSelectedServiceType();
            if(EnumServiceType.HOME.equals(service)){
                    type = "home";
            } else if(EnumServiceType.DEPOT.equals(service)){
                    type = "depot";
                    depotAffil = user.getDepotCode();
            } else if(EnumServiceType.PICKUP.equals(service)){
                    type = "pickup";
            } else if(EnumServiceType.CORPORATE.equals(service)){
                    type = "cos";
            }
            //Commented out as part of PERF-22            
            /*
            String lastOrderDate = "";
            String lastOrderType = "";
            String lastOrderZone = "";

            ErpSaleInfo lastOrder = user.getOrderHistory().getLastSale();
            if(lastOrder != null){
                lastOrderDate = lastOrder.getRequestedDate().toString();
                lastOrderType  = lastOrder.getDeliveryType().getCode().toLowerCase();
                lastOrderZone = lastOrder.getZone();
            }
            */
            
            Date dlvDate = user.getOrderHistory().getLastOrderDlvDate();
            String lastOrderDate = (dlvDate != null) ? dlvDate.toString() : "";
            EnumDeliveryType orderType = user.getOrderHistory().getLastOrderType();
	    String lastOrderType = orderType != null ? orderType.getCode().toLowerCase() : "";
	    String orderZone = user.getOrderHistory().getLastOrderZone();
	    String lastOrderZone = orderZone != null ? orderZone : "";

            // Set of String (product department Ids, "rec" for recipe items)
            Set cartDeptIds = new HashSet();
            for(Iterator i = user.getShoppingCart().getOrderLines().iterator(); i.hasNext();){
                FDCartLineI cartLine = (FDCartLineI)i.next();
                if (cartLine.getRecipeSourceId() != null) {
                    cartDeptIds.add("rec");
                } else {
                    cartDeptIds.add( cartLine.lookupProduct().getDepartment().getContentName() );
                }
            }

            // Set of product deparment ids made from CCL list contents
	    // The "loadedCclList" request attribute is set by the QuickShop controller tag (when
	    // list items are loaded)
            Set ccListDeptIds = new HashSet();
	    FDProductCollectionI ccList = (FDProductCollectionI)request.getAttribute("loadedCclList");
	    if (ccList != null) {
               for(Iterator i = ccList.getProducts().iterator(); i.hasNext(); ) {
	          FDProductSelectionI productSelection = (FDProductSelectionI)i.next();
                  if (productSelection.getRecipeSourceId() != null) {
                     ccListDeptIds.add("rec");
                  } else {
                     ccListDeptIds.add( productSelection.lookupProduct().getDepartment().getContentName() );
                  }
               }
	    }
            
            String pageId = "";
            if(request.getParameter("deptId") != null) pageId = request.getParameter("deptId");
            if(request.getParameter("catId") != null) pageId = request.getParameter("catId");
            if(request.getParameter("subCatId") != null) pageId = request.getParameter("subCatId");
            if(request.getParameter("productId") != null) pageId = request.getParameter("productId");
            if(request.getParameter("recipeId") != null) pageId = request.getParameter("recipeId");
            
            String brand = "";
            if(request.getParameter("brandValue") != null) brand = request.getParameter("brandValue");
            
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
            pages.put("/your_account/manage_account.jsp", "manage_account");
            pages.put("/login/login.jsp", "login");
            pages.put("/search.jsp", "search");
            pages.put("/help/index.jsp", "help");
            pages.put("grocery_cart_confirm.jsp", "gconfirm");
            pages.put("/pay_by_check.jsp","pay_by_check_popup");
            pages.put("/your_account/payment_information.jsp","payment_info");
            pages.put("/recipe_search.jsp", "recipe_search");

            String pageType = "";
            String uri = request.getRequestURI().toLowerCase();
            for(Iterator ptIter = pages.entrySet().iterator();ptIter.hasNext();){
                Map.Entry e = (Map.Entry) ptIter.next();
                String pattern = (String) e.getKey();
                String value = (String) e.getValue();
                
                if(uri.indexOf(pattern) > -1){
                    pageType = value;
                    break;
                }
            }	
            
            queryString.
	    	addParam("v",metalRating).
	    	addParam("hv",vip).
	    	addParam("ct",chefsTable).
	    	addParam("sv",storeVersion).
	    	addParam("zip",user.getZipCode()).
	    	addParam("type",type).
	    	addParam("depot",depotAffil).
	    	addParam("nod",lastOrderDate).
	    	addParam("do",user.getOrderHistory().getValidOrderCount()).
	    	addParam("win",2);

            if (cartDeptIds.size() > 0) {
	       StringBuffer cartString = new StringBuffer();
	       for(Iterator i=cartDeptIds.iterator(); i.hasNext(); ) {
	           cartString.append(i.next());
	           if (i.hasNext()) cartString.append(':');
               }
	       queryString.addParam("cart",cartString);
	    }

	    if (ccListDeptIds.size() > 0) {
	        StringBuffer listString = new StringBuffer();
	        for(Iterator i=ccListDeptIds.iterator(); i.hasNext(); ) {
	            listString.append(i.next());
	            if (i.hasNext()) listString.append(':');
		}
	        queryString.addParam("list",listString);
            }

	    queryString.
	    	addParam("lu",cclExperienceLevel(user)).
	    	addParam("pt",pageType).
	    	addParam("id",pageId).
	    	addParam("brand",brand).
	    	addParam("lotype",lastOrderType).
	    	addParam("lozn",lastOrderZone).
	    	addParam("ecp",user.isCheckEligible() ? 1 : 0 ).
	    	addParam("ecpoc",user.getOrderHistory().getValidECheckOrderCount()).
	    	addParam("county",NVL.apply(user.getDefaultCounty(), "")).
	    	addParam("ref_prog_id",NVL.apply(user.getLastRefProgId(), "")).
	    	addParam("oim",user.isReceiveFDEmails() ? 1 : 0).
	    	addParam("recipe",true);
            
            if (profile!=null) {
		queryString.addParam("ecppromo",NVL.apply(profile.getEcpPromo(), ""));
            } else {
		queryString.addParam("ecppromo","");
            }
            
            String extraProps = FDStoreProperties.getExtraAdServerProfileAttributes();
            StringTokenizer tokenizer = new StringTokenizer(extraProps, ",");
                
            while(tokenizer.hasMoreTokens()){
                String tok = tokenizer.nextToken();
                String[] pairs = tok.split("=");
                
                if(pairs.length != 2){
                    continue;
                }
                

		queryString.addParam(pairs[1],profile != null ? NVL.apply(profile.getAttribute(pairs[0]), "") : null);

            }
	    //Building up the values required for Delivery Pass Ads.
	    if(user.isEligibleForDeliveryPass()){
	        String profileVal = user.getDlvPassProfileValue();

		queryString.addParam("dpas",profileVal);
	        
	        String dprem = null;
	        String dpused = null;
	        boolean expired = false;
	        EnumDlvPassStatus status = user.getDeliveryPassStatus();
	        if(DeliveryPassUtil.isEligibleStatus(status)&& (user.isDlvPassExpired()==false)) {
	            //Eligible to Buy. But not purchased.
	            dprem = "n";
	            dpused = "n";
	        } else {
	            //Delivery pass purchased. Account Exists.
	            dprem = String.valueOf(user.getDlvPassInfo().getRemainingCount());
	            dpused = String.valueOf(user.getDlvPassInfo().getUsedCount());
	        }
	        
	        if(user.getEligibleDeliveryPass() == EnumDlvPassProfileType.BSGS) {
	            //If BSGS pass then pass the remaining count.
		    queryString.addParam("dpr",dprem);
	        }else {
	            //If UNLIMITED pass then pass the used count if not expired.
	            if(user.isDlvPassExpired()) {
			queryString.addParam("expd",'y');
	            }
                  else if( (user.getDlvPassInfo()!=null) && user.getDlvPassInfo().isUnlimited()==false) {
	      	      //If BSGS pass then pass the remaining count.
				dprem = String.valueOf(user.getDlvPassInfo().getRemainingCount());
		            queryString.addParam("dpr",dprem);
		
                  }
			else if(user.getUsableDeliveryPassCount()>0) {
	                //Not Purchased yet or Purchased not expired.
	            	int days=DateUtil.getDiffInDays(user.getDlvPassInfo().getExpDate(), new Date());
			queryString.
				addParam("dpu",dpused).
				addParam("expd",days);
	            }
	            else {
			queryString.addParam("dpu",dpused);
	            }
	        }
	    }
        }
        else        
        {
             if(request.getAttribute("RefProgId")!=null) {
		queryString.addParam("ref_prog_id",NVL.apply((String)request.getAttribute("RefProgId"), ""));
             }    
        }
        
	String sitePage = request.getAttribute("sitePage") == null ? "www.freshdirect.com" : (String) request.getAttribute("sitePage");
	String listPos = request.getAttribute("listPos") == null ? "SystemMessage" : (String) request.getAttribute("listPos");
	
%>

<!------ OAS SETUP begin ------>
<SCRIPT LANGUAGE=JavaScript>
<!--
protocol = 'http://';
if(document.location.href.substring(0,5) == 'https'){
	protocol = 'https://';
}

//configuration
OAS_url =  protocol + '<%= FDStoreProperties.getAdServerUrl() %>';
OAS_sitepage = '<%=sitePage%>';
OAS_listpos = '<%=listPos%>';
OAS_query = '<%= queryString.toString() %>';
OAS_target = '';
//end of configuration

OAS_version = 10;
OAS_rn = '001234567890'; OAS_rns = '1234567890';
OAS_rn = new String (Math.random()); OAS_rns = OAS_rn.substring (2, 11);
function OAS_NORMAL(pos) {
  document.write('<A HREF="' + OAS_url + 'click_nx.ads/' + OAS_sitepage + '/1' + OAS_rns + '@'
+ OAS_listpos + '!' + pos + '?' + OAS_query + '" TARGET=' + OAS_target + '>');
  document.write('<IMG SRC="' + OAS_url + 'adstream_nx.ads/' + OAS_sitepage + '/1' + OAS_rns +
'@' + OAS_listpos + '!' + pos + '?' + OAS_query + '" BORDER=0></A>');
}
//-->
</SCRIPT>
<% if (!FDStoreProperties.getAdServerUsesDeferredImageLoading()) { %><SCRIPT LANGUAGE=JavaScript1.1>
<!--
OAS_version = 11;
if ((navigator.userAgent.indexOf('Mozilla/3') != -1) ||
  (navigator.userAgent.indexOf('Mozilla/4.0 WebTV') != -1))
	OAS_version = 10;  
  
if (OAS_version >= 11)
  document.write('<SCR' + 'IPT LANGUAGE=JavaScript1.1 SRC="' + OAS_url + 'adstream_mjx.ads/' +
OAS_sitepage + '/1' + OAS_rns + '@' +
OAS_listpos + '?' + OAS_query + '"><\/SCRIPT>');//-->
</SCRIPT>
<SCRIPT LANGUAGE=JavaScript>
<!--
document.write('');
function OAS_AD(pos) {
	if (OAS_version >= 11)
		OAS_RICH(pos);
	else
		OAS_NORMAL(pos);
}
//-->
</SCRIPT><% } // ! %>
<% if (FDStoreProperties.getAdServerUsesDeferredImageLoading()) { %><SCRIPT type="text/javascript">
OAD_POS = [];

function OAS_AD(pos) {
	OAD_POS.push(pos);
	document.writeln('<div id="OAS_' + pos + '">&nbsp;</div>');
}

document.onDocumentLoaded = function() {
  var ifrm = this.getElementById('OAS_IF');
  ifrm = (ifrm.contentWindow) ? ifrm.contentWindow : (ifrm.contentDocument.document) ? ifrm.contentDocument.document : ifrm.contentDocument
  ifrm.document.open();
  ifrm.document.writeln("<BODY>");

  var p;
  for (p in OAD_POS) {
  	  ifrm.document.writeln("<DIV ID='" + OAD_POS[p] + "'></DIV>");
  }

  ifrm.document.writeln('<SCR' + 'IPT TYPE="text/javascript" SRC="' + OAS_url + 'adstream_mjx.ads/' +
	OAS_sitepage + '/1' + OAS_rns + '@' +
	OAS_listpos + '?' + OAS_query + '" defer="defer"><\/SCRIPT>');


  ifrm.document.writeln("<SCR" + "IPT TYPE='text/javascript'>");
  ifrm.document.writeln("document._fragment='';");
  ifrm.document.writeln("document.fwrite=function(str){document._fragment+=str};");
  ifrm.document.writeln("document._write=document.write;");
  ifrm.document.writeln("<\/SCRIPT>");

  ifrm.document.writeln("<SCR" + "IPT TYPE='text/javascript' defer='defer'>");
  for (p in OAD_POS) {
	  ifrm.document.writeln("document._fragment='';");
	  ifrm.document.writeln("document.write=document.fwrite;OAS_RICH('" + OAD_POS[p] + "');document.write=document._write;");
	  ifrm.document.writeln("document.getElementById('" + OAD_POS[p] + "').innerHTML = document._fragment;");
      ifrm.document.writeln("var pdiv = window.parent.document.getElementById('OAS_" + OAD_POS[p] + "');");
      ifrm.document.writeln("if(pdiv){pdiv.innerHTML = document.getElementById('" + OAD_POS[p] + "').innerHTML;}");
  }
  ifrm.document.writeln("<\/SCRIPT>");
  ifrm.document.writeln("</BODY>");
  ifrm.document.close();
}
</SCRIPT>
<IFRAME ID="OAS_IF" WIDTH="1" HEIGHT="1" SRC="about:blank" STYLE="display: none;"></IFRAME><% } //  %>
<!------ OAS SETUP end ------>
<%
}
%>
