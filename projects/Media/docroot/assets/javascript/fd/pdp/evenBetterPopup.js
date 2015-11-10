/*global jQuery,pdp*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var evenBetterPopup = Object.create(POPUPWIDGET,{
    customClass: {
      value: ''
    },
    bodyValue:{
      value:'',
      writable:true
    },
    template:{
      value:pdp.evenBetterPopup
    },
    bodySelector:{
      value:'.evenbetter-popup-body'
    },
    bodyTemplate: {
      value: function(){
        return evenBetterPopup.bodyValue;
      }
    },
    $trigger: {
      value: null
    },
    popupId: {
      value: 'evenBetterPopup'
    },
    popupConfig: {
      value: {
      valign: 'top',
      halign: 'left',
      placeholder: true,
      stayOnClick: true,
      zIndex: 500,
        overlay:true,
        delay: 300
      }
    },
    open: {
      value: function (config) {
        var target = config.element;

        if(target.length){
          this.bodyValue = target[0].innerHTML;
          this.refreshBody({},this.bodyTemplate,pdp.evenBetterPopupHeader(config));
          this.popup.show(target);

          // make ID-s unique
          $('#'+this.popupId+' '+this.bodySelector+' [id]').each(function (i, el) {
            el.id = 'trnp_'+el.id;
          });
          $('#'+this.popupId+' '+this.bodySelector+' [for]').each(function (i, el) {
            $(el).attr('for', 'trnp_'+$(el).attr('for'));
          });
          $('#'+this.popupId+' '+this.bodySelector+' [fdform]').each(function (i, el) {
            $(el).attr('fdform', 'trnp_'+$(el).attr('fdform'));
          });
        }
      }
    }
  });

  evenBetterPopup.render();

  $(document).on('mouseover','[data-evenbetteritem-trigger]',function(event){
    var element = $(event.currentTarget).closest('[data-component="evenBetterItem"]');
    evenBetterPopup.open({
      element: element,
      productId:element.data('productId'),
      catId:element.data('catId'),
      grpId: element.data('grpId')||null,
      grpVersion: element.data('grpVersion')||null
    });
  });

  fd.modules.common.utils.register("pdp", "evenBetterPopup", evenBetterPopup, fd);
}(FreshDirect));
