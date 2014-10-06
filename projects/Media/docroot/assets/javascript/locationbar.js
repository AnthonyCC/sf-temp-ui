/*global jQuery, popup, FDModalDialog*/
(function($){
	var console = console || window.top.console || { log:function(){} };
	
	var $document = $(document),
		locationHandlerAPI = '/api/locationhandler.jsp',
		locationMessages = document.getElementById('location-messages');
	
	
	var setAddress = function(zip,address){
		if(!zip || !address) return;
		if(address.length) {
			address='('+address+')';
		}
		$('#locationbar .address .text').html(address);
		$('#locationbar .address .zipcode').html(zip);
	}, successHandler = function(data){
		window.location.reload();
	}, errorHandler = function(data){
		var messages;
		$(locationMessages).html(data.responseText);
		messages = $('.message',locationMessages).messages('add');
		setAddress($('.addresszip',locationMessages).html(),$('.addresstext',locationMessages).html());
	}, playScripts = function($data){
		
		$data.each(function(){
			var $this = $(this);
			if($this.is('script')) {
				eval($this.text());
			}
		});
	};
	
  var sendZip = function (e) {
		var text = $('#newziptext').val(),
			innerHTML;
		
		if(!/(^\d{5}$)/.test(text) || parseInt(text,10)===0 ) {
			$('#unrecognized').clone().html(function(index,oldHTML){
				return oldHTML.replace('{{zip}}',text);
			}).messages('add');
		} else {
			$.ajax({
				url:locationHandlerAPI,
				data:{
					action:'setZipCode',
					zipcode:text
				},
				success:successHandler,
				error:errorHandler
			});
		}
  };

	$document.on('messageAdded',function(e){
		var $target = $(e.target); 
		if($target.hasClass('moreinfo')) {
			FDModalDialog.close('.partial-delivery-moreinfo .fd-dialog');
			FDModalDialog.openUrl('/shared/locationbar/more_info.jsp',' ',700,300,'partial-delivery-moreinfo');
			if($target.hasClass('cos')) {
				$('.partial-delivery-moreinfo').addClass('cos')
			} else {
				$('.partial-delivery-moreinfo').removeClass('cos')
			}
		}		
	});
	
	$document.on('click','.ui-widget-overlay',function(e){
		FDModalDialog.close('.partial-delivery-moreinfo .fd-dialog');
	});

	$document.on('click','#newzipgo',sendZip);

  $document.on('keyup', '#newziptext', function (e) {
    // send form on enter
    if (e.keyCode === 13) {
      sendZip();
    }
  });

  var keys=[8,12,13,33,34,35,36,37,38,39,40,46,97];
  $document.on('keydown', '#newziptext', function (e) {
  	var kc = e.keyCode;
  	if( (kc<48 || kc>57) && keys.inArray(kc)===false && (kc<96 || kc>105) ) {
  		e.preventDefault();
  	}
  });
  
  
	$document.on('click','.delivery-popuplink',function(e){
		popup('/help/delivery_zones.jsp','large');
		e.preventDefault();
	});
	
	$document.on('click','#location-submit',function(e){
		var email = $('#messages #location-email').val(),
				$form = $('#messages .nodeliver-form form'),
				pattern=/^[a-zA-Z0-9._-]+@([a-zA-Z0-9-_]+\.)+[a-zA-Z]{2,}$/;
				       
		
		if(email.match(pattern)) {
			$form.attr('class','p');
			
			
			$.ajax({
				url:'/api/locationhandler.jsp',
				data:{
					futureZoneNotificationEmail:email,
					action:"futureZoneNotification"
				},
				success:function(data){
					$('#nodeliver-thanks').messages('add');
					playScripts($(data));
				},
				error:function(){
					$form.attr('class','e');
				}
			});			
		} else {
			$('label.n',$form).html('<b>Please make sure your email address is in the format "you@isp.com"</b>');
		}
				
		
		e.preventDefault();
	});
	
	$document.on('change','#selectAddressList',function(e){
		var key = $('#selectAddressList').val();
		
		$.ajax({
			url:locationHandlerAPI,
			data:{
				action:'selectAddress',
				selectAddressList:key				
			},
			success:successHandler,
			error:errorHandler
		});
	});

	/* login & registration */
	var hidePopOuts = function(event, override) {
		if (override !== undefined && override !== null && override === false) {
			return; //skip out
		}

    	if ( FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup && FreshDirect.components.ifrPopup.popup.shown === true ) {
			updatePinLevel('#signup_cont_formContent', $('#signup_cont_formContent').data('pinlvl')-1);
			FreshDirect.components.ifrPopup.popup.hide();
			return;
    	}		
		
    	if (
    		(
				$(event.target).attr('id') != 'locabar_loginButton' 
				&& $(event.target).closest('#login_cont_formContent').length === 0
				&& (
						/* check if mouse down started inside login box */
						hiderEventStartTarget == null || (
							$(hiderEventStartTarget).attr('id') != 'locabar_loginButton' 
							&& $(hiderEventStartTarget).closest('#login_cont_formContent').length === 0
						)				
					)
    		) || override
    	) {
    		event.preventDefault();
        	$("#locabar_loginButton").removeClass("loginButtonTab");
        	$('#login_cont_formContent').hide();
    	}
    	
    	if (
        		(
    				$(event.target).attr('id') != 'locabar_signupButton' 
    				&& $(event.target).closest('#signup_cont_formContent').length === 0 
    				&& (
    						/* check if mouse down started inside reg box */
    						hiderEventStartTarget == null || (
    							$(hiderEventStartTarget).attr('id') != 'locabar_signupButton' 
    							&& $(hiderEventStartTarget).closest('#signup_cont_formContent').length === 0
    						)				
    					)
        		) || override
        	) {
    		event.preventDefault();
    		
    		if (parseInt($('#signup_cont_formContent').data('pinlvl'), 10) <= 1 || override) {
            	$('#signup_cont_formContentForm_cancel').click();
            	$("#locabar_signupButton").removeClass("signupButtonTab");
            	$('#signup_cont_formContent').hide();
            	updatePinLevel('#signup_cont_formContent', 0);
    		} else {
    			updatePinLevel('#signup_cont_formContent', $('#signup_cont_formContent').data('pinlvl')-1);
    		}
    	}
	};
	
	//pass top level elemId so error msg can be hidden as well
	var resetPopOut = function(elemId) {
		//hide error msgs
		$(elemId+' .errorMsg').hide();
		
		//reset inputs
    	var inputs = $(elemId).find(':input');
    	$(inputs).each(function (i,e) {
			$(e).trigger('cancelReset');
		});
	};
	
	var hiderEventStartTarget = null;
	var hiderEventFunction = function(event) {
		if (event.type === 'mouseup') {
			hidePopOuts(event);
		} else if (event.type === 'mousedown') {
			hiderEventStartTarget = event.target;
		} else if (event.type === 'keyup' && event.keyCode === 27) {
			//escape
			event.preventDefault();
			hidePopOuts(event, true);
		}
	};
	
	//$('#ifrPopup').on('mouseup', hiderEventFunction);
	$document.on('mousedown mouseup keyup', hiderEventFunction);
	
	/* handle signup logic */
	$('#locabar_signupButton').click(function(event) {
		event.preventDefault();
		var lEnabData = $(this).data('lightsignup');
		var ajaxEnabData = $(this).data('ajaxsignup');
		var lightEnabled = (lEnabData != null && lEnabData != undefined) ? lEnabData : false;
		var ajaxEnabled = (ajaxEnabData != null && ajaxEnabData != undefined) ? ajaxEnabData : false;

		if (lightEnabled) {
			if (ajaxEnabled) { //tab-look
				//add class here to add right-arrow			
			} else { //sign up light
				showSignupOverlay('/registration/signup_lite.jsp', '<span class=\'text12\' style=\'color: #000; margin-left: -12px;\'><strong>Already have a password? <a href=\'/login/login.jsp\' onclick=\'window.top.location=this.href;return false;\' style=\'text-decoration:none;\'>Log in now</a></strong></span>');
			}
		} else {
			//default to page
			window.location='/registration/signup.jsp';
		}
	});
	
	var showSignupOverlay = function(url, title, height, width) {
		var width = width || 480;
		var height = height || 600;
		if (FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) {
			FreshDirect.components.ifrPopup.open({ url: url, width: width, height: height, opacity: .5});
		} else {
			doOverlayWindow('<iframe id=\'signupframe\' src=\''+url+'\' width=\''+width+'px\' height=\''+height+'px\' frameborder=\'0\' ></iframe>', title);
		}
	};
	
	/* change data-attrib pinlvl for elem
	 * pass lvl to manually set to that value, otherwise: lvl++ if display:block, lvl-- if display:none
	 * */
	var updatePinLevel = function(elemId, lvl) {
		if ($(elemId).length > 0) {
			var curLvl = 0;
			if (lvl !== undefined && lvl !== null && !isNaN(parseInt(lvl, 10)) ) {
				//set
				curLvl = lvl;
			} else {
				//update
				curLvl = $(elemId).data('pinlvl');
				if (isNaN(parseInt(curLvl, 10))) {
					curLvl = 0;
				}
				
				if ($(elemId).is(":visible")) {
					curLvl++;
				} else {
					curLvl--;
				}
			}

			$(elemId).data('pinlvl', curLvl);
		}
	};

	/* login form */
	var alignLoginDropbox = function() {
		//call this each time to ensure alignment
		var cssObj = ($("#locabar_loginButton")) ? $("#locabar_loginButton").offset() : null;
		if (cssObj == null) { return; }
		cssObj.top = cssObj.top + $("#locabar_loginButton").outerHeight() + 'px';
		cssObj.left = ((cssObj.left + $("#locabar_loginButton").outerWidth()) - $('#login_cont_formContent').outerWidth()) + 'px';
		$('#login_cont_formContent').css(cssObj);
    };

	/* registration form */
    var alignSignupDropbox = function() {
		//call this each time to ensure alignment
		var cssObj = ($("#locabar_signupButton")) ? $("#locabar_signupButton").offset() : null;
		if (cssObj == null) { return; }
		cssObj.top = cssObj.top + $("#locabar_signupButton").outerHeight() + 'px';
		cssObj.left = ((cssObj.left + $("#locabar_signupButton").outerWidth()) - $('#signup_cont_formContent').outerWidth()) + 'px';
		$('#signup_cont_formContent').css(cssObj);
    };
    
    /* login & registration */
	$(window).resize(function() {
		alignLoginDropbox();
		alignSignupDropbox();
	});
    
	/* login form */
    if ($('#login_cont_formContent').length == 0) {
    	var loginDropboxHtml = '';
    	loginDropboxHtml += '<div id="login_cont_formContent" style="display: none">';
    		loginDropboxHtml += '<form id="login_cont_formContentForm">';
    			loginDropboxHtml += '<div class="fieldInputs"><input id="login_cont_formContent_email" name="userId" value="Email" data-deftext="Email" class="ccc" /></div>';
    			loginDropboxHtml += '<div class="fieldInputs"><input id="login_cont_formContent_password" name="password" value="Password" data-deftext="Password" class="ccc" type="text" /></div>';
        		loginDropboxHtml += '<div id="login_cont_formContentForm_signInCont"><span style="display: none;" id="login_cont_formContentForm_loggingIn">Logging in...</span><button id="login_cont_formContentForm_signIn" name="submit" class="imgButtonOrange">sign in</button></div>';
    		loginDropboxHtml += '</form>';
			loginDropboxHtml += '<div class="errorMsg" style="display: none;">';
				loginDropboxHtml += '<div class="header">Please re-enter your Email and Password.</div>'; 
				loginDropboxHtml += 'The information you entered is incorrect. Please try again.';
			loginDropboxHtml += '</div>';
    		loginDropboxHtml += '<div class="bold alignRight" style="margin: 20px 0;">Forgot your <a href="/login/forget_password.jsp">password</a>?</div>';
    	loginDropboxHtml += '</div>';
    	
    	$(document).ready(function() {
			$('body').append(loginDropboxHtml);
		});
	}

    /* registration form */
    if ($('#signup_cont_formContent').length == 0) {
    	var signupDropboxHtml = '';
    	
    	signupDropboxHtml += '<div id="signup_cont_formContent" style="display: none" data-pinlvl="0">';

			signupDropboxHtml += '<div class="header">Sign up. Get Started. It\'s Easy!</div>';
			
			signupDropboxHtml += '<div class="errorMsg" style="display: none;">';
				signupDropboxHtml += '<ul id="signup_cont_formContent_errorMsg"></ul>';
			signupDropboxHtml += '</div>';


			signupDropboxHtml += '<div class="signup_cont_formFieldsCont">';
	    		signupDropboxHtml += '<form id="signup_cont_formContentForm">';

					signupDropboxHtml += '<div class="signup_cont_formFieldsContLeftCol">';
						signupDropboxHtml += '<div class="text14">Select delivery type:</div>';
		    			signupDropboxHtml += '<div class="fieldInputs text14bold">';
		    				signupDropboxHtml += '<span style="display:inline-block; padding-right: 50px;"><input id="signup_cont_formContent_dTypeHome" name="serviceType" value="HOME" type="radio" style="width: auto;" checked="checked" /> Home</span>';
		    				signupDropboxHtml += '<input id="signup_cont_formContent_dTypeCos" name="serviceType" value="CORPORATE" type="radio" style="width: auto;" /> Office';
		    			signupDropboxHtml += '</div>';
		
		    			signupDropboxHtml += '<div class="fieldInputs"><input id="signup_cont_formContent_firstName" name="firstName" value="First Name" data-deftext="First Name" class="ccc" /></div>';
		    			signupDropboxHtml += '<div class="fieldInputs"><input id="signup_cont_formContent_lastName" name="lastName" value="Last Name" data-deftext="Last Name" class="ccc" /></div>';
		    			signupDropboxHtml += '<div class="fieldInputs"><input id="signup_cont_formContent_zipcode" name="zipcode" value="Delivery Zip Code" data-deftext="Delivery Zip Code" class="ccc" maxlength="5" /></div>';
		    			signupDropboxHtml += '<div class="fieldInputs"><input id="signup_cont_formContent_email" name="email" value="Email" data-deftext="Email" class="ccc" /></div>';
		    			signupDropboxHtml += '<div class="fieldInputs"><input id="signup_cont_formContent_emailConfirm" name="emailConfirm" value="Confirm Email" data-deftext="Confirm Email" class="ccc" /></div>';
		    		signupDropboxHtml += '</div>';

					signupDropboxHtml += '<div class="signup_cont_formFieldsContRightCol">';
		    			signupDropboxHtml += '<div class="fieldInputs noMargin"><input id="signup_cont_formContent_password" name="password" value="Password" data-deftext="Password" class="ccc" /></div>';
						signupDropboxHtml += '<div class="fieldInputs text12">At least 4 characters, case sensitive.</div>';
		    			signupDropboxHtml += '<div class="fieldInputs"><input id="signup_cont_formContent_passwordConfirm" name="passwordConfirm" value="Confirm Password" data-deftext="Confirm Password" class="ccc" /></div>';
		    			signupDropboxHtml += '<div class="fieldInputs noMargin"><input id="signup_cont_formContent_securityQuestion" name="securityQuestion" value="Security Question" data-deftext="Security Question" class="ccc" /></div>';
						signupDropboxHtml += '<div class="fieldInputs text12">Town of birth or mother\'s maiden name.</div>';
		
						signupDropboxHtml += '<div>';
							signupDropboxHtml += '<div class="text14bold">Need Help?<br />Call: 1.212.796.8082</div>';
							signupDropboxHtml += '<div class="dots-hline"></div>';
							signupDropboxHtml += '<div class="text14">By signing up, you agree to the <a href="javascript:popup(\'/registration/user_agreement.jsp\', \'large\')" class="text14">Terms of use</a>.</div>';
							signupDropboxHtml += '<input name="terms" value="true" style="display: none"; />';
							signupDropboxHtml += '<input name="isAjax" value="true" style="display: none"; />';
						signupDropboxHtml += '</div>';
			    	signupDropboxHtml += '</div>';
			    	
	    		signupDropboxHtml += '</form>';
				
	    		signupDropboxHtml += '<div class="clear right" style="margin-right: -10px; display: block;"><button id="signup_cont_formContentForm_signUp" name="submit" class="imgButtonGreen">sign up</button> <button id="signup_cont_formContentForm_cancel" name="cancel" class="imgButtonBrown">cancel</button></div>';
			signupDropboxHtml += '</div>';

    	signupDropboxHtml += '</div>';
    	
    	//$(document).ready(function() {
			$('body').append(signupDropboxHtml);
		//});
	}

	$(document).ready(function() {

	    
		/* login form */
	    $('#login_cont_formContentForm_signIn').click(function(event){
			event.preventDefault();
	    	$('#login_cont_formContentForm').submit();
	    });
	    
	    $('#login_cont_formContentForm').submit(function(event) {
			event.preventDefault();
			
	    	var form = $(this);
	    	if (!form.data('submitting')) {
				$('#login_cont_formContent .errorMsg').hide();
	    		form.data('submitting', true);
				$('#login_cont_formContentForm_loggingIn').show();
				$('#login_cont_formContentForm_signIn').toggleClass('imgButtonOrange imgButtonWhite');
				
	    		var formData = {};
	    		$(form.serializeArray()).each(function () { formData[this.name] = this.value; });
	    		var sPage = $jq.QueryString["successPage"];
	    		if (sPage != null && typeof sPage !== 'undefined') {
	        		formData.successPage = sPage;
	    		} else {
	    			formData.successPage = window.location.pathname+window.location.search;
	    		}
	    		formData.successPage = encodeURIComponent(formData.successPage);
	    		
	    		$jq.post('/api/login/', "data="+JSON.stringify(formData), function(data) {
	    			if (data.success) {
	    				if (data.hasOwnProperty('successPage') && data.successPage != '' && data.successPage != null) {
	    					window.location = data.successPage;
	    				} else {
	    					//refresh
	    					window.location = window.location;
	    				}
	    				
	                   	$("#locabar_loginButton").toggleClass("loginButtonTab");
	                   	$('#login_cont_formContent').toggle();
	    			} else {
	    				$('#login_cont_formContent .errorMsg').show();
	    			}
					$('#login_cont_formContentForm_loggingIn').hide();
	        		form.data('submitting', false);
					$('#login_cont_formContentForm_signIn').toggleClass('imgButtonWhite imgButtonOrange');
	    		}, "json");
	    	}
	    });
	    
		$('#locabar_loginButton').click(function(event) {
			alignLoginDropbox();
			
	        //first
	       	$("#locabar_loginButton").toggleClass("loginButtonTab");
	       	$('#login_cont_formContent').toggle();
		});
	    
		/* registration form */
	    $('#signup_cont_formContentForm_signUp').click(function(event){
			event.preventDefault();
	    	$('#signup_cont_formContentForm').submit();
	    });

	    $('#signup_cont_formContentForm').submit(function(event) {
			event.preventDefault();
			
	    	var form = $(this);

	    	if (!form.data('submitting')) {
				$('#signup_cont_formContent .errorMsg').hide();
	    		form.data('submitting', true);
	    		/* no messaging
	    		 * $('#signup_cont_formContentForm_signingUp').show();
	    		 */
				$('#signup_cont_formContentForm_signUp').toggleClass('imgButtonGreen imgButtonWhite');
	    		var formData = {};
    			//empty "default" text, so it's not always sent
	    		var proxy = $.fn.serializeArray;
	    		$.fn.serializeArray = function(){
	    			var inputs = this.find(':input');
	    			$(inputs).each(function (i,e) {
	    				if ($(e).data('deftext') && $(e).data('deftext') == this.value) {
		    				this.value = '';
		    			}
	    			});
	    	        var serialized = proxy.apply( this, arguments );
	    	        return serialized;
	    	    };
	    		$(form.serializeArray()).each(function (i,e) {
	    			formData[this.name] = this.value;
	    		});
	    		
	    		var sPage = $jq.QueryString["successPage"];
	    		if (sPage != null && typeof sPage !== 'undefined') {
	        		formData.successPage = sPage;
	    		} else {
	    			formData.successPage = window.location.pathname+window.location.search;
	    		}
	    		formData.successPage = encodeURIComponent(formData.successPage);

	    		$jq.post('/api/registration/', "data="+JSON.stringify(formData), function(data) {
	    			
	    			if (data.success) {
	    				if (data.hasOwnProperty('successPage') && data.successPage != '' && data.successPage != null) {
	    					window.location = data.successPage;
	    				} else {
	    					//refresh
	    					window.location = window.location;
	    				}
	    			} else {
	    	    		$(form.find(':input')).each(function (i,e) {
	    	    			if ($(e).val() == '') {
	    	    				$(e).val($(e).data('deftext'));
	    	    				$(e).addClass('errBorder');
	    	    			}
	    	    		});
	    	    		
	    	    		//additional overlays
	    	    		if (data.hasOwnProperty('message') && data.message != '' && data.message != null) {
	    	    			//$('#signup_cont_formContent').hide();
	    	    			//$('#locabar_signupButton').click(); //closer
	    	            	updatePinLevel('#signup_cont_formContent');
	    	    			showSignupOverlay(data.message, null, 400, 650);
	    	    		}
	    	    		
	    	    		//error msgs
	    	    		if (data.hasOwnProperty('errorMessages') && data.errorMessages != '' && data.errorMessages != null) {

		    				var errMsgsUnique = {};
		    				$('#signup_cont_formContent_errorMsg').empty();
		    				for (var errMsg in data.errorMessages) {
		    					if (data.errorMessages[errMsg] == 'This information is required.') {
		    						data.errorMessages[errMsg] = 'Information is missing, please make sure all fields are filled in';
		    					}

		    					if (data.errorMessages[errMsg].indexOf('An account already exists with this email address.') !== -1) {
		    						//clear fields
		    						$(['#signup_cont_formContent_password', '#signup_cont_formContent_passwordConfirm']).each(function(i,e) {
			    						$(e).val('');
			    						$(e).blur();
		    						})
		    					}
		    					errMsgsUnique[data.errorMessages[errMsg]] = [];
		    					errMsgsUnique[data.errorMessages[errMsg]].push(errMsg);
		    				}

		    				for (var errMsg in errMsgsUnique) {
		    					var curMsgHtml = '';
		    					curMsgHtml += '<li>';
		    						curMsgHtml += errMsg;
		    					curMsgHtml += '</li>';
		    					
		    					$('#signup_cont_formContent_errorMsg').append(curMsgHtml);
		    				}
		    				
		    				
		    				$('#signup_cont_formContent .errorMsg').show();
	    	    		}
	    			}
	    		}, "json");
				$('#signup_cont_formContentForm_signingUp').hide();
        		form.data('submitting', false);
				$('#signup_cont_formContentForm_signUp').toggleClass('imgButtonWhite imgButtonGreen');
	    	
	    	}
	    });
	    
		$('#locabar_signupButton').click(function(event) {
			alignSignupDropbox();
			
	        //first
	       	$("#locabar_signupButton").toggleClass("signupButtonTab");
	       	$('#signup_cont_formContent').toggle();

        	updatePinLevel('#signup_cont_formContent');
		});
		
		$('#signup_cont_formContentForm_cancel').click(function(event) {
			//manually set back to "home"
			$('#signup_cont_formContent_dTypeHome').click();
			resetPopOut('#signup_cont_formContent');
			$('#locabar_signupButton').click(); //closer
		});
		
		var inputFieldEvent = function(event) {
	    	var elem = $(this);
	    	var defText = elem.data('deftext');
	    	if (event.type == 'focus') {
	    		if (elem.hasClass('ccc') && elem.val() == defText) {
	    			elem.val('');
	    			elem.toggleClass('ccc');
	    			//remove "err"
    				elem.removeClass('errBorder');
	    		}
				if (elem.attr('id') == 'login_cont_formContent_password' || elem.attr('id') == 'signup_cont_formContent_password' || elem.attr('id') == 'signup_cont_formContent_passwordConfirm') {
					changeType(elem, 'password');
				}
	    	} else if (event.type == 'blur') {
	    		if (elem.val() == '') {
	    			elem.val(defText);
	    			elem.toggleClass('ccc');
	    			if (elem.attr('id') == 'login_cont_formContent_password' || elem.attr('id') == 'signup_cont_formContent_password' || elem.attr('id') == 'signup_cont_formContent_passwordConfirm') {
	    				changeType(elem, 'text');
	    			}
	    		}
	    	} else if (event.type == 'cancelReset') {
	    		if (!elem.is(":hidden")) { //use this because fields are hidden via css
	    			if (elem.data('deftext')) {
	    				elem.val(elem.data('deftext'));
	    			}
	    			elem.addClass('ccc');
	    			//remove "err"
					elem.removeClass('errBorder');
	    		}
    			if (elem.attr('id') == 'login_cont_formContent_password' || elem.attr('id') == 'signup_cont_formContent_password' || elem.attr('id') == 'signup_cont_formContent_passwordConfirm') {
    				changeType(elem, 'text');
    			}  		
	    	}
	    };
	    
		/* login & registration */
	    $('#login_cont_formContent input, #signup_cont_formContent input').on('focus blur cancelReset', inputFieldEvent);
	});
	
}(jQuery));

