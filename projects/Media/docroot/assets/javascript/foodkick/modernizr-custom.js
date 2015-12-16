/*! modernizr 3.2.0 (Custom Build) | MIT *
 * http://modernizr.com/download/?-cssanimations-fontface-inlinesvg-smil-svg-svgasimg-svgclippaths-svgfilters-svgforeignobject-prefixed-prefixedcss-prefixedcssvalue !*/
!function(e,t,n){function r(e,t){return typeof e===t}function i(){var e,t,n,i,o,s,a;for(var l in S)if(S.hasOwnProperty(l)){if(e=[],t=S[l],t.name&&(e.push(t.name.toLowerCase()),t.options&&t.options.aliases&&t.options.aliases.length))for(n=0;n<t.options.aliases.length;n++)e.push(t.options.aliases[n].toLowerCase());for(i=r(t.fn,"function")?t.fn():t.fn,o=0;o<e.length;o++)s=e[o],a=s.split("."),1===a.length?Modernizr[a[0]]=i:(!Modernizr[a[0]]||Modernizr[a[0]]instanceof Boolean||(Modernizr[a[0]]=new Boolean(Modernizr[a[0]])),Modernizr[a[0]][a[1]]=i),y.push((i?"":"no-")+a.join("-"))}}function o(e){var t=_.className,n=Modernizr._config.classPrefix||"";if(x&&(t=t.baseVal),Modernizr._config.enableJSClass){var r=new RegExp("(^|\\s)"+n+"no-js(\\s|$)");t=t.replace(r,"$1"+n+"js$2")}Modernizr._config.enableClasses&&(t+=" "+n+e.join(" "+n),x?_.className.baseVal=t:_.className=t)}function s(e){return e.replace(/([a-z])-([a-z])/g,function(e,t,n){return t+n.toUpperCase()}).replace(/^-/,"")}function a(e){return e.replace(/([A-Z])/g,function(e,t){return"-"+t.toLowerCase()}).replace(/^ms-/,"-ms-")}function l(){return"function"!=typeof t.createElement?t.createElement(arguments[0]):x?t.createElementNS.call(t,"http://www.w3.org/2000/svg",arguments[0]):t.createElement.apply(t,arguments)}function f(e,t){if("object"==typeof e)for(var n in e)O(e,n)&&f(n,e[n]);else{e=e.toLowerCase();var r=e.split("."),i=Modernizr[r[0]];if(2==r.length&&(i=i[r[1]]),"undefined"!=typeof i)return Modernizr;t="function"==typeof t?t():t,1==r.length?Modernizr[r[0]]=t:(!Modernizr[r[0]]||Modernizr[r[0]]instanceof Boolean||(Modernizr[r[0]]=new Boolean(Modernizr[r[0]])),Modernizr[r[0]][r[1]]=t),o([(t&&0!=t?"":"no-")+r.join("-")]),Modernizr._trigger(e,t)}return Modernizr}function u(e,t){return!!~(""+e).indexOf(t)}function c(e,t){return function(){return e.apply(t,arguments)}}function d(e,t,n){var i;for(var o in e)if(e[o]in t)return n===!1?e[o]:(i=t[e[o]],r(i,"function")?c(i,n||t):i);return!1}function p(){var e=t.body;return e||(e=l(x?"svg":"body"),e.fake=!0),e}function g(e,n,r,i){var o,s,a,f,u="modernizr",c=l("div"),d=p();if(parseInt(r,10))for(;r--;)a=l("div"),a.id=i?i[r]:u+(r+1),c.appendChild(a);return o=l("style"),o.type="text/css",o.id="s"+u,(d.fake?d:c).appendChild(o),d.appendChild(c),o.styleSheet?o.styleSheet.cssText=e:o.appendChild(t.createTextNode(e)),c.id=u,d.fake&&(d.style.background="",d.style.overflow="hidden",f=_.style.overflow,_.style.overflow="hidden",_.appendChild(d)),s=n(c,e),d.fake?(d.parentNode.removeChild(d),_.style.overflow=f,_.offsetHeight):c.parentNode.removeChild(c),!!s}function m(t,r){var i=t.length;if("CSS"in e&&"supports"in e.CSS){for(;i--;)if(e.CSS.supports(a(t[i]),r))return!0;return!1}if("CSSSupportsRule"in e){for(var o=[];i--;)o.push("("+a(t[i])+":"+r+")");return o=o.join(" or "),g("@supports ("+o+") { #modernizr { position: absolute; } }",function(e){return"absolute"==getComputedStyle(e,null).position})}return n}function h(e,t,i,o){function a(){c&&(delete z.style,delete z.modElem)}if(o=r(o,"undefined")?!1:o,!r(i,"undefined")){var f=m(e,i);if(!r(f,"undefined"))return f}for(var c,d,p,g,h,v=["modernizr","tspan"];!z.style;)c=!0,z.modElem=l(v.shift()),z.style=z.modElem.style;for(p=e.length,d=0;p>d;d++)if(g=e[d],h=z.style[g],u(g,"-")&&(g=s(g)),z.style[g]!==n){if(o||r(i,"undefined"))return a(),"pfx"==t?g:!0;try{z.style[g]=i}catch(w){}if(z.style[g]!=h)return a(),"pfx"==t?g:!0}return a(),!1}function v(e,t,n,i,o){var s=e.charAt(0).toUpperCase()+e.slice(1),a=(e+" "+R.join(s+" ")+s).split(" ");return r(t,"string")||r(t,"undefined")?h(a,t,i,o):(a=(e+" "+b.join(s+" ")+s).split(" "),d(a,t,n))}function w(e,t,r){return v(e,n,n,t,r)}var y=[],S=[],C={_version:"3.2.0",_config:{classPrefix:"",enableClasses:!0,enableJSClass:!0,usePrefixes:!0},_q:[],on:function(e,t){var n=this;setTimeout(function(){t(n[e])},0)},addTest:function(e,t,n){S.push({name:e,fn:t,options:n})},addAsyncTest:function(e){S.push({name:null,fn:e})}},Modernizr=function(){};Modernizr.prototype=C,Modernizr=new Modernizr,Modernizr.addTest("svg",!!t.createElementNS&&!!t.createElementNS("http://www.w3.org/2000/svg","svg").createSVGRect),Modernizr.addTest("svgfilters",function(){var t=!1;try{t="SVGFEColorMatrixElement"in e&&2==SVGFEColorMatrixElement.SVG_FECOLORMATRIX_TYPE_SATURATE}catch(n){}return t});var _=t.documentElement,x="svg"===_.nodeName.toLowerCase();Modernizr.addTest("inlinesvg",function(){var e=l("div");return e.innerHTML="<svg/>","http://www.w3.org/2000/svg"==("undefined"!=typeof SVGRect&&e.firstChild&&e.firstChild.namespaceURI)});var E={}.toString;Modernizr.addTest("svgclippaths",function(){return!!t.createElementNS&&/SVGClipPath/.test(E.call(t.createElementNS("http://www.w3.org/2000/svg","clipPath")))}),Modernizr.addTest("svgforeignobject",function(){return!!t.createElementNS&&/SVGForeignObject/.test(E.call(t.createElementNS("http://www.w3.org/2000/svg","foreignObject")))}),Modernizr.addTest("smil",function(){return!!t.createElementNS&&/SVGAnimate/.test(E.call(t.createElementNS("http://www.w3.org/2000/svg","animate")))});var T="Moz O ms Webkit",b=C._config.usePrefixes?T.toLowerCase().split(" "):[];C._domPrefixes=b;var N=function(e,t){var n=!1,r=l("div"),i=r.style;if(e in i){var o=b.length;for(i[e]=t,n=i[e];o--&&!n;)i[e]="-"+b[o]+"-"+t,n=i[e]}return""===n&&(n=!1),n};C.prefixedCSSValue=N;var R=C._config.usePrefixes?T.split(" "):[];C._cssomPrefixes=R;var P=function(t){var r,i=prefixes.length,o=e.CSSRule;if("undefined"==typeof o)return n;if(!t)return!1;if(t=t.replace(/^@/,""),r=t.replace(/-/g,"_").toUpperCase()+"_RULE",r in o)return"@"+t;for(var s=0;i>s;s++){var a=prefixes[s],l=a.toUpperCase()+"_"+r;if(l in o)return"@-"+a.toLowerCase()+"-"+t}return!1};C.atRule=P;var O;!function(){var e={}.hasOwnProperty;O=r(e,"undefined")||r(e.call,"undefined")?function(e,t){return t in e&&r(e.constructor.prototype[t],"undefined")}:function(t,n){return e.call(t,n)}}(),C._l={},C.on=function(e,t){this._l[e]||(this._l[e]=[]),this._l[e].push(t),Modernizr.hasOwnProperty(e)&&setTimeout(function(){Modernizr._trigger(e,Modernizr[e])},0)},C._trigger=function(e,t){if(this._l[e]){var n=this._l[e];setTimeout(function(){var e,r;for(e=0;e<n.length;e++)(r=n[e])(t)},0),delete this._l[e]}},Modernizr._q.push(function(){C.addTest=f}),Modernizr.addTest("svgasimg",t.implementation.hasFeature("http://www.w3.org/TR/SVG11/feature#Image","1.1"));var V=C.testStyles=g,j=function(){var e=navigator.userAgent,t=e.match(/applewebkit\/([0-9]+)/gi)&&parseFloat(RegExp.$1),n=e.match(/w(eb)?osbrowser/gi),r=e.match(/windows phone/gi)&&e.match(/iemobile\/([0-9])+/gi)&&parseFloat(RegExp.$1)>=9,i=533>t&&e.match(/android/gi);return n||i||r}();j?Modernizr.addTest("fontface",!1):V('@font-face {font-family:"font";src:url("https://")}',function(e,n){var r=t.getElementById("smodernizr"),i=r.sheet||r.styleSheet,o=i?i.cssRules&&i.cssRules[0]?i.cssRules[0].cssText:i.cssText||"":"",s=/src/i.test(o)&&0===o.indexOf(n.split(" ")[0]);Modernizr.addTest("fontface",s)});var A={elem:l("modernizr")};Modernizr._q.push(function(){delete A.elem});var z={style:A.elem.style};Modernizr._q.unshift(function(){delete z.style}),C.testAllProps=v;var L=C.prefixed=function(e,t,n){return 0===e.indexOf("@")?P(e):(-1!=e.indexOf("-")&&(e=s(e)),t?v(e,t,n):v(e,"pfx"))};C.prefixedCSS=function(e){var t=L(e);return t&&a(t)};C.testAllProps=w,Modernizr.addTest("cssanimations",w("animationName","a",!0)),i(),o(y),delete C.addTest,delete C.addAsyncTest;for(var G=0;G<Modernizr._q.length;G++)Modernizr._q[G]();e.Modernizr=Modernizr}(window,document);