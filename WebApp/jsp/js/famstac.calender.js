  var events_array = [
                        {
                        title: 'Prj10002',
                        start: '2016-09-26T05:00:00',
                        end : '2016-09-26T12',
                        tip: 'Create a logo'},
                    ];
    
    var fullCalendar = function(){
            
        var calendar = function(){
            
            if($("#calendar").length > 0){
                
                function prepare_external_list(){
                    
                    $('#external-events .external-event').each(function() {
                            var eventObject = {title: $.trim($(this).text())};

                            $(this).data('eventObject', eventObject);
                            $(this).draggable({
                                    zIndex: 999,
                                    revert: true,
                                    revertDuration: 0
                            });
                    });                    
                    
                }
                
                
                var date = new Date();
                var d = date.getDate();
                var m = date.getMonth();
                var y = date.getFullYear();

                prepare_external_list();

                var calendar = $('#calendar').fullCalendar({
                    header: {
                        left: 'prev,next today',
                        center: 'title',
                        right: 'agendaDay,agendaWeek,month'
                    },
                    height: 500,
                    defaultView: 'agendaDay',
                    weekends: false,
                    editable: false,
                    eventSources: {url: "assets/ajax_fullcalendar.jsp"},
                    droppable: false,
                    selectable: false,
                    selectHelper: false,
                    maxTime: "22:00:00",
                    minTime: "09:00:00",
                    events:events_array,
                    select: function(start, end, allDay) {
                       /* var title = prompt('Event Title:');
                        if (title) {
                            calendar.fullCalendar('renderEvent',
                            {
                                title: title,
                                start: start,
                                end: end,
                                allDay: allDay
                            },
                            true
                            );
                        }*/
                        calendar.fullCalendar('unselect');
                    },
                    drop: function(date, allDay) {
                    	console.log(allDay);
                        var originalEventObject = $(this).data('eventObject');

                        var copiedEventObject = $.extend({}, originalEventObject);

                        copiedEventObject.start = date;
                        copiedEventObject.allDay = allDay;

                        $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

                        $(this).remove();

                    }
                });
                
                $("#new-event").on("click",function(){
                    var et = $("#new-event-text").val();
                    if(et != ''){
                        $("#external-events").prepend('<a class="list-group-item external-event">'+et+'</a>');
                        prepare_external_list();
                    }
                });
                
            }            
        }
        
        return {
            init: function(){
                calendar();
            }
        }
    }();
    
    fullCalendar.init();