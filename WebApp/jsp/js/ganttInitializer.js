gantt.config.preserve_scroll = false;
gantt.config.smart_rendering = false;
gantt.config.static_background = true;
gantt.config.show_task_cells = false;
gantt.config.show_progress = false;
gantt.config.branch_loading = true;
gantt.config.drag_project = true;
//gantt.config.readonly = true;
gantt.config.details_on_dblclick = false;

gantt.templates.tooltip_text = function(start,end,task){
	var label = "Task";
	if (task.parent == 0) {
		label = "Project";
	} 
    return "<b>"+label+":</b> "+task.text+"<br/><b>Duration:</b> " + task.duration;
};

function shouldHighlightTask(task){
		var store = gantt.$resourcesStore;
		var taskResource = task[gantt.config.resource_property],
			selectedResource = store.getSelectedId();
		if(taskResource == selectedResource || store.isChildOf(taskResource, selectedResource)){
			return true;
		}
	}

	gantt.templates.task_class  = function(start, end, task){
	 if(task.parent == 0) {
		 return "projectTask";
	 } else if (task.type == 'task'){
		 return "taskTask";
	 }
	};

	gantt.templates.grid_row_class = function(start, end, task){
		var css = [];
		if(gantt.hasChild(task.id)){
			css.push("folder_row");
		}

		if(task.$virtual){
			css.push("group_row");
		}

		if(shouldHighlightTask(task)){
			css.push("highlighted_resource");
		}

		return css.join(" ");
	};

	gantt.templates.task_row_class = function(start, end, task){
		if(shouldHighlightTask(task)){
			return "highlighted_resource";
		}
		return "";
	};

	gantt.templates.scale_cell_class = function(date){
	    if(date.getDay()==0||date.getDay()==6){
	        return "weekend";
	    }
	};
	
	gantt.templates.task_cell_class = function (task, date) {
		if (!gantt.isWorkTime({date: date, task: task}))
			return "weekend";
		return "";
	};

	gantt.templates.resource_cell_class = function(start_date, end_date, resource, tasks){
		var css = [];
		css.push("resource_marker");
		if (tasks.length <= 1) {
			css.push("workday_ok");
		} else {
			css.push("workday_over");
		}
		return css.join(" ");
	};

	gantt.templates.resource_cell_value = function(start_date, end_date, resource, tasks){
		var html = "<div>"
		if(resourceMode == "hours"){
			html += (tasks.length) * 8;
		}else{
			html += (tasks.length);
		}
		html += "</div>";
		return html;
	};

	function shouldHighlightResource(resource){
		var selectedTaskId = gantt.getState().selected_task;
		if(gantt.isTaskExists(selectedTaskId)){
			var selectedTask = gantt.getTask(selectedTaskId),
				selectedResource = selectedTask[gantt.config.resource_property];

			if(resource.id == selectedResource){
				return true;
			}else if(gantt.$resourcesStore.isChildOf(selectedResource, resource.id)){
				return true;
			}
		}
		return false;
	}

	var resourceTemplates = {
		grid_row_class: function(start, end, resource){
			var css = [];
			if(gantt.$resourcesStore.hasChild(resource.id)){
				css.push("folder_row");
				css.push("group_row");
			}
			if(shouldHighlightResource(resource)){
				css.push("highlighted_resource");
			}
			return css.join(" ");
		},
		task_row_class: function(start, end, resource){
			var css = [];
			if(shouldHighlightResource(resource)){
				css.push("highlighted_resource");
			}
			if(gantt.$resourcesStore.hasChild(resource.id)){
				css.push("group_row");
			}

			return css.join(" ");

		}
	};

	gantt.locale.labels.section_owner = "Owner";
	gantt.config.lightbox.sections = [
		{name: "description", height: 38, map_to: "text", type: "textarea", focus: true},
		{name: "owner", height: 22, map_to: "owner_id", type: "select", options: gantt.serverList("people")},
		{name: "time", type: "duration", map_to: "auto"}
	];

	function getResourceTasks(resourceId){
		var store = gantt.getDatastore(gantt.config.resource_store),
			field = gantt.config.resource_property,
			tasks;

		if(store.hasChild(resourceId)){
			tasks = gantt.getTaskBy(field, store.getChildren(resourceId));
		}else{
			tasks = gantt.getTaskBy(field, resourceId);
		}
		return tasks;
	}

	var resourceConfig = {
		scale_height: 30,
		subscales: [],
		show_grid: false,
		columns: [
			{
				name: "name", label: "Name", tree:true, width:200, template: function (resource) {
					return resource.text;
				}, resize: true
			},
			{
				name: "progress", label: "Complete", align:"center",template: function (resource) {
					var tasks = getResourceTasks(resource.id);

					var totalToDo = 0,
						totalDone = 0;
					tasks.forEach(function(task){
						totalToDo += task.duration;
						totalDone += task.duration * (task.progress || 0);
					});

					var completion = 0;
					if(totalToDo){
						completion = Math.floor((totalDone / totalToDo)*100);
					}

					//return Math.floor(completion) + "%";
					return "0%";
				}, resize: true
			},
			{
				name: "workload", label: "Workload", align:"center", template: function (resource) {
					var tasks = getResourceTasks(resource.id);
					var totalDuration = 0;
					tasks.forEach(function(task){
						totalDuration += task.duration;
					});

					return (totalDuration || 0) * 8 + "h";
				}, resize: true
			},

			{
				name: "capacity", label: "Capacity", align:"center",template: function (resource) {
					var store = gantt.getDatastore(gantt.config.resource_store);
					var n = store.hasChild(resource.id) ? store.getChildren(resource.id).length : 1

					var state = gantt.getState();

					return gantt.calculateDuration(state.min_date, state.max_date) * n * 8 + "h";
				}
			}

		]
	};

	gantt.config.subscales = [
		{unit: "month", step: 1, date: "%F, %Y"}
	];

	gantt.config.auto_scheduling = true;
	gantt.config.auto_scheduling_strict = true;
	gantt.config.work_time = true;
	gantt.config.columns = [
		{name: "text", tree: true, width: 200, resize: true},
		{name: "start_date", align: "center", width: 80, resize: true},
		{name: "owner", align: "center", width: 80, label: "Owner", template: function (task) {
			if(task.type == gantt.config.types.project || task.parent == 0){
				return "";
			}

			var store = gantt.getDatastore(gantt.config.resource_store);
			var owner = store.getItem(task[gantt.config.resource_property]);
			if (owner) {
				return owner.text;
			} else {
				return "Unassigned";
			}
		}, resize: true},
		{name: "duration", width: 60, align: "center", resize: true}
	];

	gantt.config.resource_store = "resource";
	gantt.config.resource_property = "owner_id";
	gantt.config.order_branch = true;
	gantt.config.open_tree_initially = false;
	gantt.config.scale_height = 27;

	
	noresource_layout = {
			css: "gantt_container",
			rows: [
				{
					cols: [
						{view: "grid", group:"grids", scrollY: "scrollVer"},
						{resizer: true, width: 1},
						{view: "timeline", scrollX: "scrollHor", scrollY: "scrollVer"},
						{view: "scrollbar", id: "scrollVer", group:"vertical"}
					],
					gravity:2
				},
				{view: "scrollbar", id: "scrollHor"}
			]
		};
	
	resource_layout = {
		css: "gantt_container",
		rows: [
			{
				gravity: 2,
				cols: [
					{view: "grid", group:"grids", scrollY: "scrollVer"},
					{resizer: true, width: 1},
					{view: "timeline", scrollX: "scrollHor", scrollY: "scrollVer"},
					{view: "scrollbar", id: "scrollVer", group:"vertical"}
				]
			},
			{ resizer: true, width: 1, next: "resources"},
			{
				height: 35,
				cols: [
					{ html:"", group:"grids"},
					{ resizer: true, width: 1},
					{ html:"<label class='active' >Hours per day <input checked type='radio' name='resource-mode' value='hours'></label>" +
					"<label>Tasks per day <input type='radio' name='resource-mode' value='tasks'></label>", css:"resource-controls"}
				]
			},

			{
				gravity:1,
				id: "resources",
				config: resourceConfig,
				templates: resourceTemplates,
				cols: [
					{ view: "resourceGrid", group:"grids", scrollY: "resourceVScroll" },
					{ resizer: true, width: 1},
					{ view: "resourceTimeline", scrollX: "scrollHor", scrollY: "resourceVScroll"},
					{ view: "scrollbar", id: "resourceVScroll", group:"vertical"}
				]
			},
			{view: "scrollbar", id: "scrollHor"}
		]
	};

	var resourceMode = "hours";
	gantt.attachEvent("onGanttReady", function(){
		var radios = [].slice.call(gantt.$container.querySelectorAll("input[type='radio']"));
		radios.forEach(function(r){
			gantt.event(r, "change", function(e){
				var radios = [].slice.call(gantt.$container.querySelectorAll("input[type='radio']"));
				radios.forEach(function(r){
					r.parentNode.className = r.parentNode.className.replace("active", "");
				});

				if(this.checked){
					resourceMode = this.value;
					this.parentNode.className += " active";
					gantt.getDatastore(gantt.config.resource_store).refresh();
				}
			});
		});
	});

	gantt.$resourcesStore = gantt.createDatastore({
		name: gantt.config.resource_store,
		type: "treeDatastore",
		initItem: function (item) {
			item.parent = item.parent || gantt.config.root_id;
			item[gantt.config.resource_property] = item.parent;
			item.open = true;
			return item;
		}
	});

	gantt.$resourcesStore.attachEvent("onAfterSelect", function(id){
		gantt.refreshData();
	});

	gantt.config.layout =resource_layout;
	//gantt.init("gantt_here");
	//gantt.parse(demo_tasks);
    //gantt.setSizes();
    
    
    
	function toggleGroups(input) {
		gantt.$groupMode = !gantt.$groupMode;
		if (gantt.$groupMode) {
			$(input).html('Project View');

			var groups = gantt.$resourcesStore.getItems().map(function(item){
				var group = gantt.copy(item);
				group.group_id = group.id;
				group.id = gantt.uid();
				return group;
			});

			gantt.groupBy({
				groups: groups,
				relation_property: gantt.config.resource_property,
				group_id: "group_id",
				group_text: "text"
			});
		} else {
			$(input).html('Resource View');
			gantt.groupBy(false);
		}
	}

	gantt.$resourcesStore.attachEvent("onParse", function(){
		var people = [];
		gantt.$resourcesStore.eachItem(function(res){
			if(!gantt.$resourcesStore.hasChild(res.id)){
				var copy = gantt.copy(res);
				copy.key = res.id;
				copy.label = res.text;
				people.push(copy);
			}
		});
		gantt.updateCollection("people", people);
	});

/*	gantt.$resourcesStore.parse([
		{id: 5, text: "Unassigned", parent:null},
		{id: 6, text: "John", parent:null},
		{id: 7, text: "Mike", parent:null},
		{id: 8, text: "Anna", parent:null},
		{id: 9, text: "Bill", parent:null},
		{id: 10, text: "Floe", parent:null}
	]);
	
	gantt.$resourcesStore.parse([{"id":42406,"text":"Famstack Cordinator "},
	                             {"id":1000,"text":"Famstack Manager "},
	                             {"id":648052,"text":"IT Team"}]
	);*/
	
	function modHeight(){
        var headHeight = 35;
        var sch = document.getElementById("gantt_here");
        sch.style.height = (parseInt(document.body.offsetHeight)-headHeight)+"px";
        gantt.setSizes();
}
	var resource_chart_flag = true;
	function layoutChange(resource_chart){
		if(!resource_chart) {
			resource_chart_flag = true;
		}
			resource_chart_flag = !resource_chart_flag;
			if (resource_chart_flag){
				gantt.config.layout = resource_layout;
			} else{
				gantt.config.layout = noresource_layout;
			}
			// gantt.render();
			gantt.init("gantt_here");
	};
	
$("#scale0").on("click", function(){
	layoutChange(false);
	$(".showResourceChart").hide();
});

$("#scale1").on("click", function(){
	layoutChange(false);
	$(".showResourceChart").hide();
});

$(".scaleButton").on("click", function(){
	$(".showResourceChart").show();
});
