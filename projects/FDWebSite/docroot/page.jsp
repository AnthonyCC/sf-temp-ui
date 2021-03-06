<%@ page import="com.freshdirect.storeapi.content.*"%>
<%@ page import="com.freshdirect.cms.core.domain.ContentType"%>
<%@ page import="com.freshdirect.cms.core.domain.ContentKey"%>
<%@ page import="com.freshdirect.cms.core.domain.ContentKeyFactory"%>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='java.net.*'%>
<%@ page import='java.util.*'%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="com.freshdirect.framework.util.log.LoggerFactory"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<% 
	final Logger LOG = LoggerFactory.getInstance("page.jsp");
	
	String pageId = request.getParameter("pageId");
	
	String layout = "/common/template/page_template.jsp";
	
	PageModel pageModel = (PageModel) PopulatorUtil.getContentNodeByKey( ContentKeyFactory.get(ContentType.Page, pageId) );

	String title = "FreshDirect - " + pageModel.getTitle();
%>

<fd:CheckLoginStatus guestAllowed="true" recognizedAllowed="true" />

<tmpl:insert template='<%= layout %>'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="<%= title %>"/>
    </tmpl:put>
<%-- 	<tmpl:put name='title' direct='true'><%= title %></tmpl:put> --%>

  <tmpl:put name='extraHead' direct='true'>
    <fd:css href="/assets/css/common/page.css" />
  </tmpl:put>
	
	<tmpl:put name='content' direct='true'>
		
    <div class="content <%= pageModel.getShowSideNav() ? "hassidenav span-20 last" : "span-24" %>">
		<% for ( MediaI item : pageModel.getMediaList() ) { %>		
			<fd:IncludeMedia name="" media="<%= item %>" ></fd:IncludeMedia>
    <% } %>
    </div>
		
	</tmpl:put>

	<tmpl:put name='nav' direct='true'>
		<%
			if ( pageModel.getShowSideNav() ) {
				ContentNodeModel parent = pageModel.getParentNode();
				List<PageModel> siblings;
				String parentLink, parentTitle;
				if ( parent instanceof StoreModel ) {
					siblings = ((StoreModel)parent).getPages();
					parentLink = "/index.jsp";
					parentTitle = "HOME";
				} else if ( parent instanceof PageModel ) {
					siblings = ((PageModel)parent).getSubPages();
					parentLink = ((PageModel)parent).getLink();
					parentTitle = ((PageModel)parent).getTitle();
				} else {
					siblings = new ArrayList<PageModel>();
					parentLink = "#";
					parentTitle = "n/a";
				}
			%>		
			
      <div class="span-4">
        <div class="sidebar">
          <h2><%= pageModel.getTitle() %></h2>

          <ul>
            <li class="back">
              <a href="<%= parentLink %>"><%= parentTitle %></a>
            </li>
            <% for ( PageModel sibling : siblings ) { 
              if ( pageModel.getContentKey().equals( sibling.getContentKey() ) ) { %>
                <li>
                  <b><%= sibling.getTitle() %></b>
                  <ul>
                  <% for ( PageModel subpage : pageModel.getSubPages() ) { %>
                    <li>
                      <a href="<%= subpage.getLink() %>"><%= subpage.getTitle() %></a>
                    </li>
                  <% } %>					
                  </ul>
                </li>
              <% } else { %>
                <li>
                  <a href="<%= sibling.getLink() %>"><%= sibling.getTitle() %></a>
                </li>
              <% } %>
            <% } %>			
          </ul>
        </div>
      </div>
			
		<% } %>
		
	</tmpl:put>
	
</tmpl:insert>
