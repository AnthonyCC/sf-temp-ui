/*global expressco*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
	
	var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;
  var FORMS = fd.modules.common.forms;

  var addaddresspopup = Object.create(POPUPWIDGET,{
    headerContent: {
      value: ''
    },
    customClass: {
      value: 'addaddresspopup'
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
      value: '[data-component="addaddressbutton"]'
    },
    bodySelector:{
      value: '.ec-popup-content'
    },
    signal: {
      value: 'editaddress'
    },
    scrollCheck: {
      value: '.ec-popup'
    },
    template: {
      value: expressco.eccenterpopup
    },
    bodyTemplate: {
      value: expressco.addaddresspopup
    },
    popupId: {
      value: 'addaddresspopup'
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

        data = data || {};
        data.metadata = data.metadata || fd.expressco.data.formMetaData;
        if(typeof FreshDirect.standingorder !== "undefined" && typeof FreshDirect.standingorder.isStandingOrderContext !== "undefined"){
        	data.service_type = 'corporate';
        	data.standing_order = true;
        }

        if (e && e.preventDefault) {
          e.preventDefault();
        }

        this.refreshBody(data);
        this.noscroll(true);
        if ($(window).scrollTop() < 200) {
        	this.popup.$el.css('top', $(window).scrollTop() + 40);
        }
        this.popup.clicked = true;

        this.popup.show($t);

        $('#'+this.popupId+' [fdform]').each(function (i, form) {
          fd.modules.common.forms.decorateFields(form);
        });
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

  addaddresspopup.listen();
  addaddresspopup.render();

  $(document).on('click', addaddresspopup.trigger, addaddresspopup.open.bind(addaddresspopup));
  $(document).on('click', '#' + addaddresspopup.popupId + ' .close', addaddresspopup.close.bind(addaddresspopup));
  $(document).on('click', '#' + addaddresspopup.popupId + '.centerpopup-helper', function (e) {
    if ($(e.target).hasClass('centerpopup-helper')) {
      addaddresspopup.close();
    }
  });
  $(document).on('change', '#' + addaddresspopup.popupId + ' .formselector input', addaddresspopup.selectForm.bind(addaddresspopup));

  // phone number +/x
  $(document).on('click', '#' + addaddresspopup.popupId + ' .showaltphone', function (e) {
    var $form = $(e.currentTarget).closest('form');
    $form.attr('data-showaltphone', true);
  });
  $(document).on('click', '#' + addaddresspopup.popupId + ' .deletephonenumber', function (e) {
    var $el = $(e.currentTarget),
        $form = $el.closest('form'),
        $num1 = $form.find('input[name="phone"]'),
        $ext1 = $form.find('input[name="phone_ext"]'),
        $ext2 = $form.find('input[name="alt_phone_ext"]'),
        $num2 = $form.find('input[name="alt_phone"]');

    if ($el.attr('data-replacenumbers')) {
      $num1.val($num2.val());
      $ext1.val($ext2.val());
      FORMS.validateInputField({currentTarget: $num1});
    }

    $num2.val('');
    $ext2.val('');
    FORMS.clearFieldErrors($num2, {id: $form.attr('fdform')});
    FORMS.clearFieldErrors($ext2, {id: $form.attr('fdform')});

    $form.attr('data-showaltphone', null);
  });

  // backup delivery
  $(document).on('change', '#' + addaddresspopup.popupId + ' .backup_delivery input[name="bd_auth"]', function (e) {
    var $el = $(e.currentTarget),
        val = $el.val(),
        $parent = $el.closest('.backup_delivery');

    $parent.attr('bd-selected', val);
  });

  fd.modules.common.utils.register("expressco", "addaddresspopup", addaddresspopup, fd);

}(FreshDirect));
