<%@include file="includes/header.jsp" %>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Tasks</li>
 </ul>
<!-- START CONTENT FRAME -->
<div class="content-frame">     
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-tasks"></span> Tasks</h2>
        </div>                                                
        <div class="pull-right">
            <button class="btn btn-default content-frame-left-toggle"><span class="fa fa-bars"></span></button>
        </div>                                
        <div class="pull-right" style="width: 100px; margin-right: 5px;">
            <select class="form-control select">
                <option>All</option>                                
                <option>Primary</option>
                <option>Secondary</option>
            </select>
        </div>
    </div>                    
          
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME BODY -->
        <div class="row ">
            <div class="col-md-4">
                
                <h3>To-do List</h3>
                
                <div class="tasks" id="tasks">

					<c:if test="${not empty modelViewMap.projectTaskDetailsData}">
			        <c:forEach var="tasks" items="${modelViewMap.projectTaskDetailsData['ASSIGNED']}" varStatus="taskIndex">
			        <div class='task-item task-danger 
			          <c:if test="${taskIndex.index == 0 && empty modelViewMap.projectTaskDetailsData['INPROGRESS']}">
			        	task-primary
			         </c:if>
			        '>                                    
                        <div class="task-text">${tasks.name}</div>
                        <div class="task-text"><a target="_new" href="${applicationHome}/project/${tasks.projectId}">Show project</a></div>
                        <div class="task-footer">
                            <div class="pull-right"><span class="fa fa-clock-o"></span> ${tasks.duration} Hours</div>     
                            <div class="pull-left">Start Time : ${tasks.startTime}</div>      
                        </div>                                    
                    </div>
			        </c:forEach>
			        </c:if>
                </div>                            

            </div>
            <div class="col-md-4">
                <h3>In Progress</h3>
                <div class="tasks" id="tasks_progreess">
					<c:if test="${not empty modelViewMap.projectTaskDetailsData}">
			        <c:forEach var="tasks" items="${modelViewMap.projectTaskDetailsData['INPROGRESS']}">
			        <c:if test='${tasks.status == "INPROGRESS"}'>
			        <div class="task-item task-primary">                                    
                        <div class="task-text">${tasks.name}</div>
                        <div class="task-text"><a target="_new" href="${applicationHome}/project/${tasks.projectId}">Show project</a></div>
                        <div class="task-footer">
                            <div class="pull-left"><span class="fa fa-clock-o"></span> Time remaining : 2h 55min</div>
                            <div class="pull-right"><a href="holdProject"><span class="fa fa-pause"></span></a> 4:51</div>                  
                        </div>                                    
                    </div>
			        </c:if>
			        </c:forEach>
			        </c:if>
			        <div class="task-item task-primary">
                        <div class="task-text">In mauris nunc, blandit a turpis in, vehicula viverra metus. Quisque dictum purus lorem, in rhoncus justo dapibus eget. Aenean pretium non mauris et porttitor.</div>
                        <div class="task-footer">
                            <div class="pull-left">Time remaining :  2h 55min</div>
                            <div class="pull-right"><a href="holdProject"><span class="fa fa-pause"></span></a> 4:51</div>
                        </div>                                    
                    </div>  
                    <div class="task-drop push-down-10">
                        <span class="fa fa-cloud"></span>
                        Drag your task here to start it tracking time
                    </div>
                    
                </div>
            </div>
            <div class="col-md-4">
                <h3>Completed</h3>
                <div class="tasks" id="tasks_completed">
                  <c:if test="${not empty modelViewMap.projectTaskDetailsData}">
			        <c:forEach var="tasks" items="${modelViewMap.projectTaskDetailsData['COMPLETED']}">
			        <c:if test='${tasks.status == "COMPLETED"}'>
			        <div class="task-item task-info task-complete">                                    
                        <div class="task-text">${tasks.name}</div>
                        <div class="task-text"><a target="_new" href="${applicationHome}/project/${tasks.projectId}">Show project</a></div>
                        <div class="task-footer">
                             <div class="pull-left"><span class="fa fa-clock-o"></span> 35min</div>                                         
                        </div>                                    
                    </div>
			        </c:if>
			        </c:forEach>
			        </c:if>
                   
                    <div class="task-drop">
                        <span class="fa fa-cloud"></span>
                        Drag your task here to finish it
                    </div>                                    
                </div>
            </div>
        </div>                        
                                
    <!-- END CONTENT FRAME BODY -->
    
</div>
<!-- END CONTENT FRAME -->

<!-- MODALS -->        
<div class="modal fade" id="taskEdit" tabindex="-1" role="dialog" aria-labelledby="taskEditModalHead" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="taskEditModalHead">Edit Task</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>Task description</label>
                    <textarea class="form-control" id="task-text" rows="4"></textarea>
                </div>
                <div class="form-group">
                    <label>Task group</label>
                    <select class="form-control select">
                        <option>Work</option>
                        <option>Home</option>
                        <option>Friends</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">                        
                <button type="button" class="btn btn-primary">Save changes</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>        
<!-- END MODALS -->
<%@include file="includes/footer.jsp" %>  
<script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
<script>
var lastMovedItem ;
$(function(){
    
    var tasks = function(){
        
        $("#tasks,#tasks_progreess,#tasks_completed").sortable({
            items: "> .task-primary",
            connectWith: "#tasks_progreess,#tasks_completed",
            handle: ".task-text",            
            receive: function(event, ui) {
            	lastMovedItem=ui;
            	 return;
                if(this.id == "tasks_completed"){
                   ui.item.addClass("task-complete").find(".task-footer > .pull-right").remove();
                   // call complted ajax
                }
                if(this.id == "tasks_progreess"){
                    ui.item.find(".task-footer").append('<div class="pull-right"><span class="fa fa-play"></span> 00:00</div>');
                    //call assign task
                }                
                page_content_onresize();
            }
        }).disableSelection();
        
    }();
    
});

</script>
