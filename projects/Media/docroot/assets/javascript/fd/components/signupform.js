/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function(fd) {
	"use strict";
	var $ = fd.libs.$;
	fd.components = fd.components || {};
	var captchaWidgetName = 'sign-up-g-recaptcha';
	
	function init() {
		// on keyup we will check every time the form is valid or not
		if ($('[data-captcha]').attr('data-captcha-init') === 'true'){
			initCaptcha();
		}
		
		$('#litesignup').bind('change keyup', function() {
			var isCaptchaInited = $('[data-captcha]').attr('data-captcha-init') === 'true';
			var isCaptchaValid = !isCaptchaInited || (isCaptchaInited && fd.components.captchaWidget.isValid(captchaWidgetName));
			  
		    if($(this).validate().checkForm() && isCaptchaValid) { // form is valid
        		$('#signupbtn').removeClass('button_disabled').attr('disabled', false);
    		} else { // form is invalid
        		$('#signupbtn').addClass('button_disabled').attr('disabled', true);
    		}
		    
		    if ($('#litesignup #password1').val().length > 0) {
		    	$('.show-password').show();
		    } else {
		    	$('.show-password').hide();
		    }
		});

		$('#litesignup #signupbtn').click(signup);

		$('#litesignup .show-password').click(function(){
			$("#password1").toggleClass("showpwd");
			if($("#password1").hasClass("showpwd"))
			{
				$("#password1").prop("type","text");
				$('.show-password').text('Hide');
			}
			else
			{
				$("#password1").prop("type","password");
				$('.show-password').text('Show');
			}
		});

		$('#litesignup input').on('focus', function(){
			$(this).parent().find('.error-msg').hide();
		})


		$('#sul_type_fields input[type="radio"]').click(function(event){
			 var $cosFields = $('#sul_cos_fields');
			 if ($(this).val() === 'CORPORATE' && $(this).prop('checked')) {
				 $cosFields.show();
				 $cosFields.attr('aria-hidden', false);
				 $('#sul_type_fields').attr('aria-expanded', true);
			 } else {
				 $cosFields.hide();
				 $cosFields.attr('aria-hidden', true);
				 $('#sul_type_fields').attr('aria-expanded', false);
			 }
		 });
		/* Setup form validation */
		$.validator.addMethod("customemail", 
			function validateEmail(email) {
				var re = /^[a-zA-Z0-9._-]+@([a-zA-Z0-9-_]+\.)+[a-zA-Z]{2,}$/;
				return re.test(email);
			}
		);
		$('#litesignup').validate({
			errorElement: "div",		
			rules: {
				email:{
					required:true,
					email:true,
					customemail:true
				}, 
				password:{
					required:true,
					minlength: 6
				}
			},
			messages:{
				email:{
					required:"Required",
					email:"Incomplete e-mail Address",
					customemail:"Incomplete e-mail Address"
				},
				password:{
					required:"Required",
					minlength: $.validator.format( "Must be {0} or more characters" )
				}
			},
			highlight: function(element, errorClass, validClass) {
				$(element).addClass(errorClass).removeClass(validClass);
				$(element.form).find("span[id=" + element.id + "_img]").addClass('show_bg_arrow');  
		             
		             //highlight the security answer text on no input.
				if ( element.id == 'secret_answer') {                            
					$('.bodyCopySULNote').css("color","red"); 
					$(errorElement).hide();
				}                           	 	            	
			},
			unhighlight: function(element, errorClass, validClass) {
				$(element).removeClass(errorClass).addClass(validClass);
				$(element.form).find("span[id=" + element.id + "_img]").removeClass('show_bg_arrow');
		            	
				//un-highlight the security answer text on no input.
				if ( element.id == 'secret_answer') {
					$('.bodyCopySULNote').css("color","");
				}           
			},
			onkeyup: false,
			errorPlacement: function(error, element) {
				error.insertBefore(element);
			}
		});

		// google analytics
		if (window.ga && fd.gtm && fd.gtm.key) {
			ga('create', fd.gtm.key, 'auto');
			ga('set', {
				  page: '/social/signup_lite.jsp',
				  title: 'Signup',
				  location: window.location.protocol + '//' + window.location.host + '/social/signup_lite.jsp'
			});
			ga('send', 'pageview');
		}
	}

	function initCaptcha() {
		var captchaID = $('[data-captcha]').attr('data-captcha');
		
		FreshDirect.components.captchaWidget.init(captchaWidgetName, captchaID, function() {
			FreshDirect.components.signupForm.onCaptchaLoadCallback(captchaID);
		});
	}

	function renderCaptchaWidget() {
		fd.components.captchaWidget.render(captchaWidgetName, function() {
    		$('#litesignup').trigger('change');
		}, function () {
			$('#sign-up-g-recaptcha-container').hide();
		}, function () {
			$('#litesignup').trigger('change');
		});
	}

	function signup(e) {
		e.preventDefault();
		$('.social-login-spinner').show();

		var email = $('#litesignup #email').val();
		var password = $('#litesignup #password1').val();
		var successPage = $('#litesignup #success-target').val();
		var serviceType = $('input[name="serviceType"]:checked').val();

		var companyName = $('#litesignup #companyName').val();
		var firstName = $('#litesignup #firstName').val();
		var lastName = $('#litesignup #lastName').val();
		var workPhone = $('#litesignup #workPhone').val();
		var zipCode = $('#litesignup #zipCode').val();

		var signupData = {
			email: email,
			password: password,
			zipCode: zipCode,
			serviceType: serviceType,
			companyName: companyName,
			firstName: firstName,
			lastName: lastName,
			workPhone: workPhone,
			successPage: successPage
		};
		
			if (fd.components.captchaWidget && fd.components.captchaWidget.isEnabled(captchaWidgetName)) {
				signupData.captchaToken = fd.components.captchaWidget.getResponse(captchaWidgetName)
			}

		$.post('/api/signup', {
			"data" : JSON.stringify(signupData) 
		}).then(function(response) {
			FreshDirect.successPage = response.successPage || successPage;
			$('.social-login-spinner').hide();

			if (response.success) { 
				if (response.skipPopup) {
					window.location = FreshDirect.successPage;
				} else {
					$('#signup-success').attr('data-signup-success', true).removeClass('hidden');
					$('#signup-content').hide();
				}
			} else if (response.message === 'CaptchaRedirect'){
				var isCaptchaInited = $('[data-captcha]').attr('data-captcha-init') === 'true';
				if (isCaptchaInited) {
					fd.components.captchaWidget.reset(captchaWidgetName);
				} else {
					initCaptcha()
					$('[data-captcha]').attr('data-captcha-init', true);
				}
				$('[data-captcha]').removeClass('hidden');
				showError(response.errorMessages);
			} else {
				showError(response.errorMessages);
				if (fd.components.captchaWidget && fd.components.captchaWidget.isEnabled(captchaWidgetName)) {
					fd.components.captchaWidget.reset(captchaWidgetName);
				}
			}
		}, function(error) {
			showError();
			if (fd.components.captchaWidget && fd.components.captchaWidget.isEnabled()) {
				fd.components.captchaWidget.reset(captchaWidgetName);
			}
		});
		return false;

	}
	
	function showError(message) {
		var errorMessage = '';

		$('#signupbtn').addClass('button_disabled').attr('disabled', true);


		if (message) {
			$.each(Object.keys(message), 
			function(index, value) {
				$('#litesignup #' + value + '_img').addClass('show_bg_arrow');
				$('#litesignup #' + value + '_msg').html(message[value]).show();
			}
			);
		}

		if (message.email) {
			errorMessage += message.email;
		}

		if (message.captcha) {
			errorMessage += 'Captcha is not valid.</br>Please try again.';
		}

		$('.social-login-spinner').hide();
		$('#litesignup .error-message').html(errorMessage).show();

		
	}
	// if component is not registered, register
	if (!fd.components.signupForm) {
		var signupForm = {
			init : init,
			onCaptchaLoadCallback: renderCaptchaWidget
		};

		fd.modules.common.utils.register("components", "signupForm", signupForm, fd);
	}
	
}(FreshDirect));
