/*global jQuery,common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
    "use strict";

    var $ = fd.libs.$;
    var WIDGET = fd.modules.common.widget;

    var atp = Object.create(null, {
        formSelector : { value : "[data-component='atpform']" },
        /**
         * checked radio components -> value list
         */
        collectData : {
            value : function(){
              var removeIds = [];

              $("[data-component='atpremove']:checked").each(function(){
                removeIds.push($(this).attr("id"));
              });

              return removeIds;
            }
        },
        /**
         * value -> input field with value
         */
        toHtml : {
          value : function(inputName, removeId){
            return $("<input type='checkbox' name='" + inputName + "' value=" + removeId + " checked />");
          }
        },
        /**
         * value list -> input field list
         */
        toList : {
          value : function(removeIds){
            if (!removeIds || !removeIds.length){
              return [];
            }

            var i = 0,
                len = removeIds.length,
                hiddenInputs = [];
            
            for(i = 0; i < len; i++){
                hiddenInputs.push(this.toHtml("remove", removeIds[i])); 
            }

            return hiddenInputs;
          }
        },
        /**
         * submits a hidden form filled up with selected removable radio button values as hidden values
         */
        serialize: {
          value: function (e) {
            // e.currentTarget can be a link so preventing is a MUST
            // in order to really submit and not redirect before
            e.preventDefault(); 

            var $form = $(this.formSelector);

            if($form.length < 1){
              return;
            }

            $form.append(this.toList(this.collectData()));
            $form.submit();
          }
        }
    });

    $(document).on('click', "[data-component='atpsubmit']", atp.serialize.bind(atp));

    fd.modules.common.utils.register("components", "ATPSubmitter", atp, fd);

}(FreshDirect));
