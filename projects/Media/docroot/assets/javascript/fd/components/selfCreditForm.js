/*global jQuery, common*/
var FreshDirect = window.FreshDirect || {};

(function (fd) {
  "use strict";

  var $=fd.libs.$,
      selfCreditForm = Object.create(fd.modules.common.forms);

    selfCreditForm.register({
    id: 'selfcreditform',
    submit: function (e) {
      fd.components.selfCreditIssueReportPopup.reviewRequest(selfCreditForm.serialize(e.form.id));
    }
  });
}(FreshDirect));
