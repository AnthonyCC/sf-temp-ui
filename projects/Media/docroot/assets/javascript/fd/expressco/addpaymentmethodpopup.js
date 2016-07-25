/*global expressco*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  'use strict';

  var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;

  var addpaymentmethodpopup = Object.create(POPUPWIDGET,{
    headerContent: {
      value: ''
    },
    customClass: {
      value: 'addpaymentmethodpopup'
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
      value: '[data-component="addpaymentmethodbutton"]'
    },
    bodySelector:{
      value: '.ec-popup-content'
    },
    signal: {
      value: 'editpaymentmethod' //TODO
    },
    scrollCheck: {
      value: '.ec-popup'
    },
    template: {
      value: expressco.eccenterpopup
    },
    bodyTemplate: {
      value: expressco.addpaymentmethodpopup
    },
    popupId: {
      value: 'addpaymentmethodpopup'
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
        var $t = e && $(e.currentTarget) || $(document.body),
        	tabToShow = $t.attr('data-showechecktab');
        e && e.preventDefault();

        data = data || {};
        data.metadata = data.metadata || fd.metaData || fd.expressco.data.formMetaData;

        this.refreshBody(data);
        this.popup.show($t);
        this.popup.clicked = true;
        
        if(tabToShow) {
        	$('.formcontainer').attr('data-show', tabToShow);
        }

        this.noscroll(true);

        $('#'+this.popupId+' [fdform]').each(function (i, form) {
          fd.modules.common.forms.decorateFields(form);
        });

        try {
          fd.modules.common.updateOAS(OAS_url, 'www.freshdirect.com/XCpaymentpromo', OAS_rns, ['AddPaymentPromo'], OAS_query);
        } catch (e) {
          console.trace(e);
        }
      }
    },
    selectForm: {
      value: function (e) {
        var $el = $(e.currentTarget),
            val = $el.val(),
            $parent = $el.closest('[data-show]');

        $parent.attr('data-show', val);
      }
    }
  });

  addpaymentmethodpopup.listen();
  addpaymentmethodpopup.render();

  $(document).on('click', addpaymentmethodpopup.trigger,
    addpaymentmethodpopup.open.bind(addpaymentmethodpopup));

  $(document).on('click', '#' + addpaymentmethodpopup.popupId + ' .close',
    addpaymentmethodpopup.close.bind(addpaymentmethodpopup));

  $(document).on('click', '#' + addpaymentmethodpopup.popupId + '.centerpopup-helper', function (e) {
    if ($(e.target).hasClass('centerpopup-helper')) {
      addpaymentmethodpopup.close();
    }
  });

  $(document).on('change', '#' + addpaymentmethodpopup.popupId + ' .formselector input',
    addpaymentmethodpopup.selectForm.bind(addpaymentmethodpopup));

  $(document).on('click', "[data-component='pickmethodbutton']", function(){
     fd.expressco.drawer && fd.expressco.drawer.activate("payment");
  });

  fd.modules.common.utils.register('expressco', 'addpaymentmethodpopup', addpaymentmethodpopup, fd);
}(FreshDirect));

