<%@ page import="java.text.*, java.util.*,com.freshdirect.fdstore.referral.*,com.freshdirect.webapp.util.CCFormatter,com.freshdirect.framework.webapp.ActionResult,java.text.SimpleDateFormat,com.freshdirect.fdstore.referral.EnumReferralProgramStatus" %>

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
    
    
    function getNextReferralProgram(txt)
    {
   
       document.rp.START_INDEX.value=txt;
       document.forms[0].submit();          
    }   
    

    function getSortedReferralProgram(txt)
    {   
       document.rp.SORT_COLUMN_NME.value=txt;
       document.forms[0].submit();          
    }   

    
    var newwindow;
    function poptastic(url)
    {
        if(url=="")
        {
           alert("URL is empty");
        }
        else{
         newwindow=window.open(url,'name','height=400,width=400,resizable=yes,scrollbars=yes');
          if (window.focus) {newwindow.focus()}
        }
    }

    
    </script>

</head>
     


<body marginwidth="0" marginheight="0" class="composite">
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
%>

<form name="rp" method="POST"  action="ref_prg1.jsp">

  <%
    SimpleDateFormat format=new SimpleDateFormat();
    String actionType=null;
    %>
   <ref:referralProgramSummary id="refprogSum" pageName="ref_prg1">          
   


   
   <%
     String sortByColumnName="ID";
     String startIndex="0";
     String previousIndex="0";
     String nextIndex="";
     String rcdSize="0";     
     boolean isMaxRcdSizeReached=false;   
     
     ReferralSearchCriteria criteria=(ReferralSearchCriteria)pageContext.getAttribute("SearchCriteria");
     if(criteria!=null){
         sortByColumnName=criteria.getSortByColumnName();
         startIndex=""+criteria.getStartIndex();         
         previousIndex=""+criteria.getPreviousIndex();
         nextIndex=""+criteria.getEndIndex();
         rcdSize=""+criteria.getTotalRcdSize();         
         if(criteria.getEndIndex()>=criteria.getTotalRcdSize()){
            isMaxRcdSizeReached=true;
         }
     }
%>

<table width="80%" cellpadding="8" cellspacing="0" border="0">
	<tr bgcolor="lightgrey">
        <td > &nbsp;&nbsp;&nbsp;  </td>    
		<td> 
       <%  if(!"ID".equalsIgnoreCase(sortByColumnName)){        %>
        <b> <a href="javascript:getSortedReferralProgram('ID');"> Ref_Prog_Id</a> <b>
        <% }else{  %>
        <b>  Ref_Prog_Id <b>
        <% } %>
        </td>
        <td colspan="2">
        <%  if(!"PROGRAM_NAME".equalsIgnoreCase(sortByColumnName)){        %>
         <b> <a href="javascript:getSortedReferralProgram('PROGRAM_NAME');"> Name <b>
        <% }else{  %>
        <b>  Name <b>
        <% } %>        
        </td>                    
        <td > 
        <%  if(!"PROGRAM_START_DATE".equalsIgnoreCase(sortByColumnName)){        %>
         <b> <a href="javascript:getSortedReferralProgram('PROGRAM_START_DATE');"> Start Date </a> </b> 
        <% }else{  %>
        <b>  Start Date <b>
        <% } %>                
        </td>    
        <td > 
        <%  if(!"PROGRAM_EXP_DATE".equalsIgnoreCase(sortByColumnName)){        %>
         <b> <a href="javascript:getSortedReferralProgram('PROGRAM_EXP_DATE');"> Exp Date </a></b>
        <% }else{  %>
        <b>  Exp Date <b>
        <% } %>                        
        </td>    
        <td > 
        <%  if(!"STATUS".equalsIgnoreCase(sortByColumnName)){        %>
         <b> <a href="javascript:getSortedReferralProgram('STATUS');"> STATUS </a></b>
        <% }else{  %>
        <b>  STATUS <b>
        <% } %>                                
        </td>    
        <td > <b>  CREATIVE URL </b></td>    
	</tr>
  

<input type="hidden" name="pageName" value="ref_prg1"/>
<input type="hidden" name="actionName" value=""/>
<input type="hidden" name="SORT_COLUMN_NME" value="<%=sortByColumnName%>"/>
<input type="hidden" name="START_INDEX" value="<%=startIndex%>"/>
<input type="hidden" name="RCD_SIZE" value="<%=rcdSize%>"/>
   
   <logic:iterate id="rp" collection="<%=refprogSum%>" type="com.freshdirect.fdstore.referral.ReferralProgram" >
   
    <tr> 
        <td >   </td>    
		<td> <%=rp.getPK().getId() %></td>
        <td colspan="2"> <b3><a href="ref_prg2.jsp?SORT_COLUMN_NME=<%=sortByColumnName%>&START_INDEX=<%=startIndex%>&actionName=update&selectRefPrg=<%=rp.getPK().getId() %>"> <%=rp.getName()%></a><b3></td>    
        <td >  <%=CCFormatter.formatDateYear(rp.getStartDate())%> </td>            
        <td > <%=CCFormatter.formatDateYear(rp.getExpDate())%> </td>            
        <td > <%=rp.getStatus()%> </td>            
        <td ><a href="javascript:poptastic('<%=rp.getCreativeUrl()==null?"":rp.getCreativeUrl()%>');">click here</a></td>            
    </tr>
</logic:iterate>

</table>
<br>
<table width="80%" cellpadding="8" cellspacing="0" border="0">
<tr width="100%">
<td colspan=3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>
<%
if(!isMaxRcdSizeReached){
%>
<b><a href="javascript:getNextReferralProgram('<%=nextIndex %>');">  next </a></b>
<% } %>
</td>
<td>
<%
  if(!"0".equals(startIndex)){
%>
<b><a href="javascript:getNextReferralProgram('<%=previousIndex %>');">  previous </a></b>
<% }  %>
</td>
</tr>
</table>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="ref_prg2.jsp?SORT_COLUMN_NME=<%=sortByColumnName%>&START_INDEX=<%=startIndex%>">Click here to create new Referral Program</a>
</ref:referralProgramSummary>
</form>
</div>
</div>

</body>
</html>
<!-- Render time: ~ 333 ms -->


