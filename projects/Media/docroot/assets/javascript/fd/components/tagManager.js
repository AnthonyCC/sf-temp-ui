var GTMID = FreshDirect && FreshDirect.gtm && FreshDirect.gtm.key;
var dataLayer = window.dataLayer || [];
var FreshDirect = FreshDirect || {};

// load google tag manager only in non-masquerade context
(function(fd) {
  var loadGTM = function (w,d,s,l,i) {
    w[l]=w[l]||[];
    w[l].push({
        'gtm.start':new Date().getTime(),
        event:'gtm.js'
    });
    
    var f=d.getElementsByTagName(s)[0],
        j=d.createElement(s),
        dl=l!='dataLayer'?'&l='+l:'';
    
    j.async=true;
    j.src='https://www.googletagmanager.com/gtm.js?id='+i+dl;
    f.parentNode.insertBefore(j,f);
  };

  if (GTMID && (!fd || !fd.user || !fd.user.masquerade)) {
    loadGTM(window, document, 'script', 'dataLayer', GTMID);
  }

}(FreshDirect));

// check for login or signup statuses
(function(fd) {
  if (fd.utils.readCookie('hasJustLoggedIn')) {
    fd.utils.eraseCookie('hasJustLoggedIn');
    dataLayer.push({
      'event': 'user-login'
    });
  }

  if (fd.utils.readCookie('hasJustSignedUp')) {
    fd.utils.eraseCookie('hasJustSignedUp');
    dataLayer.push({
      'event': 'user-signup'
    });
  }
}(FreshDirect));

// custom analytics events
(function(fd) {
  var DISPATCHER = fd.common.dispatcher;

  var getPageType = function () {
	  var defaultValue = 'DEFAULT';
	  try {
		  var params = fd.utils.getParameters(),
		    pId = params.productId || null,
			pType = params.pageType || null;
		
		  return pId ? 'pdp' : pType || defaultValue;
	  } catch (e) {
		  return defaultValue;
	  }
  };

  var atcSuccess = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'atcResult'
    },
    callback: {
      value: function (data) {
        data.forEach(function (atcItemInfo) {
          if (atcItemInfo.status === 'SUCCESS') {
            dataLayer.push({
              'event': 'ATC-success',
              'productId': atcItemInfo.productId,
              'pageType': getPageType()
            });
          }
        });
      }
    }
  });

  atcSuccess.listen();
}(FreshDirect));
