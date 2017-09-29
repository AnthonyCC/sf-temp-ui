<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import='com.freshdirect.webapp.ajax.browse.FilteringFlowType' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

<%-- OAS variables --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>OAS controlled widget demo</title>
    <jwr:style src="/global.css" media="all" />
		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <c:set var="sitePage" scope="request" value="www.freshdirect.com/index.jsp" />
    <c:set var="listPos" scope="request" value="SystemMessage,HPFeatureTop,HPFeature,HPTab1,HPTab2,HPTab3,HPTab4,HPFeatureBottom,HPWideBottom,HPLeftBottom,HPMiddleBottom,HPRightBottom" />
    <jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
  </head>
	<body>
    <div id="content">

      <h2>Non-OAS controllers</h2>
      <ul>
        <li><button fd-signal="demo" fd-signal-params="param1;param2;param3:value3">Button</button>
        <li><button fd-signal="demo">Button w/ JSON params<span fd-signal-json-params>{"a": 1, "b": 2, "list": [1, 2, 3]}</span></button>
        <li><a href="#" fd-signal="demo">Link</a>
        <li><a href="#dont-get-here" fd-signal="demo" fd-signal-prevent-default>Link w/ default action prevented</a>
        <li> ======
        <li><button fd-signal="demo" fd-signal-params="clear">Clear content</button>
        <li><button fd-signal="demo" fd-signal-params="text:FreshDirect">Set content to "FreshDirect"</button>
        <li><button fd-signal="demo" fd-signal-params="content:/media/brands/martins/martins.html">Load media content</button>
      </ul>

      <h2>OAS slot</h2>
      <p>[HPFeature]</p>
      <div class="oas-top">
        <div class="oas-cnt" id="oas_HPFeature"><script type="text/javascript">OAS_AD('HPFeature');</script></div>
      </div>

      <h2>Demo widget below</h2>
      <div id="OAS-demo">
        <p>Nothing clicked yet.</p>
      </div>

    </div>
  <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
<script>
(function (fd) {
  var $ = fd.libs.$,
      demo = Object.create(fd.common.signalTarget, {
    signal: {
      value: 'demo'
    },
    callback: {
      value: function (params) {
        var $el = $("#OAS-demo");

        console.log('demo signal fired with the following params:', params);
        if (params.clear) {
          $el.html('');
        }
        if (params.text) {
          $el.html(params.text);
        }
        if (params.content) {
          $el.load(params.content);
        }
      }
    }
  });

  demo.listen();
}(FreshDirect));
</script>
	</body>
</html>
