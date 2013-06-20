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
				pattern=/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
		
		if(pattern.match(email)) {
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
	
	
}(jQuery));

