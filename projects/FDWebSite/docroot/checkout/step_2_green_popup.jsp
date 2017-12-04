<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/shared/template/small_pop.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Window Steering Policy"/>
  </tmpl:put>
<%--   <tmpl:put name='title'>FreshDirect - Window Steering Policy</tmpl:put> --%>
		<tmpl:put name='content' direct='true'>
	        <fd:IncludeMedia name="/media/editorial/site_pages/timeslots/timeslot_adv_help.html" />
	</tmpl:put>
</tmpl:insert>
