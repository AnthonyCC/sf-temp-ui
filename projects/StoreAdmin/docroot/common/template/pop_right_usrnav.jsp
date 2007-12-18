<%@ taglib uri='template' prefix='tmpl' %>
<% String servletContext = request.getContextPath(); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title><tmpl:get name='title'/></title>
	<link rel="stylesheet" href="<%= servletContext %>/common/css/store_admin.css" type="text/css">
	<script language="Javascript" src="<%= servletContext %>/common/javascript/common.js"></script>
</head>
<body class="basecolor" style="font-size: 8pt;">

    <%-- content left and tree in rightnav --%>
    	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="popHeader"">
    	<tr class="popHeader"><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="10"></tr>

    	<tr class="popHeader"><td>
                <tmpl:get name='heading'/>
        </tr></td>
        </table>
    <div style="float:left;width:70%;height:100%">

    	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
        	<tr>
        	<td width="92%">
                <tmpl:get name='header'/>
            </td>
        	</tr>
        </table>
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
        	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>
        	<tr><td class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
    	</table>
    	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
        	<tr><td>
                    <tmpl:get name='buttons'/>
            </tr></td>
        </table>
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
        	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>
        	<tr><td class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
    	</table>
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
        	<tr><td>
        	        <tmpl:get name='contentHeader'/>
        	</td></tr>
    	</table>
        <div style="float:right;width:98%;height:50%;overflow-y:scroll;overflow-x:hidden;">
        	<table height="100%" width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
                <tr><td>
            	<tmpl:get name='content'/>
            	</td></tr>
            </table>
        </div>
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
        	<tr><td><img src="<%= servletContext %>/images/clear.gif" width="1" height="4"></td></tr>
        	<tr><td class="separator"><img src="<%= servletContext %>/images/clear.gif" width="1" height="1"></td></tr>
    	</table>
    	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="pop">
        	<tr><td>
              <tmpl:get name='buttons'/>
            </tr></td>
        </table>
    </div>
	<div style="float:right;width:30%;height:100%">
	 	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="popHeader">
        	<tr><td>
                    <tmpl:get name='navHeader'/>
            </td></tr>
        </table>
        <div style="border: 2 solid; border-top-color: #CFCFCF; border-left-color: #CFCFCF; border-right-color: #505050; border-bottom-color: #505050; ; float:right;width:100%;height:58%;overflow-y:scroll;overflow-x:hidden;">
    	 	<table width="100%" cellpadding="2" cellspacing="0" border="0" class="tree">
            	<tr><td>
            	        <tmpl:get name='navBody'/>
            	    </td>
                </tr>
        	</table>
        </div>
    </div>
</body>
</html>