<!DOCTYPE html>
<html>
  <head>
    <title>Reviews</title>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
  </head>
  <body>
    <div id="BVRRContainer"></div>
    
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
<script>
  (function () {
    if ($BV) {
      $BV.ui('rr', 'show_reviews', { productId: FreshDirect.modules.common.utils.getParameterByName('productId')});
    }
  }());
</script>
  </body>
</html>
