function setScaleConfig(value) {
		gantt.config.fit_tasks = true;
		switch (value) {
			case "0":
				gantt.config.xml_date = "%d-%m-%Y %H:%i";
				gantt.config.scale_unit = "hour";
				gantt.config.step = 1;
				gantt.config.date_scale = "%g %a";
				gantt.config.min_column_width = 20;
				gantt.config.duration_unit = "minute";
				gantt.config.duration_step = 60;
				gantt.config.scale_height = 27;
				gantt.config.subscales = [
				                  		{unit: "day", step: 1, date: "%j %F, %l"},
				                  		{unit: "minute", step: 15, date: "%i"}
				                  	];
				break;
			case "1":
				gantt.config.scale_unit = "day";
				gantt.config.date_scale = "%l, %F %d";
				gantt.config.min_column_width = 20;
				gantt.config.scale_height = 27;
				gantt.config.subscales = [
					{unit: "hour", step: 1, date: "%G"}
				];

				break;
			case "2":
				gantt.config.scale_unit = "day";
				gantt.config.step = 1;
				gantt.config.date_scale = "%d %M";
				gantt.config.subscales = [{unit: "month", step: 1, date: "%F, %Y"}];
				gantt.config.scale_height = 27;
				gantt.config.min_column_width = 70;
				gantt.templates.date_scale = null;
				break;
			case "3":
				var weekScaleTemplate = function (date) {
					var dateToStr = gantt.date.date_to_str("%d %M");
					var startDate = gantt.date.week_start(new Date(date));
					var endDate = gantt.date.add(gantt.date.add(startDate, 1, "week"), -1, "day");
					return dateToStr(startDate) + " - " + dateToStr(endDate);
				};

				gantt.config.scale_unit = "week";
				gantt.config.step = 1;
				gantt.templates.date_scale = weekScaleTemplate;
				gantt.config.subscales = [
					{unit: "day", step: 1, date: "%D"}
				];
				gantt.config.scale_height = 27;
				break;
			case "4":
				gantt.config.scale_unit = "month";
				gantt.config.date_scale = "%F, %Y";
				gantt.config.subscales = [
					{unit: "day", step: 1, date: "%j, %D"}
				];
				gantt.config.scale_height = 27;
				gantt.templates.date_scale = null;
				break;
			case "5":
				gantt.config.scale_unit = "month";
				gantt.config.step = 1;
				gantt.config.date_scale = "%M";

				gantt.config.scale_height = 27;
				gantt.templates.date_scale = null;

				gantt.config.subscales = [
					{
						unit: "quarter", step: 1, template: function (date) {
							var dateToStr = gantt.date.date_to_str("%M");
							var endDate = gantt.date.add(gantt.date.add(date, 3, "month"), -1, "day");
							return dateToStr(date) + " - " + dateToStr(endDate);
						}
					}
				];
				break;
			case "6":
				gantt.config.scale_unit = "year";
				gantt.config.step = 1;
				gantt.config.date_scale = "%Y";
				gantt.config.min_column_width = 50;

				gantt.config.scale_height = 27;
				gantt.templates.date_scale = null;


				gantt.config.subscales = [
					{unit: "month", step: 1, date: "%M"}
				];
				break;
		}
	}

	var func = function (e) {
		e = e || window.event;
		var el = e.target || e.srcElement;
		var value = el.value;
		setScaleConfig(value);
		gantt.render();
	};

	var els = document.getElementsByName("scale");
	for (var i = 0; i < els.length; i++) {
		els[i].onclick = func;
	}
	

	var labelEls = document.getElementsByName("scalelabel");
	for (var i = 0; i < labelEls.length; i++) {
		labelEls[i].onclick = function() {
			updTargetEl(this, "checked_label");

			var labelEls = document.getElementsByTagName("label");
			updOtherEls(labelEls, this, "checked_label")

			var el = this.querySelector("i");
			updTargetEl(el, "icon_color");

			var iTagEls = document.getElementsByTagName("i");
			updOtherEls(iTagEls, el, "icon_color");
			updElsContent(iTagEls, el);
		}
	}

	function updTargetEl(el, className){
		if(el.classList.contains(className)) return

		el.classList.add(className);
	}

	function updOtherEls(arr, targetEl, className){
		for (var i = 0; i < arr.length; i++) {
			if(arr[i] != targetEl && arr[i].classList.contains(className))
				arr[i].classList.remove(className);
		}
	}

	function updElsContent(arr, targetEl){
		for (var i = 0; i < arr.length; i++) {
			arr[i].textContent = arr[i]==targetEl?"radio_button_checked":"radio_button_unchecked";
		}
	}

