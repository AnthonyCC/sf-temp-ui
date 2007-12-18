<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.fdstore.lists.*' %>
<%@ page import='com.freshdirect.framework.util.DateUtil' %>
<%@ page import='com.freshdirect.framework.util.StringUtil' %>
<%@ page import='com.freshdirect.fdstore.content.StarterList' %>
<%@ page import='java.util.Date' %>
<%@ page import='java.util.Calendar' %>
<%@ page import='java.util.TreeSet' %>
<%@ page import='java.util.List' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false' redirectPage='/quickshop/index_guest.jsp?successPage=/quickshop/index.jsp' />

<tmpl:insert template='/common/template/quick_shop_nav.jsp'>

    <tmpl:put name='extrahead' direct='true'>
        <script language="javascript" src="/assets/javascript/common_javascript.js"></script>
        <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    </tmpl:put>

	<tmpl:put name='title' direct='true'>FreshDirect - Quickshop</tmpl:put>
	<tmpl:put name='side_nav' direct='true'><font class="space4pix"><br></font>
	   <%--
	   <tmpl:put name='banner' direct='true'>
	      <a href="/newproducts.jsp"><img src="/media_stat/images/template/quickshop/qs_banner_newproduct.gif" width="140" height="108" border="0"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br>
	   </tmpl:put>
	   --%>
	    <a href="/quickshop/all_lists.jsp"><img src="/media_stat/images/template/quickshop/yourlists_catnav.gif" border="0" width="81" height="53"></a>
           <font class="space4pix"><br/></font>
           <fd:FDCustomerCreatedList id='lists' action='loadLists'>
           <%
             {
                String style = "text11";
                String selectedListId = "";
           %>
           <%@ include file="/quickshop/includes/cclist_nav.jspf"%>
           <%
             }
           %>
           </fd:FDCustomerCreatedList>
	</tmpl:put>
        <tmpl:put name='content' direct='true'>
							
<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0">
<TBODY>
<TR>
	<TD WIDTH="100%"><img src="/media_stat/images/layout/clear.gif" width="1" height="2" alt="" border="0"></TD>
</TR>
<TR>
        <TD ALIGN="left">
	   <br>
	   <img src="/media_stat/images/template/quickshop/our_fav_lists_hdr.gif" ALT="Our Favorite Lists"/>
	</TD>
</TR>
<TR>
        <TD>
	   <!-- <font class="space4pix"><br/></font> -->
	   Need a jump-start to your lists? We've compared notes, and these are our favorites of the lists we use
	   for our own orders.
	</TD>
</TR>
<TR>
        <TD>
	   <TABLE cellpadding="0" cellspacing="3" style="margin-top: 4px;" border="0" width="100%">
	      <TBODY>
	      <TR height="1px" style="padding: 0px; margin: 0px" >
	      <TD style="padding: 0px; margin: 0px"><img src="/media_stat/images/layout/clear.gif" width="175px" height="1px"></TD>
	      	<TD>
	      	</TD>
	      </TR>
        	<%
		{ // local block
		   List starterLists = StarterList.getStarterLists(true);
         	   for(Iterator I = starterLists.iterator(); I.hasNext(); ) {
         	       StarterList starterList = (StarterList)I.next();
            	   %>
	          <TR>
		    <TD style="vertical-align: top">
         	       <a href="/quickshop/starter_list.jsp?<%=CclUtils.STARTER_LIST_ID%>=<%=starterList.getContentKey().getId()%>"><b><%= StringUtil.escapeHTML(starterList.getFullName()) %></b></a>
		   </TD>
		   <TD style="vertical-align: top">
		       <%=StringUtil.escapeHTML(starterList.getBlurb()) %>
		   </TD>
		  </TR>
         	<%
         	   } // for 
		} // local block
         	%>
	      </TBODY>
	   </TABLE>
	</TD>
     </TR>
</TBODY>
</TABLE>
<br><br>
</tmpl:put>

</tmpl:insert>

</body>
