/*global expressco*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  'use strict';

  var $ = fd.libs.$;
  var DRAWER_WIDGET = fd.modules.common.drawerWidget;

  var paymentMethod = Object.create(DRAWER_WIDGET,{
    signal:{
      value:'payment'
    },
    contentTemplate: {
      value: expressco.paymentmethodcontent
    },
    previewTemplate: {
      value: expressco.paymentmethodpreview
    },
    serialize:{
      value:function(){
        return {};
      }
    }
  });

  paymentMethod.listen();

  // $(document).on('click', paymentMethod.previewHolder(), paymentMethod.previewClick.bind(paymentMethod));

  // payment related forms
  fd.modules.common.forms.register({
    id: "CC",
    success: function () {
      if (fd.expressco.addpaymentmethodpopup) {
        fd.expressco.addpaymentmethodpopup.close();
      }
    }
  });
  fd.modules.common.forms.register({
    id: "EC",
    success: function () {
      if (fd.expressco.addpaymentmethodpopup) {
        fd.expressco.addpaymentmethodpopup.close();
      }
    }
  });
  fd.modules.common.forms.register({
    id: "ET",
    success: function () {
      if (fd.expressco.addpaymentmethodpopup) {
        fd.expressco.addpaymentmethodpopup.close();
      }
    }
  });
  fd.modules.common.forms.register({
    id: "payment",
    success: function (id) {
      if (id && fd.expressco.drawer) {
        fd.expressco.drawer.reset();
      }
    }
  });
  fd.modules.common.forms.register({
    id: "payment_loadPaymentMethod",
    success: function (id, result) {
      if (id && result && result.paymentEditValue) {
        fd.expressco.addpaymentmethodpopup.open(null, result.paymentEditValue);
      }
    }
  });

  fd.modules.common.utils.register('expressco', 'paymentmethod', paymentMethod, fd);
}(FreshDirect));
