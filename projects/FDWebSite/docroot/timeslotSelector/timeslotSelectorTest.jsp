<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Timeslot Selector Test</title>
    <%@ include file="/common/template/includes/seo_canonical.jspf" %>
  </head>
  <body>
    <jwr:script src="/fdlibs_opt.js" useRandomParam="false" />
    <soy:import packageName="common"/>
    <h1>Timeslot Selector Test</h1>
    <jwr:style src="/newhomepage.css" media="all" />
    <jwr:style src="/global.css" media="all" />
    <jwr:script src="/fdmisc.js" useRandomParam="false" />
    <jwr:script src="/fdcommon.js" useRandomParam="false" />
    <jwr:script src="/fdcomponents.js" useRandomParam="false" />
  	<jwr:script src="/commonjavascript.js" useRandomParam="false" />
    <div class="timeslot-selector"></div>
    <script type="text/javascript">
      var DISPATCHER = FreshDirect.common.dispatcher;

      DISPATCHER.signal('server', {
        url: '/api/expresscheckout/timeslot',
        method: 'GET'
      });
      var selectedTimeslot = Object.create(FreshDirect.common.signalTarget,{
        signal: {
          value: 'selectedTimeslotId'
        },
        callback:{
          value:function( selectedTimeslotId ) {
            console.log(selectedTimeslotId);
          }
        }
      });
      selectedTimeslot.listen();
    </script>
  </body>
</html>
