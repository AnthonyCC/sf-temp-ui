if(!drugPanel) {

var drugPanel = function($,data,config){
	"use strict"
	
	var INGRIDIENT = "INGREDIENT",
		TABLE = "TABLE",
		FREETEXT = "FREE_TEXT";
	
	var DrugSectionType = {
			ingredient : INGRIDIENT,
			table : TABLE,
			freetext : FREETEXT
	};
	
	

	var container = config.container || document.body;
	var css = {
			display:config.displayCss  || '/assets/css/drug_nutrition_display.css',
			editor:config.editorCss || '/assets/css/drug_nutrition_edit.css'
	};
	var currentView = config.view || 'display';
	
	var DragHandle='<span class="draghandle ui-icon ui-icon-arrowthick-2-n-s"></span>';
	var ItemToolbar='';
	
	var Templates = {
		display:{
			DrugPanel:{
				template:'<div class="childBox"></div>'
			},DrugSection:{
				template:'<div class="childBox type_{{type}}"><span class="title importance_{{importance}}">{{title}}</span></div>'
			},DrugItem:{
				tagName:'span',
				templateSeparator:'<hr>',
				templateIngredient:'<span class="dotted"></span><span class="value1">{{value1}}</span><span class="ingredientValue">{{ingredientValue}}</span><span class="uom">{{uom}}</span><span class="value2">{{value2}}</span>',
				templateTable:'<span class="value1"><span class="val">{{value1}}</span></span><span class="value2"><span class="val">{{value2}}</span></span><span class="hsep"></span>',
				templateFreetext:'{{#newline}}<br>{{/newline}}<span class="value1">{{#bulleted}} <span class="bullet"></span>&nbsp;{{/bulleted}}{{value1}}</span>'
			}
		},editor:{
			DrugPanel:{
				template:'<h1>SKU code:{{skuCode}}</h1><div class="toolbar"><button class="addSection">Add section</button></div><div class="childBox"></div>'
			},DrugSection:{
				template:'<div class="toolbar"><span class="draghandle ui-icon ui-icon-arrowthick-2-n-s"></span><input name="title" type="text" value="{{title}}"><select name="importance">{{&importantSelector}}</select><select name="type">{{&typeSelector}}</select><button class="addItem">Add new item</button><button class="deleteSection"><span class="ui-icon ui-icon-trash"></span></button></div><div class="childBox type_{{type}}"></div>'
			},DrugItem:{
				tagName:'div',
				templateSeparator:DragHandle+'<hr><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="separator" {{#separator}}checked{{/separator}}> Separator</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>',
				templateIngredient:DragHandle+'<input name="value1" type="text" value="{{value1}}" placeholder="Ingredient"><input name="ingredientValue" type="text" value="{{ingredientValue}}" placeholder="Ing. value"><input name="uom" value="{{uom}}" placeholder="uom" type="text">...<input name="value2" type="text" value="{{value2}}"  placeholder="Purpose"><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="important" {{#important}}checked{{/important}}> Important</label><label class="toggle"><input type="checkbox" name="separator" {{#separator}}checked{{/separator}}> Separator</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>',
				templateTable:DragHandle+'<input name="value1" type="text" value="{{value1}}" placeholder="Left column"> | <input type="text" name="value2" value="{{value2}}" placeholder="Right column"><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="important" {{#important}}checked{{/important}}> Important</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>',
				templateFreetext:DragHandle+'<input name="value1" type="text" value="{{value1}}"  placeholder="Free text"><span class="itemtoolbar toolbar"><label class="toggle"><input type="checkbox" name="bulleted" {{#bulleted}}checked{{/bulleted}}> Bulleted</label><label class="toggle"><input type="checkbox" name="important" {{#important}}checked{{/important}}> Important</label><label class="toggle"><input type="checkbox" name="newline" {{#newline}}checked{{/newline}}> Newline</label><label class="toggle"><input type="checkbox" name="separator" {{#separator}}checked{{/separator}}> Separator</label><button class="deleteItem"><span class="ui-icon ui-icon-trash"></span></button></span>'
			}
		}
	};
	
	
	var mix = function(dest,source) {
		Object.keys(source).forEach(function(key){
			dest[key]=source[key];
		});
	}
	
	var setCurrentView = function(viewName) {
		var view = Templates[viewName],
			cssUrl = css[viewName],
			linkElement = $('#drugcss');
		mix(DrugPanel,view.DrugPanel);
		mix(DrugSection,view.DrugSection);
		mix(DrugItem,view.DrugItem);
		
		linkElement.remove();

		container.removeClass(currentView);
		currentView = viewName;
		container.addClass(currentView);
		
		$('head').append('<link id="drugcss" href="'+cssUrl+'" rel="stylesheet">');

	};
	
	var refresh = function(){
		var data = DrugPanel.getData();
		container.empty();
		Widget.clear();
		DrugPanel.init(data,container);
		$('.type_TABLE .drugitem:last-child',DrugPanel.boundingBox).addClass('last');

	};
	
	
	var Widget = {
		register:{ },
		currentId:0,
		boundingBox: null,
		tagName: 'div',
		id:'',
		template:'<div class="childBox"></div>',
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
			this.renderUI();
			this.bindUI();
			this.syncUI();
			$(parent).append(this.boundingBox);
		},
		getChildBox:function(){
			var childBox =  $('.childBox',this.boundingBox).toArray();
			if(childBox.length == 1) {
				return childBox[0];
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
			}
		},
		clear:function(){
			Widget.register = {};
		},
		destroy:function(){
			var parent = this.boundingBox.parentNode,
				id = this.id;
			parent.removeChild(this.boundingBox);
			delete Widget.register[id];
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
				this.render(container);
				json.sections.forEach(this.addSection.bind(this));
			}
		},
		syncUI:{
			value:function(){
				var childBox;
				Widget.syncUI.call(this);
				childBox = $(this.getChildBox()); 
				childBox.sortable({ handle:'.draghandle' });
			}
		},
		addSection:{
			value:function(sectionData){
				var section = Object.create(DrugSection,{
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
				section.render(this.getChildBox());
				if(sectionData.items) {
					sectionData.items.forEach(DrugSection.addItem.bind(section));
				}
			}
		},
		addNewSection:{ 
			value:function(event) {
				var target = event.target;
				this.addSection({});
			}
		},
		getData:{
			value:function(){
				var data = {
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
				childBox.sortable({ handle:'.draghandle' });
			}
		},
		importantSelector:{
			value:function(){
				var result = '',i;
				for(i=0;i<5;i++) {
					result+='<option value="'+i+'" '+((i==this.importance) ? 'selected' : '')+'>importance: '+i+'</option>'
				}
				return result;
			}
		},
		typeSelector:{
			value:function(){
				var result = '',that = this;
				Object.keys(DrugSectionType).forEach(function(key){
					var val = DrugSectionType[key];
					result+='<option value="'+val+'" '+((val==that.type) ? 'selected' : '')+'>'+key+'</option>'
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
						value: itemData.newline || false,
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
					$(this.boundingBox).addClass(prop)
				} else {
					$(this.boundingBox).removeClass(prop)
				}
			}
		},
		syncUI:{
			value:function(){
				if(this.separator) {
					this.template = this.templateSeparator;
				} else if(this.type == DrugSectionType.ingredient){
					this.template = this.templateIngredient;
				} else if(this.type == DrugSectionType.table ) {
					this.template = this.templateTable;
				} else {
					this.template = this.templateFreetext;
				}
				this.setBoundingClass('bulleted');
				this.setBoundingClass('newline');
				this.setBoundingClass('important');
				this.setBoundingClass('separator');
				Widget.syncUI.call(this)
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
			value:function(){
				var target = event.target;
				this.value1 = target.value;
			}
		},
		value2Change:{
			value:function(){
				var target = event.target;
				this.value2 = target.value;
			}
		},
		ingredientValueChange:{
			value:function(){
				var target = event.target;
				this.ingredientValue = target.value;
			}
		},
		uomChange:{
			value:function(){
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
	
	setCurrentView(currentView);
	DrugPanel.init(JSON.parse(data),container);
	
	
	if(config.events) {
		$(document).delegate('.drugpanel .toolbar .addSection','click',Widget.eventHandler(DrugPanel.addNewSection,'.'+DrugPanel.widgetClass))
		$(document).delegate('.drugsection .toolbar select[name="type"]','change',Widget.eventHandler(DrugSection.typeChange,'.'+DrugSection.widgetClass))
		$(document).delegate('.drugsection .toolbar select[name="importance"]','change',Widget.eventHandler(DrugSection.importanceChange,'.'+DrugSection.widgetClass))
		$(document).delegate('.drugsection .toolbar input[name="title"]','change',Widget.eventHandler(DrugSection.titleChange,'.'+DrugSection.widgetClass))
		$(document).delegate('.drugsection .toolbar .addItem','click',Widget.eventHandler(DrugSection.addNewItem,'.'+DrugSection.widgetClass))
		$(document).delegate('.drugsection .toolbar .deleteSection','click',Widget.eventHandler(Widget.destroy,'.'+DrugSection.widgetClass))
		$(document).delegate('.drugitem input[name="value1"]','change',Widget.eventHandler(DrugItem.value1Change,'.'+DrugItem.widgetClass))
		$(document).delegate('.drugitem input[name="value2"]','change',Widget.eventHandler(DrugItem.value2Change,'.'+DrugItem.widgetClass))
		$(document).delegate('.drugitem input[name="ingredientValue"]','change',Widget.eventHandler(DrugItem.ingredientValueChange,'.'+DrugItem.widgetClass))
		$(document).delegate('.drugitem input[name="uom"]','change',Widget.eventHandler(DrugItem.uomChange,'.'+DrugItem.widgetClass))
		$(document).delegate('.drugitem .toolbar input[name="bulleted"]','change',Widget.eventHandler(DrugItem.bulletedChange,'.'+DrugItem.widgetClass))
		$(document).delegate('.drugitem .toolbar input[name="important"]','change',Widget.eventHandler(DrugItem.importantChange,'.'+DrugItem.widgetClass))
		$(document).delegate('.drugitem .toolbar input[name="newline"]','change',Widget.eventHandler(DrugItem.newlineChange,'.'+DrugItem.widgetClass))
		$(document).delegate('.drugitem .toolbar input[name="separator"]','change',Widget.eventHandler(DrugItem.separatorChange,'.'+DrugItem.widgetClass))
		$(document).delegate('.drugitem .toolbar .deleteItem','click',Widget.eventHandler(Widget.destroy,'.'+DrugItem.widgetClass))
		$(document).delegate('#savebutton','click',function(){
			var data = DrugPanel.getData();
			$('#saveform input[name=panel]')[0].value = JSON.stringify(data);
			$('#saveform')[0].submit();
		});
		$(document).delegate('#deletebutton','click',function(){
			var data = DrugPanel.skuCode;
			if(data) {
				$('#deleteform input[name=delete]')[0].value = data;
				$('#deleteform')[0].submit();
			}
		});
		$(document).delegate('#switchbutton','click',function(){
			if(currentView == 'display') {
				setCurrentView('editor');
			} else {
				setCurrentView('display');
			}
			
			refresh();
		});
	}
	

};
};
