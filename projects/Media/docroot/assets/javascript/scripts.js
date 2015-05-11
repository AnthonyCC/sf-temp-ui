$( document ).ready(function() {


/*##########################################################################
	Tell jquery.hint.js to take the "title" attribute of all text input 
	fields and use it to create hint text for that field. The hint text 
	will then hide/show on focus/blur using the jquery.hint.js script.
############################################################################*/

	$(function(){ 
		$('input[title!=""]').hint();
	});

/*##########################################################################
	Validate email field to show example of error state on text input fields
############################################################################*/

	$('#input-error').focus(function() {
	
		$('#input-error').blur(function() {	
			var errCount = $(this).val();
			var emailPattern = new RegExp(/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i);
				

			if ( errCount.length > 0 && errCount.match(emailPattern)) {			
					$('#input-error').removeClass('input-error');
					$('#input-error').addClass('input-success');
					$('.input-error-intructions').css('display', 'none');
			} else {
				
				$('#input-error').removeClass('input-success');
				$('#input-error').addClass('input-error');
				$('.input-error-intructions').css('display', 'inline-block');

						$('#input-error').keyup(function() {	
							var errCount = $(this).val();
							var emailPattern = new RegExp(/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i);
							if ( errCount.length > 0 && errCount.match(emailPattern)) {									
									$('#input-error').removeClass('input-error');
									$('#input-error').addClass('input-success');
									$('.input-error-intructions').css('display', 'none');
							} else {							
								$('#input-error').removeClass('input-success');
								$('#input-error').addClass('input-error');
								$('.input-error-intructions').css('display', 'inline-block');
							}
						});
			}
		
	});
		
	});
	

	
/*##########################################################################
	When a password field gains focus, turn the field type to "password".
	When the field loses focus, check to see if it is empty -- if so, change
	the field type back to "text" (so the text hint displays).
############################################################################*/
	
	
	$('.password input').keyup(function() {
		
		var pswd = $(this).val();
		
		//validate length
		if ( pswd.length < 6 ) {
			$('#pw-length').removeClass('valid').addClass('invalid');
		} else {
			$('#pw-length').removeClass('invalid').addClass('valid');
		}
		
		//validate letter
		if ( pswd.match(/[A-z]/) ) {
			$('#pw-letter').removeClass('invalid').addClass('valid');
		} else {
			$('#pw-letter').removeClass('valid').addClass('invalid');
		}
		
		//validate capital letter
		if ( pswd.match(/[A-Z]/) ) {
			$('#pw-capital').removeClass('invalid').addClass('valid');
		} else {
			$('#pw-capital').removeClass('valid').addClass('invalid');
		}
		
		//validate number
		if ( pswd.match(/\d/) ) {
			$('#pw-number').removeClass('invalid').addClass('valid');
		} else {
			$('#pw-number').removeClass('valid').addClass('invalid');
		}
		
		//validation Added for Special Chars
		if ( pswd.match(/[!,@,#,$,%,^,&,*,?,_,~]/) ) {
			$('#pw-special').removeClass('invalid').addClass('valid');
		} else {
			$('#pw-special').removeClass('valid').addClass('invalid');
		}
		
		if (pswd.length >= 6 ) {
			
			$(this).blur(function() {
				$(this).removeClass('input-error');
			});
		}
		else {
			$(this).blur(function() {
				$(this).addClass('input-error');
			});			
		}
		
		
	}).focus(function() {
		
		$(this).prop('type', 'password');		
		$('.password-hinter').css('display', 'block');
		$('.password-instructions').animate({
		  top: '5px'
		}, 120);

	}).blur(function() {
		if( !this.value ) {
			$(this).prop('type', 'text');		
		}	
		$('.password-instructions').animate({
		  top: '160px'
		}, 120, function() {
			$('.password-hinter').css('display', 'none');
		});
		
	})


}); /* END DOCUMENT READY */