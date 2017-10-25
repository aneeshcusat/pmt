<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>           
<style>
.list-group.milestone-list .list-group-item {
  margin: 0 0 15px 0;
  padding: 0;
  -webkit-border-radius: 3px;
  -moz-border-radius: 3px;
  border-radius: 3px;
}
.list-group.milestone-list .milestone-date {
  display: block;
  text-align: center;
  padding: 0;
}
.list-group.milestone-list .milestone-date .milestone-date-container {
  position: relative;
  top: 50%;
  transform: translateY(50%);
  -webkit-transform: translateY(50%);
  line-height: 21px;
}
.list-group.milestone-list .milestone-date span {
  display: block;
}
.list-group.milestone-list .milestone-date .milestone-day {
  font-size: 30px;
  margin-bottom: 10px;
}
.list-group.milestone-list .milestone-date .milestone-date-item ul li {
  line-height: 14px;
  text-transform: uppercase;
  font-size: 13px;
}
.list-group.milestone-list .milestone-edit {
  position: absolute;
  right: 10px;
  top: 10px;
  z-index: 1000;
}
.list-group.milestone-list .milestone-edit a {
  opacity: .3;
}
.list-group.milestone-list .milestone-edit a:hover {
  opacity: 1;
}
.list-group.milestone-list .milestone-edit .dropdown-menu {
  min-width: 132px;
  top: 27px;
  right: -17px;
}
.list-group.milestone-list .milestone-edit .dropdown-menu li a {
  color: #3c4242;
  font-family: 'Karla', sans-serif;
  line-height: 25px;
  opacity: 1;
  border-left: 1px solid #dfe7ea;
  display: block;
  padding: 10px 15px;
}
.list-group.milestone-list .milestone-edit .dropdown-menu li:first-child a {
  border-left: 0;
}
.list-group.milestone-list .milestone-edit .dropdown-menu:after {
  right: 9%;
}
.list-group.milestone-list .milestone-edit .checkbox {
  margin: 0;
  padding: 12px 15px;
  -webkit-border-radius: 0 0 0 3px;
  -moz-border-radius: 0 0 0 3px;
  border-radius: 0 0 0 3px;
}
.list-group.milestone-list .milestone-edit .checkbox label {
  padding: 0;
  height: auto;
  min-height: inherit;
}
.list-group.milestone-list .milestone-edit .checkbox label .button-checkbox {
  top: -1px;
  position: relative;
}
.list-group.milestone-list .milestone-edit .checkbox .bootstrap-checkbox > .btn {
  padding: 0;
}
.list-group.milestone-list .milestone-edit .checkbox:hover {
  background: #f6f7f7;
}
.list-group.milestone-list .milestone-item {
  padding: 20px;
  border-left: 1px solid #dfe7ea;
}
.list-group.milestone-list .milestone-item .milestone-title {
  margin: 0 0 2px 0;
  font-size: 20px;
}
.list-group.milestone-list .milestone-item .task-meta {
  margin-bottom: 10px;
  font-family: 'Karla', sans-serif;
  color: rgba(62, 84, 101, 0.7);
}
.list-group.milestone-list .milestone-item .task-meta p {
  margin-top: 5px;
  color: rgba(62, 84, 101, 0.9);
}
.list-group.milestone-list .milestone-item .task-date {
  font-weight: 700;
}
.list-group.milestone-list .milestone-item .progress {
  margin-bottom: 0;
}

.milestone-progress,
.milestone-progress-single {
  padding-top: 20px;
  padding-bottom: 20px;
  color: #7f8c8d;
  font-weight: 400;
}
.milestone-progress ul,
.milestone-progress-single ul {
  text-align: center;
}
.milestone-progress ul li,
.milestone-progress-single ul li {
  border-left: 1px solid #dfe7ea;
  padding: 17px 15px 13px;
  line-height: 21px;
  width: 32%;
}
.milestone-progress ul li:first-child,
.milestone-progress-single ul li:first-child {
  border-left: 0;
}
.milestone-progress span,
.milestone-progress-single span {
  display: block;
  font-size: 20px;
  font-weight: 700;
  color: #3e5465;
}
.milestone-progress-single ul {
  margin-bottom: 0;
}
.panel-milestone-progress {
  padding: 20px !important;
}
.panel-milestone-progress .progress {
  height: 42px;
}
.panel-milestone-progress .progress .progress-bar {
  line-height: 42px;
}

.color-primary {
  color: #2980b9 !important;
}
.color-success {
  color: #27ae60 !important;
}
.color-info {
  color: #3498db !important;
}
.color-warning {
  color: #f39c12 !important;
}
.color-danger {
  color: #c0392b !important;
}
</style>

 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Milestones</li>
 </ul>
 <!-- END BREADCRUMB -->  

<!-- PAGE TITLE -->
<div class="page-title">                    
    <h2><span class="fa fa-flag"></span> Milestones</h2>
</div>
<!-- END PAGE TITLE -->                

<!-- PAGE CONTENT WRAPPER -->
<div class="page-content-wrap">
    
    <div class="row">
        <div class="col-md-12">
<div class="milestone-list-container">
	<ul class="list-group milestone-list row no-margin">
		<c:if test="${not empty modelViewMap.projectDetailsData}">
	        <c:forEach var="project" items="${modelViewMap.projectDetailsData}">
        	<c:if test="${project.status != 'COMPLETED'}">
        <c:set var="projectDayStatus" value="info"/>
        	<c:if test="${project.completionInDays > 0}">
         	<c:set var="projectState" value="warning"/>
        	</c:if>
        	<c:if test="${project.completionInDays < 0 }">
        	<c:set var="projectState" value="danger"/>
        </c:if>
                  
		<li class="list-group-item row">
			<div class="milestone-date col-md-1 col-sm-2">
				<div class="milestone-date-container">
					<span class="milestone-day color-warning"><c:if test="${project.completionInDays >= 0}">${project.completionInDays}</c:if><c:if test="${project.completionInDays < 0}">${project.completionInDays * -1}</c:if></span>
					<span class="milestone-date-item">
						<ul class="list-unstyled">
							<li>day</li>
							<li><c:if test="${project.completionInDays >= 0}">Left</c:if><c:if test="${project.completionInDays < 0}">Ago</c:if></li>
						</ul>
					</span>
				</div>
			</div>
				<c:set var="projectState" value="info"/>
                  <c:if test="${project.status == 'COMPLETED' }">
                   	<c:set var="projectState" value="success"/>
                  </c:if>
                  <c:if test="${project.status == 'NEW' }">
                  </c:if>
                  <c:if test="${project.projectMissedTimeLine == true }">
                  	<c:set var="projectState" value="danger"/>
                  </c:if>
				
				
			<div class="milestone-item col-md-7 col-sm-10">
				<h3 class="milestone-title">
					<a href="${applicationHome}/project/${project.id}">${project.code} | ${project.name}</strong></a>
				</h3>
				<div class="task-meta">
					<span class="">Due Date:</span>
					<span class="task-date color-${projectState}">
					<fmt:parseDate value = "${project.completionTime}" var = "parsedDate" pattern = "yyyy/MM/dd HH:mm"/>
					<fmt:formatDate pattern = "MMM dd YYYY" value = "${parsedDate}"/></span>
				</div>
				
				<div class="progress">
					<div class="progress-bar progress-bar-${projectState}" role="progressbar" aria-valuenow="${project.projectCompletionPercentage}" aria-valuemin="0" aria-valuemax="100" style="width: ${project.projectCompletionPercentage}%;">
				    	${project.projectCompletionPercentage}%
					</div>
				</div>
			
			</div>

			<div class="milestone-progress col-md-4 col-sm-12">
				<ul class="list-inline">
					<li><span>${project.noOfOpenTasks}</span> tasks</li>
					<li><span>${project.noOfTasks}</span> opens</li>
					<li><span><c:if test="${empty project.unAssignedDuration}">0</c:if><c:if test="${not empty project.unAssignedDuration}">${project.unAssignedDuration}</c:if></span> Hours pending</li>
				</ul>
			</div>
		</li>
		</c:if>
		</c:forEach>
		</c:if>
	</ul>
</div>

        </div>
    </div>
</div>