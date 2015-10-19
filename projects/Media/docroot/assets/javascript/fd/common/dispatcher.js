/*global Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var bus = new Bacon.Bus();

  var signal = function(to,body){
    try {
      // add metadata and abFeatures to body if not present
      if (body && typeof body === 'object') {
        body.metadata = body.metadata || fd.metaData;
        body.abFeatures = body.abFeatures || fd.features && fd.features.active;
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
