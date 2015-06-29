/*global expressco*/
var FreshDirect = FreshDirect || {};

(function (fd) {
    "use strict";

    var $ = fd.libs.$;
    var WIDGET = fd.modules.common.widget;

    var atpFailure = Object.create(WIDGET, {
        signal: {
          value: 'atpFailure'
        },
        placeholder:{
          value:"#atpfailure"
        },
        template: {
          value: expressco.atpFailure
        },
        serialize: {
          value: function () {
            var removeIds = [];

            $(this.placeholder + " [data-component='atpremove']:checked").each(function(){
              removeIds.push($(this).attr("id"));
            });

            return removeIds;
          }
        }
    });
    atpFailure.listen();

    fd.modules.common.utils.register("expressco", "atpFailure", atpFailure, fd);
}(FreshDirect));
