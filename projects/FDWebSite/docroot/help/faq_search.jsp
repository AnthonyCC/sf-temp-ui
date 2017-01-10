<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='java.text.*' %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.cms.ContentNodeI" %>
<%@ page import="com.freshdirect.fdstore.util.FilteringNavigator"%>
<%@ page import="com.freshdirect.fdstore.content.util.QueryParameterCollection"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature" %>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user" />
<% 
	//expanded page dimensions
	final int W_FAQ_SEARCH_TOTAL = 806;
	
	FilteringNavigator nav = new FilteringNavigator(request,8);
	nav.setFaq(true);
	
	final int defaultPageSize = nav.getDefaultPageSize();
	QueryParameterCollection qc = QueryParameterCollection.decode(request.getQueryString());
	if ( qc.getParameter("pageSize") == null ) {
		nav.setPageSize(defaultPageSize);
	}
	
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/common/template/faq_help.jsp";
	String oasSitePage = (request.getAttribute("sitePage") == null) ? "www.freshdirect.com/help/faq_search.jsp" : request.getAttribute("sitePage").toString();
	
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
		}
	}
%>
<tmpl:insert template='<%= pageTemplate %>'>
    <tmpl:put name='title' direct='true'>FreshDirect - Help - FAQs</tmpl:put>
	<tmpl:put name='leftnav' direct='true'>	</tmpl:put>
	<tmpl:put name='extraCss' direct='true'>
		<%-- don't use jawr here, it'll change the order that search.css is used --%>
		<fd:css href="/assets/css/search.css" />
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		
		<fd:SearchFaq id="result" nav="<%= nav %>">
			<table border="0" cellpadding="0" cellspacing="0" style="width: <%=((mobWeb)?"100%":W_FAQ_SEARCH_TOTAL+"px") %>">
				<% if (mobWeb) { %>
					<img src="/media_stat/images/template/help/faq_hdr.gif" class="faq-header-img" height="16" width="318" alt="Frequently Asked Questions" border="0" /><br />
					<div class="search-cont">
						<form name="contact_fd">
							<span class="icon-search"></span>
							<input type="text" id="search-faq" class="search-field fnt-18" value="<%= pageContext.getAttribute("keywords") %>" maxlength="100" name="searchFAQ2" placeholder="Search" autocomplete="off" />
						</form>
					</div>
				<% } else { %>
					<tr>
						<td valign="top"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0" /></td>
						<td valign="top"><img src="/media_stat/images/template/help/faq_hdr.gif"  height="16" width="318" alt="Frequently Asked Questions" border="0" /><br /></td>
						<td align="right" colspan="2"><img border="0" src="/media_stat/images/template/help/search.gif"  alt="Search" />
							<form name="contact_fd">
								<input type="text" class="search" value="<%= pageContext.getAttribute("keywords") %>" maxlength="100" style="width: 200px;" name="searchFAQ2" />&nbsp;
								<input type="image" name="searchFAQButton" style="border: 0pt none ; width: 35px; height: 14px;" src="/media_stat/images/template/search/search_find_button.gif" alt="Find"/>
							</form>
						</td>
					</tr>
				<% } %>
				<tr class="NOMOBWEB"><td colspan="4">&nbsp;</td></tr>
				<tr>
					<% if (!mobWeb) { %><td valign="top"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0" /></td><% } %>
					<td colspan="<%= (mobWeb)? "4":"3" %>"><img src="/media_stat/images/layout/999966.gif" height="1" border="0" width="100%" VSPACE="3"><br /></td>
				</tr>
				<tr><td colspan="4">&nbsp;</td></tr>
				<%	int itemCount = (Integer) pageContext.getAttribute("searchResultsSize"); %>
				<% if(result == null || result.isEmpty() || result.size() == 0) { %>
					<tr>
						<td valign="top"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0" /></td>
						<td colspan="3"><span class="bold">No matching FAQs found for '<%= pageContext.getAttribute("keywords") %>'</span></td>
					</tr>
					<tr>
						<td valign="top"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0" /></td>
						<td colspan="3">&nbsp;</td>
					</tr>
				<% } else { %>
					
					<tr>
						<td valign="top"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0" /></td>
						<td colspan="3"><span class="bold">Search Results for '<%= pageContext.getAttribute("keywords") %>'</span></td>
					</tr>
					<tr>
						<td valign="top"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0" /></td>
						<td colspan="3">&nbsp;</td>
					</tr>
						
					<logic:iterate id="faq" collection="<%= result.keySet()%>" type="java.lang.String">
						<% List<Faq> faqList = (List<Faq>)result.get(faq); %>
						<tr><td colspan="4">
							<table>
								<tr>
									<td valign="top"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0" /></td>
									<td colspan="3"><span class="bold"><%=faqList.get(0).getQuestion() %></span></td>
								</tr>
								<tr>
									<td valign="top"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0" /></td>
									<td colspan="3"><br /><%= faq %></td>
								</tr>
								<tr>
									<td valign="top"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0" /></td>
									<td colspan="3" style="padding-top: 10px;"> Section: 
										<logic:iterate id="actual" collection="<%= faqList %>" type="com.freshdirect.fdstore.content.Faq">
											<a href="/help/faq_home.jsp?page=<%= (String)actual.getParentNode().getContentKey().getId()%>" style="margin-right: 5px;"><%= ((String)actual.getParentNode().getCmsAttributeValue("name")).toUpperCase() %></a> 
										</logic:iterate>					
									</td>
								</tr>
							</table>
						</td></tr>
					</logic:iterate>
				<% } %>
			
				<% if(null !=result && !result.isEmpty() && result.size()!=0) { %>
					<div class="pager faqsearch <%= (mobWeb) ? "mobWeb" : "" %>" style="<%= (mobWeb) ? "" : "margin-top: 50px; margin-left: 14px;" %>">
						<div class="results">
							<span>Results: </span>
							<span class="results-current"><%= ((nav.getPageNumber())*nav.getPageSize())+1  %>-<%= nav.getPageSize()==0 ? 
									itemCount : Math.min((nav.getPageNumber()+1)*nav.getPageSize(),itemCount)  %></span>
							<span>of</span>
							<span class="results-all"><%= itemCount %></span>
						</div>
						<div class="pager-content">
							<display:Pager productsSize="<%= itemCount %>" nav="<%= nav %>" />
						</div>
					</div>
				<% } %>
				<% String resultInvalid = (String) pageContext.getAttribute("resultInvalid"); %>
				<% if("true".equals(resultInvalid)) { %>
						<tr>
							<td valign="top"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0" /></td>
							<td colspan="3"><span class="bold">No matching FAQs found for '<%= pageContext.getAttribute("keywords") %>'</span></td>
						</tr>
						<tr>
							<td valign="top"><img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0" /></td>
							<td colspan="3">&nbsp;</td>
						</tr>
				<% } %>
			</table>
		</fd:SearchFaq>
		<fd:CmPageView wrapIntoScriptTag="true" searchTerm='<%=(String)(pageContext.getAttribute("keywords"))%>' searchResultsSize='<%=(Integer)(pageContext.getAttribute("searchResultsSize"))%>'/>
	</tmpl:put>
	
	</tmpl:insert>