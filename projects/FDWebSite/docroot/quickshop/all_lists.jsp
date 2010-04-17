<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Comparator"%>
<%@ page import='java.util.Date' %>
<%@ page import='java.util.Calendar' %>
<%@ page import='java.util.TreeSet' %>
<%@ page import='java.util.List' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.lists.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.framework.util.DateUtil' %>
<%@ page import='com.freshdirect.framework.util.StringUtil' %>
<%@ page import='com.freshdirect.fdstore.content.StarterList' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<%@page import="java.util.Collections"%><fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false' redirectPage='/quickshop/index_guest.jsp?successPage=/quickshop/index.jsp' />
<fd:FDCustomerCreatedList id="lists" action="loadLists">

<%        
    //request.setAttribute("quickshop.level","index");
    request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
    request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
    //request.setAttribute("listPos", "SystemMessage,QSTopRight,QSBottom,LittleRandy");
    //

    String sortField = request.getParameter("sort_selector");
    if (sortField == null) sortField = (String)session.getAttribute("sortField"); 
    if (sortField == null) sortField = "update";
    session.setAttribute("sortField",sortField);
%>		

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
	    
		<img src="/media_stat/images/template/quickshop/yourlists_catnav.gif" border="0" width="81" height="53">
           <font class="space4pix"><br/></font>
     	   <%-- <a href="/quickshop/every_item.jsp"><img src="/media_stat/images/template/quickshop/qs_every_item_catnav.gif" width="80" height="38" border="0"></a><br><font class="space4pix"> --%>
	</tmpl:put>
        <tmpl:put name='content' direct='true'>
							
<TABLE WIDTH="470" CELLPADDING="0" CELLSPACING="0" BORDER="0">
<TBODY>
<%--
<TR>
	<TD WIDTH="100%" align="center">
	<img src="/media_stat/images/template/quickshop/quickshop_header.gif" width="100%" height="75" alt="" border="0">
	</TD>
</TR>
--%>
<TR>
	<TD WIDTH="100%"><img src="/media_stat/images/layout/clear.gif" width="1" height="2" alt="" border="0"></TD>
</TR>
<!--
-->
<TR>
        <TD WIDTH="100%"> 
	   <TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0">
	   <TBODY>
	   <TR>
	      <TD>
	         <TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0">
		 <TR>
		    <TD>
		      <br/>
		      <font class="title18"><b><%= user.getFirstName()%>'s Shopping Lists</b></font>
		    </TD>
		 </TR>
		 <TR>
		    <TD>
	   <br/>
	   <fd:IncludeMedia name="/media/editorial/site_pages/lists/your_lists_intro.html">
	   <b>THE LAST SHOPPING LISTS YOU'LL EVER MAKE!</b>
	   <br/><br/>
	   Create new lists for weekly shopping, your own favorite recipes, 
	   or monthly staples. Keep a list of foods the kids love, your workday lunch items, 
	   or tried-and-true party foods. Never forget an item again.
	   <b>Click a list name to view and modify its contents.</b><br><br>
	   </fd:IncludeMedia>
	            </TD>
		 </TR>
                 <TR>
	              <TD WIDTH="100%" height="1" bgcolor="#996699"><img src="/media_stat/images/layout/996699.gif" width="100%" height="1" alt="" border="0"></TD>
                 </TR>
		 <TR style="height: 60px">
		    <TD align="right" valign="top" >
		       <form id='sort_form' action="/quickshop/all_lists.jsp">
		               <br/>
		               <b>Sort by: </b>
			       <font class="text10">
			       <SELECT id="sort_sel" name="sort_selector" onchange="submit()" class="text9">
			           <OPTION value="name" <%= sortField.equals("name") ? "selected" : "" %> >List Name</OPTION>
			   	   <OPTION value="item_count" <%= sortField.equals("item_count") ? "selected" : "" %> >Number of Items</OPTION>
				   <OPTION value="update" <%= sortField.equals("update") ? "selected" : "" %> >Most recently updated</OPTION>
			       </SELECT>
			       </font>
		       </form>
		    </TD>
		 </TR>
		 <TR>
		    <TD>
		    <TABLE id="CCL_lists" WIDTH="100%" CELLPADDING="0" CELLSPACING="3" BORDER="0">
		    <TR>
                         <TD> <font class="textprbold" color="#996699"><b>LIST NAME</b></font></TD>
                         <TD> <font class="textprbold" color="#996699"><b>UPDATED</b></font></TD>
		    </TR>
<%
			    Comparator<FDCustomerList> comparator = null;
			    if (sortField.equals("update")) comparator = FDCustomerList.getModificationDateComparator();
			    else if (sortField.equals("item_count")) comparator = FDCustomerList.getItemCountComparator();
			    else if (sortField.equals("name")) comparator = FDCustomerList.getNameComparator();
			    
			    Collections.sort( lists, comparator );

				for( FDCustomerList list : lists ) {
					int n = 0;
					if ( list instanceof FDCustomerCreatedList ) n = ((FDCustomerCreatedList)list).getCount();
					else if ( list instanceof FDCustomerListInfo ) n = ((FDCustomerListInfo)list).getCount();
					%>
					<TR>
						<TD>
							<span id="list:<%=list.getId()%>,<%=n%>"><b><a href="/quickshop/shop_from_list.jsp?<%=CclUtils.CC_LIST_ID%>=<%=list.getId()%>"><%= StringUtil.escapeHTML(list.getName())%></a></b> <nobr>(<%=n%> <%= n == 1 ? " Item" :" Items"%>)</nobr></span><br/>
						</TD>
						<TD>
							<%= DateUtil.relativeDifferenceAsString(new Date(), list.getModificationDate()) %>
						</TD>
						<TD>
						   <a href="/unsupported.jsp" onclick="return CCL.rename_list('<%= StringUtil.escapeHTML(StringUtil.escapeJavaScript(list.getName()))%>', this);">RENAME</a>
						</TD>
					</TR>
				<% } %>
				
                   </TABLE>
                   </TD>
			     
		 </TR> 
		 <TR>
		   <TD>
		   <br/>
                        <font class="text9">To create a new list, <a onclick="return CCL.create_list(true, this);" href="/unsupported.jsp">click here.</a></font>
		   </TD>
		 </TR>
	         </TABLE>
	      </TD>
	   </TR>
	   </TBODY>
	   </TABLE>
	</TD>
     </TR>
     <TR>
        <TD WIDTH="100%"><br><br></TD>
     </TR>
     <TR>
        <TD width="100%" height="1" bgcolor="#996699"><img src="/media_stat/images/layout/996699.gif" width="100%" height="1" alt="" border="0"></TD>
     </TR>
     <TR>
        <TD align="left">
	   <div style="height: 15px"></div>
	   <img src="/media_stat/images/template/quickshop/our_fav_lists_hdr.gif" ALT="Our Favorite Lists"/>
	</TD>
     </TR>
     <TR>
        <TD>
	   Need a jump-start to your lists? We've compared notes, and these are our favorites of the
	   lists we use for our own orders.
		</TD>
     </TR>
     <TR>
        <TD>
	   <TABLE id="Starter_lists" style="margin-top: 4px;" cellpadding="0" cellspacing="3" border="0" width="100%">
	      <TBODY>
	      <TR height="1px" style="padding: 0px; margin: 0px" >
	      <TD style="padding: 0px; margin: 0px"><img src="/media_stat/images/layout/clear.gif" width="175px" height="1px"></TD>
	      	<TD>
	      	</TD>
	      </TR>
        	<%
		{ // local block
		   List starterLists = StarterList.getStarterLists(true);
		   int c =0;
         	   for(Iterator I = starterLists.iterator(); I.hasNext(); ++c) {
         	       StarterList starterList = (StarterList)I.next();

		       if (c == 5) {
		   %>
		   <TR>
		      <TD>
		  	     <div style="height: 20px"></div>
		         <a href="/quickshop/all_starter_lists.jsp"><b>View All Favorites &gt;&gt;</b></a>
		      </TD>
		   </TR>
		   <%
		          break;
		       }
            	   %>
	          <TR>
		    <TD style="vertical-align: top;">
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

</fd:FDCustomerCreatedList>
