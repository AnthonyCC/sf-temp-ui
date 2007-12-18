<%@ page import="java.util.*,java.text.*,java.io.*" %>
<%@ page import="com.freshdirect.webapp.util.JspTableSorter" %>
<%@ page import="com.freshdirect.framework.util.*" %>
<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.payment.fraud.*" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<crm:CrmGetRestrictedPaymentMethodList id="restrictedPaymentMethodList">
<crm:GetCurrentAgent id="currentAgent">
<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>Supervisor Resources > Bad Checking Accounts</tmpl:put>
<tmpl:put name='content' direct='true'>
<jsp:include page="/includes/supervisor_nav.jsp" />
<div class="sub_nav">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
<%!

private static Comparator COMP_BANK_ACCOUNT_TYPE = new Comparator () {
    public int compare(Object o1, Object o2) {
        RestrictedPaymentMethodModel p1 = (RestrictedPaymentMethodModel)o1;
        RestrictedPaymentMethodModel p2 = (RestrictedPaymentMethodModel)o2;
        
        if(p1.getBankAccountType() == null) return 1;
        if(p2.getBankAccountType() == null) return -1;

        return p1.getBankAccountType().compareTo(p2.getBankAccountType());
    }
};

private static Comparator COMP_ACCOUNT_NUMBER = new Comparator () {
    public int compare(Object o1, Object o2) {
        RestrictedPaymentMethodModel p1 = (RestrictedPaymentMethodModel)o1;
        RestrictedPaymentMethodModel p2 = (RestrictedPaymentMethodModel)o2;
        
        return p1.getAccountNumber().compareTo(p2.getAccountNumber());
    }
};

private static Comparator COMP_ABA_ROUTE_NUMBER = new Comparator () {
    public int compare(Object o1, Object o2) {
        RestrictedPaymentMethodModel p1 = (RestrictedPaymentMethodModel)o1;
        RestrictedPaymentMethodModel p2 = (RestrictedPaymentMethodModel)o2;
        
        return p1.getAbaRouteNumber().compareTo(p2.getAbaRouteNumber());
    }
};

private static Comparator COMP_NAME = new Comparator () {
    public int compare(Object o1, Object o2) {
        RestrictedPaymentMethodModel p1 = (RestrictedPaymentMethodModel)o1;
        RestrictedPaymentMethodModel p2 = (RestrictedPaymentMethodModel)o2;
        String name1 = p1.getLastName() + ", " + p1.getFirstName();
        String name2 = p2.getLastName() + ", " + p2.getFirstName();
                
        return name1.toLowerCase().compareTo(name2.toLowerCase());
    }
};

private static Comparator COMP_CREATE_DATE = new Comparator () {
    public int compare(Object o1, Object o2) {
        RestrictedPaymentMethodModel p1 = (RestrictedPaymentMethodModel)o1;
        RestrictedPaymentMethodModel p2 = (RestrictedPaymentMethodModel)o2;
        
        if(p1.getCreateDate() == null) return 1;
        if(p2.getCreateDate() == null) return -1;
        
        return p2.getCreateDate().compareTo(p1.getCreateDate());
    }
};

private static Comparator COMP_CREATE_USER = new Comparator () {
    public int compare(Object o1, Object o2) {
        RestrictedPaymentMethodModel p1 = (RestrictedPaymentMethodModel)o1;
        RestrictedPaymentMethodModel p2 = (RestrictedPaymentMethodModel)o2;
        
        return p1.getCreateUser().toLowerCase().compareTo(p2.getCreateUser().toLowerCase());
    }
};

public static Comparator COMP_REASON = new Comparator() { 
    public int compare(Object o1, Object o2) { 
    	RestrictedPaymentMethodModel p1 = (RestrictedPaymentMethodModel)o1;
    	RestrictedPaymentMethodModel p2 = (RestrictedPaymentMethodModel)o2;
    	return p1.getReason().compareTo(p2.getReason());
    } 
};

public static Comparator COMP_NOTE = new Comparator() { 
    public int compare(Object o1, Object o2) { 
    	RestrictedPaymentMethodModel p1 = (RestrictedPaymentMethodModel)o1;
    	RestrictedPaymentMethodModel p2 = (RestrictedPaymentMethodModel)o2;
        if(p1.getNote() == null) return 1;
        if(p2.getNote() == null) return -1;
        return p1.getNote().toLowerCase().compareTo(p2.getNote().toLowerCase());
    } 
};

public final static Map RPM_COMPARATORS = new HashMap();
static {
	RPM_COMPARATORS.put("bankAccountType", COMP_BANK_ACCOUNT_TYPE);
	RPM_COMPARATORS.put("accountNumber", COMP_ACCOUNT_NUMBER);
	RPM_COMPARATORS.put("abaRouteNumber", COMP_ABA_ROUTE_NUMBER);
	RPM_COMPARATORS.put("name", COMP_NAME);
    RPM_COMPARATORS.put("createDate", COMP_CREATE_DATE);
    RPM_COMPARATORS.put("createUser", COMP_CREATE_USER);
	RPM_COMPARATORS.put("reason", COMP_REASON);
	RPM_COMPARATORS.put("note", COMP_NOTE);
}

private static int NOTE_MAX_LENGTH = 25;

%>

<%    
JspTableSorter sort = new JspTableSorter(request);
%>

<form name="bad_account_list" method="post">
<script language="JavaScript">

    function sortForm(thisForm, sortBy, sortOrder) {
    	if (thisForm.sortBy.value != sortBy) {
			thisForm.sortOrder.value = 'Asc';
    	} else {
			thisForm.sortOrder.value = sortOrder;
    	}
		thisForm.sortBy.value = sortBy;
		checkForm(thisForm);
	}

    function checkForm(thisForm) {
        var okToSubmit= true;
        var createDay = thisForm.create_day.value;
        var createMonth = thisForm.create_month.value;
        var createYear = thisForm.create_year.value;
        var abaRouteNumber = thisForm.aba_route_number.value;
        var accountNumber = thisForm.account_number.value;
        
        if (createDay.length<2) createDay="0"+createDay;
        if (createMonth.length<2) createMonth="0"+createMonth;
        var createDate=createYear+createMonth+createDay;
                	
        if (createDate.length > 2 && createDate.length!=8 ) {
            alert('The CREATE DATE field is invalid. Please correct and try again.');
            okToSubmit = false;
        }
        
        if(isNaN(abaRouteNumber)) {
            alert('The ABA ROUTE NUMBER field is invalid. Please correct and try again.');
            okToSubmit = false;
        }

        if(isNaN(accountNumber)) {
            alert('The ACCOUNT NUMBER field is invalid. Please correct and try again.');
            okToSubmit = false;
        }   
                             
        if (okToSubmit) {
            thisForm.submit();
        }
    }    

</script>
    <tr>
        <td width="10%" align="left">Routing #:</td>
        <td width="10%" align="left">Account #:</td>
        <td width="10%" align="left">Account Type:</td>
        <td width="10%" align="left">First Name:</td>
        <td width="10%" align="left">Last Name:</td>
        <td width="20%" align="left">Created:</td>
        <td width="10%" align="left">Reason:</td>
        <td width="5%" align="left"></td>
        <td width="15%" align="left"></td>
    </tr>
    <tr>
        <td width="10%" align="left"><input type="text" name="aba_route_number" size="9" maxlength="9" class="text" value="<%=request.getParameter("aba_route_number")%>">&nbsp;</td>
        <td width="10%" align="left"><input type="text" name="account_number" size="15" maxlength="15" class="text" value="<%=request.getParameter("account_number")%>">&nbsp;</td>
		<td width="10%" align="left">
            <select name="bank_account_type" required="true" class="pulldown">
                <option value=""></option>
                            <%
                            String bankAccountType = request.getParameter("bank_account_type");  
                            Iterator iterBAT = EnumBankAccountType.iterator();   
                            while (iterBAT.hasNext()) {
                            	EnumBankAccountType bat = (EnumBankAccountType) iterBAT.next(); 
                            %>
                <option value="<%= bat.getName() %>" <%= (bat.getName().equals(bankAccountType))?"selected":"" %>><%= bat.getDescription() %></option>
                            <%  } %>
          </select>&nbsp;
        </td>        
        <td width="10%" align="left"><input type="text" name="first_name" size="20" maxlength="35" class="text" value="<%=request.getParameter("first_name")%>">&nbsp;</td>
        <td width="10%" align="left"><input type="text" name="last_name" size="20" maxlength="35" class="text" value="<%=request.getParameter("last_name")%>">&nbsp;</td>
        <td width="20%" align="left">
        	<select name="create_month" required="true" class="pulldown">
                <option value="">Month</option>
                            <%
							DateFormatSymbols symbols = new DateFormatSymbols();
							String cm = request.getParameter("create_month");
							int createMonth = (cm != null && !"".equals(cm)) ? Integer.parseInt(cm) : -1;
                            for (int i=0; i<12; i++) {  %>
                            <option value="<%= i+1 %>" <%= (i+1==createMonth)?"selected":"" %>><%= symbols.getShortMonths()[i] %></option>
                            <%  }   %>
            </select>&nbsp;            
            <select name="create_day" required="true" class="pulldown">
                <option value="">Day</option>
                            <%  
                            String cd = request.getParameter("create_day");  
							int createDay = (cd != null && !"".equals(cd)) ? Integer.parseInt(cd) : -1;
                            for (int i=1; i<=31; i++) { %>
                <option value="<%= i %>" <%= (i==createDay)?"selected":"" %>><%= i %></option>
                            <%  } %>
            </select>&nbsp;	
            <select name="create_year" required="true" class="pulldown">
                <option value="">Year</option>
                            <%  
                            String cy = request.getParameter("create_year");  
							int createYear = (cy != null && !"".equals(cy)) ? Integer.parseInt(cy) : -1;
                            for (int i=2001; i<2011; i++) { %>
                <option value="<%= i %>" <%= (i==createYear)?"selected":"" %>><%= i %></option>
                            <%  } %>
            </select>&nbsp;&nbsp;
		</td>
        <td width="10%" align="left">
            <select name="reason_code" required="true" class="pulldown">
                <option value=""></option>
                            <%
                            String reasonCode = request.getParameter("reason_code");  
                            Iterator iter = EnumRestrictionReason.iterator();   
                            while (iter.hasNext()) {
                            	EnumRestrictionReason reason = (EnumRestrictionReason) iter.next(); 
                            %>
                <option value="<%= reason.getName() %>" <%= (reason.getName().equals(reasonCode))?"selected":"" %>><%= reason.getDescription() %></option>
                            <%  } %>
            </select>&nbsp;        
        </td>
        <td width="5%" align="left"><input type="submit" class="submit" onClick="javascript:checkForm(bad_account_list); return false;" value="GO">&nbsp;</td>
        <td width="15%" align="right"><a href="javascript:pop('/supervisor/add_bad_account.jsp', 375, 780);">Add Account</a>&nbsp;</td>
	    <input type="hidden" name="status" value="<%= EnumRestrictedPaymentMethodStatus.BAD.getName() %>">
	    <input type="hidden" name="display_results" value="true">
    </tr>
	    <input type="hidden" name="sortBy" value="<%=request.getParameter("sortBy")%>">
	    <input type="hidden" name="sortOrder" value="<%=request.getParameter("sortOrder")%>">
    </form>
</table>
</div>
<%
if (restrictedPaymentMethodList.size() > 0) {    
	SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
	int noteLength = 0;
%>

<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_header_text">
    <tr>
		<td width="1%">&nbsp;</td>
        <td width="10%"><a href="javascript:sortForm(bad_account_list, 'accountNumber', '<%= sort.isAscending() ? "desc": "asc"%>');" class="list_header_text">Account&nbsp;#</a></td>
        <td width="10%"><a href="javascript:sortForm(bad_account_list, 'abaRouteNumber', '<%= sort.isAscending() ? "desc": "asc"%>');" class="list_header_text">Routing&nbsp;#</a></td>
        <td width="10%"><a href="javascript:sortForm(bad_account_list, 'bankAccountType', '<%= sort.isAscending() ? "desc": "asc"%>');" class="list_header_text">Account&nbsp;Type</a></td>
        <td width="15%"><a href="javascript:sortForm(bad_account_list, 'name', '<%= sort.isAscending() ? "desc": "asc"%>');" class="list_header_text">Name&nbsp;on&nbsp;account</a></td>
        <td width="10%"><a href="javascript:sortForm(bad_account_list, 'createDate', '<%= sort.isAscending() ? "desc": "asc"%>');" class="list_header_text">Created&nbsp;Date</a></td>
        <td width="10%"><a href="javascript:sortForm(bad_account_list, 'createUser', '<%= sort.isAscending() ? "desc": "asc"%>');" class="list_header_text">By</a></td>
        <td width="15%"><a href="javascript:sortForm(bad_account_list, 'reason', '<%= sort.isAscending() ? "desc": "asc"%>');" class="list_header_text">Reason</a></td>
        <td width="10%"><a href="javascript:sortForm(bad_account_list, 'note', '<%= sort.isAscending() ? "desc": "asc"%>');" class="list_header_text">Note</a></td>
        <td width="9%">&nbsp;</td>
    </tr>
</table>
</div>
<div class="list_content" id="result">
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">

<%

Comparator comp = (Comparator)RPM_COMPARATORS.get(sort.getSortBy());
if (comp == null) {
	Collections.sort(restrictedPaymentMethodList, COMP_ACCOUNT_NUMBER);
} else {
	if (comp.equals(COMP_CREATE_DATE)) {
		Collections.sort(restrictedPaymentMethodList, sort.isAscending() ? new ReverseComparator(comp) : comp);
	} else {
		Collections.sort(restrictedPaymentMethodList, sort.isAscending() ? comp : new ReverseComparator(comp));
	}
}

%>
<logic:iterate id="restrictedPaymentMethod" collection="<%= restrictedPaymentMethodList %>" type="com.freshdirect.payment.fraud.RestrictedPaymentMethodModel" indexId="counter">
        <tr <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>>
			<td width="1%">&nbsp;</td>
            <td width="10%" class="border_bottom"><%=restrictedPaymentMethod.getAccountNumber()%></td>
            <td width="10%" class="border_bottom"><%=restrictedPaymentMethod.getAbaRouteNumber()%></td>
            <td width="10%" class="border_bottom"><%=(restrictedPaymentMethod.getBankAccountType()!=null)? restrictedPaymentMethod.getBankAccountType().getDescription():"&nbsp;"%></td>
            <td width="15%" class="border_bottom"><%=restrictedPaymentMethod.getLastName() + ", " + restrictedPaymentMethod.getFirstName() %></td>
            <td width="10%" class="border_bottom"><%=sdf.format(restrictedPaymentMethod.getCreateDate())%></td>
            <td width="10%" class="border_bottom"><%=restrictedPaymentMethod.getCreateUser()%></td>
            <td width="15%" class="border_bottom"><%=(restrictedPaymentMethod.getReason()!=null)? restrictedPaymentMethod.getReason().getDescription():"&nbsp;"%></td>
            <% noteLength = (restrictedPaymentMethod.getNote()!= null && restrictedPaymentMethod.getNote().length() < NOTE_MAX_LENGTH) ? restrictedPaymentMethod.getNote().length() : NOTE_MAX_LENGTH; %>
            <td width="10%" class="border_bottom"><%=(restrictedPaymentMethod.getNote()!=null)? restrictedPaymentMethod.getNote().substring(0, noteLength):"&nbsp;"%></td>
            <td width="9%" class="border_bottom" align="right"><% if (currentAgent.isSupervisor()) { %><a href="javascript:pop('/supervisor/remove_bad_account.jsp?restrict_payment_method_id=<%=restrictedPaymentMethod.getId()%>', 350, 500);">Remove</a><% } %>&nbsp;</td>
        </tr>
</logic:iterate>
    </table>
</div>
<%      } else {
			if ("true".equalsIgnoreCase(request.getParameter("display_results"))) {
%>
    <div class="content_fixed" align="center"><br><b>No accounts that fit your selection criteria found</b><br><br></div>
<%      	}   %>
<%      }   %>
</tmpl:put>
</tmpl:insert>
</crm:GetCurrentAgent>
</crm:CrmGetRestrictedPaymentMethodList>
