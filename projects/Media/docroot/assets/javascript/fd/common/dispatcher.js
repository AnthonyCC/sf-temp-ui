/*global Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var DECORATE_BODY = true;

  var bus = new Bacon.Bus();

  var signal = function(to,body){
    try {
      // add metadata and abFeatures to body if not present
      if (DECORATE_BODY && body && typeof body === 'object') {
        body.metadata = body.metadata || fd.metaData;
        body.abFeatures = body.abFeatures || fd.features && fd.features.active;
        body.mobWeb = body.mobWeb || fd.mobWeb;
      }
      bus.push({
        to: to,
        body: body
      });
    } catch (e) {
      if (console && console.error) {
        console.error('failed to send signal: ', to, body, e.stack);
      }
    }
  };

  var dispatcher = {
    bus: bus,
    signal: signal,
    value: bus.toProperty()
  };

  fd.modules.common.utils.register("common", "dispatcher", dispatcher, fd);
}(FreshDirect));
