<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>    
<c:set var="fsVersionNumber" value="${applicationScope.applicationConfiguraion.fsVersionNumber}"/>
<link rel="stylesheet" type="text/css" href="${fn:escapeXml(css)}/pages/myTaskList.css?v=${fsVersionNumber}"/>
<style>
/* Panel - Activity Stream
---------------------------*/
.activity-stream ul.list-stream,
.panel-comment ul.list-stream,
.activity-stream ul.list-comment,
.panel-comment ul.list-comment {
  margin-top: -15px;
  margin-bottom: -15px;
}
.activity-stream ul.list-stream .list-stream-item,
.panel-comment ul.list-stream .list-stream-item,
.activity-stream ul.list-comment .list-stream-item,
.panel-comment ul.list-comment .list-stream-item {
    /* border-left: 1px solid #e1e3e4; */
    padding-top: 5px;
    padding-bottom: 5px;
    padding-right: 5px;
    margin-left: 5px;
    margin-right: 0px;
    border-top: 1px solid #e1e3e4;
}
}
.activity-stream ul.list-stream .list-stream-item:first-child,
.panel-comment ul.list-stream .list-stream-item:first-child,
.activity-stream ul.list-comment .list-stream-item:first-child,
.panel-comment ul.list-comment .list-stream-item:first-child {
  border-top: 0;
}
.activity-stream ul.list-stream .list-stream-item.minified .media-body,
.panel-comment ul.list-stream .list-stream-item.minified .media-body,
.activity-stream ul.list-comment .list-stream-item.minified .media-body,
.panel-comment ul.list-comment .list-stream-item.minified .media-body {
  display: inline-block;
}
.activity-stream ul.list-stream .list-stream-item.minified p,
.panel-comment ul.list-stream .list-stream-item.minified p,
.activity-stream ul.list-comment .list-stream-item.minified p,
.panel-comment ul.list-comment .list-stream-item.minified p {
  width: 80%;
  float: left;
}
.activity-stream ul.list-stream .list-stream-item.load .list-stream-icon,
.panel-comment ul.list-stream .list-stream-item.load .list-stream-icon,
.activity-stream ul.list-comment .list-stream-item.load .list-stream-icon,
.panel-comment ul.list-comment .list-stream-item.load .list-stream-icon {
  margin-top: -5px;
}
.activity-stream ul.list-stream .list-stream-icon,
.panel-comment ul.list-stream .list-stream-icon,
.activity-stream ul.list-comment .list-stream-icon,
.panel-comment ul.list-comment .list-stream-icon {
  margin-left: -13px;
  -webkit-border-radius: 50px;
  -moz-border-radius: 50px;
  border-radius: 50px;
  background: #7f8c8d;
  color: #ffffff;
  width: 25px;
  height: 25px;
  text-align: center;
}
.activity-stream ul.list-stream .list-stream-icon i,
.panel-comment ul.list-stream .list-stream-icon i,
.activity-stream ul.list-comment .list-stream-icon i,
.panel-comment ul.list-comment .list-stream-icon i {
  line-height: 25px;
  font-size: 13px;
}
.activity-stream .media,
.panel-comment .media {
  margin: 0 !important;
  padding-left: 20px;
  font-family: 'Karla', sans-serif;
}
.activity-stream .media .media-heading,
.panel-comment .media .media-heading {
  margin: 0 !important;
  font-size: 14px;
  font-weight: 400;
  padding: 5px 0;
  font-family: 'Montserrat', sans-serif;
}
.activity-stream .media .media-heading a,
.panel-comment .media .media-heading a {
  color: #e74c3c;
}
.activity-stream .media .media-heading a:hover,
.panel-comment .media .media-heading a:hover {
  color: #d62c1a;
}
.activity-stream .media .media-body,
.panel-comment .media .media-body {
  width: 100%;
  margin: 0 !important;
}
.activity-stream .media .meta-time,
.panel-comment .media .meta-time {
  font-weight: 400;
  font-family: 'Montserrat', sans-serif;
  color: rgba(68, 76, 82, 0.6);
  font-size: 12px;
  margin-bottom: 10px;
  position: absolute;
  right: 20px;
}
.activity-stream .media .media-avatar,
.panel-comment .media .media-avatar {
  overflow: hidden;
  text-align: center;
  border-radius: 3px;
  background: #ffffff;
  margin-right: 15px;
}
.activity-stream .media .media-avatar img,
.panel-comment .media .media-avatar img {
  width: 60px;
  -webkit-border-radius: 3px;
  -moz-border-radius: 3px;
  border-radius: 3px;
}
.activity-stream .media .media-avatar.mini img,
.panel-comment .media .media-avatar.mini img {
  width: 23px;
  height: 23px;
}
.activity-stream .media p,
.panel-comment .media p {
  margin-bottom: 0;
  line-height: 20px;
}
.activity-stream .media .grouped-avatar,
.panel-comment .media .grouped-avatar {
  margin-top: 10px;
}
.activity-stream .media .grouped-avatar .media-avatar,
.panel-comment .media .grouped-avatar .media-avatar {
  width: 20px;
  height: 20px;
  margin-right: 5px;
}
.activity-stream .media .loadmore,
.panel-comment .media .loadmore {
  position: relative;
  top: 1px;
  font-family: 'Montserrat', sans-serif;
  text-transform: uppercase;
  font-size: 11px;
}
</style>

<!-- END PAGE TITLE -->                
<div class="content-frame" style="min-height: 500px; margin-bottom: 50px">     
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-tasks"></span> Task Activities</h2>
        </div>                                                
    </div> 
<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<form class="form-horizontal">
						<div class="form-group">
							<div class="col-md-5">
								<div class="input-group">
									<div class="input-group-addon">
										<span class="fa fa-search"></span>
									</div>
									<input type="text" class="form-control" id="taskActivitySearchId" placeholder="Search for a task activity">
								</div>
							</div>
							 <div class="col-md-2">
							  <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
								<select id="taskActivityAssigneeId" name="taskAssigneeId" class="form-control select" data-live-search="true">
									<option value="0">All</option>
										 <c:if test="${not empty userMap}">
											<c:forEach var="user" items="${userMap}">
												 <c:if test="${currentUser.id eq user.id}">
												 	<option selected="selected" value="userId${user.id}">${user.firstName}</option>
												 </c:if>
												  <c:if test="${currentUser.id ne user.id}">
												  <option value="userId${user.id}">${user.firstName}</option>
												  </c:if>
							  				</c:forEach>
							  			</c:if>
									</select>
								</c:if>
								<c:if test="${!(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD')}">
									<input type="hidden" id="taskActivityAssigneeId" value="${currentUser.id}">
								</c:if>
							</div>
							<div class="col-md-1"> 
								<div class="form-group">
                     	 		<input type='text' class="form-control taskActivityMonthSelector" id='' autocomplete="off"/>
                            </div>
							</div>
							<div class="col-md-1"> 
                            </div>
							<div class="col-md-3">
								  
								  <a data-toggle="modal" data-target="#unbillableTaskCreationModal" onclick="clearUnbillableFormForCreate(${currentUser.id})"
									class="btn btn-success btn-block"> <span class="fa fa-plus"></span>
									Record Non-billable Time.
									</a>
								
							</div>
						</div>
					</form>
				</div>
			</div>

		</div>
<!-- PAGE CONTENT WRAPPER -->
<div class="page-content-wrap">
    <div class="row">
        <div class="col-md-12">
				<div class="panel panel-default panel-stream">
					<div class="panel-body">
						<div class="activity-stream">
								
						
							<ul class="list-unstyled list-stream">
							
								<li class="list-stream-item">
									<div class="list-stream-icon pull-left bg-primary">
										<i class="fa fa-tint"></i>
									</div>
									<div class="media">
										<div class="media-body">
											<h4 class="media-heading">
												<a href="#">Task Activity name</a>
											</h4>
											<p>
												Project: <a href="#">This is Awesome Project</a>
												Project Type: <a href="#">This is Awesome Project</a>
											</p>
											<p>
												Task: <a href="#">This is Awesome Project</a>
											</p>
										</div>
										<a class="media-avatar mini media-left" href="#">
											<img class="media-object assignee" src="/bops/dashboard/image/192" onerror="this.src='/bops/jsp/assets/images/users/no-image.jpg'"/>
										</a>
									</div>
								</li>
								<li class="list-stream-item">
									<div class="list-stream-icon pull-left bg-info">
										<i class="fa fa-comments"></i>
									</div>
									<div class="media">
										<a class="media-avatar mini media-left" href="#">
											<img class="media-object" src="img/uif-2.jpg" alt="...">
										</a>
										<div class="media-body">
											<h4 class="media-heading">
												<a href="#">Steve Woz</a>
												<span class="meta-time">20 hours ago</span>
											</h4>
											<p>
												Commented on: <a href="#">This is Awesome Project</a>
											</p>
										</div>
									</div>
								</li>
								<li class="list-stream-item minified load">
									<div class="list-stream-icon pull-left">
										<i class="fa fa-repeat"></i>
									</div>
									<div class="media">
										<div class="media-body">
											<a href="#" class="loadmore">Load More</a>
										</div>
									</div>
								</li>
							</ul>
						</div>
					</div>
				</div>
        </div>
    </div>
</div>
</div>
<%@include file="includes/footer.jsp" %>     
 <script type='text/javascript' src="${js}/plugins/datepicker/bootstrap-datetimepicker_new.min.js?v=${fsVersionNumber}"></script>       
<script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/unbilledtask.js?v=${fsVersionNumber}"></script>
<script>

$(document).ready(function(){
	reloadTaskActivities();
});

var activeUserId = "";

$(".taskOwnersList").on("click", function(){
	var hasClass = $("#" + this.id).hasClass("active");
	$(".taskOwnersList").removeClass("active");
	if (!hasClass) {
		$("#" + this.id).addClass("active");
		$(".taskact-item").hide();
		$(".taskact-item." + this.id).show();
		activeUserId=this.id;
	} else {
		$(".taskact-item").show();
		activeUserId="";
	}
	performSearch();
});


$("#taskAssigneeId").on("change", function(){
	var assigneeSelectionId = $(this).val();
	filterTaskActivities(assigneeSelectionId);
});

function filterTaskActivities(assigneeSelectionId){
	if (assigneeSelectionId != "") {
		$(".taskact-item").hide();
		$(".taskact-item." + assigneeSelectionId).show();
		activeUserId=assigneeSelectionId;
	} else {
		$(".taskact-item").show();
		activeUserId="";
	}
	performSearch();
}


function performSearch(){
	var serarchText = $('#taskActivitySearchId').val();
	famstacklog(serarchText);
	var searchId = ".taskact-item";

	if (activeUserId != "") {
		searchId+="."+activeUserId;
	}
	
	if (serarchText != "") {
		$('.taskact-item').hide();
	    $(searchId).each(function(){
	       if($(this).text().toUpperCase().indexOf(serarchText.toUpperCase()) != -1){
	           $(this).show();
	       }
	    });
	} else {
		$(searchId).show();
	}
}

$('#taskActivitySearchId').keydown(function(){
	performSearch();
});

$('#taskActivitySearchId').keyup(function(){
	performSearch();
});

$(document).ready(function(){
	/* MESSAGE BOX */
	$(document).on("click",".deleteTask",function(){
	    var box = $($(this).data("box"));
	    if(box.length > 0){
	        box.toggleClass("open");
	        
	        var sound = box.data("sound");
	        
	        if(sound === 'alert')
	            playAudio('alert');
	        
	        if(sound === 'fail')
	            playAudio('fail');
	        
	    }        
	    return false;
	});
	$(document).on("click",".mb-control-close",function(){
	   $(this).parents(".message-box").removeClass("open");
	   return false;
	});    
	/* END MESSAGE BOX */
});

$("#taskActivityAssigneeId").on("change", function(){
	reloadTaskActivities();
});

$(".taskActivityMonthSelector").on("change", function(){
	reloadTaskActivities();
});
function refreshCalendar(){
	reloadTaskActivities();
}
$('.taskActivityMonthSelector').datetimepicker({ 
	sideBySide: true, format: 'MMM-YYYY',
	useCurrent: true,
	defaultDate:new Date()
	}).on('dp.change', function(e) {
		reloadTaskActivities();
	});
</script>
