var FreshDirect = FreshDirect || {};

(function (fd) {
  'use strict';

  var $ = fd.libs.$;

  var widget = Object.create(fd.common.signalTarget, {
    contentTemplate: {
      value: function(){
        return '';
      }
    },
    previewTemplate: {
      value: function(){
        return '';
      }
    },
    contentHolder: {
      value: function(){ return '[data-drawer-content="' + this.signal + '"]'; }
    },
    previewHolder: {
      value: function(){ return '[data-drawer-default-content="' + this.signal + '"]'; }
    },
    render:{
      value:function(data, placeHolderSelector, templateFn){
        var $ph =  $(placeHolderSelector);

        data = data || {};
        data.metadata = data.metadata || fd.metaData;

        if($ph.length){
          $ph.html(templateFn(data));
          fd.modules.common.Select.selectize($ph);
        }
      }
    },
    callback:{
      value:function( value ) {
        this.render(value, this.previewHolder(), this.previewTemplate);
        this.render(value, this.contentHolder(), this.contentTemplate);
      }
    }
  });

  fd.modules.common.utils.register('modules.common', 'drawerWidget', widget, fd);
}(FreshDirect));
