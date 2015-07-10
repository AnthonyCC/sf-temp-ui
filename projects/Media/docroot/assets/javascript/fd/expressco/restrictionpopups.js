/*global expressco */
var FreshDirect = FreshDirect || {};

// module initialization
(function (fd) {
  'use strict';

  var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;
  var DATA = fd.expressco.data;
  var DISPATCHER = fd.common.dispatcher;
  var WIDGET = fd.modules.common.widget;

  var restrictionpopup = Object.create(POPUPWIDGET,{
    headerContent: {
      value: ''
    },
    customClass: {
      value: 'restrictionpopup'
    },
    hideHelp: {
      value: true
    },
    hasClose: {
      value: false
    },
    $trigger: {
      value: null // TODO
    },
    trigger: {
      value: '' // TODO
    },
    bodySelector:{
      value: '.ec-popup-content'
    },
    signal: {
      value: ''
    },
    scrollCheck: {
      value: '.ec-popup'
    },
    template: {
      value: expressco.eccenterpopup
    },
    bodyTemplate: {
      value: expressco.restrictionpopup
    },
    popupId: {
      value: 'restrictionpopup'
    },
    popupConfig: {
      value: {
        zIndex: 2000,
        openonclick: true,
        overlayExtraClass: 'centerpopupoverlay',
        align: false
      }
    },
    open: {
      value: function (e, popupType, popupMedia, extraData) {
        var $t = e && $(e.currentTarget) || $(document.body);

        var type = popupType && popupType.toLowerCase();
        var media = popupMedia || '';

        if(type){
          this.refreshBody({ 'type': type, 'learnMoreMedia': media, 'data': extraData });
        }

        this.popup.show($t);
        this.popup.clicked = true;

        this.noscroll(true);
      }
    },
    displayContent: {
      value: function(e){
        var $t = e && $(e.currentTarget);
        if(!$t.length){ return; }
        $('#' + restrictionpopup.popupId + ' [data-show]').attr('data-show', $t.data('go'));
      }
    }
  });

  restrictionpopup.listen();
  restrictionpopup.render();

  fd.modules.common.forms.register({
    id: 'restriction_outside',
    changemy: function () {
      restrictionpopup && restrictionpopup.close();
      if (fd.expressco.drawer) {
        fd.expressco.drawer.activate('address');
      }
    },
    success: function () {
      restrictionpopup && restrictionpopup.close();
    }
  });

  fd.modules.common.forms.register({
    id: 'restriction_pickup',
    changemy: function () {
      restrictionpopup && restrictionpopup.close();
      if (fd.expressco.drawer) {
        fd.expressco.drawer.activate('address');
      }
    },
    success: function () {
      restrictionpopup && restrictionpopup.close();
    }
  });

  fd.modules.common.forms.register({
    id: 'restriction_address',
    changemy: function () {
      restrictionpopup && restrictionpopup.close();
      if (fd.expressco.drawer) {
        fd.expressco.drawer.activate('address');
      }
    },
    success: function () {
      restrictionpopup && restrictionpopup.close();
    }
  });

  fd.modules.common.forms.register({
    id: 'restriction_timeslot',
    changemy: function () {
      restrictionpopup && restrictionpopup.close();
      if (fd.expressco.drawer) {
        fd.expressco.drawer.activate('timeslot');
      }
    },
    success: function () {
      restrictionpopup && restrictionpopup.close();
    }
  });

  fd.modules.common.forms.register({
    id: 'restriction_ebt',
    changemy: function () {
      restrictionpopup && restrictionpopup.close();
      if (fd.expressco.drawer) {
        fd.expressco.drawer.activate('payment');
      }
    },
    success: function () {
      restrictionpopup && restrictionpopup.close();
    }
  });

  fd.modules.common.forms.register({
    id: 'restriction_ageverification',
    success: function () {
      if (fd.expressco.restrictionpopup) {
        fd.expressco.restrictionpopup.close();
      }
    },
    removeandproceed: function(){
      DISPATCHER.signal('server', {
        url: '/api/expresscheckout/restriction',
        method: 'POST',
        data: {
          data: JSON.stringify({
            fdform: 'restriction_ageverification',
            formdata: {
              action: 'removeAlcohol'
            }
          })
        }
      });
    }
  });

  var popupOpener = Object.create(WIDGET,{
    signal: {
      value: 'restriction'
    },
    render: {
      value: function(data){
        if(restrictionpopup && data){
          restrictionpopup.open(null, data.type, data.media, data.data);
        }
      }
    }
  });
  popupOpener.listen();


  $(document).ready(function(){
    if(DATA && DATA.restriction){
      DISPATCHER.signal('restriction', DATA.restriction);
    }
  });
  $(document).on('click', '#' + restrictionpopup.popupId + ' [data-go]', restrictionpopup.displayContent.bind(restrictionpopup));

  fd.modules.common.utils.register('expressco', 'restrictionpopup', restrictionpopup, fd);
}(FreshDirect));
