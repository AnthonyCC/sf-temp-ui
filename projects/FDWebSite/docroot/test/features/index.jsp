<!DOCTYPE html>
<%@ taglib uri="fd-features" prefix="features" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<features:allFeaturesPotato />

<html lang="en-US" xml:lang="en-US">
<head>
  <meta charset="UTF-8">
  <title>Feature test page</title>
  <%@ include file="/common/template/includes/i_javascripts.jspf" %>
  <style>
    #features li b {
      display: inline-block;
      width: 200px;
      height: 22px;
    }
  </style>
</head>
<body>

  <h1>Enable/disable feature versions via cookie</h1>
  <ul id="features">
  </ul>
  <div class="description">
  This site enables the QA team to change between the different versions of the newly implemented features. </br>
  The tester should set the dropdowns according to which feature set configuration is needed and click on the <b>Set selected versions</b> button. </br>
  After this a cookie is created for the user with the set feature versions. </br>
  Until this cookie is deleted or reset with the <b>Reset features cookie</b> button the user will browse the store according to set features in this browser.
  </div>
  <ul class="notes">
    <li>[p]: enabled via property</li>
    <li>[c]: enabled via cookie</li>
    <li>[a]: active</li>
  </ul>
  <p>
    <button id="setselected">Set selected versions</button>
  </p>
  <p>
    <button id="reset">Reset features cookie</button>
  </p>

<script>
(function (fd) {
  
var allFeatures = <fd:ToJSON object="${allFeaturesPotato.allFeatures}" noHeaders="true"/>,
    $ = fd.libs.$,
    $features = $('#features');

Object.keys(allFeatures.versions).forEach(function (k) {
  var versions = allFeatures.versions[k],
      $feature = $('<li><b>'+k+'</b><select name="'+k+'"></select></li>');

  versions.forEach(function (v) {
    $feature.find('select').append('<option value="'+v.version+'" '+(v.active ? 'selected' : '')+'>'+v.version+(v.enabledInProperty ? ' [p]' : '')+(v.enabledInCookie ? ' [c]' : '')+(v.active ? ' [a]' : '')+'</option>');
  });
  
  $features.append($feature);
});

$('#setselected').on('click', function () {
  var features = {};

  $('#features select').each(function () {
    var $this = $(this),
        name = $this.attr('name'),
        value = $this.val();

    features[name] = value;
  });

  fd.utils.setActiveFeatures(features);
  window.location.reload();
});

$('#reset').on('click', function () {
  fd.utils.setActiveFeatures();
  window.location.reload();
});

}(FreshDirect));
</script>

</body>
</html>

