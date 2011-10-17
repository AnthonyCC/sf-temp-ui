/*global YAHOO, escape */
var FreshDirect = FreshDirect || {};

(function (fd) {

  function removeHostName(url) {
    return url.replace(/^.*?:\/\/.*?(\/.*)$/, "$1");
  }

  var LoginBox = function (trigger, container, config) {
    this.trigger = trigger;
    this.container = container;
    this.config = config || {};
    this.form = this.container.getElementsByTagName("form")[0];

    this.centered = this.config.centered || false;

    this.overlay = YAHOO.util.Dom.get("loginbox_overlay");
    if (!this.overlay) {
      this.overlay = document.createElement("div");
      this.overlay.id = "loginbox_overlay";
      document.body.appendChild(this.overlay);
    }

    this.closeEl = YAHOO.util.Dom.getElementsByClassName("close", "span", this.container).shift();

    YAHOO.util.Event.addListener(this.trigger, "click", this.toggle, null, this);
    YAHOO.util.Event.addListener(this.overlay, "click", this.hide, null, this);
    YAHOO.util.Event.addListener(this.closeEl, "click", this.hide, null, this);
  };

  LoginBox.prototype.toggle = function (e) {
    var visible = YAHOO.util.Dom.getStyle(this.container, 'display') === 'block';

    if (visible) {
      this.hide();
    } else {
      this.show();
    }

    YAHOO.util.Event.preventDefault(e);
  };

  LoginBox.prototype.show = function () {
    var href = this.trigger.href.indexOf('://') < 0 ? this.trigger.href : removeHostName(this.trigger.href);

    this.form.action = LoginBox.LOGIN_URL + escape(href);
    YAHOO.util.Dom.addClass(this.trigger, 'active');
    this.overlay.parentNode.removeChild(this.overlay);
    this.container.parentNode.removeChild(this.container);
    document.body.appendChild(this.overlay);
    document.body.appendChild(this.container);
    this.reposition();
    YAHOO.util.Dom.setStyle(this.overlay, 'display', 'block');
    YAHOO.util.Dom.setStyle(this.container, 'display', 'block');
  };

  LoginBox.prototype.hide = function () {
    YAHOO.util.Dom.setStyle(this.container, 'display', 'none');
    YAHOO.util.Dom.setStyle(this.overlay, 'display', 'none');
    YAHOO.util.Dom.removeClass(this.trigger, 'active');
  };

  LoginBox.prototype.reposition = function () {
    var trigger_region;

    if (!this.centered) {
      trigger_region = YAHOO.util.Dom.getRegion(this.trigger);
      YAHOO.util.Dom.setStyle(this.container, 'top', trigger_region.bottom + "px");
      YAHOO.util.Dom.setStyle(this.container, 'left', trigger_region.left - 1 + "px");
      YAHOO.util.Dom.removeClass(this.container, 'centered');
      YAHOO.util.Dom.removeClass(this.overlay, 'centered');
    } else {
      YAHOO.util.Dom.setStyle(this.container, 'top', (YAHOO.util.Dom.getViewportHeight() - 200) / 2 + "px");
      YAHOO.util.Dom.setStyle(this.container, 'left', (YAHOO.util.Dom.getViewportWidth() - 240) / 2 + "px");
      YAHOO.util.Dom.addClass(this.container, 'centered');
      YAHOO.util.Dom.addClass(this.overlay, 'centered');
    }
  };

  LoginBox.LOGIN_URL = '/login/login.jsp?successPage=';

  // register in fd namespace
  fd.modules.common.utils.register("modules.header", "LoginBox", LoginBox, fd);
}(FreshDirect));

// module initialization
(function () {
  var loginbox = YAHOO.util.Dom.get("loginbox"),
      trigger = YAHOO.util.Dom.get("loginbox_trigger"),
      loggedin = YAHOO.util.Dom.get("loggedin"),
      loginbox_widget,
      loginrequired = YAHOO.util.Dom.getElementsByClassName("loginrequired", "a"),
      i;

  if (loginbox) {
    loginbox_widget = new FreshDirect.modules.header.LoginBox(trigger, loginbox);
  }

  if (!loggedin) {
    for (i = 0; i < loginrequired.length; i++) {
      trigger = loginrequired[i];
      loginbox_widget = new FreshDirect.modules.header.LoginBox(trigger, loginbox, { centered: true });
    }
  }
}());

