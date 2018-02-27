/*global expressco*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  'use strict';

  var DRAWER_WIDGET = fd.modules.common.drawerWidget,
      $ = fd.libs.$;

  var address = Object.create(DRAWER_WIDGET,{
    signal:{
      value: 'address',
  	  writable: true
    },
    contentTemplate: {
      value: expressco.addresscontent
    },
    previewTemplate: {
      value: expressco.addresspreview
    },
    serialize:{
      value:function(){
        return {};
      }
    }
  });

  address.listen();

  var unattendedSelector = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'unattendedDeliveryDisplay'
    },
    callback: {
      value: function (data) {
        $(document.body).attr("unattendeddeliveryavailable", data && data.isUnattendedDelivery);
      }
    }
  });

  unattendedSelector.listen();

  // address related forms
  fd.modules.common.forms.register({
    id: "address",
    success: function () {
      if (fd.expressco.drawer) {
        fd.expressco.drawer.reset();
      }
      $("#ec-drawer").trigger("address-update");
    },
    validate: function () {
      var errors = [],
          data = fd.modules.common.forms.serialize(this.id);

      if (data.id && data.id+"_phone" in data && !data[data.id+"_phone"]) {
        errors.push({
          name: data.id+"_phone",
          error: 'Phone number is required!'
        });
      }

      return errors;
    },
    errorHandlers: {
      ebtAddressRestriction: function (form, name, error) {
        fd.expressco.restrictionpopup.open({}, 'ebt_address', null, {form: form, name: name, error: error});

        return true;
      }
    }
  });

  fd.modules.common.forms.register({
    id: "address_preview",
    validate: function () {
      var errors = [],
          data = fd.modules.common.forms.serialize(this.id);

      if (data.id && data.id+"_phone" in data && !data[data.id+"_phone"]) {
        errors.push({
          name: data.id+"_phone",
          error: 'Phone number is required!'
        });
      }

      return errors;
    }
  });

  fd.modules.common.forms.register({
    id: "addaddress_home",
    success: function () {
      if (fd.expressco.addaddresspopup) {
        fd.expressco.addaddresspopup.close();
      }
    }
  });

  fd.modules.common.forms.register({
    id: "addaddress_office",
    success: function () {
      if (fd.expressco.addaddresspopup) {
        fd.expressco.addaddresspopup.close();
      }
    }
  });

  fd.modules.common.forms.register({
    id: "address_getDeliveryAddressMethod",
    success: function (id, result) {
      if (id && result && result.address_by_id && result.address_by_id.addresses && result.address_by_id.addresses.length) {
        fd.expressco.addaddresspopup.open(null, result.address_by_id.addresses[0]);
      }
    }
  });

  fd.modules.common.utils.register('expressco', 'address', address, fd);
}(FreshDirect));
