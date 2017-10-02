<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="com.freshdirect.fdstore.customer.UnsettledOrdersInfo"%>
<%@page import="java.util.*"%>
<%@ taglib uri='freshdirect' prefix='fd'%>


<html lang="en-US" xml:lang="en-US">
<head>
<style>
<
style type ="text/css">body,html {
	height: 100%;
}

html,body,div,span,applet,object,iframe, /*h1, h2, h3, h4, h5, h6,*/ p,blockquote,pre,a,abbr,acronym,address,big,cite,code,del,dfn,em,font,img,ins,kbd,q,s,samp,small,strike,strong,sub,sup,tt,var,b,u,i,center,dl,dt,dd,ol,ul,li,fieldset,form,label,legend,table,caption,tbody,tfoot,thead,tr,th,td
	{
	margin: 0;
	padding: 0;
	border: 0;
	outline: 0;
	font-size: 100%;
	vertical-align: baseline;
	background: transparent;
}

body {
	line-height: 1;
}

ol,ul {
	list-style: none;
}

blockquote,q {
	quotes: none;
}

blockquote:before,blockquote:after,q:before,q:after {
	content: '';
	content: none;
}

:focus {
	outline: 0;
}

del {
	text-decoration: line-through;
}

table {
	border-spacing: 0;
}
	/* IMPORTANT, I REMOVED border-collapse: collapse; FROM THIS LINE IN ORDER TO MAKE THE OUTER BORDER RADIUS WORK */

/*------------------------------------------------------------------ */

/*This is not important*/
body {
	font-family: Arial, Helvetica, sans-serif;
	margin: 0 auto;
	width: 100%;
}

a:link {
	color: #666;
	font-weight: bold;
	text-decoration: none;
}

a:visited {
	color: #666;
	font-weight: bold;
	text-decoration: none;
}

a:active,a:hover {
	color: #bd5a35;
	text-decoration: underline;
}

/*
		Table Style - This is what you want
		------------------------------------------------------------------ */
table a:link {
	color: #666;
	font-weight: bold;
	text-decoration: none;
}

table a:visited {
	color: #999999;
	font-weight: bold;
	text-decoration: none;
}

table a:active,table a:hover {
	color: #bd5a35;
	text-decoration: underline;
}

table {
	font-family: Arial, Helvetica, sans-serif;
	color: #666;
	font-size: 12px;
	text-shadow: 1px 1px 0px #fff;
	background: #eaebec;
	margin: 20px;
	border: #ccc 1px solid;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	border-radius: 3px;
	-moz-box-shadow: 0 1px 2px #d1d1d1;
	-webkit-box-shadow: 0 1px 2px #d1d1d1;
	box-shadow: 0 1px 2px #d1d1d1;
}

table caption {
	font-size: 16px;
	text-shadow: 1px 1px 0px #fff;
	font-weight: bold;
	padding: 5px;
	color: #333;
}

table th {
	padding: 11px 15px 12px 15px;
	border-top: 1px solid #fafafa;
	border-bottom: 1px solid #e0e0e0;
	background: #ededed;
	background: -webkit-gradient(linear, left top, left bottom, from(#ededed),
		to(#ebebeb));
	background: -moz-linear-gradient(top, #ededed, #ebebeb);
}

table th:first-child {
	text-align: left;
	padding-left: 20px;
}

table tr:first-child th:first-child {
	-moz-border-radius-topleft: 3px;
	-webkit-border-top-left-radius: 3px;
	border-top-left-radius: 3px;
}

table tr:first-child th:last-child {
	-moz-border-radius-topright: 3px;
	-webkit-border-top-right-radius: 3px;
	border-top-right-radius: 3px;
}

table tr {
	text-align: center;
	padding-left: 20px;
}

table tr td:first-child {
	text-align: left;
	padding-left: 10px;
	border-left: 0;
}

table tr td {
	padding: 8px;
	border-top: 1px solid #ffffff;
	border-bottom: 1px solid #e0e0e0;
	border-left: 1px solid #e0e0e0;
	background: #fafafa;
	background: -webkit-gradient(linear, left top, left bottom, from(#fbfbfb),
		to(#fafafa));
	background: -moz-linear-gradient(top, #fbfbfb, #fafafa);
}

table tr.even td {
	background: #f6f6f6;
	background: -webkit-gradient(linear, left top, left bottom, from(#f8f8f8),
		to(#f6f6f6));
	background: -moz-linear-gradient(top, #f8f8f8, #f6f6f6);
}

table tr:last-child td {
	border-bottom: 0;
}

table tr:last-child td:first-child {
	-moz-border-radius-bottomleft: 3px;
	-webkit-border-bottom-left-radius: 3px;
	border-bottom-left-radius: 3px;
}

table tr:last-child td:last-child {
	-moz-border-radius-bottomright: 3px;
	-webkit-border-bottom-right-radius: 3px;
	border-bottom-right-radius: 3px;
}

table tr:hover td {
	background: #f2f2f2;
	background: -webkit-gradient(linear, left top, left bottom, from(#f2f2f2),
		to(#f0f0f0));
	background: -moz-linear-gradient(top, #f2f2f2, #f0f0f0);
}

form label {
	font-size: 14px;
	font-weight: bold;
	font-family: Arial, Helvetica, sans-serif;
}
</style>
<title>Orders Summary</title>
</head>
<body>

	<h2 align="center">Orders Summary</h2>
	<br>
	<br>
	<br>

	<fd:ProductSalesInfoController actionName='getUnsettledOrders'
		result='result' successPage='<%request.getRequestURI()%>'>
		<%
			List<UnsettledOrdersInfo> orders =  (List<UnsettledOrdersInfo>)request.getAttribute("unsettledOrders");
		%>
		<div align="center">
			<table align="center" border="1">
				<tr style="font-weight: bold">
					<td>Delivery Date</td>
					<td>Settled</td>
					<td>Charge back</td>
					<td>Settlement Failed</td>
					<td>Enroute</td>
					<td>Payment Pending</td>
					<td>Capture Pending</td>
					<td>GC Settlement Pending</td>
					<td>Pending Refusal</td>
					<td>GC Payment Pending</td>
					<td>GC Registration Pending</td>
				</tr>
				<%
					if(orders!=null){
						for(int i=0;i<orders.size();i++){
				%>
				<tr>
					<td><%=orders.get(i).getDeliveryDate()%></td>
					<td><%=orders.get(i).getSettled()%></td>
					<td><%=orders.get(i).getChargeBack()%></td>
					<td><%=orders.get(i).getSettlementFailed()%></td>
					<td><%=orders.get(i).getEnroute()%></td>
					<td><%=orders.get(i).getPaymentPending()%></td>
					<td><%=orders.get(i).getCapturePending()%></td>
					<td><%=orders.get(i).getGCSettlementPending()%></td>
					<td><%=orders.get(i).getPendingRefusal()%></td>
					<td><%=orders.get(i).getGCPaymentPending()%></td>
					<td><%=orders.get(i).getGCRegistrationPending()%></td>

				</tr>
				<%
					} }
				%>
			</table>
		</div>
	</fd:ProductSalesInfoController>
	<br />
</body>
</html>