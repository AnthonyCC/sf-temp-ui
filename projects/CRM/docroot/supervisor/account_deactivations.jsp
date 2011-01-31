<%@ page errorPage="/main/error.jsp"%>
<%@ page import='java.text.*, java.util.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="java.util.*"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!	private class CustomerNameComparator implements Comparator {
        
		public int compare(Object o1, Object o2) {
			try {
				ErpActivityRecord rec1 = (ErpActivityRecord) o1;
				ErpActivityRecord rec2 = (ErpActivityRecord) o2;
				
				// Get ErpCustomerInfoModel for each activity
				ErpCustomerInfoModel ci1 = FDCustomerFactory.getErpCustomerInfo(rec1.getCustomerId());
				ErpCustomerInfoModel ci2 = FDCustomerFactory.getErpCustomerInfo(rec2.getCustomerId());
				
				String name1 = ci1.getLastName() + ci1.getFirstName();
				String name2 = ci2.getLastName() + ci2.getFirstName();
				
				return name1.toLowerCase().compareTo(name2.toLowerCase());
			} catch (Exception ex) { return 0; }
        } // class CustomerNameComparator
	}
	CustomerNameComparator custNameComparator = new CustomerNameComparator();
	
    private class DateComparator implements Comparator {
        
		public int compare(Object o1, Object o2) {
			ErpActivityRecord rec1 = (ErpActivityRecord) o1;
			ErpActivityRecord rec2 = (ErpActivityRecord) o2;
			return ( rec1.getDate().compareTo(rec2.getDate()) );
        } // class DateComparator
	}
	DateComparator dateComparator = new DateComparator();
	
	private class CsrComparator implements Comparator {
        
		public int compare(Object o1, Object o2) {
			ErpActivityRecord rec1 = (ErpActivityRecord) o1;
			ErpActivityRecord rec2 = (ErpActivityRecord) o2;
			return rec1.getInitiator().toLowerCase().compareTo(rec2.getInitiator().toLowerCase());
        } // class CsrComparator
	}
	CsrComparator csrComparator = new CsrComparator();
	
	private class NoteComparator implements Comparator {
        
		public int compare(Object o1, Object o2) {
			try {
				ErpActivityRecord rec1 = (ErpActivityRecord) o1;
				ErpActivityRecord rec2 = (ErpActivityRecord) o2;
				return rec1.getNote().toLowerCase().compareTo(rec2.getNote().toLowerCase());
			} catch (Exception ex) { return 0; }
        } // class CsrComparator
	}
	NoteComparator notesComparator = new NoteComparator();
%>
<%!	int DISPLAY_COUNT = 30; %>
<%	int pageCount = 0;
	if (request.getParameter("page") != null) {
		pageCount = Integer.parseInt( request.getParameter("page") );
	}
	String compareByParam = request.getParameter("compareBy");
	int compareBy = 0;
	if (compareByParam != null) {
		compareBy = Integer.parseInt(compareByParam);
	}
%>

<tmpl:insert template='/template/supervisor_resources.jsp'>

	<tmpl:put name='title' direct='true'>Supervisor Resources > Account Deactivations</tmpl:put>

<%
//
// get deactivations in last 30 days
//
ErpActivityRecord template = new ErpActivityRecord();
template.setActivityType(EnumAccountActivityType.DEACTIVATE_ACCOUNT);

Calendar startDate = Calendar.getInstance();
startDate.add(Calendar.DATE, -30);
template.setDate(startDate.getTime());
java.util.Set set=new java.util.HashSet();
Collection deactivations = ActivityLog.getInstance().findActivityByTemplate(template);
Collection filteredDeactivations=new java.util.ArrayList();
java.util.Iterator iterator=deactivations.iterator();
while(iterator.hasNext()){
   com.freshdirect.customer.ErpActivityRecord rcd=(com.freshdirect.customer.ErpActivityRecord)iterator.next();
   if(!set.contains(rcd.getCustomerId())){     
     filteredDeactivations.add(rcd);
     set.add(rcd.getCustomerId());
   }
}
%>

<%	Comparator comparator = null;
		switch (compareBy) {
			case 0:
				comparator = custNameComparator;
				break;
			case 1:
				comparator = dateComparator;
				break;
			case 2:
				comparator = csrComparator;
				break;
			case 3:
				comparator = notesComparator;
				break;
			default:
				comparator = custNameComparator;
				break;
		}
		ArrayList sortedDeactivations = new ArrayList(filteredDeactivations);
		Collections.sort(sortedDeactivations, comparator);
        ArrayList finalDeactivation=new ArrayList();
        
        java.util.Iterator iterator1=sortedDeactivations.iterator();
        while(iterator1.hasNext()){        
            com.freshdirect.customer.ErpActivityRecord rec=(com.freshdirect.customer.ErpActivityRecord)iterator1.next();        
            FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomerFromErpId(rec.getCustomerId());
            if(FDCustomerManager.isCustomerActive(fdCustomer.getErpCustomerPK())){             
                  continue;
            }
            finalDeactivation.add(rec);  
          
        }                
        
	%>
	<tmpl:put name='content' direct='true'>
	
	<div class="sub_nav">
	<table width="100%" cellpadding="0" cellspacing="2" border="0" ALIGN="CENTER" class="sub_nav_text">
		<form method="POST" name="customer_details">
		<tr>
			<td width="30%"><span class="sub_nav_title">Account Deactivations</span> ( <span class="result"><%= finalDeactivation.size() %></span> )</td>
			<td width="70%" align="right"><B>page 
	<%	for (int numPages = 0; numPages * 30 <= finalDeactivation.size(); numPages++) {
			if (numPages == pageCount) { %>
				<%= (numPages+1) %> .
	<%		} else { %>
				<A HREF="/supervisor/account_deactivations.jsp?page=<%= numPages %>&compareBy=<%= compareBy %>"><%= (numPages+1) %></a> .
	<%		}
		} %>
				</B>
			</td>
		</tr>
	</table>
	</div>
	
	<div class="content" style="height: 80%;">
		<div class="list_header" style="padding-top: 4px; padding-bottom: 4px;">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER">
			<tr>
				<td width="2%">&nbsp;</td>
				<td width="15%"><a href="account_deactivations.jsp?compareBy=0&page=<%= pageCount %>" class="list_header_text">Customer Name</a></td>
				<td width="1%">&nbsp;</td>
				<td width="15%"><a href="account_deactivations.jsp?compareBy=1&page=<%= pageCount %>" class="list_header_text">Date</a></td>
				<td width="1%">&nbsp;</td>
				<td width="15%"><a href="account_deactivations.jsp?compareBy=2&page=<%= pageCount %>" class="list_header_text">Agent</a></td>
				<td width="1%">&nbsp;</td>
				<td width="50%"><a href="account_deactivations.jsp?compareBy=3&page=<%= pageCount %>" class="list_header_text">Notes</a></td>
				<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
			</tr>
		</table>
		</div>
		<div class="list_content" style="height: 95%;">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="list_content_text">
	
		<logic:iterate id="rec" collection="<%= finalDeactivation %>" type="com.freshdirect.customer.ErpActivityRecord" indexId="idx" length="<%= String.valueOf(DISPLAY_COUNT) %>" offset="<%= String.valueOf(pageCount * DISPLAY_COUNT) %>">
	<%	
        FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomerFromErpId(rec.getCustomerId());
		ErpCustomerInfoModel customerInfo = FDCustomerFactory.getErpCustomerInfo(rec.getCustomerId()); %>
			<tr valign="top" <%= idx.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;" onClick="document.location='<%= response.encodeURL("/main/account_details.jsp?erpCustId=" + rec.getCustomerId() + "&fdCustId=" + fdCustomer.getPK().getId()) %>'">
				<td width="2%">&nbsp;</td>
				<td width="15%"><A HREF="/main/account_details.jsp?erpCustId=<%= rec.getCustomerId() %>&fdCustId=<%= fdCustomer.getPK().getId() %>" class="key"><%= customerInfo.getLastName() %>, <%= customerInfo.getFirstName() %></A><BR></td>
				<td width="1%">&nbsp;</td>
				<td width="15%"><%= CCFormatter.formatDate( rec.getDate() ) %><BR></td>
				<td width="1%">&nbsp;</td>
				<td width="15%"><%= rec.getInitiator() %><BR></td>
				<td width="1%">&nbsp;</td>
				<td width="50%"><%= rec.getNote() %><BR></td>
			</tr>
			<tr><td colspan="8" class="list_separator" style="padding: 0px;"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
		</logic:iterate>
		</table>
		</div>
	</div>

	</tmpl:put>

</tmpl:insert>
