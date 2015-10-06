<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>JS Widget test page</title>
  <jwr:style src="/grid.css" media="all" />
  <jwr:style src="/global.css" media="all" />
  <link rel="stylesheet" type="text/css" href="jswidgets.css">
  <jwr:script src="/fdlibs.js"  useRandomParam="false" />

</head>
<body>
  <!-- in real use cases these should be at the bottom of BODY --!>
  <soy:import packageName="common"/>
  <soy:import packageName="test"/>

  <jwr:script src="/fdmodules.js"  useRandomParam="false" />
  <jwr:script src="/fdcomponents.js"  useRandomParam="false" />
  <jwr:script src="/fdcommon.js"  useRandomParam="false" />

  <h1>JS widget test page</h1>
  <p>Click on module titles to expand them</p>

  <div id="FD_utils" class="module" fd-toggle="toggleUtils" fd-toggle-state="disabled">
    <h2 class="module-title" fd-toggle-trigger="toggleUtils">FreshDirect.utils (fd/utils.js)</h2>
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
      </script>
    </div>

    <div id="FD_discover" class="method">
      <h3 class="method-title">discover()</h3>
      <p class="description">
      Resolves a dotted name into an object (if exists).
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>path</dt>
        <dd>The path - dotted name - of the object</dd>
        <dt class="optional">container</dt>
        <dd>The root node</dd>
      </dl>
      <pre class="prettyprint example">
        console.log(FreshDirect.utils.discover('FreshDirect.testpage.widgets'));
        // Object {}
      </pre>
      <script>
        console.log(FreshDirect.utils.discover('FreshDirect.testpage.widgets'));
      </script>
    </div>

    <div id="FD_utils_register" class="method">
      <h3 class="method-title">register()</h3>
      <p class="description">
      Register an object under a given namespace.
      </p>
      <h4>Parameters</h4>
      <dl>
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

    <div id="FD_initModule" class="method">
      <h3 class="method-title">initModule()</h3>
      <p class="description">
      Calls the module's <code>initModule()</code> method with <code>root, feature, version</code> parameters.<br/>
      Useful if the module's initialization differs for different feature versions.
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
        FreshDirect.utils.registerModule("testpage.widgets", "moduleWInit", {
          initModule: function (r, f, v) {console.log('init called for '+f+' feature (version '+v+')');}
        }, FreshDirect);
        FreshDirect.utils.initModule("testpage.widgets.moduleWInit", FreshDirect, "testpage", "1.0");
        // init called for testpage feature (version 1.0)
      </pre>
      <script>
        FreshDirect.utils.registerModule("testpage.widgets", "moduleWInit", {
          initModule: function (r, f, v) {console.log('init called for '+f+' feature (version '+v+')');}
        }, FreshDirect);
        FreshDirect.utils.initModule("testpage.widgets.moduleWInit", FreshDirect, "testpage", "1.0");
      </script>
    </div>

    <div id="FD_getActiveFeaturesFromCookie" class="method">
      <h3 class="method-title">getActiveFeaturesFromCookie()</h3>
      <p class="description">
      Get the active features from browser cookie.<br/>
      Active features could be overridden by a cookie ('features'), these can be set <a href="/test/features/">in this test page</a>, or via the <a href="#FD_setActiveFeatures">setActiveFeatures</a> method.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt class="optional">cname</dt>
        <dd>Cookie name (default: 'features')</dd>
      </dl>
      <pre class="prettyprint example">
        console.log(FreshDirect.utils.getActiveFeaturesFromCookie());
        // Object {} - whatever you set in the test page, or empty
      </pre>
      <script>
        console.log(FreshDirect.utils.getActiveFeaturesFromCookie());
      </script>
    </div>

    <div id="FD_getActiveFeatures" class="method">
      <h3 class="method-title">getActiveFeatures()</h3>
      <p class="description">
      Get the active features from backend or browser cookie.<br/>
      Backend code can be found at the top of <code>i_javascripts.jspf</code>.
      </p>
      <pre class="prettyprint example">
        console.log(FreshDirect.utils.getActiveFeatures());
        // Object {} - whatever you set in the test page, or empty
      </pre>
      <script>
        console.log(FreshDirect.utils.getActiveFeatures());
      </script>
    </div>

    <div id="FD_setActiveFeatures" class="method">
      <h3 class="method-title">setActiveFeatures()</h3>
      <p class="description">
      Set the active features via browser cookie.<br/>
      Active features could be overridden by a cookie ('features').
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>features</dt>
        <dd>
          Features object: <code>{feature1: "version", feature2: version, ...}</code><br/>
          Features not included in the object won't be set to any version.
        </dd>
        <dt class="optional">cname</dt>
        <dd>Cookie name (default: 'features')</dd>
      </dl>
      <pre class="prettyprint example">
        FreshDirect.utils.setActiveFeatures({testFeature: '1_0'});
        console.log(FreshDirect.utils.getActiveFeaturesFromCookie());
        // Object {testFeature: "1_0"}
      </pre>
      <script>
        FreshDirect.utils.setActiveFeatures({testFeature: '1_0'});
        console.log(FreshDirect.utils.getActiveFeaturesFromCookie());
      </script>
    </div>

    <div id="FD_getActive" class="method">
      <h3 class="method-title">getActive()</h3>
      <p class="description">
      Get the active version of the given feature (or "default").
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>feature</dt>
        <dd>Feature name</dd>
      </dl>
      <pre class="prettyprint example">
        console.log(FreshDirect.utils.getActive('testFeature'));
        // 1_0
        console.log(FreshDirect.utils.getActive('notSetFeature'));
        // default
      </pre>
      <script>
        console.log(FreshDirect.utils.getActive('testFeature'));
        console.log(FreshDirect.utils.getActive('notSetFeature'));
      </script>
    </div>

    <div id="FD_isActive" class="method">
      <h3 class="method-title">isActive()</h3>
      <p class="description">
      Check if the given feature/version is active.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>feature</dt>
        <dd>Feature name</dd>
        <dt>version</dt>
        <dd>Feature version</dd>
      </dl>
      <pre class="prettyprint example">
        console.log(FreshDirect.utils.isActive('testFeature', '1_0'));
        // true
        console.log(FreshDirect.utils.isActive('notSetFeature', '2_0'));
        // false
      </pre>
      <script>
        console.log(FreshDirect.utils.isActive('testFeature', '1_0'));
        console.log(FreshDirect.utils.isActive('notSetFeature', '2_0'));
      </script>
    </div>

    <div id="FD_extend" class="method">
      <h3 class="method-title">extend()</h3>
      <p class="description">
      Extends the first object given as parameter with the properties of the other objects.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>obj</dt>
        <dd>
          Object to extend<br/>
          <b>NOTE</b>: this object will be changed, use an empty object if you want to merge multiple objects.
        </dd>
        <dt class="optional">other objects</dt>
        <dd>Objects to copy properties from</dd>
      </dl>
      <pre class="prettyprint example">
        console.log(FreshDirect.utils.extend({}, {a: 1}, {b: 2}));
        // Object {a: 1, b: 2}
      </pre>
      <script>
        console.log(FreshDirect.utils.extend({}, {a: 1}, {b: 2}));
      </script>
    </div>

    <div id="FD_getParameters" class="method">
      <h3 class="method-title">getParameters()</h3>
      <p class="description">
      Get the request parameters (from the URI, or a given string).
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt class="optional">source</dt>
        <dd>Request URI like source of the parameters</dd>
      </dl>
      <pre class="prettyprint example">
        console.log(FreshDirect.utils.getParameters());
        // request parameters of the current page
        console.log(FreshDirect.utils.getParameters('testParam=testValue&apple=An%20apple'));
        // Object {testParam: "testValue", apple: "An apple"}
      </pre>
      <script>
        console.log(FreshDirect.utils.getParameters());
        console.log(FreshDirect.utils.getParameters('testParam=testValue&apple=An%20apple'));
      </script>
    </div>

    <div id="FD_setCookie" class="method">
      <h3 class="method-title">setCookie()</h3>
      <p>aka. <i>createCookie()</i></p>
      <p class="description">
      Set a cookie.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>name</dt>
        <dd>Cookie name</dd>
        <dt>value</dt>
        <dd>Cookie value</dd>
        <dt class="optional">days</dt>
        <dd>Lifetime of the cookie in days</dd>
      </dl>
      <pre class="prettyprint example">
        FreshDirect.utils.setCookie('test', 1);
      </pre>
      <script>
        FreshDirect.utils.setCookie('test', 1);
      </script>
    </div>

    <div id="FD_readCookie" class="method">
      <h3 class="method-title">readCookie()</h3>
      <p class="description">
      Read a cookie.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>name</dt>
        <dd>Cookie name</dd>
      </dl>
      <pre class="prettyprint example">
        console.log(FreshDirect.utils.readCookie('test'));
        // 1 (set in the previous example)
      </pre>
      <script>
        console.log(FreshDirect.utils.readCookie('test'));
      </script>
    </div>

    <div id="FD_eraseCookie" class="method">
      <h3 class="method-title">eraseCookie()</h3>
      <p class="description">
      Erase a cookie.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>name</dt>
        <dd>Cookie name</dd>
      </dl>
      <pre class="prettyprint example">
        FreshDirect.utils.eraseCookie('test');
        console.log(FreshDirect.utils.readCookie('test'));
        // null
      </pre>
      <script>
        FreshDirect.utils.eraseCookie('test');
        console.log(FreshDirect.utils.readCookie('test'));
      </script>
    </div>

  </div>

  <div id="FD_common" class="module" fd-toggle="toggleCommon" fd-toggle-state="disabled">
    <h2 class="module-title" fd-toggle-trigger="toggleCommon">FreshDirect.common (fd/common/**.js)</h2>

    <div id="FD_common_tooltip" class="method deprecated">
      <h3 class="method-title">Tooltip (old, deprecated)</h3>
      <p class="description">
      Basic tooltip class.<br/>
      New tooltips are created for every DOM element that has <code>tooltip</code> class, and exists before the load of the JS common bundle. (Please note that usually the bundle is loaded at the bottom of the page, but in this testpage it's at the top.)<br/>
      [getContent()] The content of the tooltip can be passed via the <code>config</code> parameter, can be the next DOM element (if it has <code>tooltipcontent</code> CSS class), or can be the <code>title</code> attribute of the DOM element.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>el</dt>
        <dd>DOM element</dd>
        <dt class="optional">config</dt>
        <dd>
        Configuration object:
          <ul>
            <li><b>content</b>: HTML content of the tooltip [default: getContent()]</li>
            <li><b>cssClass</b>: extra CSS class for the tooltip [default: 'tooltip']</li>
            <li><b>orientation</b>: "top" or "bottom" [default: 'top']</li>
            <li><b>offset</b>: gap between the object and the tooltip [default: 10]</li>
          </ul>
        </dd>
      </dl>
      <pre class="prettyprint example">
        new FreshDirect.modules.common.Tooltip(document.getElementById('oldtooltipexample'), {orientation: 'bottom'});
      </pre>

      <a href="#FD_common_tooltip" title="Example tooltip" class="hastooltip">Link w/ tooltip</a>
      <span style="display: inline-block;" id="oldtooltipexample">Hover here.</span><span class="tooltipcontent"><b>HTML</b> tooltip example<br>Multiline</span>

      <script>
        FreshDirect.modules.common.Tooltip.init();
        new FreshDirect.modules.common.Tooltip(document.getElementById('oldtooltipexample'), {orientation: 'bottom'});
      </script>
    </div>

    <div id="FD_common_tooltipPopup" class="method">
      <h3 class="method-title">FreshDirect.components.tooltipPopup</h3>
      <p class="description">
      Basic tooltip popup.<br/>
      Opens a popup for every DOM element that has <code>[data-component="tooltip"]</code> attribute.<br/>
      The content of the popup will be the next element that has <code>[data-component="tooltipcontent"]</code> attribute.<br/>
      </p>
      <h4>Data attribute parameters</h4>
      <dl>
        <dt>data-component</dt>
        <dd>tooltip</dd>
        <dt class="optional">data-tooltipalign</dt>
        <dd>
        Alignment of the popup.<br>
        Default: tc-bc; <code>popupcontent.js</code> related alignment, <b>t</b>op/<b>c</b>enter (reference DOM element) - <b>b</b>ottom/<b>c</b>enter (popup)
        </dd>
      </dl>

      <div>
      Example:<br/>
      <button data-component="tooltip">Hover here</button><span data-component="tooltipContent" style="display: none;">Example <b>tooltip</b> content</span><br/>
      <button data-component="tooltip" data-tooltipalign="cr-cl">Hover here - custom alignment</button><span data-component="tooltipContent" style="display: none;">Example <b>tooltip</b> content on the right</span><br/>
      </div>

    </div>

    <div id="FD_common_Select" class="method">
      <h3 class="method-title">Select()</h3>
      <p class="description">
      Replace a standard <code>select</code> element to a custom one.<br/>
      Select elements w/ <code>customselect</code> CSS class will be replaced on page load, or by calling <code>FreshDirect.modules.common.Select.selectize()</code>.
      </p>

      <div>
        Example: <br/>
        <select class="customselect">
          <option>Apple</option>
          <option>Banana</option>
          <option>Fish</option>
        </select>
        <script>
        FreshDirect.modules.common.Select.selectize();
        </script>
      </div>

    </div>

    <div id="FD_common_dispatcher_signal" class="method">
      <h3 class="method-title">dispatcher.signal(to, body)</h3>
      <p class="description">
      A "signal" is the base idea of our widget system. A signal is a simple string. Through the system we can
      pass JSON data through these "signals". Widgets can listen to these signals to get the appropriate data.
      </p>
      <p class="description">
      Good to know: Name your signals according to your JSON structure. For example, if a "login" widget would get its data
      from the 'loginData' field from the JSON returned by /api/example/login, then it's good to name the signal as 'loginData'. This way an automation can easily separate the returned JSON by fields and automatically refresh your widget.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>to</dt>
        <dd>Name of the signal</dd>
        <dt class="optional">body</dt>
        <dd>Regular JSON data (or null)</dd>
      </dl>
      <pre class="prettyprint example">
         document.getElementById("signal-send-button").addEventListener('click', function(e){
           FreshDirect.common.dispatcher.signal("test", { hello: "Hello World!" }); // send a signal everytime you click the button
         });

         FreshDirect.common.dispatcher.value.onValue(function(e){ // this will be called everytime when dispatcher gets a signal
           console.log('dispatcher.signal(to, body) - Dispatcher body for test signal', e);
         });
      </pre>
      <div><button id="signal-send-button" type="button">Click Me!</button> Button for sending signal, see console for results</div>
      <script>
         document.getElementById("signal-send-button").addEventListener('click', function(e){
           FreshDirect.common.dispatcher.signal("test", { hello: "Hello World!" });
         });

         FreshDirect.common.dispatcher.value.onValue(function(e){
           console.log('dispatcher.signal(to, body) - Dispatcher body for test signal', e);
         });
      </script>
    </div>

    <div id="FD_common_signalTarget" class="method">
      <h3 class="object-title">common.signalTarget</h3>
      <p class="description">
      This is a base class for the general widget 'class'. This class can listen to a Dispatcher signal and when it notices a signal it passes the provided data to a callback.
      </p>
      <p>NOTE: Do not use this class as a base class for your visible widgets. Use <code>FreshDirect.common.widget</code> instead which is another abstraction on top of this. SignalTarget shall be used for modules that doesn't render anything, like the <code>coremetrics</code> or the <code>server</code> module.</p>
      <pre class="prettyprint example">
          var widget = Object.create(fd.common.signalTarget, {
            signal: {
              value: 'login' // listen for data under login key
            },
            callback:{
              value:function( value ) {
                console.log(value) // when a signal sent to 'login' the data arrives here
              }
            }
            ... more options here ...
          };
          widget.listen(); // needed to call this manually to actually start listening
      </pre>
      <script>
      </script>
    </div>

    <div id="FD_common_widget" class="method">
      <h3 class="object-title">common.widget</h3>
      <p class="description">
      This is a base class for your widgets. Inherit from this object to create your own widgets. We're using  Soy Templates for rendering in widgets.
      Below we show a minimal code for creating a new widget. See comments there.
      </p>
      <pre class="prettyprint example">
          var minimalWidget = Object.create(fd.modules.common.widget, {
            signal : {
              value: 'login' // listen to login signal
            },
            template: {
              value: soyTemplateHere // this is a (soy) template function.
            },
            placeholder: {
              value: 'body' // where to place the rendered template.
            },
            render:{
              value:function(data){
                // For every new data sent to 'login' signal,
                // the widget will automatically render the soy template with the new data to the placeholder

                // Only overwrite this function if you actually need to control rendering somehow
              }
            },
          }
          minimalWidget.listen(); // needed to call this manually to actually start listening to signal
      </pre>
      <script>
      </script>
      <p class="description">Here is an <strong>interactive</strong> example for a widget which will set a colour and a text every time you push the button. This example shows how we used to introduce new widgets in the system. </p>
      <p>This example could be in a separate file named to 'colorWidget.js'. Basically that's the usual overlay of a widget: widget code, event handling, module registration.</p>
      <pre class="prettyprint example">
          (function(fd){
            var WIDGET = fd.modules.common.widget;
            var $ = fd.libs.$;

            var colorWidget = Object.create(WIDGET, {
              signal : {
                value: 'colorMe'
              },
              template: {
                value: test.jsWidgetsColorMe
              },
              placeholder: {
                value: '#colorWidget_example'
              }
              // notice: we have not defined 'render' method here, still it works
            });
            colorWidget.listen(); // needed to call this manually to actually start listening to signal

            // attaching eventhandlers to document, filtering for the specified element
            $(document).on('click', '#color-signal-send-button', function(e){
              var randR = parseInt(Math.random()*255, 10),
                  randG = parseInt(Math.random()*255, 10),
                  randB = parseInt(Math.random()*255, 10);

              FreshDirect.common.dispatcher.signal("colorMe", {
                 r: randR,
                 g: randG,
                 b: randB,
                 text: "Wonderful colours we have! It's rgb(" + randR + ", " + randG + ", " + randB + ")"
              });
            });

            // this widget could be referenced through FreshDirect.testpage.widgets.colorWidget
            fd.modules.common.utils.register("testpage.widgets", "colorWidget", colorWidget, fd);
          })(FreshDirect);
      </pre>
      <button id="color-signal-send-button" class="more-space" type="button">Colorize!!</button>
      <div id="colorWidget_example"></div>
      <script>
          // this could be a separate file named to 'colorWidget.js'
          // basically that's the usual overlay of a widget: widget code, event handling, module registration
          (function(fd){
            var WIDGET = fd.modules.common.widget;
            var $ = fd.libs.$;

            var colorWidget = Object.create(WIDGET, {
              signal : {
                value: 'colorMe'
              },
              template: {
                value: test.jsWidgetsColorMe
              },
              placeholder: {
                value: '#colorWidget_example'
              }
              // notice: we have not defined 'render' method here, still it works
            });
            colorWidget.listen(); // needed to call this manually to actually start listening to signal

            // attaching eventhandlers to document, filtering for the specified element
            $(document).on('click', '#color-signal-send-button', function(e){
              var randR = parseInt(Math.random()*255, 10),
                  randG = parseInt(Math.random()*255, 10),
                  randB = parseInt(Math.random()*255, 10);

              FreshDirect.common.dispatcher.signal("colorMe", {
                 r: randR,
                 g: randG,
                 b: randB,
                 text: "Wonderful colours we have! It's rgb(" + randR + ", " + randG + ", " + randB + ")"
              });
            });

            // this widget could be referenced through FreshDirect.testpage.widgets.colorWidget
            fd.modules.common.utils.register("testpage.widgets", "colorWidget", colorWidget, fd);
          })(FreshDirect);
      </script>

    </div>

    <div id="FD_common_server" class="method">
      <h3 class="object-title">common.server</h3>
      <p class="description">
      This is a meta-widget which provides server-communication. You can use it to send data to an url and then get back this data separately
      divided by JSON keys. This means you can send a request and then you should not care about the rest. The response JSON will be splitted up and according to those keys - through the dispatcher - all widgets will be updated. Usually that's why we do not use jQuery.post() or another 3rd party utility to initiate a connection because we do not want widgets handle their own server connection. We don't want to get the data back in a callback. Instead we want to update our widgets according to the response.
      </p>
      <pre class="prettyprint example">
      (function(fd){
          var WIDGET = fd.modules.common.widget,
              $ = fd.libs.$,
              DISPATCHER = fd.common.dispatcher;

          var userDisplay = Object.create(WIDGET, {
            signal : {
              value: 'userData'
            },
            template: {
              value: test.jsWidgetsUserDisplay
            },
            placeholder: {
              value: '#userDisplayWidget'
            }
          });
          userDisplay.listen();

          var friends = Object.create(WIDGET, {
            signal : {
              value: 'friends'
            },
            template: {
              value: test.jsWidgetsFriends
            },
            placeholder: {
              value: '#friendsWidget'
            }
          });
          friends.listen();

          $(document).on('click', '#user-get-data-button', function(e){

            // sending a server signal to request JSON data
            DISPATCHER.signal('server',{
             url: 'sampleUserData.jsp', // REST url
             type: 'GET'                // could be GET/PUT/POST/HEAD/...
            });

            // whenever the response comes back, userDisplay widget will be rerendered
            // if the response contains a json key called 'userData'

            // One plus to notice is that this one request also renders the 'friends' widget
            // because this is watches for 'friends' signal and our response JSON contains this key

            // NOTICE: there is no additional code for handling the response.
            // Because we sent a signal to 'server' it is handled automatically.
          });

          $(document).on('click', '#user-mock-friend-data-button', function(e){
            // here we send data only to friends widget
            DISPATCHER.signal('friends',{
              friendList: [ { "name" : "Joe1" }, { "name" : "Joe2" }, { "name" : "Joe3" } ]
            });
          });

          $(document).on('click', '#user-clear-data-button', function(e){
            // initial state
            $('#userDisplayWidget').html("User Data Widget");
            $('#friendsWidget').html("Friends Data Widget");
          });

          fd.modules.common.utils.register("testpage.widgets", "userDisplay", userDisplay, fd);
          fd.modules.common.utils.register("testpage.widgets", "friends", friends, fd);
        })(FreshDirect);
      </pre>
      <p>In this <strong>example</strong> we show how you may use common.server to update two widgets at the same time. Get User Data! sends the request to the server. After that watch our widget-system refreshing in action. In addition we added 'Mock Friends...' button to show that you can always update widgets separately as well. Try to play with the clicking order!</p>
      <button id="user-get-data-button" class="more-space" type="button">Get User Data!</button>
      <button id="user-mock-friend-data-button" class="more-space" type="button">Mock Friends w. Another Data!</button>
      <button id="user-clear-data-button" class="more-space" type="button">Initial State!</button>
      <div class="flex-container">
        <div id="userDisplayWidget">User Data Widget</div>
        <div id="friendsWidget">Friends Data Widget</div>
      </div>
      <script>
        (function(fd){
          var WIDGET = fd.modules.common.widget,
              $ = fd.libs.$,
              DISPATCHER = fd.common.dispatcher;

          var userDisplay = Object.create(WIDGET, {
            signal : {
              value: 'userData'
            },
            template: {
              value: test.jsWidgetsUserDisplay
            },
            placeholder: {
              value: '#userDisplayWidget'
            }
          });
          userDisplay.listen();

          var friends = Object.create(WIDGET, {
            signal : {
              value: 'friends'
            },
            template: {
              value: test.jsWidgetsFriends
            },
            placeholder: {
              value: '#friendsWidget'
            }
          });
          friends.listen();

          $(document).on('click', '#user-get-data-button', function(e){

            // sending a server signal to request JSON data
            DISPATCHER.signal('server',{
             url: 'sampleUserData.jsp', // REST url
             type: 'GET'                // could be GET/PUT/POST/HEAD/...
            });

            // whenever the response comes back, userDisplay widget will be rerendered
            // if the response contains a json key called 'userData'

            // One plus to notice is that this one request also renders the 'friends' widget
            // because this is watches for 'friends' signal and our response JSON contains this key

            // NOTICE: there is no additional code for handling the response.
            // Because we sent a signal to 'server' it is handled automatically.
          });

          $(document).on('click', '#user-mock-friend-data-button', function(e){
            // here we send data only to friends widget
            DISPATCHER.signal('friends',{
              friendList: [ { "name" : "Joe1" }, { "name" : "Joe2" }, { "name" : "Joe3" } ]
            });
          });

          $(document).on('click', '#user-clear-data-button', function(e){
            // initial state
            $('#userDisplayWidget').html("User Data Widget");
            $('#friendsWidget').html("Friends Data Widget");
          });

          fd.modules.common.utils.register("testpage.widgets", "userDisplay", userDisplay, fd);
          fd.modules.common.utils.register("testpage.widgets", "friends", friends, fd);
        })(FreshDirect);
      </script>
    </div>

  </div> <!-- /module -->

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

  <script src="https://cdn.rawgit.com/google/code-prettify/master/loader/run_prettify.js"></script>
</body>
</html>
