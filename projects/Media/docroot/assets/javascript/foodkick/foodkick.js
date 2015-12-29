//for Internet Explorer browser version checking
function isIE(){
	var myNav = navigator.userAgent.toLowerCase();
	return (myNav.indexOf('msie') != -1) ? parseInt(myNav.split('msie')[1]) : false;
}

function isIE9OrBelow(){
	return /MSIE\s/.test(navigator.userAgent) && parseFloat(navigator.appVersion.split("MSIE")[1]) < 10;
}

//used not only for desktop Safari, but also for iPad and iPhone
function isSafari(){
	return (navigator.userAgent.indexOf('Safari') != -1 && navigator.userAgent.indexOf('Chrome') == -1);
}

//find out what Android version is present. Versions below 4.4.4 are what we are looking for for form validation capability (below 4.4.4 is not fully supported)
function getAndroidVersion(ua){ /* String */
	ua = (ua || navigator.userAgent).toLowerCase(); 
	var match = ua.match(/android\s([0-9\.]*)/);
	return match ? match[1] : false;
};
//parseFloat(getAndroidVersion());

//detects basic support for whether the calling page has form validation support.
function hasFormValidation(){ /* void */
	return (typeof document.createElement( 'input' ).checkValidity == 'function'); 
};

function canValidateFields(){ /* void */
    var result = typeof document.createElement( 'input' ).checkValidity == 'function';
    if (result) {
        for (var i = 0; i < arguments.length; i++) {
            var element = document.getElementById(arguments[i]);
            if (!element.checkValidity() && (!element.validationMessage || element.validationMessage === null || element.validationMessage === '')) {
                return false;
            }
        }
    }
    return result;
}

//document.getElementById( 'validation' ).innerHTML = hasFormValidation();

//for the sticky header
function scroll_header_fix(){ /* void */
	if( $(this).scrollTop() > ($(window).height() / 2) ){
		$('header, nav').addClass("sticky");
	}else{
		$('header, nav').removeClass("sticky");
	}
}

function prepare_slideshow(ss_obj){ /* Object */
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
	
	//make sure that the slideshow holder shows the slideshow container, just in case
	$("#" + ss_obj.html_id).css("display", "block");
	$("#" + ss_obj.html_id).parents("section").css("display", "block");
	
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
	
	//if there is only one slide, well, our problem is more tedious, but solvable
	if( ss_obj.slides.length == 1 ){
		$("#" + ss_obj.html_id ).html(  '<div class="slidesjs-container slidesjs-single"><div class="slidesjs-control">'+ $("#" + ss_obj.html_id ).html() +'</div></div>'  );
		
		return;
	}

	//now actually generate the slideshow, stuff before was merely preperation html material
	$("#" + ss_obj.html_id ).slidesjs({
		width: ss_obj.width,
		height: ss_obj.height,
        play: {
			active: true,
			//auto: true,
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
function fullwindow_carousels_handler(){ /* void */
	//establish final height/width for the carousel
	var carousel_height = $("#carousel_1").height();

	$("#carousel_1 .slidesjs-container, #carousel_1 .slidesjs-control").css("height", carousel_height + "px");
}

function elements_size_adjuster(){ /* void */
	fullwindow_carousels_handler();
}

//executed from when the first form of 2 possible is executed.  verifies whether zipcode entered is within FDX territory
function zonenotification_zip(zip_text){ /* String */
	$.ajax({
		//url:'/api/locationhandler.jsp',
		url:'/foodkick/ajax/fdx_zone.jsp',
		async:false,
		/*data:{
			action:'ifDeliveryZone',
			noMobile: 'FALSE',
			zipcode: zip_text
		}*/
		data:{
			zipCheck: zip_text
		}
	}).done(function(data){
		$("#zipcode_lh").val( $("#zipcode_zh").val() );
		
		$("#ziphandler").fadeOut();
		
		var form_next = "#locationhandler";
		
		//if(data.trim() == "true"){
		if(data.indexOf("FDX") > -1){
			form_next = "#we_deliver_to_you";
		}
		
		$(form_next).fadeIn();
	});
}

//executed for second of 2 possible forms to add someone to the records
function zonenotification_zip_and_email(param_obj){ /* Object */
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

//for the zip code input tags
function numbersOnly(src){ /* String */
	src.value = src.value.replace(/[A-Za-z]/g, '');
}

function form_enableDisable(formId, eORd){ /*String, Boolean*/
	var eORd = (typeof eORd !== 'undefined')? eORd : false;
	
	//the submit button for the form
	var theButton = $(formId).find("button").first();
	var theButton_id = theButton.attr("id");

	if(eORd == false){
		$(formId).css("opacity", "0.5");
		
		$(formId).find("input").prop('disabled', true).css("cursor", "not-allowed");
		//theButton.prop('disabled', true).css("cursor", "not-allowed").attr("text2", theButton.text().trim());
		theButton.attr("text2", theButton.text().trim());
		button_enableDisable( "#"+theButton_id, false);
		theButton.text("please wait...");
	}else{
		//make form fully and normally opaque
		$(formId).css("opacity", "1").css("display", "block");
		
		//re-enable the form elements
		$(formId).find("input").prop('disabled', false).css("cursor", "default");
		//theButton.prop('disabled', false).css("cursor", "default").text( theButton.attr("text2") );
		button_enableDisable( "#"+theButton_id, true);
		theButton.text( theButton.attr("text2") );
	}
}

function button_enableDisable(buttonId, eORd){ /*String, Boolean*/
	if( eORd == true ){ // if this button SHOULD be enabled...
		$(buttonId).prop('disabled', false).removeClass("button_disabled"); // enables button
	}else{ // or if not
		$(buttonId).prop('disabled', 'disabled').addClass("button_disabled");  // disables button
	}
}

//reset the zipcode / email form flow
function reset_zip_forms(){
	form_enableDisable('#ziphandler', true);
	$('#we_deliver_to_you').css('display', 'none');
	
	$("#zipcode_zh").val("").text("");
}

function form_inputs_enable_lite(formId){
	$(formId).find("input").prop('disabled', false);
	//$(formId).find("button").prop('disabled', false);
}

//following 2 functions below help with page resizing
function PageShowHandler(){
	form_inputs_enable_lite("#ziphandler", true);
	
	window.addEventListener('unload', UnloadHandler, false);
}

function UnloadHandler(){
	form_inputs_enable_lite("#ziphandler", true);
	
	//enable button here
	window.removeEventListener('unload', UnloadHandler, false);
}

window.addEventListener('pageshow', PageShowHandler, false);
window.addEventListener('unload', UnloadHandler, false);

$(function(){
	elements_size_adjuster();
	scroll_header_fix();
	
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
				'scrollTop': $target.offset().top,
				'start': function(){
					console.log("this animation started");
				}
			}, 900, 'swing', function () {
				window.location.hash = target;
				
				//hide this dumb iphone menu when it is done scrolling someplace for a hash link.  needed in mobile
				$(".mobile_dropdown").css("display", "none");
				
				//signal to the hamburger button that it should not think of itself as being hovered over.  needed in mobile
				$('#mobile_link_home').trigger('mouseleave');
				
				$('#mobile_link_home').bind(
	                "mouseenter",
	                function( event ){
	                    //now remove that style attribute of big purple so that it can be seen again when hamburger helper is clicked again.  needed in mobile
	                	$(".mobile_dropdown").removeAttr("style");
	                }
	            );
			});
		}
	});
	
	//this is just for step 1, to verify if the zip code is within a delivery zone
	$('form#ziphandler').submit(function(event){
		//stop this form from submitting the normal way.  Ajax is to be used
		event.preventDefault();
		
		//disable this form
		form_enableDisable( "#" + $(this).attr("id") );
		
		zonenotification_zip( $("#zipcode_zh").val().trim() );
	});
	
	//step 2a, if there is no zip code from function above, the form that this submits to is then seen and submits to have this code activate below
	$('form#locationhandler').submit(function(event){
		event.preventDefault();
		
		form_enableDisable( "#" + $(this).attr("id") );
		
		var param_obj = new Object();
		param_obj.zip_text = $("#zipcode_lh").val();
		param_obj.email_text = $("#email_lh").val();
		
		zonenotification_zip_and_email(param_obj);
		
		button_enableDisable('#submit_locationhandler', false);
	});

	var formArray = new Array( );
	
	//this should take care of any loose end browsers that lack html5 form validation 
	$('form#ziphandler, form#locationhandler').validate({
		onkeyup: false,
		errorElement: "div",

		errorPlacement: function(error, element){
			error.insertBefore(element);
		}
	});
	
	//assisting above validation
	jQuery.validator.addMethod("zipcode", function(value, element){
		return this.optional(element) || /\d{5}-\d{4}$|^\d{5}$/.test(value)
	}, "The specified US ZIP Code is invalid");
	
	//assisting above validation for email addresses.  this is much better than the default email pattern that is used by the validate plugin
	jQuery.validator.addMethod("custom_email", function(value, element){
		return this.optional(element) || /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value)
	}, "The specified e-mail is invalid");
	
	//enable the submit button when everything is good, relating to above validation
	//$('form#ziphandler input, form#locationhandler input').on('keyup blur', function(){ // fires on every keyup & blur
	//$('form#ziphandler input, form#locationhandler input, #lost_password input, #update_change_password input').on('blur mouseleave', function(){ // fires on every blur and mouseleave
	$('form#ziphandler input, form#locationhandler input, #lost_password input, #update_change_password input').on('keyup', function(){ // fires on every keyup & blur
		var button_id = $(this).siblings("button").first().attr("id");

		//if( $(this).val().length > 0 ){
			if( $(this).valid() ){ // checks form for validity
				button_enableDisable( "#"+button_id, true);
			}else{
				button_enableDisable( "#"+button_id, false);
			}
		//}
	});	
	
	//lets make placeholders for fields work in IE 9 and maybe below
	$('[placeholder]').focus(function(){
		var input = $(this);
		if (input.val() == input.attr('placeholder')) {
			if( isIE9OrBelow() ){
				input.val('');
				input.removeClass('placeholder');
			}
		}
	}).blur(function(){
		var input = $(this);
		if (input.val() == '' || input.val() == input.attr('placeholder')) {

			if( isIE9OrBelow() ){
				input.addClass('placeholder');
				input.val(input.attr('placeholder'));
				
				if( input.attr("type") == "password" ){
					input.attr("type", "text");
				}
			}
		}
	}).blur();
});