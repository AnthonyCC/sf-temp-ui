<%@ taglib uri='template' prefix='tmpl' %>
<% String servletContext = request.getContextPath(); %>
<tmpl:insert template='/common/template/nonav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect Store Admin</tmpl:put>

    <tmpl:put name='content' direct='true'>

		<table width="80%" cellpadding="0" cellspacing="0" border="0" align="center">
		<tr align="center" valign="top">
		<td width="33%">
			<table cellpadding="4">
			<tr><td colspan="2"><br><br></td></tr>
			<tr>
			<td colspan="2"><a href="<%= servletContext %>/web_store/hierarchy/index.jsp"><b>WEB STORE</b></a></td>
			</tr>
			<tr>
			<td rowspan="4">&nbsp;&nbsp;</td>
			<td><a href="<%= servletContext %>/web_store/hierarchy/index.jsp">Hierarchy</a></td>
			</tr>
			<tr>
			<td><a href="<%= servletContext %>/web_store/brands/index.jsp">Brands</a></td>
			</tr>
			<tr>
			<td><a href="<%= servletContext %>/web_store/media/index.jsp">Media</a></td>
			</tr>
			<tr>
			<td><a href="<%= servletContext %>/web_store/domains/index.jsp">Domains</a></td>
			</tr>
			</table>
		</td>
		<td width="33%">
			<table cellpadding="4">
			<tr><td colspan="2"><br><br></td></tr>
			<tr>
			<td colspan="2"><b>ERPS DATA</b></td>
			</tr>
			<tr>
			<td rowspan="4">&nbsp;&nbsp;</td>
			<td>SKUs</td>
			</tr>
			<tr>
			<td>Materials</td>
			</tr>
			<tr>
			<td>Classes</td>
			</tr>
			<tr>
			<td>Batches</td>
			</tr>
			</table>
		</td>
		<td width="33%">
			<table cellpadding="4">
			<tr><td colspan="2"><br><br></td></tr>
			<tr>
			<td colspan="2"><b>REPORTS</b></td>
			</tr>
			<tr>
			<td rowspan="2">&nbsp;&nbsp;</td>
			<td>Standard Reports</td>
			</tr>
			<tr>
			<td>Custom Reports</td>
			</tr>
			</table>
		</td>
		</tr>
		</table>

    </tmpl:put>

</tmpl:insert>
