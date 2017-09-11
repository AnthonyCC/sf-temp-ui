/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {

	var $ = fd.libs.$;
	var OAS_UPDATER = "OAS_UPDATER";
	
	function getIfr(){
		var ifr = document.getElementById('OAS_UPDATER');
		if(!ifr) {
			ifr = document.createElement('IFRAME');
			ifr.id=OAS_UPDATER;
			ifr.src="about:blank";
			document.body.appendChild(ifr);
		}

		return ifr;
	}
	
	function updateOAS(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query, OAS_POS) {
		var ifr = getIfr(),
			scriptUrl = OAS_SCRIPT_URL(OAS_url, OAS_sitepage, OAS_rns, OAS_listpos, OAS_query);
		

		if(ifr.contentDocument) {
			var html = common.updateOAS({
				scriptUrl:scriptUrl,
				OAS_POS:OAS_POS
			});
			// OMG, MY ADS ARE BURNING
			ifr.contentDocument.write(html);
			ifr.contentDocument.close();
		}
		
	}

	var done = function(pos,s) {	
		$('#'+pos).html(s);
	};
	
	Object.create(fd.common.signalTarget,{
		allowNull:{
			value:true
		},
		signal:{
			value:'departmentChanged'
		},
		callback:{
			value:function(data) {
				var siteUrl = "www.freshdirect.com/quickshop/";
				if(data) {
					siteUrl = siteUrl + data + '/';
				}
				try {
					updateOAS(OAS_url, siteUrl, OAS_rns, OAS_listpos, OAS_query, "QSTop");
				} catch (e) {
					// console.log('OAS update failed: '+e);
				}
			}
		}
	}).listen();
	
	
	fd.modules.common.utils.register("updateOAS", "done", done, fd);
	fd.modules.common.utils.register("quickshop.common", "updateOAS", updateOAS, fd);
}(FreshDirect));
