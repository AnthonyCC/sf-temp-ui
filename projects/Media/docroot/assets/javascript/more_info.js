(function($){

	var locationHandlerAPI = '/api/locationhandler.jsp';

	var successHandler = function(response) {
		window.top.location.reload();
	};

	var resetHeight = function() {
		window.top.FDModalDialog.resizeY(parseInt($('body').css('height'),10)+25,'.partial-delivery-moreinfo');
	};

	var errorHandler = function(xhr){
		var data = $(xhr.responseText);
		$('.erroritem',data).each(function(){
			var $this = $(this);
			$('#'+$this.attr('data-errortype')+'-error').html($this.html());
		});
		resetHeight();
	};


	$(document).on('click','#chkaddress',function(e){
		var data = {
			action:'setMoreInfo',
			address1:$('#address1').val(),
			city:$('#city').val(),
			state:$('#state').val(),
			zipcode:$('#zipcode').val(),
			apartment:$('#apartment').val()
		};

		$.ajax({
			type:'POST',
			url:locationHandlerAPI,
			data:data,
			success:successHandler,
			error:errorHandler
		});

		e.preventDefault();
	});

	$(document).on('click','#continue',function(e){
		window.top.FDModalDialog.close('.partial-delivery-moreinfo .fd-dialog');
		e.preventDefault();
	});

	$(document).on('change','input',function(){
		$('.errorlabel').html("");
		resetHeight();
	});
})(jQuery)