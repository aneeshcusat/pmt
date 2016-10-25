<%@include file="includes/header.jsp" %>
<style>
@media screen and (min-width: 700px) {
	.project_progress .progress {
		margin-bottom: 5px;
		height: 10px;
	}
}
</style>
 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Dashboard</li>
 </ul>
 <div class="col-md-12">
 <ul style="float:right;margin-bottom: 3px;">
   <li>
        <div id="reportrange" class="dtrange">                                            
            <span></span><b class="caret"></b>
        </div>                                     
    </li>    
</ul>
</div>
 <!-- END BREADCRUMB -->  
                <!-- PAGE CONTENT WRAPPER -->
                <div class="page-content-wrap">
                    
                    <!-- START WIDGETS -->                    
                    <div class="row">
                        <div class="col-md-3">
                            
                            <!-- START WIDGET SLIDER -->
                            <div class="widget widget-default widget-carousel">
                                <div class="owl-carousel" id="owl-example">
                                 	<div>                                    
                                        <div class="widget-title">Upcoming Projects</div>
                                        <div class="widget-int">${projectsCount['NEW'] }</div>
                                         <div class="widget-subtitle"><a href="projects/NEW">View details</a></div>
                                    </div>
                                    <div>                                    
                                        <div class="widget-title">Unassigned Projects</div>                                                                        
                                        <div class="widget-int">${projectsCount['UNASSIGNED'] }</div>
                                        <div class="widget-subtitle"><a href="projects/UNASSIGNED">View details</a></div>
                                    </div>
                                    <div>                                    
                                        <div class="widget-title">Assigned Projects</div>
                                        <div class="widget-int">${projectsCount['ASSIGNED'] }</div>
                                         <div class="widget-subtitle"><a href="projects/ASSIGNED">View details</a></div>
                                    </div>
                                      <div>                                    
                                        <div class="widget-title">Inprogress Projects</div>
                                        <div class="widget-int">${projectsCount['INPROGRESS'] }</div>
                                         <div class="widget-subtitle"><a href="projects/INPROGRESS">View details</a></div>
                                    </div>
                                   
                                </div>                            
                                <div class="widget-controls">                                
                                    <a href="#" class="widget-control-right widget-remove" data-toggle="tooltip" data-placement="top" title="Remove Widget"><span class="fa fa-times"></span></a>
                                </div>                             
                            </div>         
                            <!-- END WIDGET SLIDER -->
                            
                        </div>
                        <div class="col-md-3">
                            
                            <!-- START WIDGET MESSAGES -->
                            <div class="widget widget-default widget-item-icon" onclick="location.href='projects/COMPLETED';">
                                <div class="widget-item-left">
                                    <span class="fa fa-tasks"></span>
                                </div>                             
                                <div class="widget-data">
                                    <div class="widget-int num-count">${projectsCount['COMPLETED'] }</div>
                                    <div class="widget-title">Completed Projects</div>
                                    <div class="widget-subtitle">Great job!</div>
                                </div>      
                                <div class="widget-controls">                                
                                    <a href="#" class="widget-control-right widget-remove" data-toggle="tooltip" data-placement="top" title="Remove Widget"><span class="fa fa-times"></span></a>
                                </div>
                            </div>                            
                            <!-- END WIDGET MESSAGES -->
                            
                        </div>
                        <div class="col-md-3">
                            
                            <!-- START WIDGET REGISTRED -->
                            <div class="widget widget-default widget-item-icon" onclick="location.href='projects/MISSED';">
                                <div class="widget-item-left">
                                    <span class="fa fa fa-exclamation-triangle"></span>
                                </div>
                                <div class="widget-data">
                                    <div class="widget-int num-count">${projectsCount['MISSED']}</div>
                                    <div class="widget-title">Missed Timeline</div>
                                   <div class="widget-subtitle">Not on track</div>
                                </div>
                                <div class="widget-controls">                                
                                    <a href="#" class="widget-control-right widget-remove" data-toggle="tooltip" data-placement="top" title="Remove Widget"><span class="fa fa-times"></span></a>
                                </div>                            
                            </div>                            
                            <!-- END WIDGET REGISTRED -->
                            
                        </div>
                        <div class="col-md-3">
                            
                            <!-- START WIDGET CLOCK -->
                            <div class="widget widget-info widget-padding-sm">
                                <div class="widget-big-int plugin-clock">00:00</div>                            
                                <div class="widget-subtitle plugin-date">Loading...</div>
                                <div class="widget-controls">                                
                                    <a href="#" class="widget-control-right widget-remove" data-toggle="tooltip" data-placement="left" title="Remove Widget"><span class="fa fa-times"></span></a>
                                </div>                            
                                <div class="widget-buttons widget-c3">
                                    <div class="col">
                                        <a href="#"><span class="fa fa-clock-o"></span></a>
                                    </div>
                                    <div class="col">
                                        <a href="#"><span class="fa fa-bell"></span></a>
                                    </div>
                                    <div class="col">
                                        <a href="#"><span class="fa fa-calendar"></span></a>
                                    </div>
                                </div>                            
                            </div>                        
                            <!-- END WIDGET CLOCK -->
                            
                        </div>
                    </div>
                    <!-- END WIDGETS -->                    
                    
                    <div class="row">
                    <div class="col-md-4">
                     <!-- START USERS ACTIVITY BLOCK -->
                    <div class="row">
                        <div class="col-md-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="panel-title-box">
                                        <h3>Employee Utilization</h3>
                                        <span>Billable/NonBillable</span>
                                    </div>                                    
                                    <ul class="panel-controls" style="margin-top: 2px;">
                                        <li><a href="#" class="panel-fullscreen"><span class="fa fa-expand"></span></a></li>
                                        <li><a href="#" class="panel-refresh"><span class="fa fa-refresh"></span></a></li>
                                        <li class="dropdown">
                                            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="fa fa-cog"></span></a>                                        
                                            <ul class="dropdown-menu">
                                                <li><a href="#" class="panel-collapse"><span class="fa fa-angle-down"></span> Collapse</a></li>
                                                <li><a href="#" class="panel-remove"><span class="fa fa-times"></span> Remove</a></li>
                                            </ul>                                        
                                        </li>                                        
                                    </ul>                                    
                                </div>                                
                                <div class="panel-body padding-0">
                                    <div class="chart-holder" id="dashboard-bar-emp" style="height: 200px;"></div>
                                </div>                                    
                            </div>
                        </div>
                        </div>
                         <!-- END USERS ACTIVITY BLOCK -->
                         <!-- START VISITORS BLOCK -->
                        <div class="row">
                        <div class="col-md-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="panel-title-box">
                                        <h3>Project chart</h3>
                                        <span>Project chart</span>
                                    </div>
                                    <ul class="panel-controls" style="margin-top: 2px;">
                                        <li><a href="#" class="panel-fullscreen"><span class="fa fa-expand"></span></a></li>
                                        <li><a href="#" class="panel-refresh"><span class="fa fa-refresh"></span></a></li>
                                        <li class="dropdown">
                                            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="fa fa-cog"></span></a>                                        
                                            <ul class="dropdown-menu">
                                                <li><a href="#" class="panel-collapse"><span class="fa fa-angle-down"></span> Collapse</a></li>
                                                <li><a href="#" class="panel-remove"><span class="fa fa-times"></span> Remove</a></li>
                                            </ul>                                        
                                        </li>                                        
                                    </ul>
                                </div>
                                <div class="panel-body padding-0">
                                    <div class="chart-holder" id="dashboard-donut-1" style="height: 200px;"></div>
                                </div>
                            </div>
                        </div>
                        </div>
                         <!-- END VISITORS BLOCK -->
                        </div>
                        <div class="col-md-5">
                    	<!-- START PROJECTS BLOCK -->
                    	<div class="row">
                        <div class="col-md-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="panel-title-box">
                                        <h3>Projects</h3>
                                        <span>Projects activity</span>
                                    </div>                                    
                                    <ul class="panel-controls" style="margin-top: 2px;">
                                        <li><a href="#" class="panel-fullscreen"><span class="fa fa-expand"></span></a></li>
                                        <li><a href="#" class="panel-refresh"><span class="fa fa-refresh"></span></a></li>
                                        <li class="dropdown">
                                            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="fa fa-cog"></span></a>                                        
                                            <ul class="dropdown-menu">
                                                <li><a href="#" class="panel-collapse"><span class="fa fa-angle-down"></span> Collapse</a></li>
                                                <li><a href="#" class="panel-remove"><span class="fa fa-times"></span> Remove</a></li>
                                            </ul>                                        
                                        </li>                                        
                                    </ul>
                                </div>
                                <div class="panel-body panel-body-table">
                                    
                                    <div class="table-responsive pre-scrollable"  style="min-height: 477px">
                                        <table class="table table-bordered table-striped">
                                            <thead>
                                                <tr>
                                                    <th width="50%">Project</th>
                                                    <th width="20%">Status</th>
                                                    <th width="30%">Activity</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:if test="${not empty projectDetails}">
	                        						<c:forEach var="project" items="${projectDetails}">
	                        						 <tr>
	                        						 <c:set var="projectState" value="info"/>
	                                                    <c:if test="${project.status == 'COMPLETED' }">
	                                                     	<c:set var="projectState" value="success"/>
	                                                    </c:if>
	                                                    <c:if test="${project.projectMissedTimeLine == 'true' }">
	                                                    	<c:set var="projectState" value="danger"/>
	                                                    </c:if>
                                                    	<td><strong><a href="${applicationHome}/project/${project.id}">${project.name}</a></strong></td>
	                                                    <td><span class="label label-${projectState}">${project.status }</span></td>
	                                                    <td  class="project_progress">
	                                                    
	                                                        <div class="progress progress-small progress-striped active">
	                                                            <div class="progress-bar progress-bar-${projectState}" role="progressbar" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width: ${project.projectCompletionPercentage}%;"></div>
	                                                             <small>${project.projectCompletionPercentage}% Complete</small>
	                                                        </div>
	                                                    </td>
                                               		 </tr>
	                        						</c:forEach>
	                        						</c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                    
                                </div>
                            </div>
                            <!-- END PROJECTS BLOCK -->
                        </div>
                        </div>
                        </div>
                     <!-- START CONTENT FRAME RIGHT -->
                    <div class="row">
                    <div class="col-md-3">
                    <div class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="panel-title-box">
                                        <h3>Employees</h3>
                                        <span>Employees Status</span>
                                    </div>                                    
                                    <ul class="panel-controls" style="margin-top: 2px;">
                                        <li><a href="#" class="panel-fullscreen"><span class="fa fa-expand"></span></a></li>
                                        <li><a href="#" class="panel-refresh"><span class="fa fa-refresh"></span></a></li>
                                        <li class="dropdown">
                                            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="fa fa-cog"></span></a>                                        
                                            <ul class="dropdown-menu">
                                                <li><a href="#" class="panel-collapse"><span class="fa fa-angle-down"></span> Collapse</a></li>
                                                <li><a href="#" class="panel-remove"><span class="fa fa-times"></span> Remove</a></li>
                                            </ul>                                        
                                        </li>                                        
                                    </ul>
                                </div>
                        
                        <div class="panel-body list-group list-group-contacts border-bottom push-down-10 pre-scrollable"  style="min-height: 467px">
                        	<c:if test="${not empty userMap}">
             					<c:forEach var="user" items="${userMap}" varStatus="userIndex"> 
             					 <c:if test="${user.role != 'SUPERADMIN'}">
		                            <a href="#" class="list-group-item">                                 
		                                <div class='list-group-status 
		                                <c:if test="${user.checkUserStatus == 5}">
		                                status-online
		                                </c:if>
		                                <c:if test="${user.checkUserStatus == 1}">
		                                status-away
		                                </c:if>
		                                 <c:if test="${user.checkUserStatus == 0}">
		                                status-offline
		                                </c:if>
		                                '></div>
		                                <img class="pull-left" src="${applicationHome}/image/${user.id}" alt="${user.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
		                                <span class="contacts-title">${user.firstName}</span>
		                                <p>Availble after 2:00 PM</p>
		                            </a> 
		                            </c:if>    
                            	</c:forEach>
                            </c:if>                           
                        </div>
                        
                        <div class="block hide">
                            <h4>Status</h4>
                            <div class="list-group list-group-simple">                                
                                <a href="#" class="list-group-item"><span class="fa fa-circle text-success"></span> Online</a>
                                <a href="#" class="list-group-item"><span class="fa fa-circle text-warning"></span> Away</a>
                                <a href="#" class="list-group-item"><span class="fa fa-circle text-muted"></span> Offline</a>                                
                            </div>
                        </div>
                        
                    </div>
                        </div>
                    </div>
                     <div class="row">
                        <div class="col-md-12">
                     <div class="row">
                        <div class="col-md-6">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="panel-title-box">
                                        <h3>Team Utilization</h3>
                                        <span>Billable/NonBillable</span>
                                    </div>                                    
                                    <ul class="panel-controls" style="margin-top: 2px;">
                                        <li><a href="#" class="panel-fullscreen"><span class="fa fa-expand"></span></a></li>
                                        <li><a href="#" class="panel-refresh"><span class="fa fa-refresh"></span></a></li>
                                        <li class="dropdown">
                                            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="fa fa-cog"></span></a>                                        
                                            <ul class="dropdown-menu">
                                                <li><a href="#" class="panel-collapse"><span class="fa fa-angle-down"></span> Collapse</a></li>
                                                <li><a href="#" class="panel-remove"><span class="fa fa-times"></span> Remove</a></li>
                                            </ul>                                        
                                        </li>                                        
                                    </ul>                                    
                                </div>                                
                                <div class="panel-body padding-0">
                                    <div class="chart-holder" id="dashboard-bar-team" style="height: 200px;"></div>
                                </div>                                    
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="panel-title-box">
                                        <h3>Type of Work</h3>
                                        <span></span>
                                    </div>                                    
                                    <ul class="panel-controls" style="margin-top: 2px;">
                                        <li><a href="#" class="panel-fullscreen"><span class="fa fa-expand"></span></a></li>
                                        <li><a href="#" class="panel-refresh"><span class="fa fa-refresh"></span></a></li>
                                        <li class="dropdown">
                                            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="fa fa-cog"></span></a>                                        
                                            <ul class="dropdown-menu">
                                                <li><a href="#" class="panel-collapse"><span class="fa fa-angle-down"></span> Collapse</a></li>
                                                <li><a href="#" class="panel-remove"><span class="fa fa-times"></span> Remove</a></li>
                                            </ul>                                        
                                        </li>                                        
                                    </ul>                                    
                                </div>                                
                                <div class="panel-body padding-0">
                                    <div class="chart-holder" id="dashboard-bar-work" style="height: 200px;"></div>
                                </div>                                    
                            </div>
                        </div>
                        </div>
                     </div>
                        </div>
                    
                    <!-- END CONTENT FRAME RIGHT -->
                    <div class="row">
						<div class="col-md-12">
                            <!-- START SALES BLOCK -->
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="panel-title-box">
                                        <h3>Clients</h3>
                                        <span>Project completion status</span>
                                    </div>                                     
                                </div>
                                <div class="panel-body">                                    
                                    <div class="row stacked">
                                        <div class="col-md-4">  
                                        	<c:if test="${not empty clientProject }">
                                        		<c:forEach var="clientProject" items="${clientProject}">
		                                        	<div class="progress-list">                                               
		                                                <div class="pull-left"><strong>${clientProject.clientName}: ${clientProject.completedCount} out of ${clientProject.noCompletedCount} completed</strong></div>
		                                                <div class="pull-right">${clientProject.percentageOfCompletion}%</div>                                                
		                                                <div class="progress progress-small progress-striped active">
		                                                    <div class="progress-bar progress-bar-primary" role="progressbar" aria-valuenow="${clientProject.percentageOfCompletion}" aria-valuemin="0" aria-valuemax="100" style="width:${clientProject.percentageOfCompletion}%;">${clientProject.percentageOfCompletion}%</div>
		                                                </div>
                                            		</div>
                                        		</c:forEach>
                                        	</c:if>
                                            <p><span class="fa fa-warning"></span> Data update in end of each hour. You can update it manual by pressign update button</p>
                                        </div>
                                        <div class="col-md-8">
                                            <div id="dashboard-map-seles" style="width: 100%; height: 200px"></div>
                                        </div>
                                    </div>                                    
                                </div>
                            </div>
                            <!-- END SALES BLOCK -->
                            
                        </div>
                        
                        <div class="col-md-4">
                            
                            <!-- START SALES & EVENTS BLOCK -->
                           
                            <!-- END SALES & EVENTS BLOCK -->
                            
                        </div>
                    </div>
                    
                    
						</div>
						
                    </div>
                </div>
               <!-- END PAGE CONTENT WRAPPER -->  
<%@include file="includes/footer.jsp" %>
<jsp:useBean id="date" class="java.util.Date"/>
<fmt:formatDate var="timeHour" value="${date}" pattern="HH"/>
<fmt:formatDate var="timeMinutes"  value="${date}" pattern="mm"/>

<script>
templatePlugins.init(${timeHour},${timeMinutes});

$(function(){        
    /* reportrange */
    if($("#reportrange").length > 0){   
        $("#reportrange").daterangepicker({                    
            ranges: {
               'Today': [moment(), moment()],
               'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
               'Last 7 Days': [moment().subtract(6, 'days'), moment()],
               'Last 30 Days': [moment().subtract(29, 'days'), moment()],
               'This Month': [moment().startOf('month'), moment().endOf('month')],
               'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
            },
            opens: 'left',
            buttonClasses: ['btn btn-default'],
            applyClass: 'btn-small btn-primary',
            cancelClass: 'btn-small',
            format: 'MM.DD.YYYY',
            separator: ' to ',
            startDate: moment().subtract('days', 0),
            endDate: moment()            
          },function(start, end) {
              $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
        });
        
        $("#reportrange span").html(moment().subtract('days', 0).format('MMMM D, YYYY') + ' - ' + moment().format('MMMM D, YYYY'));
    }
 
    
    /* Donut dashboard chart */
    Morris.Donut({
        element: 'dashboard-donut-1',
        data:  ${projectTypeJson},
        colors: ['#5cb85c',
                 '#f0ad4e'],
        resize: true
    });
    /* END Donut dashboard chart */
	
	
    /* Bar dashboard chart emp */
    Morris.Bar({
        element: 'dashboard-bar-emp',
        data: ${employeeUtilization},
        xkey: 'userId',
        ykeys: ['billableHours', 'productiveHours'],
        labels: ['Billable', 'Productive'],
        barColors: ['#33414E', '#1caf9a'],
        gridTextSize: '10px',
        hideHover: true,
        resize: true,
        gridLineColor: '#E5E5E5'
    });
    
    /* Bar dashboard chart emp */
    Morris.Bar({
        element: 'dashboard-bar-team',
        data:${teamUtilizationJson},
        xkey: 'name',
        ykeys: ['billable', 'nonBillable'],
        labels: ['Billable', 'NonBillable'],
        barColors: ['#33414E', '#1caf9a'],
        gridTextSize: '10px',
        hideHover: true,
        resize: true,
        gridLineColor: '#E5E5E5'
    });
    
    /* Bar dashboard chart emp */
    Morris.Bar({
        element: 'dashboard-bar-work',
        data: ${projectCategoryJson},
        xkey: 'categoryName',
        ykeys: ['count'],
        labels: ['Project Type'],
        barColors: ['#33414E', '#1caf9a'],
        gridTextSize: '10px',
        hideHover: true,
        resize: true,
        gridLineColor: '#E5E5E5'
    });
    var jvm_wm = new jvm.WorldMap({container: $('#dashboard-map-seles'),
        map: 'world_mill_en', 
        backgroundColor: '#FFFFFF',                                      
        regionsSelectable: true,
        regionStyle: {selected: {fill: '#B64645'},
                        initial: {fill: '#33414E'}},
        markerStyle: {initial: {fill: '#1caf9a',
                       stroke: '#1caf9a'}},
        markers: [{latLng: [50.27, 30.31], name: 'Microsoft'},                                              
                  {latLng: [52.52, 13.40], name: 'Google'},
                  {latLng: [48.85, 2.35], name: 'Cisco'},                                            
                  {latLng: [51.51, -0.13], name: 'HP'}]
    });    
    /* END Vector Map */

    
    $(".x-navigation-minimize").on("click",function(){
        setTimeout(function(){
            rdc_resize();
        },200);    
    });
    
    
});


</script>