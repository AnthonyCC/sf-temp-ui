<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
  <meta charset="UTF-8">
  <title>Multi-search test page</title>
  <jwr:style src="/grid.css" media="all" />
  <jwr:style src="/oldglobal.css" media="all" />
  <jwr:style src="/global.css" media="all" />
  <jwr:style src="/multisearch.css" media="all" />
  <jwr:script src="/fdlibs.js"  useRandomParam="false" />
</head>
<body>
  <h1>Multi-search test page</h1>

  <div id="multisearch-input">
  </div>

  <div id="multisearch-results" class="contentModules">
  </div>

  <soy:import packageName="common"/>
  <soy:import packageName="multisrch"/>

<script>
// don't open zipcheck
var FreshDirect = window.FreshDirect || {};
FreshDirect.user = FreshDirect.user || {};
FreshDirect.user.isZipPopupUsed = true;
</script>

  <jwr:script src="/fdmodules.js" useRandomParam="false" />
  <jwr:script src="/fdcomponents.js" useRandomParam="false" />
  <jwr:script src="/fdcommon.js" useRandomParam="false" />
  <jwr:script src="/multisearch.js" useRandomParam="false" />
</body>
</html>
