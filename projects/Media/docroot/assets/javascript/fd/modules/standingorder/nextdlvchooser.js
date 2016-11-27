var FreshDirect = FreshDirect || {};

(function(fd) {
  /*
   * @param selectable (boolean)
   * @param candidates (array of strings)
   * @param inputName (string, optional)
   */
  var me = function(selectable, candidates, inputName) {
    if (typeof inputName === 'undefined') {
      inputName = 'soDeliveryWeekOffset';
    }
    
    return function(dayOfWeek) {
      var l = candidates[dayOfWeek];
      var i;
      var obj = document.getElementById(inputName);
      var opt;

      if (selectable) {
        obj.innerHTML = '';
        
        for (i=0; i<l.length; i++) {
          opt = document.createElement('option');
          opt.setAttribute("value", i+1);
          obj.appendChild(opt);
          opt.innerHTML = l[i];
        }
      } else {
        obj.innerHTML = l[0];
      }
    };
  };
  
  
  // register in fd namespace
  fd.modules.common.utils.register("modules.standingorder", "NextDeliveryDateChooserObserver", me, fd);
}(FreshDirect));
