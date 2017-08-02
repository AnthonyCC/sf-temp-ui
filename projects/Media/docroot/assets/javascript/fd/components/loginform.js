/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function(fd) {
	"use strict";
	var $ = fd.libs.$;
	fd.components = fd.components || {};
	// if component is not registered, register
	if (!fd.components.loginForm) {
		function init() {
			// on keyup we will check every time the form is valid or not
			$('#fd_login').bind(
					'change keyup',
					function() {
						if ($jq(this).validate().checkForm()) { // form is valid
							$jq('#signinbtn').removeClass('button_disabled')
									.prop('disabled', false);

						} else { // form is invalid
							$jq('#signinbtn').addClass('button_disabled').prop(
									'disabled', true);
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
				/* Initialise the asynchronous queue */
				window._oneall = window._oneall || [];
				/* Social Login Example */
				window._oneall.push([ 'social_login', 'set_providers', [ 'facebook', 'google' ] ]);
				
				window._oneall.push([ 'social_login', 'set_force_re_authentication', true]);
				window._oneall.push([ 'social_login', 'set_grid_sizes', [ 1, 2 ] ]);
				window._oneall.push([ 'social_login', 'set_custom_css_uri', '//www.freshdirect.com/media/social_login/social_login_media.css']);
				
				window._oneall.push([ 'social_login', 'set_event', 'on_login_redirect', my_on_login_redirect ]);
				window._oneall.push([ 'social_login', 'set_callback_uri', $('#social-login-callback-uri').val()? 
						document.location.protocol + $('#social-login-callback-uri').val() :
							'https://' + document.location.host + '/social/social_login_success.jsp']);
				
				window._oneall.push([ 'social_login', 'do_render_ui', 'social_login_demo' ]);
				

			}	
		}
		
		function login(e) {
			$('.social-login-spinner').show();
			e.preventDefault();
			var email = $('#fd_login #email').val();
			var password = $('#fd_login #password').val();
			$.post('/api/login/', {
				"data" : '{"userId" : "'+ email + '","password" : "'+password+'"}'
			}).then(function(response) {
				var responseJson = JSON.parse(response);
				if (responseJson.success) { 
					parent.document.location.reload()
				} else {
					showError();
				}
				
				
			}, function(error) {
				showError();
			});
			return false;

		}
		
		function showError() {
			$('.social-login-spinner').hide();
			$('#fd_login .error-message').show();
			$('#email_img').addClass('show_bg_arrow');
            $('#email').addClass('error');
            $('#password_img').addClass('show_bg_arrow');
            $('#password').addClass('error');
		}
		var loginForm = {
			init : init
		};

		fd.modules.common.utils.register("components", "loginForm", loginForm,
				fd);
	}
	
}(FreshDirect));