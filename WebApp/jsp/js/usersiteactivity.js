function updateUserActivityAjax(userId, activityDateString, status){
	doAjaxRequestWithGlobal("GET", "trackUserActivity",  {"userId":userId,"activityDateString":activityDateString,"status":status},function(data) {
		$('.fc-day[data-date="'+activityDateString+'"]').html("");
		if (status){
			 $('.fc-day[data-date="'+activityDateString+'"]').css('background', "green");
			 $('.fc-day[data-date="'+activityDateString+'"]').append('<a class="marksiteactivity" style="color:red" href="javascript:updateUserActivityAjax(\''+userId+'\',\''+activityDateString+'\', false);"><span class="fa fa-times  fa-2x" aria-hidden="true"></span></a>');
		} else {
			 $('.fc-day[data-date="'+activityDateString+'"]').css('background', "red");
			 $('.fc-day[data-date="'+activityDateString+'"]').append('<a class="marksiteactivity" style="color:green" href="javascript:updateUserActivityAjax(\''+userId+'\',\''+activityDateString+'\', true);"><span class="fa fa-plus fa-2x" aria-hidden="true"></span></a>');
		}
	},function(error) {
    	famstacklog("ERROR: ", error);
    },false);
}

$('#fullcaledarusersiteactivity').fullCalendar({
    header: {
        left: 'prev,next,today',
        center: 'title',
    },
    height: 550,
    defaultView: 'month',
    weekends: true,
    editable: false,
    droppable: false,
    selectable: false,
    selectHelper: false,
    maxTime: "23:59:59",
    minTime: "0:00:00",
    select: function (start, end, jsEvent, view) {
    },
    /*events: [
           {
              title: '',
              start: '2019-02-16',
              dateString: '2019-02-16'
         	}	        
         ],*/
        eventAfterRender: function(event, element, view) {
           /* $(element).css('width','50px');*/
         },
         eventAfterAllRender: function(view){
        	    if(view.name == 'month')
        	    {                       
        	        $('.fc-day').each(function(){
        	        	$(this).css('position','relative');
        	        	$(this).html("");
        	        	if ($(this).attr("data-active") == undefined){
        	        		if (!($(this).hasClass("fc-sat") || $(this).hasClass("fc-sun"))) {
        	        			$(this).css('background', "red");
        	        		} else{
								$(this).css('background', "");
							}
        	        		if ($("#userSiteActivityAssigneeId").prop("selectedIndex") > 0) {
        	        			$(this).append('<a class="marksiteactivity" style="color:green" href="javascript:updateUserActivityAjax(\''+$("#userSiteActivityAssigneeId").val()+'\',\''+$(this).attr("data-date")+'\', true);"><span class="fa fa-plus fa-2x"></span></a>');
        	        		} else if ($("#userSiteActivityAssigneeId").val() == "-1") {
        	        			 $(this).css('background', "#FFF");
        	        		}
        	        	} else {
        	        		if ($(this).attr("data-active") != "leave") {
	     	        			$(this).css('background', "green");
	     	        			if ($("#userSiteActivityAssigneeId").prop("selectedIndex") > 0) {
	     	        				$(this).append('<a class="marksiteactivity" style="color:red" href="javascript:updateUserActivityAjax(\''+$("#userSiteActivityAssigneeId").val()+'\',\''+$(this).attr("data-date")+'\', false);"><span class="fa fa-times  fa-2x"></span></a>');
	     	        			} else if ($("#userSiteActivityAssigneeId").val() == "-1") {
	        	        			 $(this).css('background', "#FFF");
	        	        		}
        	        		} else {
        	        			$(this).css('background', "yellow");
        	        		}
  
        	        	}
        	         });      
        	    }                   
        	},
         eventRender: function(event, element) {
        	 $('.fc-day[data-date="'+event.dateString+'"]').attr("data-active", event.type);
        	 element.css("display","none");
         }
});          


var refreshCalendar = function() {
	var userId = $("#userSiteActivityAssigneeId").val();
	$('#fullcaledarusersiteactivity .fc-day').removeAttr("data-active");
	var events = {
	        url: "getUserSiteActivityCalendarJson",
	        type: 'GET',
	        data: {
	        	userId: userId
	        }
	    };
	$('#fullcaledarusersiteactivity').fullCalendar('removeEventSource', events);
    $('#fullcaledarusersiteactivity').fullCalendar('addEventSource', events);
    $('#fullcaledarusersiteactivity').fullCalendar('refetchEvents');
};

$("#userSiteActivityAssigneeId").on("change",function(){
	refreshCalendar();
});

$(document).ready(function () {
	refreshCalendar();	
});

