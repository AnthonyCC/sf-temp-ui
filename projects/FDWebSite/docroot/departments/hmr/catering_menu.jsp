<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="true" />

<%
String show = "party";

boolean breakfast = false;
boolean party = false;
boolean entree = false;
boolean salad = false;
boolean sandwich = false;
boolean kids = false;
boolean dessert = false;
boolean hors_doeuvre = false;

String title = "";

if (request.getParameter("show") != null && !"".equals(request.getParameter("show"))) {
	show = request.getParameter("show");
	
	if ("breakfast".equalsIgnoreCase(show)) {
		breakfast = true;
		title = "Breakfast Platters";
	} else if ("party".equalsIgnoreCase(show)) {
		party = true;
		title = "Party Platters";
	} else if ("entree".equalsIgnoreCase(show)) {
		entree = true;
		title = "Entr&eacute;es";
	} else if ("salad".equalsIgnoreCase(show)) {
		salad = true;
		title = "Salads";
	} else if ("sandwich".equalsIgnoreCase(show)) {
		sandwich = true;
		title = "Sandwich Platters & Heros";
	} else if ("kids".equalsIgnoreCase(show)) {
		kids = true;
		title = "Kids' Menu";
	} else if ("dessert".equalsIgnoreCase(show)) {
		dessert = true;
		title = "Dessert Platters";
	} else if ("hors_doeuvre".equalsIgnoreCase(show)) {
		hors_doeuvre = true;
		title = "Hors d'Oeuvres";
	}
	
}

%>

<tmpl:insert template='/common/template/large_long_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Catering - <%=title%></tmpl:put>
		<tmpl:put name='content' direct='true'>
		<table width="520" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td><img src="/media_stat/images/layout/clear.gif" width="130" height="4"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="8" height="4"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="8" height="4"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="373" height="4"></td>
			</tr>
			<tr valign="top">
				<td align="right" class="text12"><img src="/media_stat/images/template/kitchen/catering/catering_menu.gif" width="90" height="37"><br><br>
				<%= breakfast ? "<b>" : "<a href='?show=breakfast'>"%>Breakfast Platters<%= breakfast ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<%= party ? "<b>" : "<a href='?show=party'>"%>Party Platters<%= party ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<%= entree ? "<b>" : "<a href='?show=entree'>"%>Entr&eacute;es<%= entree ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<%= salad ? "<b>" : "<a href='?show=salad'>"%>Salads<%= salad ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<%= sandwich ? "<b>" : "<a href='?show=sandwich'>"%>Sandwich Platters & Heros<%= sandwich ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<%= kids ? "<b>" : "<a href='?show=kids'>"%>Kids' Menu<%= kids ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<%= dessert ? "<b>" : "<a href='?show=dessert'>"%>Dessert Platters<%= dessert ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<%= hors_doeuvre ? "<b>" : "<a href='?show=hors_doeuvre'>"%>Hors d'Oeuvres<%= hors_doeuvre ? "</b>" : "</a>"%><br><span class="space8pix"><br><br><br></span>
				<span class="text11">Please contact us for pricing.</span><br><br>
				</td>
				<td></td>
				<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
				<td></td>
				<td align="center">
				<% if (breakfast) { %>
					<jsp:include page='/departments/hmr/catering_breakfast.jsp'/>
				<% } else if (party) { %>
					<jsp:include page='/departments/hmr/catering_party.jsp'/>
				<% } else if (entree) { %>
					<jsp:include page='/departments/hmr/catering_entree.jsp'/>
				<% } else if (salad) { %>
					<jsp:include page='/departments/hmr/catering_salad.jsp'/>
				<% } else if (sandwich) { %>
					<jsp:include page='/departments/hmr/catering_sandwich.jsp'/>
				<% } else if (kids) { %>
					<jsp:include page='/departments/hmr/catering_kids.jsp'/>
				<% } else if (dessert) { %>
					<jsp:include page='/departments/hmr/catering_dessert.jsp'/>
				<% } else if (hors_doeuvre) { %>
					<jsp:include page='/departments/hmr/catering_hors_doeuvre.jsp'/>
				<% } %>
				</td>
			</tr>
		</table>

	</tmpl:put>
</tmpl:insert>
