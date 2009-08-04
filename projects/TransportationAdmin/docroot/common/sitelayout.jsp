<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import='com.freshdirect.transadmin.security.*' %>
<%@ page import='java.util.*' %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <title>/ FreshDirect Transportation Admin : <tmpl:get name='title'/> /</title>
	<link rel="stylesheet" href="css/transportation.css" type="text/css" />		
	<link rel="stylesheet" href="css/extremecomponents.css" type="text/css" />
	<link rel="stylesheet" href="css/jscalendar-1.0/calendar-system.css" type="text/css" />
	<script src="js/RowHandlers.js" language="javascript" type="text/javascript"></script>
	<script src="js/action.js" language="javascript" type="text/javascript"></script>
		
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" language="javascript" src="js/jscalendar-1.0/calendar-setup.js"></script>
    <script type="text/javascript" language="javascript" src="js/filter.js"></script>
	<script language="javascript" src="js/time.js"></script>
	<script type="text/javascript">
		/* allow lower resolutions to display */
		if (screen.height < 768 && screen.width <= 1024) {
			var newCSS;
			var headID = document.getElementsByTagName("head")[0];

			newCSS = document.createElement('link');
			newCSS.type = 'text/css';
			newCSS.rel = 'stylesheet';
			newCSS.href = 'css/lowres.css';
			newCSS.media = 'screen';
			headID.appendChild(newCSS);
		}
	</script>
	<tmpl:get name='yui-lib'/>
	<tmpl:get name='gmap-lib'/>
	
	
	
</head>

<body id="body1" class="" marginwidth="0" marginheight="0" border="0">
	<script>
    var s='<tmpl:get name='yui-skin'/>';   
    if(s=='') s="yui-skin-sam";
    document.getElementById("body1").className=s;          
	</script>
			<%
				try {
				MenuGroup menuGroup = MenuManager.getInstance().getMenuGroup(request);				
				Set rootMenus = menuGroup.getRootMenus();
				
				Iterator itr = rootMenus.iterator(); 
				String currentRootMenuId = "";
				if(menuGroup.getCurrentRootMenu() != null) {
					currentRootMenuId = menuGroup.getCurrentRootMenu().getMenuId();
				}
				String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(request);
				String userRole = com.freshdirect.transadmin.security.SecurityManager.getUserRole(request);
				%>
				<div class="Tlogo">
					<img src="./images/TransAppLogo.gif" width="189" height="75" border="0" alt="" title="">
				</div>
				<div class="t_tab Tspacer">
					&nbsp;
				</div>
				<div class="userinfo">
					<% if(userId != null) { %>
					<div class="loginout"><input type="button" value="LOG OUT" onclick="location.href='logout.jsp'"></div>
					<% } else { %>
					<div class="loginout"><input type="button" value="LOG IN" onclick="location.href='.'"></div>
					<% } %>
					<div><span class="nameTitle"><a href="javascript:showUserPref()"> User:</a></span> <span style=""><%= userId != null ? userId : ""%></span></div>
					<div><span class="levelTitle">Level:</span> <span style=""><%=userRole != null ? userRole : ""%></span></div>
				</div>

				<div>
					<div class="t_tab_hspacer">&nbsp;</div>
					<div class="<%= currentRootMenuId %>">
							
					<% while(itr.hasNext()) {
						Menu tmpRootMenu = (Menu)itr.next(); %>
							
							<div class="tableft tabL_<%=tmpRootMenu.getMenuId() %>">
								&nbsp;
							</div>
							<div class="t_tab <%=tmpRootMenu.getMenuId() %>">
								<div class="minwidth"><!-- --></div>
								<a href="<%= tmpRootMenu.getMenuLink() %>" target="_self" class="<%=tmpRootMenu.getMenuId() %>"><%=tmpRootMenu.getMenuTitle() %></a>
							</div>
							<div class="tabright tabR_<%=tmpRootMenu.getMenuId() %>">
								&nbsp;
							</div>					
					<%} %>
					</div>
				</div>
				<div class="subs <%=currentRootMenuId%>">
					<%
						Map subMenus = menuGroup.groupSubMenus();
						
						List leftList = (List)subMenus.get("LEFT");
						List rightList = (List)subMenus.get("RIGHT");
						
						itr = leftList.iterator(); %>
						<div class="subs_left">	
						<%	while(itr.hasNext()) {
								Menu tmpSubMenu = (Menu)itr.next(); %>
						<div class="sub_tableft sub_tabL_<%=currentRootMenuId%>
							<%= (menuGroup.getCurrentSubMenu().equals(tmpSubMenu) ? "activeL" :"")%>">
							&nbsp;
						</div>
						<div class="subtab <%= (menuGroup.getCurrentSubMenu().equals(tmpSubMenu) ? "activeT" :"")%>">
							<div class="minwidth"><!-- --></div>
							<a href="<%= tmpSubMenu.getMenuLink() %>"><%=tmpSubMenu.getMenuTitle() %></a>
						</div>
						<div class="sub_tabright sub_tabR_<%=currentRootMenuId%>
							<%= (menuGroup.getCurrentSubMenu().equals(tmpSubMenu) ? "activeR" :"")%>">
							&nbsp;
						</div>		
					<%	}
					%>
					</div>
					<%itr = rightList.iterator(); %>
						<div class="subs_right">	
						<%	while(itr.hasNext()) {
								Menu tmpSubMenu = (Menu)itr.next(); %>
						<div class="sub_tableft sub_tabL_<%=currentRootMenuId%>
							<%= (menuGroup.getCurrentSubMenu().equals(tmpSubMenu) ? "activeL" :"")%>">
							&nbsp;
						</div>
						<div class="subtab <%= (menuGroup.getCurrentSubMenu().equals(tmpSubMenu) ? "activeT" :"")%>">
							<div class="minwidth"><!-- --></div>
							<a href="<%= tmpSubMenu.getMenuLink() %>"><%=tmpSubMenu.getMenuTitle() %></a>
						</div>
						<div class="sub_tabright sub_tabR_<%=currentRootMenuId%>
							<%= (menuGroup.getCurrentSubMenu().equals(tmpSubMenu) ? "activeR" :"")%>">
							&nbsp;
						</div>		
					<%	}
					} catch (Exception e) {
						e.printStackTrace();
					}%>
					</div>
				</div>
		<br style="clear:both;">
    <tmpl:get name='content'/>
	<br>
	 <%@ include file='i_userPref.jspf'%> 
</body>
</html>
