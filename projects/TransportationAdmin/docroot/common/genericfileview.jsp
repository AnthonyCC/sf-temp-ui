<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import='com.freshdirect.transadmin.web.model.*' %>

<tmpl:insert template='/common/sitelocation.jsp'>

    <tmpl:put name='title' direct='true'>Generic File Viewer</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<br/>	
		<div align="center">
			<form method="post" enctype="multipart/form-data">				
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="screentitle">Generic File Viewer</td>
					</tr>
					<tr>
						<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
					</tr>
					
					<tr>
						<td class="screencontent">
							<table class="forms1">			  	
							  <tr>
							    <td>Input File</td>
							    <td> 								  
							  	 	<input type="file" size="50" name="file"/>
							 	</td>
							 	<td>&nbsp;</td>
							 </tr>
																		 							  
							<tr>
							 <td>File Type</td>
			                  <td>
			                    <spring:bind path="command.fileType">
			                      <select name="<c:out value="${status.expression}"/>">
				                      <option value="">--Please Select File Type</option> 
				                      <c:forEach var="typeRow" items="${filetypes}">                             
				                          <c:choose>
				                            <c:when test="${status.value == typeRow.key}" > 
				                              <option selected value="<c:out value="${typeRow.key}"/>"><c:out value="${typeRow.value}"/></option>
				                            </c:when>
				                            <c:otherwise> 
				                            	<option value="<c:out value="${typeRow.key}"/>"><c:out value="${typeRow.value}"/></option>
				                          	</c:otherwise> 
				                        	</c:choose>      
				                        </c:forEach>   
			                  	</select>
			                  	<c:forEach items="${status.errorMessages}" var="error">
                                    &nbsp;<span id="fileType.errors"><c:out value="${error}"/></span>
                                </c:forEach> 
			                    </spring:bind>
			                    
			                   </td>
						 	<td>&nbsp;</td>
                			</tr>
                			
							<tr>
							    <td colspan="3" align="center">
								   <input type = "submit" value="&nbsp;Process&nbsp;"  />
								</td>			
							</tr>
							
							
							</table>				
							
						</td>
					</tr>								
				</table>			
			</form>
			
		 </div>
		 	<br/>
		 	<div style="margin-left: 15px;margin-right: 5px;width : 98%; height : 500px;overflow : auto;" >
			<table border="1"  cellspacing="0"  cellpadding="0"  class="fileTabRegion"  width="98%">			
			    	 <% GenericBeanCommand baseBean = (GenericBeanCommand)request.getAttribute("command"); 
				    	 out.println(baseBean.getFileHtml()); %>
				</table>
			</div>		
		 
	</tmpl:put>
</tmpl:insert>