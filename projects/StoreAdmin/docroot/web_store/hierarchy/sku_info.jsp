<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='storeadmin' prefix='sa' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import ="com.freshdirect.framework.webapp.*" %>
<%@ page import ="com.freshdirect.fdstore.content.*"%>
<%@ page import ="com.freshdirect.fdstore.attributes.*"%>
<%@ page import ="com.freshdirect.fdstore.FDProduct"%>
<%@ page import ="com.freshdirect.fdstore.FDProductInfo"%>
<%@ page import ="com.freshdirect.fdstore.FDCachedFactory"%>
<%@ page import ="com.freshdirect.erp.ErpFactory"%>
<%@ page import ="com.freshdirect.erp.model.ErpMaterialModel"%>
<%--@ page import ="com.freshdirect.erp.model.ErpProductModel"--%>
<%@ page import ="java.text.*"%>
<%!
  SimpleDateFormat modByFormat = new SimpleDateFormat("hh:mm:ss MM/dd/yyyy");
%>

<%
String servletContext = request.getContextPath();
String params = request.getQueryString();
String URL = request.getRequestURI() + "?" + request.getQueryString();

String action = request.getParameter("action")==null ? "" : request.getParameter("action");
String nodeId = request.getParameter("nodeId");

String saveHref = "javascript:skuDetailsForm.submit();";
String frmNewSkuCode = request.getParameter("newSkuCode");
if (frmNewSkuCode==null) frmNewSkuCode ="";

%>
<sa:SkuController action='<%=action%>' result='result' nodeId='<%=nodeId%>' id='pm'>
<%
//dump out errors for debugging purposes

String nodePkId = pm.getPK()==null ? "" : pm.getPK().getId();
String cancelHref = "sku_info.jsp?nodeId="+nodePkId
                    +"&parentNodeId="+pm.getParentNode().getPK().getId()
                    +"&catId="+pm.getParentNode().getContentName();

Collection myErrors = result.getErrors();
for(Iterator itE=myErrors.iterator(); itE.hasNext();) {
    ActionError ae = (ActionError)itE.next();
    System.out.println(ae.getType()+"-->"+ae.getDescription());
}


boolean displaySkus = true;
if ("add".equals(action))  {
   displaySkus = false;
  if (request.getMethod().equalsIgnoreCase("post") && !result.isSuccess()) {
     displaySkus = false;
  } else if (request.getMethod().equalsIgnoreCase("post")) {
     displaySkus = true;
  }
}


String actionValue=null;
if (displaySkus){
     if (pm.getPK()!=null) {
        actionValue = "update";
     }else {
       actionValue = "create";
     }
}  else {
   actionValue = "add";
}

%>
<tmpl:insert template='/common/template/leftnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect Store Admin</tmpl:put>

    <tmpl:put name='content' direct='true'>
     <form name="skuDetailsForm" method="post" action="sku_info.jsp">
     <input type="hidden" name="action" value="<%=actionValue%>">

	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2" class="breadcrumb"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"><br>Current Path:
    <%@ include file="includes/breadcrumb.jspf"%>
     <%=pm.getFullName()%>
    </td>
	</tr>
	<tr>
	<td valign="bottom">
		<table cellpadding="3" cellspacing="0" border="0">
		<tr><td colspan="3"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
		<tr>
		<td bgcolor="#999999" class="tab"><a href="<%= servletContext %>/web_store/hierarchy/product_info.jsp" class="tab">&nbsp;&nbsp;PRODUCT INFO&nbsp;&nbsp;</a></td>
		<td class="tab"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td bgcolor="#000000" class="tab">&nbsp;&nbsp;SKU INFO&nbsp;&nbsp;</td>
		</tr>
		</table>
	</td>
	<td align="right" valign="top">
<% if (displaySkus) { %>
		<table cellpadding="1" cellspacing="3" border="0">
		<tr align="center" valign="middle">
<%      if(pm.getPK()!=null) {	%>
		<td bgcolor="#0000CC" class="button"><a href="javascript:preview('/product.jsp?catId=<%=pm.getParentNode()%>&productId=<%=pm%>')" class="button">&nbsp;PREVIEW&nbsp;</a></td>
<%      } else {      %>
		<td bgcolor="#0000CC" class="button">&nbsp;PREVIEW&nbsp;</td>
<%      }  %>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td bgcolor="#CC0000" class="button"><a href="<%=cancelHref%>" class="button">&nbsp;CANCEL&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td bgcolor="#006600" class="button"><a href="<%=saveHref%>" class="button">&nbsp;SAVE&nbsp;</a></td>

		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		</tr>
		<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
		</table>
<% }  %>
	</td>
	</tr>
	<tr>
	<td colspan="2" bgcolor="#000000">
		<table width="100%" cellpadding="2">
		<tr>
		<td align="right" class="tabDetails">Last Modified: <%=pm.getLastModified()!=null ? modByFormat.format(pm.getLastModified()) : ""%> By: <%=pm.getLastModifiedBy()%></td>
		</tr>
		</table>
	</td>
	</tr>
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="2"></td></tr>
	</table>
	<table width="98%" cellpadding="2" cellspacing="0" border="0" align="center" class="section">
	<tr bgcolor="#000000"><th class="sectionHeader" colspan="4">&nbsp;Product Summary</th></tr>
	<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr>
	<td width="2%">&nbsp;&nbsp;&nbsp;</td>
	<td width="30%">Product ID</td>
	<td width="45%"><input readonly disabled type="text" style="width:250px;" size="25" class="textbox2" value="<%=pm.getContentName()%>"></td>
	<td width="20%">&nbsp;</td>
	</tr>
	<tr>
	<td>&nbsp;&nbsp;&nbsp;</td>
	<td>Name</td>
	<td><input readonly disabled type="text" style="width:250px;" size="25" class="textbox2" value="<%=pm.getFullName()%>"></td>
	<td>&nbsp;</td>
	</tr>
	<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="3"></td></tr>
	<tr bgcolor="#000000"><th class="sectionHeader" colspan="4">&nbsp;View/Edit Component SKUs</th></tr>
	<tr><td colspan="4">
<%      if (displaySkus  && result.isSuccess()) { %>
    		<table cellpadding="3">
    		<tr>
    		<td class="menu"><a href="sku_info.jsp?action=add"><font size="4">+</font> <b>add</b></a></td>
    		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
    		<td class="menu"><a href="javascript:doConfirm2('Delete selected Skus?');"><font size="4">&ndash;</font> <b>delete</b></a></td>
    		</tr>
    		</table>
<%     }  %>
	</td></tr>
	</table>
   <%
   // diplay new sku form or display list of sku's
    if (!displaySkus) { %>
    <table width="50%" cellpadding="0" cellspacing="0" border="0" align="center" class="section">
        <tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="15"></td></tr>
        <tr><td align="center" colspan="4"><b>Add New Sku</b></td></tr>

    	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='Technical Error'>
        <tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
    	</fd:ErrorHandler>
    	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='error'>
        <tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
    	</fd:ErrorHandler>

        <tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="5"></td></tr>
        <tr><td colspan="4" align="center" width="75%">New Web ID&nbsp;&nbsp;<input type="text" name="newSkuCode" value="<%=frmNewSkuCode%>" style="width:200px;" size="20" class="textbox2"></td>
        </tr>
    	<fd:ErrorHandler id='errMsg' result='<%=result%>' name='newSkuCode'>
        <tr><td align="center" colspan="4"> <font class="error"><%=errMsg%></font></td></tr>
    	</fd:ErrorHandler>
		<tr align="center" valign="middle"><td width="100%" colspan="2">
          <table >
            <tr>
    		 <td width="20" bgcolor="#CC0000" class="button"><a href="sku_info.jsp" class="button">&nbsp;CANCEL&nbsp;</a></td>
    		 <td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
    		 <td align="center" width="85" bgcolor="#006600" class="button"><a href="<%=saveHref%>" class="button">&nbsp;Save New Sku&nbsp;</a></td>
    	     <td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		    </tr>
           </table></td></tr>
		<tr><td colspan="4"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
	</table>
<% } else { 
int matrixSize = 0;
int optionsSize = 0;
int totalVarCol = 0;
List prodDomains = new ArrayList();
List attribNameForEntry = new ArrayList();
%>
	<table width="98%" cellpadding="0" cellspacing="0" border="1" align="center" class="section">
	<tr class="sectionSubHeader">
	<td width="3%">&nbsp;</td>
	<td width="14%">Web ID</td>
	<td width="11%">SAP ID</td>
	<td width="30%">SAP Description</td>
	<% if (pm.hasAttribute("VARIATION_MATRIX")) { %>
		<%
			List thisRef = (List)pm.getAttribute("VARIATION_MATRIX").getValue();
			matrixSize = thisRef.size();
			for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
				DomainRef ref = ((DomainRef)refList.next());
				prodDomains.add(ref.getDomain());
                                attribNameForEntry.add("VARIATION_MATRIX");
		%>
            <td>VAR: <%= ref.getDomainName() %>
			<%-- List domValues = ref.getDomain().getDomainValues(); 
				for (Iterator domList = domValues.iterator(); domList.hasNext();) {
					DomainValue val = ((DomainValue)domList.next());	
			%>
				<%= val.getLabel() %><br>
			<% } --%>
			</td>
		<% } 
		} %>
		<% if (pm.hasAttribute("VARIATION_OPTIONS")) { 
			List thisRef = (List)pm.getAttribute("VARIATION_OPTIONS").getValue();
			optionsSize = thisRef.size();
			for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
				DomainRef ref = ((DomainRef)refList.next());
				prodDomains.add(ref.getDomain());
                                attribNameForEntry.add("VARIATION_OPTIONS");

%>
			<td>VAR: <%= ref.getDomainName() %></td>
		<% } 
		} 
			totalVarCol = matrixSize + optionsSize;
		%>
	</tr>
<%
    List skus = pm.getSkus();
    boolean contrast=false;
	List skuVarValues = new ArrayList();
	DomainValue currentVal = null;
    for (Iterator itr = skus.iterator(); itr.hasNext(); ) {
        SkuModel sku = (SkuModel) itr.next();
		FDProductInfo prodInfo = FDCachedFactory.getProductInfo(sku.getSkuCode());
		FDProduct fdProd = FDCachedFactory.getProduct(prodInfo.getSkuCode(), prodInfo.getVersion());
        String skuFldName="skuCode_"+sku.getContentName(); %>
    	<tr <%=contrast ? "class=\"contrast\"" : ""%>>
    	<td width="3%"><input name="deleteSku_<%=sku.getContentName()%>" type="checkbox" value="<%=sku.getContentName()%>"></td>
    	<td width="14%" style="padding-right: 6px;"><input name="<%=skuFldName%>" type="text" style="width:88px;" size="8" class="textbox1" value="<%=sku.getSkuCode()%>"></td>
		<% String sapId = fdProd.getMaterial().getMaterialNumber();
		//ErpProductModel erpProd = ErpFactory.getProduct(sapId);
		ErpMaterialModel erpMat = ErpFactory.getInstance().getMaterial(sapId);
		%>
    	<td width="11%" style="padding-right: 6px;" class="colDetails"><%= sapId %></td>
    	<td width="20%" style="padding-right: 6px;" class="colDetails"><%= erpMat.getDescription() %></td>
		<% if (sku.hasAttribute("VARIATION_MATRIX")) {
			List thisRef = (List)sku.getAttribute("VARIATION_MATRIX").getValue();
				for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
					DomainValueRef ref = ((DomainValueRef)refList.next());
					skuVarValues.add(ref.getDomainValue());
				}
			}
			if (sku.hasAttribute("VARIATION_OPTIONS")) {
			List thisRef = (List)sku.getAttribute("VARIATION_OPTIONS").getValue();
				for (Iterator refList=thisRef.iterator(); refList.hasNext();) {
					DomainValueRef ref = ((DomainValueRef)refList.next());
					skuVarValues.add(ref.getDomainValue());
				}
			}
		%>
		  <%
		  int colCount = 0;
		  while (colCount < totalVarCol) { 
		  for (int i = 0; i < skuVarValues.size(); i++) {
		  	if (((DomainValue)skuVarValues.get(i)).getDomain().equals(prodDomains.get(colCount))) {
				currentVal = (DomainValue)skuVarValues.get(i);
			} else continue;
		  }
		  %>
		  <td style="padding-right: 6px;">
		  <select name="<%=sku.getSkuCode()+"_"+(String)attribNameForEntry.get(colCount) %>" class="pulldown1"><option value="">- None -</option>
		  <% List domValues = ((Domain)prodDomains.get(colCount)).getDomainValues(); 
				for (Iterator domList = domValues.iterator(); domList.hasNext();) {
					DomainValue val = ((DomainValue)domList.next());	
			%>
				<option <%= currentVal.getLabel().equalsIgnoreCase(val.getLabel()) ? "selected" : "" %> value="<%=((Domain)prodDomains.get(colCount)).getName()+"|"+ val.getValue()%>"><%= val.getLabel() %></option>
			<% 	} %>
			</select> 
		  </td>
			<%
			colCount++;
			} %>
    	</tr>
		<fd:ErrorHandler id='errMsg' result='<%=result%>' name='<%=skuFldName%>'>
        <tr><td colspan="<%= 4 + totalVarCol %>"><font class="error"><%=errMsg%></font></td></tr>
    	</fd:ErrorHandler>
<%   contrast=!contrast;
    }  %>
	</table>
<% }
   if (displaySkus) {
%>
	<table width="98%" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="2"><img src="<%= servletContext %>/images/clear.gif" width="1" height="8"></td></tr>
	<tr><td colspan="2" class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
	</tr>
	<tr>
	<td colspan="2" align="right" valign="top">
		<table cellpadding="1" cellspacing="3" border="0">
		<tr><td colspan="6"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
		<tr align="center" valign="middle">
<%      if(pm.getPK()!=null) {	%>
		<td bgcolor="#0000CC" class="button"><a href="javascript:preview('/product.jsp?catId=<%=pm.getParentNode()%>&productId=<%=pm%>')" class="button">&nbsp;PREVIEW&nbsp;</a></td>
<%      } else {      %>
		<td bgcolor="#0000CC" class="button">&nbsp;PREVIEW&nbsp;</td>
<%      }  %>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td bgcolor="#CC0000" class="button"><a href="<%=cancelHref%>" class="button">&nbsp;CANCEL&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		<td bgcolor="#006600" class="button"><a href="<%=saveHref%>" class="button">&nbsp;SAVE&nbsp;</a></td>
		<td><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td>
		</tr>
		</table><br>
	</td>
	</tr>
	</table>
<% } %>
     <script>
             function doConfirm2(msg) {
                 if (confirm("Are you sure you want to\n"+msg)) {
                    skuDetailsForm.action.value='delete';
                    skuDetailsForm.submit();
                }
             }
     </script>
    </form>
    </tmpl:put>
</tmpl:insert>
</sa:SkuController>