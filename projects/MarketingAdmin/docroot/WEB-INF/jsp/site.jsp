<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>/ FreshDirect Marketing Admin : <tmpl:get name='title'/> /</title>
	<link rel="stylesheet" href="css/transportation.css" type="text/css" />		
	<link rel="stylesheet" href="css/extremecomponents.css" type="text/css" />
	<script src="js/RowHandlers.js" language="javascript" type="text/javascript"></script>
	<script src="js/action.js" language="javascript" type="text/javascript"></script>
		
	<link rel="stylesheet" type="text/css" href="js/JSCal2-1.9/src/css/jscal2.css" />
    <link rel="stylesheet" type="text/css" href="js/JSCal2-1.9/src/css/border-radius.css" />
    <link rel="stylesheet" type="text/css" href="js/JSCal2-1.9/src/css/win2k/win2k.css" />
    <script type="text/javascript" src="js/JSCal2-1.9/src/js/jscal2.js"></script>
    <script type="text/javascript" src="js/JSCal2-1.9/src/js/lang/en.js"></script>


		<title>Marketing Admin ::  Spring Framework </title>
		<style type="text/css">
		<!--
			td { padding:3px; }
			div#top {position:absolute; top: 0px; left: 0px; background-color: #E4EFF3;background-image: url(logo.gif);background-position: 100px 1px;background-repeat: no-repeat; height: 50px; width:100%; padding:0px; border: none;margin: 0;}
            
        .competitorHeader
        {
            FONT-WEIGHT: bold;
            FONT-SIZE: 11pt;
            BACKGROUND: #cccccc;
            LINE-HEIGHT: 12pt;
            PADDING-TOP: 2pt;
            FONT-FAMILY: Arial, Helvetica, Verdana, sans-serif;
        }            
		// -->
		</style>	



</head>
<body>
   <body marginwidth="0" marginheight="0" class="composite">
	<table class="appframe" width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr class="apptitle" >
				<td width="40%">
					<img src="images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban" />
				</td>
				<td width="60%">Marketing Admin</td>
			</tr>			
	</table>	
    <div id="banner">
	<table border="0" width="100%" cellpadding="8" cellspacing="0">
		<tr>
			<td width="100%"><center><font color="green"> <b>FreshDirect - Marketing Admin Application</b></font></center></td>            
	    </tr>
	</table>
   </div>
     <tmpl:get name='content'/>
	<br clear="all"/>
	<div class="separator"></div>
	<div class="footer"><jsp:include page='copyright.jsp'/></div>    
    <table style="width:100%"><tr>
		<td><A href="<c:url value="welcome.do"/>">Home</A></td>
		<td style="text-align:right;color:silver">Markting Admin Application</td>
	</tr></table>
</body>
</html>