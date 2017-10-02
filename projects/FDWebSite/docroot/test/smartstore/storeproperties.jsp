<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>
  <meta charset="UTF-8">
  <title>FD Store Properties Live!</title>
  <jwr:style src="/grid.css" media="all" />
  <jwr:style src="/global.css" media="all" />
  <jwr:script src="/fdlibs.js"  useRandomParam="false" />
  <link rel="stylesheet" type="text/css" href="storeproperties.css">
</head>
<body>
  <soy:import packageName="common"/>
  <soy:import packageName="test"/>

  <jwr:script src="/fdmodules.js"  useRandomParam="false" />
  <jwr:script src="/fdcomponents.js"  useRandomParam="false" />
  <jwr:script src="/fdcommon.js"  useRandomParam="false" />

  <h1>FD Store Properties</h1>
  <p>Showing live property settings from your computer</p>

  <p class='actions'>
    <input name="searchInput" type="text" placeholder="Search for property name or value" />
    <span id="reset-action" class="actions-reset cssbutton white transparent icon-cancel-circle-before notext"></span>
    <span id="refresh-action" class="actions-refresh cssbutton red icon-reorder-icon-after">Force Refresh</span>
  </p>
  <section id="storeprop"></section>

  <script>
    (function (fd) {
      var $ = fd.libs.$;
      var WIDGET = fd.modules.common.widget;
      var DISPATCHER = fd.common.dispatcher;
      var PROPERTIES_API_URI = "/api/dev/properties/store";

      var storeProperties = Object.create(WIDGET, {
        signal : {
          value: 'storeProperties'
        },
        template: {
          value: test.storeProperties
        },
        placeholder: {
          value: '#storeprop'
        },
        inputSelector: {
          value: 'input[name="searchInput"]'
        },
        callback: {
          value: function(data){
            storeProperties.render({ propertyList: data });
            var searchTerm = $(storeProperties.inputSelector).val();
            searchTerm && storeProperties.filterPropertiesBy(searchTerm);
          }
        },
        filterPropertiesBy: {
          value: function(searchTerm){
            if(searchTerm){
              console.log('youve typed in ', searchTerm);
              var searchEx = new RegExp(searchTerm, 'i');
              $(storeProperties.placeholder + ' [data-prop-name]').each(function(el){
                var propName = $(this).text();
                console.log(propName);
                if(!propName.match(searchEx)){
                  $(this).hide();
                }
              });
            }
          }
        }
      });
      storeProperties.listen();

      DISPATCHER.signal('server',{ url: PROPERTIES_API_URI, type: 'GET' });

      $(document).on('change', storeProperties.inputSelector, function(e){
        $('[data-prop-name]').show();
        storeProperties.filterPropertiesBy($(this).prop('value'));
      });

      $(document).on('click', '#reset-action', function(e){
        $(storeProperties.inputSelector).val('');
        $('[data-prop-name]').show();
      });

      $(document).on('click', '#refresh-action', function(e){
        DISPATCHER.signal('server', { url: PROPERTIES_API_URI, method: 'POST' });
      });

    }(FreshDirect));
  </script>
</body>
</html>
