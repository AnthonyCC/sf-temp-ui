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
    .module{
      margin:20px;
    }

    .module-title{
      font-size:26px;
      background-color:antiquewhite;
    }

    .module-title:before{
      content: 'Module: ';
    }

    .method{
      background-color:aliceblue;
      margin:25px;
    }

    .method-title{
      font-size:24px;
    }

    .method-title:before{
      content:'Function: ';
    }

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

  <div id="FD_utils" class="module">
    <h2 class="module-title">FreshDirect.utils (fd/utils.js)</h2>
    <p>aka. <code>FreshDirect.modules.common.utils</code></p>

    <div id="FD_utils_mknamespace" class="method">
      <h3 class="method-title">mknamespace()</h3>
      <p class="description">
      Create a namespace.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>namespace</dt>
        <dd>The namespace to register, as a dotted path</dd>
        <dt class="optional">container</dt>
        <dd>The container to put the namespace into</dd>
      </dl>
      <pre class="prettyprint example">
        FreshDirect.utils.mknamespace("testpage.widgets", FreshDirect);
        // FreshDirect.testpage.widgets = {};
      </pre>
      <script>
        FreshDirect.utils.mknamespace("testpage.widgets", FreshDirect);
        console.log('FreshDirect.testpage.widgets: ', FreshDirect.testpage.widgets);
      </script>
    </div>

    <div id="FD_utils_register" class="method">
      <h3 class="method-title">register()</h3>
      <p class="description">
      Register an object under a given namespace.
      </p>
      <h4>Parameters</h4>
      <dl
        <dt>namespace</dt>
        <dd>The target namespace</dd>
        <dt>name</dt>
        <dd>The name for the object under the namespace</dd>
        <dt>obj</dt>
        <dd>The object to add</dd>
        <dt class="optional">root</dt>
        <dd>Namespace root</dd>
        <dt class="optional">feature</dt>
        <dd>Feature name for the module [partial rollout related]</dd>
        <dt class="optional">version</dt>
        <dd>Version of the module for the given feature [partial rollout related]</dd>
      </dl>
      <pre class="prettyprint example">
        FreshDirect.utils.register("testpage.widgets", "logSomething", {
          log1: function () {console.log("test1");},
          log2: function () {console.log("test2");}
        }, FreshDirect);
        FreshDirect.testpage.widgets.logSomething.log1();
        // test1
        FreshDirect.testpage.widgets.logSomething.log2();
        // test2
      </pre>
      <script>
        FreshDirect.utils.register("testpage.widgets", "logSomething", {
          log1: function () {console.log("test1");},
          log2: function () {console.log("test2");}
        }, FreshDirect);
        FreshDirect.testpage.widgets.logSomething.log1();
        FreshDirect.testpage.widgets.logSomething.log2();
      </script>
    </div>

    <div id="FD_utils_registerModule" class="method">
      <h3 class="method-title">registerModule()</h3>
      <p class="description">
      Register a module under a given namespace.<br/>
      Basically same as <a href="#FD_utils.register">register()</a>, but if the given object is a function,
      then the result of the function call will be registered.<br/>
      In this case the function will be called with the following parameters:<br/>
      <code>fn(root, feature, version)</code><br/>
      This can be useful for partial rollout related features. The default module will determined by the feature framework (see later) or if the given featrue is not in the system yet then the first registered module will be the default.
      </p>
      <pre class="prettyprint example">
        FreshDirect.utils.registerModule("testpage.widgets", "logModule", function (r, f, v) {
          return {
            log: function () {console.log('logger for '+f+' feature (version '+v+')');}
          };
        }, FreshDirect, "testpage", "1.0");
        FreshDirect.testpage.widgets.logModule.log();
        // logger for testpage feature (version 1.0)
      </pre>
      <script>
        FreshDirect.utils.registerModule("testpage.widgets", "logModule", function (r, f, v) {
          return {
            log: function () {console.log('logger for '+f+' feature (version '+v+')');}
          };
        }, FreshDirect, "testpage", "1.0");
        FreshDirect.testpage.widgets.logModule.log();
      </script>
    </div>

    <div id="FD_utils_module" class="method">
      <h3 class="method-title">module()</h3>
      <p class="description">
      Gets the registered module with the given version.<br/>
      While the default version of the registered modules can be accessed easily, this method is used to access the
      different versions.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>name</dt>
        <dd>Dotted name of the module</dd>
        <dt>root</dt>
        <dd>Namespace root</dd>
        <dt class="optional">feature</dt>
        <dd>Feature name</dd>
        <dt class="optional">version</dt>
        <dd>Feature version</dd>
      </dl>
      <pre class="prettyprint example">
        FreshDirect.utils.registerModule("testpage.widgets", "logModule", function (r, f, v) {
          return {
            log: function () {console.log('enhanced logger for '+f+' feature (version '+v+')');}
          };
        }, FreshDirect, "testpage", "2.0");
        FreshDirect.testpage.widgets.logModule.log(); // 1.0 is the default
        // logger for testpage feature (version 1.0)
        FreshDirect.utils.module("testpage.widgets.logModule", FreshDirect, "testpage", "1.0").log();
        // logger for testpage feature (version 1.0)
        FreshDirect.utils.module("testpage.widgets.logModule", FreshDirect, "testpage", "2.0").log();
        // enhanced logger for testpage feature (version 2.0)
      </pre>
      <script>
        FreshDirect.utils.registerModule("testpage.widgets", "logModule", function (r, f, v) {
          return {
            log: function () {console.log('enhanced logger for '+f+' feature (version '+v+')');}
          };
        }, FreshDirect, "testpage", "2.0");
        FreshDirect.testpage.widgets.logModule.log(); // 1.0 is the default
        FreshDirect.utils.module("testpage.widgets.logModule", FreshDirect, "testpage", "1.0").log();
        FreshDirect.utils.module("testpage.widgets.logModule", FreshDirect, "testpage", "2.0").log();
      </script>
    </div>

<%-- template

    <div id="FD_" class="method">
      <h3 class="method-title"></h3>
      <p class="description">
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt></dt>
        <dd></dd>
        <dt class="optional"></dt>
        <dd></dd>
      </dl>
      <pre class="prettyprint example">
      </pre>
      <script>
      </script>
    </div>
--%>

  </div>

  <soy:import packageName="common"/>

  <jwr:script src="/fdmodules.js"  useRandomParam="false" />
  <jwr:script src="/fdcomponents.js"  useRandomParam="false" />
  <script src="https://cdn.rawgit.com/google/code-prettify/master/loader/run_prettify.js"></script>
</body>
</html>
