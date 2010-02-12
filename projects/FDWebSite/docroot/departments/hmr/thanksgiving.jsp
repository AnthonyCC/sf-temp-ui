<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*'%>
<%@ page import='java.net.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus guestAllowed="true" />
<% 

String catId = request.getParameter("catId"); 

%>
<tmpl:insert template='/common/template/right_dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Thanksgiving Feast</tmpl:put>
    <tmpl:put name='content' direct='true'><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
<TABLE border=0 cellPadding=0 cellSpacing=0 width=524>
<TR><TD>
<IMG src="/media_stat/images/template/kitchen/thanksgiving_hdr2.gif" WIDTH="524" HEIGHT="105" BORDER="0" alt="THANKSGIVING FEAST - Order by Sunday Nov. 24"><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10"><BR>
<FONT class=text12>Your turn to cook this year? We have you covered - from gorgeous all-natural fresh roasted turkey to spiced pumpkin pie, with every glorious morsel in between. Our chefs <a href="javascript:pop('/media/editorial/manager_bios/david_mcInerney.jsp',400,585)">David McInerney</a> (formerly of Bouley and One If by Land, Two If by Sea) and <a href="javascript:pop('/media/editorial/manager_bios/michael_stark.jsp',400,585)">Michael Stark</a> (formerly of Tribeca Grill) will be preparing every dish using top quality ingredients, family recipes, and professional know-how so it's bound to be exceptionally delicious. When we bring it to your door, all you'll have to do is heat and serve, so you can spend time with your family.<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="14"><BR><div align="center"><IMG src="/media_stat/images/template/kitchen/thanksgiving_sub_hdr.gif" WIDTH="507" HEIGHT="63" BORDER="0" alt="thanksgiving sub header"><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="12"><BR><img src="/media_stat/images/template/kitchen/turkey.jpg" WIDTH="252" HEIGHT="182" BORDER="0" alt="thanksgiving turkey"><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="14"><BR><img src="/media_stat/images/template/kitchen/thanksgiving_menu.gif" WIDTH="256" HEIGHT="14" BORDER="0" alt="thanksgiving menu">
<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10"><BR>
<font color="#996699"><b>Choose Fresh Roasted Turkey:</b></font>
<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
<b>$79.99</b> - 8-10lb Turkey (Serves 4)<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR> <b>$119.99</b> - 10-12lb Turkey (Serves 6)<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
<b>$159.99</b> - 12-14lb Turkey (Serves 8)<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
<b>$199.99</b> - 14-16lb Turkey (Serves 10)<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
<b>$239.99</b> - 16-18lb Turkey (Serves 12)<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
<b>$279.99</b> - 18-20lb Turkey (Serves 14)<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
<b>$319.99</b> - 20-22lb Turkey (Serves 16)<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
<b>$359.99</b> - 22-24lb Turkey (Serves 18)<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
<b>$399.99</b> - 24-26lb Turkey (Serves 20)<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
<b>Our delivery fee is $3.95</b>
<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="18"><BR>
<font color="#996699"><b>Included Side Dishes:</b></font><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Cranberry Orange Relish<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Parbaked Dinner Rolls<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Zucchini Bread
<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="18"><BR>
<font color="#996699"><b>Your choice of Stuffing:</b></font><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Sweet Italian Sausage & Herb Stuffing<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Dried Fruit & Nut Stuffing
<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="18"><BR>
<font color="#996699"><b>Your choice of Gravy:</b></font><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Traditional Turkey Gravy<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Turkey Au Jus
<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="18"><BR>
<font color="#996699"><b>Your choice of four Side Dishes:</b></font><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Almost Perfect Mashed Potatoes<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Brown-sugar Candied Sweet Potatoes<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Creamed Pearl Onions<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Butter-glazed Carrots<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Green Beans<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Creamed Sweet Corn
<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="18"><BR>
<font color="#996699"><b>Your choice of Desserts:</b></font><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Spiced Pumpkin Pie<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
New England Apple Pie<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Carolina Pecan Pie<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Dark Double Chocolate Layer Cake
<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="18"><BR>
<b>Phone orders only.<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"><BR>
Please call us toll free 1-866-283-7374 for more information.</b></FONT></TD>
</TR></TABLE>
    </tmpl:put>	
<%//@ include file="/includes/i_promotion_counter.jspf" %>
</tmpl:insert>