/* global jQuery, browse,srch*/
var FreshDirect = FreshDirect || {};

(function(fd){
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var carouselType = Object.create(WIDGET,{
    signal:{
      value:'carouselType'
    },
    callback:{
        value:function(data){
            var queryParams = 'type=' + data.type;
            if (data.type==='search'){
                queryParams += '&productId=' + data.productId;
            }
            this.DISPATCHER.signal('server', {
                url: '/api/carousel?' + queryParams
            });
        }
      }
  });
 
  carouselType.listen();

  fd.modules.common.utils.register("browse", "carouselType", carouselType, fd);
}(FreshDirect));