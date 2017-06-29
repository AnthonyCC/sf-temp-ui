<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/common/template/large_pop.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - User Agreement"/>
  </tmpl:put>
  <tmpl:put name='title' direct='true'>FreshDirect - User Agreement</tmpl:put>
		<tmpl:put name='content' direct='true'>
		<%
		final int W_USER_AGREEMENT_TOTAL = 500;
		%>

                <%@ include file="/shared/includes/registration/i_user_agreement.jspf"%>	
                
	</tmpl:put>
</tmpl:insert>

