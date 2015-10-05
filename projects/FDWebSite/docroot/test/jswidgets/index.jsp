<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>JS Widget test page</title>
  <jwr:style src="/grid.css" media="all" />
  <jwr:style src="/global.css" media="all" />
  <style>
    dt.optional {
      font-style: italic;
    }

    dt.optional:after {
      content: " (opt)";
    }
  </style>

  <jwr:script src="/fdlibs.js"  useRandomParam="false" />
</head>
<body>
  <h1>JS widget test page</h1>

  <div class="module">
    <h2>FreshDirect.utils (fd/utils.js)</h2>
    <p>aka. <code>FreshDirect.modules.common.utils</code></p>

    <div class="method">
      <h3>mknamespace</h3>
      <dl>
        <dt>namespace</dt>
        <dd>The namespace to register, as a dotted path.</dd>
        <dt class="optional">container</dt>
        <dd>The container to put the namespace into.</dd>
      </dl>
      <pre class="example">
        FreshDirect.utils.mknamespace("testpage.widgets", FreshDirect);
        // FreshDirect.testpage.widgets = {};
      </pre>
      <script>
        FreshDirect.utils.mknamespace("testpage.widgets", FreshDirect);
        console.log('FreshDirect.testpage.widgets: ', FreshDirect.testpage.widgets);
      </script>
    </div>

  </div>

  <soy:import packageName="common"/>

  <jwr:script src="/fdmodules.js"  useRandomParam="false" />
  <jwr:script src="/fdcomponents.js"  useRandomParam="false" />
</body>
</html>
