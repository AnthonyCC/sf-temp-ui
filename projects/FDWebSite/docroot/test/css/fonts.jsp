<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<!DOCTYPE html>
<html>
<head>
	<title>WebFont Tests</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" media="all" href="/assets/css/common/custom_fonts.css" />
	<jwr:style src="/assets/css/test/css/fonts.css" media="all" />
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>

</head>
<body>
	<h2>Custom fonts comparison. All lines should match in modern browsers.</h2>
	<div id="content"></div>

	<script>
		(function($){
			var content = [];
			[
				'BrandonTextWeb-Regular', 'BrandonTextWeb-Medium', 'BrandonTextWeb-Bold',
				'BebasRegular',	'BebasNeueRegular',
				'Eagle-Light', 'EagleRegular', 'Eagle-Bold', 'Eagle-Bold-book', 'EagleBook',
				'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-100', 'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-100italic',
				'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-200', 'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-200italic',
				'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-300', 'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-300italic',
				'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-regular', 'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-italic',
				'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-500', 'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-500italic',
				'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-600', 'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-600italic',
				'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-700', 'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-700italic',
				'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-800', 'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-800italic',
				'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-900', 'montserrat-v12-cyrillic-ext_latin_cyrillic_latin-ext-900italic',
				'TradeGothic', 'TradeGothicBold'
			].forEach(function(fontName) {
				content.push(
					'<div class="match">'+
						'<div class="fontname">'+fontName+'</div>'+
						'<div class="woff '+fontName+'-woff"></div>'+
						'<div class="woff2 '+fontName+'-woff2"></div>'+
						'<div class="auto '+fontName+'-auto"></div>'+
					'</div>'
				);
			});
			$('#content').append(content.join(''));
		}(jQuery));
	</script>
</body>
</html>