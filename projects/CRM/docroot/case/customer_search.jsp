<%@ taglib uri='template' prefix='tmpl' %>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>/ FD CRM : Case > Search Customer /</tmpl:put>

    <tmpl:put name='content' direct='true'>
		
		<div class="content" style="height: 50%; padding: 0px; margin: 0px;">
			<div class="case_search_module" style="width: 30%; height: 100%;">
					<form name="customer_search" method="POST" action="/case/customer_search.jsp">
						<b>Search Customer</b>
						<jsp:include page='/includes/customer_search.jsp'/>
					</form>
						<span class="case_search_note">> search result notes</span>
			</div>
			
			<div class="content" style="padding: 0px;">
				
				<%-- search result == 1 -->
				<div class="content_scroll" style="padding: 0px; height: 100%;">
					<table width="100%" cellpadding="0" cellspacing="0" class="case_search_result" style="padding-left: 8px;">
						<tr>
							<td width="20%" class="case_search_result_header_color"><b>Name</b></td><td colspan="2" width="80%">Johnson, Johnette</td>
						</tr>
						<tr>
							<td class="case_search_result_header_color"><b>Email</b></td><td colspan="2">whatever@company.com</td>
						</tr>
						<tr>
							<td class="case_search_result_header_color"><b>Phone</b></td><td colspan="2">(212) 555-1212</td>
						</tr>
						<tr>
							<td class="case_search_result_header_color"><b>Last Order</b></td><td colspan="2">03.03.2003</td>
						</tr>
						<tr>
							<td class="case_search_result_header_color"><b>Status</b></td><td colspan="2">Delivered</td>
						</tr>
						<tr class="case_search_result_separator">
							<td colspan="3" style="padding: 0px;"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td>
						</tr>
						<tr>
							<td width="20%" class="case_search_result_header_color"><b>Order #</b></td><td width="20%">8785678</td><td width="60%"><input type="checkbox" name="link_order" checked> Link to case</td>
						</tr>
						<tr>
							<td class="case_search_result_header_color"><b>Status</b></td><td colspan="2">Delivered</td>
						</tr>
						<tr>
							<td class="case_search_result_header_color"><b>Delivery Date</b></td><td colspan="2">04.19.2003</td>
						</tr>
						<tr>
							<td class="case_search_result_header_color"><b>Total</b></td><td colspan="2">$ 56.78</td>
						</tr>
						<tr class="case_search_result_separator">
							<td colspan="3" style="padding: 0px;"><img src="/media_stat/crm/clear.gif" width="1" height="1"></td>
						</tr>
					</table>
				</div>
				<%-- search result == 1 -->
				<%-- search results > 1 --%>
				<div class="case_search_result_header" style="height: 5%;">
					<table width="100%" cellpadding="0" cellspacing="0" border="0" class="case_search_result">
						<tr>
							<td width="3%"></td>
							<td width="25%"><b>Name</b></td>
							<td width="25%"><b>Email</b></td>
							<td width="17%"><b>Phone</b></td>
							<td width="15%"><b>Last Order</b></td>
							<td width="15%"><b>Status</b></td>
							<td><img src="/media_stat/crm/images/clear.gif" width="10" height="1"></td>
						</tr>
					</table>
				</div>
				<div class="content_scroll" style="padding: 0px; height: 95%;">
					<table width="100%" cellpadding="0" cellspacing="0" border="0" class="case_search_result">
					<%-- ENTRY --%>
						<tr class="case_search_result_odd_row">
							<td width="3%"><input type="radio" name="radiobutton" value="radiobutton"></td>
							<td width="25%">Publiciani, Johnette Q</td>
							<td width="25%">jpubliciani@company.com</td>
							<td width="17%">(212) 345-6789</td>
							<td width="15%">03.15.2003</td>
							<td width="15%">En-route</td>
						</tr>
						<tr class="case_search_result_separator" style="padding: 0px;"><td colspan="6"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
					<%-- ENTRY --%>
						<tr>
							<td><input type="radio" name="radiobutton" value="radiobutton"></td>
							<td>Publiciani, Johnette Q</td>
							<td>jpubliciani@company.com</td>
							<td>(212) 345-6789</td>
							<td>03.15.2003</td>
							<td>En-route</td>
						</tr>
						<tr class="case_search_result_separator" style="padding: 0px;"><td colspan="6"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
						
						<tr class="case_search_result_odd_row">
							<td><input type="radio" name="radiobutton" value="radiobutton"></td>
							<td>Publiciani, Johnette Q</td>
							<td>jpubliciani@company.com</td>
							<td>(212) 345-6789</td>
							<td>03.15.2003</td>
							<td>En-route</td>
						</tr>
						<tr class="case_search_result_separator" style="padding: 0px;"><td colspan="6"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
						
						<tr>
							<td><input type="radio" name="radiobutton" value="radiobutton"></td>
							<td>Publiciani, Johnette Q</td>
							<td>jpubliciani@company.com</td>
							<td>(212) 345-6789</td>
							<td>03.15.2003</td>
							<td>En-route</td>
						</tr>
						<tr class="case_search_result_separator" style="padding: 0px;"><td colspan="6"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
						
						<tr class="case_search_result_odd_row">
							<td><input type="radio" name="radiobutton" value="radiobutton"></td>
							<td>Publiciani, Johnette Q</td>
							<td>jpubliciani@company.com</td>
							<td>(212) 345-6789</td>
							<td>03.15.2003</td>
							<td>En-route</td>
						</tr>
						<tr class="case_search_result_separator" style="padding: 0px;"><td colspan="6"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
						
						<tr>
							<td><input type="radio" name="radiobutton" value="radiobutton"></td>
							<td>Publiciani, Johnette Q</td>
							<td>jpubliciani@company.com</td>
							<td>(212) 345-6789</td>
							<td>03.15.2003</td>
							<td>En-route</td>
						</tr>
						<tr class="case_search_result_separator" style="padding: 0px;"><td colspan="6"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
						
						<tr class="case_search_result_odd_row">
							<td><input type="radio" name="radiobutton" value="radiobutton"></td>
							<td>Publiciani, Johnette Q</td>
							<td>jpubliciani@company.com</td>
							<td>(212) 345-6789</td>
							<td>03.15.2003</td>
							<td>En-route</td>
						</tr>
						<tr class="case_search_result_separator" style="padding: 0px;"><td colspan="6"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
						
						<tr>
							<td><input type="radio" name="radiobutton" value="radiobutton"></td>
							<td>Publiciani, Johnette Q</td>
							<td>jpubliciani@company.com</td>
							<td>(212) 345-6789</td>
							<td>03.15.2003</td>
							<td>En-route</td>
						</tr>
						<tr class="case_search_result_separator" style="padding: 0px;"><td colspan="6"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
						
						<tr class="case_search_result_odd_row">
							<td><input type="radio" name="radiobutton" value="radiobutton"></td>
							<td>Publiciani, Johnette Q</td>
							<td>jpubliciani@company.com</td>
							<td>(212) 345-6789</td>
							<td>03.15.2003</td>
							<td>En-route</td>
						</tr>
						<tr class="case_search_result_separator" style="padding: 0px;"><td colspan="6"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
					
					</table>
				</div>
				<%-- search results > 1 --%>
			</div>
		</div>
		
		<div>
			<jsp:include page='/includes/case_summary.jsp'/>
		</div>
		
		<div class="case_footer"><img src="/media_stat/crm/images/clear.gif" width="1" height="4"></div>
		
    </tmpl:put>

</tmpl:insert>
