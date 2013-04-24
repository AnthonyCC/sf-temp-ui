function TimeEditor(args) {
		  var $input;
		  var scope = this;
		  var defaultValue;
		  this.init = function () {
			  $input = $("<INPUT type=text class='editor-text' onblur='this.value=time(this.value);' />")
		          .appendTo(args.container)
		          .bind("keydown", scope.handleKeyDown)
				  .focus()
				  .select();
		    };
		    
		    this.handleKeyDown = function (e) {
			      if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
			        e.stopImmediatePropagation();
			      } if (e.keyCode === $.ui.keyCode.TAB) {
			    	  $input.val(time($input.val()));
			      }
			    };
			    this.destroy = function () {
			        $input.remove();
			      };

			      this.focus = function () {
			        $input.focus();
			      };

			      this.getValue = function () {
			        return $input.val();
			      };

			      this.setValue = function (val) {
			        $input.val(val);
			      };

			
			      this.loadValue = function (item) {
			          defaultValue = item[args.column.field] || "";
			          $input.val(defaultValue);
			          $input[0].defaultValue = defaultValue;
			          $input.select();
			        };

			        this.serializeValue = function () {
			          return $input.val();
			        };

			        this.applyValue = function (item, state) {
			          item[args.column.field] = state;
			        };

			        this.isValueChanged = function () {
			          return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
			        };

			        this.validate = function () {
			            if (args.column.validator) {
			              var validationResults = args.column.validator($input.val());
			              if (!validationResults.valid) {
			                return validationResults;
			              }
			            }

			            return {
			              valid: true,
			              msg: null
			            };
			          };
			          
					this.init();
			    
		  }
	  
	  
		function compareTimes(a, b) { 
		    if(daterize(convert12to24(a[sortcol])) > daterize(convert12to24(b[sortcol]))) return 1;
		    if(daterize(convert12to24(a[sortcol])) < daterize(convert12to24(b[sortcol]))) return -1;
		    return 0;
		}
		function convert12to24(timeStr){
		    var meridian = timeStr.substr(timeStr.length-2).toLowerCase();
		    var hours =  timeStr.substr(0, timeStr.indexOf(':'));
		    var minutes = timeStr.substring(timeStr.indexOf(':')+1, timeStr.indexOf(' '));
		    if (meridian=='pm'){
		    	if(hours!=12)
		       		hours=hours*1+12;
		    }
		    return hours+':'+minutes+":00";
		}

		function daterize(time) {
		    return Date.parse("Thu, 01 Jan 1970 " + time + " GMT");
		}

function activateEdit(e, args) {
 var keyCode = $.ui.keyCode,
     col,
     activeCell = this.getActiveCell();

 /////////////////////////////////////////////////////////////////////
 // Allow instant editing like MS Excel (without presisng enter first
 // to go into edit mode)
 if (activeCell) {
   col = activeCell.cell;

   // Only for editable fields and not if edit is already in progress
   if (this.getColumns()[col].editor && !this.getCellEditor()) {
     // Ignore keys that should not activate edit mode
     if ($.inArray(e.keyCode, [keyCode.LEFT, keyCode.RIGHT, keyCode.UP,
                              keyCode.DOWN, keyCode.PAGE_UP, keyCode.PAGE_DOWN,
                              keyCode.SHIFT, keyCode.CONTROL, keyCode.CAPS_LOCK,
                              keyCode.HOME, keyCode.END, keyCode.INSERT,
                              keyCode.ENTER]) === -1) {
       this.editActiveCell();
     }
   }
 }
}