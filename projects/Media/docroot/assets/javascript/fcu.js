/**
 * Utilities to handle form change events.
 *
 */
var FormChangeUtilClass = function() {
};

/**
 * Default warning message. If not specifically set, this will be used.
 */
FormChangeUtilClass.prototype.defaultWarning = "www.freshdirect.com - The changes you have made to this page will be lost!";

/**
 * Disable or enable checking for a particular form.
 * Note that the function returned by warnOnSignatureChange will reset the value
 * supplied once the event has been used up.
 * @param formId the id of the form
 * @param check boolean
 */ 
FormChangeUtilClass.prototype.checkSignature = function(formId, check) {
    var form = document.getElementById(formId);
    if (form == null) return;
    form.setAttribute("FD__check_signature",check.toString());
};


/**
 * Calculate and store the form's signature.
 *
 * The signature of the form will be recorded regardless of the value for enable.
 * The second optional argument has an effect whether warnings will be issued.
 * @param formId the id of the form
 * @param enabled whether to enable form change warnings (optional, false by default)
 */
FormChangeUtilClass.prototype.recordSignature = function(formId, enabled) {

    var form = document.getElementById(formId);
    if (form == null) return;

    if (arguments.length == 1) enabled = false;

    // calculate form signature
    form.setAttribute(
         "FD__form_signature",
         YAHOO.util.Connect.setForm(formId,false,false));

    // set whether signature checking for warnings enabled
    form.setAttribute("FD__check_signature", enabled.toString());

    // set the form's default checking policy
    form.setAttribute("FD__check_signature_default", enabled.toString());

    // set the default warning message
    form.setAttribute("FD__signature_change_warning",this.defaultWarning);
};

/**
 * Reset the warning message and the checking policy to their default values.
 *
 * @param formId the id of the form
 */
FormChangeUtilClass.prototype.resetDefaults = function(formId) {
    var form = document.getElementById(formId);
    if (form == null) return;

    var checkingDefault = form.getAttribute("FD__check_signature_default");
    if (checkingDefault == null) return;

    form.setAttribute("FD__check_signature",checkingDefault);
    form.setAttribute("FD__signature_change_warning",this.defaultWarning);
};

/**
 * Returns whether the form has changed.
 * @param formId the id of the form
 */
FormChangeUtilClass.prototype.formSignatureChanged = function(formId) {

    var form = document.getElementById(formId);
    if (form == null) return false;
 
    var signature =  form.getAttribute("FD__form_signature");
    if (signature == null) return false;
 
    return signature != YAHOO.util.Connect.setForm(formId,false,false);
};

/**
 * Set a warning message for a form.
 * This also enables form change warnings. Note also that the function returned by
 * warnOnSignatureChange will reset the message and the action of warning checking to the default
 * after the event has occured.
 *
 * @param formId id of the form
 * @message warning to use
 */
FormChangeUtilClass.prototype.setWarningMessage = function(formId,message) {
    var form = document.getElementById(formId);
    if (form == null) return false;

    form.setAttribute("FD__signature_change_warning",message);
    form.setAttribute("FD__check_signature","true");
};

/**
 * Return a function that can be attached to the window.onbeforeunload event.
 *
 * The function returned will reset signature checking and the warning message
 * to their default values, since the user may press "cancel".
 *
 * @param id of the form
 */
FormChangeUtilClass.prototype.warnOnSignatureChange = function(formId) {

    var self = this;

    return function() {
        // is there such a form?
        var form = document.getElementById(formId);
        if (form == null) return;

        // should we check at all?
        var check = form.getAttribute("FD__check_signature");
        if ("true" == check) {

            // is there a specific message to use?
            var message = form.getAttribute("FD__signature_change_warning");
            if (message == null) message = self.defaultWarning;

            // consume event
            self.resetDefaults(formId);
            if (self.formSignatureChanged(formId)) return message; 
        } else {

           // consume event
           self.resetDefaults(formId);
           return;
        }
    };
};

var FormChangeUtil = new FormChangeUtilClass();
