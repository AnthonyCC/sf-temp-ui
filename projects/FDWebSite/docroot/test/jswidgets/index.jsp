<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>JS Widget test page</title>
  <jwr:style src="/grid.css" media="all" />
  <jwr:style src="/global.css" media="all" />
</head>
<body>
  <h1>JS widget test page</h1>

  <jwr:script src="/fdlibs.js"  useRandomParam="false" />

  <soy:import packageName="common"/>

  <jwr:script src="/fdmodules.js"  useRandomParam="false" />
  <jwr:script src="/fdcomponents.js"  useRandomParam="false" />
</body>
</html>
