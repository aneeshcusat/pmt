<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="includes/header.jsp" %>
<c:set var="staticFilesLocation" value="${applicationScope.applicationConfiguraion.staticFilesLocation}"/>
<c:set value="${staticFilesLocation}/css" var="css"/>
<c:set value="${staticFilesLocation}/assets" var="assets"/>
<c:set value="${staticFilesLocation}/image" var="image"/>
<c:set value="${staticFilesLocation}/js" var="js"/>
<c:set value="${staticFilesLocation}/audio" var="audio"/>
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
<link rel="stylesheet" type="text/css" href="${fn:escapeXml(css)}/gantt/dhtmlxganttp.css?v=6.1.5">
<link rel="stylesheet" type="text/css" href="${fn:escapeXml(css)}/gantt/dhtmlxgantt_material.css?v=6.1.6">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${fn:escapeXml(css)}/gantt/controls_stylesp.css?v=6.1.5">
<style>
::-webkit-scrollbar {
    width: 7px;
}
.gantt_control {
    background: #fff;
    text-align: left;
}		
#gantt_here {
	width:100%;
	min-height: 600px;
}

.gantt_grid_scale .gantt_grid_head_cell,
.gantt_task .gantt_task_scale .gantt_scale_cell {
	font-weight: bold;
	font-size: 10px;
	color: rgba(0, 0, 0, 0.7);
}

.resource_marker{
	text-align: center;
}
.resource_marker div{
	width: 28px;
	height: 28px;
	line-height: 29px;
	display: inline-block;

	color: #FFF;
	margin: 3px;
}
.resource_marker.workday_ok div {
	border-radius: 15px;
	background: #51c185;
}

.resource_marker.workday_over div{
	border-radius: 3px;
	background: #ff8686;
}

.folder_row {
	font-weight: bold;
}

.highlighted_resource,
.highlighted_resource.odd
{
	background-color: rgba(255, 251, 224, 0.6);
}

.resource-controls .gantt_layout_content{
	padding: 7px;
	overflow: hidden;
}
.resource-controls label{
	margin: 0 10px;
	vertical-align: bottom;
	display: inline-block;
	color: #3e3e3e;
	padding: 2px;
	transition: box-shadow 0.2s;
}

.resource-controls label:hover{
	box-shadow: 0 2px rgba(84, 147, 255, 0.42);
}

.resource-controls label.active,
.resource-controls label.active:hover
{
	box-shadow: 0 2px #5493ffae;
	color: #1f1f1f;
}

.resource-controls input{
	vertical-align: top;
}

.gantt_task_cell.week_end {
	background-color: #e8e8e87d;
}

.gantt_task_row.gantt_selected .gantt_task_cell.week_end {
	background-color: #e8e8e87d !important;
}


.group_row,
.group_row.odd,
.gantt_task_row.group_row{
	background-color: rgba(232, 232, 232, 0.6);
}

.material-icons {
    top: 5px;
}

.material-icons {
    font-size: 20px;
}

.weekend{ 
	background: #ddd !important;
	color:#f00 !important;
}
.projectTask{
	background-color: #8cb5f9 !important;
	border: 1px solid #215ec3 !important;
	height: 20px !important;
    line-height: 20px !important;
    border-radius:8px  !important;
}
.taskTask{
	background-color: #d3d3de !important;
	border: 1px solid #9cbaea !important;
	height: 20px !important;
    line-height: 20px !important;
    border-radius:8px  !important;
}

.projectTask .gantt_task_content {
    font-size: 12px !important;
    color: #4a4444 !important;
}
.taskTask .gantt_task_content {
     font-size: 12px !important;
    color: #4a4444 !important;
}
</style>
<!-- START CONTENT FRAME -->
<div class="content-frame margin5" style="min-height: 500px">
	<div class="content-frame-top">
		<div class="page-title">
			<h2>
				<span class="fa fa-list-alt"></span> Project Timeline
			</h2>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">

			<div class="panel panel-default">
				<div class="panel-body" style="padding: 0px;">
					<form class="gantt_control scaleControl" style="padding:0px">
						<!-- <input type="radio" id="scale0" class="gantt_radio" name="scale" value="0"/><label for="scale0" name="scalelabel"><i class="material-icons">radio_button_unchecked</i>Minute scale</label>
						 --><input type="radio" id="scale1" class="gantt_radio" name="scale" value="1"/><label for="scale1" name="scalelabel"><i class="material-icons">radio_button_unchecked</i>Hour scale</label>
						<input type="radio" id="scale2" class="gantt_radio scaleButton" name="scale" value="2" checked/><label  name="scalelabel" class="checked_label" for="scale2"><i class="material-icons icon_color">radio_button_checked</i>Day scale</label>
						<input type="radio" id="scale3" class="gantt_radio scaleButton" name="scale" value="3"/><label for="scale3"  name="scalelabel"><i class="material-icons">radio_button_unchecked</i>Week scale</label>
						<input type="radio" id="scale4" class="gantt_radio scaleButton" name="scale" value="4"/><label for="scale4"  name="scalelabel"><i class="material-icons">radio_button_unchecked</i>Month scale</label>
						<input type="radio" id="scale5" class="gantt_radio scaleButton" name="scale" value="5"/><label for="scale5"  name="scalelabel"><i class="material-icons">radio_button_unchecked</i>Quarter scale</label>
						<input type="radio" id="scale6" class="gantt_radio scaleButton" name="scale" value="6"/><label for="scale6"  name="scalelabel"><i class="material-icons">radio_button_unchecked</i>Year scale</label>
						<a type="button" id="default" style="padding: 10px 15px 0 15px;" onclick="toggleGroups(this)">Resource View</i></a>
						<a type=button class="showResourceChart" onclick="layoutChange(true)">Toggle Resource Chart</i></a>

						 <span id="reportrange" class="dtrange dateFilterDiv" style="float:right">                            
          					<span></span><b class="caret"></b>
          					<input type="hidden" id="daterangeText" value="hello" /> 
      					 </span>
      					 <input style="margin-left:10px;float:right" class="btn btn-default ganttReload" type="button" value="Reload" onclick="loadGantt()"></input>
					</form>
				</div>
			</div>

		
		</div>
	</div>
<div id="gantt_here">
</div>
</div>
<!-- END CONTENT FRAME -->
 <%@include file="includes/footer.jsp" %> 
<script src="${js}/plugins/gantt/dhtmlxganttp.js?v=6.1.5"></script>
	<script src="${js}/plugins/gantt/dhtmlxgantt_grouping.js"></script>
	<script src="${js}/plugins/gantt/dhtmlxgantt_grouping.js"></script>
	<script src="${js}/plugins/gantt/dhtmlxgantt_auto_scheduling.js"></script>
<%-- 		<script src="${js}/plugins/gantt/dhtmlxgantt_quick_info.js?v=6.1.5"></script>
 --%>	<script src="${js}/plugins/gantt/dhtmlxgantt_tooltip.js?v=6.1.5"></script>
<script>
function loadGantt() {
  doAjaxRequest("GET", fsApplicationHome+"/projectTimeLineJson", {"daterange" : $("#daterangeText").val()}, function(data) {
    famstacklog("SUCCESS: ", data);
    //var resourceData =JSON.stringify(JSON.parse(data).resource);
    var projectData =JSON.stringify(JSON.parse(data).data);
    gantt.init("gantt_here");
    gantt.parse(JSON.stringify(JSON.parse("{\"data\":" + projectData + "}")));
    gantt.setSizes();
    gantt.$resourcesStore.parse(JSON.parse(data).resource);
}, function(e) {
    famstacklog("ERROR: ", e);
    famstackalert(e);
});
}
loadGantt();
/* 
var demo_tasks = {
		  "data": [
			{ "id": 2, "text": "Office itinerancy", "start_date": "02-04-2019 06:00", "duration": 17, "progress": 0.4, "owner_id": "5", "parent": 0},
			{ "id": 5, "text": "Interior office", "type": "task", "editable":false, "start_date": "03-04-2019 00:00", "duration": 7, "parent": "2", "progress": 0.6, "owner_id": "6", "priority":1},
			{ "id": 7, "text": "Workplaces preparation", "type": "task", "start_date": "12-04-2019 00:00", "duration": 8, "parent": "3", "progress": 0.6, "owner_id": "10"},
			{ "id": 8, "text": "Preparing workplaces", "type": "task", "start_date": "14-04-2019 00:00", "duration": 5, "parent": "4", "progress": 0.5, "owner_id": "9", "priority":1},
			{ "id": 9, "text": "Workplaces importation", "type": "task", "start_date": "21-04-2019 00:00", "duration": 4, "parent": "4", "progress": 0.5, "owner_id": "7"},
			{ "id": 10, "text": "Workplaces exportation", "type": "task", "start_date": "27-04-2019 00:00", "duration": 3, "parent": "4", "progress": 0.5, "owner_id": "8", "priority":2},
			{ "id": 11, "text": "Product launch", "type": "project", "progress": 0.6, "start_date": "02-04-2019 00:00", "duration": 13, "owner_id": "5", "parent": 0},
			{ "id": 12, "text": "Perform Initial testing", "type": "task", "start_date": "03-04-2019 00:00", "duration": 5, "parent": "11", "progress": 1, "owner_id": "7"},
			{ "id": 16, "text": "Documentation creation", "type": "task", "start_date": "03-04-2019 00:00", "duration": 7, "parent": "11", "progress": 0, "owner_id": "7", "priority":1},
			{ "id": 24, "text": "Release v1.0", "type": "milestone", "start_date": "20-04-2019 00:00", "parent": "11", "progress": 0, "owner_id": "5", "duration": 0}

		  ],
		  "links": [
		  ]
		};
 */
</script>
<script src="${js}/ganttInitializer.js"></script>
<script src="${js}/ganttScaleInitializer.js"></script>
<script>
//gantt.getTask(5).readonly = true;
 
</script>


</body></html>