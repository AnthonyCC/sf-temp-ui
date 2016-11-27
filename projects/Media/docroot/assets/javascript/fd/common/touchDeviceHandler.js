/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";
  
  function distinctTouchDevices(){
    if (!("ontouchstart" in document.documentElement)) {
        document.body.className += " no-touch";
    }
  }

  distinctTouchDevices();
}(FreshDirect));
