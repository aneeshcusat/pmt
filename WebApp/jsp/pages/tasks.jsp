<%@include file="includes/header.jsp" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Tasks</li>
 </ul>
 <style>
 	#taskCompletionModal .modal-dialog {
		width: 75%;
	}
	
	#taskStartModal  .modal-dialog {
		width: 75%;
	}
 </style>
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
    </div>                    
          
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME BODY -->
        <div class="row ">
            <div class="col-md-4">
                
                
	<a data-toggle="modal" data-target="#taskCompletionModal" class="hide taskCompletionLink" data-backdrop="static">complete</a>
<a data-toggle="modal" data-target="#taskStartModal" class="hide taskStartLink" data-backdrop="static">start</a>
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

<!-- task completion modal start -->
<div class="modal fade" id="taskCompletionModal" tabindex="-1"
	role="dialog" aria-labelledby="taskCompletionModal" aria-hidden="true">
	<form:form id="taskCompletionModal" action="taskCompletion" method="POST"
		role="form" class="form-horizontal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Task Completion</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/taskCompletionModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary taskWindowCancel"
						data-dismiss="modal">Cancel</button>
					<button type="button" onclick=""
						class="btn btn-primary">
						<span id="taskComplete">Complete</span>
					</button>
				</div>
			</div>
		</div>
	</form:form>
</div>
<!-- project create modal end -->  

<!-- task completion modal start -->
<div class="modal fade" id="taskStartModal" tabindex="-1"
	role="dialog" aria-labelledby="taskStartModal" aria-hidden="true">
	<form:form id="taskCompletionModal" action="taskStart" method="POST"
		role="form" class="form-horizontal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Start this task</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/taskStartModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary taskWindowCancel"
						data-dismiss="modal">Cancel</button>
					<button type="button" onclick=""
						class="btn btn-primary">
						<span id="taskStart">Start</span>
					</button>
				</div>
			</div>
		</div>
	</form:form>
</div>
<!-- project create modal end -->  
     
<!-- END MODALS -->
<%@include file="includes/footer.jsp" %>  
<script type="text/javascript"
	src="${js}/plugins/dropzone/dropzone.min.js"></script>
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
                if(this.id == "tasks_completed"){
                	$(".taskCompletionLink").click();
                   ui.item.addClass("task-complete").find(".task-footer > .pull-right").remove();
                   // call complted ajax
                }
                if(this.id == "tasks_progreess"){
                	$(".taskStartLink").click();
                    ui.item.find(".task-footer").append('<div class="pull-right"><span class="fa fa-play"></span> 00:00</div>');
                    //call assign task
                }                
                page_content_onresize();
            }
        }).disableSelection();
        
    }();
    
    $(".taskWindowCancel").click(function(){
    	$(lastMovedItem.sender).sortable('cancel');
    });
});

Dropzone.autoDiscover = false;
$("#my-dropzone").dropzone({
	url : "${applicationHome}/uploadfile/${projectDetails.code}",
	addRemoveLinks : false,
	success : function(file, response) {
		var imgName = response;
		file.previewElement.classList.add("dz-success");
		var fileIcon = "fa-file-text";
		if (file.type == "text/xml") {
			fileIcon = "fa-file-excel-o";
		}
		
		$("#upladedFilesList").append('<li><a href="${applicationHome}/download/${projectDetails.code}/'+file.name+'?fileName='+file.name+'"><i class="fa '+fileIcon+'"></i>'+file.name+'</a></li>');
		console.log(file.name);
	},
	error : function(file, response) {
		file.previewElement.classList.add("dz-error");
	}
});

$("#taskStart").click(function(){
	$(".modal").hide();
});

$("#taskComplete").click(function(){
	$(".modal").hide();
});

</script>
