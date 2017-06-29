<%@ taglib uri='template' prefix='tmpl' %>
<tmpl:insert template='/common/template/large_pop.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - E-mail Reminder"/>
  </tmpl:put>
  <tmpl:put name='title' direct='true'>FreshDirect - E-mail Reminder</tmpl:put>
		<tmpl:put name='content' direct='true'>

                <%@ include file="/includes/registration/checkacct_terms.jspf"%>	
                
	</tmpl:put>
</tmpl:insert>

