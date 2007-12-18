<%@ page autoFlush='false' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.JspLogger' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader ("Expires", 0);
%>
<HTML>
<HEAD>
<TITLE>FreshDirect - Checkout - Checking Inventory</TITLE>
<STYLE>
BODY { font-size: 13px; font-family: Verdana, Arial, sans-serif; }
TD { font-size: 13px; font-family: Verdana, Arial, sans-serif; }
</STYLE>
<SCRIPT LANGUAGE="Javascript">
// <!-- hide me from older browsers

if (document.images) {
	pots = new Array;
	pots[1] = new Image();		pots[1].src = "/media_stat/images/template/checkout/purple_potato.jpg";	//blue
	pots[2] = new Image();		pots[2].src = "/media_stat/images/template/checkout/red_potato.jpg";	//red
	pots[3] = new Image();		pots[3].src = "/media_stat/images/template/checkout/white_potato.jpg";	//white
	
	var flag = new Array( 	new Array(1,1,1,1,2,2,2,2,2,2),
                 			new Array(1,1,1,1,3,3,3,3,3,3),
                 			new Array(1,1,1,1,2,2,2,2,2,2),
                 			new Array(3,3,3,3,3,3,3,3,3,3),
                 			new Array(2,2,2,2,2,2,2,2,2,2),
                 			new Array(3,3,3,3,3,3,3,3,3,3) 		);
} 

function go() {
		i = 0;
		x = 0;
		paintLine();
}

var numLoop = 0;

function paintLine() {
	for (x=0; x<10; x++) {
		document.images["p" + i + "_" + x].src = pots[flag[i][x]].src;
	}
	i++;
	if (i * x <= 50) {
		setTimeout('paintLine()',900);
	} else if ( i * x > 50 ) {
		setTimeout('clearGrid()',1000);
	}
}

function clearGrid() {
	j=0;
	k=0;
	for (j=0; j<6; j++) {
		for (k=0; k<10; k++) {
		document.images["p" + j + "_" + k].src = "/media_stat/images/layout/clear.gif";
		}
	}
	
	if (j * k > 50) {
		a=0;
		b=-1;
		paintItem();
	}
}

function paintItem() {
	b++;
	if (b == 10) {
		a++;	b=0;
	}
	document.images["p" + a + "_" + b].src = pots[flag[a][b]].src;
	
	if (a * b <= 44) {
		setTimeout('paintItem()',400);
	}
}

 // stop hiding -->
</SCRIPT>
</HEAD>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" <%--onLoad="go();"--%>>

<CENTER>

<!-- DON'T REMOVE THIS COMMENT: IE does not render the page until sufficient data is received.. this is *that* data, ya know... -->
<IMG SRC="/media_stat/images/template/checkout/purple_potato.jpg" ALT="Checking Inventory for Your Items..." WIDTH="1" HEIGHT="1">
<IMG SRC="/media_stat/images/template/checkout/red_potato.jpg" ALT="Checking Inventory for Your Items..." WIDTH="1" HEIGHT="1">
<IMG SRC="/media_stat/images/template/checkout/white_potato.jpg" ALT="Checking Inventory for Your Items..." WIDTH="1" HEIGHT="1">
<BR><BR>
<TABLE BORDER="0" CELLSPACING="7">
<tr><td colspan="10"><IMG SRC="/media_stat/images/template/checkout/atp_check_header.gif" ALT="Checking Inventory for Your Items..." WIDTH="599" HEIGHT="94"></td></tr>
<TR>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p0_0" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p0_1" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p0_2" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p0_3" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p0_4" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p0_5" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p0_6" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p0_7" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p0_8" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p0_9" ALT="" WIDTH="50" HEIGHT="55"></TD>
</TR>
<TR>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p1_0" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p1_1" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p1_2" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p1_3" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p1_4" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p1_5" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p1_6" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p1_7" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p1_8" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p1_9" ALT="" WIDTH="50" HEIGHT="55"></TD>
</TR>
<TR>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p2_0" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p2_1" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p2_2" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p2_3" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p2_4" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p2_5" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p2_6" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p2_7" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p2_8" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p2_9" ALT="" WIDTH="50" HEIGHT="55"></TD>
</TR>
<TR>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p3_0" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p3_1" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p3_2" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p3_3" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p3_4" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p3_5" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p3_6" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p3_7" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p3_8" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p3_9" ALT="" WIDTH="50" HEIGHT="55"></TD>
</TR>
<TR>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p4_0" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p4_1" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p4_2" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p4_3" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p4_4" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p4_5" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p4_6" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p4_7" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p4_8" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p4_9" ALT="" WIDTH="50" HEIGHT="55"></TD>
</TR>
<TR>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p5_0" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p5_1" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p5_2" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p5_3" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p5_4" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p5_5" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p5_6" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p5_7" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p5_8" ALT="" WIDTH="50" HEIGHT="55"></TD>
<TD><IMG SRC="/media_stat/images/layout/clear.gif" NAME="p5_9" ALT="" WIDTH="50" HEIGHT="55"></TD>
</TR>
<tr><td colspan="10"><img src="/media_stat/images/layout/999966.gif" width="599" height="1" border="0"></td></tr>

</TABLE>
<script>
setTimeout('go()',300);
</script>

<!-- DON'T REMOVE THIS COMMENT: IE does not render the page until sufficient data is received.. this is *that* data, ya know... -->

<%
out.flush();

try {
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	

	FDCartModel cart = FDCustomerManager.checkAvailability( user.getIdentity(), user.getShoppingCart(), 30000 );
	// recalculate promotions
	user.updateUserState();

	boolean isAvailable = cart.isFullyAvailable();

	String resultPage = null;
		
	String successPage = request.getParameter("successPage");
%>	
	<fd:DlvPassAvailabilityController id="unavailPasses" result="result">
<%	
	if (isAvailable && (unavailPasses == null || unavailPasses.size() == 0)) {
		resultPage = successPage;
	} else {
		// store cart w/ inventories in session, and go to ATP-failure page
		//session.setAttribute(SessionName.USER, user);
		resultPage = "/checkout/step_2_unavail.jsp?successPage=" + successPage;
	}
	resultPage = response.encodeRedirectURL( resultPage );
	%>
	</fd:DlvPassAvailabilityController>
	<META HTTP-EQUIV="refresh" CONTENT="0;URL=<%=resultPage%>">
	<BR>If the page does not refresh automatically, <A HREF="<%=resultPage%>">click here</A>.
<%
} catch (Exception ex) {
	ex.printStackTrace();
%>
	<BR><BR>
	<B>Error occured:</B><BR>
	<%= ex %><BR><BR>
	<B>Please, try again.</BR>
<%
}
%>
</CENTER>
</BODY>
</HTML>
<% out.flush(); %>