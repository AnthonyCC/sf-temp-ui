/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function(fd) {
	"use strict";
	var $ = fd.libs.$;
	fd.components = fd.components || {};
	
	function init() {
		// on keyup we will check every time the form is valid or not
		var isCaptchaInited = $jq('[data-captcha]').attr('data-captcha-init') === 'true';
		if (isCaptchaInited){
			initCaptcha()
			$('[data-captcha]').attr('data-captcha-init', true);
		}
		
		$jq('#litesignup').bind('change keyup', function() {
			var isCaptchaValid = !isCaptchaInited || (isCaptchaInited && fd.components.captchaWidget.isValid());
			  
		    if($jq(this).validate().checkForm() && isCaptchaValid) { // form is valid
        		$jq('#signupbtn').removeClass('button_disabled').attr('disabled', false);
    		} else { // form is invalid
        		$jq('#signupbtn').addClass('button_disabled').attr('disabled', true);
    		}
		    
		    if ($jq('#litesignup #password1').val().length > 0) {
		    	$jq('.show-password').show();
		    } else {
		    	$jq('.show-password').hide();
		    }
		});

		$('#litesignup #signupbtn').click(signup);

		$jq(document).on("click","#litesignup .show-password", function(){
			$jq("#password1").toggleClass("showpwd");
			if($jq("#password1").hasClass("showpwd"))
			{
				$jq("#password1").prop("type","text");
				$jq('.show-password').text('Hide');
			}
			else
			{
				$jq("#password1").prop("type","password");
				$jq('.show-password').text('Show');
			}
		});

		$('#litesignup input').on('focus', function(){
			$(this).parent().find('.error-msg').hide();
		})


		$jq('#sul_type_fields input[type="radio"]').click(function(event){
			 let $cosFields = $jq('#sul_cos_fields');
			 if ($jq(this).val() === 'CORPORATE' && $jq(this).prop('checked')) {
				 $cosFields.show();
				 $cosFields.attr('aria-hidden', false);
				 $jq('#sul_type_fields').attr('aria-expanded', true);
			 } else {
				 $cosFields.hide();
				 $cosFields.attr('aria-hidden', true);
				 $jq('#sul_type_fields').attr('aria-expanded', false);
			 }
		 });
		/* Setup form validation */
		$jq.validator.addMethod("customemail", 
			function validateEmail(email) {
				var re = /^[a-zA-Z0-9._-]+@([a-zA-Z0-9-_]+\.)+[a-zA-Z]{2,}$/;
				return re.test(email);
			}
		);
		$jq('#litesignup').validate({
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
					minlength: $jq.validator.format( "Must be {0} or more characters" )
				}
			},
			highlight: function(element, errorClass, validClass) {
				$jq(element).addClass(errorClass).removeClass(validClass);
				$jq(element.form).find("span[id=" + element.id + "_img]").addClass('show_bg_arrow');  
		             
		             //highlight the security answer text on no input.
				if ( element.id == 'secret_answer') {                            
					$jq('.bodyCopySULNote').css("color","red"); 
					$jq(errorElement).hide();
				}                           	 	            	
			},
			unhighlight: function(element, errorClass, validClass) {
				$jq(element).removeClass(errorClass).addClass(validClass);
				$jq(element.form).find("span[id=" + element.id + "_img]").removeClass('show_bg_arrow');
		            	
				//un-highlight the security answer text on no input.
				if ( element.id == 'secret_answer') {
					$jq('.bodyCopySULNote').css("color","");
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
		var captchaID = $jq('[data-captcha]').attr('data-captcha');
		
		FreshDirect.components.captchaWidget.init(captchaID, function() {
			FreshDirect.components.signupForm.onCaptchaLoadCallback();
		});
	}

	function renderCaptchaWidget() {
		fd.components.captchaWidget.render('sign-up-g-recaptcha', function() {
    		$jq('#litesignup').trigger('change');
		}, function () {
			$jq('#sign-up-g-recaptcha-container').hide();
		}, function () {
			$jq('#litesignup').trigger('change');
		});
	}

	function signup(e) {
		e.preventDefault();
		$jq('.social-login-spinner').show();

		var email = $jq('#litesignup #email').val();
		var password = $jq('#litesignup #password1').val();
		var successPage = $jq('#litesignup #success-target').val();
		var serviceType = $jq('input[name="serviceType"]:checked').val();

		var companyName = $jq('#litesignup #companyName').val();
		var firstName = $jq('#litesignup #firstName').val();
		var lastName = $jq('#litesignup #lastName').val();
		var workPhone = $jq('#litesignup #workPhone').val();
		var zipCode = $jq('#litesignup #zipCode').val();

		var signupData = {
			email: email,
			password, password,
			zipCode: zipCode,
			serviceType: serviceType,
			companyName: companyName,
			firstName: firstName,
			lastName: lastName,
			workPhone: workPhone,
			successPage: successPage
		};
		
			if (fd.components.captchaWidget && fd.components.captchaWidget.isEnabled()) {
				signupData.captchaToken = fd.components.captchaWidget.getResponse()
			}

		$.post('/api/signup', {
			"data" : JSON.stringify(signupData) 
		}).then(function(response) {
			FreshDirect.successPage = response.successPage || successPage;
			$jq('.social-login-spinner').hide();

			if (response.success) { 
				if (response.skipPopup) {
					window.location = FreshDirect.successPage;
				} else {
					$jq('#signup-success').attr('data-signup-success', true).removeClass('hidden');
					$jq('#signup-content').hide();
				}
			} else if (response.message === 'CaptchaRedirect'){
				var isCaptchaInited = $jq('[data-captcha]').attr('data-captcha-init') === 'true';
				if (isCaptchaInited) {
					fd.components.captchaWidget.reset();
				} else {
					initCaptcha()
					$('[data-captcha]').attr('data-captcha-init', true);
				}
				$('[data-captcha]').removeClass('hidden');
				showError(response.errorMessages);
			} else {
				showError(response.errorMessages);
				if (fd.components.captchaWidget && fd.components.captchaWidget.isEnabled()) {
					fd.components.captchaWidget.reset();
				}
			}
		}, function(error) {
			showError();
			if (fd.components.captchaWidget && fd.components.captchaWidget.isEnabled()) {
				fd.components.captchaWidget.reset();
			}
		});
		return false;

	}
	
	function showError(message) {
		var errorMessage = '';

		$jq('#signupbtn').addClass('button_disabled').attr('disabled', true);


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

		fd.modules.common.utils.register("components", "signupForm", signupForm,
				fd);
	}
	
}(FreshDirect));