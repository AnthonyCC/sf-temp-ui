<%@ page import="com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderData" %>
<%@ page import="com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderHistoryData" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="fd-features" prefix="features" %>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false'/>
<features:isActive name="selfcredit" featureName="backOfficeSelfCredit" />
<features:redirectUnauthorizedFeature featureActive="${selfcredit}" featureName='backOfficeSelfCredit'/>

<potato:selfCreditOrderHistory/>

<tmpl:insert template='/common/template/dnav.jsp'>

<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - Your Profile" pageId="credit"></fd:SEOMetaTag>
</tmpl:put>

<tmpl:put name='content' direct='true'>

	<h2>Self-Credit Test Page</h2>
	
	<div class="order_history" id="selfcredit-orders">
		<form action="/your_account/selfcredit.jsp">
			<label for="order-field" class="bold inline text-right">Order #:</label> 
			<select class="customsimpleselect" name="orderId" id="order-field">
       			<option value="">Select Order: </option>
        			<c:forEach items="${orderHistory.orders}" var="order" >
						<option value="${order.saleId}">
					    	${order.saleId} :
					    	${order.requestedDate} 
						</option>
     				</c:forEach>
         	</select>
         	<button type="submit" class="cssbutton green"> Select Order</button>
         </form>
	</div>
	
<%
String orderId = request.getParameter("orderId");

if(orderId != null) {
%>
<potato:selfCreditOrderDetails orderId="<%=orderId %>"/>

		<div class="issue-credit">
			<form method="post" action="/api/selfcredit/issuecredit">
				<table>
					<tr>
						<th bgcolor="#DDDDDD" class="text10bold">Name</th>
						<th bgcolor="#DDDDDD" class="text10bold">Price</th>
						<th bgcolor="#DDDDDD" class="text10bold">Quantity</th>
						<th bgcolor="#DDDDDD" class="text10bold">Complaint reason</th>
					</tr>
					<c:forEach items="${orderDetails.orderLines}" var="orderLine">
						<tr>
							<td>${orderLine.description}</td>
							<input type="hidden" name="orderDescription"
								value="${orderLine.description}">
							<td>${orderLine.price}</td>
							<input type="hidden" name="orderPrice" value="${orderLine.price}">
							<td>${orderLine.quantity}</td>
							<input type="hidden" name="orderQuantity"
								value="${orderLine.quantity}">
							<td><select class="customsimpleselect"
								name="complaintReason" id="complaint-reason">
									<option value="">Select complaint reason:</option>
									<c:forEach items="${orderLine.complaintReasons}"
										var="complaintReason">
										<option value="${complaintReason.reason}">
											${complaintReason.reason}</option>
									</c:forEach>
							</select></td>
						</tr>
						<input type="hidden" name="orderLineId"
							value="${orderLine.orderLineId}">
						<input type="hidden" name="orderLineSkuCode"
							value="${orderLine.orderLineSkuCode}">
						<input type="hidden" name="orderLineCartNumber"
							value="${orderLine.orderLineCartNumber}">
					</c:forEach>
				</table>
				<input type="hidden" name="orderId" value=<%=orderId%>>
				<button type="submit" class="cssbutton green">Issue Credit</button>
			</form>
		</div>
		<%
		    }
		%>

		<div class="self-credit-cron">
			<form action="/your_account/selfcredit_cron.jsp">
				<button type="submit" class="cssbutton green"> Start AutoCreditApprovalCron</button>
			</form>
		</div>

<tr VALIGN="TOP">
	<TD WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="" ALIGN="LEFT">
	CONTINUE SHOPPING
	<BR>from <FONT CLASS="text11bold">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
</tr>

</tmpl:put>
</tmpl:insert>

<script>
    window.FreshDirect = window.FreshDirect || {};
    window.FreshDirect.orderHistory = window.FreshDirect.orderHistory || {};

    window.FreshDirect.user = window.FreshDirect.user || {};
    window.FreshDirect.user.isZipPopupUsed = true;

    window.FreshDirect.orderHistory = <fd:ToJSON object="${orderHistory}" noHeaders="true"/>
    console.log(window.FreshDirect.orderHistory);
    
    window.FreshDirect.orderDetails = <fd:ToJSON object="${orderDetails}" noHeaders="true"/>
    console.log(window.FreshDirect.orderDetails);
</script>
