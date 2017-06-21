/*global jQuery, popup, FDModalDialog*/
(function($){
	var console = console || { log:function(){} };
	
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
		var text, innerHTML;

		/*text = $('#newziptext,.newziptext').map(function() {
			return $(this).val() || null;
		}).toArray().join(','); */
		text = $jq(e.target).parent().find('.newziptext').val();
		
		if(!/(^\d{5}$)/.test(text) || parseInt(text,10)===0 ) {
			/*$('#unrecognized').clone().html(function(index,oldHTML){
				return oldHTML.replace('{{zip}}',text);
			}).messages('add');*/
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
	window['sendZip']=sendZip;

	/* called for each msg (so be careful with the count) */
	function updateMessagesCount() {
		var $messages_count = $('.messages-count');
		var curCount = 0;
		var countOthers = getNonMessagesCount();

		$messages_count.data('count', ($messages_count.data('count') || $messages_count.attr('data-count') || curCount));
		curCount = $messages_count.data('count');

		curCount++;

		$messages_count.data('count', curCount);
		$messages_count.html(curCount + countOthers);
		if (curCount + countOthers > 0) {
			$('.locabar-messages-section').show();
		}
	}
	
	/* count alerts here */
	function getNonMessagesCount() {
		var count = 0;

		if($('#activatesoalert .so-activate-alert').length){
			count += $('#activatesoalert .so-activate-alert').length;
		}
		//count test alerts for debugging
		if ($jq('.testalerts').length) {
			count += $jq('.testalerts').length; 
		}
		return count;
	}

	$document.on('messageAdded',function(e){
		var $target = $(e.target);
		if($target.hasClass('moreinfo')) {
			if (FDModalDialog) {
				FDModalDialog.close('.partial-delivery-moreinfo .fd-dialog');
				FDModalDialog.openUrl('/shared/locationbar/more_info.jsp',' ',700,300,'partial-delivery-moreinfo');
			}
			if($target.hasClass('cos')) {
				$('.partial-delivery-moreinfo').addClass('cos');
			} else {
				$('.partial-delivery-moreinfo').removeClass('cos');
			}
		}
		updateMessagesCount();
	});
	
	$document.on('click','.ui-widget-overlay',function(e){
		if (FDModalDialog) {
			FDModalDialog.close('.partial-delivery-moreinfo .fd-dialog')
		};
	});

	//$document.on('click', '#newzipgo', sendZip);
	$document.on('click', '#newzipgo',function(e){
		var text_length=$(this).parent().find("input").val().length;		
		console.log(text_length);
		if(text_length<5 && text_length>=1){					
			$(this).parent().next().css("visibility","visible");
			$jq(this).next().css("visibility","visible");
			$(this).parent().find("input").addClass("input-error");	
			setTimeout(function(){
				$(this).parent().find("input").focus();
			},500);
		}
		else if(text_length!= "" && text_length == 5){
			$('#messages').removeClass("open");
			sendZip(e);
		}
	});

	$document.on('keydown', '#newziptext', function (e) {
		// send form on enter
		if (e.keyCode === 13) {
			//sendZip();
		}
	});
	$('#newzipgo').on('keydown', function (e) {
		// send form on enter
		if (e.keyCode === 13) {
			//sendZip();
			setTimeout(function(){
				//$('.locabar_addresses-anon-deliverable-item-icon-clock .avlTimeFocus').blur();
				$(this).focus();
			},500);
			$(this).focus();
		}
	});

	var keys=[8,12,13,33,34,35,36,37,38,39,40,46,97];
	$document.on('keydown', '#newziptext', function (e) {
		var kc = e.keyCode;
		if( (kc<48 || kc>57) && keys.inArray(kc)===false && (kc<96 || kc>105) ) {
			e.preventDefault();
		}
	});
	$document.on('keydown', '#location-email', function (e) {
		if (e.keyCode === 13) {
			$('#location-submit').click();
		}
	}); 
	$document.on('keydown', '#location-submit', function (e) {
		if (e.keyCode === 13) {
			$(this).click();
		}
	}); 
	//enter key press to add delivery address button

	$document.on('keydown', '.locabar_addresses-anon-deliverable-add-address-btn', function (e) {
		if (e.keyCode === 13) {
			$(this).click();
		}
	});

	$document.on('keydown', '.locabar-modify-order-dropdown-container-delails a,.locabar_triggers_menu a', function (e) {
		if (e.keyCode === 13) {
			//$(this).click();
			srcpath=$(this).attr("href");
			//window.open(location.protocol+"//"+location.host+srcpath);
			window.location.href= srcpath;
		}
	});
	$document.on('keydown', '.locabar-modify-order-dropdown-container-modify button', function (e) {
		if (e.keyCode === 13) {
			$(this).trigger("onclick");
			//e.preventDefault();
			//srcPath=$(this).attr("onclick");
			//window.location.href="";
			//$(this).trigger("onclick");
			
			// url_path=srcPath.split(".href='/");
			//window.location.href="";
			//window.location.href= url_path[1];
			//console.log(url_path[1])
			//location.replace("");
			//window.location=url_path[1];
		}
	});

	$document.on('click','.delivery-popuplink',function(e){
		popup('/help/delivery_zones.jsp','large');
		e.preventDefault();
	});
	
	$document.on('click','#location-submit',function(e){
		var email = $('#location-email,.location-email-text').map(function() {
			return $(this).val() || null;
		}).toArray().join(',');

		var $form = $('.nodeliver-form form'),
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

					if (FreshDirect.locabar.isFdx === true) {
						$('label.n',$form).html('<b>Thanks for your email!</b> We will notify you once we start delivering to your area.');
						$('#location-email,.location-email-text').val('');
					} else {
						playScripts($(data));
					}
					$(this).parent().find(".error-msg").css("visibility","");
					$(this).parent().find("input").removeClass("input-error");	
				},
				error:function(){
					$form.attr('class','e');
				}
			});			
		} else {
			//$('label.n',$form).html('<b>Please make sure your email address is in the format "you@isp.com"</b>');
			$(this).parent().find(".error-msg").css("visibility","visible");
			$(this).parent().find("input").addClass("input-error");	
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

	$document.on('mouseup', function(event) {
    	if($(event.target).attr('id') != 'locabar_loginButton' && $(event.target).closest('#login_cont_formContent').length==0) {
        	$("#locabar_loginButton").removeClass("loginButtonTab");
        	$('#login_cont_formContent').hide().trigger('hide');
    	}
    });
    
	
	var alignLoginDropbox = function() {
		//call this each time to ensure alignment
		var cssObj = ($("#locabar_loginButton")) ? $("#locabar_loginButton").offset() : null;
		if (cssObj == null) { return; }
		cssObj.top = cssObj.top + $("#locabar_loginButton").outerHeight() + 'px';
		cssObj.left = ((cssObj.left + $("#locabar_loginButton").outerWidth()) - $('#login_cont_formContent').outerWidth()) + 'px';
		$('#login_cont_formContent').css(cssObj);
    	
    }
	$(window).resize(function() {
		if (!$('#locabar_loginButton').hasClass('loginButtonSocial')) {
			alignLoginDropbox();
		}
	});
	$(document).ready(function() {
		if (!$('#locabar_loginButton').hasClass('loginButtonSocial')) {
			if ($('#login_cont_formContent').length == 0) {
		    	var loginDropboxHtml = '';
		    	loginDropboxHtml += '<div id="login_cont_formContent" style="display: none">';
		    		loginDropboxHtml += '<form id="login_cont_formContentForm">';
		    			loginDropboxHtml += '<div class="fieldInputs!!!"><label for="login_cont_formContent_email"></label><input id="login_cont_formContent_email" type="email" name="userId" value="Email" data-deftext="Email" class="ccc" /></div>';
		    			loginDropboxHtml += '<div class="fieldInputs"><label for="login_cont_formContent_password"></label><input id="login_cont_formContent_password" name="password" value="Password" data-deftext="Password" class="ccc" type="text" /></div>';
		        		loginDropboxHtml += '<div id="login_cont_formContentForm_signInCont"><span style="display: none;" id="login_cont_formContentForm_loggingIn">Logging in...</span><button id="login_cont_formContentForm_signIn" name="submit" class="imgButtonOrange">sign in</button></div>';
		    		loginDropboxHtml += '</form>';
					loginDropboxHtml += '<div class="errorMsg" style="display: none;">'
						loginDropboxHtml += '<div class="header">Please re-enter your Email and Password.</div>'; 
						loginDropboxHtml += 'The information you entered is incorrect. Please try again.';
					loginDropboxHtml += '</div>';
		    		loginDropboxHtml += '<div class="bold alignRight" style="margin: 20px 0;">Forgot your <a href="/login/forget_password.jsp">password</a>?</div>';
		    	loginDropboxHtml += '</div>';
		    	
				$('body').append(loginDropboxHtml);
			}
		}
		
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
				if (FreshDirect.locabar.isFdx === false) {
					$('#login_cont_formContentForm_signIn').toggleClass('imgButtonOrange imgButtonWhite');
				}
				
	    		var formData = {};
	    		$(form.serializeArray()).each(function () { formData[this.name] = this.value; });

	    		var sPage = $jq.QueryString["successPage"];

	    		if (!formData.hasOwnProperty('successPage') && sPage != null && typeof sPage !== 'undefined') {
	        		formData.successPage = sPage;
				} else if (formData.hasOwnProperty('successPage') && formData.successPage != '') {
					//do nothing, already set
	    		} else {
	    			formData.successPage = window.location.pathname+window.location.search;
	    		}

	    		formData.successPage = encodeURIComponent(formData.successPage);

	    		$jq.post('/api/login/', "data="+JSON.stringify(formData), function(data) {
	    			if (data.success) {
	    				if(data.message =="TcAgreeFail"){
	    					 FreshDirect.components.ifrPopup.open({ url: '/registration/tcaccept_lite.jsp', width: 320, height: 400});
	    				}else{
							if (data.hasOwnProperty('successPage') && data.successPage != '' && data.successPage != null) {
								window.location = data.successPage;
							} else {
								//refresh
								window.location = window.location;
							}
							
							if (FreshDirect.locabar.isFdx === false) {
								$("#locabar_loginButton").toggleClass("loginButtonTab");
								$('#login_cont_formContent').toggle();
							}
	    				}
	    			} else if(data.message == "CaptchaRedirect") {
    					window.location = '/login/login.jsp';
	    			} else if (data.message !== null && data.errorMessages !== null) {
	    				
	    				var errMsgs = '';
	    				if ($.isPlainObject(data.errorMessages)) {
	    					if (data.errorMessages.hasOwnProperty(data.message)) {
	    						errMsgs = data.errorMessages[data.message];
	    					}
	    				} else {
	    					errMsgs = data.errorMessages;
	    				}
	    				if (errMsgs !== '') {
	    					$('#login_cont_formContent .errorMsg').html(errMsgs);
	    					$('#login_cont_formContent .errorMsg').show();
	    				}
    				} else{
	    				$('#login_cont_formContent .errorMsg').show();
	    			}
					
					if (FreshDirect.locabar.isFdx === false) {
						$('#login_cont_formContentForm_loggingIn').hide();
					}
	        		form.data('submitting', false);
					
					if (FreshDirect.locabar.isFdx === false) {
						$('#login_cont_formContentForm_signIn').toggleClass('imgButtonWhite imgButtonOrange');
					}
	    		}, "json");
	    	}
	    });
	    
	    $('#login_cont_formContent input').on('focus blur', function(event) {
	    	var elem = $(this);
	    	var defText = elem.data('deftext');
	    	if (event.type == 'focus') {
	    		if (elem.hasClass('ccc') && elem.val() == defText) {
	    			elem.val('');
	    			elem.toggleClass('ccc');
	    		}
				if (elem.attr('id') == 'login_cont_formContent_password') {
					changeType(elem, 'password');
				}
	    	} else if (event.type == 'blur') {
	    		if (elem.val() == '') {
	    			elem.val(defText);
	    			elem.toggleClass('ccc');
	    			if (elem.attr('id') == 'login_cont_formContent_password') {
	    				changeType(elem, 'text');
	    			}
	    		}
	    	}
	    });
	    
		$('#locabar_loginButton').click(function(event) {
			if (!$('#locabar_loginButton').hasClass('loginButtonSocial')) {
				alignLoginDropbox();
			}
			
	        //first
	       	$("#locabar_loginButton").toggleClass("loginButtonTab");
	       	$('#login_cont_formContent').toggle();
		});
		
		//code for SHIFT+TAB from dropdown ,enter zip code to delivery information(dropdown with worng zipcode)
		$(".locabar_addresses-anon-nondeliverable .locabar_addresses-change-zip-cont .newziptext").keydown(function(e){
			var TABKEY = 9;
			if (e.which == TABKEY) {
				if (e.shiftKey) {
					$jq('#locabar_addresses_trigger').focus();
					
				} else {
					$jq("#newzipgo").focus();
				}
				e.preventDefault();
			}
		});
		//code for SHIFT+TAB from enter zip code to available time slots(dropdown with correct zip code)
		$(".locabar_addresses-anon-deliverable-change-zip-cont .locabar_addresses-change-zip-cont .newziptext").keydown(function(e){
			var TABKEY = 9;
			if (e.which == TABKEY) {
				if (e.shiftKey) {
					$jq('.locabar_addresses-anon-deliverable-item-icon-clock .avlTimeFocus').focus();
					
				} else {
					$jq(".locabar_addresses-anon-deliverable-change-zip-cont #newzipgo").focus();
				}
				e.preventDefault();
			}
		});
		//code to exit dropdown using ESC key ,for sing in button dropdown after sign in
		$("#locabar_user_trigger .locabar_triggers_menu.posAbs a").keyup(function(e){
			if(e.keyCode == 27){
				$jq("#locabar_user_trigger").removeClass("hover");
				$jq("#locabar_user_trigger").focus();
				e.preventDefault();
			}
		});
		//code to exit dropdown  using ESC key ,for delivery information dropwown before signin 
		$("#locabar_addresses_trigger #locabar_addresses").keyup(function(e){
			if(e.keyCode == 27){
				$jq("#locabar_addresses_trigger").removeClass("hover");
				$jq("#locabar_addresses_trigger").focus();
				e.preventDefault();
			}
		});
		//code to exit dropdown using ESC key ,for delivery information dropdown after signin
		$(".ui-selectmenu-menu ul li.ui-selectmenu-optgroup.ui-menu-divider").keyup(function(e){
			if(e.keyCode == 27){
				$jq("#locabar_addresses_trigger").removeClass("hover");
				$jq("#locabar_addresses_trigger").focus();
				e.preventDefault();
			}
		});
		

		
		
		/*		$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("visibility","hidden");
			$jq(".locabar_addresses.locabar_triggers_menu.anon-deliverable").css("opacity","0");

				$jq("#locabar_addresses_trigger").focus();
				event.preventDefault();
			}
		});*/
		
		$(".newzipgo").keydown(function(e){
			
			var text_length=$(this).parent().find("input").val().length;	
			$(this).parent().next().css("visibility","");
			console.log(text_length);
			var keycode = (e.keyCode ? e.keyCode : e.which);
			if(keycode == '13'){
				if(text_length<5 && text_length>=1){					
						
						$(this).parent().find("input").addClass("input-error");	
						/*$('.locabar_addresses-anon-deliverable-item-icon-clock .avlTimeFocus').blur();
						$(".locabar_addresses-anon-deliverable-item-icon-truck a").blur();
						$(this).parent().find("input").focus();*/
						setTimeout(function(){
							
							$(this).focus();
							$(this).parent().next().css("visibility","visible");
							
						},500);
						$(this).focus();
				}
				else if(text_length== "" && text_length == 5){
					$('#messages').removeClass("open");
					sendZip(e);
				}
					
				e.preventDefault();
			}
		});
		
	/*	$("#location-submit.cssbutton").on("focusin",function(){
			$(this).css(" background-color"," #5fb067"); 
			$(this).css(" border-color"," #306238");
			$(this).css(" box-shadow","  0 0 1px #aaa, 0 0 8px #aaa");
		});
		*/
	});
	
}(jQuery));
//code for SHIFT+TAB from -enter zip code to cart(alert box)
function goButtonFocusAlert(e) {
	var TABKEY = 9;
	if (e.which == TABKEY) {
		if (e.shiftKey) {
			$jq('#locabar_popupcart_trigger').focus();
		} 
		else{
			$jq(".newzipgoAlert").focus();
		}
		e.preventDefault();
	} 
	//enter key on zip code textbox
	
}
//enter key event for zip code textbox
function goButtonFocus(e) {
	var keycode = (e.keyCode ? e.keyCode : e.which);
	
	if(keycode == '13'){
		if($jq("#newziptext").val()!="" && $jq("#newziptext").val().length == 5){
			//$jq('#messages').removeClass("open");
			//sendZip();
		}
		else{
			setTimeout(function(){
				$jq("#newziptext").focus();
			},500);
			
			//$jq("#messages").addClass("open");
		}		
		e.preventDefault();
	}
}
