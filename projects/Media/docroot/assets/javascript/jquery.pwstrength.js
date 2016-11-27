/**
 * jquery.pwstrength http://matoilic.github.com/jquery.pwstrength
 *
 * @version v0.1.1
 * @author Mato Ilic <info@matoilic.ch>
 * @copyright 2013 Mato Ilic
 *
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 */
;(function($) {
    $.pwstrength = function(password) {
        var score = 0, length = password.length, upperCase, lowerCase, digits, nonAlpha;
        
        if(length == 0)
		{
			$('#pwindicator').hide();
		}
		else
		{
			$('#pwindicator').show();		
		}
        
        //4177 : Added to fix whitespace issue in Password strength
        length = password.trim().length;
        
        if(length < 6) score = 0;
        else if(length > 6) score += 10;
        else if(length < 12) score += 10;
        else score += 15;
        
        lowerCase = password.match(/[a-z]/g);
        if(lowerCase) score += 5;
        
        upperCase = password.match(/[A-Z]/g);
        if(upperCase) score += 5;
        
        if(upperCase && lowerCase) score += 5;
        
        digits = password.match(/\d/g);
        if(digits && digits.length > 1) score += 5;
        
        whiteSpace = password.match(/\s/);

		if(!whiteSpace){
     	nonAlpha = password.match(/\W/g);
		if(nonAlpha) score += (nonAlpha.length > 1) ? 15 : 10;
		}
        
        if(upperCase && lowerCase && digits && nonAlpha) score += 35;

        //4177 : Commented to fix whitespace issue
        //if(password.match(/\s/)) score += 15;

        //4177 : Added to fix whitespace issue in Password strength
		var whiteSpacelength = password.trim().length;
		if(whiteSpacelength == 0){
			score = 0;
		}

        if(score < 15) return 0;
        if(score < 20) return 1;
        if(score < 35) return 2;
        return 3;
    };
    
    function updateIndicator(event) {
        var strength = $.pwstrength($(this).val()), options = event.data, klass;
        klass = options.classes[strength];
        
        options.indicator.removeClass(options.indicator.data('pwclass'));
        options.indicator.data('pwclass', klass);
        options.indicator.addClass(klass);
        options.indicator.find(options.label).html(options.texts[strength]);
    }
    
    $.fn.pwstrength = function(options) {
        var options = $.extend({
            label: '.label',
            classes: ['pw-weak', 'pw-mediocre', 'pw-strong', 'pw-very-strong'],
            texts: ['weak', 'average', 'strong', 'very strong']
        }, options || {});
        options.indicator = $('#' + this.data('indicator'));
        
        return this.keyup(options, updateIndicator);
    };
})(jQuery);