/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function(fd) {
	"use strict";
	var $ = fd.libs.$;
	fd.components = fd.components || {};
	
	function init() {
		// on keyup we will check every time the form is valid or not
		$('#fd_login').bind(
				'change keyup',
				function() {
					var isCaptchaValid = !fd.components.captchaWidget || fd.components.captchaWidget.isValid();
				  
					if ($jq(this).validate().checkForm() && isCaptchaValid) { // form is valid
						$jq('#signinbtn').removeClass('button_disabled')
								.prop('disabled', false);
						$jq('#signinbtn').attr("tabindex",3);
						

					} else { // form is invalid
						$jq('#signinbtn').addClass('button_disabled').prop(
								'disabled', true);
						$jq('#signinbtn').attr("tabindex",-1);
					}
				});
		$('#fd_login #signinbtn').click(login);
		/* Setup form validation */
		$jq.validator.addMethod("customemail", 
					function validateEmail(email) {
					var re = /^[a-zA-Z0-9._-]+@([a-zA-Z0-9-_]+\.)+[a-zA-Z]{2,}$/;
					return re.test(email);
				} 
			);
		$jq('#fd_login').validate(
				{
					rules:{
						email:{
							required:true,
							email:true,
							customemail:true
					},
					userid:{
							required:true,
							email:true,
							customemail:true
						},		            
					password:{
						required:true,
					}
				},
				messages:{
					email:{
						required:"Required",
						email:"Incomplete e-mail Address",
						customemail:"Incomplete e-mail Address"
					},
					userid:{
						required:"Required",
						email:"Incomplete e-mail Address",
						customemail:"Incomplete e-mail Address"
					},		 		
				
					password:{
						required:"Required",
						//password:"Please enter at least 6 characters",
					}
					},
					highlight: function(element, errorClass, validClass) {
					$jq(element).addClass(errorClass).removeClass(validClass);
						$jq(element.form).find("span[id=" + element.id + "_img]").addClass('show_bg_arrow');
					},
					unhighlight: function(element, errorClass, validClass) {
					$jq(element).removeClass(errorClass).addClass(validClass);
						$jq(element.form).find("span[id=" + element.id + "_img]").removeClass('show_bg_arrow');
						
					},
					onkeyup: false,
					errorElement: "div",

				errorPlacement: function(error, element) {
					error.insertBefore(element);
				}, 
			}
		);
		if(fd.properties.isSocialLoginEnabled){
			/* This is an event */
			var my_on_login_redirect = function(args) {
				return true;
			}
			
			var onWidgetLoaded = function(e){
				$(e.widget).attr('tabIndex', 4);
			}

			var currentPage = window.location.pathname + window.location.search + window.location.hash,
		        target = fd.utils.getParameterByName('successPage') || currentPage;
			
			/* Initialise the asynchronous queue */
			window._oneall = window._oneall || [];
			/* Social Login Example */
			window._oneall.push([ 'social_login', 'set_providers', [ 'facebook', 'google' ] ]);
			
			window._oneall.push([ 'social_login', 'set_force_re_authentication', true]);
			window._oneall.push([ 'social_login', 'set_grid_sizes', [ 1, 2 ] ]);
			window._oneall.push([ 'social_login', 'set_custom_css_uri', '//'+window.location.host+'/media/social_login/social_login_media.css']);
			
			window._oneall.push([ 'social_login', 'set_event', 'on_login_redirect', my_on_login_redirect ]);
			window._oneall.push(['social_login', 'set_event', 'on_widget_loaded', onWidgetLoaded]);
			window._oneall.push([ 'social_login', 'set_callback_uri', $('#social-login-callback-uri').val()? 
					document.location.protocol + $('#social-login-callback-uri').val() :
						'https://' + document.location.host + '/social/social_login_success.jsp?successPage=' + encodeURIComponent(target)]);
			
			window._oneall.push([ 'social_login', 'do_render_ui', 'social_login_demo' ]);
			

		}	
		// google analytics
		if (window.ga && fd.gtm && fd.gtm.key) {
			ga('create', fd.gtm.key, 'auto');
			ga('set', {
				  page: '/social/login.jsp',
				  title: 'Login',
				  location: window.location.protocol + '//' + window.location.host + '/social/login.jsp'
			});
			ga('send', 'pageview');
		}
	}

	function renderCaptchaWidget() {
		fd.components.captchaWidget.render('login-g-recaptcha', function() {
    		$jq('#fd_login').trigger('change');
		}, function () {
			$jq('#login-g-recaptcha-container').hide();
		}, function () {
			$jq('#fd_login').trigger('change');
		});
	}
	function login(e) {
		$('.social-login-spinner').show();
		e.preventDefault();
		var email = $('#fd_login #email').val();
		var password = $('#fd_login #password').val();
		var sucessTarget = $('#fd_login #success-target').val();
		var loginData = {
				userId : email,
				password: password,
				successPage: sucessTarget
		};
		if (fd.components.captchaWidget && fd.components.captchaWidget.isEnabled()) {
			loginData.captchaToken = fd.components.captchaWidget.getResponse()
		}
		$.post('/api/login/', {
			"data" : JSON.stringify(loginData) 
		}).then(function(response) {
			var responseJson = JSON.parse(response);
			if (responseJson.success) { 
				if (fd.gtm && fd.gtm.data) {
						fd.gtm.data.googleAnalyticsData.login.loginAttempt = 'success';
						fd.gtm.updateDataLayer(fd.gtm.data.googleAnalyticsData);
				}
				parent.document.location = responseJson.successPage || sucessTarget || parent.document.location;
			} else if (responseJson.message === 'CaptchaRedirect' && (!fd.components.captchaWidget || !fd.components.captchaWidget.isEnabled())){
				FreshDirect.modules.common.login.socialLogin(FreshDirect.modules.common.login.successTarget);
			} else {
				showError(responseJson.errorMessages);
			}
			
			
		}, function(error) {
			showError();
		});
		return false;

	}
	
	function showError(message) {
		var errorMessage;
		if (!message) {
			//default message;
			errorMessage = 'Email and password do not match.</br>Please try again.';
			$('#email_img').addClass('show_bg_arrow');
			$('#email').addClass('error');
			$('#password_img').addClass('show_bg_arrow');
			$('#password').addClass('error');
		} else if (message.captcha) {
			errorMessage = 'Captcha is not valid.</br>Please try again.';
		}
		$('.social-login-spinner').hide();
		$('#fd_login .error-message').html(errorMessage).show();

		if (fd.components.captchaWidget && fd.components.captchaWidget.isEnabled()) {
			fd.components.captchaWidget.reset();
		}
		if (fd.gtm && fd.gtm.data) {
            	fd.gtm.data.googleAnalyticsData.login.loginAttempt = 'fail';
            	fd.gtm.updateDataLayer(fd.gtm.data.googleAnalyticsData);
		}
	}
	// if component is not registered, register
	if (!fd.components.loginForm) {
		var loginForm = {
			init : init,
			onCaptchaLoadCallback: renderCaptchaWidget
		};

		fd.modules.common.utils.register("components", "loginForm", loginForm,
				fd);
	}
	
}(FreshDirect));