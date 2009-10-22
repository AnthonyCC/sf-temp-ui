<%@ page import="java.text.*, java.util.*,com.freshdirect.webapp.util.CCFormatter,com.freshdirect.fdstore.referral.*,com.freshdirect.framework.webapp.ActionResult,java.text.SimpleDateFormat,com.freshdirect.fdstore.referral.EnumReferralProgramStatus" %>

<%@ taglib uri="refProgram" prefix="ref" %>

<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="template" prefix="tmpl" %>
<!DOCTYPE HTML PUBLIC "-//CollabNet//DTD XHTML 1.0 Transitional//EN">
<!-- Application: DlvAdminTest -->
<!-- Page: Home -->
<!-- Generated: Fri Oct 06 12:07:27 EDT 2006 -->
<html>
<head>
<meta name="generator" content="Tapestry Application Framework, version 2.3" />
<title> Referral Program </title>

    <link rel="stylesheet" href="/ccassets/css/tigris.css"  type="text/css" /> 
    <link rel="stylesheet" href="/ccassets/css/referral.css" type="text/css" />    
	<link rel="stylesheet" href="/ccassets/javascript/jscalendar-1.0/calendar-system.css" type="text/css" />         
    
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar-setup.js"></script>   
    
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
       var i;
       var refCheck;
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
     document.location.href = 'ref_prg1.jsp?SORT_COLUMN_NME='+sortColumnName+'&START_INDEX='+startIndex;
  }     
}

function clearAll(txt)
{
    
    if(txt!="save")
    {
       document.rp.refProgName.value="";   
    }        
    document.rp.refProgDesc.value="";
    document.rp.refProgStartDate.value="";
    document.rp.refProgExpDate.value="";    
    document.forms[0].status.selectedIndex = 0;
    document.forms[0].refProgCrtvDesc.value = "";
    document.forms[0].refProgPromoCode.value = "";
    document.forms[0].referralChannel.selectedIndex = 0;
    document.forms[0].referralPartner.selectedIndex = 0;
    document.forms[0].referralCampaign.selectedIndex = 0;
    
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


<font color="green">Referral Program</font>


	&nbsp; | &nbsp;


<a href="ref_camp1.jsp">Referral Campaign</a>


	&nbsp; | &nbsp;


<a href="ref_obj1.jsp">Referral Objective</a>


	&nbsp; | &nbsp;


<a href="ref_part1.jsp">Referral Partner</a>


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
   String selectRefPrg=(String)request.getParameter("selectRefPrg");
   if(selectRefPrg==null) selectRefPrg="";
   
%>

<form name="rp" method="POST"  action="ref_prg2.jsp">
<input type="hidden" name="pageName" value="ref_prg2"/>
<input type="hidden" name="actionName" value=""/>
<input type="hidden" name="selectRefPrg" value="<%=selectRefPrg%>"/>



    <%
    SimpleDateFormat format=new SimpleDateFormat();
    format.applyPattern("yyyy-MM-dd");   
    String actionType=null;
    %>
    <ref:referralProgram id="refProg" pageName="ref_prg2">  


<input type="hidden" name="refProgId" value="<%=refProg.getPK()==null?"":refProg.getPK().getId()%>"/>
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
 <td colspan="2" align="right"> <b><U> REFERRAL PROGRAM <UL><b></td> 
 <td></td> 
 <td></td> 
 <td></td> 
</tr>
</table>


<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <font color="green"><b>Note : </b>  <font color="red"> * </font> is a Mandatory Field </font>


<%
    ActionResult result=(ActionResult) pageContext.getAttribute("result");   
%>

<table width="80%" cellpadding="0" cellspacing="0" border="1">
<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"> <font color="red">*</font><b>  PROGRAM NAME : <b></td> 
 <td> 
 <%
if("save".equalsIgnoreCase(actionType))
   {
%>
<input type="text" name="refProgName" value="<%=refProg.getName()%>" readOnly="true" maxlength="55"/>
<%
}else{ 
%> 
 <input type="text" name="refProgName" value="<%=refProg.getName()%>"  maxlength="55"/>
<% } %> 
 
 <ref:ErrorHandler result='<%= result %>' name='name' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>
 </td> 
 <td></td> 
 <td></td> 
</tr>
<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"><font color="red">*</font> <b> PROGRAM DESC : <b></td> 
 <td> <textarea name="refProgDesc" rows="3" cols="40" style="width: 220px"  onKeyDown="textCounter(this.form.refProgDesc,255);" onKeyUp="textCounter(this.form.refProgDesc,255);" ><%=refProg.getDescription()%></textarea>
 <ref:ErrorHandler result='<%= result %>' name='description' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>

 </td> 
 <td></td> 
 <td></td> 
</tr>

<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"> <font color="red">*</font> <b> PROGRAM START DATE : <b></td> 
 <td> <input type="text" id="refProgStartDate" name="refProgStartDate" value="<%= (refProg.getStartDate()!=null)?CCFormatter.formatDateYear(refProg.getStartDate()):"" %>" readOnly="true"><a href="#" id="trigger_startDate" style="font-size: 9px;">>></a>
<ref:ErrorHandler result='<%= result %>' name='startDate' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>
<script language="javascript">
                            function toggleDate() {
                                document.getElementById("refProgStartDate").value="";                               
                            }    
                            
                            function setDate(field){
                                document.getElementById("refProgStartDate").value=field.value;
                                toggleAny();
                            }
                            
                           
                            
                           
                            
                             Calendar.setup(
                              {
                               showsTime : false,
                               electric : false,
                               inputField : "refProgStartDate",
                               ifFormat : "%Y-%m-%d",
                               singleClick: true,
                              button : "trigger_startDate" 
                              }
                            );
</script>  
 </td>
 <td></td> 
 <td></td> 
</tr>

<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"><font color="red">*</font> <b> PROGRAM EXP DATE : <b></td> 
 <td> <input type="text" id="refProgExpDate" name="refProgExpDate" value="<%= (refProg.getExpDate()!=null)?CCFormatter.formatDateYear(refProg.getExpDate()):""%>" readOnly="true"><a href="#" id="trigger_expDate" style="font-size: 9px;">>></a>
<ref:ErrorHandler result='<%= result %>' name='expDate' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>
<script language="javascript">
                            function toggleDate() {
                                document.getElementById("refProgExpDate").value="";                               
                            }    
                            
                            function setDate(field){
                                document.getElementById("refProgExpDate").value=field.value;
                                toggleAny();
                            }
                            
                           
                            
                           
                            
                             Calendar.setup(
                              {
                               showsTime : false,
                               electric : false,
                               inputField : "refProgExpDate",
                               ifFormat : "%Y-%m-%d",
                               singleClick: true,
                              button : "trigger_expDate" 
                              }
                            );
</script> 
 </td> 
 <td></td> 
 <td></td> 
</tr>

<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"><font color="red">*</font> <b> STATUS : <b></td> 
 <td> 
 <select name="status">
 <option value=""></option>
 <option value="ACT" <%= EnumReferralProgramStatus.ACTIVE.equals(refProg.getStatus())?"selected":"" %> >ACTIVE</option>
 <option value="EXP" <%= EnumReferralProgramStatus.EXPIRED.equals(refProg.getStatus())?"selected":"" %> >EXPIRED</option>
 </select>
 <ref:ErrorHandler result='<%= result %>' name='status' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>
 </td> 
 <td></td> 
 <td></td> 
</tr>


<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"> <font color="red">&nbsp;</font> <b> CREATIVE DESC : <b></td> 
 <td> <input type="text" name="refProgCrtvDesc" value="<%=refProg.getCreativeDesc()==null?"":refProg.getCreativeDesc()%>" maxlength="255"> 
 <ref:ErrorHandler result='<%= result %>' name='creativeDesc' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>

 </td>
 <td></td> 
 <td></td> 
</tr>

<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"><font color="red"> &nbsp;</font> <b> PROMOTION CODE : <b></td> 
 <td> <input type="text" name="refProgPromoCode" value="<%=refProg.getPromotionCode()%>" maxlength="255">
<ref:ErrorHandler result='<%= result %>' name='promotionCode' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>

 </td> 
 <td></td> 
 <td></td> 
</tr>


<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"> <font color="red"> &nbsp;</font> <b> CREATIVE URL : <b></td> 
 <td> <input type="text" name="refProgCreativeUrl" value="<%=refProg.getCreativeUrl()==null?"":refProg.getCreativeUrl()%>" maxlength="255">
<ref:ErrorHandler result='<%= result %>' name='creativeUrl' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>

 </td> 
 <td></td> 
 <td></td> 
</tr>


<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"> <font color="red">*</font> <b> REFERRAL CHANNEL : <b></td> 
 <td>  <select name="referralChannel">
       <option value=""></option>
      <%
             Set chaCollection=FDReferralProgramManager.getAllReferralChannels();
             Iterator chaIterator=chaCollection.iterator();
             while(chaIterator.hasNext())
             {
             ReferralChannel channel=(ReferralChannel)chaIterator.next();
      %>
      <option value="<%=channel.getPK().getId()%>" <%= channel.getName().equalsIgnoreCase(refProg.getChannel().getName())?"selected":"" %> ><%=channel.getName()+"-"+channel.getType() %></option>
      <%
             }
       %>                         
     </select>
     <ref:ErrorHandler result='<%= result %>' name='referralChannel' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>
 </td>
 </td> </td> 
 <td></td> 
</tr>

<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
 <td colspan="2" align="center"> <font color="red"> *</font> <b> REFERRAL PARTNER : <b></td> 
 <td>  <select name="referralPartner">
       <option value=""></option>
      <%
             Set partCollection=FDReferralProgramManager.getAllReferralPartners();
             Iterator partIterator=partCollection.iterator();
             while(partIterator.hasNext())
             {
             ReferralPartner channel=(ReferralPartner)partIterator.next();
      %>
      <option value="<%=channel.getPK().getId()%>"  <%= channel.getName().equalsIgnoreCase(refProg.getPartner().getName())?"selected":"" %> ><%=channel.getName()%> </option>
      <%
             }       
             
       %>           
     </select>
     <ref:ErrorHandler result='<%= result %>' name='referralPartner' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>       
 </td>
 <td></td> 
 <td></td> 
</tr>

<tr>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>  
 <td colspan="2" align="center"> <font color="red"> *</font> <b> REFERRAL CAMPAIGN : <b></td> 
 <td>  <select name="referralCampaign">
       <option value=""></option>
      <%
             Set collection=FDReferralProgramManager.getAllReferralCampaigns();
             Iterator iterator=collection.iterator();
             while(iterator.hasNext())
             {
             ReferralCampaign campaign=(ReferralCampaign)iterator.next();
      %>
      <option value="<%=campaign.getPK().getId()%>" <%= campaign.getName().equalsIgnoreCase(refProg.getCampaign().getName())?"selected":"" %> ><%=campaign.getName()%></option>
      <%
             }
       %>                         
       </select>
       <ref:ErrorHandler result='<%= result %>' name='referralCampaign' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></ref:ErrorHandler>
 </td>
 <td></td> 
</tr>
<tr> 
 <br>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 <td></td>
 <td></td>
 <td></td>
</tr>
<tr> 
 <br>
 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 <td></td>
 <td></td>
 <td></td> 
</tr>


</table>


<table width="80%" cellpadding="1" cellspacing="1" border="1">
<tr>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td align="right">
<%
if("save".equalsIgnoreCase(actionType))
   {
%>
<td align="left"><input type="button" name="UPDATE" value="UPDATE"  onClick="javascript:callReferralTag('save')" /></td>
<td align="left"><input type="button" name="DELETE" value="DELETE" onClick="javascript:callReferralTag('delete')" /></td>
<% }else{ %>
<input type="button" name="CREATE" value="  CREATE  " onClick="javascript:callReferralTag('create')" /></td>
<% } %>
<td align="left"><input type="button" name="CLEAR" value=" CLEAR " onClick="javascript:clearAll('<%=actionType%>')"/></td>
<td align="left"><INPUT type="button" value=" back " onClick="location.href='ref_prg1.jsp?SORT_COLUMN_NME=<%=sortByColumnName%>&START_INDEX=<%=startIndex%>'"/></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</ref:referralProgram>
</form>
</div>
</div>

</body>
</html>
<!-- Render time: ~ 333 ms -->


