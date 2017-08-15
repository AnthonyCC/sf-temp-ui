/*global common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var WIDGET = fd.modules.common.widget;

  var contentModules = Object.create(WIDGET,{
    signal:{
      value:'contentModules'
    },
    template:{
      value:common.contentModules
    },
    placeholder:{
      value:'#contentModules'
    },
    updateTemplate:{
      value:common.contentModuleSelector
    },
    update:{
      value:function(data){
        data.config.forEach(function (module) {
          var $ph = $('#' + module.moduleInstanceId);
          if ($ph.length) {
            $ph.after(this.updateTemplate({moduleConfig:module, data:data.data[module.moduleInstanceId]}));
            $ph.remove();
            fd.modules.common.Select.selectize($ph);
            fd.modules.common.Elements.decorate($ph);
            fd.modules.common.aria.decorate();

            fd.common.dispatcher.signal('productImpressions', $ph);
          }

        }.bind(this));

      }
    }
  });

  contentModules.listen();
  fd.modules.common.utils.register("components", "contentModules", contentModules, fd);
}(FreshDirect));
