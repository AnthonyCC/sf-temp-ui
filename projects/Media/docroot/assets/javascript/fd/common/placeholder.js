var FreshDirect = FreshDirect || {};

(function(fd) {
  /**
   * Placeholder HTML5 property emulation for older browsers
   *
   * @param {Element} el The <input> element with the placeholder
   * @config {Object} Extra configuration
   * config:
   *    - message: the placeholder message to show
   *    - cssClass: CSS class when the placeholder is active
   *    - forms: The <form> elements to clear the placeholder on submit
   */
  var Placeholder = function (el, config) {
    var i;

    this.el = el;
    this.config = config || {};
    this.message = this.config.message || el.getAttribute("placeholder");
    this.cssClass = this.config.cssClass || "ph_active";
    this.forms = this.config.forms || document.getElementsByTagName("form");
    if (typeof this.forms.length === 'undefined') {
      this.forms = [this.forms];
    }

    YAHOO.util.Event.addListener(this.el, "focus", this.clear, null, this);
    YAHOO.util.Event.addListener(this.el, "blur", this.show, null, this);
    if (this.forms) {
      for (i = 0; i < this.forms.length; i++) {
        YAHOO.util.Event.addListener(this.forms[i], "submit", this.clear, null, this);
      }
    }

    this.show();
  };

  Placeholder.supported = function () {
    var i = document.createElement('input');
    return 'placeholder' in i;
  };

  Placeholder.prototype.show = function () {
    if (this.el.value === "" && this.message) {
      this.el.value = this.message;
      YAHOO.util.Dom.addClass(this.el, this.cssClass);
    }
  };

  Placeholder.prototype.clear = function (e) {
    if (this.el.value === this.message) {
      this.el.value = '';
      YAHOO.util.Dom.removeClass(this.el, this.cssClass);
    }
  };

  // register in fd namespace
  fd.modules.common.utils.register("modules.common", "Placeholder", Placeholder, fd);
})(FreshDirect);

// module initialization
// TODO setup config.forms
(function () {
  var phinputs, phi, i;

  if (!FreshDirect.modules.common.Placeholder.supported()) {
    phinputs = YAHOO.util.Dom.getElementsByClassName("placeholder", "input");
    for (i = 0; i < phinputs.length; i++) {
      phi = new FreshDirect.modules.common.Placeholder(phinputs[i]);
    }
  }
})();


