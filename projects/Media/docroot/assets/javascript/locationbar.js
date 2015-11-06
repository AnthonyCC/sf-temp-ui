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
	
	
	$document.on('mouseup', function(event) {
    	if($(event.target).attr('id') != 'locabar_loginButton' && $(event.target).closest('#login_cont_formContent').length==0) {
        	$("#locabar_loginButton").removeClass("loginButtonTab");
        	$('#login_cont_formContent').hide();
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
		    			loginDropboxHtml += '<div class="fieldInputs"><input id="login_cont_formContent_email" name="userId" value="Email" data-deftext="Email" class="ccc" /></div>';
		    			loginDropboxHtml += '<div class="fieldInputs"><input id="login_cont_formContent_password" name="password" value="Password" data-deftext="Password" class="ccc" type="text" /></div>';
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
	    				if(data.message =="TcAgreeFail"){
	    					 FreshDirect.components.ifrPopup.open({ url: '/registration/tcaccept_lite.jsp', width: 400, height: 400});
	    				}else{
	    				if (data.hasOwnProperty('successPage') && data.successPage != '' && data.successPage != null) {
	    					window.location = data.successPage;
	    				} else {
	    					//refresh
	    					window.location = window.location;
	    				}
	    				
	                   	$("#locabar_loginButton").toggleClass("loginButtonTab");
	                   	$('#login_cont_formContent').toggle();
	    				}
	    			} else if(data.message == "CaptchaRedirect") {
    					window.location = '/login/login.jsp';
    				} else{
	    				$('#login_cont_formContent .errorMsg').show();
	    			}
					$('#login_cont_formContentForm_loggingIn').hide();
	        		form.data('submitting', false);
					$('#login_cont_formContentForm_signIn').toggleClass('imgButtonWhite imgButtonOrange');
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
	});
	
}(jQuery));
function goButtonFocus(e){
	 var TABKEY = 9;
    if(e.keyCode == TABKEY) {
   	 document.getElementById("newzipgo").focus();
    }
}

