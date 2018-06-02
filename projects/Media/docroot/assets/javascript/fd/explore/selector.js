/*global explore*/
var FreshDirect = FreshDirect || {};
/* TODO 
	styling - mob web
 */
(function (fd) {
	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;
	var DISPATCHER = fd.common.dispatcher;

	var exploreSelector = Object.create(WIDGET,{
		getCategoryList:{
			value: function(deptId, superdeptId) {
				var dep = this.getDepartment(deptId, superdeptId);

				return [].concat( (dep) ? dep.categories : [] );
			}
		},
		getSuperdepartmentList: {/* excludes superdepts with no depts */
			value: function() {
				var expdata = fd.explore && fd.explore.data && fd.explore.data.abstractDepartments;
				return [].concat( expdata.filter(function(d, includeEmpty) { return d.departments && d.departments.length; }) );
			}
		},
		getSuperdepartment: {
			value: function(superdeptId) {
				var expdata = fd.explore && fd.explore.data && fd.explore.data.abstractDepartments;
				return expdata.filter(function(d) { return d.id.toLowerCase() === superdeptId; })[0];
			}
		},
		getDepartmentList: {
			value: function(superdeptId) {
				var expdata = fd.explore && fd.explore.data && fd.explore.data.abstractDepartments;
				var deptList = [];

				if (superdeptId) {
					var node = this.getSuperdepartment(superdeptId);
					if (node && node.departments) {
						return deptList.concat( node.departments );
					}
				}

				return deptList.concat( expdata.filter(function(d) { return !d.departments; }) );
				
			}
		},
		getDepartment: {
			value: function(deptId, superdeptId) {
				var expdata = fd.explore && fd.explore.data && fd.explore.data.abstractDepartments;
				var node = null;
				if (deptId) {
					if (superdeptId) {
						node = this.getDepartmentList(superdeptId).filter(function (d) { return d.id.toLowerCase() === deptId.toLowerCase(); })[0];
					} else {
						node = expdata.filter(function(d) { return d.id.toLowerCase() === deptId; })[0];
					}
				}

				return node;
			}
		},
		updateDepartmentList:{
			value: function(deptId, superdeptId){
				DISPATCHER.signal('exploreDepartmentList', {
					superdeptId: superdeptId, 
					deptId: deptId, 
					list: exploreSelector.getDepartmentList(superdeptId) 
				});
			}
		},
		updateCategoryList:{
			value: function(deptId, superdeptId){
				DISPATCHER.signal('exploreCategoryList', {
					list: this.getCategoryList(deptId, superdeptId) 
				});
			}
		},
		getCleanUri:{
			value: function(deptId, superdeptId){
				var uri = document.location.pathname;
				if (deptId) { uri += '?dep='+deptId; }
				if (superdeptId) { uri += ((uri.indexOf('?')===-1)?'?':'&')+'superdeptId='+superdeptId; }
				return uri;
			}
		},
		scrollCarouselToElemByIndex:{
			value: function($carousel, elemIndex) {
				var $mask = $('[data-component="carousel-mask"]', $carousel ),
				$list = $('[data-component="carousel-list"]', $carousel ),
				elements = $list.children(),
				itemsize = elements.length ? Math.floor(elements[0].getBoundingClientRect().width) : 0,
				itemPerPage = Math.floor($mask.width() / itemsize) || 1,
				scrollToPage = Math.ceil(elemIndex/itemPerPage)-1;
				
				fd.components.carousel.changePage($carousel, null, scrollToPage);
			}
		}
	});
	exploreSelector.listen();

	$(document).on('click', '[data-component="explore-superdepartment-selector"],[data-component="explore-department-selector"]', function (e) {
		var el = e.currentTarget,
			href = el.href,
			sdepId = el.getAttribute('data-superdept'),
			depId = el.getAttribute('data-dept');

		e.preventDefault();

		window.history.pushState({superdeptId: sdepId, deptId: depId}, "Shop by category", href);

		DISPATCHER.signal('exploreSuperdepartmentList', { superdeptId: sdepId });
		DISPATCHER.signal('exploreDepartmentList', { superdeptId: sdepId, deptId: depId, list: exploreSelector.getDepartmentList(sdepId) });
	});

	fd.modules.common.utils.register("modules.explore", "selector", exploreSelector, fd);
}(FreshDirect));

/* process URI or fallback to defaults on page load */
(function (fd) {
	var DISPATCHER = fd.common.dispatcher,
		sdepId = fd.utils.getParameterByName('sdep').toLowerCase(),
		depId = fd.utils.getParameterByName('dep').toLowerCase(),
		expdata = fd.explore && fd.explore.data && fd.explore.data.abstractDepartments,
		sdep = null, dep = null,
		superdeptList = [], deptList = [], catList = [];

	if (expdata) {
		//verify sdep
		superdeptList = fd.modules.explore.selector.getSuperdepartmentList();
		if (superdeptList.length) {
			sdep = fd.modules.explore.selector.getSuperdepartment(sdepId);
			if (!sdep && superdeptList.length) {
				//fallback to first
				sdep = superdeptList[0];
			}
			if (sdep) {
				if (sdep.id !== sdepId) {
					//update id
					sdepId = sdep.id;
				}
			}
		}

		//verify dep
		dep = fd.modules.explore.selector.getDepartment(depId, sdepId);
		deptList = fd.modules.explore.selector.getDepartmentList(sdepId);
		if (deptList.length) {
			if (!dep && deptList.length) {
				//fallback to first
				dep = deptList[0];
			}
			if (dep) {
				if (dep.id !== depId) {
					//update id
					depId = dep.id;
				}
			}
		}

		//update display
		if (superdeptList.length) {
			DISPATCHER.signal('exploreSuperdepartmentList', {
				list: superdeptList,
				superdeptId: sdep.id || null,
				deptId: dep.id || null
			});
		}

		if (deptList.length) {
			DISPATCHER.signal('exploreDepartmentList', {
				list: deptList,
				superdeptId: sdep.id || null,
				deptId: dep.id || null
			});
		}

		catList = fd.modules.explore.selector.getCategoryList(depId, sdepId);
		if (catList.length) {
			DISPATCHER.signal('exploreCategoryList', { list: catList });
		}
	}
}(FreshDirect));

/* subscribe for popstate if feature is active */
(function (fd) {
	window.onpopstate = function (e) {
		var state = e.state;
		if (state) {
			var DISPATCHER = fd.common.dispatcher;
			var depId = state.deptId, sdepId = state.superdeptId;

			DISPATCHER.signal('exploreSuperdepartmentList', { superdeptId: sdepId });
			DISPATCHER.signal('exploreDepartmentList', { superdeptId: sdepId, deptId: depId });
			DISPATCHER.signal('exploreCategoryList', { list: fd.modules.explore.selector.getCategoryList(depId, sdepId) });
		}
	};
}(FreshDirect));

