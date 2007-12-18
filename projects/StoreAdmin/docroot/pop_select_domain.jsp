<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%@ page import ="com.freshdirect.fdstore.content.*"%>
<%@ page import ="com.freshdirect.fdstore.attributes.*"%>
<%@ page import ="java.text.*"%>
<%
    String servletContext = request.getContextPath();
	String expandParam = request.getParameter("expand");

    String expandLink = request.getRequestURI();
	String expansionImg = " + ";


    String action = request.getParameter("action");
    if (action==null) action="select";
    String cancelHref = "javascript:window.close();";
    String saveHref = "pop_select_domain.jsp?action=refreshParent";
    Random rand = new Random();
    String domTypeId = request.getParameter("domainTypeId");
    String domTypeParam="";
    if (domTypeId!=null) {
        domTypeParam ="&domainTypeId="+domTypeId;
    }
System.out.println(" ****** Domain: "+request.getParameter("selectedDomain") + " / Value: "+request.getParameter("selectedDomainValue"));


%>
<sa:DomianRefSelection action='<%=action%>' id='theAttribute' result='result'>
<%
Collection myErrors = result.getErrors();
for(Iterator itE=myErrors.iterator(); itE.hasNext();) {
    ActionError ae = (ActionError)itE.next();
    System.out.println(ae.getType()+"-->"+ae.getDescription());
}

if (result.isSuccess() && "refreshParent".equalsIgnoreCase(request.getParameter("action"))) { %>
<script>
	window.opener.location='<%=(String)session.getAttribute("parentURI")%>?action=updateAttribute';
	window.close();
</script>
<%}  //not closing window...so show stuff %>

<tmpl:insert template='/common/template/pop_right_usrnav.jsp'>
<tmpl:put name='title'>Domain Selector</tmpl:put>
<tmpl:put name='buttons'>
        <table width="45"><tr>
		<td width="20" bgcolor="#CC0000" class="button"><a href="<%=cancelHref%>" class="button">&nbsp;CANCEL&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="5" height="1"></td>
		<td width="20" bgcolor="#006600" class="button"><a href="<%=saveHref%>" class="button">&nbsp;SAVE&nbsp;</a></td>
		<xtd><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></xtd>
		</tr></table>
</tmpl:put>
<tmpl:put name='heading'>Domain/DominValue Selection</tmpl:put>
<tmpl:put name='header'>Domain: <b><%=theAttribute.getKey()%></b></tmpl:put>
<tmpl:put name='contentHeader'>
<table width="100%">
<tr>
    <td width="49%" align="left"><b>Domain Name</b></td>
    <td width="2%"></td>
    <td width="49%"align="left"><b>Domain Value</b></td>
</tr>
</table>
</tmpl:put>
<tmpl:put name='content'>
 <table width="100%">
<%
    List items;
	if (!isMultiAttribute.booleanValue()) {
		items = new ArrayList();
		items.add(theAttribute.getValue());
	} else { 
		items = ((MultiAttribute)theAttribute).getValues();
	}
        String domainName;
        String domainValue;
System.out.println("Items: "+items.size());
   	for (Iterator itemItr=items.iterator(); itemItr.hasNext(); ) {
		ContentRef dRefItem = (ContentRef)itemItr.next();
                if (dRefItem==null) continue;
		domainName = dRefItem.getRefName();
		domainValue = dRefItem.getRefName2();
%>
<tr>
<td width="49"><%=domainName%></td>
<td width="2%">&nbsp;</td>
<td width="49%"><%=domainValue%></td>
</tr>
<%}%>
</table>
</tmpl:put>
<tmpl:put name='navHeader'>
<b>Domain Tree</b>
</tmpl:put>
<tmpl:put name='navBody'>
    <sa:DomainTree id="node" >
       <%
       Domain dom;
        boolean isDomain=false;
       if (node.getType().equals(EnumAttributeType.DOMAINREF.getName())){ 
           isDomain = true;
           dom = ((DomainRef)node).getDomain();
       } else {
          isDomain=false;
          dom = ((DomainValueRef) node).getDomain();
       }
       
       if(domTypeId!=null && !dom.getDomainType().getId().equalsIgnoreCase(domTypeId)) {
           continue;
       }

       if (isDomain){ %>
       <tr><td><a name="<%=node.getRefName()%>" href="pop_select_domain.jsp?expand=<%=node.getRefName()%><%=domTypeParam%>&rnd=<%=rand.nextInt()%>#<%=node.getRefName()%>">&curren;</a>&nbsp;
         <a  href="pop_select_domain.jsp?expand=<%=node.getRefName()%><%=domTypeParam%>&selectedDomain=<%=node.getRefName()%>#<%=node.getRefName()%>"><%=node.getRefName()%></a>
       </td></tr>
       <% } else { %>
       <tr><td>&nbsp;&nbsp;
         <a href="pop_select_domain.jsp?expand=<%=node.getRefName()%><%=domTypeParam%>&selectedDomain=<%=node.getRefName()%>&selectedDomainValue=<%=node.getRefName2()%>#<%=node.getRefName()%>"><%=node.getRefName2()%></a>
       </td></tr>
       <%}%>
    </sa:DomainTree>
</tmpl:put>
</tmpl:insert>
</sa:DomianRefSelection>