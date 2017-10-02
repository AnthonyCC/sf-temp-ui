<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
  <meta charset="UTF-8">
  <title>JS Widget test page</title>
  <jwr:style src="/grid.css" media="all" />
  <jwr:style src="/global.css" media="all" />
  <link rel="stylesheet" type="text/css" href="/test/jswidgets/jswidgets.css">
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

  <div id="FD_utils" class="module" fd-toggle="toggleUtils" fd-toggle-state="enabled">
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

  </div> <!-- /module -->

  <div id="FD_common_widget_framework" class="module" fd-toggle="toggleWidgetFW" fd-toggle-state="enabled">
    <h2 class="module-title" fd-toggle-trigger="toggleWidgetFW">FreshDirect Widget Framework</h2>

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
        <dd>
        Regular JSON data (or null)<br/>
        <b>Note:</b> if this data is an object, then it will be extended with a <code>metadata</code> and an <code>abFeatures</b> field (the values of <code>FreshDirect.metaData</code> and <code>FreshDirect.features.active</code>), to enable the soy templates access for site metadata and features. In this way we can use different templates for different feature versions without writing/changing any JS code.
        </dd>
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
  </div>

  <div id="FD_common" class="module" fd-toggle="toggleCommon" fd-toggle-state="enabled">
    <h2 class="module-title" fd-toggle-trigger="toggleCommon">FreshDirect Basic Widgets</h2>

    <div id="FD_common_tooltip" class="method deprecated">
      <h3 class="method-title">Tooltip (old, deprecated, use csstooltip instead)</h3>
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

    <div id="CSS_tooltip" class="method">
      <h3 class="method-title">CSS based Tooltip</h3>
      <p class="description">
        Lightweight, A11Y aware tooltip implementation.<br>
      </p>

      <pre class="prettyprint example">
      </pre>

      <div class="example-container">
        <button class="csstooltip cssbutton green transparent icon-info-before" aria-describedby="example-tooltip1">Button w/ tooltip</button>
        <div id="example-tooltip1" class="csstooltipcontent">
          This is a tooltip.
        </div>
      </div>

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

    <div id="FD_common_loginlinks" class="method">
      <h3 class="method-title">[fd-login-required]</h3>
      <p class="description">
        Links that requires logged in user can be decorated with <code>fd-login-required</code> attribute, this will handle the login in an A11Y friendly way:<br>
        When the user clicks on them with the mouse the login popup should be opened (non-A11Y user), but if <code>ENTER</code> is pressed when they're in focus then they should work as standard links (and backend should redirect the user to the login page).
      </p>

      <script>
        FreshDirect.properties = FreshDirect.properties || {};
        FreshDirect.user = FreshDirect.user || {};
        FreshDirect.properties.isSocialLoginEnabled = true;
        FreshDirect.user.guest = true;
      </script>
      <pre class="prettyprint example">
      </pre>
      <div class="example-container">
        <a href="#FD_common_loginlinks" fd-login-required>Example login link</a>
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

    <div id="FD_components_toggle" class="method">
      <h3 class="method-title">components.toggle</h3>
      <p class="description">
      Clicking on an element with <code>[fd-toggle-trigger="<em>id</em>"]</code> will change the state (<code>[fd-toggle-state="<em>enabled</em> or <em>disabled</em>"]</code>) of the corresponding <code>[fd-toggle="<em>id</em>"]</code>.
      </p>
      <pre class="prettyprint example">
      </pre>
      <div class="example-container">
        <style scoped>
          button[fd-toggle-trigger]:after {
            content: none;
          }
          span[fd-toggle="testtoggle"] {
            display: inline-block;
            background-color: yellow;
          }
          span[fd-toggle="testtoggle"][fd-toggle-state="enabled"] {
            background-color: lightgreen;
          }
          span[fd-toggle="testtoggle"][fd-toggle-state="enabled"]:after {
           content: ' [enabled]';
          }
          span[fd-toggle="testtoggle"][fd-toggle-state="disabled"] {
            background-color: lightcyan;
          }
          span[fd-toggle="testtoggle"][fd-toggle-state="disabled"]:after {
           content: ' [disabled]';
          }
        </style>
        <button fd-toggle-trigger="testtoggle">Toggle!</button>
        <span fd-toggle="testtoggle">Demo toggle</span>
      </div>
    </div>

    <div id="FD_components_readMore" class="method">
      <h3 class="method-title">components.readMore</h3>
      <p class="description">
      Truncate the content of the HTML element that has the <code>[fd-readmore]</code> property and shows a <i>read more</i> link to show all the content.<br/>
      NOTE: the content of the element will be converted to plain text before truncating. The <i>read more</i> link will show the original HTML content.<br/>
      Call <code>FreshDirect.components.readMore.initAll()</code> to initilaize this feature on elements that are rendered after page load.
      </p>
      <h4>Data Attributes</h4>
      <dl>
        <dt class="optional">fd-readmore-open</dt>
        <dd>Callback function called when opening the truncated element.</dd>
        <dt class="optional">fd-readmore-close</dt>
        <dd>Callback function called when closing the truncated element.</dd>
        <dt class="optional">fd-readmore-truncate</dt>
        <dd>Maximum length of truncated text. [default: 200]</dd>
      </dl>
      <h4>Example</h4>
      <pre class="prettyprint example">
      </pre>
      <div class="example-container">
        <style scoped>
        [fd-readmore] {
          display: block;
          opacity: .4;
        }
        [fd-readmore][fd-readmore-state] {
          opacity: 1;
        }
        </style>
        <div fd-readmore fd-readmore-truncate="100">
        Lorem ipsum dolor sit amet, ut nec <i>doctus</i> aperiri, veniam laboramus est ea, quo ad meis facete fastidii. Est eu soleat eleifend eloquentiam, probo aliquid lobortis eam cu, <b>justo</b> integre inermis id usu. Nec solet accommodare an. Electram forensibus ullamcorper cu ius, novum sonet nam te. Cu civibus consequat per, et nam tota minimum deseruisse, legere dicunt verear ut vis. Eos at libris intellegat voluptaria.
        </div>
        <button id="initTruncate">Truncate it!</button>
        <script>
          document.getElementById('initTruncate').onclick = function () {
            FreshDirect.components.readMore.initAll();
            document.getElementById('initTruncate').remove();
          };
        </script>
      </div>
    </div>

  </div> <!-- /module -->

  <div id="FD_common_form" class="module" fd-toggle="toggleForm" fd-toggle-state="enabled">
    <h2 class="module-title" fd-toggle-trigger="toggleForm">FreshDirect Forms</h2>

    <div id="FD_common_forms" class="method">
      <h3 class="method-title">modules.common.forms (common/form.js)</h3>
      <p class="description">
      General purpose form library for AJAX form handling and FE/AJAX validation.<br/>
      The setup is mainly done by HTML attributes (starting w/ fdform).
      </p>
      <h4>HTML attributes for configuration</h4>
      <dl>
        <dt>fdform</dt>
        <dd>Id of the form (string)</dd>
        <dt class="optional">fdform-serialize</dt>
        <dd>Custom serialization function for the form (dotted name)</dd>
        <dt class="optional">fdform-fill</dt>
        <dd>Custom fill function for the form (dotted name)</dd>
        <dt class="optional">fdform-endpoint-submit</dt>
        <dd>Submit endpoint (url)</dd>
        <dt class="optional">fdform-endpoint-validator</dt>
        <dd>AJAX validation endpoint (url)</dd>
        <dt class="optional">fdform-submit</dt>
        <dd>Custom submit function (dotted name) - Note: AJAX submit has higher priority</dd>
        <dt class="optional">fdform-disable-resubmit</dt>
        <dd>Locks the form until the AJAX response received - disables submit buttons or elements defined by <code>fdform-disable-resubmit-selector</code></dd>
        <dt class="optional">fdform-disable-resubmit-selector</dt>
        <dd>Elements to disable on form submit (CSS selector)</dd>
        <dt class="optional">fdform-disable-resubmit-release</dt>
        <dd>If <i>'manual'</i> then the form won't be re-enabled automatically when the AJAX response arrives (it can be re-enabled in the success/failure handler)</dd>
        <dt class="optional">fdform-reset</dt>
        <dd>Custom reset function, called on clicking an </code>input/button type="reset"</code> (dotted name)</dd>
        <dt class="optional">fdform-success</dt>
        <dd>Callback function called if the AJAX form submit succeeded (dotted name)</dd>
        <dt class="optional">fdform-failure</dt>
        <dd>Callback function called if the AJAX form submit failed (dotted name)</dd>
        <dt class="optional">fdform-action-<ins>actionname</ins></dt>
        <dd>
        Function to call for the given action. Such actions can be issued via <code>[fdform-button="actionname"]</code> buttons. (dotted name)<br/>
        For example clicking on <code>[fdform-button="dosomething"]</code> will call the function set by <code>[fdform-action-dosomething]</code>.
        </dd>
        <dt class="optional">fdform-formatter</dt>
        <dd>Formatter function to call if the given input field is changed/focused (dotted name)</dd>
        <dt class="optional">fdform-button</dt>
        <dd>The value of the <code>fdform-button</code> property will be an action issued on the form (see <code>fdform-acion</code> above). If the same element's (button's) <code>fdform-prevent</code> attribute is set to <i>"manual"</i>, then the click event's preventDefault() method won't be called automatically.</dd>
        <dt class="optional">fdform-error-container</dt>
        <dd>The validation errors that are not associated with a field (general errors for example) are displayed in the HTML element which <code>fdform-error-container</code> property is set to the given form's id.</dd>
      </dl>
      <h4>Form object properties</h4>
      <p>
      It is possible to create and setup forms via javascript (not using the attributes above), and register it via the <code>FreshDirect.modules.common.forms.register(formObject)</code>. The form object is a plain JS object that has at least an <code>'id'</code> property, and there should be a form HTML element somewhere in the DOM that has the same id set by the <code>fdform</code> attribute.<br/>
      Custom methods (such us serialize or success) are easier to set this way.<br/>
      The properties of the registered form object has priority over the HTML attributes.
      </p>
      <dl>
        <dt>id</dt>
        <dd>Form id [fdform]</dd>
        <dt class="optional">serialize</dt>
        <dd>Custom serialize method [fdform-serialize]</dd>
        <dt class="optional">fill</dt>
        <dd>Custom fill method [fdform-fill]</dd>
        <dt class="optional">submitEndpoint</dt>
        <dd>AJAX submit endpoint [fdform-endpoint-submit]</dd>
        <dt class="optional">validatorEndpoint</dt>
        <dd>AJAX validator endpoint [fdform-endpoint-validator]</dd>
        <dt class="optional">submit</dt>
        <dd>Custom submit function [fdform-submit]</dd>
        <dt class="optional">reset</dt>
        <dd>Custom reset function [fdform-reset]</dd>
        <dt class="optional"><ins>*action*</ins> (any method)</dt>
        <dd>Form action function, called on pressing <code>fdform-button="actionname"</code> [fdform-action]</dd>
        <dt class="optional">success</dt>
        <dd>Callback function called if the AJAX form submit succeeded [fdform-success]</dd>
        <dt class="optional">failure</dt>
        <dd>Callback function called if the AJAX form submit succeeded [fdform-failure]</dd>
        <dt class="optional">validate</dt>
        <dd>Custom validation function</dd>
      </dl>

      <h4>Form library API functions</h4>
      <dl>
        <dt>forms.register(form)</dt>
        <dd>Register form object</dd>
        <dt>forms.registerValidator(selector, validator)</dt>
        <dd>Register a validator function for the input fields that match the given CSS selector</dd>
        <dt>forms.registerFormatter(name, formatter)</dt>
        <dd>Register a formatter function with the given name. The formatter is applied to fields that has the <code>[fdform-formatter="name"]</code> property set to the name of the formatter.</dd>
        <dt>forms.get(id)</dt>
        <dd>Get the form object for the id</dd>
        <dt>forms.getEl(id)</dt>
        <dd>Get the form HTML element for the id (if exists)</dd>
        <dt>forms.validate(form, silent)</dt>
        <dd>Validate the given form. If silent is true then the errors won't be displayed, but only returned.</dd>
        <dt>forms.clearErrors(form)</dt>
        <dd>Clear displayed errors for the given form</dd>
        <dt>forms.serialize(id)</dt>
        <dd>Serialize the form with the given id</dd>
        <dt>forms.fill(id, data)</dt>
        <dd>Fill the form with the provided data</dd>
      </dl>

      <h4>Validators</h4>
      <p>
      A validator is a function that gets the field to validate as a parameter, and returns the <b>list</b> of errors.<br/>
      The list of errors should have the following format:
      </p>
      <pre id="FD_forms_error_format">
      [
        {
          name: fieldName, // [name] property of the field
          error: errorMessage,
          field: HTMLField, // the field for the error, <i>optional</i>
          errorid: errorId // id of the error, to be able to remove it separately, <i>optional</i>
        },
        { ... }
      ]
      </pre>
      <p>
      Some default validators are applied automatically, like <code>[required]</code>.
      </p>

      <h4>Formatters</h4>
      <p>
      A formatter is a function that gets the input field, a boolean (if the field is focused or not), and the event object (if available).
      </p>

      <h4>AJAX request/response format</h4>
      <p>
      The request is POST-ed to the given endpoint for both submit and validation, the HTML request contains a data named property with an encoded JSON with the following fields:
      </p>
      <b>Validation JSON request data format</b>
      <pre>
        {
          "fdform": "form id",
          "formdata": { <i>serialized form data (result of <code>forms.serialize("form id")</code>)</i> },
          "edited": "changed field name"
        }
      </pre>
      <b>Submit JSON request data format</b>
      <pre>
        {
          "fdform": "form id",
          "formdata": { <i>serialized form data (result of <code>forms.serialize("form id")</code>)</i> }
        }
      </pre>
      <p>
      The response of the submit/validation process should be a standard JSON response that is processed by the dispatcher module. (See <a href="#FD_common_server">common/server</a>.)<br/>
      The form framework is listening to the <code>'submitForm'</code> and <code>'validationResult'</code> signals. Obviously a response could contain both of them, or any other data for other widgets.<br/>
      </p>
      <b>Validation JSON response data format</b>
      <pre>
        {
          "validationResult": {
            "fdform": "form id, that has been submitted for validation",
            "errors": [ ... errors in <a href="#FD_forms_error_format">standard error format</a> ]
          }
        }
      </pre>
      <b>Submit JSON response data format</b>
      <pre>
        {
          "submitForm": {
            "fdform": "form id, that has been submitted",
            "success": true/false, // was the form submit successful?
            "result": {
              ... general JSON response, processed by dispatcher, or anything that is processed by the custom success function
            }
          }
        }
      </pre>

      <h4>Example</h4>
      <b>Simple form w/ custom submit that logs the serialized form with validators and formatter</b>
      <pre class="prettyprint example">
      </pre>
      <div class="example-container">
        <form fdform="form-example-1">
          <p>
            <label>Simple field <input type="text" name="simplefield"/></label>
          </p>
          <p>
            <label>Required field <input type="text" name="reqfield" fdform-v-required/></label>
            <span fdform-error-for="reqfield"></span>
          </p>
          <p>
          Select at least one of the following options:
          </p>
          <p>
            <label>Apple <input type="checkbox" class="customcheckbox" name="fruit" value="Apple" fdform-v-onerequired="fruit"/></label>
            <label>Banana <input type="checkbox" class="customcheckbox" name="fruit" value="Banana" fdform-v-onerequired="fruit"/></label>
          </p>
          <p>
          Select only one of the following options:
          </p>
          <p>
            <label><input type="radio" class="customradio" name="fruit" value="Rasberry" fdform-v-onerequired="fruit"/>Rasberry</label>
            <label><input type="radio" class="customradio" name="fruit" value="Pear" fdform-v-onerequired="fruit"/>Pear</label>
          </p>
          <p>Select from the following options</p>
          <p>
            <select class="customsimpleselect">
          	  <option>Option1</option>
          	  <option>Option2</option>
          	  <option>Option3</option>
            </select>
          </p>
          <p>
            <label>Only '<b>A</b>' validator <input type="text" name="aaafield" /></label>
             <span fdform-error-for="aaafield"></span> 
          </p>
          <p>
            <label>Uppercase formatter <input type="text" fdform-formatter="uppercase" name="uppercasefield" /></label>
          </p>
          <button fdform-submit>Submit</button>
        </form>
        <script>
        FreshDirect.modules.common.forms.register({
          id: 'form-example-1',
          submit: function (e) {
            var data = FreshDirect.modules.common.forms.serialize(e.form.id);

            console.log(data);
          }
        });
        FreshDirect.modules.common.forms.registerValidator('[name="aaafield"]', function (field) {
          var errors = [],
              val = $(field).val();

          if (!val.match(/^[aA]*$/)) {
            errors.push({
              field: field,
              name: 'aaafield',
              error: "Only A-s please."
            });
          }

          return errors;
        });
        FreshDirect.modules.common.forms.registerFormatter('uppercase', function (field, focus) {
          var $field = $(field);

          if (!focus) {
            $field.val($field.val().toUpperCase());
          }
        });
        </script>
      </div>

      <p>
      <b>Simple AJAX submit/validation</b>
      </p>
      <pre class="prettyprint example">
      </pre>
      <div class="example-container">
        <form fdform="form-example-2" fdform-endpoint-submit="form_submit.jsp" fdform-endpoint-validator="form_validate.jsp">
          <p>
            <label>Simple field <input type="text" name="simplefield"/></label>
          </p>
          <p>
            <label>AJAX invalid field (always invalid)<input type="text" name="invalidfield"/></label>
          </p>
          <button fdform-submit>AJAX Submit</button>
        </form>
        <script>
        </script>
      </div>

    </div>

  </div> <!-- /module -->

  <div id="FD_common_popup" class="module" fd-toggle="togglePopup" fd-toggle-state="enabled">
    <h2 class="module-title" fd-toggle-trigger="togglePopup">FreshDirect Popups</h2>
    <p>incl. popups from <code>FreshDirect.modules.common.* and FreshDirect.components.*</code></p>

    <div id="FD_common_popupWidget" class="method">
      <h3 class="object-title">modules.common.popupWidget (common/popup.js)</h3>
      <p class="description">
      This is a common base for creating popups in the system. Instead of describing every property from the code we'll show basic examples for common use. So the goal is to describe how to create a new popup based on this object and give an overview of the further capabilities.
      </p>
      <p class="description">This <strong>example</strong> will create a basic popup with a simple string body template, showing the minimal code to initialize a popup based on popupWidget. By default the popup will align itself to the trigger element (the button) and will be rendered to the body html tag. After Open the popup will close when you click outside of it. Of course all these behaviours are configurable.</p>
      <pre class="prettyprint example">
      (function (fd) {
        "use strict";

        var $=fd.libs.$;
        var POPUPWIDGET = fd.modules.common.popupWidget;

        var basicPopup = Object.create(POPUPWIDGET,{
          bodyTemplate: {
            value: function(){ return "&lt;p&gt;Hello Popup World&lt;/p&gt;"; } // this should be a soy template
          },
          trigger: {
            value: '[data-basic-popup]' // we will open the popup if the element we click on has this trigger attribute
          },
          popupId: {
            value: 'basicPopup' // this popup will be placed into 'body' by default with this id
          }
        });

        basicPopup.render();

        // have to attach widget's open method to the trigger to show the popup upon click
        $(document).on('click', basicPopup.trigger, basicPopup.open.bind(basicPopup));

        fd.modules.common.utils.register("testpage.widgets", "basicPopup", basicPopup, fd);
      }(FreshDirect));
      </pre>
      <button class="cssbutton large green" data-basic-popup data-popup data-config-popupConfig-align="bc-bc">Open Popup</button>
      <script>
      (function (fd) {
        "use strict";

        var $=fd.libs.$;
        var POPUPWIDGET = fd.modules.common.popupWidget;
        var basicPopup = Object.create(POPUPWIDGET,{
          bodyTemplate: {
            value: function(){ return "<p>Hello Popup World</p>"; }
          },
          trigger: {
            value: '[data-basic-popup]'
          },
          popupId: {
            value: 'basicPopup'
          },
          open:{
            value: function (e) {
              var $t = e && $(e.currentTarget) || $(document.body);
              var alignment = $t.attr('data-align');

              this.refreshBody();
              if(alignment){
                this.popup.show($t, alignment === "false" ? false : alignment);
              }
              else{
                this.popup.show($t);
              }
              this.popup.clicked = true;
            }
          }
        });

        basicPopup.render();

        $(document).on('click', basicPopup.trigger, basicPopup.open.bind(basicPopup));

        fd.modules.common.utils.register("testpage.widgets", "basicPopup", basicPopup, fd);
      }(FreshDirect));
      </script>
      <p class="description">You can change this alignment by controlling 'popupConfig' attribute.</p>
      <pre class="prettyprint example">
          var basicPopup = Object.create(POPUPWIDGET,{
            ... ,
            popupConfig:{
              value:{
                align: "tc-bc"
              }
            }
            ...
          });
      </pre>
      <p><strong>tc-bc</strong> means: trigger element's Top Center (tc) will be aligned with popup's Bottom Center (bc)</p>
      <p>In "tc" first letter can vary according to vertical axis: Top (t), Center (c), Bottom (b), second letter is the horizontal axis: Left (l), Center (c), Right (r)</p>
      <button class="cssbutton large green" data-align-popup data-align="tc-bc">Popup tc-bc</button>
      <button class="cssbutton large green" data-align-popup data-align="cr-cl">Popup cr-cl</button>
      <script>
      (function (fd) {
        "use strict";

        var $=fd.libs.$;
        var POPUPWIDGET = fd.modules.common.popupWidget;
        var alignPopup = Object.create(POPUPWIDGET,{
          bodyTemplate: {
            value: function(){ return "<p>Hello Popup World</p>"; }
          },
          trigger: {
            value: '[data-align-popup]'
          },
          popupId: {
            value: 'alignPopup'
          },
          open:{
            value: function (e) {
              var $t = e && $(e.currentTarget) || $(document.body);
              var alignment = $t.attr('data-align');

              if(alignment){
                this.refreshBody();
                this.popup.show($t, alignment === "false" ? false : alignment);
                this.popup.clicked = true;
              }
            }
          }
        });

        alignPopup.render();

        $(document).on('click', alignPopup.trigger, alignPopup.open.bind(alignPopup));

        fd.modules.common.utils.register("testpage.widgets", "alignPopup", alignPopup, fd);
      }(FreshDirect));
      </script>
      <p>Another usual use-case for popups is to create a centered popup with or without an overlay.</p>
      <pre class="prettyprint example">
      (function (fd) {
        "use strict";

        var $=fd.libs.$;
        var centeredPopup = Object.create(fd.modules.common.popupWidget, {
          template: {
            value: common.fixedPopup // creating popup based on fixedpopup
          },
          bodyTemplate: {
            value: ... // provide template function for rendering popup body
          },
          trigger: {
            value: '[data-centered-popup]'
          },
          popupId: {
            value: 'centeredPopup'
          },
          popupConfig: {
            value: {
              align:false // Important: disable automatic alignment!
            }
          },
          hasClose: {
              value: true // This will show a little X in the top right corner
          }
        });

        centeredPopup.render();

        $(document).on('click', centeredPopup.trigger, centeredPopup.open.bind(centeredPopup));

        fd.modules.common.utils.register("testpage.widgets", "centeredPopup", centeredPopup, fd);
      }(FreshDirect));
      </pre>
      <button class="cssbutton large green" data-centered-popup>Centered Popup</button>
      <style type="text/css">
        .my-white-popup-overlay{
          background-color: #fff;
          opacity: .7;
        }
      </style>
      <script>
      (function (fd) {
        "use strict";

        var $=fd.libs.$;
        var centeredPopup = Object.create(fd.modules.common.popupWidget, {
          template: {
            value: common.fixedPopup
          },
          bodyTemplate: {
            value: function(){ return "<strong>Centering Popup</strong><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec varius nibh id ornare pellentesque. Nullam hendrerit tempus elit et interdum. Etiam leo velit, dapibus ac pharetra eu, lobortis ut augue. Aenean tincidunt ligula ligula, id fermentum enim tempus vel. Aliquam erat volutpat. Suspendisse sit amet arcu tincidunt, convallis ex non, feugiat velit. Sed a odio eget enim sollicitudin pretium quis ut eros. Maecenas ut velit pharetra, pellentesque ex in, laoreet erat. Vestibulum in eros felis. Morbi tempor lobortis est vel vestibulum. In id erat eu ex varius sollicitudin.</p>"; }
          },
          trigger: {
            value: '[data-centered-popup]'
          },
          popupId: {
            value: 'centeredPopup'
          },
          popupConfig: {
            value: {
              align:false
            }
          },
          hasClose: {
              value: true
          }
        });

        centeredPopup.render();

        $(document).on('click', centeredPopup.trigger, centeredPopup.open.bind(centeredPopup));

        fd.modules.common.utils.register("testpage.widgets", "centeredPopup", centeredPopup, fd);
      }(FreshDirect));
      </script>
      <p>If you want to create a modal-style popup which hides the content behind the popup you have to provide an extra CSS class with the colour of the overlay like:</p>
      <pre class="prettyprint example lang-css">
        .my-white-popup-overlay{
          background-color: #fff;
          opacity: .7;
        }
      </pre>
      <p>Tell the popup framework to use this class in your 'popupConfig'. Also you can tell that you want to close the popup when clicking anywhere outside of it.</p>
      <pre class="prettyprint example">
          var centeredPopup = Object.create(... ,{
            ... ,
            popupConfig:{
              value:{
                overlay:true, // this is the default value but it's encouraged to set it explicitly
                overlayExtraClass:'my-white-popup-overlay', // name of your css class
                hideOnOverlayClick: true // when clicking out of the popup it will close
              }
            }
            ...
          });
      </pre>
      <button class="cssbutton large green" data-centered-overlay-popup>Centered Overlay Popup</button>
      <script>
      (function (fd) {
        "use strict";

        var $=fd.libs.$;
        var centeredOverlayPopup = Object.create(fd.modules.common.popupWidget, {
          template: {
            value: common.fixedPopup
          },
          bodyTemplate: {
            value: function(){ return "<strong>Centering Popup</strong><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec varius nibh id ornare pellentesque. Nullam hendrerit tempus elit et interdum. Etiam leo velit, dapibus ac pharetra eu, lobortis ut augue. Aenean tincidunt ligula ligula, id fermentum enim tempus vel. Aliquam erat volutpat. Suspendisse sit amet arcu tincidunt, convallis ex non, feugiat velit. Sed a odio eget enim sollicitudin pretium quis ut eros. Maecenas ut velit pharetra, pellentesque ex in, laoreet erat. Vestibulum in eros felis. Morbi tempor lobortis est vel vestibulum. In id erat eu ex varius sollicitudin.</p>"; }
          },
          trigger: {
            value: '[data-centered-overlay-popup]'
          },
          popupId: {
            value: 'centeredOverlayPopup'
          },
          popupConfig: {
            value: {
              align:false,
              overlay:true,
              overlayExtraClass:'my-white-popup-overlay',
              hideOnOverlayClick:true
            }
          },
          hasClose: {
              value: true
          }
        });

        centeredOverlayPopup.render();

        $(document).on('click', centeredOverlayPopup.trigger, centeredOverlayPopup.open.bind(centeredOverlayPopup));

        fd.modules.common.utils.register("testpage.widgets", "centeredOverlayPopup", centeredOverlayPopup, fd);
      }(FreshDirect));
      </script>
    </div>

    <div id="FD_components_ifrPopup" class="method">
      <h3 class="object-title">Iframe (ifr) Popup</h3>
      <p class="description">
      With this widget you can easily open a popup which has iframe content. For example a link or a pdf page. It's <strong>discouraged to inherit</strong> from this object, rather use the predefined data-attributes on a DOM element shown below.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>data-ifrpopup</dt>
        <dd>URL to the iframe content (jsp,pdf,etc.)</dd>
        <dt class="optional">data-ifrpopup-width</dt>
        <dd>Specify the width for the popup if required</dd>
        <dt class="optional">data-ifrpopup-height</dt>
        <dd>Specify the height for the popup if required</dd>
      </dl>

      <pre class="example prettyprint">
      </pre>
      <div class="example-container">
        <button class="cssbutton large green" data-ifrpopup="/help/estimated_price.jsp" data-ifrpopup-width="500" data-ifrpopup-height="400">
          Open Iframe Conent
        </button>
      </div>
    </div>

    <div id="FD_components_confirmpopup" class="method">
      <h3 class="object-title">Confirm Popup</h3>
      <p class="description">
      This widget is used to get confirmation from a user for a specified action (e.g. delete/delete all action).
      This is also a popup widget that is <strong>discouraged</strong> to inherit from but you can set your configuration through data attributes.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>data-confirm</dt>
        <dd>Attribute to set on element to open a confirmation popup</dd>
        <dt class="optional">data-confirm-message</dt>
        <dd>Optionally set a simple string message you want to ask</dd>
        <dt class="optional">data-confirm-header</dt>
        <dd>Optionally set a bold title for the question</dd>
        <dt class="optional">data-confirm-button-*</dt>
        <dd>You can attach eventhandlers to buttons. Will be explained in further examples. Basic template buttons are 'yes' and 'no'.</dd>
        <dt class="optional">data-confirm-template</dt>
        <dd>Override basic template with your custom one</dd>
        <dt class="optional">data-confirm-process</dt>
        <dd>Preprocess data sent to the popup. Here you can add a fully-qualified string path for your process function</dd>
        <dt class="optional">data-confirm-data</dt>
        <dd>Override data sent to the popup</dd>
      </dl>
      <p class="description">
      The most basic usage is to asking a question (maybe with a title) and attaching custom eventhandlers to the default 'yes' and 'no' buttons. If you want to use different buttons with specified color/name see next example.
      </p>
      <pre class="prettyprint">
        (function (fd) {
          "use strict";

          var confirmAnswers = {
            yesAnswer : function(){
              alert("Meaning of life is 42");
            },
            noAnswer : function(){
              alert("Oww I'm sorry to hear that!");
            }
          };

          fd.modules.common.utils.register("testpage.widgets", "confirmAnswers", confirmAnswers, fd);
        }(FreshDirect));

        &lt;button class="cssbutton large green"
          data-confirm="true" // init confirm popup
          data-confirm-message="What's the meaning of life?"
          data-confirm-header="This will be a tricky question! :)"
          data-confirm-button-yes="FreshDirect.testpage.widgets.confirmAnswers.yesAnswer" // attach our functions registered before
          data-confirm-button-no="FreshDirect.testpage.widgets.confirmAnswers.noAnswer" // aftere these ran there is an automatic 'close' event called
          data-alignpopup="tr-bl"&gt; // set a custom alignment for this popup (top-right, bottom-left)
          Open Confirmation
        &lt;/button&gt;
      </pre>
      <div>
        <button class="cssbutton large green"
          data-confirm="true"
          data-confirm-message="What's the meaning of life?"
          data-confirm-header="This will be a tricky question! :)"
          data-confirm-button-yes="FreshDirect.testpage.widgets.confirmAnswers.yesAnswer"
          data-confirm-button-no="FreshDirect.testpage.widgets.confirmAnswers.noAnswer"
          data-alignpopup="tr-bl">
          Open Confirmation
        </button>
        <button class="cssbutton large green"
          data-confirm="true"
          data-confirm-message="What's the meaning of life?"
          data-confirm-header="This will be a tricky question! :)"
          data-confirm-button-yes="FreshDirect.testpage.widgets.confirmAnswers.yesAnswer"
          data-confirm-button-no="FreshDirect.testpage.widgets.confirmAnswers.noAnswer"
          data-alignpopup="tc-bc">
          Open Confirmation Bubble
        </button>
        <p>In the latter case you could see that an arrow appear between the button and the popup. That's called <code>.bubblepopup</code> in code. It's configured by the <code>data-alignpoup</code> attribute and it does not work for all combinations! For example for <code>tr-bl</code> it's not working but for <code>tc-bc</code> it is. See <strong>fixedpopup.css</strong> to find out which ways are supported.</p>
      </div>
      <script>
      (function (fd) {
        "use strict";

        var confirmAnswers = {
          yesAnswer : function(){
            alert("Meaning of life is 42");
          },
          noAnswer : function(){
            alert("Oww I'm sorry to hear that!");
          }
        };

        fd.modules.common.utils.register("testpage.widgets", "confirmAnswers", confirmAnswers, fd);
      }(FreshDirect));
      </script>
      <!-- TODO: continue examples -->
    </div> <!-- /component -->

    <div id="FD_components_ajaxPopup" class="method">
      <h3 class="object-title">AJAX Popup</h3>
      <p class="description">
      Goal is to show a popup with some AJAX content in it. Configurable through data-attributes on a trigger DOM element.
      </p>
      <h4>Parameters</h4>
      <dl>
        <dt>data-component="ajaxpopup"</dt>
        <dd>Set precisely this on the trigger element to show an AJAX popup</dd>
        <dt>href</dt>
        <dd>URI for AJAX endpoint providing data through GET method</dd>
        <dt class="optional">data-ajaxpopup-type</dt>
        <dd>This attribute will be set on the popup element. Can be used for applying separate styles.</dd>
        <dt>data-ajaxpopup-template</dt>
        <dd>Soy template function or a discovarble fully-qualified path to a function providing a template</dd>
        <dt class="optional">data-ajaxpopup-after-render-callback</dt>
        <dd>Get control over content after rendering with this function</dd>
      </dl>
      <pre class="prettyprint example">
        &lt;style type="text/css"&gt;
          [data-ajaxpopup-type="show-user"] .fixedPopupContent{
            background-color: floralwhite;
            border: 5px dashed cornflowerblue;
          }
        &lt;/style&gt;
        &lt;button class="cssbutton large green"
          href="sampleUserData.jsp"  // grab the data from here
          data-component="ajaxpopup"  // init an AJAX popup
          data-ajaxpopup-template="test.jsWidgetsAjaxPopupExample1"
          data-ajaxpopup-type="show-user"&gt;  // little CSS funky
          Open AJAX Content
        &lt;/button&gt;
      </pre>
      <style type="text/css">
        [data-ajaxpopup-type="show-user"] .fixedPopupContent{
          background-color: floralwhite;
          border: 5px dashed cornflowerblue;
        }
      </style>
      <button class="cssbutton large green"
        href="sampleUserData.jsp"
        data-component="ajaxpopup"
        data-ajaxpopup-template="test.jsWidgetsAjaxPopupExample1"
        data-ajaxpopup-type="show-user">
        Open AJAX Content
      </button>
    </div>

  </div> <!-- /module -->

  <div id="FD_buttons" class="module">
    <h2 class="module-title">FreshDirect CSS Buttons</h2>
    <p>These button colors/sizes/icons recommended to use sitewide. Linking an <a href="/test/components/cssbuttons.jsp">already existing testpage to show these</a>.</p>
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
      <div class="example-container">
        <script>
        </script>
      </div>
    </div>
--%>

  <script>
    (function (fd) {
      var $ = fd.libs.$;

      $('pre.example').each(function (i, el) {
        var $el = $(el),
            $next = $el.next('.example-container');

        if ($next.size() > 0) {
          $el.text($next.html());
        }
      });

      var goToHashAndCloseModules = function(){
        if (window.location.hash) {
          var elementId = window.location.hash.substr(1);
          var moduleId = $("#" + elementId).closest('[fd-toggle]').attr('id');

          if(moduleId){
            $('[fd-toggle]').not('#' + moduleId).attr('fd-toggle-state', 'disabled');
            return;
          }
        }

        // close all modules
        $('[fd-toggle]').attr('fd-toggle-state', 'disabled');
      };

      goToHashAndCloseModules();
    }(FreshDirect));
  </script>
  <script src="https://cdn.rawgit.com/google/code-prettify/master/loader/run_prettify.js"></script>
</body>
</html>
