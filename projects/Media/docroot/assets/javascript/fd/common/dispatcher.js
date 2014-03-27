/*global jQuery,Bacon*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  var bus = new Bacon.Bus();

  var signal = function(to,body){
    try {
      bus.push({
        to:to,
        body:body
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
