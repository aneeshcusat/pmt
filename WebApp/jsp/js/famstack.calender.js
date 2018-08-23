 var fullCalendar = function(){
        var calendartu = function(){
        	$('#fullcaledartu').fullCalendar({
        	    header: {
        	        left: 'today',
        	        center: 'prev,title,next',
        	        right: 'agendaDay,agendaWeek,month'
        	    },
        	    height: 375,
        	    defaultView: 'month',
        	    weekends: true,
        	    editable: false,
        	    eventSources: {url: "getAjaxFullcalendar.jsp"},
        	    droppable: false,
        	    selectable: false,
        	    selectHelper: false,
        	    maxTime: "23:59:59",
        	    minTime: "0:00:00",
        	    events: [{
        	            title: 'Tracker study',
        	            start: '2018-08-14',
        	            textColor: '#fff',
        	            color:'#20CEC9'
        	          },
        	          {
        	              title: 'Template Creation',
        	              start: '2018-08-14',
        	              textColor: '#fff',
        	              color:'#20CEC9'
        	          },
        	          {
        	              title: 'Template Creation1',
        	              start: '2018-08-14',
        	              textColor: '#fff',
        	              color:'#20CEC9'
        	          },
        	          {
        	              title: 'Template Creation2',
        	              start: '2018-08-14',
        	              textColor: '#fff',
        	              color:'#20CEC9'
        	          }
        	        ],
        	        dayRender: function(date, cell) {
        	            var today = $.fullCalendar.moment();
        	            var end = $.fullCalendar.moment().add(1, 'days');
        	            if (date.get('date') == end.get('date')) {
        	                cell.css("background", "darkgray");
        	 			}
        			},
        			eventRender: function(event, element) {
                     	 element.popover({
                 	        title: event.taskName,
                 	        content: function () {
                                 return "<table class='calenderEventPopOver'><tbody><tr><td>Task Status</td><td>"+event.taskStatus+"</td></tr><tr><td>Task Category</td><td>"+event.taskActCategory+"</td></tr><tr><td>TaskType</td><td>"+event.projectType+"</td></tr><tr><td>Assignee</td><td>"+event.assigneeName+"</td></tr><tr><td>Project Id</td><td>"+event.projectId+"</td></tr><tr><td>Est Task Start Time</td><td>"+event.estTaskStartTime+"</td></tr><tr><td>Est Task End Time</td><td>"+event.estTaskEndTime+"</td></tr><tr><td>Act Task Start Time</td><td>"+event.taskActActualStartTime+"</td></tr><tr><td>Act Task End Time</td><td>"+event.taskActActualEndTime+"</td></tr></tbody></table>";
                             },
                 	        animation:true,
                             delay: 300,
                 	        trigger: 'hover',
                 	        placement: 'top',
                 	        container: 'body',
                 	        html: 'true',
                 	      });
                     },
        	    
        	    
        	});

        }
        var calendarbs = function(){
        	$('#fullcaledarbs').fullCalendar({
        	    header: {
        	        left: 'today',
        	        center: 'prev,title,next',
        	        right: 'agendaDay,agendaWeek,month'
        	    },
        	    height: 550,
        	    defaultView: 'month',
        	    weekends: true,
        	    editable: false,
        	    eventSources: {url: "assets/ajax_fullcalendar.jsp"},
        	    droppable: false,
        	    selectable: false,
        	    selectHelper: false,
        	    maxTime: "23:59:59",
        	    minTime: "0:00:00",
        	    events: [{
        	            title: 'Amit',
        	            start: '2018-08-14',
        	            textColor: '#fff',
        	            color:'#EF8175'
        	          },
        	          {
        	              title: 'Vinay',
        	              start: '2018-08-14',
        	              textColor: '#fff',
        	              color:'#E5E5E5'
        	          },
        	          {
        	              title: 'Udhay',
        	              start: '2018-08-15',
        	              textColor: '#fff',
        	              color:'#EF8175'
        	          },
        	          {
        	              title: 'Vinay',
        	              start: '2018-08-16',
        	              textColor: '#fff',
        	              color:'#E5E5E5'
        	          }
        	        ],
        	        eventAfterRender: function(event, element, view) {
        	            $(element).css('width','50px');
        	         }
        	});          
        }
        return {
            initbs: function(){
                calendarbs();
            },
            inittu: function(){
                calendartu();
            }
        }
    }();