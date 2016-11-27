/* global jQuery, fd*/
var FreshDirect = FreshDirect || {};

(function(fd){
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var pageType = Object.create(WIDGET,{
    signal:{
      value:'searchParams'
    },
    template:{
      value:browse.pageType
    },
    placeholder:{
      value:'.page-type'
    },
    serialize: {
      value: function () {
        var pageType = $(this.placeholder+' input.pagetype').val();

        return { pageType: pageType };
      }
    }
  });
 
  pageType.listen();

  fd.modules.common.utils.register("browse", "pageType", pageType, fd);
}(FreshDirect));
