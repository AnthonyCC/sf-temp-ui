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

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<%
String errorLineColor = "#FFCCCC";
String bgcolor="#CCCCFF";
ContentFactory contentfactory = ContentFactory.getInstance();
%>

<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);%>

<tmpl:insert template='/template/top_nav.jsp'>

<%  String orderId = (String) request.getParameter("orderId"); %>

<tmpl:put name='title' direct='true'>Order <%= orderId%> Issue Credits</tmpl:put>

<fd:GetOrder id='orderI' saleId='<%= orderId %>'>
<%
    FDOrderAdapter order = (FDOrderAdapter) orderI;
    ErpPaymentMethodI paymentMethod = order.getPaymentMethod();
    Collection appliedCredits = order.hasInvoice() ? order.getActualAppliedCredits() : order.getAppliedCredits(); 
    String action = NVL.apply(request.getParameter("actionName"), "submit");
%>

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
    
    String [] lineReturnQty = request.getParameterValues("ol_credit_qty");
    
    String [] lineReturnQtyChanged = request.getParameterValues("ol_credit_qty_changed");
    
    String [] lineReason = request.getParameterValues("ol_credit_reason");
    
    String [] lineMethod = request.getParameterValues("ol_credit_method");
    
    String [] lineAmount = request.getParameterValues("ol_credit_amount");

    // Miscellaneous request Params
    
    String [] miscAmount = request.getParameterValues("misc_credit_amount");
    
    String [] miscReason = request.getParameterValues("misc_credit_reason");
        
    String [] miscMethod = request.getParameterValues("misc_credit_method");
    
    String send_email = NVL.apply(request.getParameter("send_email"), EnumSendCreditEmail.SEND_ON_APPROVAL.getName());
    String email_type = NVL.apply(request.getParameter("email_type"), "generic");
    String email_template_code = request.getParameter("email_template_code");
    String custom_message = NVL.apply(request.getParameter("custom_message"), "");
    boolean agent_signature = "yes".equalsIgnoreCase(request.getParameter("agent_signature"));
    
    if (email_template_code==null || "generic".equalsIgnoreCase(email_type)) {
        email_template_code = EnumCreditEmailType.DEFAULT_EMAIL.getName();
        custom_message="";
        agent_signature=false;
    }
%>

<tmpl:put name='content' direct='true'>
<%@ include file="/includes/order_nav.jspf"%>
<%@ include file="/includes/order_summary.jspf"%>
<script language="JavaScript" type="text/javascript">
    function preventCreditIssue() {
        document.issue_credit.do_issue_credit.value='false';
    }
    function setPreviewEmailOn() {
        document.issue_credit.previewEmail.value='yes';
        document.issue_credit.actionName.value='preview';
        document.issue_credit.submit();
    }
    
    function setPreviewEmailOff() {
        document.issue_credit.previewEmail.value='no';
        document.issue_credit.actionName.value='preview';
        document.issue_credit.submit();
    }
    
    function setEditReturnQty(i){
        if (i ==0 && !document.issue_credit.ol_credit_qty_changed[0]) {
           fld = document.issue_credit.ol_credit_qty_changed;
        } else {
            fld = document.issue_credit.ol_credit_qty_changed[i];
        }
        fld.value = 'Q';
    }
</script>

<%String [] checkedErrors = {"technical_difficulty", "general_error_msg", "invalid_complaint", "payment_method_type", "no_more_refunds"};%>
<fd:ErrorHandler result="<%=result%>" field="<%=checkedErrors%>" id='errorMsg'>
	<div class="error_detail"><%= errorMsg %></div>
</fd:ErrorHandler>

<div class="content_scroll" style="height: 72%; padding-top: 0px;">
<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order_detail">
<form name="issue_credit" method="POST" action="issue_credit.jsp?orderId=<%= orderId %>">
    <input type="hidden" name="orig_sale_id" value="<%= orderId %>">
    <input type="hidden" name="do_issue_credit" value="true">
    <input type="hidden" name="previewEmail" value="no">
    <input type="hidden" name="actionName" value="submit">
    <tr class="list_header">
        <td WIDTH="7%" COLSPAN="2" class="list_header_detail"><b>Order Qty.</b></td>
        <td WIDTH="23%" class="list_header_detail"><b>Name</b></td>
        <td WIDTH="9%" class="list_header_detail"><b>Price</b></td>
        <td WIDTH="7%" class="list_header_detail"><b>Orig. Total</b></td>
        <td WIDTH="7%" COLSPAN="2" class="list_header_detail"><b>Prev. Qty. Returned</b></td>
        <td WIDTH="7%" ALIGN="CENTER" class="list_header_detail"><b>Prev. Credit</b></td>
        <td WIDTH="5%" ALIGN="CENTER" class="list_header_detail"><b>Current Total</b></td>
        <td WIDTH="5%" class="list_header_detail"><b>New Qty. Returned</b></td>
        <td WIDTH="17%" class="list_header_detail"><b>Credit Reason</b></td>
        <td WIDTH="8%" class="list_header_detail"><b>Credit Type</b></td>
        <td WIDTH="5%" ALIGN="CENTER" class="list_header_detail"><b>Credit Amt</b></td>
    </tr>
    <tr VALIGN="bottom">
        <td WIDTH="45%" COLSPAN="5"><FONT CLASS="space2pix"><BR></FONT><b>PRODUCTS</b><BR></td>
        <td WIDTH="50%" COLSPAN="7"><b>1. Enter product credits here:</b></td>
        <td WIDTH="6%" ALIGN="RIGHT"><INPUT TYPE="submit" class="submit" value="update" onclick="preventCreditIssue()"></td>
    </tr>
    <tr>
        <td WIDTH="100%" COLSPAN="13"><TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td WIDTH="100%" BGCOLOR="#CCCCCC"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr></TABLE></td>
    </tr>
<%  
    int i = 0;
    String lastdept = null;
%>
    <logic:iterate id="orderLine" collection="<%= order.getOrderLines() %>" type="com.freshdirect.fdstore.customer.FDCartLineI">
        <%      
        ErpInvoiceLineI invoiceLine = orderLine.hasInvoiceLine() ? orderLine.getInvoiceLine() : null;
        
        if ( lastdept == null || !lastdept.equalsIgnoreCase( orderLine.getDepartmentDesc() ) ) {
            lastdept = orderLine.getDepartmentDesc();
        %>
            <%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  Department Header row  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
            <tr VALIGN="bottom">
                <td WIDTH="7%" COLSPAN="2"><BR></td>
                <td WIDTH="32%" COLSPAN="3"><FONT CLASS="space2pix"><BR></FONT><b><%= lastdept %></b><BR></td>
                <td WIDTH="55%" COLSPAN="8" BGCOLOR=<%= bgcolor %>><BR></td>
            </tr>
        <% } %>
        
<%      //
        // Calculate orderline complaint values
        //
        double retQty = 0.0;
        double retAmount = 0.0;
        for (Iterator it = lineComplaints.iterator(); it.hasNext(); ) {
            ErpComplaintLineModel line = (ErpComplaintLineModel) it.next();
            if (i == Integer.parseInt( line.getComplaintLineNumber() ) ) {
                retQty += line.getQuantity();
                retAmount += line.getAmount();
            }
        }

	String deptDesc=lastdept;
	if (lastdept.startsWith("Recipe:")) {  // get the dept of the primary home of this item
		ProductModel prodModl = contentfactory.getProduct(orderLine.getSku().getSkuCode());
		if (prodModl!=null){
			if (prodModl.getPrimaryHome()!=null) {
				deptDesc=prodModl.getPrimaryHome().getDepartment().getFullName();
			}
		}
	}
%>
<%      // check if this line contains an error
        boolean isErrorLine = ( !result.isSuccess() && (result.hasError("ol_error_qty_"+i) || result.hasError("ol_error_"+i)) ) ? true : false;
%>
    <%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ORDER LINE ROW ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
    <tr VALIGN="TOP" <%= ( isErrorLine ) ? "BGCOLOR='" + errorLineColor + "'" : "" %>>
        <td WIDTH="2%" ALIGN="CENTER"><%= invoiceLine != null ? invoiceLine.getQuantity() : orderLine.getQuantity() %></td>
        <td WIDTH="2%" ALIGN="CENTER"><%= orderLine.getSalesUnitDescription() %></td>
        <td WIDTH="25%">
            <%= orderLine.getDescription() %> (<%=orderLine.getConfigurationDesc()%>)
            <fd:ErrorHandler result='<%=result%>' name='<%= "ol_error_"+i %>' id="errorMsg">
                <br><FONT CLASS="text11rbold">&middot; <%= errorMsg %></FONT>
            </fd:ErrorHandler>
            <fd:ErrorHandler result='<%=result%>' name='<%= "ol_error_qty_"+i %>' id="errorMsg">
                <br><FONT CLASS="text11rbold">&middot; <%= errorMsg %></FONT>
            </fd:ErrorHandler>
        </td>
        <td WIDTH="9%"><%= orderLine.getUnitPrice() %></td>
        <td WIDTH="7%" ALIGN="CENTER"><%= invoiceLine != null ? invoiceLine.getPrice(): orderLine.getPrice() %></td>
        <td WIDTH="3%" BGCOLOR="<%= ( isErrorLine ) ? errorLineColor : bgcolor %>" ALIGN="CENTER">
<%      if ( !result.isSuccess() && (result.hasError("ol_error_qty_"+i) || result.hasError("ol_error_"+i)) ) { %><FONT CLASS="text8redbold">*</font><%  } %>
            <%= CCFormatter.formatQuantity( retQty ) %>
        </td>
        <td WIDTH="4%" BGCOLOR="<%= ( isErrorLine ) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><%= orderLine.getSalesUnitDescription() %></td>
        <td WIDTH="7%" BGCOLOR="<%= ( isErrorLine ) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><%= CCFormatter.formatCurrency(retAmount) %></td>
        <td WIDTH="5%" BGCOLOR="<%= ( isErrorLine ) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><%= CCFormatter.formatCurrency(invoiceLine != null ? invoiceLine.getPrice() - retAmount: orderLine.getPrice() - retAmount) %></td>
        <td WIDTH="5%" BGCOLOR="<%= ( isErrorLine ) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><INPUT TYPE="TEXT" NAME="ol_credit_qty" SIZE="4" value="<%= lineReturnQty == null ? "0" : lineReturnQty[i] %>" onChange="setEditReturnQty(<%=i%>)"></td>
        <td WIDTH="17%" BGCOLOR="<%= ( isErrorLine ) ? errorLineColor : bgcolor %>">
            <SELECT name="ol_credit_reason" class="pulldown_detail">
                <OPTION value="">select credit reason</OPTION>
                <logic:iterate id="reason" collection="<%= ComplaintUtil.getReasonsForDepartment(deptDesc) %>" type="com.freshdirect.customer.ErpComplaintReason">
                    <OPTION value="<%= reason.getId() %>" <%= ( lineReason != null && reason.getId().equals(lineReason[i]) ) ? "SELECTED" : "" %>>
                        <%= reason.getReason() %>
                    </OPTION>
                </logic:iterate>
            </SELECT>
        </td>
        <td WIDTH="8%" BGCOLOR="<%= ( isErrorLine ) ? errorLineColor : bgcolor %>">
            <SELECT name="ol_credit_method" class="pulldown_detail">
                <OPTION value="FDC" <%= ( lineMethod != null && "FDC".equals(lineMethod[i]) ) ? "SELECTED" : "" %>>store</OPTION>
                <OPTION value="CSH" <%= ( lineMethod != null && "CSH".equals(lineMethod[i]) ) ? "SELECTED" : "" %>>cash back</OPTION>
            </SELECT>
        </td>
<%      
        double creditAmount = 0.0;
        double creditQty = 0.0;
        boolean quantityChanged = false;
        if(lineReturnQtyChanged != null && lineReturnQtyChanged[i] != null && "Q".equals(lineReturnQtyChanged[i])){
            quantityChanged = true;
        }
        if ( lineReturnQty != null && lineReturnQty[i] != null && !"".equals(lineReturnQty[i])) {
            creditQty = Double.parseDouble(lineReturnQty[i]);
        }
        if(creditQty <= 0 && !quantityChanged){
            if(lineAmount != null && lineAmount[i] != null && !"".equals(lineAmount[i])){
                creditAmount = Double.parseDouble(lineAmount[i]);
            }
        }else{
            if ( lineReturnQty != null && lineReturnQty[i] != null && !"".equals(lineReturnQty[i])) {
                creditQty = Double.parseDouble( lineReturnQty[i] );
                double origQty = invoiceLine != null ? invoiceLine.getQuantity() : orderLine.getQuantity();
                if (origQty!=0) {
                    creditAmount = creditQty * ( (invoiceLine != null ? invoiceLine.getPrice() : orderLine.getPrice()) / origQty);
                }
            }
        }
%>
        <td WIDTH="5%" BGCOLOR="<%= ( isErrorLine ) ? errorLineColor : bgcolor %>" ALIGN="CENTER"><input type="text" name="ol_credit_amount" value="<%= MathUtil.roundDecimal(creditAmount) %>" size="4"></td>
        <input type="hidden" name="ol_credit_qty_changed" value="">
        <input type="hidden" name="ol_orig_qty" value="<%= invoiceLine != null ? invoiceLine.getQuantity() : orderLine.getQuantity() %>">
        <input type="hidden" name="ol_orig_price" value="<%= invoiceLine != null ? invoiceLine.getPrice() : orderLine.getPrice() %>">
        <input type="hidden" name="ol_sku_code" value="<%= orderLine.getSkuCode() %>">
        <input type="hidden" name="ol_credit_dept" value="<%= orderLine.getDepartmentDesc() %>">
        <input type="hidden" name="ol_credit_qty_returned" value="<%= CCFormatter.formatQuantity( retQty ) %>">
        <input type="hidden" name="ol_tax_rate" value="<%=orderLine.getTaxRate() %>">
        <input type="hidden" name="ol_deposit_value" value="<%=orderLine.getDepositValue() %>">
        <input type="hidden" name="orderlineId" value="<%=orderLine.getOrderLineId() %>">
    </tr>
<%      i++; %>
</logic:iterate>
    <tr>
        <td WIDTH="100%" COLSPAN="13"><TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td WIDTH="100%" BGCOLOR="#CCCCCC"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr></TABLE></td>
    </tr>
    <%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ BEGIN DEPT TOTALS/CREDITS HEADER ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
    <tr VALIGN="bottom">
        <td WIDTH="45%" COLSPAN="5"><FONT CLASS="space2pix"><BR></FONT><b>DEPARTMENTS</b><BR></td>
        <td WIDTH="50%" COLSPAN="7"><b>2. Enter department credits here:</b></td>
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
        <td COLSPAN="3"><FONT CLASS="space2pix"><BR></FONT><b>Goodwill</b><BR></td>
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
        <td COLSPAN="3"><FONT CLASS="space2pix"><BR></FONT><b>Transportation</b><BR></td>
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
        <td COLSPAN="3"><FONT CLASS="space2pix"><BR></FONT><b>Extra Item</b><BR></td>
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
    <tr>
        <td WIDTH="100%" COLSPAN="13"><hr class="gray1px"></td>
    </tr>
    <tr><td colspan="13">
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
        <td COLSPAN="5"><BR></td>
        <td COLSPAN="8"><FONT CLASS="space2pix"><BR></FONT><b>3. Enter credit notes here:</b><BR><FONT CLASS="space2pix"><BR></FONT></td>
    </tr>
    <tr>
        <td></td>
        <td COLSPAN="4"><b>Note:</b> If total credit amount is over $14, supervisor approval will be required.<br>Store credit and cashbacks will not be processed until supervisor has reviewed and approved credit.</td>
        <td BGCOLOR=<%= bgcolor %> COLSPAN="8">
            <textarea cols="30" rows="5" name="description" wrap="virtual"><%= request.getParameter("description") %></TEXTAREA><BR><fd:ErrorHandler result='<%=result%>' name='credit_description' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
        </td>
    </tr>
    <tr VALIGN="bottom">
        <td COLSPAN="5"><BR></td>
        <td COLSPAN="8"><FONT CLASS="space2pix"><BR></FONT><b>
        <% if (emailPreview==null || emailPreview.getXslPath()==null) { %>
        4. Select email option and notification time:</b>
        <% } else { %>
        4. Preview custom email:  (</b><a href="javascript:setPreviewEmailOff()">Edit email options</a><b>)</b>
        <% } %>
        <BR><FONT CLASS="space2pix"><BR></FONT></td>
    </tr>
    <tr>
        <td></td>
        <td COLSPAN="4"></td>
        <td BGCOLOR=<%= bgcolor %> COLSPAN="8">
        <% if (emailPreview==null || emailPreview.getXslPath()==null) { %>
        <%@ include file="/includes/i_credit_email_options.jspf" %>
        <% } else {  %>
        <%@ include file="/includes/i_credit_email_preview.jspf" %>
        <% } %>
        </td>
    </tr>
    <tr VALIGN="bottom">
        <td COLSPAN="5"><fd:ErrorHandler result='<%=result%>' name='payment_method_type' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler><BR></td>
        <td COLSPAN="8"><FONT CLASS="space2pix"><BR></FONT><b>5. Select payment method for charge back OR mailing address for check:</b><BR><FONT CLASS="space2pix"><BR></FONT></td>
    </tr>

<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ BEGIN CREDIT NOTES SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<tr>
    <td colspan="5"></td>
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
<td colspan="5"></td>
<td colspan="8"><b>6. Initiate Credit Processing:</b> <INPUT TYPE="submit" value="PROCESS CREDIT" class="submit"></td>
</tr>
</form>
</TABLE>
</div>

</tmpl:put>
</fd:IssueCredits>
</fd:CreateComplaint>
</fd:ComplaintGrabber>
</fd:GetOrder>
</tmpl:insert>
