<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='java.util.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" />

<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name='title' direct='true'>Delivery Information</tmpl:put>
		<tmpl:put name='content' direct='true'>
<table width="693" border="0" cellpadding="0" cellspacing="0">
     <tr>
          <td><img src="/media_stat/images/layout/clear.gif" width="506" height="18"></td>
          <td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="13" height="1"></td>
          <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
          <td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="13" height="1"></td>
          <td><img src="/media_stat/images/layout/clear.gif" width="160" height="1"></td>
    </tr>
          
    <tr valign="top">
    <%
    boolean fromZipCheck = false;
    boolean isPopup = false;
    %>
          <td class="text12"><%@ include file="/shared/includes/delivery/i_lic_pickup.jspf"%><br><br><br></td>
          <td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
          <td align="center"><img src="/media_stat/images/template//help/delivery/download_maps.gif" width="144" height="52" border="0" alt="Download Printable Maps & Directions to our Facility "><br><img src="/media_stat/images/template/help/delivery/pickup_map.gif" width="151" height="115" border="0" alt="" vspace="8"><br><table width="100%" cellpadding="0" cellspacing="0"><tr><td><img src="/media_stat/images/template/help/delivery/pdf_ico.gif" width="40" height="35" border="0" alt="" vspace="6"></td><td><a href="/media_stat/pdf/map_manhattan.pdf" target="fd_pdf">From<br>Manhattan</a></td></tr><tr><td><img src="/media_stat/images/template/help/delivery/pdf_ico.gif" width="40" height="35" border="0" alt="" vspace="6"></td><td><a href="/media_stat/pdf/map_li_queens.pdf" target="fd_pdf">From<br>Long Island/Queens</a></td></tr><tr><td><img src="/media_stat/images/template/help/delivery/pdf_ico.gif" width="40" height="35" border="0" alt="" vspace="6"></td><td><a href="/media_stat/pdf/map_brooklyn.pdf" target="fd_pdf">From<br>Brooklyn</a></td></tr><tr><td><img src="/media_stat/images/template/help/delivery/pdf_ico.gif" width="40" height="35" border="0" alt="" vspace="6"></td><td><a href="/media_stat/pdf/map_bronx.pdf" target="fd_pdf">From<br>The Bronx</a></td></tr>
<tr><td colspan="2"><img src="/media_stat/images/layout/999966.gif" width="100%" height="1" vspace="10"></td></tr>
<tr><td colspan="2"><b>All documents in Adobe<br>Acrobat&copy;</b> format <a href="http://www.adobe.com/products/acrobat/readstep2.html" target="_blank">Click here<br>to download</a> the free Acrobat<br><a href="http://www.adobe.com/products/acrobat/readstep2.html" target="_blank"><img src="/media_stat/images/template/help/delivery/getacro.gif" width="88" height="31" border="0" alt="" vspace="10"></a></td></tr>
</table>
</td></tr>
</table>
</tmpl:put>
</tmpl:insert>