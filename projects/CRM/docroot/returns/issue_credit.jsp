<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.*" %>
<%@ page import="com.freshdirect.fdstore.content.*"%>
<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.framework.core.*" %>
<%@ page import="com.freshdirect.framework.mail.*" %>
<%@ page import="com.freshdirect.framework.xml.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.callcenter.ComplaintUtil" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.framework.util.MathUtil" %>
<%@ page import="com.freshdirect.fdstore.deliverypass.DeliveryPassUtil" %>
<%@ page import="com.freshdirect.ErpServicesProperties"%>
<%@ page import="com.freshdirect.crm.CrmCaseConstants" %>
<%@ page import="com.freshdirect.webapp.util.JSHelper" %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%
String errorLineColor = "#FFCCCC";
String bgcolor="#CCCCFF";
ContentFactory contentfactory = ContentFactory.getInstance();

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

// Include YUI selector and buttons API
request.setAttribute("__yui_load_selector__", Boolean.TRUE);
request.setAttribute("__yui_load_buttons__", Boolean.TRUE);

String orderId = (String) request.getParameter("orderId");

boolean isClassicView = "dept".equalsIgnoreCase(request.getParameter("view"));
%>
<tmpl:insert template='/template/top_nav_changed_dtd.jsp'>
<tmpl:put name='title' direct='true'>Order <%= orderId%> Issue Credits</tmpl:put>
<fd:GetOrder id='orderI' saleId='<%= orderId %>'>
<%
    FDOrderAdapter order = (FDOrderAdapter) orderI;
    ErpPaymentMethodI paymentMethod = order.getPaymentMethod();
    Collection appliedCredits = order.hasInvoice() ? order.getActualAppliedCredits() : order.getAppliedCredits(); 
    String action = NVL.apply(request.getParameter("actionName"), "submit");
    boolean isGiftCardOrder = (order.getSale().getType().getSaleType().equalsIgnoreCase("GCD"))?true:false;    
    final boolean noFreeGrouping = order.getComplaintGroupingFashion() != FDOrderAdapter.IC_FREE2GROUP;
    if (noFreeGrouping) {
        // it is already decided so ignore / override URL parameter value
    	isClassicView = order.getComplaintGroupingFashion() == FDOrderAdapter.IC_GROUP_BY_DEPTS;
    }
%>
<fd:GetGiftCardPurchased id="recipients" saleId='<%= orderId %>'>
<fd:ComplaintGrabber order="<%= order %>" complaints="complaints" lineComplaints="lineComplaints" deptComplaints="deptComplaints" miscComplaints="miscComplaints" fullComplaints="fullComplaints" restockComplaints="restockComplaints" retrieveApproved="true">
<fd:CreateComplaint result="createComplaintResult" newComplaint="newComplaint" emailPreview="emailPreview">
<fd:IssueCredits action="<%=action%>" result='issueCreditResult' successPage='<%= "issue_credit_confirm.jsp?orderId=" + orderId %>' complaintModel='<%= newComplaint %>'>
<%  
    ActionResult result = new ActionResult();
    for (Iterator i = createComplaintResult.getErrors().iterator(); i.hasNext(); ) {
        result.addError( (ActionError) i.next() );
    }
    
    for (Iterator i = issueCreditResult.getErrors().iterator(); i.hasNext(); ) {
        result.addError( (ActionError) i.next() );
    }
    
    // Orderline request Params
    
    // Miscellaneous request Params
    
    String [] miscAmount = request.getParameterValues("misc_credit_amount");
    String [] miscReason = request.getParameterValues("misc_credit_reason");
    String [] miscMethod = request.getParameterValues("misc_credit_method");
    
    String send_email = NVL.apply(request.getParameter("send_email"), EnumSendCreditEmail.SEND_ON_APPROVAL.getName());
    String custom_message = NVL.apply(request.getParameter("custom_message"), "");
    boolean agent_signature = "yes".equalsIgnoreCase(request.getParameter("agent_signature"));
%>

<tmpl:put name='content' direct='true'>
<%@ include file="/includes/order_nav.jspf"%>
<%@ include file="/includes/order_summary.jspf"%>
<script type="text/javascript">
    function preventCreditIssue() {
        $('form-issue-credit').do_issue_credit.value='false';
    }
    function setPreviewEmailOn() {
        $('form-issue-credit').previewEmail.value='yes';
        $('form-issue-credit').actionName.value='preview';
        $('form-issue-credit').submit();
    }
    
    function setPreviewEmailOff() {
        $('form-issue-credit').previewEmail.value='no';
        $('form-issue-credit').actionName.value='preview';
        $('form-issue-credit').submit();
    }
    
    function setEditReturnQty(i){
        if (i ==0 && !$('form-issue-credit').ol_credit_qty_changed[0]) {
           fld = $('form-issue-credit').ol_credit_qty_changed;
        } else {
            fld = $('form-issue-credit').ol_credit_qty_changed[i];
        }
        fld.value = 'Q';
    }
</script>
<% if (!isClassicView) { %>
<%-- Group controls script --%>
<script type="text/javascript">
	var ISGroupControl = function(container, cartonGroup, bgColor, firstIndexIsOne) {
		/**
		 * Container table, actually 'tbl_order_detail'
		 */
		this.mainObj = $(container);
	
		/**
		 * Carton group identifier
		 */
		this.cartonGroup = cartonGroup;

		this.bgColor = bgColor;
		
		/**
		 * Starts option index from 1 (==true) or from 0 (==false)
		 */
		this.firstIndexIsOne = firstIndexIsOne;
	
		var self = this;
		this.init = function(groupSelector) {
			var _gs = $(groupSelector);		
	
			// attach change listener to group SELECT
			$E.on(_gs, 'change', this.onGroupSelectorChange);
			
			// single selectors in carton group
			var selectors = YAHOO.util.Selector.query('select[cartonGroup="'+self.cartonGroup+'"]' , self.mainObj, false);
			for (i=0; i<selectors.length; i++) {
				$E.on(selectors[i], 'change', function(e) {
					_gs.value = _gs.options[0].value;
					// this.style.backgroundColor = '';

					var objs = YAHOO.util.Selector.query('select[cartonGroup="'+self.cartonGroup+'"]' , self.mainObj, false);
					for (i=0; i<objs.length; i++) {
						objs[i].style.backgroundColor = '';
					}
				});
			}
		};
		
		this.onGroupSelectorChange = function(e) {
			var i, j;
		
			if (this.selectedIndex > 0) {
				var selVal = this.options[this.selectedIndex].text;
		
				var aSelect;

				if ('RESET' == selVal) {
					var objs = YAHOO.util.Selector.query('select[cartonGroup="'+self.cartonGroup+'"]' , self.mainObj, false);
					for (i=0; i<objs.length; i++) {
						objs[i].value = objs[i].options[0].value;
						if (objs[i].onchange)
							objs[i].onchange();
						objs[i].style.backgroundColor = '';
					}
					this.value = this.options[0].value;
				} else {
					var objs = YAHOO.util.Selector.query('select[cartonGroup="'+self.cartonGroup+'"]' , self.mainObj, false);
					for (i=0; i<objs.length; i++) {
						aSelect = objs[i];
						aSelect.style.backgroundColor = self.bgColor;
			
						for (j = (this.firstIndexIsOne?1:0); j<aSelect.options.length; j++) {
							if (selVal == aSelect.options[j].text) {
								aSelect.value = aSelect.options[j].value;
								break;
							}
						}
					}
				}
			}
		};
	};
</script>
<% } %>
<%-- Dirty Form script --%>
<script type="text/javascript">
	var checkFormDirty = function(e) {
		if ($('form-issue-credit').isDirty) {
			return confirm("The form has been modified. If you change the view your settings will be lost. Are you sure you want to do this?");
		}

		return true;		
	};
</script>
<script type="text/javascript">
var OL_PRICES = {};
<%
	for (Iterator it=order.getOrderLines().iterator(); it.hasNext();) {
		FDCartLineI cl = (FDCartLineI) it.next();
		double amt;
		if (cl.hasInvoiceLine()) {
			amt = cl.getInvoiceLine().getPrice();
		} else {
			amt = cl.getPrice();
		}
%>OL_PRICES['<%= cl.getOrderLineId() %>'] = <%= amt %>;
<%
	}
%></script>
<%String [] checkedErrors = {"technical_difficulty", "general_error_msg", "invalid_complaint", "payment_method_type", "no_more_refunds"};%>
<fd:ErrorHandler result="<%=result%>" field="<%=checkedErrors%>" id='errorMsg'>
  <div class="error_detail"><%= errorMsg %></div>
</fd:ErrorHandler>

<div class="content_scroll" style="height: 72%; padding-top: 0px;">
<form id="form-issue-credit" "name="issue_credit" method="POST" action="issue_credit.jsp?orderId=<%= orderId %>&view=<%= isClassicView ? "dept" : "cartons" %>">
<input type="hidden" name="orig_sale_id" value="<%= orderId %>">
<input type="hidden" name="do_issue_credit" value="true">
<input type="hidden" name="previewEmail" value="no">
<input type="hidden" name="actionName" value="submit">
<TABLE id="tbl_order_detail" "WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order_detail">
    <tr class="list_header">
    	<% if(isGiftCardOrder){ %>
        <td WIDTH="6%" COLSPAN="2" class="list_header_detail"><b>Order Qty.</b></td>
        <td WIDTH="20%" class="list_header_detail"><b>Name</b></td>
        <td WIDTH="7%" class="list_header_detail"><b>Certificate Number</b></td>
        <td WIDTH="8%" class="list_header_detail"><b>Givex Number</b></td>
        <td WIDTH="9%" class="list_header_detail"><b>Recipient Name</b></td>
        <td WIDTH="6%" class="list_header_detail"><b>Price</b></td>
        <td WIDTH="6%" class="list_header_detail"><b>Orig. Total</b></td>
        <%} else{ %>    
        <td WIDTH="7%" COLSPAN="2" class="list_header_detail"><b>Order Qty.</b></td>
        <td WIDTH="23%" class="list_header_detail"><b>Name</b></td>
        <td WIDTH="9%" class="list_header_detail"><b>Price</b></td>
        <td WIDTH="7%" class="list_header_detail"><b>Orig. Total</b></td>
        <%} %>
        <td WIDTH="7%" COLSPAN="2" class="list_header_detail"><b>Prev. Qty. Returned</b></td>
        <td WIDTH="7%" ALIGN="CENTER" class="list_header_detail"><b>Prev. Credit</b></td>
        <td WIDTH="5%" ALIGN="CENTER" class="list_header_detail"><b>Current Total</b></td>
        <td WIDTH="5%" class="list_header_detail"><b>New Qty. Returned</b></td>
        <td WIDTH="17%" class="list_header_detail"><b>Credit Reason</b></td>
        <td WIDTH="8%" class="list_header_detail"><b>Credit Type</b></td>
        <td WIDTH="5%" ALIGN="CENTER" class="list_header_detail"><b>Credit Amt</b></td>
    </tr>
    <tr VALIGN="bottom">
    <% if(!isGiftCardOrder){ %>    
        <td COLSPAN="5"><span style="font-weight: bold;"><%= isClassicView ? "PRODUCTS" : "CARTONS" %></span></td>
        <td COLSPAN="7"><b>1. Enter product credits here:</b></td>
        <td ALIGN="RIGHT" style="white-space: nowrap; text-align: right;">
<%
	if (isClassicView) {
		if (noFreeGrouping) {
%>			<span style="color: gray;">Order by Cartons</span>
<%
		} else {
%>			<a href="<%= request.getRequestURI() %>?orderId=<%= orderId %>" onclick="return checkFormDirty();">Order by Cartons</a>
<%
		}
	} else {
		if (noFreeGrouping) {
%>			<span style="color: gray;">Order by Departments</span>
<%
		} else {
%>			<a href="<%= request.getRequestURI() %>?orderId=<%= orderId %>&view=dept" onclick="return checkFormDirty();">Order by Departments</a>
<%		}
	} %>
    <%} else{ %>
        <td WIDTH="45%" COLSPAN="8"><FONT CLASS="space2pix"><BR></FONT><b>PRODUCTS</b><BR></td>
        <td WIDTH="50%" COLSPAN="7"><b>1. Enter product credits here:</b></td>
        <%} %>
			<INPUT TYPE="submit" class="submit" value="update" onclick="preventCreditIssue()">
        </td>
    </tr>
<%

	Set multipleOrderlines = new HashSet();



    if (isClassicView) {
        // collect orderlines from cartons
		final Set orderLines = new HashSet(order.getOrderLines());
        final String cartNum = "default";
        
%>	<tr>
		<td WIDTH="100%" COLSPAN="13"><TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td WIDTH="100%" BGCOLOR="#CCCCCC"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr></TABLE></td>
	</tr>
<%
        // reset last department binding
		request.setAttribute("__is__lastdept", null);
		request.setAttribute("__is__multiples", multipleOrderlines);
		
		int i = 0; // orderline counter
		for (Iterator it2=order.getOrderLines().iterator(); it2.hasNext(); ) {
			FDCartLineI orderLine = (FDCartLineI) it2.next();
		
		    // bind variables
		    request.setAttribute("__issue_credit_orderline", orderLine);
		    request.setAttribute("__issue_credit_lineComplaints", lineComplaints);
		    request.setAttribute("__issue_credit_actionResult", result);
		    request.setAttribute("__issue_credit_cartonNumber", cartNum);
		    request.setAttribute("__issue_credit_paramIndex", new Integer(i));
	        request.setAttribute("__issue_credits_cartonDetail", null);
%>
<%@ include file="/returns/issue_credits_line.jspf" %>
<%
			i++;
		}
    } else {
		int i = 0; // orderline counter
		
		// select orderlines appearing multiple times in OL list
		Set olNames = new HashSet();
		for (Iterator it1=order.getCartonContents().iterator(); it1.hasNext(); ) {
	    	FDCartonInfo cInfo = (FDCartonInfo) it1.next();
	    	for (Iterator it2=cInfo.getCartonDetails().iterator(); it2.hasNext(); ) {
	    		FDCartLineI orderLine = ((FDCartonDetail) it2.next()).getCartLine();

	    		String olId = orderLine.getOrderLineId();
	    		if (olNames.contains(olId)) {
	    			multipleOrderlines.add(olId);
	    		} else {
	    			olNames.add(olId);
	    		}
	    	}
		}
	    	



		for (Iterator it1=order.getCartonContents().iterator(); it1.hasNext(); ) {
	    	FDCartonInfo cInfo = (FDCartonInfo) it1.next();
	    	final String cartNum = cInfo.getCartonInfo().getCartonNumber();
	    	
	        // collect orderlines from cartons
	        Set orderLines = new HashSet();
	    	for (Iterator it2=cInfo.getCartonDetails().iterator(); it2.hasNext(); ) {
	    		FDCartLineI orderLine = ((FDCartonDetail) it2.next()).getCartLine();
	    		orderLines.add(orderLine);
	    	}

	    	// bind values
	        request.setAttribute("__issue_credits_orderlines", orderLines);
	        request.setAttribute("__issue_credits_groupName", cartNum);
	        request.setAttribute("__issue_credits_cartonInfo", cInfo);
			request.setAttribute("__is__multiples", multipleOrderlines);
%>
<%@ include file="/returns/issue_credits_header.jspf" %>
<%
	    	
	    	
			// reset last department binding
			request.setAttribute("__is__lastdept", null);

			for (Iterator it2=cInfo.getCartonDetails().iterator(); it2.hasNext(); ) {
	    		FDCartonDetail cDetail = (FDCartonDetail) it2.next();
	    		
	    		FDCartLineI orderLine = cDetail.getCartLine();
	
		        // bind variables
		        request.setAttribute("__issue_credit_orderline", orderLine);
		        request.setAttribute("__issue_credit_lineComplaints", lineComplaints);
		        request.setAttribute("__issue_credit_actionResult", result);
		        request.setAttribute("__issue_credit_cartonNumber", cartNum);
		        request.setAttribute("__issue_credit_paramIndex", new Integer(i));
		        request.setAttribute("__issue_credits_cartonDetail", cDetail);
%>
<%@ include file="/returns/issue_credits_line.jspf" %>
<%
				i++;
	    	} // orderLines per carton
	    } // cartons
    }
%>
    <tr>
        <td WIDTH="100%" COLSPAN="13"><TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td WIDTH="100%" BGCOLOR="#CCCCCC"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr></TABLE></td>
    </tr>
    <%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ BEGIN DEPT TOTALS/CREDITS HEADER ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
    <tr VALIGN="bottom">
     <% if(!isGiftCardOrder){ %>    
        <td WIDTH="45%" COLSPAN="5"><FONT CLASS="space2pix"><BR></FONT><b>DEPARTMENTS</b><BR></td>
        <td WIDTH="50%" COLSPAN="7"><b>2. Enter department credits here:</b></td>
     <%} else{ %>
     	<td WIDTH="45%" COLSPAN="8"><FONT CLASS="space2pix"><BR></FONT><b>DEPARTMENTS</b><BR></td>
        <td WIDTH="50%" COLSPAN="7"><b>2. Enter department credits here:</b></td>
     <%} %>        
        <td WIDTH="5%" ALIGN="RIGHT">&nbsp;</td>
    </tr>
    <tr>
        <td WIDTH="100%" COLSPAN="13"><TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td WIDTH="100%" BGCOLOR="#CCCCCC"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr></TABLE></td>
    </tr>
    <%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ END DEPT TOTALS/CREDITS HEADER ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>

    <%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ BEGIN DEPT TOTALS/CREDITS LINES ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<%      int j = 0; %>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ GOODWILL ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<%      boolean goodwillErr = ( !result.isSuccess() && result.hasError("misc_error_0") ) ? true : false; %>
    <tr VALIGN="bottom" <%= (goodwillErr) ? "BGCOLOR='" + errorLineColor + "'" : "" %>>
        <td COLSPAN="2"><BR></td>
         <% if(!isGiftCardOrder){ %>        
        <td COLSPAN="3"><FONT CLASS="space2pix"><BR></FONT><b>Goodwill</b><BR></td>
        <%} else { %>
        <td COLSPAN="6"><FONT CLASS="space2pix"><BR></FONT><b>Goodwill</b><BR></td>
        <%} %>        
        <td BGCOLOR="<%= (goodwillErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><% if (goodwillErr) { %><FONT CLASS="text8redbold">*</font><% } %><BR></td>
        <td BGCOLOR="<%= (goodwillErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><BR></td>
        <td BGCOLOR="<%= (goodwillErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER">&nbsp;</td>
        <td BGCOLOR="<%= (goodwillErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER">&nbsp;</td>
        <td BGCOLOR="<%= (goodwillErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER">&nbsp;</td>
        <td BGCOLOR="<%= (goodwillErr) ? errorLineColor : bgcolor %>">
            <SELECT name="misc_credit_reason" class="pulldown_detail">
                <OPTION value="">select credit reason</OPTION>
                <fd:ComplaintReasons department='goodwill' reasons='reasons'>
                <logic:iterate id="reason" collection="<%= reasons %>" type="com.freshdirect.customer.ErpComplaintReason">
                <OPTION value="<%= reason.getId() %>" <%= ( miscReason != null && reason.getId().equals(miscReason[0]) ) ? "SELECTED" : "" %>><%= reason.getReason() %></OPTION>
                </logic:iterate>
                </fd:ComplaintReasons>
            </SELECT>
        </td>
        <td BGCOLOR="<%= (goodwillErr) ? errorLineColor : bgcolor %>">
            <SELECT NAME="misc_credit_method" class="pulldown_detail">
                <OPTION value="FDC" <%= ( miscMethod != null && "FDC".equals(miscMethod[0]) ) ? "SELECTED" : "" %>>store</OPTION>
                <OPTION value="CSH" <%= ( miscMethod != null && "CSH".equals(miscMethod[0]) ) ? "SELECTED" : "" %>>cash back</OPTION>
            </SELECT>
        </td>
        <td BGCOLOR="<%= (goodwillErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><INPUT TYPE="text" NAME="misc_credit_amount" VALUE="<%= newComplaint.getGoodwillCreditAmount() %>" SIZE="4"></td>
    </tr>

    <%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ TRANSPORTATION ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<%      boolean transportationErr = ( !result.isSuccess() && result.hasError("misc_error_1") ) ? true : false; %>
    <tr VALIGN="bottom" <%= (transportationErr) ? "BGCOLOR='" + errorLineColor + "'" : "" %>>
        <td COLSPAN="2"><BR></td>
        <% if(!isGiftCardOrder){ %>        
        <td COLSPAN="3"><FONT CLASS="space2pix"><BR></FONT><b>Transportation</b><BR></td>
        <%} else { %>
         <td COLSPAN="6"><FONT CLASS="space2pix"><BR></FONT><b>Transportation</b><BR></td>
         <% } %>
        <td BGCOLOR="<%= (transportationErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><%   if (transportationErr) { %><FONT CLASS="text8redbold">*</font><% } %><BR></td>
        <td BGCOLOR="<%= (transportationErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><BR></td>
        <td BGCOLOR="<%= (transportationErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER">&nbsp;</td>
        <td BGCOLOR="<%= (transportationErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER">&nbsp;</td>
        <td BGCOLOR="<%= (transportationErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER">&nbsp;</td>
        <td BGCOLOR="<%= (transportationErr) ? errorLineColor : bgcolor %>">
            <SELECT name="misc_credit_reason" class="pulldown_detail">
                <OPTION value="">select credit reason</OPTION>
                <fd:ComplaintReasons department='transportation' reasons='reasons'>
                <logic:iterate id="reason" collection="<%= reasons %>" type="com.freshdirect.customer.ErpComplaintReason"> 
                <OPTION value="<%= reason.getId() %>" <%= ( miscReason != null && reason.getId().equals(miscReason[1]) ) ? "SELECTED" : "" %>><%= reason.getReason() %></OPTION>
                </logic:iterate>
                </fd:ComplaintReasons>
            </SELECT>
        </td>
        <td BGCOLOR="<%= (transportationErr) ? errorLineColor : bgcolor %>">
            <SELECT NAME="misc_credit_method" class="pulldown_detail">
                <OPTION value="FDC" <%= ( miscMethod != null && "FDC".equals(miscMethod[1]) ) ? "SELECTED" : "" %>>store</OPTION>
                <OPTION value="CSH" <%= ( miscMethod != null && "CSH".equals(miscMethod[1]) ) ? "SELECTED" : "" %>>cash back</OPTION>
            </SELECT>
        </td>
        <td BGCOLOR="<%= (transportationErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><INPUT TYPE="text" NAME="misc_credit_amount" VALUE="<%= newComplaint.getTransportationCreditAmount() %>" SIZE="4"></td>
    </tr>

    <%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ EXTRA ITEM ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<%      boolean extraItemErr = ( !result.isSuccess() && result.hasError("misc_error_2") ) ? true : false; %>
    <tr VALIGN="bottom" <%= (extraItemErr) ? "BGCOLOR='" + errorLineColor + "'" : "" %>>
        <td COLSPAN="2"><BR></td>
         <% if(!isGiftCardOrder){ %>        
        <td COLSPAN="3"><FONT CLASS="space2pix"><BR></FONT><b>Extra Item</b><BR></td>
         <%} else { %>
         <td COLSPAN="6"><FONT CLASS="space2pix"><BR></FONT><b>Extra Item</b><BR></td>
         <% } %>        
        <td BGCOLOR="<%= (extraItemErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><%    if (extraItemErr) { %><FONT CLASS="text8redbold">*</font><% } %><BR></td>
        <td BGCOLOR="<%= (extraItemErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><BR></td>
        <td BGCOLOR="<%= (extraItemErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER">&nbsp;</td>
        <td BGCOLOR="<%= (extraItemErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER">&nbsp;</td>
        <td BGCOLOR="<%= (extraItemErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER">&nbsp;</td>
        <td BGCOLOR="<%= (extraItemErr) ? errorLineColor : bgcolor %>">
            <SELECT name="misc_credit_reason" class="pulldown_detail">
                <OPTION value="">select credit reason</OPTION>
                <fd:ComplaintReasons department='extraItem' reasons='reasons'>
                <logic:iterate id="reason" collection="<%= reasons %>" type="com.freshdirect.customer.ErpComplaintReason"> 
                <OPTION value="<%= reason.getId() %>" <%= ( miscReason != null && reason.getId().equals(miscReason[2]) ) ? "SELECTED" : "" %>><%= reason.getReason() %></OPTION>
                </logic:iterate>
                </fd:ComplaintReasons>
            </SELECT>
        </td>
        <td BGCOLOR="<%= (extraItemErr) ? errorLineColor : bgcolor %>">
            <SELECT NAME="misc_credit_method" class="pulldown_detail">
                <OPTION value="FDC" <%= ( miscMethod != null && "FDC".equals(miscMethod[2]) ) ? "SELECTED" : "" %>>store</OPTION>
                <OPTION value="CSH" <%= ( miscMethod != null && "CSH".equals(miscMethod[2]) ) ? "SELECTED" : "" %>>cash back</OPTION>
            </SELECT>
        </td>
        <td BGCOLOR="<%= (extraItemErr) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><INPUT TYPE="text" NAME="misc_credit_amount" VALUE="<%= newComplaint.getExtraItemCreditAmount() %>" SIZE="4"></td>
    </tr>

    <%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ END DEPT TOTALS/CREDITS LINES ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>

<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ BEGIN ORDER TOTALS SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<% if(!isGiftCardOrder){ %>
    <tr>
        <td WIDTH="100%" COLSPAN="13"><hr class="gray1px"></td>
    </tr>
    <tr><td colspan="13">
    <% } else { %>
    <tr>
        <td WIDTH="100%" COLSPAN="16"><hr class="gray1px"></td>
    </tr>
    <tr><td colspan="16">
    <% } %>    
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="order_detail">
<%      double allCreditsTotal = 0.0; %>
    <logic:iterate id="complaint" collection="<%= complaints %>" type="com.freshdirect.customer.ErpComplaintModel">
<%      allCreditsTotal += complaint.getAmount(); %>    
    </logic:iterate>
    <tr VALIGN="bottom">
        <td WIDTH="10%" ALIGN="right"><b>orig. totals:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td WIDTH="10%">Subtotal&nbsp;</td>
        <td WIDTH="13%" align="right"><%= CCFormatter.formatCurrency( order.hasInvoice() ? order.getInvoicedSubTotal() : order.getSubTotal() ) %></td>
        <td WIDTH="11%" ALIGN="RIGHT"><b>current</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td WIDTH="10%">Subtotal&nbsp;</td>
        <td WIDTH="14%" align="right"><%= CCFormatter.formatCurrency( (order.hasInvoice() ? order.getInvoicedSubTotal() : order.getSubTotal()) - allCreditsTotal ) %></td>
        <td WIDTH="10%" ALIGN="RIGHT"><b>new totals:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td WIDTH="10%">Subtotal</td>
        <td WIDTH="13%" align="right"><%= CCFormatter.formatCurrency( (order.hasInvoice()? order.getInvoicedSubTotal() : order.getSubTotal()) - (allCreditsTotal + newComplaint.getAmount()) ) %></td>
    </tr>
<%  double appliedCreditTotal = 0.0;
    for (Iterator it = appliedCredits.iterator(); it.hasNext(); ) {
        ErpAppliedCreditModel appliedCredit = (ErpAppliedCreditModel) it.next();
        appliedCreditTotal += appliedCredit.getAmount();
    }
    if (appliedCreditTotal > 0) {
%>
    <tr VALIGN="bottom">
        <td>&nbsp;</td>
        <td>Applied Credits&nbsp;</td>
        <td align="right">-<%= CCFormatter.formatCurrency( appliedCreditTotal ) %></td>
        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td>Applied Credits&nbsp;</td>
        <td align="right">-<%= CCFormatter.formatCurrency( appliedCreditTotal ) %></td>
        <td>&nbsp;</td>
        <td>Applied Credits</td>
        <td align="right">-<%= CCFormatter.formatCurrency( appliedCreditTotal ) %></td>
    </tr>
<%  } %>
<%  if (order.getTotalDiscountValue() > 0) { %>
    <tr VALIGN="bottom">
        <td>&nbsp;</td>
        <td>Promotions&nbsp;</td>
        <td align="right">-<%= CCFormatter.formatCurrency( order.hasInvoice() ? order.getActualDiscountValue() : order.getTotalDiscountValue() ) %></td>
        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td>Promotions&nbsp;</td>
        <td align="right">-<%= CCFormatter.formatCurrency( order.hasInvoice() ? order.getActualDiscountValue() : order.getTotalDiscountValue() ) %></td>
        <td>&nbsp;</td>
        <td>Promotions</td>
        <td align="right">-<%= CCFormatter.formatCurrency( order.hasInvoice() ? order.getActualDiscountValue() : order.getTotalDiscountValue() ) %></td>
    </tr>
<%  } %>
<% if(order.getDepositValue() > 0){ %>
    <tr VALIGN="bottom">
        <td ALIGN="right">&nbsp;</td>
        <td>Bottle Deposit&nbsp;</td>
        <td align="right"><%= CCFormatter.formatCurrency(order.hasInvoice() ? order.getInvoicedDepositValue() : order.getDepositValue()) %></td>
        <td ALIGN="RIGHT"><b>totals:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td>Bottle Deposit&nbsp;</td>
        <td align="right"><%= CCFormatter.formatCurrency(order.hasInvoice() ? order.getInvoicedDepositValue() : order.getDepositValue()) %></td>
        <td ALIGN="RIGHT">&nbsp;</td>
        <td>Bottle Deposit</td>
        <td align="right"><%= CCFormatter.formatCurrency(order.hasInvoice() ? order.getInvoicedDepositValue() : order.getDepositValue()) %></td>
    </tr>
<%}%>
    <tr VALIGN="bottom">
        <td ALIGN="right">&nbsp;</td>
        <td>Tax&nbsp;</td>
        <td align="right"><%= CCFormatter.formatCurrency(order.hasInvoice() ? order.getInvoicedTaxValue() : order.getTaxValue()) %></td>
        <td ALIGN="RIGHT"><b>totals:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td>Tax&nbsp;</td>
        <td align="right"><%= CCFormatter.formatCurrency(order.hasInvoice() ? order.getInvoicedTaxValue() : order.getTaxValue()) %></td>
        <td ALIGN="RIGHT">&nbsp;</td>
        <td>Tax</td>
        <td align="right"><%= CCFormatter.formatCurrency(order.hasInvoice() ? order.getInvoicedTaxValue() : order.getTaxValue()) %></td>
    </tr>
    <tr VALIGN="bottom">
        <td>&nbsp;</td>
        <td>Shipping&nbsp;</td>
        <td align="right">
      <%
         if(order.isDlvPassApplied()) {
       %>
      <%= DeliveryPassUtil.getDlvPassAppliedMessage(user) %>
         <% } else { %>           
            <%= (order.isDeliveryChargeWaived()) ? "WAIVED" : CCFormatter.formatCurrency(order.getDeliverySurcharge()) %>
       <% } %>       
        </td>
        <td>&nbsp;</td>
        <td>Shipping&nbsp;</td>
        <td align="right">
      <%
         if(order.isDlvPassApplied()) {
       %>
      <%= DeliveryPassUtil.getDlvPassAppliedMessage(user) %>
         <% } else { %>           
            <%= (order.isDeliveryChargeWaived()) ? "WAIVED" : CCFormatter.formatCurrency(order.getDeliverySurcharge()) %>
       <% } %>       
        </td>
        <td>&nbsp;</td>
        <td>Shipping</td>
        <td align="right">
      <%
         if(order.isDlvPassApplied()) {
       %>
      <%= DeliveryPassUtil.getDlvPassAppliedMessage(user) %>
         <% } else { %>           
            <%= (order.isDeliveryChargeWaived()) ? "WAIVED" : CCFormatter.formatCurrency(order.getDeliverySurcharge()) %>
       <% } %>       
        </td>
    </tr>
<%  if (order.getPhoneCharge() > 0) { %>
    <tr VALIGN="bottom">
        <td>&nbsp;</td>
        <td>Phone Handling&nbsp;</td>
        <td align="right"><%= CCFormatter.formatCurrency(order.getPhoneCharge()) %></td>
        <td>&nbsp;</td>
        <td>Phone Handling&nbsp;</td>
        <td align="right"><%= CCFormatter.formatCurrency(order.getPhoneCharge()) %></td>
        <td>&nbsp;</td>
        <td>Phone Handling</td>
        <td align="right"><%= CCFormatter.formatCurrency(order.getPhoneCharge()) %></td>
    </tr>
<%  } %>
    <tr VALIGN="bottom">
        <td>&nbsp;</td>
        <td>Order Total&nbsp;</td>
        <td align="right"><%= CCFormatter.formatCurrency( order.hasInvoice() ? order.getInvoicedTotal() : order.getTotal() ) %></td>
        <td>&nbsp;</td>
        <td>Order Total&nbsp;</td>
        <td align="right"><%= CCFormatter.formatCurrency( (order.hasInvoice() ? order.getInvoicedTotal() : order.getTotal()) - allCreditsTotal ) %></td>
        <td>&nbsp;</td>
        <td>Order Total</td>
        <td align="right"><%= CCFormatter.formatCurrency( (order.hasInvoice() ? order.getInvoicedTotal() : order.getTotal()) - (allCreditsTotal + newComplaint.getAmount()) ) %></td>
    </tr>
    <tr VALIGN="bottom">
        <td colspan="9" ALIGN="RIGHT"><INPUT TYPE="submit" value="update" class="submit" onclick="preventCreditIssue()"></td>
    </tr>
    <tr>
        <td COLSPAN="9"><FONT CLASS="space4pix"><BR></FONT><TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td WIDTH="100%" BGCOLOR="#CCCCCC"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr></TABLE></td>
    </tr>
    </TABLE>
    <%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ END ORDER TOTALS SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
</td></tr>
    <tr VALIGN="bottom">
    <% if(!isGiftCardOrder){ %>
        <td COLSPAN="5"><BR></td>
       <% } else { %>   
        <td COLSPAN="8"><BR></td>
       <% } %>        
        <td COLSPAN="8"><FONT CLASS="space2pix"><BR></FONT><b>3. Enter credit notes here:</b><BR><FONT CLASS="space2pix"><BR></FONT></td>
    </tr>
    <tr>
        <td></td>
        <% if(!isGiftCardOrder){ %>
        <td COLSPAN="4"><b>Note:</b> If total credit amount is over $<%= ErpServicesProperties.getCreditAutoApproveAmount()%>, supervisor approval will be required.<br>Store credit and cashbacks will not be processed until supervisor has reviewed and approved credit.</td>
        <% } else { %>   
          <td COLSPAN="7"><b>Note:</b> If total credit amount is over $<%= ErpServicesProperties.getCreditAutoApproveAmount()%>, supervisor approval will be required.<br>Store credit and cashbacks will not be processed until supervisor has reviewed and approved credit.</td>
          <% } %>
        <td BGCOLOR=<%= bgcolor %> COLSPAN="8">
            <textarea cols="30" rows="5" name="description" wrap="virtual"><%= request.getParameter("description") %></TEXTAREA><BR><fd:ErrorHandler result='<%=result%>' name='credit_description' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
        </td>
    </tr>
    <tr VALIGN="bottom">
         <% if(!isGiftCardOrder){ %>
        <td COLSPAN="5"><BR></td>
         <% } else { %>   
         <td COLSPAN="8"><BR></td>
          <% } %>        
        <td COLSPAN="8"><FONT CLASS="space2pix"><BR></FONT>
        <% if (emailPreview==null || emailPreview.getXslPath()==null) { %>
        <b>4. Select email option and notification time:</b>
        <% } else { %>
        <b>4. Preview custom email: (</b><a href="javascript:setPreviewEmailOff()">Edit email options</a><b>)</b>
        <% } %>
        <BR><FONT CLASS="space2pix"><BR></FONT></td>
    </tr>
    <tr>
        <td></td>
         <% if(!isGiftCardOrder){ %>        
        <td COLSPAN="4"></td>
        <% } else { %>  
        <td COLSPAN="7"></td>
        <% } %>        
        <td BGCOLOR=<%= bgcolor %> COLSPAN="8">
        <% if (emailPreview==null || emailPreview.getXslPath()==null) { %>
        <%@ include file="/includes/i_credit_email_options.jspf" %>
        <% } else {  %>
        <%@ include file="/includes/i_credit_email_preview.jspf" %>
        <% } %>
        </td>
    </tr>
    <tr VALIGN="bottom">
        <% if(!isGiftCardOrder){ %>
         <td COLSPAN="5">
        <% } else { %>  
        <td COLSPAN="8">
        <% } %><fd:ErrorHandler result='<%=result%>' name='payment_method_type' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler><BR></td>
        <td COLSPAN="8"><FONT CLASS="space2pix"><BR></FONT><b>5. Select payment method for charge back OR mailing address for check:</b><BR><FONT CLASS="space2pix"><BR></FONT></td>
    </tr>

<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ BEGIN CREDIT NOTES SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<tr>
    <% if(!isGiftCardOrder){ %>
         <td COLSPAN="5">
        <% } else { %>  
        <td COLSPAN="8">
        <% } %></td>
    <td COLSPAN="8" BGCOLOR=<%= bgcolor %>>
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" class="order_detail">
<tr VALIGN="TOP">
<%      
if(EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())){
%>
    <td WIDTH="5%" ALIGN="RIGHT"><INPUT TYPE="radio" NAME="payment_method_type" value="card" <%= ( "card".equalsIgnoreCase( request.getParameter("payment_method_type") ) ) ? "CHECKED" : "" %>></td>
    <td WIDTH="7%">
        <b>Name:<BR>
        Method:<BR>
        <% if (paymentMethod.getCardType() != null) { %>
        CC Type<BR>
        <% } %>
        Acct #:<BR>
        <% if (paymentMethod.getExpirationDate() != null) { %>
        Exp. Dt:<BR>
        <% } %>
        Address:</b>
    </td>
    <td WIDTH="13%">
        <%= paymentMethod.getName() %><BR>
        <%= paymentMethod.getPaymentMethodType() %><BR>
        <% if (paymentMethod.getCardType() != null) { %><%= paymentMethod.getCardType() %><BR><% } %>
        <%= PaymentMethodUtil.getDisplayableAccountNumber(paymentMethod) %><BR>
        <% if (paymentMethod.getAbaRouteNumber() != null) { %><%= paymentMethod.getAbaRouteNumber() %><BR><% } %>
        <% if (paymentMethod.getBankAccountType() != null) { %><%= paymentMethod.getBankAccountType() %><BR><% } %>
        <% if (paymentMethod.getExpirationDate() != null) { %><%= CCFormatter.formatCreditCardExpDate( paymentMethod.getExpirationDate() ) %><BR><% } %>
        <%= paymentMethod.getAddress1() %><BR>
        <%= paymentMethod.getCity() %>, <%= paymentMethod.getState() %> <%= paymentMethod.getZipCode() %>
    </td>
<%      } %>
    <td WIDTH="5%"><BR></td>
<%      
if(EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())){
%>
    <td WIDTH="70%"><b>Issue Cash Back</b>
        <TABLE WIDTH="30%" CELLPADDING="2" CELLSPACING="0" BORDER="0" class="order_detail">
        <tr VALIGN="TOP">
            <td WIDTH="10%" ALIGN="RIGHT"><INPUT TYPE="radio" NAME="payment_method_type" value="check" <%= ( "check".equalsIgnoreCase( request.getParameter("payment_method_type") ) ) ? "CHECKED" : "" %>>&nbsp;</td>
            <td WIDTH="90%"><%= paymentMethod.getName() %><BR>
            <%= paymentMethod.getAddress1() %><BR>
            <%= paymentMethod.getCity() %>, <%= paymentMethod.getState() %> <%= paymentMethod.getZipCode() %></td>
            <td WIDTH="1%"  BGCOLOR=<%= bgcolor %>>&nbsp;</td>
        </tr>
        </TABLE><br>
    </td>
<%      } %>
</tr>
</TABLE>
</td></tr>
<tr VALIGN="middle">
	<% if(!isGiftCardOrder){ %>
         <td COLSPAN="5">
        <% } else { %>  
        <td COLSPAN="8">
        <% } %></td>
	<td colspan="8">
		<b>6. Initiate Credit Processing:</b> <button id="process-button" class="submit">PROCESS CREDIT</button>
	</td>
</tr>
</TABLE>
<!-- additional fields -->
<%
final String CASEINFO_FIELDS[] = {"ci_media", "ci_moreissue", "ci_1st_ctct", "ci_isres", "ci_reason_nr", "ci_satisf", "ci_ctone"};
for (int k=0; k<CASEINFO_FIELDS.length; k++) {
%><input type='hidden' id='h_<%= CASEINFO_FIELDS[k] %>' name='<%= CASEINFO_FIELDS[k] %>' value="<%= request.getParameter(CASEINFO_FIELDS[k]) %>">
<%	
}
%>
</form>
<script type="text/javascript">
	var caseInfoArray = [
		{label:'Media', input:'ci_media', list: <%= JSHelper.listToJSArray(CrmCaseConstants.getCrmCaseMedia()) %>},
		{label:'More than one issue with order', input:'ci_moreissue', list: <%= JSHelper.listToJSArray(CrmCaseConstants.getCaseMoreThenOneIssueList()) %>},
		{label:'First contact for issue', input:'ci_1st_ctct', list: <%= JSHelper.listToJSArray(CrmCaseConstants.getCaseFirstContactForIssueList()) %>},
		{label:'Resolved on first contact', input:'ci_isres', list: <%= JSHelper.listToJSArray(CrmCaseConstants.getCaseResolvedOnFirstContactList()) %>},
		{label:'NOT resolved reason', input:'ci_reason_nr', list: <%= JSHelper.listToJSArray(CrmCaseConstants.getCrmCaseReasonForNotResolve()) %>},
		{label:'Resolution satisfactory', input:'ci_satisf', list: <%= JSHelper.listToJSArray(CrmCaseConstants.getCrmResolutionSatisfactoryList()) %>},
		{label:'Customer\'s tone', input:'ci_ctone', list: <%= JSHelper.listToJSArray(CrmCaseConstants.getCrmCaseCustomerTone()) %>}
	];

	var casePanel;

	$E.on('process-button', 'click', function(evt) {
		// prevent submit
		YAHOO.util.Event.preventDefault(evt);


		casePanel = new YAHOO.widget.Dialog(
			"casePanel",
			{
				width:"420px",
				fixedcenter: true,
				visible: false,
				draggable: false,
				modal: true,
				close: false,
				constrainttoviewport: true,

				buttons: [
					{text:'Process', handler:
						{
							fn: function(obj) {
								if (this.allFieldsAnswered()) {
									// submit form
									document.forms['form-issue-credit'].submit();
								}
							}
						}
					},
					{text:'Cancel', handler:
						{
							fn: function(obj) {
								this.destroy();
							}
						}
					}
				]
			}
		);
		casePanel.setHeader("Disposition fields");
		casePanel.setBody("<form name=\"dummyDialogForm\" onsubmit=\"return false;\"><table id=\"casePanel-table\" style=\"width: 100%;\"></table></form>");

		casePanel.fields = [];
		casePanel.allFieldsAnswered = function() {
			var flag1 = ('Other' == $('h_ci_media').value);
			var flag2 = ('No' == $('h_ci_isres').value);

			if (flag1) {
				// this is the only value if set
				return true;
			}

			var fldz = ["ci_media", "ci_moreissue", "ci_1st_ctct", "ci_isres", "ci_satisf", "ci_ctone"];
			
			for (i=0; i<fldz.length; i++) {
				var inputEl = $('h_'+fldz[i]);
				if ('' === inputEl.value) {
					return false;
				}
			}

			return !('No' == $('h_ci_isres').value && '' == $('h_ci_reason_nr').value);
		};

		casePanel.adjustRowDisplay = function() {
			var flag1 = ('Other' == $('h_ci_media').value);
			var flag2 = ('No' == $('h_ci_isres').value);
			var fldz = ["ci_moreissue", "ci_1st_ctct", "ci_isres", "ci_satisf", "ci_ctone"];

			for (i=0; i<fldz.length; i++) {
				$('row_'+fldz[i]).style.display = flag1 ? 'none' : '';
			}
			
			$('row_ci_reason_nr').style.display = (!flag1 && flag2) ? '' : 'none';
		};

		
		casePanel.appendRow = function(parent, case_item) {
			var row, cell, sel, opt, label, k;

			var inputEl = $('h_'+case_item.input);
			
			sel = document.createElement("SELECT");
			sel.style.width = '100%';

			// add default empty item
			opt = document.createElement("OPTION");
			sel.appendChild(opt);

			// add list items
			for (i=0; i<case_item.list.length; i++) {
				opt = document.createElement("OPTION");
				opt.setAttribute("value", case_item.list[i]);
				if (case_item.list[i] == inputEl.value)
					opt.setAttribute("selected", "selected");
				opt.innerHTML = case_item.list[i];
				sel.appendChild(opt);
			}
	
			row = document.createElement("TR");
			row.setAttribute('id', 'row_'+case_item.input);

			// question label
			label = document.createElement("SPAN");
			label.innerHTML = case_item.label;

			// label cell
			cell = document.createElement("TD");
			cell.appendChild(label);
			row.appendChild(cell);
			
			// drop-down list cell
			cell = document.createElement("TD");
			cell.style.width = "16em";
			cell.appendChild(sel);
			row.appendChild(cell);

			parent.appendChild(row);

			// capture select value in input field
			var self = this;
			$E.on(sel, 'change', function(e){
				inputEl.value = this.value;

				self.adjustRowDisplay();

				// adjust 'process' button
				if (self.processButton) {
					try {
						var result = self.allFieldsAnswered();
						self.processButton.set("disabled", !result );
					} catch(exc) {
						alert('exc='+exc);
					}
				}
			});

			this.fields.push(case_item.input);
		};

		casePanel.render(document.body);

		var btns = casePanel.getButtons();
		for (k=0; k<btns.length; k++) {
			if ('Process' == btns[k].get('label')) {
				casePanel.processButton = btns[k];
				var result = casePanel.allFieldsAnswered();
				casePanel.processButton.set("disabled", !result );
				break;
			}
		}

		var container = $('casePanel-table');

		// find tbody
		if (container.childNodes.length > 0) {
			var table_body = YAHOO.util.Selector.query('tbody', container, true);
			if (table_body)
				container = table_body;
		}

		for (k=0; k<caseInfoArray.length; k++) {
			casePanel.appendRow(container, caseInfoArray[k]);
		}
		
		casePanel.center();
		casePanel.adjustRowDisplay();
		casePanel.show();
	});



	$E.onDOMReady(function(){
		var aForm = $('form-issue-credit');
		aForm.isDirty = <%= request.getAttribute("__is_dirty") != null ? "true":"false" %>;
		
		var dirtyMaker = function(e) {
			// mark form dirty
			$('form-issue-credit').isDirty = true;
		};
		
		var inps = YAHOO.util.Selector.query('INPUT', aForm, false);
		for (i=0; i<inps.length; i++) {
			$E.on(inps[i], 'change', dirtyMaker);
		}

		inps = YAHOO.util.Selector.query('SELECT', aForm, false);
		for (i=0; i<inps.length; i++) {
			$E.on(inps[i], 'change', dirtyMaker);
		}
	});
</script>
</div>
</tmpl:put>
</fd:IssueCredits>
</fd:CreateComplaint>
</fd:ComplaintGrabber>
</fd:GetGiftCardPurchased>
</fd:GetOrder>
</tmpl:insert>
