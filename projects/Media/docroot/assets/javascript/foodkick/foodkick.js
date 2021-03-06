/*
 * BROWSER DETECTION METHODS/FUNCTIONS
 * */

//for Internet Explorer browser version checking
function isIE(){ /* void */
	var myNav = navigator.userAgent.toLowerCase();
	return (myNav.indexOf('msie') != -1) ? parseInt(myNav.split('msie')[1]) : false;
}

//older IE versions
function isIE9OrBelow(){ /* void */
	return /MSIE\s/.test(navigator.userAgent) && parseFloat(navigator.appVersion.split("MSIE")[1]) < 10;
}

//used not only for desktop Safari, but also for iPad and iPhone versions of Safari. NOTE: unlikely to apply to IOS versions of Chrome or Firefox
function isSafari(){ /* void */
	return (navigator.userAgent.indexOf('Safari') != -1 && navigator.userAgent.indexOf('Chrome') == -1);
}

//find out what Android version is present. Versions below 4.4.4 are what we are looking for for form validation capability (below 4.4.4 is not fully supported)
function getAndroidVersion(ua){ /* String */
	ua = (ua || navigator.userAgent).toLowerCase(); 
	var match = ua.match(/android\s([0-9\.]*)/);
	return match ? match[1] : false;
};
//typical use: parseFloat(getAndroidVersion());

/*
 * END BROWSER DETECTION METHODS/FUNCTIONS
 * */

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
	$("#" + ss_obj.html_id).show();
	$("#" + ss_obj.html_id).parents("section").show();
	
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
				
				temp_innerslide_fullcontent = "<div class=\"img_shadowhelper\"><img id='media_"+ss_obj.html_id+"_"+i+"' src='"+if_content+"' rel='"+temp_end_slide_content+"' /></div>";
			break;
		}

		//add the complete new slide to what will be the slide show
		$("#" + ss_obj.html_id ).append( "<figure><figcaption>"+ss_obj.slides[i].caption+"</figcaption>"+temp_innerslide_fullcontent+"</figure>" );
	}//end for loop
	
	//if there is only one slide, well, our problem is more tedious, but solvable
	if( ss_obj.slides.length == 1 ){
		$("#" + ss_obj.html_id ).html( '<div class="slidesjs-container slidesjs-single"><div class="slidesjs-control">'+ $("#" + ss_obj.html_id ).html() +'</div></div>' );
		
		return;
	}

	//now actually generate the slide show, stuff before was merely preparation html material
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
			complete: function(number) { //occurs when the carousel slide show changes slide to display
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

//new slideshow the angular js way.  REQUIRES: ss_obj.html_id must pertain to a pre-existing html element
function new_angularstrapousel(ss_obj){ /* object */
	angular.module('ui.bootstrap.demo').controller(ss_obj.html_id, function($scope){
		$scope.myInterval = ss_obj.slide_interval;
		$scope.noWrapSlides = false;
		var slides = $scope.slides = [];
		
		$scope.html_id = ss_obj.html_id;

		for(var i=0; i<ss_obj.slides.length; i++){
			slides.push({
				content: ss_obj.img_dir + ss_obj.slides[i]["content"],
				caption: ss_obj.slides[i]["caption"],
				type: ss_obj.slides[i]["type"]
			});
		}
	});
}

//to make sure the carousel looks fairly proper when the browser window is resized
function fullwindow_carousels_handler(){ /* void */
	//establish final height/width for the carousel
	var carousel_height = $("#carousel_1").height();
	
	//console.log( '$(".img_shadowhelper img").first().height() = ' + $(".img_shadowhelper img").first().height() );

	//$("#carousel_1 .slidesjs-container, #carousel_1 .slidesjs-control").css("height", carousel_height + "px");
}

function elements_size_adjuster(){ /* void */
	fullwindow_carousels_handler();
}

//executed from when the first form of 2 possible is executed.  verifies whether zipcode entered is within FDX territory
function zonenotification_zip(zip_text){ /* String */
	var form_id = "#ziphandler";
	var action_url = $(form_id).attr("action");
	
	$.ajax({
		url: action_url,
		async:false,
		data:{
			zipCheck: zip_text
		}
	}).done(function(data){
		$("#zipcode_lh").val( $("#zipcode_zh").val() );
		
		$(form_id).fadeOut();
		
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
	var form_id = "#locationhandler";
	var action_url = $(form_id).attr("action");
	
	$(form_id).addClass("form_disabled");
	
	$.ajax({
		url: action_url,
		data:{
			action:'futureZoneNotificationFdx',
			zipcode: param_obj.zip_text,
			email: param_obj.email_text
		}
	}).done(function(data){
		$(form_id).fadeOut();
		
		var form_next = "#form_congratulations";
		
		$(form_next).fadeIn();
	});
}

//for the zip code input tags
function numbersOnly(src){ /* String */
	src.value = src.value.replace(/[A-Za-z]/g, '');
}

function form_enableDisable(formId, eORd, seenNow){ /*String, Boolean, Boolean*/
	var eORd = (typeof eORd !== 'undefined')? eORd : false;
	
	//the submit button for the form
	var theButton = $(formId).find("button").first();
	var theButton_id = theButton.attr("id");

	if(eORd == false){
		//$(formId).css("opacity", "0.5");
		
		$(formId).addClass("form_disabled");
		
		$(formId).find("input").prop('disabled', true);
		//theButton.prop('disabled', true).css("cursor", "not-allowed").attr("text2", theButton.text().trim());
		theButton.attr("text2", theButton.text().trim());
		button_enableDisable( "#"+theButton_id, false);
		theButton.text("please wait...");
	}else{
		//make form fully and normally opaque
		$(formId).removeClass("form_disabled");
		
		if( !(typeof seenNow !== 'undefined' && seenNow == false) ){
			$(formId).show();
		}
		
		//re-enable the form elements
		//$(formId).find("input").prop('disabled', false).css("cursor", "default");
		$(formId).find("input").prop('disabled', false);
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
function reset_zip_forms(){ /* void */
	form_enableDisable('#ziphandler', true);
	$('#we_deliver_to_you').hide();
	$('#form_congratulations').hide();
	
	//$('#locationhandler').css("opacity", "1");
	
	/*$('#locationhandler input').each(function(){
		$(this).val("").text("").prop("disabled", false);
	})*/
	
	form_enableDisable('#locationhandler', true, false);
	$('#email_lh, #zipcode_zh').text("").val("").removeAttr("style");
}

function form_inputs_enable_lite(formId){ /* String */
	$(formId).find("input").prop('disabled', false);
	//$(formId).find("button").prop('disabled', false);
}

//APPDEV-4776
function go_away_mobile_menu_ur_drunk(){ /* void */
	$('#mobile_link_home').trigger('mouseleave');
	
	$("#mobile_dropdown_menu").hide();
}

//following 2 functions below help with page resizing
function PageShowHandler(){ /* void */
	form_inputs_enable_lite("#ziphandler", true);
	
	window.addEventListener('unload', UnloadHandler, false);
}

function UnloadHandler(){ /* void */
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
	
	//APPDEV-4777
	$("section").on( "click hover rollover mouseenter", function() {
		$('#mobile_link_home').trigger('mouseleave');
	});

	//enable animated scrolling to hashtag links (provided that the 'a' tag with anchor targets actually exist)
	$('a[href*="#"]').on('click', function(e){
		if( window.location.href.indexOf("/index.jsp") != -1 ){ //only if this is the landing page
			e.preventDefault();

			var target = this.hash;
			var $target = $(target);

			if($target.length > 0){
				$('html, body').stop().animate({
					'scrollTop': $target.offset().top,
					'start': function(){
						//console.log("this animation started");
					}
				}, 900, 'swing', function(){
					window.location.hash = target;
					
					//hide this dumb iphone menu when it is done scrolling someplace for a hash link.  needed in mobile
					$(".mobile_dropdown").hide();
					
					//signal to the hamburger button that it should not think of itself as being hovered over.  needed in mobile
					$('#mobile_link_home').trigger('mouseleave');
				});
			}//end if $target.length > 0
		}//end if this is only if this is the landing page
	});
	
	$('#mobile_link_home').on( "click", function(){
		if( $("#mobile_dropdown_menu").is(':visible') ){
			go_away_mobile_menu_ur_drunk();
		}else{
			$("#mobile_dropdown_menu").removeAttr("style");
		}
	});
	
	$('#mobile_dropdown_menu, #mobile_dropdown_menu .mobile_link').on( "click", function() {
		go_away_mobile_menu_ur_drunk();
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
		
		//if there are errors in this form, tell them to get out of here and fix it
		if( $(this).find("input.error").length > 0 ){
			return;
		}
		
		form_enableDisable( "#" + $(this).attr("id") );
		
		var param_obj = new Object();
		param_obj.zip_text = $("#zipcode_lh").val();
		param_obj.email_text = $("#email_lh").val();
		
		zonenotification_zip_and_email(param_obj);
		
		button_enableDisable('#submit_locationhandler', false);
	});

	//this is so that the customer does not start out seeing error messages when typing in the email or whatever, APPDEV-4969
	var show_locationerrors = false;

	//this should take care of any loose end browsers that lack html5 form validation 
	$('form#locationhandler').validate({
		errorElement: "label",
		onkeyup: function(element, error){ /*APPDEV-4969*/
			
			//the following if bracket allows for validation while typing, but no nasty reds or error messages
			if(! $('form#locationhandler').valid() ){

				if( show_locationerrors == true ){
					$("#"+error.currentTarget.name+"-error").show();
					$("#"+error.currentTarget.name).removeClass("niceerror");
				}else{
					$("#"+error.currentTarget.name+"-error").hide();
					$("#"+error.currentTarget.name).addClass("niceerror");
				}
			}
		},
		onfocusout: function(element, error){ /*APPDEV-4969*/
			//allows for this element to have its error message below to show, along with red error text and border
			show_locationerrors = true;

			if( this.numberOfInvalids() > 0 ){
				$("#"+error.currentTarget.name+"-error").show();
				$("#"+error.currentTarget.name).removeClass("niceerror");
			}
		},

		errorPlacement: function(error, element){
			error.insertAfter(element); /* ready the form elements with a error element after them */
		},

		showErrors: function(errorMap, errorList) {
			//show those red errors
			this.defaultShowErrors();
			
			//console.log( "this.numberOfInvalids() = ", this.numberOfInvalids() );
			
			//enable or disable the submit button when appropriate
			if( this.numberOfInvalids() < 1 ){
				button_enableDisable('#submit_locationhandler', true);
			}else{
				button_enableDisable('#submit_locationhandler', false);
			}
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
	//$('form#ziphandler input, form#locationhandler input, #lost_password input, #update_change_password input').on('keyup', function(){ // fires on every keyup & blur
	/*$('form#ziphandler input, form#locationhandler input, #lost_password input, #update_change_password input').on('blur', function(){ 
		var button_id = $(this).parents("form").find('button[type="submit"]').attr("id");

		if( $(this).valid() && ($(this).parents("form").find("input.error").length < 1) ){ // checks form for validity, including with sibling form elements
			button_enableDisable( "#"+button_id, true);
		}else{
			button_enableDisable( "#"+button_id, false);
		}
	});	*/

	//do soft validation for the first form with the zip here
	$('form#ziphandler input').on('keyup', function(){
		var button_id = $(this).parents("form").find('button[type="submit"]').attr("id");
		
		var value = $(this).val();
		
		if( /^\d{5}$/.test(value) ){
			button_enableDisable( "#"+button_id, true);
		}else{
			button_enableDisable( "#"+button_id, false);
		}
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