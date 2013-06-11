/*global jQuery, popup, FDModalDialog*/
(function($){
	var console = console || { log:function(){} };
	
	var $document = $(document),
		locationHandlerAPI = '/api/locationhandler.jsp',
		locationMessages = document.getElementById('location-messages');
	
	
	var setAddress = function(zip,address){
		if(address.length) {
			address='('+address+')';
		}
		$('#locationbar .address .text').html(address);
		$('#locationbar .address .zipcode').html(zip);
	}, successHandler = function(data){
		window.location.reload();
	}, errorHandler = function(data){
		var messages;
		$(locationMessages).html(data);
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
		
		if(!/(^\d{5}$)/.test(text)) {
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
		if($(e.target).hasClass('moreinfo')) {
			FDModalDialog.close('.partial-delivery-moreinfo .fd-dialog');
			FDModalDialog.openUrl('/shared/locationbar/more_info.jsp',' ',700,250,'partial-delivery-moreinfo');
		}		
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
  	console.log(kc);
  	if( (kc<48 || kc>57) && keys.inArray(kc)===false && (kc<96 || kc>105) ) {
  		e.preventDefault();
  	}
  });
  
  
	$document.on('click','.delivery-popuplink',function(e){
		popup('/help/delivery_zones.jsp','large');
		e.preventDefault();
	});
	
	$document.on('click','#location-submit',function(e){
		var email = $('#location-email').val(),
			$form = $('#messages .nodeliver-form form');
		
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

