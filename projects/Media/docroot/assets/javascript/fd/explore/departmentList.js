/*global explore*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;

	var exploreDepartmentList = Object.create(WIDGET,{
		signal: {
			value:'exploreDepartmentList'
		},
		template: {
			value: explore.departmentList
		},
		placeholder: {
			value: '#explore-department-list'
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
				if (!data.deptId || data.deptId === null){
					
					if ((this.selectedId === null || data.deptId === null) && this.list.length) {
						data.deptId = this.list[0].id;
						
						/* fix history to have default fallback */
						window.history.replaceState({superdeptId: data.superdeptId, deptId: data.deptId}, "Shop by category", fd.modules.explore.selector.getCleanUri(data.deptId, data.superdeptId));
					}
				}
				if (data.deptId !== this.selectedId) {
					this.selectedId = data.deptId;

					//update cat list
					fd.modules.explore.selector.updateCategoryList(data.deptId, data.superdeptId);

					//only render when the id changes
					WIDGET.render.call(this, data);
					//scroll carousel
					fd.modules.explore.selector.scrollCarouselToElemByIndex($('.explore__department-list-cont [data-component="carousel"]'), $('.explore__department-list-element.selected').attr('data-position'));
				}
			}
		}
	});
	exploreDepartmentList.listen();

	fd.modules.common.utils.register("modules.explore", "departmentList", exploreDepartmentList, fd);
}(FreshDirect));
