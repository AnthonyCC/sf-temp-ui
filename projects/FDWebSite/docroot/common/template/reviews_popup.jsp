<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
  <head>
    <title>Reviews</title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
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
