<!DOCTYPE html>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<html lang="en-US" xml:lang="en-US">
<head>
  <meta charset="UTF-8">
  <%-- <title>Soy Template Test Page</title> --%>
   <fd:SEOMetaTag title="Soy Template Test Page"/>
  
  <jwr:script src="/fdlibs.js"/>
  <jwr:script src="/fdcomponents.js"/>
  
  <soy:import packageName="common"/>
  
</head>
<body>

</body>
<script type="text/javascript">
	document.body.appendChild(soy.renderAsFragment(common.quantitybox,{
		qMin:3,
		qMax:10,
		qInc:2,
		quantity:6
	}));
	document.body.appendChild(soy.renderAsFragment(common.quantitybox,{
		qMin:3,
		qMax:10,
		qInc:2,
		quantity:6,
		mayempty:true
	}));
</script>
</html>
