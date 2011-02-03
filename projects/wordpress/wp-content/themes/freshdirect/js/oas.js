OAS_protocol = 'http://';
if (document.location.href.substring(0, 5) == 'https') {
	OAS_protocol = 'https://';
}

// OAS_url = OAS_protocol + 'nyc1stam01.nyc1.freshdirect.com/RealMedia/ads/';
OAS_url = OAS_protocol + 'promo.freshdirect.com/RealMedia/ads/';
OAS_sitepage = 'blog.freshdirect.com';
// OAS_sitepage = 'www.freshdirect.com';
OAS_listpos = 'BlogSideNav';
// OAS_listpos = 'HPLeftTop';
OAS_query = '';
OAS_target = '';

OAS_rn = '001234567890';
OAS_rns = '1234567890';
OAS_rn = new String(Math.random());
OAS_rns = OAS_rn.substring(2, 11);

function OAS_RICH(pos) {
}

document.write('<scr' + 'ipt type="text/javascript" src="' + OAS_url
		+ 'adstream_mjx.ads/' + OAS_sitepage + '/1' + OAS_rns + '@'
		+ OAS_listpos + '?' + OAS_query + '"><\/script>');

document.write('');
function OAS_AD(pos) {
	OAS_RICH(pos);
}

