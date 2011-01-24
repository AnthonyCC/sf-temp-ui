<%@ page autoFlush='false' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<fd:CheckLoginStatus guestAllowed="false" redirectPage='/index.jsp' />
<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader ("Expires", 0);
%>
<%!
final static String[] FORTUNE = {
	"No one is listening until you make a mistake.",
	"He who hesitates is probably right.",
	"The ideal resume will turn up one day after the position is filled.",
	"Now and then an innocent man is sent to the legislature.",
	"Everybody wants to go to heaven, but nobody wants to die.",
	"In order to get a loan, you must first prove that you don't need it.",
	"When it comes to humility, I'm the very BEST there is!",
	"Never argue with a fool, people might not know the difference.",
	"I refuse a battle of wits with an unarmed person.",
	"I never repeat gossip, so please listen carefully the first time.",
	"Hard work never killed anybody, but why take a chance?",
	"If at first you do succeed, try to hide your surprise.",
	"Teamwork is essential, it allows you to blame someone else.",
	"Always lean on your principles, until they give way.",
	"When I was a kid, I ran away from home. My parents changed the locks." };
%>
<html>
<head>
<title>Checkout > ATP Check</title>
<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
</head>
<body>
<br>
<center>
<div class="content" style="width: 60%; height: auto;" align="center">
<br>
<span class="order_step">FreshDirect ATP Check</span>
<hr class="gray1px">
<span class="order"><b>Please wait, checking inventory. This may take up to 60 seconds.<br>
Do not hit the back button on your browser.</b>
<br><br>
<b>Your fortune cookie is:</b><br><br>
<%= FORTUNE[(int)(Math.random()*FORTUNE.length)] %><br><br>
</span>
<img src="/media_stat/images/template/checkout/potato_clock.gif" width="140" height="50" alt="Processing...">
<br><br><br>
</div>

<!-- DON'T REMOVE THIS COMMENT: IE does not render the page until sufficient data is received.. this is *that* data, ya know... -->

<%
out.flush();

try {
	FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
	// this cart has the inventories set
	FDCartModel cart = FDCustomerManager.checkAvailability( user.getIdentity(), user.getShoppingCart(), 30000 );

	boolean isAvailable = cart.isFullyAvailable();

	String resultPage = null;
%>
	<fd:DlvPassAvailabilityController id="unavailPasses" result="result">
<%	
	if (isAvailable && (unavailPasses == null || unavailPasses.size() == 0)) {
		resultPage = request.getParameter("successPage");
	} else {
		// store cart w/ inventories in session, and go to ATP-failure page
		session.setAttribute(SessionName.USER, user);
		resultPage = "checkout_ATP_unavail.jsp?successPage=/checkout/checkout_select_payment.jsp";
	}
	resultPage = response.encodeRedirectURL( resultPage );
	%>
	</fd:DlvPassAvailabilityController>
	<META HTTP-EQUIV="refresh" CONTENT="0;URL=<%=resultPage%>">
	<br>If the page does not refresh automatically, <A HREF="<%=resultPage%>">click here</A>.
<%
} catch (Exception ex) {
	out.print("<B>Error occured:</B><BR>" + ex.getMessage() );
	ex.printStackTrace();
}
%>
</center>
</body>
</html>
<% out.flush(); %>
