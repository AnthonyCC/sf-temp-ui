<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.lists.*'%>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.framework.util.DateUtil' %>
<%@ page import='com.freshdirect.framework.util.StringUtil' %>
<%@ page import='java.util.Date' %>
<%@ page import='java.util.List' %>
<%@ page import='java.util.Iterator' %>
<%@ page import='java.util.Comparator' %>
<%@ page import='java.util.Calendar' %>
<%@ page import='java.util.TreeSet' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:FDCustomerCreatedList id="lists" action="loadLists">

<% request.setAttribute("needsCCL","true"); %>

<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>/ FD CRM : Quickshop > Lists /</tmpl:put>

<%        

    FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
    String sortField = request.getParameter("sort_selector");
    if (sortField == null) sortField = (String)session.getAttribute("sortField"); 
    if (sortField == null) sortField = "update";
    session.setAttribute("sortField",sortField);
%>		



<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>
<div class="content_scroll" style="float: left; width:60%;"> 
<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0">
<TBODY>
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
	              <TD WIDTH="100%" height="1" bgcolor="#996699"><img src="/media_stat/images/layout/996699.gif" width="100%" height="1" alt="" border="0"></TD>
                 </TR>
		 <TR>
		    <TD align="right">
		       <form id='sort_form' action="/order/quickshop/all_lists.jsp">
		               <br/>
		               <b>Sort by: </b>
			       <font class="text9">
			       <SELECT id="sort_sel" name="sort_selector" onchange="submit()" class="text9">
			           <OPTION value="name" <%= sortField.equals("name") ? "selected" : "" %> >List Name</OPTION>
			   	   <OPTION value="item_count" <%= sortField.equals("item_count") ? "selected" : "" %> >Number of Items</OPTION>
				   <OPTION value="update" <%= sortField.equals("update") ? "selected" : "" %> >Most recently updated</OPTION>
			       </SELECT>
			       </font>
		       </form>
		    <br/><br/>
		    </TD>
		 </TR>
		 <TR>
		    <TD>
		    <TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0">
<%
			    Comparator comparator = null;
			    if (sortField.equals("update")) comparator = FDCustomerCreatedList.getModificationDateComparator();
			    else if (sortField.equals("item_count")) comparator = FDCustomerCreatedList.getItemCountComparator();
			    else if (sortField.equals("name")) comparator = FDCustomerCreatedList.getNameComparator();
			    TreeSet<FDCustomerListInfo> sorted = new TreeSet<FDCustomerListInfo>(comparator);
			    sorted.addAll(lists);

                            for(Iterator I = sorted.iterator(); I.hasNext();) {
                            	FDCustomerListInfo list = (FDCustomerListInfo)I.next();
			       int n = list.getCount();
%>
                            <TR>
			        <TD>
				   <a href="/order/quickshop/shop_from_list.jsp?<%=CclUtils.CC_LIST_ID%>=<%=list.getId()%>"><b><%= StringUtil.escapeHTML(list.getName())%> (<%=n%> <%= n == 1 ? " Item" :" Items"%>)</b></a><br/>
				</TD>
				<TD>
				   Updated <%= DateUtil.relativeDifferenceAsString(new Date(), list.getModificationDate()) %>
				</TD>
				<TD>
				   <a href="/unsupported.jsp" onclick="return CCL.rename_list('<%= StringUtil.escapeHTML(StringUtil.escapeJavaScript(list.getName()))%>', this);">RENAME</a>
				</TD>
			    </TR>
<%
			    }
%>
                   </TABLE>
                   </TD>
			     
		 </TR>
		 <TR>
		   <TD>
		   <br/>
                        <font class=text9">To create a new list, <a onclick="return CCL.create_list(true,this);" href="/unsupported.jsp">click here.</a></font>
		   </TD>
		 </TR>
	          
	         </TABLE>
	      </TD>
	   </TR>
	   </TBODY>
	   </TABLE>
	</TD>
</TR>
</TBODY>
</TABLE>
</div>
<div class="order_list" style=" float: right;"> 
	<%@ include file="/includes/cart_header.jspf"%>
</div>
<br clear="all"> 
</tmpl:put>



<tmpl:put name='slot4' direct='true'>
</tmpl:put>

</tmpl:insert>
</fd:FDCustomerCreatedList>
