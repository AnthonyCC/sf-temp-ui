<%@ taglib uri='template' prefix='tmpl' %>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Error! No Case for this Customer, actions not permitted</tmpl:put>
		
    	<tmpl:put name='content' direct='true'>
		<div class="content_fixed" align="center">
			<br>
			<div class="error"><b>>> Error! No Case for this Customer, actions not permitted <<</div>
			Please create a new case or work on an existing case to open options.
			</b>
			<br><br><br>
		</div>
	    </tmpl:put>

</tmpl:insert>