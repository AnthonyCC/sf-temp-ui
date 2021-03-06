
if (typeof FreshDirect == "undefined" || !FreshDirect) {
    var FreshDirect = {};
}

(function() {
	var PhoneValidator = {};

	isDigit = function (c)
	{
		return ((c >= "0") && (c <= "9"));
	};
	
	min = function(a, b) {
		return a < b ? a : b;
	}

	normalize = function (s, search) {
		var nstr = "";
		for (var i = 0; i < s.length; i++)
			if (isDigit(s.charAt(i)) || (search && s.charAt(i) == '*'))
				nstr += s.charAt(i);

		return nstr;
	};

	isValid = function (s, search) {
		var digits = normalize(s, search);
		return digits.length == 10 || digits.indexOf("*") >= 0;
	};
	
	function FieldValidator(search) {
	}
	
	FieldValidator.prototype.onchange = function (params) {
		var elem = params[0];
		var search = params[1] || false;

		if($jq.trim(elem.value) == "" ||
				isValid(elem.value, search))
			elem.style.color = "black";
		else
			elem.style.color = "red";
	}

	FieldValidator.prototype.onblur = function (params) {
		var elem = params[0];
		var search = params[1] || false;

		var digits = normalize(elem.value, search);
		if ((digits.length >= 3 && (!search || digits.indexOf("*") == -1)) 
				|| (digits.length > 3 && search && digits.indexOf("*") == (digits.length - 1))) {
			var val = "(";
			val += digits.substr(0, min(digits.length, 3));
			val += ") ";
			val += digits.substr(3, min(digits.length - 3, 3));
			if (digits.length > 6) {
				val += "-";
				val += digits.substr(6);
			}
			elem.value = val;
		} else
			elem.value = digits;
	}
	
	PhoneValidator.registerFieldValidator = function (field, srch) {
		var search = srch === true;
		var validator = new FieldValidator();
		var params = [ field, search ];
		$jq(field).on('change keyup', validator.onchange.bind(null, params));
		$jq(field).blur(validator.onblur.bind(null, params));
	}

	PhoneValidator.register = function (field, search) {
		PhoneValidator.registerFieldValidator(field, search);
	}

	FreshDirect.PhoneValidator = PhoneValidator;
})();

