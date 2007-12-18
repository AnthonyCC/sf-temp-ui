<%@ page import="java.text.*, java.util.*,com.freshdirect.fdstore.referral.*,com.freshdirect.framework.webapp.ActionResult,java.text.SimpleDateFormat,com.freshdirect.fdstore.referral.EnumReferralProgramStatus" %>

<%@ taglib uri="refProgram" prefix="ref" %>

<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="template" prefix="tmpl" %>
<!DOCTYPE HTML PUBLIC "-//CollabNet//DTD XHTML 1.0 Transitional//EN">
<!-- Application: DlvAdminTest -->
<!-- Page: Home -->
<!-- Generated: Fri Oct 06 12:07:27 EDT 2006 -->
<html>
<head>
<meta name="generator" content="Tapestry Application Framework, version 2.3"/>
<title> Referral Program </title>

<link rel="stylesheet" href="/ccassets/css/tigris.css"  type="text/css" /> 
<link rel="stylesheet" href="/ccassets/css/referral.css" type="text/css" />    

<script language="javascript">

function textCounter(field, maxlimit) {
if (field.value.length > maxlimit) // if too long...trim it!
{
  field.value = field.value.substring(0, maxlimit);
}

}

function callReferralTag(txt)
{

   if(txt=="create" || txt=='save' )
   {
      
       document.rp.actionName.value=txt;                   
       document.forms[0].submit();       
   }
   else if(txt=="update")
   {
       
       document.rp.actionName.value=txt;               
       document.forms[0].submit();       

   }

   else if(txt=="delete")
   {

       document.rp.actionName.value=txt;
       document.forms[0].submit();       
   }

}   
    
function checkError()
{
  //alert("function checkError"+document.rp.errMsg1.value) 
  if(document.rp.errMsg1.value!="" && document.rp.errMsg1.value=="false")
  {
     //alert(document.rp.errMsg1.value);
     var startIndex=document.rp.START_INDEX.value;
     var sortColumnName=document.rp.SORT_COLUMN_NME.value;          
     document.location.href = 'ref_part1.jsp?SORT_COLUMN_NME='+sortColumnName+'&START_INDEX='+startIndex;
  }     
}


function clearAll(txt)
{
    if(txt!="save")
    {
       document.rp.refPartName.value="";   
    }   
    document.rp.refPartDesc.value="";
    
    
}

</script>

</head>

<body marginwidth="0" marginheight="0" class="composite" onLoad="checkError()">
<div id="banner">
	<table border="0" width="100%" cellpadding="8" cellspacing="0">
		<tr>
			<td>FreshDirect - Referral Program</td>
	    </tr>
	</table>
</div>
<div id="breadcrumbs">
	<table border="0" width="100%" cellpadding="4" cellspacing="0">
		<tr>
			<td>



<<a href="ref_prg1.jsp"> Referral Program</a>


	&nbsp; | &nbsp;


<a href="ref_camp1.jsp">Referral Campaign</a>


	&nbsp; | &nbsp;


<a href="ref_obj1.jsp">Referral Objective</a>


	&nbsp; | &nbsp;



<font color="green">Referral Partner</font>


	&nbsp; | &nbsp;


<a href="ref_cha1.jsp">Referral Channel</a>


	&nbsp; | &nbsp;



	</td>
			<td>
				<div align="right">
				</div>
			</td>
		</tr>
	</table>
</div>
<br>
<br>
<div class="bodycol">
<div class="app">

<%
   String selectRefPart=(String)request.getParameter("selectRefPart");
   if(selectRefPart==null) selectRefPart="";
   
%>

<form name="rp" method="POST"  action="ref_part2.jsp">
<input type="hidden" name="pageName" value="ref_part2"/>
<input type="hidden" name="actionName" value=""/>
<input type="hidden" name="selectRefPart" value="<%=selectRefPart%>"/>
  

 <%
    SimpleDateFormat format=new SimpleDateFormat();
    format.applyPattern("yyyy-mm-dd");  
    String actionType=null;
    %>

  <ref:referralPartner id="refPart" pageName="ref_part2">   

<input type="hidden" name="refPartId" value="<%=refPart.getPK()==null?"":refPart.getPK().getId()%>"/>

<%
   
   actionType=(String)request.getAttribute("actionType");
   if(actionType==null) actionType="";
   String errorMsg1=(String)request.getAttribute("hasError");
   if(errorMsg1==null) errorMsg1="";
   
   String startIndex=(String)request.getParameter("START_INDEX");
   String sortByColumnName=(String)request.getParameter("SORT_COLUMN_NME");
   
%>

<input type="hidden" name="errMsg1" value='<%=errorMsg1%>'/>  
<input type="hidden" name="SORT_COLUMN_NME" value="<%=sortByColumnName%>"/>
<input type="hidden" name="START_INDEX" value="<%=startIndex%>"/>

<table width="80%" cellpadding="1" cellspacing="1" border="1">
<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"> <b><U> REFERRAL PARTNER <UL><b></td> 
 <td></td> 
 <td></td> 
 <td></td> 
</tr>
</table>

<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <font color="green"><b>Note : </b>  <font color="red"> * </font> is a Mandatory Field </font>
<br>
<br>

<%
    ActionResult result=(ActionResult) pageContext.getAttribute("result");
    //System.out.println("result"+result+"refPrg"+refPart);
%>

<table width="80%" cellpadding="0" cellspacing="0" border="1">
<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"> <font color="red">*</font> <b> PARTNER NAME : <b></td> 
 <td>
<%
if("save".equalsIgnoreCase(actionType))
   {
%>
<input type="text" name="refPartName" value="<%=refPart.getName()%>" readOnly="true" />
<%
}else{
%>
<input type="text" name="refPartName" value="<%=refPart.getName()%>" maxlength="55" />
<%
}
%>
  <ref:ErrorHandler result='<%= result %>' name='name' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>
  
 </td> 
 <td></td> 
 <td></td> 
</tr>

<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"> <font color="red">*</font> <b> PARTNER DESC: <b></td> 
 <td> 
 <textarea name="refPartDesc" rows="3" cols="40" style="width: 220px" onKeyDown="textCounter(this.form.refPartDesc,255);" onKeyUp="textCounter(this.form.refPartDesc,255);" ><%=refPart.getDescription()%></textarea>
 <ref:ErrorHandler result='<%= result %>' name='description' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>
 </td> 
 <td></td> 
 <td></td> 
</tr>


<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"> </td> 
 <td> </td> 
 <td></td> 
 <td></td> 
</tr>

<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="3" align="center"><ref:ErrorHandler result='<%= result %>' name='delResult' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler> </td> 
 </td> </td> 
 <td></td> 
</tr>

<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"> </td> 
 <td> </td> 
 <td></td> 
 <td></td> 
</tr>

<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"> </td> 
 <td>  
 </td>
 </td> </td> 
 <td></td> 
</tr>

<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"></td> 
 <td>
  </td>    
 <td></td> 
 <td></td> 
</tr>




<tr>

</table>


<table width="80%" cellpadding="1" cellspacing="1" border="1">
<tr>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<%
if("save".equalsIgnoreCase(actionType))
   {
%>
<td align="left"><input type="button" name="UPDATE" value="UPDATE"  onClick="javascript:callReferralTag('save')" /></td>
<td align="left"><input type="button" name="DELETE" value="DELETE" onClick="javascript:callReferralTag('delete')" /></td>
<% }else{ %>
<td align="left"><input type="button" name="CREATE" value="  CREATE  " onClick="javascript:callReferralTag('create')" /></td>
<% } %>
<td align="left"><input type="button" name="CLEAR" value=" CLEAR " onClick="javascript:clearAll('<%=actionType%>')"/></td>
<td align="left"><INPUT type="button" value=" back " onClick="location.href='ref_part1.jsp?SORT_COLUMN_NME=<%=sortByColumnName%>&START_INDEX=<%=startIndex%>'"/></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>

</ref:referralPartner>


</form>
</div>
</div>

</body>
</html>
<!-- Render time: ~ 333 ms -->


