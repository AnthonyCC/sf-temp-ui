/* global drugPanel */
if(!drugPanel) {

var drugPanel = function($,data,config){
  "use strict";
  
  var INGREDIENT = "INGREDIENT",
      TABLE = "TABLE",
      FREETEXT = "FREETEXT",
      T_DRUG = "DRUG",
      T_PET = "PET",
      T_BABY = "BABY",
      T_SUPPL = "SUPPL";
  
  var DrugSectionType = {
      ingredient : INGREDIENT,
      table : TABLE,
      freetext : FREETEXT
  };
  
  var PanelTypes = [
    { name: 'drug', value: T_DRUG },
    { name: 'pet', value: T_PET },
    { name: 'baby', value: T_BABY },
    { name: 'supplement', value: T_SUPPL }
  ];

  // TODO autocomplete list
  window.FreshDirect = window.FreshDirect || {};
  window.FreshDirect.autocomplete = {
//    TODO: temporary removed
//    value1: ['A VITAMIN', 'B VITAMIN'],
//    uom: ['g', 'kg', 'mg'],
    GENERAL: []
  };

  var container = config.container || document.body;
  var type = data.type || T_DRUG;
  var getCss = function (viewName, type) {
    // set css explicitly via config.displayCss & config.editorCss
    return config[viewName+'Css'] || '/assets/css/'+type.toLowerCase()+'_nutrition_'+viewName.toLowerCase()+'.css';
  };
  var currentView = config.view || 'display';
  
  var DragHandle='<span class="draghandle ui-icon ui-icon-arrowthick-2-n-s"></span>';
  var ItemToolbar='';
  
  var Templates = {
    display:{
      defaultTemplate: {
        DrugPanel:{
          template:'<div class="childBox"></div>'
        },DrugSection:{
          template:'<div class="childBox type_{{type}}"><span class="title importance_{{importance}}">{{title}}</span></div>'
        },DrugItem:{
          tagName:'span',
          templateSeparator:'<hr>',
          templateIngredient:'<span class="value1">{{value1}}</span>{{#showValues}}<span class="ingredientValue">{{ingredientValue}}</span><span class="uom">{{uom}}</span><span class="dotted-wrapper"><span class="value2">{{value2}}</span><span class="dotted"></span></span>{{/showValues}}',
          templateTable:'<span class="value1"><span class="val">{{value1}}</span></span><span class="value2"><span class="val">{{value2}}</span></span><span class="hsep"></span>',
          templateFreetext:'{{#newline}}<br>{{/newline}}<span class="value1">{{#bulleted}} <span class="bullet"></span>&nbsp;{{/bulleted}}{{value1}}</span>'
        }
      },
      pet: {
        DrugItem:{
          tagName:'span',
          templateSeparator:'<hr>',
          templateIngredient:'<span class="value1">{{value1}}</span>{{#showValues}}<span class="dotted-wrapper"><span class="ingredientValue"><span class="value2">{{value2}}</span> {{ingredientValue}} {{uom}}</span><span class="dotted"></span></span>{{/showValues}}',
          templateTable:'<span class="value1"><span class="val">{{value1}}</span></span><span class="value2"><span class="val">{{value2}}</span></span><span class="hsep"></span>',
          templateFreetext:'{{#newline}}<div class="linebreak"></div>{{/newline}}<span class="value1">{{#bulleted}} <span class="bullet"></span>&nbsp;{{/bulleted}}{{value1}}</span>'
        }
      },
      baby: {
        DrugItem:{
          tagName:'span',
          templateSeparator:'<hr>',
          templateIngredient:'<span class="value1">{{value1}}</span>{{#showValues}}<span class="dotted-wrapper"><span class="ingredientValue">{{ingredientValue}} {{uom}}</span><span class="dotted"></span></span>{{/showValues}}',
          templateTable:'<span class="value1"><span class="val">{{value1}}</span></span><span class="value2"><span class="val">{{value2}}</span></span><span class="hsep"></span>',
          templateFreetext:'{{#newline}}<div class="linebreak"></div>{{/newline}}<span class="value1">{{#bulleted}} <span class="bullet"></span>&nbsp;{{/bulleted}}{{value1}}</span>'
        }
      },
      suppl: {
        DrugItem:{
          tagName:'span',
          templateSeparator:'<hr>',
          templateIngredient:'<span class="value1">{{value1}}</span>{{#showValues}}<span class="ingredientValue">{{ingredientValue}} {{uom}}</span>{{/showValues}} <span class="value2">{{value2}}</span>',
          templateTable:'<span class="value1"><span class="val">{{value1}}</span></span><span class="value2"><span class="val">{{value2}}</span></span><span class="hsep"></span>',
          templateFreetext:'{{#newline}}<div class="linebreak"></div>{{/newline}}<span class="value1">{{#bulleted}} <span class="bullet"></span>&nbsp;{{/bulleted}}{{value1}}</span>'
        }
      }
    },editor:{
      defaultTemplate: {
        DrugPanel:{
          template:'<h1>SKU code:{{skuCode}}</h1><div class="toolbar">Panel type:<select class="changeType">{{#panelTypes}}<option value="{{value}}" {{#selected}}selected="selected"{{/selected}}>{{name}}</option>{{/panelTypes}}</select><button class="addSection">Add section</button></div><div class="childBox"></div>'
        },DrugSection:{
          template:'<div class="toolbar"><span class="draghandle ui-icon ui-icon-arrowthick-2-n-s"></span><input name="title" type="text" value="{{title}}"><select name="importance">{{&importantSelector}}</select><select name="type">{{&typeSelector}}</select><button class="deleteSection"><span class="ui-icon ui-icon-trash"></span></button><button class="addItem">Add new item</button></div><div class="childBox type_{{type}}"></div>'
        },DrugItem:{
          tagName:'div',
          templateSeparator:DragHandle+'<hr><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="separator" {{#separator}}checked{{/separator}}> Separator</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>',
          templateIngredient:DragHandle+'<input name="value1" class="autocomplete" type="text" value="{{value1}}" placeholder="Ingredient"><input name="ingredientValue" maxlength="8" type="number" max="99999999.99" value="{{ingredientValue}}" placeholder="Ing. value"><input class="autocomplete" name="uom" value="{{uom}}" placeholder="uom" type="text">...<input name="value2" type="text" value="{{value2}}"  placeholder="Purpose"><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="important" {{#important}}checked{{/important}}> Title</label><label class="toggle"><input type="checkbox" name="separator" {{#separator}}checked{{/separator}}> Separator</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>',
          templateTable:DragHandle+'<input name="value1" type="text" value="{{value1}}" placeholder="Left column"> | <input type="text" name="value2" value="{{value2}}" placeholder="Right column"><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="important" {{#important}}checked{{/important}}> Important</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>',
          templateFreetext:DragHandle+'<input name="value1" type="text" value="{{value1}}"  placeholder="Free text"><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="bulleted" {{#bulleted}}checked{{/bulleted}}> Bulleted</label><label class="toggle"><input type="checkbox" name="important" {{#important}}checked{{/important}}> Important</label><label class="toggle"><input type="checkbox" name="newline" {{#newline}}checked{{/newline}}> Newline</label><label class="toggle"><input type="checkbox" name="separator" {{#separator}}checked{{/separator}}> Separator</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>'
        }
      },
      pet: {
        DrugItem:{
          tagName:'div',
          templateSeparator:DragHandle+'<hr><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="separator" {{#separator}}checked{{/separator}}> Separator</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>',
          templateIngredient:DragHandle+'<input name="value1" class="autocomplete" type="text" value="{{value1}}" placeholder="Ingredient">...<input name="value2" type="text" value="{{value2}}" placeholder="Min/Max"><input name="ingredientValue" max="99999999.99" maxlength="8" type="number" value="{{ingredientValue}}" placeholder="Ing. value"><input class="autocomplete" name="uom" value="{{uom}}" placeholder="uom" type="text"><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="important" {{#important}}checked{{/important}}> Title</label><label class="toggle"><input type="checkbox" name="separator" {{#separator}}checked{{/separator}}> Separator</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>',
          templateTable:DragHandle+'<input name="value1" type="text" value="{{value1}}" placeholder="Left column"> | <input type="text" name="value2" value="{{value2}}" placeholder="Right column"><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="important" {{#important}}checked{{/important}}> Important</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>',
          templateFreetext:DragHandle+'<input name="value1" type="text" value="{{value1}}"  placeholder="Free text"><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="bulleted" {{#bulleted}}checked{{/bulleted}}> Bulleted</label><label class="toggle"><input type="checkbox" name="important" {{#important}}checked{{/important}}> Important</label><label class="toggle"><input type="checkbox" name="newline" {{#newline}}checked{{/newline}}> Newline</label><label class="toggle"><input type="checkbox" name="separator" {{#separator}}checked{{/separator}}> Separator</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>'
        }
      },
      baby: {
        DrugItem:{
          tagName:'div',
          templateSeparator:DragHandle+'<hr><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="separator" {{#separator}}checked{{/separator}}> Separator</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>',
          templateIngredient:DragHandle+'<input name="value1" class="autocomplete" type="text" value="{{value1}}" placeholder="Ingredient">...<input name="ingredientValue" max="99999999.99" type="number" maxlength="8" value="{{ingredientValue}}" placeholder="Ing. value"><input class="autocomplete" name="uom" value="{{uom}}" placeholder="uom" type="text"><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="important" {{#important}}checked{{/important}}> Title</label><label class="toggle"><input type="checkbox" name="separator" {{#separator}}checked{{/separator}}> Separator</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>',
          templateTable:DragHandle+'<input name="value1" type="text" value="{{value1}}" placeholder="Left column"> | <input type="text" name="value2" value="{{value2}}" placeholder="Right column"><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="important" {{#important}}checked{{/important}}> Important</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>',
          templateFreetext:DragHandle+'<input name="value1" type="text" value="{{value1}}"  placeholder="Free text"><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="bulleted" {{#bulleted}}checked{{/bulleted}}> Bulleted</label><label class="toggle"><input type="checkbox" name="important" {{#important}}checked{{/important}}> Important</label><label class="toggle"><input type="checkbox" name="newline" {{#newline}}checked{{/newline}}> Newline</label><label class="toggle"><input type="checkbox" name="separator" {{#separator}}checked{{/separator}}> Separator</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>'
        }
      }
    }
  };
  
  var getTemplate = function (viewName, type) {
    var typeTemplate,
        defaultTemplate,
        template = {}, prop;

    type = type.toLowerCase() || 'defaultTemplate';
    viewName = viewName || 'display';

    defaultTemplate = Templates[viewName].defaultTemplate;
    typeTemplate = Templates[viewName][type];

    if (defaultTemplate === typeTemplate) {
      return defaultTemplate;
    }

    // copy default template
    for (prop in defaultTemplate) {
      if (defaultTemplate.hasOwnProperty(prop)) {
        template[prop] = defaultTemplate[prop];
      }
    }
    // copy type template - overwrite default properties
    for (prop in typeTemplate) {
      if (typeTemplate.hasOwnProperty(prop)) {
        template[prop] = typeTemplate[prop];
      }
    }

    return template;
  };
  
  var mix = function(dest,source) {
    Object.keys(source).forEach(function(key){
      dest[key]=source[key];
    });
  };
  
  var setCurrentView = function(viewName, type) {
    var view,
        cssUrl,
        linkElement = $('#drugcss_'+viewName);

    type = type || 'DRUG';
    cssUrl = getCss(viewName, type);

    view = getTemplate(viewName, type);

    mix(DrugPanel,view.DrugPanel);
    mix(DrugSection,view.DrugSection);
    mix(DrugItem,view.DrugItem);
    
    linkElement.remove();

    container.removeClass('display editor');
    currentView = viewName;
    container.addClass(currentView);
    if (document.createStyleSheet) {
        document.createStyleSheet(cssUrl);
    } else {
    	$('<link rel="stylesheet" type="text/css" id="drugcss_'+viewName+'" href="'+cssUrl+'">').appendTo('head');
    }

  };
  
  var refresh = function(data){
    data = data || DrugPanel.getData();
    DrugPanel.container.empty();
    Widget.clear();
    setCurrentView(currentView, data.type);
    DrugPanel.init(data);
    $('.type_TABLE .drugitem:last-child',DrugPanel.boundingBox).addClass('last');

    // call callback with data
    if (config.refreshCallback) {
      config.refreshCallback(data);
    }
  };
  
  var setDirty = function(dirty){
    var $saveButton = $('#savebutton');
    window.drogPanelWarning = dirty;
    
    if(dirty) {
      $saveButton.html('Save panel');
      $saveButton.removeAttr('disabled');
    } else {
      $saveButton.html('Saved');
      $saveButton.attr('disabled','disabled');
    }
    
  };
  
  var dirtySaveButton = function(){
    setDirty(true);
  };
  
  
  var Widget = {
    register:{ },
    currentId:0,
    boundingBox: null,
    tagName: 'div',
    id:'',
    template:'<div class="childBox"></div>',
    container: null,
    init:function(tagName){
      if(tagName) {
        this.tagName = tagName;
      }
      this.boundingBox = document.createElement(this.tagName);
      this.id = "widget_"+(++Widget.currentId);
      this.boundingBox.className = this.getWidgetClasses();
      this.boundingBox.id = this.id;
      Widget.register[this.id] = this;
      return this;
    },
    getWidgetClasses:function(){
      var obj = this,
        result = 'widget';
      
      while(obj !== Widget) {
        if(obj.widgetClass) {
          result += ' '+obj.widgetClass;
        }
        
        obj = Object.getPrototypeOf(obj);
      } 
      
      return result;
    },
    render:function(parent) {
      if (parent) {
        this.container = parent;
      }
      this.renderUI();
      this.bindUI();
      this.syncUI();
      $(this.container).append(this.boundingBox);
    },
    getChildBox:function(){
      var childBox =  $('.childBox',this.boundingBox).toArray();
      if(childBox.length > 0 ) {
        return childBox.shift();
      } else {
        return this.boundingBox;
      }
    },
    syncUI:function(){
      this.boundingBox.innerHTML = $.mustache(this.template, this);
    },    
    bindUI: function(){},
    renderUI: function(){},
    lookup: function($element, className){
      var widgetBoundingBox=$($element).closest(className || '.widget'),
        widget;
      if(widgetBoundingBox.length) {
        widget=Widget.register[widgetBoundingBox[0].id];
      }
      return widget;
    },
    lookupById:function(id){
      return Widget.register[id];
    },
    eventHandler:function(handler,cssClass){
      return function(event) {
        var widgetEventHandler = handler,
          widgetCssClassname = cssClass,
          target=event.target,
          widget = Widget.lookup(target,widgetCssClassname);
        if(widget) {
          widgetEventHandler.call(widget,event);
        }
        // call refresh callback after every data change
        if (config.refreshCallback && DrugPanel) {
          config.refreshCallback(DrugPanel.getData());
        }
      };
    },
    clear:function(){
      Widget.register = {};
    },
    destroy:function(){
      var parent = this.boundingBox.parentNode,
        id = this.id;
      parent.removeChild(this.boundingBox);
      delete Widget.register[id];
      setDirty(true);
    }
  };

  var DrugPanel = Object.create(Widget,{
    widgetClass:{
      value:'drugpanel'
    },
    init:{
      value:function(json,container){
        Widget.init.call(this);
        this.skuCode = json.skuCode;
        this.type = json.type;
        this.updatePanelTypes();
        this.render(container);
        json.sections.forEach(this.addSection.bind(this));

        // setup autocomplete
        if (json.autocomplete && json.autocomplete[json.type]) {
          window.FreshDirect.autocomplete = json.autocomplete[json.type];
        }
      }
    },
    syncUI:{
      value:function(){
        var childBox;
        Widget.syncUI.call(this);
        childBox = $(this.getChildBox()); 
        childBox.sortable({
          handle:'.draghandle',
          stop: function (e, ui) {
            setDirty(true);
            if (config.refreshCallback && DrugPanel) {
              config.refreshCallback(DrugPanel.getData());
            }
          }
        });
      }
    },
    updatePanelTypes:{
      value:function(){
        var i, l = PanelTypes.length;

        this.panelTypes = [];
        for (i = 0; i < l; i++) {
          this.panelTypes[i] = { 
            name: PanelTypes[i].name,
            value: PanelTypes[i].value
          };
          if (PanelTypes[i].value.toLowerCase() === this.type.toLowerCase()) {
            this.panelTypes[i].selected = true;
          }
        }
      }
    },
    addSection:{
      value:function(sectionData){
        var childBox = this.getChildBox(),
          section = Object.create(DrugSection,{
          title:{
            value: sectionData.title || '',
            writable:true,
            enumerable:true
          },
          type:{
            value: sectionData.type || DrugSectionType.freetext,
            writable:true,
            enumerable:true
          },
          importance:{
            value: sectionData.importance || 0,
            writable:true,
            enumerable:true
          }
        });

        section.init();       
        section.render(childBox);
        if(sectionData.items) {
          sectionData.items.forEach(DrugSection.addItem.bind(section));
        }
      }
    },
    addNewSection:{ 
      value:function(event) {
        var target = event.target;
        this.addSection({});
        setDirty(true);
      }
    },
    changeType:{
      value:function(event) {
        this.type = $(event.target).val().toUpperCase();
        this.updatePanelTypes();
        refresh(); // TODO make this a method
      }
    },
    getData:{
      value:function(){
        var data = {
          type:this.type,
          skuCode:this.skuCode,
          sections:[]
        };
        
        $('.drugsection',this.getChildBox()).toArray().forEach(function(element){
          var widget = Widget.lookupById(element.id);
          data.sections.push(widget.getData());
        });
        
        return data;
      }
    }
  });
  
  var DrugSection = Object.create(Widget,{
    widgetClass:{
      value:'drugsection'
    },
    importance:{
      value:0,
      enumerable:true
    },
    type:{
      value:DrugSectionType.freetext,
      enumerable:true
    },
    title:{
      value:'',
      enumerable:true
    },
    syncUI:{
      value:function(){
        var childBox;
        Widget.syncUI.call(this);
        childBox = $(this.getChildBox()); 
        childBox.sortable({
          handle:'.draghandle',
          stop: function (e, ui) {
            setDirty(true);
            if (config.refreshCallback && DrugPanel) {
              config.refreshCallback(DrugPanel.getData());
            }
          }
        });
      }
    },
    importantSelector:{
      value:function(){
        var result = '',i;
        for(i=0;i<5;i++) {
          result+='<option value="'+i+'" '+((i===this.importance) ? 'selected' : '')+'>importance: '+i+'</option>';
        }
        return result;
      }
    },
    typeSelector:{
      value:function(){
        var result = '',that = this;
        Object.keys(DrugSectionType).forEach(function(key){
          var val = DrugSectionType[key];
          result+='<option value="'+val+'" '+((val===that.type) ? 'selected' : '')+'>'+key+'</option>';
        });

        return result;
      }
    },
    addItem:{
      value:function(itemData){
        var item = Object.create(DrugItem,{
          value1:{
            value: itemData.value1 || '',
            writable:true,
            enumerable:true
          },
          value2:{
            value: itemData.value2 || '',
            writable:true,
            enumerable:true
          },
          showValues:{
            value: itemData.value2 || parseFloat(itemData.ingredientValue) !== 0 || itemData.uom,
            writable:true,
            enumerable:true
          },
          ingredientValue:{
            value: itemData.ingredientValue || 0,
            writable:true,
            enumerable:true
          },
          uom:{
            value: itemData.uom || '',
            writable:true,
            enumerable:true
          },
          bulleted:{
            value: itemData.bulleted || false,
            writable:true,
            enumerable:true
          },
          important:{
            value: itemData.important || false,
            writable:true,
            enumerable:true
          },
          newline:{
            value: !(itemData.newline === false),
            writable:true,
            enumerable:true
          },
          separator:{
            value: itemData.separator || false,
            writable:true,
            enumerable:true
          }
        });
        
        item.init();
        item.render(this.getChildBox());
        this.updateItem(item);
      }
    },
    updateItem:{ value:function(item) {
        item.type = this.type;
        item.syncUI();
      }
    },
    addNewItem:{ 
      value:function(event) {
        var target = event.target;
        this.addItem({});
        setDirty(true);
      }
    },
    getItems:{
      value:function(){
        var result,
          elements = $('.'+DrugItem.widgetClass,this.boundingBox).toArray();
        
        return elements.map(function(item){
          var id = item.id;
          return Widget.register[id];
        });
      }
    },
    typeChange:{ 
      value:function(event) {
        var target = event.target,
          items;
        $(this.boundingBox).removeClass('type_'+this.type);
        this.type = target.value;
        $(this.boundingBox).addClass('type_'+this.type);
        items = this.getItems();
        items.forEach(this.updateItem,this);
      }
    },
    titleChange:{ 
      value:function(event) {
        var target = event.target;
        this.title = target.value;
      }
    },
    importanceChange:{ 
      value:function(event) {
        var target = event.target;
        this.importance = target.value;
      }
    },
    getData:{
      value:function(){
        var data = {
          importance:this.importance,
          type:this.type,
          title:this.title,
          items:[]
        };
        
        $('.drugitem',this.getChildBox()).toArray().forEach(function(element){
          var widget = Widget.lookupById(element.id);
          data.items.push(widget.getData());
        });
        
        return data;
      }
    }
  });
  
  var DrugItem = Object.create(Widget,{
    widgetClass:{
      value:'drugitem'
    },
    value1:{
      value:'',
      enumerable:true
    },
    value2:{
      value:'',
      enumerable:true
    },
    ingredientValue:{
      value:'',
      enumerable:true
    },
    uom:{
      value:'',
      enumerable:true
    },
    bulleted:{
      value:false,
      enumerable:true
    },
    important:{
      value:false,
      enumerable:true
    },
    newline:{
      value:true,
      enumerable:true
    },
    separator:{
      value:false,
      enumerable:true
    },
    setBoundingClass:{
      value:function(prop) {
        if(this[prop]) {
          $(this.boundingBox).addClass(prop);
        } else {
          $(this.boundingBox).removeClass(prop);
        }
      }
    },
    syncUI:{
      value:function(){
        if(this.separator) {
          this.template = this.templateSeparator;
        } else if(this.type === DrugSectionType.ingredient){
          this.template = this.templateIngredient;
        } else if(this.type === DrugSectionType.table ) {
          this.template = this.templateTable;
        } else {
          this.template = this.templateFreetext;
        }
        this.setBoundingClass('bulleted');
        this.setBoundingClass('newline');
        this.setBoundingClass('important');
        this.setBoundingClass('separator');
        Widget.syncUI.call(this);
      }
    },
    bulletedChange:{ value: function(event){
        var target = event.target;
        this.bulleted = target.checked;
        this.syncUI();
      }
    },
    importantChange:{ value: function(event){
        var target = event.target;
        this.important = target.checked;
        this.syncUI();
      }
    },
    newlineChange:{ value: function(event){
        var target = event.target;
        this.newline = target.checked;
        this.syncUI();
      }
    },
    separatorChange:{ value: function(event){
        var target = event.target;
        this.separator = target.checked;
        this.syncUI();        
      }
    },
    value1Change:{
      value:function(event){
        var target = event.target;
        this.value1 = target.value;
      }
    },
    value2Change:{
      value:function(event){
        var target = event.target;
        this.value2 = target.value;
      }
    },
    ingredientValueChange:{
      value:function(event){
        var target = event.target,
            numValue = parseFloat(target.value) || 0;
        // number check
        this.ingredientValue = numValue;
        target.value = numValue;
      }
    },
    uomChange:{
      value:function(event){
        var target = event.target;
        this.uom = target.value;
      }
    },
    getData:{
      value:function(){
        var data = {
          value1:this.value1,
          value2:this.value2,
          ingredientValue:this.ingredientValue,
          uom:this.uom,
          bulleted:this.bulleted,
          important:this.important,
          newline:this.newline,
          separator:this.separator
        };
        
        return data;
      }
    }
  });
  
  setCurrentView(currentView, type);
  DrugPanel.init(data,container);
  
  // if id is null then it's a proto panel
  setDirty(data.id === null);

  
  if(config.events) {
    container.delegate('.drugpanel .toolbar .changeType','change',Widget.eventHandler(DrugPanel.changeType,'.'+DrugPanel.widgetClass));
    container.delegate('.drugpanel .toolbar .addSection','click',Widget.eventHandler(DrugPanel.addNewSection,'.'+DrugPanel.widgetClass));
    container.delegate('.drugsection .toolbar select[name="type"]','change',Widget.eventHandler(DrugSection.typeChange,'.'+DrugSection.widgetClass));
    container.delegate('.drugsection .toolbar select[name="importance"]','change',Widget.eventHandler(DrugSection.importanceChange,'.'+DrugSection.widgetClass));
    container.delegate('.drugsection .toolbar input[name="title"]','change',Widget.eventHandler(DrugSection.titleChange,'.'+DrugSection.widgetClass));
    container.delegate('.drugsection .toolbar .addItem','click',Widget.eventHandler(DrugSection.addNewItem,'.'+DrugSection.widgetClass));
    container.delegate('.drugsection .toolbar .deleteSection','click',Widget.eventHandler(Widget.destroy,'.'+DrugSection.widgetClass));
    container.delegate('.drugitem input[name="value1"]','change',Widget.eventHandler(DrugItem.value1Change,'.'+DrugItem.widgetClass));
    container.delegate('.drugitem input[name="value2"]','change',Widget.eventHandler(DrugItem.value2Change,'.'+DrugItem.widgetClass));
    container.delegate('.drugitem input[name="ingredientValue"]','change',Widget.eventHandler(DrugItem.ingredientValueChange,'.'+DrugItem.widgetClass));
    container.delegate('.drugitem input[name="uom"]','change',Widget.eventHandler(DrugItem.uomChange,'.'+DrugItem.widgetClass));
    container.delegate('.drugitem .toolbar input[name="bulleted"]','change',Widget.eventHandler(DrugItem.bulletedChange,'.'+DrugItem.widgetClass));
    container.delegate('.drugitem .toolbar input[name="important"]','change',Widget.eventHandler(DrugItem.importantChange,'.'+DrugItem.widgetClass));
    container.delegate('.drugitem .toolbar input[name="newline"]','change',Widget.eventHandler(DrugItem.newlineChange,'.'+DrugItem.widgetClass));
    container.delegate('.drugitem .toolbar input[name="separator"]','change',Widget.eventHandler(DrugItem.separatorChange,'.'+DrugItem.widgetClass));
    container.delegate('.drugitem .toolbar .deleteItem','click',Widget.eventHandler(Widget.destroy,'.'+DrugItem.widgetClass));
    container.delegate('.drugpanel input[type="text"]','keyup',dirtySaveButton);
    container.delegate('.drugpanel input[type="number"]','keyup',dirtySaveButton);
    container.delegate('.drugpanel select','change',dirtySaveButton);
    container.delegate('.drugpanel input[type="checkbox"]','change',dirtySaveButton);
    container.delegate('.drugpanel input[type="number"]','change',dirtySaveButton);
    $(document).delegate('#savebutton','click',function(){
      var data = DrugPanel.getData(),
          $this = $('#savebutton'),
          oldText = $this.html();

      $this.html('Saving ...');
      $this.attr('disabled','disabled');
      $.ajax({
        url:window.location+'&redirect=false',
        type:"POST",
        data:{
          skuCode:DrugPanel.skuCode,
          panel:JSON.stringify(data)
          },
          success:function(){
            setDirty(false);
          },
          error:function(){
            setDirty(true);
          }
      });
      
    });
    $(document).delegate('#deletebutton','click',function(){
      var data = DrugPanel.skuCode;
      if(data && window.confirm("Are you sure?")) {
        
        $('#deleteform input[name=delete]')[0].value = data;
        $('#deleteform input[name=skuCode]')[0].value = data;
        setDirty(false);
        $('#deleteform')[0].submit();
      }
    });
    $(document).delegate('#refreshbutton', 'click', function () {
      if (config.refreshCallback) {
        config.refreshCallback(DrugPanel.getData());
      }
    });
    $(document).delegate('#switchbutton','click',function(){
      if(currentView === 'display') {
        currentView = 'editor';
      } else {
        currentView = 'display';
      }
      
      refresh();
    });
    
    // warn if the page was not saved
    window.onbeforeunload = function (e) {
      var message = "Panel has not been saved yet, are you sure to leave this page?";

      if (!window.drogPanelWarning) {
        return;
      }

      e = e || window.event;

      if (e) {
        e.returnValue = message;
      }

      return message;
    };
  }
  
  return {
    refresh: refresh,
    setCurrentView: setCurrentView
  };

};
}
