var uiT ;
$(function(){
    
    var tasks = function(){
        
        $("#tasks,#tasks_progreess,#tasks_completed").sortable({
            items: "> .task-primary",
            connectWith: "#tasks_progreess,#tasks_completed",
            handle: ".task-text",            
            receive: function(event, ui) {
            	alert(ui.item);
            	uiT=ui;
            	 return;
                if(this.id == "tasks_completed"){
                   ui.item.addClass("task-complete").find(".task-footer > .pull-right").remove();
                }
                if(this.id == "tasks_progreess"){
                    ui.item.find(".task-footer").append('<div class="pull-right"><span class="fa fa-play"></span> 00:00</div>');
                }                
                page_content_onresize();
            }
        }).disableSelection();
        
    }();
    
});
