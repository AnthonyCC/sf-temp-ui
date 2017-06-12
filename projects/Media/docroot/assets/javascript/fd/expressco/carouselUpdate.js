/*global expressco */
var FreshDirect = FreshDirect || {};

// module initialization
(function (fd) {
  'use strict';

  var $=fd.libs.$;
  var WIDGET = fd.modules.common.widget;
  var DISPATCHER = fd.common.dispatcher;

  var carouselUpdate = Object.create(WIDGET,{
    signal: {
      value: 'carouselData'
    },
    template: {
        value: expressco.viewCartTabbedCarousel
    },
    placeholder: {
      value: '#cartCarousels'
    }
  });
  
  carouselUpdate.listen();
}(FreshDirect));