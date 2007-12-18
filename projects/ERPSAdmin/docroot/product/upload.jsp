<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/common/templates/main.jsp'>
    <tmpl:put name='title' content='ERPSY Daisy: Reports' direct='true'/>
    
     <tmpl:put name='content' direct='true'>

        <fd:NutritionUpload result='actionResult' successPage='upload.jsp?result=true'>
<%List updatedSkus = (List) session.getAttribute("updateList");
  if(updatedSkus == null) updatedSkus = new ArrayList();
%>
        <table border='0'>
            <form enctype="multipart/form-data" method='post'>
                <tr>
                     <td>File to upload:</td>
                </tr>
                <tr>
                    <td><input type="file" name="uploaded_file" size="50" /></td>
                </tr>
                <tr>
                    <td colspan='2' align='right'><input type='submit' value='Send File'></td>
                </tr>
            </form>
            <% if(!actionResult.isSuccess() || updatedSkus.size() > 0 ){%>
                <tr> 
                    <td class='<%=actionResult.isSuccess() ? "text11bold" : "text11rbold" %>'> 
                        <%=actionResult.isSuccess() && updatedSkus.size() > 0 ? "Upload Successfull!" : "Upload Failed:" %><br>
                        <%for(Iterator i = actionResult.getErrors().iterator();i.hasNext();){ 
                            ActionError e = (ActionError)i.next(); 
                        %> 
                                <%=e.getDescription()%><br>
                        <%}%>
            
                        <%=updatedSkus.size() > 0 ? "Updated:<br>" : "" %>
                        <%for(Iterator i = updatedSkus.iterator();i.hasNext();){
                            String sku = (String)i.next();
                        %>
                            <a href="product_view.jsp?skuCode=<%=sku%>"><%=sku%></a><br>
                        <%}%>
                    </td>
                </tr>
            <% } %>
        </table>
        </fd:NutritionUpload>
     </tmpl:put>
</tmpl:insert>