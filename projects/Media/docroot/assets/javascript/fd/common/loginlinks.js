var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";
  var $ = fd.libs.$;
  var loginAjaxOverlayClassName = 'login-ajax-overlay';
  
  fd.properties = fd.properties || {};
  fd.user = fd.user || {};

  var loginSignupPopup = function (target, popupUrl) {
    if (fd.mobWeb) { /* send user to page instead of popup */
      var API = $("#nav-menu").data("mmenu");
      window.top.location = '/social/signup_lite'+((fd.mobWeb)?'_mobile':'')+'.jsp?successPage=' + window.location.pathname + window.location.search + window.location.hash;
      if (API) {
        API.close();
      }
    } else {

        if (fd.components && fd.components.ifrPopup) {
          fd.components.ifrPopup.open({ url: popupUrl + '?successPage=' + encodeURIComponent(target), height: 590, width: 560, opacity: .5});
        }
    }
  };
  
  var showLoginAjaxPopup = function (target, popupUrl, type) {
	  if (fd.mobWeb) { /* send user to page instead of popup */
		  window.top.location = popupUrl + '?successPage=' + encodeURIComponent(target);
	  } else {
		    if (fd.components && fd.components.ajaxPopup) {
		      var loginAjaxPopup;
		      
		      var hideCallback = function() {
		        if (loginAjaxPopup) {
		          loginAjaxPopup.popup.$el.removeClass(loginAjaxOverlayClassName);
		          
		        }
		        loginAjaxPopup = null;
		      };

		      var renderCallback  = (function(myType) {
		        return function(ajaxPopup, target, data) {
		          loginAjaxPopup = ajaxPopup;
		          ajaxPopup.popup.$overlay.off('click');
		          ajaxPopup.popup.config.valign = 'top';
		          ajaxPopup.popup.config.stayOnClick = true;
		          ajaxPopup.popup.config.noCloseOnOverlay = true;
		          ajaxPopup.popup.config.hidecallback = hideCallback;
		          ajaxPopup.scrollCheck.splice(0, ajaxPopup.scrollCheck.length);
		          ajaxPopup.popup.$overlay.css('opacity', 0.5);
		          ajaxPopup.popup.$el.addClass(loginAjaxOverlayClassName);
		          ajaxPopup.popup.show($('body'), 't');

		          if (myType === 'login') {
		            fd.components.loginForm.init();
		          } else {
		            fd.components.signupForm.init();
		          }
		        };
		      })(type)

		      fd.components.ajaxPopup.open({ 
		        href: popupUrl + '?successPage=' + encodeURIComponent(target),
		        afterRenderCallback: renderCallback
		      });
		    }
	  }
  };

  var socialSignup = function (target) {
    showLoginAjaxPopup(target,  '/social/signup_lite'+((fd.mobWeb)?'_mobile':'')+'.jsp', 'signup');
  };

  var lightSignup = function (target) {
    showLoginAjaxPopup(target, '/registration/signup_lite.jsp', 'signup');
  };

  var socialLogin = function (target) {
    showLoginAjaxPopup(target, '/'+((fd.mobWeb)?'login':'social')+'/login.jsp', 'login');
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
      var currentPage = window.location.pathname + window.location.search + window.location.hash;
      var target = fd.utils.getParameterByName('successPage') || currentPage;

      if (isMouseEvent && fd.properties && fd.properties.isLightSignupEnabled) {
        e.preventDefault();
        if (fd.properties.isSocialLoginEnabled) {
          socialSignup(target);
        } else {
          lightSignup(target);
        }
      }
    });

    // login required links
    $(document).on('click', '[fd-login-required]', function (e) {
      var isMouseEvent = e.x || e.clientX || e.y || e.clientY,
          ct = e.currentTarget,
          currentPage = window.location.pathname + window.location.search + window.location.hash,
          target = ct.hasAttribute('fd-login-successpage-current') && currentPage || ct.getAttribute('fd-login-successpage') || ct.pathname || ct.href || currentPage;
      //APPDEV-3971
        login.successTarget = target;
      
          if (window.location.pathname.indexOf('login.jsp') === -1 && isMouseEvent && fd.user && (fd.user.guest || fd.user.recognized)) {
            e.preventDefault();
            if (fd.user.guest && !ct.hasAttribute('fd-login-nosignup')) {
              socialSignup(target);
            } else {
              socialLogin(target);
            }
          }
    });
  };
  //APPDEV-3971

  var login = {
    socialSignup: socialSignup,
    lightSignup: lightSignup,
    socialLogin: socialLogin,
    initModule: init
  };

  fd.utils.registerModule("modules.common", "login", login, fd);
  fd.utils.initModule("modules.common.login", fd);
}(FreshDirect));

