//for Internet Explorer browser version checking
function isIE(){
	var myNav = navigator.userAgent.toLowerCase();
	return (myNav.indexOf('msie') != -1) ? parseInt(myNav.split('msie')[1]) : false;
}

//for the sticky header
function scroll_header_fix(){
	if( $(this).scrollTop() > ($(window).height() / 2) ){
		$('header, nav').addClass("sticky");
	}else{
		$('header, nav').removeClass("sticky");
	}
}

function prepare_slideshow(ss_obj){
	var temp_img_src = "";

	if( $("#" + ss_obj.html_id).find(".slidesjs-container").length < 1 ){
		for(var i in ss_obj.slides){
			temp_img_src = ss_obj.img_dir + ss_obj.slides[i].img_src;

			$("#" + ss_obj.html_id ).append( "<figure><figcaption>"+ss_obj.slides[i].caption+"</figcaption><img src='"+temp_img_src+"' /></figure>" );
		}

		$("#" + ss_obj.html_id ).slidesjs({
			width: ss_obj.width,
			height: ss_obj.height
		});
	}
}

//to make sure the carousel looks fairly proper when the browser window is resized
function fullwindow_carousels_handler(){
	//establish final height/width for the carousel
	var carousel_height = $("#carousel_1").height();

	$("#carousel_1 .slidesjs-container, #carousel_1 .slidesjs-control").css("height", carousel_height + "px");
}

function elements_size_adjuster(){
	fullwindow_carousels_handler();
}

function zonenotification_zip_and_email(param_obj){
	$.ajax({
		url:'/api/locationhandler.jsp',
		data:{
			action:'futureZoneNotificationFdx',
			zipcode: param_obj.zip_text,
			email: param_obj.email_text
		}
	}).done(function(data){
		$("#locationhandler").fadeOut();
		
		$("#form_congratulations").fadeIn();
	});
}

function zonenotification_zip(zip_text){
	$.ajax({
		url:'/api/locationhandler.jsp',
		data:{
			action:'ifDeliveryZone',
			zipcode: zip_text
		}
	}).done(function(data){
		$("#zipcode_lh").val( $("#zipcode_zh").val() );
		
		$("#ziphandler").fadeOut();
		
		var form_next = "#locationhandler";
		
		if(data.trim() == "true"){
			form_next = "#we_deliver_to_you";
		}
		
		$(form_next).fadeIn();
	});
}

//for the zip code input tags
function numbersOnly(src){
	src.value = src.value.replace(/[A-Za-z]/g, '');
}

function form_enableDisable(formId, eORd){
	var eORd = (typeof eORd !== 'undefined')? eORd : false;

	if(eORd == false){
		$(formId).css("opacity", "0.5");
		
		$(formId).find("input").prop('disabled', true).css("cursor", "not-allowed");
		$(formId).find("button").prop('disabled', true).css("cursor", "not-allowed").attr("text2", $(formId).text().trim()).text("please wait...");
		
		console.log( $(formId) );
	}else{
		$(formId).css("opacity", "1");
		
		$(formId).find("input").prop('disabled', false).css("cursor", "default");
		$(formId).find("button").prop('disabled', false).css("cursor", "default").text( $(formId).attr("text2") );
	}
}

$(function(){
	elements_size_adjuster();
	
	$(window).resize(function(){
		scroll_header_fix();
		
		elements_size_adjuster();
	});
	
	$(window).scroll(function(){
		scroll_header_fix();
	});

	$('a[href^="#"]').on('click', function(e){
	    e.preventDefault();

	    var target = this.hash;
	    var $target = $(target);

		if($target.length > 0){
			$('html, body').stop().animate({
				'scrollTop': $target.offset().top
			}, 900, 'swing', function () {
				window.location.hash = target;
			});
		}
	});
	
	//this is just for step 1, to verify if the zip code is within a delivery zone
	$('form#ziphandler').submit(function(event){
		form_enableDisable( "#" + $(this).attr("id") );
		
		zonenotification_zip( $("#zipcode_zh").val().trim() );
		
		event.preventDefault();
	});
	
	//step 2a, if there is no zip code from function above, the form that this submits to is then seen and submits to have this code activate below
	$('form#locationhandler').submit(function(event){
		form_enableDisable( $(this).attr("id") );
		
		var param_obj = new Object();
		param_obj.zip_text = $("#zipcode_lh").val();
		param_obj.email_text = $("#email_lh").val();
		
		zonenotification_zip_and_email(param_obj);
		
		event.preventDefault();
	});
});

//html5 quickfix for Internet Explorer versions below 9.  probably should run last.
if (isIE () && isIE () < 9) {
	'article aside footer figure figcaption header nav section time'.replace(/\w+/g,function(n){document.createElement(n)});
}