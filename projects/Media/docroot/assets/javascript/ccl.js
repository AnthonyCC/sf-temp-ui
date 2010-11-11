/*jslint browser: true, evil: false, nomen: false, undef: false, white: false, widget: true */
/* Customer Created Lists AJAX interface */                                                                                      

var escapeHTML = function(str) {
   var buff = new Array();
   for(i=0; i< str.length; ++i) {
      switch(str.charAt(i)) {
         case '<':  buff.push("&lt;"); break;
         case '>':  buff.push("&gt;"); break;
         case '\"': buff.push("&quot;"); break;
         case '&':  buff.push("&amp;"); break;
         default:
             buff.push(str.charAt(i));
      }
   }
   return buff.join("");
};

/**
 * Utility function to produce query string collecting key-value pairs from hash table (Object type)
 * 
 * @param hash Hash table to flatten
 * @param array hash containing hash elements in '<key>=<escaped value>' form
 */
function flattenHash(obj, array) {
    if (typeof(obj) == "object") {
        var k;
        for (k in obj) {
            if (typeof(obj[k]) == "string" || typeof(obj[k]) == "number") {
                array.push(k + "=" + escape(obj[k]));
            } else if (typeof(obj[k]) == "object") {
                flattenHash(obj[k], array);
            } // "function" types skipped
        }
    }

    return array;
}




var CCLLinkTargetsClass  = function() {
};

CCLLinkTargetsClass.prototype.set_target = function(link,target) {
    this.targets[link] = target;
};

CCLLinkTargetsClass.prototype.target = function(link) {
    if (this.targets[link]) return this.targets[link];
    window.alert("no target for link " + link);
};

var CCLLinkTargets = new CCLLinkTargetsClass();

CCLLinkTargets.targets = {
    visitLists      : "/quickshop/all_lists.jsp",
    visitThisList   : "/quickshop/shop_from_list.jsp",
    visitThisSOList : "/quickshop/so_details.jsp",
    selection_check : "/api/check_selection.jsp",
    goToQS          : "/quickshop/index.jsp",
    modifyItem      : "/quickshop/ccl_item_modify.jsp"
};



/** Convenience class for creating CCL Popups 
 * 
 */
var CCLPanel = function (el, titleImg, userConfig) {
    if (arguments.length > 0) { 
        CCLPanel.superclass.constructor.call(this, el, userConfig); 
        this.titleImg = titleImg;
    }             

};

YAHOO.extend(CCLPanel, YAHOO.widget.Panel); 

CCLPanel.CSS_CCLPANEL = "ccl-panel";

CCLPanel.IMAGE_PREFIX = "/media_stat/ccl/";

CCLPanel.TITLE_IMAGES = {
    "wait_animation.gif": { width: "32px", height: "32px" },
    "list_create_title.gif": { width: "128px", height: "13px" },
    "adopt_diag_title.gif": { width: "86px", height: "12px" },
    "list_help_title.gif": { width: "174px", height: "12px" },
    "list_rename_title.gif": { width: "94px", height: "13px" },
    "list_save_item_title.gif": { width: "93px", height: "12px" },
    "list_delete_title.gif": { width: "84px", height: "12px" },
    "list_diag_help.gif": { width: "12px", height: "12px" },
    "list_diag_close.gif": { width: "12px", height: "12px" },
    "shopping_lists_title.gif": { width: "118px", height: "12px" },
    "so_chgfrq_title.gif": { width: "162px", height: "14px" },
    "so_delorder_title.gif": { width: "108px", height: "14px" },
    "so_shiftdlv_title.gif": { width: "119px", height: "13px" }
};

CCLPanel.TXT = {
    "EnterNewList" : { 
        txt:   "Enter a new list name:", 
        style: "text11" },
    "EnterName" : { 
        txt:   "Please enter a name:", 
        style: "text11rbold" },
    "EnterNewName" : { 
        txt:   "Enter a new list name:", 
        style: "text11"},
    "NeedsName" : { 
        txt:   "Your list needs a name:", 
        style: "text11rbold"},
    "NewNeedsName" : { 
        txt:   "Your new list needs a name:", 
        style: "text11rbold"},
    "YouAlreadyHaveList" : { 
        txt:   "Oops! That name is taken.", 
        style: "text11rbold" },
    "NameTooLong" : {
        txt:   "List name too long (max 35 characters)!",
        style: "text11rbold" },
    "YouDontHaveAList" : { 
        txt: "You do not have another list, please create one!",
        style: "text11gbold" },
    "PleaseWait" : { 
        txt:   "Please wait...", 
        style: "text11" },
    "UnexpectedError" : { 
        txt:   "Unexpected error. Please try again later.", 
        style: "text11rbold" },
    "SessionTimeout" : {
        txt:   "Your session has timed out.",
    style: "text11rbold" },
    "UnexpectedJSONResponse" : { 
        txt: "Unexpected server response. Please try again later.",
        style: "text11rbold" },
    "ServiceError" : { 
        txt: "Service unavailable. Please try again later.",
        style: "text11rbold" },
    "ChooseList" : { 
        txt:   "Please choose a list:", 
        style: "text11" },
    "DelOrder" : { 
        txt:   "Yes, delete this standing order", 
        style: "text11" },
    "DelOrderTitle" : { 
        txt:   "<div style=\"color: #660000; font-weight: bold; text-align: left;\">ARE YOU SURE YOU WANT TO DELETE THIS STANDING ORDER?</div>", 
        style: "text12" },
    "DelOrderBody" : {
        txt:   "<ul><li>This list will be deleted<br><br></li><li>No further deliveries will be scheduled.</li></ul>", 
        style: "text11" },
    "DelOrderConfirm" : {
        txt:   "<div style=\"color: #660000; font-weight: bold; text-align: left;\">STANDING ORDER HAS BEEN DELETED</div>", 
        style: "text11" },
    "SkipDelivery" : { 
        txt:   "Your upcoming delivery is ", 
        style: "text12" }
};

CCLPanel.LINKS = {
    "contLink" : { 
        txt:   "<u>Continue</u>", 
        style: "text11gbold" },
    "contShopping" : { 
        txt:   "<u>Continue Shopping</u>", 
        style: "text11gbold" },
    "cancel" : { 
        txt:   "<u>Cancel</u>", 
        style: "text11gbold" },
    "errorClose" : { 
        txt:   "<u>Close</u>", 
        style: "text11gbold" },
    "keepShopping" : { 
        txt:   "<u>Continue Shopping</u>", 
        style: "text11gbold" },
    "visitLists" : { 
        txt:   "<u>Visit Your Shopping Lists</u>", 
        style: "text11g" },
    "visitThisList" : { 
        txt:   "<u>View this list in Quickshop</u>", 
        style: "text11g" },
    "visitThisSOList" : { 
        txt:   "<u>View this list in Quickshop</u>", 
        style: "text11g" },
    "deleteList" : { 
        txt:   "<u>Yes, delete this list.</u>", 
        style: "text11gbold" },
    "createAnotherList" : { 
        txt:   "<u>Create another list</u>", 
        style: "text11g" },
    "goToQS"   : { 
        txt:   "<u>To the QuickShop page</u>", 
        style: "text11g" },
    "refresh" : {
        txt:   "<u>Refresh the page.</u>",
	    style: "text11g" },
    "signUp"   : { 
        txt:   "<u>Sign Up</u>", 
        style: "text11g ccl-panel-needlogintext",
        prefixStyle: "text11 ccl-font-black",
        prefix: "<b>New Customer?</b> " },
    "logIn"   : { 
        txt:   "<u>Log In</u>", 
        style: "text11g ccl-panel-needlogintext",
        prefixStyle: "text11 ccl-font-black",
        prefix: "<b>Current Customer?</b> " },
    "delOrderLink" : { 
        txt:   "<u>Yes, delete this standing order</u>", 
        style: "text11gbold" },
    "skipDlvLink" : { 
        txt:   "<u>Skip this delivery</u>", 
        style: "text11gbold" },
    "skipCancelLink" : { 
        txt:   "<u>Cancel, do not skip</u>", 
        style: "text11gbold" }
};


CCLPanel.INPUT = {
    "nameInput" : { 
        style: "text11",
        value: "" }
};



CCLPanel.prototype.init = function(el, userConfig) { 
    CCLPanel.superclass.init.call(this, el);  
         
    this.beforeInitEvent.fire(CCLPanel); 
     
    YAHOO.util.Dom.addClass(this.innerElement, CCLPanel.CSS_CCLPANEL); 

    // Set up YUI panel configuration
    if (!userConfig) {
        userConfig = { 
            width: "250px",
            // fixedcenter: true,
            close:true, 
            draggable:true, 
            modal:true,
            visible:false,
            underlay: "none"
        };
    }
    this.cfg.applyConfig(userConfig, true);

    var self = this;
    // register a handler for closing the dialog when clicked "outside"
    YAHOO.util.Event.addListener(el + '_mask','click',function() { if (self.cfg.getProperty("visible")) self.hide(); });

    // Set up state machine                       
    this.states = {};
    this.sources = {};

    // Create default header and footer
    this.createHeader();
    this.createFooter();

    // Make sure that the body element has the holyhack class
    this.setBody("");
    YAHOO.util.Dom.addClass(this.body, "ieholyhack"); 
        
    // Subscribe to change content event
    // Most of this is necessary for browser workarounds
    this.changeContentEvent.subscribe(function() {
        var me = this;
        if (me.innerElement) {
            if (me._disableChangeContentHandler) return;
            // IE forgets to reapply CSS on resize
            YAHOO.util.Dom.removeClass(me.innerElement, CCLPanel.CSS_CCLPANEL); 
            YAHOO.util.Dom.addClass(me.innerElement, CCLPanel.CSS_CCLPANEL); 
            // Safari has a bug with curvyCorners on resize
            me.remakeCurvy(); 
            // Workaround IE bug: make sure that the panel is correctly realigned
            if (me.shown) {
                me.hide();
                me.show();
            }
        }
    }, this, true); 
    // Allows to temporarly disable the changeContentHandler 
    this._disableChangeContentHandler = false;
    
    // Set up the help function (to load the wait text from server)
    var self = this;
    this.cfg.queueProperty("help", function() {
        self.cfg.setProperty("width", "350px");
        self.createHeader(self.createTitleImg("list_help_title.gif"));
        self.makeBody(self.createWaitAnimation());
        self.JSON.MediaFacade.getHelpMessage(self);
        self.currentState = "wait_help";
        }
    );

    // Standard state function: wait for help text from the server
    this.states["wait_help"] = function(self, event_type, orig, args) {
        if (event_type == "json") {
            var result = args[0];
            var exception = args[1];

            self.handleJSONException(result, exception);  // Bail out if there was an error                       

            self.makeBody(
                self.createText(result, "text11"),
                self.createBulletedList(
                    self.createLink("keepShopping"),
                    self.createLink("visitLists")
                )
            );
            self.currentState = "help";
        }                        
    };

    // Standard state function: react on clicks in the help screen
    this.states["help"] = function(self, event_type, orig) {
        if (event_type == "click") {
            if (orig == self.sources.visitLists) {
                window.location.href = CCLLinkTargets.target("visitLists");
            }
            if (orig == self.sources.keepShopping) { }
            self.hide();
        }
    };

    // Standard state function: in error case we hide on mouse clicks.
    this.states["error"] = function(self, event_type, orig)  {
        if (event_type == "click") {
            if (orig == self.sources.signUp) {
                window.location.href = "/registration/signup.jsp";
            } else if (orig == self.sources.logIn) {
                window.location.href = "/login/login.jsp";
            }
            self.hide();
        }
    };

    // Special init function to show "Need to login screen"
    this.states["_nojson"] = function(self) {
        self.cfg.setProperty("width", "250px");
        self.createHeader(self.createTitleImg(self.titleImg));
        self.makeBody(self.createWaitAnimation());
        self.JSON.MediaFacade.getNeedLoginMessage(self);
        self.render(document.body);
        self.show();
        self.currentState = "_nojson_wait";
    };

    // Standard state function: wait for help text from the server
    this.states["_nojson_wait"] = function(self, event_type, orig, args) {
        if (event_type == "json") {
            var result = args[0];
            var exception = args[1];

            self.handleJSONException(result, exception);  // Bail out if there was an error                       

            self.makeBody(
                self.createText(result, "text11 ccl-panel-needlogintext"),
                self.createPrefixedLink("signUp"),
                self.createPrefixedLink("logIn")
            );
            self.currentState = "_nojson_shown";
        }                        
    };

    this.states["_nojson_shown"] = function(self, event_type, orig)  {
        if (event_type == "click") {
            if (orig == self.sources.signUp) {
                window.location.href="/registration/signup.jsp?successPage=" + escape(window.location.pathname + window.location.search);
            } else if (orig == self.sources.logIn) {
                window.location.href="/login/login.jsp?successPage=" + escape(window.location.pathname + window.location.search);
            }
            self.hide();
        }
    };
            
    // Fire YUI initialization event            
    this.initEvent.fire(CCLPanel); 
}; 

// Configures the help button
CCLPanel.prototype.configHelp = function(type, args, obj) {
    var val = args[0];

    if (val) {
        if (! this.help) {
            this.help = document.createElement("span");
            YAHOO.util.Dom.addClass(this.help, "ccl-panel-help");
            this.help.innerHTML = "&#160;";
            this.innerElement.appendChild(this.help);
            YAHOO.util.Event.addListener(this.help, "click", val, this);
        } else {
            this.help.style.display = "block";
            YAHOO.util.Event.removeListener(this.help, "click");                        
            YAHOO.util.Event.addListener(this.help, "click", val, this);                        
        }
    } else {
        if (this.help) {
            this.help.style.display = "none";
        }
    }
};

// Configures the title image
CCLPanel.prototype.configTitleImg = function(type, args, obj) {
    var val = args[0];
    if (val) {
        this.titleImg = this.createTitleImg(val);
        this.createHeader(this.titleImg);
    }
};

// Sets up initialization parameters (help and titleImg)
CCLPanel.prototype.initDefaultConfig = function() {
    CCLPanel.superclass.initDefaultConfig.call(this);                
    this.cfg.addProperty("help", { handler:this.configHelp, suppressEvent:true });
    this.cfg.addProperty("titleImg", { handler:this.configTitleImg, suppressEvent:true });
};

// Instantiate wait animation widget
CCLPanel.prototype.createWaitAnimation = function() {
    return this.createTitleImg("wait_animation.gif");
};

// Instantiate link widget                        
CCLPanel.prototype.createLink = function(id, withspan) {
    if (CCLPanel.LINKS[id]) {
        var text = CCLPanel.LINKS[id].txt;
        var style = CCLPanel.LINKS[id].style;
    }
    
    var elem = document.createElement(withspan ? "span" : "div");
    var anchor = document.createElement("a");
    var myPanel = this;
    anchor.onclick = function() {
        myPanel.handleStateEvent("click", this);
        return false;
    };
    
    anchor.innerHTML = text;
    anchor.id = id;
    YAHOO.util.Dom.addClass(anchor, "ccl-panel-link"); 
    
    styleStr = "ccl-panel-link-text";
    if (style) styleStr = styleStr + " " + style;
    
    YAHOO.util.Dom.addClass(elem, styleStr); 
    elem.appendChild(anchor);
    myPanel.sources[id] = anchor;
    
    return elem;
};

CCLPanel.prototype.createPrefixedLink = function(id, withspan) {
    if (CCLPanel.LINKS[id]) {
       var text = CCLPanel.LINKS[id].txt;
       var style = CCLPanel.LINKS[id].style;
       var prefix = CCLPanel.LINKS[id].prefix;
       var prefixStyle = CCLPanel.LINKS[id].prefixStyle;
    }
    
    var elem = document.createElement(withspan ? "span" : "div");

    if (prefix) {
        var prefixCtr = document.createElement("span");
        prefixCtr.innerHTML = prefix + "&nbsp;";
        elem.appendChild(prefixCtr);
        if (prefixStyle) {
            YAHOO.util.Dom.addClass(prefixCtr, prefixStyle); 
        }
    }

    var anchor = document.createElement("a");
    var myPanel = this;
    anchor.onclick = function() {
        myPanel.handleStateEvent("click", this);
        return false;
    };
    
    anchor.innerHTML = text;
    anchor.id = id;
    YAHOO.util.Dom.addClass(anchor, "ccl-panel-link"); 
    
    styleStr = "ccl-panel-link-text";
    if (style) styleStr = styleStr + " " + style;
    
    YAHOO.util.Dom.addClass(elem, styleStr); 
    elem.appendChild(anchor);
    myPanel.sources[id] = anchor;
    
    return elem;    
};

CCLPanel.prototype.createBulletedList = function() {
    // create unordered list
    var list = document.createElement("ul");
    var elem;
    
    var i;
    for(i=0; i<arguments.length; i++) {
        elem = document.createElement("li");
        elem.appendChild(arguments[i]);

        // append item embedded in 'li' tag to list        
        list.appendChild(elem);
    }

    return list;
};


// Instantiate text widget
CCLPanel.prototype.createText = function(id) {
    if (arguments.length == 2) {
        var text = arguments[0];
        var style = arguments[1];
    } else if (CCLPanel.TXT[id]) {
        var text = CCLPanel.TXT[id].txt;
        var style = CCLPanel.TXT[id].style;
    }
    var elem = document.createElement("div");
    elem.innerHTML = text;
    styleStr = "ccl-panel-text";
    if (style) styleStr = styleStr + " " + style;
    YAHOO.util.Dom.addClass(elem, styleStr); 
    return elem;
};

// Class method for createTitleImg
CCLPanel.createTitleImg = function(imgname, loadfn) {
    var elem = document.createElement("div");
    var dims = CCLPanel.TITLE_IMAGES[imgname];
    if (!dims) {
        window.alert("createTitleImg: Tried to use unregistered title img:"+imgname);
    }
    if (dims.cachedImg) {
        //console.log("Reusing cached image:"+ imgname);
        var img = dims.cachedImg;
    } else {
        //console.log("createTitleImg:"+imgname+","+dims.width+","+dims.height);
        var img = document.createElement("img");
        img._imgId = imgname;
        img.setAttribute("width", dims.width);
        img.setAttribute("height", dims.height);
        if ((typeof loadfn) == "function") {
            img.onload = loadfn;
        }
        img.src = CCLPanel.IMAGE_PREFIX + imgname;
    }

    elem.appendChild(img);
    YAHOO.util.Dom.addClass(elem, "ccl-panel-title"); 
    return elem;
};


// Instantiate title image widget
CCLPanel.prototype.createTitleImg = CCLPanel.createTitleImg;

// Set the header of the panel with a title
CCLPanel.prototype.createHeader = function(title) {
    titlestr = "";
    if (title) titlestr = title;
        
        this.setHeader(titlestr);
};

// Set an empty footer for the panel
CCLPanel.prototype.createFooter = function() {
        this.setFooter('');        
};

// Create text input box
CCLPanel.prototype.createTextInput = function(id) {
    //console.log("Created text control: " +id);
    if (CCLPanel.INPUT[id]) {
        var style = CCLPanel.INPUT[id].style;
        var value = CCLPanel.INPUT[id].value;
    }

    var myPanel = this;
    var input = document.createElement("input");
    input.type = "text";
    input.value = value;
    input.onkeydown = function(evt) {
        var code;
        if (!evt) evt = window.event;
        if (evt.keyCode) {
            code = evt.keyCode;
        } else if (evt.which) {
            code = evt.which;
        }
        //console.log("Pressed key: " + code);
        if (code == 13) {
            // Enter pressed
            myPanel.handleStateEvent("textenter", this);
            return false;
        }
        return true;
    };
        
    styleStr = "ccl-panel-text-input";
    if (style) styleStr = styleStr + " " + style;
    YAHOO.util.Dom.addClass(input, styleStr); 
    if (myPanel.sources[id]) {
        // this is a recreation, reuse the value of the previous element
        input.value = myPanel.sources[id].value;
    }

    myPanel.sources[id] = input;
    return input;
};

// Create dropdown list
CCLPanel.prototype.createDropdownList = function(id, style, labels, values) {

    if (typeof(labels) == "undefined") {
        labels = [];
    }
    if (typeof(values) == "undefined") {
        values = [];
    }

    var myPanel = this;
    var div = document.createElement("div");
    var select = document.createElement("select");
    div.appendChild(select);

    select.id = id;
    for(i = 0; i < values.length; ++i) {
        var option = document.createElement("option");
        option.label = labels[i];
        option.value = values[i];
        option.appendChild(document.createTextNode(labels[i]));
        select.appendChild(option);
    }

    // Use onchange instead of onclick to support Selenium
    select.onchange = function(evt) { 
        myPanel.handleStateEvent("click", this);
        return false;
    };

    styleStr = "ccl-panel-dropdown";
    if (style) styleStr = styleStr + " " + style;
    YAHOO.util.Dom.addClass(select, styleStr);
    this.sources[id] = select;
    return div;
};

CCLPanel.prototype.computeDropDownMembers = function(result) {
    var labels = new Array();
    var values = new Array();
    var types = new Object();

    for(i = 0; i< result.length; ++i) {
        var itemLabel = " (" +  result[i][1] + (result[i][1] == 1 ? " item" : " items") + ")" ;
        values[i] = result[i][0];
        labels[i] = values[i] + itemLabel;
        // types[i] = result[i][2];
        types[result[i][0]] = result[i][2];
    }
    values[result.length] = "__NEW__";
    labels[result.length] = "CREATE NEW LIST";
    types[result.length] = ""; // no type

    this.dropdownLabels = labels;
    this.dropdownValues = values;
    this.dropdownTypes = types;
};

CCLPanel.prototype.setupJSON = function(){
    if (!this.JSON || !this.JSON.CCLFacade) {
        // Set up JSON-RPC
        this.JSON = new JSONRpcClient("/api/json-rpc.jsp");
        this.JSON.responseHandlerName = "handleJSONResponse";
        this.JSON.setup_server_url = function(req) {
            return this.serverURL + "?methodName=" + req.methodName;
        };
        if (this.JSON.CCLFacade) {
            return true;
        }
        if (this.noNeedToLogin) {
        return true;
              }
        return false;
    }
    return true;
};

// Start the State Machine
CCLPanel.prototype.startSM = function() {
    if (!this.states || !this.states.init) {
        window.alert("Invalid state machine configuration!");
        return ;
    }
    if (this.setupJSON()) {
        this.currentState = "init";
    this.states["init"](this);
    } else {
        this.currentState = "_nojson";
        this.states["_nojson"](this);
    }
};

// State function dispatcher
CCLPanel.prototype.handleStateEvent = function(event_type, originator) {
    // console.log("State change to " + this.currentState);
    var stateFunc = this.states[this.currentState];
    if (!stateFunc) {
        window.alert("Invalid state machine configuration! No state function for state "+this.currentState);
        return ;
    }
    var remainingArgs = new Array();
    for (var i = 2; i < arguments.length; i ++) {
        remainingArgs[i - 2] = arguments[i];
    }
    try { 
        stateFunc(this, event_type, originator, remainingArgs);
    } catch (e) {
        if (e != "JSON_Exception_Handled") {
            throw e;
       }
    }
};

// Generic callback function for JSON-RPC
CCLPanel.prototype.handleJSONResponse = function(result, exception) {
    this.handleStateEvent("json", null, result, exception);
};

// Standard exception handler: show generic error + go to error state
CCLPanel.prototype.handleJSONException = function(result, exception) {
    if (exception == null) return;
    this.makeBody(
        this.createText("UnexpectedError"),
    this.createText(exception,name.split("\n")[0]),
        this.createLink("errorClose")
    );
    this.currentState = "error";
    throw "JSON_Exception_Handled";
};

CCLPanel.JSON_OK = 0;
CCLPanel.JSON_NameExists = 1;
CCLPanel.JSON_NameEmpty = 2;
CCLPanel.JSON_NameTooLong = 3;
CCLPanel.JSON_SessionTimeout = 4;

CCLPanel.prototype.handleJSONListNameException = function(result, exception) {
    if (exception == null) return CCLPanel.JSON_OK;

    var realExName = exception.name.split("\n")[0];

    if (realExName == "com.freshdirect.webapp.util.CustomerCreatedListAjaxFacade$NameEmpty") {
        return CCLPanel.JSON_NameEmpty;
    } else if (realExName == "com.freshdirect.webapp.util.CustomerCreatedListAjaxFacade$NameExists") {
        return CCLPanel.JSON_NameExists;
    } else if (realExName == "com.freshdirect.webapp.util.CustomerCreatedListAjaxFacade$NameTooLong") {
        return CCLPanel.JSON_NameTooLong;
    } else if (realExName == "com.freshdirect.webapp.util.CustomerCreatedListAjaxFacade$SessionTimeout") {
        this.handleTimeoutException();
    return CCLPanel.JSON_SessionTimeout;
    } else {
        this.handleJSONException(result, exception);
    }                                        
};

CCLPanel.prototype.handleTimeoutException = function() {
    this.makeBody(
        this.createText("SessionTimeout"),
        this.createPrefixedLink("refresh")
    );
    this.currentState = "refresh";
    if (!this.states["refresh"]) {
        this.states["refresh"] = function(self, event_type, orig) {
            if (event_type == "click") {
                if (orig == self.sources.refresh) {
                    window.location.href = window.location.href;
                }
                self.hide();
            }
        };
    }
};

CCLPanel.prototype.handleJSONExceptionNoLogin = function(result, exception) {
    if (exception == null) return;
    var realExName = exception.name.split("\n")[0];

    // TODO, do something smarter, ie login should be handled differently
    this.makeBody(this.createText("UnexpectedError"),
    //this.createText('(' + realExName + ')',"text9"),
    this.createText("Did you login?","text11"),
    this.createLink("goToQS"),
    this.createLink("errorClose"));
    this.currentState = "error_no_login";

    // Dynamically generate state function
    if (!this.states["error_no_login"]) {
        this.states["error_no_login"] = function(self, event_type, orig) {
            if (event_type == "click") {
                if (orig == self.sources.goToQS) {
                    window.location.href = CCLLinkTargets.target("goToQS");
                }
                self.hide();
            }
        };
    }
    throw "JSON_Exception_Handled";
};


// Generate curvy corners for the panel
CCLPanel.prototype.makeCurvy = function() {

    if (!this.fullCorn) {
        var full_settings = {
            tl: { radius: 6 },
            tr: { radius: 6 },
            bl: { radius: 6 },
            br: { radius: 6 },
            topColour: "#996699",
            antiAlias: true,
            autoPad: false
        };
 
        this.fullCorn = new curvyCorners(full_settings, this.innerElement);
        this.fullCorn.applyCornersToAll();
    }
};

// Reapply the curvy corners if already applied (workaround for Safari)
CCLPanel.prototype.remakeCurvy = function() {
    if (this.fullCorn) {
        this.fullCorn.reapplyCornersToAll();
    }
};

// Override show: also make the corners curvy if necessary
CCLPanel.prototype.show = function() {
    if (!this.shown) {
        this.makeCurvy();
        CCLPanel.superclass.show.call(this);
        this.alignPanel();
        this.shown = true;
    }
};

// Override hide: also make the corners curvy if necessary
CCLPanel.prototype.hide = function() {
    if (this.shown) {
        CCLPanel.superclass.hide.call(this);
        this.shown = false;
    }
};


// Replaces the content of the panel with a list of widgets.
CCLPanel.prototype.makeBody = function() {
    this._disableChangeContentHandler = true;
    this.setBody("");
    for(var i = 0; i< arguments.length; ++i) {
          this.appendToBody(arguments[i]);
    }
    this._disableChangeContentHandler = false;
    this.changeContentEvent.fire();
};

// Replaces the content of the panel with a list of widgets.
CCLPanel.prototype.makeBodyL = function() {
    this._disableChangeContentHandler = true;
    this.setBody("");
    
    var container = document.createElement("DIV");
    // align div content to left
    YAHOO.util.Dom.addClass(container, "toleft");
    
    this.appendToBody(container);
    
    for(var i = 0; i< arguments.length; ++i) {
          container.appendChild(arguments[i]);
    }
    this._disableChangeContentHandler = false;
    this.changeContentEvent.fire();
};


CCLPanel.prototype.alignPanel = function() {
    var contextArgs = this.cfg.getProperty("context");
    if (contextArgs) {
        var context = contextArgs[0];
        if (context) {
            var contextRegion = YAHOO.util.Dom.getRegion(context);
            var panelLeft = contextRegion.left + context.offsetWidth / 2 - this.element.offsetWidth / 2;
            var panelTop = contextRegion.top + context.offsetHeight / 2 - this.element.offsetHeight / 2;
            
            // Adjust position so no scrolling is required
            var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
            var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
        
            var viewPortWidth = YAHOO.util.Dom.getClientWidth();
            var viewPortHeight = YAHOO.util.Dom.getClientHeight();
        
            var elementWidth = this.element.offsetWidth;
            var elementHeight = this.element.offsetHeight;
            var panelRight = panelLeft + elementWidth;
            var panelBottom = panelTop + elementHeight;
            var offset = 8; // Magic offset to make sure that the full dialog is shown
            
            if (elementWidth + 2*offset < viewPortWidth && elementHeight + 2*offset < viewPortHeight ) {
                if (panelTop < scrollY + offset) panelTop = scrollY + offset;
                if (panelLeft < scrollX + offset) panelLeft = scrollX + offset;
                if (panelRight > viewPortWidth + scrollX - offset) {
                    panelLeft = panelLeft - (panelRight - (viewPortWidth + scrollX) + offset);
                } 
                if (panelBottom > viewPortHeight + scrollY - offset) {
                    panelTop = panelTop - (panelBottom - (viewPortHeight + scrollY) + offset);
                }
            }
            
            this.moveTo(panelLeft, panelTop);
            return;
        }
    }
    this.center();
};

CCLPanel.prototype.setContext = function(context) {
    this.cfg.setProperty("context", [context]);
};







/** Main CCL interface entry point class
 * 
 */
var CCLClass = function() {
    YAHOO.namespace("com.freshdirect.ccl");
};



/**
 * Creates a generic CCL panel
 * 
 * @param {Object} id			DOM ID
 * @param {Object} titleImg		Title image
 * @param {Object} context
 * @param {Object} panelAlign	Panel alignment
 * @param {Object} contextAlign	Context alignment
 * @param {Object} bodyBuilder	Builder function that builds up the body content of the panel (optional)
 * 
 */
CCLClass._create_text_panel = function(id, titleImg, context, panelAlign, contextAlign, bodyBuilder) {

    var textPanel = new CCLPanel(id, titleImg);

    if (context) {
        textPanel.cfg.setProperty("context", [context]);
        textPanel.panelAlign = panelAlign;
        textPanel.contextAlign = contextAlign;
    }

    /* Allow customized panel decorator when operation completed */
    if (typeof(bodyBuilder) == 'function') {
    	textPanel.builder = bodyBuilder;
    } else {
    	textPanel.builder = function(text) {
	        this.makeBody(text,
	            this.createLink("contShopping"),
	            this.createLink("visitLists")
	        );
    	};
    }

    
	/**
	 * First state - draw panel
	 * Next state: "shown"
	 * 
	 * @param {Object} self
	 */
    textPanel.states["init"] = function(self) {
        self.cfg.setProperty("width", "250px");

        self.createHeader(self.createTitleImg(titleImg));
                                    
        self.makeBody(
            self.createText("EnterNewList"),
            self.createTextInput("nameInput"),
            self.createLink("contLink")
        );

        self.initTextValue(self);

        self.render(document.body);
        self.show();
        self.currentState = "shown";
		
		// put caret to input field
        self.sources.nameInput.focus();
    };


	/**
	 * Receives input and triggers AJAX operation
	 * Next state: "wait_response"
	 * 
	 * @param {Object} self
	 * @param {Object} event_type
	 * @param {Object} orig
	 */
    textPanel.states["shown"] = function(self, event_type, orig) {
        if ((event_type == "textenter" && orig == self.sources.nameInput) || 
             (event_type == "click" && orig == self.sources["contLink"])) {
            // Let's trigger AJAX op
			
			// change panel display
            self.makeBody(self.createWaitAnimation());
			
			// go
            var value = self.sources["nameInput"].value;
            self.executeOperation(self);

            self.currentState = "wait_response";
        }
    };


	/**
	 * In this phase ajax response is processed
	 * 
	 * @param {Object} self
	 * @param {Object} event_type
	 * @param {Object} orig
	 * @param {Object} args
	 */
    textPanel.states["wait_response"] = function(self, event_type, orig, args) {
        if (event_type == "json") {
            var result = args[0];
            var exception = args[1];
                
            if (exception != null) {
                // ERROR BRANCH
				// Ugly errors happen
                switch (self.handleJSONListNameException(result, exception)) {
                    case CCLPanel.JSON_NameEmpty:
                		var link = "EnterName";
            			if (self.NeedsName)
							link = self.NeedsName;
                        var text = self.createText(link);
                        break;
                    case CCLPanel.JSON_NameExists:
                        var text = self.createText("YouAlreadyHaveList");
                        break;
                    case CCLPanel.JSON_NameTooLong:
                        var text = self.createText("NameTooLong");
                        break;
		            case CCLPanel.JSON_SessionTimeout:
		                return;
                    default:
                        var text = self.createText("EnterNewList"); // This should not happen, fail gracefully
                }
                self.makeBody(
                    text,
                    self.createTextInput("nameInput"),
                    self.createLink("contLink")
                );
                self.currentState = "shown";
                self.sources["nameInput"].focus();
				
				// END
                return;
            } else if (self.close_on_success) {
				// 'close_on_success' flag is set
				// so hide panel

                self.hide();
                window.location.reload();
				
				// END
                return;
            }

            var text = self.createResultText(self, result); 

            try {
            	self.builder(text);
            } catch(e) {
            	alert(e);
            }

            self.currentState = "complete";
        }
    };
    
	/**
	 * Final state, handle 
	 * 
	 * @param {Object} self
	 * @param {Object} event_type
	 * @param {Object} orig
	 */
    textPanel.states["complete"] = function(self, event_type, orig) {
        if (event_type == "click") {
            if (orig == self.sources.visitLists) {
                window.location.href = CCLLinkTargets.target("visitLists");
            }
            if (orig == self.sources.contShopping) {
                window.location.reload();
            }
            self.hide();
        }
    };
    
    return textPanel;
};

/** Show intro */
CCLClass.prototype.show_intro = function(context) {
    if (YAHOO.com.freshdirect.ccl.introPanel) {
        // If the panel was created before, reuse it
        var introPanel = YAHOO.com.freshdirect.ccl.introPanel;
        introPanel.setContext(context);
        introPanel.startSM();
        return false;
    }
        
    // Create the panel
    var introPanel = new CCLPanel("CCL_introPanel", "list_help_title.gif");

    if (context) {
        introPanel.cfg.setProperty("context", [context]);
    }

    introPanel.noNeedToLogin = true;
    introPanel.states["init"] = function(self) {
        var helpFunc = self.cfg.getProperty("help");
        self.render(document.body);
        helpFunc();
        self.show();
    };

    YAHOO.com.freshdirect.ccl.introPanel = introPanel;

    introPanel.startSM();

    return false;
};

/** Create List 
 * Shows a popup and asks for the name of the list
 */
CCLClass.prototype.create_list = function(close_on_success, context) {
    if (YAHOO.com.freshdirect.ccl.createPanel) {
        // If the panel was created before, reuse it
        var createPanel = YAHOO.com.freshdirect.ccl.createPanel;
        YAHOO.com.freshdirect.ccl.createPanel.setContext(context);
        createPanel.startSM();
        return false;
    }

        
    // Create the panel
    var createPanel = CCLClass._create_text_panel(
        "CCL_createPanel",
        "list_create_title.gif", 
        context,
        YAHOO.widget.Overlay.BOTTOM_LEFT,
        YAHOO.widget.Overlay.TOP_RIGHT
    );
   
    YAHOO.com.freshdirect.ccl.createPanel = createPanel;
    createPanel.NeedsName = "NewNeedsName";

    if (close_on_success) {
        createPanel.close_on_success = true;
    }
    
    createPanel.initTextValue = function(self) {
        self.sources["nameInput"].value = "";
    };

    createPanel.executeOperation = function(self) {
        self.JSON.CCLFacade.createList(self, self.sources["nameInput"].value);        
    };

    createPanel.createResultText = function(self, result) {
        return self.createText("New list: <b>"+result+"</b> created!", "text11");
    };
        
    createPanel.startSM();

    return false;
};


/** Delete List 
 * Shows a popup and asks for the name of the list
 */
CCLClass.prototype.delete_list = function(listName, context) {
    // Reuse existing panel
    if (YAHOO.com.freshdirect.ccl.deletePanel) {
        YAHOO.com.freshdirect.ccl.deletePanel.setContext(context);        
        YAHOO.com.freshdirect.ccl.deletePanel.startSM();
        return false;
    }
    
    var deletePanel = new CCLPanel("CCL_deletePanel", "list_delete_title.gif");
    if (context) {
        deletePanel.cfg.setProperty("context", [context]);
    }   

    YAHOO.com.freshdirect.ccl.deletePanel = deletePanel;        

    deletePanel.states["init"] = function(self) {
        self.cfg.setProperty("width", "250px");
        self.createHeader(self.createTitleImg("list_delete_title.gif"));

        self.makeBody(self.createText("Are you sure you want to delete "+escapeHTML(listName)+"?", "text11rbold"),
                      self.createLink("deleteList"),
              self.createLink("cancel"));

        self.render(document.body);
        self.show();            
        self.currentState = "shown";
    };

    deletePanel.states["shown"] = function(self, event_type, orig) {
        if (event_type == "click" && orig == self.sources["deleteList"]) {
            // Let's JSON
            self.makeBody(self.createWaitAnimation());
            self.JSON.CCLFacade.deleteList(self, listName);            
            self.currentState = "wait_response";
        } else if (orig == self.sources.cancel) {
        self.hide();
    }
    };
    
    deletePanel.states["wait_response"] = function(self, event_type, orig, args) {
        if (event_type == "json") {
            var result = args[0];
            var exception = args[1];
            
            self.handleJSONException(result, exception);
            
            self.hide();
            window.location.href = CCLLinkTargets.target("visitLists");
        }
    };
        
    deletePanel.startSM();

    return false;
};


CCLClass.prototype.rename_list = function(listname, context) {
    if (YAHOO.com.freshdirect.ccl.renamePanel) {
        YAHOO.com.freshdirect.ccl.renamePanel.oldListName = listname;
        YAHOO.com.freshdirect.ccl.renamePanel.setContext(context);
        YAHOO.com.freshdirect.ccl.renamePanel.startSM();
        return false;
    }
        
    var renamePanel = new CCLClass._create_text_panel(
        "CCL_renamePanel",
        "list_rename_title.gif",
        context,
        YAHOO.widget.Overlay.TOP_LEFT,
        YAHOO.widget.Overlay.BOTTOM_RIGHT
    );
                
    YAHOO.com.freshdirect.ccl.renamePanel = renamePanel;        
    renamePanel.oldListName = listname;
    renamePanel.close_on_success = false;
    renamePanel.NeedsName = "NeedsName";

    renamePanel.initTextValue = function(self) {
        self.sources["nameInput"].value = self.oldListName;
    };
    
    renamePanel.executeOperation = function(self) {
        self.JSON.CCLFacade.renameList(self, self.oldListName, self.sources["nameInput"].value);        
    };
    
    renamePanel.createResultText = function(self, result) {
        return self.createText("List has been renamed to <b>"+result+"</b>", "text11");
    };
    
    renamePanel.startSM();

    return false;
};

/* Rename a Standing Order list */
CCLClass.prototype.rename_so_list = function(listname, context) {
    if (YAHOO.com.freshdirect.ccl.renameSOPanel) {
        YAHOO.com.freshdirect.ccl.renameSOPanel.oldListName = listname;
        YAHOO.com.freshdirect.ccl.renameSOPanel.setContext(context);
        YAHOO.com.freshdirect.ccl.renameSOPanel.startSM();
        return false;
    }
        
    var renamePanel = new CCLClass._create_text_panel(
        "CCL_renamePanel",
        "list_rename_title.gif",
        context,
        YAHOO.widget.Overlay.TOP_LEFT,
        YAHOO.widget.Overlay.BOTTOM_RIGHT,
        function(txt) {
            this.makeBody(txt,
                this.createLink("contShopping")
            );
        }
    );
                
    YAHOO.com.freshdirect.ccl.renameSOPanel = renamePanel;        
    renamePanel.oldListName = listname;
    renamePanel.close_on_success = false;
    renamePanel.NeedsName = "NeedsName";

    renamePanel.initTextValue = function(self) {
        self.sources["nameInput"].value = self.oldListName;
    };
    
    renamePanel.executeOperation = function(self) {
        self.JSON.CCLFacade.renameSOList(self, self.oldListName, self.sources["nameInput"].value);        
    };
    
    renamePanel.createResultText = function(self, result) {
        return self.createText("List has been renamed to <b>"+result+"</b>", "text11");
    };
    
    renamePanel.startSM();

    return false;
};


/**
 * [APPDEV-141] Change frequency of a standing order
 * 
 * @param {Object} listname Name of Standing Order list
 * @param {Object} context Current script scope
 */
CCLClass.prototype.change_so_frequency = function(soId, freq, nextDlvDate, context) {
	// check if panel already created
    if (YAHOO.com.freshdirect.ccl.changeFrqPanel) {
        YAHOO.com.freshdirect.ccl.changeFrqPanel.setContext(context);

		// init panel
		YAHOO.com.freshdirect.ccl.changeFrqPanel.soId = soId; // FIXME
		YAHOO.com.freshdirect.ccl.changeFrqPanel.freq = freq; //FIXME

        YAHOO.com.freshdirect.ccl.changeFrqPanel.startSM();
        return false;
    }


	var titleImg = "so_chgfrq_title.gif";
    var aPanel = new CCLClass._create_text_panel(
        "CCL_chgFrqPanel",
        titleImg,
        context,
        YAHOO.widget.Overlay.TOP_LEFT,
        YAHOO.widget.Overlay.BOTTOM_RIGHT,
        function(txt) {
            this.makeBody(txt,
                this.createLink("contShopping")
            );
        }
    );
	
	
	
	// cache panel
	YAHOO.com.freshdirect.ccl.changeFrqPanel = aPanel;

	// init panel
	aPanel.soId = soId; // FIXME
	aPanel.freq = freq; //FIXME

    
	aPanel.states["init"] = function(self) {
        self.cfg.setProperty("width", "250px");
        self.createHeader(self.createTitleImg(titleImg));
                                    
        self.makeBody(
			function(text, style) {
			    var elem = document.createElement("div");
			    elem.innerHTML = text;
			    styleStr = "ccl-panel-text";
			    if (style)
					styleStr = styleStr + " " + style;
			    YAHOO.util.Dom.addClass(elem, styleStr); 
			    return elem;
			}("Select delivery frequency for orders after " + nextDlvDate, 'text11'),
            self.createLink("contLink"),
            self.createLink("cancel")
        );

        // self.initTextValue(self);

        self.render(document.body);
        self.show();
		
        self.currentState = "retrieve_lists";
        self.handleStateEvent("auto", self);

		// put caret to input field
        // self.sources.nameInput.focus();
	};
	
    aPanel.states["shown"] = function(self, event_type, orig) {
        if (event_type == "click" && orig == self.sources["dropdown"]) {
			var freqOrd = self.sources.dropdown.value;

            // User picked a frequency
            self.makeBody(self.createWaitAnimation());
			
			// let FD know
	        YAHOO.util.Connect.asyncRequest(
	            "POST",
	            "/api/so_api.jsp",
	            {
	                success: function(resp) {
						var ptr = resp.argument.ptr;

						ptr.makeBody(
							ptr.createText(resp.responseText, 'text11'),
							ptr.createLink("contLink")
						);
					},
	                failure: function(resp) {
			            var ptr = resp.argument.ptr;
			            ptr.makeBody(
							resp.responseText ? ptr.createText(resp.responseText, "text11rbold") : ptr.createText("ServiceError"),
			                ptr.createLink("errorClose")
			            );
			            ptr.currentState = "error";
			        },
	                argument: { ptr: self}
	            },
	            "action=setFrequency&soId="+self.soId+"&freqOrd="+freqOrd
	        );
			
			self.currentState = "complete";
        } else if (orig == self.sources.cancel) {
        	self.hide();
		}
    };


    aPanel.states["retrieve_lists"] = function(self, event_type) {
        self.get_list_items(self);
		
		// move forth
        self.currentState = "shown";
    };

    aPanel.states["complete"] = function(self, event_type, orig) {
        if (event_type == "click") {
            if (orig == self.sources.visitLists) {
                window.location.href = CCLLinkTargets.target("visitLists");
            }
            if (orig == self.sources.contLink) {
                window.location.reload();
            }
            self.hide();
        }
    };
	
	aPanel.executeOperation = function(self) {};

	// retrieve enums
	aPanel.get_list_items = function(self) {
        YAHOO.util.Connect.asyncRequest(
            "POST",
            "/api/so_api.jsp",
            {
                success: function(resp) {
					var self = resp.argument.ptr;
					
			        if (!resp.responseText) {
			            self.unexpectedJSONResponse(ptr);
			            return;
			        }
					

					var enumList = eval('(' + resp.responseText + ')');

					var vals = [];
					var texts = [];
					for ( i = 0; i < enumList.length; i++ ) {
						vals.push(enumList[i]["_ord"]);
						texts.push(enumList[i].title);
					}
					
			        self.makeBody(
						function(text, style) {
						    var elem = document.createElement("div");
						    elem.innerHTML = text;
						    styleStr = "ccl-panel-text";
						    if (style)
								styleStr = styleStr + " " + style;
						    YAHOO.util.Dom.addClass(elem, styleStr); 
						    return elem;
						}("Select delivery frequency for orders after " + nextDlvDate, 'text11'),
						self.createDropdownList('dropdown', 'text11', texts, vals),
			            self.createLink("contLink"),
			            self.createLink("cancel")
			        );
					
					// set current value
					for (n in enumList) {
						if (Number(enumList[n].frequency) == self.freq) {
							self.sources.dropdown.value = enumList[n]["_ord"];
							break;
						}
					}
					
				},
                failure: function(resp) {
		            var ptr = resp.argument.ptr;
		            ptr.makeBody(
		                resp.responseText ? ptr.createText(resp.responseText, "text11rbold") : ptr.createText("ServiceError"),
		                ptr.createLink("errorClose")
		            );
		            ptr.currentState = "error";
		        },
                argument: { ptr: self}
            },
            "action=getFrequencyList"
        );
	};

	aPanel.startSM();


	// stop JS event bubbling
    return false;
};




/**
 * Shows up a delete SO panel
 * 
 * @param {Object} soId Standing Order ID
 * @param {Object} scope
 */
CCLClass.prototype.delete_so = function(soId, scope) {
	// check if panel already created
    if (YAHOO.com.freshdirect.ccl.deleteSoPanel) {
        YAHOO.com.freshdirect.ccl.deleteSoPanel.setContext(scope);

		// init panel
		YAHOO.com.freshdirect.ccl.deleteSoPanel.soId = soId;

        YAHOO.com.freshdirect.ccl.deleteSoPanel.startSM();
        return false;
    }


	var titleImg = "so_delorder_title.gif";
    var aPanel = new CCLClass._create_text_panel(
        "CCL_delSOPanel",
        titleImg,
        scope,
        YAHOO.widget.Overlay.TOP_LEFT,
        YAHOO.widget.Overlay.BOTTOM_RIGHT
    );
	
	
	
	// cache panel
	YAHOO.com.freshdirect.ccl.deleteSoPanel = aPanel;

	// init panel
	aPanel.soId = soId;


	aPanel.states["init"] = function(self) {
        self.cfg.setProperty("width", "250px");
        self.createHeader(self.createTitleImg(titleImg));
                                    
        self.makeBody(
			self.createText("DelOrderTitle"),
			self.createText("DelOrderBody"),
            self.createLink("delOrderLink"),
            self.createLink("cancel")
        );

        // self.initTextValue(self);

        self.render(document.body);
        self.show();
		
        self.currentState = "shown";
        self.handleStateEvent("auto", self);

		// put caret to input field
        // self.sources.nameInput.focus();
	};
	
	aPanel.states["shown"] = function(self, event_type, orig) {
        if (event_type == "click") {
			if (orig == self.sources["delOrderLink"]) {
	            self.makeBody(self.createWaitAnimation());
		        YAHOO.util.Connect.asyncRequest(
		            "POST",
		            "/api/so_api.jsp",
		            {
		                success: function(resp) {
							var ptr = resp.argument.ptr;
	
							ptr.makeBody(
								ptr.createText("DelOrderConfirm"),
								ptr.createLink("contLink")
							);
						},
		                failure: function(resp) {
				            var ptr = resp.argument.ptr;
				            ptr.makeBody(
				                resp.responseText ? ptr.createText(resp.responseText, "text11rbold") : ptr.createText("ServiceError"),
				                ptr.createLink("errorClose")
				            );
				            ptr.currentState = "error";
				        },
		                argument: { ptr: self}
		            },
		            "action=delete&soId="+self.soId
		        );

				// self.currentState = "complete";
			} else if (orig == self.sources["contLink"]) {
				window.location = "/quickshop/standing_orders.jsp";
				self.hide();
			} else if (orig == self.sources["cancel"]) {
				self.hide();
			}
		}
	}

	aPanel.startSM();
	return false;
};



CCLClass.prototype.shift_so_delivery = function(soId, dlv, scope) {
	// check if panel already created
    if (YAHOO.com.freshdirect.ccl.shiftDlvPanel) {
        YAHOO.com.freshdirect.ccl.shiftDlvPanel.setContext(scope);

		// init panel
		YAHOO.com.freshdirect.ccl.shiftDlvPanel.soId = soId;
		YAHOO.com.freshdirect.ccl.shiftDlvPanel.dlv = dlv;

        YAHOO.com.freshdirect.ccl.shiftDlvPanel.startSM();
        return false;
    }


	var titleImg = "so_shiftdlv_title.gif";
    var aPanel = new CCLClass._create_text_panel(
        "CCL_shiftdlvSOPanel",
        titleImg,
        scope,
        YAHOO.widget.Overlay.TOP_LEFT,
        YAHOO.widget.Overlay.BOTTOM_RIGHT
    );
	
	
	
	// cache panel
	YAHOO.com.freshdirect.ccl.shiftDlvPanel = aPanel;

	// init panel
	aPanel.soId = soId;
	aPanel.dlv = dlv;


	aPanel.states["init"] = function(self) {
        self.cfg.setProperty("width", "250px");
        self.createHeader(self.createTitleImg(titleImg));
                                    
        self.makeBody(
			function(text, style) {
			    var elem = document.createElement("div");
			    elem.innerHTML = text;
			    styleStr = "ccl-panel-text";
			    if (style)
					styleStr = styleStr + " " + style;
			    YAHOO.util.Dom.addClass(elem, styleStr); 
			    return elem;
			}("Your upcoming delivery is " + dlv, 'title12'),
            self.createLink("skipDlvLink"),
            self.createLink("skipCancelLink")
        );

        self.render(document.body);
        self.show();
		
        self.currentState = "shown";
        self.handleStateEvent("auto", self);
	};
	
	aPanel.states["shown"] = function(self, event_type, orig) {
        if (event_type == "click") {
			if (orig == self.sources["skipDlvLink"]) {
	            self.makeBody(self.createWaitAnimation());
		        YAHOO.util.Connect.asyncRequest(
		            "POST",
		            "/api/so_api.jsp",
		            {
		                success: function(resp) {
							var ptr = resp.argument.ptr;
	
							ptr.makeBody(
								ptr.createText(resp.responseText, "title12"),
								ptr.createLink("contLink")
							);
						},
		                failure: function(resp) {
				            var ptr = resp.argument.ptr;
				            ptr.makeBody(
				                resp.responseText ? ptr.createText(resp.responseText, "text11rbold") : ptr.createText("ServiceError"),
				                ptr.createLink("errorClose")
				            );
				            ptr.currentState = "error";
				        },
		                argument: { ptr: self}
		            },
		            "action=skip&soId="+self.soId
		        );

				// self.currentState = "complete";
			} else if (orig == self.sources["skipCancelLink"]) {
				self.hide();
			} else if (orig == self.sources["contLink"]) {
				window.location.reload();
				self.hide();
			}
		}
	}

	aPanel.startSM();
	return false;
};

/**
 * Creates a panel with a drop down list option
 * 
 * Properties:
 *    this.hiddenParams : set hidden form params here
 *    this.the_list		: Name of selected list
 *    this.items		: 
 * 
 * The following methods must be added to the created panel:
 *   get_list_items(self)	- Triggers JSON RPC Action to retrieve list of items
 *   modify_list(self)		- 
 * 
 * @param {Object} id			DOM ID
 * @param {Object} titleImg		Title image file
 * @param {Object} context		Current JS scope
 */
CCLClass.prototype._create_list_panel = function(id, titleImg, context) {
    var listPanel =  new CCLPanel(id, titleImg);   
   
    if (context) {
        listPanel.cfg.setProperty("context", [context]);
    } 

    listPanel.unexpectedJSONResponse = function(self) {
      
        self.makeBody(
            self.createText("UnexpectedJSONResponse"),
            self.createLink("errorClose")
        );

        self.currentState = "error";
    };

    // params expected to be in the form of a URL query string
    // i.e. name1=value1&name2=value2
    listPanel.addHiddenParams = function(self,params) {
       if (!params) return;
       var form = document.getElementById(self.form_id);
       var paramArray = {};
       paramArray = params.split('&');
       var parmDef = {};
       
       for(i = 0; i< paramArray.length; ++i) {
           paramDef = paramArray[i].split('=');
           if (paramDef.length != 2) continue;
           input = document.createElement("input");
           input.type = "hidden";
           input.name = paramDef[0];
           input.value = paramDef[1];
           form.appendChild(input);
       }
    };

    
    

    /**
     * Initialize list panel states
     */
    // INIT
    listPanel.states["init"] = function(self) {
        self.cfg.setProperty("width", "250px");
        self.createHeader(self.createTitleImg(titleImg));

        self.the_list = "";			// name of selected list
        self.the_list_type = "";	// type of selected list

        self.makeBody(self.createWaitAnimation());


        self.render(document.body);
        self.show();                    

        // callback to handle the case when the asynchronous service
        // that checks the items selected fails.
        // It is different from the situation when the service
        // returns an error (e.g. wrong configuration)
        var check_failed = function(resp) {
            var ptr = resp.argument.ptr;
            ptr.makeBody(
                ptr.createText("ServiceError"),
                ptr.createLink("errorClose")
            );

            ptr.currentState = "error";
        };

        // submit the form to check the selection

        // 1. Set the form
        YAHOO.util.Connect.setForm(document.getElementById(self.form_id));

        // 2. Call the service asynchronosly
        //    The success function must be declared by the panel
        //    it must also set the current state in cases of
        //    pass and error. 
        YAHOO.util.Connect.asyncRequest(
            "POST",
            CCLLinkTargets.target("selection_check"),
            { 
                success: self.process_selection_check, 
                failure: check_failed,
                argument: { ptr     : self,
                            hiddens : self.hiddenParams}
            },
            self.qsParams
        );

    }; // states["init"]


    // RETRIEVE_LISTS
    listPanel.states["retrieve_lists"] = function(self, event_type) {
        self.get_list_items(self);
        self.currentState = "wait_lists_response";
    };


    // WAIT_LISTS_RESPONSE
    listPanel.states["wait_lists_response"] = function(self, event_type, orig, args) {
        if (event_type == "json") {
            var result = args[0];
            var exception = args[1];
        
            self.handleJSONExceptionNoLogin(result, exception);
        
            if (result == null || result.listNames == null) { 
                // some weird error, user must have at least one list
                self.handleJSONException(result, "weird error");                
                return ;
            } 

			// if list is empty
            if (result.listNames.length == 0) {
                self.makeBody(
                    self.createText("YouDontHaveAList"),
                    self.createText("EnterName"),
                    self.createTextInput("nameInput"),
                    self.createLink("contLink")
                );
                self.currentState = "shown_create_list";
                return;
            }

            // save automatically on one list, then do that
            if (self.autoSaveOnOneItem && result.listNames.length == 1) {
                self.the_list = result.listNames[0][0];
                self.the_list_type = result.listNames[0][2];
                self.currentState = "wait_save_response";
                self.modify_list(self);
                return;
            }
                        
            self.computeDropDownMembers(result.listNames);
                      
            self.makeBody(
                self.createText("ChooseList"),
                self.createDropdownList("dropdown","text11",self.dropdownLabels, self.dropdownValues),
                self.createLink("contLink")
            );

            if (result.mostRecentList) {              
                self.sources.dropdown.value = result.mostRecentList;
            }			

            self.currentState = "shown_list";
        } // if event_type == "json"
    };


    // SHOWN_LISTS
    listPanel.states["shown_list"] = function(self, event_type, orig) {
        if (event_type != "click") return;

        // dropdown click
        if (orig == self.sources.dropdown) {
            if (self.sources.dropdown.value == "__NEW__") {
                var oldValue = self.sources.dropdown.value;             
                self.makeBody(
                    self.createText("ChooseList"),
                    self.createDropdownList("dropdown","text11",self.dropdownLabels, self.dropdownValues),
                    self.createText("EnterNewList"),
                    self.createTextInput("nameInput"),                          
                    self.createLink("contLink")
                );
                self.sources.dropdown.value = oldValue;
                self.sources.nameInput.value = "";
                self.sources.nameInput.focus();
                self.currentState = "shown_create_list";
             } 
        } else if (orig == self.sources["contLink"]) {
            self.the_list = self.sources["dropdown"].value;
            self.the_list_type = self.dropdownTypes[self.the_list];
            self.makeBody(self.createWaitAnimation());
            self.modify_list(self);
            self.currentState = "wait_save_response";
        }
    };


    // SHOWN_CREATE_LIST
    listPanel.states["shown_create_list"] = function(self, event_type, orig) {
        // dropdown click
        if (event_type == "click" && orig == self.sources.dropdown) {
            // make the input text disappear
            if (self.sources["dropdown"].value != "__NEW__") {
                var oldValue = self.sources.dropdown.value;             
                self.makeBody(
                    self.createText("ChooseList"),
                    self.createDropdownList("dropdown","text11",self.dropdownLabels, self.dropdownValues),
                    self.createLink("contLink")
                );
                self.sources.dropdown.value = oldValue;
                self.currentState = "shown_list";
            }         
        } else if ((event_type == "click" && orig == self.sources["contLink"]) ||
                 (event_type == "textenter" && orig == self.sources.nameInput)) {
            if (self.sources.dropdown) {
                self.dropdownValue_ = self.sources.dropdown.value;
            }
            self.makeBody(self.createWaitAnimation());
            self.JSON.CCLFacade.createList(self, self.sources.nameInput.value);
            self.currentState = "wait_create_response";
        }
    };


    // WAIT_SAVE_RESPONSE
    listPanel.states["wait_save_response"] = function(self, event_type, orig, args)  {
        if (event_type == "json") {
            var result = args[0];
            var exception = args[1];

            self.handleJSONExceptionNoLogin(result, exception);

            self.listId = result.listID;
            self.lineItems = result.selection; // store 'selection' of line items

            if (self.id == "CCL_adoptListPanel") {
                self.makeBodyL(
                    self.createText(
                        "New list: <b>" + escapeHTML(self.the_list) +
                           "</b> created!", "text11"),
                    self.createBulletedList(
                        self.createLink("keepShopping"),
                        self.createLink("visitLists")
                    )
                );
            } else {
                self.makeBodyL(
                    // 'YOU HAVE JUST SAVED:' image
                    function() {
                        var el = document.createElement("IMG");
                        YAHOO.util.Dom.addClass(el, "ccl-confirm-hdr");
                        el.src = "/media_stat/ccl/lists_confirm_hdr.gif";
                        return el;
                    }.call(self),
                    // product name or 'N items'
                    function() {
                        var txt = "???";

                        var arr = []; // saved items                        
                        if (this.items) {
                            // check selection sets 'this.items'
                            arr = this.items.selection;
                        } else {
                            // otherwise use line items array
                            arr = this.lineItems;
                        }
                        txt = (arr.length == 1 ? arr[0].fullName : arr.length + " items");

                        var el = document.createElement("div");
                        YAHOO.util.Dom.addClass(el, "title18 prod-name-text");
                        el.innerHTML = escapeHTML(txt);
                        return el;                    
                    }.call(self),
                    // list name
                    function(txt) {
                        var el = document.createElement("div");
                        YAHOO.util.Dom.addClass(el, "text13 list-text");
                        el.innerHTML = "to <span id='CCL_listName'>" + escapeHTML(txt) + "</span>";
                        return el;
                    }.call(self, self.the_list),
                    // 'edit' button
                    function() {
                        if (this.lineItems.length != 1) {
                            return document.createComment("no edit button");
                        }

                        var qarr = [];
                        var it = this.lineItems[0];
                        var z;


                        // collect a query string to 'edit' button
                        qarr.push('skuCode=' + escape(it.skuCode));
                        qarr.push('catId=' + escape(it.categoryID));
                        qarr.push('productId=' + escape(it.productID));
                        qarr.push('ccListId=' + escape(this.listId));
                        qarr.push('lineId=' + escape(it.lineID));
                        qarr.push('qcType=' + escape(self.the_list_type));
                        if (it.recipeSourceId) qarr.push('recipeId=' + escape(it.recipeSourceId));

                        flattenHash(it.configuration, qarr);
                        
                        var el = document.createElement("A");
                        YAHOO.util.Dom.addClass(el, "ccl-edit-btn");

                        var link = CCLLinkTargets.targets.modifyItem;
                        el.setAttribute("href", link + "?action=CCL:ItemManipulate&" + qarr.join("&"));
                        
                        var img = document.createElement("IMG");
                        img.src = "/media_stat/images/buttons/edit_product.gif";

                        el.appendChild(img);    
                        return el;
                    }.call(self),
                    // bulleted list of links
                    self.createBulletedList(
                        self.createLink("keepShopping"),
                        self.the_list_type == "SO" ? self.createLink("visitThisSOList") : self.createLink("visitThisList"),
                        self.createLink("createAnotherList")
                    )
                );
            }
            self.currentState = "complete";
        }
    };


    // WAIT_CREATE_RESPONSE
    listPanel.states["wait_create_response"] = function(self, event_type, orig, args) {
        if (event_type == "json") {
            var result = args[0];
            var exception = args[1];

            if (exception != null) {
                /*
                 * Handle incoming exception
                 */
                switch (self.handleJSONListNameException(result, exception)) {
                    case CCLPanel.JSON_NameEmpty:
                        var link = "EnterName";
                        if (self.NeedsName) link = self.NeedsName;
                        var text = self.createText(link);
                        break;
                    case CCLPanel.JSON_NameExists:
                        var text = self.createText("YouAlreadyHaveList");
                        break;
                    case CCLPanel.JSON_NameTooLong:
                        var text = self.createText("NameTooLong");
                        break;                        
                    default:
                        var text = self.createText("EnterNewList"); // This should not happen, fail gracefully
                }
                
                /*
                 * Create response panel
                 */
                if (self.id == "CCL_adoptListPanel") {
                   self.makeBody(
                       text,
                       self.createTextInput("nameInput"), 
                               self.createLink("contLink")
                   );
                } else {
                    /*
                     * Create a drop down list of complementer shopping lists
                     */
                    self.makeBody(
                        self.createText("ChooseList"),
                        function() {
                            if (typeof(this.dropdownLabels) != "undefined" && typeof(this.dropdownValues) != "undefined") {
                                return this.createDropdownList("dropdown", "text11", this.dropdownLabels, this.dropdownValues);
                            } else {
                                // TODO: this is a hack to fix CCL-89. Should handle this issue gracefully
                                // This case will happen if complementer list is empty
                                return this.createDropdownList("dropdown", "text11", ["CREATE NEW LIST"], ["__NEW__"]);
                            }
                        }.call(self),
                        text,
                        self.createTextInput("nameInput"),                          
                        self.createLink("contLink")
                    );
                    self.sources.dropdown.value = self.dropdownValue_;
                }
                self.sources.nameInput.focus();
                self.currentState = "shown_create_list";
                return ;
            }
            self.the_list = result;
            // self.the_list_type = self.dropdownTypes[self.the_list];
            self.the_list_type = "CCL";

            self.modify_list(self);
            self.currentState = "wait_save_response";
        } // json
    }; // states["wait_create_response"]


    // COMPLETE
    listPanel.states["complete"] = function(self, event_type, orig)  {
        if (event_type == "click") {
            self.hide();
            if (orig == self.sources["visitThisList"]) {
				window.location.href = CCLLinkTargets.target("visitThisList") + "?ccListId=" + self.listId;
			} else if (orig == self.sources["visitThisSOList"]) {
                window.location.href = CCLLinkTargets.target("visitThisSOList") + "?ccListId=" + self.listId;
            } else if (orig == self.sources["createAnotherList"]) {
                CCL.create_list(false, context);
            } else if (orig == self.sources["keepShopping"] &&
                self.id == "CCL_adoptListPanel") {
                window.location.reload();
           }
        }
    };

    return listPanel;
};






/** Save items to a list
  *
  * The function will submit the form to the check_selection.jsp service
  * asynchronously. This will only succeed if there were no errors in the
  * selection (e.g.invalid configuration). On success, the list names
  * will be retrieved and the user can select where the items should be saved.
  *
  * Errors are handled ubiquitously; the form is resubmitted so the controller
  * tag can catch the errors. The hiddenParams can supply extra context dependent
  * information as hidden input elements.
  *
  * qsParams and hiddenParams are expected to be in URL query string format, 
  * (i.e. name1=value1&name2=value2). qsParams will be submitted to the 
  * selection checking service while hiddenParams are only considered when
  * an error occurs and the form is (re)submitted to the original service.
  * Currently errors are handled by the controller tag which embeds the form,
  * but these hidden parameters can supply additional information to the controller
  * tag with the submission.
  *
  * @param form_id the id element of the form which contains the selection of items
  * @param config list of properties. It must contain a 'context' property having the actual calling context (i.e. this)
  * @param qsParam any additional parameter needed for checking the selection
  * @param hiddenParam any additional parameter supplied when resubmitting the form
  */
CCLClass.prototype.save_items = function(form_id, context, qsParams, hiddenParams) {    
    if (YAHOO.com.freshdirect.ccl.savePanel) {
        YAHOO.com.freshdirect.ccl.savePanel.form_id = form_id;
        YAHOO.com.freshdirect.ccl.savePanel.qsParams = qsParams;
        YAHOO.com.freshdirect.ccl.savePanel.hiddenParams = hiddenParams;
        YAHOO.com.freshdirect.ccl.savePanel.startSM();
        return false;
    }

    var savePanel = this._create_list_panel("CCL_savePanel",  "shopping_lists_title.gif", context);
    YAHOO.com.freshdirect.ccl.savePanel = savePanel;

    // If there is only one list, go ahead and save
    savePanel.autoSaveOnOneItem = true;
    savePanel.form_id = form_id;
    savePanel.qsParams = qsParams;
    savePanel.hiddenParams = hiddenParams;

    savePanel.process_selection_check = function(resp) {
        // ptr is the callback parameter "self" of savePanel
        var ptr = resp.argument.ptr;
        if (!resp.responseText) {
            ptr.unexpectedJSONResponse(ptr);
            return;
        }
        var jsonObject = eval('(' + resp.responseText + ')');

        // sanity check, we are waiting for a save selection response
        if (jsonObject.type != "save_selection") {
	        if (jsonObject.type == "no_session")
				ptr.handleTimeoutException();
            else
				ptr.unexpectedJSONResponse(ptr);
            return;
        }

        if (jsonObject.errors.length > 0 || jsonObject.warnings.length > 0) {
            // If we want to see some error message before redirection
            // then this should be different
            ptr.addHiddenParams(ptr,resp.argument.hiddens);
            document.getElementById(ptr.form_id).submit();
        } else {
            var itemCount = 0;
	        var item;
            for(item in jsonObject.items)
				++itemCount;
            if (itemCount > 0) {
                ptr.items = jsonObject.items;
                ptr.item_count = itemCount;
				
                ptr.currentState = "retrieve_lists";
                ptr.handleStateEvent("async_check",ptr);
            } else {
                // Again, there are no items, thus resubmit the form
                ptr.addHiddenParams(ptr,resp.argument.hiddens);
                document.getElementById(ptr.form_id).submit();
            }
        }
    };

    savePanel.get_list_items = function(self) {
        self.JSON.CCLFacade.getListNamesWithItemCount(self);
    };

    savePanel.modify_list = function(self) {
        self.JSON.CCLFacade.addItemsToList(
            self,self.the_list, self.the_list_type, self.items
        );
    };

    savePanel.startSM();
    return false;
};


CCLClass.prototype.add_recent_cart_items = function() {
    if (YAHOO.com.freshdirect.ccl.cartPanel) {
        YAHOO.com.freshdirect.ccl.cartPanel.startSM();
        return false;
    }

    var cartPanel =  this._create_list_panel("CCL_cartPanel", "list_save_item_title.gif", null);
    YAHOO.com.freshdirect.ccl.cartPanel = cartPanel;

    cartPanel.states["init"] = function(self) {
        self.cfg.setProperty("width", "250px");
        self.createHeader(self.createTitleImg(self.titleImg));

        self.the_list = "";

        self.makeBody(self.createWaitAnimation());


        self.render(document.body);
        self.show();
        
        // move to new state
        self.currentState = "retrieve_lists";
        self.handleStateEvent("auto", self);
    };
    

    cartPanel.get_list_items = function(self) {
        self.JSON.CCLFacade.getListNamesWithItemCount(self,self.src_listname);
    };

    cartPanel.modify_list = function(self) {
        self.JSON.CCLFacade.addRecentItemsToList(self,self.the_list, self.the_list_type);
    };

    cartPanel.startSM();
    return false;
};



/** Copy items to a list
  *
  * The function will submit the form to the check_selection.jsp service
  * asynchronously. On success, the list names will be retrieved and the 
  * user can select where the items should be copied.
  *
  * Errors are handled ubiquitously; the form is resubmitted so the controller
  * tag can catch the errors. The hiddenParams can supply extra context dependent
  * information (they are actually added as hidden parameters to the form).
  *
  * @param form_id the id element of the form which contains the selection of items
  * @param context the actual calling context (i.e. this)
  * @param qsParams any additional parameters needed for checking the selection
  * @param hiddenParams any additional parameters needed when resubmitting the form
  */
CCLClass.prototype.copy_items = function(form_id, context, qsParams, hiddenParams) {    
    if (YAHOO.com.freshdirect.ccl.copyPanel) {

        YAHOO.com.freshdirect.ccl.copyPanel.form_id = form_id;
        YAHOO.com.freshdirect.ccl.copyPanel.qsParams = qsParams;
        YAHOO.com.freshdirect.ccl.copyPanel.hiddenParams = hiddenParams;

        YAHOO.com.freshdirect.ccl.copyPanel.setContext(context);
        YAHOO.com.freshdirect.ccl.copyPanel.startSM();
        return false;
    }

    var copyPanel =  this._create_list_panel("CCL_copyPanel", "list_save_item_title.gif", context);
    YAHOO.com.freshdirect.ccl.copyPanel = copyPanel;
           
    copyPanel.form_id = form_id;
    copyPanel.qsParams = qsParams;
    copyPanel.hiddenParams = hiddenParams;

    copyPanel.process_selection_check = function(resp) {
        var ptr = resp.argument.ptr;
        if (!resp.responseText) {
            ptr.unexpectedJSONResponse(ptr);
            return;
        }
        var jsonObject = eval('(' + resp.responseText + ')');
        // sanity check, we are waiting for a copy selection response
        if (jsonObject.type != "copy_selection") {
        if (jsonObject.type == "no_session") ptr.handleTimeoutException();
            else ptr.unexpectedJSONResponse(ptr);
            return;
        }

        if (jsonObject.items != null && jsonObject.errors.length == 0 && jsonObject.warnings.length == 0) {
            ptr.src_listname = jsonObject.list_name;
            ptr.items = jsonObject.items;
            ptr.item_count = jsonObject.line_count;
            ptr.currentState = "retrieve_lists";
            ptr.handleStateEvent("async_check",ptr);
        } else {
           ptr.addHiddenParams(ptr,resp.argument.hiddens);
           document.getElementById(ptr.form_id).submit();
        }
    };

    copyPanel.get_list_items = function(self) {
        self.JSON.CCLFacade.getListNamesWithItemCount(self,self.src_listname);
    };

    copyPanel.modify_list = function(self) {
        self.JSON.CCLFacade.addItemsToList(self, self.the_list, self.the_list_type, self.items);
    };

    copyPanel.startSM();
    return false;
};

/** Adopt a start list 
  *
  * The function will submit the form to the check_selection.jsp service
  * asynchronously. This will only succeed if there were no errors in the
  * selection (e.g.invalid configuration). On success, the list names
  * will be retrieved and the user can select where the items should be saved.
  *
  * Errors are handled ubiquitously; the form is resubmitted so the controller
  * tag can catch the errors. The hiddenParams can supply extra context dependent
  * information as hidden input elements.
  *
  * qsParams and hiddenParams are expected to be in URL query string format, 
  * (i.e. name1=value1&name2=value2). qsParams will be submitted to the 
  * selection checking service while hiddenParams are only considered when
  * an error occurs and the form is (re)submitted to the original service.
  * Currently errors are handled by the controller tag which embeds the form,
  * but these hidden parameters can supply additional information to the controller
  * tag with the submission.
  *
  * @param form_id the id element of the form which contains the selection of items
  * @param context the actual calling context (i.e. this)
  * @param def_list the suggested name for the list (the user's first name followed by the original starter list name)
  * @param qsParam any additional parameter needed for checking the selection
  * @param errParam any additional parameter supplied when resubmitting the form
  */

CCLClass.prototype.adopt_list = function(form_id, context, def_list, qsParams, hiddenParams) {
   if (YAHOO.com.freshdirect.ccl.adoptListPanel) {      

        YAHOO.com.freshdirect.ccl.adoptListPanel.form_id = form_id;
        YAHOO.com.freshdirect.ccl.adoptListPanel.def_list = def_list;
        YAHOO.com.freshdirect.ccl.adoptListPanel.qsParams = qsParams;
        YAHOO.com.freshdirect.ccl.adoptListPanel.hiddenParams = hiddenParams;

        YAHOO.com.freshdirect.ccl.adoptListPanel.setContext(context);
        YAHOO.com.freshdirect.ccl.adoptListPanel.startSM();
        return false;
    }

    var adoptListPanel =  this._create_list_panel("CCL_adoptListPanel", "adopt_diag_title.gif", context);
    YAHOO.com.freshdirect.ccl.adoptListPanel = adoptListPanel;

    adoptListPanel.form_id = form_id;
    adoptListPanel.def_list = def_list;
    adoptListPanel.qsParams = qsParams;
    adoptListPanel.hiddenParams = hiddenParams;

    adoptListPanel.process_selection_check = function(resp) {
        // ptr is the callback parameter "self" of savePanel
        var ptr = resp.argument.ptr;
        if (!resp.responseText) {
            ptr.unexpectedJSONResponse(ptr);
            return;
        }
        var jsonObject = eval('(' + resp.responseText + ')');
        // sanity check, we are waiting for a save selection response
        if (jsonObject.type != "save_selection") {
        if (jsonObject.type == "no_session") ptr.handleTimeoutException();
            else ptr.unexpectedJSONResponse(ptr);
            return;
        }
        if (jsonObject.errors.length > 0 || jsonObject.warnings.length > 0) {
            // If we want to see some error message before redirection
            // then this should be different
            ptr.addHiddenParams(ptr,resp.argument.hiddens);
            document.getElementById(ptr.form_id).submit();
        } else {
            var itemCount = 0;
        var item;
            for(item in jsonObject.items) ++itemCount;
            if (itemCount > 0) {
            var input = ptr.createTextInput("nameInput");
        input.value = ptr.def_list;
            ptr.makeBody(
            ptr.createText("EnterNewName"),
            input,
                    ptr.createLink("contLink")
        );

                ptr.items = jsonObject.items;
            ptr.currentState = "shown_create_list";
            } else {
                // Again, there are no items, thus resubmit the form
                ptr.addHiddenParams(ptr,resp.argument.hiddens);
                document.getElementById(ptr.form_id).submit();
            }
        }
    };

    adoptListPanel.modify_list = function(self) {
        self.JSON.CCLFacade.addItemsToList(
            self,self.the_list, self.the_list_type, self.items
        );
    };

    adoptListPanel.startSM();
    return false;
};

CCLClass.prototype.remove_item = function(listId,lineId) {
    if (!this.json) this.json = new JSONRpcClient("/api/json-rpc.jsp");
    // Make sure that the lineId is passed as string
    lineId = "" + lineId;
    this.json.CCLFacade.removeLineItem(lineId);
};

CCLClass.prototype.remove_so_item = function(listId,lineId) {
    if (!this.json) this.json = new JSONRpcClient("/api/json-rpc.jsp");
    // Make sure that the lineId is passed as string
    lineId = "" + lineId;
    this.json.CCLFacade.removeSOLineItem(lineId);
};


var CCL = new CCLClass();






// formerly known as preload_images
function CCL_hookEvents() {
    // create a hidden element in the DOM:
    var preloadElement = document.createElement("div");
    preloadElement.style.display = "none";
    preloadElement.style.visibility = "hidden";
    document.body.appendChild(preloadElement);

    for (id in CCLPanel.TITLE_IMAGES) {
		preloadElement.appendChild(CCLPanel.createTitleImg(id, function() {
			CCLPanel.TITLE_IMAGES[this._imgId].cachedImg = this;
	    }));
    }
}
