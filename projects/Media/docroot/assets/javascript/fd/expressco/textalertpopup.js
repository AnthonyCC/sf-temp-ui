/*global expressco */
var FreshDirect = FreshDirect || {};

// module initialization
(function (fd) {
  'use strict';

  var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;
  var DISPATCHER = fd.common.dispatcher;

  var textalertpopup = Object.create(POPUPWIDGET,{
    headerContent: {
      value: ''
    },
    customClass: {
      value: 'textalertpopup'
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
      value: '[data-component="textalert"]'
    },
    bodySelector:{
      value: '.ec-popup-content'
    },
    signal: {
      value: 'verifyage'
    },
    scrollCheck: {
      value: '.ec-popup'
    },
    template: {
      value: expressco.eccenterpopup
    },
    bodyTemplate: {
      value: expressco.textalertpopup
    },
    popupId: {
      value: 'textalertpopup'
    },
    popupConfig: {
      value: {
        zIndex: 2000,
        openonclick: true,
        overlayExtraClass: 'centerpopupoverlay',
        align: false
      }
    },
    close: {
      value: function () {
        if (this.popup) { this.popup.hide(); }

        return false;
      }
    },
    open: {
      value: function (e, data) {
        var $t = e && $(e.currentTarget) || $(document.body);

        this.refreshBody(data);
        this.popup.show($t);
        this.popup.clicked = true;

        this.noscroll(true);
      }
    },
    displayContent: {
      value: function(e){
        var $t = e && $(e.currentTarget);
        if(!$t.length){ return; }
        $('#' + textalertpopup.popupId + ' [data-show]').attr('data-show', $t.data('go'));
      }
    }
  });

  function openAfterPageRender(){
    var ex = fd.expressco;
    if(ex && ex.data && ex.data.textMessageAlertData && ex.data.textMessageAlertData.show){
      textalertpopup.open(null, ex.data.textMessageAlertData);
    }
  }

  textalertpopup.listen();
  textalertpopup.render();

  $(document).on('click', textalertpopup.trigger, textalertpopup.open.bind(textalertpopup));
  $(document).on('click', '#' + textalertpopup.popupId + ' [fdform-button="cancel"]', textalertpopup.close.bind(textalertpopup));
  $(document).on('click', '#' + textalertpopup.popupId + ' .close', textalertpopup.close.bind(textalertpopup));
  $(document).on('click', '#' + textalertpopup.popupId + ' [data-go]', textalertpopup.displayContent.bind(textalertpopup));

  fd.modules.common.utils.register('expressco', 'textalertpopup', textalertpopup, fd);

  // form
  fd.modules.common.forms.register({
    id: "textalert",
    nothanksEndpoint: "/api/expresscheckout/textalert/cancel",
    nothanks: function (e) {
      var remindme = $(e.formEl).find('input[name="remindme"]').prop('checked');

      if (!remindme) {
        DISPATCHER.signal('server', {
          url: e.form.nothanksEndpoint,
          method: 'POST',
          data: {
            data: JSON.stringify({
              fdform: "textalert",
              formdata: {}
            })
          }
        });
      }

      textalertpopup.close();
    }
  });

  openAfterPageRender();
}(FreshDirect));
