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
	//will contain the image or video url, or it will contain the html content provided from OAS, should the slide be of type 'html'
	var if_content = "";
	
	//will always contain the desired content value defined in OAS
	var temp_end_slide_content = "";
	
	//below var will contain default image for subsequent slides beyond the first.  this is so that there is an OAS media impression for each slide only upon actual visiting, rather than all at once, no matter what
	var temp_empty_img = window.IMAGES_DIR + "/spacer.gif";
	
	//entire html content for the inner slide, (within the figure tag, besides the figcaption tag)
	var temp_innerslide_fullcontent = "";

	//create an html element with the given id/name from the ss_obj object, parameter: 'html_id', should the element not currently exist.
	if( $("#" + ss_obj.html_id).length == -1 ){
		//cross browser getting of the script block which has called this function.  
		var currentScript = document.currentScript || (function() {
		      var scripts = document.getElementsByTagName('script');
		      return scripts[scripts.length - 1];
		    })();
		
		//if the cross browser hack STILL did not work, then the new element will be placed after the body
		if( $(currentScript).length < 1 ){
			var placeafter = $("body");
		}else{
			//or (hopefully), place it after the calling 'script' block
			var placeafter = $(currentScript);
		}
		
		//place the new element in to the DOM
		placeafter.after("<div id='"+ss_obj.html_id+"'></div>");
	}
	
	//loop through the slide array object from OAS
	for(var i in ss_obj.slides){
		//if no one specifies a 'type' property for this slide, then default to 'img'
		if( ss_obj.slides[i].type == null ){
			ss_obj.slides[i].type = "img";
		}
		
		switch( ss_obj.slides[i].type ){
			case "html":
				//if this is the first slide, then load the expect content, otherwise, load the spacer content
				temp_end_slide_content = ss_obj.slides[i].content;
				if_content = ( parseFloat(i) == 0 )? temp_end_slide_content : temp_empty_img;
				
				//this will populate the inner content of the slide
				temp_innerslide_fullcontent = "<div id='div_"+ss_obj.html_id+"_"+i+"'>"+if_content+"</div><textarea id='hid_"+ss_obj.html_id+"_"+i+"' class='hidden_content'>"+temp_end_slide_content+"</textarea>";
			break;
			case "video":
			case "youtube":
			case "vimeo":
			case "iframe":
				temp_end_slide_content = ss_obj.slides[i].content;
				if_content = ( parseFloat(i) == 0 )? temp_end_slide_content : temp_empty_img;
				
				temp_innerslide_fullcontent = "<iframe id='media_"+ss_obj.html_id+"_"+i+"' src='"+if_content+"' rel='"+temp_end_slide_content+"' width='100%' height='100%' frameborder='0' allowfullscreen></iframe>";
			break;
			case "img":
			case "image":
			default:
				//the directory for the OAS images is included as part of the source if this is an 'img/image' type
				temp_end_slide_content = ss_obj.img_dir + ss_obj.slides[i].content;
				if_content = ( parseFloat(i) == 0 )? temp_end_slide_content : temp_empty_img;
				
				temp_innerslide_fullcontent = "<img id='media_"+ss_obj.html_id+"_"+i+"' src='"+if_content+"' rel='"+temp_end_slide_content+"' />";
			break;
		}

		//add the complete new slide to what will be the slide show
		$("#" + ss_obj.html_id ).append( "<figure><figcaption>"+ss_obj.slides[i].caption+"</figcaption>"+temp_innerslide_fullcontent+"</figure>" );
	}//end for loop

	//now actually generate the slideshow, stuff before was merely preperation html material
	$("#" + ss_obj.html_id ).slidesjs({
		width: ss_obj.width,
		height: ss_obj.height,
        play: {
			active: true,
			auto: true,
			interval: 5000,
			swap: true,
			//pauseOnHover: true,
			restartDelay: 2500
		},
        callback: {
			complete: function(number) { //occurs when the carousel slideshow changes slide to display
				// Passes slide number at end of animation
				var j = number - 1;
				
				//if this is an image or a video slide, then do the rest
				if( $( "#media_"+ss_obj.html_id+"_"+j ).length > 0 ){
					$( "#media_"+ss_obj.html_id+"_"+j ).attr("src", $( "#media_"+ss_obj.html_id+"_"+j ).attr("rel") ); //take it from the img or iframe 'rel' attribute
				}else if( $( "#div_"+ss_obj.html_id+"_"+j ).length > 0 ){ //else if this is html content
					$( "#media_"+ss_obj.html_id+"_"+j ).html( $( "#hid_"+ss_obj.html_id+"_"+j ).val() ); //take it from the sibling 'textarea' tag value
				}
			}
        }
	});
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

	//enable animated scrolling to hashtag links (provided that the 'a' tag with anchor targets actually exist)
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