<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.net.URLDecoder' %>

<% //expanded page dimensions
final int W_LOGIN_TOTAL = 970;
%>

<% 
String template = "/common/template/no_nav.jsp";
	//diff nav for popup login
	//if ("popup".equals( request.getParameter("type") ))
boolean isPopup = false;
String sPage = (request.getParameter("successPage")!=null)?request.getParameter("successPage").toLowerCase():null;
	if (sPage != null) {
		if (sPage.indexOf("type=popup") != -1){
			template = "/common/template/large_pop.jsp";
			isPopup = true;
		}else if ( sPage.indexOf("gift_card") > 0 && FDStoreProperties.isGiftCardEnabled() ) {
			template = "/common/template/giftcard.jsp";
		}else if ( sPage.indexOf("robin_hood") > 0 && FDStoreProperties.isRobinHoodEnabled() ) {
			template = "/common/template/robinhood.jsp";
		}
	}
%>
<fd:CheckLoginStatus/>
<tmpl:insert template='<%=template%>'>
<tmpl:put name='title' direct='true'>FreshDirect - Log In</tmpl:put>
<tmpl:put name='content' direct='true'>
<table border="0" cellspacing="0" cellpadding="0" width="<%=W_LOGIN_TOTAL%>" align="center">
		<tr><td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="20"></td></tr>
        <tr>
            <td width="<%=W_LOGIN_TOTAL-200%>" colspan="3">
				<img src="/media_stat/images/navigation/login_header.gif" width="276" height="10" border="0" alt="CURRENT CUSTOMERS LOG IN HEADER"></td>
        	<td width="200" align="right"><font class="text9">* Required Information</font></td>
		</tr>
		<tr><td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
		<tr><td colspan="4" bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
		<tr><td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
		<tr>
			<td  width="<%=W_LOGIN_TOTAL-550%>" >
			<br>
			<table border="0" cellspacing="0" cellpadding="0" align="left">
				<tr>
            		<td>
						<img src="/media_stat/images/navigation/current_cust_log_in_now.gif" width="222" height="13" border="0" alt="CURRENT CUSTOMERS LOG IN NOW">
					</td>        			
				</tr>
				<tr>
					<td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0"><BR></td>
				</tr>
				<tr>					
					<td class="text13">
							<%@ include file="/includes/i_login_field.jspf" %>
					<br>
				<% if (!isPopup) { %>
					<%if(uri.indexOf("quickshop")== -1){%>
						
						<font class="text13bold" style="padding-left:120px;">Forgot your password?</FONT><BR>
						<font class="text13" style="padding-left:120px;">
							<%if(uri.indexOf("main")> -1){%>
									<A HREF="/login/forget_password_main.jsp">Click here for help</a>
							<%}else{%>	
									<A HREF="/login/forget_password.jsp">Click here for help</a>
							<%}%>
						</FONT>
						
					<%}%>
				
				<br><br>
					<%-- <% if( null != request.getParameter("successPage")) { %>
						<% if(request.getParameter("successPage").toLowerCase().indexOf("gift_card") > 0 ) { %>
							<font class="text13bold">NEW TO FRESHDIRECT?</FONT><BR>
							<A HREF='<%= response.encodeURL("/gift_card/purchase/register_and_purchase.jsp") %>'><font class="text13"><b>Click here to continue</b></font></a><br><br></td>
						<% } else if(request.getParameter("successPage").toLowerCase().indexOf("robin_hood") > 0 ){ %>        
							<font class="text13bold">NEW TO FRESHDIRECT?</FONT><BR>
							<A HREF='<%= response.encodeURL("/robin_hood/register_purchase.jsp") %>'><font class="text13"><b>Click here to continue</b></font></a><br><br></td>
							
						<% }else{ %>
							<font class="text13bold">New Customer?</FONT><BR>
								<A HREF='<%= response.encodeURL("/about/index.jsp?siteAccessPage=aboutus&successPage=/index.jsp") %>'><font class="text13">See if we deliver to your area</font></a>.<% } %>
					<%} else { %>        
						<font class="text13bold">New Customer?</FONT><BR>
						<A HREF='<%= response.encodeURL("/about/index.jsp?siteAccessPage=aboutus&successPage=/index.jsp") %>'><font class="text13">See if we deliver to your area</font></a>.<% } %>
				 --%><% } %>
					</td>
				</tr>
				
				<tr>
					<td style="padding-left:120px;">
						<input type="image" src="/media_stat/images/navigation/login_btn.jpg" alt="Login Now" name="check_access" id="check_access" width="91" height="34" border="0" onclick="setCheckAccess();return false;" />
						</td>
				</tr>
				
				</table>
			</td>
			<td width="1" bgcolor="#cccccc">
				<img src="/media_stat/images/layout/dotted_line_w.gif" width="1" height="1" border="0" />
			</td>
			<td>&nbsp;&nbsp;</td>
			<td width="500" valign="top">
				<br>
				<table border="0" cellspacing="0" cellpadding="0" align="left">
				<tr>
            		<td>
						<img src="/media_stat/images/navigation/customer_new.gif" width="143" height="9" border="0" alt="CURRENT CUSTOMERS LOG IN NOW">
					</td>        			
				</tr>
				<tr>
					<td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0"><BR></td>
				</tr>
				<tr>					
					<td class="text13">
					 	<img src="/media_stat/images/navigation/signup_recieve.jpg" border="0" alt="CURRENT CUSTOMERS LOG IN NOW">					 
						<br/><br/>This offer is not valid towards giftcard donations,<br/> this is FPO.<br/><br/><br/>
					</td>
				</tr>
				<tr>					
					<td class="text13">
					<% if( null != request.getParameter("successPage")) { %>
						<% if(request.getParameter("successPage").toLowerCase().indexOf("gift_card") > 0 ) { %>
							<input type="image" src="/media_stat/images/navigation/create_account_btn.jpg" alt="SIGNUP AND RECIEVE" name="create_account" id="create_account" border="0" onclick="setCreateAccount(1);return false;" />
						<% } else if(request.getParameter("successPage").toLowerCase().indexOf("robin_hood") > 0 ){ %>        
							<input type="image" src="/media_stat/images/navigation/create_account_btn.jpg" alt="SIGNUP AND RECIEVE" name="create_account" id="create_account" border="0" onclick="setCreateAccount(2);return false;" />
						<% } else { %>
							<input type="image" src="/media_stat/images/navigation/create_account_btn.jpg" alt="SIGNUP AND RECIEVE" name="create_account" id="create_account" border="0" onclick="setCreateAccount(3);return false;" />
						<% } %>	
					<% } else { %>        
							<input type="image" src="/media_stat/images/navigation/create_account_btn.jpg" alt="SIGNUP AND RECIEVE" name="create_account" id="create_account" border="0" onclick="setCreateAccount(3);return false;" />
					<% } %>	
					</td>
				</tr>
				</table>
				
			</td>
		</tr>
		
		<script>
			function setCheckAccess() {				
				document.fd_login.submit();
			}
			
			function setCreateAccount(id) {				
				if(id !== null){					
					if(id === 1) {
						<%
							String donIdParam = null;
							if( null != request.getParameter("successPage")) {
								String queryString = URLDecoder.decode(request.getParameter("successPage"));								
								donIdParam = queryString != null ? queryString.substring(queryString.indexOf("?")+1) : null;
								donIdParam = donIdParam != null ? donIdParam.substring(donIdParam.indexOf("=")+1) : null;
							}
						%>
						document.location = '<%= response.encodeURL("/gift_card/purchase/register_and_purchase.jsp?gcDonId="+donIdParam) %>';
					} else if(id === 2) {
						document.location = '<%= response.encodeURL("/robin_hood/register_purchase.jsp") %>';
					} else if(id === 3) {
						document.location = '<%= response.encodeURL("/about/index.jsp?siteAccessPage=aboutus&successPage=/index.jsp") %>';
					}
				} else {
					document.location = '<%= response.encodeURL("/about/index.jsp?siteAccessPage=aboutus&successPage=/index.jsp") %>';
				}
			}
	
		</script>
</table>
</tmpl:put>
</tmpl:insert>
