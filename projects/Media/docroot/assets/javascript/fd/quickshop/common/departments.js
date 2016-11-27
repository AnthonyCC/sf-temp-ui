/*global jQuery,quickshop*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	"use strict";

	var $ = fd.libs.$;
	var DATALISTWIDGET = fd.quickshop.datalistWidget;
	var DISPATCHER = fd.common.dispatcher;

	function reduceValues(prev,current) {
		prev.push(current.value);
		return prev;
	}

	var departments = Object.create(DATALISTWIDGET,{
		signal:{
			value:'DEPARTMENTS'
		},
		template:{
			value: quickshop.departments
		},
		placeholder:{
			value:'#departments .departments-list'
		},
		serialize:{
			value:function(element){
				var el = (element) ? element : $(this.placeholder),
					result = $('input[type="radio"]',el).serializeArray()
								.reduce(reduceValues,[]),
					listSize = $('[data-listsize]',el).data('listsize')*1;

				if(result.length === 1 && result[0]==='all') {
					result=[];
				}

				return {
					deptIdList:result
				};
			}
		},
		render:{
			value:function(data){
				DATALISTWIDGET.render.call(this,data);
				var departmentsHeader = $('.departments-header');
				departmentsHeader.attr('data-listsize',data.data.length);
				departmentsHeader.find('.counter').html("("+data.data.length+")");
			}
		},
		handleClick:{
			value:function(clickEvent){
			}
		}
	});

	departments.listen();
	
	$(document).on('change','#departments input[type="radio"]',function(e){
		DISPATCHER.signal('departmentChanged',departments.serialize().deptIdList[0] || null);		
	});

	fd.modules.common.utils.register("quickshop.common", "departments", departments, fd);
}(FreshDirect));
