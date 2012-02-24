<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import="com.freshdirect.fdstore.content.DomainValue"%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.content.attributes.*'%>
<%@ page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@ page import="com.freshdirect.fdstore.util.SearchNavigator"%>
<%@ page import="com.freshdirect.fdstore.*"%>
<%@ page import="com.freshdirect.cms.*"%>
<%@ page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@ page import="com.freshdirect.fdstore.content.*"%>
<%@ page import='com.freshdirect.fdstore.attributes.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='oscache' prefix='oscache'%>

<% //expanded page dimensions
final int W_SEARCH_TOTAL = 970;
final int W_SEARCH_LEFT = 150;
final int W_SEARCH_CENTER_PADDING = 14;
final int W_SEARCH_RIGHT = 806;
%>

<fd:CheckLoginStatus id="user" guestAllowed="true"
	recognizedAllowed="true" />
<%
	final String SEPARATOR = "&nbsp;<span class=\"text12\" style=\"color: #CCCCCC\">&bull;</span>&nbsp;";
	final String trk = "srch"; // tracking code

	String criteria = request.getParameter("searchParams");
	String upc = request.getParameter("upc");
	String displayCriteria = upc != null && !upc.trim().equals("") ? upc : criteria;
	String searchTerm = displayCriteria;

	// OAS AD settings
	request.setAttribute("sitePage", "www.freshdirect.com/search.jsp");
	request.setAttribute("listPos", "SystemMessage,LittleRandy,CategoryNote");

	// storing the view settings in the session
	SearchNavigator nav = new SearchNavigator(request);
	if (SearchNavigator.VIEW_DEFAULT == nav.getView() && request.getParameter("refinement") == null) {
		// view is not set yet (== default)
		//   let's get the latest used one from the session
		if (session.getAttribute(SessionName.SMART_SEARCH_VIEW) != null) {
			boolean wasDefaultSort = nav.isDefaultSort();
			nav.setView(SearchNavigator.convertToView((String) session.getAttribute(SessionName.SMART_SEARCH_VIEW)));
			if (wasDefaultSort && nav.isTextView()) {
				// reset default sort which was adjusted by changing to text view
				nav.setSortBy(SearchSortType.DEF4TEXT);
			}
		}
	} else {
		// record custom view type
		if (SearchNavigator.VIEW_DEFAULT == nav.getView()) {
			session.removeAttribute(SessionName.SMART_SEARCH_VIEW);
		} else {
			session.setAttribute(SessionName.SMART_SEARCH_VIEW, SearchNavigator.convertToViewName(nav.getView()));
		}
	}
%>

<fd:SmartSearch id="search" nav="<%= nav %>">
	<tmpl:insert template='/common/template/search_nav.jsp'>
		<%
			if (search.hasProducts() || search.hasRecipes()) {
		%>
		<tmpl:put name="categoryPanel" direct="true">
			<%-- CATEGORY TREE NAVIGATOR --%>
			<%@ include file="/includes/search/treenav.jspf"%>
		</tmpl:put>
		<%
			} else {
				if (FDStoreProperties.isAdServerEnabled()) {
		%>
		<tmpl:put name="categoryPanel" direct="true">
		<%--
		<div style="width:150px; margin-top: 1em; overflow: hidden;">
		<script type="text/javascript">
			OAS_AD('LittleRandy');
		</script>
		</div>
		--%>
		<div style="width:150px; margin-top: 15px; overflow: hidden;">
			<a href="javascript:pop('/request_product.jsp',400,585)"><img src="/media_stat/images/template/search/ken_request_a_product.jpg" border="0" hspace="0" vspace="3" width="150" height="100"></a>
		</div>
		</tmpl:put>
		<%
				}
			}
		%>
		<tmpl:put name='title' direct='true'>FreshDirect - Search<%=displayCriteria != null && displayCriteria.length() > 0 ? (" - " + displayCriteria) : ""%></tmpl:put>
		<tmpl:put name='content' direct='true'>
		<%
			if (FDStoreProperties.isAdServerEnabled()) {
		%>
		<div style="width: <%=W_SEARCH_RIGHT%>px; border: 0; margin-top: 10px; padding: 0px; margin-left:auto; margin-right:auto; text-align:center;">
			<script type="text/javascript">
				OAS_AD('CategoryNote');
			</script>
		</div>
		<%
			}
			if (nav.isEmptySearch()) {
		%><%@ include file="/includes/search/search_tips.jspf"%>
		<%
			} else if (search.hasNoResults()) {
		%><%@ include file="/includes/search/no_results.jspf"%>
		<%
			} else { // has results
				if (search.hasProducts()) {
				%>
					<div class="text10" style="text-align: right; font-weight: bold"> View:</div>
					<%
						if (nav.getCategory() != null) {
							ContentNodeModel node = search.getCategoryTree().getTreeElement(nav.getCategory()).getModel();
							displayCriteria = "<a href=\"" + FDURLUtil.getCategoryURI(node.getContentKey().getId(), trk) + "\">" + node.getFullName() + "</a>";
						} else if (nav.getDepartment() != null) {
							ContentNodeModel node = search.getCategoryTree().getTreeElement(nav.getDepartment()).getModel();
							displayCriteria = "<a href=\"" + FDURLUtil.getDepartmentURI(node.getContentKey().getId(), trk) + "\">" + node.getFullName() + "</a>";
						}
					%>
					<div style="font-size: 18px; font-weight: normal"><%-- view selectors --%>
						<span style="float: right">
						<%
							if (nav.isListView()) {
						%>
							<img src="/media_stat/images/template/search/search_view_list_on.gif" style="border: none" alt="List View">
							<a href="<%=nav.getSwitchViewAction(SearchNavigator.VIEW_GRID)%>"><img src="/media_stat/images/template/search/search_view_grid_off.gif" style="border: none" alt="Grid View"></a>
							<a href="<%=nav.getSwitchViewAction(SearchNavigator.VIEW_TEXT)%>"><img src="/media_stat/images/template/search/search_view_text_off.gif" style="border: none" alt="Text View"></a>
						<%
							} else if (nav.isGridView()) {
						%>
			 				<a href="<%=nav.getSwitchViewAction(SearchNavigator.VIEW_LIST)%>"><img src="/media_stat/images/template/search/search_view_list_off.gif" style="border: none" alt="List View"></a>
			 				<img src="/media_stat/images/template/search/search_view_grid_on.gif" alt="Grid View" style="border: none">
			 				<a href="<%=nav.getSwitchViewAction(SearchNavigator.VIEW_TEXT)%>"><img src="/media_stat/images/template/search/search_view_text_off.gif" style="border: none" alt="Text View"></a>
			 			<%
			 				} else if (nav.isTextView()) {
			 			%>
			 				<a href="<%=nav.getSwitchViewAction(SearchNavigator.VIEW_LIST)%>"><img src="/media_stat/images/template/search/search_view_list_off.gif" style="border: none" alt="List View"></a>
			 				<a href="<%=nav.getSwitchViewAction(SearchNavigator.VIEW_GRID)%>"><img src="/media_stat/images/template/search/search_view_grid_off.gif" style="border: none" alt="Grid View"></a>
			 				<img src="/media_stat/images/template/search/search_view_text_on.gif" style="border: none" alt="Text View">
			 			<%
			 				}
			 			%> 
			 			</span>
			 			<%=search.getNoOfFilteredProducts()%> product<%=search.getNoOfFilteredProducts() > 1 ? "s" : ""%> found <%=displayCriteria.equals(upc) ? "for barcode" : (displayCriteria.equals(criteria) ? "for" : "in")%>
						<span style="font-weight: bold"><%=displayCriteria%></span>
					</div>
					<%@ include file="/includes/search/didyoumean.jspf"%>
					<table cellpadding="0" cellspacing="0" style="width: <%=W_SEARCH_RIGHT%>px; border: 0; background-color: #E0E3D0; padding: 2px; margin-top: 10px; line-height: 25px;">
						<tr>
							<td style="text-align: left;"><span class="text11bold">Sort:</span> <%
		 					SearchNavigator.SortDisplay[] sbar = nav.getSortBar();
		
		 					for (int i = 0; i < sbar.length; i++) {
		 			%><a href="<%=nav.getChangeSortAction(sbar[i].sortType)%>" class="<%=sbar[i].isSelected ? "text11bold" : "text11"%>"><%=sbar[i].text%></a><%
		 						if (i < sbar.length - 1) {
		 				%><%=SEPARATOR%><%
		 						}
		 					}
		 					%></td>
							<td style="text-align: right;">
							<form name="form_brand_filter" id="form_brand_filter" method="GET" style="margin: 0;">
								<input type="hidden" name="searchParams" value="<%=StringEscapeUtils.escapeHtml(criteria)%>">
								<input type="hidden" name="upc" value="<%=upc%>">
								<%
									nav.appendToBrandForm(out);
								%>
								<input type="hidden" name="sort" value="<%= nav.getSortByName() %>">
								<% if (nav.isFromDym()) { %><input type="hidden" name="fromDym" value="1"><% } %>
								<select name="brandValue" class="text9" style="width: 140px;" onchange="document.getElementById('form_brand_filter').submit()">
									<option value=""><%=nav.getBrand() == null ? "Filter by brand" : "SHOW ALL PRODUCTS"%></option>
									<%
										if (!search.getBrands().isEmpty()) {
											for (Iterator<BrandModel> iter = search.getBrands().iterator(); iter.hasNext();) {
												BrandModel cm = iter.next();
									%>
									<option value="<%=cm.getContentKey().getId()%>"<%if (cm.getContentKey().getId().equals(nav.getBrand())) {%> selected="selected" <%}%>><%=cm.getFullName()%></option>
									<%
											}
										}
									%>
									</select>
								</form>
							</td>
						</tr>
					</table>

					<%-- result list--%>
					<div style="margin-top: 5px">
					<%
						if (nav.isListView()) {
					%><%@ include file="/includes/search/sx_listview.jspf"%>
					<%
						} else if (nav.isGridView()) {
					%><%@ include file="/includes/search/sx_gridview.jspf"%>
					<%
						} else if (nav.isTextView()) {
												if (nav.isTextViewDefault()) {
					%><%@ include file="/includes/search/sx_textview.jspf"%>
					<%
						} else {
					%><%@ include file="/includes/search/sx_textview_flat.jspf"%>
					<%
						}
					}

				} // hasProducts()

				if (search.hasRecipes()) {
			%>
				<%
					if (search.hasProducts() && !nav.isTextViewDefault()) {
				%>
					<div style="border-top: 4px solid #ff9933; margin: 10px 0px; font-size: 0px;"></div>
				<%
					}
				%>
				<%
					int noOfShownRecipes = search.getRecipes().size();
					String foundRecipes = noOfShownRecipes + " recipe"
												+ (noOfShownRecipes > 1 ? "s were" : " was") + " found";
					if (nav.getRecipeFilter() != null) {
						DomainValue dv = (DomainValue) ContentFactory.getInstance().getContentNode("DomainValue" , nav.getRecipeFilter());
						if (dv != null) {
							foundRecipes += " in ";
							foundRecipes += dv.getLabel();
						}
					}
					if (search.hasProducts()) {
						if (nav.isTextViewDefault()) {
				%>
					<div class="text13bold" style="margin-top: 10px;">
					<span style="color:#ff9f41;">RECIPES</span>
					(<%=noOfShownRecipes%> recipe<%= noOfShownRecipes > 1 ? "s" : "" %>)
					</div>
				<%
						} else {
				%>
					<div class="title12" style="padding: 0px 0px 8px;">
					<%=foundRecipes%>		
					<%
							if (noOfShownRecipes > 5) {
							%>
						(<span class="bold">Show <a href="#" onclick="toggleRecipes(); return false;" id="recipe_label">All</a></span>)	
					<%
							}
						}
							%>
					</div>
				<%
					} else {
				%>
					<div style="font-size: 18px; font-weight: normal;"><%=foundRecipes%>
					</div>
					<%@ include file="/includes/search/didyoumean.jspf"%>
					<div class="text13" style="background-color: #E0E3D0; padding: 2px; margin: 10px 0px; line-height: 25px;">
					<div style="float: left"><span class="text11bold">Sort:</span>
					<%
						boolean descName = nav.isSortByName() && !nav.isSortOrderingAscending();
					%> <a href="<%=nav.getChangeSortAction(SearchSortType.BY_NAME)%>" class="<%=nav.isSortByName() ? "text11bold" : "text11"%>"><%=descName ? "Name (Z-A)" : "Name (A-Z)"%></a>
					</div>
					<div style="clear: both;"></div>
					</div>
				<%
					}
				%> <%@ include file="/includes/search/recipes_textview.jspf"%>
			<%
				}
			%>
			<div style="border-top: 4px solid #ff9933; margin: 10px 0px; font-size: 0px;"></div>
			</div>
			<%
				// Don't show pager for text view!
				if (search.hasProducts() && !nav.isTextView()) {
			%>
			<%@ include file="/includes/search/pager.jspf"%>
			<%
				}
			%>
		<%
			} // has results
		%>
		</tmpl:put>
	</tmpl:insert>
</fd:SmartSearch>
