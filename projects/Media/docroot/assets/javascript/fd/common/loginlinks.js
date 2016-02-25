var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";
  var $ = fd.libs.$;

  fd.properties = fd.properties || {};
  fd.user = fd.user || {};

  var loginSignupPopup = function (target, popupUrl) {
    if (fd.components && fd.components.ifrPopup) {
      fd.components.ifrPopup.open({ url: popupUrl + '?successPage=' + target, height: 590, width: 560, opacity: .5});
    }
  };

  var socialSignup = function (target) {
    loginSignupPopup(target, '/social/signup_lite.jsp');
  };

  var lightSignup = function (target) {
    loginSignupPopup(target, '/registration/signup_lite.jsp');
  };

  var socialLogin = function (target) {
    loginSignupPopup(target, '/social/login.jsp');
  };

  var showLoginDialog = function (target, e) {
    if (window.showLoginDialog) {
      if (e) { e.preventDefault(); }
      window.showLoginDialog(target, false);
    }
  };

  var init = function () {
    // signup handler
    $(document).on('click', '[data-component="signup"]', function (e) {
      var isMouseEvent = e.x || e.clientX || e.y || e.clientY;

      if (isMouseEvent && fd.properties && fd.properties.isLightSignupEnabled) {
        e.preventDefault();
        if (fd.properties.isSocialLoginEnabled) {
          socialSignup();
        } else {
          lightSignup();
        }
      }
    });

    // login required links
    $(document).on('click', '[fd-login-required]', function (e) {
      var isMouseEvent = e.x || e.clientX || e.y || e.clientY,
          ct = e.currentTarget,
          currentPage = window.location.pathname + window.location.search + window.location.hash,
          target = ct.hasAttribute('fd-login-successpage-current') && currentPage || ct.getAttribute('fd-login-successpage') || ct.pathname || ct.href || currentPage;

      if (isMouseEvent && fd.user && (fd.user.guest || fd.user.recognized)) {
        if (fd.properties.isSocialLoginEnabled) {
          e.preventDefault();
          if (fd.user.guest && !ct.hasAttribute('fd-login-nosignup')) {
            socialSignup(target);
          } else {
            socialLogin(target);
          }
        } else {
          showLoginDialog(target);
        }
      }
    });
  };

  var login = {
    socialSignup: socialSignup,
    lightSignup: lightSignup,
    socialLogin: socialLogin,
    initModule: init
  };

  fd.utils.registerModule("modules.common", "login", login, fd);
  fd.utils.initModule("modules.common.login", fd);
}(FreshDirect));
