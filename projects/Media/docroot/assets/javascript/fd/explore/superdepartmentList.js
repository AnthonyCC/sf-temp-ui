/*global explore*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;

	var exploreSuperdepartmentList = Object.create(WIDGET,{
		signal: {
			value:'exploreSuperdepartmentList'
		},
		template: {
			value: explore.superdepartmentList
		},
		placeholder: {
			value: '#explore-superdepartment-list'
		},
		selectedId: {
			value: null,
			writable: true
		},
		list: {
			value: [],
			writable: true
		},
		render: {
			value: function (data){
				if (!data.list) {
					data.list = this.list;
				} else {
					this.list = data.list;
				}
				if (data.superdeptId !== this.selectedId) {
					this.selectedId = data.superdeptId;
					
					//update dept list
					fd.modules.explore.selector.updateDepartmentList(data.deptId, data.superdeptId);
					
					//only render when the id changes
					WIDGET.render.call(this, data);
				}
			}
		}
	});
	exploreSuperdepartmentList.listen();

	fd.modules.common.utils.register("modules.explore", "superdepartmentList", exploreSuperdepartmentList, fd);
}(FreshDirect));
